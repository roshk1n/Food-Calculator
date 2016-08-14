package com.example.roshk1n.foodcalculator.fragments;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.activities.MainActivity;
import com.example.roshk1n.foodcalculator.adapters.RecyclerDiaryAdapter;
import com.example.roshk1n.foodcalculator.presenters.DiaryPresenterImpl;
import com.example.roshk1n.foodcalculator.Views.DiaryView;
import com.example.roshk1n.foodcalculator.responseAdapter.CallbackDiaryAdapter;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.utils.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


import java.util.Calendar;
import java.util.Date;

public class DiaryFragment extends Fragment implements DiaryView, CallbackDiaryAdapter, DatePickerDialog.OnDateSetListener{

    private DiaryPresenterImpl diaryPresenter;
    private Day day;
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
    private CoordinatorLayout coordinatorLayout;
    private CoordinatorLayout HintAddFoodLayout;

    public DiaryFragment() { }

    public static DiaryFragment newInstance() { return new DiaryFragment(); }

    public static DiaryFragment newInstance(long date) {
        DiaryFragment diaryFragment = new DiaryFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("date", date);
        diaryFragment.setArguments(bundle);
        return diaryFragment;
    }

    public static DiaryFragment newInstance(int ndbno) {
        DiaryFragment diaryFragment = new DiaryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ndbno", ndbno);
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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diary, container, false);

        initUI();

        ((MainActivity)view.getContext()).setUpDrawerMenu();
        ((MainActivity)view.getContext()).enableMenuSwipe();

        if(getArguments() != null) {
            diaryPresenter.setDate(new Date(getArguments().getLong("date")));
        }

        day = diaryPresenter.loadDay();
        date_tv.setText(diaryPresenter.getDateString());
        diaryPresenter.getGoalCalories();
        diaryPresenter.calculateCalories();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerDiaryAdapter(day.getFoods(),this);
        mRecyclerView.setAdapter(mAdapter);

        addFoodFab.show();

        follow_day_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaryPresenter.setFollowDate();
                Utils.navigateToFragmentCustom(getActivity().getSupportFragmentManager(),
                        R.id.fragment_conteiner,
                        DiaryFragment.newInstance(diaryPresenter.getDate().getTime()),
                        R.anim.slide_in_right_enter,
                        R.anim.slide_in_right_exit,
                        false
                        );
            }
        });

        next_day_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaryPresenter.setNextDate();
                Utils.navigateToFragmentCustom(getActivity().getSupportFragmentManager(),
                        R.id.fragment_conteiner,
                        DiaryFragment.newInstance(diaryPresenter.getDate().getTime()),
                        R.anim.slide_in_left_enter,
                        R.anim.slide_in_left_exit,
                        false
                );
            }
        });

        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Food removedFood = day.getFoods().remove(viewHolder.getAdapterPosition());
                makeSnackBarAction(viewHolder.getAdapterPosition(),removedFood);
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        addFoodFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFoodFab.hide();
                hideHintAddAnim();
                Utils.navigateToFragment(getActivity().getSupportFragmentManager(),
                        R.id.fragment_conteiner,
                        SearchFragment.newInstance(diaryPresenter.getDate().getTime()),
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                        true);
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Date date = new Date();
        date.setMonth(monthOfYear);
        date.setDate(dayOfMonth);
        date.setYear(year-1900);

        diaryPresenter.setDate(date);
        String str = diaryPresenter.getDateString();
        date_tv.setText(str);

        day = diaryPresenter.loadDay();
        mAdapter = new RecyclerDiaryAdapter(day.getFoods(),this); //need new Recycler because load new foods
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateCalories(String eat, String remaining, int color) {
        eat_daily_calories_tv.setText(eat);
        remaining_calories_tv.setText(remaining);
        remaining_calories_tv.setTextColor(color);
    }

    @Override
    public void setGoalCalories(String goalCalories) {
        goal_calories_tv.setText(goalCalories);
    }

    @Override
    public void showDialog(String remaining, int checkLimit) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Limit of calories"); //TODO text for dialog
        if(checkLimit == 2) alertDialog.setMessage(getString(R.string.test));

        if (checkLimit == 3) alertDialog.setMessage("    You reached the limit today." +
                "\n    Recommend will not eat today :(");

        if(checkLimit == 4) alertDialog.setMessage("     You exceeded the limit greatly today!");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void hideHintAddAnim() {
        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext()
                ,R.anim.hide_hint_add_food);
        hintCircleAddFood.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                HintAddFoodLayout.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void showHintAddAnim() {
        Animation animation1 = AnimationUtils.loadAnimation(getActivity().getApplicationContext()
                ,R.anim.show_hint_add_food);
        hintCircleAddFood.startAnimation(animation1);
        HintAddFoodLayout.setVisibility(View.VISIBLE);

  /*      final Animation animation = new AlphaAnimation(1, 0.6f);  // light fab
        animation.setDuration(800);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(4);
        animation.setRepeatMode(Animation.REVERSE);
        addFoodFab.startAnimation(animation);*/
    }

    private void makeSnackBarAction(final int position, final Food removedFood) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item was removed successfully.", Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {
           @Override
           public void onDismissed(Snackbar snackbar, int event) {
               super.onDismissed(snackbar, event);
               if(event==Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                   diaryPresenter.removeFoodDB(position);
                   mAdapter.notifyItemRemoved(position);
                   diaryPresenter.calculateCalories();

               }
           }
       }).setAction("Undo", new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               day.getFoods().add(position,removedFood);
               mAdapter.notifyItemInserted(position);
           }
       }).setActionTextColor(Color.YELLOW);
        snackbar.show();
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
        HintAddFoodLayout = (CoordinatorLayout) getActivity().findViewById(R.id.hint_add_food_coordinator);
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Diary");
    }

    public void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setAccentColor(getActivity().getResources().getColor(R.color.colorPrimary));
        datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog" );
    }

    @Override
    public void navigateToInfoFood(Food food) {
        Utils.navigateToFragment(getActivity().getSupportFragmentManager(),
                R.id.fragment_conteiner,
                InfoFoodFragment.newInstance(food),
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                true);
    }
}
