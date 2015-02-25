package com.example.tabviews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.tabviews.MainActivity.BookOptionFragment.SearchActivity;
import com.example.tabviews.MainActivity.MemberSectionFragment.MemberOptions;





public class SearchMemberActivity extends ListActivity {
	
	TextView name, uid, dept, group, interest;
	
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_TITLE = "title";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_ENT_MEMBER = "member";
    private static final String TAG_ANO = "acc_no";
    private static final String TAG_CNO = "call_no";
    private static final String TAG_IDATE = "issue_date";
    private static final String TAG_DDATE = "due_date";
    private static final String TAG_ITYPE = "item_type";
    private static final String TAG_SUC_ISSUE = "succ_issue";
    private static final String TAG_ENT_ISSUE = "issue";
    private static final String TAG_USERID = "user_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_MEMBER = "member";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUC_MEMBER = "succ_mem";
    private static final String TAG_SUC_RECOMM = "succ_recomm";
    private static final String TAG_SUC_RNR = "succ_rnr";
    private static final String TAG_ENT_RECOMM = "recomm";
    private static final String TAG_ENT_RNR = "rnr";
    private static final String TAG_REVIEW = "review";
    private static final String TAG_DEPT = "dept";
    private static final String TAG_GROUP_NAME = "group_name";
    
    private ProgressDialog pDialog;

	JSONParser jParser = new JSONParser();
	 
    ArrayList<HashMap<String, String>> members_List;
 
    private static String search_a_member = "http://10.0.2.2/con_test/search_a_member.php";
    private static String member_options = "http://10.0.2.2/con_test/member_options.php";
    JSONArray results = null;
    
    private String user_id,mname, mgroup_name, mdept, AccNo = "", Calno, Title, Author, Idate, Ddate, Type, RnR="", Recomm="";
	
    TableLayout tb,tb2,tb3,tb4;
    TableRow tblRow1,tblRow2,tblRow3,tblRow4,tblRow5;
    
    private static String msearch_id;
    
    public void addKeyListener()
    {
         
        // get edittext component
        final EditText edittext = (EditText) findViewById(R.id.searchMem);
        
       
        // add a keylistener to keep track user input
        edittext.setOnKeyListener(new OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
     
            // if keydown and "enter" is pressed
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                && (keyCode == KeyEvent.KEYCODE_ENTER)) {
           	 msearch_id = edittext.getText().toString();
           	 Log.d("Search member",msearch_id);
           	 new MemberOptions().execute();
           	 
           	return true;
               
            }
     
            return false;
        };
     });
    }
    
    class MemberOptions extends AsyncTask<String, String, String>{


		@Override
		protected void onPreExecute() {
        	Log.d("FLAG","In Member Options");
        	members_List = new ArrayList<HashMap<String, String>>();
        }    
		
		@Override
		protected String doInBackground(String... strings ){
    		List<NameValuePair> params = new ArrayList<NameValuePair>();
			 	
			 	params.add(new BasicNameValuePair(TAG_USERID, msearch_id));
    		
            JSONObject json = jParser.makeHttpRequest(member_options, "GET", params);
 
            Log.d("All Members: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    results = json.getJSONArray(TAG_MEMBER);
 
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(i);
 
                        // Storing each json item in variable
                        String uid = c.getString(TAG_USERID);
                        String mname = c.getString(TAG_NAME);
                        mname = mname + " (" + uid + ")";
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put(TAG_USERID, uid);
                        map.put(TAG_NAME, mname);
                        // adding HashList to ArrayList
                        members_List.add(map);
                    }
                }
                else{
                	if(json.getString(TAG_MESSAGE) == "Required field(s) is missing"){
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_USERID, null);
                    map.put(TAG_NAME, "Please type something");
                	}
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
			return null;
		}
		
		protected void onPostExecute(String file_url) {
			Log.d("FLAG","In postExecute");
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					setListAdapter(new SimpleAdapter(
                            SearchMemberActivity.this, members_List,
                            R.layout.memberpage_list_item, new String[] { TAG_USERID,
                                    TAG_NAME},
                            new int[] { R.id.mp_user_id, R.id.mp_name }));
				}
			});
			
		}
		
	}

	
    
    
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_member);
		Bundle b = getIntent().getExtras();
		if(b!=null){
			user_id = b.getString(TAG_USERID);
			Log.d("user_id",user_id);
		}
		

		addKeyListener();
		 
		ListView listview = getListView();
		listview.setTextFilterEnabled(true);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
 
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        SearchMemberActivity.class);
                // sending pid to next activity
                Bundle b = new Bundle();
				b.putString(TAG_USERID, msearch_id);
				in.putExtras(b);
                startActivity(in); 
				//finish(); 
                // starting new activity and expecting some response back
                //startActivityForResult(in, 100);
				
			}
		});
		
		

		
		ScrollView sc= (ScrollView) findViewById(R.id.sc);
		//sc.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

	tb= (TableLayout) findViewById(R.id.table);
	tb.setBackgroundColor(Color.WHITE);
	tb2= (TableLayout) findViewById(R.id.table2);
	tb2.setBackgroundColor(Color.WHITE);
	
	tb3= (TableLayout) findViewById(R.id.table3);
	tb3.setBackgroundColor(Color.WHITE);
	
	tb4= (TableLayout) findViewById(R.id.table4);
	tb4.setBackgroundColor(Color.WHITE);
	
	tblRow1 = new TableRow(this);
	
	
    tblRow2 = new TableRow(this);
    tblRow3 =new TableRow(this);
    
    tblRow4 = new TableRow(this);
    
    tblRow5 =new TableRow(this);
    
    
   
    new MemberDisplayActivity().execute();
 /*
    
    name= new TextView(this);
    uid=  new TextView(this);
    dept=  new TextView(this);
    group=  new TextView(this);
    
    name.setText("Name: Kritika Agarwal");
    name.setPadding(3, 3, 3, 3);
    name.setGravity(Gravity.LEFT| Gravity.TOP);
    name.setBackgroundColor(Color.WHITE);
    
    
    uid.setText("Roll No: Y11uc123");
    uid.setPadding(3, 3, 3, 3);
    uid.setGravity(Gravity.CENTER_VERTICAL | Gravity.TOP);
   
    
    dept.setText("Dept: CCE");
    dept.setPadding(3, 3, 3, 3);
    dept.setGravity(Gravity.CENTER_VERTICAL| Gravity.TOP);
   
   
    
    group.setText("Group: ug");
    group.setPadding(3, 3, 3, 3);
    group.setGravity(Gravity.CENTER_VERTICAL| Gravity.TOP);
    group.setWidth(500);
   

   
   tb.addView(tblRow1);
   tb.addView(tblRow2);
   tb.addView(tblRow3);
   tb.addView(tblRow4);
   
	tblRow1.addView(name);
	tblRow2.addView(uid);
	tblRow3.addView(group);
	tblRow4.addView(dept);
	
	
    TableRow tb2Row1 = new TableRow(this);
    TableRow tb2Row2 = new TableRow(this);
    TableRow tb2Row3 =new TableRow(this);
    TableRow tb2Row4 = new TableRow(this);
    TableRow tb2Row5 =new TableRow(this);
    TableRow tb2Row6 =new TableRow(this);
    TableRow tb2Row7 =new TableRow(this);
    TableRow tb2Row8 =new TableRow(this);
    TableRow tt2=new TableRow(this);
   
  
  TextView head= new TextView(this);
  TextView accNo= new TextView(this);
  TextView calno=  new TextView(this);
  TextView title=  new TextView(this);
  TextView author=  new TextView(this);
  TextView idate=  new TextView(this);
  TextView rdate=  new TextView(this);
  TextView type=  new TextView(this);  
  TextView m=new TextView(this);
  
  accNo.setText("Acession No : 111222");
  accNo.setPadding(3, 3, 3, 3);
  
  calno.setText("Call No : 11111");
  calno.setPadding(3, 3, 3, 3);
  
  title.setText("Title: Harry Potter");
  title.setPadding(3, 3, 3, 3);

  author.setText("Author: J.K.Rowling");
  author.setPadding(3, 3, 3, 3);
  
  idate.setText("Issued on: 2014-1-12");
  idate.setPadding(3, 3, 3, 3);
  
  rdate.setText("Returned on: 2014-1-13");
  rdate.setPadding(3, 3, 3, 3);
  
  type.setText("Type: Book");
  type.setPadding(3, 3, 3, 3);
  
  head.setText("Issue details:");
  head.setPadding(3, 3, 3, 3);
  
  m.setText("More....");
  m.setPadding(3, 3, 3, 3);
 
  m.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
          Intent i1 = new Intent (getApplicationContext(), IssueActivity.class);
          startActivity(i1);
          finish();
           }
  }); 
  
  tb2.addView(tt2);	
  tb2.addView(tb2Row1);
  tb2.addView(tb2Row2);
  tb2.addView(tb2Row3);
  tb2.addView(tb2Row4);
  tb2.addView(tb2Row5);	  
  tb2.addView(tb2Row6);	  
  tb2.addView(tb2Row7);	  
  tb2.addView(tb2Row8);

  tt2.addView(head);
  tb2Row1.addView(accNo); 
  tb2Row2.addView(calno);
  tb2Row3.addView(title);
  tb2Row4.addView(author);
  tb2Row5.addView(idate);
  tb2Row6.addView(rdate);
  tb2Row7.addView(type);
  tb2Row8.addView(m);
  
  TableRow tb3Row1 = new TableRow(this);
  TableRow tb3Row2 = new TableRow(this);
  
  
   TextView rate=new TextView(this);
        TextView rnr=new TextView(this);
        
    rate.setText("Ratings & Reviews:"); 
   rate.setPadding(3,3,3,3);
   
   rnr.setText("Harry Potter and Prisoner of Azkaban:\n Best. Book. Ever");
   rnr.setPadding(3,3,3,3);
   
   tb3.addView(tb3Row1);
   tb3.addView(tb3Row2);
   
   tb3Row1.addView(rate);
   tb3Row2.addView(rnr);
   

   TableRow tb4Row1 = new TableRow(this);
   TableRow tb4Row2 = new TableRow(this);
   
   
    TextView rec=new TextView(this);
         TextView rect=new TextView(this);
         
     rec.setText("Recommendations:"); 
    rec.setPadding(3,3,3,3);
    
    rect.setText("Harry Potter and Prisoner of Azkaban");
    rect.setPadding(3,3,3,3);
    
    tb4.addView(tb4Row1);
    tb4.addView(tb4Row2);
    
    tb4Row1.addView(rec);
    tb4Row2.addView(rect);

   
		  */

}
	
	class MemberDisplayActivity extends AsyncTask<String, String, String>{
		
		Boolean flag = false,flag_acc = false;
		
		@Override
	    protected void onPreExecute() {
	         super.onPreExecute();
	         Log.d("Flag","In PreExecute - MemberDisplay");
	         pDialog = new ProgressDialog(SearchMemberActivity.this);
	         pDialog.setMessage("Loading Member Details. Please wait...");
	         pDialog.setIndeterminate(false);
	         pDialog.setCancelable(false);
	         pDialog.show();
	    }
        
        
        protected String doInBackground(String... strings ){
    		List<NameValuePair> params = new ArrayList<NameValuePair>();
    		Log.d(TAG_USERID,user_id);
    		params.add(new BasicNameValuePair(TAG_USERID, user_id));
    		
            JSONObject json = jParser.makeHttpRequest(search_a_member, "POST", params);
 
            // Check your log cat for JSON reponse
            Log.d("Member Details: ", json.toString());
 
            try {
                int success = json.getInt(TAG_SUC_MEMBER);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    results = json.getJSONArray(TAG_ENT_MEMBER);
                    
                    flag = true;
 
                    // looping through All Products
                    JSONObject c = results.getJSONObject(0);
 
                        // Storing each json item in variable
                    mname = c.getString(TAG_NAME);
                    mdept = c.getString(TAG_DEPT);
                    mgroup_name = c.getString(TAG_GROUP_NAME);
                    Log.d("Values",mname);
                        
                        
                }
 
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUC_ISSUE);
                Log.d(TAG_SUC_ISSUE,"" + success);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    results = json.getJSONArray(TAG_ENT_ISSUE);
                    
                    flag_acc = true;
 
                    Log.d("results.length","" + results.length());
                    // looping through All Products
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(i);
 
                        Log.d("i",""+i);
                        if(i==results.length()-1){
                        // Storing each json item in variable
                        AccNo = c.getString(TAG_ANO);
                        Calno = c.getString(TAG_CNO);
                        Title = c.getString(TAG_TITLE);
                        Author = c.getString(TAG_AUTHOR);
                        Idate = c.getString(TAG_IDATE);
                        Ddate = c.getString(TAG_DDATE);
                        Type = c.getString(TAG_ITYPE);
                        }
                    }
                }
                
                success = json.getInt(TAG_SUC_RECOMM);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    results = json.getJSONArray(TAG_ENT_RECOMM);
                    

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(i);
                        
                        Recomm = Recomm + "\t" + c.getString(TAG_TITLE) + "\n";
                    }
                }
                else
                	Recomm = "No Recommendations made yet.";
                
                success = json.getInt(TAG_SUC_RNR);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    results = json.getJSONArray(TAG_ENT_RNR);
                    

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(i);
                        
                        RnR = RnR + "\t" + c.getString(TAG_TITLE) + "\n";
                        RnR = RnR + "\t\t" + c.getString(TAG_REVIEW) + "\n\n";
                    }
                }
                else
                	RnR = "No Reviews written yet";
                    
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
    		return null;
        }
        
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
    		//pDialog.dismiss();
			Log.d("FLAG","In postExecute");
			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					TextView name=  new TextView(getApplicationContext());
				    
				    name.setText("Name: " + mname + "\nRoll No: " + user_id + "\nDepartment: " + mdept + "\nGroup Name: " + mgroup_name);
				    name.setPadding(3, 3, 3, 3);
				    name.setGravity(Gravity.LEFT| Gravity.TOP);
				    name.setBackgroundColor(Color.WHITE);
				    		    
				    tb.addView(tblRow1);
				    
				 	tblRow1.addView(name);
				 	
				 					 	
				    TableRow tb2Row1 = new TableRow(getApplicationContext());
				    TableRow tb2Row2 = new TableRow(getApplicationContext());
				    TableRow tt2=new TableRow(getApplicationContext());
				    
				   
				    TextView head= new TextView(getApplicationContext());
				    TextView accNo= new TextView(getApplicationContext());
				    TextView m=new TextView(getApplicationContext());
				    

				    head.setText("Issue details:");
				    head.setPadding(3, 3, 3, 3);
				    
				    if(flag_acc == true){
				    accNo.setText("Acession No: " + AccNo + 
				    		"\nCall No: " + Calno + 
				    		"\nTitle: " + Title + 
				    		"\nAuthor: " + Author + 
				    		"\nIssued on: " + Idate + 
				    		"\nDue Date: " + Ddate + 
				    		"Type: " + Type);
				    m.setText("More....");
				    m.setPadding(3, 3, 3, 3);
				    m.setOnClickListener(new OnClickListener() {
				        @Override
				        public void onClick(View v) {
				            Intent intent = new Intent (getApplicationContext(), IssueActivity.class);
			                Bundle b = new Bundle();
							b.putString(TAG_USERID, user_id);
							intent.putExtras(b);
				            startActivity(intent);
				             }
				    }); 
				    }
				    else
				    	accNo.setText("No accession Details");
				    accNo.setPadding(3, 3, 3, 3);
				    
				    //accNo.setWidth(getResources().getDimension(R.dimen.table_width));
				    
				    
				    
				    tb2.addView(tt2);	
				    tb2.addView(tb2Row1); 

				    tt2.addView(head);
				    tb2Row1.addView(accNo);
				    
				    if(flag_acc == true){
				    	tb2.addView(tb2Row2);
				    	tb2Row2.addView(m);
				    }
				    
				    TableRow tb3Row1 = new TableRow(getApplicationContext());
				    TableRow tb3Row2 = new TableRow(getApplicationContext());
				    
				    
				     TextView rate=new TextView(getApplicationContext());
				          TextView rnr=new TextView(getApplicationContext());
				          
				     rate.setText("Ratings & Reviews:"); 
				     rate.setPadding(3,3,3,3);
				     
				     rnr.setText(RnR);
				     rnr.setPadding(3,3,3,3);
				     
				     tb3.addView(tb3Row1);
				     tb3.addView(tb3Row2);
				     
				     tb3Row1.addView(rate);
				     tb3Row2.addView(rnr);
				     

				     TableRow tb4Row1 = new TableRow(getApplicationContext());
				     TableRow tb4Row2 = new TableRow(getApplicationContext());
				     
				     
				      TextView rec=new TextView(getApplicationContext());
				      TextView rect=new TextView(getApplicationContext());
				           
				      rec.setText("Recommendations:"); 
				      rec.setPadding(3,3,3,3);
				      
				      rect.setText(Recomm);
				      rect.setPadding(3,3,3,3);
				      
				      tb4.addView(tb4Row1);
				      tb4.addView(tb4Row2);
				      
				      tb4Row1.addView(rec);
				      tb4Row2.addView(rect);
				    
				   
				    

				}
			}); 
									
        }
	}
	
}

	
	
	
