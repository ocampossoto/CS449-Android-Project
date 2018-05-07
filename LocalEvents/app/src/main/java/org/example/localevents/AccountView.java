package org.example.localevents;

import android.accounts.Account;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class AccountView extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_view);
        mAuth = FirebaseAuth.getInstance();

        //sign out button
        Button signout = findViewById(R.id.SignOut);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
            }
        });

        //return button
        Button home = findViewById(R.id.Back_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountView.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(AccountView.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            //if logged in reload events
            updateUI(currentUser);
        }

    }

    private void updateUI(final FirebaseUser currentUser) {
        //firebase set up
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //save user profile as an object
                    user_profile item = snapshot.getValue(user_profile.class);
                    //check if we have the correct User
                    if(item.getUID().equals(currentUser.getUid())){
                        //ui items
                        TextView email = findViewById(R.id.Email_Display);
                        TextView name = findViewById(R.id.Account_Name);
                        TextView dob = findViewById(R.id.Birthday_Display);
                        //update ui items with user info
                        email.setText("Email: " +item.getEmail());
                        name.setText(item.getFName()+ " " + item.getLName());
                        dob.setText("Birthday: " + item.getDOB());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });

    }

}