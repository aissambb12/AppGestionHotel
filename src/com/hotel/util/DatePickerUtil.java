package com.hotel.util;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

public class DatePickerUtil {

    /**
     * Crée un JPanel avec un calendrier cliquable
     */
    public static JPanel creerCalendrier(LocalDate dateSelectionnee, DateSelectionListener listener) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        YearMonth mois = dateSelectionnee != null ? YearMonth.from(dateSelectionnee) : YearMonth.now();

        // Panel header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(244, 244, 244));

        JButton btnPrev = new JButton("◀");
        JButton btnNext = new JButton("▶");
        JLabel lblMois = new JLabel(mois.getMonth() + " " + mois.getYear());
        lblMois.setHorizontalAlignment(JLabel.CENTER);

        headerPanel.add(btnPrev, BorderLayout.WEST);
        headerPanel.add(lblMois, BorderLayout.CENTER);
        headerPanel.add(btnNext, BorderLayout.EAST);

        // Panel jours
        JPanel daysPanel = new JPanel(new GridLayout(7, 7, 5, 5));
        daysPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        daysPanel.setBackground(Color.WHITE);

        String[] joursSemaine = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        for (String jour : joursSemaine) {
            JLabel lblJour = new JLabel(jour);
            lblJour.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblJour.setHorizontalAlignment(JLabel.CENTER);
            daysPanel.add(lblJour);
        }

        afficherJoursDuMois(daysPanel, mois, dateSelectionnee, listener);

        btnPrev.addActionListener(e -> {
            YearMonth precedent = mois.minusMonths(1);
            daysPanel.removeAll();
            for (String jour : joursSemaine) {
                JLabel lblJour = new JLabel(jour);
                lblJour.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lblJour.setHorizontalAlignment(JLabel.CENTER);
                daysPanel.add(lblJour);
            }
            afficherJoursDuMois(daysPanel, precedent, dateSelectionnee, listener);
            lblMois.setText(precedent.getMonth() + " " + precedent.getYear());
            daysPanel.revalidate();
            daysPanel.repaint();
        });

        btnNext.addActionListener(e -> {
            YearMonth suivant = mois.plusMonths(1);
            daysPanel.removeAll();
            for (String jour : joursSemaine) {
                JLabel lblJour = new JLabel(jour);
                lblJour.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lblJour.setHorizontalAlignment(JLabel.CENTER);
                daysPanel.add(lblJour);
            }
            afficherJoursDuMois(daysPanel, suivant, dateSelectionnee, listener);
            lblMois.setText(suivant.getMonth() + " " + suivant.getYear());
            daysPanel.revalidate();
            daysPanel.repaint();
        });

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(daysPanel, BorderLayout.CENTER);

        return panel;
    }

    private static void afficherJoursDuMois(JPanel daysPanel, YearMonth mois, LocalDate dateSelectionnee, DateSelectionListener listener) {
        LocalDate debut = mois.atDay(1);
        int jour = debut.getDayOfWeek().getValue();

        for (int i = 1; i < jour; i++) {
            daysPanel.add(new JLabel());
        }

        int nombreJours = mois.lengthOfMonth();
        for (int i = 1; i <= nombreJours; i++) {
            LocalDate date = mois.atDay(i);
            JButton btnJour = new JButton(String.valueOf(i));
            btnJour.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnJour.setBackground(Color.WHITE);
            btnJour.setForeground(Color.BLACK);
            btnJour.setFocusPainted(false);

            if (dateSelectionnee != null && date.equals(dateSelectionnee)) {
                btnJour.setBackground(new Color(212, 175, 55));
                btnJour.setForeground(Color.WHITE);
            }

            final LocalDate dateFinale = date;
            btnJour.addActionListener(e -> listener.onDateSelected(dateFinale));

            daysPanel.add(btnJour);
        }
    }

    public interface DateSelectionListener {
        void onDateSelected(LocalDate date);
    }
}