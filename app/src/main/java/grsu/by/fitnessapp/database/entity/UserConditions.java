package grsu.by.fitnessapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
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
public class UserConditions implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Byte age;
    private Short weight;
    private Short height;
    private Boolean gender;

    @ColumnInfo(name = "checkup_Date")
    private Date checkupDate;
}

