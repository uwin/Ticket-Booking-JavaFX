import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;
import java.util.Arrays;

public class PassengerQueue {
    private static Passenger [] queueArray = new Passenger[42];
    private int first=0;
    private int last=0;
    private int length=0;
    private int maxStayInQueue=0;

    public  void clearQueue(){
        queueArray = new Passenger[42];
    }

    public  Passenger[] getQueueArray() {
        return queueArray;
    }

    public  void setQueueArray(Passenger[] queueArray) {
        PassengerQueue.queueArray = queueArray;
    }

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
        return length==0;
    }
    public boolean isFull(){
        return length==42;
    }
    public void display(){
        for (Passenger x: queueArray){
            System.out.println(x);
        }
    }

    public  <Passenger extends Comparable<Passenger>> Passenger[] bubbleSort(Passenger[] arr){
        for (int i=0; i<arr.length-1;i++){
            for(int j=0;j<arr.length-i-1;j++){
                if(arr[j].compareTo(arr[j+1])>0){
                    Passenger temp=arr[j];
                    arr[j]=arr[j+1];
                    arr[j+1]=temp;
                }
            }
        }
        return arr;
    }

    public int getLength() { return length; }
    public int getMaxStayInQueue() { return maxStayInQueue; }
}
