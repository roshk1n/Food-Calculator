package com.example.roshk1n.foodcalculator.fragments;
//TODO fix bag with snackbar is show change day
//TODO need twice change day for see update remote
import android.content.Context;
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
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.interfaces.OnFragmentListener;
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

public class DiaryFragment extends Fragment implements DiaryView, CallbackDiaryAdapter,
        DatePickerDialog.OnDateSetListener, View.OnClickListener{

    private DiaryPresenterImpl diaryPresenter;
    private Day day;
    private boolean checkUndo = true;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerDiaryAdapter mAdapter;
    private OnFragmentListener mFragmentListener;

    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelper.SimpleCallback simpleItemTouchCallback;

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

    public DiaryFragment() {
    }

    public static DiaryFragment newInstance() {
        return new DiaryFragment();
    }

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

        if(mFragmentListener != null) {
            mFragmentListener.setDrawerMenu();
            mFragmentListener.enableMenuSwipe();
        }

        if (getArguments() != null) {
            diaryPresenter.setDate(new Date(getArguments().getLong("date")));
        }
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        diaryPresenter.loadDay();
        date_tv.setText(diaryPresenter.getDateString());

        addFoodFab.show();

        follow_day_iv.setOnClickListener(this);
        next_day_iv.setOnClickListener(this);
        date_tv.setOnClickListener(this);
        addFoodFab.setOnClickListener(this);

        simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return super.isItemViewSwipeEnabled();
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                Food removedFood = day.getFoods().remove(position);
                mAdapter.notifyItemRemoved(position);
                diaryPresenter.calculateCalories();
                makeSnackBarAction(position, removedFood);
            }
        };
        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (addFoodFab.isShown()) {
                        addFoodFab.hide();
                    }
                } else if (dy < 0) {
                    if (!addFoodFab.isShown()) {
                        addFoodFab.show();
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            mFragmentListener = (OnFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v == follow_day_iv) {
            diaryPresenter.setFollowDate();
            Utils.navigateToFragmentCustom(getActivity().getSupportFragmentManager(),
                    R.id.fragment_conteiner,
                    DiaryFragment.newInstance(diaryPresenter.getDate().getTime()),
                    R.anim.slide_in_right_enter,
                    R.anim.slide_in_right_exit,
                    false);

        } else if (v == next_day_iv) {
                diaryPresenter.setNextDate();
                Utils.navigateToFragmentCustom(getActivity().getSupportFragmentManager(),
                        R.id.fragment_conteiner,
                        DiaryFragment.newInstance(diaryPresenter.getDate().getTime()),
                        R.anim.slide_in_left_enter,
                        R.anim.slide_in_left_exit,
                        false);

        } else if (v == date_tv) {
            showDatePicker();

        } else if (v == addFoodFab) {
            addFoodFab.hide();
            hideHintAddAnim();
            Utils.navigateToFragment(getActivity().getSupportFragmentManager(),
                    R.id.fragment_conteiner,
                    TabSearchFragment.newInstance(diaryPresenter.getDate().getTime()),
                    FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                    true);
        }
    }

    @Override
    public void navigateToInfoFood(Food food) {
        Utils.navigateToFragment(getActivity().getSupportFragmentManager(),
                R.id.fragment_conteiner,
                InfoFoodFragment.newInstance(food),
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                true);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Date date = new Date();
        date.setMonth(monthOfYear);
        date.setDate(dayOfMonth);
        date.setYear(year - 1900);

        diaryPresenter.setDate(date);
        String str = diaryPresenter.getDateString();
        date_tv.setText(str);

        diaryPresenter.loadDay();
        mAdapter = new RecyclerDiaryAdapter(day.getFoods(), this); //need new Recycler because load new foods
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
        diaryPresenter.calculateCalories();
    }

    @Override
    public void showDialog(String remaining, int checkLimit) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.title_limit));

        if(checkLimit == 2) alertDialog.setMessage(getString(R.string.alert2,remaining));

        if (checkLimit == 3) alertDialog.setMessage(getString(R.string.alert3));

        if (checkLimit == 4) alertDialog.setMessage(getString(R.string.alert4));

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
                , R.anim.hide_hint_add_food);
        hintCircleAddFood.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                HintAddFoodLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    @Override
    public void setDay(Day day) {
        this.day = day;

        mAdapter = new RecyclerDiaryAdapter(day.getFoods(), this);
        mRecyclerView.setAdapter(mAdapter);

        diaryPresenter.getGoalCalories();
    }

    @Override
    public void showHintAddAnim() {
        Animation animation1 = AnimationUtils.loadAnimation(getActivity().getApplicationContext()
                , R.anim.show_hint_add_food);
        hintCircleAddFood.startAnimation(animation1);
        HintAddFoodLayout.setVisibility(View.VISIBLE);
    }

    private void makeSnackBarAction(final int position, final Food removedFood) {
        checkUndo = true;
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout,
                "Item was removed successfully.",
                Snackbar.LENGTH_LONG)
                .setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    diaryPresenter.removeFoodDB(position, removedFood.getTime());
                }

            }
        }).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUndo) {
                    day.getFoods().add(position, removedFood);
                    mAdapter.notifyItemInserted(position);
                    diaryPresenter.calculateCalories();
                    itemTouchHelper.attachToRecyclerView(mRecyclerView);
                    checkUndo = false;
                }
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
        follow_day_iv = (ImageView) view.findViewById(R.id.follow_day_iv);
        next_day_iv = (ImageView) view.findViewById(R.id.next_day_iv);
        hintCircleAddFood = getActivity().findViewById(R.id.hint_add_food_view);
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
        datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }
}
