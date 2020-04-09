import java.util.ArrayList;

public class PassengerQueue {
    Passenger [] queueArray = new Passenger[6];
    private int first;
    private int last;
    private int maxLength;
    private int maxStayInQueue;


    private void add(Passenger data){
        queueArray[last]=data;
        maxLength++;
    }
    public void addToQueue(Passenger data){
        add(data);
    }

    private Passenger remove(){
        Passenger data= queueArray[first];
        first++;
        maxLength--;
        return data;
    }
    public void removeFromQueue(){
        remove();
    }

    public boolean isEmpty(){
        return false;
    }

    public boolean isFull(){
        return false;
    }
    public void display(){
        for(int i=0;i<maxLength;i++){
            System.out.println(queueArray[i]);
        }
    }

    public int getLength() { return maxLength; }
    public int getMaxStayInQueue() { return maxStayInQueue; }
}
