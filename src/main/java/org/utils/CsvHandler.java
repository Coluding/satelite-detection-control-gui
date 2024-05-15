package org.utils;
import java.io.*;
import java.util.ArrayList;


public class CsvHandler {

    private String path;

    public CsvHandler(String path) {
        this.path = path;
    }

    public ArrayList<String[]> readCsvToList() {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            ArrayList<String[]> lines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                lines.add(line.split(","));
            }
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeToCSV(String[] line, String newPath){
        try (PrintWriter pw = new PrintWriter(new FileWriter(newPath, true))) {
            pw.println(String.join(",", line));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Written in csv!");
    }
}
