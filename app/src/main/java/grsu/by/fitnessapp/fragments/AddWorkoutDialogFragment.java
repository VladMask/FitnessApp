package grsu.by.fitnessapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.database.entity.Exercise;
import grsu.by.fitnessapp.database.entity.Workout;
import grsu.by.fitnessapp.viewmodels.WorkoutsViewModel;

public class AddWorkoutDialogFragment extends DialogFragment {

    private static final String ARG_WORKOUT = "arg_workout";
    private WorkoutsViewModel viewModel;
    private AutoCompleteTextView categoryDropdown;
    private LinearLayout exercisesContainer;
    private LayoutInflater inflater;
    private LiveData<List<Exercise>> filteredExercises;

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

        inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_workout, null);

        categoryDropdown = view.findViewById(R.id.workoutCategoryInput);
        exercisesContainer = view.findViewById(R.id.exercisesContainer);

        TextInputEditText nameInput = view.findViewById(R.id.workoutNameInput);
        AutoCompleteTextView categoryDropdown = view.findViewById(R.id.workoutCategoryInput);


        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewModel.getAvailableCategories()
        );
        categoryDropdown.setAdapter(categoryAdapter);

        categoryDropdown.setOnItemClickListener((parent, v, position, id) -> {
            String category = (String) parent.getItemAtPosition(position);
            this.filteredExercises = viewModel.getExercisesByCategory(category);
        });

        MaterialButton addExerciseButton = view.findViewById(R.id.btnAddExercise);
        addExerciseButton.setOnClickListener(this::addExerciseClickListener);

        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.add_new_workout)
                .setView(view)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String category = categoryDropdown.getText().toString().trim();

                    if (!name.isEmpty() && !category.isEmpty()) {
                        Workout newWorkout = new Workout();
                        newWorkout.setName(name);
                        newWorkout.setCategory(category);

                        Toast.makeText(requireContext(), R.string.workout_added, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private void addExerciseClickListener(View v) {
        String selectedCategory = categoryDropdown.getText().toString().trim();

        if (selectedCategory.isEmpty()) {
            Toast.makeText(requireContext(), R.string.select_category_first, Toast.LENGTH_SHORT).show();
            return;
        }

        View exerciseView;
        if (selectedCategory.equals(getString(R.string.category_strength))) {
            exerciseView = inflater.inflate(R.layout.strength_exercise_layout, exercisesContainer, false);
        } else {
            exerciseView = inflater.inflate(R.layout.cardio_exercise_layout, exercisesContainer, false);
        }

        exercisesContainer.addView(exerciseView);

        AutoCompleteTextView exerciseDropdown = exerciseView.findViewById(R.id.exerciseSelect);

        ArrayAdapter<Exercise> exerciseAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line
        );
        exerciseDropdown.setAdapter(exerciseAdapter);

        if (filteredExercises != null) {
            filteredExercises.observe(this, exercises -> {
                exerciseAdapter.clear();
                exerciseAdapter.addAll(exercises);
                exerciseAdapter.notifyDataSetChanged();
            });
        }
    }
}

