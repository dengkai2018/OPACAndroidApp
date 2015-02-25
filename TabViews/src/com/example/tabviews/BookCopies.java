package com.example.tabviews;

import java.util.ArrayList;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class BookCopies extends Activity {
	
	String[][] data;
	int size;

    private ProgressDialog pDialog;

	JSONParser jParser = new JSONParser();
 
    private static String book_copies = "http://10.0.2.2/con_test/book_copies.php";
    JSONArray results = null;
    
    private String isbn;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ENTRY = "book";
    private static final String TAG_STATUS = "status";
    private static final String TAG_TYPE = "item_type";
    private static final String TAG_COUNT = "count";
    private static final String TAG_TITLE = "title";
    private static final String TAG_ISBN = "isbn";
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
		setContentView(R.layout.activity_book_copies);
		
		Bundle b = getIntent().getExtras();
		if(b!=null){
			isbn = b.getString(TAG_ISBN);
			Log.d("isbn",isbn);
		}
		
		TextView heading = (TextView) findViewById(R.id.bcp_details_heading);
		
		/*
		int i = 0;
		while(i<mname.length()){
			if(mname.charAt(i) == ' '){
				mname = mname.substring(0, i);
				break;
			}
		} */
		heading.setText("Copies of the book");
		
		TableRow tr_head = new TableRow(this);
		TextView accessionNo = new TextView(this);
		TextView status = new TextView(this);
		TextView type = new TextView(this);
		TextView reserve = new TextView(this);
 
		tl = (TableLayout) findViewById(R.id.bcp_main_table);
		
		tr_head.setId(10);
		tr_head.setBackgroundColor(Color.GRAY);
		tr_head.setLayoutParams(new LayoutParams(
		LayoutParams.MATCH_PARENT,
		LayoutParams.WRAP_CONTENT));
		
        accessionNo.setId(20);
        accessionNo.setText(" TITLE ");
        accessionNo.setTextColor(Color.WHITE);
        accessionNo.setPadding(7, 7, 7, 7);
        tr_head.addView(accessionNo);

        type.setId(22);
        type.setText(" ISSUE DATE ");
        type.setTextColor(Color.WHITE);
        type.setPadding(7, 7, 7, 7);
        tr_head.addView(type);
        
        status.setId(20);
        status.setText(" AUTHOR ");
        status.setTextColor(Color.WHITE);
        status.setPadding(7, 7, 7, 7);
        tr_head.addView(status);
        
        reserve.setId(23);
        reserve.setText(" DUE DATE ");
        reserve.setTextColor(Color.WHITE);
        reserve.setPadding(7, 7, 7, 7);
        tr_head.addView(reserve);// add the column to the table row here

        tl.addView(tr_head, new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
       		
		new ViewCopies().execute();
	}
	
	class ViewCopies extends AsyncTask<String, String, String>{


		@Override
		protected void onPreExecute() {
        	Log.d("FLAG","In Member Options");
        	pDialog = new ProgressDialog(BookCopies.this);
	        pDialog.setMessage("Loading Book Copies. Please wait...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(false);
	        pDialog.show();
        }    
		
		@Override
		protected String doInBackground(String... strings ){
    		List<NameValuePair> params = new ArrayList<NameValuePair>();
			 	
			params.add(new BasicNameValuePair(TAG_ISBN, isbn));
    		
            JSONObject json = jParser.makeHttpRequest(book_copies, "GET", params);
 
            Log.d("Book Copies: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    results = json.getJSONArray(TAG_ENTRY);
 
                    size = results.length();
                    data = new String[size][6];
                    int count=0;
                    String ct;
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(i);
 
                        // Storing each json item in variable
                        data[i][0] = c.getString(TAG_ANO);
                        data[i][1] = c.getString(TAG_TYPE);
                        data[i][2] = c.getString(TAG_STATUS);
                        data[i][3] = c.getString(TAG_COUNT);
                        if(data[i][3] == "null")
                        	data[i][3] = "0";
                        else
                            data[i][2] += "(" + data[i][3] + ")";
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
						int col;
				        
				       while(count<size){
				           
							TableRow tr =new TableRow(getApplicationContext());
							TextView labelAno = new TextView(getApplicationContext());
							TextView labelType = new TextView(getApplicationContext());
							TextView labelStatus = new TextView(getApplicationContext());
							Button labelReserve = new Button(getApplicationContext());
				       
				      
							if(count%2!=0) col = Color.GRAY;
							else col = Color.BLACK;
							tr.setBackgroundColor(col);
							tr.setId(100+count);
							tr.setPadding(7, 2, 7, 2);
							tr.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));

							labelAno.setId(200+count); 
							labelAno.setText(data[count][0]);
							//labelAno.setPadding(7, 2, 7, 2);
							labelAno.setTextColor(Color.WHITE);
							tr.addView(labelAno);
				       
				       
							labelType.setId(200+count);
							labelType.setText(data[count][1]);
							//labelType.setPadding(7, 2, 7, 2);
							labelType.setTextColor(Color.WHITE);
							tr.addView(labelType);
							
							

							labelStatus.setId(200+count);
							labelStatus.setText(data[count][2]);
							//labelStatus.setPadding(0, 0, 0, 0);
							labelStatus.setTextColor(Color.WHITE);
							tr.addView(labelStatus);
				      
				       
							labelReserve.setId(200+count);
							labelReserve.setPadding(0, 0, 0, 0);
							labelReserve.setText("Reserve?");
							labelReserve.setTextColor(getResources().getColor(R.color.lightGrey));
							labelReserve.setOnClickListener(new OnClickListener() {
									
								@Override
								public void onClick(View arg0) {
									Button RButton = (Button) arg0;
									int id = RButton.getId();
									Log.d("Button id",id + "");
									id = id-200;
									String queryText = data[id][2];

									if( ( queryText.toLowerCase().contains("reserved") || queryText.toLowerCase().contains("issued") && !(queryText.toLowerCase().contains("circulation")) )){
										Toast.makeText(getApplicationContext(), "You have reserved this book", Toast.LENGTH_SHORT).show();
									}
									else{
										String ttext = new String();
										if(queryText.toLowerCase().contains("available"))
											ttext = "This book is available in the library";
										else
											ttext = "This book is already reserved";
										Toast.makeText(getApplicationContext(), ttext, Toast.LENGTH_LONG).show();
									}
										
									
								}
								});
								
							
							tr.addView(labelReserve);
				      
				       
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
		