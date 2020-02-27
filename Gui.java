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
    List<String> seatList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) {welcome();}
    /*
        public String getName(String confirmUserSeat) {
            return confirmUserSeat;
        }
        public String getSeat(String confirmUserSeat) {
            return confirmUserSeat;
        }

        public void booking(String tempUserSeat, String tempUserName) {
            System.out.println(getSeat(tempUserSeat) + getName(tempUserName));
            seatList.add(tempUserSeat);
            nameList.add((tempUserName));
            options();
        }
        public void confirmAlert(String title, String message, TextField username) {

            confirmBut.setOnAction(e -> {
                username.setText("");
                tempUserSeat = "0";
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
            layout.getChildren().addAll(label, confirmBut, cancelBut);
            layout.setAlignment(Pos.CENTER);

            //Display window and wait for it to be closed before returning
            Scene viewConfi = new Scene(layout);
            window.setScene(viewConfi);
            window.showAndWait();
        }
        public void emptyFieldAlert() {
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
            Scene emptyviewFeild = new Scene(layout);
            window.setScene(emptyviewFeild);
            window.show();

        }
     */
    private void addOption(){
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
                addOption();
            } else {
                nameList.add(username.getText());
                window.close();
                options();
            }
        });
        grid.add(okBut, 7, 6);

        //program close button
        Button closeBut = new Button("close");
        closeBut.setOnAction(event -> window.close());
        grid.add(closeBut, 9, 6);

        window.show();
    }
    public void viewOption(){
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
            options();
        });
        gridTwo.add(cancelBut,9,6);
        window.show();
    }
    public void emptyOption(){
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
            options();
        });

        gridTwo.add(cancelBut,9,6);
        window.show();
    }
    public void deleteOption(){
        System.out.println(seatList);
        System.out.println(nameList);
        Scanner scanSeat = new Scanner(System.in);
        System.out.println("enter your seat number");
        String deleteValues= scanSeat.next();
        int delete= seatList.indexOf(deleteValues);
        seatList.remove(delete);
        nameList.remove(delete);
        options();
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
        optionsTest();
    }
    public void optionsTest(){
        Scanner scanOption= new Scanner(System.in);
        System.out.println(">> select a option");
        String userOption= scanOption.next().toUpperCase();
        switch (userOption) {
            case "A":           //add
                addOption();
                break;
            case "V":     //view
                viewOption();
                break;
            case "E":     //empty
                emptyOption();
                break;
            case "D":     //delete
                deleteOption();
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
                options();
                break;
        }
    }
}
