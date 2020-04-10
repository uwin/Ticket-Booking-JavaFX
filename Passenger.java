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
    private int secondsInQueue;

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
