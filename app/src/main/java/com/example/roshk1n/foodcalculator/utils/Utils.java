package com.example.roshk1n.foodcalculator.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.roshk1n.foodcalculator.activities.MainActivity;

public class Utils {
    public static void navigateToFragment(FragmentManager fragmentManager,
                                          int container,
                                          Fragment fragment,
                                          int transaction,
                                          boolean backStack) {
        if (backStack) {
            fragmentManager.beginTransaction()
                    .setTransition(transaction)
                    .replace(container, fragment)
                    .addToBackStack(null)
                    .commit();

        } else {
            fragmentManager.beginTransaction()
                    .setTransition(transaction)
                    .replace(container, fragment)
                    .commit();
        }

    }

    public static void navigateToFragmentCustom(FragmentManager fragmentManager,
                                                int container,
                                                Fragment fragment,
                                                int enterAnim,
                                                int exitAnim,
                                                boolean backStack) {
        if (backStack) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(enterAnim,exitAnim)
                    .replace(container, fragment)
                    .addToBackStack(null)
                    .commit();

        } else {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(enterAnim,exitAnim)
                    .replace(container, fragment)
                    .commit();
        }

    }

    public static void hideKeyboard(Context context, View field) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
    }
}
