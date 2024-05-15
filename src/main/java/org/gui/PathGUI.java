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
    private JButton submitButton;
    private File[] selectedFiles = new File[3];
    private ConfigHandler config;

    public PathGUI(ConfigHandler config) {
        this.config = config;
        setTitle("Path GUI");
        setSize(500, 300); // Adjusted size to better fit the new component
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10)); // Adjusted for an additional row

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
