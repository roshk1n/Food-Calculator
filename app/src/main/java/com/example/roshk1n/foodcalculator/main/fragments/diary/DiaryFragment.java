package com.example.roshk1n.foodcalculator.main.fragments.diary;


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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.MainActivity;
import com.example.roshk1n.foodcalculator.main.adapters.RecyclerDiaryAdapter;
import com.example.roshk1n.foodcalculator.main.fragments.search.SearchFragment;
import com.example.roshk1n.foodcalculator.main.fragments.search.SearchView;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmList;

public class DiaryFragment extends Fragment implements DiaryView,  DatePickerDialog.OnDateSetListener{

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

        ((MainActivity)view.getContext()).setUpDrawerMenu();
        ((MainActivity)view.getContext()).enableMenuSwipe();

        Bundle bundle = getArguments();

        if(bundle != null) {
           date_add = new Date(bundle.getLong("date"));
        }

        foods = diaryPresenter.getFoods(date_add);
        date_tv.setText(diaryPresenter.dateToString(date_add));

        diaryPresenter.getGoalCalories();
        diaryPresenter.calculateCalories(date_add);

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

                makeSnackBar(new FoodRealm(),viewHolder.getAdapterPosition());
                /*diaryPresenter.removeFood(viewHolder.getAdapterPosition(),date_add);
                diaryPresenter.calculateCalories(date_add);
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());*/
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Date date = new Date();
        date.setMonth(monthOfYear);
        date.setDate(dayOfMonth);
        date.setYear(year-1900);

        date_add = date;
        String str = diaryPresenter.dateToString(date);
        date_tv.setText(str);

        foods = diaryPresenter.getFoods(date_add);
        mAdapter = new RecyclerDiaryAdapter(foods);
        mRecyclerView.setAdapter(mAdapter);

        if(foods.size()==0) {
            showHintAddAmin();
        } else {
            HintAddFoodLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateCalories(String eat, String remaining, int checkLimit, int color) {
        eat_daily_calories_tv.setText(eat);
        remaining_calories_tv.setText(remaining);
        remaining_calories_tv.setTextColor(color);
        remaining_field.setTextColor(color);

        if (checkLimit!=1) { //if need dialog for limit
            showDialog(remaining,checkLimit);
        } else {
            remaining_field.setTextColor(getResources().getColor(R.color.colorSecondaryText));
        }
    }

    @Override
    public void setGoalCalories(String goalCalories) {
        goal_calories_tv.setText(goalCalories);
    }

    @Override
    public void makeSnackBar(FoodRealm deleteFood, final int index) {
       Snackbar.make(coordinatorLayout, "Had a snack at Snackbar", Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {
           @Override
           public void onDismissed(Snackbar snackbar, int event) {
               super.onDismissed(snackbar, event);
               switch(event) {
                   case Snackbar.Callback.DISMISS_EVENT_ACTION: {
                       mAdapter.notifyDataSetChanged();

                   } break;

                   case Snackbar.Callback.DISMISS_EVENT_TIMEOUT: {
                       diaryPresenter.removeFood(index,date_add);
                       diaryPresenter.calculateCalories(date_add);
                       mAdapter.notifyItemRemoved(index);
                   } break;
               }
           }

           @Override
           public void onShown(Snackbar snackbar) {
               super.onShown(snackbar);
           }
       }).setAction("Undo", new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       }).setActionTextColor(Color.RED).show();
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
        remaining_field = (TextView) view.findViewById(R.id.remaining_cal_diary_field_tv);
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

    private void showDialog(String remaining, int checkLimit) {

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
                HintAddFoodLayout.setVisibility(View.GONE);
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
        HintAddFoodLayout.setVisibility(View.VISIBLE);

  /*      final Animation animation = new AlphaAnimation(1, 0.6f);  // light fab
        animation.setDuration(800);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(4);
        animation.setRepeatMode(Animation.REVERSE);
        addFoodFab.startAnimation(animation);*/

    }
}
