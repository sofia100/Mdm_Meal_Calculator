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
Button update,ok_amt,ok_budget;
EditText students, add_amt_text, add_bdgt_text;
    FirebaseDatabase database;
    DatabaseReference refAmount, refBudget;
double unit_budget=6.83;
double unit_amount = 0.15;

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
        add_amt_text=findViewById(R.id.add_amount_edit);
        add_bdgt_text=findViewById(R.id.add_budget_edit);
        ok_amt=findViewById(R.id.ok_amt);
        ok_budget=findViewById(R.id.ok_budget);


        database = FirebaseDatabase.getInstance();
        refAmount = database.getReference("amount");
        refBudget = database.getReference("budget");

        updateValues();

        add_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_amt_text.setVisibility(View.VISIBLE);
                ok_amt.setVisibility(View.VISIBLE);


                ok_amt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //extract from edittext n upload to db added value n gone visibility

                        String s= add_amt_text.getText().toString();
                        int x = Integer.valueOf(s);

                        int total = Integer.valueOf(amount.getText().toString())+x;

                        refAmount.setValue(total);
                        updateValues();

                        add_amt_text.setVisibility(View.INVISIBLE);
                        ok_amt.setVisibility(View.INVISIBLE);


                    }
                });
            }
        });

        add_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_bdgt_text.setVisibility(View.VISIBLE);
                ok_budget.setVisibility(View.VISIBLE);


                ok_budget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //extract from edittext n upload to db added value n gone visibility
                        String s= add_bdgt_text.getText().toString();
                        int x = Integer.valueOf(s);

                        int total = Integer.valueOf(budget.getText().toString())+x;

                        refBudget.setValue(total);
                        updateValues();

                        add_bdgt_text.setVisibility(View.INVISIBLE);
                        ok_budget.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity","here!!");
                int n ;
                if(students.getText().toString().equals(""))
                    n=0;
                else
                    n= Integer.valueOf(students.getText().toString());

                Log.d("update", "here ert");
//                caln haba amt n budget n upload

                String s= budget.getText().toString();
                int x = Integer.valueOf(s);
                Log.d("update", "here ert2" + x);

                s= amount.getText().toString();
                int y = Integer.valueOf(s);
                Log.d("update", "here ert2 budget" + y);

                double new_amount = y - unit_amount*n;
                double new_budget = x- unit_budget*n;
                Log.d("update", "here ert9");


                refBudget.setValue(new_budget);
                Log.d("update", "here ert0");

                refAmount.setValue(new_amount);
                Log.d("update", "here ert99");

                updateValues();
                Log.d("update", "here ert77");

            }
        }
        );

    }

    private void updateValues() {

        refAmount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Log.d("MainActivity","here!! jkl");
                int value = dataSnapshot.getValue(int.class);
                Log.d("MainActivity","here!!2000 jkl");
                amount.setText(String.valueOf(value));
                Log.d("MainActivity","here!!7000 jkl");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("MainActivity", "Failed to read amount.", error.toException());
            }
        });

        refBudget.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Log.d("MainActivity","here!! mlp");
                int value = dataSnapshot.getValue(int.class);
                budget.setText(String.valueOf(value));
                // Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("mainActivity", "Failed to read budget.", error.toException());
            }
        });


    }


}
