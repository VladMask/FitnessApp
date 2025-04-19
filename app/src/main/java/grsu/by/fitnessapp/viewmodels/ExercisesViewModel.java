package grsu.by.fitnessapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.database.AppDatabase;
import grsu.by.fitnessapp.database.dao.ExerciseDao;
import grsu.by.fitnessapp.database.entity.Exercise;

public class ExercisesViewModel extends AndroidViewModel {

    private static final String TAG = "ExercisesViewModel";
    private final ExerciseDao exerciseDao;
    private final List<String> availableCategories;

    public ExercisesViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        exerciseDao = db.exerciseDao();
        
        // Категории упражнений
        availableCategories = Arrays.asList(
                application.getString(R.string.category_strength),
                application.getString(R.string.category_cardio),
                application.getString(R.string.category_flexibility),
                application.getString(R.string.category_balance),
                application.getString(R.string.category_endurance)
        );
    }

    public void addExercise(Exercise exercise) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                exerciseDao.insert(exercise);
            } catch (Exception e) {
                Log.e(TAG, "Error adding exercise", e);
            }
        });
    }

    public void deleteExercise(Exercise exercise) {
        Executors.newSingleThreadExecutor().execute(() -> {
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

    public List<String> getAvailableCategories() {
        return availableCategories;
    }
}
