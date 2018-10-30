package cs.dal.chatbot;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

public class DetailList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        BusinessModel modelData = (BusinessModel) getIntent().getSerializableExtra("BusinessModel");

        if(modelData==null)
            return;

        ((EditText)findViewById(R.id.name)).setText(modelData.getName());

        SetStarRating(R.id.foodRatingBar,modelData.food_compound_score);
        SetStarRating(R.id.serviceRatingBar, modelData.service_compound_score);
        SetStarRating(R.id.ambianceRatingbar, modelData.ambience_compound_score);
        SetStarRating(R.id.discountratingBar, modelData.discount_compound_score);
    }


    public void SetStarRating(int viewId, String ratingValue)
    {
        float ratingStars = Math.round(Float.parseFloat(ratingValue));

        RatingBar stars = (RatingBar)findViewById(viewId);
        stars.setEnabled(false);
        int scaledStarValue = (int)map(ratingStars);
        if(scaledStarValue>0 && scaledStarValue<=5)
            stars.setRating(scaledStarValue);
        else
            stars.setRating(0);
    }


    public static double map(float valueCoord1) {

        double scale = (5 - 1) / (2 - (-2));
        return scale * (valueCoord1-(-2))+1 ;
    }
}
