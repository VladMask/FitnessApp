package grsu.by.fitnessapp.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity(tableName = "exercises")
public class Exercise implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public String name;
    public String category;
}


