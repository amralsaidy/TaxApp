package com.asb.taxapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

/**
 * Created by cuongmv162 on 2/1/2017.
 */

public class YearDialogFragment extends DialogFragment {

    final Integer[] years = new Integer[]{2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019};
    private View view;
    private GridView yearsGridView;
    Animation animScaleOut;
    private Animation fadeIn;
    ArrayAdapter<Integer> arrayAdapter;
    OnItemSelectedListener onItemSelectedListener;

    public static YearDialogFragment newInstance() {
        Bundle args = new Bundle();
        YearDialogFragment fragment = new YearDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        view = inflater.inflate(R.layout.dialog_layout, null);

        animScaleOut = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_out);
        fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_out_and_fade_in);

        view.startAnimation(animScaleOut);

        yearsGridView = view.findViewById(R.id.years_gv_id);
        arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_year, years);
        yearsGridView.setAdapter(arrayAdapter);
        yearsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedListener.onItemClick(view, years[position]);
            }
        });

        return view;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener){
        this.onItemSelectedListener = onItemSelectedListener;
    }

    interface OnItemSelectedListener{
        void onItemClick(View view, int position);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
