package m13.retrofittest.main.database;

public interface TDaoProvider<T> {

    /*
    этот метод возвращает Dao,
    как RepoDatabase возвращала repositoryDao раньше
   */
    GenericDao<T> getTDao(RepoDatabase db);
}
