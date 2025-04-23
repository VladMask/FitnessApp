package grsu.by.fitnessapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import grsu.by.fitnessapp.database.AppDatabase;
import grsu.by.fitnessapp.database.dao.UserConditionsDao;
import grsu.by.fitnessapp.database.entity.UserConditions;
import lombok.Getter;

public class UserConditionsViewModel extends AndroidViewModel {

    private final UserConditionsDao userConditionsDao;
    @Getter
    private final LiveData<List<UserConditions>> allUserConditions;

    public UserConditionsViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        userConditionsDao = database.userConditionsDao();
        allUserConditions = userConditionsDao.getAll();
    }

    public void insert(UserConditions condition) {
        userConditionsDao.insert(condition);
    }

    public void delete(UserConditions condition) {
        userConditionsDao.delete(condition);
    }
}

