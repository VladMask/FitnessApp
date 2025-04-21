package grsu.by.fitnessapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import grsu.by.fitnessapp.database.entity.ExerciseWorkload;

@Dao
public interface ExerciseWorkloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExerciseWorkload exerciseWorkload);

    @Update
    void update(ExerciseWorkload exerciseWorkload);

    @Delete
    void delete(ExerciseWorkload exerciseWorkload);

    @Query("SELECT * FROM exercise_workloads WHERE workout_id = :workoutId")
    LiveData<List<ExerciseWorkload>> getWorkloadsByWorkoutId(long workoutId);

    @Query("SELECT * FROM exercise_workloads WHERE exercise_id = :exerciseId")
    LiveData<List<ExerciseWorkload>> getWorkloadsByExerciseId(long exerciseId);

    @Query("SELECT * FROM exercise_workloads WHERE id = :id")
    LiveData<ExerciseWorkload> getWorkloadById(long id);

    @Query("DELETE FROM exercise_workloads WHERE workout_id = :workoutId")
    void deleteByWorkoutId(long workoutId);

    @Query("DELETE FROM exercise_workloads WHERE exercise_id = :exerciseId")
    void deleteByExerciseId(long exerciseId);
}

