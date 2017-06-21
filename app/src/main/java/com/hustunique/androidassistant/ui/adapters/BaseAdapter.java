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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import com.hustunique.androidassistant.R;
import java.util.List;

/**
 * Created by JianGuo on 6/16/17.
 */

public abstract class BaseAdapter<T, VH extends ViewHolder> extends RecyclerView.Adapter<ViewHolder> {
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;


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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            return createEmptyView(parent);
        } else if (viewType == VIEW_TYPE_NORMAL) {
            return createViewHolder(parent);
        } else {
            return null;
        }
    }

    public static class EmptyViewHolder extends ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    protected abstract VH createViewHolder(ViewGroup parent);

    protected ViewHolder createEmptyView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_empty_view, parent, false);
        return new EmptyViewHolder(view);
    }


    @Override
    @SuppressWarnings("unchecked")
    // from onCreateViewHolder, the holder here must be VH type
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) != VIEW_TYPE_EMPTY) {
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
            bindView(t, (VH) holder);
        }
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
    public int getItemViewType(int position) {
        if (mData == null || mData.size() == 0) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null || mData.size() == 0) {
            return 1;
        } else {
            return mData.size();
        }
    }
}
