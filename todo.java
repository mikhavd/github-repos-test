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
        https://api.github.com/repos/square/wire/contributors
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










































