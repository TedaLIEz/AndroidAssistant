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

package com.hustunique.androidassistant.ui.fragments;


import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hustunique.androidassistant.R;
import com.hustunique.androidassistant.db.BlockedSMSSaver;
import com.hustunique.androidassistant.model.BlockedSMSModel;
import com.hustunique.androidassistant.ui.adapters.BlockedMsgAdapter;
import com.hustunique.androidassistant.ui.adapters.BlockedMsgAdapter.BlockedMsg;
import com.hustunique.androidassistant.ui.adapters.BlockedMsgAdapter.OnItemClickListener;
import com.hustunique.androidassistant.ui.adapters.BlockedMsgAdapter.OnItemLongClickListener;
import com.hustunique.androidassistant.util.LogUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JianGuo on 6/16/17.
 * Fragment used for showing blocked message in {@link com.hustunique.androidassistant.ui.activity.BlockedListActivity}
 */

public class BlockedMsgFragment extends Fragment {

    private static final String TAG = "BlockedMsgFragment";
    @BindView(R.id.rv_block_list)
    RecyclerView mRecyclerView;
    private BlockedMsgAdapter mAdapter;
    private BlockedSMSSaver bsms = new BlockedSMSSaver();
    public BlockedMsgFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blocked_phone, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        assert mRecyclerView != null;
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new BlockedMsgAdapter(mockList());
        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(BlockedMsg msg) {
                new Builder(getActivity())
                    .setTitle(getString(R.string.delete_blocked_msg_title))
                    .setMessage(getString(R.string.delete_blocked_msg_content))
                    .setPositiveButton(getString(R.string.ok), new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO: 6/20/17 Delete msg
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
                LogUtil.d(TAG, "long click msg: " + msg);
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BlockedMsg msg) {
                // TODO: 6/20/17 Show message
                LogUtil.d(TAG, "click on msg " + msg);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<BlockedMsg> mockList() {
        List<BlockedMsg> rst = new ArrayList<>();
        List<BlockedSMSModel> blocks = bsms.getAllBlockedSMS();
        for (BlockedSMSModel b : blocks) {
            String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date(b.time));
            rst.add(new BlockedMsg(b.number, b.text, b.autoblocked?0:1, date));
        }
        return rst;
    }
}
