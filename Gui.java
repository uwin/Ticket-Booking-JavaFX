/*
\ https://stackoverflow.com/questions/29679971/javafx-make-a-grid-of-buttons/29719308
\ https://beginnersbook.com/2019/04/java-program-to-perform-bubble-sort-on-strings/
 */
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Gui extends Application {
    static final int SEATING_CAPACITY = 42;
    public static void main(String[] args) {
        launch();
    }
    @Override
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
            case "A":     //add
                addOption(nameList,seatList);
                break;
            case "V":     //view
                viewOption(nameList,seatList);
                break;
            case "E":     //empty
                emptyOption(nameList,seatList);
                break;
            case "D":     //delete
                deleteOption(nameList,seatList);
                break;
            case "F":     //find
                findOption(nameList,seatList);
                break;
            case "S":     //save
                try {
                    saveOption(nameList, seatList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "L":      //load
                try {
                    loadOption(nameList,seatList);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "O":      //view
                oderOption(nameList,seatList);
                break;
            case "Q":      //quit
                System.exit(0);
            default:
                System.out.println("invalid input");
                listOption(nameList, seatList);
                break;
        }
    }

    public void    addOption(List <String> nameList, List <String> seatList){
        Stage window = new Stage();
        int number;
        List<String> temp = new ArrayList<>();
        temp.add("0");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 2, 5, 2));
        grid.setHgap(10);
        grid.setVgap(10);
        Scene addView = new Scene(grid, 400, 220);
        window.setScene(addView);
        number = 1;
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 10; c++) {
                if (number <=SEATING_CAPACITY) {
                    Button button = new Button(String.valueOf(number));
                    button.setId(String.valueOf(number));
                    if (seatList.contains(button.getId())) button.setStyle("-fx-background-color: #C0C0C0; ");
                    button.setOnAction(event -> {
                        if (seatList.contains(button.getId())){
                            button.setStyle("-fx-background-color: #ff6347; ");
                        }else if (temp.contains("0")){
                            temp.set(0,button.getId());
                            button.setStyle("-fx-background-color: #00ff00; ");
                        }else if(temp.contains(button.getId())){
                                button.setStyle("");
                                temp.set(0,"0");
                            }
                    });
                    number++;
                    grid.add(button, c, r);
                }
            }
        }
        TextField username = new TextField();
        username.setPromptText("enter name");
        grid.add(username, 3, 3, 7, 4);
        Button okBut = new Button("ok");
        okBut.setStyle("-fx-background-color: #00A4B2; ");
        okBut.setOnAction(event -> {
            if (username.getText().trim().isEmpty()|| temp.contains("0")) {
                //open a alert <<<<<<window.close();
                window.close();
                addOption(nameList,seatList);
            } else {
                nameList.add(username.getText());
                seatList.add(temp.get(0));
                temp.set(0,"0");
                System.out.println(nameList+"\n"+seatList);
                window.close();
                listOption(nameList, seatList);
            }
        });
        grid.add(okBut, 7, 6);
        Button closeBut = new Button("close");
        closeBut.setStyle("-fx-background-color: #ab0000; ");
        closeBut.setOnAction(event -> {
            window.close();
            listOption(nameList, seatList);
        });
        grid.add(closeBut, 9, 6);
        window.show();
    }
    public void   viewOption(List <String> nameList, List <String> seatList){
        Stage window= new Stage();
        int number;
        GridPane gridTwo = new GridPane();
        gridTwo.setPadding(new Insets(5, 2, 5, 2));
        gridTwo.setHgap(10);
        gridTwo.setVgap(10);
        Scene viewSeat = new Scene(gridTwo, 400, 220);
        window.setScene(viewSeat);
        number = 1;
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 10; c++) {
                if (number <=SEATING_CAPACITY) {
                    Button button = new Button(String.valueOf(number));
                    button.setId(String.valueOf(number));
                    if (seatList.contains(String.valueOf(number))){
                        button.setStyle("-fx-background-color: #C0C0C0; ");
                    }
                    number++;
                    gridTwo.add(button, c, r);
                }
            }
        }
        Button closeBut = new Button("Close");
        closeBut.setStyle("-fx-background-color: #ab0000; ");
        closeBut.setOnAction(event -> {
            window.close();
            listOption(nameList, seatList);
        });
        gridTwo.add(closeBut,9,6);
        window.show();
    }
    public void  emptyOption(List <String> nameList, List <String> seatList){
        Stage window= new Stage();
        int number;
        GridPane gridTwo = new GridPane();
        gridTwo.setPadding(new Insets(5, 2, 5, 2));
        gridTwo.setHgap(10);
        gridTwo.setVgap(10);
        Scene viewEmpty = new Scene(gridTwo, 400, 220);
        window.setScene(viewEmpty);
        number = 1;
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 10; c++) {
                if (number <=SEATING_CAPACITY) {
                    Button button = new Button(String.valueOf(number));
                    button. setId(String.valueOf(number));
                    if (seatList.contains(String.valueOf(number))){
                        button.setText("X");
                        button.setStyle("-fx-background-color: #A9A9A9; ");
                    }else{
                        button.setStyle("");
                    }
                    number++;
                    gridTwo.add(button, c, r);
                }
            }
        }
        Button closeBut = new Button("Close");
        closeBut.setStyle("-fx-background-color: #ab0000; ");
        closeBut.setOnAction(event -> {
            window.close();
            listOption(nameList, seatList);
        });
        gridTwo.add(closeBut,9,6);
        window.show();
    }
    public void deleteOption(List <String> nameList, List <String> seatList){
        System.out.println(nameList+"\n"+seatList);
        Scanner scanSeat = new Scanner(System.in);
        System.out.println("enter your seat number: ");
        String deleteValues= scanSeat.next();
        if (seatList.contains(deleteValues)){
            int delete= seatList.indexOf(deleteValues);
            seatList.remove(delete);
            nameList.remove(delete);
            System.out.println(nameList+"\n"+seatList);
        }else{
            System.out.println("your seat no is not booked");
            //deleteOption(nameList,seatList);
        }
        consoleWait(nameList,seatList);
    }
    public void   findOption(List <String> nameList, List <String> seatList){
        Scanner scanSeat = new Scanner(System.in);
        System.out.println("enter your name: ");
        String findName= scanSeat.next();
        int delete= nameList.indexOf(findName);
        System.out.println("your seat number is: "+seatList.get(delete));
        consoleWait(nameList,seatList);
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
        consoleWait(nameList,seatList);
    }
    public void   loadOption(List <String> nameList, List <String> seatList) throws FileNotFoundException {
        Scanner scanSeats = new Scanner(new File("seats.txt"));

        while(scanSeats.hasNext())
        {
            seatList.add(scanSeats.next());
        }
        Scanner scanNames = new Scanner(new File("names.txt"));

        while(scanNames.hasNext())
        {
            nameList.add(scanNames.next());
        }
        consoleWait(nameList,seatList);
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
        consoleWait(nameList,seatList);
    }
    public void  consoleWait(List <String> nameList, List <String> seatList){
        Scanner scanContinue = new Scanner(System.in);
        String continueConsole=scanContinue.next();
        if (continueConsole.isEmpty()){
            System.out.println("Press any key to continue");
        }else {
            listOption(nameList, seatList);
        }
    }
}
