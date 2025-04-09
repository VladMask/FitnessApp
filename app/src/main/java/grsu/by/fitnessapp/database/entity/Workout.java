package grsu.by.fitnessapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity(tableName = "workouts")
public class Workout {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public String name;

    public String category;

    @ColumnInfo(name = "start_date")
    public Date startDate;

    @ColumnInfo(name = "end_date")
    public Date endDate;

    public Integer duration;
}

