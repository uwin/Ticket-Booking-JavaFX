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

public class TrainStation {
    private static Passenger[] waitingRoom= new Passenger[42];
    private static PassengerQueue trainQueue = new PassengerQueue();

    private static void listOption() {
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


    private static void importData() {
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
                    trainNo="fromColombo";
                }else {trainNo="fromBadulla"; }

                String selectedDate="2020-04-06";
                String selectedStation= "Colombo Fort";
                String selectedTrain="fromColombo"; //"fromBadulla"
                Passenger passengerObj = new Passenger();

                if (    selectedDate.equalsIgnoreCase(date) &&
                        selectedStation.equalsIgnoreCase(start) &&
                        selectedTrain.equals(trainNo)
                ){
                    passengerObj.setName(name,surname);
                    passengerObj.setSeat(seat);
                }
                waitingRoom[i]=(passengerObj);
                i++;
            }
            System.out.println(Arrays.toString(waitingRoom));
            System.out.println("Passenger Data imported to Waiting Room");
        }
//        if not a message will be printed
        else
        {
            System.out.println("Passenger Data is Unavailable to be imported");
        }
//        close mongo client
        dbClient.close();
    }

    private static void add() { }
    private static void view() {}
    private static void delete() {}
    private static void save() {}
    private  static void load() {}
    private  static void run() {}

    public static void main(String[]args) {
        importData();
        //listOption();
    }
}
