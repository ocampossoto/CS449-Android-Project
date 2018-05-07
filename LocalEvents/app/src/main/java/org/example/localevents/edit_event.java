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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class edit_event extends AppCompatActivity {
    String ID;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        mAuth = FirebaseAuth.getInstance();
        //get intent data
        Intent extras = getIntent();
        //save the ID
        ID = extras.getStringExtra("ID");
        get_event(ID); //get the event data and update UI
        //Button click listener
        Button btn = findViewById(R.id.Submit);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                //update database with new event data
                update_database(get_new_event());
                finish(); //close page
            }
        });
        //cancel button listener
        Button btn_cancel = findViewById(R.id.Cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish(); //exit activity
            }
        });

        //remove button listener
        Button delete_btn = findViewById(R.id.Remove_Event);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_event(ID); //delete event from database.
                finish(); // exit activity
            }
        });
    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in exit if not signed in
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(edit_event.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private Event get_new_event(){
        //event data storage.
        Event new_event = new Event();
        //data fields
        EditText Name_edit = findViewById(R.id.Event_Name);
        EditText Street_edit = findViewById(R.id.Street_Edit);
        EditText City_edit = findViewById(R.id.City_Edit);
        EditText State_edit = findViewById(R.id.State_Edit);
        EditText Zip_edit = findViewById(R.id.Zip_Edit);
        EditText description_edit = findViewById(R.id.Description_Edit);
        CheckBox privacy = findViewById(R.id.Privacy_Edit);
        //add data to event object
        new_event.Event_from_data(Name_edit.getText().toString(), description_edit.getText().toString(), Street_edit.getText().toString(),City_edit.getText().toString(),State_edit.getText().toString(), Zip_edit.getText().toString(), currentUser.getUid(), privacy.isChecked());
        //return the new event object
        return new_event;
    }

    private void update_database(final Event new_event){
        //firebase database set up
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Events");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //update the child by id
                ref.child(ID).setValue(new_event);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }

    private void get_event(final String ID_event){
        //firebase setup
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Events");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //get id
                    String snapkey= snapshot.getKey();
                    //check if this is the event
                    if(ID_event.equals(snapkey)){
                        //get event data and update ui
                        Event item = snapshot.getValue(Event.class);
                        update_UI(item);
                        break; //leave loop
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }

    private void delete_event(final String ID_event){
        //firebase setup
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Events");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //get key
                    String snapkey= snapshot.getKey();
                    //check if this is the event we are looking for
                    if(ID_event.equals(snapkey)){
                        //remove the event with the given key
                        ref.child(snapkey).removeValue();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }

    private void update_UI(Event given_event){
        //data inputs
        EditText name = findViewById(R.id.Event_Name);
        EditText Street = findViewById(R.id.Street_Edit);
        EditText City = findViewById(R.id.City_Edit);
        EditText State = findViewById(R.id.State_Edit);
        EditText Zip = findViewById(R.id.Zip_Edit);
        EditText description = findViewById(R.id.Description_Edit);
        CheckBox privacy = findViewById(R.id.Privacy_Edit);
        //get address as an array
        ArrayList<String> Address_Array = given_event.getAddress();
        //add data to ui interface
        name.setText(given_event.getEvent_name());
        Street.setText(Address_Array.get(0));
        City.setText(Address_Array.get(1));
        State.setText(Address_Array.get(2));
        Zip.setText(Address_Array.get(3));
        description.setText(given_event.getDescription());
        privacy.setChecked(given_event.isPrivate());
    }

}
