package com.hotel.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;


public class ImageUtil {

    private static final String RESOURCES_PATH = "src/resources/images/";
    private static final String ICONS_PATH = RESOURCES_PATH + "icons/";
    private static final String BG_PATH = RESOURCES_PATH + "backgrounds/";

    private ImageUtil() {}

    /**
     * Charge une icône redimensionnée
     * @param nomFichier Nom du fichier (ex: "logo_hotel.png")
     * @param largeur Largeur en pixels
     * @param hauteur Hauteur en pixels
     * @return ImageIcon redimensionnée ou fallback
     */
    public static ImageIcon chargerIcone(String nomFichier, int largeur, int hauteur) {
        try {
            String cheminComplet = ICONS_PATH + nomFichier;
            File fichier = new File(cheminComplet);

            if (fichier.exists()) {
                ImageIcon icone = new ImageIcon(cheminComplet);
                Image image = icone.getImage().getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            } else {
                System.err.println("⚠️ Image manquante : " + cheminComplet);
                return creerIconeParDefaut(largeur, hauteur, "?");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur : " + e.getMessage());
            return creerIconeParDefaut(largeur, hauteur, "!");
        }
    }

    /**
     * Charge une image de fond
     */
    public static ImageIcon chargerFond(String nomFichier) {
        try {
            String cheminComplet = BG_PATH + nomFichier;
            File fichier = new File(cheminComplet);

            if (fichier.exists()) {
                return new ImageIcon(cheminComplet);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Image de fond manquante");
        }
        return null;
    }

    /**
     * Crée une icône par défaut (fallback)
     */
    private static ImageIcon creerIconeParDefaut(int largeur, int hauteur, String texte) {
        BufferedImage img = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fond dégradé
        GradientPaint gradient = new GradientPaint(0, 0, new Color(200, 200, 200), largeur, hauteur, new Color(150, 150, 150));
        g2.setPaint(gradient);
        g2.fillRect(0, 0, largeur, hauteur);

        // Bordure
        g2.setColor(new Color(100, 100, 100));
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(1, 1, largeur - 2, hauteur - 2);

        // Texte
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, Math.max(12, largeur / 4)));
        FontMetrics fm = g2.getFontMetrics();
        int x = (largeur - fm.stringWidth(texte)) / 2;
        int y = ((hauteur - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(texte, x, y);

        g2.dispose();
        return new ImageIcon(img);
    }

    /**
     * Charge le logo principal de l'application
     * IMAGE À AJOUTER : logo_hotel.png (200x200px, fond transparent)
     * Description: Logo de l'hôtel avec style professionnel, fond transparent
     */
    public static ImageIcon chargerLogoApp() {
        return chargerIcone("logo_hotel.png", 200, 200);
    }

    /**
     * Charge l'icône personnel
     * IMAGE À AJOUTER : personnel.png (64x64px)
     * Description: Icône d'un groupe de personnes/utilisateurs
     */
    public static ImageIcon chargerIconePersonnel() {
        return chargerIcone("personnel.png", 64, 64);
    }

    /**
     * Charge l'icône chambres
     * IMAGE À AJOUTER : chambres.png (64x64px)
     * Description: Icône d'une clé d'hôtel ou d'une chambre
     */
    public static ImageIcon chargerIconeChambres() {
        return chargerIcone("chambres.png", 64, 64);
    }

    /**
     * Charge l'icône réservations
     * IMAGE À AJOUTER : reservations.png (64x64px)
     * Description: Icône d'un calendrier ou d'une réservation
     */
    public static ImageIcon chargerIconeReservations() {
        return chargerIcone("reservations.png", 64, 64);
    }

    /**
     * Charge l'icône statistiques
     * IMAGE À AJOUTER : statistiques.png (64x64px)
     * Description: Icône d'un graphique ou d'un rapport
     */
    public static ImageIcon chargerIconeStatistiques() {
        return chargerIcone("statistiques.png", 64, 64);
    }

    /**
     * Charge l'icône clients
     * IMAGE À AJOUTER : clients.png (64x64px)
     * Description: Icône d'une personne ou d'un profil utilisateur
     */
    public static ImageIcon chargerIconeClients() {
        return chargerIcone("clients.png", 64, 64);
    }

    /**
     * Charge l'icône check-in
     * IMAGE À AJOUTER : checkin.png (64x64px)
     * Description: Icône d'une clé donnée ou d'une porte ouverte
     */
    public static ImageIcon chargerIconeCheckIn() {
        return chargerIcone("checkin.png", 64, 64);
    }

    /**
     * Charge l'icône maintenance
     * IMAGE À AJOUTER : maintenance.png (64x64px)
     * Description: Icône d'un outil ou d'une clé à molette
     */
    public static ImageIcon chargerIconeMaintenance() {
        return chargerIcone("maintenance.png", 64, 64);
    }

    /**
     * Charge l'icône interventions
     * IMAGE À AJOUTER : interventions.png (64x64px)
     * Description: Icône d'une alerte ou d'une réparation urgente
     */
    public static ImageIcon chargerIconeInterventions() {
        return chargerIcone("interventions.png", 64, 64);
    }

    /**
     * Charge l'icône historique
     * IMAGE À AJOUTER : historique.png (64x64px)
     * Description: Icône d'une horloge ou d'un historique
     */
    public static ImageIcon chargerIconeHistorique() {
        return chargerIcone("historique.png", 64, 64);
    }

    /**
     * Charge l'icône déconnexion
     * IMAGE À AJOUTER : logout.png (48x48px)
     * Description: Icône d'une porte sortie ou d'une flèche de sortie
     */
    public static ImageIcon chargerIconeLogout() {
        return chargerIcone("logout.png", 48, 48);
    }

    /**
     * Charge l'icône retour
     * IMAGE À AJOUTER : back.png (48x48px)
     * Description: Icône d'une flèche gauche ou de retour
     */
    public static ImageIcon chargerIconeRetour() {
        return chargerIcone("back.png", 48, 48);
    }

    /**
     * Charge l'icône ajouter
     * IMAGE À AJOUTER : add.png (48x48px)
     * Description: Icône d'un plus (+) ou d'un crayon
     */
    public static ImageIcon chargerIconeAjouter() {
        return chargerIcone("add.png", 48, 48);
    }

    /**
     * Charge l'icône supprimer
     * IMAGE À AJOUTER : delete.png (48x48px)
     * Description: Icône d'une corbeille ou d'une croix rouge
     */
    public static ImageIcon chargerIconeSupprimer() {
        return chargerIcone("delete.png", 48, 48);
    }

    /**
     * Charge l'icône éditer
     * IMAGE À AJOUTER : edit.png (48x48px)
     * Description: Icône d'un crayon ou d'un stylo
     */
    public static ImageIcon chargerIconeEditer() {
        return chargerIcone("edit.png", 48, 48);
    }

    /**
     * Charge l'icône sauvegarder
     * IMAGE À AJOUTER : save.png (48x48px)
     * Description: Icône d'une disquette ou d'une sauvegarde
     */
    public static ImageIcon chargerIconeSauvegarder() {
        return chargerIcone("save.png", 48, 48);
    }

    /**
     * Charge l'icône annuler
     * IMAGE À AJOUTER : cancel.png (48x48px)
     * Description: Icône d'une croix rouge ou d'un refus
     */
    public static ImageIcon chargerIconeAnnuler() {
        return chargerIcone("cancel.png", 48, 48);
    }

    /**
     * Charge l'icône rafraîchir
     * IMAGE À AJOUTER : refresh.png (48x48px)
     * Description: Icône d'une flèche circulaire ou d'un rafraîchissement
     */
    public static ImageIcon chargerIconeRafraichir() {
        return chargerIcone("refresh.png", 48, 48);
    }
}