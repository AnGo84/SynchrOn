package com.synchron.security;

import com.synchron.MainApp;
import com.synchron.custom.DateUtil;
import com.synchron.properties.PreferencesHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by AnGo on 11.09.2017.
 */
public class Security {
    private static final Logger LOGGER = LogManager.getRootLogger();
    public static final String DATE_PATTERN = "dd/MM/yyyy";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

    private static int TRIAL_DAYS_COUNT = 15;

    //private static final String DATE_TEMP_TRIAL = "01/09/2017";


    public static boolean checkTrialPeriod() {
        final String preferenceName = "FirstRunDate";
        boolean isTrial = false;
        Date now = getDateFromString(null, DATE_FORMAT.format(new Date()), DATE_FORMAT);

        //PreferencesHandler.setPreferenceString(DATE_TEMP_TRIAL, MainApp.class, preferenceName);

        String stringPreferenceDate = getStringDateFromPreference(preferenceName, now);

        Date firstRunDate = getDateFromString(now, stringPreferenceDate, DATE_FORMAT);
        LOGGER.info("Expiry date: '" + stringPreferenceDate + "'");

        if ((DateUtil.addToDate(firstRunDate, 86400 * TRIAL_DAYS_COUNT)).compareTo(now) > 0) {
            isTrial = true;
        }
        return isTrial;
    }

    private static Date getDateFromString(Date defaultDate, String stringDate, SimpleDateFormat  simpleDateFormat) {
        Date firstRunDate = defaultDate;
        try {
            firstRunDate = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            Security.LOGGER.info("Wrong string date for parse: '" + stringDate + "'");
        }
        finally {
            return firstRunDate;
        }
    }

    private static String getStringDateFromPreference(String preferenceName, Date now) {
        if (PreferencesHandler.getPreferenceString(MainApp.class, preferenceName).equals("")) {
            PreferencesHandler.setPreferenceString(DATE_FORMAT.format(now), MainApp.class, preferenceName);
        }
        //String textDate = PreferencesHandler.getPreferenceString(this.getClass(), preferenceName);
        return PreferencesHandler.getPreferenceString(MainApp.class, preferenceName);
    }


//    public static boolean isEndTrial(Date date){
//        try {
//            Date trialDate = DATE_FORMAT.parse(DATE_TEMP_TRIAL);
//            if (date.compareTo(trialDate)<=0){
//                return false;
//            }
//        } catch (ParseException e) {
//
//        }
//        return true;
//
//    }
}

