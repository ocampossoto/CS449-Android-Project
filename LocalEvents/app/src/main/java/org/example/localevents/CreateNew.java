package org.example.localevents;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CreateNew extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);
        mAuth = FirebaseAuth.getInstance();

        //findViewById(R.id.Ttile_Event).add
        //get the button we just created
        Button btn1 = findViewById(R.id.Submit);

        //set the action upon click
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    submit_to_Firebase();
                }
                catch(AssertionError t){
                    Toast message = Toast.makeText(CreateNew.this,"Please Login",Toast.LENGTH_LONG);
                    message.show();
                }
                finish();
            }
        });

    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();

    }

    private void submit_to_Firebase (){

        if (currentUser == null){
            throw new AssertionError("User cannot be null, must login");
        }

        //get all items from page
        TextView Name = findViewById(R.id.Event_Name);
        TextView Street = findViewById(R.id.Street_Edit);
        TextView City = findViewById(R.id.City_Edit);
        TextView State = findViewById(R.id.State_Edit);
        TextView Zip = findViewById(R.id.Zip_Edit);
        TextView Description = findViewById(R.id.Description_Edit);
        final Event event_data = new Event();

        event_data.Event_from_data(Name.getText().toString(), Description.getText().toString(), Street.getText().toString(),City.getText().toString(),State.getText().toString(), Zip.getText().toString(), currentUser.getUid());

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Events");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
//                        ArrayList<Event> event_list = (ArrayList<Event>) dataSnapshot.getValue();
//                        event_list.add(event_data);
                        ref.push().setValue(event_data);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

    }

    class Post_Data extends AsyncTask<Void, Void, String> {


        //get all items from page
        TextView Name = findViewById(R.id.Event_Name);
        TextView Street = findViewById(R.id.Street_Edit);
        TextView City = findViewById(R.id.City_Edit);
        TextView State = findViewById(R.id.State_Edit);
        TextView Zip = findViewById(R.id.Zip_Edit);
        TextView Description = findViewById(R.id.Description_Edit);



        String event_details = "Name=" + Name.getText()
                + "&Description=" + Description.getText()
                + "&Street="+ Street.getText()
                + "&City=" + City.getText()
                + "&State="+ State.getText()
                + "&Zip="+ Zip.getText()
                + "&Host_id=0&Privacy=0";

        //private Exception exception;

        @Override
        protected void onPreExecute() {
            //findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            String url = "https://eventsapi-jhqptvquoo.now.sh/events/";
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, event_details);
            Request request = new Request.Builder()
                    .url("https://eventsapi-jhqptvquoo.now.sh/events/")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "b301cb63-533f-4177-aacd-3fb33ed889c6")
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String myResponse = response.body().string();
                    Log.e("Response", myResponse);
                }
            });

            return null;
        }

        protected void onPostExecute(String response) {
            //findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        }
    }


}


