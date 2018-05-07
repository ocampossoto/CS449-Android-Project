package org.example.localevents;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
                //attempt to submit to firebase
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
        // Check if user is signed in if not send user to login page
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(CreateNew.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void submit_to_Firebase (){

        //get all items from page
        TextView Name = findViewById(R.id.Event_Name);
        TextView Street = findViewById(R.id.Street_Edit);
        TextView City = findViewById(R.id.City_Edit);
        TextView State = findViewById(R.id.State_Edit);
        TextView Zip = findViewById(R.id.Zip_Edit);
        TextView Description = findViewById(R.id.Description_Edit);
        CheckBox privacy = findViewById(R.id.Privacy_create);
        //create event object
        final Event event_data = new Event();
        //set event data
        event_data.Event_from_data(Name.getText().toString(), Description.getText().toString(), Street.getText().toString(),City.getText().toString(),State.getText().toString(), Zip.getText().toString(), currentUser.getUid(), privacy.isChecked());
        //firebase set up
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Events");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //add event to database
                        ref.push().setValue(event_data);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

    }
}


