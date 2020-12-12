package com.example.connectionhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    private ConnectionHelper connectionClass;
    private boolean success = false;
    Button btn;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         btn = (Button) findViewById(R.id.button);
         tv = (TextView) findViewById(R.id.textView);

        connectionClass = new ConnectionHelper(); // Connection Class Initialization

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                SyncData orderData = new SyncData();
                orderData.execute("");

            }
        });
    }

    private class SyncData extends AsyncTask<String, String, String>
    {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error,See Android Monitor in the bottom For details!";
        ProgressDialog progress;
        @Override
        protected void onPreExecute() //Starts the progress dailog
        {

        }
        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try
            {
                Connection conn = connectionClass.CONN(); //Connection Object
                if (conn == null)
                {
                    success = false;
                }
                else {
                    // Change below query according to your own database.
                        if (conn!=null){
                            Statement statement = null;
                            try {
                                statement = conn.createStatement();
                                ResultSet resultSet = statement.executeQuery("Select Name from fetch_data;");
                                while (resultSet.next()){
                                    tv.setText(resultSet.getString(1)

                                    );

                                }
                            }
                            catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            tv.setText("Connection is null");

                        }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }
        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my listview
        {

            Toast toast=Toast. makeText(getApplicationContext(),"data fetched from MS SQL!",Toast. LENGTH_SHORT);
            toast. setMargin(50,50);
            toast. show();
        }
    }
    
    
}