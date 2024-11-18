package org.gui;

import org.utils.ConfigHandler;
import org.utils.CsvHandler;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiWrapper {
    private ArrayList<String[]> csvResult;
    private int currentIndex;
    private ConfigHandler config;
    private boolean labelGUI;

    private int latIndex;
    private int lonIndex;
    private int catIndex;

    public GuiWrapper(ConfigHandler config, boolean labelGUI) {
        this.config = config;
        this.labelGUI = labelGUI;

        latIndex = -1;
        lonIndex = -1;
        catIndex = -1;
        if (labelGUI) {
            CsvHandler ch = new CsvHandler(config.getProperty("app.dataCSV"));
            csvResult = ch.readCsvToList();
        } else {
            CsvHandler ch = new CsvHandler(config.getProperty("app.initial_csv"));
            csvResult = ch.readCsvToList();
        }

        currentIndex = 1;
        if (labelGUI) {
            createNextLabelGUI();
        } else {
            createNextGUI();
        }
    }

    private void createNextGUI() {
        if (currentIndex < csvResult.size()) {
            if (latIndex == -1) {
                latIndex = Arrays.asList(csvResult.get(0)).indexOf("latitude");
                lonIndex = Arrays.asList(csvResult.get(0)).indexOf("longitude");
                catIndex = Arrays.asList(csvResult.get(0)).indexOf("category");
            }
            float longitude = Float.parseFloat(csvResult.get(currentIndex)[latIndex]);
            float latitude = Float.parseFloat(csvResult.get(currentIndex)[lonIndex]);
            int label = (int) Float.parseFloat(csvResult.get(currentIndex)[catIndex]);
            GUI gui = new GUI(longitude, latitude, String.valueOf(label));

            gui.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    currentIndex++;
                    createNextGUI();
                }
            });
        }
    }

    private void createNextLabelGUI() {
        if (currentIndex < csvResult.size()) {
            if (latIndex == -1) {
                latIndex = Arrays.asList(csvResult.get(0)).indexOf("latitude");
                lonIndex = Arrays.asList(csvResult.get(0)).indexOf("longitude");
            }
            float longitude = Float.parseFloat(csvResult.get(currentIndex)[latIndex]);
            float latitude = Float.parseFloat(csvResult.get(currentIndex)[lonIndex]);
            LabelGUI gui = new LabelGUI(config, longitude, latitude);

            gui.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    currentIndex++;
                    createNextLabelGUI();
                }
            });
        }
    }

}