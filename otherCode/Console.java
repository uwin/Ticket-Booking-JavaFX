/*
import java.util.*;

class MultidimensionalArrayList {

    */
/*function for creating and returning 2D ArrayList*//*

    static List create2DArrayList()
    {

        public void addProd(View ap){
            // test arraylist of hashmaps
            HashMap<String, String> prodHashMap = new HashMap<String, String>();
            prodHashMap.put("prod", tvProd.getText().toString());
            prodArrayList.add(prodHashMap);
            tvProd.setText("");
            // check data ///
            Log.e("myLog","Data prodArrayList in ADD Method Size = "+prodArrayList.size());
            for(int i=0; i< prodArrayList.size();i++)
            { Log.e("myLog","Data prodArrayList in ADD Method = "+prodArrayList.get(i).toString()); }
        }

        ArrayList<HashMap<String, String>> prodArrayList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> prodHashMap = new HashMap<String, String>();
        public void addProd(View ap){
            // test arraylist of hashmaps
            prodHashMap.put("prod", tvProd.getText().toString());
            prodArrayList.add(prodHashMap);
            tvProd.setText("");
            // check data ///
            Log.e("myLog","Data prodArrayList in ADD Method Size = "+prodArrayList.size());
            for(int i=0; i< prodArrayList.size();i++)
            { Log.e("myLog","Data prodArrayList in ADD Method = "+prodArrayList.get(i).toString()); }
        }
    }

    public static void main(String args[])
    {
*/
/*        System.out.println("2D ArrayList :");
        System.out.println(create2DArrayList());*//*


        ArrayList<ArrayList<Integer> > x = new ArrayList<ArrayList<Integer> >();
        x.add(new ArrayList<Integer>());
        x.get(0).add(0, 3);
        x.add(new ArrayList<Integer>());
        x.get(1).add(0, 4);
*/
/*        x.add(new ArrayList<Integer>(Arrays.asList(3, 4, 6)));
        x.get(1).add(0, 366);
        x.get(1).add(4, 576);
        x.add(2, new ArrayList<>(Arrays.asList(3, 84)));
        x.add(new ArrayList<Integer>(Arrays.asList(83, 6684, 776)));
        x.add(new ArrayList<>(Arrays.asList(8)));*//*

        System.out.println(x);
    }
}*/
