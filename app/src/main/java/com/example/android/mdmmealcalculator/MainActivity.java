package com.example.android.mdmmealcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


TextView amount, budget;
ImageButton add_amount, add_budget;
Button update;
EditText students;
    FirebaseDatabase database;
    DatabaseReference refAmount, refBudget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        amount = findViewById(R.id.amt);
        budget = findViewById(R.id.budget);
        add_amount = findViewById(R.id.add_amount);
        add_budget = findViewById(R.id.add_budget);
        students = findViewById(R.id.no_of_students_editview);
        update=findViewById(R.id.update);

        database = FirebaseDatabase.getInstance();
        refAmount = database.getReference("amount");
        refBudget = database.getReference("budget");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refAmount.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        int value = dataSnapshot.getValue(int.class);
                        amount.setText(value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("MainActivity", "Failed to read amount.", error.toException());
                    }
                });

                refBudget.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        int value = dataSnapshot.getValue(int.class);
                        budget.setText(value);
                        // Log.d(TAG, "Value is: " + value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("mainActivity", "Failed to read budget.", error.toException());
                    }
                });

            }
        }
        );

    }


}
