package grsu.by.fitnessapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import grsu.by.fitnessapp.database.entity.Exercise;

@Dao
public interface ExerciseDao {
    @Insert
    void insert(Exercise exercise);

    @Update
    void update(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Query("delete from exercises where id = :exerciseId")
    void deleteById(long exerciseId);

    @Query("select * from exercises order by name asc")
    LiveData<List<Exercise>> getAll();

    @Query(" select * from exercises where category = :selectedCategory order by name asc")
    LiveData<List<Exercise>> getExercisesByCategory(String selectedCategory);
}

