список репозиториев c открытого апи гитхаба (например, компании square), 
отобразить в RecyclerView. 
Элемент списка: Название репозитория, под ним слева направо - 
иконка звезды, 
кол-во звезд на гитхабе, 
иконка форка, 
кол-во контрибуторов (можно добавить что-то на своё усмотрение). 
По клику на айтем списка открывается 
    экран с детальной информацией о репозитории, 
    обязательный контент - 
        кнопка "к контрибуторам" и 
        "кнопка к коммитам", 
    остальное на усмотрение разработчика. 
    По клику на кнопки контрибуторов и коммитов 
    открывается экран со списком контрибуторов и коммитов соответственно.
    
Стэк:  RxJava2, Kotlin, Retrofit, MVP
 
Задание(*): Поддержка поворота экрана 
Задание(**): Сохранение списка репозиториев в базу (Room)
 
-------------------------------------------------------
I. (REST API Github)
-------------------------------------------------------
1. по адресу baseURL + get получил ответ-образец JSON
https://api.github.com/orgs/square/repos

сгенерировал классы через http://www.jsonschema2pojo.org/

прежде чем перейти к работе над представлением RecyclerView,
нужно подготовить все REST IPI запросы к Github'у
и организовать их по Selfcontrol-овскому приниципу

    какие запросы нужны? те, которые обеспечат нам требуемую информацию:
    a. для RecyclerView, т.е. в list-e:
        1. название репозитория //
        2. количество звёзд на гитхабе ★ 
        3. количество контрибуторов 
        //stats/contributors
        GET /repos/:owner/:repo/stats/contributors
        
        https://api.github.com/repos/square/wire/contributors/per_page=1000
            пример с другого приложения: https://api.github.com/repos/tensorflow/tensorflow/contributors?per_page=1
        4. что-то своё на усмотрение (подсмотреть с web-представления листа репозиториев?) //
        
    b. для окна с детальной информацией о репозитории:
        1. кнопка "к контрибуторам" (обязательный контент) //
        2. "кнопка к коммитам" (обязательный контент) //
        3. что-то на своё усмотрение (подсмотреть с web-представления инфы о репозитории?) //
        
    c. для экрана контрибуторов:
        1. список контрибуторов и коммитов //
        
    d. для экрана коммитов:
        1. список коммитов //
-----------------------------------------
RxJava позволяет "объединить" два запроса к API c общими данными в один:
(отсюда: 
https://stackoverflow.com/questions/21890338/when-should-one-use-rxjava-observable-and-when-simple-callback-on-android)

private class Combined {
    UserDetails details;
    List<Photo> photos;
}

    Observable.zip(api.getUserDetails(userId), api.getUserPhotos(userId), new Func2<UserDetails, List<Photo>, Combined>() {
        @Override
        public Combined call(UserDetails details, List<Photo> photos) {
            Combined r = new Combined();
            r.details = details;
            r.photos = photos;
            return r;
        }
    }).subscribe(new Action1<Combined>() {
        @Override
        public void call(Combined combined) {
        }
    });
это аналог для 
        api.getUserDetails(userId, new Callback<UserDetails>() {
            @Override
            public void onSuccess(UserDetails details, Response response) {
                this.details = details;
                if(this.photos != null) {
                    displayPage();
                }
            }
        });

        api.getUserPhotos(userId, new Callback<List<Photo>>() {
            @Override
            public void onSuccess(List<Photo> photos, Response response) {
                this.photos = photos;
                if(this.details != null) {
                    displayPage();
                }
            }
        });

МОЖЕТ БЫТЬ ВОЗМОЖНО ОБЪЕДИНИТЬ ДВА ЗАПРОСА 

---------------------------------------------
LAMBDAS+JAVARX
---------------------------------------------
Observable.just("Hello, world!")
    .map(new Func1<String, String>() {
        @Override
        public String call(String s) {
            return s + " -Dan";
        }
    })
    .subscribe(s -> System.out.println(s));

Observable.just("Hello, world!")
    .map(s -> s + " -Dan")
    .subscribe(s -> System.out.println(s));
    
нтересным свойством map() является то, что он не обязан порождать данные того же самого типа, что и исходный Observable.
Допустим, что наш Subscriber должен выводить не порождаемый текст, а его хэш:

Observable.just("Hello, world!")
    .map(new Func1<String, Integer>() {
        @Override
        public Integer call(String s) {
            return s.hashCode();
        }
    })
    .subscribe(i -> System.out.println(Integer.toString(i)));

Observable.just("Hello, world")
    .map(s -> s.hashCode(s))
    .subscribe(i -> System.out.println(Integer.toString(i)));

Observable.just("Hello, world")
    .map(s -> s.hashCode(s))
    .map(i -> Integer.toString(i))
    .subscribe(s -> System.out.println(s))


------------------------------

private void getMovieListingsWithImages() {
    Observer<MovieResponse> observer = new Observer<MovieResponse>() {
        @Override
        public void onSubscribe(Disposable d) {
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(MovieResponse movieResponse) {
            //for each movie response make a call to the API which provides the image for the movie
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getApplicationContext(), "Error getting image for the movie", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {
            Toast.makeText(getApplicationContext(), "Finished getting images for all the movies in the stream", Toast.LENGTH_SHORT).show();
        }
    };

    getAPI().getRepos()
            // converts your list of movieResponse into and observable which emits one movieResponse object at a time.
            .flatMapIterable(responseList -> mresponseList) 
            // method converts the each movie response object into an observable
            .flatMap(this::getObservableFromString) 
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer);
}


private Observable<MovieResponse> getObservableFromString(MovieResponse movieResponse) {
    return Observable.just(movieResponse);
}

 getAPI().getRepos().concatMap(Observable::from)
            .flatMap(item -> someOtherService.getDetails(item.somedetail), (item, detail) -> new ExtendedItem(item, detail))
            .toList()
...








gerRepos
    .flatMap(new Func1<List<String>, Observable<String>>() {
        @Override
        public Observable<String> call(List<String> urls) {
            return Observable.from(urls);
        }
    })
    .subscribe(repoWithContributors -> addRepo(repoWithContributors));

    
    //MY ATTEMTP!
public void getRepoWithContributors() throws IOException {
        rxRepoApi.getRepoList()
        .flatMap(Observable::from)
        .flatMap((Func1<Repo, Observable<List<Contributor>>>) repo -> rxRepoApi.getContribsList(repo.getName()))
        .flatMap((Func1<List<Contributor>, Observable<RepoWithContributors>>) contributors -> new RepoWithContributors(repo, contributors))
        .subscribe(this::addRepo);
    }



//MY ATTEMTP 2!
 public void getRepoWithContributors() {
        rxRepoApi.getRepoList()
            .flatMap(Observable::from)
            .flatMap(
                repo -> new RepoWithContributors(repo,
                            rxRepoApi.getContribsList(repo.getName())
                                    .map(contributors -> contributors)

                    ))
            .subscribe(this::addRepo());
    }

    
    rxRepoApi.getRepoList()
       .flatMap(repos -> Observable.fromIterable(repos)) //.flatMap(Observable::from)
       .flatMap(
               repo -> rxRepoApi.getContribsList(repo.getName()),
               (repo, contributors) -> {
                   return new RepoWithContributors(repo, contributors);
               })
       .subscribe(user -> saveUser(user));







rxRepoApi.getRepoList()
       .flatMap(new Function<List<Repo>, ObservableSource<List>>() {
           @Override
           public ObservableSource<Repo> apply(List<Repo> repos) throws Exception {
               return Observable.fromIterable(repos);
           }
       })
       .flatMap(
               new Function<Repo, ObservableSource<List<Contributor>>>() {
                   @Override
                   public ObservableSource<List<Contributor>> apply(Repo repo) throws Exception {
                       return rxRepoApi.getContribs(repo.getName());
                   }
               }, new BiFunction<Repo, List<Contributor>, RepoWithContributors>() {
                   @Override
                   public RepoWithContributors apply(Repo repo, 
                       List<Contributor> contributors) throws Exception {
                       return new RepoWithContributors(repo, contributors);
                   }
               })
       .subscribe(new Consumer<RepoWithContributors>() {
           @Override
           public void accept(RepoWithContributors repoWithContributors) throws Exception {
               //saveUser(user);
           }
       });









































    