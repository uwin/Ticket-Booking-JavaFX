import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.stage.Stage;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TrainStation extends Application {
    private  ArrayList<ArrayList<String>> waitingRoom = new ArrayList<>();;
    private  ArrayList<ArrayList<String>> trainQueue;

    private void listOption() {
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
//        calling methods depending on the users input form the previous method
//        gui methods will run though first gui
            case "A":
                break;
            case "V":
                break;
            case "D":
                break;
            case "S":
                break;
            case "L":
                loadOption();
                break;
            case "R":
                break;
            default:
                System.out.println("invalid input");
                break;
        }
    }

    private void saveOption() {
        com.mongodb.MongoClient dbClient = new MongoClient
                ("localhost", 27017);
        MongoDatabase dbDatabase = dbClient.getDatabase
                ("trainBookingSystem");
        MongoCollection<Document> bookings = dbDatabase.getCollection
                ("BookingData");
        System.out.println("connected to BookingData");
        FindIterable<Document> bookingDocument = bookings.find();

        if(bookings.countDocuments()>1)
        {
            for(Document document: bookingDocument)
            {
                bookings.deleteOne(document);
            }
        }

        for (ArrayList<String> data : waitingRoom)
        {
            Document userDocument = new Document();
            userDocument.append("Date", data.get(0));
            userDocument.append("Start",data.get(1));
            userDocument.append("End",  data.get(2));
            userDocument.append("User", data.get(3));
            userDocument.append("Seat", data.get(4));
            userDocument.append("Nic",  data.get(5));
            userDocument.append("Surname",  data.get(6));
            bookings.insertOne(userDocument);
        }
        dbClient.close();
        System.out.println("saved files");
    }

    private  void loadOption() {
//        getting user date in the format yyyy-mm-dd
        Scanner scanDate = new Scanner(System.in);
        System.out.println("enter Date: [yyyy-mm-dd]");
        String date= scanDate.next().toLowerCase();

//        getting user Nic
        Scanner scanStation = new Scanner(System.in);
        System.out.println("enter your station: ");
        String station= scanStation.nextLine();

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
//        creating a new a new array to collect values
        ArrayList<String> temporaryList = new ArrayList<>(5);
        for (int i = 0; i < 6; i++) temporaryList.add("0");
//        if a document exists the it will be added to the array
        if(bookings.countDocuments()>0)
        {
//         resetting the existing main data structure
            waitingRoom.clear();
            for(Document document:bookingDocument)
            {
                if(document.getString("Date").equals(date) & document.getString("Start").equalsIgnoreCase(station))
                {
                    temporaryList.set(0,document.getString("Date"));
                    temporaryList.set(1,document.getString("User"));
                    temporaryList.set(2,document.getString("Surname"));
                    temporaryList.set(3,document.getString("Seat"));
                    temporaryList.set(4,document.getString("Nic"));
                    if (document.getString("Start").equalsIgnoreCase("Colombo Fort"))
                    {
                        temporaryList.set(5,"1");
                    }else
                        temporaryList.set(5,"2");
                    waitingRoom.add(new ArrayList<>(temporaryList));
                }
//                adding collected sets of values to the main data structure
                }
            System.out.println(waitingRoom);
        }
//        if not a message will be printed
        else
        {
            System.out.println("no files were added, no data is changed");
        }
//        close mongo client
        dbClient.close();
    }

    public static void main(String[]args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        listOption();
    }
}
