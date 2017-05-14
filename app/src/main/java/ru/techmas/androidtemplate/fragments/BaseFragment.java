package ru.techmas.androidtemplate.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.arellomobile.mvp.MvpAppCompatFragment;

import ru.techmas.androidtemplate.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Alex Bykov on 09.11.2016.
 * You can contact me at: me@alexbykov.ru.
 */

public class BaseFragment extends MvpAppCompatFragment {

    protected String TAG = getClass().getSimpleName();

    public ProgressDialog progressDialog;
    protected View rootView;
    boolean inProgress;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void startActivity(Class<?> activityClass) {
        Intent intent = new Intent(getActivity(), activityClass);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.no_animation, R.anim.no_animation);
    }

    protected void hideView(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    protected void showView(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }


    @SuppressWarnings("unchecked")
    protected <T extends View> T bindView(@IdRes int id) {
        return (T) rootView.findViewById(id);
    }


}
