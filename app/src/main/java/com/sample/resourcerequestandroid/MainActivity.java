package com.sample.resourcerequestandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import com.worklight.wlclient.api.*;
import com.worklight.wlclient.api.WLResourceRequest;

public class MainActivity extends AppCompatActivity {
    private WLClient client = null;
    private static MainActivity _this;
    private TextView first_name = null;
    private TextView middle_name = null;
    private TextView last_name = null;
    private TextView age = null;
    private TextView height = null;
    private TextView date = null;
    private static TextView summary = null;
    private Button sendButtonm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = this;
        WLClient.createInstance(this);

        setContentView(R.layout.activity_main);
        first_name = (TextView) findViewById(R.id.first_name);
        middle_name = (TextView) findViewById(R.id.middle_name);
        last_name = (TextView) findViewById(R.id.last_name);
        age = (TextView) findViewById(R.id.age);
        height = (TextView) findViewById(R.id.height);
        date = (TextView) findViewById(R.id.date);
        summary = (TextView) findViewById(R.id.summary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sendButtonm = (Button) findViewById(R.id.button_send);
        sendButtonm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Path Parameters (First Name, Middle Name and Last Name)
                    URI adapterPath = new URI("/adapters/JavaAdapter/users/"
                            + first_name.getText().toString() + "/"
                            + middle_name.getText().toString() + "/"
                            + last_name.getText().toString()
                    );

                    WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.POST);
                    // Query Parameters
                    request.setQueryParameter("age", age.getText().toString());

                    // Header Parameters
                    request.addHeader("date", date.getText().toString());

                    // Form Parameters
                    HashMap formParams = new HashMap();
                    formParams.put("height", height.getText().toString());

                    // Send
                    request.send(formParams, new WLResponseListener() {
                        public void onSuccess(WLResponse response) {
                            String responseText = response.getResponseText();
                            String resultText = "";

                            try {
                                resultText += "Name = " + response.getResponseJSON().getString("first")
                                        + " " + response.getResponseJSON().getString("middle")
                                        + " " + response.getResponseJSON().getString("last") + "\n";
                                resultText += "Age = " + response.getResponseJSON().getInt("age") + "\n";
                                resultText += "Height = " + response.getResponseJSON().getString("height") + "\n";
                                resultText += "Date = " + response.getResponseJSON().getString("Date");
                            } catch (org.json.JSONException e) {
                                _this.updateTextView(e.getMessage());
                            }

                            Log.d("InvokeSuccess", responseText);
                            _this.updateTextView(resultText);
                        }

                        public void onFailure(WLFailResponse response) {
                            //String responseText = response.getResponseText();
                            String errorMsg = response.getErrorMsg();
                            Log.d("InvokeFail", errorMsg);
                            _this.updateTextView("Failed to Invoke Adapter Procedure\n" + errorMsg);
                        }
                    });

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void updateTextView(final String str){
        Runnable run = new Runnable() {
            public void run() {
                summary.setText(str);
            }
        };
        _this.runOnUiThread(run);
    }
}
