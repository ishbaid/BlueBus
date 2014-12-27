package com.baid.bluebus.bluebus;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ish on 10/23/14.
 */
public class BusScheduleActivity extends Activity implements OnTaskCompleted{

    //displays bus arrival times
    ListView arrivalTimes;

    TextView title;
    Spinner busStops;
    HashMap<Integer, String> routeMap;
    RetrieveStops rbs;
    RetrieveETA eta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_schedule);

        title = (TextView) findViewById(R.id.title);
        busStops = (Spinner) findViewById(R.id.bus_stops);
        arrivalTimes = (ListView) findViewById(R.id.arrival_times);

        //TODO: STORE locally/read from a database
        //keeps track of bus IDs associated with each bus
        routeMap = new HashMap<Integer, String>();

        //not all bus ids are known at this time
        routeMap.put(0,  "Commuter South");
        routeMap.put(1,  "Commuter North");
        routeMap.put(2,  "Northwood");
        routeMap.put(68, "Bursley-Baits");//weekend
        routeMap.put(69, "Bursley-Baits");//night
        routeMap.put(72, "Int.E.C.");
        routeMap.put(73, "Int.NIB");
        routeMap.put(75, "Mitchell-Glazier");
        routeMap.put(78, "KMS Shuttle");
        routeMap.put(87, "Oxford");
        routeMap.put(92, "Diag-Diag");
        routeMap.put(102, "Com.N.(Ni)");
        routeMap.put(107, "Oxford");
        routeMap.put(166, "Bursley-Baits");
        routeMap.put(167, "Bursley-Baits");
        routeMap.put(170, "Commuter North");
        routeMap.put(171, "Commuter North");
        routeMap.put(172, "Commuter South");
        routeMap.put(173, "Commuter South");
        routeMap.put(174, "Commuter South");
        routeMap.put(179, "Bio-Research Shuttle");
        routeMap.put(180, "MedExpress");
        routeMap.put(181, "MedExpress");
        routeMap.put(182, "North-East Shuttle");
        routeMap.put(183, "Wall Street - NIB");
        routeMap.put(184, "Wall Street Express");
        routeMap.put(185, "Wall Street Express");
        routeMap.put(186, "Northwood Express");
        routeMap.put(187, "Diag to Diag");
        routeMap.put(188, "Northwood");
        routeMap.put(189, "Oxford Shuttle");
        routeMap.put(190, "North Campus");
        routeMap.put(191, "North Campus");
        routeMap.put(192, "Night Owl");
        routeMap.put(193, "North Campus");
        routeMap.put(194, "North Campus");
        routeMap.put(197, "Bursley-Baits");
        routeMap.put(198, "Bursley-Baits");
        routeMap.put(199, "Northwood");
        routeMap.put(200, "Oxford");

    }

    @Override
    protected void onResume() {
        super.onResume();
        populateStops();
    }

    private void populateStops(){

        //retrieves bus stops
        rbs = new RetrieveStops(this);
        rbs.execute();

    }

    //when bus stops have been retrieved we can populate spinner
    @Override
    public void onTask1Completed() {

        ArrayList<String> stopNames = rbs.getStopNames();
        final HashMap<String, Integer> stopMap = rbs.getStopMap();
        ArrayAdapter <String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, stopNames);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );


        busStops.setAdapter(spinnerArrayAdapter);
        busStops.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //show arrival times for a stop
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String bName = (String) busStops.getItemAtPosition(position);

                //set title
                title.setText(bName);

                //retrieve bus stop arrival times
                int bid = stopMap.get(bName);
                eta = new RetrieveETA(bid, BusScheduleActivity.this);
                eta.execute();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //set defalt spinner value
        setSpinner();

    }

    //sets default value for spinner
    private void setSpinner(){

        Intent intent = getIntent();
        String myString = intent.getStringExtra("stopName");
        //If string is null, then just assume cc little
        if(myString == null)
            myString = "Central Campus Transit Center: CC Little"; //the value you want the position for

        ArrayAdapter myAdap = (ArrayAdapter) busStops.getAdapter(); //cast to an ArrayAdapter

        int spinnerPosition = myAdap.getPosition(myString);

        //set the default according to value
        busStops.setSelection(spinnerPosition);
    }

    //ETA is done
    @Override
    public void onTask2Completed() {

        ArrayList<Integer> bIDs = eta.getBusIDs();
        ArrayList<Integer> expTime = eta.getExpTimes();

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (int i = 0; i < bIDs.size(); i ++) {

            Map<String, String> datum = new HashMap<String, String>(2);

            Integer bid = bIDs.get(i);
            String bName = routeMap.get(bid);
            Integer bTime = expTime.get(i);

            //we only want to consider buses that will arrive within 30 minutes
            if(bTime > 30)
                continue;


            //put bus name and time into hashmap
            if(bName != null){

                datum.put("bus", bName);
            }
            else{

                datum.put("bus", "Unknown");
            }

            datum.put("time", bTime.toString());
            data.add(datum);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"bus", "time"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        arrivalTimes.setAdapter(adapter);


    }
}