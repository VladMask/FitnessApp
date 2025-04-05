package grsu.by.fitnessapp.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercises")
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @NonNull
    public String name;

    @NonNull
    public String type;
}


