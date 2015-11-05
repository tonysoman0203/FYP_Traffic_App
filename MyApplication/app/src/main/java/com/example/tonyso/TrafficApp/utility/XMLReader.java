package com.example.tonyso.TrafficApp.utility;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.example.tonyso.TrafficApp.Interface.XMLFetchInterface;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.model.RouteCCTV;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soman on 2015/10/30.
 */
public class XMLReader {
    Context context;

    private static final String TAG = XMLReader.class.getSimpleName();
    private static final String imageList ="image-list";
    private static final String image ="image";
    private static final String IMAGE_KEY ="key";
    private static final String IMAGE_ENG_REGION = "english-region";
    private static final String IMAGE_CHI_REGION = "chinese-region";
    private static final String IMAGE_ENG_DESC = "english-description";
    private static final String IMAGE_CHI_DESC = "chinese-description";
    private static final String IMAGE_COORDINATE = "coordinate";

    private XMLFetchInterface xmlFetchInterface;
    private static final String ns = null;

    public XMLReader(Context context,XMLFetchInterface xmlFetchInterface) {
        this.context = context;
        this.xmlFetchInterface = xmlFetchInterface;
    }

    public void feedImageXml(){
        xmlFetchInterface.onXMLFetch(fetchImageXML());
    }

    private  List fetchImageXML(){
        XmlResourceParser parser = createXMLParser();
        ArrayList<RouteCCTV> list = new ArrayList<>();
        try {
            parser.next();
            parser.next();
            return readFeed(parser,list);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            parser.close();
        }
        return list;
    }

    private List readFeed(XmlResourceParser parser,List list) throws IOException, XmlPullParserException {
        List entries = list;
        //parser.require(XmlPullParser.START_TAG, ns, image);
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals(image)) {
                entries.add(readImage(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private RouteCCTV readImage(XmlResourceParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "image");
        int id=0;
        String [] regions = new String[2];
        String [] description = new String[2];
        String key = null;
        double[] coordinate = new double[2];

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(IMAGE_KEY)) {
                key = readKey(parser);
            } else if (name.equals(IMAGE_ENG_REGION)) {
                regions[0] = readRegion(parser,IMAGE_ENG_REGION);
            } else if (name.equals(IMAGE_CHI_REGION)) {
                regions[1] = readRegion(parser,IMAGE_CHI_REGION);
            } else if (name.equals(IMAGE_ENG_DESC)) {
                description[0] = readDesc(parser,IMAGE_ENG_DESC);
            } else if (name.equals(IMAGE_CHI_DESC)) {
                description[1] = readDesc(parser,IMAGE_CHI_DESC);
            } else if (name.equals(IMAGE_COORDINATE)){
                coordinate = readCoordinate(parser);
            }
            id++;
        }
        return new RouteCCTV(id,key,description,regions,coordinate);
    }

    private double[] readCoordinate(XmlResourceParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, IMAGE_COORDINATE);
        String lat = readText(parser);
        Log.e(TAG, lat);
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

    private XmlResourceParser createXMLParser(){
        XmlResourceParser xpp = context.getResources().getXml(R.xml.imagelist);
        return xpp;
    }
}
