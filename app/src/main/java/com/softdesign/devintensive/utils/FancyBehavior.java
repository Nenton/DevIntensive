package com.softdesign.devintensive.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class FancyBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    public FancyBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        float delta = dependency.getY() - 228;
        double speed = 6; // в пределах 1-6

        if (delta >= 0 && delta <= 84 * speed) {
            child.setPadding(0, (int) (delta / speed),
                    0, (int) (delta / speed));
            dependency.setPadding(dependency.getPaddingLeft(), child.getHeight(),
                    dependency.getPaddingRight(), dependency.getPaddingBottom());
        }

        child.setY(dependency.getY());
        return true;
    }
}
