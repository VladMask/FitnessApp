package grsu.by.fitnessapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import grsu.by.fitnessapp.database.converters.Converters;
import grsu.by.fitnessapp.database.dao.ExerciseDao;
import grsu.by.fitnessapp.database.dao.ExerciseWorkloadDao;
import grsu.by.fitnessapp.database.dao.NotificationDao;
import grsu.by.fitnessapp.database.dao.UserConditionsDao;
import grsu.by.fitnessapp.database.dao.WorkoutDao;
import grsu.by.fitnessapp.database.entity.Exercise;
import grsu.by.fitnessapp.database.entity.ExerciseWorkload;
import grsu.by.fitnessapp.database.entity.Notification;
import grsu.by.fitnessapp.database.entity.UserConditions;
import grsu.by.fitnessapp.database.entity.Workout;

@Database(
        entities = {
                Exercise.class,
                ExerciseWorkload.class,
                Workout.class,
                Notification.class,
                UserConditions.class
        },
        version = 1
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "fitness_app_db";
    private static volatile AppDatabase instance;

    public abstract ExerciseDao exerciseDao();
    public abstract WorkoutDao workoutDao();
    public abstract ExerciseWorkloadDao exerciseWorkloadDao();
    public abstract NotificationDao notificationDao();
    public abstract UserConditionsDao userConditionsDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME)
                    .build();
        }
        return instance;
    }

}
