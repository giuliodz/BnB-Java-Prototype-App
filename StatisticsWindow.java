import javafx.geometry.Pos ;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
* Write a description of JavaFX class StatisticsWindow here.
*
 * @author Brandon Cardillo, Giulio Di Zio
 * @version 17.03.2019
*/
public class StatisticsWindow
{
    // We keep track of the count, and label displaying the count:
    private Statistics stat;
    
    private int fromPrice;
    private int toPrice;
    
    private GridPane gPane;// root pane
    private ArrayList<BorderPane> statPanes;//An ArrayList that keeps all the borderPanes showing different statistics.
    
    private String boroughSelected = null;
    private List<String> boroughNames = Arrays.asList("Barnet","Enfield","Haringey","Waltham Forest","Harrow","Brent","Camden","Islington",
         "Redbridge","Havering","Hillingdon","Ealing","Kensington and Chelsea","Westminster","Tower Hamlets","Newham","Barking and Dagenham",
         "Hounslow","Hammersmith and Fulham","Wandsworth","City of London","Greenwich","Bexley","Richmond Upon Thames","Merton","Lambeth",
         "Southwark","Lewisham","Kingston Upon Thames","Sutton","Croydon","Bromley","Hackney");
    public StatisticsWindow()
    {
        // initialise instance variables
        Collections.sort(boroughNames);
    }

    public GridPane showStatistics(){
        stat = new Statistics();
        gPane = new GridPane();
        statPanes = new ArrayList<>();
        
        Label borughNameLabel = new Label("Neighbourhood: ");
        borughNameLabel.setWrapText(true);
        borughNameLabel.setTextAlignment(TextAlignment.CENTER);
        
        ChoiceBox boroughChoice = new ChoiceBox();
        boroughChoice.getItems().addAll(boroughNames);
        
        boroughChoice.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> boroughChoiceListener(newVal));
        
        HBox selectBoroughBox = new HBox(borughNameLabel,boroughChoice);
        selectBoroughBox.setAlignment(Pos.TOP_RIGHT);
        gPane.add(selectBoroughBox,1,0);
        
        String explainText ="Please select your desired neighbourhood."+
                            "\nStatistics boxes will show up contaning information you might be intrested in." ;
                                      
        Label explainLabel = new Label(explainText);
        explainLabel.setWrapText(true);
        explainLabel.setTextAlignment(TextAlignment.CENTER);
        explainLabel.setMinWidth(750);
        explainLabel.setMinHeight(450);
        explainLabel.setAlignment(Pos.CENTER);
        explainLabel.setStyle("-fx-background-color: rgba(255,255,255,0.75),linear-gradient(to bottom,#aaaaaa 0%,#cccccc 100%);"+
                           "-fx-background-insets: 0,1;"+
                           "-fx-padding: 7px;"+
                          " -fx-background-radius: 3px;"+
                          " -fx-border-radius: 3px;"+
                          " -fx-effect: innershadow(three-pass-box, rgba(0,0,0,0.75),1,0,1,1);"+
                          " -fx-font: 25px 'Aria';"+
                          " -fx-text-fill: black;");
                          
        gPane.add(explainLabel,0,1,2,1);  
        
        gPane.setAlignment(Pos.CENTER);
        return gPane;
    }

    public void setFromPrice(int from){
        fromPrice = from;
    }
    
    public void setToPrice(int to){
        toPrice = to;
    }
    
    public void boroughChoiceListener(Object newVal){
        if(newVal!=null){
            boroughSelected = newVal.toString();
        }
        gPane.getChildren().clear();
        gPane.getColumnConstraints().clear();
        gPane.getRowConstraints().clear();
        setRowsAndColumns(gPane);
        Label borughNameLabel = new Label("Neighbourhood: ");
        borughNameLabel.setWrapText(true);
        borughNameLabel.setTextAlignment(TextAlignment.CENTER);
        
        ChoiceBox boroughChoice = new ChoiceBox();
        boroughChoice.getItems().addAll(boroughNames);
        
        boroughChoice.setValue(boroughSelected);
        boroughChoice.getSelectionModel().selectedItemProperty().addListener((v, oldVal, thenewVal) -> boroughChoiceListener(thenewVal));
        
        
        HBox selectBoroughBox = new HBox(borughNameLabel,boroughChoice);
        gPane.add(selectBoroughBox,1,0);
        
        statPanes.clear();
        statPanes.add(createReviewsPorPropertyPanel(fromPrice, toPrice, boroughSelected));
        statPanes.add(createAvailablePropertiesPanel(fromPrice, toPrice, boroughSelected));
        statPanes.add( createEntireHomesPanel(fromPrice, toPrice, boroughSelected));
        statPanes.add(createMostExpensiveNeighbourhoodPanel());
        statPanes.add(createNeighbourhoodRankPanel(boroughSelected));
        statPanes.add(createAveragePricePanel(boroughSelected));
        statPanes.add(createFilteredPropertiesPanel(fromPrice, toPrice, boroughSelected));
        
        gPane.add(statPanes.get(0),0,1);
        gPane.add(statPanes.get(1),1,1);
        gPane.add(statPanes.get(2),0,2);
        gPane.add(statPanes.get(3),1,2);
    }
    
    /**
     * Set the constraints for the gridpane. It gives it a constraint of having max 2 columns and max 2 rows and each of These columns and rows should
     * take respectively the 50% of the width and the 50% of the hight of the whole grid pane (but it doesnt work...).
     * @param The root pane.
     */
    private void setRowsAndColumns(GridPane gPane)
    {
        ColumnConstraints columnsize = new ColumnConstraints();
        columnsize.setPercentWidth(100/2);
        RowConstraints rowsize1 = new RowConstraints();
        rowsize1.setPercentHeight(10);
        RowConstraints rowsize2 = new RowConstraints();
        rowsize2.setPercentHeight(45);
        
        gPane.getColumnConstraints().addAll(columnsize,columnsize);
        gPane.getRowConstraints().addAll(rowsize1,rowsize2,rowsize2);
    }
    
    
    /**
     * Create a panel showing the statistics of the number of reviews por property taking into account some filters.
     * @param Minimum price
     * @param Maximum price
     * @param Neighbourhood name
     * @return Statistic pane.
     */
    private BorderPane createReviewsPorPropertyPanel(Integer fromPrice, Integer toPrice, String neighbourhood)
    {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(12,12,12,12));
        
        Button leftArrow = new Button("<");
        Button rightArrow = new Button(">");
        leftArrow.setMaxHeight(Double.MAX_VALUE);
        rightArrow.setMaxHeight(Double.MAX_VALUE);
        leftArrow.setMinWidth(75);
        rightArrow.setMinWidth(75);
        
        Label statName = new Label("Average number of reviews per property:");
        statName.setWrapText(true);
        statName.setTextAlignment(TextAlignment.CENTER);
        
        int numberOfReviews = stat.reviewsForProperty(fromPrice, toPrice, neighbourhood);
        Label statResult = new Label(Integer.toString(numberOfReviews));
        
        Region spacer = new Region();
        spacer.setPrefHeight(50);
        
        VBox centerVbox = new VBox(statName, spacer, statResult);
        centerVbox.setAlignment(Pos.CENTER);
        
        pane.setCenter(centerVbox);
        pane.setLeft(leftArrow);
        pane.setRight(rightArrow);
        pane.setId("borderpane");
        
        leftArrow.setOnAction(event -> leftArrowAction(event, pane));
        rightArrow.setOnAction(event -> rightArrowAction(event, pane));
        
        return pane;
    }
    
    /**
     * Create a pane showing the statistics of the number of available properties taking into account some filters.
     * @param Minimum price
     * @param Maximum price
     * @param Neighbourhood name
     * @return Statistic pane.
     */
    private BorderPane createAvailablePropertiesPanel(int fromPrice, int toPrice, String neighbourhood)
    {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(12,12,12,12));
        
        Button leftArrow = new Button("<");
        Button rightArrow = new Button(">");
        leftArrow.setMaxHeight(Double.MAX_VALUE);
        rightArrow.setMaxHeight(Double.MAX_VALUE);
        leftArrow.setMinWidth(75);
        rightArrow.setMinWidth(75);
        
        Label statName = new Label("Number of available properties:");
        statName.setWrapText(true);
        
        statName.setTextAlignment(TextAlignment.CENTER);
        
        int numberOfProperties = stat.getNumberAvailableProperties(fromPrice, toPrice, neighbourhood);
        Label statResult = new Label(Integer.toString(numberOfProperties));
        
        Region spacer = new Region();
        spacer.setPrefHeight(50);
        
        VBox centerVbox = new VBox(statName, spacer, statResult);
        centerVbox.setAlignment(Pos.CENTER);
        
        pane.setCenter(centerVbox);
        pane.setLeft(leftArrow);
        pane.setRight(rightArrow);
        pane.setId("borderpane");
        
        leftArrow.setOnAction(event -> leftArrowAction(event, pane));
        rightArrow.setOnAction(event -> rightArrowAction(event, pane));
        return pane;
    }
    
    /**
     * Create a pane showing the statistics of the number of entire homes taking into account some filters.
     * @param Minimum price
     * @param Maximum price
     * @param Neighbourhood name
     * @return Statistic pane.
     */
    private BorderPane createEntireHomesPanel(int fromPrice, int toPrice, String neighbourhood)
    {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(12,12,12,12));
        
        Button leftArrow = new Button("<");
        Button rightArrow = new Button(">");
        leftArrow.setMaxHeight(Double.MAX_VALUE);
        rightArrow.setMaxHeight(Double.MAX_VALUE);
        leftArrow.setMinWidth(75);
        rightArrow.setMinWidth(75);
        
        Label statName = new Label("Number of entire homes:");
        statName.setWrapText(true);
        
        statName.setTextAlignment(TextAlignment.CENTER);
        
        int numberOfEntireHomes = stat.getNumberEntireHomes(fromPrice, toPrice, neighbourhood);
        Label statResult = new Label(Integer.toString(numberOfEntireHomes));
        
        Region spacer = new Region();
        spacer.setPrefHeight(50);
        
        VBox centerVbox = new VBox(statName, spacer, statResult);
        centerVbox.setAlignment(Pos.CENTER);
        
        pane.setCenter(centerVbox);
        pane.setLeft(leftArrow);
        pane.setRight(rightArrow);
        pane.setId("borderpane");
        
        leftArrow.setOnAction(event -> leftArrowAction(event, pane));
        rightArrow.setOnAction(event -> rightArrowAction(event, pane));
        return pane;
    }
    
    /**
     * Create a pane showing the name of the most expensive neighbourhood.
     * @return Statistic pane.
     */
    private BorderPane createMostExpensiveNeighbourhoodPanel()
    {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(12,12,12,12));
        
        Button leftArrow = new Button("<");
        Button rightArrow = new Button(">");
        leftArrow.setMaxHeight(Double.MAX_VALUE);
        rightArrow.setMaxHeight(Double.MAX_VALUE);
        leftArrow.setMinWidth(75);
        rightArrow.setMinWidth(75);
        
        Label statName = new Label("Most expensive neighbourhood:");
        statName.setWrapText(true);
        
        statName.setTextAlignment(TextAlignment.CENTER);
        
        stat.getMostExpensiveNeighbourhood();
        Label statResult = new Label(stat.getMostExpensiveNeighbourhood());
        
        Region spacer = new Region();
        spacer.setPrefHeight(50);
        
        VBox centerVbox = new VBox(statName, spacer, statResult);
        centerVbox.setAlignment(Pos.CENTER);
        
        pane.setCenter(centerVbox);
        pane.setLeft(leftArrow);
        pane.setRight(rightArrow);
        pane.setId("borderpane");
        
        leftArrow.setOnAction(event -> leftArrowAction(event, pane));
        rightArrow.setOnAction(event -> rightArrowAction(event, pane));
        return pane;
    }
    
    /**
     * Create a panel showing the rank-position of a neighbourhood in terms of expensiveness.
     * @param Neighbourhood name
     * @return Statistic pane.
     */
    private BorderPane createNeighbourhoodRankPanel(String neighbourhood) 
    {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(12,12,12,12));
        
        Button leftArrow = new Button("<");
        Button rightArrow = new Button(">");
        leftArrow.setMaxHeight(Double.MAX_VALUE);
        rightArrow.setMaxHeight(Double.MAX_VALUE);
        leftArrow.setMinWidth(75);
        rightArrow.setMinWidth(75);
        
        Label statName = new Label("Neighbourhood cost rank:");
        statName.setWrapText(true);
        
        statName.setTextAlignment(TextAlignment.JUSTIFY);
        
        stat.getMostExpensiveNeighbourhood();
        Label statResult = new Label(Integer.toString(stat.getNeighbourhoodRank(neighbourhood)));
        
        Region spacer = new Region();
        spacer.setPrefHeight(50);
        
        VBox centerVbox = new VBox(statName, spacer, statResult);
        centerVbox.setAlignment(Pos.CENTER);
        
        pane.setCenter(centerVbox);
        pane.setLeft(leftArrow);
        pane.setRight(rightArrow);
        pane.setId("borderpane");
        
        leftArrow.setOnAction(event -> leftArrowAction(event, pane));
        rightArrow.setOnAction(event -> rightArrowAction(event, pane));
        return pane;
    }
    
    /**
     * Create a panel showing the statistics of the average price por property in a given neighbourhood.
     * @param Neighbourhood name
     * @return Statistic pane.
     */
    private BorderPane createAveragePricePanel(String neighbourhood) 
    {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(12,12,12,12));
        
        Button leftArrow = new Button("<");
        Button rightArrow = new Button(">");
        leftArrow.setMaxHeight(Double.MAX_VALUE);
        rightArrow.setMaxHeight(Double.MAX_VALUE);
        leftArrow.setMinWidth(75);
        rightArrow.setMinWidth(75);
        
        Label statName = new Label("Average price for properties in this neighbourhood:");
        statName.setWrapText(true);
        statName.setTextAlignment(TextAlignment.CENTER);
        
        stat.getMostExpensiveNeighbourhood();
        Label statResult;
        if(neighbourhood != null){
            // Checking if the user has chosen a neighbourhood.
            statResult = new Label(Integer.toString(stat.getNeighbourhoodAveragePrice(neighbourhood)));
        }
        else{
            statResult = new Label("Select a neighbourood first.");
        }
        
        Region spacer = new Region();
        spacer.setPrefHeight(50);
        
        VBox centerVbox = new VBox(statName, spacer, statResult);
        centerVbox.setAlignment(Pos.CENTER);
        
        pane.setCenter(centerVbox);
        pane.setLeft(leftArrow);
        pane.setRight(rightArrow);
        pane.setId("borderpane");
        
        leftArrow.setOnAction(event -> leftArrowAction(event, pane));
        rightArrow.setOnAction(event -> rightArrowAction(event, pane));
        return pane;
    }
    
    /**
     * Create a pane showing the number of all the properties in the bnb dataset taking into account some filters.
     * @param Minimum price
     * @param Maximum price
     * @param Neighbourhood name
     * @return Statistic pane.
     */
    private BorderPane createFilteredPropertiesPanel(Integer fromPrice, Integer toPrice, String neighbourhood) 
    {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(12,12,12,12));
        
        Button leftArrow = new Button("<");
        Button rightArrow = new Button(">");
        leftArrow.setMaxHeight(Double.MAX_VALUE);
        rightArrow.setMaxHeight(Double.MAX_VALUE);
        leftArrow.setMinWidth(75);
        rightArrow.setMinWidth(75);
        
        Label statName = new Label("Number of properties that fit filters in this neighbourhood:");
        statName.setWrapText(true);
        
        statName.setTextAlignment(TextAlignment.CENTER);
        
        stat.getMostExpensiveNeighbourhood();
        Label statResult = new Label(Integer.toString(stat.getMatchingProperties(fromPrice, toPrice, neighbourhood)));
        
        Region spacer = new Region();
        spacer.setPrefHeight(50);
        
        VBox centerVbox = new VBox(statName, spacer, statResult);
        centerVbox.setAlignment(Pos.CENTER);
        
        pane.setCenter(centerVbox);
        pane.setLeft(leftArrow);
        pane.setRight(rightArrow);
        pane.setId("borderpane");
        
        leftArrow.setOnAction(event -> leftArrowAction(event, pane));
        rightArrow.setOnAction(event -> rightArrowAction(event, pane));
        return pane;
    }
    
    /**
     * It makes sure that clicking on the left-arrow button on a statistic pane makes change the statistic to show.
     * @param ActionEvent clicking on the button.
     * @param The pane of the statistic whose button has been pressed.
     */
    private void leftArrowAction(ActionEvent e, BorderPane pane) 
    {
            BorderPane paneToChange = new BorderPane();
            
            List panesInUse = ((GridPane) pane.getParent()).getChildren();
            List<BorderPane> usedPanes = (List) panesInUse;

            for(int paneIndex = statPanes.indexOf(pane); paneIndex<8; paneIndex--){
                // get the first pane on the list that has not been displayed on the gridPane yet.
                if (!usedPanes.contains(statPanes.get(paneIndex))){
                    paneToChange = statPanes.get(paneIndex);
                    break;
                }
                //making sure that it loops through the whole collection and not just one side.
                if(paneIndex == 0){
                    paneIndex = 8;
                }
            }
            
            int column = ((GridPane) pane.getParent()).getColumnIndex(pane);
            int row = ((GridPane) pane.getParent()).getRowIndex(pane); 
      
            ((GridPane) pane.getParent()).getChildren().add(paneToChange);
            ((GridPane) pane.getParent()).getChildren().remove(pane);
            ((GridPane) pane.getParent()).setColumnIndex(paneToChange, column);
            ((GridPane) pane.getParent()).setRowIndex(paneToChange, row);            

    }
    
    /**
     * It makes sure that clicking on the left-arrow button on a statistic pane makes change the statistic to show.
     * @param ActionEvent clicking on the button.
     * @param The pane of the statistic whose button has been pressed.
     */
    private void rightArrowAction(ActionEvent e, BorderPane pane) 
    {
            BorderPane paneToChange = new BorderPane();
            
            List panesInUse = ((GridPane) pane.getParent()).getChildren();
            List<BorderPane> usedPanes = (List) panesInUse;

            for(int paneIndex = statPanes.indexOf(pane); paneIndex<8; paneIndex++){
                // get the first pane on the list that has not been displayed on the gridPane yet.
                if (!usedPanes.contains(statPanes.get(paneIndex))){
                    paneToChange = statPanes.get(paneIndex);
                    break;
                }
                //making sure that it loops through the whole collection and not just one side.
                if(paneIndex == 7){
                    paneIndex = -1;
                }
            }
            
            int column = ((GridPane) pane.getParent()).getColumnIndex(pane);
            int row = ((GridPane) pane.getParent()).getRowIndex(pane); 
      
            ((GridPane) pane.getParent()).add(paneToChange,column,row);
            ((GridPane) pane.getParent()).getChildren().remove(pane);     

    }
}