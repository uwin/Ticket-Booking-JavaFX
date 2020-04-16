import java.util.Random;

public class Passenger {

    public Passenger(String firstName, String secondName, String seatNumber) {
        this.name= firstName+" "+secondName;
        this.seat = seatNumber;
        Random rand = new Random();
        int randNum = rand.nextInt(100) + 1;
        this.ticketNumber = "TK"+String.valueOf(randNum);
        this.secondsInQueue=0;
    }
    public Passenger() {
    }
    //private String firstName;
    //private String secondName;
    private String name;
    private String seat;
    private String ticketNumber;
    private String date;
    private String station;
    private String train;
    private int secondsInQueue;

    public String getTrain() {
        return train;
    }
    public void setTrain(String train) {
        this.train = train;
    }

    public String getStation() {
        return station;
    }
    public void setStation(String station) {
        this.station = station;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public void setTicketNumber(String ticketNumber) {this.ticketNumber=ticketNumber;}
    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getSeat(){
        return seat;
    }
    public void setSeat(String seatNumber ){
        seat = seatNumber;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name= name;
    }

    public int getSecondsInQueue() {
        return secondsInQueue;
    }
    public void setSecondsInQueue(int sec ) {
        secondsInQueue=sec;
    }

    public void display() {

    }
}
