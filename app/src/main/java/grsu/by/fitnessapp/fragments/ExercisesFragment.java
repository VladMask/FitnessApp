package grsu.by.fitnessapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.adapters.ExerciseAdapter;
import grsu.by.fitnessapp.database.entity.Exercise;
import grsu.by.fitnessapp.viewmodels.ExercisesViewModel;

public class ExercisesFragment extends Fragment {

    private ExercisesViewModel mViewModel;
    private ExerciseAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercises, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ExercisesViewModel.class);

        RecyclerView recyclerView = getView().findViewById(R.id.exercisesRecyclerView);
        adapter = new ExerciseAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        mViewModel.getAllExercises().observe(getViewLifecycleOwner(), exercises -> {
            adapter.setExercises(exercises);
        });

        adapter.setOnDeleteClickListener(exercise -> {
            mViewModel.deleteExercise(exercise);
            Toast.makeText(getContext(), R.string.exercise_deleted, Toast.LENGTH_SHORT).show();
        });

        FloatingActionButton addButton = getView().findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> showAddExerciseDialog());
    }

    private void showAddExerciseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_exercise, null);
        builder.setView(dialogView);

        TextInputEditText nameInput = dialogView.findViewById(R.id.exerciseNameInput);
        AutoCompleteTextView categoryDropdown = dialogView.findViewById(R.id.categoryDropdown);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                mViewModel.getAvailableCategories()
        );
        categoryDropdown.setAdapter(categoryAdapter);

        builder.setTitle(R.string.add_new_exercise)
                .setPositiveButton(R.string.add, (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String category = categoryDropdown.getText().toString().trim();

                    if (!name.isEmpty() && !category.isEmpty()) {
                        Exercise exercise = new Exercise();
                        exercise.setName(name);
                        exercise.setCategory(category);
                        mViewModel.addExercise(exercise);
                        Toast.makeText(getContext(), R.string.exercise_added, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
