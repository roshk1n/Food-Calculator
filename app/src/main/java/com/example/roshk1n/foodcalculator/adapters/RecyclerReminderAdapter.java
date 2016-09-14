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
import java.util.Calendar;

public class RecyclerReminderAdapter extends RecyclerView.Adapter<RecyclerReminderAdapter.ViewHolder> {
    private ArrayList<Reminder> reminders;
    private CallbackReminderAdapter callbackReminderAdapter;
    private View v;

    public RecyclerReminderAdapter(ArrayList<Reminder> reminders, CallbackReminderAdapter callbackReminderAdapter) {
        this.reminders = reminders;
        this.callbackReminderAdapter = callbackReminderAdapter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            CompoundButton.OnCheckedChangeListener {
        private final CallbackReminderAdapter callbackReminderAdapter;
        private final TextView title_reminder_tv;
        private final TextView time_reminder_tv;
        private final Switch reminder_switch;
        private final CardView reminder_cv;
        private ArrayList<Reminder> reminders;

        public ViewHolder(View v, ArrayList<Reminder> reminders, CallbackReminderAdapter callbackReminderAdapter) {
            super(v);
            this.reminders = reminders;
            this.callbackReminderAdapter = callbackReminderAdapter;
            title_reminder_tv = (TextView) v.findViewById(R.id.title_reminders_tv);
            time_reminder_tv = (TextView) v.findViewById(R.id.time_reminders_tv);
            reminder_switch = (Switch) v.findViewById(R.id.reminder_switch);
            reminder_cv = (CardView) v.findViewById(R.id.item_reminder_card_view);
        }

        public void setUpListeners() {
            reminder_cv.setOnClickListener(this);
            reminder_switch.setOnCheckedChangeListener(this);
        }

        public void setData() {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(reminders.get(getAdapterPosition()).getTime());
            int minute= date.get(Calendar.MINUTE);
            final int hour = date.get(Calendar.HOUR_OF_DAY);
            String time = (hour < 10 ? "0" + hour : hour)+":"+(minute < 10 ? "0" + minute : minute);
            Reminder reminder = reminders.get(getAdapterPosition());
            title_reminder_tv.setText(reminder.getName());
            time_reminder_tv.setText(time);
            reminder_switch.setChecked(reminder.getState());
        }

        @Override
        public void onClick(View v) {
            if (v == reminder_cv) {
                callbackReminderAdapter.createPicker(
                        getAdapterPosition(),
                        reminders.get(getAdapterPosition()).getName());
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            callbackReminderAdapter.updateSwitch(isChecked, getAdapterPosition());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_of_reminder,parent,false);
        return new ViewHolder(v, reminders, callbackReminderAdapter);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setData();
        holder.setUpListeners();
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }
}
