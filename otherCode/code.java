package otherCode;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Scanner;

public class code extends Application {

    public static void main (String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Greetings();
    }
    public void Greetings() {
        selectionMenu();
    }
    public void selectionMenu() {
        Scanner src = new Scanner(System.in);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Hello and Welcome to our Denuwara Menike Train Reservation Program");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        System.out.println("A: Add A Customer To A Seat.");
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("E: Display Empty Seats.");
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("V: View all Seats.");
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("D: Delete customer from seat.");
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("F: Find the seat from a given customer name.");
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("S: Store program data in to file.");
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("L: Load program data from file.");
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("O: View seats Ordered alphabetically by name.");
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("Q: Quit from the program.");
        System.out.println("---------------------------------------------------------------------------------------\n");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        String myChoice = "";
        while (!myChoice.equals("Q")) {
            System.out.print("Please select one of the options: ");
            String choice = src.next();
            myChoice = choice.toUpperCase();
            switch (myChoice) {
                case "A":
                    System.out.println("Add a customer to seat");
                    addingSeat();
                    break;
                case "E":
                    System.out.println("Display empty seats.");
                    break;
                case "V":
                    System.out.println("View all seats.");
                    break;
                case "D":
                    System.out.println("Delete customer from seat.");
                    break;
                case "F":
                    System.out.println("Find the seat.");
                    break;
                case "S":
                    System.out.println("Store program data.");
                    break;
                case "L":
                    System.out.println("Load program data.");
                    break;
                case "O":
                    System.out.println("Alphabetically order.");
                    break;
                case "Q":
                    break;
                default:
                    System.out.println("Invalid Option");
                    break;
            }
        }

    }
    public void addingSeat () {
        Stage window = new Stage();
        Scene scene;

        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(20,20,20,20));
        gridpane.setHgap(20);
        gridpane.setVgap(20);
        int number=1;
        for (int i = 0; i<7; i++) {
            for (int j=0; j<6; j++) {
                //if
                Button btn = new Button(String.valueOf(number));
                btn.setText(String.valueOf(number));
                //btn.setOnAction(event -> );
                gridpane.add(btn,j,i);
                number++;
            }
        }
        scene = new Scene(gridpane,500,500);
        window.setScene(scene);
        window.showAndWait();
        //System.out.println("Im the end");

    }
}