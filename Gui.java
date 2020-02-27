import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Gui extends Application {
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) {
        welcome();
    }
    public void addOption(List nameList, List seatList){
        Stage window = new Stage();
        int number;
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 2, 5, 2));
        grid.setHgap(10);
        grid.setVgap(10);
        Scene addView = new Scene(grid, 400, 220);
        window.setScene(addView);

        //seat grid
        number = 1;
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 10; c++) {
                if (number != 43) {
                    Button button = new Button(String.valueOf(number));
                    button.setId(String.valueOf(number));
                    if (seatList.contains(button.getId())) button.setStyle("-fx-background-color: #ff0000; ");
                    button.setOnAction(event -> {
                        if (seatList.contains(button.getId())){
                            button.setStyle("");
                        } else {
                            seatList.add(button.getId());
                            button.setStyle("-fx-background-color: #ff0000; ");
                        }
                    });
                    number++;
                    grid.add(button, c, r);
                }
            }
        }
        //username field
        TextField username = new TextField();
        username.setPromptText("enter name");
        grid.add(username, 3, 3, 7, 4);

        //booking confirm button
        Button okBut = new Button("ok");
        okBut.setOnAction(event -> {
            if (username.getText().trim().isEmpty()) {
                //open a alert <<<<<<window.close();
                window.close();
                addOption(nameList,seatList);
            } else {
                nameList.add(username.getText());
                window.close();
                options(nameList, seatList);
            }
        });
        grid.add(okBut, 7, 6);

        //program close button
        Button closeBut = new Button("close");
        closeBut.setOnAction(event -> window.close());
        grid.add(closeBut, 9, 6);

        window.show();
    }
    public void viewOption(List nameList, List seatList){
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
                if (number != 43) {
                    Button button = new Button(String.valueOf(number));
                    button.setId(String.valueOf(number));
                    if (seatList.contains(String.valueOf(number))){
                        button.setStyle("-fx-background-color: #ff0000; ");
                    }
                    number++;
                    gridTwo.add(button, c, r);
                }
            }
        }

        Button cancelBut = new Button("Close");
        cancelBut.setOnAction(event -> {
            window.close();
            options(nameList, seatList);
        });
        gridTwo.add(cancelBut,9,6);
        window.show();
    }
    public void emptyOption(List nameList, List seatList){
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
                if (number != 43) {
                    Button button = new Button(String.valueOf(number));
                    button. setId(String.valueOf(number));
                    if (seatList.contains(String.valueOf(number))){
                        button.setText("X");
                        button.setStyle("-fx-background-color: #C0C0C0; ");
                    }else{
                        button.setStyle("");
                    }
                    number++;
                    gridTwo.add(button, c, r);
                }
            }
        }
        Button cancelBut = new Button("Close");
        cancelBut.setOnAction(event -> {
            window.close();
            options(nameList, seatList);
        });

        gridTwo.add(cancelBut,9,6);
        window.show();
    }
    public void deleteOption(List nameList, List seatList){
        System.out.println(seatList);
        System.out.println(nameList);
        Scanner scanSeat = new Scanner(System.in);
        System.out.println("enter your seat number");
        String deleteValues= scanSeat.next();
        int delete= seatList.indexOf(deleteValues);
        seatList.remove(delete);
        nameList.remove(delete);
        options(nameList, seatList);
    }
    public void welcome() {
        System.out.println("\nwelcome to ticket booking system \nA/C compartment for Denuwara Menike");
        List<String> seatList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        options(nameList, seatList);
    }
    public void options(List nameList, List seatList) {
        System.out.println("\n\n"+
                "A Add a seat\n"+
                "V View all seats\n"+
                "E View empty seats\n"+
                "D Delete seat\n"+
                "F Find the seat\n"+
                "S Save details\n"+
                "L Load details\n"+
                "O List seats\n");
        optionsTest(nameList,seatList);
    }
    public void optionsTest(List nameList, List seatList){
        Scanner scanOption= new Scanner(System.in);
        System.out.println(">> select a option");
        String userOption= scanOption.next().toUpperCase();
        switch (userOption) {
            case "A":           //add
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
                break;
            case "S":     //save
                break;
            case "L":      //load
                break;
            case "O":      //view
                break;
            case "Q":
                System.exit(0);
            default:
                System.out.println("invalid input");
                options(nameList, seatList);
                break;
        }
    }
}