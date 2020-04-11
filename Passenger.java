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

    private String firstName;
    private String secondName;
    private String name;
    private String seat;
    private String ticketNumber;
    private String date;
    private String Station;
    private String train;

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

    private int secondsInQueue;

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
        return secondsInQueue;
    }
    public void setSeconds(int sec ) {
//        secondsInQueue change
    }

    public void display() {

    }
}
