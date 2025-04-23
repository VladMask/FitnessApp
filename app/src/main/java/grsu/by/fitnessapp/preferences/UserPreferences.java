package grsu.by.fitnessapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_AGE = "age";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_FIRST_LAUNCH = "is_first_launch";

    private final SharedPreferences prefs;


    public UserPreferences(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isFirstLaunch() {
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public void setFirstLaunchCompleted() {
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply();
    }

    public void saveUserInfo(int age, float weight, float height, String gender) {
        prefs.edit()
                .putInt(KEY_AGE, age)
                .putFloat(KEY_WEIGHT, weight)
                .putFloat(KEY_HEIGHT, height)
                .putString(KEY_GENDER, gender)
                .apply();
    }

    public float getWeight() {
        return prefs.getFloat(KEY_WEIGHT, 0f);
    }

    public float getHeight() {
        return prefs.getFloat(KEY_HEIGHT, 0f);
    }

    public void updateWeightAndHeight(float weight, float height) {
        prefs.edit()
                .putFloat(KEY_WEIGHT, weight)
                .putFloat(KEY_HEIGHT, height)
                .apply();
    }
}

