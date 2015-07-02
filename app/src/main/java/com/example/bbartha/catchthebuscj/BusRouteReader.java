package com.example.bbartha.catchthebuscj;

import com.example.bbartha.catchthebuscj.util.LatLngUtils;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Bartha on 11/11/2014.
 */
public class BusRouteReader {

    public List<BusRouteCoordinates> read(final InputStream inputStream) throws ParserConfigurationException, IOException,
            SAXException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(inputStream);
        doc.getDocumentElement().normalize();
        return getLineCoordinatesList(doc);
    }

    private List<BusRouteCoordinates> getLineCoordinatesList(Document doc) {
        NodeList placemarkNodeList = doc.getElementsByTagName("Placemark");

        List<LatLng> lineList = new ArrayList<>();
        List<LatLng> placemarkList = new ArrayList<>();
        List<String> placeMarkNames = new ArrayList<>();

        for (int s = 0; s < placemarkNodeList.getLength(); s++) {

            Node placemarkNode = placemarkNodeList.item(s);

            if (placemarkNode.getNodeType() == Node.ELEMENT_NODE) {

                Element placemarkElement = (Element) placemarkNode;
                NodeList lineStringNodeList = placemarkElement.getElementsByTagName("LineString");
                if (lineStringNodeList.getLength() > 0) {
                    Element lineStringNodeElement = (Element) lineStringNodeList.item(0);
                    String nodeValue = getNodeValueFromElement(lineStringNodeElement, "coordinates");

                    addCoordinatesToList(lineList, nodeValue);
                }

                NodeList pointNodeList = placemarkElement.getElementsByTagName("Point");
                if (pointNodeList.getLength() > 0) {
                    Element pointNodeElement = (Element) pointNodeList.item(0);
                    String[] nodeValues = getNodeValueFromElement(pointNodeElement, "coordinates").split(",");
                    String latitude = nodeValues[1].trim();
                    String longitude = nodeValues[0].trim();
                    placemarkList.add(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                    placeMarkNames.add(getNodeValueFromElement(placemarkElement, "name"));
                }
            }

        }

        return concatenate(lineList, placemarkList, placeMarkNames);
    }

    private List<BusRouteCoordinates> concatenate(final List<LatLng> lineList, final List<LatLng> placemarkList,
                                                  final List<String> placeMarkNames) {

        List<BusRouteCoordinates> busRouteCoordinatesCoordinatesList = new ArrayList<>();

        for (LatLng latLng : lineList) {
            busRouteCoordinatesCoordinatesList.add(new BusRouteCoordinates(latLng, false, ""));
        }

        int count = 0;
        for (LatLng latLng : placemarkList) {
            busRouteCoordinatesCoordinatesList.add(getClosest(latLng, busRouteCoordinatesCoordinatesList),
                    new BusRouteCoordinates(latLng, true, placeMarkNames.get(count)));
            count++;
        }

        return busRouteCoordinatesCoordinatesList;
    }

    private int getClosest(LatLng latLng, List<BusRouteCoordinates> lineList) {
        int count = 0;
        int returnIndex = 1;
        float distance = Float.MAX_VALUE;
        for (BusRouteCoordinates latLngList : lineList) {
            if (LatLngUtils.distanceBetween(latLng, latLngList.getLatLng()) < distance) {
                distance = LatLngUtils.distanceBetween(latLng, latLngList.getLatLng());
                returnIndex = count;
            }
            count++;
        }

        if (returnIndex == 0) {
            return 0;
        }

        if (returnIndex >= lineList.size() - 3) {
            return returnIndex;
        }

        if (LatLngUtils.distanceBetween(latLng, lineList.get(returnIndex - 1).getLatLng())
                < LatLngUtils.distanceBetween(latLng, lineList.get(returnIndex + 1).getLatLng())) {
            return returnIndex;
        }

        return returnIndex + 1;
    }

    private String getNodeValueFromElement(Element lineStringNodeElement, String nodeName) {
        NodeList coordinatesNodeList = lineStringNodeElement.getElementsByTagName(nodeName);
        Element coordinatesNodeElement = (Element) coordinatesNodeList.item(0);
        NodeList coordinates = coordinatesNodeElement.getChildNodes();
        return coordinates.item(0).getNodeValue();
    }

    private void addCoordinatesToList(List<LatLng> returnList, String coordinatesLine) {
        String[] lngLatAlt = coordinatesLine.split(" ");
        for (int i = 0; i < lngLatAlt.length; i++) {
            String[] oneLngLatAlt = lngLatAlt[i].split(",");
            if (oneLngLatAlt.length == 3)
                returnList.add(new LatLng(Double.parseDouble(oneLngLatAlt[1].trim()), Double.parseDouble(oneLngLatAlt[0].trim())));
        }
    }
}

