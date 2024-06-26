package org.gui;

import org.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GUI extends JFrame {
    private AtomicReference<Float> latitudeRef;
    private AtomicReference<Float> longitudeRef;
    private String label;
    ConfigHandler config = new ConfigHandler();

    public GUI(float longitude, float latitude, String label) {
        longitudeRef = new AtomicReference<>(longitude);
        latitudeRef = new AtomicReference<>(latitude);
        this.label = label;

        int size = Integer.parseInt(config.getProperty("app.imageWidth"));
        List<String> labelMapping = config.getListProp("app.labels");
        JLabel imgLabel = createImgLabel(longitudeRef.get(), latitudeRef.get(), size);
        JLabel mainPanel = new JLabel();
        JLabel headerLabel = new JLabel();
        headerLabel.setText(labelMapping.get(Integer.parseInt(label)));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new GridBagLayout());

        contentPanel.add(imgLabel, new GridBagConstraints());
        JPanel controlButtonPanel = new JPanel();
        JButton plusButtonX = new JButton("X+");
        JButton minusButtonX = new JButton("X-");
        JButton plusButtonY = new JButton("Y+");
        JButton minusButtonY = new JButton("Y-");
        plusButtonY.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                longitudeRef.set(longitudeRef.get() + 0.00005f);
                Image newImage;
                try {
                    newImage = getCurrentImage(longitudeRef.get(), latitudeRef.get());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                Image modifiedImage = ImageUtils.addCenterLabel((BufferedImage) newImage,
                        size / 2, size / 2);
                ImageIcon icon = new ImageIcon(modifiedImage);
                imgLabel.setIcon(icon);
                imgLabel.revalidate();
                imgLabel.repaint();
            }
        });

        plusButtonX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                latitudeRef.set(latitudeRef.get() + 0.00005f);
                Image newImage;
                try {
                    newImage = getCurrentImage(longitudeRef.get(), latitudeRef.get());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                Image modifiedImage = ImageUtils.addCenterLabel((BufferedImage) newImage,
                        size / 2, size / 2);
                ImageIcon icon = new ImageIcon(modifiedImage);
                imgLabel.setIcon(icon);
                imgLabel.revalidate();
                imgLabel.repaint();
            }
        });

        minusButtonX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                latitudeRef.set(latitudeRef.get() - 0.00005f);
                Image newImage;
                try {
                    newImage = getCurrentImage(longitudeRef.get(), latitudeRef.get());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                Image modifiedImage = ImageUtils.addCenterLabel((BufferedImage) newImage,
                        size / 2, size / 2);
                ImageIcon icon = new ImageIcon(modifiedImage);
                imgLabel.setIcon(icon);
                imgLabel.revalidate();
                imgLabel.repaint();
            }
        });

        minusButtonY.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                longitudeRef.set(longitudeRef.get() - 0.00005f);
                Image newImage;
                try {
                    newImage = getCurrentImage(longitudeRef.get(), latitudeRef.get());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                Image modifiedImage = ImageUtils.addCenterLabel((BufferedImage) newImage,
                        size / 2, size / 2);
                ImageIcon icon = new ImageIcon(modifiedImage);
                imgLabel.setIcon(icon);
                imgLabel.revalidate();
                imgLabel.repaint();
            }
        });


        controlButtonPanel.add(plusButtonX);
        controlButtonPanel.add(minusButtonX);
        controlButtonPanel.add(plusButtonY);
        controlButtonPanel.add(minusButtonY);


        JPanel buttonPanel = new JPanel();
        JButton denyButton = new JButton();
        denyButton.setSize(new Dimension(50, 50));
        denyButton.setText("Prediction ablehnen");
        denyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] values = {String.valueOf(longitudeRef.get()), String.valueOf(latitudeRef.get()),
                        labelMapping.get(Integer.valueOf(label))};
                CsvHandler.writeToCSV(values, config.getProperty("app.deniedCSV"));
                GUI.super.dispose();
            }
        });

        JButton evalButton = new JButton();
        evalButton.setSize(new Dimension(50, 50));
        evalButton.setText("Separat evaluieren");
        evalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] values = {String.valueOf(longitudeRef.get()), String.valueOf(latitudeRef.get()),
                        labelMapping.get(Integer.parseInt(label))};
                CsvHandler.writeToCSV(values, config.getProperty("app.evalCSV"));
                GUI.super.dispose();
            }
        });

        JButton acceptButton = new JButton();
        acceptButton.setSize(new Dimension(50, 50));
        acceptButton.setText("Prediction annehmen");
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] values = {String.valueOf(longitudeRef.get()), String.valueOf(latitudeRef.get()),
                        labelMapping.get(Integer.parseInt(label))};
                CsvHandler.writeToCSV(values, config.getProperty("app.approvedCSV"));
                GUI.super.dispose();
            }
        });
        buttonPanel.add(denyButton, new GridBagConstraints());
        buttonPanel.add(evalButton, new GridBagConstraints());
        buttonPanel.add(acceptButton, new GridBagConstraints());

        JPanel exitPanel = new JPanel();
        JButton exitButton = new JButton("EXIT");
        exitButton.setBackground(Color.RED);
        exitButton.setSize(new Dimension(100,50));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exitPanel.add(exitButton);

        mainPanel.add(headerLabel);
        mainPanel.add(controlButtonPanel);
        mainPanel.add(contentPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(exitPanel);
        this.setSize(new Dimension(700, 700));
        this.setTitle("Prediction Evaluation");


        this.add(mainPanel);
        this.setResizable(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
    }

    private Image getCurrentImage(float longitude, float latitude) throws IOException {
        String url = config.getProperty("app.formatURL");
        String size = config.getProperty("app.imageWidth");
        float relativeSize = Float.parseFloat(config.getProperty("app.relativeWidth"));
        ArrayList<Float> bbox = (ArrayList<Float>) CoordinatesHandler.formBbox(longitude, latitude, relativeSize);
        String filledUrl = RequestHandler.fillURL(url, bbox, size);
        return RequestHandler.getImageFromURL(filledUrl);
    }


    private JLabel createImgLabel(float longitude, float latitude, int size) {
        Image image;
        try {
            image = getCurrentImage(longitude, latitude);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image modifiedImage;
        if (longitude == 0 && latitude == 0) {
            modifiedImage = image;
        } else {
            modifiedImage = ImageUtils.addCenterLabel((BufferedImage) image, size / 2, size / 2);
        }

        ImageIcon img = new ImageIcon(modifiedImage);
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(size, size));
        imgLabel.setMaximumSize(new Dimension(size, size));
        imgLabel.setMinimumSize(new Dimension(size, size));
        imgLabel.setIcon(img);

        return imgLabel;
    }
}


