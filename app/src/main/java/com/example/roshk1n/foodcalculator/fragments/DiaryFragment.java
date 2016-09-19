package com.example.roshk1n.foodcalculator.fragments;

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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.OnSwipeTouchListener;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.interfaces.OnFragmentListener;
import com.example.roshk1n.foodcalculator.adapters.RecyclerDiaryAdapter;
import com.example.roshk1n.foodcalculator.presenters.DiaryPresenterImpl;
import com.example.roshk1n.foodcalculator.views.DiaryView;
import com.example.roshk1n.foodcalculator.interfaces.responseAdapter.CallbackDiaryAdapter;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Day;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.utils.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class DiaryFragment extends Fragment implements DiaryView, CallbackDiaryAdapter,
        DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private static final String DATE_KEY = "date";
    private DiaryPresenterImpl diaryPresenter;
    private Day day;
    private RecyclerView mRecyclerView;
    private RecyclerDiaryAdapter mAdapter;
    private OnFragmentListener mFragmentListener;
    private boolean checkUndo = true;
    private boolean checkDayRemote = false;

    private ItemTouchHelper itemTouchHelper;
    private View view;
    private TextView dateTv;
    private TextView goalCaloriesTv;
    private TextView eatDailyCaloriesTv;
    private TextView remainingCaloriesTv;
    private ImageView previousDayIv;
    private ImageView nextDayIv;
    private View hintCircleAddFood;
    private Snackbar snackbar;
    private FloatingActionButton addFoodFab;
    private CoordinatorLayout coordinatorLayout;
    private CoordinatorLayout HintAddFoodLayout;
    private LinearLayout limitDiaryLayout;
    private LinearLayout dateDiaryLayout;

    public static DiaryFragment newInstance() {
        return new DiaryFragment();
    }

    public static DiaryFragment newInstance(long date) {
        DiaryFragment diaryFragment = new DiaryFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(DATE_KEY, date);
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
        checkDayRemote = false;
        initUI();

        if (mFragmentListener != null) {
            mFragmentListener.setTitle(getString(R.string.diary));
            mFragmentListener.setDrawerMenu();
            mFragmentListener.enableMenuSwipe();
        }

        if (getArguments() != null) {
            diaryPresenter.setDate(getArguments().getLong(DATE_KEY));
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        diaryPresenter.loadDay();
        diaryPresenter.calculateCalories();
        dateTv.setText(diaryPresenter.getDateString());

        addFoodFab.show();

        previousDayIv.setOnClickListener(this);
        nextDayIv.setOnClickListener(this);
        dateTv.setOnClickListener(this);
        addFoodFab.setOnClickListener(this);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
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

        OnSwipeTouchListener touchListener = new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                previousDay();
            }

            public void onSwipeLeft() {
                nextDay();
            }
        };

        limitDiaryLayout.setOnTouchListener(touchListener);
        dateDiaryLayout.setOnTouchListener(touchListener);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        hideHintAddAnim();
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
        mFragmentListener = null;
        diaryPresenter.destroy();
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        if (v == previousDayIv) {
            previousDay();

        } else if (v == nextDayIv) {
            nextDay();

        } else if (v == dateTv) {
            showDatePicker();

        } else if (v == addFoodFab) {
            addFoodFab.hide();
            hideHintAddAnim();

            Utils.navigateToFragment(getActivity().getSupportFragmentManager(),
                    R.id.fragment_conteiner,
                    TabSearchFragment.newInstance(diaryPresenter.getDate().getTime().getTime()),
                    FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                    true);
            addFoodFab.hide();
        }
    }

    @Override
    public void navigateToInfoFood(Food food) {
        Utils.navigateToFragment(getActivity().getSupportFragmentManager(),
                R.id.fragment_conteiner,
                InfoFoodFragment.newInstance(food),
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                true);
        addFoodFab.hide();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.MONTH, monthOfYear);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date.set(Calendar.YEAR, year);

        Utils.navigateToFragment(getActivity().getSupportFragmentManager(),
                R.id.fragment_conteiner,
                DiaryFragment.newInstance(date.getTime().getTime()),
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                false);
    }

    @Override
    public void updateCalories(String eat, String remaining, int color) {
        eatDailyCaloriesTv.setText(eat);
        remainingCaloriesTv.setText(remaining);
        remainingCaloriesTv.setTextColor(color);
    }

    @Override
    public void setGoalCalories(String goalCalories) {
        goalCaloriesTv.setText(goalCalories);
    }

    @Override
    public void showDialog(String remaining, int checkLimit) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.title_limit));

        if (checkLimit == 2) alertDialog.setMessage(getString(R.string.alert2, remaining));

        if (checkLimit == 3) alertDialog.setMessage(getString(R.string.alert3));

        if (checkLimit == 4) alertDialog.setMessage(getString(R.string.alert4));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
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
    public void setDay(Day day) {
        diaryPresenter.getGoalCalories();
        if (checkDayRemote)
            diaryPresenter.calculateCalories();
        this.day = day;
        mAdapter = new RecyclerDiaryAdapter(day.getFoods(), this);
        mRecyclerView.setAdapter(mAdapter);
        checkDayRemote = true;
    }

    @Override
    public void showHintAddAnim() {
        if (isVisible()) {
            Animation animation1 = AnimationUtils.loadAnimation(getActivity().getApplicationContext()
                    , R.anim.show_hint_add_food);
            hintCircleAddFood.startAnimation(animation1);
            HintAddFoodLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(diaryPresenter.getDate().getTimeInMillis());
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setAccentColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }

    private void makeSnackBarAction(final int position, final Food removedFood) {
        checkUndo = true;
        snackbar = Snackbar
                .make(coordinatorLayout,
                        R.string.item_was_removed,
                        Snackbar.LENGTH_LONG)
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                            diaryPresenter.removeFoodDB(position, removedFood.getTime());
                        }
                    }
                }).setAction(R.string.undo, new View.OnClickListener() {
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

    private void previousDay() {
        hideHintAddAnim();
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        } else {
            diaryPresenter.setPreviousDate();
            Utils.navigateToFragmentCustom(getActivity().getSupportFragmentManager(),
                    R.id.fragment_conteiner,
                    DiaryFragment.newInstance(diaryPresenter.getDate().getTime().getTime()),
                    R.anim.slide_in_right_enter,
                    R.anim.slide_in_right_exit,
                    false);
        }
    }

    private void nextDay() {
        hideHintAddAnim();
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();

        } else {
            diaryPresenter.setNextDate();
            Utils.navigateToFragmentCustom(getActivity().getSupportFragmentManager(),
                    R.id.fragment_conteiner,
                    DiaryFragment.newInstance(diaryPresenter.getDate().getTime().getTime()),
                    R.anim.slide_in_left_enter,
                    R.anim.slide_in_left_exit,
                    false);
        }
    }

    private void initUI() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_meal);
        dateTv = (TextView) view.findViewById(R.id.date_diary_tv);
        addFoodFab = (FloatingActionButton) getActivity().findViewById(R.id.addFood_fab);
        goalCaloriesTv = (TextView) view.findViewById(R.id.goal_cal_diary_tv);
        eatDailyCaloriesTv = (TextView) view.findViewById(R.id.eatdaily_cal_diary_tv);
        remainingCaloriesTv = (TextView) view.findViewById(R.id.remaining_cal_diary_tv);
        previousDayIv = (ImageView) view.findViewById(R.id.previous_day_iv);
        nextDayIv = (ImageView) view.findViewById(R.id.next_day_iv);
        hintCircleAddFood = getActivity().findViewById(R.id.hint_add_food_view);
        HintAddFoodLayout = (CoordinatorLayout) getActivity().findViewById(R.id.hint_add_food_coordinator);
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
        limitDiaryLayout = (LinearLayout) view.findViewById(R.id.limit_diary_layout);
        dateDiaryLayout = (LinearLayout) view.findViewById(R.id.date_diary_layout);
    }
}
