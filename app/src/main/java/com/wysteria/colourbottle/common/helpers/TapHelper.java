package com.wysteria.colourbottle.common.helpers;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.wysteria.colourbottle.activity.MainActivity;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Helper to detect taps using Android GestureDetector, and pass the taps between UI thread and
 * render thread.
 */
public final class TapHelper implements OnTouchListener {
    private final GestureDetector gestureDetector;
    private final BlockingQueue<MotionEvent> queuedSingleTaps = new ArrayBlockingQueue<>(16);
    private int mPtrCount = 0;

    /**
     * Creates the tap helper.
     *
     * @param context the application's context.
     */
    public TapHelper(Context context) {
        gestureDetector =
                new GestureDetector(
                        context,
                        new GestureDetector.SimpleOnGestureListener() {
                            @Override
                            public boolean onSingleTapUp(MotionEvent e) {
                                // Queue tap if there is space. Tap is lost if queue is full.
                                queuedSingleTaps.offer(e);
                                return true;
                            }

                            @Override
                            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                                if (mPtrCount < 2) {
                                    queuedSingleTaps.offer(e2);
                                    return true;
                                } else {
                                    return false;
                                }
                            }

//                            @Override
//                            public boolean onDoubleTap(MotionEvent e) {
//                                GlobalClass.scaleFactor += GlobalClass.scaleFactor;
//                                Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
//                                return true;
//                            }
                        });
    }

    /**
     * Polls for a tap.
     *
     * @return if a tap was queued, a MotionEvent for the tap. Otherwise null if no taps are queued.
     */
    public MotionEvent poll() {
        return queuedSingleTaps.poll();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = (event.getAction() & MotionEvent.ACTION_MASK);
        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
                mPtrCount++;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mPtrCount--;
                break;
            case MotionEvent.ACTION_DOWN:
                mPtrCount++;
                break;
            case MotionEvent.ACTION_UP:
                mPtrCount--;
                break;
        }
//        MainActivity.motionEvent = event;
        if (!gestureDetector.onTouchEvent(event))
            if (!MainActivity.mRotationDetector.onTouchEvent(event))
                MainActivity.scaleGestureDetector.onTouch(view, event);

        return true;
    }
}
