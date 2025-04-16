package grsu.by.fitnessapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        // Подписка на изменения в списке упражнений
        mViewModel.getAllExercises().observe(getViewLifecycleOwner(), exercises -> {
            adapter.setExercises(exercises);
        });

        // Установка обработчика удаления
        adapter.setOnDeleteClickListener(exercise -> {
            mViewModel.deleteExercise(exercise);
            Toast.makeText(getContext(), "Exercise deleted", Toast.LENGTH_SHORT).show();
        });

        FloatingActionButton addButton = getView().findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            // Пример добавления упражнения
            Exercise exercise = new Exercise();
            exercise.setName("Push Ups");
            exercise.setCategory("Strength");
            mViewModel.addExercise(exercise);
        });
    }
}
