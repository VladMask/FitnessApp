package grsu.by.fitnessapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class UserConditions {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    public byte age;
    public short weight;
    public short height;
    public boolean gender;

    @ColumnInfo(name = "checkup_Date")
    public Date checkupDate;
}

