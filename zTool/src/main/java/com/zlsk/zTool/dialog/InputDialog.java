package com.zlsk.zTool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zlsk.zTool.R;

/**
 * Created by IceWang on 2018/5/18.
 */

public class InputDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private static final int DEFAULT_DECIMAL_NUMBER = 1;
    private View.OnClickListener onBtnOkClickListener;
    private EditText et_count;
    public int selectType = 0;

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        et_count.setText(button.getText());
        int i = v.getId();
        if (i == R.id.btn_select_left) {
            setColor(0);
        }else if(i == R.id.btn_select_right){
            setColor(1);
        }
    }

    private void setColor(int type){
        btn_select_left.setBackgroundColor(Color.parseColor(type == 0 ? "#0094ff" : "#999999"));
        btn_select_right.setBackgroundColor(Color.parseColor(type == 1 ? "#0094ff" : "#999999"));
        selectType = type;
    }

    public enum InputType {type_int,type_float,other,select}
    private InputType inputType;
    public Button btn_select_left;
    public Button btn_select_right;

    public InputDialog(InputType inputType, Context context, View.OnClickListener onBtnOkClickListener) {
        super(context, R.style.common_dialog);
        this.context = context;
        this.inputType = inputType;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cshu_material_input);
        this.onBtnOkClickListener = onBtnOkClickListener;
        initUi();
    }

    public Editable getCount(){
        return et_count.getText();
    }

    public void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(et_count.getWindowToken(), 0);
        }
    }

    private void initUi() {
        LinearLayout layout_input_cost = findViewById(R.id.layout_input_cost);
        layout_input_cost.setVisibility(inputType != InputType.select ? View.VISIBLE : View.GONE);

        LinearLayout layout_select = findViewById(R.id.layout_select);
        layout_select.setVisibility(inputType == InputType.select ? View.VISIBLE : View.GONE);
        btn_select_left= findViewById(R.id.btn_select_left);
        btn_select_right = findViewById(R.id.btn_select_right);

        btn_select_left.setOnClickListener(this);
        btn_select_right.setOnClickListener(this);

        setColor(0);

        et_count = findViewById(R.id.et_cost);
        if(inputType == InputType.type_int){
            et_count.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        }else if(inputType == InputType.type_float){
            et_count.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL |
                    android.text.InputType.TYPE_NUMBER_FLAG_SIGNED);
            et_count.setFilters(new InputFilter[]{new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    String lastInputContent = dest.toString();
                    if (lastInputContent.contains(".")) {
                        int index = lastInputContent.indexOf(".");
                        if(dend - index >= DEFAULT_DECIMAL_NUMBER + 1){
                            return "";
                        }
                    }
                    return null;
                }
            }});
        }

        findViewById(R.id.custom_dialog_ok).setOnClickListener(onBtnOkClickListener);
        findViewById(R.id.custom_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
