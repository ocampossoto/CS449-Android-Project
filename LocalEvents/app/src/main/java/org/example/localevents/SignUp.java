package org.example.localevents;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        //submit button
        Button submit = findViewById(R.id.Sign_Up_Commit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });


    }

    private void signup() {
        //user input items
        EditText email_box = findViewById(R.id.Email_SignUp);
        EditText email_verify_box = findViewById(R.id.Email_Verify_Signup);
        EditText password_box = findViewById(R.id.Password_SignUp);
        EditText password_verify_box = findViewById(R.id.Password_Verify_Signup);
        EditText FName_Box = findViewById(R.id.FName);
        EditText LName_Box = findViewById(R.id.LName);
        EditText Birthday_Box = findViewById(R.id.Birthday);
        //save inputed text
        String email = email_box.getText().toString();
        String email_verify = email_verify_box.getText().toString();
        String password = password_box.getText().toString();
        String password_verify = password_verify_box.getText().toString();
        String FName = FName_Box.getText().toString();
        String LName = LName_Box.getText().toString();
        String Birthday = Birthday_Box.getText().toString();
        //check if emails match and passwords match
        if(email.equals(email_verify)){
            if(password.equals(password_verify)){
                //post to database and sign up
                commit_signup(email, password, FName, LName, Birthday);
            }
            else{
                Toast.makeText(SignUp.this, "Sign up failed." + " Password did not match",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(SignUp.this, "Sign up failed." + " Email did not match",
                    Toast.LENGTH_SHORT).show();
        }

    }


    private void commit_signup(final String email, String password, final String First, final String Last, final String DOB){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            //save user data to database
                            final user_profile user_data = new user_profile();
                            user_data.from_data(email, First, Last, DOB, user.getUid());
                            save_user_data(user_data);
                            //exit to logged in page
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Error", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Sign pp failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });

    }
    private void save_user_data(final user_profile user){
        //firebase set up
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Users");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //save user data to database
                ref.push().setValue(user);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(SignUp.this, AccountView.class);
            startActivity(intent);
            finish();
        }
    }
}
