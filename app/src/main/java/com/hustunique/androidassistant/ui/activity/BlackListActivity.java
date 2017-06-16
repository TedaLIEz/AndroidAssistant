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

package com.hustunique.androidassistant.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.hustunique.androidassistant.R;
import com.hustunique.androidassistant.ui.adapters.BlackListAdapter;
import com.hustunique.androidassistant.ui.adapters.BlackListAdapter.NumModel;
import java.util.ArrayList;
import java.util.List;

public class BlackListActivity extends BaseActivity {

    @BindView(R.id.rv_block_nums)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    Unbinder mUnbinder;

    private BlackListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        mUnbinder = ButterKnife.bind(this);
        setupToolbar(R.string.title_add_number);
        setUpRv();
        mFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BlackListActivity.this, "TODO", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRv() {
        List<NumModel> list = mockList();
        mAdapter = new BlackListAdapter(list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    // TODO: 6/16/17 Retrieve real blacked number from db
    private List<NumModel> mockList() {
        List<NumModel> rst = new ArrayList<>();
        NumModel model = new NumModel("13018060139", "Wuhan");
        NumModel model1 = new NumModel("13018060139", "Wuhan");
        NumModel model2 = new NumModel("13018060139", "Wuhan");
        for (int i = 0; i < 5; i++) {
            rst.add(model);
            rst.add(model1);
            rst.add(model2);
        }
        return rst;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}