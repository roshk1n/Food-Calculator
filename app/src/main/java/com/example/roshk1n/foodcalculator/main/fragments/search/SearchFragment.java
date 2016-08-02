package com.example.roshk1n.foodcalculator.main.fragments.search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roshk1n.foodcalculator.MyApplication;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.adapters.RecyclerSearchAdapter;
import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientFoodResponse;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.ListFoodResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchFragment extends Fragment implements SearchView {


    private SearchPresenterImpl searchPresenter;
    private RestClient restClient;
    private String[] nutrients = {"204","208","205","203"};;
    private long mdate=0;

    private View view;
    private EditText searchEt;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerSearchAdapter mAdapter;
    private ArrayList<NutrientFoodResponse> nutrientFoodResponses = new ArrayList<NutrientFoodResponse>();

    public SearchFragment() {}

    public static Fragment newInstance() { return  new SearchFragment(); }

    public static SearchFragment newInstance(long date) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("date", date);
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        initUI();

        searchPresenter = new SearchPresenterImpl();
        searchPresenter.setView(this);

        Bundle bundle = getArguments();
        if(bundle != null) {
            mdate = bundle.getLong("date");
        }
        restClient = MyApplication.getRestClient();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerSearchAdapter(nutrientFoodResponses, mdate);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);

        searchEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchEt.getText().length() != 0) {
                    nutrientFoodResponses.clear();
                    mAdapter.notifyDataSetChanged();

                    searchPresenter.searchFood(searchEt.getText().toString());
                }
            }
        });
        return view;
    }

    private void initUI() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_search);
        searchEt = (EditText) view.findViewById(R.id.et_food_name);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search");
    }
    @Override
    public void updateUI(ArrayList<NutrientFoodResponse> nutrientFoodResponses) {
        this.nutrientFoodResponses = nutrientFoodResponses;
        mAdapter = new RecyclerSearchAdapter(nutrientFoodResponses,mdate);
        mRecyclerView.setAdapter(mAdapter);
    }
}
