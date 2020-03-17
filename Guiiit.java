//test
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

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoClients;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientOptions;
import com.mongodb.ClientSessionOptions;
import com.mongodb.client.*;
import com.mongodb.client.internal.MongoDatabaseImpl;
import com.mongodb.connection.ClusterDescription;

import java.io.FileNotFoundException;
import java.io.IOException;
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
        testOptions();
    }
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

//        window headFirst
        Label headFirst1 = new Label("Denuwara Menike Ticket Booking System\n                   Colombo-Budulla");
        headFirst1.setFont(new Font("Arial", 30));
        headFirst1.setTextFill(Color.web("#0076a3")); //light blue
        gridFirst.add(headFirst1,20,3,50,8);

        Label headFirst2 = new Label("Enter Date");
        headFirst2.setFont(new Font("Arial", 23));
        headFirst2.setTextFill(Color.web("#0076a3")); //light blue
        gridFirst.add(headFirst2,3,12,9,4);

        DatePicker datePick = new DatePicker();
        datePick.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
        datePick.setValue(LocalDate.now());
        gridFirst.add(datePick, 13, 12,18,4);

        //        continue button
        Button toBudulla = new Button("From Colombo");
        toBudulla.setMaxSize(120, 60);
        toBudulla.setOnAction(event -> {
            int Colombo_Budullaverify=1;
            int Budulla_Colomboverify=0;
            LocalDate date=datePick.getValue();
            window.close();
            switch (userOption.toLowerCase()) {
                case "a":
                    addOption(Colombo_Budullaverify, Budulla_Colomboverify, date);
                    break;
                case "v":
                    viewOption(Colombo_Budullaverify, Budulla_Colomboverify, date);
                    break;
                case "e":
                    emptyOption(Colombo_Budullaverify, Budulla_Colomboverify, date);
                    break;
            }
        });
        gridFirst.add(toBudulla,35, 16,10,12);

//                continue button
        Button toColombo = new Button("From Budulla");
        toColombo.setMaxSize(120, 60);
        toColombo.setOnAction(event -> {
            int Budulla_Colomboverify=1;
            int Colombo_Budullaverify=0;
            LocalDate date=datePick.getValue();
            window.close();
            if (userOption.toLowerCase().equals("a")) addOption(Colombo_Budullaverify,Budulla_Colomboverify,date);
            else if (userOption.toLowerCase().equals("v")) viewOption(Colombo_Budullaverify,Budulla_Colomboverify,date);
            else if (userOption.toLowerCase().equals("e")) emptyOption(Colombo_Budullaverify,Budulla_Colomboverify,date);
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

    public void    addOption(int Colombo_Budullaverify, int Budulla_Colomboverify,LocalDate date){
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

//      values needed for the loop
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
//                    change seat colour to red if it's already booked
                    if(Colombo_Budullaverify==1)
                    {
                        if(dateC2B.contains(date))
                        {
                            ArrayList<HashMap<String,String>> inti = hashC2B.get(dateC2B.indexOf(date));
                            HashMap<String,String> hash = inti.get(0);
                            for(String i: hash.keySet()) ColomboToBudulla.put(i,hash.get(i));
                        }else ColomboToBudulla.put("","");
                        if (ColomboToBudulla.containsKey(button.getId())) button.setImage(seatRed);
                        if (temp.contains(button.getId())) button.setImage(seatGreen);
                        button.setOnMouseClicked(event -> {
                            //flash the seat colour if the user tries to click a already booked seat
                            if (ColomboToBudulla.containsKey(button.getId()))
                            {
                                button.setImage(seatRed);
                                //if the seat is not booked add the seat to the temporary seatList,change colour to green
                            } else if (!temp.contains(button.getId()))
                            {
                                button.setImage(seatGreen);
                                temp.add(button.getId());
                                //if the user again clicks a already booked seat, remove it from the temp booked list, revert colour
                            } else if (temp.contains(button.getId()))
                            {
                                temp.remove(button.getId());
                                button.setImage(seatBlack);
                            }
                        });
                    }
                    else if(Budulla_Colomboverify==1)
                    {
                        if(dateB2C.contains(date))
                        {
                            ArrayList<HashMap<String,String>> inti = hashB2C.get(dateB2C.indexOf(date));
                            HashMap<String,String> hash = inti.get(0);
                            for(String i: hash.keySet()) BudullaToColombo.put(i,hash.get(i));
                        }else ColomboToBudulla.put("","");
                        if (BudullaToColombo.containsKey(button.getId())) button.setImage(seatRed);
                        if (temp.contains(button.getId())) button.setImage(seatGreen);
                        button.setOnMouseClicked(event -> {
                            //                      flash the seat colour if the user tries to click a already booked seat
                            if (BudullaToColombo.containsKey(button.getId()))
                            {
                                button.setImage(seatRed);
                                //                      if the seat is not booked add the seat to the temporary seatList,change colour to green
                            }else if(!temp.contains(button.getId()))
                            {
                                button.setImage(seatGreen);
                                temp.add(button.getId());
                                //                      if the user again clicks a already booked seat, remove it from the temp booked list, revert colour
                            }else if (temp.contains(button.getId()))
                            {
                                temp.remove(button.getId());
                                button.setImage(seatBlack);
                            }
                        });
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
//            alert will be shown if either the name or a seat is not selected
            if (username.getText().trim().isEmpty()||temp.isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                if (username.getText().trim().isEmpty())a.setHeaderText("enter a name");
                if (temp.isEmpty())a.setHeaderText("select seats");
                a.show();
                a.setOnCloseRequest(event1 -> {
                    window.close();
                    addOption(Colombo_Budullaverify,Budulla_Colomboverify,date);
                });
//                alert will be shown if the user name is already existing
            }else if(Budulla_Colomboverify==1){
                if (BudullaToColombo.containsValue(username.getText().toLowerCase())) {
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setContentText("enter a unique name");
                    a.show();
                    a.setOnCloseRequest(event1 -> {
                        window.close();
                        addOption(Colombo_Budullaverify,Budulla_Colomboverify,date);
                    });
                }else {
                    if(!dateB2C.contains(date)) {
                        HashMap<String, String> TBudullaToColombo = new HashMap<>();
                        for (String i : temp) {
                            int indexforHash = temp.indexOf(i);
                            //if(Colombo_Budullaverify==1) ColomboToBudulla.put(temp.get(indexforHash),username.getText().toLowerCase());
                            if (Budulla_Colomboverify == 1) TBudullaToColombo.put(temp.get(indexforHash), username.getText().toLowerCase());
                        }
                        dateB2C.add(date);
                        System.out.println("B>C" + dateB2C);
                        System.out.println("B>C" + TBudullaToColombo);
                        hashB2C.add(new ArrayList<>());
                        int hashhashindex = dateB2C.size();
                        hashhashindex -= 1;
                        hashB2C.get(hashhashindex).add(0, TBudullaToColombo);
                        System.out.println("first time"+hashB2C);
                    }else {
                        System.out.println("pressed");
                        ArrayList<HashMap<String,String>> inti = hashB2C.get(dateB2C.indexOf(date));
                        System.out.println("initi"+inti);
                        HashMap<String,String> TBudullaToColombo = inti.get(0);
                        System.out.println("hash"+TBudullaToColombo);
                        for(String i: temp){
                            int indexforHash = temp.indexOf(i);
                            TBudullaToColombo.put(temp.get(indexforHash),username.getText().toLowerCase());
                        }
                        hashB2C.get(dateB2C.indexOf(date)).clear();
                        System.out.println("get"+hashB2C.get(dateB2C.indexOf(date)));
                        //hashB2C.add(new ArrayList<>());
                        hashB2C.get(dateB2C.indexOf(date)).add(0,TBudullaToColombo);
                        System.out.println(dateB2C);
                        System.out.println("hash end"+hashB2C);
//                            System.out.println(hashB2C);
                    }
                    temp.clear();
                    window.close();
                    listOption();
                }
            }else if (Colombo_Budullaverify==1){
                if (ColomboToBudulla.containsValue(username.getText().toLowerCase())) {
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setContentText("enter a unique name");
                    a.show();
                    a.setOnCloseRequest(event1 -> {
                        window.close();
                        addOption(Colombo_Budullaverify,Budulla_Colomboverify,date);
                    });
                }else {
                    if(!dateC2B.contains(date)) {
                        HashMap<String, String> TColomboToBudulla = new HashMap<String, String>();
                        for (String i : temp) {
                            int indexforHash = temp.indexOf(i);
                            if (Colombo_Budullaverify == 1) TColomboToBudulla.put(temp.get(indexforHash), username.getText().toLowerCase());
                            //if(Budulla_Colomboverify==1) BudullaToColombo.put(temp.get(indexforHash),username.getText().toLowerCase());
                        }
                        dateC2B.add(date);
                        System.out.println("C>B" + dateC2B);
                        System.out.println("C>B" + TColomboToBudulla);
                        hashC2B.add(new ArrayList<>());
                        int hashhashindex = dateC2B.size();
                        hashhashindex -= 1;
                        hashC2B.get(hashhashindex).add(0, TColomboToBudulla);
                        System.out.println(hashC2B);
                    }else{
                        System.out.println("pressed");
                        ArrayList<HashMap<String,String>> inti = hashC2B.get(dateC2B.indexOf(date));
                        System.out.println("initi"+inti);
                        HashMap<String,String> TColomboToBudulla = inti.get(0);
                        System.out.println("hash"+TColomboToBudulla);
                        for(String i: temp){
                            int indexforHash = temp.indexOf(i);
                            TColomboToBudulla.put(temp.get(indexforHash),username.getText().toLowerCase());
                        }
                        hashC2B.get(dateC2B.indexOf(date)).clear();
                        System.out.println("get"+hashC2B.get(dateC2B.indexOf(date)));
                        //hashB2C.add(new ArrayList<>());
                        hashC2B.get(dateC2B.indexOf(date)).add(0,TColomboToBudulla);
                        System.out.println(dateC2B);
                        System.out.println("hash end"+hashC2B);
                    }
                    temp.clear();
                    window.close();
                    listOption();
                }
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
            addOption(Colombo_Budullaverify,Budulla_Colomboverify,date);

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
    public void   viewOption(int Colombo_Budullaverify, int Budulla_Colomboverify,LocalDate date){
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
                    if (Colombo_Budullaverify==1)
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
                    if (Budulla_Colomboverify==1)
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
    public void  emptyOption(int Colombo_Budullaverify, int Budulla_Colomboverify,LocalDate date){
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
                    if (Colombo_Budullaverify==1){
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
                    if (Budulla_Colomboverify==1){
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
    public void deleteOption(){
        Scanner scanDName = new Scanner(System.in);
        System.out.println("enter your name:");
        String deleteName= scanDName.next().toLowerCase();

        Scanner scanDSeat = new Scanner(System.in);
        System.out.println("enter your seat:");
        String deleteSeat= scanDSeat.next();

        System.out.println("Name: "+deleteName);
        System.out.println("Seat: "+deleteSeat);

        System.out.println("1| Colombo to Badulla");
        System.out.println("2| Badulla to Colombo");

        Scanner scanDFinal = new Scanner(System.in);
        System.out.println("enter Route ");
        String deleteFinal= scanDFinal.next();

        if (deleteFinal.equals("1"))
        {
            int count=0;
            int deleteCount = 1;
            List<LocalDate> dateList = new ArrayList<>();
            for(LocalDate i: dateC2B)
            {
                for (String j : hashC2B.get(count).get(0).keySet())
                {
                    if (j.equals(deleteSeat))
                    {
                        System.out.println(deleteCount + " | "+i);
                        dateList.add(i);
                        deleteCount++;
                    }
                }
                count++;
            }
            Scanner scFinal = new Scanner(System.in);
            System.out.println("select ");
            int Final= scFinal.nextInt();
            Final-=1;
            int deleteIndex= dateC2B.indexOf(dateList.get(Final));
            System.out.println(hashC2B.get(deleteIndex).get(0));
            hashC2B.get(deleteIndex).get(0).remove(deleteSeat);
            System.out.println(hashC2B.get(deleteIndex).get(0));
        }
        else
        {
            int count=0;
            int deleteCount = 1;
            List<LocalDate> dateList = new ArrayList<>();
            for(LocalDate i: dateB2C)
            {
                for (String j : hashB2C.get(count).get(0).keySet())
                {
                    if (j.equals(deleteSeat))
                    {
                        System.out.println(deleteCount + " | "+i);
                        dateList.add(i);
                        deleteCount++;
                    }
                }
                count++;
            }
            Scanner scFinal = new Scanner(System.in);
            System.out.println("select ");
            int Final= scFinal.nextInt();
            Final-=1;
            System.out.println(dateList);
            System.out.println(Final);
            int deleteIndex= dateB2C.indexOf(dateList.get(Final));
            System.out.println(deleteIndex);
            System.out.println(hashB2C.get(deleteIndex).get(0));
            hashB2C.get(deleteIndex).get(0).remove(deleteSeat);
            System.out.println(hashB2C.get(deleteIndex).get(0));
        }
        waitOption();
    }
    public void   findOption(){
//        getting user name
        Scanner scanFind = new Scanner(System.in);
        System.out.println("enter your name:");
        String findName= scanFind.next().toLowerCase();

        System.out.println("Name: "+findName+"\n");
        int count=0;
        for(LocalDate i: dateC2B) {
            if (hashC2B.get(count).get(0).containsValue(findName))
            {
                System.out.println("Route: Colombo to Badulla");
                System.out.println("Date : "+i);
                System.out.print("Seats: ");
                for (String j : hashC2B.get(count).get(0).keySet())
                {
                    if (hashC2B.get(count).get(0).get(j).equals(findName))
                    {
                        System.out.print(j + "|");
                    }
                }
                System.out.println("\n");
            }
            count++;

        }
        count=0;
        for(LocalDate i: dateB2C) {
            if (hashB2C.get(count).get(0).containsValue(findName))
            {
                System.out.println("Route: Badulla to Colombo");
                System.out.println("Date : "+i);
                System.out.print("Seats: ");
                for (String j : hashB2C.get(count).get(0).keySet())
                {
                    if (hashB2C.get(count).get(0).get(j).equals(findName))
                    {
                        System.out.print(j + "|");
                    }
                }
                System.out.println("\n");
            }
            count++;
        }
        waitOption();
    }
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
                Document userdocument = new Document();
                for(String item: hashC2B.get(dateC2B.indexOf(colombodate)).get(0).keySet())
                {
                    //Document userdocument = new Document();
                    userdocument.append("Seat number",item);
                    userdocument.append("user name",hashC2B.get(dateC2B.indexOf(colombodate)).get(0).get(item));
                    userdocument.append("date",colombodate.toString()); //LocalDate.parse("2019-03-29");
                    userdocument.append("from","Colombo");
                    userdocument.append("to","Badulla");
                }
                colombocollection.insertOne(userdocument);
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
                Document userdocument = new Document();
                for(String item: hashC2B.get(dateC2B.indexOf(colombodate)).get(0).keySet())
                {
                    //Document userdocument = new Document();
                    userdocument.append("Seat number",item);
                    userdocument.append("user name",hashC2B.get(dateC2B.indexOf(colombodate)).get(0).get(item));
                    userdocument.append("date",colombodate.toString()); //LocalDate.parse("2019-03-29");
                    userdocument.append("from","Colombo");
                    userdocument.append("to","Badulla");
                }
                colombocollection.insertOne(userdocument);
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
                Document userdocument = new Document();
                for(String item: hashB2C.get(dateB2C.indexOf(badulladate)).get(0).keySet())
                {
                    //Document userdocument = new Document();
                    userdocument.append("Seat number",item);
                    userdocument.append("user name",hashB2C.get(dateB2C.indexOf(badulladate)).get(0).get(item));
                    userdocument.append("date",badulladate.toString()); //LocalDate.parse("2019-03-29");
                    userdocument.append("from","Badulla");
                    userdocument.append("to","Colombo");
                }
                badullacollection.insertOne(userdocument);
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
                Document userdocument = new Document();
                for(String item: hashB2C.get(dateB2C.indexOf(badulladate)).get(0).keySet())
                {
                    //Document userdocument = new Document();
                    userdocument.append("Seat number",item);
                    userdocument.append("user name",hashB2C.get(dateB2C.indexOf(badulladate)).get(0).get(item));
                    userdocument.append("date",badulladate.toString()); //LocalDate.parse("2019-03-29");
                    userdocument.append("from","Badulla");
                    userdocument.append("to","Colombo");
                }
                badullacollection.insertOne(userdocument);
            }
            System.out.println("restored data for user names & seats");
        }

        dbclient.close();
        System.out.println("saved files");
        waitOption();
    }
    public void testi(){
        System.out.println(hashB2C);
        System.out.println(hashC2B);
    }
    public void   loadOption(){
        System.out.println("datec2b"+dateC2B);
        dateC2B.clear();
        System.out.println("datec2b"+dateC2B);
        System.out.println("hashc2b"+hashC2B);
        hashC2B.clear();
        System.out.println("hashc2b"+hashC2B);
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
//                temphashmap.put(document.getString("Seat number"),document.getString("user name"));
//                hashC2B.add(new ArrayList<>());
//                hashC2B.get(dateC2B.indexOf(LocalDate.parse(document.getString("date")))).add(0,temphashmap);
            }
        }
        System.out.println("datec2b"+dateC2B);
        System.out.println("hashc2b"+hashC2B);
        waitOption();
    }
    public void   oderOption(){/*
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
        waitOption();*/}

    public void   waitOption(){
//        to let the use consume the details of console functions before moving to the menu
        Scanner scanContinue = new Scanner(System.in);
        System.out.println("Press any key to continue");
        String continueConsole=scanContinue.next();
        if (!continueConsole.isEmpty()) listOption();
    }
}