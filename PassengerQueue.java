import java.util.Arrays;

public class PassengerQueue {
    private Passenger [] queueArray = new Passenger[42];
    private int first;
    private int last;
    private int length;
    private static int maxStayInQueue=0;
    private static int minStayInQueue=0;
    private int Maxlength=0;


    public  Passenger[] getQueueArray() {

        return queueArray;
    }
    public  void setQueueArray(Passenger[] queueArray) {

        this.queueArray = queueArray;
    }
    public  void clearQueue(){

        Arrays.fill(queueArray, null);
        last=0;
        first=0;
        length=0;
    }

    public void add(Passenger data){

        queueArray[last]=data;
        last=last+1%42;
        length++;
    }
    public Passenger remove(){

        Passenger data= queueArray[first];
        first=first+1%42;
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
    public int getLength() {

        return length;
    }

    public int getMaxlength() {

        return Maxlength;
    }
    public void setMaxlength(int Maxlength) {

        this.Maxlength = Maxlength;
    }

    public int getMaxStayInQueue() {

        return maxStayInQueue;
    }
    public void setMaxStayInQueue(int maxStayInQueuedata) {

        maxStayInQueue =maxStayInQueuedata;
    }

    public int getMinStayInQueue() {

        return minStayInQueue;
    }
    public void setMinStayInQueue(int minStayInQueuedata) {

        minStayInQueue =minStayInQueuedata;
    }

    public void setrest() {

        length=0;
        first=0;
        last=0;
    }
    public void removeSeat() {

        last--;
        length--;
    }

}