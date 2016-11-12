package edu.uco.map2016.mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    Button searchButton;
    EditText searchTextField;
    Button butt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = (Button) findViewById(R.id.layout_main_search_button);
        searchTextField = (EditText) findViewById(R.id.layout_main_search_text_field);
        //butt1 = (Button) findViewById(R.id.butt1);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityList = new Intent(MainActivity.this, ListActivity.class); //getApplicationContext()
                activityList.putExtra("SEARCH", searchTextField.getText().toString());
                startActivity(activityList);
            }
        });

        Button browseButton = (Button)findViewById(R.id.layout_main_browse_button);

        browseButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OpenActivity.class);
            startActivity(intent);
        });




    }




}
