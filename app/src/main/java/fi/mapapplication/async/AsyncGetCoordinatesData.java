package fi.mapapplication.async;


import android.os.AsyncTask;

import java.util.ArrayList;

import fi.mapapplication.common.Extra;
import fi.mapapplication.data.Location;
import fi.mapapplication.data.Point;
import fi.mapapplication.interfaces.IResponseHelper;


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
        Location mLocation = (Location) params[0];

        if (mLocation.getGeojson().getType().equals("Polygon")) {
            ArrayList<ArrayList<ArrayList<Double>>> coordinates = (ArrayList<ArrayList<ArrayList<Double>>>) mLocation.getGeojson().getCoordinates();
            for (int i = 0; i < coordinates.size(); i++)
                for (int j = 0; j < coordinates.get(i).size(); j++)
                    for (int k = 0; k < coordinates.get(i).get(j).size() - 1; k++) {
                        point = new Point();
                        point.setLongitude(coordinates.get(i).get(j).get(k));
                        point.setLatitude(coordinates.get(i).get(j).get(k + 1));
                        pointArrayList.add(point);

                    }
            point = centerPolygon(pointArrayList);

        } else if (mLocation.getGeojson().getType().equals("Point")) {
            ArrayList<Double> coordinates = (ArrayList<Double>) mLocation.getGeojson().getCoordinates();
            for (int i = 0; i < coordinates.size() - 1; i++) {
                point = new Point();
                point.setLongitude(coordinates.get(i));
                point.setLatitude(coordinates.get(i + 1));
            }

        } else if (mLocation.getGeojson().getType().equals("LineString")) {
            ArrayList<ArrayList<Double>> coordinates = (ArrayList<ArrayList<Double>>) mLocation.getGeojson().getCoordinates();

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

    private Point centerPolygon(ArrayList<Point> pointArrayList) {
        int i = 0;
        int j = pointArrayList.size() - 1;
        Double totalArea = 0.0;
        Double firstTerm = 0.0, secondTerm = 0.0;
        Double area;
        while (i < pointArrayList.size()) {
            Point point1 = pointArrayList.get(i);
            Point point2 = pointArrayList.get(j);

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

    private Point centerLinestring(ArrayList<Point> pointArrayList) {

        Double longitude = 0.0, latitude = 0.0;
        for (int i = 0; i < pointArrayList.size(); i++) {
            latitude += pointArrayList.get(i).getLatitude();
            longitude += pointArrayList.get(i).getLongitude();
        }

        int totalPoints = pointArrayList.size();
        longitude = longitude / totalPoints;
        latitude = latitude / totalPoints;

        Point point = new Point();
        point.setLatitude(latitude);
        point.setLongitude(longitude);


        return point;
    }

    @Override
    protected void onPostExecute(Point point) {
        iResponseHelper.getData(point);
    }

    @Override
    protected void onPreExecute() {
    }
}
