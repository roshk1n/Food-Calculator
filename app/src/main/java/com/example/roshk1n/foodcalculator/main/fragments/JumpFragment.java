package com.example.roshk1n.foodcalculator.main.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.fragments.diary.DiaryFragment;

/**
 * Created by roshk1n on 7/29/2016.
 */
@SuppressWarnings("ALL")
public class JumpFragment extends Fragment {

    @SuppressWarnings("FieldCanBeLocal")
    private long mdate;

    public static JumpFragment newInstance(long date,int side) {
        JumpFragment jumpFragment = new JumpFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("date", date);
        bundle.putInt("side",side);
        jumpFragment.setArguments(bundle);
        return jumpFragment;
    }

    @Override
    public void onStart() {

        Bundle bundle = getArguments();
        if(bundle != null) {
            mdate = bundle.getLong("date");
            int side = bundle.getInt("side");
            if (side == 0) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left_enter, FragmentTransaction.TRANSIT_NONE)
                        .replace(R.id.fragment_conteiner, DiaryFragment.newInstance(mdate))
                        .addToBackStack(null)
                        .commit();
            } else {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right_enter, FragmentTransaction.TRANSIT_NONE)
                        .replace(R.id.fragment_conteiner, DiaryFragment.newInstance(mdate))
                        .addToBackStack(null)
                        .commit();
            }
        }
        super.onStart();

    }
}
