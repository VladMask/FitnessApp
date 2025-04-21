package grsu.by.fitnessapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import grsu.by.fitnessapp.fragments.ExercisesFragment;
import grsu.by.fitnessapp.fragments.ProgressFragment;
import grsu.by.fitnessapp.fragments.WorkoutsFragment;

public class MainActivity extends AppCompatActivity {

    private final Fragment workoutsFragment = new WorkoutsFragment();
    private final Fragment exercisesFragment = new ExercisesFragment();
    private final Fragment progressFragment = new ProgressFragment();
    private Fragment activeFragment = workoutsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, progressFragment, "progressFragment").hide(progressFragment)
                .add(R.id.fragment_container, exercisesFragment, "exercisesFragment").hide(exercisesFragment)
                .add(R.id.fragment_container, workoutsFragment, "workoutsFragment")
                .commit();

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_workouts) {
                    switchFragment(workoutsFragment);
                    return true;
            }
            if (item.getItemId() == R.id.nav_exercises) {
                switchFragment(exercisesFragment);
                return true;
            }
            if (item.getItemId() == R.id.nav_progress) {
                switchFragment(progressFragment);
                return true;
            }
            return false;
        });
    }

    private void switchFragment(Fragment targetFragment) {
        if (activeFragment != targetFragment) {
            getSupportFragmentManager().beginTransaction()
                    .hide(activeFragment)
                    .show(targetFragment)
                    .commit();
            activeFragment = targetFragment;
        }
    }
}
