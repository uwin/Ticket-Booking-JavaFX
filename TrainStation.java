import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.bson.Document;
import java.time.LocalDate;
import java.util.*;

public class TrainStation extends Application{
    private static Passenger[] waitingRoom= new Passenger[42];
    private  PassengerQueue trainQueue = new PassengerQueue();
    private static Passenger[] reportData = new Passenger[42];

    /**
     *
     * this method is used to insert records within arrays of passenger objects
     * to a Observable list, which will be used to insert details in the table
     * view
     * @param nameArray
     * @return
     */
    private  ObservableList<Passenger> getDataToTable(Passenger[] nameArray){
        ObservableList<Passenger> table= FXCollections.observableArrayList();
        for (Passenger i : nameArray) {
//            if the current object is null it will be not added to the list
            if (i != null) {
//                for boarded only add to list if passenger arrived
                if (nameArray == reportData) {
                    if (i.getArrived()) table.add(i);
                } else {
                    table.add(i);
                }
            }
        }
        return table;
    }

    /**
     *
     *this method is used to create a gui that allows the user to select the
     * date, station & the train from train booking system that will be imported
     * to the waiting room
     */
    private  void importGui(){
//        creating the scene
        Stage window = new Stage();
        window.setTitle("Train Booking System");
        GridPane first = new GridPane();
        first.setPadding(new Insets(2, 2, 2, 2));
        first.setHgap(10);
        first.setVgap(10);
        Scene addViewFirst = new Scene(first, 650, 400);
        addViewFirst.getStylesheets().add("style.css"); //import css
        window.setScene(addViewFirst);
        window.show();

//        main Label
        Label head = new Label("Denuwara Menike Ticket Booking System\n" +
                "                   A/C compartment");
        head.setFont(new Font("Arial", 30));
        first.add(head,5,3,60,8);

//        label for Date
        Label headDate = new Label("Date");
        headDate.setFont(new Font("Arial", 23));
        first.add(headDate,3,12,9,4);

//        date picker
        DatePicker datePick = new DatePicker();
        datePick.setValue(LocalDate.now());
        first.add(datePick, 12, 12,18,4);

//        list of all the stations
        String[] stops = new String[]{ "Colombo Fort","Polgahawela",
                "Peradeniya Junction", "Gampola","Nawalapitiya",
                "Hatton","Thalawakele","Nanuoya", "Haputale","Diyatalawa",
                "Bandarawela","Ella", "Badulla" };

//       Label for Station
        Label headStart = new Label("Station");
        headStart.setFont(new Font("Arial", 23));
        first.add(headStart,3,16,9,4);

//        drop down menu for start
        ComboBox<String> stationDrop = new ComboBox<>();
        stationDrop.getItems().addAll(stops);
        stationDrop.setValue("Colombo Fort");
        first.add(stationDrop,12,16,18,4);

//        button for colombo > badulla train
        Button colomboSButton = new Button("Badulla Train");
        colomboSButton.setMaxSize(120, 60);
        colomboSButton.setOnAction(event -> {
            String selectedTrain ="1";
            String selectedStation =stationDrop.getValue();
            String selectedDate=datePick.getValue().toString();
            window.close();
            importData(selectedDate,selectedTrain,selectedStation);

        });

        first.add(colomboSButton,15,20,40,12);

//        button for badulla > colombo train
        Button badullaSButton = new Button("Colombo Train");
        badullaSButton.setMaxSize(120, 60);
        badullaSButton.setOnAction(event -> {
            String selectedTrain ="2";
            String selectedStation =stationDrop.getValue();
            String selectedDate=datePick.getValue().toString();
            window.close();
            importData(selectedDate,selectedTrain,selectedStation);
        });
        first.add(badullaSButton,35,20,40,12);

//        close button
        Button closeButFirst = new Button("close");
        closeButFirst.setMaxSize(120, 60);
        closeButFirst.setStyle("-fx-background-color: #d21e3c; ");
        closeButFirst.setOnMouseEntered(event ->  closeButFirst.setStyle("-fx-background-color: #c64058; "));
        closeButFirst.setOnMouseExited(event ->  closeButFirst.setStyle("-fx-background-color: #d21e3c; "));
        closeButFirst.setOnAction(event -> {
            window.close();
            System.exit(0);
        });
        first.add(closeButFirst,54,30,10,12);

//        skip button
        Button skipButFirst = new Button("Skip");
        skipButFirst.setMaxSize(120, 60);
        skipButFirst.setStyle("-fx-background-color: #871327; ");
        skipButFirst.setOnMouseEntered(event ->  skipButFirst.setStyle("-fx-background-color: #743c42; "));
        skipButFirst.setOnMouseExited(event ->  skipButFirst.setStyle("-fx-background-color: #871327; "));
        skipButFirst.setOnAction(event -> {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Skip loading from Train Booking");
            a.setContentText("Programme Will Continue Without Loading Data from Saved bookings");
            a.showAndWait();
            window.close();
            listOption();
        });
        first.add(skipButFirst,44,30,10,12);

    }

    /**
     * this method is used to import the recods from the train booking system to
     * the waiting room with the use of users selections
     * @param selectedDate
     * @param selectedTrain
     * @param selectedStation
     */
    private  void importData(String selectedDate,String selectedTrain,String selectedStation) {
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

//        import data to the document
        FindIterable<Document> bookingDocument = bookings.find();

        ArrayList<String> stopsList = new ArrayList<>(Arrays.asList(
                "Colombo Fort","Polgahawela", "Peradeniya Junction",
                "Gampola","Nawalapitiya", "Hatton","Thalawakele","Nanuoya",
                "Haputale","Diyatalawa", "Bandarawela","Ella", "Badulla"));

//        creating a array to temporarily store arrived passengers
        ArrayList<Passenger> arrivedarray = new ArrayList<>();
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
//                check if the data matches the user input
                if (    selectedDate.equalsIgnoreCase(date) &&
                        selectedStation.equalsIgnoreCase(start) &&
                        selectedTrain.equals(trainNo)
                ){
                    Passenger passengerObj = new Passenger(name,surname,seat);
                    passengerObj.setDate(date);
                    passengerObj.setStation(start);
                    passengerObj.setTrain(trainNo);
//                    add records to the temporary array if they arrived
//                    if not add to report array ( boarded array)
                    if(passengerObj.getArrived()) {
                        arrivedarray.add(passengerObj);
                    }else {
                        reportData[i]=passengerObj;
//                        trainQueue.add(passengerObj);
                        i++;
                    }
                }
                int count=0;
//                move passenger records in temporary array to waiting room
                for (Passenger data: arrivedarray) {
                    waitingRoom[count]=data;
                    count++;
                }
            }
//            check if the waiting room has been occupied
            boolean validateImport =false;
            for (Passenger data:waitingRoom){
                if (data != null) {
                    validateImport = true;
                    break;
                }
            }
            Alert a = new Alert(Alert.AlertType.WARNING);
            if (!validateImport) {
                a.setHeaderText("No Data Found");
                a.setContentText("Booking data contain no records " +
                        "that suite the selected date and Station");
                a.showAndWait();
                importGui();
            }else {
                a.setHeaderText("Data Loaded from Booking Data");
                a.setContentText("Records of "+waitingRoom[0].getStation()+
                        " station\nLoaded for " +selectedDate );
                a.showAndWait();
                listOption();
            }
        }
        else
        {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("No Saved Data is Unavailable");
            a.setContentText("Booking data contains no saved records\n " +
                    "Program will continue without loading from Booking data");
            a.showAndWait();
            listOption();
        }
//        close mongo client
        dbClient.close();
    }

    /**
     *
     * this method is used to print the menu options & to run the relevant
     * methods according to user input, the code will print a all options of
     * the menu along with a corresponding letter in front of it.
     */
    private  void listOption() {
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
//        calling methods depending on user input
        switch (userOption) {
            case "A":
                add();
                break;
            case "V":
                view(trainQueue.getQueueArray());
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
            case "Q":
                System.exit(0);
                break;
            default:
                System.out.println("invalid input");
                listOption();
                break;
        }
    }

    /**
     *
     * this method is used to move passengers from the waiting room to the train
     * queue
     */
    private  void add() {
//        create scene
        Stage window = new Stage();
        window.setTitle("Add to Train Queue");
        AnchorPane addView = new AnchorPane();
        Scene addViewFirst = new Scene(addView, 680, 580);
        addViewFirst.getStylesheets().add("style.css"); //import css
        window.setScene(addViewFirst);
        window.show();

//        label for waiting room table
        Label waitingRoomHead = new Label("Waiting Room");
        waitingRoomHead.setFont(new Font("Arial", 23));
        addView.getChildren().add(waitingRoomHead);
        AnchorPane.setLeftAnchor(waitingRoomHead,20d);
        AnchorPane.setTopAnchor(waitingRoomHead,10d);

//        waiting room table view
        TableView<Passenger> waitingRoomTable;
        TableColumn<Passenger,String> ticket_col = new TableColumn<>("Ticket");
        ticket_col.setMinWidth(100);
        ticket_col.setCellValueFactory(new PropertyValueFactory<>("ticketNumber"));
        TableColumn<Passenger,String> name_col = new TableColumn<>("Name");
        name_col.setMinWidth(100);
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Passenger,String> seat_col = new TableColumn<>("Seat");
        seat_col.setMinWidth(100);
        seat_col.setCellValueFactory(new PropertyValueFactory<>("seat"));

//        waiting room table details
        waitingRoomTable = new TableView<>();
        waitingRoomTable.setPlaceholder(new Label("Waiting Room Is Empty"));
        waitingRoomTable.setMinWidth(300);
        waitingRoomTable.setMinHeight(450);
        waitingRoomTable.setItems(getDataToTable(waitingRoom));
        waitingRoomTable.getColumns().add(ticket_col);
        waitingRoomTable.getColumns().add(name_col);
        waitingRoomTable.getColumns().add(seat_col);
        addView.getChildren().addAll(waitingRoomTable);
        AnchorPane.setLeftAnchor(waitingRoomTable,20d);
        AnchorPane.setTopAnchor(waitingRoomTable,40d);


//        label for train queue table
        Label trainQueueHead = new Label("Train Queue");
        trainQueueHead.setFont(new Font("Arial", 23));
        addView.getChildren().addAll(trainQueueHead);
        AnchorPane.setRightAnchor(trainQueueHead,200d);
        AnchorPane.setTopAnchor(trainQueueHead,10d);

//        train queue table view
        TableView<Passenger> trainQueueTable;
        TableColumn<Passenger,String> ticket_col2 = new TableColumn<>("Ticket");
        ticket_col2.setCellValueFactory(new PropertyValueFactory<>("ticketNumber"));
        ticket_col2.setMinWidth(100);
        TableColumn<Passenger,String> name_col2 = new TableColumn<>("Name");
        name_col2.setMinWidth(100);
        name_col2.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Passenger,String> seat_col2 = new TableColumn<>("Seat");
        seat_col2.setMinWidth(100);
        seat_col2.setCellValueFactory(new PropertyValueFactory<>("seat"));

//        train queue table details
        trainQueueTable = new TableView<>();
        trainQueueTable.setPlaceholder(new Label("Train Queue Is Empty"));
        trainQueueTable.setMinWidth(300);
        trainQueueTable.setMinHeight(450);
        trainQueueTable.setItems(getDataToTable(trainQueue.getQueueArray()));
        trainQueueTable.getColumns().add(ticket_col2);
        trainQueueTable.getColumns().add(name_col2);
        trainQueueTable.getColumns().add(seat_col2);
        addView.getChildren().addAll(trainQueueTable);
        AnchorPane.setRightAnchor(trainQueueTable,20d);
        AnchorPane.setTopAnchor(trainQueueTable,40d);

//        close button
        Button closeButFirst = new Button("close");
        closeButFirst.setStyle("-fx-background-color: #d21e3c; ");
        closeButFirst.setOnMouseEntered(event ->  closeButFirst.setStyle("-fx-background-color: #c64058; "));
        closeButFirst.setOnMouseExited(event ->  closeButFirst.setStyle("-fx-background-color: #d21e3c; "));
        closeButFirst.setMinSize(120, 60);
        closeButFirst.setOnAction(event -> {
            window.close();
            listOption();
        });
        addView.getChildren().addAll(closeButFirst);
        AnchorPane.setRightAnchor(closeButFirst,10d);
        AnchorPane.setBottomAnchor(closeButFirst,10d);

//        add button
        Button addButFirst = new Button("Add");
        addButFirst.setStyle("-fx-background-color: #0e3c50; ");
        addButFirst.setMinSize(120, 60);
        addButFirst.setOnMouseEntered(event ->  addButFirst.setStyle("-fx-background-color: #1a5680; "));
        addButFirst.setOnMouseExited(event ->  addButFirst.setStyle("-fx-background-color: #0e3c50; "));
        addButFirst.setOnAction(event -> {
//            alert if train queue is empty
            if (trainQueue.isFull()){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Train Queue is full");
                a.show();

            }
//            alert if Waiting room is empty
            else if (getDataToTable(waitingRoom).isEmpty()){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Waiting Room is Empty");
                a.show();

            }
            else {
//                generate a random number from 1-6
                int generateNo = (int) (Math.random() * ((6 - 1) + 1)) + 1;
                if (getDataToTable(waitingRoom).size() < generateNo) {
                    generateNo = getDataToTable(waitingRoom).size();
                }
                int i = 0;
                for (int j = 0; j <= waitingRoom.length; j++) {
//                      if array length without null is zero
                    if (getDataToTable(waitingRoom).size() == 0) break;
                    if (waitingRoom[j] != null) {
//                        add to train queue
                        trainQueue.add(waitingRoom[j]);
//                        remove from waiting room
                        waitingRoom[j] = null;
                        i++;
                        if (i == generateNo) break;
                    }
                }
            }
            sortValueToEnd(null,waitingRoom);
            sortTrainQueue();
//            refresh values in both table views
            trainQueueTable.setItems(getDataToTable(trainQueue.getQueueArray()));
            waitingRoomTable.setItems(getDataToTable(waitingRoom));
        });
        addView.getChildren().addAll(addButFirst);
        AnchorPane.setRightAnchor(addButFirst,140d);
        AnchorPane.setBottomAnchor(addButFirst,10d);
    }

    /**
     *
     * this method is used to view the passenger queues using a 42 slot grid
     * @param arrayToView this parameter will pass the name of the array being
     *                    displayed currently
     *
     */
    private  void view(Passenger[] arrayToView) {
//        create scene
        Stage window = new Stage();
        window.setTitle("train queue");
        AnchorPane viewView = new AnchorPane();
        GridPane first = new GridPane();
        first.setPadding(new Insets(2, 2, 2, 2));
        first.setHgap(10);
        first.setVgap(10);
        Scene addViewFirst = new Scene(viewView, 924, 570);
        addViewFirst.getStylesheets().add("style.css");
        window.setScene(addViewFirst);
        window.show();

//        create a variable to handle the position of the toggle
        double x=0;
        if (arrayToView.equals(waitingRoom)) x=5d;
        if (arrayToView.equals(reportData)) x=320d;
        if (arrayToView.equals(trainQueue.getQueueArray())) x=160d;

//        looping through 42 to create a grid of slots
        int number=0;
        for (int r = 2; r < 9; r++) {
            for (int c = 2; c < 8; c++) {
                if (number <42)
                {
                    Rectangle passengerData = new Rectangle();
                    passengerData.setHeight(60);
                    passengerData.setWidth(120);
                    passengerData.setArcHeight(12);
                    passengerData.setArcWidth(12);
                    passengerData.setFill(Paint.valueOf("#0E3C50"));
                    Label passengerDataText = new Label();
                    passengerDataText.setFont(new Font("Arial", 15));
                    passengerDataText.setPadding(new Insets(0, 0, 0, 8));
                    if (arrayToView[number]!=null){
//                        if passenger arrived add the name & seat number
                        if (arrayToView[number].getArrived()) {
                            passengerDataText.setText(
                                    arrayToView[number].getName() + "\n" +
                                    arrayToView[number].getSeat() + "| " +
                                    arrayToView[number].getTicketNumber());
                        }else {
//                            if not arrived add the seat number and Not arrived
                            passengerDataText.setText(arrayToView[number].getSeat() + "| " +"Not Arrived");
                        }
                    }
                    first.add(passengerData, c, r);
                    first.add(passengerDataText, c, r);
                    number++;
                }
            }
        }

//        toggle to indicate active page
        Rectangle passengerHeadBox = new Rectangle();
        passengerHeadBox.setHeight(50);
        passengerHeadBox.setWidth(160);
        passengerHeadBox.setArcHeight(25);
        passengerHeadBox.setArcWidth(25);
        passengerHeadBox.setFill(Paint.valueOf("#212121"));
        viewView.getChildren().add(passengerHeadBox);
        AnchorPane.setTopAnchor(passengerHeadBox,5d);
        AnchorPane.setLeftAnchor(passengerHeadBox,x);

        viewView.getChildren().add(first);
        AnchorPane.setTopAnchor(first,45d);
        AnchorPane.setLeftAnchor(first,10d);

//        label for waiting room
        Label passengerViewTextW = new Label();
        passengerViewTextW.setText("Waiting Room");
        passengerViewTextW.setFont(new Font("Arial", 23));
        viewView.getChildren().add(passengerViewTextW);
        AnchorPane.setLeftAnchor(passengerViewTextW,10d);
        AnchorPane.setTopAnchor(passengerViewTextW,15d);
        passengerViewTextW.setOnMouseClicked(event -> {
            window.close();
            view(waitingRoom);
        });

//        label for train queue
        Label passengerViewTextT = new Label();
        passengerViewTextT.setText("Train Queue");
        passengerViewTextT.setFont(new Font("Arial", 23));
        viewView.getChildren().add(passengerViewTextT);
        AnchorPane.setLeftAnchor(passengerViewTextT,178d);
        AnchorPane.setTopAnchor(passengerViewTextT,15d);
        passengerViewTextT.setOnMouseClicked(event -> {
            window.close();
            view(trainQueue.getQueueArray());
        });

//        label for boarded in
        Label passengerViewTextR = new Label();
        passengerViewTextR.setText("Boarded In");
        passengerViewTextR.setFont(new Font("Arial", 23));
        viewView.getChildren().add(passengerViewTextR);
        AnchorPane.setLeftAnchor(passengerViewTextR,345d);
        AnchorPane.setTopAnchor(passengerViewTextR,15d);
        passengerViewTextR.setOnMouseClicked(event -> {
            window.close();
            view(reportData);
        });

//        close button
        Button closeBut = new Button("close");
        closeBut.setMinSize(100, 60);
        closeBut.setStyle("-fx-background-color: #d21e3c; ");
        closeBut.setOnMouseEntered(event ->  closeBut.setStyle("-fx-background-color: #c64058; "));
        closeBut.setOnMouseExited(event ->  closeBut.setStyle("-fx-background-color: #d21e3c; "));
        closeBut.setOnAction(event -> {
            window.close();
            listOption();
        });
        viewView.getChildren().add(closeBut);
        AnchorPane.setBottomAnchor(closeBut,10d);
        AnchorPane.setRightAnchor(closeBut,10d);
    }

    /**
     * this parameter will be used to sort a given object to the end of the
     * given array without changing the order of the array
     * @param value
     * @param arrayName
     */
    private void sortValueToEnd(Passenger value,Passenger[]  arrayName){
        int j = 0;
        for (int i = 0; i < waitingRoom.length; i++) {
            if (arrayName[i] != value) {
                Passenger temp = arrayName[j];
                arrayName[j] = arrayName[i];
                arrayName[i] = temp;
                j++;
            }
        }
    }

    /**
     *
     * this method is used to sort the train queue according to the seat number
     */
    public void sortTrainQueue() {

        for (int a = 1; a < trainQueue.getLength()+1; a++) {
            for (int b = 0; b < trainQueue.getLength()+1 - a-1; b++) {
                if ((Integer.parseInt(trainQueue.getQueueArray()[b].getSeat())>(Integer.parseInt(trainQueue.getQueueArray()[b + 1].getSeat())))) {
                    Passenger temp = trainQueue.getQueueArray()[b];
                    trainQueue.getQueueArray()[b] = trainQueue.getQueueArray()[b + 1];
                    trainQueue.getQueueArray()[b + 1] = temp;
                }
            }
        }
    }

    /**
     *
     * this method is used to remove passengers from the train queue using the
     * seat number which will be taken in as a user input
     */
    private  void delete() {
        Scanner scanSeat= new Scanner(System.in);
        System.out.println("> Enter Seat Number");
        String deleteSeat = scanSeat.next();
        int lenNo=0;
        for (Passenger index: waitingRoom){
            if (index==null) lenNo++;
            if (index!=null) break;
        }

        List<Passenger> deleteArray = new ArrayList<>(Arrays.asList(trainQueue.getQueueArray()));
        for (Passenger temp: trainQueue.getQueueArray()){
            if(temp==null){
                System.out.println("invalid seat number");
                break;
            } else if (temp.getSeat().equals(deleteSeat)) {
                if (!temp.getArrived()) {
                    System.out.println("passenger has not arrived");
                    break;
                }
                deleteArray.remove(temp);
                trainQueue.removeSeat();
                deleteArray.add(null);
                trainQueue.setQueueArray(deleteArray.toArray(new Passenger[0]));
                System.out.println("Deleted Passenger");
                System.out.println("Name: "+temp.getName()+"\n"+"Seat No: "+temp.getSeat());
                break;
            }
        }
        listOption();
    }

    /**
     * this method is used to save the records in waiting room, train queue & the boarded seats
     * to a mongo database
     */
    private  void save() {
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
            userDocument.append("Arrived", data.getArrived());
            trainqueue.insertOne(userDocument);
        }
        System.out.println("|-Train Queue Saved");

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
            userDocument.append("Arrived", data.getArrived());
            waitingroom.insertOne(userDocument);
        }
        System.out.println("|-Waiting Room Saved");

        MongoCollection<Document> reportdata = dbDatabase.getCollection("ReportData");
        System.out.println("connected to ReportData");
        FindIterable<Document> reportDocument = reportdata.find();
        if(reportdata.countDocuments()>1){
            for(Document document: reportDocument) {
                reportdata.deleteOne(document);
            }
        }
        for (Passenger data : reportData){
            Document userDocument = new Document();
            if (data==null) continue;
            userDocument.append("Name", data.getName());
            userDocument.append("Seat", data.getSeat());
            userDocument.append("TicketNo", data.getTicketNumber());
            userDocument.append("Date", data.getDate());
            userDocument.append("Station", data.getStation());
            userDocument.append("Train", data.getTrain());
            userDocument.append("Seconds",data.getSecondsInQueue());
            userDocument.append("Arrived", data.getArrived());
            reportdata.insertOne(userDocument);
        }
        System.out.println("|-Report Data Saved");


        dbClient.close();
        System.out.println("saved files");
        listOption();
    }

    /**
     *
     * this method is used to load records back to the waiting room,train queue & boarded seats
     * from the mongo database
     */
    private  void load() {
        trainQueue.clearQueue();
        Arrays.fill(reportData,null);
        Arrays.fill(waitingRoom, null);
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

        if(trainqueue.countDocuments()>0) {
            for(Document document:queueDocument) {
                String name    = document.getString("Name");
                String seat    = document.getString("Seat");
                String ticketNo= document.getString("TicketNo");
                String date    = document.getString("Date");
                String station = document.getString("Station");
                String train   = document.getString("Train");
                boolean Arrived= document.getBoolean("Arrived");

                Passenger passengerObj = new Passenger();
                passengerObj.setTicketNumber(ticketNo);
                passengerObj.setName(name);
                passengerObj.setDate(date);
                passengerObj.setStation(station);
                passengerObj.setTrain(train);
                passengerObj.setSeat(seat);
                passengerObj.setArrived(Arrived);
                trainQueue.add(passengerObj);
            }
            System.out.println("|-Train Queue loaded");
        }
        else { System.out.println("|-Train Queue not loaded");
        }

        MongoCollection<Document> waitingroom = dbDatabase.getCollection("WaitingRoomData");
        System.out.println("connected to WaitingRoomData");
        FindIterable<Document> waitingRoomDocument = waitingroom.find();

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
                boolean Arrived= document.getBoolean("Arrived");

                Passenger passengerObj = new Passenger();
                passengerObj.setTicketNumber(ticketNo);
                passengerObj.setName(name);
                passengerObj.setDate(date);
                passengerObj.setStation(station);
                passengerObj.setTrain(train);
                passengerObj.setSeat(seat);
                passengerObj.setArrived(Arrived);
                waitingRoom[i]=(passengerObj);
                i++;
            }
            System.out.println("|-Waiting Rooom loaded");
        }
        else { System.out.println("|-Waiting Room not loaded");
        }

        MongoCollection<Document> reportdata = dbDatabase.getCollection("ReportData");
        System.out.println("connected to ReportData");
        FindIterable<Document> reportDocument = reportdata.find();
        Arrays.fill(reportData, null);
        if(reportdata.countDocuments()>0) {
            int i=0;
            for(Document document:reportDocument)
            {
                String name    = document.getString("Name");
                String seat    = document.getString("Seat");
                String ticketNo= document.getString("TicketNo");
                String date    = document.getString("Date");
                String station = document.getString("Station");
                String train   = document.getString("Train");
                int seconds = document.getInteger("Seconds");
                boolean Arrived= document.getBoolean("Arrived");

                Passenger passengerObj = new Passenger();
                passengerObj.setTicketNumber(ticketNo);
                passengerObj.setName(name);
                passengerObj.setDate(date);
                passengerObj.setStation(station);
                passengerObj.setTrain(train);
                passengerObj.setSeat(seat);
                passengerObj.setSecondsInQueue(seconds);
                passengerObj.setArrived(Arrived);
                reportData[i]=(passengerObj);
                i++;
            }
            System.out.println("|-Report Data loaded");
        }
        else { System.out.println("|-Report not loaded");
        }

//        close mongo client
        dbClient.close();
        listOption();
    }

    /**
     *
     * this method is used to move passsengers from train queue to the boarded seats
     * automacticly & to collect staticstics
     */
    private  void run() {
        if (trainQueue.isEmpty() && getDataToTable(reportData).isEmpty()){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Data Insufficient for Simulation");
                a.setContentText("Train Queue and Boarded contain no records");
                a.showAndWait();
                listOption();
        }else {
            int queueDelay = 0;
            int i=0;
            int lenReport =0;
            int lenNoArrive=0;
            int minimumWaitTime = 0;
            int maximumWaitTime = 0;
            for (Passenger index: reportData){
                if (index!=null) {
                    lenReport++;
                    if (!index.getArrived()) lenNoArrive++;
                }
            }
            for (Passenger index: trainQueue.getQueueArray()){
                if (index!=null) {
                    if (!index.getArrived()) lenNoArrive++;
                }
            }
            for (Passenger pasangerObjest: trainQueue.getQueueArray()){
                //TODO change to break
                if (pasangerObjest==null) continue;
                reportData[lenReport+i]=pasangerObjest;
                trainQueue.remove();
                trainQueue.getQueueArray()[i]=null;
                i++;
            }
            trainQueue.setMaxStayInQueue(lenReport+i-lenNoArrive);
            trainQueue.setrest();

            for (Passenger pasangerObjest: reportData){
                //TODO change to break
                if (pasangerObjest==null) continue;
                int genDelay= 3 + (int) (Math.random() * (18 - 3 + 1));
                pasangerObjest.setSecondsInQueue(genDelay);
                if (!pasangerObjest.getArrived())pasangerObjest.setSecondsInQueue(0);
                queueDelay=queueDelay+pasangerObjest.getSecondsInQueue();
                pasangerObjest.setSecondsInQueue(queueDelay);
                if(minimumWaitTime== 0){
                    minimumWaitTime=queueDelay;
                }
                if (queueDelay>maximumWaitTime) {
                    maximumWaitTime=queueDelay;
                }
                if (queueDelay<minimumWaitTime) {
                    minimumWaitTime=queueDelay;
                }
            }
            float averageSecondsInQueue = Math.round(queueDelay/trainQueue.getMaxStayInQueue());
            System.out.println("averageSecondsInQueue >"+averageSecondsInQueue);
            System.out.println("minimumWaitingTime >"+minimumWaitTime);
            System.out.println("maximumWaitingTime >"+maximumWaitTime);
            System.out.println("maximumLengthQueue >"+trainQueue.getMaxStayInQueue());
            runGui(minimumWaitTime,maximumWaitTime,averageSecondsInQueue);
        }
    }

    /**
     * this method is used to display a report generated using the statistics
     * collected in the run method
     * @param minimumWaitTime this passes the lowest wait time for a passenger
     * @param maximumWaitTime this passes the highest wait time for a passenger
     * @param averageSecondsInQueue passes the average wait time for all
     */
    public   void runGui(int minimumWaitTime,int maximumWaitTime,double averageSecondsInQueue){

        Stage window = new Stage();
        window.setTitle("Add to Train Queue");
        GridPane addView = new GridPane();
        AnchorPane runView = new AnchorPane();
        addView.setPadding(new Insets(2, 2, 2, 2));
        addView.setHgap(10);
        addView.setVgap(10);
        Scene addViewFirst = new Scene(runView, 760, 624);
        addViewFirst.getStylesheets().add("style.css");
        window.setScene(addViewFirst);

        TableView<Passenger> ReportTable;
        TableColumn<Passenger,String> ticket_col = new TableColumn<>("Ticket");
        ticket_col.setMinWidth(100);
        ticket_col.setCellValueFactory(new PropertyValueFactory<>("ticketNumber"));
        TableColumn<Passenger,String> name_col = new TableColumn<>("Name");
        name_col.setMinWidth(100);
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Passenger,String> seat_col = new TableColumn<>("Seat");
        seat_col.setMinWidth(100);
        seat_col.setCellValueFactory(new PropertyValueFactory<>("seat"));
        TableColumn<Passenger,String> seconds_col = new TableColumn<>("Seconds");
        seconds_col.setMinWidth(100);
        seconds_col.setCellValueFactory(new PropertyValueFactory<>("secondsInQueue"));
        TableColumn<Passenger,String> Station_col = new TableColumn<>("Station");
        Station_col.setMinWidth(100);
        Station_col.setCellValueFactory(new PropertyValueFactory<>("Station"));
        TableColumn<Passenger,String> Arrived_col = new TableColumn<>("Arrived");
        Arrived_col.setMinWidth(100);
        Arrived_col.setCellValueFactory(new PropertyValueFactory<>("Station"));

        TableView<Passenger> ReportTable2;
        TableColumn<Passenger,String> ticket_col2 = new TableColumn<>("Ticket");
        ticket_col2.setMinWidth(100);
        ticket_col2.setCellValueFactory(new PropertyValueFactory<>("ticketNumber"));
        TableColumn<Passenger,String> name_col2 = new TableColumn<>("Name");
        name_col2.setMinWidth(100);
        name_col2.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Passenger,String> seat_col2 = new TableColumn<>("Seat");
        seat_col2.setMinWidth(100);
        seat_col2.setCellValueFactory(new PropertyValueFactory<>("seat"));
        TableColumn<Passenger,String> seconds_col2 = new TableColumn<>("Seconds");
        seconds_col2.setMinWidth(100);
        seconds_col2.setCellValueFactory(new PropertyValueFactory<>("secondsInQueue"));
        TableColumn<Passenger,String> Station_col2 = new TableColumn<>("Station");
        Station_col2.setMinWidth(100);
        Station_col2.setCellValueFactory(new PropertyValueFactory<>("Station"));
        TableColumn<Passenger,String> Arrived_col2 = new TableColumn<>("Arrived");
        Arrived_col2.setMinWidth(100);
        Arrived_col2.setCellValueFactory(new PropertyValueFactory<>("Station"));


        ReportTable = new TableView<>();
        ReportTable.setPlaceholder(new Label("No Passenger has boarded"));
        ReportTable.setMinWidth(300);
        ReportTable.setMaxHeight(250);
        ReportTable.setItems(getDataToTable(reportData));
        ReportTable.getColumns().add(ticket_col);
        ReportTable.getColumns().add(name_col);
        ReportTable.getColumns().add(seat_col);
        ReportTable.getColumns().add(seconds_col);
        ReportTable.getColumns().add(Station_col);
        runView.getChildren().add(ReportTable);
        AnchorPane.setTopAnchor(ReportTable,40d);
        AnchorPane.setLeftAnchor(ReportTable,10d);

        Passenger[] notArrivedArray =new Passenger[42];
        int j=0;
        for (Passenger notArrived: reportData){

            if (notArrived!=null) {
                if (!notArrived.getArrived()){
                    notArrivedArray[j]=notArrived;
                    j++;
                }
            }
        }
        ReportTable2 = new TableView<>();
        ReportTable2.setPlaceholder(new Label("All Passengers have Arrrived"));
        ReportTable2.setMinWidth(300);
        ReportTable2.setMaxHeight(250);
        ReportTable2.setItems(getDataToTable(notArrivedArray));
        ReportTable2.getColumns().add(ticket_col2);
        ReportTable2.getColumns().add(name_col2);
        ReportTable2.getColumns().add(seat_col2);
        //ReportTable2.getColumns().add(seconds_col2);
        ReportTable2.getColumns().add(Station_col2);
        runView.getChildren().add(ReportTable2);
        AnchorPane.setTopAnchor(ReportTable2,340d);
        AnchorPane.setLeftAnchor(ReportTable2,10d);

        Label passengerRunText = new Label();
        passengerRunText.setText("Boarded ");
        passengerRunText.setFont(new Font("Arial", 18));
        runView.getChildren().add(passengerRunText);
        AnchorPane.setLeftAnchor(passengerRunText,10d);
        AnchorPane.setTopAnchor(passengerRunText,10d);

        Label passengerRunText2 = new Label();
        passengerRunText2.setText("Not Arrived ");
        passengerRunText2.setFont(new Font("Arial", 18));
        runView.getChildren().add(passengerRunText2);
        AnchorPane.setLeftAnchor(passengerRunText2,10d);
        AnchorPane.setTopAnchor(passengerRunText2,310d);

        Button closeBut = new Button("close");
        closeBut.setMinSize(100, 60);
        closeBut.setStyle("-fx-background-color: #d21e3c; ");
        closeBut.setOnMouseEntered(event ->  closeBut.setStyle("-fx-background-color: #c64058; "));
        closeBut.setOnMouseExited(event ->  closeBut.setStyle("-fx-background-color: #d21e3c; "));
        closeBut.setOnAction(event -> {
            window.close();
            listOption();
        });
        runView.getChildren().add(closeBut);
        AnchorPane.setBottomAnchor(closeBut,10d);
        AnchorPane.setRightAnchor(closeBut,10d);

        VBox reportArea = new VBox();
        reportArea.setSpacing(10d);
        for (int i=0;i<4;i++) {
            Rectangle reportBox = new Rectangle();
            reportBox.setHeight(73);
            reportBox.setWidth(210);
            reportBox.setArcHeight(12);
            reportBox.setArcWidth(12);
            reportBox.setFill(Paint.valueOf("#0E3C50"));
            reportArea.getChildren().add(reportBox);
        }
        runView.getChildren().add(reportArea);
        AnchorPane.setTopAnchor(reportArea,40d);
        AnchorPane.setRightAnchor(reportArea,14d);

        Font reportFont = Font.font("Arial", FontWeight.valueOf("BOLD"),17);

        VBox reportDetails = new VBox();
        reportDetails.setSpacing(43d);
        Label reportMinTime = new Label();
        reportMinTime.setText("Minimum Waiting Time\n"+minimumWaitTime+"s");
        reportMinTime.setFont(reportFont);
        Label reportMaxTime = new Label();
        reportMaxTime.setText("Maximum Waiting Time\n"+maximumWaitTime+"s");
        reportMaxTime.setFont(reportFont);
        Label reportMaxInqueue = new Label();
        reportMaxInqueue.setText("Length In Queue\n"+trainQueue.getMaxStayInQueue()+" Passengers");
        reportMaxInqueue.setFont(reportFont);
        Label reportAverageTime = new Label();
        reportAverageTime.setText("Average Time\n"+ averageSecondsInQueue+"s");
        reportAverageTime.setFont(reportFont);

        reportDetails.getChildren().addAll(reportMinTime,reportMaxTime,reportMaxInqueue,reportAverageTime);
        runView.getChildren().add(reportDetails);
        AnchorPane.setTopAnchor(reportDetails,50d);
        AnchorPane.setRightAnchor(reportDetails,28d);

        window.show();
    }

    public static void main(String[]args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        importGui();
    }
}