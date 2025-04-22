package grsu.by.fitnessapp.fragments;

import android.app.AlertDialog;
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

import com.google.android.material.textfield.TextInputEditText;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.database.entity.Exercise;
import grsu.by.fitnessapp.viewmodels.ExercisesViewModel;

public class AddExerciseDialogFragment extends DialogFragment {

    private ExercisesViewModel viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(ExercisesViewModel.class);
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_exercise, null);

        TextInputEditText nameInput = dialogView.findViewById(R.id.exerciseNameInput);
        AutoCompleteTextView categoryDropdown = dialogView.findViewById(R.id.categoryDropdown);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewModel.getAvailableCategories()
        );
        categoryDropdown.setAdapter(categoryAdapter);

        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.add_new_exercise)
                .setView(dialogView)
                .setPositiveButton(R.string.add, (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String category = categoryDropdown.getText().toString().trim();

                    if (!name.isEmpty() && !category.isEmpty()) {
                        Exercise exercise = new Exercise();
                        exercise.setName(name);
                        exercise.setCategory(category);
                        viewModel.addExercise(exercise);
                        Toast.makeText(getContext(), R.string.exercise_added, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }
}
