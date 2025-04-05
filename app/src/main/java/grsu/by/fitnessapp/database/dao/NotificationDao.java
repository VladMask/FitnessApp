package grsu.by.fitnessapp.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import grsu.by.fitnessapp.database.entity.Notification;

@Dao
public interface NotificationDao {
    @Insert
    void insert(Notification notification);

    @Query("SELECT * FROM Notification")
    List<Notification> getAll();

    @Query("SELECT * FROM Notification WHERE status = 0")
    List<Notification> getPendingNotifications();

    @Update
    void update(Notification notification);
}

