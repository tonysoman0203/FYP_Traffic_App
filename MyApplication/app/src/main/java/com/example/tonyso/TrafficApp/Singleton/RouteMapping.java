package com.example.tonyso.TrafficApp.Singleton;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.example.tonyso.TrafficApp.model.Route;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.rss.XMLReader;

import java.util.List;

/**
 * Created by TonySo on 18/9/2015.
 */
public class RouteMapping {

    public static RouteMapping routeMapping = null;
    private Context context;
    private LruCache<String,Bitmap> mMemoryCache;
    public static final String TAG = RouteMapping.class.getName();

    public static RouteMapping getInstance(Context context){
        if (routeMapping==null){
            synchronized (RouteMapping.class){
                if (routeMapping==null){
                    routeMapping = new RouteMapping(context);
                    Log.e(RouteMapping.class.getName(),"This is a new Instance of "+routeMapping);
                    return routeMapping;
                }
            }
        }
        Log.e(RouteMapping.class.getName(),"This is a same Instance of "+routeMapping);
        return routeMapping;
    }

    @Override
    public String toString() {
        return "RouteMapping{" +
                "context=" + context +
                ", mMemoryCache=" + mMemoryCache +
                '}';
    }

    private RouteMapping(Context context){
        this.context = context;
    }

    public List<RouteCCTV> loadCache(){
        XMLReader xmlReader = new XMLReader(context);
        return xmlReader.getImageXML();
    }

    public void setImageCache(){
        final int cacheSize = (int) (Runtime.getRuntime().maxMemory()/1024)/8;
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
       }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public int getPutCount(){
        Log.e(TAG,""+mMemoryCache.putCount());
        return mMemoryCache.putCount();
    }
}
