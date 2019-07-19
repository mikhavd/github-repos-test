package m13.retrofittest.main.database;

import m13.retrofittest.main.repos.GenericDao;

public interface TDaoProvider<T> {

    /*
    этот метод возвращает Dao,
    как RepoDatabase возвращала repositoryDao раньше
   */
    GenericDao<T> getTDao(RepoDatabase db);
}
