package org.example.localevents;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class edit_event extends AppCompatActivity {
    String ID;
    JSONObject temp = new JSONObject();
    String host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Intent extras = getIntent();

        ID = extras.getStringExtra("ID");
        host = extras.getStringExtra("host_id");

        new RetrieveFeedTask().execute();

        Button btn = findViewById(R.id.Submit);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
    }
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        //private Exception exception;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {
            // add events dynamically
            RequestQueue queue = Volley.newRequestQueue(edit_event.this);
            String url = "https://eventsapi-jhqptvquoo.now.sh/events/" + ID.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(String response) {
                            try {
                                temp = new JSONObject(response);
                                //Log.e("Item", response);
                                display_data();
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
    private void display_data(){
        EditText name = findViewById(R.id.Event_Name);
        EditText Street = findViewById(R.id.Street_Edit);
        EditText City = findViewById(R.id.City_Edit);
        EditText State = findViewById(R.id.State_Edit);
        EditText Zip = findViewById(R.id.Zip_Edit);
        EditText description = findViewById(R.id.Description_Edit);
        try{
            name.setText(temp.getString("Name"));
            JSONObject Address_obj = temp.getJSONObject("Address");
            Street.setText(Address_obj.getString("Street"));
            City.setText(Address_obj.getString("City"));
            State.setText(Address_obj.getString("State"));
            Zip.setText(Address_obj.getString("Zip"));
            //Address.setText(Address_obj.getString("Street").toString() + " " + Address_obj.getString("City").toString() +" " + Address_obj.getString("State").toString() + " " +Address_obj.getString("Zip").toString());
            description.setText(temp.getString("Description"));

        }
        catch (Throwable t){
            Log.e("Error", "Json error");
        }

    }
}
