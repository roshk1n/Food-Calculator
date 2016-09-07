package com.example.roshk1n.foodcalculator.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

    public static boolean isConnectNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static void hideKeyboard(Context context, View field) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
    }

    public static void clearBackStack(FragmentManager supportFragmentManager) {
        FragmentManager.BackStackEntry first = supportFragmentManager.getBackStackEntryAt(0);
        supportFragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
