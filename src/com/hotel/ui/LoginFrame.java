package com.hotel.ui;

import com.hotel.model.Utilisateur;
import com.hotel.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtMotDePasse;
    private JButton btnConnexion;

    private AuthService authService;

    public LoginFrame() {
        UITheme.applyGlobalStyle();
        authService = new AuthService();

        setTitle("Connexion - Gestion Hôtel");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
        initActions();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UITheme.BACKGROUND);

        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(15, 15));
        card.setPreferredSize(new Dimension(500, 350));

        JLabel title = new JLabel("GESTION HÔTEL", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(UITheme.PRIMARY_DARK);

        JLabel subtitle = new JLabel("Veuillez vous connecter", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(100, 116, 139));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 0, 0));
        formPanel.setBackground(Color.WHITE);

        JLabel lblLogin = new JLabel("Login");
        JLabel lblPassword = new JLabel("Mot de passe");

        txtLogin = UITheme.createTextField();
        txtMotDePasse = UITheme.createPasswordField();

        formPanel.add(lblLogin);
        formPanel.add(txtLogin);
        formPanel.add(lblPassword);
        formPanel.add(txtMotDePasse);

        btnConnexion = UITheme.createPrimaryButton("Connexion");

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnConnexion, BorderLayout.CENTER);

        card.add(titlePanel, BorderLayout.NORTH);
        card.add(formPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(card);
        add(mainPanel);
    }

    private void initActions() {
        btnConnexion.addActionListener(e -> seConnecter());

        txtMotDePasse.addActionListener(e -> seConnecter());
    }

    private void seConnecter() {
        String login = txtLogin.getText().trim();
        String motDePasse = new String(txtMotDePasse.getPassword());

        Utilisateur utilisateur = authService.authentifier(login, motDePasse);

        if (utilisateur != null) {
            MainFrame mainFrame = new MainFrame(utilisateur);
            mainFrame.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Login ou mot de passe incorrect.",
                    "Erreur de connexion",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}