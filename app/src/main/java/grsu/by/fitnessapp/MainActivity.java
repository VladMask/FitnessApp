package grsu.by.fitnessapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import grsu.by.fitnessapp.fragments.ExercisesFragment;
import grsu.by.fitnessapp.fragments.OnboardingFragment;
import grsu.by.fitnessapp.fragments.ProgressFragment;
import grsu.by.fitnessapp.fragments.WorkoutsFragment;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "fitness_prefs";
    private static final String KEY_ONBOARDING_COMPLETED = "onboarding_completed";

    private final Fragment workoutsFragment = new WorkoutsFragment();
    private final Fragment exercisesFragment = new ExercisesFragment();
    private final Fragment progressFragment = new ProgressFragment();
    private Fragment activeFragment = workoutsFragment;

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNav);

        if (isOnboardingCompleted()) {
            setupMainFragments();
        } else {
            showOnboarding();
        }
    }

    private boolean isOnboardingCompleted() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false);
    }

    private void setOnboardingCompleted() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, true).apply();
    }

    private void setupMainFragments() {
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

        bottomNav.setVisibility(View.VISIBLE);
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

    private void showOnboarding() {
        bottomNav.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new OnboardingFragment())
                .commit();
    }

    public void onOnboardingComplete() {
        setOnboardingCompleted();
        setupMainFragments();
    }
}
