package com.example.roshk1n.foodcalculator.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.responseAdapter.CallbackReminderAdapter;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Reminder;

import java.util.ArrayList;
import java.util.Date;


public class RecyclerReminderAdapter extends RecyclerView.Adapter<RecyclerReminderAdapter.ViewHolder> {
    private ArrayList<Reminder> reminders;
    private CallbackReminderAdapter callbackReminderAdapter;
    private View v;

    public RecyclerReminderAdapter(ArrayList<Reminder> reminders, CallbackReminderAdapter callbackReminderAdapter) {
        this.reminders = reminders;
        this.callbackReminderAdapter = callbackReminderAdapter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_reminder_tv;
        public TextView time_reminder_tv;
        public Switch reminder_switch;
        public CardView reminder_cv;

        public ViewHolder(View v) {
            super(v);

            title_reminder_tv = (TextView) v.findViewById(R.id.title_reminders_tv);
            time_reminder_tv = (TextView) v.findViewById(R.id.time_reminders_tv);
            reminder_switch = (Switch) v.findViewById(R.id.reminder_switch);
            reminder_cv = (CardView) v.findViewById(R.id.item_reminder_card_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_reminder,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Date  date = new Date(reminders.get(position).getTime());
        int minute= date.getMinutes();
        final int hour = date.getHours();
        String time = (hour < 10 ? "0" + hour : hour)+":"+(minute < 10 ? "0" + minute : minute);

        holder.title_reminder_tv.setText(reminders.get(position).getName());
        holder.time_reminder_tv.setText(time);
        holder.reminder_switch.setChecked(reminders.get(position).getState());

        holder.reminder_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackReminderAdapter.createPicker(holder.getAdapterPosition()
                        , reminders.get(holder.getAdapterPosition()).getName());
            }
        });

        holder.reminder_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callbackReminderAdapter.updateSwitch(isChecked,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }
}
