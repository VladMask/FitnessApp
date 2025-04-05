package grsu.by.fitnessapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import grsu.by.fitnessapp.database.entity.Exercise;

@Dao
public interface ExerciseDao {
    @Insert
    void insert(Exercise exercise);

    @Query("SELECT * FROM exercises")
    List<Exercise> getAll();
}

