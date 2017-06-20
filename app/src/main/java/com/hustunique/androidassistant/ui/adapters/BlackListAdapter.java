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

 import android.content.Context;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.View.OnLongClickListener;
 import android.view.ViewGroup;

 import com.hustunique.androidassistant.R;
 import com.hustunique.androidassistant.db.LocationQuery;
 import com.hustunique.androidassistant.model.BlackListModel;
 import com.hustunique.androidassistant.ui.viewholders.BlackListViewHolder;

 import java.util.List;

/**
 * Created by JianGuo on 6/16/17.
 * Adapter for Black list phone numbers
 */

public class BlackListAdapter extends BaseAdapter<BlackListModel, BlackListViewHolder> {

    LocationQuery lq;

    public BlackListAdapter(List<BlackListModel> data, Context context) {
        super(data);
        lq = new LocationQuery(context);
    }


    @Override
    public BlackListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_black_list, parent, false);
        return new BlackListViewHolder(view);
    }


    @Override
    protected void bindView(NumModel item, BlackListViewHolder holder) {
        holder.getTvBlockNum().setText(item.getNum());
        holder.getTvBlockLoc().setText(item.getLoc());
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
