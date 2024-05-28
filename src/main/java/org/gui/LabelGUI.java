package org.gui;

import org.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LabelGUI extends JFrame {
    private AtomicReference<Float> latitudeRef;
    private AtomicReference<Float> longitudeRef;
    private float currentX;
    private float currentY;
    private float centerLongitude;
    private float centerLatitude;
    private ConfigHandler config;
    private BufferedImage originalImage;
    private int stepSize = 5;
    private JLabel coordinatesLabel;
    private JLabel zoomLevelLabel;
    private float relativeSize;

    private enum Direction {
        Xmin, Xplus, Ymin, Yplus;
    }

    public LabelGUI(ConfigHandler config, float longitude, float latitude) {
        centerLongitude = longitude;
        centerLatitude = latitude;

        longitudeRef = new AtomicReference<>(longitude);
        latitudeRef = new AtomicReference<>(latitude);

        this.config = config;
        relativeSize = Float.parseFloat(config.getProperty("app.relativeWidth"));
        String[] buttons = config.getProperty("app.chooseLabels").split(",");

        int size = Integer.parseInt(config.getProperty("app.imageWidth"));
        currentX = size / 2;
        currentY = size / 2;
        List<String> labelMapping = List.of(buttons);
        JLabel imgLabel = createImgLabel(longitudeRef.get(), latitudeRef.get(), size);

        // Create the main panel and set its layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Create the header label and add it to the main panel
        JLabel headerLabel = new JLabel("Label GUI");
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(headerLabel);

        // Create and configure the content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.add(imgLabel, new GridBagConstraints());
        mainPanel.add(contentPanel);

        // Add mouse listener to the image label
        imgLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentX = (float) e.getX();
                currentY = (float) e.getY();
                List<Float> geoCoordinates = CoordinatesHandler.convertToGeoCoordinates(currentX, currentY,
                        centerLongitude, centerLatitude, size, relativeSize);
                longitudeRef.set(geoCoordinates.get(0));
                latitudeRef.set(geoCoordinates.get(1));
                drawRedCircle(imgLabel, (int) currentX, (int) currentY, size);
                System.out.println("Longitude, Latitude: " + geoCoordinates.get(0) + "," + geoCoordinates.get(1));
                updateCoordinatesLabel();
            }
        });

        // Create and configure the control button panel
        JPanel controlButtonPanel = new JPanel();
        controlButtonPanel.setLayout(new BoxLayout(controlButtonPanel, BoxLayout.Y_AXIS));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton plusButtonX = new JButton("X+");
        JButton minusButtonX = new JButton("X-");
        JButton plusButtonY = new JButton("Y+");
        JButton minusButtonY = new JButton("Y-");

        plusButtonY.addActionListener(e -> moveCircle(imgLabel, size, Direction.Ymin));
        plusButtonX.addActionListener(e -> moveCircle(imgLabel, size, Direction.Xplus));
        minusButtonX.addActionListener(e -> moveCircle(imgLabel, size, Direction.Xmin));
        minusButtonY.addActionListener(e -> moveCircle(imgLabel, size, Direction.Yplus));

        buttonPanel.add(plusButtonX);
        buttonPanel.add(minusButtonX);
        buttonPanel.add(plusButtonY);
        buttonPanel.add(minusButtonY);

        // Add step size slider
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new FlowLayout());
        JSlider stepSizeSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, stepSize);
        stepSizeSlider.setMajorTickSpacing(5);
        stepSizeSlider.setMinorTickSpacing(1);
        stepSizeSlider.setPaintTicks(true);
        stepSizeSlider.setPaintLabels(true);
        stepSizeSlider.addChangeListener(e -> stepSize = stepSizeSlider.getValue());
        stepSizeSlider.setPreferredSize(new Dimension(stepSizeSlider.getPreferredSize().width * 2, stepSizeSlider.getPreferredSize().height));
        sliderPanel.add(new JLabel("Schrittgröße:"));
        sliderPanel.add(stepSizeSlider);

        controlButtonPanel.add(buttonPanel);
        controlButtonPanel.add(sliderPanel);

        // Create and configure the coordinates panel
        JPanel coordinatesPanel = new JPanel();
        coordinatesLabel = new JLabel();
        updateCoordinatesLabel();
        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(e -> {
            String coordinates = longitudeRef.get() + "," + latitudeRef.get();
            StringSelection stringSelection = new StringSelection(coordinates);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        });

        JButton googleMapsButton = new JButton("Open in Google Maps");
        googleMapsButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.google.com/maps/search/?api=1&query=" + longitudeRef.get() + "," + latitudeRef.get()));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        });

        coordinatesPanel.add(copyButton);
        coordinatesPanel.add(coordinatesLabel);
        coordinatesPanel.add(googleMapsButton);
        controlButtonPanel.add(coordinatesPanel);

        // Create and configure the zoom panel
        JPanel zoomPanel = new JPanel();
        zoomPanel.setLayout(new FlowLayout());
        JButton zoomInButton = new JButton("Zoom In");
        JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (relativeSize * 10000));
        zoomSlider.setMajorTickSpacing(10);
        zoomSlider.setMinorTickSpacing(1);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);
        zoomSlider.addChangeListener(e -> {
            relativeSize = zoomSlider.getValue() / 10000.0f;
            updateZoomLevelLabel();
        });

        zoomInButton.addActionListener(e -> {
            relativeSize = relativeSize + 0.0001f;
            zoomSlider.setValue((int) (relativeSize * 10000));
            updateZoomLevelLabel();
            updateImage(imgLabel, size);
        });

        zoomSlider.setPreferredSize(new Dimension(zoomSlider.getPreferredSize().width * 2, zoomSlider.getPreferredSize().height));
        zoomLevelLabel = new JLabel();
        updateZoomLevelLabel();

        zoomPanel.add(new JLabel("Zoom Level:"));
        zoomPanel.add(zoomLevelLabel);
        zoomPanel.add(zoomSlider);
        zoomPanel.add(zoomInButton);

        controlButtonPanel.add(zoomPanel);
        mainPanel.add(controlButtonPanel);

        // Create and configure the label button panel
        JPanel labelButtonPanel = new JPanel();
        for (String button : buttons) {
            JButton labelButton = new JButton(button);
            String label = button;
            labelButton.addActionListener(e -> {
                String[] values = {String.valueOf(longitudeRef.get()), String.valueOf(latitudeRef.get()), label};
                CsvHandler.writeToCSV(values, config.getProperty("app.storeCSV"));
                LabelGUI.super.dispose();
            });
            labelButtonPanel.add(labelButton, new GridBagConstraints());
        }

        mainPanel.add(labelButtonPanel);

        // Create and configure the exit panel
        JPanel exitPanel = new JPanel();
        JButton exitButton = new JButton("EXIT");
        exitButton.setBackground(Color.RED);
        exitButton.setSize(new Dimension(100, 50));
        exitButton.addActionListener(e -> System.exit(0));
        exitPanel.add(exitButton);

        mainPanel.add(exitPanel);

        // Wrap the main panel in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(mainPanel);

        this.add(scrollPane);
        this.setResizable(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);

        // Set the frame to full screen
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
    }

    private Image getCurrentImage(float longitude, float latitude) throws IOException {
        String url = config.getProperty("app.formatURL");
        String size = config.getProperty("app.imageWidth");
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
        originalImage = ImageUtils.toBufferedImage(image);
        Image modifiedImage;
        if (longitude == 0 && latitude == 0) {
            modifiedImage = image;
        } else {
            modifiedImage = ImageUtils.addCenterLabel(originalImage, size / 2, size / 2);
        }

        ImageIcon img = new ImageIcon(modifiedImage);
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(size, size));
        imgLabel.setMaximumSize(new Dimension(size, size));
        imgLabel.setMinimumSize(new Dimension(size, size));
        imgLabel.setIcon(img);

        return imgLabel;
    }

    private void updateImage(JLabel imgLabel, int size) {
        Image newImage;
        try {
            newImage = getCurrentImage(longitudeRef.get(), latitudeRef.get());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        originalImage = ImageUtils.toBufferedImage(newImage);
        Image modifiedImage = ImageUtils.addCenterLabel(originalImage, size / 2, size / 2);
        ImageIcon icon = new ImageIcon(modifiedImage);
        imgLabel.setIcon(icon);
        imgLabel.revalidate();
        imgLabel.repaint();
        centerLongitude = longitudeRef.get();
        centerLatitude = latitudeRef.get();
        currentY = size / 2;
        currentX = size / 2;
    }

    private void drawRedCircle(JLabel imgLabel, int x, int y, int size) {
        BufferedImage bufferedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.setColor(Color.RED);
        g2d.fillOval(x - 5, y - 5, 10, 10);  // Drawing a red circle with radius 5
        g2d.dispose();

        ImageIcon newIcon = new ImageIcon(bufferedImage);
        imgLabel.setIcon(newIcon);
        imgLabel.revalidate();
        imgLabel.repaint();
    }

    private void moveCircle(JLabel imgLabel, int size, Direction direction) {
        switch (direction) {
            case Xmin:
                currentX = currentX - stepSize;
                break;
            case Xplus:
                currentX = currentX + stepSize;
                break;
            case Ymin:
                currentY = currentY - stepSize;
                break;
            case Yplus:
                currentY = currentY + stepSize;
                break;
        }
        List<Float> geoCoordinates = CoordinatesHandler
                .convertToGeoCoordinates(currentX, currentY, centerLongitude,
                        centerLatitude, size, relativeSize);
        drawRedCircle(imgLabel, (int) currentX, (int) currentY, size);
        System.out.println("Longitude, Latitude: " + geoCoordinates.get(0) + "," + geoCoordinates.get(1));
        latitudeRef.set(geoCoordinates.get(1));
        longitudeRef.set(geoCoordinates.get(0));
        updateCoordinatesLabel();
    }

    private void updateCoordinatesLabel() {
        coordinatesLabel.setText("Longitude, Latitude: " + longitudeRef.get() + "," + latitudeRef.get());
    }

    private void updateZoomLevelLabel() {
        zoomLevelLabel.setText(String.format("%.4f", relativeSize));
    }
}
