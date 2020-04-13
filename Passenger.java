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
    private String Station;
    private String train;
    private float secondsInQueue;

    public String getTrain() {
        return train;
    }
    public void setTrain(String train) {
        this.train = train;
    }

    public String getStation() {
        return Station;
    }
    public void setStation(String station) {
        Station = station;
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

    public int getSeconds() {
        return 3 + (int) (Math.random() * ((18 - 3 + 1)));
    }
    public void setSeconds(float sec ) {
        secondsInQueue=sec;
    }

    public void display() {

    }
}
