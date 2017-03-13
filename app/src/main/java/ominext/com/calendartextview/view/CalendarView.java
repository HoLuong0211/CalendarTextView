package ominext.com.calendartextview.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import ominext.com.calendartextview.R;

/**
 * Created by LuongHH on 2/16/2017.
 */
public class CalendarView extends LinearLayout {

    // how many days to show, defaults to five weeks, 35 days
    private static final int DAYS_COUNT = 35;

    // current displayed month
    private Calendar mCurrentDate = Calendar.getInstance();
    private Calendar mSelectedDate;

    //event handling
    private EventHandler mEventHandler = null;

    // internal components
    private View btnPrev;
    private View btnNext;
    private TextView txtCurDate;
    private GridView grid;

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_table, this);

        assignUiElements();
        assignClickHandlers();
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        txtCurDate = (TextView) findViewById(R.id.calendar_date_display);
        grid = (GridView) findViewById(R.id.calendar_grid);
    }

    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentDate.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentDate.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // handle long-press
                if (mEventHandler != null) {
                    mEventHandler.onDayPress((Calendar) adapterView.getItemAtPosition(position));
                }
            }
        });
    }

    public void setCurrentDate(Calendar currentDate) {
        mCurrentDate = currentDate;
        mSelectedDate = (Calendar) currentDate.clone();
        updateCalendar();
    }

    /**
     * Display dates correctly in grid
     */
    private void updateCalendar() {
        ArrayList<Calendar> cells = new ArrayList<>();
        Calendar calendar = (Calendar) mCurrentDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT) {
            Calendar cal = (Calendar) calendar.clone();
            cells.add(cal);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells));

        // update title
        txtCurDate.setText(mCurrentDate.get(Calendar.YEAR) + getContext().getString(R.string.year)
                + " " + (mCurrentDate.get(Calendar.MONTH) + 1) + getContext().getString(R.string.month));
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler) {
        this.mEventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler {
        void onDayPress(Calendar cal);
    }

    private class CalendarAdapter extends ArrayAdapter<Calendar> {
        // for view inflation
        private LayoutInflater inflater;

        CalendarAdapter(Context context, ArrayList<Calendar> days) {
            super(context, R.layout.calendar_day_item, days);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View view, @NonNull ViewGroup parent) {
            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.calendar_day_item, parent, false);
            TextView tvDate = (TextView) view.findViewById(R.id.tv_date_item);

            // day in question
            Calendar cal = getItem(position);
            if (cal.get(Calendar.MONTH) != mCurrentDate.get(Calendar.MONTH)) {
                view.setVisibility(INVISIBLE);
            } else {
                tvDate.setText(cal.get(Calendar.DAY_OF_MONTH) + "");
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                int drawableId = R.drawable.bg_white_solid_gray_stroke;
                switch (dayOfWeek) {
                    case Calendar.SUNDAY:
                        drawableId = R.drawable.bg_light_pink_solid_gray_stroke;
                        break;
                    case Calendar.SATURDAY:
                        drawableId = R.drawable.bg_light_sky_blue_solid_gray_stroke;
                        break;
                    default:
                        break;
                }

                if (mSelectedDate.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && mSelectedDate.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)) {
                    drawableId = R.drawable.bg_light_orange_solid_gray_stroke;
                }

                // setBackground
                Drawable background;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    background = getContext().getDrawable(drawableId);
                } else {
                    background = getContext().getResources().getDrawable(drawableId);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tvDate.setBackground(background);
                } else {
                    tvDate.setBackgroundDrawable(background);
                }
            }
            return view;
        }
    }
}
