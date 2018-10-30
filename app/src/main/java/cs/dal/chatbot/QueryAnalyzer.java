package cs.dal.chatbot;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jebes on 4/4/2018.
 */

public class QueryAnalyzer {

    QuestionTree qTree =  new QuestionTree();

    public QueryAnalyzer()
    {

    }

    public LinkedHashMap<String,String> IsQueryDescribed(String userQuery)
    {
        userQuery = userQuery.toLowerCase();

        for(String key: QuestionTree.rootTable.keySet())
        {
            String currentKey = key.toLowerCase();
            if(DoQueryMatch(userQuery,currentKey))
            {
                // User Question matched the described data
                return new LinkedHashMap<>(QuestionTree.rootTable.get(key));
            }
        }
        // If there is no Match
        return null;
    }

    public boolean UserIntentToStop(String userquery)
    {
        for(String stops: QuestionTree.CancelKeyWords.split("_"))
        {
            if(stops.equalsIgnoreCase(userquery))
                return true;
        }
        return false;
    }


    //Do a Fuzzy Distance token Match
    public boolean DoQueryMatch(String userQuery, String storedQuery)
    {
         String[] splitedStoredQuery =  storedQuery.split("_");

        int splittedQueryCount = splitedStoredQuery.length;

        int matchedCount =0;
        for (String token: splitedStoredQuery)
        {
            if(userQuery.contains(token))
                matchedCount++;
        }

        int percentageMatch = (matchedCount*100)/splittedQueryCount;

        if(percentageMatch>75)
        {
            return  true;
        }
        return false;
    }


    public String GenQueryString(LinkedHashMap<String,String> hMap)
    {
        String outputString = "";


        for(Map.Entry<String,String> entry : hMap.entrySet())
        {
            outputString += "&"+entry.getKey().replace("$","") +"="+entry.getValue();
        }

        return outputString;
    }
}
