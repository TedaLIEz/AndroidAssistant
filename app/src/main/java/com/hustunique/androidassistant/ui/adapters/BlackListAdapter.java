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

package com.hustunique.androidassistant.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import com.hustunique.androidassistant.R;
import com.hustunique.androidassistant.ui.adapters.BlackListAdapter.NumModel;
import com.hustunique.androidassistant.ui.viewholders.BlackListViewHolder;
import java.util.List;

/**
 * Created by JianGuo on 6/16/17.
 * Adapter for Black list phone numbers
 */

public class BlackListAdapter extends BaseAdapter<NumModel, BlackListViewHolder> {


    public BlackListAdapter(List<NumModel> data) {
        super(data);
    }

    public interface OnItemLongClickListener {

        void onItemLongClick(NumModel num);
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(
        OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public BlackListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_black_list, parent, false);
        return new BlackListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BlackListViewHolder holder, int position) {
        final NumModel model = mData.get(position);
        holder.itemView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemLongClick(model);
                    return true;
                }
                return false;
            }
        });
        holder.getTvBlockNum().setText(model.getNum());
        holder.getTvBlockLoc().setText(model.getLoc());
    }


    // TODO: 6/16/17 Construct block num model
    public static class NumModel {

        String mNum;
        String mLoc;

        public NumModel(String num, String loc) {
            mNum = num;
            mLoc = loc;
        }

        public String getLoc() {
            return mLoc;
        }

        public String getNum() {
            return mNum;
        }

        @Override
        public String toString() {
            return "NumModel{" +
                "mNum='" + mNum + '\'' +
                ", mLoc='" + mLoc + '\'' +
                '}';
        }
    }
}
