public class Passenger {

    private String firstName;
    private String secondName;
    private String seatNumber;
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
