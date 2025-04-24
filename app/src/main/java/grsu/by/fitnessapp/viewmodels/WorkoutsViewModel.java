package grsu.by.fitnessapp.viewmodels;

import android.app.Application;
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
import grsu.by.fitnessapp.database.dao.ExerciseWorkloadDao;
import grsu.by.fitnessapp.database.dao.WorkoutDao;
import grsu.by.fitnessapp.database.entity.Exercise;
import grsu.by.fitnessapp.database.entity.ExerciseWorkload;
import grsu.by.fitnessapp.database.entity.Workout;
import grsu.by.fitnessapp.database.entity.WorkoutExercise;
import lombok.Getter;

public class WorkoutsViewModel extends AndroidViewModel {

    private static final String TAG = "WorkoutsViewModel";
    private final WorkoutDao workoutDao;
    private final ExerciseDao exerciseDao;
    private final ExerciseWorkloadDao exerciseWorkloadDao;

    @Getter
    private final List<String> availableCategories;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public WorkoutsViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        workoutDao = db.workoutDao();
        exerciseDao = db.exerciseDao();
        exerciseWorkloadDao = db.exerciseWorkloadDao();

        availableCategories = Arrays.asList(
                application.getString(R.string.category_strength),
                application.getString(R.string.category_cardio),
                application.getString(R.string.category_flexibility),
                application.getString(R.string.category_balance),
                application.getString(R.string.category_endurance)
        );
    }

    public void addWorkout(Workout workout, List<ExerciseWorkload> workloads) {
        executor.execute(() -> {
            try {
                long workoutId = workoutDao.insert(workout);
                for (ExerciseWorkload workload : workloads) {
                    workload.setWorkoutId(workoutId);
                    exerciseWorkloadDao.insert(workload);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error adding workout", e);
            }
        });
    }

    public void updateWorkout(Workout workout, List<ExerciseWorkload> workloads) {
        executor.execute(() -> {
            try {
                workoutDao.update(workout);
                exerciseWorkloadDao.deleteByWorkoutId(workout.getId());
                for (ExerciseWorkload workload : workloads) {
                    workload.setWorkoutId(workout.getId());
                    exerciseWorkloadDao.insert(workload);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error updating workout", e);
            }
        });
    }

    public void deleteWorkout(Workout workout) {
        executor.execute(() -> {
            try {
                workoutDao.delete(workout);
            } catch (Exception e) {
                Log.e(TAG, "Error deleting workout", e);
            }
        });
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return workoutDao.getAllWorkouts();
    }

    public LiveData<List<WorkoutExercise>> getWorkoutExercisesWorkoutById(long id) {
         return exerciseWorkloadDao.getByWorkoutIdWithDetails(id);
    }

    public LiveData<List<Exercise>> getExercisesByCategory(String category) {
        return exerciseDao.getExercisesByCategory(category);
    }

}
