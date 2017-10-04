package com.laucherish.purestatelayout.demo;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.laucherish.purestatelayout.PureStateLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private PureStateLayout mStateLayout;
    private Button mBtnEmpty, mBtnError, mBtnLoading, mBtnContent;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mStateLayout.showLoading();
                mStateLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(false);
                        }
                        mStateLayout.showContent();
                    }
                }, 3000);
            }
        });
        mStateLayout = (PureStateLayout) findViewById(R.id.pure_state_layout);
        mStateLayout.setStateChangeListener(new PureStateLayout.SimpleStateChangeListener(){
            @Override
            public void onErrorRetryClick() {
                super.onErrorRetryClick();
                mStateLayout.showLoading();
                mStateLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(false);
                        }
                        mStateLayout.showEmpty();
                    }
                }, 3000);
            }

            @Override
            public void onEmptyRetryClick() {
                super.onEmptyRetryClick();
                mStateLayout.showLoading();
                mStateLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(false);
                        }
                        mStateLayout.showError();
                    }
                }, 3000);
            }
        });
        mBtnContent = (Button) findViewById(R.id.btn_content);
        mBtnEmpty = (Button) findViewById(R.id.btn_empty);
        mBtnError = (Button) findViewById(R.id.btn_error);
        mBtnLoading = (Button) findViewById(R.id.btn_loading);
        mBtnContent.setOnClickListener(this);
        mBtnEmpty.setOnClickListener(this);
        mBtnError.setOnClickListener(this);
        mBtnLoading.setOnClickListener(this);

        getData();
    }

    private void getData(){
        mStateLayout.showLoading();
        mStateLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                mStateLayout.showError();
            }
        }, 3000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_empty:

                mStateLayout.showEmpty();
                break;
            case R.id.btn_error:
                mStateLayout.showError();
                break;
            case R.id.btn_content:
                mStateLayout.showContent();
                break;
            case R.id.btn_loading:
                mStateLayout.showLoading();
                break;
        }
    }
}
