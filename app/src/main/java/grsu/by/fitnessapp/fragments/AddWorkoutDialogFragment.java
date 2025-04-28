package grsu.by.fitnessapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
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
import com.google.android.material.datepicker.MaterialDatePicker;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.database.entity.Exercise;
import grsu.by.fitnessapp.database.entity.ExerciseWorkload;
import grsu.by.fitnessapp.database.entity.Workout;
import grsu.by.fitnessapp.util.StringUtils;
import grsu.by.fitnessapp.viewmodels.WorkoutsViewModel;

public class AddWorkoutDialogFragment extends DialogFragment {
    private WorkoutsViewModel viewModel;
    private AutoCompleteTextView categoryDropdown;
    private LinearLayout exercisesContainer;
    private LayoutInflater inflater;
    private LiveData<List<Exercise>> filteredExercises;
    private Date selectedDate = new Date();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(WorkoutsViewModel.class);

        inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_workout, null);

        categoryDropdown = view.findViewById(R.id.workoutCategoryInput);
        exercisesContainer = view.findViewById(R.id.exercisesContainer);

        TextInputEditText nameInput = view.findViewById(R.id.workoutNameInput);


        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewModel.getAvailableCategories()
        );
        categoryDropdown.setAdapter(categoryAdapter);

        categoryDropdown.setOnItemClickListener((parent, v, position, id) -> {
            String category = (String) parent.getItemAtPosition(position);
            this.filteredExercises = viewModel.getExercisesByCategory(category);
            exercisesContainer.removeAllViews();
        });

        MaterialButton addExerciseButton = view.findViewById(R.id.btnAddExercise);
        addExerciseButton.setOnClickListener(this::addExerciseClickListener);

        TextInputEditText dateInput = view.findViewById(R.id.workoutDateInput);

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.choose_workout_date)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        dateInput.setOnClickListener(v -> {
            datePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String formattedDate = sdf.format(new Date(selection));
            dateInput.setText(formattedDate);

            selectedDate = new Date(selection);
        });


        return new MaterialAlertDialogBuilder(requireContext())
                .setView(view)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String name = Objects.requireNonNull(nameInput.getText()).toString().trim();
                    String category = categoryDropdown.getText().toString().trim();

                    if(saveWorkout(name, category)) {
                        dialog.dismiss();
                        Toast.makeText(requireContext(), R.string.workout_added, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(requireContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
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

            ImageButton btnRemoveExercise = exerciseView.findViewById(R.id.btnRemoveExercise);
            btnRemoveExercise.setOnClickListener(v1 -> exercisesContainer.removeView(exerciseView));
        } else {
            exerciseView = inflater.inflate(R.layout.cardio_exercise_layout, exercisesContainer, false);

            ImageButton btnRemoveExercise = exerciseView.findViewById(R.id.btnRemoveExercise);
            btnRemoveExercise.setOnClickListener(v1 -> exercisesContainer.removeView(exerciseView));
        }

        exercisesContainer.addView(exerciseView);

        AutoCompleteTextView exerciseDropdown = exerciseView.findViewById(R.id.exerciseSelect);

        ArrayAdapter<Exercise> exerciseAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line
        );
        exerciseDropdown.setAdapter(exerciseAdapter);

        exerciseDropdown.setOnItemClickListener((parent, view, position, id) -> {
            Exercise selectedExercise = (Exercise) parent.getItemAtPosition(position);
            exerciseDropdown.setTag(selectedExercise);
        });

        if (filteredExercises != null) {
            filteredExercises.observe(this, exercises -> {
                exerciseAdapter.clear();
                exerciseAdapter.addAll(exercises);
                exerciseAdapter.notifyDataSetChanged();
            });
        }
    }

    private int parseIntSafe(Editable editable) {
        try {
            return Integer.parseInt(editable != null ? editable.toString().trim() : "0");
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private float parseFloatSafe(Editable editable) {
        try {
            return Float.parseFloat(editable != null ? editable.toString().trim() : "0");
        } catch (NumberFormatException e) {
            return 0f;
        }
    }


    private boolean saveWorkout(String name, String category) {
        if (!name.isEmpty() && !category.isEmpty()) {
            Workout newWorkout = new Workout();
            newWorkout.setName(StringUtils.formatName(name));
            newWorkout.setCategory(category);
            newWorkout.setStartDate(selectedDate);

            List<ExerciseWorkload> workloads = new ArrayList<>();

            for (int i = 0; i < exercisesContainer.getChildCount(); i++) {
                View exerciseView = exercisesContainer.getChildAt(i);

                AutoCompleteTextView exerciseSelect = exerciseView.findViewById(R.id.exerciseSelect);
                Exercise selectedExercise = (Exercise) exerciseSelect.getTag();

                ExerciseWorkload workload = new ExerciseWorkload();
                workload.setExerciseId(selectedExercise.getId());

                if (category.equals(getString(R.string.category_strength))) {
                    TextInputEditText setsInput = exerciseView.findViewById(R.id.exerciseSets);
                    TextInputEditText repsInput = exerciseView.findViewById(R.id.exerciseReps);
                    TextInputEditText weightInput = exerciseView.findViewById(R.id.exerciseWeight);

                    workload.setSets(parseIntSafe(setsInput.getText()));
                    workload.setReps(parseIntSafe(repsInput.getText()));
                    workload.setWeight(parseFloatSafe(weightInput.getText()));
                } else {
                    TextInputEditText setsInput = exerciseView.findViewById(R.id.cardioSets);
                    TextInputEditText timeInput = exerciseView.findViewById(R.id.cardioTime);

                    workload.setSets(parseIntSafe(setsInput.getText()));
                    workload.setDuration(parseIntSafe(timeInput.getText()));
                }

                workloads.add(workload);
            }

            viewModel.addWorkout(newWorkout, workloads);
            return true;
        } else {
            return false;
        }
    }

}

