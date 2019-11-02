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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import static com.asb.taxapp.Utils.hideKeyboardFrom;

public class IndividualFragment extends BaseFragment implements DateSetListener {
    private int year = 2005;
    private int kh;
    private int id;
    private double A = 0.0;
    private double x0, x1, x2, x3, x4, r, r1, r2, r3, r4;
    private float percent1, percent2, percent3, percent4;
    private EditText valueEditText;
    private ImageView calculatorImageView;
    private LinearLayout discountLinearLayout, detailsLinearLayout, extraLinearLayout, extra2LinearLayout;
    private FrameLayout taxFrameLayout, tax2FrameLayout, taxRatioFrameLayout, fakeFrameLayout;
    private CTextView sec0TextView, sec1TextView, sec2TextView, sec3TextView, sec4TextView;
    private CTextView sh0TextView, sh1TextView, sh2TextView, sh3TextView, sh4TextView;
    private CTextView per0TextView, per1TextView, per2TextView, per3TextView, per4TextView;
    private CTextView result0TextView, result1TextView, result2TextView, result3TextView, result4TextView;
    private CTextView startDateTextView, endDateTextView, calculateTextView, resultTaxTextView, result2TaxTextView, dateRatioTextView,
            periodTextView, yearsTextView, taxRatioTextView, extraTaxTextView, extra2TaxTextView, discountTextView;
    private CheckBox discountCheckBox, withoutCheckBox, extraCheckBox, extra2CheckBox;
    private boolean isDiscount = true, isExtra = true, isWithout = false;
    private YearDialogFragment yearDialog;
    private MessageDialogFragment messageDialogFragment;
    private Date startDate = new Date();
    private Date endDate = new Date();
    private DateItem startDateItem = new DateItem();
    private DateItem endDateItem = new DateItem();
    private JDF startJdf, endJdf;
    private double dateRatio;
    private String dateFormat = "yyyy-MM-dd";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();

    public static IndividualFragment newInstance() {
        Bundle args = new Bundle();
        IndividualFragment fragment = new IndividualFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_individual, container, false);
        init(view);
        BaseActivity.forceLocale(getActivity(), "en");
        if (getActivity().getCurrentFocus() != null) {
            hideKeyboardFrom(getActivity());
        }

        try {
            fillDate(year);
        } catch (ParseException e) {
            Log.e("Parse", e.getMessage());
        }

        dateRatio = (betweenTwoDates(startCalendar.getTime(), endCalendar.getTime())) / 12;
        dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));

        if (getArguments() != null) {
            valueEditText.setText(getArguments().getString("VALUE")); // Get value from Calculator
//            if (dateRatio > 1) {
//                dateRatio = 1;  //for ratio correct
//            }
            if (valueEditText.getText().toString().equals("")) {
                restViews();
                periodTextView.setText("");
            } else {

                    A = Double.valueOf(valueEditText.getText().toString());

                periodTextView.setText(String.valueOf(Math.round((A * dateRatio) * 100.0) / 100.0));
            }
        }

        calculatorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(getActivity(), CalculatorActivity.class), 1);
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
                if (year == 2017 || year == 2018 || year == 2019) {
                    discountLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    discountLinearLayout.setVisibility(View.GONE);
                }
                restViews();
                if (isWithout) {
                    if (Arrays.asList(new Integer[]{2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012}).contains(year)) {
                        taxRatioTextView.setText("20 %");
                    } else if (Arrays.asList(new Integer[]{2013, 2014}).contains(year)) {
                        taxRatioTextView.setText("25 %");
                    } else if (Arrays.asList(new Integer[]{2015, 2016, 2017, 2018, 2019}).contains(year)) {
                        taxRatioTextView.setText("22.5 %");
                    }
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

                builder.build(IndividualFragment.this).show(getFragmentManager(), "");
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

                builder.build(IndividualFragment.this).show(getFragmentManager(), "");
            }
        });

        discountCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDiscount = true;
                    calc();
                    result();
                } else {
                    isDiscount = false;
                    discountTextView.setText("");
                    calc();
                    result();
                }
            }
        });

        withoutCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isWithout = true;
                    detailsLinearLayout.setVisibility(View.GONE);
                    taxFrameLayout.setVisibility(View.GONE);
                    taxRatioFrameLayout.setVisibility(View.VISIBLE);
                    tax2FrameLayout.setVisibility(View.VISIBLE);
                    fakeFrameLayout.setVisibility(View.VISIBLE);
                    result2TaxTextView.setText("0");
                } else {
                    isWithout = false;
                    detailsLinearLayout.setVisibility(View.VISIBLE);
                    taxFrameLayout.setVisibility(View.VISIBLE);
                    taxRatioFrameLayout.setVisibility(View.GONE);
                    tax2FrameLayout.setVisibility(View.GONE);
                    fakeFrameLayout.setVisibility(View.GONE);
                }
            }
        });

        extraCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isExtra = true;
                    extraTaxTextView.setVisibility(View.VISIBLE);
                    calc();
                    result();
                } else {
                    isExtra = false;
                    extraTaxTextView.setVisibility(View.GONE);
                    calc();
                    result();
                }
            }
        });

        extra2CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isExtra = true;
                    extra2TaxTextView.setVisibility(View.VISIBLE);
                    calc2();
                } else {
                    isExtra = false;
                    extra2TaxTextView.setVisibility(View.GONE);
                    calc2();
                }
            }
        });

        calculateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valueEditText.getText().toString().equals("")) {
                    restViews();
                    return;
                }
                per0TextView.setText("0%");
//                hideKeyboardFrom(getActivity());
                hideKeyboard(getActivity());
                A = Double.valueOf(valueEditText.getText().toString()) * dateRatio;
                if (isWithout) {
                    calc2();
                } else {
                    calc();
                }
                result();
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
                    restViews();
                    periodTextView.setText("");
                } else if (valueEditText.getText().toString().equals(".")) {
                    return;
                } else {
                    A = Double.valueOf(valueEditText.getText().toString());
                    periodTextView.setText(String.valueOf(Math.round((A * dateRatio) * 100.0) / 100.0));
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
        sec0TextView = view.findViewById(R.id.sec0_tv_id);
        sec1TextView = view.findViewById(R.id.sec1_tv_id);
        sec2TextView = view.findViewById(R.id.sec2_tv_id);
        sec3TextView = view.findViewById(R.id.sec3_tv_id);
        sec4TextView = view.findViewById(R.id.sec4_tv_id);
        sh0TextView = view.findViewById(R.id.sh0_tv_id);
        sh1TextView = view.findViewById(R.id.sh1_tv_id);
        sh2TextView = view.findViewById(R.id.sh2_tv_id);
        sh3TextView = view.findViewById(R.id.sh3_tv_id);
        sh4TextView = view.findViewById(R.id.sh4_tv_id);
        per0TextView = view.findViewById(R.id.per0_tv_id);
        per1TextView = view.findViewById(R.id.per1_tv_id);
        per2TextView = view.findViewById(R.id.per2_tv_id);
        per3TextView = view.findViewById(R.id.per3_tv_id);
        per4TextView = view.findViewById(R.id.per4_tv_id);
        result0TextView = view.findViewById(R.id.result0_tv_id);
        result1TextView = view.findViewById(R.id.result1_tv_id);
        result2TextView = view.findViewById(R.id.result2_tv_id);
        result3TextView = view.findViewById(R.id.result3_tv_id);
        result4TextView = view.findViewById(R.id.result4_tv_id);
        discountTextView = view.findViewById(R.id.discount_tax_tv_id);
        discountCheckBox = view.findViewById(R.id.discount_cb_id);
        withoutCheckBox = view.findViewById(R.id.without_cb_id);
        extraCheckBox = view.findViewById(R.id.extra_cb_id);
        extra2CheckBox = view.findViewById(R.id.extra2_cb_id);
        discountLinearLayout = view.findViewById(R.id.discount_ll_id);
        yearsTextView = view.findViewById(R.id.years_tv_id);
        calculateTextView = view.findViewById(R.id.calculate_tax_tv_id);
        resultTaxTextView = view.findViewById(R.id.result_tax_tv_id);
        result2TaxTextView = view.findViewById(R.id.result2_tax_tv_id);
        dateRatioTextView = view.findViewById(R.id.date_ratio_tv_id);
        periodTextView = view.findViewById(R.id.period_tv_id);
        extraLinearLayout = view.findViewById(R.id.extra_ll_id);
        extra2LinearLayout = view.findViewById(R.id.extra2_ll_id);
        extraTaxTextView = view.findViewById(R.id.extra_tax_tv_id);
        extra2TaxTextView = view.findViewById(R.id.extra2_tax_tv_id);
        calculatorImageView = view.findViewById(R.id.calculator_iv_id);
        detailsLinearLayout = view.findViewById(R.id.details_ll_id);
        taxFrameLayout = view.findViewById(R.id.tax_fl_id);
        tax2FrameLayout = view.findViewById(R.id.tax2_fl_id);
        taxRatioFrameLayout = view.findViewById(R.id.tax_ratio_fl_id);
        taxRatioTextView = view.findViewById(R.id.tax_ratio_tv_id);
        fakeFrameLayout = view.findViewById(R.id.fake_fl_id);

        yearDialog = YearDialogFragment.newInstance();
        discountCheckBox.setChecked(isDiscount);
        extraCheckBox.setChecked(isExtra);
        extra2CheckBox.setChecked(isExtra);
        discountLinearLayout.setVisibility(View.GONE);
    }

    void restViews() {
        sh0TextView.setText("");
        sh1TextView.setText("");
        sh2TextView.setText("");
        sh3TextView.setText("");
        sh4TextView.setText("");
        sec0TextView.setText("");
        sec1TextView.setText("");
        sec2TextView.setText("");
        sec3TextView.setText("");
        sec4TextView.setText("");
        sh0TextView.setText("");
        sh1TextView.setText("");
        sh2TextView.setText("");
        sh3TextView.setText("");
        sh4TextView.setText("");
        per0TextView.setText("");
        per1TextView.setText("");
        per2TextView.setText("");
        per3TextView.setText("");
        per4TextView.setText("");
        result0TextView.setText("");
        result1TextView.setText("");
        result2TextView.setText("");
        result3TextView.setText("");
        result4TextView.setText("");
        resultTaxTextView.setText("0.0");
    }

    void calc() {
        if (year == 2005 || year == 2006 || year == 2007 || year == 2008 || year == 2009 || year == 2010) {
            percent1 = 0.1f;
            percent2 = 0.15f;
            percent3 = 0.2f;
            per1TextView.setText("10%");
            per2TextView.setText("15%");
            per3TextView.setText("20%");
            per4TextView.setText("");
            sec0TextView.setText("1 - 5000");
            sec1TextView.setText("5001 - 20000");
            sec2TextView.setText("20001 - 40000");
            sec3TextView.setText("40001 -   *");
            sec4TextView.setText("");
            if (A > 40000) {
                x4 = 0;
                x3 = A - 40000;
                x2 = 20000;
                x1 = 15000;
                x0 = 5000;
            } else if (A > 20000 && A <= 40000) {
                x4 = 0;
                x3 = 0;
                x2 = A - 20000;
                x1 = 15000;
                x0 = 5000;
            } else if (A > 5000 && A <= 20000) {
                x4 = 0;
                x3 = 0;
                x2 = 0;
                x1 = A - 5000;
                x0 = 5000;
            } else if (A <= 5000) {
                x4 = 0;
                x3 = 0;
                x2 = 0;
                x1 = 0;
                x0 = 0;
            }
        } else if (year == 2011 || year == 2012) {
            percent1 = 0.1f;
            percent2 = 0.15f;
            percent3 = 0.2f;
            percent4 = 0.25f;
            per1TextView.setText("10%");
            per2TextView.setText("15%");
            per3TextView.setText("20%");
            per4TextView.setText("25%");
            sec0TextView.setText("1 - 5000");
            sec1TextView.setText("5001 - 20000");
            sec2TextView.setText("20001 - 40000");
            sec3TextView.setText("40001 - 10000000");
            sec4TextView.setText("10000001 -   *");
            if (A > 1000000) {
                x4 = A - 10000000;
                x3 = 40000;
                x2 = 20000;
                x1 = 15000;
                x0 = 5000;
            }
            if (A > 40000 && A <= 10000000) {
                x4 = 0;
                x3 = A - 40000;
                x2 = 20000;
                x1 = 15000;
                x0 = 5000;
            } else if (A > 20000 && A <= 40000) {
                x4 = 0;
                x3 = 0;
                x2 = A - 20000;
                x1 = 15000;
                x0 = 5000;
            } else if (A > 5000 && A <= 20000) {
                x4 = 0;
                x3 = 0;
                x2 = 0;
                x1 = A - 5000;
                x0 = 5000;
            } else if (A <= 5000) {
                x4 = 0;
                x3 = 0;
                x2 = 0;
                x1 = 0;
                x0 = 0;
            }
        } else if (year == 2013 || year == 2014) {
            percent1 = 0.1f;
            percent2 = 0.15f;
            percent3 = 0.2f;
            percent4 = 0.25f;
            per1TextView.setText("10%");
            per2TextView.setText("15%");
            per3TextView.setText("20%");
            per4TextView.setText("25%");
            sec0TextView.setText("1 - 5000");
            sec1TextView.setText("5001 - 30000");
            sec2TextView.setText("30001 - 45000");
            sec3TextView.setText("45001 - 250000");
            sec4TextView.setText("250001 -   *");
            if (A > 250000) {
                x4 = A - 250000;
                x3 = 205000;
                x2 = 15000;
                x1 = 25000;
                x0 = 5000;
            }
            if (A > 45000 && A <= 250000) {
                x4 = 0;
                x3 = A - 45000;
                x2 = 15000;
                x1 = 25000;
                x0 = 5000;
            } else if (A > 30000 && A <= 45000) {
                x4 = 0;
                x3 = 0;
                x2 = A - 30000;
                x1 = 25000;
                x0 = 5000;
            } else if (A > 5000 && A <= 30000) {
                x4 = 0;
                x3 = 0;
                x2 = 0;
                x1 = A - 5000;
                x0 = 5000;
            } else if (A <= 5000) {
                x4 = 0;
                x3 = 0;
                x2 = 0;
                x1 = 0;
                x0 = 0;
            }
        } else if (year == 2015 || year == 2016) {
            percent1 = 0.1f;
            percent2 = 0.15f;
            percent3 = 0.2f;
            percent4 = 0.225f;
            per1TextView.setText("10%");
            per2TextView.setText("15%");
            per3TextView.setText("20%");
            per4TextView.setText("22.5%");
            sec0TextView.setText("1 - 6500");
            sec1TextView.setText("6500 - 30000");
            sec2TextView.setText("30001 - 45000");
            sec3TextView.setText("45001 - 200000");
            sec4TextView.setText("200001 -   *");
            if (A > 200000) {
                x4 = A - 200000;
                x3 = 155000;
                x2 = 15000;
                x1 = 23500;
                x0 = 6500;
            }
            if (A > 45000 && A <= 200000) {
                x4 = 0;
                x3 = A - 45000;
                x2 = 15000;
                x1 = 23500;
                x0 = 6500;
            } else if (A > 30000 && A <= 45000) {
                x4 = 0;
                x3 = 0;
                x2 = A - 30000;
                x1 = 23500;
                x0 = 6500;
            } else if (A > 6500 && A <= 30000) {
                x4 = 0;
                x3 = 0;
                x2 = 0;
                x1 = A - 6500;
                x0 = 6500;
            } else if (A <= 6500) {
                x4 = 0;
                x3 = 0;
                x2 = 0;
                x1 = 0;
                x0 = 0;
            }
        } else if (year == 2017) {
            percent1 = 0.1f;
            percent2 = 0.15f;
            percent3 = 0.2f;
            percent4 = 0.225f;
            per1TextView.setText("10%");
            per2TextView.setText("15%");
            per3TextView.setText("20%");
            per4TextView.setText("22.5%");
            sec0TextView.setText("1 - 7200");
            sec1TextView.setText("7200 - 30000");
            sec2TextView.setText("30001 - 45000");
            sec3TextView.setText("45001 - 200000");
            sec4TextView.setText("200001 -   *");
            if (A > 200000) {
                x4 = A - 200000;
                x3 = 155000;
                x2 = 15000;
                x1 = 22800;
                x0 = 7200;
                kh = 5;
            }
            if (A > 45000 && A <= 200000) {
                x4 = 0;
                x3 = A - 45000;
                x2 = 15000;
                x1 = 22800;
                x0 = 7200;
                kh = 4;
            } else if (A > 30000 && A <= 45000) {
                x4 = 0;
                x3 = 0;
                x2 = A - 30000;
                x1 = 22800;
                x0 = 7200;
                kh = 3;
            } else if (A > 7200 && A <= 30000) {
                x4 = 0;
                x3 = 0;
                x2 = 0;
                x1 = A - 7200;
                x0 = 7200;
                kh = 2;
            } else if (A <= 7200) {
                x4 = 0;
                x3 = 0;
                x2 = 0;
                x1 = 0;
                x0 = 0;
                kh = 1;
            }
        } else if (year == 2018 || year == 2019) {
            percent1 = 0.1f;
            percent2 = 0.15f;
            percent3 = 0.2f;
            percent4 = 0.225f;
            per1TextView.setText("10%");
            per2TextView.setText("15%");
            per3TextView.setText("20%");
            per4TextView.setText("22.5%");
            sec0TextView.setText("1 - 8000");
            sec1TextView.setText("8000 - 30000");
            sec2TextView.setText("30001 - 45000");
            sec3TextView.setText("45001 - 200000");
            sec4TextView.setText("200001 -   *");
            if (A > 200000) {
                x4 = A - 200000;
                x3 = 155000;
                x2 = 15000;
                x1 = 22000;
                x0 = 8000;
                kh = 5;
            }
            if (A > 45000 && A <= 200000) {
                x4 = 0;
                x3 = A - 45000;
                x2 = 15000;
                x1 = 22000;
                x0 = 8000;
                kh = 4;
            } else if (A > 30000 && A <= 45000) {
                x4 = 0;
                x3 = 0;
                x2 = A - 30000;
                x1 = 22000;
                x0 = 8000;
                kh = 3;
            } else if (A > 8000 && A <= 30000) {
                x4 = 0;
                x3 = 0;
                x2 = 0;
                x1 = A - 8000;
                x0 = 8000;
                kh = 2;
            } else if (A <= 8000) {
                x4 = 0;
                x3 = 0;
                x2 = 0;
                x1 = 0;
                x0 = 0;
                kh = 1;
            }
        }
    }

    void calc2() {
        if (Arrays.asList(new Integer[]{2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012}).contains(year)) {
            r = Double.valueOf(valueEditText.getText().toString()) * 0.2 * dateRatio;
        } else if (Arrays.asList(new Integer[]{2013, 2014}).contains(year)) {
            r = Double.valueOf(valueEditText.getText().toString()) * 0.25 * dateRatio;
        } else if (Arrays.asList(new Integer[]{2015, 2016, 2017, 2018, 2019}).contains(year)) {
            r = Double.valueOf(valueEditText.getText().toString()) * 0.225 * dateRatio;
        }

        if (isExtra) {
            if (year == 2014 || year == 2015) {
                if (Double.valueOf(valueEditText.getText().toString()) > 1000000) {
                    r = r * 1.05;
                    extra2LinearLayout.setVisibility(View.VISIBLE);
                    extra2TaxTextView.setText("اضافة 5%");
                }
            }
        }

        result2TaxTextView.setText(String.valueOf(Math.round((r) * 100.0) / 100.0));
    }

    void result() {
        r1 = x1 * percent1;
        r2 = x2 * percent2;
        r3 = x3 * percent3;
        r4 = x4 * percent4;

        sh0TextView.setText(String.valueOf(Math.round((x0) * 100.0) / 100.0));
        sh1TextView.setText(String.valueOf(Math.round((x1) * 100.0) / 100.0));
        sh2TextView.setText(String.valueOf(Math.round((x2) * 100.0) / 100.0));
        sh3TextView.setText(String.valueOf(Math.round((x3) * 100.0) / 100.0));
        sh4TextView.setText(String.valueOf(Math.round((x4) * 100.0) / 100.0));
        result0TextView.setText(String.valueOf(0));
        result1TextView.setText(String.valueOf(Math.round((r1) * 100.0) / 100.0));
        result2TextView.setText(String.valueOf(Math.round((r2) * 100.0) / 100.0));
        result3TextView.setText(String.valueOf(Math.round((r3) * 100.0) / 100.0));
        result4TextView.setText(String.valueOf(Math.round((r4) * 100.0) / 100.0));
        r = r1 + r2 + r3 + r4;
        if (isExtra) {
            if (year == 2014 || year == 2015) {
                if (A > 1000000) {
                    r = r * 1.05;
                    extraLinearLayout.setVisibility(View.VISIBLE);
                    extraTaxTextView.setText("اضافة 5%");
                }
            }
        }
        if (isDiscount) {
            if (year == 2017) {
                if (kh == 2) {
                    r = r * 0.2f;
                    discountLinearLayout.setVisibility(View.VISIBLE);
                    discountTextView.setVisibility(View.VISIBLE);
                    discountTextView.setText("خصم 80%");
                } else if (kh == 3) {
                    r = r * 0.6f;
                    discountLinearLayout.setVisibility(View.VISIBLE);
                    discountTextView.setVisibility(View.VISIBLE);
                    discountTextView.setText("خصم 40%");
                } else if (kh == 4) {
                    r = r * 0.95f;
                    discountLinearLayout.setVisibility(View.VISIBLE);
                    discountTextView.setVisibility(View.VISIBLE);
                    discountTextView.setText("خصم 5%");
                } else if (kh == 1 || kh == 5) {
                    discountTextView.setVisibility(View.INVISIBLE);
                    discountLinearLayout.setVisibility(View.GONE);
                }
            } else if (year == 2018 || year == 2019) {
                if (kh == 2) {
                    r = r * 0.15f;
                    discountLinearLayout.setVisibility(View.VISIBLE);
                    discountTextView.setVisibility(View.VISIBLE);
                    discountTextView.setText("خصم 85%");
                } else if (kh == 3) {
                    r = r * 0.55f;
                    discountLinearLayout.setVisibility(View.VISIBLE);
                    discountTextView.setVisibility(View.VISIBLE);
                    discountTextView.setText("خصم 45%");
                } else if (kh == 4) {
                    r = r * 0.925f;
                    discountLinearLayout.setVisibility(View.VISIBLE);
                    discountTextView.setVisibility(View.VISIBLE);
                    discountTextView.setText("خصم 7.5%");
                } else if (kh == 1 || kh == 5) {
                    discountTextView.setVisibility(View.INVISIBLE);
                    discountLinearLayout.setVisibility(View.GONE);
                }
            } else {
                discountTextView.setVisibility(View.INVISIBLE);
            }
        }
        resultTaxTextView.setText(String.valueOf(Math.round((r) * 100.0) / 100.0));
    }

    public double monthsBetween(Date d1, Date d2) {
        double AVERAGE_MILLIS_PER_MONTH = 365.24 * 24 * 60 * 60 * 1000 / 12;
        return (d2.getTime() - d1.getTime()) / AVERAGE_MILLIS_PER_MONTH;
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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
                dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
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

        monthsBetween(startCalendar.getTime(), endCalendar.getTime());
        dateRatio = (betweenTwoDates(startCalendar.getTime(), endCalendar.getTime())) / 12;
//        if (dateRatio > 0.999) {
//            dateRatio = Math.round(dateRatio);
//        }
        dateRatioTextView.setText(String.valueOf(Math.round((dateRatio) * 100.0) / 100.0));
        periodTextView.setText(String.valueOf(Math.round((A * dateRatio) * 100.0) / 100.0));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hideKeyboard(getActivity());
    }
}
