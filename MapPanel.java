import javafx.scene.paint.Color;
import javafx.scene.input.*;
import javafx.scene.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.shape.Rectangle;

/**
 * Write a description of class MapPanel here.
 *
 * @author Brandon Cardillo, Daniel Idowu
 * @version 17.03.2019
 */
public class MapPanel
{
    // instance variables - replace the example below with your own
    private int fromPrice;
    private int toPrice;
        
    // Range of Key for Borough Map
    private int mapKey[][]={{1,100},{101,250},{251,500},{501,1000},{1001,2500},{2501,5000},{5001,10000}};
    
    // Initial rgb (The ligthest rgb colour)
    private int r = 255;
    private int g = 0;
    private int b = 0;
    
    // Colour corespoinding to each range
    private String mapColour[]={r+","+g+","+b, r*6/7+","+g*6/7+","+b*6/7, r*5/7+","+g*5/7+","+b*5/7, 
        r*4/7+","+g*4/7+","+b*4/7, r*3/7+","+g*3/7+","+b*3/7, r*2/7+","+g*2/7+","+b*2/7, r*1/7+","+g*1/7+","+b*1/7};

    // Array That will hold the number of properties in each Borough
    private String[][] sizesOfBoroughs;
    
    // List of the abriations for the Borough Map
    private String boroughNameAbr[][] = {{"BARN", "Barnet"},{"ENFI", "Enfield"},{"HRGY", "Haringey"},{"WALT", "Waltham Forest"},
         {"HRRW", "Harrow"},{"BREN", "Brent"},{"CAMD", "Camden"},{"ISLI", "Islington"},{"HACK", "Hackney"},{"REDB", "Redbridge"},
         {"HAVE", "Havering"},{"HILL", "Hillingdon"},{"EALI", "Ealing"},{"KENS", "Kensington and Chelsea"},{"WSTM", "Westminster"},
         {"TOWH", "Tower Hamlets"},{"NEWH", "Newham"},{"BARK", "Barking and Dagenham"},{"HOUN", "Hounslow"},
         {"HAMM", "Hammersmith and Fulham"},{"WAND", "Wandsworth"},{"CITY", "City of London"},{"GWCH", "Greenwich"},{"BEXL", "Bexley"},
         {"RICH", "Richmond Upon Thames"},{"MERT", "Merton"},{"LAMB", "Lambeth"},{"STHW", "Southwark"},
         {"LEWS", "Lewisham"},{"KING", "Kingston Upon Thames"},{"SUTT", "Sutton"},{"CROY", "Croydon"},{"BROM", "Bromley"}};
    /**
     * Constructor for objects of class MapPanel
     */
    public MapPanel(int fromPrice, int toPrice)
    {
        // initialise instance variables
        this.fromPrice = fromPrice;
        this.toPrice = toPrice;
    }
    
    /**
     * set from price
     */
    public void setFromPrice(int from){
        fromPrice = from;
    }
    
    /**
     * set to price
     */
    public void setToPrice(int to){
        toPrice = to;
    }
    
    /**
     * returns a gridpane with graphical map that is made of hexagons buttons
     * showing the boroughs available.
     */
    public GridPane getMap(){
         loadMapData();
         
         //hexagon shape svg path
         String svgPathShape = "M86.60254037844386 0L173.20508075688772 50L173.20508075688772 150L86.60254037844386 200L0 150L0 50Z";
         int columnSize = 50;
         int rowSize = 30;
         int width = (columnSize*2)-5;
         int height = (rowSize*4)-5;
         
         // create a grid that is 21 rows by 14 colums 
         GridPane boroughMap = new GridPane();
         for (int i = 0; i < 15; i++) {
             boroughMap.getColumnConstraints().add(new ColumnConstraints(columnSize)); // 0
            }
         for (int i = 0; i < 22; i++) {
             boroughMap.getRowConstraints().add(new RowConstraints(rowSize)); // 0
            }
            
         //create and place every single button in the correct grid
         Button enfiButton = new Button("ENFI");
         enfiButton.setStyle("-fx-background-color: rgb("+getMapColour(enfiButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         enfiButton.setMaxSize(width,height);
         enfiButton.setMinSize(width,height);
         boroughMap.add(enfiButton,7,0,2,4);
         enfiButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button barnButton = new Button("BARN");
         barnButton.setStyle("-fx-background-color: rgb("+getMapColour(barnButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         barnButton.setMaxSize(width,height);
         barnButton.setMinSize(width,height);
         boroughMap.add(barnButton,4,3,2,4);
         barnButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button hrgyButton = new Button("HRGY");
         hrgyButton.setStyle("-fx-background-color: rgb("+getMapColour(hrgyButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         hrgyButton.setMaxSize(width,height);
         hrgyButton.setMinSize(width,height);
         boroughMap.add(hrgyButton,6,3,2,4);
         hrgyButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button waltButton = new Button("WALT");
         waltButton.setStyle("-fx-background-color: rgb("+getMapColour(waltButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         waltButton.setMaxSize(width,height);
         waltButton.setMinSize(width,height);
         boroughMap.add(waltButton,8,3,2,4);
         waltButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button hrrwButton = new Button("HRRW");
         hrrwButton.setStyle("-fx-background-color: rgb("+getMapColour(hrrwButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         hrrwButton.setMaxSize(width,height);
         hrrwButton.setMinSize(width,height);
         boroughMap.add(hrrwButton,1,6,2,4);
         hrrwButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button brenButton = new Button("BREN");
         brenButton.setStyle("-fx-background-color: rgb("+getMapColour(brenButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         brenButton.setMaxSize(width,height);
         brenButton.setMinSize(width,height);
         boroughMap.add(brenButton,3,6,2,4);
         brenButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button camdButton = new Button("CAMD");
         camdButton.setStyle("-fx-background-color: rgb("+getMapColour(camdButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         camdButton.setMaxSize(width,height);
         camdButton.setMinSize(width,height);
         boroughMap.add(camdButton,5,6,2,4);
         camdButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button isliButton = new Button("ISLI");
         isliButton.setStyle("-fx-background-color: rgb("+getMapColour(isliButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         isliButton.setMaxSize(width,height);
         isliButton.setMinSize(width,height);
         boroughMap.add(isliButton,7,6,2,4);
         isliButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button hackButton = new Button("HACK");
         hackButton.setStyle("-fx-background-color: rgb("+getMapColour(hackButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         hackButton.setMaxSize(width,height);
         hackButton.setMinSize(width,height);
         boroughMap.add(hackButton,9,6,2,4);
         hackButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button redbButton = new Button("REDB");
         redbButton.setStyle("-fx-background-color: rgb("+getMapColour(redbButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         redbButton.setMaxSize(width,height);
         redbButton.setMinSize(width,height);
         boroughMap.add(redbButton,11,6,2,4);
         redbButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button haveButton = new Button("HAVE");
         haveButton.setStyle("-fx-background-color: rgb("+getMapColour(haveButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         haveButton.setMaxSize(width,height);
         haveButton.setMinSize(width,height);
         boroughMap.add(haveButton,13,6,2,4);
         haveButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button hillButton = new Button("HILL");
         hillButton.setStyle("-fx-background-color: rgb("+getMapColour(hillButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         hillButton.setMaxSize(width,height);
         hillButton.setMinSize(width,height);
         boroughMap.add(hillButton,0,9,2,4);
         hillButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button ealiButton = new Button("EALI");
         ealiButton.setStyle("-fx-background-color: rgb("+getMapColour(ealiButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         ealiButton.setMaxSize(width,height);
         ealiButton.setMinSize(width,height);
         boroughMap.add(ealiButton,2,9,2,4);
         ealiButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button kensButton = new Button("KENS");
         kensButton.setStyle("-fx-background-color: rgb("+getMapColour(kensButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         kensButton.setMaxSize(width,height);
         kensButton.setMinSize(width,height);
         boroughMap.add(kensButton,4,9,2,4);
         kensButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button wstmButton = new Button("WSTM");
         wstmButton.setStyle("-fx-background-color: rgb("+getMapColour(wstmButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         wstmButton.setMaxSize(width,height);
         wstmButton.setMinSize(width,height);
         boroughMap.add(wstmButton,6,9,2,4);
         wstmButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button towhButton = new Button("TOWH");
         towhButton.setStyle("-fx-background-color: rgb("+getMapColour(towhButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         towhButton.setMaxSize(width,height);
         towhButton.setMinSize(width,height);
         boroughMap.add(towhButton,8,9,2,4);
         towhButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button newhButton = new Button("NEWH");
         newhButton.setStyle("-fx-background-color: rgb("+getMapColour(newhButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         newhButton.setMaxSize(width,height);
         newhButton.setMinSize(width,height);
         boroughMap.add(newhButton,10,9,2,4);
         newhButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button barkButton = new Button("BARK");
         barkButton.setStyle("-fx-background-color: rgb("+getMapColour(barkButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         barkButton.setMaxSize(width,height);
         barkButton.setMinSize(width,height);
         boroughMap.add(barkButton,12,9,2,4);
         barkButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button hounButton = new Button("HOUN");
         hounButton.setStyle("-fx-background-color: rgb("+getMapColour(hounButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         hounButton.setMaxSize(width,height);
         hounButton.setMinSize(width,height);
         boroughMap.add(hounButton,1,12,2,4);
         hounButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button hammButton = new Button("HAMM");
         hammButton.setStyle("-fx-background-color: rgb("+getMapColour(hammButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         hammButton.setMaxSize(width,height);
         hammButton.setMinSize(width,height);
         boroughMap.add(hammButton,3,12,2,4);
         hammButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button wandButton = new Button("WAND");
         wandButton.setStyle("-fx-background-color: rgb("+getMapColour(wandButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         wandButton.setMaxSize(width,height);
         wandButton.setMinSize(width,height);
         boroughMap.add(wandButton,5,12,2,4);
         wandButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button cityButton = new Button("CITY");
         cityButton.setStyle("-fx-background-color: rgb("+getMapColour(cityButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         cityButton.setMaxSize(width,height);
         cityButton.setMinSize(width,height);
         boroughMap.add(cityButton,7,12,2,4);
         cityButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button gwchButton = new Button("GWCH");
         gwchButton.setStyle("-fx-background-color: rgb("+getMapColour(gwchButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         gwchButton.setMaxSize(width,height);
         gwchButton.setMinSize(width,height);
         boroughMap.add(gwchButton,9,12,2,4);
         gwchButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button bexlButton = new Button("BEXL");
         bexlButton.setStyle("-fx-background-color: rgb("+getMapColour(bexlButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         bexlButton.setMaxSize(width,height);
         bexlButton.setMinSize(width,height);
         boroughMap.add(bexlButton,11,12,2,4);
         bexlButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button richButton = new Button("RICH");
         richButton.setStyle("-fx-background-color: rgb("+getMapColour(richButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         richButton.setMaxSize(width,height);
         richButton.setMinSize(width,height);
         boroughMap.add(richButton,2,15,2,4);
         richButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button mertButton = new Button("MERT");
         mertButton.setStyle("-fx-background-color: rgb("+getMapColour(mertButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         mertButton.setMaxSize(width,height);
         mertButton.setMinSize(width,height);
         boroughMap.add(mertButton,4,15,2,4);
         mertButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button lambButton = new Button("LAMB");
         lambButton.setStyle("-fx-background-color: rgb("+getMapColour(lambButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         lambButton.setMaxSize(width,height);
         lambButton.setMinSize(width,height);
         boroughMap.add(lambButton,6,15,2,4);
         lambButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button sthwButton = new Button("STHW");
         sthwButton.setStyle("-fx-background-color: rgb("+getMapColour(sthwButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         sthwButton.setMaxSize(width,height);
         sthwButton.setMinSize(width,height);
         boroughMap.add(sthwButton,8,15,2,4);
         sthwButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button lewsButton = new Button("LEWS");
         lewsButton.setStyle("-fx-background-color: rgb("+getMapColour(lewsButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         lewsButton.setMaxSize(width,height);
         lewsButton.setMinSize(width,height);
         boroughMap.add(lewsButton,10,15,2,4);
         lewsButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button kingButton = new Button("KING");
         kingButton.setStyle("-fx-background-color: rgb("+getMapColour(kingButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         kingButton.setMaxSize(width,height);
         kingButton.setMinSize(width,height);
         boroughMap.add(kingButton,3,18,2,4);
         kingButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button suttButton = new Button("SUTT");
         suttButton.setStyle("-fx-background-color: rgb("+getMapColour(suttButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         suttButton.setMaxSize(width,height);
         suttButton.setMinSize(width,height);
         boroughMap.add(suttButton,5,18,2,4);
         suttButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button croyButton = new Button("CROY");
         croyButton.setStyle("-fx-background-color: rgb("+getMapColour(croyButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         croyButton.setMaxSize(width,height);
         croyButton.setMinSize(width,height);
         boroughMap.add(croyButton,7,18,2,4);
         croyButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         Button bromButton = new Button("BROM");
         bromButton.setStyle("-fx-background-color: rgb("+getMapColour(bromButton)+"); -fx-text-fill: white; -fx-shape: '"+svgPathShape+"';");
         bromButton.setMaxSize(width,height);
         bromButton.setMinSize(width,height);
         boroughMap.add(bromButton,9,18,2,4);
         bromButton.setOnMouseClicked(e -> actionMapButtonPressed(e));
         
         boroughMap.setAlignment(Pos.CENTER);
         
         int recHeight = 20;
         
         //Create a key that has the colours of the buttons and what they mean
         Label firstKeyLabel = new Label(mapKey[0][0]+" - "+mapKey[0][1]);
         firstKeyLabel.setMaxSize(100,recHeight);
         firstKeyLabel.setMinSize(100,recHeight);
         firstKeyLabel.setTextAlignment(TextAlignment.RIGHT);
         
         Label secondKeyLabel = new Label(mapKey[1][0]+" - "+mapKey[1][1]);
         secondKeyLabel.setMaxSize(100,recHeight);
         secondKeyLabel.setMinSize(100,recHeight);
         secondKeyLabel.setTextAlignment(TextAlignment.RIGHT);
         
         Label thirdKeyLabel = new Label(mapKey[2][0]+" - "+mapKey[2][1]);
         thirdKeyLabel.setMaxSize(100,recHeight);
         thirdKeyLabel.setMinSize(100,recHeight);
         thirdKeyLabel.setTextAlignment(TextAlignment.RIGHT);
         
         Label fourthKeyLabel = new Label(mapKey[3][0]+" - "+mapKey[3][1]);
         fourthKeyLabel.setMaxSize(100,recHeight);
         fourthKeyLabel.setMinSize(100,recHeight);
         fourthKeyLabel.setTextAlignment(TextAlignment.RIGHT);
         
         Label fifthKeyLabel = new Label(mapKey[4][0]+" - "+mapKey[4][1]);
         fifthKeyLabel.setMaxSize(100,recHeight);
         fifthKeyLabel.setMinSize(100,recHeight);
         fifthKeyLabel.setTextAlignment(TextAlignment.RIGHT);
         
         Label sixthKeyLabel = new Label(mapKey[5][0]+" - "+mapKey[5][1]);
         sixthKeyLabel.setMaxSize(100,recHeight);
         sixthKeyLabel.setMinSize(100,recHeight);
         sixthKeyLabel.setTextAlignment(TextAlignment.RIGHT);
         
         Label seventhKeyLabel = new Label(mapKey[6][0]+" - "+mapKey[6][1]);
         seventhKeyLabel.setMaxSize(100,recHeight);
         seventhKeyLabel.setMinSize(100,recHeight);
         seventhKeyLabel.setTextAlignment(TextAlignment.RIGHT);
         
         String[] values;
         
         Rectangle firstRectangle = new Rectangle();
         firstRectangle.setWidth(50);
         firstRectangle.setHeight(recHeight);
         values = mapColour[0].split(",");
         firstRectangle.setFill(Color.rgb(Integer.valueOf(values[0]),Integer.valueOf(values[1]),Integer.valueOf(values[2])));
         Rectangle secondRectangle = new Rectangle();
         secondRectangle.setWidth(50);
         secondRectangle.setHeight(recHeight);
         values = mapColour[1].split(",");
         secondRectangle.setFill(Color.rgb(Integer.valueOf(values[0]),Integer.valueOf(values[1]),Integer.valueOf(values[2])));
         Rectangle thirdRectangle = new Rectangle();
         thirdRectangle.setWidth(50);
         thirdRectangle.setHeight(recHeight);
         values = mapColour[2].split(",");
         thirdRectangle.setFill(Color.rgb(Integer.valueOf(values[0]),Integer.valueOf(values[1]),Integer.valueOf(values[2])));
         Rectangle fourthRectangle = new Rectangle();
         fourthRectangle.setWidth(50);
         fourthRectangle.setHeight(recHeight);
         values = mapColour[3].split(",");
         fourthRectangle.setFill(Color.rgb(Integer.valueOf(values[0]),Integer.valueOf(values[1]),Integer.valueOf(values[2])));
         Rectangle fifthRectangle = new Rectangle();
         fifthRectangle.setWidth(50);
         fifthRectangle.setHeight(recHeight);
         values = mapColour[4].split(",");
         fifthRectangle.setFill(Color.rgb(Integer.valueOf(values[0]),Integer.valueOf(values[1]),Integer.valueOf(values[2])));
         Rectangle sixthRectangle = new Rectangle();
         sixthRectangle.setWidth(50);
         sixthRectangle.setHeight(recHeight);
         values = mapColour[5].split(",");
         sixthRectangle.setFill(Color.rgb(Integer.valueOf(values[0]),Integer.valueOf(values[1]),Integer.valueOf(values[2])));
         Rectangle seventhRectangle = new Rectangle();
         seventhRectangle.setWidth(50);
         seventhRectangle.setHeight(recHeight);
         values = mapColour[6].split(",");
         seventhRectangle.setFill(Color.rgb(Integer.valueOf(values[0]),Integer.valueOf(values[1]),Integer.valueOf(values[2])));
         
         VBox labelBox = new VBox(firstKeyLabel,secondKeyLabel,thirdKeyLabel,fourthKeyLabel,fifthKeyLabel,sixthKeyLabel,seventhKeyLabel);
         VBox colourBox = new VBox(firstRectangle,secondRectangle,thirdRectangle,fourthRectangle,fifthRectangle,sixthRectangle,seventhRectangle);
         colourBox.setSpacing(5);
         labelBox.setSpacing(5);
         
         HBox keyBox = new HBox(colourBox,labelBox);
         keyBox.setStyle("-fx-border-width: 1; -fx-border-color: Black; -fx-border-insets: 5;");
         boroughMap.add(keyBox,11,0,3,6);

         return boroughMap;
    }
    
    /**
     * Method that gets how many propeties are in the borough
     * (needed so that we know how dark to shade buttons)
     */
    private String[][] getBoroughSize(){
        String[][] boroughSizes = new String[boroughNameAbr.length][2];
        for(int i=0; i<boroughNameAbr.length; i++){
            int size = new PropertyListWindow(0, 50000, boroughNameAbr[i][1]).getFilteredListSize();
            boroughSizes[i][0] = boroughNameAbr[i][0];
            boroughSizes[i][1] = String.valueOf(size);
        }
        return boroughSizes;
    }
    
    /**
     * get the colour of the button depeding on the size of the borough
     */
    private String getMapColour(Button button){
        String bouroughNameSelected = button.getText();
        String colour = "";
        for(int i=0; i<sizesOfBoroughs.length; i++){
            if(sizesOfBoroughs[i][0].equals(bouroughNameSelected)){
                int size = Integer.parseInt(sizesOfBoroughs[i][1]);
                for(int j=0; j<mapKey.length; j++){
                    int lowerLimit = mapKey[j][0];
                    int upperLimit = mapKey[j][1];
                    if(size>=lowerLimit && size<=upperLimit){
                        colour = mapColour[j];
                    }
                }
            }
        }
        return colour;
    }
    
    /**
     * get all the data for the map
     */
    private void loadMapData() {
        sizesOfBoroughs = getBoroughSize();
    }
    
    /**
     * generalized method that takes the text in the button and 
     * compares it to the list of borough name abriviations 
     * then opens up the appropriate window
     */
    private void actionMapButtonPressed(MouseEvent event) {
         if(event.getButton().equals(MouseButton.PRIMARY)){
             if(event.getClickCount() == 2){
                 Button x = (Button) event.getSource();
                 String bouroughNameSelected = x.getText();
                 String searchBorough = "";
                 int i =0;
                 boolean found = false;
                 while(i<boroughNameAbr.length && found==false){
                    if (boroughNameAbr[i][0].equals(bouroughNameSelected)) {
                        searchBorough=boroughNameAbr[i][1];
                        found=true;
                    }
                    i++;
                 }
                 //show property list window of that borough
                 new PropertyListWindow(fromPrice, toPrice, searchBorough).show();
             }
         }
    }
}
