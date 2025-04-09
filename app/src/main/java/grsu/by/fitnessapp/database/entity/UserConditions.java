package grsu.by.fitnessapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

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
@Entity
public class UserConditions {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    public Byte age;
    public Short weight;
    public Short height;
    public Boolean gender;

    @ColumnInfo(name = "checkup_Date")
    public Date checkupDate;
}

