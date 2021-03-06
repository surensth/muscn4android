package com.awecode.muscn.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.awecode.muscn.R;
import com.awecode.muscn.util.Util;
import com.awecode.muscn.util.retrofit.MuscnApiInterface;
import com.awecode.muscn.util.retrofit.ServiceGenerator;
import com.awecode.muscn.util.stateLayout.StateLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by munnadroid on 9/21/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected MuscnApiInterface mApiInterface = ServiceGenerator.createService(MuscnApiInterface.class);
    @BindView(R.id.stateLayout)
    StateLayout mStateLayout;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
        mContext = this;
    }

    protected int getDimen(int id) {
        return (int) mContext.getResources().getDimension(id);
    }

    public void toast(String message) {
        Util.toast(mContext, message);
    }

    protected abstract int getLayoutResourceId();

    protected void showProgressView(String message) {
        mStateLayout.showProgressView(message);
    }

    protected void showProgressView() {
        mStateLayout.showProgressView();
    }

    protected void showErrorView(String message) {
        mStateLayout.showEmptyView(message);
    }

    protected void showErrorView() {
        mStateLayout.showEmptyView();
    }

    protected void showContentView() {
        mStateLayout.showContentView();
    }


}
