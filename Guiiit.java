/*
\need to flash when reClicked after booked
\
\check for duplicates when loading and saving
\ https://stackoverflow.com/questions/48238855/how-to-disable-past-dates-in-datepicker-of-javafx-scene-builder
\ https://stackoverflow.com/questions/29679971/javafx-make-a-grid-of-buttons/29719308
\ https://beginnersbook.com/2019/04/java-program-to-perform-bubble-sort-on-strings/
*/

import com.mongodb.MongoClient;
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
import com.mongodb.client.*;
import java.time.LocalDate;
import java.util.*;

public class Guiiit extends Application {
    static final int SEATING_CAPACITY = 42;
    public static void main(String[] args) {
        launch();
    }

    HashMap<String, String> ColomboToBudulla = new HashMap<>();
    HashMap<String, String> BudullaToColombo = new HashMap<>();

    List<String> temp = new ArrayList<>();

    List<LocalDate> dateC2B = new ArrayList<>();
    List<LocalDate> dateB2C = new ArrayList<>();
    ArrayList<ArrayList<HashMap<String,String>>> hashC2B = new ArrayList<>();
    ArrayList<ArrayList<HashMap<String,String>>> hashB2C = new ArrayList<>();

    public void start(Stage stage) {
        welcome();
    }

    /**
     * this method is used to initiate the  ticket booking system with a message
     */
    public void welcome() {
        System.out.println("\nwelcome to ticket booking system \nA/C compartment for Denuwara Menike");
        //opens the options menu
        listOption();
    }

    /**
     * this method is used to show the user options to interact with the system
     */
    public void   listOption() {
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
        testOptions();
    }

    /**
     * this method is used to handle the options selected by the user & to call the
     * respective method, if the option is view, add or empty, a gui will be shown for
     * the user to select the  route & the date
     *
     */
    public void   testOptions(){
//      create the stage
        Stage window = new Stage();
        window.setTitle("Train Booking System");
        GridPane gridFirst = new GridPane();
        gridFirst.setPadding(new Insets(2, 2, 2, 2));
        gridFirst.setHgap(10);
        gridFirst.setVgap(10);
        Scene addveiwFirst = new Scene(gridFirst, 1020, 400);
        window.setScene(addveiwFirst);

//      calling methods depending on the users input
        Scanner scanOption= new Scanner(System.in);
        System.out.println(">> select a option");
        String userOption= scanOption.next().toUpperCase();
        switch (userOption) {
            case "A":
            case "V":
            case "E":
                window.show();
                break;
            case "D":
                deleteOption();
                break;
            case "F":
                findOption();
                break;
            case "S":
                saveOption();
                break;
            case "L":
                loadOption();
                break;
            case "O":
                oderOption();
                break;
            case "Q":
                System.exit(0);
            default:
                System.out.println("invalid input");
                listOption();
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
            int colomboBadullaVerify=1;
            int badullaColomboVerify=0;
            LocalDate date=datePick.getValue();
            window.close();
            switch (userOption.toLowerCase()) {
                case "a":
                    addOption(colomboBadullaVerify, badullaColomboVerify, date);
                    break;
                case "v":
                    viewOption(colomboBadullaVerify, badullaColomboVerify, date);
                    break;
                case "e":
                    emptyOption(colomboBadullaVerify, badullaColomboVerify, date);
                    break;
            }
        });
        gridFirst.add(fromColombo,35, 16,10,12);

//      gui element to progress to Badulla to Colombo booking page
        Button toColombo = new Button("From Badulla");
        toColombo.setMaxSize(120, 60);
        toColombo.setOnAction(event -> {
            int badullaColomboVerify=1;
            int colomboBadullaVerify=0;
            LocalDate date=datePick.getValue();
            window.close();
            switch (userOption.toLowerCase()) {
                case "a":
                    addOption(colomboBadullaVerify, badullaColomboVerify, date);
                    break;
                case "v":
                    viewOption(colomboBadullaVerify, badullaColomboVerify, date);
                    break;
                case "e":
                    emptyOption(colomboBadullaVerify, badullaColomboVerify, date);
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
            listOption();
        });
        gridFirst.add(closeButFirst,80,30,10,12);
    }

    /**
     * this method is used perform the actions for seat icons
     * @param dateB2C passing date list to verify status of booking
     * @param hashB2C passing seat data list to verify status of booking
     * @param date   date related for the booking
     * @param badullaColomboVerify this parameter passes the route selected by the user
     * @param username
     */
    private void buttonAtion(List<LocalDate> dateB2C, ArrayList<ArrayList<HashMap<String, String>>> hashB2C,
                             LocalDate date, int badullaColomboVerify, TextField username) {
        if(!dateB2C.contains(date))
        {
            HashMap<String, String> TBudullaToColombo = new HashMap<>();
            for (String i : temp)
            {
                int indexforHash = temp.indexOf(i);
                //if(colomboBadullaVerify==1) ColomboToBudulla.put(temp.get(indexforHash),username.getText().toLowerCase());
                if (badullaColomboVerify == 1) TBudullaToColombo.put(temp.get(indexforHash), username.getText().toLowerCase());
            }
            dateB2C.add(date);
            //System.out.println("B>C" + dateB2C);
            //System.out.println("B>C" + TBudullaToColombo);
            hashB2C.add(new ArrayList<>());
            int hashhashindex = dateB2C.size();
            hashhashindex -= 1;
            hashB2C.get(hashhashindex).add(0, TBudullaToColombo);
            //System.out.println("first time"+hashB2C);
        }
        else
            {
                ArrayList<HashMap<String,String>> inti = hashB2C.get(dateB2C.indexOf(date));
                HashMap<String,String> TBudullaToColombo = inti.get(0);
                for(String i: temp)
                {
                    int indexforHash = temp.indexOf(i);
                    TBudullaToColombo.put(temp.get(indexforHash),username.getText().toLowerCase());
                }
                hashB2C.get(dateB2C.indexOf(date)).clear();
                hashB2C.get(dateB2C.indexOf(date)).add(0,TBudullaToColombo);
            }
    }

    /**
     * this method is used to colour changes for the add view
     * @param hashC2B passing seat data list to verify status of booking
     * @param dateC2B passing date list to verify status of booking
     * @param button passing the image which needs to be styled
     * @param date   date related for the booking
     */
    private void seatcolourloop(ArrayList<ArrayList<HashMap<String, String>>> hashC2B, List<LocalDate> dateC2B,
                                ImageView button, LocalDate date) {
        Image seatBlack = new Image(getClass().getResourceAsStream("images/black.png"));
        Image seatRed = new Image(getClass().getResourceAsStream("images/red.png"));
        Image seatGreen = new Image(getClass().getResourceAsStream("images/green.png"));
        if(dateC2B.contains(date))
        {
            ArrayList<HashMap<String,String>> inti = hashC2B.get(dateC2B.indexOf(date));
            HashMap<String,String> hash = inti.get(0);
            for(String i: hash.keySet()) ColomboToBudulla.put(i,hash.get(i));
        }
        else ColomboToBudulla.put("","");
//                      if seat is already booked change colour to red
        if (ColomboToBudulla.containsKey(button.getId())) button.setImage(seatRed);
//                      if seat is temporary booked change colour to red
        if (temp.contains(button.getId())) button.setImage(seatGreen);

        button.setOnMouseClicked(event -> {
//                          flash the seat colour if the user tries to click a already booked seat
            if (ColomboToBudulla.containsKey(button.getId()))
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

    /**
     * in  this method  42 seat icons will be created & style with the use of parameters & data structures
     * then give the user the ability to select & book seats on a preferred day
     * @param colomboBadullaVerify this parameter passes a 0 01 depending on users choice of route
     * @param badullaColomboVerify this parameter passes a 0 01 depending on users choice of route
     * @param date                 this parameter passes the date selected by the user
     */
    public void    addOption(int colomboBadullaVerify, int badullaColomboVerify,LocalDate date){
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
        int number = 1;
        Image seatBlack = new Image(getClass().getResourceAsStream("images/black.png"));
        Image seatRed = new Image(getClass().getResourceAsStream("images/red.png"));
        Image seatGreen = new Image(getClass().getResourceAsStream("images/green.png"));

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

//                    style seat icons depending on the route
                    if(colomboBadullaVerify==1)
                    {
                        seatcolourloop(hashC2B,dateC2B,button,date);
                    }
                    else if(badullaColomboVerify==1)
                    {
                        seatcolourloop(hashB2C,dateB2C,button,date);
                    }
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
            System.out.println("[ c ]"+hashC2B);
            System.out.println("[ c ]"+dateC2B);
            System.out.println("[ b ]"+hashB2C);
            System.out.println("[ b ]"+dateB2C);
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
                    addOption(colomboBadullaVerify,badullaColomboVerify,date);
                });
//                alert will be shown if the user name is already existing
            }
            else if(badullaColomboVerify==1)
            {
                buttonAtion(dateB2C,hashB2C,date,badullaColomboVerify,username);
            }
            else if (colomboBadullaVerify==1)
            {
                buttonAtion(dateC2B, hashC2B, date, colomboBadullaVerify, username);
            }
            System.out.println("[ c ]"+hashC2B);
            System.out.println("[ c ]"+dateC2B);
            System.out.println("[ b ]"+hashB2C);
            System.out.println("[ b ]"+dateB2C);
            temp.clear();
            window.close();
            listOption();
        });
        grid.add(bookBut, 10, 9,10,9);

//      Reset Button
        Button resetBut = new Button("Clear");
        resetBut.setMaxSize(120, 60);
        resetBut.setStyle("-fx-background-color: orange; ");
        resetBut.setOnAction(event -> {
            temp.clear();
            window.close();
            addOption(colomboBadullaVerify,badullaColomboVerify,date);

        });
        grid.add(resetBut, 12, 9,12,9);

//      close button
        Button closeBut = new Button("close");
        closeBut.setMaxSize(120, 60);
        closeBut.setStyle("-fx-background-color: red; ");
        closeBut.setOnAction(event -> {
            ColomboToBudulla.clear();
            BudullaToColombo.clear();
            temp.clear();
            window.close();
            listOption();
        });
        grid.add(closeBut, 14, 9,14,9);//      close button
    }

    /**
     *this method is used to show seats, booked seats will be styled
     * @param colomboBadullaVerify parameter is passed to show the selected route
     * @param badullaColomboVerify parameter is passed to show the selected route
     * @param date parameter is passed to show the bookings for the selected date
     */
    public void   viewOption(int colomboBadullaVerify, int badullaColomboVerify,LocalDate date){
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
        int number = 1;
        Image seatBlack = new Image(getClass().getResourceAsStream("images/black.png"));
        Image seatGrey = new Image(getClass().getResourceAsStream("images/grey.png"));

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
//                    if seat is booked the seat button is greyed out
                    if (colomboBadullaVerify==1)
                    {
                        if(dateC2B.contains(date))
                        {
                            ArrayList<HashMap<String,String>> inti = hashC2B.get(dateC2B.indexOf(date));
                            HashMap<String,String> hash = inti.get(0);
                            for(String i: hash.keySet()) ColomboToBudulla.put(i,hash.get(i));
                        }
                        else ColomboToBudulla.put("","");

                        if (ColomboToBudulla.containsKey(String.valueOf(number))) button.setImage(seatGrey);
                    }
                    if (badullaColomboVerify==1)
                    {
                        if(dateB2C.contains(date))
                        {
                            ArrayList<HashMap<String,String>> inti = hashB2C.get(dateB2C.indexOf(date));
                            HashMap<String,String> hash = inti.get(0);
                            for(String i: hash.keySet()) BudullaToColombo.put(i,hash.get(i));
                        }
                        else ColomboToBudulla.put("","");
                        if (BudullaToColombo.containsKey(String.valueOf(number))) button.setImage(seatGrey);
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
            ColomboToBudulla.clear();
            BudullaToColombo.clear();
            listOption();
        });
        grid.add(closeBut,14,6,14,6);

    }

    /**
     *this method is used to show empty seats, booked seats will not be shown
     * @param colomboBadullaVerify parameter is passed to show the selected route
     * @param badullaColomboVerify parameter is passed to show the selected route
     * @param date parameter is passed to show the bookings for the selected date
     */
    public void  emptyOption(int colomboBadullaVerify, int badullaColomboVerify,LocalDate date){
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
//                  if the seat is not booked the seat will be shown
                    if (colomboBadullaVerify==1){
                        if(dateC2B.contains(date))
                        {
                            ArrayList<HashMap<String,String>> inti = hashC2B.get(dateC2B.indexOf(date));
                            HashMap<String,String> hash = inti.get(0);
                            for(String i: hash.keySet())
                            {
                                ColomboToBudulla.put(i,hash.get(i));
                            }
                        }else ColomboToBudulla.put("","");
                        if (!ColomboToBudulla.containsKey(String.valueOf(number)))
                        {
                            grid.add(button, c, r);
                            grid.add(num, c, r);
                        }
                    }
                    if (badullaColomboVerify==1){
                        if(dateB2C.contains(date))
                        {
                            ArrayList<HashMap<String,String>> inti = hashB2C.get(dateB2C.indexOf(date));
                            HashMap<String,String> hash = inti.get(0);
                            for(String i: hash.keySet())
                            {
                                BudullaToColombo.put(i,hash.get(i));
                            }
                        }else BudullaToColombo.put("","");
                        if (!BudullaToColombo.containsKey(String.valueOf(number)))
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
            ColomboToBudulla.clear();
            BudullaToColombo.clear();
            window.close();
            listOption();
        });
        grid.add(closeBut,14,6,14,6);
    }

    /**
     * this method is used to find & remove user seat  bookings from a given day
     */
    public void deleteOption(){
        Scanner scanDName = new Scanner(System.in);
        System.out.println("enter your name:");
        String deleteName= scanDName.next().toLowerCase();

        if (getCustomerNames().contains(deleteName))
        {
            System.out.println("1| Colombo to Badulla");
            System.out.println("2| Badulla to Colombo");

            Scanner scanRoute = new Scanner(System.in);
            System.out.println("enter Route ");
            String  route = scanRoute.next();

            if (route.equals("1")) {
                Scanner scanDeleteSeat= new Scanner(System.in);
                System.out.println("enter seat number ");
                String  deleteSeat = scanDeleteSeat.next();
                int count = 0;
                int deleteCount = 1;
                List<LocalDate> dateList = new ArrayList<>();
                List<String> seatList = new ArrayList<>();
                for (LocalDate i : dateC2B)
                {
                    for (String j : hashC2B.get(count).get(0).keySet())
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
                    deleteOption();
                }
                Scanner scFinal = new Scanner(System.in);
                System.out.println("remove date ");
                int Final = scFinal.nextInt();
                Final -= 1;
                int deleteIndex = dateC2B.indexOf(dateList.get(Final));
                //System.out.println(hashC2B.get(deleteIndex).get(0));
                hashC2B.get(deleteIndex).get(0).remove(deleteSeat);
                System.out.println("Seat no:"+deleteSeat+" removed on :"+dateList.get(Final));
                //System.out.println(hashC2B.get(deleteIndex).get(0));
            }
            else if(route.equals("2")){
                Scanner scanDeleteSeat= new Scanner(System.in);
                System.out.println("enter seat number ");
                String  deleteSeat = scanDeleteSeat.next();
                int count = 0;
                int deleteCount = 1;
                List<LocalDate> dateList = new ArrayList<>();
                List<String> seatList = new ArrayList<>();
                for (LocalDate i : dateB2C)
                {
                    for (String j : hashB2C.get(count).get(0).keySet())
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
                    deleteOption();
                }
                Scanner scFinal = new Scanner(System.in);
                System.out.println("remove date ");
                int Final = scFinal.nextInt();
                Final -= 1;
                int deleteIndex = dateB2C.indexOf(dateList.get(Final));
                //System.out.println(hashC2B.get(deleteIndex).get(0));
                hashB2C.get(deleteIndex).get(0).remove(deleteSeat);
                System.out.println("Seat no:"+deleteSeat+" removed on :"+dateList.get(Final));
                //System.out.println(hashC2B.get(deleteIndex).get(0));
            }
            else {
                System.out.println("invaied route");
                deleteOption();
            }
        } else {
            System.out.println("name is not booked");
            deleteOption();
        }
        waitOption();
    }


    /**
     * this method is used a allow the user to find all bookings using user name
     */
    public void   findOption(){
//        getting user name
        Scanner scanFind = new Scanner(System.in);
        System.out.println("enter your name:");
        String findName= scanFind.next().toLowerCase();
        //List<String> allC = getCustomerNames();
        if (getCustomerNames().contains(findName)) {
            System.out.println("Name: " + findName + "\n");
            int count = 0;
            for (LocalDate i : dateC2B) {
                if (hashC2B.get(count).get(0).containsValue(findName)) {
                    System.out.println("Route: Colombo to Badulla");
                    System.out.println("Date : " + i);
                    System.out.print("Seats: ");
                    for (String j : hashC2B.get(count).get(0).keySet()) {
                        if (hashC2B.get(count).get(0).get(j).equals(findName)) {
                            System.out.print(j + "|");
                        }
                    }
                    System.out.println("\n");
                }
                count++;
            }
            count = 0;
            for (LocalDate i : dateB2C) {
                if (hashB2C.get(count).get(0).containsValue(findName)) {
                    System.out.println("Route: Badulla to Colombo");
                    System.out.println("Date : " + i);
                    System.out.print("Seats: ");
                    for (String j : hashB2C.get(count).get(0).keySet()) {
                        if (hashB2C.get(count).get(0).get(j).equals(findName)) {
                            System.out.print(j + "|");
                        }
                    }
                    System.out.println("\n");
                }
                count++;
            }
        }else findOption();
        waitOption();
    }

    /**
     * this method is used to save user data to a data base
     */
    public void   saveOption(){
        com.mongodb.MongoClient dbclient = new MongoClient("localhost", 27017);
        MongoDatabase dbDatabase = dbclient.getDatabase("users");


        MongoCollection<Document> colombocollection = dbDatabase.getCollection("ColomboData");
        System.out.println("connected to Colombodetails");
        FindIterable<Document> colomboDocument = colombocollection.find();

        if(colombocollection.countDocuments()==0)
        {
            for(LocalDate colombodate: dateC2B)
            {
                for(String item: hashC2B.get(dateC2B.indexOf(colombodate)).get(0).keySet())
                {
                    Document userdocument = new Document();
                    userdocument.append("Seat number",item);
                    userdocument.append("user name",hashC2B.get(dateC2B.indexOf(colombodate)).get(0).get(item));
                    userdocument.append("date",colombodate.toString()); //LocalDate.parse("2019-03-29");
                    userdocument.append("from","Colombo");
                    userdocument.append("to","Badulla");
                    colombocollection.insertOne(userdocument);
                }
            }
            System.out.println("stored data for user names & seats");
        }else if(colombocollection.countDocuments()>1)
        {
            for(Document document: colomboDocument)
            {
                colombocollection.deleteOne(document);
            }
            for(LocalDate colombodate: dateC2B)
            {
                for(String item: hashC2B.get(dateC2B.indexOf(colombodate)).get(0).keySet())
                {
                    Document userdocument = new Document();
                    userdocument.append("Seat number",item);
                    userdocument.append("user name",hashC2B.get(dateC2B.indexOf(colombodate)).get(0).get(item));
                    userdocument.append("date",colombodate.toString()); //LocalDate.parse("2019-03-29");
                    userdocument.append("from","Colombo");
                    userdocument.append("to","Badulla");
                    colombocollection.insertOne(userdocument);
                }
            }
            System.out.println("restored data for user names & seats");
        }

        MongoCollection<Document> badullacollection = dbDatabase.getCollection("BadullaData");
        System.out.println("connected to badulladetails");
        FindIterable<Document> badullaDocument = badullacollection.find();

        if(badullacollection.countDocuments()==0)
        {
            for(LocalDate badulladate: dateB2C)
            {
                for(String item: hashB2C.get(dateB2C.indexOf(badulladate)).get(0).keySet())
                {
                    Document userdocument = new Document();
                    userdocument.append("Seat number",item);
                    userdocument.append("user name",hashB2C.get(dateB2C.indexOf(badulladate)).get(0).get(item));
                    userdocument.append("date",badulladate.toString()); //LocalDate.parse("2019-03-29");
                    userdocument.append("from","Badulla");
                    userdocument.append("to","Colombo");
                    badullacollection.insertOne(userdocument);
                }
            }
            System.out.println("stored data for user names & seats");
        }else if(badullacollection.countDocuments()>1)
        {
            for(Document document: badullaDocument)
            {
                badullacollection.deleteOne(document);
            }
            for(LocalDate badulladate: dateB2C)
            {
                for(String item: hashB2C.get(dateB2C.indexOf(badulladate)).get(0).keySet())
                {
                    Document userdocument = new Document();
                    userdocument.append("Seat number",item);
                    userdocument.append("user name",hashB2C.get(dateB2C.indexOf(badulladate)).get(0).get(item));
                    userdocument.append("date",badulladate.toString()); //LocalDate.parse("2019-03-29");
                    userdocument.append("from","Badulla");
                    userdocument.append("to","Colombo");
                    badullacollection.insertOne(userdocument);
                }
            }
            System.out.println("restored data for user names & seats");
        }

        dbclient.close();
        System.out.println("saved files");
        waitOption();
    }

    /**
     * this method is used restore a saved user data set
     */
    public void   loadOption(){
        dateC2B.clear();
        hashC2B.clear();
        HashMap<String,String> temphashmap = new HashMap<>();

        com.mongodb.MongoClient dbclient = new MongoClient("localhost", 27017);
        MongoDatabase dbDatabase = dbclient.getDatabase("users");

        MongoCollection<Document> colombocollection = dbDatabase.getCollection("ColomboData");
        System.out.println("connected to Colombodetails");
        FindIterable<Document> colomboDocument = colombocollection.find();

        for(Document document:colomboDocument)
        {
            if (!dateC2B.contains(LocalDate.parse(document.getString("date"))))
            {
                dateC2B.add(LocalDate.parse(document.getString("date")));
                temphashmap.put(document.getString("Seat number"),document.getString("user name"));
                hashC2B.add(new ArrayList<>());
                hashC2B.get(dateC2B.indexOf(LocalDate.parse(document.getString("date")))).add(0,temphashmap);
            }
            else{
            }
        }

        dateB2C.clear();
        hashB2C.clear();
        temphashmap.clear();
        System.out.println(temphashmap);

        MongoCollection<Document> badullacollection = dbDatabase.getCollection("BadullaData");
        System.out.println("connected to Badulladetails");
        FindIterable<Document> badullaDocument = badullacollection.find();

        for(Document document:badullaDocument)
        {
            if (!dateB2C.contains(LocalDate.parse(document.getString("date"))))
            {
                dateB2C.add(LocalDate.parse(document.getString("date")));
                temphashmap.put(document.getString("Seat number"),document.getString("user name"));
                hashB2C.add(new ArrayList<>());
                hashB2C.get(dateB2C.indexOf(LocalDate.parse(document.getString("date")))).add(0,temphashmap);
            }
            else{
            }
        }

        System.out.println("datec2b"+dateC2B);
        System.out.println("hashc2b"+hashC2B);
        System.out.println("dateb2c"+dateB2C);
        System.out.println("hashb2c"+hashB2C);

        dbclient.close();
        waitOption();
    }

    /**
     * this method used to alphabetically oder all booked seats
     */
    public void   oderOption(){
        String sortTemp;
        List<String> seatList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        for(LocalDate i :dateC2B){
            for (String j : hashC2B.get(dateC2B.indexOf(i)).get(0).keySet()) {
                seatList.add(j);
                nameList.add(hashC2B.get(dateC2B.indexOf(i)).get(0).get(j));
            }
        }
        for(LocalDate i :dateB2C){
            for (String j : hashB2C.get(dateB2C.indexOf(i)).get(0).keySet()) {
                seatList.add(j);
                nameList.add(hashB2C.get(dateB2C.indexOf(i)).get(0).get(j));
            }
        }


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
        waitOption();
    }

    /**
     * this method is used to get user confirmation to proceed after
     * a console command is run
     */
    public void   waitOption(){
//        to let the use consume the details of console functions before moving to the menu
        Scanner scanContinue = new Scanner(System.in);
        System.out.println("Press any key to continue");
        String continueConsole=scanContinue.next();
        if (!continueConsole.isEmpty()) listOption();
    }

    public List<String>   getCustomerNames(){
        List<String> nameList = new ArrayList<>();
        for(LocalDate i :dateC2B){
            for (String j : hashC2B.get(dateC2B.indexOf(i)).get(0).keySet()) {
                nameList.add(hashC2B.get(dateC2B.indexOf(i)).get(0).get(j));
            }
        }
        for(LocalDate i :dateB2C){
            for (String j : hashB2C.get(dateB2C.indexOf(i)).get(0).keySet()) {
                nameList.add(hashB2C.get(dateB2C.indexOf(i)).get(0).get(j));
            }
        }
        return nameList;
    }
    public List<String>   getCustomerSeats(){
        List<String> seatList = new ArrayList<>();
        for(LocalDate i :dateC2B){
            for (String j : hashC2B.get(dateC2B.indexOf(i)).get(0).keySet()) {
                seatList.add(j);
            }
        }
        for(LocalDate i :dateB2C){
            for (String j : hashB2C.get(dateB2C.indexOf(i)).get(0).keySet()) {
                seatList.add(j);
            }
        }
        return seatList;
    }

}