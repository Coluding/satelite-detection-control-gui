package org.utils;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestHandler {
    public static void main(String[] args) throws IOException {
        Image image = getImageFromURL("https://sg.geodatenzentrum.de/wms_dop__28801866-9069-0953-9c70-c85a2cfa13d6?SERVICE=WMS&VERSION=1.1.0&REQUEST=GetMap&Layers=rgb&STYLES=&SRS=CRS:84&bbox=13.3750,%2052.5130,13.3800,%2052.52000&Width=512&Height=512&Format=image/jpeg");
        // Do something with the image...
    }

    public static Image getImageFromURL(String url) throws IOException{
        URL url_full = new URL((url));
        HttpURLConnection con = (HttpURLConnection) url_full.openConnection();
        con.setRequestMethod("GET");
        return (ImageIO.read(con.getInputStream()));
    }

    public static String fillURL(String url, ArrayList<Float> coordinates, String imageSize){

        String filledURL = String.format(url, coordinates.get(3), coordinates.get(0), coordinates.get(2),
                coordinates.get(1), imageSize, imageSize);

        return filledURL;
    }
}
