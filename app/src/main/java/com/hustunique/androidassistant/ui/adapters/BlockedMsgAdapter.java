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
import com.hustunique.androidassistant.ui.adapters.BlockedMsgAdapter.BlockedMsg;
import com.hustunique.androidassistant.ui.viewholders.BlockedCallViewHolder;
import java.util.List;

/**
 * Created by JianGuo on 6/16/17.
 */

public class BlockedMsgAdapter extends BaseAdapter<BlockedMsg, BlockedCallViewHolder> {

    public BlockedMsgAdapter(List<BlockedMsg> data) {
        super(data);
    }

    @Override
    public BlockedCallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blocked, parent, false);
        return new BlockedCallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BlockedCallViewHolder holder, int position) {
        BlockedMsg msg = mData.get(position);
        holder.getTvBlockTime().setText(msg.getTime());
        holder.getTvBlockType().setText(msg.getType() == 0 ?
            R.string.forbid_auto : R.string.forbid_list);
        holder.getTvBlockNum().setText(msg.getPhoneNum());
        holder.getDetail().setText(msg.getContent());
    }

    public static class BlockedMsg {
        String mPhoneNum;
        String mContent;
        int mType;
        String mTime;

        public BlockedMsg(String phoneNum, String content, int type, String time) {
            mPhoneNum = phoneNum;
            mContent = content;
            mType = type;
            mTime = time;
        }

        public String getPhoneNum() {
            return mPhoneNum;
        }

        public String getContent() {
            return mContent;
        }

        public int getType() {
            return mType;
        }

        public String getTime() {
            return mTime;
        }
    }
}
