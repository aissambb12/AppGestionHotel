package com.hotel.util;

import java.util.Date;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern TELEPHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    private static final Pattern CIN_PATTERN =
            Pattern.compile("^[A-Za-z]{1,2}[0-9]{4,8}$");

    private ValidationUtil() {
    }

    public static boolean estVide(String texte) {
        return texte == null || texte.trim().isEmpty();
    }

    public static boolean estEmailValide(String email) {
        if (estVide(email)) {
            return false;
        }

        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean estTelephoneValide(String telephone) {
        if (estVide(telephone)) {
            return false;
        }

        return TELEPHONE_PATTERN.matcher(telephone).matches();
    }

    public static boolean estCinValide(String cin) {
        if (estVide(cin)) {
            return false;
        }

        return CIN_PATTERN.matcher(cin).matches();
    }

    public static boolean estNombrePositif(double nombre) {
        return nombre > 0;
    }

    public static boolean estEntierPositif(int nombre) {
        return nombre > 0;
    }

    public static boolean estPrixValide(double prix) {
        return prix > 0;
    }

    public static boolean sontDatesReservationValides(Date dateDebut, Date dateFin) {
        return DateUtil.estDateFinApresDateDebut(dateDebut, dateFin);
    }

    public static boolean estQuantiteValide(int quantite) {
        return quantite > 0;
    }

    public static boolean estLoginValide(String login) {
        return !estVide(login) && login.length() >= 3;
    }

    public static boolean estMotDePasseValide(String motDePasse) {
        return !estVide(motDePasse) && motDePasse.length() >= 4;
    }

    public static boolean estNomValide(String nom) {
        return !estVide(nom) && nom.length() >= 2;
    }
}