package com.halo.aircraftinventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AircraftMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aircraft_menu);
    }

    public void gotoInsertAircraft(View view){
        Intent intent = new Intent(this,InsertAircraft.class);

        startActivity(intent);
    }

    public void gotoConsultAircraft(View view){
        Intent intent = new Intent(this,ViewAircraft.class);

        startActivity(intent);
    }

    public void gotoUpdateAircraft(View v){
        Intent i = new Intent(this,UpdateAircraft.class);

        startActivity(i);
    }

    public void gotoDeleteAircraft(View v){
        Intent i = new Intent(this,DeleteAircraft.class);
        startActivity(i);
    }
}
