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

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.hustunique.androidassistant.R;
import com.hustunique.androidassistant.db.BlackList;
import com.hustunique.androidassistant.model.BlackListModel;
import com.hustunique.androidassistant.ui.adapters.BaseAdapter.OnItemClickListener;
import com.hustunique.androidassistant.ui.adapters.BlackListAdapter;
import com.hustunique.androidassistant.ui.widget.AddBlackNumDialog;
import com.hustunique.androidassistant.ui.widget.AddBlackNumDialog.OnNegativeButtonListener;
import com.hustunique.androidassistant.ui.widget.AddBlackNumDialog.OnPositiveButtonListener;
import com.hustunique.androidassistant.util.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BlackListActivity extends BaseActivity {

    private static final String TAG = "BlackListActivity";
    @BindView(R.id.rv_block_nums)
    RecyclerView mRecyclerView;

    Unbinder mUnbinder;

    private BlackList blDb = new BlackList();

    private BlackListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        mUnbinder = ButterKnife.bind(this);
        setupToolbar(R.string.title_add_number);
        setUpRv();
    }

    private void setUpRv() {
        List<BlackListModel> list = mockList();
        mAdapter = new BlackListAdapter(list, this);
        mAdapter.setOnItemClickListener(new OnItemClickListener<BlackListModel>() {
            @Override
            public void onItemLongClick(final BlackListModel t) {
                new Builder(BlackListActivity.this)
                        .setTitle(getString(R.string.delete_black_num_title))
                        .setMessage(getString(R.string.delete_black_num_content))
                        .setPositiveButton(getString(R.string.ok), new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                blDb.deleteNumberFromBlackList(t.number);
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
                LogUtil.d(TAG, "long click num: " + t);
            }

            @Override
            public void onItemClick(BlackListModel t) {

            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<BlackListModel> mockList() {
        return blDb.getAllBlackNumbers();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.fab)
    public void onFabClick() {
        AddBlackNumDialog dialog = new AddBlackNumDialog(this);
        dialog.show();
        dialog.setOnNegativeButton(getString(R.string.no), new OnNegativeButtonListener() {
            @Override
            public void onClick(Dialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveButton(getString(R.string.ok), new OnPositiveButtonListener() {
            @Override
            public void onClick(Dialog dialog, String num) {
                blDb.addNewBlackNumber(num);
                mAdapter.setData(mockList());
                LogUtil.d(TAG, "add phone num " + num);
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
