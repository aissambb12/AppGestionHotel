package com.hotel.ui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UITheme {

    public static final Color PRIMARY = new Color(30, 64, 175);
    public static final Color PRIMARY_DARK = new Color(30, 58, 138);
    public static final Color BACKGROUND = new Color(245, 247, 250);
    public static final Color SIDEBAR = new Color(17, 24, 39);
    public static final Color SIDEBAR_BUTTON = new Color(31, 41, 55);
    public static final Color TEXT_LIGHT = Color.WHITE;
    public static final Color TEXT_DARK = new Color(31, 41, 55);

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    private UITheme() {
    }

    public static void applyGlobalStyle() {
        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("Label.font", NORMAL_FONT);
        UIManager.put("TextField.font", NORMAL_FONT);
        UIManager.put("PasswordField.font", NORMAL_FONT);
        UIManager.put("ComboBox.font", NORMAL_FONT);
        UIManager.put("Table.font", NORMAL_FONT);
        UIManager.put("Table.rowHeight", 28);
    }

    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        return button;
    }

    public static JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SIDEBAR_BUTTON);
        button.setForeground(TEXT_LIGHT);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        return button;
    }

    public static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(NORMAL_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(NORMAL_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));
        return field;
    }

    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(NORMAL_FONT);
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT_DARK);
        table.setGridColor(new Color(229, 231, 235));

        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_DARK);
        header.setForeground(Color.WHITE);
        header.setFont(BUTTON_FONT);
    }
}