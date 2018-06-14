package comp231.drbooking;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: Set any variables that can be used by multiple classes
 * e.g. hostname for API in Azure etc. For testing wecan replace that with localhost:<postNumber> etc
 */
public class VariablesGlobal
{
    //public static boolean isLoggedIn = false;//this will destroy once app closed. So prefs are better.
    public static String API_URI = "http://drappapi.azurewebsites.net";
    //public static String API_URI = "http://192.168.1.7:45455";

    //CANNOT .add() to static List. Workaround is : https://alvinalexander.com/source-code/java/how-create-populate-static-list-arraylist-linkedlist-syntax-in-java
    public static   List<String> DrNamesList = new ArrayList<String>(){{add("Please Wait"); add("Fetching List of Doctors");}};
    public static List<Model_DrProfile> DrProfiles =new ArrayList<Model_DrProfile>();

public static ArrayAdapter spinAdapter;


}
