import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.scene.*;
import javafx.scene.effect.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.scene.web.WebView;
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.beans.value.*;

import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.collections.FXCollections;
/**
 * This window shows up when the user has selected a borough on the map
 * It will then give details of individual listingings in that borough
 * and shows a google map section which the user can search for POIs on.
 *
 * @author Brandon Cardillo
 * @version 17.03.2019
 */ 
public class PropertyListWindow
{
    // Create instances.
    private int fromPrice;
    private int toPrice;
    private String boroughName;
    
    //The full list and filtered listings depending on what the user has selected and how they sorted them.
    private AirbnbDataLoader propertyLoader = new AirbnbDataLoader();
    private ArrayList<AirbnbListing> listings = new ArrayList<AirbnbListing>();
    private ArrayList<AirbnbListing> fileredListings = new ArrayList<AirbnbListing>();
    
    private Stage stage= new Stage();
    private Stage descriptionStage= new Stage();
    
    private ListView<AirbnbListing> listOfProperties = new ListView<>();
    private ObservableList<AirbnbListing> items =FXCollections.observableArrayList();
    
    // View for the Google Map API to fit into
    private WebView mapView;
    private MapFunctionality maps = new MapFunctionality();
    private String currentMapView = "Map";
    
    /**
     * Create window with the user selcted price range and thier choice of borough
     */
    public PropertyListWindow(int fromPrice, int toPrice, String boroughName)
    {
        this.fromPrice = fromPrice;
        this.toPrice = toPrice;
        this.boroughName = boroughName;
        listings = propertyLoader.load();
        if(this.boroughName==null){
            this.boroughName="";
        }
        filter();
    }

    /**
     * The main funtion which shows the filtered list of properties in its window
     * This is done using JavaFx
     */
    public void show()
    {
        //Main Grid
        GridPane mainGridPane = new GridPane();
        GridPane gridPane = new GridPane();
        
        //Borough Map creation
        WebView boroughMap = maps.showBoroughMap(boroughName, 500, 600);
        
        mainGridPane.add(gridPane, 0, 0);
        mainGridPane.add(boroughMap, 1, 0);
        
        // Make the grid column and row sizes equal
        ColumnConstraints columnMain = new ColumnConstraints();
        columnMain.setPercentWidth(50);
        RowConstraints rowMain = new RowConstraints();
        rowMain.setPercentHeight(100);
        mainGridPane.getColumnConstraints().addAll(columnMain,new ColumnConstraints(520));
        mainGridPane.getRowConstraints().addAll(rowMain);
        
        //Add labels and choice boxes for the sorting 
        Label sortLabel = new Label("Sort:  ");
        sortLabel.setFont(new Font("Arial", 24));
        ChoiceBox<String> sortChoices = new ChoiceBox<>();
        sortChoices.getItems().addAll("Price","Reviews","Host Name");
        sortChoices.setMinWidth(100);
        sortChoices.setMinHeight(30);
        
        //Add labels and choice boxes for the ordering 
        Label orderLabel = new Label("Order:  ");
        orderLabel.setFont(new Font("Arial", 24));
        ChoiceBox<String> orderChoices = new ChoiceBox<>();
        orderChoices.getItems().addAll("ASC","DESC");
        orderChoices.setMinWidth(100);
        orderChoices.setMinHeight(30);
        
        sortChoices.getSelectionModel().selectFirst();
        orderChoices.getSelectionModel().selectFirst();
        
        //Listeners for the choice boxes
        sortChoices.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> changeListView(newVal,orderChoices.getValue()));
        orderChoices.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> changeListView(sortChoices.getValue(),newVal));
        
        
        HBox rightUpHbox = new HBox(sortLabel, sortChoices);  
        HBox leftUpHbox = new HBox(orderLabel, orderChoices);
        
        
        sortListing(fileredListings, "Price");
        orderListing(fileredListings, "ASC");
        fillList(fileredListings);
        
        // when item in list is pressed show that propety desription
        listOfProperties.setOnMouseClicked(e -> showPropertyDescriptionWindow(e));
            
        gridPane.add(rightUpHbox, 0, 0, 2, 1);
        gridPane.add(leftUpHbox, 2, 0, 2, 1);
        gridPane.add(listOfProperties, 0, 1, 4, 6);
        
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(25);
        
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(100/6);
        
        gridPane.getColumnConstraints().addAll(column1, column1, column1, column1);
        gridPane.getRowConstraints().addAll(row1, row1, row1, row1,row1, row1, row1);
        
        mainGridPane.setPadding(new Insets(10, 10, 10, 10));
        mainGridPane.setStyle("-fx-background-color: #ccc;");
        Scene scene = new Scene(mainGridPane,1060, 640);
        stage.setTitle("Properties in "+boroughName);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Show the more detailed description of the property
     */
    private void showPropertyDescriptionWindow(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                AirbnbListing currentProperty = listOfProperties.getSelectionModel().getSelectedItem();
                showDescription(currentProperty);
            }
        }
    }
    
    /**
     * Window for more detailed description of property listing
     */
    private void showDescription(AirbnbListing listing)
    {
        // GridPane
        GridPane gridPane = new GridPane();
        GridPane descriptionGridPane = new GridPane();
        
        double lat = listing.getLatitude();
        double lon = listing.getLongitude();
        
        // Add map for the listing
        mapView = maps.showPropertyMap(lat, lon, 500 , 600);
        gridPane.add(mapView, 0, 0);
        gridPane.add(descriptionGridPane, 1, 0);
        
        Text t = new Text();
        String thePropertyDescription = listing.fullDesription();
        
        StringBuilder input = new StringBuilder(thePropertyDescription);  
        int indexOfA = input.indexOf("Address:  ");
        input.insert(indexOfA+10, maps.getAddressString(lat, lon));
        thePropertyDescription = input.toString();
        
        t.setText(thePropertyDescription);
        t.setFont(Font.font ("Verdana", 18));
        t.setWrappingWidth(500);
        t.setTextAlignment(TextAlignment.JUSTIFY);
        descriptionGridPane.add(t, 0, 1, 4, 6);
        
        Button mapModeButton = new Button("Street View");
        mapModeButton.setMinWidth(100);
        mapModeButton.setMinHeight(30);
        
        mapModeButton.setOnMouseClicked(e -> changeMapModeVIew(e,mapModeButton,gridPane,lat,lon));
         
        //Add Poi functionality using MapFunctionality (Google API)
        Label poiLabel = new Label("Point's Of Intrest:  ");
        poiLabel.setFont(new Font("Arial", 24));
        ChoiceBox<String> poiChoices = new ChoiceBox<>();
        poiChoices.getItems().addAll("Map", "Bus Stops","Train Stations","Tube Stations", "Schools", "Night Life", "Pubs", "Restaurants", "Bars");
        poiChoices.setMinWidth(150);
        poiChoices.setMinHeight(30);
        poiChoices.setMaxWidth(150);
        poiChoices.setMaxHeight(30);
        HBox poiBox = new HBox(poiLabel, poiChoices); 
        
        poiChoices.getSelectionModel().selectFirst();
        poiChoices.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> changePoiMap(v,oldVal,newVal,gridPane,lat,lon));
        
        descriptionGridPane.add(poiBox, 1, 0, 3, 1);
        descriptionGridPane.add(mapModeButton, 0, 7);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(100);
        gridPane.getColumnConstraints().addAll(column1,column1);
        gridPane.getRowConstraints().addAll(row1);
        
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(100/4);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(100/8);
        
        descriptionGridPane.getColumnConstraints().addAll(column1,column1,column1,column1);
        descriptionGridPane.getRowConstraints().addAll(row1,row1,row1,row1,row1,row1,row1,row1);
        
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setStyle("-fx-background-color: #ccc;");
        
        Scene scene = new Scene(gridPane, 1060, 640);
        descriptionStage.setTitle("Airbnb Lisiting Description");
        descriptionStage.setScene(scene);
        descriptionStage.show();
    }
    
    /**
     * Function that changes what the map shows depending on the users choiceof poi
     */
    private void changePoiMap(ObservableValue ov, String oldvalue, String newvalue, GridPane gridPane, double lat, double lon)
    {
        if (newvalue.equals("Map")){
            gridPane.getChildren().remove(mapView);
            mapView = maps.showPropertyMap(lat, lon, 500 , 600);
        }
        else{
            gridPane.getChildren().remove(mapView);
            mapView = maps.showSearchPoi(newvalue,lat, lon, 500 , 600);
        }
        gridPane.add(mapView, 0, 0);
    }
     
    /**
     * Function to change map view from regular to street view
     */
    private void changeMapModeVIew(MouseEvent event, Button mapModeButton, GridPane gridPane, double lat, double lon){
        //change from Map to StreetView
        if (currentMapView.equalsIgnoreCase("Map")){
            TextField tf = new TextField();
            mapModeButton.setText("Map");
            currentMapView = "Street";
            mapView = maps.showPropertyStreetViewStatic(lat, lon, 500 , 600);
            gridPane.getChildren().remove(mapView);
            gridPane.add(mapView, 0, 0);
        }
        
        //change from StreetView to Map
        else if (currentMapView.equalsIgnoreCase("Street")){
            TextField tf = new TextField();
            mapModeButton.setText("Street View");
            currentMapView = "Map";
            mapView = maps.showPropertyMap(lat, lon, 500 , 600);
            gridPane.getChildren().remove(mapView);
            gridPane.add(mapView, 0, 0);
        }
    }
    
    /**
     * Filter the listing depening on the users price range and borough
     */
    private void filter()
    {
        for (AirbnbListing listing : listings){
            int currentPrice= listing.getPrice();
            String currentNeighbourhood= listing.getNeighbourhood();
            if(currentNeighbourhood.equalsIgnoreCase(boroughName) && currentPrice<=toPrice && currentPrice>=fromPrice){
                fileredListings.add(listing);
            }
            else if(boroughName.equalsIgnoreCase("") && currentPrice<=toPrice && currentPrice>=fromPrice){
                fileredListings.add(listing);
            }
        }
    }
    
    /**
     * Sort list based on users choice
     */
    private void sortListing(ArrayList<AirbnbListing> unsortedList, String sortMethod)
    {
        if (sortMethod.equalsIgnoreCase("Price")){
            unsortedList.sort(Comparator.comparing(AirbnbListing::getPrice));
        }
        else if (sortMethod.equalsIgnoreCase("Reviews")){
            unsortedList.sort(Comparator.comparing(AirbnbListing::getNumberOfReviews));
        }
        else if (sortMethod.equalsIgnoreCase("Host Name")){
            unsortedList.sort(Comparator.comparing(AirbnbListing::getHost_name));
        }
    }
    
    /**
     * Order list based on user choice
     */
    private void orderListing(ArrayList<AirbnbListing> unsortedList, String order)
    {
        if (order.equalsIgnoreCase("ASC")){
            //Already in Ascending Order
        }
        else if (order.equalsIgnoreCase("DESC")){
            Collections.reverse(unsortedList);
        }
    }
    
    /**
     * Fill the observable list (what the user can see)
     * with the filtered list
     */
    private void fillList(ArrayList<AirbnbListing> currentList)
    {
        listOfProperties.getItems().clear();
        for (AirbnbListing listing : currentList){
            items.add(listing);
        }
        listOfProperties.setItems(items);
    }

    /**
     * A function that takes the sort and order choices and refreshes the observable list
     */
    private void changeListView(String newSort, String newOrder){
        sortListing(fileredListings,newSort);
        orderListing(fileredListings,newOrder);
        fillList(fileredListings);
    }
    
    /**
     * get size of filtered list
     */
    public int getFilteredListSize(){
        return fileredListings.size();
    }
}
