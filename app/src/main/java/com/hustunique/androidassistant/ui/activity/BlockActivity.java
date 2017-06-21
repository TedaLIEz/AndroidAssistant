package com.hustunique.androidassistant.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.hustunique.androidassistant.R;
import com.hustunique.androidassistant.manager.PrefManager;
import com.hustunique.androidassistant.util.LogUtil;

/**
 * Created by sunpe on 2017/6/15.
 */

public class BlockActivity extends BaseActivity {

    private Unbinder mUnbinder;
    @BindView(R.id.block_switch)
    SwitchCompat mBlockSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        setupToolbar(R.string.title_block);
        mUnbinder = ButterKnife.bind(this);
        boolean enabled = PrefManager.getInstance()
                .getDefaultPreferences(this)
                .getBoolean("BlockEnable", false);
        mBlockSwitch.setChecked(enabled);
        enabled = PrefManager.getInstance()
                .getDefaultPreferences(this)
                .getBoolean("AutoBlockEnable", false);
    }


    @OnCheckedChanged(R.id.block_switch)
    void onSwitchChecked(boolean checked) {
        LogUtil.d("BlockEnable", "block switch :" + (checked?"true":"false"));
        PrefManager.getInstance()
                .getDefaultPreferences(this)
                .edit()
                .putBoolean("BlockEnable", checked)
                .apply();
    }


    @OnClick(R.id.block_add_number)
    public void addBlockNumber() {
        Intent intent = new Intent(this, BlackListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.check_blocked_msg)
    public void checkBlockMsg() {
        Intent intent = new Intent(this, BlockedListActivity.class);
        startActivity(intent);
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
