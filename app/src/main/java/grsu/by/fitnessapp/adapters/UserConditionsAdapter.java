package grsu.by.fitnessapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import grsu.by.fitnessapp.R;
import grsu.by.fitnessapp.database.entity.UserConditions;
import lombok.Setter;

public class UserConditionsAdapter extends RecyclerView.Adapter<UserConditionsAdapter.UserConditionsViewHolder> {

    private List<UserConditions> userConditionsList = new ArrayList<>();
    @Setter
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(UserConditions item);
    }

    public void setData(List<UserConditions> data) {
        this.userConditionsList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserConditionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_condition, parent, false);
        return new UserConditionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserConditionsViewHolder holder, int position) {
        UserConditions item = userConditionsList.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String formattedDate = sdf.format(item.getCheckupDate());

        Context context = holder.itemView.getContext();

        holder.checkupDateText.setText(String.format("%s%s", context.getString(R.string.date_with_colon), formattedDate));
        holder.weightText.setText(String.format("%s%s", context.getString(R.string.weight_with_colon), item.getWeight()));
        holder.heightText.setText(String.format("%s%s", context.getString(R.string.height_with_colon), item.getHeight()));

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userConditionsList.size();
    }

    static class UserConditionsViewHolder extends RecyclerView.ViewHolder {
        TextView checkupDateText, weightText, heightText;
        ImageButton deleteButton;

        public UserConditionsViewHolder(@NonNull View itemView) {
            super(itemView);
            checkupDateText = itemView.findViewById(R.id.checkupDateText);
            weightText = itemView.findViewById(R.id.weightText);
            heightText = itemView.findViewById(R.id.heightText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}

