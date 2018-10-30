package cs.dal.chatbot;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TestReview extends Activity {

    EditText reviewText;
    Button getSentiment;
    TextView rating;
    RatingBar starRating;
    TextView polarityScore;
    TextView compoundScore;
    TextView polarityValue;
    TextView compoundValue;
    JSONObject jsonObject;
    int starValue;
    double polarity;
    double compound;

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    public void getSentimentValues(final String review){
        AsyncTask<Void,Void,Void> newTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String inputJSON = "";
                HttpURLConnection httpURLConnection = null ;
                BufferedReader bufferedReader = null ;
                try {
                    String urlstring = "http://feedmefood.mybluemix.net/sentiment?textReview="+ URLEncoder.encode(review,"UTF-8");
                    URL url = new URL(urlstring);
                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.connect();
                    InputStream stream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    String line = "";
                    StringBuffer buffer = new StringBuffer();
                    while ((line = bufferedReader.readLine()) != null){
                        buffer.append(line);
                    }
                    jsonObject = new JSONObject(buffer.toString());
                    Log.d("JSON Value",inputJSON);
                    JSONObject starJSON = jsonObject.getJSONObject("stars");
                    Log.d("Star Values",""+starJSON);
                    starValue = (int) Math.ceil(starJSON.getDouble("averaged_star_value"));
                    polarity = jsonObject.getDouble("nltk_polarity_score");
                    compound = jsonObject.getDouble("text_blob_polarity");
                }
                catch (IOException e){
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        starRating.setVisibility(View.VISIBLE);
                        rating.setVisibility(View.VISIBLE);
                        polarityScore.setVisibility(View.VISIBLE);
                        compoundScore.setVisibility(View.VISIBLE);
                        polarityValue.setVisibility(View.VISIBLE);
                        compoundValue.setVisibility(View.VISIBLE);
                        starRating.setRating(starValue);
                        polarityValue.setText(String.valueOf(polarity));
                        compoundValue.setText(String.valueOf(compound));
                    }
                });
                return null;
            }
        };
        runAsyncTask(newTask);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_review);
        reviewText = (EditText) findViewById(R.id.review);
        getSentiment = (Button)findViewById(R.id.getsentiment);
        starRating = (RatingBar) findViewById(R.id.rating);
        rating = (TextView) findViewById(R.id.ratingText);
        polarityScore = (TextView) findViewById(R.id.polarity_text);
        polarityValue = (TextView) findViewById(R.id.polarity_value);
        compoundScore = (TextView) findViewById(R.id.compound_text);
        compoundValue = (TextView) findViewById(R.id.compound_value);
        starRating.setVisibility(View.INVISIBLE);
        rating.setVisibility(View.INVISIBLE);
        polarityScore.setVisibility(View.INVISIBLE);
        compoundScore.setVisibility(View.INVISIBLE);
        polarityValue.setVisibility(View.INVISIBLE);
        compoundValue.setVisibility(View.INVISIBLE);

        getSentiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSentimentValues(reviewText.getText().toString());
            }
        });
    }

}
