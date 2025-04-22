package grsu.by.fitnessapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import grsu.by.fitnessapp.database.entity.Workout;

@Dao
public interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Workout workout);

    @Update
    void update(Workout workout);

    @Delete
    void delete(Workout workout);

    @Query("DELETE FROM workouts WHERE id = :workoutId")
    void deleteById(long workoutId);

    @Query("SELECT * FROM workouts ORDER BY start_date DESC")
    LiveData<List<Workout>> getAllWorkouts();

    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    LiveData<Workout> getWorkoutById(long workoutId);

    @Query("SELECT * FROM workouts WHERE category = :category ORDER BY start_date DESC")
    LiveData<List<Workout>> getWorkoutsByCategory(String category);
}

