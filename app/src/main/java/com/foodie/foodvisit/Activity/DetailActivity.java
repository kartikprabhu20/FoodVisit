package com.foodie.foodvisit.Activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodie.foodvisit.BuildConfig;
import com.foodie.foodvisit.FoodVisitApp;
import com.foodie.foodvisit.R;
import com.foodie.foodvisit.Utils;
import com.foodie.foodvisit.model.Location;
import com.foodie.foodvisit.model.Restaurant;
import com.foodie.foodvisit.model.RestaurantInfo;
import com.foodie.foodvisit.model.UserRating;
import com.foodie.foodvisit.widget.FoodVisitWidget;
import com.foodie.foodvisit.widget.FoodVisitWidgetManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kprabhu on 11/12/17.
 */

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_details_activity)
    Toolbar toolbar;

    @BindView(R.id.backdrop)
    ImageView backDropImage;

    @BindView(R.id.cost_for_two)
    TextView tvCostForTwo;

    @BindView(R.id.price_range)
    TextView tvPriceRange;

    @BindView(R.id.online_available)
    TextView tvOnlineAvailable;

    @BindView(R.id.has_table_booking)
    TextView tvHastableBooking;

    @BindView(R.id.address)
    TextView tvAddress;

    @BindView(R.id.locality)
    TextView tvLocality;

    @BindView(R.id.city)
    TextView tvCity;

    @BindView(R.id.zipcode)
    TextView tvZipcode;

    @BindView(R.id.rating)
    TextView tvRating;

    @BindView(R.id.describte)
    TextView tvDescribte;

    @BindView(R.id.votes)
    TextView tvVotes;

    @BindView(R.id.fab_fav)
    FloatingActionButton fab;

    @BindView(R.id.adView)
    AdView adview;

    private Restaurant restaurant;
    private RestaurantInfo restaurantInfo;
    private Location location;
    private UserRating userRatings;

    private String name,backDropImagePath;
    private String costForTwo,priceRange,onlineAvailable,hastableBooking;
    private String userRating,userRatingText,userRatingVotes;
    private String address,locality,city,zipcode,longitude,lattitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        restaurant = getRestaurant();
        restaurantInfo = restaurant.getRestaurantInfo();

        fab.setOnClickListener(this);

        initialiseFab();
        parseData();
        initialiseViews();
        initialiseAdView();
    }

    private void initialiseFab() {
        if (Utils.isToVist(this,restaurantInfo)){
            fab.setImageResource(R.drawable.favorite_added);
        }else{
            fab.setImageResource(R.drawable.favorite_removed);
        }
    }

    private void initialiseAdView() {

        if("free".equalsIgnoreCase(BuildConfig.FLAVOR)){
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            adview.loadAd(adRequest);
        }else {
            adview.setVisibility(View.GONE);
        }
    }

    private void parseData() {
        name = restaurantInfo.getName();
        backDropImagePath = (TextUtils.isEmpty(restaurantInfo.getFeaturedImage())) ?
                getResources().getResourceEntryName(R.drawable.food) : restaurantInfo.getFeaturedImage();

        costForTwo = String.valueOf(restaurantInfo.getAverageCostForTwo());
        priceRange = String.valueOf(restaurantInfo.getPriceRange());
        onlineAvailable = "1".equalsIgnoreCase(restaurantInfo.getHasOnlineDelivery())? getResources().getString(R.string.available_yes): getResources().getString(R.string.available_no);
        hastableBooking = "1".equalsIgnoreCase(restaurantInfo.getHasTableBooking())? getResources().getString(R.string.available_yes): getResources().getString(R.string.available_no);

        userRatings = restaurantInfo.getUserRating();
        userRating = userRatings.getAggregateRating();
        userRatingText = userRatings.getRatingText();
        userRatingVotes = userRatings.getVotes();

        location = restaurantInfo.getLocation();

        address = location.getAddress();
        locality = location.getLocality();
        city = location.getCity();
        zipcode = location.getZipcode();
        longitude = location.getLongitude();
        lattitude = location.getLatitude();

    }

    private void initialiseViews() {

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(name);

        Picasso.with(getApplicationContext())
                .load(backDropImagePath)
                .placeholder(R.drawable.food)
                .error(R.mipmap.ic_launcher)
                .fit()
                .into(backDropImage);

        tvCostForTwo.setText(costForTwo);
        tvHastableBooking.setText(hastableBooking);
        tvOnlineAvailable.setText(onlineAvailable);
        tvPriceRange.setText(priceRange);

        tvAddress.setText(address);
        tvLocality.setText(locality);
        tvCity.setText(city);
        tvZipcode.setText(zipcode);

        tvRating.setText(userRating);
        tvDescribte.setText(userRatingText);
        tvVotes.setText(userRatingVotes);
    }


    @Override
    public void onClick(View view) {
        setToVist();
    }

    private void setToVist() {
        if (Utils.isToVist(this, restaurantInfo)) {
            Utils.removeFromToVist(this, restaurantInfo.getId());
            fab.setImageResource(R.drawable.favorite_removed);
        } else {
            Utils.addToVist(this, restaurantInfo);
            fab.setImageResource(R.drawable.favorite_added);
        }

        new FoodVisitWidgetManager().updateRestaurants(Utils.getRestrauntFromDB(FoodVisitApp.getAppContext()));

    }

    public Restaurant getRestaurant() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            throw new IllegalStateException();
        }

        if (bundle.containsKey(FoodVisitWidget.RESTAURANT_LIST)) {
            String list = bundle.getString(FoodVisitWidget.RESTAURANT_LIST);
            int pos = bundle.getInt(FoodVisitWidget.POSITION);
            return new FoodVisitWidgetManager().getInfo(list, pos);
        }

        if (!bundle.containsKey("restaurant")) {
            throw new IllegalStateException();
        }
        return bundle.getParcelable("restaurant");
    }
}
