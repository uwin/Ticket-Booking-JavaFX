/*
\need to flash when reClicked after booked
\ https://stackoverflow.com/questions/29679971/javafx-make-a-grid-of-buttons/29719308
\ https://beginnersbook.com/2019/04/java-program-to-perform-bubble-sort-on-strings/
*/
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Guiiit extends Application {
    static final int SEATING_CAPACITY = 42;
    public static void main(String[] args) {
        launch();
    }
    private static final List<String> seatList = new ArrayList<>();
    private static final List<String> nameList = new ArrayList<>();
    private static final List<String> temp = new ArrayList<>();
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
        runOption();
    }
    public void    runOption(){
        Scanner scanOption= new Scanner(System.in);
        System.out.println(">> select a option");
        String userOption= scanOption.next().toUpperCase();
        switch (userOption) {
            case "A":
                addOption();
                break;
            case "V":
                viewOption();
                break;
            case "E":
                emptyOption();
                break;
            case "D":
                deleteOption();
                break;
            case "F":
                findOption();
                break;
            case "S":
                try {
                    saveOption();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "L":
                try {
                    loadOption();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
    public void    addOption(){
//      create the stage
        Stage window = new Stage();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 2, 5, 2));
        grid.setHgap(10);
        grid.setVgap(10);
        Scene addView = new Scene(grid, 1020, 400);
        window.setTitle("Train Booking System");
        window.setScene(addView);
        window.show();

//      values needed for the loop
        int number = 1;
        Image seatBlack = new Image(getClass().getResourceAsStream("black.png"));
        Image seatRed = new Image(getClass().getResourceAsStream("red.png"));
        Image seatGreen = new Image(getClass().getResourceAsStream("green.png"));

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
                    button.setId(String.valueOf(number));
//                    change seat colour to orange if it's already booked
                    if (seatList.contains(button.getId())) {
                        button.setImage(seatRed);
                    }
                    if (temp.contains(button.getId())) button.setImage(seatGreen);
                    button.setOnMouseClicked(event -> {
//                      flash the seat colour if the user tries to click a already booked seat
                        if (seatList.contains(button.getId())){
                            button.setImage(seatRed);
//                      if the seat is not booked add the seat to the temporary seatList,change colour to green
                        }else if(!temp.contains(button.getId())){
                            button.setImage(seatGreen);
                            temp.add(button.getId());
//                      if the user again clicks a already booked seat, remove it from the temp booked list, revert colour
                        }else if (temp.contains(button.getId())){
                            temp.remove(button.getId());
                            button.setImage(seatBlack);
                        }
                    });
                    number++;
                    grid.add(button, c, r);
                    grid.add(num, c, r);
                }
            }
        }
//      window head
        Label head = new Label("Select Seat");
        head.setFont(new Font("Arial", 30));
        head.setTextFill(Color.web("#0076a3")); //light blue
        grid.add(head,1,1,10,1);

//      space for user name
        TextField username = new TextField();
        username.setPromptText("enter name");
        grid.add(username, 8, 5, 10, 6);



        //      Confirm button
        Button bookBut = new Button("Book");
        bookBut.setMaxSize(120, 60);
        bookBut.setStyle("-fx-background-color: green; ");
        bookBut.setOnAction(event -> {
//            alert will be shown if either the name or a seat is not selected
            if (username.getText().trim().isEmpty()||temp.isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                if (username.getText().trim().isEmpty())a.setHeaderText("enter a name");
                if (temp.isEmpty())a.setHeaderText("select seats");
                a.show();
                a.setOnCloseRequest(event1 -> {
                    window.close();
                    addOption();
                });
//                alert will be shown if the user name is already existing
            }else if (nameList.contains(username.getText().toLowerCase())) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("enter a unique name");
                a.show();
                a.setOnCloseRequest(event1 -> {
                    window.close();
                    addOption();
                });
//                all user selected seats will be added to the seatList
            }else {
                for (Object ignored : temp) {
                    nameList.add(username.getText().toLowerCase());
                }
                seatList.addAll(temp);
                temp.clear();
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
            temp.clear();
            window.close();
            addOption();
        });
        grid.add(resetBut, 12, 9,12,9);

//      close button
        Button closeBut = new Button("close");
        closeBut.setMaxSize(120, 60);
        closeBut.setStyle("-fx-background-color: red; ");
        closeBut.setOnAction(event -> {
            window.close();
            listOption();
        });
        grid.add(closeBut, 14, 9,14,9);
    }
    public void   viewOption(){
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
        Image seatBlack = new Image(getClass().getResourceAsStream("black.png"));
        Image seatGrey = new Image(getClass().getResourceAsStream("grey.png"));

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
//                    if seat is booked the seat button is greyed out
                    if (seatList.contains(String.valueOf(number))){
                        button.setImage(seatGrey);
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
            listOption();
        });
        grid.add(closeBut,14,6,14,6);

    }
    public void  emptyOption(){
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
        Image seatBlack = new Image(getClass().getResourceAsStream("black.png"));

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
                    if (!seatList.contains(String.valueOf(number))) {
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
    public void deleteOption(){
        List<String> deleteList = new ArrayList<>();
        System.out.println(nameList+"\n"+seatList);
//        getting user name
        Scanner scanName = new Scanner(System.in);
        System.out.println("enter your name:");
        String deleteName= scanName.next();
        if (nameList.contains(deleteName.toLowerCase())) {
//            printing all the seats for the user name in a row
            for (int i=0;i<nameList.size();i++){
                if(nameList.get(i).equals(deleteName.toLowerCase())) {
                    System.out.print(seatList.get(i)+" | ");
                    deleteList.add(seatList.get(i));
                }
            }
//            looping till a valid seat is given
            while (true){
                System.out.println();
//                getting user to select a seat number
                Scanner scanDelete = new Scanner(System.in);
                System.out.println("enter your seat number:");
                String deleteSeat = scanDelete.next();
                int deleteFinal = seatList.indexOf(deleteSeat);

//                removing the given seat from the seat list
                if (deleteList.contains(String.valueOf(deleteSeat))){
                    seatList.remove(deleteFinal);
                    nameList.remove(deleteFinal);
                    deleteList.clear();
                    System.out.println(nameList+"\n"+seatList);
                    waitOption();
                    break;
                 }else if(deleteSeat.toLowerCase().equals("q")){
                    listOption();
                    break;
                }
                System.out.println("the seat no is not booked");
            }
        }else if(deleteName.toLowerCase().equals("q")){
            listOption();
        }else {
//            looping until a valid user name is given
            System.out.println("your have no seats booked");
            deleteOption();
        }
    }
    public void   findOption(){
//        getting user name
        Scanner scanFind = new Scanner(System.in);
        System.out.println("enter your name:");
        String findName= scanFind.next();
//        printing all seat values for the given name
        if (nameList.contains(findName.toLowerCase())){
            for (int i=0;i<nameList.size();i++){
                if(nameList.get(i).equals(findName.toLowerCase())) {
                    System.out.print(seatList.get(i)+"| ");
                }
            }
            System.out.println();
            waitOption();
        }else if(findName.toLowerCase().equals("q")){
            listOption();
        }else{
//            looping till a valid user name is given
            findOption();
        }

    }
    public void   saveOption() throws IOException {
        for (String i : seatList){
            System.out.print(i+"|");
            System.out.print(nameList.get(seatList.indexOf(i)));
            System.out.println();
        }
        while (true){
            Scanner scanSave =new Scanner(System.in);
            System.out.println("\n Save "+seatList.size()+" Bookings? (y/n)");
            String saveSeat = scanSave.next();
            if(saveSeat.toLowerCase().equals("y")||saveSeat.toLowerCase().equals("yes")){
                PrintWriter saveSeats = new PrintWriter(new BufferedWriter(new FileWriter("seats.txt")));
                for (String s : seatList) {
                    saveSeats.println(s);
                }
                saveSeats.close();
                PrintWriter saveNames = new PrintWriter(new BufferedWriter(new FileWriter("names.txt")));
                for (String s : nameList) {
                    saveNames.println(s);
                }
                saveNames.close();
                waitOption();
                break;
            }else if(saveSeat.toLowerCase().equals("q")||saveSeat.toLowerCase().equals("n")||saveSeat.toLowerCase().equals("no")){
                listOption();
                break;
            }
        }
    }
    public void   loadOption() throws FileNotFoundException {
        while (true){
            Scanner scanLoad =new Scanner(System.in);
            System.out.println("\n load Bookings? (y/n)");
            String loadSeat = scanLoad.next();
            if(loadSeat.toLowerCase().equals("y")){
                Scanner scanSeats = new Scanner(new File("seats.txt"));
                while(scanSeats.hasNext()) seatList.add(scanSeats.next());
                Scanner scanNames = new Scanner(new File("names.txt"));
                while(scanNames.hasNext()) nameList.add(scanNames.next());
                for (String i : seatList){
                    System.out.print(i+"|");
                    System.out.print(nameList.get(seatList.indexOf(i)));
                    System.out.println();
                }
                System.out.println("\n"+seatList.size()+" Bookings Loaded");
                waitOption();
                break;
            }else if(loadSeat.toLowerCase().equals("q")||loadSeat.toLowerCase().equals("n")){
                listOption();
                break;
            }
        }
    }
    public void   oderOption(){
        String sortTemp;
        for (int j = 0; j < nameList.size(); j++) {
            for (int i = j + 1; i < nameList.size(); i++) {
                // comparing adjacent strings
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
    public void   waitOption(){
        Scanner scanContinue = new Scanner(System.in);
        System.out.println("Press any key to continue");
        String continueConsole=scanContinue.next();
        if (!continueConsole.isEmpty()) listOption();
    }
}