package grsu.by.fitnessapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.Objects;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.database.entity.UserConditions;
import grsu.by.fitnessapp.viewmodels.UserConditionsViewModel;

public class AddUserConditionsDialogFragment extends DialogFragment {

    private UserConditionsViewModel viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(UserConditionsViewModel.class);
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_user_condition, null);

        TextInputEditText weightInput = dialogView.findViewById(R.id.weightInput);
        TextInputEditText heightInput = dialogView.findViewById(R.id.heightInput);

        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.add_conditions_record)
                .setView(dialogView)
                .setPositiveButton(R.string.add, (dialog, which) -> {
                    Float weight = Float.parseFloat(Objects.requireNonNull(weightInput.getText()).toString().trim());
                    Float height = Float.parseFloat(Objects.requireNonNull(heightInput.getText()).toString().trim());

                    if (weight != 0 && height != 0) {
                        UserConditions userConditions = new UserConditions();
                        userConditions.setWeight(weight);
                        userConditions.setHeight(height);
                        userConditions.setCheckupDate(new Date());
                        viewModel.insert(userConditions);
                        Toast.makeText(getContext(), R.string.record_added, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }
}
