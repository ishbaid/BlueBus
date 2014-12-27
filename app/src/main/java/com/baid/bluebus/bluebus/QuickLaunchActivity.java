package com.baid.bluebus.bluebus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Ish on 11/2/14.
 */
public class QuickLaunchActivity extends Activity  implements View.OnClickListener{

    Button imBuilding, mUnion, ccLitte, couzens, markley, pierpont, bursely;
    Button[]buttons;
    String[] stopNames;
    final int NUM_BUTTONS = 7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_launch);


        imBuilding = (Button) findViewById(R.id.im_building);
        mUnion = (Button) findViewById(R.id.michigan_union);
        ccLitte = (Button) findViewById(R.id.cc_little);
        couzens = (Button) findViewById(R.id.couzens);
        markley = (Button) findViewById(R.id.markley);
        pierpont = (Button) findViewById(R.id.bursley);
        bursely = (Button) findViewById(R.id.bursley);

        buttons = new Button[NUM_BUTTONS];
        buttons[0] = imBuilding;
        buttons[1] = mUnion;
        buttons[2] = ccLitte;
        buttons[3] = couzens;
        buttons[4] = markley;
        buttons[5] = pierpont;
        buttons[6] = bursely;

        stopNames = new String[NUM_BUTTONS];
        stopNames[0] = "Intramural Building - Outbound";
        stopNames[1] = "Michigan Union - South University";
        stopNames[2] = "Central Campus Transit Center: CC Little";
        stopNames[3] = "Couzens\\/Zina Pitcher";
        stopNames[4] = "Markley";
        stopNames[5] = "Pierpont Commons - Bonisteel Inbound";
        stopNames[6] = "Bursley - Inbound";

        for(int i = 0; i < buttons.length; ++ i){

            buttons[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        for(int i = 0; i < buttons.length; i ++){

            if(id == buttons[i].getId()){

                String name = stopNames[i];

                Intent intent = new Intent(QuickLaunchActivity.this, BusScheduleActivity.class);
                intent.putExtra("stopName", name);
                startActivity(intent);

            }
        }
    }
}
