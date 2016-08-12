package com.example.roshk1n.foodcalculator.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.MyApplication;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.activities.MainActivity;
import com.example.roshk1n.foodcalculator.adapters.RecyclerSearchAdapter;
import com.example.roshk1n.foodcalculator.responseAdapter.CallbackSearchAdapter;
import com.example.roshk1n.foodcalculator.presenters.SearchPresenterImpl;
import com.example.roshk1n.foodcalculator.Views.SearchView;
import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientSpecialFoodResponse;
import java.util.ArrayList;

public class SearchFragment extends Fragment implements SearchView, CallbackSearchAdapter {

    private SearchPresenterImpl searchPresenter;
    private EditText searchEt;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerSearchAdapter mAdapter;
    private ArrayList<NutrientSpecialFoodResponse> nutrientSpecialFoodResponses = new ArrayList<>();
    private long mdate=0;

    private View view;

    public SearchFragment() {}

    public  static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public static SearchFragment newInstance(long date) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("date", date);
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        searchPresenter = new SearchPresenterImpl();
        searchPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        initUI();

        ((MainActivity)view.getContext()).setUpToolbarArrow();
        ((MainActivity)view.getContext()).disableMenuSwipe();
      //  ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search");


        Bundle bundle = getArguments();
        if(bundle != null) {
            mdate = bundle.getLong("date");
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerSearchAdapter(nutrientSpecialFoodResponses, mdate,this);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);

        searchEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (searchEt.getText().length() != 0) {
                        nutrientSpecialFoodResponses.clear();
                        mAdapter.notifyDataSetChanged();
                        searchPresenter.searchFood(searchEt.getText().toString());
                        hideKeyboard();
                    }
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setFoodNutrients(NutrientSpecialFoodResponse nutrientSpecialFoodResponses) {
        this.nutrientSpecialFoodResponses.add(nutrientSpecialFoodResponses);
        mAdapter.notifyItemInserted(this.nutrientSpecialFoodResponses.size());
    }

    @Override
    public void setErrorNetwork() {
        Log.d("Myy","error network");
    }

    @Override
    public void navigateToAddFood(Food food) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_conteiner, AddFoodFragment.newInstance(food))
                .addToBackStack(null)
                .commit();
    }

    private void initUI() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_search);
        searchEt = (EditText) view.findViewById(R.id.et_food_name);
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(MainActivity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
