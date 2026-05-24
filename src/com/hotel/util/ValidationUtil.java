package com.hotel.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern TELEPHONE_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern CIN_PATTERN = Pattern.compile("^[A-Za-z]{1,2}[0-9]{4,8}$");

    private ValidationUtil() {}

    public static boolean estVide(String texte) {
        return texte == null || texte.trim().isEmpty();
    }

    public static boolean estEmailValide(String email) {
        if (estVide(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean estTelephoneValide(String telephone) {
        if (estVide(telephone)) return false;
        String nettoyee = telephone.replaceAll("[^0-9]", "");
        return TELEPHONE_PATTERN.matcher(nettoyee).matches();
    }

    public static boolean estCinValide(String cin) {
        if (estVide(cin)) return false;
        return CIN_PATTERN.matcher(cin.trim().toUpperCase()).matches();
    }

    public static boolean estPrixValide(double prix) {
        return prix > 0;
    }

    public static boolean estQuantiteValide(int quantite) {
        return quantite > 0;
    }

    public static boolean estMotDePasseValide(String motDePasse) {
        return !estVide(motDePasse) && motDePasse.length() >= 4;
    }

    public static boolean estNomValide(String nom) {
        return !estVide(nom) && nom.trim().length() >= 2 && nom.trim().length() <= 100;
    }

    public static boolean sontDatesReservationValides(LocalDate dateArrivee, LocalDate dateDepart) {
        return DateUtil.estDateFinApresDateDebut(dateArrivee, dateDepart);
    }

    public static boolean estDateDansLeFuturOuAujourdhui(LocalDate date) {
        if (date == null) return false;
        return !date.isBefore(LocalDate.now());
    }

    public static String obtenirMessageErreur(String champ, String raison) {
        return "❌ " + champ + " : " + raison;
    }
}