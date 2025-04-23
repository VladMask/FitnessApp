package grsu.by.fitnessapp.fragments;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.adapters.UserConditionsAdapter;
import grsu.by.fitnessapp.database.entity.UserConditions;
import grsu.by.fitnessapp.viewmodels.UserConditionsViewModel;

public class ProgressFragment extends Fragment {

    private UserConditionsAdapter adapter;

    public ProgressFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.conditionsRecyclerView);

        adapter = new UserConditionsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        UserConditionsViewModel viewModel = new ViewModelProvider(this).get(UserConditionsViewModel.class);

        viewModel.getAllUserConditions().observe(getViewLifecycleOwner(), conditions -> {
            if (conditions != null && !conditions.isEmpty()) {
                showWeightProgressChart(conditions);
            }
        });

        viewModel.getAllUserConditions().observe(getViewLifecycleOwner(), userConditions -> {
            if (userConditions != null && !userConditions.isEmpty()) {
                adapter.setData(userConditions);
                updateCurrentStats(userConditions);
            }
        });

        MaterialButton addButton = view.findViewById(R.id.btnAddConditions);
        addButton.setOnClickListener(v -> {
            showAddDialog();
        });

        return view;
    }

    private void showWeightProgressChart(List<UserConditions> conditions) {
        List<Entry> entries = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        Collections.sort(conditions, Comparator.comparing(UserConditions::getCheckupDate));

        for (int i = 0; i < conditions.size(); i++) {
            UserConditions condition = conditions.get(i);
            float weight = condition.getWeight();

            String dateString = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(condition.getCheckupDate());
            dates.add(dateString);
            entries.add(new Entry(i, weight));
        }

        LineDataSet dataSet = new LineDataSet(entries, getContext().getString(R.string.weight_kg));
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextSize(10f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);

        LineChart chart = requireView().findViewById(R.id.progressChart);
        chart.setData(lineData);
        chart.getDescription().setText(getContext().getString(R.string.weight_dynamics));

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setLabelCount(6, false);
        yAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);

        chart.invalidate();
    }

    private void updateCurrentStats(List<UserConditions> conditions) {
        if (conditions != null && !conditions.isEmpty()) {
            UserConditions latestCondition = conditions.get(conditions.size() - 1);
            float weight = latestCondition.getWeight();
            float height = latestCondition.getHeight();
            float bmi = calculateBMI(weight, height);

            TextView weightText = getView().findViewById(R.id.weightValue);
            TextView heightText = getView().findViewById(R.id.heightValue);
            TextView bmiText = getView().findViewById(R.id.bmiValue);

            weightText.setText(String.format(Locale.getDefault(), "%.1f кг", weight));
            heightText.setText(String.format(Locale.getDefault(), "%.1f см", height));
            bmiText.setText(String.format(Locale.getDefault(), "%.2f", bmi));
        }
    }

    private float calculateBMI(float weight, float height) {
        height = height / 100;
        return weight / (height * height);
    }



    private void showAddDialog() {
        AddUserConditionsDialogFragment dialogFragment = new AddUserConditionsDialogFragment();
        dialogFragment.show(getFragmentManager(), "addUserConditionsDialog");
    }
}
