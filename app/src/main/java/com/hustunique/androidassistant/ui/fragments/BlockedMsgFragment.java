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
import com.hustunique.androidassistant.ui.adapters.BaseAdapter;
import com.hustunique.androidassistant.ui.adapters.BlockedMsgAdapter;
import com.hustunique.androidassistant.util.LogUtil;

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
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<BlockedSMSModel>() {
            @Override
            public void onItemLongClick(final BlockedSMSModel t) {
                new Builder(getActivity())
                    .setTitle(R.string.delete_blocked_msg_title)
                    .setMessage(R.string.delete_blocked_msg_content)
                    .setPositiveButton(R.string.ok, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bsms.deleteBlockedSMS(t.time);
                            mAdapter.setData(mockList());
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
                LogUtil.d(TAG, "long click msg: " + t);
            }

            @Override
            public void onItemClick(BlockedSMSModel t) {
                LogUtil.d(TAG, "click on msg " + t);
                new Builder(getActivity())
                        .setTitle(R.string.sms_detail)
                        .setMessage(t.getContent())
                        .setPositiveButton(R.string.ok, new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<BlockedSMSModel> mockList() {
        return bsms.getAllBlockedSMS();
    }
}
