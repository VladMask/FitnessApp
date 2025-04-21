package grsu.by.fitnessapp.database.entity;

import androidx.room.ColumnInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkoutExercise {

    private String name;

    private String category;

    private Integer sets;

    private Integer reps;

    private float weight;

    private Integer duration;

    public WorkoutExercise(Exercise exercise, ExerciseWorkload workload) {
        this.name = exercise.getName();
        this.category = exercise.getCategory();
        this.sets = workload.getSets();
        this.reps = workload.getReps();;
        this.weight = workload.getWeight();
        this.duration = workload.getDuration();
    }
}
