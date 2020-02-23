import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Gui extends Application {
    Stage window;
    String tempuserSeat = "0";
    List<String> seatlist = new ArrayList<>();
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) {
        welcome();
    }
    public void welcome() {
        System.out.println("\nwelcome to ticket booking system \nA/C compartment for Denuwara Menike");
        options();
    }
    public void options() {
        System.out.println("\n\n"+
                "A Add a seat\n"+
                "V View all seats\n"+
                "E View empty seats\n"+
                "D Delete seat\n"+
                "F Find the seat\n"+
                "S Save details\n"+
                "L Load details\n"+
                "O List seats\n");
        optionstest();
    }
    public void optionstest(){
        Scanner scanoption= new Scanner(System.in);
        System.out.println(">> select a option");
        String userOption= scanoption.next().toUpperCase();
        switch (userOption) {
            case "A":           //add
                addoption();
                break;
            case "V":     //view
                viewoption();
                break;
            case "E":     //empty
                emptyoption();
                break;
            case "D":     //delete
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
                break;
        }
    }
    public String getname(String tempuserName) {
        return tempuserName;
    }
    public String getseat(String tempuserSeat) {
        return tempuserSeat;
    }
    public void booking(String tempuserSeat, String tempuserName) {
        if (tempuserName.equals("") && tempuserSeat.equals("")) {
            window.show();
        } else {
            System.out.println(getseat(tempuserSeat) + getname(tempuserName));
            seatlist.add(tempuserSeat);
            window.close();
            options();
        }
    }
    public void confirmAlert(String title, String message, TextField username) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("confirm your seat");
        window.setMinWidth(300);

        Label label = new Label();
        label.setText("seat no: " + message + "\n Username " + title);
        Button closeBut = new Button("Confirm");
        closeBut.setOnAction(e -> {
            username.setText("");
            tempuserSeat = "0";
            window.close();
            booking(message, title);

        });

        Button cancelBut = new Button("Cancel");
        cancelBut.setOnAction(event -> {
            username.setText("");
            window.close();
            //window.show();;
            booking("", "");
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeBut, cancelBut);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
    public void emptyfeildAlert() {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Alert");
        window.setMinWidth(300);

        Label label = new Label();
        label.setText("enter your name and select a seat");

        Button okayBut = new Button("okay");
        okayBut.setOnAction(event -> {
            window.close();
            booking("", "");
            //window.show();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, okayBut);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();

    }
    private void addoption(){
        Stage window = new Stage();
        int number;
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 2, 5, 2));
        grid.setHgap(10);
        grid.setVgap(10);
        Scene sceneone = new Scene(grid, 400, 220);
        window.setScene(sceneone);

        //seat grid
        number = 1;
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 10; c++) {
                if (number != 43) {
                    Button button = new Button(String.valueOf(number));
                    button.setId(String.valueOf(number));
                    button.setOnAction(event -> {
                        if (tempuserSeat.equals("0")) {
                            tempuserSeat = button.getId();
                            button.setStyle("-fx-background-color: #ff0000; ");
                            //getseat(tempuserSeat);
                        } else {
                            if (button.getId().equals(tempuserSeat)) {
                                button.setStyle("");
                                tempuserSeat = "0";
                            }
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
        Button okbut = new Button("ok");
        okbut.setOnAction(event -> {
            if (username.getText().trim().isEmpty() || tempuserSeat.equals("0")) {
                emptyfeildAlert();
                //System.out.println("select seat or name");
            } else {
                String tempuserName = username.getText();
                String tName = getname(tempuserName);
                String tSeat = getseat(tempuserSeat);
                confirmAlert(tName, tSeat, username);
            }
        });
        grid.add(okbut, 7, 6);
        //program close button
        Button closebut = new Button("close");
        closebut.setOnAction(event -> {
            System.out.println("programe will close");
            tempuserSeat = "0";
            window.close();
            options();
        });
        grid.add(closebut, 9, 6);
        window.show();
    }
    public void viewoption(){
        Stage window= new Stage();
        //window.initModality(Modality.NONE);
        int number;
        GridPane gridtwo = new GridPane();
        gridtwo.setPadding(new Insets(5, 2, 5, 2));
        gridtwo.setHgap(10);
        gridtwo.setVgap(10);
        Scene scenetwo = new Scene(gridtwo, 400, 220);
        window.setScene(scenetwo);

        number = 1;
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 10; c++) {
                if (number != 43) {
                    Button button = new Button(String.valueOf(number));
                    button.setId(String.valueOf(number));
                    if (seatlist.contains(String.valueOf(number))){
                        button.setStyle("-fx-background-color: #ff0000; ");
                    }
                    number++;
                    gridtwo.add(button, c, r);
                }
            }
        }

        Button closebutt = new Button("Close");
        closebutt.setOnAction(event -> {
            window.close();
            options();
        });
        gridtwo.add(closebutt,9,6);
        window.show();
    }
    public void emptyoption(){
        Stage window= new Stage();
        //window.initModality(Modality.NONE);
        int number;
        GridPane gridtwo = new GridPane();
        gridtwo.setPadding(new Insets(5, 2, 5, 2));
        gridtwo.setHgap(10);
        gridtwo.setVgap(10);
        Scene scenetwo = new Scene(gridtwo, 400, 220);
        window.setScene(scenetwo);

        number = 1;
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 10; c++) {
                if (number != 43) {
                    Button button = new Button(String.valueOf(number));
                    button.setId(String.valueOf(number));
                    if (seatlist.contains(String.valueOf(number))){
                        button.setText("X");
                        button.setStyle("-fx-background-color: #C0C0C0; ");
                    }else{
                        button.setStyle("");
                    }
                    number++;
                    gridtwo.add(button, c, r);
                }
            }
        }
        Button closebutt = new Button("Close");
        closebutt.setOnAction(event -> {
            window.close();
            options();
        });
        gridtwo.add(closebutt,9,6);
        window.show();
    }
    public void deleteoption(){

    }
    public void findoption(){}
    public void saveoption(){}
    public void loadoption(){}
    public void oderoption(){}
}