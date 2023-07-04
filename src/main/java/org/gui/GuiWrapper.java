package org.gui;

import org.utils.CsvHandler;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class GuiWrapper {
    private ArrayList<String[]> csvResult;
    private int currentIndex;

    public GuiWrapper() {
        CsvHandler ch = new CsvHandler("/home/lubi/Documents/Projects/BISp/AI/sat_detect/src/inference/results/initial/test.csv");
        csvResult = ch.readCsvToList();
        currentIndex = 1;
        createNextGUI();
    }

    private void createNextGUI() {
        if (currentIndex < csvResult.size()) {
            float longitude = Float.parseFloat(csvResult.get(currentIndex)[0]);
            float latitude = Float.parseFloat(csvResult.get(currentIndex)[1]);
            String label = csvResult.get(currentIndex)[4];
            GUI gui = new GUI(longitude, latitude, label);

            gui.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    currentIndex++;
                    createNextGUI();
                }
            });
        }
    }
}