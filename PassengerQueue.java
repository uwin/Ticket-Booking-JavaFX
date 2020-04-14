import java.util.Arrays;

public class PassengerQueue {
    private Passenger [] queueArray = new Passenger[42];
    private int first;
    private int last;
    private int length;
    private static int maxStayInQueue=0;
    private static int minStayInQueue=0;
    private int Maxlength=0;

    public int getMaxlength() {
        return Maxlength;
    }

    public void setMaxlength(int Maxlength) {
        this.Maxlength = Maxlength;
    }

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
        System.out.println("last"+last);
        System.out.println("len"+length);
    }
    public Passenger remove(){
        Passenger data= queueArray[first];
        if(!isEmpty()){
            first=first+1%42;
            length--;
        }
        System.out.println("first"+first);
        System.out.println("len"+length);
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
    public int getMaxStayInQueue(int delay) {
        if(minStayInQueue==0){
            minStayInQueue=delay;
        }
        return maxStayInQueue;
    }
    public void setMaxStayInQueue(int maxStayInQueue) {
        this.maxStayInQueue =maxStayInQueue;
    }

    public int getMinStayInQueue() { return minStayInQueue; }
    public void setMinStayInQueue(int minStayInQueue) {
        this.minStayInQueue =minStayInQueue;
    }

    public void sortSeat(Passenger[] sortArray,int length) {
        for (int a = 1; a < length; a++) {
            for (int b = 0; b < length - a-1; b++) {
                if ((Integer.parseInt(sortArray[b].getSeat())>(Integer.parseInt(sortArray[b + 1].getSeat())))) {
                    // swap movies[b] with movies[b+1]
                    Passenger temp = sortArray[b];
                    sortArray[b] = sortArray[b + 1];
                    sortArray[b + 1] = temp;
                }
            }
        }
    }
    public void removeSeat() {
        last--;
        length--;
    }
}