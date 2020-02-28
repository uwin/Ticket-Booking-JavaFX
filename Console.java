import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
public class Console{
    public static void main(String[] args) {
        welcome();
    }
    static final int SEATING_CAPACITY = 42;
    public static List<String> list(){
        List<String> seatList = new ArrayList<>();
        int i=0;
        while(i< SEATING_CAPACITY){
            seatList.add("0");
            i+= 1;
        }
        return seatList;
    }
    public static void welcome() {
        System.out.println("\nwelcome to ticket booking system \nA/C compartment for Denuwara Menike");
        startbooking(list());
    }
    public static void startbooking(List  seatList){
        options(seatList);
    }
    public static void options(List seatList) {
        System.out.println("\n\n"+
                "A Add a seat\n"+
                "V View all seats\n"+
                "E View empty seats\n"+
                "D Delete seat\n"+
                "F Find the seat\n"+
                "S Save details\n"+
                "L Load details\n"+
                "O List seats\n");
        optionstest(seatList);
    }
    public static void optionstest(List seatList){
        Scanner scanoption= new Scanner(System.in);
        System.out.println(">> select a option");
        String userOption= scanoption.next().toUpperCase();

        switch (userOption) {
            case "A": {          //add
                String userName = getname();
                getseat(userName, seatList);
                break;
            }
            case "V":     //veiw
                getVeiw(seatList);
                break;
            case "E":     //empty
                getempty(seatList);
                break;
            case "D": {    //delete
                String userName = getname();
                getdelete(seatList, userName);
                break;
            }
            case "F": {    //find
                String userName = getname();
                getfind(seatList, userName);
                break;
            }
            case "S":     //save
                getsave();
                break;
            case "L":      //load
                getload();
                break;
            case "O":      //veiw
                getlist(seatList);
                break;
            case "Q":
                System.exit(0);
            default:
                System.out.println("invaid input");
                options(seatList);
                break;
        }
    }
    private static String getname(){
        Scanner scanUserName = new Scanner(System.in);
        System.out.println("Enter your name");
        return scanUserName.next();
    }
    //A  gui needed
    public static void getseat(String userName, List seatList){
        try {
            Scanner scanseat = new Scanner(System.in);
            System.out.println("Enter seat number");
            int userSeat= scanseat.nextInt();
            userSeat-=1;
            seatList.set(userSeat,userName);
        }catch (java.util.InputMismatchException seat){
            System.out.println("wrong input");
            getseat(userName,seatList);
        }
        options(seatList);
    }
    //V  gui needed
    public static void getVeiw(List seatList){
        int c=1;
        for (Object i : seatList) {
            System.out.print(c++ +": ");
            System.out.println(i);
        }
        options(seatList);
    }
    //E  gui needed
    public static void getempty(List seatList){
        int c=0;
        for (Object i : seatList) {
            c++;
            if (i.equals("0")){
                System.out.print(c+":");
                System.out.println(i);
            }
        }
        options(seatList);
    }
    //D console
    public static void getdelete(List seatList, String userName){
        int scandelete = seatList.indexOf(userName);
        seatList.set(scandelete,0);

        options(seatList);
    }
    //F console
    public static void getfind(List seatList, String userName){
        int scanfind = seatList.indexOf(userName);
        System.out.println("seat no for" +userName+ "is" +scanfind);
        options(seatList);
    }
    //S database
    public static void getsave(){
        System.out.println("e");
    }
    //L database
    public static void getload(){
        System.out.println("e");
    }
    //O console?
    public static void getlist(List seatList){
        List<String> sortlist= new ArrayList<String>(seatList);
        Collections.sort(sortlist);
        for(Object str: sortlist) {
            String i = (String) str;
            if (i.equals("0")){
                System.out.print("");
            }else {
                int x = seatList.indexOf(str);
                System.out.println(" "+(x+1)+": "+str);
            }
        }
        options(seatList);
    }
}