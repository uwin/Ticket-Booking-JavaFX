//test
/*
\need to flash when reClicked after booked
\
\check for duplicates when loading and saving
\
\ https://stackoverflow.com/questions/29679971/javafx-make-a-grid-of-buttons/29719308
\ https://beginnersbook.com/2019/04/java-program-to-perform-bubble-sort-on-strings/
*/
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
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
    ArrayList<ArrayList<HashMap>> hashC2B = new ArrayList<>();
    ArrayList<ArrayList<HashMap>> hashB2C = new ArrayList<>();
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
        datePick.setValue(LocalDate.now());
        gridFirst.add(datePick, 13, 12,18,4);

                //        continue button
        Button toBudulla = new Button("From Colombo");
        toBudulla.setMaxSize(120, 60);
        toBudulla.setOnAction(event -> {
            int Colombo_Budullaverify=1;
            int Budulla_Colomboverify=0;
             LocalDate date=datePick.getValue();
            System.out.println("c2b"+hashC2B);
            window.close();
            if (userOption.toLowerCase().equals("a")) addOption(Colombo_Budullaverify,Budulla_Colomboverify,date);
            else if (userOption.toLowerCase().equals("v")) viewOption(Colombo_Budullaverify,Budulla_Colomboverify,date);
            else if (userOption.toLowerCase().equals("e")) emptyOption(Colombo_Budullaverify,Budulla_Colomboverify,date);
        });
        gridFirst.add(toBudulla,35, 16,10,12);

//                continue button
        Button toColombo = new Button("From Budulla");
        toColombo.setMaxSize(120, 60);
        toColombo.setOnAction(event -> {
            int Budulla_Colomboverify=1;
            int Colombo_Budullaverify=0;
            System.out.println("b2c"+hashB2C);
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
        Image seatBlack = new Image(getClass().getResourceAsStream("black.png"));
        Image seatRed = new Image(getClass().getResourceAsStream("red.png"));
        Image seatGreen = new Image(getClass().getResourceAsStream("green.png"));

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
                    button.setId(String.valueOf(number));
//                    change seat colour to red if it's already booked

                    if(Colombo_Budullaverify==1) {
                        if (ColomboToBudulla.containsKey(button.getId())) {
                            button.setImage(seatRed);
                        }
                        if (temp.contains(button.getId())) {
                            button.setImage(seatGreen);
                        }
                        button.setOnMouseClicked(event -> {
                            //                      flash the seat colour if the user tries to click a already booked seat
                            if (ColomboToBudulla.containsKey(button.getId())) {
                                button.setImage(seatRed);
                                //                      if the seat is not booked add the seat to the temporary seatList,change colour to green
                            } else if (!temp.contains(button.getId())) {
                                button.setImage(seatGreen);
                                temp.add(button.getId());
                                //                      if the user again clicks a already booked seat, remove it from the temp booked list, revert colour
                            } else if (temp.contains(button.getId())) {
                                temp.remove(button.getId());
                                button.setImage(seatBlack);
                            }
                        });
                    } else if(Budulla_Colomboverify==1){
                        if (BudullaToColombo.containsKey(button.getId())) {button.setImage(seatRed);}
                        if (temp.contains(button.getId())) {button.setImage(seatGreen);}
                        button.setOnMouseClicked(event -> {
                            //                      flash the seat colour if the user tries to click a already booked seat
                            if (BudullaToColombo.containsKey(button.getId())){
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
                    HashMap<String, String> TBudullaToColombo = new HashMap<String, String>();
                    for (String i : temp) {
                        int indexforHash=temp.indexOf(i);
                        //if(Colombo_Budullaverify==1) ColomboToBudulla.put(temp.get(indexforHash),username.getText().toLowerCase());
                        if(Budulla_Colomboverify==1) TBudullaToColombo.put(temp.get(indexforHash),username.getText().toLowerCase());
                    }
                    dateB2C.add(date);
                    System.out.println("B>C"+dateB2C);
                    //System.out.println("C>B"+TColomboToBudulla);
                    System.out.println("B>C"+TBudullaToColombo);
                    hashB2C.add(new ArrayList<>());
                    int hashhashindex= dateB2C.size();
                    hashhashindex-=1;
                    hashB2C.get(hashhashindex).add(0, TBudullaToColombo);
                    //hashB2C.get(hashhashindex).add(1, date);
                    //System.out.println(hashB2C);
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
                    HashMap<String, String> TColomboToBudulla = new HashMap<String, String>();
                    for (String i : temp) {
                        int indexforHash=temp.indexOf(i);
                        if(Colombo_Budullaverify==1) TColomboToBudulla.put(temp.get(indexforHash),username.getText().toLowerCase());
                        //if(Budulla_Colomboverify==1) BudullaToColombo.put(temp.get(indexforHash),username.getText().toLowerCase());
                    }
                    dateC2B.add(date);
                    System.out.println("C>B"+dateC2B);
                    System.out.println("C>B"+TColomboToBudulla);
                    hashC2B.add(new ArrayList<>());
                    int hashhashindex= dateC2B.size();
                    hashhashindex-=1;
                    hashC2B.get(hashhashindex).add(0, TColomboToBudulla);
                    System.out.println(hashC2B);
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
            //Colombo_Budullaverify=0;
            //Budulla_Colomboverify=0;
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
            //Colombo_Budullaverify=0;
            //Budulla_Colomboverify=0;
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
        Image seatBlack = new Image(getClass().getResourceAsStream("black.png"));
        Image seatGrey = new Image(getClass().getResourceAsStream("grey.png"));

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
                    if (Colombo_Budullaverify==1){
                    if (ColomboToBudulla.containsKey(String.valueOf(number))) button.setImage(seatGrey);}
                    if (Budulla_Colomboverify==1){
                    if (BudullaToColombo.containsKey(String.valueOf(number))) button.setImage(seatGrey);}
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
                    if (Colombo_Budullaverify==1){
                        if (!ColomboToBudulla.containsKey(String.valueOf(number)))
                        {
                            grid.add(button, c, r);
                            grid.add(num, c, r);
                        }
                    }
                    if (Budulla_Colomboverify==1){
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
            window.close();
            listOption();
        });
        grid.add(closeBut,14,6,14,6);
    }
    public void deleteOption(){
        Scanner scanTrain = new Scanner(System.in);
        System.out.println("select Train:");
        System.out.println("1| Colombo To Budulla");
        System.out.println("2| Budulla To Colombo");
        String Train= scanTrain.next();

        if (Train.equals("1")){

        List<String> deleteList = new ArrayList<>();
        //for (String i : ColomboToBudulla.keySet()) {
                //System.out.println( i + " |" + ColomboToBudulla.get(i));
        //}
//        getting user name
        Scanner scanName = new Scanner(System.in);
        System.out.println("enter your name:");
        String deleteName= scanName.next();
        if (ColomboToBudulla.containsValue(deleteName.toLowerCase())) {
//            printing all the seats for the user name in a row
            for (String i : ColomboToBudulla.keySet()) {
                if (ColomboToBudulla.get(i).equals(deleteName)){
                    System.out.println(i + " |");
                    deleteList.add(i);
                }
            }
//            looping till a valid seat is given
            while (true){
//                getting user to select a seat number
                Scanner scanDelete = new Scanner(System.in);
                System.out.println("enter your seat number:");
                String deleteSeat = scanDelete.next();

                if (deleteList.contains(String.valueOf(deleteSeat))){
                    for (String i : ColomboToBudulla.keySet()) {
                        if (i.equals(deleteSeat)){
                            //System.out.println("bfE"+ColomboToBudulla);
                            System.out.println("[REMOVED] Seat : "+i);
                            ColomboToBudulla.remove(i);
                            ///System.out.println("afE"+ColomboToBudulla);
                            break;
                        }
                }
/*                for (String i : ColomboToBudulla.keySet()) {
                        System.out.println( i + " |" + ColomboToBudulla.get(i));
                }*/
                //deleteList.clear();
                waitOption();
                break;
//                    quit for entering seat number
                }else if(deleteSeat.toLowerCase().equals("q")){
                    waitOption();
                    break;
                }
                System.out.println("the seat no is not booked");
            }
//            quit for entering name
        }else if(deleteName.toLowerCase().equals("q")){
            waitOption();
        }else {
//            looping until a valid user name is given
            System.out.println("your have no seats booked");
            deleteOption();
        }
        }
        else if (Train.equals("2")){

        List<String> deleteList = new ArrayList<>();
        //for (String i : BudullaToColombo.keySet()) {
        //        System.out.println( i + " |" + BudullaToColombo.get(i));
        //}
//        getting user name
        Scanner scanName = new Scanner(System.in);
        System.out.println("enter your name:");
        String deleteName= scanName.next();
        if (BudullaToColombo.containsValue(deleteName.toLowerCase())) {
//            printing all the seats for the user name in a row
            for (String i : BudullaToColombo.keySet()) {
                if (BudullaToColombo.get(i).equals(deleteName)){
                    System.out.println( i + " |");
                    deleteList.add(i);
                }
            }
//            looping till a valid seat is given
            while (true){
//                getting user to select a seat number
                Scanner scanDelete = new Scanner(System.in);
                System.out.println("enter your seat number:");
                String deleteSeat = scanDelete.next();

                if (deleteList.contains(String.valueOf(deleteSeat))){
                    for (String i : BudullaToColombo.keySet()) {
                        if (i.equals(deleteSeat)){
                            //System.out.println("bfE"+BudullaToColombo);
                            System.out.println("[REMOVED] Seat : "+i);
                            BudullaToColombo.remove(i);
                            //System.out.println("afE"+BudullaToColombo);
                            break;
                        }
                }
/*                for (String i : BudullaToColombo.keySet()) {
                        System.out.println( i + " |" + BudullaToColombo.get(i));
                }*/
                //deleteList.clear();
                waitOption();
                break;
//                    quit for entering seat number
                }else if(deleteSeat.toLowerCase().equals("q")){
                    waitOption();
                    break;
                }
                System.out.println("the seat no is not booked");
            }
//            quit for entering name
        }else if(deleteName.toLowerCase().equals("q")){
            waitOption();
        }else {
//            looping until a valid user name is given
            System.out.println("your have no seats booked");
            deleteOption();
        }
        }
        else if (Train.toLowerCase().equals("q")) {
            waitOption();
        }
        else deleteOption();
    }
    public void   findOption(){
//        getting user name
        Scanner scanTrain = new Scanner(System.in);
        System.out.println("select Train:");
        System.out.println("1| Colombo To Budulla");
        System.out.println("2| Budulla To Colombo");
        String Train= scanTrain.next();

        if (Train.equals("1")){
            Scanner scanFind = new Scanner(System.in);
            System.out.println("enter your name:");
            String findName= scanFind.next();
    //        printing all seat values for the given name
            if (ColomboToBudulla.containsValue(findName)){
            for (String i : ColomboToBudulla.keySet()) {
                if (ColomboToBudulla.get(i).equals(findName)){
                    System.out.println( i + " |" );
                }
            }
                System.out.println();
                waitOption();
            }else if(findName.toLowerCase().equals("q")){
                waitOption();
            }else{
    //            looping till a valid user name is given
                findOption();
            }
        }
        else if (Train.equals("2")){
            Scanner scanFind = new Scanner(System.in);
            System.out.println("enter your name:");
            String findName= scanFind.next();
    //        printing all seat values for the given name
            if (BudullaToColombo.containsValue(findName)){
            for (String i : BudullaToColombo.keySet()) {
                if (BudullaToColombo.get(i).equals(findName)){
                    System.out.println( i + " |" );
                }
            }
                System.out.println();
                waitOption();
            }else if(findName.toLowerCase().equals("q")){
                waitOption();
            }else{
    //            looping till a valid user name is given
                findOption();
            }
        }
        else if (Train.toLowerCase().equals("q")) {
            waitOption();
        }
        else findOption();
    }
    public void   saveOption() throws IOException {/*
//        print all the booked seats along with user name
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
                FileWriter save = new FileWriter("src/Data.txt");
                for(String i: seatList){
                    int index= seatList.indexOf(i);
                    save.write(seatList.get(index)+"-"+nameList.get(index)+"\n");

                }
                save.close();
                waitOption();
                break;
            }else if(saveSeat.toLowerCase().equals("q")||saveSeat.toLowerCase().equals("n")||saveSeat.toLowerCase().equals("no")){
                listOption();
                break;
            }
        }*/
    }
    public void   loadOption() throws FileNotFoundException {/*
        while (true){
            Scanner scanLoad =new Scanner(System.in);
            System.out.println("\n load Bookings? (y/n)");
            String loadSeat = scanLoad.next();
            if(loadSeat.toLowerCase().equals("y")){
                Scanner Load= new Scanner(new File("src/Data.txt"));
                while (Load.hasNextLine()){
                    String line = Load.nextLine();
                    String [] separate= line.split("-");
                    seatList.add(separate[0]);
                    nameList.add(separate[1]);
                }
//                print all the names & seats loaded
                for (String i : seatList){
                    System.out.print(i+"|");
                    System.out.print(nameList.get(seatList.indexOf(i)));
                    System.out.println();
                }
                System.out.println("\n"+seatList.size()+" Bookings Loaded");
                waitOption();
                break;
            }else if(loadSeat.toLowerCase().equals("q")||loadSeat.toLowerCase().equals("n")||loadSeat.toLowerCase().equals("no")){
                listOption();
                break;
            }
        }*/
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
        waitOption();*/
    }
    public void   waitOption(){
//        to let the use consume the details of console functions before moving to the menu
        Scanner scanContinue = new Scanner(System.in);
        System.out.println("Press any key to continue");
        String continueConsole=scanContinue.next();
        if (!continueConsole.isEmpty()) listOption();
    }
}