//public class CW1 extends Application {
//    static final int SEATING_CAPACITY = 42;
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        HashMap<String, String> customerSeats = new HashMap<String, String>();
//        String textFile = ("C:\\Train Booking System.txt");
//        menu(customerSeats, new File(textFile));
//    }
//    public void menu(HashMap customerSeats,File textFile) {
//        Scanner input = new Scanner(System.in);
//
//        System.out.println("Enter 'V' to view all seats ");
//        System.out.println("Enter 'A' to add a seat ");
//        System.out.println("Enter 'E' to display the empty seats ");
//        System.out.println("Enter 'D' to delete a seat ");
//        System.out.println("Enter 'F' to find your seat ");
//        System.out.println("Enter 'S' to save your information ");
//        System.out.println("Enter 'L' to load your data ");
//        System.out.println("Enter 'O' to view all the ordered seats ");
//        System.out.println("Enter 'Q' to quit ");
//        System.out.print("Enter a letter : ");
//
//        String options = input.nextLine();
//
//        switch (options) {
//            case "v":
//                view(customerSeats,textFile);
//                break;
//            case "a":
//                add(customerSeats,textFile);
//                break;
//
//            case "e":
//                empty(customerSeats,textFile);
//                break;
//
//            case "d":
//                delete(customerSeats,textFile);
//                break;
//
//            case "f":
//                find(customerSeats,textFile);
//
//
//            case "s":
//                save(customerSeats,textFile);
//
//            case "l":
//                load(customerSeats,textFile);
//
//            case "o":
//                OrderedSeats(customerSeats);
//
//            case "q":
//                quit(customerSeats);
//                break;
//        }
//    }
//
///**Set up "v" to view all seat
//     *
//     * @param customerSeats
//     * @param textFile
//     *//*
//
//    public void view(HashMap customerSeats,File textFile) {
//        Stage view = new Stage();
//        view.setTitle("Train Booking");
//        FlowPane root = new FlowPane(Orientation.HORIZONTAL, 20, 10);
//        root.setPadding(new Insets(10));
//        Scene scene = new Scene(root, 850, 300);
//*/
///** Set a seats numbers from 1 to 42
// *
// *//*
//
//        for (int num = 1; num <= SEATING_CAPACITY; num++) {
//            Button seats = new Button("Seat" + num);
//            root.getChildren().add(seats);
//
//*/
///**After booking custormer seat this one show on the button "Reserved"
// *
// *//*
//
//            if (customerSeats.containsValue(seats.getText())) {
//                seats.setText("Reserved");
//                seats.setStyle("-fx-background-color: red;-fx-text-fill: white");
//            } else {
//                seats.setStyle("-fx-background-color: yellow;-fx-text-fill: black");
//
//            }
//            view.setOnCloseRequest(event -> {
//                view.close();
//                menu(customerSeats,textFile);
//            });
//        }
//        view.setScene(scene);
//        view.show();
//    }
//
//    */
///**Set the "a" to add custormer seats when press it
//     *
//     * @param customerSeats
//     * @param textFile
//     *//*
//
//    public void add(HashMap customerSeats,File textFile) {
//        Stage add = new Stage();
//        add.setTitle("Train Booking");
//        FlowPane root = new FlowPane(Orientation.VERTICAL, 20, 10);
//        root.setPadding(new Insets(10));
//        Scene scene = new Scene(root, 850, 300);
//
//*/
///**Set the Custormer name input
// *
// *//*
//
//        Label label = new Label("Enter the Name:");
//        TextField name = new TextField();
//        name.setStyle("-fx-background-color: powderblue");
//        for (int num = 1; num <= SEATING_CAPACITY; num++) {
//            Button seats = new Button("Seat" + num);
//            root.getChildren().add(seats);
//
//            if (customerSeats.containsValue(seats.getText())) {
//                seats.setText("Reserved");
//                seats.setStyle("-fx-background-color: red;-fx-text-fill: white");
//            } else {
//                seats.setStyle("-fx-background-color: yellow;-fx-text-fill: black");
//
//            }
//            seats.setOnAction(event -> {
//                seats.setStyle("-fx-background-color: red;-fx-text-fill: white");
//                System.out.println(seats.getText());
//                customerSeats.put(name.getText(),seats.getText());
//                seats.setText("Reserved");
//            });
//        }
//        add.setOnCloseRequest(event -> {
//            System.out.println(customerSeats);
//            add.close();
//            menu(customerSeats,textFile);
//        });
//        root.getChildren().addAll(label,name);
//        root.setOrientation(Orientation.VERTICAL);
//        add.setScene(scene);
//        add.show();
//    }
//
//    */
///** Set "e" for empty seats
//     *
//     * @param customerSeats
//     * @param textFile
//     *//*
//
//    public void empty(HashMap customerSeats,File textFile) {
//        Stage empty = new Stage();
//        empty.setTitle("Train Booking");
//        FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10);
//        root.setPadding(new Insets(10));
//        Scene scene = new Scene(root, 850, 300);
//
//        for (int num = 1; num <= SEATING_CAPACITY; num++) {
//            Button seats = new Button("Seat" + num);
//            root.getChildren().add(seats);
//
//            if (customerSeats.containsValue(seats.getText())) {
//                seats.setStyle("-fx-background-color: white;-fx-text-fill: white");
//            } else {
//                seats.setStyle("-fx-background-color: yellow;-fx-text-fill: black");
//
//            }
//            empty.setOnCloseRequest(event -> {
//                empty.close();
//                menu(customerSeats,textFile);
//            });
//            root.setOrientation(Orientation.VERTICAL);
//            empty.setScene(scene);
//            empty.show();
//        }
//    }
//
//    */
///** Set "d" to Delete customer from seat
//     *
//     * @param customerSeats
//     * @param textFile
//     *//*
//
//    public void delete(HashMap customerSeats,File textFile){
//        Scanner seat = new Scanner(System.in);
//        System.out.println("Enter your name:");
//        String deleteS = seat.nextLine();
//        customerSeats.remove(deleteS);
//        menu(customerSeats,textFile);
//    }
//
//    */
///** Set "f" for Find the seat for a given customers name
//     *
//     * @param customerSeats
//     * @param textFile
//     *//*
//
//    public void find(HashMap customerSeats,File textFile){
//        Scanner seat = new Scanner(System.in);
//        System.out.println("Enter your name:");
//        String deleteS = seat.nextLine();
//        System.out.println(customerSeats.get(deleteS));
//    }
//
//    */
///** Set "s" for Store program data in to file
//     *
//     * @param customerSeats
//     * @param textFile
//     *//*
//
//    public void save(HashMap customerSeats,File textFile){
//
//        try{
//            BufferedReader br = new BufferedReader(new FileReader(textFile));
//            String st;
//            while ((st = br.readLine()) != null)
//                System.out.println(st);
//
//        } catch(IOException ex){
//            System.out.println(ex.getMessage());
//        }
//        menu(customerSeats,textFile);
//    }
//
//    */
///** Set "l" to Load program data from file
//     *
//     * @param customerSeats
//     * @param textFile
//     *//*
//
//    public void load(HashMap customerSeats,File textFile){
//        try {
//            File myObj = new File("Train Booking System.txt");
//            Scanner myReader = new Scanner(myObj);
//            while (myReader.hasNextLine()) {
//                String data = myReader.nextLine();
//                System.out.println(data);
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//        menu(customerSeats,textFile);
//    }
//
//    */
///** Set "o" to View seats Ordered alphabetically by name
//     *
//     *//*
//
//    public void OrderedSeats (HashMap customerSeats){
//        Set<String> strset = customerSeats.keySet();
//        String str[] = strset.toArray(new String[0]);
//        String temp;
//        for (int i = 0; i < str.length ; i++) {
//            for (int j = i + 1; j < str.length ; j++) {
//                if (str[j].compareTo(str[i]) < 0){
//                    temp = str[i];
//                    str[i] = str[j];
//                    str[j] = temp;
//                }
//            }
//            System.out.println(str[i]+ ":" +customerSeats.get(str[i]));
//
//        }
//        menu(customerSeats,textFile);
//
//    }
//
//    */
///** Set "q" to quit programme
//     *
//     * @param customerSeats
//     *//*
//
//    public void quit(HashMap customerSeats){
//}
//
//    */
///** Launch the Programme
//     *
//     *
//     *//*
//
//    public static void main (String[]args){
//        launch(args);
//    }
//}*/
