package com.asb.taxapp;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static double betweenTwoDates(Date startDate, Date endDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        double result = 0.0;
        int x = 0, y = 0, z = 0, a = 0, b = 0;
        double c = 0.0, d = 0.0;
        x = startCalendar.get(Calendar.MONTH) + 1;
        y = endCalendar.get(Calendar.MONTH) + 1;
        z = y - x + 1;
        a = startCalendar.get(Calendar.DAY_OF_MONTH);
        b = endCalendar.get(Calendar.DAY_OF_MONTH);
        int[] group30 = {4, 6, 9, 11};
        int[] group31 = {1, 3, 5, 7, 8, 10, 12};

        if (a == 1) {
            a = 0;
        }

        if (isContain(group30, x)) {
            if (a < 30) {
                z = z - 1;
                c = (double) (30 - a) / (double) 30;
            }
        } else if (isContain(group31, x)) {
            if (a < 31) {
                z = z - 1;
                c = (double) (31 - a) / (double) 31;
            }
        } else if (x == 2) {
            if (a < 28) {
                z = z - 1;
                c = (double) (28 - a) / (double) 28;
            }
        }

        if (isContain(group30, y)) {
            if (b < 30) {
                z = z - 1;
                d = (double) b / (double) 30;
            }
        } else if (isContain(group31, y)) {
            if (b < 31) {
                z = z - 1;
                d = (double) b / (double) 31;
            }
        } else if (y == 2) {
            if (b < 28) {
                z = z - 1;
                d = (double) b / (double) 28;
            }
        }

        result = Double.valueOf(z + c + d);
        return result;
    }

    public static boolean isContain(int[] arr, int item) {
        for (int n : arr) {
            if (item == n) {
                return true;
            }
        }
        return false;
    }

    public static double monthsBetween(Date d1, Date d2) {
        double AVERAGE_MILLIS_PER_MONTH = 365.24 * 24 * 60 * 60 * 1000 / 12;
        return (d2.getTime() - d1.getTime()) / AVERAGE_MILLIS_PER_MONTH;
    }

    /**
     * This method is used to hide keyboard
     *
     * @param activity
     */
    public static void hideKeyboardFrom(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}
