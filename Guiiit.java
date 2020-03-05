/*
\open a alert <<<<<<window.close();
\need to flash when reClicked after booked
\
\ https://stackoverflow.com/questions/29679971/javafx-make-a-grid-of-buttons/29719308
\ https://beginnersbook.com/2019/04/java-program-to-perform-bubble-sort-on-strings/
/*
change max size for all buttons in add option
changed stage sizs
added seatminit
changed seat value to a png
*/
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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
    public void start(Stage stage) {
        welcome();
    }
    public void welcome() {
        System.out.println("\nwelcome to ticket booking system \nA/C compartment for Denuwara Menike");
        List<String> seatList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        listOption(nameList, seatList);
    }

    public void   listOption(List <String> nameList, List <String> seatList) {
        System.out.println("\n\n"+
                "A Add a seat\n"+
                "V View all seats\n"+
                "E View empty seats\n"+
                "D Delete seat\n"+
                "F Find the seat\n"+
                "S Save details\n"+
                "L Load details\n"+
                "O List seats\n");
        runOption(nameList,seatList);
    }
    public void    runOption(List <String> nameList, List <String> seatList){
        Scanner scanOption= new Scanner(System.in);
        System.out.println(">> select a option");
        String userOption= scanOption.next().toUpperCase();
        switch (userOption) {
            case "A":
                addOption(nameList,seatList);
                break;
            case "V":
                viewOption(nameList,seatList);
                break;
            case "E":
                emptyOption(nameList,seatList);
                break;
            case "D":
                deleteOption(nameList,seatList);
                break;
            case "F":
                findOption(nameList,seatList);
                break;
            case "S":
                try {
                    saveOption(nameList, seatList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "L":
                try {
                    loadOption(nameList,seatList);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "O":
                oderOption(nameList,seatList);
                break;
            case "Q":
                System.exit(0);
            default:
                System.out.println("invalid input");
                listOption(nameList, seatList);
                break;
        }
    }
    public void    addOption(List <String> nameList, List <String> seatList){
//      create the stage
        Stage window = new Stage();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 2, 5, 2));
        grid.setHgap(10);
        grid.setVgap(10);
        Scene addView = new Scene(grid, 770, 420);
        window.setScene(addView);
        window.show();

//      values needed for the loop
        int number = 1;
        List<String> temp = new ArrayList<>();
        Image seatBlack = new Image(getClass().getResourceAsStream("seatminit.png"));

//      loop to create seat buttons
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 10; c++) {
                if (number <=SEATING_CAPACITY) {
                    Button button = new Button(""/*String.valueOf(number)*/, new ImageView(seatBlack));
                    button.setId(String.valueOf(number));
                    //button.setMaxSize(60, 60);
//                    change seat colour to orange if it's already booked
                    if (seatList.contains(button.getId())) {
                        button.setStyle("-fx-background-color: #ff6347; ");
                    }
                    button.setOnAction(event -> {
//                      flash the seat colour if the user tries to click a already booked seat
                        if (seatList.contains(button.getId())){
                            button.setStyle("-fx-background-color: #ff6347; "); //redundent need a flash
//                      if the seat is not booked add the seat to the temporary seatlist,change colour to green
                        }else if(!temp.contains(button.getId())){
                            button.setStyle("-fx-background-color: #00ff00; ");
                            temp.add(button.getId());
//                      if the user again clicks a already booked seat, remove it from the temp booked list, revert colour
                        }else if (temp.contains(button.getId())){
                            temp.remove(button.getId());
                            button.setStyle("");
                        }
                    });
                    number++;
                    grid.add(button, c, r);
                }
            }
        }

//      space for user name
        TextField username = new TextField();
        username.setPromptText("enter name");
        grid.add(username, 3, 3, 7, 4);

//      Comfirm button
        Button okBut = new Button("ok");
        //okBut.setMaxSize(60, 60);
        okBut.setStyle("-fx-background-color: #00A4B2; ");
        okBut.setOnAction(event -> {
//            if (username.getText().trim().isEmpty()|| temp.contains("0")) {
            if (username.getText().trim().isEmpty()|| temp.isEmpty()) {
                //open a alert <<<<<<window.close();
                window.close();
                addOption(nameList,seatList);
            } else {
                for (String ignored : temp) {
                    nameList.add(username.getText());
                }
//                seatList.add(temp.get(0));
                seatList.addAll(temp);
//                temp.set(0,"0");
                temp.clear();
                System.out.println(nameList+"\n"+seatList);
                window.close();
                listOption(nameList, seatList);
            }
        });
        grid.add(okBut, 7, 6);

//      close buttton
        Button closeBut = new Button("close");
        //closeBut.setMaxSize(60, 60);
        closeBut.setStyle("-fx-background-color: #ab0000; ");
        closeBut.setOnAction(event -> {
            window.close();
            listOption(nameList, seatList);
        });
        grid.add(closeBut, 9, 6);
    }
    public void   viewOption(List <String> nameList, List <String> seatList){
//      create the stage
        Stage window= new Stage();
        GridPane gridTwo = new GridPane();
        gridTwo.setPadding(new Insets(5, 2, 5, 2));
        gridTwo.setHgap(10);
        gridTwo.setVgap(10);
        Scene viewSeat = new Scene(gridTwo, 770, 420);
        window.setScene(viewSeat);
        window.show();

//      values needed for the loop
        int number = 1;
        Image seatBlack = new Image(getClass().getResourceAsStream("seatminit.png"));

//      loop to create seat buttons
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 10; c++) {
                if (number <=SEATING_CAPACITY) {
                    Button button = new Button(""/*String.valueOf(number)*/, new ImageView(seatBlack));
                    //button.setMaxSize(60, 60);
//                    if seat is booked the seat button is greyed out
                    if (seatList.contains(String.valueOf(number))){
                        button.setStyle("-fx-background-color: #C0C0C0; ");
                    }
                    number++;
                    gridTwo.add(button, c, r);
                }
            }
        }

//      close buttton
        Button closeBut = new Button("Close");
        //closeBut.setMaxSize(60, 60);
        closeBut.setStyle("-fx-background-color: #ab0000; ");
        closeBut.setOnAction(event -> {
            window.close();
            listOption(nameList, seatList);
        });
        gridTwo.add(closeBut,9,6);

    }
    public void  emptyOption(List <String> nameList, List <String> seatList){
//      create the stage
        Stage window= new Stage();
        GridPane gridTwo = new GridPane();
        gridTwo.setPadding(new Insets(5, 2, 5, 2));
        gridTwo.setHgap(10);
        gridTwo.setVgap(10);
        Scene viewEmpty = new Scene(gridTwo, 770, 420);
        window.setScene(viewEmpty);
        window.show();

//      values needed for the loop
        int number = 1;
        Image seatBlack = new Image(getClass().getResourceAsStream("seatminit.png"));

//      loop to create seat buttons
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 10; c++) {
                if (number <=SEATING_CAPACITY) {
                    Button button = new Button(""/*String.valueOf(number)*/, new ImageView(seatBlack));
//                  if the seat is booked nothing will be done
//                    if the seat is not booked the seat will be done
                    if (!seatList.contains(String.valueOf(number))) gridTwo.add(button, c, r);
                    number++;
                }
            }
        }

//      close buttton
        Button closeBut = new Button("Close");
        //closeBut.setMaxSize(60, 60);
        closeBut.setStyle("-fx-background-color: #ab0000; ");
        closeBut.setOnAction(event -> {
            window.close();
            listOption(nameList, seatList);
        });
        gridTwo.add(closeBut,9,6);
    }
    public void deleteOption(List <String> nameList, List <String> seatList){
        System.out.println(nameList+"\n"+seatList);
        Scanner scanSeat = new Scanner(System.in);
        System.out.println("enter your name: ");
        String deleteValue= scanSeat.next();
        if (nameList.contains(deleteValue)){
            for (int i=0;i<nameList.size();i++){
                if(nameList.get(i).equals(deleteValue)) {
                    System.out.print(seatList.get(i)+"| ");
                }
            }
            Scanner scanDelete = new Scanner(System.in);
            System.out.println();
            System.out.println("enter your seat number: ");
            String deleteSeat= scanDelete.next();

            int deleteFinal= seatList.indexOf(deleteSeat);
            seatList.remove(deleteFinal);
            nameList.remove(deleteFinal);
            System.out.println(nameList+"\n"+seatList);
        }else{
            System.out.println("your have no seats booked");
            deleteOption(nameList,seatList);
        }
        waitOption(nameList,seatList);
    }
    public void   findOption(List <String> nameList, List <String> seatList){
        Scanner scanSeat = new Scanner(System.in);
        System.out.println("enter your name: ");
        String findName= scanSeat.next();
        int delete= nameList.indexOf(findName);
        System.out.println("your seat number is: "+seatList.get(delete));
        waitOption(nameList,seatList);
    }
    public void   saveOption(List <String> nameList, List <String> seatList) throws IOException {
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
        waitOption(nameList,seatList);
    }
    public void   loadOption(List <String> nameList, List <String> seatList) throws FileNotFoundException {
        Scanner scanSeats = new Scanner(new File("seats.txt"));
        while(scanSeats.hasNext()) seatList.add(scanSeats.next());
        Scanner scanNames = new Scanner(new File("names.txt"));
        while(scanNames.hasNext()) nameList.add(scanNames.next());
        waitOption(nameList,seatList);
    }
    public void   oderOption(List <String> nameList, List <String> seatList){
        String sortTemp;
        System.out.println("Strings in sorted order:");
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
        waitOption(nameList,seatList);
    }
    public void   waitOption(List <String> nameList, List <String> seatList){
        Scanner scanContinue = new Scanner(System.in);
        System.out.println("Press any key to continue");
        String continueConsole=scanContinue.next();
        if (!continueConsole.isEmpty()) listOption(nameList, seatList);
    }
}