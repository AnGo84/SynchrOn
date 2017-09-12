package com.synchron.security;

import com.synchron.MainApp;
import com.synchron.custom.DateUtil;
import com.synchron.properties.PreferencesHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AnGo on 11.09.2017.
 */
public class Security {
    public static final String LICENSE_LINK = "www.google.com";

    public static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final Logger LOGGER = LogManager.getRootLogger();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

    private static final String PREFERENCE_NAME = "FirstRunDate";

    private static int TRIAL_DAYS_COUNT = 15;

    //private static final String DATE_TEMP_TRIAL = "01/09/2017";


    public static boolean isTrialPeriod() {

        boolean isTrial = false;
        Date now = getDateFromString(null, DATE_FORMAT.format(new Date()), DATE_FORMAT);

        //PreferencesHandler.setPreferenceString(DATE_TEMP_TRIAL, MainApp.class, PREFERENCE_NAME);

        String stringPreferenceDate = getStringDateFromPreference(PREFERENCE_NAME, now);

        Date firstRunDate = getDateFromString(now, stringPreferenceDate, DATE_FORMAT);
        LOGGER.info("Expiry date: '" + stringPreferenceDate + "'");

        if ((DateUtil.addToDate(firstRunDate, 86400 * TRIAL_DAYS_COUNT)).compareTo(now) > 0) {
            Security.LOGGER.info("Trial off: '" + (DateUtil.addToDate(firstRunDate, 86400 * TRIAL_DAYS_COUNT) + "'"));
            isTrial = true;
        }
        return isTrial;
    }

    private static Date getDateFromString(Date defaultDate, String stringDate, SimpleDateFormat simpleDateFormat) {
        Date firstRunDate = defaultDate;
        try {
            firstRunDate = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            Security.LOGGER.info("Wrong string date for parse: '" + stringDate + "'");
        } finally {
            return firstRunDate;
        }
    }

    private static String getStringDateFromPreference(String preferenceName, Date now) {
        if (PreferencesHandler.getPreferenceString(MainApp.class, preferenceName).equals("")) {
            PreferencesHandler.setPreferenceString(DATE_FORMAT.format(now), MainApp.class, preferenceName);
        }
        //String textDate = PreferencesHandler.getPreferenceString(this.getClass(), PREFERENCE_NAME);
        return PreferencesHandler.getPreferenceString(MainApp.class, preferenceName);
    }


//    public static boolean isEndTrial(Date date) {
//        try {
//
//            Date trialDate = DATE_FORMAT.parse(getStringDateFromPreference(PREFERENCE_NAME, new Date()));
//            System.out.println("2Check trial: " +trialDate);
//
//            //if (date.compareTo(trialDate) <= 0) {
//            if (date.compareTo(DateUtil.addToDate(trialDate, 86400 * TRIAL_DAYS_COUNT)) <= 0) {
//                Security.LOGGER.info("Trial off: '" + (DateUtil.addToDate(trialDate, 86400 * TRIAL_DAYS_COUNT) + "'"));
//                return false;
//            }
//        } catch (ParseException e) {
//            System.out.println("Wrong DATE");
//        }
//        return true;
//    }

    public static boolean isLicenseActive(String license) {
        if (license == null) {

        }
        return false;
    }
}

