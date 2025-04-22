package grsu.by.fitnessapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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
        @ForeignKey(
            entity = Workout.class,
            parentColumns = "id",
            childColumns = "workout_id",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Exercise.class,
            parentColumns = "id",
            childColumns = "exercise_id",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index("workout_id"),
        @Index("exercise_id")
    }
)
public class ExerciseWorkload implements Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "workout_id")
    private Long workoutId;

    @ColumnInfo(name = "exercise_id")
    private Long exerciseId;

    @ColumnInfo(name = "sets")
    private Integer sets;

    @ColumnInfo(name = "reps")
    private Integer reps;

    @ColumnInfo(name = "weight")
    private float weight;

    @ColumnInfo(name = "duration")
    private Integer duration;
}

