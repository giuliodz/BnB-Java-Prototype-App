import java.util.*;
import org.json.*;
import java.net.*;
import java.io.*;
/**
 * This class parses through Airbnb data and returns 
 * relevant statistics of the data that can then be displayed in the GUI
 *
 * @author Giulio Di Zio
 * @version 17.03.2019
 */
public class Statistics
{
    private ArrayList<AirbnbListing> propertyList;
    String API_KEY = "AIzaSyBxViOnCV63wXCRnsOrlAr4i3j4h8_t1QQ";
    /**
     * Constructor to initialize the referred propertyList for statistics computation.
     */
   Statistics(){
       AirbnbDataLoader x = new AirbnbDataLoader();
       propertyList = new ArrayList<>();
       propertyList = x.load();
       
    }
    
    /**
     * @return ArrayList containing properties within a certain range of pricce and in a specific neighbourhood.
     * @param fromPrice. The minimum price for the properties to look for.
     * @param toPrice. The maximum price for the properties to look for.
     * @param neighbourhood. The name of the neighbourhood for filtering properties. 
     */
    public ArrayList<AirbnbListing> filterProperties(Integer fromPrice, Integer toPrice, String neighbourhood)
    {
         ArrayList<AirbnbListing> queriedProperties = new ArrayList<>(); 
       for(AirbnbListing property : propertyList){
           if(((fromPrice == null)||(property.getPrice() >= fromPrice)) && ((toPrice == null)||(property.getPrice() <= toPrice)) && ((neighbourhood == null)||(property.getNeighbourhood().equalsIgnoreCase(neighbourhood)))){
             queriedProperties.add(property)  ;
            }
       }
       
       return queriedProperties;
    }
    
    /**
     * @retunr The number of reviews por property
     * @param fromPrice. The minimum price for the properties to look for.
     * @param toPrice. The maximum price for the properties to look for.
     * @param neighbourhood. The name of the neighbourhood for filtering properties. 
     */
   public int reviewsForProperty(int fromPrice, int toPrice, String neighbourhood)
   {
       
       ArrayList<AirbnbListing> queriedProperties = filterProperties(fromPrice, toPrice, neighbourhood);
       if (queriedProperties.size()==0){
           return 0;
       }
       int numberOfReviews = 0;
       for(AirbnbListing property : queriedProperties){
           numberOfReviews += property.getNumberOfReviews();
       }
       return (numberOfReviews / queriedProperties.size());
   }
   
   /**
    * @return Number of available properties.
    * @param fromPrice. The minimum price for the properties to look for.
     * @param toPrice. The maximum price for the properties to look for.
     * @param neighbourhood. The name of the neighbourhood for filtering properties. 
    */
   public int getNumberAvailableProperties(int fromPrice, int toPrice, String neighbourhood)
   {
       ArrayList<AirbnbListing> queriedProperties = filterProperties(fromPrice, toPrice, neighbourhood);
       if (queriedProperties.size()==0){
           return 0;
       }
       int numberOfAvailableProperties = 0;
       for(AirbnbListing property : queriedProperties){
           if(property.getAvailability365() > 0){
           numberOfAvailableProperties += 1;
          }
        }
        
       return numberOfAvailableProperties;
   }
   
   /**
    * @return number of entire homes.
    * @param fromPrice. The minimum price for the properties to look for.
     * @param toPrice. The maximum price for the properties to look for.
     * @param neighbourhood. The name of the neighbourhood for filtering properties. 
    */
   public int getNumberEntireHomes(int fromPrice, int toPrice, String neighbourhood)
   {
       ArrayList<AirbnbListing> queriedProperties = filterProperties(fromPrice, toPrice, neighbourhood);
       if (queriedProperties.size()==0){
           return 0;
       }
       int numberOfEntireHouses = 0;
       for(AirbnbListing property : queriedProperties){
           if(property.getRoom_type().trim().equalsIgnoreCase("Entire home/apt")){
               numberOfEntireHouses += 1;
            }
       }
       return numberOfEntireHouses;
    }
    
    /**
     * @return An hashMap that contains the neighbourhood names as keys and the sum of their property prices as values. (the sum is take into account also the number of minimum nights of staying).
     */
    public HashMap<String,Integer> getNeighbourhoodAndCost()
    {
        HashMap<String,Integer> neighbourhoodAndPrices = new HashMap<>(); // HashMap that stores the sum of all the property's prices for each neighbourhood.
        for(AirbnbListing property : propertyList){
             //cehck if the property is in a neighbourhood which hasn' t been put in the neighbourhoodAndPrices map yet.
             if(!neighbourhoodAndPrices.containsKey(property.getNeighbourhood())){
                 //add an entry in the map with the neighbourhood and price*minNights.
                 int calculatedPrice = property.getPrice() * property.getMinimumNights();  
                 neighbourhoodAndPrices.put(property.getNeighbourhood(), calculatedPrice);
                }
                
             else{
                 //The neighbourhood of the property is already in the map. Just update the value that holds for the total price for that neighbourhood.
                 int calculatedPrice = property.getPrice() * property.getMinimumNights();
                 int previousNeighbourhoodPrice = neighbourhoodAndPrices.get(property.getNeighbourhood());
                 calculatedPrice += previousNeighbourhoodPrice;
                 neighbourhoodAndPrices.replace(property.getNeighbourhood(), calculatedPrice);
                }
        }
        return neighbourhoodAndPrices;
    }
    
    
    /**
     * @return The name of the most expensive neighbourhood taking into account the minimunm number of nights of staying too.
     */
    public String getMostExpensiveNeighbourhood()
    {
        HashMap<String,Integer> neighbourhoodAndPrices = getNeighbourhoodAndCost();
        int highestPrice = 0;
        String mostExpensiveNeighbourhood = ""; 
        Iterator it = neighbourhoodAndPrices.entrySet().iterator();
        // Iterate every entry of the hasmap and update the highestPrice and the name of the mostExpensiveNeighbourhood.
        while(it.hasNext()){
             Map.Entry neighbourhood = (Map.Entry) it.next();
             if((Integer) neighbourhood.getValue() > highestPrice){
                    highestPrice = (Integer) neighbourhood.getValue();
                    mostExpensiveNeighbourhood = (String) neighbourhood.getKey();
               }
             }
        return mostExpensiveNeighbourhood;
   
    }
    
    /**
     * @param The neighbourhood to query.
     * @return The position of that neighbourhood in the "neigbourhood-cost-rank"
     */
    public int getNeighbourhoodRank(String neighbourhood)
    {
        HashMap<String,Integer> neighbourhoodAndPrices = getNeighbourhoodAndCost(); // HashMap that stores the sum of all the property's prices for each neighbourhood.
        Set <Map.Entry<String, Integer>> neighbourhoodSet = neighbourhoodAndPrices.entrySet();
        Iterator it = neighbourhoodSet.iterator();
        int highestPrice = 0;
        //Go through every neighbourhood and update the highestPrice to have the price of the most expensive one.
        while (it.hasNext()){
         Map.Entry<String,Integer> element = (Map.Entry)  it.next();
         int price = element.getValue();
         if(price > highestPrice){
             highestPrice = price;
            }
        }
        //Get a sorted arrayList of neighbourhoods considering their expensiveness.
        ArrayList<String> rankedList = sortNeighbourhoods(neighbourhoodSet, highestPrice);
        //Uncomment below to check rankedList
        //System.out.println(rankedList);
        return rankedList.indexOf(neighbourhood.toLowerCase()) + 1;
  }

  /**
   * @param Set of key-value pairs containing the name of the neighbourhood and the price of the properties it holds.
   * @param highest The price of the most expensive neighbourhood.
   * @return A sorted arrayList of neighbourhoods according to their expensiveness (Most to least expensive).
   */
    private ArrayList<String> sortNeighbourhoods (Set <Map.Entry<String, Integer>> set, int highest)
    {
        ArrayList<String> rankedList = new ArrayList<>();
        while(set.size() > 0){
            Iterator it = set.iterator();
            while (it.hasNext()){
                Map.Entry<String, Integer> element = (Map.Entry<String, Integer>) it.next();
                if (compare(element.getValue(), highest) == 0){
                     it.remove();
                     rankedList.add(element.getKey().toLowerCase());
                }
                
            }
            highest--;
        }   
        return rankedList;
    }
    
    /**
     * Simple method used to compare 2 integers.
     * @param First number
     * @param Second number
     * @return 1, 0, or -1 for if the first number is greater, equal to, or less then the second one.
     */
    public int compare(int a, int b)
    {
        int difference = a - b;
        if (difference < 0)return -1; 
        else if (difference == 0)return 0; 
        else return 1;
    }
    
    /**
     * caluculates the average price por property in a given neighbourhood.
     * @param The name of the neighbourhood.
     * @return Average price.
     */
    public int getNeighbourhoodAveragePrice(String neighbourhood)
    {
        ArrayList<AirbnbListing> queriedProperties = filterProperties(null,null, neighbourhood);
        if (queriedProperties.size()==0){
           return 0;
        }
        int sum = 0;
        for(AirbnbListing property : queriedProperties){
            sum += property.getPrice();
        }
        
        return (sum / queriedProperties.size());
    }   
    
    /**
     * Calculates how many properties there are that match the user filters.
     * @param Minimum price
     * @param Maximum price
     * @param Name of the neighbourhood
     * @return number of matching properties 
     */
    public int getMatchingProperties(int fromPrice, int toPrice, String neighbourhood)
    {
         ArrayList<AirbnbListing> queriedProperties = filterProperties(fromPrice, toPrice, neighbourhood);
         return queriedProperties.size();
    } 
    
    /**
     * Calculates how many properties are within 500m away from a tube station taking into account some filters that the user might have set.
     */
    public int getPropertiesNearTubeStations(int fromPrice, int toPrice, String neighbourhood)
    {
        ArrayList<AirbnbListing> queriedProperties = filterProperties(null,null, neighbourhood);
        int propertiesNearStations = 0;
        for(AirbnbListing property : queriedProperties){
            if(isNearbyStations(property.getLatitude(), property.getLongitude())){
            propertiesNearStations += 1 ;
           }
        }
        return propertiesNearStations;
    }
    
    /**
     *  Check whether 500meters away from a given coordinate there are any tube stations.
     *  @param latitude
     *  @param longitude
     *  @return true if 500m away from the given location there's at least one tube station.
     */
    public boolean isNearbyStations(double lat, double lon)
    {
       try{    
            URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?key="+API_KEY+"&location="+lat+","+lon+"&radius=500&type=subway_station");
            URLConnection con = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONArray json = new JSONObject(sb.toString().trim()).getJSONArray("results");
            if (json.length()>0){
                return true;
            }
            else{
                    return false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
}
}