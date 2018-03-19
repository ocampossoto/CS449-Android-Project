package org.example.localevents;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class view_event extends AppCompatActivity {
    String ID;
    JSONObject temp = new JSONObject();
    String host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        Intent extras = getIntent();

        ID = extras.getStringExtra("ID");
        host = extras.getStringExtra("host_id");
        new RetrieveFeedTask().execute();
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        //private Exception exception;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {
            // add events dynamically
            RequestQueue queue = Volley.newRequestQueue(view_event.this);
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
        TextView name = findViewById(R.id.event_name_view);
        TextView Address = findViewById(R.id.address_view);
        TextView description = findViewById(R.id.description_view);
        try{
            name.setText(temp.getString("Name"));
            JSONObject Address_obj = temp.getJSONObject("Address");
            Address.setText(Address_obj.getString("Street").toString() + " " + Address_obj.getString("City").toString() +" " + Address_obj.getString("State").toString() + " " +Address_obj.getString("Zip").toString());
            description.setText(temp.getString("Description"));
            if(host.equals(temp.getString("Host_id"))){
                LinearLayout layout = findViewById(R.id.layout); //outer layout

                Button btn = new Button(this);
                btn.setText("Edit");

                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), edit_event.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("host_id", "1");
                        startActivity(intent);
                        finish();
                    }
                });
                layout.addView(btn);

            }

        }
        catch (Throwable t){
            Log.e("Error", "Json error");
        }

    }
}
