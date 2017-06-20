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
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import com.hustunique.androidassistant.R;
import com.hustunique.androidassistant.model.BlockedSMSModel;
import com.hustunique.androidassistant.ui.viewholders.BlockedItemViewHolder;

import java.util.List;

/**
 * Created by JianGuo on 6/16/17.
 */

public class BlockedMsgAdapter extends BaseAdapter<BlockedSMSModel, BlockedItemViewHolder> {
    private static final String TAG = "BlockedMsgAdapter";

    public BlockedMsgAdapter(List<BlockedSMSModel> data) {
        super(data);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(BlockedSMSModel msg);
    }


    public interface OnItemClickListener {
        void onItemClick(BlockedSMSModel msg);
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(
        OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(
        OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public BlockedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blocked, parent, false);
        return new BlockedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BlockedItemViewHolder holder, int position) {

        final BlockedSMSModel msg = mData.get(position);
        holder.itemView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemLongClick(msg);
                    return true;
                }
                return false;
            }
        });

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(msg);
                }
            }
        });
        holder.getTvBlockTime().setText(msg.getTime());
        holder.getTvBlockType().setText(msg.getType() == 0 ?
            R.string.forbid_auto : R.string.forbid_list);
        holder.getTvBlockNum().setText(msg.getPhoneNum());
        holder.getDetail().setText(msg.getContent());
    }

}
