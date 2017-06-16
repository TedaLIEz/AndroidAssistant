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

package com.hustunique.androidassistant.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hustunique.androidassistant.R;

/**
 * Created by JianGuo on 6/16/17.
 */

public class BlockedCallViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_block_num)
    TextView mTvBlockNum;
    @BindView(R.id.tv_block_loc)
    TextView mTvDetail;

    @BindView(R.id.tv_type)
    TextView mTvBlockType;

    @BindView(R.id.tv_block_time)
    TextView mTvBlockTime;
    public BlockedCallViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public TextView getTvBlockNum() {
        return mTvBlockNum;
    }

    public TextView getDetail() {
        return mTvDetail;
    }

    public TextView getTvBlockType() {
        return mTvBlockType;
    }

    public TextView getTvBlockTime() {
        return mTvBlockTime;
    }

}
