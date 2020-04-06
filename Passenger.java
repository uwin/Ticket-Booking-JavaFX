public class Passenger {
    private String firstName;
    private String secondName;
    private int secondsInQueue;

    public String getName() {
//        get from passenger[]
        return firstName+" "+secondName;
    }
    public void setName(String firstName,String secondName) {
        this.firstName = firstName;
        this.secondName=secondName;
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
