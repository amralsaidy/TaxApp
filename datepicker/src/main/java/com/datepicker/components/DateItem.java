package com.datepicker.components;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class DateItem {
    private int day;
    private int year;
    private int month;
    private JDF maxDate;
    private JDF minDate;
    private int currentYear;
    private boolean showYearFirst;
    private boolean closeYearAutomatically;

    public DateItem() {
        JDF jdf = new JDF();
        currentYear = jdf.getGregorianYear();
        setDate(jdf);
        setMaxDate(new JDF(currentYear + 20, 12, 31));
        setMinDate(new JDF(currentYear - 90, 1, 1));
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public boolean shouldShowYearFirst() {
        return showYearFirst;
    }

    public void setShowYearFirst(boolean showYearFirst) {
        this.showYearFirst = showYearFirst;
    }

    public boolean shouldCloseYearAutomatically() {
        return closeYearAutomatically;
    }

    public void setCloseYearAutomatically(boolean closeYearAutomatically) {
        this.closeYearAutomatically = closeYearAutomatically;
    }

    public void setDate(JDF jdf) {
        setDate(jdf.getGregorianDay(), jdf.getGregorianMonth(), jdf.getGregorianYear());
    }

    public void setDate(int day, int month, int year) {
        this.day = day;
        this.year = year;
        this.month = month;
    }

    public void setMaxDate(JDF maxDate) {
        this.maxDate = maxDate;
    }

    public JDF getMaxDate() {
        return maxDate;
    }

    public void setMinDate(JDF minDate) {
        this.minDate = minDate;
    }

    public JDF getMinDate() {
        return minDate;
    }

    public int getGregorianDay() {
        try {
            return new JDF().getGregorianDay(year, month, day);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Calendar getCalendar() {
        JDF jdf = new JDF(year, month, day);
        Calendar calendar = Calendar.getInstance();
        calendar.set(jdf.getGregorianYear(),jdf.getGregorianMonth() - 1, jdf.getGregorianDay());
        return calendar;
    }

    public String getDate() {
        return String.format(Locale.US, "%d-%d-%d", getYear(), getMonth(), getDay());
    }
}
