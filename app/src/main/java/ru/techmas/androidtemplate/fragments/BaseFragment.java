package ru.techmas.androidtemplate.fragments;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import ru.techmas.androidtemplate.App;
import ru.techmas.androidtemplate.api.RestApi;
import ru.techmas.androidtemplate.interfaces.views.BaseFragmentView;
import ru.techmas.androidtemplate.presenters.BaseFragmentPresenter;
import ru.techmas.androidtemplate.utils.PreferenceHelper;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Alex Bykov on 09.11.2016.
 * You can contact me at: me@alexbykov.ru.
 */

public class BaseFragment extends MvpAppCompatFragment implements BaseFragmentView {

    protected String TAG = getClass().getSimpleName();

    //@formatter:off
    @InjectPresenter BaseFragmentPresenter baseFragmentPresenter;
    @ProvidePresenter BaseFragmentPresenter provideBasePresenter(){
        return App.getAppComponent().getBaseFragmentPresenter();
    }


    @Inject protected RestApi restApi;
    @Inject protected PreferenceHelper preferenceHelper;
    //@formatter:on

    public ProgressDialog progressDialog;
    protected View rootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
    }


    protected void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void startProgress() {
        progressDialog = ProgressDialog.show(getContext(), null, null);
        progressDialog.setContentView(new ProgressBar(getContext()));
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    public void stopProgress() {
        progressDialog.cancel();
    }

    @Override
    public void showProgress(boolean progress) {
        baseFragmentPresenter.doSomeThingWithProgress(progress);
    }


    @SuppressWarnings("unchecked")
    protected <T extends View> T bindView(@IdRes int id) {
        return (T) rootView.findViewById(id);
    }


}