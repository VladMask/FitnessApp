package grsu.by.fitnessapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import grsu.by.fitnessapp.database.entity.UserConditions;

@Dao
public interface UserConditionsDao {
    @Insert
    void insert(UserConditions userConditions);

    @Query("SELECT * FROM UserConditions ORDER BY checkup_Date DESC LIMIT 1")
    UserConditions getLatest();

    @Update
    void update(UserConditions userConditions);
}

