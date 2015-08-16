package com.internal.jk.myday;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText activityTxt, durationTxt;
    private Button btn;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityTxt = (EditText) findViewById(R.id.activity_text);
        durationTxt = (EditText) findViewById(R.id.duration_text);
        btn = (Button) findViewById(R.id.add_button);
        list = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<String>();

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);

        // Here, you set the data in your ListView
        list.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (activityTxt.getText().toString().trim().isEmpty() || durationTxt.getText().toString().trim().isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Activity or duration is empty", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }


                // this line adds the data of your EditText and puts in your array
                arrayList.add(activityTxt.getText().toString() + ": " + durationTxt.getText().toString() + " hrs");
                // next thing you have to do is check if your adapter has changed
                adapter.notifyDataSetChanged();

                durationTxt.setText(null);
                activityTxt.setText(null);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendData(View v) {

        PackageManager pm = getPackageManager();

        String summaryData = "Summary of the day:\n\n";

        for (int i = 0; i < arrayList.size(); i++) {
            summaryData += arrayList.get(i).toString() + "\n";
        }

        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = summaryData;

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void clearData(View v) {
        durationTxt.setText(null);
        activityTxt.setText(null);
        //list.setAdapter(null);
        arrayList.clear();
        adapter.notifyDataSetChanged();
    }

}
