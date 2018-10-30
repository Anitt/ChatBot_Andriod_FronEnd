package cs.dal.chatbot;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, LocationListener {


    private ListView mListView;
    private FloatingActionButton mButtonSend;
    private EditText mEditTextMessage;
    private ImageView micImageView;
    private ChatMessageAdapter mAdapter;
    private QueryAnalyzer queryAnalyzer = new QueryAnalyzer();

    private TextToSpeech tts;
    protected LocationManager locationManager;
    private final int REQ_CODE_SPEECH_INPUT = 1000;
    public static final String QString = "QueryString";

    public String lastMessage = "";
    public boolean isLocationSet =false;
    public Address recentAddress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null)
            return;

        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        mButtonSend = (FloatingActionButton) findViewById(R.id.btn_send);
        mEditTextMessage = (EditText) findViewById(R.id.et_message);
        micImageView = (ImageView) findViewById(R.id.iv_image);

        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);

        tts = new TextToSpeech(this, this);

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mEditTextMessage.getText().toString();
                sendMessage(message);
                mEditTextMessage.setText("");
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });

        micImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeechInput(null);
            }
        });

        CheckPermission();
    }

    public void CheckPermission()
    {
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

       boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
       boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else{

            if(isGPSEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                Location recentLoc = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(recentLoc != null) {
                    recentAddress = getAddress(recentLoc.getLatitude(), recentLoc.getLongitude());
                    sendBotMessage("Location is set to " + recentAddress.getLocality(), false);
                    isLocationSet = true;
                    return;
                }
            }

            if(isNetworkEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                Location recentLoc = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(recentLoc != null) {
                    recentAddress = getAddress(recentLoc.getLatitude(), recentLoc.getLongitude());
                    sendBotMessage("Location is set to " + recentAddress.getLocality(), false);
                    isLocationSet =true;
                    return;
                }
            }
        }

    }

    private void SpeechInput(String userInput) {

        String defaultString = getString(R.string.speech_prompt);
        if(userInput==null)
            userInput = defaultString;

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,userInput);

        try {
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException a){
            Toast.makeText(getApplicationContext(),getString(R.string.speech_not_supported),Toast.LENGTH_SHORT).show();
        }

    }

    protected void onActivityResult(int requestCode , int resultCode , Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT : {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String recMessage = result.get(0);

                    if(recMessage!=null && !recMessage.isEmpty())
                    {
                        lastMessage = recMessage;
                        sendMessage(recMessage);
                    }
                }
                break;
            }
        }
    }

    private void sendMessage(final String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                GenerateResponse(message);
            }
        };
        runnable.run();
    }

    LinkedHashMap<String,String> responseObject = null;
    LinkedHashMap<String,String> sessionObject = new LinkedHashMap<>();
    String userQuestioned = null;
    private void GenerateResponse(String userQuery)
    {
        //Predefined stop Commands
        if(queryAnalyzer.UserIntentToStop(userQuery))
        {
            responseObject =null;
            sessionObject.clear();
            sendBotMessage("Okay",true);
            return;
        }

        //Check for Stored Responses
        if(responseObject==null)
        responseObject=  queryAnalyzer.IsQueryDescribed(userQuery);

        //No Stored Response Found
        if(responseObject==null)
        {
            if(queryAnalyzer.DoQueryMatch(userQuery,"hi feed me"))
            {
                sendBotMessage("I am Listening",true); return;
            }
            sendBotMessage("Sorry I do not Understand. You said "+userQuery,true); return;
        }

        if(null != userQuestioned)
        {
            sessionObject.put(userQuestioned,userQuery.trim());
            userQuestioned = null;
        }

        // Get the Next Response Step
        String currKey = responseObject.keySet().toArray()[0].toString();

        final String currValue = responseObject.get(currKey);

        if(currKey.startsWith("$"))
        {
            userQuestioned = currKey;
            sendBotMessage(currValue,true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SpeechInput(currValue);
                }
            },2000);

        }else{
            //Respond to User
            sendBotMessage(currValue,true);
        }
        //Remove the Response Step
        responseObject.remove(currKey);

        if(responseObject.size() <1) {
            if(recentAddress!=null)
            sessionObject.put("$Location",recentAddress.getLocality());

            String qString =  queryAnalyzer.GenQueryString(sessionObject);
            Intent restIntent = new Intent(MainActivity.this,ResponseList.class);
            restIntent.putExtra( QString,qString);
            sessionObject.clear();
            responseObject = null;
            startActivity(restIntent);
        }

    }

    private void sendBotMessage(String botMessage, boolean speakOut) {
        ChatMessage chatMessage = new ChatMessage(botMessage, false, false);
        mAdapter.add(chatMessage);

        if(speakOut)
        speakOut(chatMessage.getContent());
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {

            } else {

                sendBotMessage("Welcome to Feed Me !",true);
            }
        }
    }

    private void speakOut(String message) {

        if(message==null || message.isEmpty())
            return;

        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        recentAddress = getAddress(location.getLatitude(),location.getLongitude());
        if(recentAddress!=null && !isLocationSet) {
            sendBotMessage("Location is set to " + recentAddress.getLocality(), false);
            isLocationSet =true;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Address getAddress(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1);
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
