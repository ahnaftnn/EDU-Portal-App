package com.ahnaftn.eduportal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {
    Context context;
    List<RoutineItem> routineList;

    public RoutineAdapter(Context context, List<RoutineItem> routineList){
        this.context= context;
        this.routineList = routineList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView dayText, timeText, roomText, facultyText,courseText;
        public ViewHolder(View view){
             super(view);
             courseText=view.findViewById(R.id.courseText);
             dayText=view.findViewById(R.id.dayText);
             timeText=view.findViewById(R.id.timeText);
             roomText= view.findViewById(R.id.roomText);
             facultyText=view.findViewById(R.id.facultyText);
        }
    }

    @NonNull
    @Override
    public RoutineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_routine,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoutineItem item = routineList.get(position);
        holder.courseText.setText(item.course);
        holder.dayText.setText("Day: "+item.day);
        holder.timeText.setText("Time: "+item.time);
        holder.roomText.setText("Room: "+item.room);
        holder.facultyText.setText(item.faculty);
    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }

}