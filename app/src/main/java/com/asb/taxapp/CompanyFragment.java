package com.asb.taxapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.datepicker.DatePicker;
import com.datepicker.components.DateItem;
import com.datepicker.components.JDF;
import com.datepicker.interfaces.DateSetListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.asb.taxapp.Utils.betweenTwoDates;

public class CompanyFragment extends BaseFragment implements DateSetListener {
    private int year = 2005;
    private double A = 0.0;
    private double dateRatio;
    private int id;
    private EditText valueEditText;
    private CTextView startDateTextView, endDateTextView, calculateTextView, resultTextView, dateRatioTextView,
            periodTextView, yearsTextView, taxRatioTextView, extraTaxTextView;
    private YearDialogFragment yearDialog;
    private MessageDialogFragment messageDialogFragment;
    private Date startDate = new Date();
    private Date endDate = new Date();
    private DateItem startDateItem = new DateItem();
    private DateItem endDateItem = new DateItem();
    private JDF startJdf, endJdf;
    private String dateFormat = "yyyy-MM-dd";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private CheckBox extraCheckBox;
    private LinearLayout extraLinearLayout;
    private double result;
    private boolean isExtra = true;
    private ImageView calculatorImageView;

    public static CompanyFragment newInstance() {
        Bundle args = new Bundle();
        CompanyFragment fragment = new CompanyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        init(view);

        try {
            fillDate(year);
        } catch (ParseException e) {
            Log.e("Parse", e.getMessage());
        }

        dateRatio = (betweenTwoDates(startCalendar.getTime(), endCalendar.getTime())) / 12;
        dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));


        if (getArguments() != null) {
            valueEditText.setText(getArguments().getString("VALUE"));
            if (dateRatio > 1) {
                dateRatio = 1;  //for ratio correct
            }
            if (valueEditText.getText().toString().equals("")) {
                periodTextView.setText("");
            } else {
                A = Double.valueOf(valueEditText.getText().toString());
                periodTextView.setText(String.valueOf(Math.round((A * dateRatio)*100.0)/100.0));
            }
        }

        calculatorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(getActivity(), CalculatorActivity.class), 2);
                getActivity().overridePendingTransition(R.anim.fragment_slide_in_right, 0);
            }
        });


        yearsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearDialog.show(getFragmentManager(), "dialog");
            }
        });

        yearDialog.setOnItemSelectedListener(new YearDialogFragment.OnItemSelectedListener() {
            @Override
            public void onItemClick(View view, int position) {
                yearsTextView.setText(String.valueOf(position));
                year = position;
                try {
                    fillDate(year);
                } catch (ParseException e) {
                    Log.e("Parse", e.getMessage());
                }
                yearDialog.dismiss();
            }
        });

        startDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = startDateTextView.getId() == R.id.start_date_tv_id ? 1 : 2;
                DatePicker.Builder builder = new DatePicker.Builder()
                        .setRetainInstance(true)
                        .id(id)
                        .date(startDateItem.getDay(), startDateItem.getMonth(), startDateItem.getYear());

                builder.build(CompanyFragment.this).show(getFragmentManager(), "");
            }
        });


        endDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = endDateTextView.getId() == R.id.end_date_tv_id ? 2 : 1;
                DatePicker.Builder builder = new DatePicker.Builder()
                        .id(id)
                        .setRetainInstance(true)
                        .date(endDateItem.getDay(), endDateItem.getMonth(), endDateItem.getYear());

                builder.build(CompanyFragment.this).show(getFragmentManager(), "");
            }
        });

        extraCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isExtra = true;
                    extraTaxTextView.setVisibility(View.VISIBLE);
                    calc();
                } else {
                    isExtra = false;
                    extraTaxTextView.setVisibility(View.GONE);
                    calc();
                }
            }
        });

        calculateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valueEditText.getText().toString().equals("")) {
                    return;
                }
                hideKeyboard(getActivity());
                A = Double.valueOf(valueEditText.getText().toString()) * dateRatio;
                calc();
            }
        });


        valueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                dateRatio = (monthsBetween(startCalendar.getTime(), endCalendar.getTime()) + 0.05) / 12;
//                if (dateRatio > 1) {
//                    dateRatio = 1;
//                }
                if (valueEditText.getText().toString().equals("")) {
                    periodTextView.setText("");
                } else if (valueEditText.getText().toString().equals(".")) {
                    return;
                } else {
                    A = Double.valueOf(valueEditText.getText().toString());
                    periodTextView.setText(String.valueOf(Math.round((A * dateRatio)*100.0)/100.0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    void init(View view) {
        valueEditText = view.findViewById(R.id.value_et_id);
        startDateTextView = view.findViewById(R.id.start_date_tv_id);
        endDateTextView = view.findViewById(R.id.end_date_tv_id);
        yearsTextView = view.findViewById(R.id.years_tv_id);
        calculateTextView = view.findViewById(R.id.calculate_tax_tv_id);
        resultTextView = view.findViewById(R.id.result_tax_tv_id);
        dateRatioTextView = view.findViewById(R.id.date_ratio_tv_id);
        periodTextView = view.findViewById(R.id.period_tv_id);
        calculatorImageView = view.findViewById(R.id.calculator_iv_id);
        taxRatioTextView = view.findViewById(R.id.tax_ratio_tv_id);
        extraLinearLayout = view.findViewById(R.id.extra_ll_id);
        extraTaxTextView = view.findViewById(R.id.extra_tax_tv_id);
        extraCheckBox = view.findViewById(R.id.extra_cb_id);

        yearDialog = YearDialogFragment.newInstance();
        extraCheckBox.setChecked(isExtra);
    }


    void calc() {
        if (Arrays.asList(new Integer[]{2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012}).contains(year)) {
            result = Double.valueOf(valueEditText.getText().toString()) * 0.2 * dateRatio;
        } else if (Arrays.asList(new Integer[]{2013, 2014}).contains(year)) {
            result = Double.valueOf(valueEditText.getText().toString()) * 0.25 * dateRatio;
        } else if (Arrays.asList(new Integer[]{2015, 2016, 2017, 2018, 2019}).contains(year)) {
            result = Double.valueOf(valueEditText.getText().toString()) * 0.225 * dateRatio;
        }

        if (isExtra) {
            if (year == 2014 || year == 2015) {
                if (Double.valueOf(valueEditText.getText().toString()) > 1000000) {
                    result = result * 1.05;
                    extraLinearLayout.setVisibility(View.VISIBLE);
                    extraTaxTextView.setText("اضافة 5%");
                }
            }
        }


        resultTextView.setText(String.valueOf(Math.round((result)*100.0)/100.0));
    }

    void fillDate(int year) throws ParseException {
        switch (year) {
            case 2005:
                startDateTextView.setText("2005-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2005-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("20 %");
                break;
            case 2006:
                startDateTextView.setText("2006-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2006-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("20 %");
                break;
            case 2007:
                startDateTextView.setText("2007-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2007-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("20 %");
                break;
            case 2008:
                startDateTextView.setText("2008-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2008-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("20 %");
                break;
            case 2009:
                startDateTextView.setText("2009-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2009-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("20 %");
                break;
            case 2010:
                startDateTextView.setText("2010-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2010-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("20 %");
                break;
            case 2011:
                startDateTextView.setText("2011-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2011-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("20 %");
                break;
            case 2012:
                startDateTextView.setText("2012-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2012-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("20 %");
                break;
            case 2013:
                startDateTextView.setText("2013-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2013-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("25 %");
                break;
            case 2014:
                startDateTextView.setText("2014-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2014-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("25 %");
                break;
            case 2015:
                startDateTextView.setText("2015-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2015-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("22.5 %");
                break;
            case 2016:
                startDateTextView.setText("2016-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2016-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("22.5 %");
                break;
            case 2017:
                startDateTextView.setText("2017-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2017-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("22.5 %");
                break;
            case 2018:
                startDateTextView.setText("2018-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2018-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("22.5 %");
                break;
            case 2019:
                startDateTextView.setText("2019-1-1");
                startDate = simpleDateFormat.parse(startDateTextView.getText().toString());
                startCalendar.setTime(startDate);
                startJdf = new JDF(startCalendar);
                startDateItem.setDate(startJdf);
                endDateTextView.setText("2019-12-31");
                endDate = simpleDateFormat.parse(endDateTextView.getText().toString());
                endCalendar.setTime(endDate);
                endJdf = new JDF(endCalendar);
                endDateItem.setDate(endJdf);
                dateRatio = 1;
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
                taxRatioTextView.setText("22.5 %");
                break;
        }
    }

    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        if (id == 1) {
            startCalendar = calendar;
            if (startCalendar.getTime().before(endCalendar.getTime())) {
                startDateItem.setDate(day, month, year);
                startDateTextView.setText(startDateItem.getDate());
            } else {
                messageDialogFragment = MessageDialogFragment.newInstance("تاريخ بداية الفترة لابد أن يكون أصغر من نهاية الفترة");
                messageDialogFragment.show(getFragmentManager(), "message");
            }
        } else if (id == 2) {
            endCalendar = calendar;
            if (startCalendar.getTime().before(endCalendar.getTime())) {
                endDateItem.setDate(day, month, year);
                endDateTextView.setText(endDateItem.getDate());
            } else {
                messageDialogFragment = MessageDialogFragment.newInstance("تاريخ بداية الفترة لابد أن يكون أصغر من نهاية الفترة");
                messageDialogFragment.show(getFragmentManager(), "message");
            }
        }


//        monthsBetween(startCalendar.getTime(), endCalendar.getTime());
        dateRatio = (betweenTwoDates(startCalendar.getTime(), endCalendar.getTime())) / 12;
        if (dateRatio > 1) {
            dateRatio = 1;
        }
        dateRatioTextView.setText(String.valueOf(Math.round((dateRatio)*100.0)/100.0));
        periodTextView.setText(String.valueOf(Math.round((A * dateRatio)*100.0)/100.0));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hideKeyboard(getActivity());
    }
}
