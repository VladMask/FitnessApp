package grsu.by.fitnessapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Notification {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "workout_id")
    public long workoutId;

    @ColumnInfo(name = "send_date")
    public Date sendDate;

    public boolean status;
}

