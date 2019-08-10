package com.optiquall.childappusage.rent_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optiquall.childappusage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;


public class GooglePlusButton extends AppCompatActivity implements View.OnClickListener {

    public String personName;
    public String email = "";
    public String personPhotoUrl = "";
    private Button button_logout;
    private TextView textView_name, textView_email;
    private RelativeLayout profile_layout;
    private ImageView imageView_profile_image;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_plus_button);


        Thread timer = new Thread() {
            public void run() {
                try {
                    //Display for 3 seconds
                    sleep(3000);
                } catch (InterruptedException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    //Goes to Activity  StartingPoint.java(STARTINGPOINT)
                    Intent openstartingpoint = new Intent(GooglePlusButton.this, NavigationLandlord.class);
                    startActivity(openstartingpoint);
                }
            }
        };
        timer.start();


        button_logout = (Button) findViewById(R.id.button_logout);
        button_logout.setOnClickListener(this);

        imageView_profile_image = (ImageView) findViewById(R.id.imageView_profile_image);
        textView_name = (TextView) findViewById(R.id.textView_name);
        textView_email = (TextView) findViewById(R.id.textView_email);
        profile_layout = (RelativeLayout) findViewById(R.id.profile_layout);

        // Initializing google plus api client

    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
        finish();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.button_logout:
                // logout button clicked
                Utility.deleteValueFromSP(this, email);

                break;


        }

    }


    private void getProfileInformation() {


        Utility.saveValueToSP(this, "email", email);


        textView_name.setText(personName);
        textView_email.setText(email);


    }

    public void usermodes(View view) {
        Intent intent = new Intent(this, NavigationLandlord.class);
        startActivity(intent);
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            Log.d("USERSIGNUP", "HTTP ASYNC CALLED");
            new HttpUserSignUp().execute("");

        }
    }

    private class HttpUserSignUp extends AsyncTask<String, Void, String> {
        @Override

        protected String doInBackground(String... urls) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("name", personName);
                jsonObject.accumulate("email", email);
                jsonObject.accumulate("imageurl", personPhotoUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONPostParser jParser = new JSONPostParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl("http://52.39.4.163:3000/api/users/signup", jsonObject.toString(), email);

            return json.toString();

        }

        protected void onPostExecute(String response) {
            Log.d("USERSIGNUP", response);
            Log.d("USERSIGNUP", "USER SIGNUP DONE");
        }
    }


}

