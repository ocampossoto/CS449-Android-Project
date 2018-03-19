package org.example.localevents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class edit_event extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        Button btn = findViewById(R.id.Submit);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
    }
}
