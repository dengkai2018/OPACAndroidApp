package com.example.tabviews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OPAC extends Activity {
	
	private EditText  username=null;
	private EditText  password=null;
	private TextView attempts;
	private Button login;
	private ProgressDialog pDialog;
	int counter = 3;
	
	JSONParser jParser = new JSONParser();
	 
    ArrayList<HashMap<String, String>> productsList;
 
    // url to get all products list
    private static String url_all_products = "http://10.0.2.2/con_test/get_login_details.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NAME = "username";
    private static final String TAG_VALUE = "password";
 
    // products JSONArray
    JSONArray products = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page);
		
	    username = (EditText)findViewById(R.id.editText1);
	    password = (EditText)findViewById(R.id.editText2);
	    attempts = (TextView)findViewById(R.id.textView4);
	    attempts.setText(Integer.toString(counter));
	    login = (Button) findViewById(R.id.button1);
	    
        // button click event
        login.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new LoginActivity().execute();
            }
        });
	}
	
	class LoginActivity extends AsyncTask<String, String, String>{
		protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OPAC.this);
            pDialog.setMessage("Checking Credentials");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		protected String doInBackground(String... strings){
			String uname = username.getText().toString();
			String pass = password.getText().toString();
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_NAME, uname));
			params.add(new BasicNameValuePair(TAG_VALUE, pass));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "POST", params);
 
            // Check your log cat for JSON response
            Log.d("Response: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                		Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    	pDialog.dismiss();
                    	Bundle b = new Bundle();
                    	b.putString("user_id", uname);
                    	intent.putExtras(b);
    				    startActivity(intent);

    				
				    finish();
 
                } else{
                	counter--;
		            runOnUiThread(new Runnable() {
	            			public void run(){
	            			attempts.setText(Integer.toString(counter)+" Attempts Left");
	            			attempts.setVisibility(View.VISIBLE);
	            			if(counter==0){
	            				login.setEnabled(false);
	            			}
	            		}
		            });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
			return null;
		}
		
		protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

        }
	}
	

}
