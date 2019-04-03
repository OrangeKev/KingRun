package com.king.run.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.king.run.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者：shuizi_wade on 2017/5/17 16:05
 * 邮箱：674618016@qq.com
 */

public class VerifycationCodeView extends LinearLayout {

    private Context context;
    public EditText etInputOne;
    public EditText etInputTwo;
    public EditText etInputThree;
    public EditText etInputFour;
    public EditText etInputFive;
    public EditText etInputSix;

    private AutoCommitListener autoCommitListener;

    /**
     * autoCommit when input is finished
     *
     * @param autoCommitListener
     */
    public void setAutoCommitListener(AutoCommitListener autoCommitListener) {
        this.autoCommitListener = autoCommitListener;
    }

    public VerifycationCodeView(Context context) {
        this(context, null);
    }

    public VerifycationCodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public VerifycationCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    /**
     * initView
     */
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.verify_code_view, null);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        etInputOne = (EditText) view.findViewById(R.id.et_input_one);
        etInputTwo = (EditText) view.findViewById(R.id.et_input_two);
        etInputThree = (EditText) view.findViewById(R.id.et_input_three);
        etInputFour = (EditText) view.findViewById(R.id.et_input_four);
        etInputFive = (EditText) view.findViewById(R.id.et_input_five);
        etInputSix = (EditText) view.findViewById(R.id.et_input_six);
        etInputOne.setFocusable(true);
        etInputOne.requestFocus();
        etInputOne.setFocusableInTouchMode(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }

        }, 500);
        etInputOne.addTextChangedListener(mTextWatcher);
        etInputTwo.addTextChangedListener(mTextWatcher);
        etInputThree.addTextChangedListener(mTextWatcher);
        etInputFour.addTextChangedListener(mTextWatcher);
        etInputFive.addTextChangedListener(mTextWatcher);
        etInputSix.addTextChangedListener(mTextWatcher);

        etInputOne.setOnKeyListener(mOnKeyListener);
        etInputTwo.setOnKeyListener(mOnKeyListener);
        etInputThree.setOnKeyListener(mOnKeyListener);
        etInputFour.setOnKeyListener(mOnKeyListener);
        etInputFive.setOnKeyListener(mOnKeyListener);
        etInputSix.setOnKeyListener(mOnKeyListener);

        this.addView(view, layoutParams);

    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            System.out.print(s.toString());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            System.out.print(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {  //inputText
            if (s.toString().length() == 1) {
                if (etInputOne.isFocused()) {//Auto jump next inputEditText when the intput is Finished
                    etInputTwo.requestFocus();
                } else if (etInputTwo.isFocused()) {
                    etInputThree.requestFocus();
                } else if (etInputThree.isFocused()) {
                    etInputFour.requestFocus();
                } else if (etInputFour.isFocused()) {
                    etInputFive.requestFocus();
                } else if (etInputFive.isFocused()) {
                    etInputSix.requestFocus();
                }
//                if (getInputValue().length() >= 6) {
                if (autoCommitListener != null) {
                    autoCommitListener.autoCommit(getInputValue().toString());
                }
//                }
            } else if (s.toString().length() == 2) {
                String strFirst = s.toString().substring(0, 1);
                String strSecond = s.toString().substring(1, 2);
                if (etInputOne.isFocused()) {//Auto jump next inputEditText when the intput is Finished
                    setText(etInputOne, etInputTwo, strFirst, strSecond);
                } else if (etInputTwo.isFocused()) {
                    setText(etInputTwo, etInputThree, strFirst, strSecond);
                } else if (etInputThree.isFocused()) {
                    setText(etInputThree, etInputFour, strFirst, strSecond);
                } else if (etInputFour.isFocused()) {
                    setText(etInputFour, etInputFive, strFirst, strSecond);
                } else if (etInputFive.isFocused()) {
                    setText(etInputFive, etInputSix, strFirst, strSecond);
                } else if (etInputSix.isFocused()) {
                    etInputSix.setText(strFirst);
                }
                if (getInputValue().length() >= 6) {
                    if (autoCommitListener != null) {
                        autoCommitListener.autoCommit(getInputValue().toString());
                    }
                }
            }
        }
    };

    private void setText(EditText firstEidttext, EditText secondEdittext, String first, String second) {
        firstEidttext.setText(first);
        secondEdittext.setText(second);
    }

    /**
     * getInputValue
     */
    private StringBuffer getInputValue() {
        StringBuffer stringBuffer = new StringBuffer();
        return stringBuffer.append(etInputOne.getText().toString())
                .append(etInputTwo.getText().toString())
                .append(etInputThree.getText().toString())
                .append(etInputFour.getText().toString())
                .append(etInputFive.getText().toString())
                .append(etInputSix.getText().toString());
    }

    private OnKeyListener mOnKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) { //deleteText
                if (etInputSix.isFocused()) {//Auto jump previous when the delete is finished and input.length == 0
                    etInputSix.setText("");
                    if (etInputSix.getText().toString().length() == 0)
                        etInputFive.requestFocus();
                } else if (etInputFive.isFocused()) {
                    etInputFive.setText("");
                    if (etInputFive.getText().toString().length() == 0)
                        etInputFour.requestFocus();
                } else if (etInputFour.isFocused()) {
                    etInputFour.setText("");
                    if (etInputFour.getText().toString().length() == 0)
                        etInputThree.requestFocus();
                } else if (etInputThree.isFocused()) {
                    etInputThree.setText("");
                    if (etInputThree.getText().toString().length() == 0)
                        etInputTwo.requestFocus();
                } else if (etInputTwo.isFocused()) {
                    etInputTwo.setText("");
                    if (etInputTwo.getText().toString().length() == 0)
                        etInputOne.requestFocus();
                }
            }
            return false;
        }
    };

    /**
     * interface when input is finished autoCommit
     */
    public interface AutoCommitListener {
        void autoCommit(String inputText);
    }
}
