package org.main;

import org.gui.GuiWrapper;
import org.gui.PathGUI;
import org.utils.ConfigHandler;

import java.io.IOException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) throws IOException {
        ConfigHandler config = new ConfigHandler();
        PathGUI pathGUI = new PathGUI(config);

        // Add a window close listener to print properties when the window is closed
        pathGUI.addWindowCloseListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // When the window is closed, print the properties
                System.out.println("Current app.initial_csv: " + config.getProperty("app.initial_csv"));
                System.out.println("Current app.approvedCSV: " + config.getProperty("app.approvedCSV"));
                System.out.println("Current app.deniedCSV: " + config.getProperty("app.deniedCSV"));
                GuiWrapper guiWrapper = new GuiWrapper(config, true);
            }
        });

        pathGUI.setVisible(true);
        // If GuiWrapper is intended to be used, initialize it as well
        // GuiWrapper guiWrapper = new GuiWrapper(config);
    }
}
