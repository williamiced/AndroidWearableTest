package com.twotoasters.messageapidemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.WatchViewStub;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

public class MyActivity extends FragmentActivity implements MessageApi.MessageListener {
    private WatchViewStub mStub;
    private GoogleApiClient mGoogleApiClient;
    private ViewPager mViewPager;
    private DismissOverlayView mDismissOverlayView;
    private GestureDetector mGestureDetector;
    private Fragment1 mFragment1;
    private Fragment2 mFragment2;
    private Fragment3 mFragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mStub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        mStub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setPager();
            }
        });
        initGoogleApiClient();
    }

    private void setPager() {
        mDismissOverlayView = (DismissOverlayView) mStub.findViewById(R.id.dismiss);
        mDismissOverlayView.setIntroText(R.string.intro_text);
        mDismissOverlayView.showIntroIfNecessary();
        mViewPager = (ViewPager) mStub.findViewById(R.id.pager);

        final FragmentAdapter adapter = new FragmentAdapter( getSupportFragmentManager());
        mFragment1 = new Fragment1();
        mFragment2 = new Fragment2();
        mFragment3 = new Fragment3();
        adapter.addFragment(mFragment1);
        adapter.addFragment(mFragment2);
        adapter.addFragment(mFragment3);

        mViewPager.setAdapter(adapter);

        mGestureDetector = new GestureDetector(this, new LongPressListener());
    }

    private void initGoogleApiClient() {
        mGoogleApiClient = getGoogleApiClient(this);
        mGoogleApiClient.connect();
    }

    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (messageEvent.getPath().equals("Up")) {
                    if (mViewPager.getCurrentItem() == 1) {
                        ScrollView scrollView = mFragment2.getScrollView();
                        scrollView.smoothScrollBy(0, -100);
                    }
                }
                else if (messageEvent.getPath().equals("Down")) {
                    if (mViewPager.getCurrentItem() == 1) {
                        ScrollView scrollView = mFragment2.getScrollView();
                        scrollView.smoothScrollBy(0, 100);
                    }
                }
                else if (messageEvent.getPath().equals("Left"))
                    mViewPager.setCurrentItem( (mViewPager.getCurrentItem() + 2) % 3);
                else if (messageEvent.getPath().equals("Right"))
                    mViewPager.setCurrentItem( (mViewPager.getCurrentItem() + 1) % 3);
                else
                    Toast.makeText(MyActivity.this, "This is toast", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event)
                || super.dispatchTouchEvent(event);
    }

    private class LongPressListener extends
            GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent event) {
            mDismissOverlayView.show();

        }

    }
}
