package grsu.by.fitnessapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

import grsu.by.fitnessapp.MainActivity;
import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.database.AppDatabase;
import grsu.by.fitnessapp.database.dao.UserConditionsDao;
import grsu.by.fitnessapp.database.entity.UserConditions;
import grsu.by.fitnessapp.preferences.UserPreferences;

public class OnboardingFragment extends Fragment {

    private UserConditionsDao userConditionsDao;

    public static OnboardingFragment newInstance() {
        return new OnboardingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);

        TextInputLayout ageInput = view.findViewById(R.id.ageInput);
        TextInputLayout weightInput = view.findViewById(R.id.weightInput);
        TextInputLayout heightInput = view.findViewById(R.id.heightInput);
        RadioGroup genderGroup = view.findViewById(R.id.genderGroup);
        MaterialButton startButton = view.findViewById(R.id.startButton);

        Context context = requireContext();
        this.userConditionsDao = AppDatabase.getInstance(context).userConditionsDao();

        startButton.setOnClickListener(v -> {
            try {
                int age = Integer.parseInt(ageInput.getEditText().getText().toString().trim());
                float weight = Float.parseFloat(weightInput.getEditText().getText().toString().trim());
                float height = Float.parseFloat(heightInput.getEditText().getText().toString().trim());
                int checkedId = genderGroup.getCheckedRadioButtonId();

                if (checkedId == -1) {
                    Toast.makeText(context, context.getString(R.string.choose_gender), Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedGender = view.findViewById(checkedId);
                String gender = selectedGender.getText().toString();

                UserPreferences preferences = new UserPreferences(context);
                preferences.saveUserInfo(age, weight, height, gender);

                UserConditions firstEntry = new UserConditions();
                firstEntry.setAge((byte) age);
                firstEntry.setCheckupDate(new Date());
                firstEntry.setWeight(weight);
                firstEntry.setHeight(height);

                new Thread(() -> userConditionsDao.insert(firstEntry)).start();


                preferences.setFirstLaunchCompleted();
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .remove(this)
                        .commit();
                ((MainActivity) requireActivity()).onOnboardingComplete();

            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), context.getString(R.string.please_enter_correct_data), Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }

}