import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.bson.Document;

import java.time.LocalDate;
import java.util.*;

public class trainBooking extends Application {
    static final int SEATING_CAPACITY = 42;
    static final ArrayList<ArrayList<String>> booking = new ArrayList<>();
    public static void main(String[] args) {
        launch();
    }
    public void start(Stage stage) {
        welcome();
    }
    private void welcome() {
        System.out.println("\nwelcome to ticket booking system \n" +
                "A/C compartment for Denuwara Menike");
        listOption();
    }
    private void   listOption() {
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
        Scanner scanOption= new Scanner(System.in);
        System.out.println(">> select a option");
        String userOption= scanOption.next().toUpperCase();
        switch (userOption) {
//        calling methods depending on the users input for the previous method
//        gui methods will run though first gui
            case "A":
            case "V":
            case "E":
                firstGui(userOption);
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
    }
    private void  firstGui(String userOption){
//        create the stage
        Stage window = new Stage();
        window.setTitle("Train Booking System");
        GridPane First = new GridPane();
        First.setPadding(new Insets(2, 2, 2, 2));
        First.setHgap(10);
        First.setVgap(10);
        Scene addViewFirst = new Scene(First, 1020, 400);
        window.setScene(addViewFirst);
        window.show();

//        main text for first window
        Label head = new Label("Denuwara Menike Ticket Booking System\n" +
                "                   Colombo-Badulla");
        head.setFont(new Font("Arial", 30));
        head.setTextFill(Color.web("#0076a3")); //light blue
        First.add(head,20,3,50,8);

//        Secondary text for first window
        Label headDate = new Label("Date");
        headDate.setFont(new Font("Arial", 23));
        headDate.setTextFill(Color.web("#0076a3")); //light blue
        First.add(headDate,3,12,9,4);

//        creating Gui element to select date
        DatePicker datePick = new DatePicker();
//        disabling past days on the date picker
        datePick.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) < 0 );
            }
        });
        datePick.setValue(LocalDate.now());
        First.add(datePick, 12, 12,18,4);

//        text for start
        Label headStart = new Label("Start");
        headStart.setFont(new Font("Arial", 23));
        headStart.setTextFill(Color.web("#0076a3")); //light blue
        First.add(headStart,3,16,9,4);

        String[] stops = new String[]{ "Colombo Fort","Polgahawela",
                "Peradeniya Junction", "Gampola","Nawalapitiya",
                "Hatton","Thalawakele","Nanuoya", "Haputale","Diyatalawa",
                "Bandarawela","Ella", "Badulla" };
        ArrayList<String> stopsList = new ArrayList<>(Arrays.asList(stops));
        System.out.println(stopsList);

//        drop down menu for start
        ComboBox<String> startComboBox = new ComboBox<>();
        startComboBox.getItems().addAll(stops);
        startComboBox.setValue("Colombo Fort");
        First.add(startComboBox,12,16,9,4);

//        text for End
        Label headEnd = new Label("End");
        headEnd.setFont(new Font("Arial", 23));
        headEnd.setTextFill(Color.web("#0076a3")); //light blue
        First.add(headEnd,3,20,9,4);

//        drop down menu for End
        ComboBox<String> endComboBox = new ComboBox<>();
        endComboBox.getItems().addAll(stops);
        endComboBox.setValue("Badulla");
        First.add(endComboBox,12,20,9,4);


//        gui element to progress to booking page
        Button continueBut = new Button("Continue");
        continueBut.setMaxSize(120, 60);
        continueBut.setOnAction(event -> {
//        creating a temporarily array for seat numbers
            ArrayList <String>temporarySeat = new ArrayList<>();
//        creating a array to to temporarily store user data
            ArrayList<String> temporaryList = new ArrayList<>(5);
//        [ date , start , end , name , seatNo ]
            for (int i = 0; i < 6; i++) {
                temporaryList.add("0");
            }
//        adding the date, start location & end location
            String temporaryDate  = datePick.getValue().toString();
            temporaryList.set(0,temporaryDate);
            String temporaryStart = startComboBox.getValue();
            temporaryList.set(1,temporaryStart);
            String temporaryEnd   = endComboBox.getValue();
            temporaryList.set(2,temporaryEnd);

            window.close();
//        calling the Gui related functions
            switch (userOption.toLowerCase()) {
                case "a":
                    addOption(temporaryList, temporarySeat, stopsList);
                    break;
                case "v":
                    viewOption(temporaryList);
                    break;
                case "e":
                    emptyOption(temporaryList);
                    break;
            }
        });
        First.add(continueBut,70, 30,10,12);

//      close button
        Button closeButFirst = new Button("close");
        closeButFirst.setMaxSize(120, 60);
        closeButFirst.setStyle("-fx-background-color: red; ");
        closeButFirst.setOnAction(event -> {
            window.close();
            listOption();
        });
        First.add(closeButFirst,80,30,10,12);
    }
    private void    addOption(ArrayList<String> temporaryList, ArrayList<String> temporarySeat, ArrayList stopsList) {
//        create the stage
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
        Image seatRed = new Image(getClass().getResourceAsStream("images/red.png"));
        Image seatGreen = new Image(getClass().getResourceAsStream("images/green.png"));
        Image seatBlack = new Image(getClass().getResourceAsStream("images/black.png"));
//        used to set the seat number
        int number = 1;
//        creating a array of booked seats temporarily for styling
//        for bookings with same date,from,to as temporary booking
        ArrayList <String>bookedSeat = new ArrayList<>();

        int startingStop= stopsList.indexOf(temporaryList.get(1));
        System.out.println("index of start"+startingStop);
        int endingStop= stopsList.indexOf(temporaryList.get(2));
        System.out.println("index of end"+endingStop);



        while (startingStop<endingStop)
        {
            int checkStop = startingStop+1;
            while (checkStop<=endingStop)
            {
                String temporaryStart= (String) stopsList.get(startingStop);
                System.out.println("temp start"+temporaryStart);
                String temporaryEnd= (String) stopsList.get(checkStop);
                System.out.println("temp end"+temporaryEnd);
                for (ArrayList<String> strings : booking)
                {
                    if
                    (
                            strings.get(0).equals(temporaryList.get(0))&&
                            strings.get(1).equals(temporaryStart) &&
                            strings.get(2).equals(temporaryEnd)
                    )
                    {

                        bookedSeat.add(strings.get(4));
                    }
                }
                System.out.println("booking"+bookedSeat);
                checkStop++;
            }
            startingStop++;
        }

        System.out.println("///////////////");

        int beforeStop= stopsList.indexOf(temporaryList.get(1))-1;
        int afterStop=  stopsList.indexOf(temporaryList.get(2))-1;
        int firstStop=0;
        int lastStop=12;

        while (firstStop<beforeStop)
        {
            int checkStop = afterStop +1;
            while (checkStop<=lastStop)
            {
                String temporaryStart= (String) stopsList.get(firstStop);
                System.out.println("pre start"+temporaryStart);
                String temporaryEnd= (String) stopsList.get(checkStop);
                System.out.println("pre end"+temporaryEnd);

                for (ArrayList<String> strings : booking)
                {
                    if
                    (
                            strings.get(0).equals(temporaryList.get(0))&&
                                    strings.get(1).equals(temporaryStart) &&
                                    strings.get(2).equals(temporaryEnd)
                    )
                    {

                        bookedSeat.add(strings.get(4));
                    }
                }
                System.out.println("booking"+bookedSeat);
                checkStop++;
            }
            firstStop++;
        }



//        loop to create seat buttons & seat numbers
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
//        if the current seat id is already booked change to red
                    if (bookedSeat.contains(button.getId())) button.setImage(seatRed);
//        if the current seat id is temporarily booked change to green
                    if (temporarySeat.contains(button.getId())) button.setImage(seatGreen);

                    button.setOnMouseClicked(event ->
                    {
//        selected seat is already booked change to red
                        if (bookedSeat.contains(button.getId())) button.setImage(seatRed);
//        selected seat is not temporarily booked change to green & append
                        else if (!temporarySeat.contains(button.getId()))
                        {
                            button.setImage(seatGreen);
                            temporarySeat.add(button.getId());
                        }
//        selected seat is temporarily booked change to black & remove
                        else if (temporarySeat.contains(button.getId()))
                        {
                            button.setImage(seatBlack);
                            temporarySeat.remove(button.getId());
                        }
                    });
                    number++;
                    grid.add(button, c, r);
                    grid.add(num, c, r);
                }
            }
        }

//        main text
        Label head = new Label("Select Seat");
        head.setFont(new Font("Arial", 30));
        head.setTextFill(Color.web("#0076a3")); //light blue
        grid.add(head,1,1,10,1);

//        field for user name
        TextField username = new TextField();
        username.setPromptText("enter Name");
        grid.add(username, 2, 5, 6, 6);

//        field for user id
        TextField userId = new TextField();
        userId.setPromptText("enter NIC");
        grid.add(userId, 2, 9, 6, 6);

//        Confirm button
        Button bookBut = new Button("Book");
        bookBut.setMaxSize(120, 60);
        bookBut.setStyle("-fx-background-color: green; ");
        bookBut.setOnAction(event -> {
//        alert will be shown if name,seat or Nic is not entered
//        then addOption method will rerun
            if (username.getText().trim().isEmpty()||temporarySeat.isEmpty()||userId.getText().trim().isEmpty())
            {
                Alert a = new Alert(Alert.AlertType.WARNING);
                if (username.getText().trim().isEmpty())a.setHeaderText("enter a name");
                if (temporarySeat.isEmpty())a.setHeaderText("select seats");
                if (userId.getText().trim().isEmpty())a.setHeaderText("enter Nic");
                a.show();
                a.setOnCloseRequest(event1 ->
                {
                    window.close();
                    addOption(temporaryList, temporarySeat, stopsList);
                });
            }else
            {
//        else username and Nic will be added to temporary list
                temporaryList.set(3,username.getText());
                temporaryList.set(5,userId.getText());
//        each seat number will be added to the temporary list
//        each temporary list will be added to booking list
                for(String seat: temporarySeat)
                {
                    temporaryList.set(4,seat);
                    booking.add(new ArrayList<>(temporaryList));
                }
                temporarySeat.clear();
                window.close();
                listOption();
            }
        });
        grid.add(bookBut, 10, 9,10,9);

//        Reset Button
        Button resetBut = new Button("Clear");
        resetBut.setMaxSize(120, 60);
        resetBut.setStyle("-fx-background-color: orange; ");
        resetBut.setOnAction(event -> {
            temporarySeat.clear();
            window.close();
            addOption(temporaryList, temporarySeat, stopsList);
        });
        grid.add(resetBut, 12, 9,12,9);

//        close button
        Button closeBut = new Button("close");
        closeBut.setMaxSize(120, 60);
        closeBut.setStyle("-fx-background-color: red; ");
        closeBut.setOnAction(event -> {
            temporarySeat.clear();
            window.close();
            listOption();
        });
        grid.add(closeBut, 14, 9,14,9);

    }
    private void   viewOption(ArrayList<String> temporaryList) {
//        create the stage
        Stage window= new Stage();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 2, 5, 2));
        grid.setHgap(10);
        grid.setVgap(10);
        Scene viewSeat = new Scene(grid, 1020, 340);
        window.setTitle("Train Booking System");
        window.setScene(viewSeat);
        window.show();

//      variables needed for seat icon loop
        Image seatBlack = new Image(getClass().getResourceAsStream("images/black.png"));
        Image seatGrey = new Image(getClass().getResourceAsStream("images/grey.png"));
        //used to get value for seat number
        int number = 1;

//        creating a array of booked seats temporarily for styling
//        for bookings with same date,from,to as temporary booking
        ArrayList <String>bookedSeat = new ArrayList<>();
        for (ArrayList<String> strings : booking)
        {
            if (    strings.get(0).equals(temporaryList.get(0)) &&
                    strings.get(1).equals(temporaryList.get(1)) &&
                    strings.get(2).equals(temporaryList.get(2)))
            {
                bookedSeat.add(strings.get(4));
            }
        }
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

//        if the selected seat is already booked change to grey
                    if (bookedSeat.contains(num.getText())) button.setImage(seatGrey);
                    number++;
                    grid.add(button, c, r);
                    grid.add(num, c, r);
                }
            }
        }
//        main text
        Label head = new Label("Viewing Seats ");
        head.setFont(new Font("Arial", 30));
        head.setTextFill(Color.web("#0076a3")); //light blue
        grid.add(head,1,1,10,1);

//        close button
        Button closeBut = new Button("Close");
        closeBut.setMaxSize(120, 60);
        closeBut.setStyle("-fx-background-color: red; ");
        closeBut.setOnAction(event -> {
            window.close();
            listOption();
        });
        grid.add(closeBut,14,6,14,6);
    }
    private void  emptyOption(ArrayList<String> temporaryList) {
//        create the stage
        Stage window= new Stage();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 2, 5, 2));
        grid.setHgap(10);
        grid.setVgap(10);
        Scene viewEmpty = new Scene(grid, 1020, 340);
        window.setTitle("Train Booking System");
        window.setScene(viewEmpty);
        window.show();

//        values needed for the loop
        int number = 1;
        Image seatBlack = new Image(getClass().getResourceAsStream("images/black.png"));

//        creating a array of booked seats temporarily for styling
//        for bookings with same date,from,to as temporary booking
        ArrayList <String>bookedSeat = new ArrayList<>();
        for (ArrayList<String> strings : booking)
        {
            if (    strings.get(0).equals(temporaryList.get(0)) &&
                    strings.get(1).equals(temporaryList.get(1)) &&
                    strings.get(2).equals(temporaryList.get(2)))
            {
                bookedSeat.add(strings.get(4));
            }
        }

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

//        if the selected seat is not booked the seat icon is shown
                    if (!bookedSeat.contains(num.getText())) {
                        button.setImage(seatBlack);
                        grid.add(button, c, r);
                        grid.add(num, c, r);
                    }
                    number++;
                }
            }
        }
//        main text
        Label head = new Label("Empty Seats ");
        head.setFont(new Font("Arial", 30));
        head.setTextFill(Color.web("#0076a3")); //light blue
        grid.add(head,1,1,10,1);

//        close button
        Button closeBut = new Button("Close");
        closeBut.setMaxSize(120, 60);
        closeBut.setStyle("-fx-background-color: red; ");
        closeBut.setOnAction(event -> {
            window.close();
            listOption();
        });
        grid.add(closeBut,14,6,14,6);
    }
    private void deleteOption() {
        Scanner scanDDate = new Scanner(System.in);
        System.out.println("enter Date: [yyyy-mm-dd]");
        String deleteDate= scanDDate.next().toLowerCase();
        ArrayList <String>dateList = new ArrayList<>();
        for (ArrayList<String> data : booking) dateList.add(data.get(0));

        Scanner scanDNic = new Scanner(System.in);
        System.out.println("enter your Nic: ");
        String deleteNic= scanDNic.next().toLowerCase();
        ArrayList <String>nicList = new ArrayList<>();
        for (ArrayList<String> data : booking) nicList.add(data.get(5));

        ArrayList <Integer>deleteIndex = new ArrayList<>();
        if (nicList.contains(deleteNic) && dateList.contains(deleteDate))
        {
            for(ArrayList<String> data : booking){
                if(data.get(0).equals(deleteDate)&&data.get(5).equals(deleteNic))
                {
                    deleteIndex.add(booking.indexOf(data));
                }
            }
            Collections.reverse(deleteIndex);
            System.out.println("removed data");
            for(int i : deleteIndex)
            {
                System.out.println(booking.get(i));
                booking.remove(i);
            }
        }
        else if(deleteNic.toLowerCase().equals("q")) waitOption();
        else if(!nicList.contains(deleteNic))
        {
            System.out.println("Nic is not in records");
            deleteOption();
        }
        else if(!dateList.contains(deleteDate))
        {
            System.out.println("Date is not in records");
            deleteOption();
        }
        waitOption();
    }
    private void   findOption() {
        Scanner scanFNic = new Scanner(System.in);
        System.out.println("enter your Nic: ");
        String findNic= scanFNic.next().toLowerCase();

        ArrayList <String>nicList = new ArrayList<>();
        for (ArrayList<String> data : booking) nicList.add(data.get(5));

        if (nicList.contains(findNic))
        {
            for (ArrayList<String> data : booking)
            {
                if (data.get(5).equals(findNic))
                {
                    System.out.println(
                            "\nName: " + data.get(3) +
                            "\nDate: " + data.get(0) +
                            "\nFrom: " + data.get(1) +
                            "\nTo: "   + data.get(2) +
                            "\nSeat: " + data.get(4) );
                }
            }
            waitOption();
        }
        else if (findNic.toLowerCase().equals("q"))
        {
            waitOption();
        }
        else
            {
                System.out.println("Nic not in records");
                findOption();
            }
    }
    private void   saveOption() {
        com.mongodb.MongoClient dbClient = new MongoClient("localhost", 27017);
        MongoDatabase dbDatabase = dbClient.getDatabase("trainBookingSystem");
        MongoCollection<Document> bookings = dbDatabase.getCollection("BookingData");
        System.out.println("connected to BookingData");
        FindIterable<Document> bookingDocument = bookings.find();

        if(bookings.countDocuments()>1) for(Document document: bookingDocument) bookings.deleteOne(document);

        for (ArrayList<String> data : booking)
        {
            Document userDocument = new Document();
            userDocument.append("Date", data.get(0));
            userDocument.append("Start",data.get(1));
            userDocument.append("End",  data.get(2));
            userDocument.append("User", data.get(3));
            userDocument.append("Seat", data.get(4));
            userDocument.append("Nic",  data.get(5));
            bookings.insertOne(userDocument);
        }
        dbClient.close();
        System.out.println("saved files");
        waitOption();
    }
    private void   loadOption() {
        com.mongodb.MongoClient dbClient = new MongoClient("localhost", 27017);
        MongoDatabase dbDatabase = dbClient.getDatabase("trainBookingSystem");
        MongoCollection<Document> bookings = dbDatabase.getCollection("BookingData");
        System.out.println("connected to BookingData");
        FindIterable<Document> bookingDocument = bookings.find();

        ArrayList<String> temporaryList = new ArrayList<>(5);
        for (int i = 0; i < 6; i++) temporaryList.add("0");
        booking.clear();
        for(Document document:bookingDocument)
        {
            temporaryList.set(0,document.getString("Date"));
            temporaryList.set(1,document.getString("Start"));
            temporaryList.set(2,document.getString("End"));
            temporaryList.set(3,document.getString("User"));
            temporaryList.set(4,document.getString("Seat"));
            temporaryList.set(5,document.getString("Nic"));
            booking.add(new ArrayList<>(temporaryList));
        }
        System.out.println(booking);
        dbClient.close();
        System.out.println("files loaded");
        waitOption();
    }
    private void   oderOption() {
        String sortTemp;
        List<String> seatList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<String> nicList = new ArrayList<>();
        for (ArrayList<String> data : booking)
        {
            nameList.add(data.get(3));
            seatList.add(data.get(4));
            nicList.add(data.get(5));
        }
        for (int j = 0; j < nameList.size(); j++) {
            for (int i = j + 1; i < nameList.size(); i++) {
                if (String.valueOf(nameList.get(i)).compareTo(String.valueOf(nameList.get(j))) < 0) {
                    sortTemp = String.valueOf(nameList.get(j));
                    nameList.set(j, nameList.get(i));
                    nameList.set(i, sortTemp);
                }
            }
            System.out.println(seatList.get(j)+": "+nameList.get(j)+" ["+nicList.get(j)+"]");
        }
        waitOption();
    }
    private void   waitOption() {
//        to let the use consume the details of console functions before moving to the menu
        Scanner scanContinue = new Scanner(System.in);
        System.out.println("Press any key to continue");
        String continueConsole=scanContinue.next();
        if (!continueConsole.isEmpty()) listOption();
    }
}