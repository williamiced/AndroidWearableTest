package com.twotoasters.messageapidemo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class Fragment2  extends Fragment {
    public ScrollView mScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View pageView = inflater.inflate(R.layout.pic2, container, false);
        mScrollView = (ScrollView) pageView.findViewById(R.id.scrollView);
        return pageView;
    }

    public ScrollView getScrollView() {
        return mScrollView;
    }

    public static void stopCycling() {
    }
}
