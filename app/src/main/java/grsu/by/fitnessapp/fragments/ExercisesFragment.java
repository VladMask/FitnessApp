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
        addButton.setOnClickListener(v ->
                new AddExerciseDialogFragment().show(getParentFragmentManager(), "AddExerciseDialog")
        );
    }

}
