package org.utils;

import java.util.ArrayList;
import java.util.List;

public class CoordinatesHandler {

    public static List<Float> formBbox(float longitude, float latitude, float coordinateWidth) {


        float leftOffset = coordinateWidth * 0.5f;
        float rightOffset = coordinateWidth * 0.5f;
        float upOffset = coordinateWidth * 0.5f;
        float downOffset = coordinateWidth * 0.5f;
        float x1 = longitude - leftOffset;
        float x2 = longitude + rightOffset;
        float y2 = latitude - downOffset;
        float y1 = latitude + upOffset;

        List<Float> result = new ArrayList<>();
        result.add(x1);
        result.add(x2);
        result.add(y1);
        result.add(y2);

        return result;
    }

    public static List<Float> convertToGeoCoordinates(float x, float y, float longitude, float latitude, int size, float relativeSize) {
        List<Float> bbox = formBbox(longitude, latitude, relativeSize);
        float relX = x / size;
        float relY = y / size;
        float newLongitude = bbox.get(1) - relY * relativeSize;
        float newLatitude = bbox.get(3) + relX * relativeSize;

        List<Float> newData =  new ArrayList<>();
        newData.add(newLongitude);
        newData.add(newLatitude);
        return newData;
    }
}
