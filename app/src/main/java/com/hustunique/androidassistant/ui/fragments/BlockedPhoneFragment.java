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


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hustunique.androidassistant.R;
<<<<<<< HEAD
import com.hustunique.androidassistant.db.LocationQuery;
import com.hustunique.androidassistant.model.BlockedCallModel;
=======
>>>>>>> master
import com.hustunique.androidassistant.db.BlockedCallSaver;
import com.hustunique.androidassistant.model.BlockedCallModel;
import com.hustunique.androidassistant.ui.adapters.BlockedCallAdapter;
import com.hustunique.androidassistant.ui.adapters.BlockedCallAdapter.BlockedCall;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JianGuo on 6/16/17. Fragment used for showing blocked phone call in {@link
 * com.hustunique.androidassistant.ui.activity.BlockedListActivity}
 */

public class BlockedPhoneFragment extends Fragment {

    @BindView(R.id.rv_block_list)
    RecyclerView mRecyclerView;
    private BlockedCallSaver bc = new BlockedCallSaver();
    private BlockedCallAdapter mAdapter;

    public BlockedPhoneFragment() {
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

        mAdapter = new BlockedCallAdapter(mockList());
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<BlockedCall> mockList() {
        List<BlockedCall> rst = new ArrayList<>();
        List<BlockedCallModel> blocks = bc.getAllBlockedCall();
        LocationQuery lq = new LocationQuery(getActivity());
        for (BlockedCallModel b : blocks) {
            String date = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(b.time));
            String area = lq.getLocationInfo(b.number);
            rst.add(new BlockedCall(b.number, area, b.autoblocked?0:1, date));

        }
        return rst;
    }
}
