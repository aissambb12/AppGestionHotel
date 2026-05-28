package com.hotel.util;

import javax.swing.*;
        import java.awt.*;
        import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class DatePickerUtil {

    private static final String[] MOIS_FR = {
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
    };

    private static final Color BLEU_NUIT  = new Color(44, 62, 80);
    private static final Color DORE_LUXE  = new Color(212, 175, 55);
    private static final Color GRIS_FOND  = new Color(245, 246, 250);
    private static final Color GRIS_LIGNE = new Color(220, 220, 220);

    /**
     * Crée un JPanel calendrier cliquable.
     * Le listener reçoit la date sélectionnée et a la responsabilité de fermer le dialog.
     */
    public static JPanel creerCalendrier(LocalDate dateSelectionnee, DateSelectionListener listener) {
        final YearMonth[] moisCourant = {
                dateSelectionnee != null ? YearMonth.from(dateSelectionnee) : YearMonth.now()
        };

        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(GRIS_LIGNE));

        // ============== HEADER (Mois / Année + nav) ==============
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BLEU_NUIT);
        header.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        // Boutons << et < à gauche
        JButton btnAnneePrev = boutonNav("<<");
        JButton btnMoisPrev  = boutonNav("<");
        JPanel gauche = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        gauche.setOpaque(false);
        gauche.add(btnAnneePrev);
        gauche.add(btnMoisPrev);

        // ComboBox au centre
        JComboBox<String> comboMois = new JComboBox<>(MOIS_FR);
        comboMois.setSelectedIndex(moisCourant[0].getMonthValue() - 1);
        comboMois.setFont(new Font("Segoe UI", Font.BOLD, 12));

        int anneeActuelle = LocalDate.now().getYear();
        Integer[] annees = new Integer[16]; // -5 → +10
        for (int i = 0; i < annees.length; i++) annees[i] = (anneeActuelle - 5) + i;
        JComboBox<Integer> comboAnnee = new JComboBox<>(annees);
        comboAnnee.setSelectedItem(moisCourant[0].getYear());
        comboAnnee.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JPanel centre = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        centre.setOpaque(false);
        centre.add(comboMois);
        centre.add(comboAnnee);

        // Boutons > et >> à droite
        JButton btnMoisNext  = boutonNav(">");
        JButton btnAnneeNext = boutonNav(">>");
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        droite.setOpaque(false);
        droite.add(btnMoisNext);
        droite.add(btnAnneeNext);

        header.add(gauche, BorderLayout.WEST);
        header.add(centre, BorderLayout.CENTER);
        header.add(droite, BorderLayout.EAST);

        // ============== ZONE JOURS ==============
        JPanel daysPanel = new JPanel(new GridLayout(0, 7, 4, 4));
        daysPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        daysPanel.setBackground(Color.WHITE);

        // Reconstruction complète à chaque changement
        Runnable rafraichir = () -> {
            daysPanel.removeAll();
            // En-tête jours de la semaine
            String[] jours = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
            for (String j : jours) {
                JLabel lbl = new JLabel(j);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lbl.setForeground(BLEU_NUIT);
                lbl.setHorizontalAlignment(JLabel.CENTER);
                daysPanel.add(lbl);
            }
            afficherJoursDuMois(daysPanel, moisCourant[0], dateSelectionnee, listener);
            daysPanel.revalidate();
            daysPanel.repaint();
        };
        rafraichir.run();

        // ============== ACTIONS NAVIGATION ==============
        // ComboBox mois
        comboMois.addActionListener(e -> {
            int idx = comboMois.getSelectedIndex();
            if (idx >= 0) {
                moisCourant[0] = YearMonth.of(moisCourant[0].getYear(), idx + 1);
                rafraichir.run();
            }
        });
        // ComboBox année
        comboAnnee.addActionListener(e -> {
            Integer an = (Integer) comboAnnee.getSelectedItem();
            if (an != null) {
                moisCourant[0] = YearMonth.of(an, moisCourant[0].getMonthValue());
                rafraichir.run();
            }
        });
        // Mois précédent
        btnMoisPrev.addActionListener(e -> {
            moisCourant[0] = moisCourant[0].minusMonths(1);
            synchroCombo(comboMois, comboAnnee, moisCourant[0]);
            rafraichir.run();
        });
        // Mois suivant
        btnMoisNext.addActionListener(e -> {
            moisCourant[0] = moisCourant[0].plusMonths(1);
            synchroCombo(comboMois, comboAnnee, moisCourant[0]);
            rafraichir.run();
        });
        // Année précédente
        btnAnneePrev.addActionListener(e -> {
            moisCourant[0] = moisCourant[0].minusYears(1);
            synchroCombo(comboMois, comboAnnee, moisCourant[0]);
            rafraichir.run();
        });
        // Année suivante
        btnAnneeNext.addActionListener(e -> {
            moisCourant[0] = moisCourant[0].plusYears(1);
            synchroCombo(comboMois, comboAnnee, moisCourant[0]);
            rafraichir.run();
        });

        // ============== FOOTER (bouton Aujourd'hui) ==============
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        footer.setBackground(GRIS_FOND);
        JButton btnToday = new JButton("Aujourd'hui");
        btnToday.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnToday.setBackground(DORE_LUXE);
        btnToday.setForeground(Color.WHITE);
        btnToday.setFocusPainted(false);
        btnToday.setOpaque(true);
        btnToday.setBorderPainted(false);
        btnToday.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToday.addActionListener(e -> listener.onDateSelected(LocalDate.now()));
        footer.add(btnToday);

        // ============== ASSEMBLAGE ==============
        panel.add(header, BorderLayout.NORTH);
        panel.add(daysPanel, BorderLayout.CENTER);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private static JButton boutonNav(String texte) {
        JButton b = new JButton(texte);
        b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        b.setBackground(DORE_LUXE);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setMargin(new Insets(2, 8, 2, 8));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static void synchroCombo(JComboBox<String> comboMois, JComboBox<Integer> comboAnnee, YearMonth ym) {
        // On désactive temporairement les listeners pour éviter une boucle infinie
        java.awt.event.ActionListener[] listenersM = comboMois.getActionListeners();
        java.awt.event.ActionListener[] listenersA = comboAnnee.getActionListeners();
        for (java.awt.event.ActionListener l : listenersM) comboMois.removeActionListener(l);
        for (java.awt.event.ActionListener l : listenersA) comboAnnee.removeActionListener(l);

        comboMois.setSelectedIndex(ym.getMonthValue() - 1);

        // Si l'année est hors plage de la combo, on l'ajoute en tête de liste
        boolean trouve = false;
        for (int i = 0; i < comboAnnee.getItemCount(); i++) {
            if (comboAnnee.getItemAt(i).equals(ym.getYear())) { trouve = true; break; }
        }
        if (!trouve) {
            comboAnnee.insertItemAt(ym.getYear(), 0);
        }
        comboAnnee.setSelectedItem(ym.getYear());

        for (java.awt.event.ActionListener l : listenersM) comboMois.addActionListener(l);
        for (java.awt.event.ActionListener l : listenersA) comboAnnee.addActionListener(l);
    }

    private static void afficherJoursDuMois(JPanel daysPanel, YearMonth mois,
                                            LocalDate dateSelectionnee,
                                            DateSelectionListener listener) {
        LocalDate debut = mois.atDay(1);
        int dayOfWeek = debut.getDayOfWeek().getValue(); // 1 = lundi … 7 = dimanche

        // Cases vides avant le premier jour du mois
        for (int i = 1; i < dayOfWeek; i++) {
            JLabel vide = new JLabel("");
            daysPanel.add(vide);
        }

        LocalDate aujourdhui = LocalDate.now();
        int nombreJours = mois.lengthOfMonth();
        for (int i = 1; i <= nombreJours; i++) {
            final LocalDate date = mois.atDay(i);
            JButton btnJour = new JButton(String.valueOf(i));
            btnJour.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnJour.setBackground(Color.WHITE);
            btnJour.setForeground(Color.BLACK);
            btnJour.setFocusPainted(false);
            btnJour.setBorder(BorderFactory.createLineBorder(GRIS_LIGNE));
            btnJour.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnJour.setOpaque(true);
            btnJour.setMargin(new Insets(4, 4, 4, 4));

            // Aujourd'hui : contour doré
            if (date.equals(aujourdhui)) {
                btnJour.setBorder(BorderFactory.createLineBorder(DORE_LUXE, 2));
                btnJour.setFont(new Font("Segoe UI", Font.BOLD, 12));
            }
            // Date sélectionnée : fond doré
            if (dateSelectionnee != null && date.equals(dateSelectionnee)) {
                btnJour.setBackground(DORE_LUXE);
                btnJour.setForeground(Color.WHITE);
                btnJour.setFont(new Font("Segoe UI", Font.BOLD, 12));
            }

            btnJour.addActionListener(e -> listener.onDateSelected(date));
            daysPanel.add(btnJour);
        }
    }

    public interface DateSelectionListener {
        void onDateSelected(LocalDate date);
    }
}
