package grsu.by.fitnessapp.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.viewmodels.AddExerciseDialogViewModel;

public class AddExerciseDialogFragment extends Fragment {

    private AddExerciseDialogViewModel mViewModel;

    public static AddExerciseDialogFragment newInstance() {
        return new AddExerciseDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_exercise_dialog, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddExerciseDialogViewModel.class);
        // TODO: Use the ViewModel
    }

}