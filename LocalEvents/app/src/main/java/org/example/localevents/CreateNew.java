package org.example.localevents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class CreateNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);

        //findViewById(R.id.Ttile_Event).add
        //get the button we just created
        Button btn1 = findViewById(R.id.Submit);

        //set the action upon click
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            }
        });
    }
}
