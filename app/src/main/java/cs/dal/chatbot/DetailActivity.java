package cs.dal.chatbot;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        BusinessModel modelData = (BusinessModel) getIntent().getSerializableExtra("BusinessModel");

        if(modelData==null)
            return;

        ((EditText)findViewById(R.id.name)).setText(modelData.getName());

        SetStarRating(R.id.food,modelData.food_compound_score);
        SetStarRating(R.id.service, modelData.service_compound_score);
        SetStarRating(R.id.ambiance, modelData.ambience_compound_score);
        SetStarRating(R.id.discount, modelData.discount_compound_score);
    }


    public void SetStarRating(int viewId, String ratingValue)
    {
        float ratingStars = Math.round(Float.parseFloat(ratingValue));
        RatingBar stars = (RatingBar)findViewById(viewId);
        if(ratingStars>0 && ratingStars<=5)
            stars.setRating(ratingStars);
        else
            stars.setRating(0);
    }

}
