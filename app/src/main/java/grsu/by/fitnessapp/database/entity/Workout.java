package grsu.by.fitnessapp.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "workouts")
public class Workout {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @NonNull
    public String name;

    @NonNull
    public String category;

    @ColumnInfo(name = "start_date")
    public Date startDate;

    @ColumnInfo(name = "end_date")
    public Date endDate;

    public Integer duration;
}

