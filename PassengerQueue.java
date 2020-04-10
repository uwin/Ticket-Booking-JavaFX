import java.util.ArrayList;

public class PassengerQueue {
    static Passenger [] queueArray = new Passenger[];
    private int first;
    private int last;
    private int length;
    private int maxStayInQueue;

    public PassengerQueue() {
        this.first = 0;
        this.last = 0;
        this.length = 0;
        this.maxStayInQueue = 0;
    }

    public void add(Passenger data){ }
    public void remove(){ }
    public boolean isEmpty(){
        return false;
    }
    public boolean isFull(){
        return false;
    }
    public void display(){ }

    public int getLength() { return length; }
    public int getMaxStayInQueue() { return maxStayInQueue; }
}
