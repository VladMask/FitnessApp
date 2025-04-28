package grsu.by.fitnessapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import grsu.by.fitnessapp.database.entity.ExerciseWorkload;
import grsu.by.fitnessapp.database.entity.WorkoutExercise;

@Dao
public interface ExerciseWorkloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExerciseWorkload exerciseWorkload);

    @Update
    void update(ExerciseWorkload exerciseWorkload);

    @Delete
    void delete(ExerciseWorkload exerciseWorkload);
    @Query("DELETE FROM exercise_workloads WHERE workout_id = :workoutId")
    void deleteByWorkoutId(long workoutId);

    @Transaction
    @Query("SELECT * FROM exercise_workloads WHERE workout_id = :id")
    LiveData<List<WorkoutExercise>> getWorkoutExercisesByWorkoutId(long id);
}

