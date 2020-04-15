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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.bson.Document;
import java.time.LocalDate;
import java.util.*;

public class TrainStation extends Application{
    private static Passenger[] waitingRoom= new Passenger[42];
    private  PassengerQueue trainQueue = new PassengerQueue();
    private static Passenger[] reportData = new Passenger[42];

    /*private  ObservableList<Passenger> getDataToTable(Passenger[] namearray){
        ObservableList<Passenger> table= FXCollections.observableArrayList();
        for (Passenger i: namearray) {
            if (i!=null){
                table.add(i);
            }
        }
        return table;
    }*/
    private  ObservableList<Passenger> getWaitRoomData(){
        ObservableList<Passenger> passengers= FXCollections.observableArrayList();
        for (Passenger i: waitingRoom) {
            if (i!=null){
                passengers.add(i);
            }
        }
        return passengers;
    }
    private  ObservableList<Passenger> getTrainQueueData(){
        ObservableList<Passenger>queuePassengers= FXCollections.observableArrayList();

        for (Passenger i: trainQueue.getQueueArray())
            if (i!=null){
                queuePassengers.add(i);
            }
        return queuePassengers;
    }
    private  ObservableList<Passenger> getReportData(){
        ObservableList<Passenger>recordPassengers= FXCollections.observableArrayList();

        for (Passenger i: reportData)
            if (i!=null){
                recordPassengers.add(i);
            }
        return recordPassengers;
    }

    private  void importGui(){
        Stage window = new Stage();
        window.setTitle("Train Booking System");
        GridPane first = new GridPane();
        first.setPadding(new Insets(2, 2, 2, 2));
        first.setHgap(10);
        first.setVgap(10);
        Scene addViewFirst = new Scene(first, 650, 400);
        window.setScene(addViewFirst);
        window.show();

        Label head = new Label("Denuwara Menike Ticket Booking System\n" +
                "                   A/C compartment");
        head.setFont(new Font("Arial", 30));
        head.setTextFill(Color.web("#0076a3")); //light blue
        first.add(head,5,3,60,8);

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
        Label headStart = new Label("Station");
        headStart.setFont(new Font("Arial", 23));
        headStart.setTextFill(Color.web("#0076a3")); //light blue
        first.add(headStart,3,16,9,4);

//        drop down menu for start
        ComboBox<String> stationDrop = new ComboBox<>();
        stationDrop.getItems().addAll(stops);
        stationDrop.setValue("Colombo Fort");
        first.add(stationDrop,12,16,18,4);


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

        first.add(colomboSButton,15,20,40,12);

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
        first.add(badullaSButton,35,20,40,12);

        Button closeButFirst = new Button("close");
        closeButFirst.setMaxSize(120, 60);
        closeButFirst.setStyle("-fx-background-color: red; ");
        closeButFirst.setOnAction(event -> {
            window.close();
            listOption();
        });
        first.add(closeButFirst,54,30,10,12);

        Button skipButFirst = new Button("Skip");
        skipButFirst.setMaxSize(120, 60);
        skipButFirst.setStyle("-fx-background-color: brown; ");
        skipButFirst.setOnAction(event -> {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Programme Will Continue Without Loading Data");
            a.showAndWait();
            window.close();
            listOption();
        });
        first.add(skipButFirst,44,30,10,12);

    }

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
            a.setHeaderText("No Saved Data is Unavailable\n programme will continue without loading Data");
            a.showAndWait();
            listOption();
        }
//        close mongo client
        dbClient.close();
    }

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
        switch (userOption) {
            case "A":
                add();
                break;
            case "V":
                view(trainQueue.getQueueArray(),160d);
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

    private  void add() {
        Stage window = new Stage();
        window.setTitle("Add to Train Queue");
        AnchorPane addView = new AnchorPane();

        Scene addViewFirst = new Scene(addView, 680, 580);
        window.setScene(addViewFirst);
        window.show();

        Label waitingRoomHead = new Label("Waiting Room");
        waitingRoomHead.setFont(new Font("Arial", 23));
        //waitingRoomHead.setTextFill(Color.web("#black")); //light blue
        addView.getChildren().add(waitingRoomHead);
        AnchorPane.setLeftAnchor(waitingRoomHead,20d);
        AnchorPane.setTopAnchor(waitingRoomHead,10d);

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
        waitingRoomTable.setMinHeight(450);
        waitingRoomTable.setItems(getWaitRoomData());
        waitingRoomTable.getColumns().add(ticket_col);
        waitingRoomTable.getColumns().add(name_col);
        waitingRoomTable.getColumns().add(seat_col);
        addView.getChildren().addAll(waitingRoomTable);
        AnchorPane.setLeftAnchor(waitingRoomTable,20d);
        AnchorPane.setTopAnchor(waitingRoomTable,40d);



        Label trainQueueHead = new Label("Train Queue");
        trainQueueHead.setFont(new Font("Arial", 23));
        //trainQueueHead.setTextFill(Color.web("#black")); //light blue
        addView.getChildren().addAll(trainQueueHead);
        AnchorPane.setRightAnchor(trainQueueHead,200d);
        AnchorPane.setTopAnchor(trainQueueHead,10d);
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
        trainQueueTable.setMinHeight(450);
        trainQueueTable.setItems(getTrainQueueData());
        trainQueueTable.getColumns().add(ticket_col2);
        trainQueueTable.getColumns().add(name_col2);
        trainQueueTable.getColumns().add(seat_col2);
        addView.getChildren().addAll(trainQueueTable);
        AnchorPane.setRightAnchor(trainQueueTable,20d);
        AnchorPane.setTopAnchor(trainQueueTable,40d);

        Button closeButFirst = new Button("close");
        closeButFirst.setStyle("-fx-background-color: red; ");
        closeButFirst.setMinSize(120, 60);
        closeButFirst.setOnAction(event -> {
            window.close();
            listOption();
        });
        addView.getChildren().addAll(closeButFirst);
        AnchorPane.setRightAnchor(closeButFirst,10d);
        AnchorPane.setBottomAnchor(closeButFirst,10d);

        Button addButFirst = new Button("Add");
        addButFirst.setStyle("-fx-background-color: lightblue; ");
        addButFirst.setMinSize(120, 60);
        addButFirst.setOnAction(event -> {
            if (trainQueue.isFull()){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Train Queue is full");
                a.show();
            }else if (getWaitRoomData().isEmpty()){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Waiting Room is Empty");
                a.show();
            } else {
                int generateNo = (int) (Math.random() * ((6 - 1) + 1)) + 1;
                if (getWaitRoomData().size() < generateNo) generateNo = getWaitRoomData().size();
                int i = 0;
                for (int j = 0; j <= TrainStation.waitingRoom.length; j++) {
                    //if (trainQueue.isFull())
                    if (getWaitRoomData().size() == 0) break;
                    if (TrainStation.waitingRoom[j] != null) {
                        trainQueue.add(TrainStation.waitingRoom[j]);
                        TrainStation.waitingRoom[j] = null;
                        i++;
                        if (i == generateNo) break;
                    }
                }
            }
            trainQueue.sortSeat(trainQueue.getQueueArray(),trainQueue.getLength()+1);
            trainQueueTable.setItems(getTrainQueueData());
            waitingRoomTable.setItems(getWaitRoomData());
        });
        addView.getChildren().addAll(addButFirst);
        AnchorPane.setRightAnchor(addButFirst,140d);
        AnchorPane.setBottomAnchor(addButFirst,10d);
    }

    private  void view(Passenger[] arrayToView,double x) {
        Stage window = new Stage();
        window.setTitle("train queue");
        AnchorPane viewView = new AnchorPane();
        GridPane first = new GridPane();
        first.setPadding(new Insets(2, 2, 2, 2));
        first.setHgap(10);
        first.setVgap(10);
        Scene addViewFirst = new Scene(viewView, 924, 540);
        window.setScene(addViewFirst);
        window.show();

        int number=1;
        for (int r = 2; r < 9; r++) {
            for (int c = 2; c < 8; c++) {
                if (number <=42)
                {
                    Rectangle passengerData= new Rectangle();
                    passengerData.setHeight(60);
                    passengerData.setWidth(120);
                    passengerData.setArcHeight(12);
                    passengerData.setArcWidth(12);
                    passengerData.setFill(Color.LIGHTGRAY);
                    Label passengerDataText = new Label();
                    passengerDataText.setFont(new Font("Arial", 15));
                    passengerDataText.setPadding(new Insets(0,0,0,8));
                    if (arrayToView[number-1]!=null){
                            passengerDataText.setText(
                            arrayToView[number-1].getName()+"\n"+
                            arrayToView[number-1].getSeat()+"|"+
                            arrayToView[number-1].getTicketNumber());
                    }
                    number++;
                    first.add(passengerData, c, r);
                    first.add(passengerDataText, c, r);
                }
            }
        }

        Rectangle passengetHeadBox = new Rectangle();
        passengetHeadBox.setHeight(50);
        passengetHeadBox.setWidth(160);
        passengetHeadBox.setArcHeight(25);
        passengetHeadBox.setArcWidth(25);
        passengetHeadBox.setFill(Color.DIMGREY);
        viewView.getChildren().add(passengetHeadBox);
        AnchorPane.setTopAnchor(passengetHeadBox,1d);
        AnchorPane.setLeftAnchor(passengetHeadBox,x);



        viewView.getChildren().add(first);
        AnchorPane.setTopAnchor(first,40d);
        AnchorPane.setLeftAnchor(first,10d);

        Label passengerViewTextv = new Label();
        passengerViewTextv.setText("Waiting Room");
        passengerViewTextv.setFont(new Font("Arial", 23));
        viewView.getChildren().add(passengerViewTextv);
        AnchorPane.setLeftAnchor(passengerViewTextv,10d);
        AnchorPane.setTopAnchor(passengerViewTextv,10d);
        passengerViewTextv.setOnMouseClicked(event -> {
            window.close();
            view(waitingRoom,5d);
        });

        Label passengerViewTextt = new Label();
        passengerViewTextt.setText("Train Queue");
        passengerViewTextt.setFont(new Font("Arial", 23));
        viewView.getChildren().add(passengerViewTextt);
        AnchorPane.setLeftAnchor(passengerViewTextt,170d);
        AnchorPane.setTopAnchor(passengerViewTextt,10d);
        passengerViewTextt.setOnMouseClicked(event -> {
            window.close();
            view(trainQueue.getQueueArray(),160d);
        });

        Label passengerViewTextr = new Label();
        passengerViewTextr.setText("Boarded In");
        passengerViewTextr.setFont(new Font("Arial", 23));
        viewView.getChildren().add(passengerViewTextr);
        AnchorPane.setLeftAnchor(passengerViewTextr,320d);
        AnchorPane.setTopAnchor(passengerViewTextr,10d);
        passengerViewTextr.setOnMouseClicked(event -> {
            window.close();
            view(reportData,320d);
        });


        Button closeBut = new Button("close");
        closeBut.setMinSize(100, 60);
        closeBut.setStyle("-fx-background-color: red; ");
        closeBut.setOnAction(event -> {
            window.close();
            listOption();
        });
        viewView.getChildren().add(closeBut);
        AnchorPane.setBottomAnchor(closeBut,10d);
        AnchorPane.setRightAnchor(closeBut,10d);
    }

    private  void delete() {
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
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Deleted\n"+"Name: "+temp.getName()+"\n"+"Seat No: "+temp.getSeat());
                a.showAndWait();
                break;
            }
        }
        listOption();
    }

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
            reportdata.insertOne(userDocument);
        }


        dbClient.close();
        System.out.println("saved files");
        listOption();
    }

    private  void load() {
        Arrays.fill(reportData,null);
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
        Arrays.fill(waitingRoom, null);
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

                Passenger passengerObj = new Passenger(ticketNo,name);
                passengerObj.setDate(date);
                passengerObj.setStation(station);
                passengerObj.setTrain(train);
                passengerObj.setSeat(seat);
                passengerObj.setTrain(train);
                reportData[i]=(passengerObj);
                i++;
            }
            System.out.println("report data loaded");
        }
        else { System.out.println("no files were added, no data is changed");
        }

//        close mongo client
        dbClient.close();
        listOption();
    }

    private  void run() {
        //Arrays.fill(reportData,null);
        if (trainQueue.isEmpty()){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Train queue is Empty");
            a.showAndWait();
            listOption();
        }else {
            int queueDelay = 0;
            int i=0;
            int lenNoNull =0;
            for (Passenger index: reportData){
                if (index!=null) lenNoNull++;
            }
            for (Passenger pasangerObjest: trainQueue.getQueueArray()){
                if (pasangerObjest==null) {
                    i++;
                    continue;
                }
                int genDelay= 3 + (int) (Math.random() * ((18 - 3 + 1)));
                pasangerObjest.setSecondsInQueue(genDelay);
                reportData[lenNoNull+i]=pasangerObjest;
                if (i>trainQueue.getMaxlength()) trainQueue.setMaxlength(i);
                trainQueue.remove();
                trainQueue.getQueueArray()[i]=null;
                i++;
            }
            trainQueue.setrest();
            for (Passenger pasangerObjest: trainQueue.getQueueArray()){
                if (pasangerObjest==null) continue;
                queueDelay+=pasangerObjest.getSecondsInQueue();
                if (queueDelay>trainQueue.getMaxStayInQueue(queueDelay)) trainQueue.setMaxStayInQueue(queueDelay);
                if (queueDelay<trainQueue.getMinStayInQueue()) trainQueue.setMinStayInQueue(queueDelay);
                pasangerObjest.setSecondsInQueue(queueDelay);
            }
            float averageSecondsInQueue = (float)queueDelay/i;
            System.out.println("averageSecondsInQueue >"+averageSecondsInQueue);
            System.out.println("minimumWaitingTime >"+trainQueue.getMinStayInQueue());
            System.out.println("maximunWaitingTime >"+trainQueue.getMaxStayInQueue(queueDelay));
            System.out.println("maximunLengthQueue >"+trainQueue.getMaxlength());
            runGui();
        }
    }

    public   void runGui(){
        Stage window = new Stage();
        window.setTitle("Add to Train Queue");
        GridPane addView = new GridPane();
        AnchorPane runView = new AnchorPane();
        addView.setPadding(new Insets(2, 2, 2, 2));
        addView.setHgap(10);
        addView.setVgap(10);
        Scene addViewFirst = new Scene(runView, 760, 650);
        window.setScene(addViewFirst);

        TableView<Passenger> ReportTable;
        TableColumn<Passenger,String> ticket_col = new TableColumn<>("Ticket");
        ticket_col.setMinWidth(100);
        ticket_col.setCellValueFactory(new PropertyValueFactory<Passenger,String>("ticketNumber"));
        TableColumn<Passenger,String> name_col = new TableColumn<>("Name");
        name_col.setMinWidth(100);
        name_col.setCellValueFactory(new PropertyValueFactory<Passenger,String>("name"));
        TableColumn<Passenger,String> seat_col = new TableColumn<>("Seat");
        seat_col.setMinWidth(100);
        seat_col.setCellValueFactory(new PropertyValueFactory<Passenger,String>("seat"));
        TableColumn<Passenger,String> seconds_col = new TableColumn<>("Seconds");
        seconds_col.setMinWidth(100);
        seconds_col.setCellValueFactory(new PropertyValueFactory<Passenger,String>("secondsInQueue"));
        TableColumn<Passenger,String> Station_col = new TableColumn<>("Station");
        Station_col.setMinWidth(100);
        Station_col.setCellValueFactory(new PropertyValueFactory<Passenger,String>("Station"));


        ReportTable = new TableView<>();
        //waitingRoomTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ReportTable.setMinWidth(300);
        ReportTable.setMinHeight(450);
        ReportTable.setItems(getReportData());
        ReportTable.getColumns().add(ticket_col);
        ReportTable.getColumns().add(name_col);
        ReportTable.getColumns().add(seat_col);
        ReportTable.getColumns().add(seconds_col);
        ReportTable.getColumns().add(Station_col);
        runView.getChildren().add(ReportTable);
        AnchorPane.setTopAnchor(ReportTable,40d);
        AnchorPane.setLeftAnchor(ReportTable,10d);

        Label passengerRunText = new Label();
        passengerRunText.setText("Report ");
        passengerRunText.setFont(new Font("Arial", 23));
        runView.getChildren().add(passengerRunText);
        AnchorPane.setLeftAnchor(passengerRunText,10d);
        AnchorPane.setTopAnchor(passengerRunText,10d);

        Button closeBut = new Button("close");
        closeBut.setMinSize(100, 60);
        closeBut.setStyle("-fx-background-color: red; ");
        closeBut.setOnAction(event -> {
            window.close();
            listOption();
        });
        runView.getChildren().add(closeBut);
        AnchorPane.setBottomAnchor(closeBut,10d);
        AnchorPane.setRightAnchor(closeBut,10d);

        window.show();
    }

    public static void main(String[]args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        importGui();
    }
}