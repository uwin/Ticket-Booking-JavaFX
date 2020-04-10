import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class TrainStation extends Application{
    private static Passenger[] waitingRoom= new Passenger[42];
    private static PassengerQueue trainQueue = new PassengerQueue();

    private  static void listOption() {
        System.out.println("\n"+
                "A Add a seat\n"+
                "V View all seats\n"+
                "D Delete seat\n"+
                "S Save details\n"+
                "L Load details\n"+
                "R Run simulation\n"+
                "q quit\n");
        Scanner scanOption= new Scanner(System.in);
        System.out.println("> select a option");
        String userOption= scanOption.next().toUpperCase();
        switch (userOption) {
            case "A":
                add();
                break;
            case "V":
                view();
                break;
            case "D":
                delete();
                break;
            case "S":
                save();
                break;
            case "L":
                load();
                break;
            case "R":
                run();
                break;
            default:
                System.out.println("invalid input");
                break;
        }
    }

    private static void importGui(){
        Stage window = new Stage();
        window.setTitle("Train Booking System");
        GridPane first = new GridPane();
        first.setPadding(new Insets(2, 2, 2, 2));
        first.setHgap(10);
        first.setVgap(10);
        Scene addViewFirst = new Scene(first, 1020, 400);
        window.setScene(addViewFirst);
        window.show();

        Label head = new Label("Denuwara Menike Ticket Booking System\n" +
                "                   A/C compartment");
        head.setFont(new Font("Arial", 30));
        head.setTextFill(Color.web("#0076a3")); //light blue
        first.add(head,20,3,50,8);

        Label headDate = new Label("Date");
        headDate.setFont(new Font("Arial", 23));
        headDate.setTextFill(Color.web("#0076a3")); //light blue
        first.add(headDate,3,12,9,4);

        DatePicker datePick = new DatePicker();
        datePick.setValue(LocalDate.now());
        first.add(datePick, 12, 12,18,4);


        String[] stops = new String[]{ "Colombo Fort","Polgahawela",
                "Peradeniya Junction", "Gampola","Nawalapitiya",
                "Hatton","Thalawakele","Nanuoya", "Haputale","Diyatalawa",
                "Bandarawela","Ella", "Badulla" };

        //        text for start
        Label headStart = new Label("Select Station");
        headStart.setFont(new Font("Arial", 23));
        headStart.setTextFill(Color.web("#0076a3")); //light blue
        first.add(headStart,3,16,9,4);

//        drop down menu for start
        ComboBox<String> stationDrop = new ComboBox<>();
        stationDrop.getItems().addAll(stops);
        stationDrop.setValue("Colombo Fort");
        first.add(stationDrop,12,16,9,4);


        Button colomboSButton = new Button("Colombo Station");
        colomboSButton.setMaxSize(120, 60);
        colomboSButton.setStyle("-fx-background-color: lightblue; ");
        colomboSButton.setOnAction(event -> {
            colomboSButton.setStyle("-fx-background-color: blue; ");
            String selectedTrain ="1";
            String selectedStation =stationDrop.getValue();
            String selectedDate=datePick.getValue().toString();
            window.close();
            importData(selectedDate,selectedTrain,selectedStation);

        });

        first.add(colomboSButton,30,19,40,12);

        Button badullaSButton = new Button("Badulla Station");
        badullaSButton.setMaxSize(120, 60);
        badullaSButton.setStyle("-fx-background-color: lightblue; ");
        badullaSButton.setOnAction(event -> {
            badullaSButton.setStyle("-fx-background-color: blue; ");
            String selectedTrain ="2";
            String selectedStation =stationDrop.getValue();
            String selectedDate=datePick.getValue().toString();
            window.close();
            importData(selectedDate,selectedTrain,selectedStation);
        });
        first.add(badullaSButton,50,19,40,12);

        Button closeButFirst = new Button("close");
        closeButFirst.setMaxSize(120, 60);
        closeButFirst.setStyle("-fx-background-color: red; ");
        closeButFirst.setOnAction(event -> {
            window.close();
            listOption();
        });
        first.add(closeButFirst,80,30,10,12);

    }
    private static void importData(String selectedDate,String selectedTrain,String selectedStation) {
        //        initiate MongoClient
        com.mongodb.MongoClient dbClient = new MongoClient
                ("localhost", 27017);
//        creating a database
        MongoDatabase dbDatabase = dbClient.getDatabase
                ("trainBookingSystem");
//        creating a document
        MongoCollection<Document> bookings = dbDatabase.getCollection
                ("BookingData");
        System.out.println("connected to BookingData");
        FindIterable<Document> bookingDocument = bookings.find();

        ArrayList<String> stopsList = new ArrayList<>(Arrays.asList(
                "Colombo Fort","Polgahawela", "Peradeniya Junction",
                "Gampola","Nawalapitiya", "Hatton","Thalawakele","Nanuoya",
                "Haputale","Diyatalawa", "Bandarawela","Ella", "Badulla"));

        if(bookings.countDocuments()>0)
        {
            int i=0;
            for(Document document:bookingDocument)
            {
                String date   = document.getString("Date");
                String name   = document.getString("User");
                String surname= document.getString("Surname");
                String seat   = document.getString("Seat");
                String start  = document.getString("Start");
                String end    = document.getString("End");

                String trainNo;
                if(stopsList.indexOf(start)<stopsList.indexOf(end)){
                    trainNo="1";
                }else {
                    trainNo="2"; }

                if (    selectedDate.equalsIgnoreCase(date) &&
                        selectedStation.equalsIgnoreCase(start) &&
                        selectedTrain.equals(trainNo)
                ){
                    Passenger passengerObj = new Passenger(name,surname,seat);
                    waitingRoom[i]=(passengerObj);
                    i++;
                }
            }
        }
//        if not a message will be printed
        else
        {
            System.out.println("Passenger Data is Unavailable to be imported");
        }
//        close mongo client
        dbClient.close();
        listOption();
    }



    private static void add() {
        Stage window = new Stage();
        window.setTitle("adding to passanger");
        GridPane addView = new GridPane();
        addView.setPadding(new Insets(2, 2, 2, 2));
        addView.setHgap(10);
        addView.setVgap(10);
        Scene addViewFirst = new Scene(addView, 600, 500);
        window.setScene(addViewFirst);
        window.show();

        TableView<Passenger> waitingRoomTable;
        TableColumn<Passenger,String> ticket_col = new TableColumn<>("Ticket");
        ticket_col.setMaxWidth(100);
        ticket_col.setCellValueFactory(new PropertyValueFactory<Passenger,String>("ticketNumber"));
        TableColumn<Passenger,String> name_col = new TableColumn<>("Name");
        name_col.setMaxWidth(100);
        name_col.setCellValueFactory(new PropertyValueFactory<Passenger,String>("name"));
        TableColumn<Passenger,String> seat_col = new TableColumn<>("Seat");
        seat_col.setMaxWidth(100);
        seat_col.setCellValueFactory(new PropertyValueFactory<Passenger,String>("seat"));
        waitingRoomTable = new TableView<>();
        waitingRoomTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        waitingRoomTable.setItems(getWaitRoomData());
        waitingRoomTable.getColumns().add(ticket_col);
        waitingRoomTable.getColumns().add(name_col);
        waitingRoomTable.getColumns().add(seat_col);
        addView.add(waitingRoomTable,0,4,80,400);

        TableView<Passenger> trainQueueTable;
        TableColumn<Passenger,String> ticket_col2 = new TableColumn<>("Ticket");
        ticket_col2.setCellValueFactory(new PropertyValueFactory<Passenger,String>("ticketNumber"));
        ticket_col2.setMaxWidth(100);
        TableColumn<Passenger,String> name_col2 = new TableColumn<>("Name");
        name_col2.setMaxWidth(100);
        name_col2.setCellValueFactory(new PropertyValueFactory<Passenger,String>("name"));
        TableColumn<Passenger,String> seat_col2 = new TableColumn<>("Seat");
        seat_col2.setMaxWidth(100);
        seat_col2.setCellValueFactory(new PropertyValueFactory<Passenger,String>("seat"));
        trainQueueTable = new TableView<>();
        trainQueueTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        trainQueueTable.setItems(getTrainQueueData());
        trainQueueTable.getColumns().add(ticket_col2);
        trainQueueTable.getColumns().add(name_col2);
        trainQueueTable.getColumns().add(seat_col2);
        addView.add(trainQueueTable,30,4,80,400);

        Button addButFirst = new Button("Add");
        addButFirst.setStyle("-fx-background-color: lightblue; ");
        addButFirst.setOnAction(event -> {
            int generateNo=(int)(Math.random() * ((6 - 1) + 1)) + 1;
            if (getWaitRoomData().size()<generateNo) generateNo=getWaitRoomData().size();
            int i=0;
            for(int j=0;j<=waitingRoom.length;j++){
                if(trainQueue.isFull()) System.out.println("ss");
                if (getWaitRoomData().size()==0) break;
                if(waitingRoom[j]!=null){
                    trainQueue.add(waitingRoom[j]);
                    waitingRoom[j]=null;
                    i++;
                    if (i==generateNo) break;
                }
            }
            trainQueueTable.setItems(getTrainQueueData());
            waitingRoomTable.setItems(getWaitRoomData());
        });
        addView.add(addButFirst,2,2,50,2);

        Button closeButFirst = new Button("close");
        closeButFirst.setStyle("-fx-background-color: red; ");
        closeButFirst.setOnAction(event -> {
            window.close();
            listOption();
        });
        addView.add(closeButFirst,2,5,50,2);
    }
    private static ObservableList<Passenger> getWaitRoomData(){
        ObservableList<Passenger> passengers= FXCollections.observableArrayList();
        for (Passenger i: waitingRoom) {
            if (i!=null){
                passengers.add(i);
            }
        }
        return passengers;
    }
    private static ObservableList<Passenger> getTrainQueueData(){
        ObservableList<Passenger>queuePassengers= FXCollections.observableArrayList();

        for (Passenger i: trainQueue.getQueueArray())
            if (i!=null){
                queuePassengers.add(i);
            }
        return queuePassengers;
    }


    private static void view() {
        Stage window = new Stage();
        window.setTitle("adding to passanger");
        GridPane first = new GridPane();
        first.setPadding(new Insets(2, 2, 2, 2));
        first.setHgap(10);
        first.setVgap(10);
        Scene addViewFirst = new Scene(first, 1020, 350);
        window.setScene(addViewFirst);
        window.show();

        Image seatBlack = new Image(TrainStation.class.getResourceAsStream
                ("images/black.png"));
        Passenger[] array = trainQueue.getQueueArray();
        int number=1;
        for (int r = 2; r < 5; r++) {
            for (int c = 2; c < 16; c++) {
                if (number <=42)
                {
                    while (array[number-1]!=null){
                        ImageView button = new ImageView(seatBlack);
                        Label num = new Label();
                        num.setFont(new Font("Arial", 15));
                        num.setText(array[number-1].getName());
                        button.setFitHeight(60);
                        button.setFitWidth(60);
                        button.setId(String.valueOf(number));
                        number++;
                        first.add(button, c, r);
                        first.add(num, c, r);
                        break;
                    }
                }
            }
        }
        Button closeBut = new Button("close");
        closeBut.setMaxSize(120, 60);
        closeBut.setStyle("-fx-background-color: red; ");
        closeBut.setOnAction(event -> {
            window.close();
            listOption();
        });
        first.add(closeBut, 14, 9,14,9);
    }

    private static void delete() {

    }


    private static void save() {}
    private  static void load() {}
    private  static void run() {}

    public static void main(String[]args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        boolean validateImport =false;
        for (Passenger i:waitingRoom){
            if (i != null) {
                validateImport = true;
                break;
            }
        }
        if (!validateImport) {
            importGui();
        }
    }
}
