package grsu.by.fitnessapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import grsu.by.fitnessapp.database.entity.Workout;

@Dao
public interface WorkoutDao {
    @Insert
    void insert(Workout workout);

    @Query("SELECT * FROM workouts")
    List<Workout> getAll();

    @Query("SELECT * FROM workouts WHERE id = :id")
    Workout getById(int id);

    @Delete
    void delete(Workout workout);
}

