package grsu.by.fitnessapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "age")
    private Byte age;

    @ColumnInfo(name = "weight")
    private Float weight;

    @ColumnInfo(name = "height")
    private Float height;

    @ColumnInfo(name = "checkup_Date")
    private Date checkupDate;

    public String getFormattedCheckupDate() {
        return new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(this.checkupDate);
    }
}

