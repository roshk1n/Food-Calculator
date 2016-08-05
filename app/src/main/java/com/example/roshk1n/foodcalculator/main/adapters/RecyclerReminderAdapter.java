package com.example.roshk1n.foodcalculator.main.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.fragments.remiders.ResponseAdapter;
import com.example.roshk1n.foodcalculator.realm.ReminderReaml;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by roshk1n on 8/3/2016.
 */
public class RecyclerReminderAdapter extends RecyclerView.Adapter<RecyclerReminderAdapter.ViewHolder> {

    private RealmList<ReminderReaml> reminderReamls;
    private ResponseAdapter responseAdapter;
    private Realm realm = Realm.getDefaultInstance();
    private View v;

    public RecyclerReminderAdapter(RealmList<ReminderReaml> reminderReamls, ResponseAdapter responseAdapter) {
        this.reminderReamls = reminderReamls;
        this.responseAdapter = responseAdapter;
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
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Date  date = new Date(reminderReamls.get(position).getTime());
        int minute= date.getMinutes();
        final int hour = date.getHours();
        String time = (hour < 10 ? "0" + hour : hour)+":"+(minute < 10 ? "0" + minute : minute);

        holder.title_reminder_tv.setText(reminderReamls.get(position).getName());
        holder.time_reminder_tv.setText(time);
        holder.reminder_switch.setChecked(reminderReamls.get(position).getState());

        holder.reminder_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseAdapter.createPicker(holder.getAdapterPosition()
                        ,reminderReamls.get(holder.getAdapterPosition()).getName());
            }
        });

        holder.reminder_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        reminderReamls.get(holder.getAdapterPosition()).setState(isChecked);
                        responseAdapter.updateSwitch(isChecked, holder.getAdapterPosition());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminderReamls.size();
    }
}
