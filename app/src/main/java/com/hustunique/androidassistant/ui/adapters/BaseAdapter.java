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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import java.util.List;

/**
 * Created by JianGuo on 6/16/17.
 */

public abstract class BaseAdapter<T, VH extends ViewHolder> extends RecyclerView.Adapter<VH> {



    protected List<T> mData;

    public interface OnItemClickListener<E> {
        void onItemLongClick(E t);
        void onItemClick(E t);
    }

    private OnItemClickListener<T> mOnItemClickListener;

    public void setOnItemClickListener(
        OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    BaseAdapter(List<T> data) {
        mData = data;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        final T t = mData.get(position);
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(t);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemLongClick(t);
                }
                return false;
            }
        });
        bindView(t, holder);
    }

    protected abstract void bindView(T item, VH holder);

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
