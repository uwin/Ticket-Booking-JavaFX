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

import java.time.LocalDate;
import java.util.*;

import static javax.xml.bind.DatatypeConverter.parseInt;

public class trainbooking extends Application {
    static final int SEATING_CAPACITY = 42;
    static final ArrayList<ArrayList<String>> booking = new ArrayList<ArrayList<String>>();
    public static void main(String[] args) {
        launch();
    }
    public void start(Stage stage) {
        welcome();
    }
    public void welcome() {
        System.out.println("\nwelcome to ticket booking system \nA/C compartment for Denuwara Menike");
        listOption();
    }
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
        //      calling methods depending on the users input for the previous method
        Scanner scanOption= new Scanner(System.in);
        System.out.println(">> select a option");
        String userOption= scanOption.next().toUpperCase();
        switch (userOption) {
            case "A":            //gui related methods wil be further tested in this method for getting the route & date
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
    public void  firstGui(String userOption){
//      create the stage
        Stage window = new Stage();
        window.setTitle("Train Booking System");
        GridPane gridFirst = new GridPane();
        gridFirst.setPadding(new Insets(2, 2, 2, 2));
        gridFirst.setHgap(10);
        gridFirst.setVgap(10);
        Scene addveiwFirst = new Scene(gridFirst, 1020, 400);
        window.setScene(addveiwFirst);
        window.show();

//      main head for first window
        Label head = new Label("Denuwara Menike Ticket Booking System\n                   Colombo-Badulla");
        head.setFont(new Font("Arial", 30));
        head.setTextFill(Color.web("#0076a3")); //light blue
        gridFirst.add(head,20,3,50,8);

//      Secondary text
        Label headDate = new Label("Date");
        headDate.setFont(new Font("Arial", 23));
        headDate.setTextFill(Color.web("#0076a3")); //light blue
        gridFirst.add(headDate,3,12,9,4);

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
        gridFirst.add(datePick, 12, 12,18,4);

        //      Secondary text
        Label headStart = new Label("Start");
        headStart.setFont(new Font("Arial", 23));
        headStart.setTextFill(Color.web("#0076a3")); //light blue
        gridFirst.add(headStart,3,16,9,4);

        ComboBox<String> startComboBox = new ComboBox<>();
        startComboBox.getItems().addAll(

                "Colombo Fort","Polgahawela","Peradeniya Junction","Gampola","Nawalapitiya",
                "Hatton","Thalawakele","Nanuoya","Haputale","Diyatalawa","Bandarawela","Ella",
                "Badulla"
        );
        startComboBox.setValue("Colombo Fort");
        gridFirst.add(startComboBox,12,16,9,4);

        //      Secondary text
        Label headEnd = new Label("End");
        headEnd.setFont(new Font("Arial", 23));
        headEnd.setTextFill(Color.web("#0076a3")); //light blue
        gridFirst.add(headEnd,3,20,9,4);

        ComboBox<String> endComboBox = new ComboBox<>();
        endComboBox.getItems().addAll(
                "Colombo Fort","Polgahawela","Peradeniya Junction","Gampola","Nawalapitiya",
                "Hatton","Thalawakele","Nanuoya","Haputale","Diyatalawa","Bandarawela","Ella",
                "Badulla"
        );
        endComboBox.setValue("Badulla");
        gridFirst.add(endComboBox,12,20,9,4);


//      gui element to progress to booking page
        Button continuebut = new Button("Continue");
        continuebut.setMaxSize(120, 60);
        continuebut.setOnAction(event -> {
            ArrayList<String> temporaryList = new ArrayList<>(5);
            ArrayList <String>temporarySeat = new ArrayList<>();
            /*[ date , start , end , name , seatNo ]*/
            for (int i = 0; i < 5; i++) {
                temporaryList.add("0");
            }
            String temporaryDate  = datePick.getValue().toString();
            String temporaryStart = startComboBox.getValue().toString();
            String temporaryEnd   = endComboBox.getValue().toString();
            temporaryList.set(0,temporaryDate);
            temporaryList.set(1,temporaryStart);
            temporaryList.set(2,temporaryEnd);
            System.out.println(temporaryList);
            window.close();
            switch (userOption.toLowerCase()) {   //calling the Gui related functions
                case "a":
                    addOption(temporaryList, temporarySeat);
                    break;
                case "v":
                    viewOption(temporaryList);
                    break;
                case "e":
                    emptyOption(temporaryList);
                    break;
            }
        });
        gridFirst.add(continuebut,70, 30,10,12);

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
    private void addOption(ArrayList<String> temporaryList, ArrayList<String> temporarySeat) {
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
        Image seatRed = new Image(getClass().getResourceAsStream("images/red.png"));
        Image seatGreen = new Image(getClass().getResourceAsStream("images/green.png"));
        Image seatBlack = new Image(getClass().getResourceAsStream("images/black.png"));
        int number = 1; //used to set the seat number
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
                if (number <=SEATING_CAPACITY)
                {
                    ImageView button = new ImageView(seatBlack);
                    Label num = new Label();
                    num.setFont(new Font("Arial", 15));
                    num.setText(String.valueOf(number));
                    button.setFitHeight(60);
                    button.setFitWidth(60);
                    button.setId(String.valueOf(number));

                    if (bookedSeat.contains(button.getId())) button.setImage(seatRed);
                    if (temporarySeat.contains(button.getId())) button.setImage(seatGreen);

                    button.setOnMouseClicked(event ->
                    {
                        if (bookedSeat.contains(button.getId())) button.setImage(seatRed);
                        else if (!temporarySeat.contains(button.getId()))
                        {
                            button.setImage(seatGreen);
                            temporarySeat.add(button.getId());
                        }
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
            if (username.getText().trim().isEmpty()||temporarySeat.isEmpty())
            {
                Alert a = new Alert(Alert.AlertType.WARNING);
                if (username.getText().trim().isEmpty())a.setHeaderText("enter a name");
                if (temporarySeat.isEmpty())a.setHeaderText("select seats");
                a.show();
                a.setOnCloseRequest(event1 ->
                {
                    window.close();
                    addOption(temporaryList, temporarySeat);
                });
            }else
            {
                temporaryList.set(3,username.getText());
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

//      Reset Button
        Button resetBut = new Button("Clear");
        resetBut.setMaxSize(120, 60);
        resetBut.setStyle("-fx-background-color: orange; ");
        resetBut.setOnAction(event -> {
            temporarySeat.clear();
            window.close();
            addOption(temporaryList, temporarySeat);
        });
        grid.add(resetBut, 12, 9,12,9);

//      close button
        Button closeBut = new Button("close");
        closeBut.setMaxSize(120, 60);
        closeBut.setStyle("-fx-background-color: red; ");
        closeBut.setOnAction(event -> {
            temporarySeat.clear();
            window.close();
            listOption();
        });
        grid.add(closeBut, 14, 9,14,9);//      close button

    }
    private void viewOption(ArrayList<String> temporaryList) {
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

//        impoting icons
        Image seatBlack = new Image(getClass().getResourceAsStream("images/black.png"));
        Image seatGrey = new Image(getClass().getResourceAsStream("images/grey.png"));
        int number = 1; //used to get value for seat number
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

                    if (bookedSeat.contains(num.getText())) button.setImage(seatGrey);
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
            listOption();
        });
        grid.add(closeBut,14,6,14,6);
    }
    private void emptyOption(ArrayList<String> temporaryList) {
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

                    if (!bookedSeat.contains(num.getText())) {
                        button.setImage(seatBlack);
                        grid.add(button, c, r);
                        grid.add(num, c, r);
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
            window.close();
            listOption();
        });
        grid.add(closeBut,14,6,14,6);
    }

    private void deleteOption() {
        Scanner scanDName = new Scanner(System.in);
        System.out.println("enter your name:");
        String deleteName= scanDName.next().toLowerCase();
        ArrayList <String>nameList = new ArrayList<>();
        for (ArrayList<String> data : booking) nameList.add(data.get(3));
        if (nameList.contains(deleteName))
        {
            ArrayList <String>deleteDates = new ArrayList<>();
            int i=0;
            for (ArrayList<String> data : booking)
            {
                if (data.get(3).equals(deleteName))
                {
                    if(!deleteDates.contains(data.get(0)))
                    {
                        System.out.println(i+" | "+data.get(0));
                        i++;
                        deleteDates.add(data.get(0));
                    }
                }
            }
            Scanner scanDDate = new Scanner(System.in);
            System.out.println("select the date");
            String selectDate= scanDDate.next();
            if(selectDate.toLowerCase().equals("q"))
            {
                waitOption();
            }
            try {
                if (parseInt(selectDate) < i && parseInt(selectDate) >= 0)
                {
                    String date = deleteDates.get(parseInt(selectDate));
                    System.out.println("Deleted data\n");
                    ArrayList<Integer> indexDelete = new ArrayList<>();
                    for (ArrayList<String> data : booking)
                    {
                        if (data.get(3).equals(deleteName) && data.get(0).equals(date))
                        {
                            System.out.println("From: " + data.get(1) + "\nTo: " + data.get(2) + "\nSeat :" + data.get(4) + "\n");
                            indexDelete.add(booking.indexOf(data));
                        }
                    }
                    Collections.reverse(indexDelete);
                    for (int index : indexDelete) booking.remove(index);
                    waitOption();
                }
                else
                {
                    System.out.println("input is not available");
                    deleteOption();
                }
            }
            catch (Exception e)
            {
                System.out.println("input is invalied");
                deleteOption();
            }
        }
        else if(deleteName.toLowerCase().equals("q"))
        {
            waitOption();
        }else {
                System.out.println("name is not in records");
                deleteOption();
        }
    }

    private void findOption() {
        Scanner scanDName = new Scanner(System.in);
        System.out.println("enter your name:");
        String deleteName= scanDName.next().toLowerCase();
        ArrayList <String>nameList = new ArrayList<>();
        for (ArrayList<String> data : booking) nameList.add(data.get(3));
        if (nameList.contains(deleteName))
        {
            for (ArrayList<String> data : booking)
            {
                if (data.get(3).equals(deleteName))
                {
                    System.out.println("From: " + data.get(1) + "\nTo: " + data.get(2) + "\nSeat :" + data.get(4) + "\n");
                }
            }
        }
        else
            {
                System.out.println("name not in records");
            }
    }

    private void saveOption() {
    }

    private void loadOption() {
    }

    private void oderOption() {
    }

    public void   waitOption(){
//        to let the use consume the details of console functions before moving to the menu
        Scanner scanContinue = new Scanner(System.in);
        System.out.println("Press any key to continue");
        String continueConsole=scanContinue.next();
        if (!continueConsole.isEmpty()) listOption();
    }
}
