package grsu.by.fitnessapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.database.entity.Workout;
import lombok.Setter;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private List<Workout> workouts = new ArrayList<>();
    private final OnWorkoutClickListener onWorkoutClickListener;
    @Setter
    private OnDeleteClickListener onDeleteClickListener;

    public WorkoutAdapter(OnWorkoutClickListener onWorkoutClickListener) {
        this.onWorkoutClickListener = onWorkoutClickListener;
    }

    public interface OnWorkoutClickListener {
        void onWorkoutClick(Workout workout);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Workout workout);
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.nameTextView.setText(workout.getName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String formattedDate = sdf.format(workout.getStartDate());

        holder.dateTextView.setText(formattedDate);
        holder.categoryTextView.setText(workout.getCategory());

        holder.itemView.setOnClickListener(v -> {
            if (onWorkoutClickListener != null) {
                onWorkoutClickListener.onWorkoutClick(workout);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(workout);
            }
        });
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public void setWorkouts(List<Workout> newWorkouts) {
        this.workouts = newWorkouts;
        notifyDataSetChanged();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView dateTextView;
        TextView categoryTextView;
        ImageButton deleteButton;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.workoutName);
            dateTextView = itemView.findViewById(R.id.workoutDate);
            categoryTextView = itemView.findViewById(R.id.workoutCategory);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
