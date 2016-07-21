package com.example.roshk1n.foodcalculator.main.fragments.diary;


import android.os.Bundle;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roshk1n.foodcalculator.enums.MealOfDay;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.adapters.RecyclerDiaryAdapter;
import com.example.roshk1n.foodcalculator.main.adapters.RecyclerListOfMealAdapter;
import com.example.roshk1n.foodcalculator.realm.FoodRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;


import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class DiaryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerDiaryAdapter mAdapter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_meal);
        final Realm realm = Realm.getDefaultInstance();
        final RealmResults<FoodRealm> foodRealm = realm.where(FoodRealm.class).findAll();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerDiaryAdapter(foodRealm);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                realm.beginTransaction();
                foodRealm.get(viewHolder.getPosition()).deleteFromRealm();
                realm.commitTransaction();
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        return view;
    }
}
