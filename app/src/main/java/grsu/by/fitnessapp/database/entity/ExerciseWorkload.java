package grsu.by.fitnessapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Entity;

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
public class ExerciseWorkload {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "exercise_id")
    public int exerciseId;

    @ColumnInfo(name = "workout_id")
    public long workoutId;

    public int sets;
    public Integer reps;
    public Double weight;
    public int duration; // seconds
}

