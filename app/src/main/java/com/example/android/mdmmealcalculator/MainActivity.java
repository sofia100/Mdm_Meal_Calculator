package com.example.android.mdmmealcalculator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
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

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


TextView amount, budget,cummu;
ImageButton add_amount, add_budget;
Button update,ok_amt,ok_budget,export;
EditText students, add_amt_text, add_bdgt_text;
    FirebaseDatabase database;
    DatabaseReference refAmount, refBudget,refCummu, refRow;
double unit_budget=6.83;
double unit_amount = 0.15;
    StringBuilder data ;
Row row;
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data= new StringBuilder();

        export=findViewById(R.id.export);
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
        cummu = findViewById(R.id.cummu);

        row = new Row();

        database = FirebaseDatabase.getInstance();
        refAmount = database.getReference("amount");
        refBudget = database.getReference("budget");
        refRow= database.getReference("Row");
        refCummu=database.getReference("Cummulative Meal");

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
                        double x = Double.valueOf(s);

                        Double total = Double.valueOf(amount.getText().toString())+x;

                        refAmount.setValue(total);
                        updateValues();

                        add_amt_text.setVisibility(View.GONE);
                        ok_amt.setVisibility(View.GONE);

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
                        double x = Double.valueOf(s);

                        double total = Double.valueOf(budget.getText().toString())+x;

                        refBudget.setValue(total);
                        updateValues();

                        add_bdgt_text.setVisibility(View.GONE);
                        ok_budget.setVisibility(View.GONE);
                    }
                });
            }
        });


        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportcsv();
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

                row.setStudents(n);

                Log.d("update", "here ert");
//                caln haba amt n budget n upload

                String s= budget.getText().toString();
                double x = Double.valueOf(s);
                Log.d("update", "here ert2" + x);

                s= amount.getText().toString();
                double y = Double.valueOf(s);
                Log.d("update", "here ert2 budget" + y);

                double new_amount = y - unit_amount*n;
                double new_budget = x- unit_budget*n;
                Log.d("update", "here ert9");


                refBudget.setValue(new_budget);
                Log.d("update", "here ert0");

                row.setBudget(new_budget);
                row.setAmount(new_amount);
                row.setDate(new Date());
                double c = Double.valueOf( cummu.getText().toString());

                c+=n;
                row.setCummulative(c);
                refCummu.setValue(c);

                refAmount.setValue(new_amount);
                Log.d("update", "here ert99");

                refRow.push().setValue(row);
                updateValues();
                Log.d("update", "here ert77");

            }
        }
        );

    }

    private void exportcsv() {
            //generate data

            data.append("Date,No.of Beneficiaries,Cumulative No.of Meals,Food Grain left, Funds left");

            /*for(int i = 0; i<5; i++){
                data.append("\n"+String.valueOf(i)+","+String.valueOf(i*i));
            }
*/
        refRow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Log.d("data export","here!! export");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d("data export","here!! export");

                    Row value = postSnapshot.getValue(Row.class);
                    Log.d("data export","here!! export");

                    data.append("\n" + String.valueOf(value.getDate()) + "," + String.valueOf(value.getStudents()) + ","
                            + String.valueOf(value.getCummulative()) + "," + String.valueOf(value.getAmount()) + "," +
                            String.valueOf(value.getBudget()));
                    Log.d("data export",data.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("mainActivity", "Failed to read budget.", error.toException());
            }
        });


        try{
                //saving the file into device
                FileOutputStream out = openFileOutput("mdm_meal.csv", Context.MODE_PRIVATE);
                out.write((data.toString()).getBytes());
                out.close();

                //exporting
                Context context = getApplicationContext();
                File filelocation = new File(getFilesDir(), "mdm_meal.csv");
                Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation);
                Intent fileIntent = new Intent(Intent.ACTION_SEND);
                fileIntent.setType("text/csv");
                fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
                fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                startActivity(Intent.createChooser(fileIntent, "Send mail"));
            }
            catch(Exception e){
                e.printStackTrace();
            }


            }

    private void updateValues() {

        refAmount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Log.d("MainActivity","here!! jkl");
                double value = dataSnapshot.getValue(double.class);
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
                double value = dataSnapshot.getValue(double.class);
                budget.setText(String.valueOf(value));
                // Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("mainActivity", "Failed to read budget.", error.toException());
            }
        });

        refCummu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Log.d("MainActivity","here!! mlp");
                double value = dataSnapshot.getValue(double.class);
                cummu.setText(String.valueOf(value));
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
