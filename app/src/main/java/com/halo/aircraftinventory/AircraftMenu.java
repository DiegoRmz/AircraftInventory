package com.halo.aircraftinventory;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.halo.aircraftinventory.fragment.MenuFragment;

public class AircraftMenu extends AppCompatActivity implements MenuFragment.OnFragmentInteractionListener{
    MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aircraft_menu);

        menuFragment = new MenuFragment();


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.add(R.id.menuFrame,menuFragment);

        ft.commit();
    }

    public void gotoInsertAircraft(View view){
        menuFragment.liftInsert(this);
    }

    public void gotoConsultAircraft(View view){
        menuFragment.liftShow(this);
    }

    public void gotoUpdateAircraft(View v){
        menuFragment.liftUpdate(this);
    }

    public void gotoDeleteAircraft(View v){
        menuFragment.liftDelete(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
