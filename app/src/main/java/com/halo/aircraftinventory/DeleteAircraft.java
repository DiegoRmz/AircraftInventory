package com.halo.aircraftinventory;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteAircraft extends AppCompatActivity {
    private AircraftDatabaseController fishDatabaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_aircraft);

        this.fishDatabaseController = new AircraftDatabaseController(this.getBaseContext());


        createDeleteTable();
    }

    private void createDeleteTable(){
        Cursor cursor = fishDatabaseController.selectAircraft("",null);

        ViewGroup tbl = findViewById(R.id.uselessTableLayout);

        tbl.removeAllViews();   //Allows reloading

        Log.d("Tag",cursor.toString());


        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(Aircraft.COLUMN_ID));
            String producer = cursor.getString(cursor.getColumnIndex(Aircraft.COLUMN_PRODUCER));
            String model = cursor.getString(cursor.getColumnIndex(Aircraft.COLUMN_MODEL));
            String status = cursor.getString(cursor.getColumnIndex(Aircraft.COLUMN_STATUS));
            String pilot = cursor.getString(cursor.getColumnIndex(Aircraft.COLUMN_PILOT));

            View row = getLayoutInflater().inflate(R.layout.aircraft_row,null);

            TextView idTextView = row.findViewById(R.id.id_);
            TextView producerTextView = row.findViewById(R.id.producer_);
            TextView modelTextView = row.findViewById(R.id.model_);
            EditText statusTextView = row.findViewById(R.id.status_);
            EditText pilotTextView = row.findViewById(R.id.pilot_);

            idTextView.setText(""+id);
            producerTextView.setText(producer);
            modelTextView.setText(model);
            statusTextView.setText(status);
            pilotTextView.setText(pilot);

            Button editBtn = row.findViewById(R.id.editButton);
            editBtn.setText(R.string.delet);

            editBtn.setOnClickListener(deleteActionListener(editBtn));
            tbl.addView(row);
        }
    }
    View.OnClickListener deleteActionListener(final Button btn){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "Deletion done succesfully";

                View parentView = (View) v.getParent();
                Aircraft editable = new Aircraft();

                TextView idTextView = parentView.findViewById(R.id.id_);

                int id = Integer.parseInt(""+idTextView.getText());


                editable.setId(id);



                if(fishDatabaseController.delete(editable) < 0){
                    msg = "There was an error in the delete";
                }

                Toast.makeText(parentView.getContext(),msg,Toast.LENGTH_LONG).show();
                createDeleteTable();
            }
        };
    }

}
