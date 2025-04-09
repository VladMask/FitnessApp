package grsu.by.fitnessapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FrameLayout mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = findViewById(R.id.mainFragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            mainFragment.removeAllViews();

            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View newView = null;

            if (id == R.id.workouts_layout) {
                newView = inflater.inflate(R.layout.workouts_layout, mainFragment, false);
            } else if (id == R.id.exercise_progress_layout) {
                newView = inflater.inflate(R.layout.exercise_progress_layout, mainFragment, false);
            } else if (id == R.id.conditions_progress_layout) {
                newView = inflater.inflate(R.layout.conditions_progress_layout, mainFragment, false);
            }

            if (newView != null) {
                mainFragment.addView(newView);
                return true;
            }

            return false;
        });

        View defaultView = LayoutInflater.from(this).inflate(R.layout.workouts_layout, mainFragment, false);
        mainFragment.addView(defaultView);
    }
}
