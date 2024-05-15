package org.gui;

import org.utils.ConfigHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PathGUI extends JFrame {
    private JButton[] chooseButtons = new JButton[3];
    private JLabel[] pathLabels = new JLabel[3];
    private JTextField labelsInputField; // TextField for inputting labels
    private JTextField apiUrlField; // TextField for API URL
    private JTextField imageWidthField; // TextField for Image Width
    private JTextField relativeImageWidthField; // TextField for Relative Image Width
    private JButton submitButton;
    private File[] selectedFiles = new File[3];
    private ConfigHandler config;

    public PathGUI(ConfigHandler config) {
        this.config = config;
        setTitle("Path GUI");
        setSize(500, 400); // Adjusted size to better fit the new components
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(8, 2, 10, 10)); // Adjusted for additional rows

        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        String[] buttons = {"Daten", "Speicherdatei"};
        for (int i = 0; i < buttons.length; i++) {
            int index = i;
            chooseButtons[i] = new JButton("Wähle " + buttons[i]);
            pathLabels[i] = new JLabel("No file selected");
            chooseButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileFilter(filter);
                    int choice = chooser.showOpenDialog(PathGUI.this);
                    if (choice == JFileChooser.APPROVE_OPTION) {
                        selectedFiles[index] = chooser.getSelectedFile();
                        pathLabels[index].setText(selectedFiles[index].getAbsolutePath());
                    }
                }
            });
            add(chooseButtons[i]);
            add(pathLabels[i]);
        }

        // Add label and text field for user to enter labels
        add(new JLabel("Labels (Komma-getrennt):"));
        labelsInputField = new JTextField();
        add(labelsInputField);

        // Add label and text field for API URL
        add(new JLabel("API URL:"));
        apiUrlField = new JTextField("https://sg.geodatenzentrum.de/wms_dop__28801866-9069-0953-9c70-c85a2cfa13d6?SERVICE=WMS&VERSION=1.1.0&REQUEST=GetMap&Layers=rgb&STYLES=&SRS=CRS:84&bbox=%f,%f,%f,%f&Width=%s&Height=%s&Format=image/jpeg\n"); // Default value
        add(apiUrlField);

        // Add label and text field for Image Width
        add(new JLabel("Bild Breite (Pixel):"));
        imageWidthField = new JTextField("512"); // Default value
        add(imageWidthField);

        // Add label and text field for Relative Image Width
        add(new JLabel("Relative Bild Breite:"));
        relativeImageWidthField = new JTextField("0.004"); // Default value
        add(relativeImageWidthField);

        submitButton = new JButton("Bestätigen");
        add(submitButton);
        add(new JLabel("")); // Placeholder for grid alignment

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProperties();
                processLabelsInput();
                dispose(); // Close the window
            }
        });

        setVisible(true);
    }

    private void updateProperties() {
        if (selectedFiles[0] != null) config.setProperty("app.dataCSV", selectedFiles[0].getAbsolutePath());
        if (selectedFiles[1] != null) config.setProperty("app.storeCSV", selectedFiles[1].getAbsolutePath());
        config.setProperty("app.formatURL", apiUrlField.getText());
        config.setProperty("app.imageWidth", imageWidthField.getText());
        config.setProperty("app.relativeWidth", relativeImageWidthField.getText());
    }

    public void addWindowCloseListener(WindowListener listener) {
        this.addWindowListener(listener);
    }

    private void processLabelsInput() {
        String labels = labelsInputField.getText();
        if (labels != null && !labels.isEmpty()) {
            config.setProperty("app.chooseLabels", labels);
        }
    }
}
