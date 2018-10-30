package cs.dal.chatbot;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jebes on 4/4/2018.
 */

public class CustomListAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<BusinessModel> masterList;
    public ArrayList<BusinessModel> restaurantlist;
    LayoutInflater inflater;

    private Random random = new Random();

    public CustomListAdapter(Context context,ArrayList<BusinessModel> restaurantlist){

        this.context = context;
        this.restaurantlist = restaurantlist;
        this.masterList=restaurantlist;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return restaurantlist.size();
    }

    @Override
    public Object getItem(int i) {
        return restaurantlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = inflater.inflate(R.layout.item,null);
        }
        TextView restaurantname = (TextView)view.findViewById(R.id.restaurantname);
        TextView country = (TextView)view.findViewById(R.id.country);
        RatingBar stars = (RatingBar) view.findViewById(R.id.stars);
        ImageView imgView = (ImageView)view.findViewById(R.id.imgView);

        switch (random.nextInt(6)+1)
        {
            case 1: imgView.setImageResource(R.drawable.restaurant1);
                break;
            case 2:  imgView.setImageResource(R.drawable.restaurant2);
                break;
            case 3:  imgView.setImageResource(R.drawable.restaurant3);
                break;
            case 4:  imgView.setImageResource(R.drawable.restaurant4);
                break;
            case 5:  imgView.setImageResource(R.drawable.restaurant5);
                break;
            case 6:  imgView.setImageResource(R.drawable.restaurant6);
                break;
            default: imgView.setImageResource(R.drawable.restaurant2);
        }


        view.setTag(restaurantlist.get(i));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusinessModel businessModel = (BusinessModel) v.getTag();
                Intent callIntent = new Intent(context,DetailList.class);
                callIntent.putExtra("BusinessModel",businessModel);
                context.startActivity(callIntent);
            }
        });
        //setting values
        String Name = restaurantlist.get(i).getName().replace('"',' ').trim();
        if(Name.length()>15)
        {
            Name = Name.subSequence(0,15).toString();
        }
        restaurantname.setText(Name);
        country.setText(restaurantlist.get(i).getCountry());

        float ratingStars = Math.round(Float.parseFloat(restaurantlist.get(i).getStars()));
        if(ratingStars>0 && ratingStars<=5)
        stars.setRating(ratingStars);
        else
            stars.setRating(0);

        return view;
    }

}