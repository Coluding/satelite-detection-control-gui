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

public class LabelGUI extends JFrame {
    private AtomicReference<Float> latitudeRef;
    private AtomicReference<Float> longitudeRef;

    private ConfigHandler config = new ConfigHandler();

    public LabelGUI(ConfigHandler config, float longitude, float latitude) {
        longitudeRef = new AtomicReference<>(longitude);
        latitudeRef = new AtomicReference<>(latitude);


        String[] buttons = config.getProperty("app.chooseLabels").split(",");

        int size = Integer.parseInt(config.getProperty("app.imageWidth"));
        List<String> labelMapping = List.of(buttons);
        JLabel imgLabel = createImgLabel(longitudeRef.get(), latitudeRef.get(), size);
        JLabel mainPanel = new JLabel();
        JLabel headerLabel = new JLabel();
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
        for (int i = 0; i < buttons.length; i++) {
            JButton labelButton = new JButton(buttons[i]);
            String label = buttons[i];
            labelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] values = {String.valueOf(longitudeRef.get()), String.valueOf(latitudeRef.get()),
                            label};
                    CsvHandler.writeToCSV(values, config.getProperty("app.storeCSV"));
                    LabelGUI.super.dispose();
                }
            });
            buttonPanel.add(labelButton, new GridBagConstraints());
        }



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


