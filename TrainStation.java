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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.bson.Document;

import java.time.LocalDate;
import java.util.*;

public class TrainStation extends Application{
    private static Passenger[] waitingRoom= new Passenger[42];

    private static PassengerQueue trainQueue = new PassengerQueue();

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
                    passengerObj.setDate(date);
                    passengerObj.setStation(start);
                    passengerObj.setTrain(trainNo);
                    waitingRoom[i]=(passengerObj);
                    i++;
                }
            }
            boolean validateImport =false;
            for (Passenger data:waitingRoom){
                if (data != null) {
                    validateImport = true;
                    break;
                }
            }
            Alert a = new Alert(Alert.AlertType.WARNING);
            if (!validateImport) {
                a.setHeaderText("No Data Found for selected Parameters");
                a.showAndWait();
                importGui();
            }else {
                a.setHeaderText("Data Loaded from Train Booking");
                a.showAndWait();
                listOption();
            }
        }
        else
        {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Saved Data is Unavailable\n programme will continue without loading data       ");
            a.showAndWait();
            listOption();
        }
//        close mongo client
        dbClient.close();
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
                listOption();
                break;
        }
    }

    private static void add() {
        Stage window = new Stage();
        window.setTitle("Add to Train Queue");
        GridPane addView = new GridPane();
        addView.setPadding(new Insets(2, 2, 2, 2));
        addView.setHgap(10);
        addView.setVgap(10);
        Scene addViewFirst = new Scene(addView, 650, 500);
        window.setScene(addViewFirst);
        window.show();

        TableView<Passenger> waitingRoomTable;
        TableColumn<Passenger,String> ticket_col = new TableColumn<>("Ticket");
        ticket_col.setMinWidth(100);
        ticket_col.setCellValueFactory(new PropertyValueFactory<Passenger,String>("ticketNumber"));
        TableColumn<Passenger,String> name_col = new TableColumn<>("Name");
        name_col.setMinWidth(100);
        name_col.setCellValueFactory(new PropertyValueFactory<Passenger,String>("name"));
        TableColumn<Passenger,String> seat_col = new TableColumn<>("Seat");
        seat_col.setMinWidth(100);
        seat_col.setCellValueFactory(new PropertyValueFactory<Passenger,String>("seat"));
        waitingRoomTable = new TableView<>();
        //waitingRoomTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        waitingRoomTable.setMinWidth(300);
        waitingRoomTable.setItems(getWaitRoomData());
        waitingRoomTable.getColumns().add(ticket_col);
        waitingRoomTable.getColumns().add(name_col);
        waitingRoomTable.getColumns().add(seat_col);
        addView.add(waitingRoomTable,1,4);

        TableView<Passenger> trainQueueTable;
        TableColumn<Passenger,String> ticket_col2 = new TableColumn<>("Ticket");
        ticket_col2.setCellValueFactory(new PropertyValueFactory<Passenger,String>("ticketNumber"));
        ticket_col2.setMinWidth(100);
        TableColumn<Passenger,String> name_col2 = new TableColumn<>("Name");
        name_col2.setMinWidth(100);
        name_col2.setCellValueFactory(new PropertyValueFactory<Passenger,String>("name"));
        TableColumn<Passenger,String> seat_col2 = new TableColumn<>("Seat");
        seat_col2.setMinWidth(100);
        seat_col2.setCellValueFactory(new PropertyValueFactory<Passenger,String>("seat"));
        trainQueueTable = new TableView<>();
        //trainQueueTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        trainQueueTable.setMinWidth(300);
        trainQueueTable.setItems(getTrainQueueData());
        trainQueueTable.getColumns().add(ticket_col2);
        trainQueueTable.getColumns().add(name_col2);
        trainQueueTable.getColumns().add(seat_col2);
        addView.add(trainQueueTable,3,4);

        Button addButFirst = new Button("Add");
        addButFirst.setStyle("-fx-background-color: lightblue; ");
        addButFirst.setOnAction(event -> {
            if (getWaitRoomData().isEmpty()){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Waiting Room is Empty");
                a.show();
            } else if (trainQueue.isFull()){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Train Queue is full");
                a.show();
            }else {
                int generateNo = (int) (Math.random() * ((6 - 1) + 1)) + 1;
                if (getWaitRoomData().size() < generateNo) generateNo = getWaitRoomData().size();
                int i = 0;
                for (int j = 0; j <= waitingRoom.length; j++) {
                    //if (trainQueue.isFull())
                    if (getWaitRoomData().size() == 0) break;
                    if (waitingRoom[j] != null) {
                        trainQueue.add(waitingRoom[j]);
                        waitingRoom[j] = null;
                        i++;
                        if (i == generateNo) break;
                    }
                }
            }
            trainQueue.sortSeat(trainQueue.getQueueArray(),trainQueue.getLength()+1);
            trainQueueTable.setItems(getTrainQueueData());
            waitingRoomTable.setItems(getWaitRoomData());
        });
        addView.add(addButFirst,3,5);

        Button closeButFirst = new Button("close");
        closeButFirst.setStyle("-fx-background-color: red; ");
        closeButFirst.setOnAction(event -> {
            window.close();
            listOption();
        });
        addView.add(closeButFirst,3,6);
    }

    private static void view() {
        Stage window = new Stage();
        window.setTitle("train queue");
        GridPane first = new GridPane();
        first.setPadding(new Insets(2, 2, 2, 2));
        first.setHgap(10);
        first.setVgap(10);
        Scene addViewFirst = new Scene(first, 720, 440);
        window.setScene(addViewFirst);
        window.show();

        Passenger[] array = trainQueue.getQueueArray();
        int number=1;
        for (int r = 2; r < 8; r++) {
            for (int c = 2; c < 9; c++) {
                if (number <=42)
                {
                    Button button = new Button();
                    button.setFont(new Font("Arial", 12));
                    button.setMinHeight(60);
                    button.setMinWidth(60);
                    button.setId(String.valueOf(number));
                    if (array[number-1]!=null)button.setText(array[number-1].getName()+"\n"+array[number-1].getTicketNumber());
                    number++;
                    first.add(button, c, r);
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
        first.add(closeBut, 14, 6,14,9);
    }

    private static void delete() {
        Scanner scanSeat= new Scanner(System.in);
        System.out.println("> Enter Seat Number");
        String deleteSeat = scanSeat.next();

        //List<Passenger> deleteArray = new ArrayList<>(Arrays.asList(trainQueue.getQueueArray()));
        List<Passenger> deleteArray = new ArrayList<Passenger>(Arrays.asList(trainQueue.getQueueArray()));
        for (Passenger temp: trainQueue.getQueueArray()){
            if(temp==null){
                System.out.println("invalid input");
                break;
            } else if (temp.getSeat().equals(deleteSeat)) {
                deleteArray.remove(temp);
                trainQueue.removeSeat();
                waitingRoom[trainQueue.getLength()]=temp;
                deleteArray.add(null);
                trainQueue.setQueueArray(deleteArray.toArray(new Passenger[0]));
                break;
            }
        }
        listOption();
    }

    private static void save() {
        com.mongodb.MongoClient dbClient = new MongoClient
                ("localhost", 27017);
        MongoDatabase dbDatabase = dbClient.getDatabase
                ("trainBookingSystem");


        MongoCollection<Document> trainqueue = dbDatabase.getCollection("QueueData");
        System.out.println("connected to QueueData");
        FindIterable<Document> queueDocument = trainqueue.find();
        if(trainqueue.countDocuments()>1) {
            for(Document document: queueDocument) {
                trainqueue.deleteOne(document);
            }
        }
        for (Passenger data : trainQueue.getQueueArray()) {
            Document userDocument = new Document();
            if (data==null) continue;
            userDocument.append("Name", data.getName());
            userDocument.append("Seat", data.getSeat());
            userDocument.append("TicketNo", data.getTicketNumber());
            userDocument.append("Date", data.getDate());
            userDocument.append("Station", data.getStation());
            userDocument.append("Train", data.getTrain());
            trainqueue.insertOne(userDocument);
        }

        MongoCollection<Document> waitingroom = dbDatabase.getCollection("WaitingRoomData");
        System.out.println("connected to WaitingRoomData");
        FindIterable<Document> waitingRoomDocument = waitingroom.find();
        if(waitingroom.countDocuments()>1){
            for(Document document: waitingRoomDocument) {
                waitingroom.deleteOne(document);
            }
        }
        for (Passenger data : waitingRoom){
            Document userDocument = new Document();
            if (data==null) continue;
            userDocument.append("Name", data.getName());
            userDocument.append("Seat", data.getSeat());
            userDocument.append("TicketNo", data.getTicketNumber());
            userDocument.append("Date", data.getDate());
            userDocument.append("Station", data.getStation());
            userDocument.append("Train", data.getTrain());
            waitingroom.insertOne(userDocument);
        }

        dbClient.close();
        System.out.println("saved files");
        listOption();
    }

    private  static void load() {
        //        initiate MongoClient
        com.mongodb.MongoClient dbClient = new MongoClient
                ("localhost", 27017);
//        creating a database
        MongoDatabase dbDatabase = dbClient.getDatabase
                ("trainBookingSystem");
//        creating a document

        MongoCollection<Document> trainqueue = dbDatabase.getCollection("QueueData");
        System.out.println("connected to QueueData");
        FindIterable<Document> queueDocument = trainqueue.find();
        trainQueue.clearQueue();
        if(trainqueue.countDocuments()>0) {
            for(Document document:queueDocument) {
                String name    = document.getString("Name");
                String seat    = document.getString("Seat");
                String ticketNo= document.getString("TicketNo");
                String date    = document.getString("Date");
                String station = document.getString("Station");
                String train   = document.getString("Train");

                Passenger passengerObj = new Passenger(ticketNo,name);
                passengerObj.setDate(date);
                passengerObj.setStation(station);
                passengerObj.setTrain(train);
                passengerObj.setSeat(seat);
                passengerObj.setTrain(train);
                trainQueue.add(passengerObj);
            }
            System.out.println("Train Queue loaded");
        }
        else { System.out.println("no files were added, no data is changed");
        }

        MongoCollection<Document> waitingroom = dbDatabase.getCollection("WaitingRoomData");
        System.out.println("connected to WaitingRoomData");
        FindIterable<Document> waitingRoomDocument = waitingroom.find();
        System.out.println(waitingRoom);
        Arrays.fill(waitingRoom, null);
        System.out.println(waitingRoom);
        if(waitingroom.countDocuments()>0) {
            int i=0;
            for(Document document:waitingRoomDocument)
            {
                String name    = document.getString("Name");
                String seat    = document.getString("Seat");
                String ticketNo= document.getString("TicketNo");
                String date    = document.getString("Date");
                String station = document.getString("Station");
                String train   = document.getString("Train");

                Passenger passengerObj = new Passenger(ticketNo,name);
                passengerObj.setDate(date);
                passengerObj.setStation(station);
                passengerObj.setTrain(train);
                passengerObj.setSeat(seat);
                passengerObj.setTrain(train);
                waitingRoom[i]=(passengerObj);
                i++;
            }
            System.out.println("Waiting Rooom loaded");
        }
        else { System.out.println("no files were added, no data is changed");
        }

//        close mongo client
        dbClient.close();
        listOption();
    }

    private  static void run() {
        Passenger[] reportData = new Passenger[trainQueue.getLength()];
        Passenger[] reportArray = trainQueue.getQueueArray();
        float maximunWaitingTime=0;
        float minimumWaitingTime=0;
        float averageSecondsInQueue;
        int maximunLengthQueue;
        if (trainQueue.isEmpty()){
            System.out.println("k");
        }else {
            float queueDelay = 0;
            int i=0;
            for (Passenger pasangerObjest: reportArray){
                if (pasangerObjest==null) continue;
                queueDelay+=pasangerObjest.getSeconds();
                if (minimumWaitingTime==0) minimumWaitingTime=queueDelay;
                if (queueDelay<minimumWaitingTime) minimumWaitingTime=queueDelay;
                if (queueDelay>maximunWaitingTime) maximunWaitingTime=queueDelay;
                trainQueue.remove();
                pasangerObjest.setSeconds(queueDelay);
                reportData[i]=pasangerObjest;
                i++;
            }
            averageSecondsInQueue = queueDelay/i;
            System.out.println("averageSecondsInQueue >"+averageSecondsInQueue);
            System.out.println("minimumWaitingTime >"+minimumWaitingTime);
            System.out.println("maximunWaitingTime >"+maximunWaitingTime);
            System.out.println("maximunLengthQueue >"+i);
        }
    }

    public static void main(String[]args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        importGui();
    }
}