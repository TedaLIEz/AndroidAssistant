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

    @Override
    public BlockedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blocked, parent, false);
        return new BlockedItemViewHolder(view);
    }

    @Override
    protected void bindView(BlockedSMSModel item, BlockedItemViewHolder holder) {
        holder.getTvBlockTime().setText(item.getTime());
        holder.getTvBlockType().setText(item.getType() == 0 ?
            R.string.forbid_auto : R.string.forbid_list);
        holder.getTvBlockNum().setText(item.getPhoneNum());
        holder.getDetail().setText(item.getContent());
    }

}
