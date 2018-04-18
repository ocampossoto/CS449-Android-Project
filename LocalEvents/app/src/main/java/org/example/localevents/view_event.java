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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        mAuth = FirebaseAuth.getInstance();

        Intent extras = getIntent();

        ID = extras.getStringExtra("ID");
        host = extras.getStringExtra("host_id");
        get_event(ID);

        Button btn = findViewById(R.id.Edit_Button);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), edit_event.class);
                intent.putExtra("ID", ID);
                startActivity(intent);
                finish();
            }
        });
    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
    }

    private void get_event(final String ID_event){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Events");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String snapkey= snapshot.getKey();
                    if(ID_event.equals(snapkey)){
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
        TextView name = findViewById(R.id.event_name_view);
        TextView Address = findViewById(R.id.address_view);
        TextView description = findViewById(R.id.description_view);

        name.setText(given_event.getEvent_name());
        Address.setText(given_event.getAddressString());
        description.setText(given_event.getDescription());
    }

}
