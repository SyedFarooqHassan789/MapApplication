package fi.abloy.mapapplication.async;


import android.os.AsyncTask;

import java.util.ArrayList;

import fi.abloy.mapapplication.common.Extra;
import fi.abloy.mapapplication.data.Location;
import fi.abloy.mapapplication.data.Point;
import fi.abloy.mapapplication.interfaces.IResponseHelper;


public class AsyncGetCoordinatesData extends AsyncTask<Object, Void, Point> {
    private IResponseHelper iResponseHelper;
    private ArrayList<Point> pointArrayList;
    private Point point;

    public AsyncGetCoordinatesData(IResponseHelper iResponseHelper) {
        this.iResponseHelper = iResponseHelper;
        pointArrayList = new ArrayList<>();
    }

    @Override
    protected Point doInBackground(Object... params) {
        Location getLocation = (Location) params[0];

        if (getLocation.getGeojson().getType().equals("Polygon")) {
            ArrayList<ArrayList<ArrayList<Double>>> coordinates = (ArrayList<ArrayList<ArrayList<Double>>>) getLocation.getGeojson().getCoordinates();
            for (int i = 0; i < coordinates.size(); i++)
                for (int j = 0; j < coordinates.get(i).size(); j++)
                    for (int k = 0; k < coordinates.get(i).get(j).size() - 1; k++) {
                        point = new Point();
                        point.setLongitude(coordinates.get(i).get(j).get(k));
                        point.setLatitude(coordinates.get(i).get(j).get(k + 1));
                        pointArrayList.add(point);

                    }
            point = centerPolygon(pointArrayList);

        } else if (getLocation.getGeojson().getType().equals("Point")) {
            ArrayList<Double> coordinates = (ArrayList<Double>) getLocation.getGeojson().getCoordinates();
            for (int i = 0; i < coordinates.size() - 1; i++) {
                point = new Point();
                point.setLongitude(coordinates.get(i));
                point.setLatitude(coordinates.get(i + 1));
            }

        } else if (getLocation.getGeojson().getType().equals("LineString")) {
            ArrayList<ArrayList<Double>> coordinates = (ArrayList<ArrayList<Double>>) getLocation.getGeojson().getCoordinates();

            for (int i = 0; i < coordinates.size(); i++)
                for (int j = 0; j < coordinates.get(i).size() - 1; j++) {
                    point = new Point();
                    point.setLongitude(coordinates.get(i).get(j));
                    point.setLatitude(coordinates.get(i).get(j + 1));
                    pointArrayList.add(point);
                }
            point = centerLinestring(pointArrayList);
        }
        return point;
    }

    private Point centerPolygon(ArrayList<Point> points) {
        int i = 0;
        int j = points.size() - 1;
        Double totalArea = 0.0;
        Double firstTerm = 0.0, secondTerm = 0.0;
        Double area;
        pointArrayList = new ArrayList<>();
        while (i < points.size()) {
            Point point1 = points.get(i);
            Point point2 = points.get(j);

            area = point1.getLongitude() * point2.getLatitude() - point2.getLongitude() * point1.getLatitude();
            totalArea += area;
            firstTerm += (point1.getLongitude() + point2.getLongitude()) * area;
            secondTerm += (point1.getLatitude() + point2.getLatitude()) * area;
            j = i++;
        }
        totalArea = totalArea * Extra.AREA_CONSTANT;
        Point point = new Point();
        Double longitude = firstTerm / totalArea;
        Double latitude = secondTerm / totalArea;
        point.setLongitude(longitude);
        point.setLatitude(latitude);


        return point;

    }

    private Point centerLinestring(ArrayList<Point> points) {

        Double longitude = 0.0, latitude = 0.0;
        for (int i = 0; i < points.size(); i++) {
            latitude += points.get(i).getLatitude();
            longitude += points.get(i).getLongitude();
        }

        int totalPoints = points.size();
        longitude = longitude / totalPoints;
        latitude = latitude / totalPoints;

        Point point = new Point();
        point.setLatitude(latitude);
        point.setLongitude(longitude);


        return point;
    }

    @Override
    protected void onPostExecute(Point result) {
        iResponseHelper.getData(result);
    }

    @Override
    protected void onPreExecute() {
    }
}
