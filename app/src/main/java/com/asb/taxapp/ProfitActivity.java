package com.asb.taxapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;


public class ProfitActivity extends BaseActivity {
    private CTextView individualCTextView;
    private CTextView companyCTextView;
    private static int selectedFragment = 0;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);
        setStatusBarGradiant(this, R.color.md_cyan_A700);
        forceLocale(this, "en");
        individualCTextView = findViewById(R.id.individual_tv_id);
        companyCTextView = findViewById(R.id.company_tv_id);


        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_fl_id, IndividualFragment.newInstance()).commit();



        individualCTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFragment != 1) {
                    selectedFragment = 1;
                    individualCTextView.setBackground(getResources().getDrawable(R.color.md_cyan_300));
                    companyCTextView.setBackground(getResources().getDrawable(R.color.md_cyan_A700));
                    getSupportFragmentManager().beginTransaction()
//                            .setCustomAnimations(R.anim.fragment_slide_in_left, 0)
                            .replace(R.id.fragment_container_fl_id, IndividualFragment.newInstance()).commit();
                }
            }
        });

        companyCTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFragment != 2) {
                    selectedFragment = 2;
                    companyCTextView.setBackground(getResources().getDrawable(R.color.md_cyan_300));
                    individualCTextView.setBackground(getResources().getDrawable(R.color.md_cyan_A700));
                    getSupportFragmentManager().beginTransaction()
//                            .setCustomAnimations(R.anim.fragment_slide_in_left, 0)
                            .replace(R.id.fragment_container_fl_id, CompanyFragment.newInstance()).commit();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bundle bundle = new Bundle();
        Fragment fragment;
        if (requestCode == 1 && resultCode == RESULT_OK) {
            bundle.putString("VALUE", data.getExtras().getString("RESPONSE"));
            fragment = IndividualFragment.newInstance();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_fl_id, fragment).commitAllowingStateLoss();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            bundle.putString("VALUE", data.getExtras().getString("RESPONSE"));
            fragment = CompanyFragment.newInstance();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_fl_id, fragment).commitAllowingStateLoss();
        }
    }

}
