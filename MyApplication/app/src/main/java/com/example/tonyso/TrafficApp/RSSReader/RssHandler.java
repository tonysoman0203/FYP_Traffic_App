package com.example.tonyso.TrafficApp.RSSReader;

//import java.io.BufferedReader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.tonyso.TrafficApp.Interface.Rss_Listener;
import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.model.Weather;
import com.example.tonyso.TrafficApp.utility.ErrorDialog;
import com.example.tonyso.TrafficApp.utility.LanguageSelector;

import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

//import java.io.InputStream;
//import java.io.InputStreamReader;

@SuppressLint("SimpleDateFormat")
public class RssHandler extends DefaultHandler {

    private final String item = "item";
    private String desc = "";

    //private final String LANG = "language";

    private String tagName ="";

    Rss_Listener listener;

    private XmlPullParserFactory xmlPullParserFactory;
    private XmlPullParser xmlPullParser;

    private static final String TAG = RssHandler.class.getSimpleName();

    private ArrayList<Weather>weathers;

    ErrorDialog errorDialog;
    HttpURLConnection connection;
    int id = 0;

    public RssHandler(ErrorDialog errorDialog) {this.errorDialog = errorDialog;}

    public void setListener(Rss_Listener listener) {
        this.listener = listener;
    }

    protected void setupHTTPConnection(URL myURL){
        try {
            connection = (HttpURLConnection) myURL.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(true);
            xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(inputStream, "UTF-8");
        } catch (ProtocolException e) {
            e.printStackTrace();
            errorDialog.displayAlertDialog(e.getMessage());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            errorDialog.displayAlertDialog(e.getLocalizedMessage());
        } catch (ConnectException e){
            errorDialog.displayAlertDialog("Connection TimeOut... Please Try Again Later...");
        } catch (IOException e) {
            e.printStackTrace();
            //errorListener.processErrorMessage(e.toString());
        }
    }

    protected void processWeatherFeed(Context context, URL url) {
        setupHTTPConnection(url);
        try {
            weathers = new ArrayList<>();
            Weather weather = new Weather();
            int event = xmlPullParser.getEventType();
            String t = "";

            while (event != XmlPullParser.END_DOCUMENT){
                tagName = xmlPullParser.getName();
                switch (event){
                    case XmlPullParser.START_DOCUMENT:break; //1
                    case XmlPullParser.START_TAG: //2
                        if (tagName.equalsIgnoreCase(item)) {
                            weather = new Weather(id);

                            int token = xmlPullParser.nextToken();
                            while (token!= XmlPullParser.CDSECT){
                                token = xmlPullParser.nextToken();
                            }
                            String CDATA = xmlPullParser.getText();
                            /**
                                                        * should modify to Use Static Data to check Language and Get Weather, Humidity Tag, Degree Tag
                                                        */
                            String degreeResult,degreeResultSet,degreeSign,result_withoutTEMP;
                            LanguageSelector languageSelector = new LanguageSelector(context);
                            if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)){
                                degreeResult = CDATA.substring(
                                        CDATA.indexOf(context.getString(R.string.WEATHER_TAG)),
                                        CDATA.indexOf(context.getString(R.string.HUMIDITY_TAG)));
                                degreeResultSet = degreeResult.replace("<br/>", "");
                                result_withoutTEMP = degreeResultSet.replace(context.getString(R.string.WEATHER_TAG),"");
                                degreeSign = result_withoutTEMP.replace(context.getString(R.string.Degree),context.getString(R.string.DegreeSign));
                            }else{
                                degreeResult = CDATA.substring(
                                        CDATA.indexOf(context.getString(R.string.WEATHER_TAG_ZH)),
                                        CDATA.indexOf(context.getString(R.string.HUMIDITY_TAG_ZH)));
                                degreeResultSet = degreeResult.replace("<br/>", "");
                                result_withoutTEMP = degreeResultSet.replace(context.getString(R.string.WEATHER_TAG_ZH),"");
                                degreeSign = result_withoutTEMP.replace(context.getString(R.string.Degree_ZH),context.getString(R.string.DegreeSign));
                            }
                            weather.setDegree((degreeSign));
                            String link = getWeatherIcon(CDATA);
                            weather.setWeatherIcon(link);
                            id++;
                            break;
                        }
                        break;

                    case XmlPullParser.TEXT: break;

                    case XmlPullParser.END_TAG:
                        switch (tagName){
                            case item:
                                weathers.add(weather);break;
                            default:break;
                        }
                        break;
                }
                event = xmlPullParser.next();
                if (event == XmlPullParser.END_DOCUMENT){
                    listener.ParsedWeatherRssInfo(weathers);
                }
            }

        } catch (ConnectException e){
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            //errorListener.processErrorMessage(e.toString());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }

    }



    private String getWeatherIcon(String description){
       this.desc = description;
        if (description.contains("<img ")){
            String img  = description.substring(description.indexOf("<img "));
            String cleanUp = img.substring(0, img.indexOf(">") + 1);
            img = img.substring(img.indexOf("src=") + 5);
            int indexOf = img.indexOf("\"");
            if (indexOf == -1)
            {
                indexOf = img.indexOf("\"");
            }else
            {
                img = img.substring(0,indexOf);
            }
            Log.e("Image URL",img);
            this.desc = this.desc.replace(cleanUp,"");
            return img;
        }
        return this.desc;
    }

}
