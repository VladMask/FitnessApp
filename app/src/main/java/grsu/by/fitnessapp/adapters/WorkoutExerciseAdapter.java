package grsu.by.fitnessapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.database.entity.WorkoutExercise;

public class WorkoutExerciseAdapter extends RecyclerView.Adapter<WorkoutExerciseAdapter.ExerciseViewHolder> {

    private final List<WorkoutExercise> exercises;

    public WorkoutExerciseAdapter(List<WorkoutExercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        WorkoutExercise item = exercises.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return exercises != null ? exercises.size() : 0;
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView exerciseName;
        private final TextView workloadDetails;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            workloadDetails = itemView.findViewById(R.id.workloadDetails);
        }

        public void bind(WorkoutExercise exercise) {
            Context context = itemView.getContext();

            exerciseName.setText(exercise.getName());

            StringBuilder details = new StringBuilder();

            if (exercise.getSets() != null && exercise.getReps() != null) {
                details.append(context.getString(R.string.sets))
                        .append(exercise.getSets())
                        .append(", ")
                        .append(context.getString(R.string.reps))
                        .append(exercise.getReps());
            }

            if (exercise.getWeight() > 0) {
                details.append(", ")
                        .append(context.getString(R.string.weight_kg))
                        .append(": ")
                        .append(exercise.getWeight());
            }

            if (exercise.getDuration() != null && exercise.getDuration() > 0) {
                details.append(", ")
                        .append(context.getString(R.string.duration_sec))
                        .append(": ").append(exercise.getDuration())
                        .append(" сек");
            }

            if (details.length() == 0) {
                details.append(context.getString(R.string.no_info_about_workload));
            }

            workloadDetails.setText(details.toString());
            System.out.print("aaa");
        }
    }
}
