package grsu.by.fitnessapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.adapters.WorkoutAdapter;
import grsu.by.fitnessapp.database.entity.Exercise;
import grsu.by.fitnessapp.database.entity.Workout;
import grsu.by.fitnessapp.viewmodels.WorkoutsViewModel;

public class WorkoutsFragment extends Fragment implements WorkoutAdapter.OnWorkoutClickListener {
    private WorkoutsViewModel viewModel;
    private WorkoutAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workouts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(WorkoutsViewModel.class);
        setupRecyclerView(view);
        setupAddButton(view);
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.workoutsRecyclerView);
        adapter = new WorkoutAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        viewModel.getAllWorkouts().observe(getViewLifecycleOwner(), workouts -> {
            adapter.setWorkouts(workouts);
        });
    }

    private void setupAddButton(View view) {
        FloatingActionButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> showWorkoutDialog(null));
    }

    private void showWorkoutDialog(Workout workout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_workout, null);
        builder.setView(dialogView);

        TextInputEditText nameInput = dialogView.findViewById(R.id.workoutNameInput);
        AutoCompleteTextView categoryDropdown = dialogView.findViewById(R.id.workoutCategoryInput);
        AutoCompleteTextView exerciseDropdown = dialogView.findViewById(R.id.exerciseSelect);

        // Setup category dropdown
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            viewModel.getAvailableCategories()
        );
        categoryDropdown.setAdapter(categoryAdapter);

        // Setup exercise dropdown
        ArrayAdapter<Exercise> exerciseAdapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line
        );
        exerciseDropdown.setAdapter(exerciseAdapter);

        // Handle category selection
        categoryDropdown.setOnItemClickListener((parent, view1, position, id) -> {
            String category = (String) parent.getItemAtPosition(position);
            viewModel.getExercisesByCategory(category).observe(getViewLifecycleOwner(), exercises -> {
                exerciseAdapter.clear();
                exerciseAdapter.addAll(exercises);
                exerciseAdapter.notifyDataSetChanged();
                exerciseDropdown.setText("");
                exerciseDropdown.showDropDown();
                setWorkloadVisible(dialogView, category);
            });
        });

        // Handle exercise selection
        exerciseDropdown.setOnItemClickListener((parent, view1, position, id) -> {
            Exercise selectedExercise = (Exercise) parent.getItemAtPosition(position);
        });

        if (workout != null) {
            nameInput.setText(workout.getName());
            categoryDropdown.setText(workout.getCategory());
        }

        builder.setPositiveButton(R.string.save, (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String category = categoryDropdown.getText().toString().trim();

            if (!name.isEmpty() && !category.isEmpty()) {
                Workout newWorkout = workout != null ? workout : new Workout();
                newWorkout.setName(name);
                newWorkout.setCategory(category);

                if (workout != null) {
                    viewModel.updateWorkout(newWorkout, new ArrayList<>());
                } else {
                    viewModel.addWorkout(newWorkout, new ArrayList<>());
                }
                Toast.makeText(requireContext(), R.string.workout_added, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    @Override
    public void onWorkoutClick(Workout workout) {
        showWorkoutDialog(workout);
    }

    private void setWorkloadVisible(View dialogView, String selectedCategory) {
        View strengthLayout = dialogView.findViewById(R.id.strengthExerciseLayout);
        View cardioLayout = dialogView.findViewById(R.id.cardioExerciseLayout);

        strengthLayout.setVisibility(View.GONE);
        cardioLayout.setVisibility(View.GONE);

        if (selectedCategory.equals(getString(R.string.category_strength))) {
            strengthLayout.setVisibility(View.VISIBLE);
        }
        else {
            cardioLayout.setVisibility(View.VISIBLE);
        }
    }

}
