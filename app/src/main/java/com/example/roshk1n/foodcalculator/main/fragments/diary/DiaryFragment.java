package com.example.roshk1n.foodcalculator.main.fragments.diary;


import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.adapters.RecyclerDiaryAdapter;
import com.example.roshk1n.foodcalculator.main.fragments.search.SearchFragment;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;


import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmList;

public class DiaryFragment extends Fragment implements DiaryView{

    private DiaryPresenterImpl diaryPresenter;
    private RealmList<FoodRealm> foods;

    private Date date_add ;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerDiaryAdapter mAdapter;

    private View view;
    private TextView date_tv;
    private TextView goal_calories_tv;
    private TextView eat_daily_calories_tv;
    private TextView remaining_calories_tv;
    private ImageView follow_day_iv;
    private ImageView next_day_iv;
    private View hintCircleAddFood;
    private FloatingActionButton addFoodFab;
    
    private CoordinatorLayout coordinatorHintAdd;

    public DiaryFragment() { }

    public static DiaryFragment newInstance() { return new DiaryFragment(); }

    public static DiaryFragment newInstance(long date) {
        DiaryFragment diaryFragment = new DiaryFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("date", date);
        diaryFragment.setArguments(bundle);
        return diaryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        diaryPresenter = new DiaryPresenterImpl();
        diaryPresenter.setView(this);
        date_add = new Date();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diary, container, false);

        Log.d("My", "onCreateView");

        initUI();

        Bundle bundle = getArguments();

        if(bundle != null) {
           date_add = new Date(bundle.getLong("date"));
        }

        setData(date_add);

        foods = diaryPresenter.getFoods(diaryPresenter.getCurrentUserRealm(),date_add);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerDiaryAdapter(foods);
        mRecyclerView.setAdapter(mAdapter);

        addFoodFab.show();

        if(foods.size()==0) {
            showHintAddAmin();
        } else {
            hideHintAddAmin();
        }

        follow_day_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_add.setDate(date_add.getDate()-1);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right_enter,R.anim.slide_in_right_exit)
                        .replace(R.id.fragment_conteiner, DiaryFragment.newInstance(date_add.getTime()))
                        .commit();
            }
        });

        next_day_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_add.setDate(date_add.getDate()+1);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left_enter,R.anim.slide_in_left_exit)
                        .replace(R.id.fragment_conteiner, DiaryFragment.newInstance(date_add.getTime()))
                        .commit();
            }
        });

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
                addFoodFab.hide();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_conteiner, SearchFragment.newInstance(date_add.getTime()))
                        .addToBackStack(null)
                        .commit();
                hideHintAddAmin();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {
                    if (addFoodFab.isShown()) {
                        addFoodFab.hide();
                    }
                }
                else if (dy <0) {
                    if (!addFoodFab.isShown()) {
                        addFoodFab.show();
                    }
                }
            }
           /* @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && addFoodFab.isShown())
                    addFoodFab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    addFoodFab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }*/
        });
        return view;
    }

    @Override
    public void setData(Date date) {
        SimpleDateFormat format1=new SimpleDateFormat();
        format1.applyPattern("EEEE, dd MMMM");
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
        addFoodFab = (FloatingActionButton) getActivity().findViewById(R.id.addFood_fab);
        goal_calories_tv = (TextView) view.findViewById(R.id.goal_cal_diary_tv);
        eat_daily_calories_tv = (TextView) view.findViewById(R.id.eatdaily_cal_diary_tv);
        remaining_calories_tv = (TextView) view.findViewById(R.id.remaining_cal_diary_tv);
        follow_day_iv= (ImageView) view.findViewById(R.id.follow_day_iv);
        next_day_iv= (ImageView) view.findViewById(R.id.next_day_iv);
        hintCircleAddFood =  getActivity().findViewById(R.id.hint_add_food_view);
        coordinatorHintAdd = (CoordinatorLayout) getActivity().findViewById(R.id.hint_add_food_coordinator);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Diary");

    }

    void hideHintAddAmin() {
        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext()
                ,R.anim.hide_hint_add_food);
        hintCircleAddFood.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                coordinatorHintAdd.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    void showHintAddAmin() {

        Animation animation1 = AnimationUtils.loadAnimation(getActivity().getApplicationContext()
                ,R.anim.show_hint_add_food);
        hintCircleAddFood.startAnimation(animation1);
        coordinatorHintAdd.setVisibility(View.VISIBLE);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
