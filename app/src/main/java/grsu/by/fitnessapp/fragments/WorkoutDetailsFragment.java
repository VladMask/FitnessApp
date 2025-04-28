package grsu.by.fitnessapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.adapters.WorkoutExerciseAdapter;
import grsu.by.fitnessapp.database.entity.Workout;
import grsu.by.fitnessapp.viewmodels.WorkoutsViewModel;

public class WorkoutDetailsFragment extends Fragment {

    private WorkoutsViewModel viewModel;
    private final Workout workout;


    public WorkoutDetailsFragment (Workout workout) {
        super();
        this.workout = workout;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout_details, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(WorkoutsViewModel.class);
        setupRecyclerView(view, workout.getId());

        TextView name = view.findViewById(R.id.workoutDetailsName);
        name.setText(workout.getName());
        TextView category = view.findViewById(R.id.workoutDetailsCategory);
        category.setText(workout.getCategory());
        TextView date = view.findViewById(R.id.workoutDetailsDate);
        date.setText(workout.getStringStartDate());

        ImageButton editButton = view.findViewById(R.id.editWorkoutButton);
        editButton.setOnClickListener(v -> {
            EditWorkoutDialogFragment dialogFragment = new EditWorkoutDialogFragment(workout);
            dialogFragment.show(getParentFragmentManager(), "EditWorkoutDialog");
        });
    }

    private void setupRecyclerView(View view, Long workoutId) {
        RecyclerView recyclerView = view.findViewById(R.id.workoutExercisesRecyclerView);
        WorkoutExerciseAdapter adapter = new WorkoutExerciseAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        viewModel.getWorkoutExercisesByWorkoutId(workoutId).observe(getViewLifecycleOwner(), workoutExercises -> {
            adapter.setItems(workoutExercises);
            adapter.notifyDataSetChanged();
            workout.setWorkoutExercises(workoutExercises);
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.setVisibility(View.VISIBLE);
    }

}
