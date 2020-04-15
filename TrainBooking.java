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

public class TrainBooking extends Application {
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

    /**
     * this method is used to print the menu options & to run the relevant
     * methods according to user input
     * the code will print a all options of the menu along with a corresponding
     * letter in front of it.
     *
     * the code will request a user input a letter using the given menu the user
     * if the methods are to be run on the console the code will run the
     * relevant methods directly using a switch case, if it's gui related the
     * firstGui method will be run by passing the user input to it
     */
    private void   listOption() {
        System.out.println("\n"+
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
        System.out.println("> select a option");
        String userOption= scanOption.next().toUpperCase();
        switch (userOption) {
//        calling methods depending on the users input form the previous method
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
                break;
            default:
                System.out.println("invalid input");
                waitOption();
                break;
        }
    }

    /**
     * this method is used to create a gui window that lets the user pick a date
     * using a datePicker & starting and ending stops using two drop down menus
     *
     * the date, starting stop & ending stop  values will be passed to the gui
     * related methods addOption, viewOption and emptyOption. these methods will
     * be run using a switch case with the use of userOption parameter
     * @param userOption this parameter is used to pass the user inputs for Gui
     *                   related function from the listOptions method
     */
    private void  firstGui(String userOption){
//        create the stage
        Stage window = new Stage();
        window.setTitle("Train Booking System");
        GridPane first = new GridPane();
        first.setPadding(new Insets(2, 2, 2, 2));
        first.setHgap(10);
        first.setVgap(10);
        Scene addViewFirst = new Scene(first, 1020, 400);
        window.setScene(addViewFirst);
        window.show();

//        main text for first window
        Label head = new Label("Denuwara Menike Ticket Booking System\n" +
                "                   A/C compartment");
        head.setFont(new Font("Arial", 30));
        head.setTextFill(Color.web("#0076a3")); //light blue
        first.add(head,20,3,50,8);

//        text for date
        Label headDate = new Label("Date");
        headDate.setFont(new Font("Arial", 23));
        headDate.setTextFill(Color.web("#0076a3")); //light blue
        first.add(headDate,3,12,9,4);

//        creating Gui element to select date
        DatePicker datePick = new DatePicker();
//        disabling past days on the date picker if user is booking seats
        if (userOption.equalsIgnoreCase("a"))
        {
            datePick.setDayCellFactory(picker -> new DateCell() {
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.compareTo(LocalDate.now()) < 0);
                }
            });
        }
        datePick.setValue(LocalDate.now());
        first.add(datePick, 12, 12,18,4);

        String[] stops = new String[]{ "Colombo Fort","Polgahawela",
                "Peradeniya Junction", "Gampola","Nawalapitiya",
                "Hatton","Thalawakele","Nanuoya", "Haputale","Diyatalawa",
                "Bandarawela","Ella", "Badulla" };
        ArrayList<String> stopsList = new ArrayList<>(Arrays.asList(stops));

        //        text for start
        Label headStart = new Label("Start");
        headStart.setFont(new Font("Arial", 23));
        headStart.setTextFill(Color.web("#0076a3")); //light blue
        first.add(headStart,3,16,9,4);

//        drop down menu for start
        ComboBox<String> startDrop = new ComboBox<>();
        startDrop.getItems().addAll(stops);
        startDrop.setValue("Colombo Fort");
        first.add(startDrop,12,16,9,4);

//        text for End
        Label headEnd = new Label("End");
        headEnd.setFont(new Font("Arial", 23));
        headEnd.setTextFill(Color.web("#0076a3")); //light blue
        first.add(headEnd,3,20,9,4);

//        drop down menu for End
        ComboBox<String> endDrop = new ComboBox<>();
        endDrop.getItems().addAll(stops);
        endDrop.setValue("Badulla");
        first.add(endDrop,12,20,9,4);

        Button switchB = new Button("switch");
        switchB.setOnAction(event -> {
            String temp = startDrop.getValue();
            startDrop.setValue(endDrop.getValue());
            endDrop.setValue(temp);
        });
        first.add(switchB,22,20,9,4);

//        gui element t o progress to booking page
        Button continueB = new Button("Continue");
        continueB.setMaxSize(120, 60);
        continueB.setOnAction(event -> {
//        creating a temporarily array for seat numbers
            ArrayList <String>temporarySeat = new ArrayList<>();
//        creating a array to to temporarily store user data
            ArrayList<String> temporaryList = new ArrayList<>(6);
//        [ date , start , end , name , seatNo, Nic, Surname ]
            for (int i = 0; i < 7; i++) {
                temporaryList.add("0");
            }
//        adding the date, start location & end location
            String temporaryDate  = datePick.getValue().toString();
            temporaryList.set(0,temporaryDate);
            String temporaryStart = startDrop.getValue();
            temporaryList.set(1,temporaryStart);
            String temporaryEnd   = endDrop.getValue();
            temporaryList.set(2,temporaryEnd);

            window.close();
//        calling the Gui related functions
            switch (userOption.toLowerCase()) {
                case "a":
                    addOption(temporaryList, temporarySeat, stopsList);
                    break;
                case "v":
                    viewOption(temporaryList, stopsList);
                    break;
                case "e":
                    emptyOption(temporaryList, stopsList);
                    break;
            }
        });
        first.add(continueB,70, 30,10,12);

//      close button
        Button closeButFirst = new Button("close");
        closeButFirst.setMaxSize(120, 60);
        closeButFirst.setStyle("-fx-background-color: red; ");
        closeButFirst.setOnAction(event -> {
            window.close();
            listOption();
        });
        first.add(closeButFirst,80,30,10,12);
    }


    /**
     * this method is used to book seats using grid of 42 seat icon
     * checkBSeats method is used to find out the all booked seats for the
     * selected route
     * if the seat no is booked seat it will be styled red
     * if the seat no is not booked it will be styled black
     * if the seat no is temporary booked it will  be styled green
     * the gui has 3 text fields that are used to get the user name, surname and
     * the user Nic
     *
     * when the user presses icons to book seat they will react depending on the
     * state of the seat number
     *
     * if the seat no is booked it will not change
     * if the seat no is temporary booked it'll changed to black & removed from
     * the temporary booked list
     * if the seat is not temporary booked it will be styled green and added to
     * temporary booked list
     *
     * @param temporaryList this array has 7 values
     *                      (date,from,to,name.seat number, Nic, surname)
     *                      when it's passed to this method it only contains the
     *                      date, from and to, these are initially used to get
     *                      already booked seats using the checkBSeats method
     *                      then the values are used to style the seat icons
     *
     *                      the entered user name, surname & Nic values
     *                      will be assigned to this array to the relevant index
     * @param temporarySeat this is array list which contains seat numbers,which
     *                      is used to style selected seats and is used to add
     *                      user data to the main data structure (booking list)
     * @param stopsList     this array contains a list of all stops for this
     *                      method the values are used to reference when
     *                      checking for booked seats
     *                      this is used to pass in to the checkBSeats method
     */
    private void    addOption(ArrayList<String> temporaryList,
                              ArrayList<String> temporarySeat,
                              ArrayList<String> stopsList) {
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

//        variables needed for seat icon loop
        Image seatRed = new Image(getClass().getResourceAsStream
                ("images/red.png"));
        Image seatGreen = new Image(getClass().getResourceAsStream
                ("images/green.png"));
        Image seatBlack = new Image(getClass().getResourceAsStream
                ("images/black.png"));
//        used to set the seat number
        int number = 1;

//        creating a array of booked seats temporarily for styling
//        for bookings with same date,from,to as temporary booking
        ArrayList <String>bookedSeat = checkBSeats(temporaryList, stopsList);

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
                    if (bookedSeat.contains(button.getId()))
                    {
                        button.setImage(seatRed);
                    }
//        if the current seat id is temporarily booked change to green
                    if (temporarySeat.contains(button.getId()))
                    {
                        button.setImage(seatGreen);
                    }
                    button.setOnMouseClicked(event ->
                    {
//        selected seat is already booked change to red
                        if (bookedSeat.contains(button.getId()))
                        {
                            button.setImage(seatRed);
                        }
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
        Label head = new Label("Select Seat"+"  "+temporaryList.get(1)+"-"+
                temporaryList.get(2));
        head.setFont(new Font("Arial", 30));
        head.setTextFill(Color.web("#0076a3")); //light blue
        grid.add(head,1,1,10,1);

//        field for user name
        TextField username = new TextField();
        if(temporaryList.get(3).equals("0"))username.setPromptText("Name");
        else username.setText(temporaryList.get(3));
        grid.add(username, 2, 5, 3, 6);

        TextField surname = new TextField();
        if(temporaryList.get(6).equals("0"))surname.setPromptText("surname");
        else surname.setText(temporaryList.get(6));
         grid.add(surname, 5, 5, 3, 6);

//        field for user id
        TextField userId = new TextField();
        if(temporaryList.get(5).equals("0"))userId.setPromptText("enter NIC");
        else userId.setText(temporaryList.get(5));
        grid.add(userId, 2, 9, 6, 6);

//        Confirm button
        Button bookBut = new Button("Book");
        bookBut.setMaxSize(120, 60);
        bookBut.setStyle("-fx-background-color: green; ");
        bookBut.setOnAction(event -> {
//        alert will be shown if name,seat or Nic is not entered
//        then addOption method will rerun
            if (
                    username.getText().trim().isEmpty()||
                    temporarySeat.isEmpty()||
                    userId.getText().trim().isEmpty()||
                    surname.getText().trim().isEmpty()
            )
            {
                Alert a = new Alert(Alert.AlertType.WARNING);
                if (username.getText().trim().isEmpty())
                {
                    a.setHeaderText("enter a name");
                }
                if (surname.getText().trim().isEmpty())
                {
                    a.setHeaderText ("enter a surname");
                }
                if (temporarySeat.isEmpty())
                {
                    a.setHeaderText("select seats");
                }
                if (userId.getText().trim().isEmpty())
                {
                    a.setHeaderText("enter Nic");
                }
                a.show();
                a.setOnCloseRequest(event1 ->
                {
//        setting the text fields with the entered values for validation
                    temporaryList.set(3,username.getText());
                    temporaryList.set(5,userId.getText());
                    temporaryList.set(6,surname.getText());
                    window.close();
                    addOption(temporaryList, temporarySeat, stopsList);
                });
            }else
            {
//        else username, surname and Nic will be added to temporary list
                temporaryList.set(3,username.getText());
                temporaryList.set(5,userId.getText());
                temporaryList.set(6,surname.getText());
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


    /**
     *this method is used to show the seats for the selected stops on the
     * selected date using grid of 42 icon
     * checkBSeats method is used to find out the booked seats
     * if the seat is booked seats will be coloured red
     * if the seat is not booked they coloured black
     * @param temporaryList this array has 7 values & for this method just the
     *                      starting stop (1st index) and ending stop(2nd index)
     *                      and date(index 0) these are initially used to get
     *                      already booked seats using the checkBSeats method
     *                      then the values are used to style the seat icons
     *
     * @param stopsList     this array contains a list of all stops for this
     *                      method the values are used to reference when
     *                      checking for booked seats
     *                      this is used to pass in to the checkBSeats method
     */
    private void   viewOption(ArrayList<String> temporaryList,
                              ArrayList<String> stopsList) {
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
        Image seatBlack = new Image(getClass().getResourceAsStream
                ("images/black.png"));
        Image seatGrey = new Image(getClass().getResourceAsStream
                ("images/grey.png"));
        //used to get value for seat number
        int number = 1;

//        creating a array of booked seats temporarily for styling
//        for bookings with same date,from,to as temporary booking
        ArrayList <String>bookedSeat = checkBSeats(temporaryList, stopsList);

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
                    if (bookedSeat.contains(num.getText()))
                    {
                        button.setImage(seatGrey);
                    }
                    number++;
                    grid.add(button, c, r);
                    grid.add(num, c, r);
                }
            }
        }
//        main text
        Label head = new Label("Viewing Seats "+"  "+temporaryList.get(1)+
                "-"+temporaryList.get(2));
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


    /**
     *this method is used to show the empty seat for the selected stops on the
     * selected date using grid of 42 icon
     * checkBSeats method is used to find out the booked seats
     * if the seat is booked seats will not be shown
     * if the seat is not booked they are shown & is kept black
     * @param temporaryList this array has 7 values & for this method just the
     *                      starting stop (1st index) and ending stop(2nd index)
     *                      and date(index 0) these are initially used to get
     *                      already booked seats using the checkBSeats method
     *                      then the values are used to style the seat icons
     *                      this is used to pass in to the checkBSeats method
     * @param stopsList     this array contains a list of all stops for this
     *                      method the values are used to reference when
     *                      checking for booked seats
     *                      this is used to pass in to the checkBSeats method
     */
    private void  emptyOption(ArrayList<String> temporaryList,
                              ArrayList<String> stopsList) {
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
        Image seatBlack = new Image(getClass().getResourceAsStream
                ("images/black.png"));

//        creating a array of booked seats temporarily for styling
//        for bookings with same date,from,to as temporary booking
        ArrayList <String>bookedSeat = checkBSeats(temporaryList, stopsList);

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
                    if (!bookedSeat.contains(num.getText()))
                    {
                        button.setImage(seatBlack);
                        grid.add(button, c, r);
                        grid.add(num, c, r);
                    }
                    number++;
                }
            }
        }
//        main text
        Label head = new Label("Empty Seats "+"  "+temporaryList.get(1)+"-"
                +temporaryList.get(2));
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

    /**
     * this method is used to delete sets of user data from the main data
     * structure
     * for this given user Nic and date will be taken as input & the code will
     * delete all records with the given Nic on the given date & will print a
     * record of the deleted data
     */
    private void deleteOption() {
//        getting user date in the format yyyy-mm-dd
        Scanner scanDDate = new Scanner(System.in);
        System.out.println("enter Date: [yyyy-mm-dd]");
        String deleteDate= scanDDate.next().toLowerCase();
        ArrayList <String>dateList = new ArrayList<>();
//        creating a list of all existing dates for verification
        for (ArrayList<String> data : booking) dateList.add(data.get(0));

//        getting user Nic
        Scanner scanDNic = new Scanner(System.in);
        System.out.println("enter your Nic: ");
        String deleteNic= scanDNic.next().toLowerCase();
        ArrayList <String>nicList = new ArrayList<>();
//        creating a list of all existing nic for verification
        for (ArrayList<String> data : booking) nicList.add(data.get(5));

//        array to collect index of user on the main data structure (booking)
        ArrayList <Integer>deleteIndex = new ArrayList<>();
        if (nicList.contains(deleteNic) && dateList.contains(deleteDate))
        {
            for(ArrayList<String> data : booking){
                if(data.get(0).equals(deleteDate)&&
                        data.get(5).equals(deleteNic))
                {
                    deleteIndex.add(booking.indexOf(data));
                }
            }
            Collections.reverse(deleteIndex);
            System.out.println("removed data");
            for(int i : deleteIndex)
            {
                System.out.println(
                        "\nFrom: " + booking.get(i).get(1) +
                        "\nTo:   " + booking.get(i).get(2) +
                        "\nSeat: " + booking.get(i).get(4));
                booking.remove(i);
            }
            waitOption();
        }
        else if(deleteNic.equals("q")||deleteDate.equals("q"))
        {
            waitOption();
        }
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
    }


    /**
     * this method is used to get all data related for a given user
     * for this user Nic will be taken as input & the code will loop thorough
     * main data structure( booking array) & the code will print out sets of
     * data related to the given Nic
     */
    private void   findOption() {
//        getting user Nic
        Scanner scanFNic = new Scanner(System.in);
        System.out.println("enter your Nic: ");
        String findNic= scanFNic.next().toLowerCase();
//        creating a list of all existing nic for verification
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
                                    " "+ data.get(6) +
                            "\nDate: " + data.get(0) +
                            "\nFrom: " + data.get(1) +
                            "\nTo: "   + data.get(2) +
                            "\nSeat: " + data.get(4) );
                }
            }
            waitOption();
        }
        else if (findNic.equals("q"))
        {
            waitOption();
        }
        else
            {
                System.out.println("Nic not in records");
                findOption();
            }
    }


    /**
     * this method is used to save data stored in the main data structure,this
     * will use a mongodb (a NoSQL database system) to save the date, username,
     * surname, from, to, Nic and seat number each sets of these values will
     * have a document. everything  will be stored in a single collection.
     *
     * before data will be stored, the code will check if a record already
     * exists if it dose the existing file will be deleted & a new collection
     * will be made
     */
    private void   saveOption() {
        com.mongodb.MongoClient dbClient = new MongoClient
                ("localhost", 27017);
        MongoDatabase dbDatabase = dbClient.getDatabase
                ("trainBookingSystem");
        MongoCollection<Document> bookings = dbDatabase.getCollection
                ("BookingData");
        System.out.println("connected to BookingData");
        FindIterable<Document> bookingDocument = bookings.find();

        if(bookings.countDocuments()>1)
        {
            for(Document document: bookingDocument)
            {
                bookings.deleteOne(document);
            }
        }

        for (ArrayList<String> data : booking)
        {
            Document userDocument = new Document();
            userDocument.append("Date", data.get(0));
            userDocument.append("Start",data.get(1));
            userDocument.append("End",  data.get(2));
            userDocument.append("User", data.get(3));
            userDocument.append("Seat", data.get(4));
            userDocument.append("Nic",  data.get(5));
            userDocument.append("Surname",  data.get(6));
            bookings.insertOne(userDocument);
        }
        dbClient.close();
        System.out.println("saved files");
        waitOption();
    }


    /**
     * this method is used to recover data that was previously saved, this will
     * use a mongodb (a NoSQL database system) to receive the date, username,
     * surname, from, to, Nic and seat number. each sets of these values will
     * be saved to a temporary array and that array will be added to the main
     * data structure .
     *
     * before data will be restored, the code will check if a record exists. if
     * so the main data structure will be reset and data will be restored. if
     * the a record is not there a message will be shown and data structure
     * won't be changed
     */
    private void   loadOption() {
//        initiate MongoClient
        com.mongodb.MongoClient dbClient = new MongoClient
                ("localhost", 27017);
//        creating a database
        MongoDatabase dbDatabase = dbClient.getDatabase
                ("trainBookingSystem");
//        creating a document
        MongoCollection<Document> bookings = dbDatabase.getCollection
                ("BookingData");
        System.out.println("connected to BookingData");
        FindIterable<Document> bookingDocument = bookings.find();

//        creating a new a new array to collect values
        ArrayList<String> temporaryList = new ArrayList<>(6);
        for (int i = 0; i < 7; i++) temporaryList.add("0");

//        if a document exists the it will be added to the array
        if(bookings.countDocuments()>0)
        {
//         resetting the existing main data structure
            booking.clear();

            for(Document document:bookingDocument)
            {
                temporaryList.set(0,document.getString("Date"));
                temporaryList.set(1,document.getString("Start"));
                temporaryList.set(2,document.getString("End"));
                temporaryList.set(3,document.getString("User"));
                temporaryList.set(4,document.getString("Seat"));
                temporaryList.set(5,document.getString("Nic"));
                temporaryList.set(6,document.getString("Surname"));

//                adding collected sets of values to the main data structure
                booking.add(new ArrayList<>(temporaryList));
            }
            System.out.println("files loaded");
        }
//        if not a message will be printed
        else
            {
                System.out.println("no files were added, no data is changed");
            }

//        close mongo client
        dbClient.close();
        waitOption();
    }


    /**
     * this method is used to sort all the booked seats in the main data
     * structure using a bubble sort by name of the user & the this code will
     * print out the sorted date in the following format
     *  seat + number + name + surname + Nic
     */
    private void   oderOption() {
        if (booking.isEmpty()) System.out.println("no seats are booked");
        String sortTemp;
//        creating arrays for each value needed
//        List<String> seatList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
//        List<String> nicList = new ArrayList<>();

//        looping through the main data structure to extract data
        for (ArrayList<String> data : booking)
        {
            nameList.add(data.get(3)+" "+data.get(6)+" "+data.get(4)+" "+data.get(5));
        }
//        running the bubble sort algorithm
        for (int j = 0; j < nameList.size(); j++) {
            for (int i = j + 1; i < nameList.size(); i++) {
                if (String.valueOf(nameList.get(i))
                        .compareTo(String.valueOf(nameList.get(j))) < 0)
                {
                    sortTemp = String.valueOf(nameList.get(j));
                    nameList.set(j, nameList.get(i));
                    nameList.set(i, sortTemp);
                }
            }
//         printing out needed values
            System.out.println(nameList.get(j));
        }
        waitOption();
    }


    /**
     * this method is used to get a temporary list for booked seats relevant to
     * the selected route, this method will check for booked seats
     * > between the starting stop & ending stop
     * > between routes that include the selected route
     * > routes that start before the start stop and end before the end stop
     * > routes that start after the start stop and end after the end stop
     * @param temporaryList this array has 7 values & for this method just the
     *                      starting stop (1st index) and ending stop(2nd index)
     *                      and date(index 0) are used
     * @param stopsList     this array contains a list of all stops for this
     *                      method the values are used to reference when
     *                      checking for booked seats
     * @return              this method will return a array list containing a
     *                      list of all booked seats for the given stops
     */
    private ArrayList<String> checkBSeats(ArrayList<String> temporaryList,
                                          ArrayList<String> stopsList){
//        creating a array for already booked seats
        ArrayList<String> bookedSeat = new ArrayList<>();

//        changing the order of stops array according to selected stops
//        this is done to have colombo to badulla & badulla to colombo
//        bookings separate
        if (stopsList.indexOf(temporaryList.get(2))
                <stopsList.indexOf(temporaryList.get(1)))
        {
            Collections.reverse(stopsList);
        }
//        getting index of first & last stop from a list of stops
        int startingStop= stopsList.indexOf(temporaryList.get(1));
        int endingStop= stopsList.indexOf(temporaryList.get(2));
//        looping to check for booked seats in between the 2 stops
        while (startingStop<endingStop)
        {
            int checkStop = startingStop+1;
            while (checkStop<=endingStop)
            {
                String temporaryStart= stopsList.get(startingStop);
                String temporaryEnd= stopsList.get(checkStop);

//        getting  seat numbers using the main data structure
                for (ArrayList<String> strings : booking)
                {
                    if
                    (
                            strings.get(0).equals(temporaryList.get(0))&&
                            strings.get(1).equals(temporaryStart) &&
                            strings.get(2).equals(temporaryEnd)
                    )
                    {
//        adding seat number to created array
                        bookedSeat.add(strings.get(4));
                    }
                }
                checkStop++;
            }
            startingStop++;
        }


//        getting index of stop immediately & before start stop
        int afterStop=  stopsList.indexOf(temporaryList.get(2))-1;
        int firstStop=0;
        int lastStop=12;
//        looping to check for booked seats for routes containing the 2 stops &
//        for routes that begin before the start stop and end before end stop
        while (firstStop<afterStop)
        {
            int checkStop = afterStop+1;
            while (checkStop<=lastStop)
            {
                String temporaryStart= stopsList.get(firstStop);
                String temporaryEnd= stopsList.get(checkStop);

//        getting  seat numbers using the main data structure
                for (ArrayList<String> strings : booking)
                {
                    if
                    (
                            strings.get(0).equals(temporaryList.get(0))&&
                            strings.get(1).equals(temporaryStart) &&
                            strings.get(2).equals(temporaryEnd)
                    )
                    {
//        adding seat number to created array
                        bookedSeat.add(strings.get(4));
                    }
                }
                checkStop++;
            }
            firstStop++;
        }

        int beforeStop= stopsList.indexOf(temporaryList.get(1))+1;
        afterStop=  stopsList.indexOf(temporaryList.get(2));
        firstStop=0;
//        looping to check for booked seats in between routes that start after
//        the start stop and end after the end stop
        while (firstStop<beforeStop)
        {
            int checkStop = beforeStop+1;
            while (checkStop<=afterStop)
            {
                String temporaryStart= stopsList.get(firstStop);
                String temporaryEnd= stopsList.get(checkStop);


//        getting  seat numbers using the main data structure
                for (ArrayList<String> strings : booking)
                {
                    if
                    (
                            strings.get(0).equals(temporaryList.get(0))&&
                                    strings.get(1).equals(temporaryStart) &&
                                    strings.get(2).equals(temporaryEnd)
                    )
                    {
//        adding seat number to created array
                        bookedSeat.add(strings.get(4));
                    }
                }
                checkStop++;
            }
            firstStop++;
        }

        return bookedSeat ;
    }


    /**
     * this method is used as a way to give the user a way to manually move to
     * the menu
     * this method will prevent the code from automatically moving to the menu
     * and lets the user do it on on their own pace.
     */
    private void   waitOption() {
//        to let the use consume the details of console functions
        Scanner scanContinue = new Scanner(System.in);
        System.out.println("Press any key to continue");
        String continueConsole=scanContinue.next();
        if (!continueConsole.isEmpty())
        {
//        opening the menu
            listOption();
        }
    }
}