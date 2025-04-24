package grsu.by.fitnessapp.database.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkoutExercise {

    @Embedded
    public ExerciseWorkload workload;

    @Relation(
            parentColumn = "workout_id",
            entityColumn = "id"
    )
    public Workout workout;

    @Relation(
            parentColumn = "exercise_id",
            entityColumn = "id"
    )
    public Exercise exercise;
}
