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
import com.hustunique.androidassistant.ui.adapters.BlockedCallAdapter.BlockedCall;
import com.hustunique.androidassistant.ui.viewholders.BlockedItemViewHolder;
import java.util.List;

/**
 * Created by JianGuo on 6/16/17.
 */

public class BlockedCallAdapter extends BaseAdapter<BlockedCall, BlockedItemViewHolder> {

    public BlockedCallAdapter(List<BlockedCall> data) {
        super(data);
    }

    @Override
    public BlockedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blocked, parent, false);
        return new BlockedItemViewHolder(view);
    }


    @Override
    protected void bindView(BlockedCall item, BlockedItemViewHolder holder) {
        holder.getDetail().setText(item.getLoc());
        holder.getTvBlockNum().setText(item.getPhoneNum());
        holder.getTvBlockTime().setText(item.getTime());
        holder.getTvBlockType().setText(item.getType() == 0 ?
            R.string.forbid_auto : R.string.forbid_list);
    }

    // TODO: 6/16/17 Construct model from db layer
    public static class BlockedCall {
        String mPhoneNum;
        String mLoc;
        int mType;
        String time;

        public BlockedCall(String phoneNum, String loc, int type, String time) {
            mPhoneNum = phoneNum;
            mLoc = loc;
            mType = type;
            this.time = time;
        }

        public String getPhoneNum() {
            return mPhoneNum;
        }

        public String getLoc() {
            return mLoc;
        }

        public int getType() {
            return mType;
        }

        public String getTime() {
            return time;
        }
    }
}
