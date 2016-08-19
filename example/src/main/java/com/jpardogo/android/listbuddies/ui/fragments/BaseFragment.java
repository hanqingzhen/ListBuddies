package com.jpardogo.android.listbuddies.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpardogo.android.listbuddies.R;
import com.jpardogo.android.listbuddies.Utils.LogMe;
import com.umeng.analytics.MobclickAgent;

/**
 *
 */
public class BaseFragment extends Fragment {
    public final String TAG = this.getClass().getSimpleName()+"My";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogMe.i(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogMe.i(TAG, "onCreate");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogMe.i(TAG, "onActivityCreated");
    }


    @Override
    public void onStart() {
        super.onStart();
        LogMe.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogMe.i(TAG, "onResume");
        MobclickAgent.onPageStart(this.getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        LogMe.i(TAG, "onPause");
        MobclickAgent.onPageEnd(this.getClass().getName());
    }

    @Override
    public void onStop() {
        super.onStop();
        LogMe.i(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {

        LogMe.i(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogMe.i(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogMe.i(TAG, "onDetach");
    }
}
