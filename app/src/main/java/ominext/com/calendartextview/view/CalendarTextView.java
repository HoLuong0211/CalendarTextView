package ominext.com.calendartextview.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.Calendar;

import ominext.com.calendartextview.R;
import ominext.com.calendartextview.util.DateTimeUtils;

/**
 * Created by LuongHH on 2/16/2017.
 */

public class CalendarTextView extends LinearLayout {

    private static final int POPUP_WIDTH = R.dimen._140sdp;
    private static final int POPUP_HEIGHT = R.dimen._132sdp;

    // internal components
    private EditText etDate;
    private ImageView btnCalendar;
    private PopupWindow mPopupWindow;

    private Calendar mCurrentDate;

    public CalendarTextView(Context context) {
        super(context);
        initControl(context);
    }

    public CalendarTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public CalendarTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_textview, this);
        assignUiElements();
        assignClickHandlers();
        assignClickHandlers();
        mCurrentDate = Calendar.getInstance();
    }

    private void assignUiElements() {
        etDate = (EditText) findViewById(R.id.et_date);
        btnCalendar = (ImageView) findViewById(R.id.btn_calendar);
    }

    private void assignClickHandlers() {
        btnCalendar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopupWindow == null || !mPopupWindow.isShowing()) {
                    showCalendarPopup(btnCalendar);
                }
            }
        });
    }

    private void showCalendarPopup(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.calendar_popup,
                    (ViewGroup) findViewById(R.id.popup_element));

            // create width and height PopupWindow
            int width = getContext().getResources().getDimensionPixelOffset(POPUP_WIDTH);
            int height = getContext().getResources().getDimensionPixelOffset(POPUP_HEIGHT);
            mPopupWindow = new PopupWindow(layout, width, height, true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mPopupWindow.setOutsideTouchable(true);

            // display the popup in the center
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mPopupWindow.showAsDropDown(v, 0, 0, Gravity.END);
            } else {
                mPopupWindow.showAsDropDown(v, v.getWidth() - mPopupWindow.getWidth(), 0);
            }

            CalendarView calendarView = (CalendarView) layout.findViewById(R.id.calendar_view);
            calendarView.setCurrentDate(mCurrentDate);
            calendarView.setEventHandler(new CalendarView.EventHandler() {
                @Override
                public void onDayPress(Calendar cal) {
                    setCurrentDate(cal);
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setEnabled(boolean isEnabled) {
        btnCalendar.setEnabled(isEnabled);
    }

    public void reset() {
        mCurrentDate = Calendar.getInstance();
        etDate.setText("");
    }

    public Calendar getCurrentDate() {
        return mCurrentDate;
    }

    public void setCurrentDate(Calendar currentDate) {
        mCurrentDate = currentDate;
        etDate.setText(DateTimeUtils.formatJapaneseDate(mCurrentDate.getTime()));
    }
}
