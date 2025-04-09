package grsu.by.fitnessapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import grsu.by.fitnessapp.fragments.ExercisesFragment;
import grsu.by.fitnessapp.fragments.ProgressFragment;
import grsu.by.fitnessapp.fragments.WorkoutsFragment;

public class MainActivity extends AppCompatActivity {

    private void loadFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.workouts_layout) {
                selectedFragment = new WorkoutsFragment();
            } else if (id == R.id.exercises_layout) {
                selectedFragment = new ExercisesFragment();
            } else if (id == R.id.progress_layout) {
                selectedFragment = new ProgressFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }

            return false;
        });

        loadFragment(new WorkoutsFragment());
    }
}