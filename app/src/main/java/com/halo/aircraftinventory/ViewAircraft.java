package com.halo.aircraftinventory;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.halo.aircraftinventory.helper.CheckNetworkStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewAircraft extends AppCompatActivity {
    private AircraftDatabaseController fishDatabaseController;
    private ProgressDialog pDialog;


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

    public void callBackup(View view){
        if(CheckNetworkStatus.isNetworkAvailable(getApplicationContext())){
            showBackup();
        }else{
            Toast.makeText(this.getBaseContext(),"You need network for this, baka",Toast.LENGTH_LONG).show();
        }
    }

    private  void  showBackup(){
        new RemoteDatabaseTask().execute("http://192.168.1.76:3000/aircraft/get");
    }

    private void afterExecute(String data){
        if(data.matches(".*There was an error:.*")){
            Toast.makeText(this.getBaseContext(),data,Toast.LENGTH_LONG).show();
        }else{
            try{
                JSONArray jsonArray = new JSONArray(data);

                ViewGroup tbl = findViewById(R.id.aircraftTable);

                tbl.removeAllViews();

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    System.out.println(object);

                    View row = getLayoutInflater().inflate(R.layout.aircraft_remote_row,null);

                    TextView idTextView = row.findViewById(R.id.id_rem);
                    TextView producerTextView = row.findViewById(R.id.producer_rem);
                    TextView modelTextView = row.findViewById(R.id.model_rem);
                    TextView statusTextView = row.findViewById(R.id.status_rem);
                    TextView pilotTextView = row.findViewById(R.id.pilot_rem);

                    idTextView.setText(object.get("id").toString());
                    producerTextView.setText(object.get("producer").toString());
                    modelTextView.setText(object.get("model").toString());
                    statusTextView.setText(object.get("currentStatus").toString());
                    pilotTextView.setText(object.get("pilot").toString());

                    tbl.addView(row);

                }
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this.getBaseContext(),"Error parsing data",Toast.LENGTH_LONG).show();
            }
        }
    }

    private class RemoteDatabaseTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ViewAircraft.this);
            pDialog.setMessage("Getting backup. This could take a while");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            int statusCode = 0;
            HttpURLConnection connection = null;

            try{
                URL url = new URL(strings[0]);

                 connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");

                connection.connect();

                InputStreamReader irs = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(irs);
                StringBuilder sb = new StringBuilder();
                String il;

                while ((il = reader.readLine())!= null){
                    sb.append(il);
                }

                reader.close();
                irs.close();

                data = sb.toString();

                statusCode = connection.getResponseCode();
                System.out.println(data);
            }catch (Exception e){
                e.printStackTrace();
                data = "There was an error: " + statusCode;
            }finally {
                pDialog.dismiss();
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            afterExecute(s);
        }
    }
}
