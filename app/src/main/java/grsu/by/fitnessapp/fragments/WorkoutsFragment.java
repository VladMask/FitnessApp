package grsu.by.fitnessapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.adapters.WorkoutAdapter;
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

        adapter.setOnDeleteClickListener(workout -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.delete_workout)
                    .setMessage(R.string.are_you_sure)
                    .setPositiveButton(R.string.delete, (dialog, which) -> {
                        viewModel.deleteWorkout(workout);
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });
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
        addButton.setOnClickListener(v ->
                {
                    AddWorkoutDialogFragment dialogFragment = new AddWorkoutDialogFragment();
                    dialogFragment.show(getFragmentManager(), "addWorkoutDialog");
                }
        );
    }

    @Override
    public void onWorkoutClick(Workout workout) {
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.setVisibility(View.GONE);

        WorkoutDetailsFragment detailsFragment = new WorkoutDetailsFragment(workout);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit();
    }


}
