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
        4. что-то своё на усмотрение (подсмотреть с web-представления листа репозиториев?) //
        
    b. для окна с детальной информацией о репозитории:
        1. кнопка "к контрибуторам" (обязательный контент) //
        2. "кнопка к коммитам" (обязательный контент) //
        3. что-то на своё усмотрение (подсмотреть с web-представления инфы о репозитории?) //
        
    c. для экрана контрибуторов:
        1. список контрибуторов и коммитов //
        
    d. для экрана коммитов:
        1. список коммитов //