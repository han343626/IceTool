package com.zlsk.zTool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zlsk.zTool.R;
import com.zlsk.zTool.utils.date.DateUtil;
import com.zlsk.zTool.utils.string.StringUtil;

import java.lang.reflect.Field;
import java.util.Calendar;

public class DatetimePickerDialog extends Dialog {
    private Context context;
    private TextView okBtn;
    private TextView cancelBtn;
    private String okText;
    private String cancelText;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private DatetimePickerCallback callback;
    private EPickerType type = EPickerType.YEAR_MONTH_DATE_TIME;
    private EPickerDialogStyle style = EPickerDialogStyle.OK_CANCEL;
    private String defaultTime;
    private EPickerType ePickerType;

    public interface DatetimePickerCallback {
        void OnOK(String input);
    }

    /**
     * 日期选取类型
     *
     * @author ZiZhengzhuan
     */
    public enum EPickerType {
        YEAR_MONTH_DATE_TIME, YEAR_MONTH_DATE, YEAR_MONTH, YEAR, TIME
    }

    public enum EPickerDialogStyle {
        OK, OK_CANCEL, YESNO,
        OK_CANCEL_HIGHLIGHT_CANCEL, OK_CANCEL_HIGHLIGHT_OK
    }

    public DatetimePickerDialog(Context context, int theme, DatetimePickerCallback callback) {
        this(context, callback, EPickerDialogStyle.OK_CANCEL, EPickerType.YEAR_MONTH_DATE_TIME, null);
    }

    public DatetimePickerDialog(Context context, int theme, EPickerType ePickerType, DatetimePickerCallback callback) {
        this(context, callback, EPickerDialogStyle.OK_CANCEL, ePickerType, null);
        this.ePickerType = ePickerType;
    }

    /***
     *
     * @param context
     * @param callback
     * @param style
     * @param type
     * @param defaultTime yyyy-MM-dd HH:mm:ss
     */
    public DatetimePickerDialog(Context context, DatetimePickerCallback callback, EPickerDialogStyle style, EPickerType type, String defaultTime) {
        super(context, R.style.AlertView);
        this.context = context;
        this.callback = callback;
        this.type = type;
        this.style = style;
        this.defaultTime = defaultTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.datetimepicker_layout);
        initView();
        initPicker();
    }

    private void initView() {
        if (StringUtil.isBlank(okText)) {
            okText = "确定";
        }
        if (StringUtil.isBlank(cancelText)) {
            cancelText = "取消";
        }

        okBtn = (TextView) findViewById(R.id.txt_ok_cancel_dialog_ok);
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        timePicker = (TimePicker) findViewById(R.id.time_picker);

        okBtn.setText(okText);
        cancelBtn = (TextView) findViewById(R.id.txt_ok_cancel_dialog_cancel);
        cancelBtn.setText(cancelText);

        switch (style) {
            case OK:
                cancelBtn.setVisibility(View.GONE);
                findViewById(R.id.txt_btn_divider)
                        .setVisibility(View.GONE);
                break;
            case OK_CANCEL_HIGHLIGHT_CANCEL:
                cancelBtn.setTextColor(Color.RED);
                break;
            case OK_CANCEL_HIGHLIGHT_OK:
                okBtn.setTextColor(Color.RED);
                break;

            default:
                break;
        }

        okBtn.setOnClickListener(clickOkListener);
        cancelBtn.setOnClickListener(clickCancelListener);
    }

    private void initPicker() {
        long time = 0;
        if (!StringUtil.isBlank(defaultTime)) {
            time = DateUtil.changeStringToLong(defaultTime);
        } else {
            time = System.currentTimeMillis();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        //设置时间选择限制
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
        switch (type) {
            case YEAR_MONTH_DATE_TIME:
                setTitleWithResourceID(R.string.time_picker);
                datePicker.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.VISIBLE);
                break;
            case YEAR_MONTH_DATE:
                setTitleWithResourceID(R.string.date_picker);
                datePicker.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.GONE);
                break;
            case YEAR_MONTH:
                setTitleWithResourceID(R.string.date_month_picker);
                datePicker.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.GONE);
                hideDay(datePicker);
                break;
            case YEAR:
                setTitleWithResourceID(R.string.date_year_picker);
                datePicker.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.GONE);
                hideDay(datePicker);
                hideMonth(datePicker);
                break;
            case TIME:
                setTitleWithResourceID(R.string.time_picker);
                datePicker.setVisibility(View.GONE);
                timePicker.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    private void hideDay(DatePicker datePicker) {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= 20) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = datePicker.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = datePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(datePicker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideMonth(DatePicker datePicker) {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= 20) {
                int daySpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = datePicker.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = datePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mMonthSpinner".equals(datePickerField.getName()) || ("mMonthSpinner").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(datePicker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        Window mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();

        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            lp.width = height / 10 * 8;
        } else {
            lp.width = width / 10 * 8;
        }
        mWindow.setAttributes(lp);

        super.show();
    }

    private View.OnClickListener clickOkListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (null != callback) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                if (ePickerType == EPickerType.YEAR_MONTH_DATE) {
                    callback.OnOK(DateUtil.getDateStr(calendar.getTime()));
                } else {
                    callback.OnOK(DateUtil.getDateTime(calendar.getTime()));
                }
            }

            DatetimePickerDialog.this.dismiss();
        }
    };
    private View.OnClickListener clickCancelListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DatetimePickerDialog.this.dismiss();
        }
    };

    // 返回按钮
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            DatetimePickerDialog.this.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setOkText(String okText) {
        this.okText = okText;
        if (null != okBtn) {
            okBtn.setText(okText);
        }
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
        if (null != cancelBtn) {
            cancelBtn.setText(cancelText);
        }
    }

    private void setTitleWithResourceID(int resId) {
        String title = "";
        try {
            title = context.getString(resId);
        } catch (Exception e) {
        }
        ((TextView) findViewById(R.id.tv_title)).setText(title);
    }
}
