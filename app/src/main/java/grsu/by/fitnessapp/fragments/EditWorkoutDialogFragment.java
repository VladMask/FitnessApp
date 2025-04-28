package grsu.by.fitnessapp.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

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
import grsu.by.fitnessapp.database.entity.WorkoutExercise;
import grsu.by.fitnessapp.util.StringUtils;
import grsu.by.fitnessapp.viewmodels.WorkoutsViewModel;

public class EditWorkoutDialogFragment extends DialogFragment {

    private WorkoutsViewModel viewModel;
    private Workout workout;
    private AutoCompleteTextView categoryDropdown;
    private LinearLayout exercisesContainer;
    private LayoutInflater inflater;
    private LiveData<List<Exercise>> filteredExercises;
    private Date selectedDate;
    private TextInputEditText dateInput;
    private TextInputEditText nameInput;

    public EditWorkoutDialogFragment(Workout workout) {
        this.workout = workout;
        this.selectedDate = workout.getStartDate();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(WorkoutsViewModel.class);
        inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_workout, null);

        initViews(view);
        setupCategoryDropdown();
        setupDatePicker();
        fillDialog();

        return new MaterialAlertDialogBuilder(requireContext())
                .setView(view)
                .setPositiveButton(R.string.save, (dialog, which) -> saveWorkout())
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private void initViews(View view) {
        categoryDropdown = view.findViewById(R.id.workoutCategoryInput);
        exercisesContainer = view.findViewById(R.id.exercisesContainer);
        nameInput = view.findViewById(R.id.workoutNameInput);
        dateInput = view.findViewById(R.id.workoutDateInput);
        MaterialButton addExerciseButton = view.findViewById(R.id.btnAddExercise);

        addExerciseButton.setOnClickListener(this::onAddExerciseClick);
    }

    private void setupCategoryDropdown() {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewModel.getAvailableCategories()
        );
        categoryDropdown.setAdapter(categoryAdapter);

        categoryDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String category = (String) parent.getItemAtPosition(position);
            filteredExercises = viewModel.getExercisesByCategory(category);
            exercisesContainer.removeAllViews();
        });
    }

    private void setupDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.choose_workout_date)
                .setSelection(selectedDate.getTime())
                .build();

        dateInput.setOnClickListener(v -> datePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER"));

        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDate = new Date(selection);
            dateInput.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selectedDate));
        });
    }

    private void onAddExerciseClick(View v) {
        String selectedCategory = categoryDropdown.getText().toString().trim();
        if (selectedCategory.isEmpty()) {
            Toast.makeText(requireContext(), R.string.select_category_first, Toast.LENGTH_SHORT).show();
            return;
        }
        addExerciseView(null);
    }

    @SuppressLint("SetTextI18n")
    private void fillDialog() {
        categoryDropdown.setText(workout.getCategory());
        nameInput.setText(workout.getName());
        dateInput.setText(workout.getStringStartDate());

        filteredExercises = viewModel.getExercisesByCategory(workout.getCategory());

        for (WorkoutExercise workoutExercise : workout.getWorkoutExercises()) {
            addExerciseView(workoutExercise);
        }
    }

    private void addExerciseView(@Nullable WorkoutExercise workoutExercise) {
        String category = categoryDropdown.getText().toString().trim();
        int layout = category.equals(getString(R.string.category_strength)) ?
                R.layout.strength_exercise_layout : R.layout.cardio_exercise_layout;

        View exerciseView = inflater.inflate(layout, exercisesContainer, false);
        ImageButton removeButton = exerciseView.findViewById(R.id.btnRemoveExercise);
        removeButton.setOnClickListener(v -> exercisesContainer.removeView(exerciseView));

        AutoCompleteTextView exerciseDropdown = exerciseView.findViewById(R.id.exerciseSelect);
        setupExerciseDropdown(exerciseDropdown);

        if (workoutExercise != null) {
            Exercise exercise = workoutExercise.getExercise();
            exerciseDropdown.setTag(exercise);
            exerciseDropdown.setText(exercise.getName(), false);

            ExerciseWorkload workload = workoutExercise.getWorkload();
            if (category.equals(getString(R.string.category_strength))) {
                setInputValue(exerciseView, R.id.exerciseSets, workload.getSets());
                setInputValue(exerciseView, R.id.exerciseReps, workload.getReps());
                setInputValue(exerciseView, R.id.exerciseWeight, workload.getWeight());
            } else {
                setInputValue(exerciseView, R.id.cardioSets, workload.getSets());
                setInputValue(exerciseView, R.id.cardioTime, workload.getDuration());
            }
        }

        exercisesContainer.addView(exerciseView);
    }

    private void setupExerciseDropdown(AutoCompleteTextView dropdown) {
        ArrayAdapter<Exercise> exerciseAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line
        );
        dropdown.setAdapter(exerciseAdapter);

        dropdown.setOnItemClickListener((parent, view, position, id) -> {
            Exercise selectedExercise = (Exercise) parent.getItemAtPosition(position);
            dropdown.setTag(selectedExercise);
        });

        if (filteredExercises != null) {
            filteredExercises.observe(this, exercises -> {
                exerciseAdapter.clear();
                exerciseAdapter.addAll(exercises);
                exerciseAdapter.notifyDataSetChanged();
            });
        }
    }

    private void setInputValue(View parent, int inputId, Number value) {
        TextInputEditText input = parent.findViewById(inputId);
        input.setText(String.valueOf(value));
    }

    private void saveWorkout() {
        String name = Objects.requireNonNull(nameInput.getText()).toString().trim();
        String category = categoryDropdown.getText().toString().trim();

        if (name.isEmpty() || category.isEmpty()) {
            Toast.makeText(requireContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        workout.setName(StringUtils.formatName(name));
        workout.setCategory(category);
        workout.setStartDate(selectedDate);

        List<ExerciseWorkload> workloads = new ArrayList<>();
        for (int i = 0; i < exercisesContainer.getChildCount(); i++) {
            View exerciseView = exercisesContainer.getChildAt(i);
            workloads.add(createWorkloadFromView(exerciseView, category));
        }

        viewModel.addWorkout(workout, workloads);

        Toast.makeText(requireContext(), R.string.workout_updated, Toast.LENGTH_SHORT).show();
    }

    private ExerciseWorkload createWorkloadFromView(View view, String category) {
        ExerciseWorkload workload = new ExerciseWorkload();
        AutoCompleteTextView exerciseDropdown = view.findViewById(R.id.exerciseSelect);
        Exercise exercise = (Exercise) exerciseDropdown.getTag();
        workload.setExerciseId(exercise.getId());

        if (category.equals(getString(R.string.category_strength))) {
            workload.setSets(parseIntSafe(view, R.id.exerciseSets));
            workload.setReps(parseIntSafe(view, R.id.exerciseReps));
            workload.setWeight(parseFloatSafe(view, R.id.exerciseWeight));
        } else {
            workload.setSets(parseIntSafe(view, R.id.cardioSets));
            workload.setDuration(parseIntSafe(view, R.id.cardioTime));
        }

        return workload;
    }

    private int parseIntSafe(View parent, int id) {
        TextInputEditText input = parent.findViewById(id);
        try {
            return Integer.parseInt(Objects.requireNonNull(input.getText()).toString().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private float parseFloatSafe(View parent, int id) {
        TextInputEditText input = parent.findViewById(id);
        try {
            return Float.parseFloat(Objects.requireNonNull(input.getText()).toString().trim());
        } catch (Exception e) {
            return 0f;
        }
    }
}
