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
@Entity
public class Notification {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "workout_id")
    public Long workoutId;

    @ColumnInfo(name = "send_date")
    public Date sendDate;

    public Boolean status;
}

