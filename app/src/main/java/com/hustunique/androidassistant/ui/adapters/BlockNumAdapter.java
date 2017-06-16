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
import com.hustunique.androidassistant.ui.adapters.BlockNumAdapter.NumModel;
import com.hustunique.androidassistant.ui.viewholders.BlockNumViewHolder;
import java.util.List;

/**
 * Created by JianGuo on 6/16/17.
 */

public class BlockNumAdapter extends BaseAdapter<NumModel, BlockNumViewHolder> {


    public BlockNumAdapter(List<NumModel> data) {
        super(data);
    }

    @Override
    public BlockNumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_block_num, parent, false);
        return new BlockNumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BlockNumViewHolder holder, int position) {
        NumModel model = mData.get(position);
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
    }
}
