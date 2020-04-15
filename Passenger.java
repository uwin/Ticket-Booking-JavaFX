import java.util.Random;

public class Passenger {

    public Passenger(String firstName, String secondName, String seatNumber) {
        this.firstName = firstName;
        this.secondName = secondName;

        setSeat(seatNumber);
        this.seat = getSeat();

        setName(firstName,secondName);
        this.name= getName();

        this.ticketNumber=setTicketNumber();
        this.secondsInQueue=0;
    }
    public Passenger(String ticketNumber,String name) {
        this.ticketNumber = ticketNumber;
        this.name= name;
    }

    private String firstName;
    private String secondName;
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

    public String setTicketNumber() {
        Random rand = new Random();
        int randNum = rand.nextInt(100) + 1;
        String ticketNumber = "TK"+String.valueOf(randNum);
        return ticketNumber;
    }
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
    public void setName(String firstName,String secondName) {
        this.firstName = firstName;
        this.secondName=secondName;
        name= firstName+" "+secondName;
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
