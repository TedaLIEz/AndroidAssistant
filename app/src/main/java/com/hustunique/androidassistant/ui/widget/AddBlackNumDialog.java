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

package com.hustunique.androidassistant.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hustunique.androidassistant.R;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by JianGuo on 6/19/17.
 */

public class AddBlackNumDialog extends Dialog {

    @BindView(R.id.dialog_negative)
    Button mNegative;

    @BindView(R.id.dialog_positive)
    Button mPositive;

    @BindView(R.id.et_num)
    MaterialEditText mAddNum;

    public AddBlackNumDialog(@NonNull Context context) {
        this(context, 0);
    }

    public AddBlackNumDialog(@NonNull Context context,
        @StyleRes int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_black);
        ButterKnife.bind(this);
    }

    public interface OnPositiveButtonListener {

        void onClick(Dialog dialog, String num);
    }

    public interface OnNegativeButtonListener {

        void onClick(Dialog dialog);
    }


    private OnPositiveButtonListener onPositiveButtonListener;
    private OnNegativeButtonListener onNegativeButtonListener;

    public void setOnPositiveButton(String text,
        OnPositiveButtonListener onPositiveButtonListener) {
        mPositive.setText(text);
        this.onPositiveButtonListener = onPositiveButtonListener;
    }


    public void setOnNegativeButton(String text,
        OnNegativeButtonListener onNegativeButtonListener) {
        mNegative.setText(text);
        this.onNegativeButtonListener = onNegativeButtonListener;
    }


    @OnClick(R.id.dialog_negative)
    void onNegativeClick() {
        if (onNegativeButtonListener != null) {
            onNegativeButtonListener.onClick(this);
        }
    }

    @OnClick(R.id.dialog_positive)
    void onPositiveClick() {
        if (onPositiveButtonListener != null) {
            if (!checkValid(mAddNum.getText())) {
                mAddNum.setError(getContext().getString(R.string.add_num_error));
                return;
            }
            onPositiveButtonListener.onClick(this, mAddNum.getText().toString());
        }
    }

    private boolean checkValid(Editable text) {
        return !(text == null || text.length() == 0) && !(text.length() != 11 || !TextUtils
            .isDigitsOnly(text));
    }


}
