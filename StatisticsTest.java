import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * The test class StatisticsTest.
 *
 * @author  Stuart Davis
 * @version 25.03.2019
 */
public class StatisticsTest
{
    private ArrayList<AirbnbListing> propertyList;
    //change the next 2 lines to set a price range for the queried properties
    private int fromPrice = 54;
    private int toPrice = 55;
    //change to filter for your desired borough
    private String neighbourhood = "Kingston upon Thames";
    private Statistics stats1 = new Statistics();
    private ArrayList<AirbnbListing> queriedProperties = stats1.filterProperties(fromPrice,toPrice,neighbourhood);
    //change to the expected IDs for your filtered properties
    private ArrayList<Integer> testFilteredPropertyIDs = new ArrayList <Integer>(Arrays.asList(6443494,6179409, 14976692, 15330979));
    //change the below values to your expected results using the csv file
    private int testReviewsForProperty = 6;
    private int testNumberAvailableProperties = 2;
    private int testNumberEntireHomes = 1;
    private int neighbourhoodAndPricesTest = 64447;
    private String testMostExpensiveNeighbourhood = "Westminster";
    private int testNeighbourhoodRank = 24;
    private int testNeighbourhoodAveragePrice = 74;
    private int testMatchingPropertiesNumber = 4;
    private int testPropertiesNearTubeStations = 0;
    /**
     * Default constructor for test class StatisticsTest
     */
    public StatisticsTest()
    {      
        
    }

    /**
     * Sets up the test fixture.
     * Called before every test case method.
     * Initialises the propertyList and the AirbnbDataLoader.
     */
    @Before
    public void setUp()
    {
        AirbnbDataLoader x = new AirbnbDataLoader();
        propertyList = new ArrayList<>();
        propertyList = x.load();
    }
    
    @Test
    /**
     * Tests the quieried properties against the expected filtered properties.
     */
    public void filterPropertiesTest(){
        ArrayList<Integer> queriedPropertyIDs = new ArrayList<Integer>();
        for(AirbnbListing property : queriedProperties){
         Integer id = Integer.valueOf(property.getId());
         queriedPropertyIDs.add(id);
        }
        Collections.sort(queriedPropertyIDs);
        Collections.sort(testFilteredPropertyIDs);
        assertEquals(queriedPropertyIDs, testFilteredPropertyIDs);
    }
        
    @Test
    /**
     * Tests the number of reviews for the queried properties against the number of expected reviews.
     */
    public void reviewsForPropertyTest(){
        int actualReviewsForProperty = stats1.reviewsForProperty(fromPrice, toPrice, neighbourhood);
        assertEquals(actualReviewsForProperty, testReviewsForProperty);
    }
        
    @Test 
    /**
     * Tests the number of available properties within the queried properties against the expected number.
     */
    public void numberOfAvailablePropertiesTest(){
        int actualNoAvailableProperties = stats1.getNumberAvailableProperties(fromPrice, toPrice, neighbourhood);
        assertEquals(actualNoAvailableProperties, testNumberAvailableProperties);
    }
    
    @Test
    /**
     * Tests the number of properties that are entire homes within the queried properties against
     * the expected number.
     */
    public void numberEntireHomesTest(){ 
        int actualNoEntireHomes = stats1.getNumberEntireHomes(fromPrice, toPrice, neighbourhood);
        assertEquals(actualNoEntireHomes, testNumberEntireHomes);
    }
    
    @Test
    /**
     * Tests the total cost for a neighbourhood's properties against the expected value.
     */
    public void neighbourhoodAndCostTest(){
      HashMap neighbourhoodAndPrices = stats1.getNeighbourhoodAndCost();
      int actualNeighbourhoodAndPrices = (int) neighbourhoodAndPrices.get(neighbourhood);
      assertEquals(actualNeighbourhoodAndPrices,neighbourhoodAndPricesTest);
    }
    
    @Test
    /**
     * Tests the most expected neighbourhood against the expected one.
     */
    public void mostExpensiveNeighbourhoodTest(){
        assertEquals(stats1.getMostExpensiveNeighbourhood(),testMostExpensiveNeighbourhood);
    }
    
    @Test
    /**
     * Tests that the rank for a neighbourhood's cost is correct against an expected value.
     * To check the returned rankedList from the sortNeighbourhoods method uncomment the print from
     * the getNeighbourhoodRank method, this will help to identify which method the problem is in.
     */
    public void neighbourhoodRankTest(){
        int actualNeighbourhoodRank = stats1.getNeighbourhoodRank(neighbourhood);
        assertEquals(actualNeighbourhoodRank, testNeighbourhoodRank);
    }    
    
    @Test
    /**
     * Test that the compare method works as expected.
     */
    public void compareTest(){
        int actualCompare1 = stats1.compare(7,8);
        int testCompare1 = -1;
        assertEquals(actualCompare1,testCompare1);
        int actualCompare2 = stats1.compare(7,7);
        int testCompare2 = 0;
        assertEquals(actualCompare2,testCompare2);
        int actualCompare3 = stats1.compare(8,7);
        int testCompare3 = 1;
        assertEquals(actualCompare3,testCompare3);
    }
    
    @Test
    /**
     * Tests the average property price for a neighbourhood against it's expected value.
     */
    public void neighbourhoodAveragePriceTest(){
        int actualNeighbourhoodAveragePrice = stats1.getNeighbourhoodAveragePrice(neighbourhood);
        assertEquals(actualNeighbourhoodAveragePrice,testNeighbourhoodAveragePrice);
    }
    
    @Test
    /**
     * Test the number of matching properties that are queried against the expected value.
     */
    public void matchingPropertiesTest(){
        int actualMatchingPropertiesNumber = stats1.getMatchingProperties(fromPrice, toPrice, neighbourhood);
        assertEquals(actualMatchingPropertiesNumber,testMatchingPropertiesNumber);
    }
    
    @Test
    public void getPropertiesNearTubeStationsTest(){
        int actualPropertiesNearTubeStations = stats1.getPropertiesNearTubeStations(fromPrice,toPrice,neighbourhood);
        System.out.println(actualPropertiesNearTubeStations);
        assertEquals(actualPropertiesNearTubeStations,testPropertiesNearTubeStations);
    }
    
    /**
     * Tears down the test fixture.
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

}


