package com.hotel.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    // Formats standards de la base de données et de l'interface
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMAT_DATE_HEURE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private DateUtil() {
    }

    /**
     * Convertit un texte (ex: "2026-06-02") en objet LocalDate.
     */
    public static LocalDate stringVersLocalDate(String dateTexte) {
        if (ValidationUtil.estVide(dateTexte)) return null;
        try {
            return LocalDate.parse(dateTexte.trim(), FORMAT_DATE);
        } catch (DateTimeParseException e) {
            return null; // Retourne null si le format n'est pas respecté
        }
    }

    /**
     * Convertit un objet LocalDate en texte pour l'afficher proprement dans un tableau Swing.
     */
    public static String localDateVersString(LocalDate date) {
        if (date == null) return "";
        return date.format(FORMAT_DATE);
    }

    public static String localDateTimeVersString(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(FORMAT_DATE_HEURE);
    }

    public static boolean estDateValide(String dateTexte) {
        return stringVersLocalDate(dateTexte) != null;
    }

    /**
     * Vérifie que le départ est bien APRÈS l'arrivée (logique stricte).
     */
    public static boolean estDateFinApresDateDebut(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) return false;
        return dateFin.isAfter(dateDebut);
    }

    /**
     * Calcule automatiquement le nombre de nuits entre deux dates.
     */
    public static int calculerNombreNuits(LocalDate dateArrivee, LocalDate dateDepart) {
        if (!estDateFinApresDateDebut(dateArrivee, dateDepart)) {
            return 0;
        }
        // Magie de l'API java.time : calcule la différence en jours
        return (int) ChronoUnit.DAYS.between(dateArrivee, dateDepart);
    }

    public static LocalDate dateAujourdhui() {
        return LocalDate.now();
    }

    public static LocalDateTime dateEtHeureMaintenant() {
        return LocalDateTime.now();
    }
}