package grsu.by.fitnessapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.database.entity.Exercise;
import grsu.by.fitnessapp.database.entity.Workout;
import grsu.by.fitnessapp.viewmodels.WorkoutsViewModel;

public class AddWorkoutDialogFragment extends DialogFragment {

    private static final String ARG_WORKOUT = "arg_workout";
    private WorkoutsViewModel viewModel;
    private Workout workoutToEdit;

    public static AddWorkoutDialogFragment newInstance(@Nullable Workout workout) {
        AddWorkoutDialogFragment fragment = new AddWorkoutDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_WORKOUT, workout);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(WorkoutsViewModel.class);

        if (getArguments() != null) {
            workoutToEdit = (Workout) getArguments().getSerializable(ARG_WORKOUT);
        }

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_workout, null);

        TextInputEditText nameInput = dialogView.findViewById(R.id.workoutNameInput);
        AutoCompleteTextView categoryDropdown = dialogView.findViewById(R.id.workoutCategoryInput);
//        AutoCompleteTextView exerciseDropdown = dialogView.findViewById(R.id.exerciseSelect);

        // Category Dropdown
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewModel.getAvailableCategories()
        );
        categoryDropdown.setAdapter(categoryAdapter);

        // Exercise Dropdown
        ArrayAdapter<Exercise> exerciseAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line
        );
//        exerciseDropdown.setAdapter(exerciseAdapter);
//
//        categoryDropdown.setOnItemClickListener((parent, view, position, id) -> {
//            String category = (String) parent.getItemAtPosition(position);
//            viewModel.getExercisesByCategory(category).observe(this, exercises -> {
//                exerciseAdapter.clear();
//                exerciseAdapter.addAll(exercises);
//                exerciseAdapter.notifyDataSetChanged();
//                exerciseDropdown.setText("");
//                exerciseDropdown.showDropDown();
//                setWorkloadVisible(dialogView, category);
//            });
//        });

        if (workoutToEdit != null) {
            nameInput.setText(workoutToEdit.getName());
            categoryDropdown.setText(workoutToEdit.getCategory());
        }

        return new MaterialAlertDialogBuilder(requireContext())
//                .setTitle(workoutToEdit != null ? R.string.edit_workout : R.string.add_workout)
                .setView(dialogView)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String category = categoryDropdown.getText().toString().trim();

                    if (!name.isEmpty() && !category.isEmpty()) {
                        Workout workout = workoutToEdit != null ? workoutToEdit : new Workout();
                        workout.setName(name);
                        workout.setCategory(category);

                        if (workoutToEdit != null) {
                            viewModel.updateWorkout(workout, new ArrayList<>());
                        } else {
                            viewModel.addWorkout(workout, new ArrayList<>());
                        }

                        Toast.makeText(requireContext(), R.string.workout_added, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private void setWorkloadVisible(View dialogView, String selectedCategory) {
        View strengthLayout = dialogView.findViewById(R.id.strengthExerciseLayout);
        View cardioLayout = dialogView.findViewById(R.id.cardioExerciseLayout);

        strengthLayout.setVisibility(View.GONE);
        cardioLayout.setVisibility(View.GONE);

        if (selectedCategory.equals(getString(R.string.category_strength))) {
            strengthLayout.setVisibility(View.VISIBLE);
        } else {
            cardioLayout.setVisibility(View.VISIBLE);
        }
    }
}

