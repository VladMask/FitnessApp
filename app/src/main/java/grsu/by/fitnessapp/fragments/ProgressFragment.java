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
import grsu.by.fitnessapp.viewmodels.ProgressViewModel;

public class ProgressFragment extends Fragment {

    private ProgressViewModel mViewModel;

    public static ProgressFragment newInstance() {
        return new ProgressFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProgressViewModel.class);
        // TODO: Use the ViewModel
    }

}