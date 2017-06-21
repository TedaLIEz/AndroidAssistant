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
import android.view.ViewGroup;

import com.hustunique.androidassistant.R;
import com.hustunique.androidassistant.db.LocationQuery;
import com.hustunique.androidassistant.model.BlockedCallModel;
import com.hustunique.androidassistant.ui.viewholders.BlockedItemViewHolder;

import java.util.List;

/**
 * Created by JianGuo on 6/16/17.
 */

public class BlockedCallAdapter extends BaseAdapter<BlockedCallModel, BlockedItemViewHolder> {
    private LocationQuery lq;

    public BlockedCallAdapter(List<BlockedCallModel> data, Context context) {
        super(data);
        lq = new LocationQuery(context);
    }


    @Override
    protected BlockedItemViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blocked, parent, false);
        return new BlockedItemViewHolder(view);
    }




    @Override
    protected void bindView(BlockedCallModel item, BlockedItemViewHolder holder) {
        holder.getDetail().setText(lq.getLocationInfo(item.getPhoneNum()));
        holder.getTvBlockNum().setText(item.getPhoneNum());
        holder.getTvBlockTime().setText(item.getTime());
        holder.getTvBlockType().setText(item.getType() == 0 ?
            R.string.forbid_auto : R.string.forbid_list);
    }

}
