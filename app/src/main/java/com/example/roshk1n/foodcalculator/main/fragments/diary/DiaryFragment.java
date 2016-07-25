package com.example.roshk1n.foodcalculator.main.fragments.diary;


import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.OnSwipeTouchListener;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.MainActivity;
import com.example.roshk1n.foodcalculator.main.adapters.RecyclerDiaryAdapter;
import com.example.roshk1n.foodcalculator.main.fragments.remiders.RemindersFragment;
import com.example.roshk1n.foodcalculator.main.fragments.search.SearchFragment;
import com.example.roshk1n.foodcalculator.realm.DayRealm;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.realm.UserRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class DiaryFragment extends Fragment implements DiaryView{

    private DiaryPresenterImpl diaryPresenter;
    private RealmList<FoodRealm> foods;

    private Date date_add = new Date();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerDiaryAdapter mAdapter;

    private View view;
    private TextView date_tv;
    private TextView goal_calories_tv;
    private TextView eat_daily_calories_tv;
    private TextView remaining_calories_tv;

    private FloatingActionButton addFoodFab;


    public DiaryFragment() { }

    public static DiaryFragment newInstance() {
        return new DiaryFragment();
    }

    public static DiaryFragment newInstance(Food food) {
        DiaryFragment diaryFragment = new DiaryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("food", food);
        diaryFragment.setArguments(bundle);
        return diaryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        diaryPresenter = new DiaryPresenterImpl();
        diaryPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diary, container, false);

        initUI();

        SimpleDateFormat format1=new SimpleDateFormat("EEEE, d MMM.");
        date_tv.setText(format1.format(date_add));

        foods = diaryPresenter.getFoods(diaryPresenter.getCurrentUserRealm(),date_add);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerDiaryAdapter(foods);
        mRecyclerView.setAdapter(mAdapter);

        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaryPresenter.showDatePicker(DiaryFragment.this);
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                diaryPresenter.removeFood(viewHolder.getAdapterPosition(),date_add);
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        addFoodFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat format2=new SimpleDateFormat("EEEE/dd/MMMM/yyyy");
                String finalDay=format2.format(date_add);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_conteiner, SearchFragment.newInstance(finalDay))
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
    @Override
    public void setDate(Date date) {
        SimpleDateFormat format1=new SimpleDateFormat();
        format1.applyPattern("EEEE, dd MMM.");
        date_add = date;
        String str = format1.format(date);
        date_tv.setText(str);
        goal_calories_tv.setText("1600");
        eat_daily_calories_tv.setText("0");
        remaining_calories_tv.setText("1600");
        foods = diaryPresenter.getFoods(diaryPresenter.getCurrentUserRealm(),date_add);
        mAdapter = new RecyclerDiaryAdapter(foods);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void updateCalories(int goal, int eat, int remaining) {
        goal_calories_tv.setText(String.valueOf(goal));
        eat_daily_calories_tv.setText(String.valueOf(eat));
        remaining_calories_tv.setText(String.valueOf(remaining));
    }

    void initUI() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_meal);
        date_tv = (TextView) view.findViewById(R.id.date_diary_tv);
        addFoodFab = (FloatingActionButton) view.findViewById(R.id.addFood_fab);
        goal_calories_tv = (TextView) view.findViewById(R.id.goal_cal_diary_tv);
        eat_daily_calories_tv = (TextView) view.findViewById(R.id.eatdaily_cal_diary_tv);
        remaining_calories_tv = (TextView) view.findViewById(R.id.remaining_cal_diary_tv);
    }
}
