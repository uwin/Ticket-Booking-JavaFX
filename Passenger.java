import java.util.Random;

public class Passenger {

    public Passenger(String firstName, String secondName, String seatNumber) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.seatNumber = seatNumber;
        this.ticketNumber=setTicketNumber();
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

    private String firstName;
    private String secondName;
    private String seatNumber;
    private String ticketNumber;
    private int secondsInQueue;

    public void setName(String firstName,String secondName) {
        this.firstName = firstName;
        this.secondName=secondName;
    }
    public void setSeat(String seatNumber){
        this.seatNumber = seatNumber;
    }
    public String getSeat(){
        return seatNumber;
    }
    public String getName() {
        return firstName+" "+secondName;
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
