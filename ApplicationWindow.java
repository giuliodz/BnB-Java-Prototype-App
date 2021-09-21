import java.util.stream.IntStream; 

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.scene.image.*;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.Interpolator;

import javafx.scene.input.*;
import javafx.scene.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.web.WebView;

/**
 * The main application window to search Airbnb listings available
 * in london.
 *
 * @author Brandon Cardillo, Daniel Idowu, Giulio Di Zio, Stuart Davis
 * @version 17.03.2019
 */
public class ApplicationWindow extends Application
{
    
    private Integer fromPrice=0;
    private Integer toPrice;
    private String boroughName;
    
    private String currentSlideName = "Welcome";
    private String slideOrder[] = {"Welcome", "Map", "Statistics","GoogleMap"};
    
    private MapPanel mapPanel;
    private MapFunctionality mapFunc = new MapFunctionality();
    private StatisticsWindow statsPanel = new StatisticsWindow();
   
    //Main Boarder Pane on main window
    BorderPane border = new BorderPane();
    
    private StackPane slidePane = new StackPane();
     
    private GridPane slideGridWelcome;
    private GridPane slideGridMap;
    private GridPane slideGridStatistics;
    private GridPane slideGridGoogleMap;
    
    private HashMap<String, GridPane> slideAbr = new HashMap<String, GridPane>();
    /**
     * The main method to start and launch the whole application window. This launches the GUI.
     */
    @Override
    public void start(Stage stage) throws Exception
    { 
         //Load up each panel in the GUI
         slideGridMap = getMap();
         slideGridWelcome = getWelcome();
         slideGridStatistics = getStatistics();
         slideGridGoogleMap = getGoogleMap();
         
         //Establishing the order of each of the panels
         slideAbr.put(slideOrder[0],slideGridWelcome);
         slideAbr.put(slideOrder[1],slideGridMap);
         slideAbr.put(slideOrder[2],slideGridStatistics);
         slideAbr.put(slideOrder[3],slideGridGoogleMap);
         
         //Creating buttons and choice options for the to and from prices
         int buttonWidth = 100;
         int buttonHeight = 30;
         Label fromLabel = new Label("From:  ");
         fromLabel.setMinWidth(50);
         fromLabel.setMinHeight(30);
         fromLabel.setFont(new Font("Arial", 20));
         ChoiceBox lowerPriceBox = new ChoiceBox();
         lowerPriceBox.getItems().addAll(0,25,50,100,250,500,750,1000,2000,5000,10000);
         Region spacer1 = new Region();
         spacer1.setPrefWidth(20);
         Region spacer2 = new Region();
         spacer2.setPrefWidth(15);
         
         Label toLabel = new Label("To:");
         toLabel.setMinWidth(40);
         toLabel.setMinHeight(30);
         toLabel.setFont(new Font("Arial", 20));
         ChoiceBox upperPriceBox = new ChoiceBox();
         upperPriceBox.getItems().addAll(0,25,50,100,250,500,750,1000,2000,5000,10000);
         
         //Inserting the logo in the GUI toolbar
         Image img = new Image(getClass().getResourceAsStream("Logo.jpg"));
         ImageView logo = new ImageView(img);
         
         //Creating 'next' and 'previous' buttons for the GUI
         Button previousButton = new Button("<");
         previousButton.setMaxSize(buttonWidth,buttonHeight);
         previousButton.setMinSize(buttonWidth,buttonHeight);
         previousButton.setDisable(true);
         
         Button nextButton = new Button(">");
         nextButton.setMaxSize(buttonWidth,buttonHeight);
         nextButton.setMinSize(buttonWidth,buttonHeight);
         nextButton.setDisable(true);
         
         //Functionality to switch between different panels
         previousButton.setOnMouseClicked(e -> switchScene(previousButton, nextButton));
         nextButton.setOnMouseClicked(e -> switchScene(nextButton, previousButton));
         
         final Pane spacer3 = new Pane();
         HBox.setHgrow(spacer3, Priority.ALWAYS);
         
         final Pane spacerImage = new Pane();
         HBox.setHgrow(spacerImage, Priority.ALWAYS);
         
         HBox topHbox = new HBox(logo, spacerImage, fromLabel, lowerPriceBox, spacer1, toLabel, upperPriceBox, spacer2);
         topHbox.setAlignment(Pos.TOP_RIGHT);
         border.setTop(topHbox);
         
         HBox bottomHbox = new HBox(previousButton, spacer3, nextButton);
         bottomHbox.setAlignment(Pos.BOTTOM_CENTER);
         border.setBottom(bottomHbox);
         
         //Functionality to select lower and upper price limits.
         lowerPriceBox.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> 
         getChoiceBoxValues(newVal,upperPriceBox.getValue(), lowerPriceBox, upperPriceBox, previousButton, nextButton));
         upperPriceBox.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> 
         getChoiceBoxValues(lowerPriceBox.getValue(),newVal, lowerPriceBox, upperPriceBox, previousButton, nextButton));
         
         //Styling for the window
         slidePane.getChildren().add(slideGridWelcome);;
         slidePane.setPadding(new Insets(12,12,12,12));
         border.setCenter(slidePane);
         border.setPadding(new Insets(10, 10, 10, 10));
         border.setStyle("-fx-background-color: #ccc;");
         
         Scene scene = new Scene(border, 850, 800);
         stage.setTitle("Application Window");
         stage.setScene(scene);
         Image imgTop = new Image(getClass().getResourceAsStream("LogoTop.jpg"));
         stage.getIcons().add(imgTop);
         // Show the Stage (window)
         stage.show();
    }  
    
    /**
     * Method to load up the borough map of London.
     * @return the visual and functional representation of the map
     */
    private GridPane getMap(){
        if(fromPrice==null){fromPrice=0;}
        if(toPrice==null){toPrice=0;}
        mapPanel = new MapPanel(fromPrice,toPrice);
        return mapPanel.getMap();
    }
    
    
    /**
     * Load up the google map panel which displays a view of London.
     * @return the google map view of London
     */
    private GridPane getGoogleMap(){
        GridPane slideGrid = new GridPane(); 
        WebView londonMap = mapFunc.showBoroughMap("London", 780 , 580);
        slideGrid.add(londonMap,0,0);  
        
        ColumnConstraints columnsize = new ColumnConstraints();
        columnsize.setPercentWidth(100);
        RowConstraints rowsize = new RowConstraints();
        rowsize.setPercentHeight(100);
        
        slideGrid.getColumnConstraints().addAll(columnsize);
        slideGrid.getRowConstraints().addAll(rowsize);
        slideGrid.setAlignment(Pos.CENTER);
        
        slideGrid.widthProperty().addListener((obs, oldVal, newVal) -> changeMapSize(newVal,slideGrid.heightProperty().doubleValue(),slideGrid));
        slideGrid.heightProperty().addListener((obs, oldVal, newVal) -> changeMapSize(slideGrid.widthProperty().doubleValue(),newVal,slideGrid));
        return slideGrid;
    }  
    
    /**
     * To change the size of the map if needed (for example with fullscreen).
     */
    private void changeMapSize(Object newWidth, Object newHeigth, GridPane slideGrid){
        Integer newWidthMap = (int)Math.round((double) newWidth);
        Integer newHeigthMap = (int)Math.round((double) newHeigth);
        WebView londonMap = mapFunc.showBoroughMap("London",  newWidthMap-20, newHeigthMap-20);
        slideGrid.getChildren().clear();
        slideGrid.add(londonMap,0,0);  
    }
    
    /**
     * Prints the welcome message on the application window upon its load up.
     * Contains information for the user on how to use the GUI.
     * 
     * @return the welcoming grid pane
     */
    private GridPane getWelcome(){
        GridPane slideGrid = new GridPane(); 
         //Prints welcome message on the landing screen of the GUI with instructions for use.
        String welcomeText =" Welcome, \n  This Application has been created for the you to browse  "+
                                      "\n  through our selection of AirBnb listings. "+
                                      "\n  Here is a simple explanation of how to do so. "+
                                      "\n\n  Steps:"+
                                      "\n  1.  Select your price range in the top right corner."+
                                      "\n  2.  Select Left or Right int the bottom of the app "+
                                      "\n  3.  On Map select your desired borough. "+
                                      "\n  4.  Sort the results as you wish and choose which is"+
                                      "\n       best for you."+
                                      "\n\n  For more information please Direct Message "+
                                      "\n  @brandon_cardillo on INSTAGRAM.";
        
        //Styling for welcome message text and landing screen of GUI.                              
        Label welcomeLabel = new Label(welcomeText);
        BorderPane pane = new BorderPane();  
        pane.setCenter(welcomeLabel);
        pane.setStyle("-fx-background-color: rgba(255,255,255,0.75),linear-gradient(to bottom,#aaaaaa 0%,#cccccc 100%);"+
                           "-fx-background-insets: 0,1;"+
                           "-fx-padding: 7px;"+
                          " -fx-background-radius: 3px;"+
                          " -fx-border-radius: 3px;"+
                          " -fx-effect: innershadow(three-pass-box, rgba(0,0,0,0.75),1,0,1,1);"+
                          " -fx-font: 25px 'Aria';"+
                          " -fx-text-fill: black;");
        
        //Allignment and positioning of the elements of the panel                  
        slideGrid.add(pane,0,0);          
        ColumnConstraints columnsize = new ColumnConstraints();
        columnsize.setPercentWidth(100);
        RowConstraints rowsize = new RowConstraints();
        rowsize.setPercentHeight(100);
        
        slideGrid.getColumnConstraints().addAll(columnsize);
        slideGrid.getRowConstraints().addAll(rowsize);
        
        slideGrid.setAlignment(Pos.CENTER);
        return slideGrid;
    }

    /**
     * Method to show the statistics in the statistics panel.
     * 
     * @return grid pane showing the statistics
     */
    private GridPane getStatistics(){
        return statsPanel.showStatistics();
    }
    
    /**
     * Functionality of choice boxes in the GUI.
     * Prompts for valid lower and upper price limits, as well as the functionality
     * of changing the price range.
     */
    private void getChoiceBoxValues(Object newFromVal, Object newToVal, ChoiceBox from, ChoiceBox to, Button back, Button forward){
        Integer newFrom = (Integer) newFromVal;
        Integer newTo = (Integer) newToVal;
        
        //changed price range must not be null
        if (newFrom!=null && newTo!=null && newFrom>newTo){
            from.setValue(fromPrice);
            to.setValue(toPrice);
            
            Stage stage= new Stage();
            GridPane warning = new GridPane();
            warning.setStyle("-fx-background-color: #ccc;");
            
            //prompting for a valid price range
            Label warngingText = new Label("Please choose valid prices. \n You cannot have a higher Max price than Min price");
            warning.add(warngingText,0,0);
            warning.setAlignment(Pos.CENTER);
            
            //Error message if invalid price range selected
            Scene scene = new Scene(warning, 350, 100);
            stage.setTitle("Ivalid Price Input");
            stage.setScene(scene);
            stage.show();
        }
        fromPrice = newFrom;
        toPrice = newTo;
        
        if(fromPrice==null){fromPrice=0;}
        if(toPrice==null){toPrice=0;}
        mapPanel.setFromPrice(fromPrice);
        mapPanel.setToPrice(toPrice);
        statsPanel.setFromPrice(fromPrice);
        statsPanel.setToPrice(toPrice);
        try{
            statsPanel.boroughChoiceListener(null);
        }
        catch(NullPointerException e){
            System.out.println("No Borough Selected");
        }
        
        boolean fromBoxEmpty = from.getSelectionModel().isEmpty();
        boolean toBoxEmpty = to.getSelectionModel().isEmpty();
        if (!fromBoxEmpty && !toBoxEmpty){
            back.setDisable(false);
            forward.setDisable(false);
        }
    }
    

    /**
     * Changing active GUI panel using panel buttons.
     * 
     * @param panel buttons
     */
    private void switchScene(Button button, Button oppositeButton){
        button.setDisable(true);
        oppositeButton.setDisable(true);
        
        //Switches the scene to the next panel in the order
        String direction = button.getText();
        int nextSlideIndex = findNextIndex(slideOrder, findIndex(slideOrder, currentSlideName), direction );
        String nextSlideName = slideOrder[nextSlideIndex];
        
        //resets the GUI with the new, current panel.
        slidePane.getChildren().clear();
        slidePane.getChildren().addAll(slideAbr.get(nextSlideName),slideAbr.get(currentSlideName));
        
        double width = slidePane.getWidth();
        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(slideAbr.get(nextSlideName).translateXProperty(), width),
                new KeyValue(slideAbr.get(currentSlideName).translateXProperty(), 0));
        KeyFrame end = new KeyFrame(Duration.seconds(1),
                new KeyValue(slideAbr.get(nextSlideName).translateXProperty(), 0),
                new KeyValue(slideAbr.get(currentSlideName).translateXProperty(), -width));

        if(direction.equals("<")){
            start = new KeyFrame(Duration.ZERO,
                    new KeyValue(slideAbr.get(currentSlideName).translateXProperty(), 0),
                    new KeyValue(slideAbr.get(nextSlideName).translateXProperty(), -width));
            end = new KeyFrame(Duration.seconds(1),
                    new KeyValue(slideAbr.get(currentSlideName).translateXProperty(), width),
                    new KeyValue(slideAbr.get(nextSlideName).translateXProperty(), 0));
        }
        
        //Transition to next panel
        Timeline slide = new Timeline(start, end);
        slide.setOnFinished(e -> clearAndAdd(slideAbr.get(nextSlideName), button, oppositeButton));
        slide.play();
        
        currentSlideName = nextSlideName;
    }
    
    /**
     * Reset the GUI to display new, current panel.
     */
    private void clearAndAdd(GridPane grid,Button button, Button oppositeButton){
        button.setDisable(false);
        oppositeButton.setDisable(false);
        slidePane.getChildren().clear();
        slidePane.getChildren().add(grid);
    }
    
    /**
     * Finds index of the needed panel to maintain order.
     */
    private static int findIndex(String arr[], String searchWord) 
    { 
        int len = arr.length; 
        return IntStream.range(0, len) 
            .filter(i -> searchWord == arr[i]) 
            .findFirst() // first occurence 
            .orElse(-1); // No element found 
    } 
    
    /**
     * Finds the index of the successive panel.
     */
    private static int findNextIndex(String arr[], int currentIndex, String direction ){
        int len = arr.length; 
        int lastIndex = len-1;
        if (direction.equals(">")){
            if(currentIndex==lastIndex){
                return 0;
            }
        }
        else if(direction.equals("<")){
            if(currentIndex==0){
                return lastIndex;
            }
            else{
                return currentIndex-1;
            }
        }
        return currentIndex+1;
    }
}
