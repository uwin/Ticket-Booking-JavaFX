import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;

public class PassengerQueue {
    static Passenger [] queueArray = new Passenger[42];
    private int first=0;
    private int last=0;
    private int length=0;
    private int maxStayInQueue=0;


    public void add(Passenger data){
        queueArray[last]=data;
        last++;
        length++;
    }
    public Passenger  remove(){
        Passenger data= queueArray[first];
        first++;
        length--;
        return data;


    }
    public boolean isEmpty(){
        return false;
    }
    public boolean isFull(){
        return false;
    }
    public void display(){
        for (Passenger x: queueArray){
            System.out.println(x);
        }
    }

    public int getLength() { return length; }
    public int getMaxStayInQueue() { return maxStayInQueue; }
}
