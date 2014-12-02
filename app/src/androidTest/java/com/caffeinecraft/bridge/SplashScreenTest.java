package com.caffeinecraft.bridge;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.MotionEvent;
import android.view.View;

public class SplashScreenTest extends ActivityUnitTestCase<TestActivity> {
    Intent mLaunchIntent;
    Activity mActivity;
    public SplashScreenTest() {
        super(TestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mLaunchIntent = new Intent(getInstrumentation().getTargetContext(), TestActivity.class);
    }

    @SmallTest
    public void testNextActivityLaunchedOnTouch() {
        startActivity(mLaunchIntent, null, null);
        final View pokeMe = getActivity().findViewById(android.R.id.content).getRootView();
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 50.0f;
        float y = 50.0f;
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                metaState
        );
        pokeMe.dispatchTouchEvent(motionEvent);

        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
        assertTrue(isFinishCalled());
    }
}
