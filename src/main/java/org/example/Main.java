package org.example;

import org.gui.GuiWrapper;
import org.utils.ConfigHandler;
import org.gui.GUI;
import org.utils.CsvHandler;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        //GUI gui = new GUI(49.274270379289234f, 11.501957522361739f, "Fussball");
        //CsvHandler c = new CsvHandler("/home/lubi/Documents/Projects/BISp/AI/sat_detect/src/inference/results/initial/test.csv");
        //System.out.println(c.readCsvToList().get(1)[0]);
        GuiWrapper gui = new GuiWrapper();
    }
}