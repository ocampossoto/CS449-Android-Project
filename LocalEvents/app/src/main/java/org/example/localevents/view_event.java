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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class view_event extends AppCompatActivity {
    String ID;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String host;
    boolean event_privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        mAuth = FirebaseAuth.getInstance();
        //get intent extras
        Intent extras = getIntent();
        //get items from intent
        ID = extras.getStringExtra("ID");
        host = extras.getStringExtra("host_id");
        //get event data
        get_event(ID);
        // exit button actions
        Button btn = findViewById(R.id.Edit_Button);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //create intent to edit page
                Intent intent = new Intent(view.getContext(), edit_event.class);
                //set id of the event
                intent.putExtra("ID", ID);
                //start activity
                startActivity(intent);
                finish(); //exit this activity
            }
        });
    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            //exit if user isn't logged in
            Intent intent = new Intent(view_event.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void get_event(final String ID_event){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Events");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //save key
                    String snapkey= snapshot.getKey();
                    //if correct key update ui
                    if(ID_event.equals(snapkey)){
                        //save event data from db
                        Event item = snapshot.getValue(Event.class);
                        update_UI(item);
                        break;
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
        //ui items
        TextView name = findViewById(R.id.event_name_view);
        TextView Address = findViewById(R.id.address_view);
        TextView description = findViewById(R.id.description_view);
        //update ui info
        name.setText(given_event.getEvent_name());
        Address.setText(given_event.getAddressString());
        description.setText(given_event.getDescription());
        //check if this is the host
        if(!given_event.getHost().equals(currentUser.getUid())){
            //add edit button for the host.
            Button edit_button = findViewById(R.id.Edit_Button);
            edit_button.setVisibility(View.INVISIBLE);
        }
    }

}
