package org.example.localevents;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.*;


public class MainActivity extends AppCompatActivity {

    JSONArray event_list = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add button to create an event.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), CreateNew.class);
                startActivity(intent);
            }
        });

        Button refresh = findViewById(R.id.Refresh_button);
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new RetrieveFeedTask().execute();

            }
        });
        //run database connection
        new RetrieveFeedTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //if account setting is selected
        if (id == R.id.account) {
            //open the login activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        //private Exception exception;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {
            // add events dynamically
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            String url = "https://eventsapi-jhqptvquoo.now.sh/events/";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray temp = new JSONArray(response);
                                event_list = temp;
                                update();
                                //Log.e("Event List", event_list.toString());

                            } catch (Throwable t) {
                                Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
            return null;
        }

        protected void onPostExecute(String response) {
        }
    }

    void update(){
        findViewById(R.id.Refresh_button).setVisibility(View.GONE);
        LinearLayout layout = findViewById(R.id.layout); //outer layout
        //add 20 items to the layout
        //Log.e("list", event_list.toString());
        for (int i = 0; i < event_list.length(); i++) {
            String Event_name;
            String Description;
            JSONObject Address_obj;
            String Address;
            String event_id;
            //json object
            try{
                JSONObject obj = event_list.optJSONObject(i);
                event_id = obj.getString("_id");
                Event_name = obj.getString("Name");
                Description = obj.getString("Description");
                Address_obj = obj.getJSONObject("Address");
                Address = Address_obj.getString("Street").toString() + " " + Address_obj.getString("City").toString() +" " + Address_obj.getString("State").toString() + " " +Address_obj.getString("Zip").toString();

            }
            catch (Throwable t){
                Log.e("error", "getting values");
                event_id = "0";
                Event_name = "Event Name";
                Description = "Description";
                Address = "Location";
            }


            //get the screen data
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            //set the size constraint for each item
            int height = displayMetrics.heightPixels/8;
            int width = displayMetrics.widthPixels-(displayMetrics.widthPixels/4);

            //horizontal layout for event items
            LinearLayout layoutHorizontal = new LinearLayout(MainActivity.this);
            layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);

            //vertical layout for list of event info
            LinearLayout layoutVertical = new LinearLayout(MainActivity.this);
            layoutVertical.setOrientation(LinearLayout.VERTICAL);
            //set height and width of the vertical layout
            //helps keep the button on right and info on left
            layoutVertical.setLayoutParams(new LinearLayout.LayoutParams(width,height));

            //add border to layout
            GradientDrawable border = new GradientDrawable();
            border.setColor(0xFFFFFFFF); //white background
            border.setStroke(1, 0xFF000000); //black border with full opacity
            //add to layout
            layoutVertical.setBackground(border);

            //add textview for the event name
            TextView Name = new TextView(MainActivity.this);
            Name.setText(Event_name); //event name
            Name.setTextSize(18); // set size
            Name.setTextColor(Color.BLACK); //text color
            layoutVertical.addView(Name); //add to vertical layout

            //add location textview
            TextView Location = new TextView(MainActivity.this);
            Location.setText(Description + "\n" + Address);
            //Location.setHeight(300);
            Location.setTextSize(18); //size
            layoutVertical.addView(Location); // add to vertical layout

            //add to horizontal layout
            layoutHorizontal.addView(layoutVertical);

            //add view button
            Button btn = new Button(MainActivity.this);
            btn.setId(i); //set button id
            final int id_ = btn.getId(); //get id
            btn.setText("View"); // button text
            //add to horizontal layout
            layoutHorizontal.addView(btn);
            //add to the upper layout
            layout.addView(layoutHorizontal);

            //get the button we just created
            Button btn1 = findViewById(id_);

            final String event_ID = event_id;
            //set the action upon click
            btn1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //if clicked open the view activity and send data for it to work properly

                    Intent intent = new Intent(view.getContext(), view_event.class);
                    intent.putExtra("ID", event_ID);
                    intent.putExtra("host_id", "1");
                    startActivity(intent);

                }
            });

        }
    }
}
