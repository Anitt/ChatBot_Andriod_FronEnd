package cs.dal.chatbot;

import android.content.res.Resources;

import java.util.Hashtable;
import java.util.LinkedHashMap;

/**
 * Created by jebes on 4/4/2018.
 */

public class QuestionTree {

    public static Hashtable<String,LinkedHashMap<String,String>> rootTable = new Hashtable<>();

    public static String CancelKeyWords= "cancel_go back_stop_undo_close_clear";

    public QuestionTree() {

        LinkedHashMap<String,String> q1 = new LinkedHashMap<>();
        rootTable.put("Find_Restaurants_near_me",q1);
        q1.put("$q","Would you like to update your Location");
        q1.put("$Location","Please tell me a location ?");
        q1.put("$Cuisine","Sure, Help us with your Favourite Cuisine ?");
        q1.put("there_you_go","Okay Here is what I found!");
    }

}
