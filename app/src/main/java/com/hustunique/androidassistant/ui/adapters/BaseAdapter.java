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

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import java.util.List;

/**
 * Created by JianGuo on 6/16/17.
 */

abstract class BaseAdapter<T, VH extends ViewHolder> extends RecyclerView.Adapter<VH> {


    protected List<T> mData;


    BaseAdapter(List<T> data) {
        mData = data;
    }


    public void addItem(T t) {
        mData.add(t);
        notifyItemInserted(mData.size());
    }

    public void removeItem(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }
}
