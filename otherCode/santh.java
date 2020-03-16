package otherCode;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class santh extends Application {

    static final int SEATING_CAPACITY = 42;
    public ArrayList<String> bookedSeats = new ArrayList<>(SEATING_CAPACITY);
    public ArrayList<String> bookedNames = new ArrayList<>(SEATING_CAPACITY);
    private ArrayList<String> phoneNumber = new ArrayList<>(SEATING_CAPACITY);
    private ArrayList<String> orderedNames = new ArrayList<>(SEATING_CAPACITY);

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        welcome();
    }

    //Basic Console Menu
    private void welcome() {
        displayOptions();
    }
    private void displayOptions() {
        System.out.println("Hello Welcome to Train Booking System " +
                "\n==============================================" +
                "\n**********************************************");
        System.out.println("Press Q : Quit the Booking" +
                "\nPress E :Display Empty seats " +
                "\nPress D : Delete the clicked Seats " +
                "\nPress F: Find seats " +
                "\nPress S : store the data " +
                "\nPress L :Load from file " +
                "\nPress O : view Seats Alphabetllcally");
        System.out.println("==============================================" +
                "\n**********************************************");
        customerSelectOption();
    }
    private void customerSelectOption() {
        Scanner usrInput = new Scanner(System.in);
        System.out.print("Enter Your Option :");
        String customerOption = usrInput.next().toLowerCase();
        switch (customerOption) {
            case ("Q"):
            case ("q"):
                System.out.println("Train booking System is Quitting..");
                System.out.println("Thank you for Using Our Train Booking System");
                System.out.println("Hello Welcome to Train Booking System " +
                        "\n==============================================" +
                        "\n**********************************************");
                System.out.println("All the Booking saved to the File... ");
                System.exit(0);

            case ("V"):
            case ("v"):
                System.out.println("View All Seats Command is Executing...");
                viewAllSeats(bookedSeats);
                break;

            case ("A"):
            case ("a"):
                System.out.println("Add Customers to Seat Command is Executing...");
                addCustomerToSeats(bookedSeats, bookedNames);
                break;

            case ("E"):
            case ("e"):
                System.out.println("View Empty Seats Command is Executing...");
                viewEmptySeats(bookedSeats);
                break;

            case ("F"):
            case ("f"):
                System.out.println("Find Seats Command is Executing...  ");
                findSeatsOfCustomer(bookedSeats, bookedNames);
                break;

            case ("D"):
            case ("d"):
                System.out.println("Delete Customer Command is Executing... ");
                deleteCustomerSeats(bookedSeats, bookedNames);
                break;

            case ("S"):
            case ("s"):
                System.out.println("Your Details Being Saved To a File... ");
                saveToFile();
                break;

            case ("L"):
            case ("l"):
                System.out.println("Loading From File... ");
                loadFromFile();
                break;

            case ("O"):
            case ("o"):
                System.out.println("View All booking Command is Executing... ");
                sortBooking(bookedNames, bookedSeats, orderedNames);
                break;
            //Error Handles for Option Other than Given Ones.
            default:
                System.out.println("Invalid Option Please Choose the Right Option...");
                welcome();
                break;
        }
    }
    private void createButtons(GridPane grid, int i, Button btn) {
        //Creating Button as Row By Row.
        //Total seats number define AS static final int SEATING_CAPACITY
        int row1 = 11;
        int row2 = 11;
        int row3 = 10;
        int col1 = 4;
        int col2 = 6;
        int col3 = 26;
        int col4 = 28;
        if (i <= row1) {
            grid.add(btn, col1, i);
        } else if (i <= (row1 + row2)) {
            grid.add(btn, col2, (i - row1));
        } else if (i <= (row1 + row2 + row3)) {
            grid.add(btn, col3, (i - (row1 + row2)));
        } else {
            grid.add(btn, col4, (i - (row1 + row2 + row3)));
        }
    }

    //GUI
    private void viewAllSeats(ArrayList<String> bookedSeats) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        //Creating Buttons
        for (int i = 1; i <= SEATING_CAPACITY; i++) {
            Button btn = new Button("A" + i);
            btn.setId(String.valueOf(i));
            btn.setPadding(new Insets(10, 10, 10, 10));
            if (bookedSeats.contains(btn.getId())) {
                btn.setStyle("-fx-background-color: red");
                btn.setDisable(true);
            }
            createButtons(grid, i, btn);
        }

        Button btnQuit = new Button();
        btnQuit.setId("quit");
        btnQuit.setText("Quit");
        grid.add(btnQuit, 14, 20);
        btnQuit.setOnAction(event -> {
            Stage stage1 = (Stage) btnQuit.getScene().getWindow();
            stage1.close();
            welcome();
            System.out.println("Are you Sure You want to Quit? \n Press q to Quit..");
        });

        Button btnContinue = new Button();
        btnContinue.setId("continue");
        btnContinue.setText("Continue --->");
        grid.add(btnContinue, 16, 20);
        btnContinue.setOnAction(event -> {
            Stage stage1 = (Stage) btnContinue.getScene().getWindow();
            stage1.close();
            welcome();
        });

        Stage stage1 = new Stage();
        stage1.setTitle("Train Booking System");
        Scene viewScene = new Scene(grid, 600, 800);
        stage1.setScene(viewScene);
        stage1.setOnCloseRequest(event->{
            stage1.close();
            welcome();
        });
        stage1.show();
    }
    private void addCustomerToSeats(ArrayList<String> bookedSeats, ArrayList<String> namesList) {
        //Temporary Array to save Selected Seats by Customer
        ArrayList<String> selectSeats = new ArrayList<>();
        //Creating 2nd Stage
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 30, 0, 30));

        //Creating Buttons 42 Times
        for (int i = 1; i <= SEATING_CAPACITY; i++) {
            Button btn = new Button("A" + i);
            btn.setId(String.valueOf(i));
            btn.setPadding(new Insets(10, 10, 10, 10));
//            for(int j =0 ; j<42;j++) {
//                if (bookedSeats.get(j).contains(btn.getId()))) {
            if (bookedSeats.contains(btn.getId())) {
                btn.setStyle("-fx-background-color: red;");
                btn.setDisable(true);
            }
//            }
            //method of adding Buttons In grid.a
            createButtons(grid, i, btn);
            //check whether seat Number in Temporary Array. It add and remove element form list So it give more User Accessibility.
            btn.setOnAction(event -> {
                if (!selectSeats.contains(btn.getId())) {
                    selectSeats.add(btn.getId());
                    System.out.println(selectSeats);
                    // printSeats(selectSeats);
                    btn.setStyle("-fx-background-color: red;");
                } else {
                    selectSeats.remove(btn.getId());
                    btn.setStyle("");
                }
                System.out.println(selectSeats);
            });
        }

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(grid);

        DatePicker datePicker = new DatePicker();
        //  datePicker.setValue(LocalDate.now());
        AnchorPane.setBottomAnchor(datePicker,255.0);
        AnchorPane.setLeftAnchor(datePicker,150.0);
//            datePicker.getDatePicker().setMinDate(LocalDate.now();
//// this line is used to prevent date selection
//            datePicker.getDatePicker().setCalendarViewShown(false);
        TextField tfName = new TextField();
        tfName.setId("Name");
        tfName.setPromptText("Enter Name..");
        AnchorPane.setBottomAnchor(tfName,220.0);
        AnchorPane.setLeftAnchor(tfName,150.0);

        TextField tfPhone = new TextField();
        tfPhone.setId("Phone Number");
        tfPhone.setPromptText("Phone Number");
        AnchorPane.setBottomAnchor(tfPhone,185.0);
        AnchorPane.setLeftAnchor(tfPhone,150.0);

        TextField tfSeatsNum = new TextField();
        tfSeatsNum.setId("Number of Seats");
        tfSeatsNum.setPromptText("Number of Seats");
        tfSeatsNum.setMaxWidth(100);
        AnchorPane.setBottomAnchor(tfSeatsNum,150.0);
        AnchorPane.setLeftAnchor(tfSeatsNum,150.0);

        Button btnGoBack = new Button();
        btnGoBack.setId("GoBack");
        btnGoBack.setText("Go Menu <---");
        AnchorPane.setBottomAnchor(btnGoBack,150.0);
        AnchorPane.setLeftAnchor(btnGoBack,350.0);

        // anchorPane.getChildren().add(btnGoBack, 12, 20);
        btnGoBack.setOnAction(event -> {
            Stage stage2 = (Stage) btnGoBack.getScene().getWindow();
            stage2.close();
            welcome();
        });

        Button btnBook = new Button();
        btnBook.setId("book");
        btnBook.setText("Book Now");
        AnchorPane.setBottomAnchor(btnBook,185.0);
        AnchorPane.setLeftAnchor(btnBook,350.0);

        //    anchorPane.add(btnBook, 14, 20);
        btnBook.setOnAction(event -> {
            if (tfName.getText() == null || tfName.getText().trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                // set content text
                a.setContentText("No Name Found..\nPlease Enter A Name..");
                a.show();

            }else if (selectSeats.size()==0) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                // set content text
                a.setContentText("No Seats Selected\nPlease Select Seat Numbers..");
                a.show();
            }else {
                for (String item : selectSeats) {
                    namesList.add(tfName.getText());
                    System.out.println(namesList);
                }
                bookedSeats.addAll(selectSeats);
                Stage stage2 = (Stage) btnBook.getScene().getWindow();
                stage2.close();
                welcome();
            }
        });

        anchorPane.getChildren().addAll(btnBook, btnGoBack,tfName,tfPhone,tfSeatsNum,datePicker);

        Stage stage2 = new Stage();
        stage2.setTitle("Train Booking System");
        Scene addScene = new Scene(anchorPane, 600, 800);
        stage2.setScene(addScene);
        stage2.setOnCloseRequest(event->{
            stage2.close();
            welcome();
        });
        stage2.show();
    }
    private void viewEmptySeats(ArrayList<String> bookedSeats) {

        GridPane grid;
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        //Creating Buttons
        for (int i = 1; i <= SEATING_CAPACITY; i++) {
            Button btn = new Button("A" + i);
            btn.setId(String.valueOf(i));
            btn.setPadding(new Insets(10, 10, 10, 10));
            //  grid.getChildren().add(btn);
            if (bookedSeats.contains(btn.getId())) {
                btn.setStyle("-fx-background-color: red");
                btn.setVisible(false);
            }
            createButtons(grid, i, btn);
        }

        Button btnContinue = new Button();
        btnContinue.setId("continue");
        btnContinue.setText("Continue");
        grid.add(btnContinue, 15, 16);
        btnContinue.setOnAction(event -> {
            Stage stage3 = (Stage) btnContinue.getScene().getWindow();
            stage3.close();
            welcome();
        });

        Stage stage3 = new Stage();
        stage3.setTitle("Train Booking System");
        Scene emptyScene = new Scene(grid, 600, 800);
        stage3.setScene(emptyScene);
        stage3.setOnCloseRequest(event->{
            stage3.close();
            welcome();
        });
        stage3.show();
    }

    //Console
    private void findSeatsOfCustomer(ArrayList<String> bookedNames, ArrayList<String> bookedSeats) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter customer Name You Want to search : ");
        String customerName = sc.nextLine();
        if (bookedNames.contains(customerName)) {
            int indexInList = bookedNames.indexOf(customerName);
            String seatNumbers = bookedSeats.get(indexInList);
            System.out.println("Customer Name : " + customerName + "\nSeat Numbers : " + seatNumbers);
            welcome();
        } else {
            System.out.println("Entered Name Not in the Booked Persons List..\nPlease Check The Name...\nThank you..");
            welcome();
        }
    }
    private void deleteCustomerSeats(ArrayList<String> bookedNames, ArrayList<String> bookedSeats) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter customer Name you want to Delete : ");
        String customerName = sc.nextLine();
        if (bookedNames.contains(customerName)) {
            int indexInList = bookedNames.indexOf(customerName);
            bookedSeats.remove(indexInList);
            System.out.println("Customer " + customerName + " Booking was Deleted..\nThank You...");
        }else{
            System.out.println("Customer Name Not Exists");
            welcome();
        }
    }

    class ConnectingToDataBase {
        public  void main(String[] args) {
            if (bookedSeats.size() != bookedNames.size()) {
                throw new IllegalArgumentException("Both two ArrayList have unequal sizes");
            }
            HashMap<String,String> bookedList = new LinkedHashMap<String,String>();
            for (int i=0; i<bookedSeats.size(); i++) {
                bookedList.put(bookedSeats.get(i), bookedNames.get(i));
            }
            System.out.println(bookedList);

//            Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
//            mongoLogger.setLevel(Level.SEVERE);
            Map<String, String> documentMap = new HashMap<String, String>();
            documentMap.put("name", "lokesh");
            documentMap.put("website", "howtodoinjava.com");


            Iterator it = documentMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
        }

        // Retrieving a collection
//          MongoCollection<Document> collection = database.getCollection("BookingList");
//        System.out.println("Collection sampleCollection selected successfully");

        MongoClient mongo = new MongoClient("localhost", 27017);
        MongoDatabase database = mongo.getDatabase("BookingList");



//               database.createCollection("bookedSeatList");
////            BookingList.insert(new BasicDBObject(documentMap));
//
//        collection.insert(new BasicDBObject(documentMap));

    }


    private void saveToFile() {

        //Converting To ArrayList of bookedSeats and bookedNames to hash map bookedList

        new ConnectingToDataBase();

    }
//
//        try {
//            FileWriter fr = new FileWriter("src/PP02CourseWork/BookedNames.txt");
//            BufferedWriter br = new BufferedWriter(fr);
//            PrintWriter out = new PrintWriter(br);
//            for (int i = 0; i < bookedNames.size(); i++) {
//                if (bookedNames.get(i) != null)
//                    out.write(bookedSeats.get(i)+ " = "+bookedNames.get(i));
//                out.write("\n");
//            }
//            out.close();

    //
//            FileWriter fr2 = new FileWriter("src/PP02CourseWork/BookedSeats.txt");
//            BufferedWriter br2 = new BufferedWriter(fr2);
//            PrintWriter out2 = new PrintWriter(br2);
//            for (int i = 0; i < bookedSeats.size(); i++) {
//                if (bookedSeats.get(i) != null)
//                    out.write(bookedSeats.get(i));
//                out.write("\n");
//          0   0         }
//            out.close();
//            welcome();
//        }catch (IOException e){
//                System.err.println(e);
//            }
//        }
    private void loadFromFile() {
        try {
//            File file = new File("src/PP02CourseWork/BookedSeats.txt");
//            BufferedReader br = new BufferedReader(new FileReader(file));
            Scanner sc = new Scanner(new File("src/PP02CourseWork/BookedNames.txt"));
//            String st;
//            while ((st = br.readLine()) != null)
            while (sc.hasNextLine()){
                String line = sc.nextLine();
                String[] NewArray = line.split(" = ");
                bookedSeats.add(NewArray[0]);
                bookedNames.add(NewArray[1]);
                System.out.println(bookedSeats);
                System.out.println(bookedNames);
            }
            System.out.println();
        } catch (IOException err) {
            System.err.println(err);
        }
        welcome();
    }
    private void sortBooking(ArrayList<String> bookedNames, ArrayList<String> bookedSeats, ArrayList<String> orderedNames) {

        for (int i = 0; i < bookedNames.size(); i++) {
            for (int j = (i + 1); j < bookedNames.size(); j++) {
                if (bookedNames.get(i).compareToIgnoreCase(bookedNames.get(j)) > 0) {
                    String temp = bookedNames.get(i);
                    bookedNames.set(i, bookedNames.get(j));
                    bookedNames.set(j, temp);
                }
            }
            orderedNames.add(bookedNames.get(i));
        }
        for (String it : orderedNames) {
            if (bookedNames.contains(it)) {
                int indexInList = bookedNames.indexOf(it);
                String seatNumbers = bookedSeats.get(indexInList);
                System.out.println("Customer Names In Alphabetical Order ");
                System.out.println("=====================================\n*************************************");
                System.out.println("Name : "+it + ", Seat Numbers : " + seatNumbers);
            }
        }
        welcome();
    }

}// End Of mainClass


