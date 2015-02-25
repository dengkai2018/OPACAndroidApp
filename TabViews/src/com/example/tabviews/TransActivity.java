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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;

public class TransActivity extends Activity {
	
	String[][] data;
	int size;

    private ProgressDialog pDialog;

	JSONParser jParser = new JSONParser();
	 
    ArrayList<HashMap<String, String>> members_List;
 
    private static String view_transaction = "http://10.0.2.2/con_test/view_transaction.php";
    JSONArray results = null;
    
    private String user_id,mname;
    
    private static final String TAG_TITLE = "title";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_ANO = "acc_no";
    private static final String TAG_CNO = "call_no";
    private static final String TAG_IDATE = "issue_date";
    private static final String TAG_DDATE = "due_date";
    private static final String TAG_RDATE = "return_date";
    private static final String TAG_SUC_TRANS = "succ_trans";
    private static final String TAG_ENT_TRANS = "trans";
    private static final String TAG_USERID = "user_id";
    private static final String TAG_NAME = "name";
    

	TableLayout tl;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trans);
		
		Bundle b = getIntent().getExtras();
		if(b!=null){
			user_id = b.getString(TAG_USERID);
			Log.d("user_id",user_id);
		}
		
		TextView heading = (TextView) findViewById(R.id.t_details_heading);
		
		/*
		int i = 0;
		while(i<mname.length()){
			if(mname.charAt(i) == ' '){
				mname = mname.substring(0, i);
				break;
			}
		} */
		heading.setText("Transaction Details:");
		
		TableRow tr_head = new TableRow(this);
		TextView title = new TextView(this);
		TextView author = new TextView(this);
		TextView AccessionNo = new TextView(this);
		TextView idate = new TextView(this);
		TextView ddate = new TextView(this);
		TextView rdate = new TextView(this);
 
		tl = (TableLayout) findViewById(R.id.t_main_table);
		
		tr_head.setId(10);
		tr_head.setBackgroundColor(Color.GRAY);
		tr_head.setLayoutParams(new LayoutParams(
		LayoutParams.MATCH_PARENT,
		LayoutParams.WRAP_CONTENT));
		
		
		
        title.setId(20);
        title.setText(" TITLE ");
        title.setTextColor(Color.WHITE);
        title.setPadding(7, 7, 7, 7);
        tr_head.addView(title);
        
        author.setId(20);
        author.setText(" AUTHOR ");
        author.setTextColor(Color.WHITE);
        author.setPadding(7, 7, 7, 7);
        tr_head.addView(author);

        
        AccessionNo.setId(21);
        AccessionNo.setText(" ACCESSION NO. "); 
        AccessionNo.setTextColor(Color.WHITE); 
        AccessionNo.setPadding(7, 7, 7, 7); 
        tr_head.addView(AccessionNo); 
        
        
        idate.setId(22);
        idate.setText(" ISSUE DATE ");
        idate.setTextColor(Color.WHITE);
        idate.setPadding(7, 7, 7, 7);
        tr_head.addView(idate);
        
        
        ddate.setId(23);
        ddate.setText(" DUE DATE ");
        ddate.setTextColor(Color.WHITE);
        ddate.setPadding(7, 7, 7, 7);
        tr_head.addView(ddate);// add the column to the table row here


        rdate.setId(22);
        rdate.setText(" RETURN DATE-TIME ");
        rdate.setTextColor(Color.WHITE);
        rdate.setPadding(7, 7, 7, 7);
        tr_head.addView(rdate);
        tl.addView(tr_head, new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
       		
		new ViewTrans().execute();
	}
	
	class ViewTrans extends AsyncTask<String, String, String>{


		@Override
		protected void onPreExecute() {
        	Log.d("FLAG","In Member Options");
        	pDialog = new ProgressDialog(TransActivity.this);
	        pDialog.setMessage("Loading Member Details. Please wait...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(false);
	        pDialog.show();
        }    
		
		@Override
		protected String doInBackground(String... strings ){
    		List<NameValuePair> params = new ArrayList<NameValuePair>();
			 	
			params.add(new BasicNameValuePair(TAG_USERID, user_id));
    		
            JSONObject json = jParser.makeHttpRequest(view_transaction, "GET", params);
 
            Log.d("Issue Details: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUC_TRANS);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    results = json.getJSONArray(TAG_ENT_TRANS);
 
                    size = results.length();
                    data = new String[size][6];
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(i);
 
                        // Storing each json item in variable
                        data[i][0] = c.getString(TAG_TITLE);
                        data[i][1] = c.getString(TAG_AUTHOR);
                        data[i][2] = c.getString(TAG_ANO);
                        data[i][3] = c.getString(TAG_IDATE);
                        data[i][4] = c.getString(TAG_DDATE);
                        data[i][5] = c.getString(TAG_RDATE);
                    }
                }
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
			return null;
		}
		
		protected void onPostExecute(String file_url) {
			Log.d("FLAG","In postExecute");
			pDialog.dismiss();
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					   Integer count=0;
				        
				       while(count<size){
				           
							TableRow tr =new TableRow(getApplicationContext());
							TextView labeltitle = new TextView(getApplicationContext());
							TextView labelauthor = new TextView(getApplicationContext());
							TextView labelano = new TextView(getApplicationContext());
							TextView labelidate = new TextView(getApplicationContext());
							TextView labelddate = new TextView(getApplicationContext());
							TextView labelrdate = new TextView(getApplicationContext());
				       
				      
							if(count%2!=0) tr.setBackgroundColor(Color.GRAY);
							else tr.setBackgroundColor(Color.BLACK);
							tr.setId(100+count);
							tr.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));

				       
				      
							labeltitle.setId(200+count); 
							labeltitle.setText(data[count][0]);
							labeltitle.setPadding(7, 2, 7, 2);
							labeltitle.setTextColor(Color.WHITE);
							tr.addView(labeltitle);
				       
				       
							labelauthor.setId(200+count);
							labelauthor.setText(data[count][1]);
							labelauthor.setPadding(7, 2, 7, 2);
							labelauthor.setTextColor(Color.WHITE);
							tr.addView(labelauthor);
							

							labelano.setId(200+count);
							labelano.setText(data[count][2]);
							labelano.setPadding(7, 2, 7, 2);
							labelano.setTextColor(Color.WHITE);
							tr.addView(labelano);
				      
				       
							labelidate.setId(200+count);
							labelidate.setPadding(7, 2, 7, 2);
							labelidate.setText(data[count][3]);
							labelidate.setTextColor(Color.WHITE);
							tr.addView(labelidate);
				      
				       
							labelddate.setId(200+count);
							labelddate.setText(data[count][4]);
							labelddate.setPadding(7, 2, 7, 2);
							labelddate.setTextColor(Color.WHITE);
							tr.addView(labelddate);
							
							labelrdate.setId(200+count);
							labelrdate.setText(data[count][5]);
							labelrdate.setPadding(7, 2, 7, 2);
							labelrdate.setTextColor(Color.WHITE);
							tr.addView(labelrdate);

							// finally add this to the table row
							tl.addView(tr, new TableLayout.LayoutParams(
				       	                    LayoutParams.MATCH_PARENT,
				       	                    LayoutParams.WRAP_CONTENT));
				       		count++;
				       	}

				}
			});
			
		}
		
	}

	
	}
		