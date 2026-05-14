package com.hotel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    private static final String FORMAT_DATE = "yyyy-MM-dd";

    private DateUtil() {
    }

    public static Date stringVersDate(String dateTexte) {
        if (dateTexte == null || dateTexte.trim().isEmpty()) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
        sdf.setLenient(false);

        try {
            return sdf.parse(dateTexte);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateVersString(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
        return sdf.format(date);
    }

    public static boolean estDateValide(String dateTexte) {
        return stringVersDate(dateTexte) != null;
    }

    public static boolean estDateFinApresDateDebut(Date dateDebut, Date dateFin) {
        if (dateDebut == null || dateFin == null) {
            return false;
        }

        return dateFin.after(dateDebut);
    }

    public static int calculerNombreNuits(Date dateDebut, Date dateFin) {
        if (!estDateFinApresDateDebut(dateDebut, dateFin)) {
            return 0;
        }

        long differenceMillis = dateFin.getTime() - dateDebut.getTime();
        long nombreJours = TimeUnit.MILLISECONDS.toDays(differenceMillis);

        return (int) nombreJours;
    }

    public static Date dateAujourdhui() {
        return new Date();
    }
}