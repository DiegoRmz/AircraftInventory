package com.halo.aircraftinventory;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class ViewAircraft extends AppCompatActivity {
    private AircraftDatabaseController fishDatabaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_aircraft);

        fishDatabaseController = new AircraftDatabaseController(this.getBaseContext());
    }

    public void loadDatabase(View view){
        Cursor cursor = fishDatabaseController.selectAircraft("",null);

        ViewGroup tbl = findViewById(R.id.aircraftTable);

        tbl.removeAllViews();

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

            tbl.addView(row);
        }
    }
}
