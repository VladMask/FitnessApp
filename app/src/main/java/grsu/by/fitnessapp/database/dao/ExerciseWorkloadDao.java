package grsu.by.fitnessapp.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import grsu.by.fitnessapp.database.entity.ExerciseWorkload;

@Dao
public interface ExerciseWorkloadDao {
    @Insert
    void insert(ExerciseWorkload workload);

    @Query("SELECT * FROM exercise_workloads")
    List<ExerciseWorkload> getAll();

    @Query("SELECT * FROM exercise_workloads WHERE workout_id = :workoutId")
    List<ExerciseWorkload> getByWorkoutId(long workoutId);
}

