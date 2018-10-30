package cs.dal.chatbot;

import java.io.Serializable;

/**
 * Created by jebes on 4/4/2018.
 */

public class BusinessModel implements Serializable {


    public String food_polarity_score;

    public String discount_polarity_score;

    public String overall_compound_score;

    public String service_compound_score;

    public String food_compound_score;

    public String overall_polarity_score;

    public String compound_star_value;

    public String polartiy_star_value;

    public String ambience_compound_score;

    public String averaged_star_value;

    public String service_polarity_score;

    public String ambience_polarity_score;

    public String discount_compound_score;

    private String review_count;

    private String neighborhood;

    private String is_open;

    private String state;

    private String city;

    private String country;

    private String address;

    private String stars;

    private String name;

    private String postal_code;

    private String business_id;

    private String categories;

    private String longitude;

    private String latitude;

    public String getReview_count ()
    {
        return review_count;
    }

    public void setReview_count (String review_count)
    {
        this.review_count = review_count;
    }

    public String getNeighborhood ()
    {
        return neighborhood;
    }

    public void setNeighborhood (String neighborhood)
    {
        this.neighborhood = neighborhood;
    }

    public String getIs_open ()
    {
        return is_open;
    }

    public void setIs_open (String is_open)
    {
        this.is_open = is_open;
    }

    public String getState ()
    {
        return state;
    }

    public void setState (String state)
    {
        this.state = state;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getStars ()
    {
        return stars;
    }

    public void setStars (String stars)
    {
        this.stars = stars;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getPostal_code ()
    {
        return postal_code;
    }

    public void setPostal_code (String postal_code)
    {
        this.postal_code = postal_code;
    }

    public String getBusiness_id ()
    {
        return business_id;
    }

    public void setBusiness_id (String business_id)
    {
        this.business_id = business_id;
    }

    public String getCategories ()
    {
        return categories;
    }

    public void setCategories (String categories)
    {
        this.categories = categories;
    }

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (String latitude)
    {
        this.latitude = latitude;
    }

    @Override
    public String toString()
    {
        return "ListModel [review_count = "+review_count+", neighborhood = "+neighborhood+", is_open = "+is_open+", state = "+state+", city = "+city+", country = "+country+", address = "+address+", stars = "+stars+", name = "+name+", postal_code = "+postal_code+", business_id = "+business_id+", categories = "+categories+", longitude = "+longitude+", latitude = "+latitude+"]";
    }
}