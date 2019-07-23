package m13.retrofittest.main.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;

import m13.retrofittest.main.database.RepoDatabase;
import m13.retrofittest.main.database.TDaoProvider;
import m13.retrofittest.main.repos.Repozitory;

public class RepozitoriesViewModel extends GenericViewModel<Repozitory> {
    public RepozitoriesViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    final TDaoProvider<Repozitory> getDaoProvider() {
        return RepoDatabase::repoDao;
    }

    @Override
    public void insert(Repozitory item) {
            genericRepository.insert(item);
    }
}
