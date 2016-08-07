package com.example.roshk1n.foodcalculator.main.fragments.diary;


import android.content.DialogInterface;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
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
    private TextView remaining_field;
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

        initUI();

        Bundle bundle = getArguments();

        if(bundle != null) {
           date_add = new Date(bundle.getLong("date"));
        }

        setData(date_add);

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
                hideHintAddAmin();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_conteiner, SearchFragment.newInstance(date_add.getTime()))
                        .addToBackStack(null)
                        .commit();
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
        });
        return view;
    }

    @Override
    public void setData(Date date) {
        SimpleDateFormat format1 = new SimpleDateFormat();
        format1.applyPattern("EEEE, dd MMMM");
        date_add = date;
        String str = format1.format(date);
        date_tv.setText(str);
        goal_calories_tv.setText(R.string._1660);
        eat_daily_calories_tv.setText(R.string._0);
        remaining_calories_tv.setText(R.string._1660);
        foods = diaryPresenter.getFoods(diaryPresenter.getCurrentUserRealm(),date_add);
        mAdapter = new RecyclerDiaryAdapter(foods);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateCalories(int goal, int eat, int remaining, int checkLimit, int color) {
        goal_calories_tv.setText(String.valueOf(goal));
        eat_daily_calories_tv.setText(String.valueOf(eat));
        remaining_calories_tv.setText(String.valueOf(remaining));
        remaining_calories_tv.setTextColor(color);
        remaining_field.setTextColor(color);

        if (checkLimit!=1) { //if need dialog for limit
            showDialog(remaining,checkLimit);
        } else {
            remaining_field.setTextColor(getResources().getColor(R.color.colorSecondaryText));
        }
    }

    private void initUI() {
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
        remaining_field = (TextView) view.findViewById(R.id.remaining_cal_diary_field_tv);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Diary");

    }

    private void hideHintAddAmin() {
        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext()
                ,R.anim.hide_hint_add_food);
        hintCircleAddFood.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                coordinatorHintAdd.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void showHintAddAmin() {

        Animation animation1 = AnimationUtils.loadAnimation(getActivity().getApplicationContext()
                ,R.anim.show_hint_add_food);
        hintCircleAddFood.startAnimation(animation1);
        coordinatorHintAdd.setVisibility(View.VISIBLE);

        final Animation animation = new AlphaAnimation(1, 0.6f);
        animation.setDuration(800);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(4);
        animation.setRepeatMode(Animation.REVERSE);
        addFoodFab.startAnimation(animation);

    }

    private void showDialog(int remaining, int checklimit) {

        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Attention");
        if(checklimit==2) alertDialog.setMessage("     You almost reached the limit today." +
                "\n     You have "+remaining+" calories.");

        if (checklimit==3) alertDialog.setMessage("    You reached the limit today." +
                "\n    Recommend will not eat today :(");

        if(checklimit ==4 ) alertDialog.setMessage("     You exceeded the limit greatly today!");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

}
