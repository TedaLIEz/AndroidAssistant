/*
 * Copyright 2017 TedaLIEz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hustunique.androidassistant.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by JianGuo on 6/16/17.
 */

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {


    private static final String TAG = "ScrollingFABBehavior";

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
        // Log.e(TAG, "ScrollAwareFABBehavior");
    }


    public boolean onStartNestedScroll(CoordinatorLayout parent, FloatingActionButton child,
        View directTargetChild, View target, int nestedScrollAxes) {

        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child,
        View dependency) {
        if (dependency instanceof RecyclerView) {
            return true;
        }

        return false;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
        FloatingActionButton child, View target, int dxConsumed,
        int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        // TODO Auto-generated method stub
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
            dxUnconsumed, dyUnconsumed);
        //Log.e(TAG, "onNestedScroll called");
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            //   Log.e(TAG, "child.hide()");
            child.hide();
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            //  Log.e(TAG, "child.show()");
            child.show();
        }
    }


}
