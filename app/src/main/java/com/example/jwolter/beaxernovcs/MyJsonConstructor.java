package com.example.jwolter.beaxernovcs;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Diese Klasse erstellt aus einem String der JSON Format hat Objekte.
 */
public class MyJsonConstructor {

    private ArrayList<LocationInformation> listOfLocationInformation;
    private IndoorPosition indoorPosition;

    public MyJsonConstructor(String restClientResponse) {

        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(restClientResponse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject inputJson = (JSONObject) obj;

        JSONArray information = (JSONArray)inputJson.get("informations");
        if (information != null) {
            Iterator<JSONObject> iterator = information.iterator();
            while (iterator.hasNext()) {
                JSONObject jsonObj = iterator.next();
                LocationInformation locInf = new LocationInformation(
                        (String)jsonObj.get("header"),
                        (String)jsonObj.get("text"),
                        (String)jsonObj.get("open"),
                        (String)jsonObj.get("close"),
                        (String)jsonObj.get("imageUrl")
                );
                listOfLocationInformation.add(locInf);
            }
        }


        int intX,intY,intZ;
        intX = Integer.parseInt((String)inputJson.get("x"));
        intY = Integer.parseInt((String)inputJson.get("y"));
        intZ = Integer.parseInt((String)inputJson.get("z"));

        indoorPosition = new IndoorPosition(intX,intY,intZ);
    }

    public ArrayList<LocationInformation> getListOfLocationInformation() {
        return listOfLocationInformation;
    }

    public IndoorPosition getIndoorPosition() {
        return indoorPosition;
    }
}
