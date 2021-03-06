package com.example.tonyso.TrafficApp.rss_xml_feed;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.model.RouteSpeedMap;
import com.example.tonyso.TrafficApp.utility.Convertor;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soman on 2015/10/30.
 */
public class XMLReader {
    private static final String TAG = XMLReader.class.getSimpleName();
    private static final String IMAGE_LIST_TITLEt = "image-list";
    private static final String image ="image";
    private static final String IMAGE_KEY ="key";
    private static final String IMAGE_ENG_REGION = "english-region";
    private static final String IMAGE_CHI_REGION = "chinese-region";
    private static final String IMAGE_ENG_DESC = "english-description";
    private static final String IMAGE_CHI_DESC = "chinese-description";
    private static final String IMAGE_COORDINATE = "coordinate";
    private static final String ns = null;
    private static final String ROUTE = "Route";
    private static final String Speed = "Speed";
    private static final String Location_SPec = "location_Spec";
    public static XMLReader reader = null;
    Context context;

    private XMLReader(Context context) {
        this.context = context;
    }

    public static XMLReader getInstance(Context context) {
        if (reader == null) {
            synchronized (XMLReader.class) {
                if (reader == null) {
                    reader = new XMLReader(context);
                    return reader;
                }
            }
        }
        Log.d(TAG, "Using Same Instance of" + reader.context);
        return reader;
    }

    @Override
    public String toString() {
        return "XMLReader {" +
                "context=" + context +
                '}';
    }

    public List<RouteSpeedMap> getRouteImageSpeedMap() {
        List<RouteSpeedMap> speedMaps = null;
        try {
            Log.e(TAG, "Start Fetching Route Speed Map");
            speedMaps = fetchSpeedMap();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.e(TAG, "Fetching Route Speed Map Complete with the size=" + speedMaps.size());
            return speedMaps;
        }
    }

    private List<RouteSpeedMap> fetchSpeedMap() {
        XmlResourceParser parser = createXMLParser(R.xml.routespeed);
        List<RouteSpeedMap> speedMaps = new ArrayList<>();
        try {
            parser.next();
            parser.next();
            return readSpeedMap(parser, speedMaps);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        } finally {
            parser.close();
        }
        return speedMaps;
    }

    private List<RouteSpeedMap> readSpeedMap(XmlResourceParser parser, List<RouteSpeedMap> speedMaps) throws IOException, XmlPullParserException {
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.e(TAG, name);
            // Starts by looking for the entry tag
            if (name.equals(Speed)) {
                speedMaps.add(readSpeed(parser));
            } else {
                skip(parser);
            }
        }
        return speedMaps;
    }

    private RouteSpeedMap readSpeed(XmlResourceParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, Speed);
        int id = 0;
        String[] regions = new String[2];
        String[] description = new String[2];
        String key;
        double[] coordinate;
        RouteSpeedMap routeSpeedMap = new RouteSpeedMap();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case IMAGE_KEY:
                    key = readKey(parser);
                    routeSpeedMap.setRef_key(key);
                    break;
                case IMAGE_ENG_REGION:
                    regions[0] = readRegion(parser, IMAGE_ENG_REGION);
                    break;
                case IMAGE_CHI_REGION:
                    regions[1] = readRegion(parser, IMAGE_CHI_REGION);
                    break;
                case IMAGE_ENG_DESC:
                    description[0] = readDesc(parser, IMAGE_ENG_DESC);
                    break;
                case IMAGE_CHI_DESC:
                    description[1] = readDesc(parser, IMAGE_CHI_DESC);
                    break;
                case IMAGE_COORDINATE:
                    coordinate = readCoordinate(parser);
                    routeSpeedMap.setLatLng(coordinate);
                    break;
            }
            id++;
        }
        return routeSpeedMap.setRegions(regions).setDescription(description).build(id);
    }

    public List<RouteCCTV> getImageXML(){
        return fetchImageXML();
    }

    private List<RouteCCTV> fetchImageXML() {
        XmlResourceParser parser = createXMLParser(R.xml.imagelist);
        List<RouteCCTV> list = new ArrayList<>();
        try {
            parser.next();
            parser.next();
            return readFeed(parser,list);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        } finally {
            parser.close();
        }
        return list;
    }

    private List<RouteCCTV> readFeed(XmlResourceParser parser, List<RouteCCTV> list) throws IOException, XmlPullParserException {
        //parser.require(XmlPullParser.START_TAG, ns, image);
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals(image)) {
                list.add(readImage(parser));
            } else {
                skip(parser);
            }
        }
        return list;
    }

    private RouteCCTV readImage(XmlResourceParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "image");
        int id=0;
        String [] regions = new String[2];
        String [] description = new String[2];
        String key = null;
        double[] coordinate;
        RouteCCTV.Builder builder = new RouteCCTV.Builder();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case IMAGE_KEY:
                    key = readKey(parser);
                    builder.setKey(key);
                    break;
                case IMAGE_ENG_REGION:
                    regions[0] = readRegion(parser, IMAGE_ENG_REGION);
                    break;
                case IMAGE_CHI_REGION:
                    regions[1] = readRegion(parser, IMAGE_CHI_REGION);
                    break;
                case IMAGE_ENG_DESC:
                    description[0] = readDesc(parser, IMAGE_ENG_DESC);
                    break;
                case IMAGE_CHI_DESC:
                    description[1] = readDesc(parser, IMAGE_CHI_DESC);
                    break;
                case IMAGE_COORDINATE:
                    coordinate = readCoordinate(parser);
                    builder.setLatLngs(coordinate);
                    break;
            }
            id++;
        }
        return builder.setDescription(description).setRegion(regions).setType(RouteCCTV.TYPE_CCTV).build();
    }

    private double[] readCoordinate(XmlResourceParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, IMAGE_COORDINATE);
        String lat = readText(parser);
        //Log.e(TAG, lat);
        parser.require(XmlPullParser.END_TAG, ns, IMAGE_COORDINATE);
        String[] latlong = lat.split(" ");
        Convertor convertor = new Convertor(latlong[1],latlong[0]);
        return convertor.output();
    }

    private String readDesc(XmlResourceParser parser, String key) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, key);
        String region = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, key);
        return region;
    }

    private String readRegion(XmlResourceParser parser,String key) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, key);
        String region = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, key);
        return region;
    }

    private String readKey(XmlResourceParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, IMAGE_KEY);
        String key = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, IMAGE_KEY);
        return key;
    }

    private String readText(XmlResourceParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }



    private void skip(XmlResourceParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
}

    private XmlResourceParser createXMLParser(int src) {
        return context.getResources().getXml(src);
    }
}
