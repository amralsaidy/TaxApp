package com.datepicker.components;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class JDF {

    /**
     * Main: The default constructor uses the current Gregorian date to
     * initialize the other private members of the class (Iranian and Julian
     * dates).
     */
    public JDF() {
        Calendar calendar = new GregorianCalendar();
        setGregorianDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public JDF(GregorianCalendar calendar) {
        setGregorianDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public JDF(Calendar calendar) {
        setGregorianDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Main: This constructor receives a Iranian date and initializes the
     * other private members of the class accordingly.
     *
     * @param year  int
     * @param month int
     * @param day   int
     * @return
     */
    public JDF(int year, int month, int day) {
        this();
        setGregorianDate(year, month, day);
    }


    /**
     * getGregorianCalendar: gets Iranian date and returns Gregorian calendar
     *
     * @return calendar
     */
    public GregorianCalendar getGregorianCalendar(int year, int month, int day)
            throws ParseException {
        setGregorianDate(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/M/d", Locale.US);
        Date myDate = dateFormat.parse(getGregorianDate());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(myDate);
        calendar.add(Calendar.MONTH, 1);
        return calendar;
    }

    public long getTimeInMillis(){
        try {
            return getGregorianCalendar(gYear, gMonth, gDay).getTimeInMillis();
        } catch (ParseException e) {
            return 0;
        }
    }


    /**
     * getGregorianYear: Returns the 'year' part of the Gregorian date.
     *
     * @return int
     */
    public int getGregorianYear() {
        return gYear;
    }

    /**
     * getGregorianMonth: Returns the 'month' part of the Gregorian date.
     *
     * @return int
     */
    public int getGregorianMonth() {
        return gMonth;
    }

    /**
     * getGregorianDay: Returns the 'day' part of the Gregorian date.
     *
     * @return int
     */
    public int getGregorianDay() {
        return gDay;
    }

    /**
     * getIranianDayName: Returns the number of Iranian day of week
     *
     * @return int
     */
    public int getGregorianDay(int year, int month, int day) throws ParseException {

        setGregorianDate(year, month, day);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/M/d", Locale.US);
        Date myDate = dateFormat.parse(getGregorianDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (Calendar.SATURDAY == dayOfWeek) {
            dayOfWeek = 0;
        } else if (Calendar.SUNDAY == dayOfWeek) {
            dayOfWeek = 1;
        } else if (Calendar.MONDAY == dayOfWeek) {
            dayOfWeek = 2;
        } else if (Calendar.TUESDAY == dayOfWeek) {
            dayOfWeek = 3;
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            dayOfWeek = 4;
        } else if (Calendar.THURSDAY == dayOfWeek) {
            dayOfWeek = 5;
        } else if (Calendar.FRIDAY == dayOfWeek) {
            dayOfWeek = 6;
        }

        return dayOfWeek;
    }

    /**
     * getGregorianDate: Returns a string version of Gregorian date
     *
     * @return String
     */
    public String getGregorianDate() {
        return (gYear + "/" + gMonth + "/" + gDay);
    }

    /**
     * getWeekDayStr: Returns the week day name.
     *
     * @return String
     */
    public String getWeekDayStr() {
        String weekDayStr[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        return (weekDayStr[getDayOfWeek()]);
    }

    /**
     * toString: Overrides the default toString() method to return all dates.
     *
     * @return String
     */
    @Override
    public String toString() {
        return (getWeekDayStr() + ", Gregorian:[" + getGregorianDate()
                + "]");
    }

    /**
     * getDayOfWeek: Returns the week day number. Monday=0..Sunday=6;
     *
     * @return int
     */
    public int getDayOfWeek() {
        return (JDN % 7);
    }

    /**
     * nextDay: Go to next julian day number (JDN) and adjusts the other dates.
     */
    public void nextDay() {
        JDN++;
        JDNToGregorian();
    }

    /**
     * nextDay: Overload the nextDay() method to accept the number of days to go
     * ahead and adjusts the other dates accordingly.
     *
     * @param days int
     */
    public void nextDay(int days) {
        JDN += days;
        JDNToGregorian();
    }

    /**
     * previousDay: Go to previous julian day number (JDN) and adjusts the otehr
     * dates.
     */
    public void previousDay() {
        JDN--;
        JDNToGregorian();
    }

    /**
     * previousDay: Overload the previousDay() method to accept the number of
     * days to go backward and adjusts the other dates accordingly.
     *
     * @param days int
     */
    public void previousDay(int days) {
        JDN -= days;
        JDNToGregorian();
    }


    /**
     * setGregorianDate: Sets the date according to the Gregorian calendar and
     * adjusts the other dates.
     *
     * @param year  int
     * @param month int
     * @param day   int
     */
    public void setGregorianDate(int year, int month, int day) {
        gYear = year;
        gMonth = month;
        gDay = day;
        JDN = gregorianDateToJDN(year, month, day);
        JDNToGregorian();
    }

    /**
     * gergorianDateToJDN: Calculates the julian day number (JDN) from Gregorian
     * calendar dates. This integer number corresponds to the noon of the date
     * (i.e. 12 hours of Universal Time). This method was tested to be good
     * (valid) since 1 March, -100100 (of both calendars) up to a few millions
     * (10^6) years into the future. The algorithm is based on D.A.Hatcher,
     * Q.Jl.R.Astron.Soc. 25(1984), 53-55 slightly modified by K.M. Borkowski,
     * Post.Astron. 25(1987), 275-279.
     *
     * @param year  int
     * @param month int
     * @param day   int
     * @return int
     */
    private int gregorianDateToJDN(int year, int month, int day) {
        int jdn = (year + (month - 8) / 6 + 100100) * 1461 / 4
                + (153 * ((month + 9) % 12) + 2) / 5 + day - 34840408;
        jdn = jdn - (year + 100100 + (month - 8) / 6) / 100 * 3 / 4 + 752;
        return (jdn);
    }

    public static boolean isLeapYear(int year) {
        double a = 0.025;
        int b = 266;
        double leapDays0;
        double leapDays1;
        if (year > 0) {
            leapDays0 = ((year + 38) % 2820) * 0.24219 + a;
            leapDays1 = ((year + 39) % 2820) * 0.24219 + a;
        } else if (year < 0) {
            leapDays0 = ((year + 39) % 2820) * 0.24219 + a;
            leapDays1 = ((year + 40) % 2820) * 0.24219 + a;
        } else
            return false;

        int frac0 = (int) ((leapDays0 - (int) (leapDays0)) * 1000);
        int frac1 = (int) ((leapDays1 - (int) (leapDays1)) * 1000);

        return frac0 <= b && frac1 > b;
    }

    /**
     * JDNToGregorian: Calculates Gregorian calendar dates from the julian day
     * number (JDN) for the period since JDN=-34839655 (i.e. the year -100100 of
     * both calendars) to some millions (10^6) years ahead of the present. The
     * algorithm is based on D.A. Hatcher, Q.Jl.R.Astron.Soc. 25(1984), 53-55
     * slightly modified by K.M. Borkowski, Post.Astron. 25(1987), 275-279).
     */
    private void JDNToGregorian() {
        int j = 4 * JDN + 139361631;
        j = j + (((((4 * JDN + 183187720) / 146097) * 3) / 4) * 4 - 3908);
        int i = ((j % 1461) / 4) * 5 + 308;
        gDay = (i % 153) / 5 + 1;
        gMonth = ((i / 153) % 12) + 1;
        gYear = j / 1461 - 100100 + (8 - gMonth) / 6;
    }

    private int gYear; // Year part of a Gregorian date
    private int gMonth; // Month part of a Gregorian date
    private int gDay; // Day part of a Gregorian date
    private int leap; // Number of years since the last leap year (0 to 4)
    private int JDN; // Julian Day Number
    private int march; // The march day of Farvardin the first (First day of
    // jaYear)
}
