import java.util.ArrayList;
import java.io.*;
import java.net.*;
import org.json.*;


import javafx.scene.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.scene.web.WebView;
/**
 * Write a description of class MapFunctionality here.
 *
 * @author Brandon Cardillo, Giulio Di Zio
 * @version 17.03.2019
 */
public class MapFunctionality
{
    String API_KEY = "AIzaSyBxViOnCV63wXCRnsOrlAr4i3j4h8_t1QQ";
    
    /**
     * Constructor for objects of class MapFunctionality
     */
    public MapFunctionality()
    {
        //Empty
    }
    
    /**
     * load the webview of a borough with a given width and heigh
     * 
     * @param name, width and height
     * @return WebView
     */
    public WebView showBoroughMap(String boroughName, int width , int height)
    {
        String htmlboroughName = boroughName.replaceAll("\\s","+").toLowerCase();
        WebView mapView = new WebView();
        String url = "<iframe width="+width+" height="+height+" frameborder='1' style='border:1'src='https://www.google.com/maps/embed/v1/place?q="+htmlboroughName+",+London,+Uk&zoom=12&key=AIzaSyBjcgL_1rQcwnlrJo80Fzz6Xvcq2yYSRxg'></iframe>";
        mapView.getEngine().loadContent(url);
        return mapView;
    }
    
    /**
     * load the webview of a streetView that is static (you can not move arround, so basically an image)
     * with a given width and heigh
     * 
     * @param lat, lon, width and height
     * @return WebView
     */
    public WebView showPropertyStreetViewStatic(double lat, double lon, int width , int height)
    {
        WebView mapView = new WebView();
        String url = "https://maps.googleapis.com/maps/api/streetview?size="+width+"x"+height+"&location="+lat+","+lon+"&key="+API_KEY;
        mapView.getEngine().load(url);

        return mapView;
    }
    
    /**
     * load the webview of a streetView that is dynamic (you can  move arround, but glitchy due to bad api)
     * with a given width and heigh
     * 
     * @param lat, lon, width and height
     * @return WebView
     */
    public WebView showPropertyStreetView(double lat, double lon, int width , int height)
    {
        WebView mapView = new WebView();
        String url = "<iframe width="+width+" height="+height+" frameborder='0' style='border:0'src='https://www.google.com/maps/embed/v1/streetview?location="+lat+","+lon+"&key="+API_KEY+"'></iframe>";
        mapView.getEngine().loadContent(url);

        return mapView;
    }
    
    /**
     * load the webview of a map with a marker placed on the properties location
     * with a given width and heigh
     * 
     * @param lat, lon, width and height
     * @return WebView
     */
    public WebView showPropertyMap(double lat, double lon, int width , int height)
    {
        WebView mapView = new WebView();
        String url = "<iframe width="+width+" height="+height+" frameborder='1' style='border:1'src='https://www.google.com/maps/embed/v1/place?q=location="+lat+","+lon+"&zoom=15&key="+API_KEY+"'></iframe>";
        mapView.getEngine().loadContent(url);
        
        return mapView;
    }
    
    /**
     * load the webview of a user specied point of interest
     * with a given width and heigh
     * 
     * @param poi, lat, lon, width and height
     * @return WebView
     */
    public WebView showSearchPoi(String poi, double lat, double lon, int width , int height)
    {
        WebView mapView = new WebView();
        String address = getAddressString(lat, lon);
        address = address.replaceAll("\\s","+").toLowerCase();
        poi = poi.replaceAll("\\s","+").toLowerCase();
        String url = "<iframe width="+width+" height="+height+" frameborder='1' style='border:1'src='https://www.google.com/maps/embed/v1/search?key="+API_KEY+"&q="+poi+"+near+"+address+"&zoom=14&center="+lat+","+lon+"'></iframe>";
        mapView.getEngine().loadContent(url);
        
        return mapView;
    }
    
    /**
     * Uses geocoding Api to perform reverse GeoCoding on the lat and lon of a property.
     * @param lat, lon
     * @return String (the address at these coordinates)
     */
    public String getAddressString(double lat, double lon){
        String address= null;
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lon+"&key="+API_KEY);
            URLConnection con = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONObject json = new JSONObject(sb.toString());
            String intermidiate = json.getString("results");
            intermidiate = intermidiate.substring(1, intermidiate.length()-1);
            json = new JSONObject(intermidiate);
            address = json.getString("formatted_address");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }
}
