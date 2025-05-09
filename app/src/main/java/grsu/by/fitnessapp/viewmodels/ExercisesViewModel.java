package grsu.by.fitnessapp.viewmodels;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.database.AppDatabase;
import grsu.by.fitnessapp.database.dao.ExerciseDao;
import grsu.by.fitnessapp.database.entity.Exercise;
import lombok.Getter;

public class ExercisesViewModel extends AndroidViewModel {

    private static final String TAG = "ExercisesViewModel";
    private final ExerciseDao exerciseDao;

    @Getter
    private final List<String> availableCategories;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ExercisesViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        exerciseDao = db.exerciseDao();


        availableCategories = Arrays.asList(
                application.getString(R.string.category_strength),
                application.getString(R.string.category_cardio),
                application.getString(R.string.category_flexibility),
                application.getString(R.string.category_balance),
                application.getString(R.string.category_endurance)
        );
    }

    public boolean addExercise(Exercise exercise) {
            try {
                exerciseDao.insert(exercise);
                return true;
            } catch (SQLiteConstraintException e) {
                Log.e(TAG, "Error adding exercise", e);
                return false;
            }
    }

    public void deleteExercise(Exercise exercise) {
        executor.execute(() -> {
            try {
                exerciseDao.delete(exercise);
            } catch (Exception e) {
                Log.e(TAG, "Error deleting exercise", e);
            }
        });
    }

    public LiveData<List<Exercise>> getAllExercises() {
        return exerciseDao.getAll();
    }
}
