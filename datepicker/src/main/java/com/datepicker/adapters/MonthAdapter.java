package com.datepicker.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datepicker.R;
import com.datepicker.components.JDF;
import com.datepicker.components.SquareTextView;
import com.datepicker.interfaces.DateInterface;

import java.text.ParseException;

/**
 * Created by Alireza Afkar on 2/11/16 AD.
 */
public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.ViewHolder> {
    private static final int TYPE_DAY = 0;
    private static final int TYPE_TITLE = 1;
    private static final int TYPE_NONE = 2;

    private int mYear;
    private int mMonth;
    private JDF mToday;
    private Long maxDate;
    private Long minDate;
    private int mStartDay;
    private DateInterface mCallback;
    private View.OnClickListener mOnClickListener;

    public MonthAdapter(DateInterface callback, View.OnClickListener onClickListener, int currentMonth, int chosenYear) {
        mToday = new JDF();
        mYear = chosenYear;
        mCallback = callback;
        mMonth = currentMonth + 1;
        mOnClickListener = onClickListener;
        maxDate = callback.getDateItem().getMaxDate().getTimeInMillis();
        minDate = callback.getDateItem().getMinDate().getTimeInMillis();

        try {
            mStartDay = new JDF().getGregorianDay(mYear, mMonth, 1);
        } catch (ParseException ignored) {
        }
    }

    private boolean isSelected(int day) {
        return mCallback.getMonth() == mMonth &&
                mCallback.getDay() == day &&
                mCallback.getYear() == mYear;
    }

    private boolean isToday(int day) {
        return (mMonth == mToday.getGregorianMonth()
                && day == mToday.getGregorianDay()
                && mYear == mToday.getGregorianYear());
    }

    @NonNull
    @Override
    public MonthAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthAdapter.ViewHolder holder, int position) {
        String text = null;
        int day;
        boolean checked = false;
        boolean selected = false;
        boolean clickable = false;

        int viewType = getItemViewType(position);

        if (viewType == TYPE_TITLE) {
            text = mCallback.getWeekDaysٍٍShortcut()[position];
        } else if (viewType == TYPE_DAY) {
            day = getDay(position);
            selected = isSelected(day);
            text = String.valueOf(day);
            clickable = isSelectableDay(day);
            checked = isToday(day);
        }

        holder.mTextView.setClickable(clickable);
        holder.mTextView.setSelected(selected);
        holder.mTextView.setEnabled(clickable);
        holder.mTextView.setChecked(checked);
        holder.mTextView.setText(text);
    }

    private boolean isSelectableDay(int day) {
        long date = new JDF(mYear, mMonth, day).getTimeInMillis();
        return date >= minDate && date <= maxDate;
    }

    private int getDay(int position) {
        return (position - mStartDay - 7) + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position < 7) {
            return TYPE_TITLE;
        } else if (position - 7 >= mStartDay) {
            return TYPE_DAY;
        } else {
            return TYPE_NONE;
        }
    }

    @Override
    public int getItemCount() {
        int days = 30;
        if (mMonth == 1 || mMonth == 3 || mMonth == 5 || mMonth == 7 || mMonth == 8 || mMonth == 10 || mMonth == 12 )
            days = 31;
        if (mMonth == 2 && !JDF.isLeapYear(mYear))
            days = 29;

        return days + 7 + mStartDay;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SquareTextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = (SquareTextView) itemView;
            mTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getDay(getLayoutPosition());
            if (mCallback == null || position < 0) return;

            int oldMonth = mCallback.getMonth();
            mCallback.setDay(position, mMonth, mYear);
            if (oldMonth != mMonth) {
                mOnClickListener.onClick(view);
            } else {
                notifyDataSetChanged();
            }
        }
    }
}
