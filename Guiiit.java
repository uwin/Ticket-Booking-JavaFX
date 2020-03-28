/*
\need to flash when reClicked after booked
\
\check for duplicates when loading and saving
\ https://stackoverflow.com/questions/48238855/how-to-disable-past-dates-in-datepicker-of-javafx-scene-builder
\ https://stackoverflow.com/questions/29679971/javafx-make-a-grid-of-buttons/29719308
\ https://beginnersbook.com/2019/04/java-program-to-perform-bubble-sort-on-strings/
*/

//import com.mongodb.MongoClient;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.bson.Document;
//import org.bson.Document;
//import com.mongodb.client.*;
import java.time.LocalDate;
import java.util.*;
public class Guiiit extends Application {
    static final int SEATING_CAPACITY = 42;
    public static void main(String[] args) {
        launch();
    }
    static final List<LocalDate> dateColomboList = new ArrayList<>();
    static final List<LocalDate> dateBadullaList = new ArrayList<>();
    static final ArrayList<ArrayList<HashMap<String,String>>> dataColombo = new ArrayList<>();
    static final ArrayList<ArrayList<HashMap<String,String>>> dataBadulla = new ArrayList<>();
    public void start(Stage stage) {
        welcome();
    }
    public void welcome() {
        System.out.println("\nwelcome to ticket booking system \nA/C compartment for Denuwara Menike");
        List<String> temp = new ArrayList<>();
        HashMap<String, String> ColomboToBadulla = new HashMap<>();
        HashMap<String, String> BadullaToColombo = new HashMap<>();
        //opens the options menu
        listOption(temp, ColomboToBadulla, BadullaToColombo);
    }
    public void   listOption(List<String> temp, HashMap<String, String> ColomboToBadulla, HashMap<String, String> BadullaToColombo) {
        System.out.println("\n\n"+
                "A Add a seat\n"+
                "V View all seats\n"+
                "E View empty seats\n"+
                "D Delete seat\n"+
                "F Find the seat\n"+
                "S Save details\n"+
                "L Load details\n"+
                "O List seats\n"+
                "q quit\n");
        testOptions(temp, ColomboToBadulla, BadullaToColombo);
    }
    public void   testOptions(List<String> temp, HashMap<String, String> ColomboToBadulla, HashMap<String, String> BadullaToColombo){
//      create the stage
        Stage window = new Stage();
        window.setTitle("Train Booking System");
        GridPane gridFirst = new GridPane();
        gridFirst.setPadding(new Insets(2, 2, 2, 2));
        gridFirst.setHgap(10);
        gridFirst.setVgap(10);
        Scene addveiwFirst = new Scene(gridFirst, 1020, 400);
        window.setScene(addveiwFirst);

//      calling methods depending on the users input for the previous method
        Scanner scanOption= new Scanner(System.in);
        System.out.println(">> select a option");
        String userOption= scanOption.next().toUpperCase();
        switch (userOption) {
            case "A":            //gui related methods wil be further tested in this method for getting the route & date
            case "V":
            case "E":
                window.show();
                break;
            case "D":
                deleteOption(temp, ColomboToBadulla, BadullaToColombo);
                break;
            case "F":
                findOption(temp, ColomboToBadulla, BadullaToColombo);
                break;
            case "S":
                saveOption(temp, ColomboToBadulla, BadullaToColombo);
                break;
            case "L":
                loadOption(temp, ColomboToBadulla, BadullaToColombo);
                break;
            case "O":
                oderOption(temp, ColomboToBadulla, BadullaToColombo);
                break;
            case "Q":
                System.exit(0);
            default:
                System.out.println("invalid input");
                listOption(temp, ColomboToBadulla, BadullaToColombo);
                break;
        }
        //window.show();


//      main head for first window
        Label headFirst1 = new Label("Denuwara Menike Ticket Booking System\n                   Colombo-badulla");
        headFirst1.setFont(new Font("Arial", 30));
        headFirst1.setTextFill(Color.web("#0076a3")); //light blue
        gridFirst.add(headFirst1,20,3,50,8);

//      Secondary text
        Label headFirst2 = new Label("Enter Date");
        headFirst2.setFont(new Font("Arial", 23));
        headFirst2.setTextFill(Color.web("#0076a3")); //light blue
        gridFirst.add(headFirst2,3,12,9,4);

//      creating Gui element to select date
        DatePicker datePick = new DatePicker();
        //disabling past days on the date picker
        datePick.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) < 0 );
            }
        });
        datePick.setValue(LocalDate.now());
        gridFirst.add(datePick, 13, 12,18,4);

//      gui element to progress to Colombo to Badulla booking page
        Button fromColombo = new Button("From Colombo");
        fromColombo.setMaxSize(120, 60);
        fromColombo.setOnAction(event -> {
            int colomboBadullaVerify=1; //given 1 since it's the choosen value
            int badullaColomboVerify=0;
            LocalDate date=datePick.getValue();
            window.close();
            switch (userOption.toLowerCase()) {  //calling the Gui related functions
                case "a":
                    addOption(colomboBadullaVerify, badullaColomboVerify, date, temp, BadullaToColombo, BadullaToColombo);
                    break;
                case "v":
                    viewOption(colomboBadullaVerify, badullaColomboVerify, date, temp, BadullaToColombo, BadullaToColombo);
                    break;
                case "e":
                    emptyOption(colomboBadullaVerify, badullaColomboVerify, date, temp, ColomboToBadulla, BadullaToColombo);
                    break;
            }
        });
        gridFirst.add(fromColombo,35, 16,10,12);

//      gui element to progress to Badulla to Colombo booking page
        Button toColombo = new Button("From Badulla");
        toColombo.setMaxSize(120, 60);
        toColombo.setOnAction(event -> {
            int badullaColomboVerify=1; //given 1 since it's the choosen value
            int colomboBadullaVerify=0;
            LocalDate date=datePick.getValue();
            window.close();
            switch (userOption.toLowerCase()) {   //calling the Gui related functions
                case "a":
                    addOption(colomboBadullaVerify, badullaColomboVerify, date, temp, BadullaToColombo, BadullaToColombo);
                    break;
                case "v":
                    viewOption(colomboBadullaVerify, badullaColomboVerify, date, temp, BadullaToColombo, BadullaToColombo);
                    break;
                case "e":
                    emptyOption(colomboBadullaVerify, badullaColomboVerify, date, temp, ColomboToBadulla, BadullaToColombo);
                    break;
            }
        });
        gridFirst.add(toColombo,49, 16,10,12);

//      close button
        Button closeButFirst = new Button("close");
        closeButFirst.setMaxSize(120, 60);
        closeButFirst.setStyle("-fx-background-color: red; ");
        closeButFirst.setOnAction(event -> {
            window.close();
            listOption(temp, ColomboToBadulla, BadullaToColombo);
        });
        gridFirst.add(closeButFirst,80,30,10,12);
    }
    private void buttonAtion(List<LocalDate> dateBadullaList, ArrayList<ArrayList<HashMap<String, String>>> dataBadulla, LocalDate date, int badullaColomboVerify, TextField username, List<String> temp) {

        if(!dateBadullaList.contains(date))
        {
//            creating a temp hashmap to get name  from the text & seat numbers from temp list
            HashMap<String, String> TBadullaToColombo = new HashMap<>();
            for (String i : temp)
            {
                int indexforHash = temp.indexOf(i);
                if (badullaColomboVerify == 1) TBadullaToColombo.put(temp.get(indexforHash), username.getText().toLowerCase());
            }
//            adding the  date to the date list
            dateBadullaList.add(date);
//            adding seat numbers & names to the data structure
            dataBadulla.add(new ArrayList<>());
            int hashhashindex = dateBadullaList.size();
            hashhashindex -= 1;
            dataBadulla.get(hashhashindex).add(0, TBadullaToColombo);
        }
        else
            {
//                creating a temp hashmap to get name  from the text & seat numbers from temp list
                ArrayList<HashMap<String,String>> inti = dataBadulla.get(dateBadullaList.indexOf(date));
                HashMap<String,String> TBadullaToColombo = inti.get(0);
                for(String i: temp)
                {
                    int indexforHash = temp.indexOf(i);
                    TBadullaToColombo.put(temp.get(indexforHash),username.getText().toLowerCase());
                }
//                replacing the exisiting hashmap at data structure index relavent to the date with the temprary hashmap
                dataBadulla.get(dateBadullaList.indexOf(date)).clear();
                dataBadulla.get(dateBadullaList.indexOf(date)).add(0,TBadullaToColombo);
            }
    }
    private void seatcolourloop(ArrayList<ArrayList<HashMap<String, String>>> dataColombo, List<LocalDate> dateColomboList, ImageView button, LocalDate date, List<String> temp, HashMap<String, String> ColomboToBadulla) {
//        importing icons
        Image seatBlack = new Image(getClass().getResourceAsStream("images/black.png"));
        Image seatRed = new Image(getClass().getResourceAsStream("images/red.png"));
        Image seatGreen = new Image(getClass().getResourceAsStream("images/green.png"));

        if(dateColomboList.contains(date))
        {
//            creating a temporary hashmap for styling
            ArrayList<HashMap<String,String>> inti = dataColombo.get(dateColomboList.indexOf(date));
            HashMap<String,String> hash = inti.get(0);
            for(String i: hash.keySet()) ColomboToBadulla.put(i,hash.get(i));
        }
        else ColomboToBadulla.put("","");
//                      if seat is already booked change colour to red
        if (ColomboToBadulla.containsKey(button.getId())) button.setImage(seatRed);
//                      if seat is temporary booked change colour to red
        if (temp.contains(button.getId())) button.setImage(seatGreen);

        button.setOnMouseClicked(event -> {
//                           keep the seat colour red  if the user tries to click a already booked seat
            if (ColomboToBadulla.containsKey(button.getId()))
            {
                button.setImage(seatRed);
            }
//                          if the seat is not booked add the seat to the temporary seatList,change colour to green
            else if (!temp.contains(button.getId()))
            {
                button.setImage(seatGreen);
                temp.add(button.getId());
            }
//                          if the user again clicks a already booked seat, remove it from the temp booked list, revert colour
            else if (temp.contains(button.getId()))
            {
                temp.remove(button.getId());
                button.setImage(seatBlack);
            }
        });
    }
    public void    addOption(int colomboBadullaVerify, int badullaColomboVerify, LocalDate date, List<String> temp, HashMap<String, String> ColomboToBadulla, HashMap<String, String> BadullaToColombo){
//      create the stage
        Stage window = new Stage();
        window.setTitle("Train Booking System");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 2, 5, 2));
        grid.setHgap(10);
        grid.setVgap(10);
        Scene addView = new Scene(grid, 1020, 400);
        window.setScene(addView);
        window.show();

//      variables needed for seat icon loop
        Image seatBlack = new Image(getClass().getResourceAsStream("images/black.png"));
        int number = 1; //used to set the seat number

//      loop to create seat buttons & seat numbers
        for (int r = 2; r < 5; r++) {
            for (int c = 2; c < 16; c++) {
                if (number <=SEATING_CAPACITY)
                {
                    ImageView button = new ImageView(seatBlack);
                    Label num = new Label();
                    num.setFont(new Font("Arial", 15));
                    num.setText(String.valueOf(number));
                    button.setFitHeight(60);
                    button.setFitWidth(60);
                    button.setId(String.valueOf(number));

//                    calling the relavent method to style seat icons depending on the route
                    if(colomboBadullaVerify==1)
                    {
                        seatcolourloop(dataColombo,dateColomboList,button,date, temp, ColomboToBadulla);
                    }
                    else if(badullaColomboVerify==1)
                    {
                        seatcolourloop(dataBadulla,dateBadullaList,button,date, temp, ColomboToBadulla);
                    }
//                    adding images to grid
                    number++;
                    grid.add(button, c, r);
                    grid.add(num, c, r);
                }
            }
        }

//      window headFirst
        Label head = new Label("Select Seat");
        head.setFont(new Font("Arial", 30));
        head.setTextFill(Color.web("#0076a3")); //light blue
        grid.add(head,1,1,10,1);

//      space for user name
        TextField username = new TextField();
        username.setPromptText("enter name");
        grid.add(username, 8, 5, 10, 6);

//        Confirm button
        Button bookBut = new Button("Book");
        bookBut.setMaxSize(120, 60);
        bookBut.setStyle("-fx-background-color: green; ");
        bookBut.setOnAction(event -> {
//            alert will be shown if either the name or a seat is not selected
            if (username.getText().trim().isEmpty()||temp.isEmpty())
            {
                Alert a = new Alert(Alert.AlertType.WARNING);
                if (username.getText().trim().isEmpty())a.setHeaderText("enter a name");
                if (temp.isEmpty())a.setHeaderText("select seats");
                a.show();
                a.setOnCloseRequest(event1 ->
                {
                    window.close();
                    addOption(colomboBadullaVerify,badullaColomboVerify,date, temp, ColomboToBadulla, BadullaToColombo);
                });
            }
//            relavent method is called to handle the button actions
            else if(badullaColomboVerify==1)
            {
                buttonAtion(dateBadullaList,dataBadulla,date,badullaColomboVerify,username, temp);
                temp.clear();
                window.close();
                listOption(temp, ColomboToBadulla, BadullaToColombo);
            }
            else if (colomboBadullaVerify==1)
            {
                buttonAtion(dateColomboList, dataColombo, date, colomboBadullaVerify, username, temp);
                temp.clear();
                window.close();
                listOption(temp, ColomboToBadulla, BadullaToColombo);
            }
        });
        grid.add(bookBut, 10, 9,10,9);

//      Reset Button
        Button resetBut = new Button("Clear");
        resetBut.setMaxSize(120, 60);
        resetBut.setStyle("-fx-background-color: orange; ");
        resetBut.setOnAction(event -> {
            temp.clear();
            window.close();
            addOption(colomboBadullaVerify,badullaColomboVerify,date, temp, ColomboToBadulla, BadullaToColombo);

        });
        grid.add(resetBut, 12, 9,12,9);

//      close button
        Button closeBut = new Button("close");
        closeBut.setMaxSize(120, 60);
        closeBut.setStyle("-fx-background-color: red; ");
        closeBut.setOnAction(event -> {
            ColomboToBadulla.clear();
            BadullaToColombo.clear();
            temp.clear();
            window.close();
            listOption(temp, ColomboToBadulla, BadullaToColombo);
        });
        grid.add(closeBut, 14, 9,14,9);//      close button
    }
    public void   viewOption(int colomboBadullaVerify, int badullaColomboVerify, LocalDate date, List<String> temp, HashMap<String, String> ColomboToBadulla, HashMap<String, String> BadullaToColombo){
//      create the stage
        Stage window= new Stage();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 2, 5, 2));
        grid.setHgap(10);
        grid.setVgap(10);
        Scene viewSeat = new Scene(grid, 1020, 340);
        window.setTitle("Train Booking System");
        window.setScene(viewSeat);
        window.show();

//      values needed for the loop

//        impoting icons
        Image seatBlack = new Image(getClass().getResourceAsStream("images/black.png"));
        Image seatGrey = new Image(getClass().getResourceAsStream("images/grey.png"));

        int number = 1; //used to get value for seat number
//      loop to create seat buttons & seat numbers
        for (int r = 2; r < 5; r++) {
            for (int c = 2; c < 16; c++) {
                if (number <=SEATING_CAPACITY) {
                    ImageView button = new ImageView(seatBlack);
                    Label num = new Label();
                    num.setFont(new Font("Arial", 15));
                    num.setText(String.valueOf(number));
                    button.setFitHeight(60);
                    button.setFitWidth(60);

//                    styling done relavent to the route
                    if (colomboBadullaVerify==1)
                    {
                        if(dateColomboList.contains(date))
                        { //creating atemporary hashmap for styling
                            ArrayList<HashMap<String,String>> inti = dataColombo.get(dateColomboList.indexOf(date));
                            HashMap<String,String> hash = inti.get(0);
                            for(String i: hash.keySet()) ColomboToBadulla.put(i,hash.get(i));
                        }
                        else ColomboToBadulla.put("","");
//                     if seat is booked the seat button is greyed out
                        if (ColomboToBadulla.containsKey(String.valueOf(number))) button.setImage(seatGrey);
                    }
                    if (badullaColomboVerify==1)
                    {
                        if(dateBadullaList.contains(date))
                        { //creating a temporary hashmap for styling
                            ArrayList<HashMap<String,String>> inti = dataBadulla.get(dateBadullaList.indexOf(date));
                            HashMap<String,String> hash = inti.get(0);
                            for(String i: hash.keySet()) BadullaToColombo.put(i,hash.get(i));
                        }
                        else ColomboToBadulla.put("","");
                        //if seat is booked the seat button is greyed out
                        if (BadullaToColombo.containsKey(String.valueOf(number))) button.setImage(seatGrey);
                    }
                    number++;
                    grid.add(button, c, r);
                    grid.add(num, c, r);
                }
            }
        }

//      window head
        Label head = new Label("Viewing Seats ");
        head.setFont(new Font("Arial", 30));
        head.setTextFill(Color.web("#0076a3")); //light blue
        grid.add(head,1,1,10,1);

//      close button
        Button closeBut = new Button("Close");
        closeBut.setMaxSize(120, 60);
        closeBut.setStyle("-fx-background-color: red; ");
        closeBut.setOnAction(event -> {
            window.close();
            ColomboToBadulla.clear();
            BadullaToColombo.clear();
            listOption(temp, ColomboToBadulla, BadullaToColombo);
        });
        grid.add(closeBut,14,6,14,6);

    }
    public void  emptyOption(int colomboBadullaVerify, int badullaColomboVerify, LocalDate date, List<String> temp, HashMap<String, String> ColomboToBadulla, HashMap<String, String> BadullaToColombo){
//      create the stage
        Stage window= new Stage();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 2, 5, 2));
        grid.setHgap(10);
        grid.setVgap(10);
        Scene viewEmpty = new Scene(grid, 1020, 340);
        window.setTitle("Train Booking System");
        window.setScene(viewEmpty);
        window.show();

//      values needed for the loop
        int number = 1;
        Image seatBlack = new Image(getClass().getResourceAsStream("images/black.png"));

//      loop to create seat buttons
        for (int r = 2; r < 5; r++) {
            for (int c = 2; c < 16; c++) {
                if (number <=SEATING_CAPACITY) {
                    ImageView button = new ImageView(seatBlack);
                    Label num = new Label();
                    num.setFont(new Font("Arial", 15));
                    num.setText(String.valueOf(number));
                    button.setFitHeight(60);
                    button.setFitWidth(60);
//                  if the seat is booked nothing will be done

//                    styling will be done relavent to the route
                    if (colomboBadullaVerify==1){
                        if(dateColomboList.contains(date))
                        { //creating atemporary hashmap for styling
                            ArrayList<HashMap<String,String>> inti = dataColombo.get(dateColomboList.indexOf(date));
                            HashMap<String,String> hash = inti.get(0);
                            for(String i: hash.keySet())
                            {
                                ColomboToBadulla.put(i,hash.get(i));
                            }
                        }else ColomboToBadulla.put("","");
                        //                  if the seat is not booked the seat will be shown
                        if (!ColomboToBadulla.containsKey(String.valueOf(number)))
                        {
                            grid.add(button, c, r);
                            grid.add(num, c, r);
                        }
                    }
                    if (badullaColomboVerify==1){
                        if(dateBadullaList.contains(date))
                        {   //creating atemporary hashmap for styling
                            ArrayList<HashMap<String,String>> inti = dataBadulla.get(dateBadullaList.indexOf(date));
                            HashMap<String,String> hash = inti.get(0);
                            for(String i: hash.keySet())
                            {
                                BadullaToColombo.put(i,hash.get(i));
                            }
                        }else BadullaToColombo.put("","");
                        //                  if the seat is not booked the seat will be shown
                        if (!BadullaToColombo.containsKey(String.valueOf(number)))
                        {
                            grid.add(button, c, r);
                            grid.add(num, c, r);
                        }
                    }
                    number++;
                }
            }
        }

//      window head
        Label head = new Label("Empty Seats ");
        head.setFont(new Font("Arial", 30));
        head.setTextFill(Color.web("#0076a3")); //light blue
        grid.add(head,1,1,10,1);

//      close button
        Button closeBut = new Button("Close");
        closeBut.setMaxSize(120, 60);
        closeBut.setStyle("-fx-background-color: red; ");
        closeBut.setOnAction(event -> {
            ColomboToBadulla.clear();
            BadullaToColombo.clear();
            window.close();
            listOption(temp, ColomboToBadulla, BadullaToColombo);
        });
        grid.add(closeBut,14,6,14,6);
    }
    public void deleteOption(List<String> temp, HashMap<String, String> ColomboToBadulla, HashMap<String, String> BadullaToColombo){
//        getting user name
        Scanner scanDName = new Scanner(System.in);
        System.out.println("enter your name:");
        String deleteName= scanDName.next().toLowerCase();

        //checking if the name is vaid
        if (getCustomerNames().contains(deleteName))
        {
            //asking for the route
            System.out.println("1| Colombo to Badulla");
            System.out.println("2| Badulla to Colombo");

            Scanner scanRoute = new Scanner(System.in);
            System.out.println("enter Route ");
            String  route = scanRoute.next();

            if (route.equals("1")) {

                //getting seat number
                Scanner scanDeleteSeat= new Scanner(System.in);
                System.out.println("enter seat number ");
                String  deleteSeat = scanDeleteSeat.next();

                int count = 0;
                int deleteCount = 1;

//                creating temporarylist for deleting
                List<LocalDate> dateList = new ArrayList<>();
                List<String> seatList = new ArrayList<>();
//                looping dates
                for (LocalDate i : dateColomboList)
                {
                    for (String j : dataColombo.get(count).get(0).keySet())
                    {
                        if (j.equals(deleteSeat))
                        {
                            System.out.println(deleteCount + " | " + i);
                            dateList.add(i);   //adding vaid dates to temporary list
                            seatList.add(j);   //adding vaid seatnumbers to temporary list
                            deleteCount++;
                        }
                    }
                    count++;
                }
                if (!seatList.contains(deleteSeat)) //checking if the seat number was vaid
                {
                    System.out.println("seat number not booked");
                    deleteOption(temp, ColomboToBadulla, BadullaToColombo);
                }
                Scanner scFinal = new Scanner(System.in);
                System.out.println("remove date ");              //selecting date for deletion
                int Final = scFinal.nextInt();
                Final -= 1;
                int deleteIndex = dateColomboList.indexOf(dateList.get(Final));
                //System.out.println(dataColombo.get(deleteIndex).get(0));
                dataColombo.get(deleteIndex).get(0).remove(deleteSeat);
                System.out.println("Seat no:"+deleteSeat+" removed on :"+dateList.get(Final)); //deleting seat from the given date
                //System.out.println(dataColombo.get(deleteIndex).get(0));
            }
            else if(route.equals("2")){                           //same process is repeated
                Scanner scanDeleteSeat= new Scanner(System.in);
                System.out.println("enter seat number ");
                String  deleteSeat = scanDeleteSeat.next();
                int count = 0;
                int deleteCount = 1;
                List<LocalDate> dateList = new ArrayList<>();
                List<String> seatList = new ArrayList<>();
                for (LocalDate i : dateBadullaList)
                {
                    for (String j : dataBadulla.get(count).get(0).keySet())
                    {
                        if (j.equals(deleteSeat))
                        {
                            System.out.println(deleteCount + " | " + i);
                            dateList.add(i);
                            seatList.add(j);
                            deleteCount++;
                        }
                    }
                    count++;
                }
                if (!seatList.contains(deleteSeat))
                {
                    System.out.println("seat number not booked");
                    deleteOption(temp, ColomboToBadulla, BadullaToColombo);
                }
                Scanner scFinal = new Scanner(System.in);
                System.out.println("remove date ");
                int Final = scFinal.nextInt();
                Final -= 1;
                int deleteIndex = dateBadullaList.indexOf(dateList.get(Final));
                //System.out.println(dataColombo.get(deleteIndex).get(0));
                dataBadulla.get(deleteIndex).get(0).remove(deleteSeat);
                System.out.println("Seat no:"+deleteSeat+" removed on :"+dateList.get(Final));
                //System.out.println(dataColombo.get(deleteIndex).get(0));
            }
            else {
                System.out.println("invaied route");                      //if route is in vaid method is looped
                deleteOption(temp, ColomboToBadulla, BadullaToColombo);
            }
        } else {
            System.out.println("name is not booked");                    //if name is in vaied method is looped
            deleteOption(temp, ColomboToBadulla, BadullaToColombo);
        }
        waitOption(temp, ColomboToBadulla, BadullaToColombo);
    }
    public void   findOption(List<String> temp, HashMap<String, String> ColomboToBadulla, HashMap<String, String> BadullaToColombo){
//        getting user name
        Scanner scanFind = new Scanner(System.in);
        System.out.println("enter your name:");
        String findName= scanFind.next().toLowerCase();

        //List<String> allC = getCustomerNames();
        //validating name
        if (getCustomerNames().contains(findName)) {
            System.out.println("Name: " + findName + "\n");
            int count = 0;
//            looping for each day with the given name for the relavent route
            for (LocalDate i : dateColomboList)
            {
//                getting relavent details
                if (dataColombo.get(count).get(0).containsValue(findName))
                {
                    System.out.println("Route: Colombo to Badulla");
                    System.out.println("Date : " + i);
                    System.out.print("Seats: ");
                    for (String j : dataColombo.get(count).get(0).keySet())
                    {
                        if (dataColombo.get(count).get(0).get(j).equals(findName))
                        {
                            System.out.print(j + "|");
                        }
                    }
                    System.out.println("\n");
                }
                count++;
            }
            count = 0;
            for (LocalDate i : dateBadullaList) { //same prosess is repeated
                if (dataBadulla.get(count).get(0).containsValue(findName)) {
                    System.out.println("Route: Badulla to Colombo");
                    System.out.println("Date : " + i);
                    System.out.print("Seats: ");
                    for (String j : dataBadulla.get(count).get(0).keySet()) {
                        if (dataBadulla.get(count).get(0).get(j).equals(findName)) {
                            System.out.print(j + "|");
                        }
                    }
                    System.out.println("\n");
                }
                count++;
            }
        }else findOption(temp, ColomboToBadulla, BadullaToColombo); //if name is invaid method is looped
        waitOption(temp, ColomboToBadulla, BadullaToColombo);
    }
    public void   saveOption(List<String> temp, HashMap<String, String> ColomboToBadulla, HashMap<String, String> BadullaToColombo){
//        start mongo client
        com.mongodb.MongoClient dbclient = new MongoClient("localhost", 27017);
//        create database
        MongoDatabase dbDatabase = dbclient.getDatabase("users");
//        create collection for relavent route
        MongoCollection<Document> colombocollection = dbDatabase.getCollection("ColomboData");
        System.out.println("connected to Colombodetails");
        FindIterable<Document> colomboDocument = colombocollection.find();

//if the database is empty
//create document
        if(colombocollection.countDocuments()==0)
        {
            for(LocalDate colombodate: dateColomboList)
            {
                for(String item: dataColombo.get(dateColomboList.indexOf(colombodate)).get(0).keySet())
                {
                    Document userdocument = new Document();
                    userdocument.append("Seat number",item);
                    userdocument.append("user name",dataColombo.get(dateColomboList.indexOf(colombodate)).get(0).get(item));
                    userdocument.append("date",colombodate.toString()); //LocalDate.parse("2019-03-29");
                    userdocument.append("from","Colombo");
                    userdocument.append("to","Badulla");
                    colombocollection.insertOne(userdocument);
                }
            }
            System.out.println("stored data for user names & seats");
//if a database is not empty
// dete the existing file & replace it
        }else if(colombocollection.countDocuments()>1)
        {
            for(Document document: colomboDocument)
            {
                colombocollection.deleteOne(document);
            }
            for(LocalDate colombodate: dateColomboList)
            {
                for(String item: dataColombo.get(dateColomboList.indexOf(colombodate)).get(0).keySet())
                {
                    Document userdocument = new Document();
                    userdocument.append("Seat number",item);
                    userdocument.append("user name",dataColombo.get(dateColomboList.indexOf(colombodate)).get(0).get(item));
                    userdocument.append("date",colombodate.toString()); //LocalDate.parse("2019-03-29");
                    userdocument.append("from","Colombo");
                    userdocument.append("to","Badulla");
                    colombocollection.insertOne(userdocument);
                }
            }
            System.out.println("restored data for user names & seats");
        }

//        create collection for relavent route
        MongoCollection<Document> badullacollection = dbDatabase.getCollection("BadullaData");
        System.out.println("connected to badulladetails");
        FindIterable<Document> badullaDocument = badullacollection.find();
//if the database is empty
//create document

        if(badullacollection.countDocuments()==0)
        {
            for(LocalDate badulladate: dateBadullaList)
            {
                for(String item: dataBadulla.get(dateBadullaList.indexOf(badulladate)).get(0).keySet())
                {
                    Document userdocument = new Document();
                    userdocument.append("Seat number",item);
                    userdocument.append("user name",dataBadulla.get(dateBadullaList.indexOf(badulladate)).get(0).get(item));
                    userdocument.append("date",badulladate.toString()); //LocalDate.parse("2019-03-29");
                    userdocument.append("from","Badulla");
                    userdocument.append("to","Colombo");
                    badullacollection.insertOne(userdocument);
                }
            }
            System.out.println("stored data for user names & seats");
//if a database is not empty
// dete the existing file & replace it
        }else if(badullacollection.countDocuments()>1)
        {
            for(Document document: badullaDocument)
            {
                badullacollection.deleteOne(document);
            }
            for(LocalDate badulladate: dateBadullaList)
            {
                for(String item: dataBadulla.get(dateBadullaList.indexOf(badulladate)).get(0).keySet())
                {
                    Document userdocument = new Document();
                    userdocument.append("Seat number",item);
                    userdocument.append("user name",dataBadulla.get(dateBadullaList.indexOf(badulladate)).get(0).get(item));
                    userdocument.append("date",badulladate.toString()); //LocalDate.parse("2019-03-29");
                    userdocument.append("from","Badulla");
                    userdocument.append("to","Colombo");
                    badullacollection.insertOne(userdocument);
                }
            }
            System.out.println("restored data for user names & seats");
        }
//close client
        dbclient.close();
        System.out.println("saved files");
        waitOption(temp, ColomboToBadulla, BadullaToColombo);
    }
    public void   loadOption(List<String> temp, HashMap<String, String> ColomboToBadulla, HashMap<String, String> BadullaToColombo){
        System.out.println("dateColomboList"+dateColomboList);
        dateColomboList.clear();
        System.out.println("dateColomboList"+dateColomboList);
        System.out.println("dataColombo"+dataColombo);
        dataColombo.clear();
        System.out.println("dataColombo"+dataColombo);
        HashMap<String,String> temphashmap = new HashMap<>();
        com.mongodb.MongoClient dbclient = new MongoClient("localhost", 27017);
        MongoDatabase dbDatabase = dbclient.getDatabase("users");

        MongoCollection<Document> colombocollection = dbDatabase.getCollection("ColomboData");
        System.out.println("connected to Colombodetails");
        FindIterable<Document> colomboDocument = colombocollection.find();
        for(Document document:colomboDocument)
        {
            if (!dateColomboList.contains(LocalDate.parse(document.getString("date"))))
            {
                dateColomboList.add(LocalDate.parse(document.getString("date")));
                temphashmap.put(document.getString("Seat number"),document.getString("user name"));
                //dataColombo.add(new ArrayList<>());
                //dataColombo.get(dateColomboList.indexOf(LocalDate.parse(document.getString("date")))).add(0,temphashmap);
            }
        }
        System.out.println("dateColomboList"+dateColomboList);
        System.out.println("dataColombo"+dataColombo);
        waitOption(temp, ColomboToBadulla, BadullaToColombo);
    }
    public void   oderOption(List<String> temp, HashMap<String, String> ColomboToBadulla, HashMap<String, String> BadullaToColombo){
        String sortTemp;
//        create temporary arrays
        List<String> seatList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();

//        transfer hashhmap arrays to 2 arraylists
        for(LocalDate i :dateColomboList){
            for (String j : dataColombo.get(dateColomboList.indexOf(i)).get(0).keySet()) {
                seatList.add(j);
                nameList.add(dataColombo.get(dateColomboList.indexOf(i)).get(0).get(j));
            }
        }
        for(LocalDate i :dateBadullaList){
            for (String j : dataBadulla.get(dateBadullaList.indexOf(i)).get(0).keySet()) {
                seatList.add(j);
                nameList.add(dataBadulla.get(dateBadullaList.indexOf(i)).get(0).get(j));
            }
        }

//        creating the buble sort
        for (int j = 0; j < nameList.size(); j++) {
            for (int i = j + 1; i < nameList.size(); i++) {
                if (String.valueOf(nameList.get(i)).compareTo(String.valueOf(nameList.get(j))) < 0) {
                    sortTemp = String.valueOf(nameList.get(j));
                    nameList.set(j, nameList.get(i));
                    nameList.set(i, sortTemp);
                }
            }
            System.out.println(seatList.get(j)+": "+nameList.get(j));
        }
        waitOption(temp, ColomboToBadulla, BadullaToColombo);
    }
    public void   waitOption(List<String> temp, HashMap<String, String> ColomboToBadulla, HashMap<String, String> BadullaToColombo){
//        to let the use consume the details of console functions before moving to the menu
        Scanner scanContinue = new Scanner(System.in);
        System.out.println("Press any key to continue");
        String continueConsole=scanContinue.next();
        if (!continueConsole.isEmpty()) listOption(temp, ColomboToBadulla, BadullaToColombo);
    }
    public List<String>   getCustomerNames(){
        List<String> nameList = new ArrayList<>();
        for(LocalDate i :dateColomboList){
            for (String j : dataColombo.get(dateColomboList.indexOf(i)).get(0).keySet()) {
                nameList.add(dataColombo.get(dateColomboList.indexOf(i)).get(0).get(j));
            }
        }
        for(LocalDate i :dateBadullaList){
            for (String j : dataBadulla.get(dateBadullaList.indexOf(i)).get(0).keySet()) {
                nameList.add(dataBadulla.get(dateBadullaList.indexOf(i)).get(0).get(j));
            }
        }
        return nameList;
    }
}
