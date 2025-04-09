package grsu.by.fitnessapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Entity;

import java.io.Serializable;

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
@Entity(
        tableName = "exercise_workloads",
        foreignKeys = {
                @ForeignKey(entity = Exercise.class,
                        parentColumns = "id",
                        childColumns = "exercise_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Workout.class,
                        parentColumns = "id",
                        childColumns = "workout_id",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class ExerciseWorkload implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "exercise_id")
    public Integer exerciseId;

    @ColumnInfo(name = "workout_id")
    public Long workoutId;

    public Integer sets;
    public Integer reps;
    public Double weight;
    public Integer duration; // seconds
}

