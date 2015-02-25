package com.example.tabviews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    ViewPager mViewPager;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_KEY = "key";
    private static final String TAG_OPT = "column";
    private static final String TAG_TITLE = "title";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_PUB = "publisher";
    private static final String TAG_ISBN = "isbn";
    private static final String TAG_ENTRY1 = "book";
    private static final String TAG_ANO = "acc_no";
    private static final String TAG_CNO = "call_no";
    private static final String TAG_IDATE = "issue_date";
    private static final String TAG_DDATE = "due_date";
    private static final String TAG_RDATE = "return_date";
    private static final String TAG_ITYPE = "item_type";
    private static final String TAG_SUC_ISSUE = "succ_issue";
    private static final String TAG_SUC_TRANS = "succ_trans";
    private static final String TAG_ENT_ISSUE = "issue";
    private static final String TAG_ENT_TRANS = "trans";
    private static final String TAG_USERID = "user_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_MEMBER = "member";
    private static final String TAG_MESSAGE = "message";
    
    
    static String user_id;
    
    static String iacc = "Accession No: ";
    static String tacc = "Accession No: ";
    static String icalno = "Call No: ";
    static String ititle = "Title: ";
    static String iauthor = "Author: ";
    static String iissue = "Issue Date: ";
    static String idue = "Due Date: ";
    static String itype = "Item Type: ";
    static String tcalno = "Call No: ";
    static String ttitle = "Title: ";
    static String tauthor = "Author: ";
    static String tissue = "Issue Date: ";
    static String tdue = "Due Date: ";
    static String ttype = "Item Type: ";
    static String tret = "Return Date: ";
    
    
    private ProgressDialog pDialog;
    
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> productsList;
 
    // url to get all products list
    private static String search_a_book = "http://10.0.2.2/con_test/search_a_book.php";
    private static String creat_home_page = "http://10.0.2.2/con_test/home_page.php";
    private static String member_options = "http://10.0.2.2/con_test/member_options.php";
    
    JSONArray values = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle b = getIntent().getExtras();
		if(b!=null){
			user_id = b.getString(TAG_USERID);
			Log.d(TAG_USERID,user_id);
		}
		else
			user_id = "y11uc242";

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);
        

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }
    

	@Override
	public void onTabReselected(Tab arg0, android.app.FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(Tab arg0, android.app.FragmentTransaction arg1) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(arg0.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, android.app.FragmentTransaction arg1) {
	}


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentStatePagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
            case 0: return new HPSectionFragment();
            	
                case 1:
                    return new BookSectionFragment();
                    
                case 2: return new MemberSectionFragment();

                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
            case 0: return "HOME PAGE";
            case 1: return "BOOKS";
            case 2: return "MEMBERS";
            default: return "";
            }
        }
        
        @Override
        public int getItemPosition(Object object){
        	return POSITION_NONE;
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class BookSectionFragment extends Fragment {
    	
    	int i ;
    	Spinner searchopt;
    	Button search;
    	EditText  keyword=null,Result=null;
    	
    	View rootview;
    	
    	static String str1,key,opt,isbn = "";
    	private ProgressDialog pDialog;
    	JSONParser jParser = new JSONParser();
   	 
        static ArrayList<HashMap<String, String>> booksList;
     
        // url to get all products list
        JSONArray results = null;
    	
     

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	rootview = inflater.inflate(R.layout.fragment_home_page, container,false);
        	searchopt = (Spinner) rootview.findViewById(R.id.searchopt);
            search = (Button) rootview.findViewById(R.id.search);
            keyword = (EditText) rootview.findViewById(R.id.keyword);
            search.setOnClickListener(new View.OnClickListener() {	
            	@Override
    			public void onClick(View v) {
            		//BookOptionFragment bog = new BookOptionFragment();
            		//Bundle b = new Bundle();
            		key = keyword.getText().toString();
            		str1 = searchopt.getSelectedItem().toString();
            		
            		new SearchActivity().execute();
            		
            		/*
            		b.putString(TAG_KEY, keyword.getText().toString());
            		b.putString(TAG_OPT, searchopt.getSelectedItem().toString());
            		bog.sendArgs(b);
            		
            		
            		
            		FragmentTransaction trans = getFragmentManager().beginTransaction();
            		trans.replace(((ViewGroup)getView().getParent()).getId(), bog);
            		//trans.add(R.id.book_page_fragment, bog);
            		//bog.addList();
            		//bog.setArguments(b);
            		trans.addToBackStack(null);
            		trans.commit();
            		
            		*/
    			}
            });
            
            
            
            return rootview;
        }
        
        class SearchActivity extends AsyncTask<String, String, String>{
        	
            Boolean flag = false;
            
            
            protected void onPreExecute() {
            	Log.d("FLAG","In SearchActivity");
                booksList = new ArrayList<HashMap<String, String>>();
            	/*
                super.onPreExecute();
                pDialog = new ProgressDialog(getActivity().getApplicationContext());
                pDialog.setMessage("Loading book. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show(); */
            }    
            
    	protected String doInBackground(String... strings ){
    		List<NameValuePair> params = new ArrayList<NameValuePair>();
			 	
			 	if(str1.equals("Call No."))
			 	{
			 		opt="call_no"; 
			 	}
			 	else if(str1.equals("Title"))
			 	{
			 		opt="title"; 
			 		
			 	}
			 	else if(str1.equals("Author"))
			 	{
			 		opt="author";
			 	}
			 	
			 	params.add(new BasicNameValuePair(TAG_OPT, opt));
			 	params.add(new BasicNameValuePair(TAG_KEY, key));
    		
            JSONObject json = jParser.makeHttpRequest(search_a_book, "GET", params);
 
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    results = json.getJSONArray(TAG_ENTRY1);
                    
                    flag = true;
 
                    // looping through All Products
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(i);
 
                        // Storing each json item in variable
                        String title = c.getString(TAG_TITLE);
                        String author = c.getString(TAG_AUTHOR);
                        isbn = c.getString(TAG_ISBN);
                        
                        title = title + " by " + author;
 
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_ISBN, isbn);
                        map.put(TAG_TITLE, title);
 
                        // adding HashList to ArrayList
                        booksList.add(map);
                        
                    }
                } else {
                	flag = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
    		
    		
    		/*
    		str = keyword.getText().toString();
			 	str1 = searchopt.getSelectedItem().toString();
			 	
			 	if(str1.equals("Call No."))
			 	{
			 		if(str.equals("823.91422 R8956m"))
			 			flag=true; 
			 	}
			 	else if(str1.equals("Title"))
			 	{
			 		if(str.equals("The Giving Tree"))
			 			flag=true; 
			 		
			 	}
			 	else if(str1.equals("Author"))
			 	{
			 		if(str.equals("Shel Silverstein"))
			 			flag=true; 
			 		
			 	}
			 	else if(str1.equals("Accession No."))
			 	{
			 		if(str.equals("1146"))
			 			flag=true; 
			 		
			 	}
			 	if(flag == true){
			 			Intent intent = new Intent(getActivity().getApplicationContext(),BookPageActivity.class);
			 			startActivity(intent);
			 		} 
    		
    		return null;
    		*/
    	}
    	
    	protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
    		//pDialog.dismiss();
			Log.d("FLAG","In postExecute");
			//pDialog.dismiss();
            	getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Log.d("FLAG","In thread");
						if(!isbn.equals("")){
							new BookOptionsDialog().show(getActivity().getSupportFragmentManager(), null);
							
							
							/*
							Intent intent = new Intent(getActivity().getApplicationContext(),BookPageActivity.class);
							Bundle b = new Bundle();
							b.putString(TAG_ISBN, isbn);
							intent.putExtras(b);
							startActivity(intent);
							*/
						}
						else{
							//getFragmentManager().popBackStack();
							Toast toast = Toast.makeText(getActivity().getApplicationContext(), "No such book exists", Toast.LENGTH_LONG);
							toast.show();
						}
					}
				});
        }
    	
    }
    
    public static class BookOptionsDialog extends DialogFragment {
    	@Override
    	public Dialog onCreateDialog(Bundle savedInstanceState){
    		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    		LayoutInflater inflater = getActivity().getLayoutInflater();
    		View v = inflater.inflate(R.layout.book_options_dialog, null);
    		builder.setView(v);
    		
    		
    		ListView listview = (ListView) v.findViewById(R.id.bo_list);
    		listview.setTextFilterEnabled(true);
    		listview.setOnItemClickListener(new OnItemClickListener() {
    			@Override
				public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
    				String isbnSelected = ((TextView) view.findViewById(R.id.bo_dialog_key)).getText().toString();
    				 
	                // Starting new intent
	                Intent in = new Intent(getActivity().getApplicationContext(),
	                        BookPageActivity.class);
	                // sending pid to next activity
	                Bundle b = new Bundle();
					b.putString(TAG_ISBN, isbnSelected);
					in.putExtras(b);
	                startActivity(in);
    			}
    			
			});
    		
    		listview.setAdapter(new SimpleAdapter(
                    getActivity(), 
                    booksList,
                    R.layout.book_options_list_item, 
                    new String[] { TAG_ISBN, TAG_TITLE},
                    new int[] { R.id.bo_dialog_key, R.id.bo_dialog_desc }));
    		
    		
    		/*
    		builder.setTitle(R.string.book_options_dialog_title);
    		builder.setAdapter(
    				new SimpleAdapter(
    						getActivity(), 
    						booksList, 
    						R.layout.book_options_list_item, 
    						new String[] { TAG_ISBN,
                                    TAG_TITLE},
                            new int[] { R.id.bo_dialog_key, R.id.bo_dialog_desc }),
                     new OnItemClickListener() {
						
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
			                    int position, long id) {
							
							
						}
					});
					
					*/
    		
    		return builder.create();
    	}
    }

    }
    
    public static class BookOptionFragment extends ListFragment{
    	private ProgressDialog pDialog;
    	String key,str1,opt,isbn = "";
    	JSONParser jParser = new JSONParser();
   	 
        static ArrayList<HashMap<String, String>> booksList;
     
        // url to get all products list
        JSONArray results = null;
        
        public void sendArgs(Bundle args){
        	Log.d("FLAG","bypass");
        	if(args != null ){
        		Log.d("FLAG","Gooooottt them ahahahahaha");
        	key = args.getString(TAG_KEY);
        	str1 = args.getString(TAG_OPT);
        	Log.d("key",key);
        	Log.d("opt",str1);
        	}
        }
        
        @Override
        public void onCreate(Bundle savedInstanceState){
        	super.onCreate(savedInstanceState);
        	
        }
        
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
        	Log.d("In bog","now");
        	View rootView = inflater.inflate(R.layout.display_list, container,false);
        	booksList = new ArrayList<HashMap<String, String>>();

        	Log.d("FLAG","Now receiving values:bog");
        	Log.d("FLAG","values received:bog");
        	
        	new SearchActivity().execute();
        	  
        	return rootView;
        }
        
        
        class SearchActivity extends AsyncTask<String, String, String>{
        	
	            Boolean flag = false;
	            
	            
	            protected void onPreExecute() {
	            	Log.d("FLAG","In SearchActivity");/*
	                super.onPreExecute();
	                pDialog = new ProgressDialog(getActivity().getApplicationContext());
	                pDialog.setMessage("Loading book. Please wait...");
	                pDialog.setIndeterminate(false);
	                pDialog.setCancelable(true);
	                pDialog.show(); */
	            }    
	            
        	protected String doInBackground(String... strings ){
        		List<NameValuePair> params = new ArrayList<NameValuePair>();
   			 	
   			 	if(str1.equals("Call No."))
   			 	{
   			 		opt="call_no"; 
   			 	}
   			 	else if(str1.equals("Title"))
   			 	{
   			 		opt="title"; 
   			 		
   			 	}
   			 	else if(str1.equals("Author"))
   			 	{
   			 		opt="author";
   			 	}
   			 	
   			 	params.add(new BasicNameValuePair(TAG_OPT, opt));
   			 	params.add(new BasicNameValuePair(TAG_KEY, key));
        		
                JSONObject json = jParser.makeHttpRequest(search_a_book, "GET", params);
     
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
     
                try {
                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);
     
                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        results = json.getJSONArray(TAG_ENTRY1);
                        
                        flag = true;
     
                        // looping through All Products
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject c = results.getJSONObject(i);
     
                            // Storing each json item in variable
                            String title = c.getString(TAG_TITLE);
                            String author = c.getString(TAG_AUTHOR);
                            isbn = c.getString(TAG_ISBN);
                            
                            title = title + " by " + author;
     
                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();
     
                            // adding each child node to HashMap key => value
                            map.put(TAG_ISBN, isbn);
                            map.put(TAG_TITLE, title);
     
                            // adding HashList to ArrayList
                            booksList.add(map);
                            
                        }
                    } else {
                    	flag = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
     
                return null;
        		
        		
        		/*
        		str = keyword.getText().toString();
   			 	str1 = searchopt.getSelectedItem().toString();
   			 	
   			 	if(str1.equals("Call No."))
   			 	{
   			 		if(str.equals("823.91422 R8956m"))
   			 			flag=true; 
   			 	}
   			 	else if(str1.equals("Title"))
   			 	{
   			 		if(str.equals("The Giving Tree"))
   			 			flag=true; 
   			 		
   			 	}
   			 	else if(str1.equals("Author"))
   			 	{
   			 		if(str.equals("Shel Silverstein"))
   			 			flag=true; 
   			 		
   			 	}
   			 	else if(str1.equals("Accession No."))
   			 	{
   			 		if(str.equals("1146"))
   			 			flag=true; 
   			 		
   			 	}
   			 	if(flag == true){
   			 			Intent intent = new Intent(getActivity().getApplicationContext(),BookPageActivity.class);
   			 			startActivity(intent);
   			 		} 
        		
        		return null;
        		*/
        	}
        	
        	protected void onPostExecute(String file_url) {
                // dismiss the dialog once done
        		//pDialog.dismiss();
				Log.d("FLAG","In postExecute");
				//pDialog.dismiss();
                	getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Log.d("FLAG","In thread");
							if(!isbn.equals("")){
								new BookOptionsDialog().show(getActivity().getSupportFragmentManager(), null);
								
								
								/*
								Intent intent = new Intent(getActivity().getApplicationContext(),BookPageActivity.class);
								Bundle b = new Bundle();
								b.putString(TAG_ISBN, isbn);
								intent.putExtras(b);
								startActivity(intent);
								*/
							}
							else{
								getFragmentManager().popBackStack();
								Toast toast = Toast.makeText(getActivity().getApplicationContext(), "No such book exists", Toast.LENGTH_LONG);
								toast.show();
							}
						}
					});
            }
        	
        }
        
        public static class BookOptionsDialog extends DialogFragment {
        	@Override
        	public Dialog onCreateDialog(Bundle savedInstanceState){
        		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        		LayoutInflater inflater = getActivity().getLayoutInflater();
        		View v = inflater.inflate(R.layout.book_options_dialog, null);
        		builder.setView(v);
        		
        		
        		ListView listview = (ListView) v.findViewById(R.id.bo_list);
        		listview.setTextFilterEnabled(true);
        		listview.setOnItemClickListener(new OnItemClickListener() {
        			@Override
    				public void onItemClick(AdapterView<?> parent, View view,
    	                    int position, long id) {
        				String isbnSelected = ((TextView) view.findViewById(R.id.bo_dialog_key)).getText().toString();
        				 
    	                // Starting new intent
    	                Intent in = new Intent(getActivity().getApplicationContext(),
    	                        BookPageActivity.class);
    	                // sending pid to next activity
    	                Bundle b = new Bundle();
    	                
    	               // HashMap<String, String> map = new H
    	                
    					b.putString(TAG_ISBN, isbnSelected);
    					in.putExtras(b);
    	                startActivity(in);
        			}
        			
				});
        		
        		listview.setAdapter(new SimpleAdapter(
                        getActivity(), 
                        booksList,
                        R.layout.book_options_list_item, 
                        new String[] { TAG_ISBN, TAG_TITLE},
                        new int[] { R.id.bo_dialog_key, R.id.bo_dialog_desc }));
        		
        		
        		/*
        		builder.setTitle(R.string.book_options_dialog_title);
        		builder.setAdapter(
        				new SimpleAdapter(
        						getActivity(), 
        						booksList, 
        						R.layout.book_options_list_item, 
        						new String[] { TAG_ISBN,
	                                    TAG_TITLE},
	                            new int[] { R.id.bo_dialog_key, R.id.bo_dialog_desc }),
	                     new OnItemClickListener() {
							
							@Override
							public void onItemClick(AdapterView<?> parent, View view,
				                    int position, long id) {
								
								
							}
						});
						
						*/
        		
        		return builder.create();
        	}
        }
        
        }
    
    

    

    public static class HPSectionFragment extends Fragment {
    	private ProgressDialog pDialog;
    	JSONParser jParser = new JSONParser();
   	 
        ArrayList<HashMap<String, String>> booksList;
        
        TextView accNo,calno,title,author,idate,ddate,type,accNot,calnot,titlet,authort,idatet,ddatet,typet,rdate,m,mt;
     
        // url to get all products list
        private static String search_a_book = "http://10.0.2.2/con_test/search_a_book.php";
        JSONArray results = null;
    	
    	public void onStart(){
    		super.onStart();
    		initFrag();
    	}
    	
    		
    	private void initFrag(){
    		//booksList = new ArrayList<HashMap<String, String>>();
    		

            TableLayout tblLayout = (TableLayout) getView().findViewById(R.id.tblLayout);

                ScrollView sc= (ScrollView) getView().findViewById(R.id.sc);
               final HorizontalScrollView HSV = new HorizontalScrollView(getActivity().getApplicationContext());
                HSV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

                TableRow tblRow = new TableRow(getActivity().getApplicationContext());
                tblRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 
                		LayoutParams.WRAP_CONTENT));
                
                tblRow.setBackgroundResource(R.drawable.bookshelf);
               	tblRow.setPadding(30,45,30 , 20);
               	tblRow.setGravity(Gravity.TOP);
               	

              	//new HPSQLActivity().execute();
              	
                ImageView iv1 = new ImageView(getActivity().getApplicationContext());
                iv1.setImageResource(R.drawable.harry2);
                iv1.setMaxHeight(500);
                ImageView iv2 = new ImageView(getActivity().getApplicationContext());
                iv2.setImageResource(R.drawable.harry3);
                iv1.setMaxHeight(500);
                ImageView iv3 = new ImageView(getActivity().getApplicationContext());
                iv1.setMaxHeight(500);
                iv3.setImageResource(R.drawable.harry4);
                ImageView iv4 = new ImageView(getActivity().getApplicationContext());
                iv1.setMaxHeight(500);
                iv4.setImageResource(R.drawable.cn);
                ImageView iv5 = new ImageView(getActivity().getApplicationContext());
                iv1.setMaxHeight(500);
                iv5.setImageResource(R.drawable.dta);
                
                
                tblRow.addView(iv1);
                tblRow.addView(iv2);
                tblRow.addView(iv3);
                tblRow.addView(iv4);
                tblRow.addView(iv5);
                
                ImageView iv6 = new ImageView(getActivity().getApplicationContext());
                iv6.setImageResource(R.drawable.nnn);
                tblLayout.addView(iv6);
              
                 HSV.addView(tblRow);
                tblLayout.addView(HSV);
                
                ImageButton al = (ImageButton) getView().findViewById(R.id.al);
                al.setBackgroundColor(Color.TRANSPARENT);
        		al.setOnClickListener(new OnClickListener() {
         
        			@Override
        			public void onClick(View arg0) {
         
        			  HSV.fullScroll(View.FOCUS_LEFT);
         
        			}
             		        
            
            });
               
                
                ImageButton ar = (ImageButton) getView().findViewById(R.id.ar);
                ar.setBackgroundColor(Color.TRANSPARENT);
                
                ar.setOnClickListener(new OnClickListener() {
                    
        			@Override
        			public void onClick(View arg0) {
         
        			  HSV.fullScroll(View.FOCUS_RIGHT);
         
        			}
             		        
            
            });
                
               TableLayout Is=(TableLayout) getView().findViewById(R.id.is);
               TableRow tr1=new TableRow(getActivity().getApplicationContext());
               tr1.setGravity(Gravity.CENTER_HORIZONTAL);
               tr1.setPadding(5, 5, 5,5);
               Is.setBackgroundResource(R.drawable.back);
               TextView it=new TextView (getActivity().getApplicationContext());
               it.setText("Issue Details");
               it.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
               it.setTextColor(Color.parseColor("#966F33"));
               
               
               TableRow tr2=new TableRow(getActivity().getApplicationContext());
               tr2.setGravity(Gravity.CENTER_HORIZONTAL);
               tr2.setPadding(3, 3, 3,3);
               TableRow tr3=new TableRow(getActivity().getApplicationContext());
               tr3.setGravity(Gravity.CENTER_HORIZONTAL);
               tr3.setPadding(3, 3, 3,3);
               TableRow tr4=new TableRow(getActivity().getApplicationContext());
               tr4.setGravity(Gravity.CENTER_HORIZONTAL);
               tr4.setPadding(3, 3, 3,3);
               TableRow tr5=new TableRow(getActivity().getApplicationContext());
               tr5.setGravity(Gravity.CENTER_HORIZONTAL);
               tr5.setPadding(3, 3, 3,3);
               TableRow tr6=new TableRow(getActivity().getApplicationContext());
               tr6.setGravity(Gravity.CENTER_HORIZONTAL);
               tr6.setPadding(3, 3, 3,3);
               TableRow tr7=new TableRow(getActivity().getApplicationContext());
               tr7.setGravity(Gravity.CENTER_HORIZONTAL);
               tr7.setPadding(3, 3, 3,3);
               TableRow tr8=new TableRow(getActivity().getApplicationContext());
               tr8.setGravity(Gravity.CENTER_HORIZONTAL);
               tr8.setPadding(3, 3, 3,3);
              

               
               accNo= new TextView(getActivity().getApplicationContext());
               calno=  new TextView(getActivity().getApplicationContext());
               title=  new TextView(getActivity().getApplicationContext());
               author=  new TextView(getActivity().getApplicationContext());
               idate=  new TextView(getActivity().getApplicationContext());
               ddate=  new TextView(getActivity().getApplicationContext());
               type=  new TextView(getActivity().getApplicationContext());  
               m=new TextView(getActivity().getApplicationContext());
               
               //accNo.setText("Acession No : 11327");
               accNo.setPadding(3, 3, 3, 3);
               accNo.setTextColor(Color.parseColor("#AF9B60"));
               
               //calno.setText("Call No : 004.6 f769c");
               calno.setPadding(3, 3, 3, 3);
               calno.setTextColor(Color.parseColor("#AF9B60"));
               
               //title.setText("Title: Computer Networks");
               title.setPadding(3, 3, 3, 3);
               title.setTextColor(Color.parseColor("#AF9B60"));

               //author.setText("Author: Behrouz A. Furouzan");
               author.setPadding(3, 3, 3, 3);
               author.setTextColor(Color.parseColor("#AF9B60"));
               
               //idate.setText("Issued on: 2014-04-02");
               idate.setPadding(3, 3, 3, 3);
               idate.setTextColor(Color.parseColor("#AF9B60"));
               
               //ddate.setText("Due Date : 2014-04-19");
               ddate.setPadding(3, 3, 3, 3);
               ddate.setTextColor(Color.parseColor("#AF9B60"));
               
               //type.setText("Type: Circulation");
               type.setPadding(3, 3, 3, 3);
               type.setTextColor(Color.parseColor("#AF9B60"));
               
               
               m.setText("More....");
               m.setPadding(3, 3, 3, 3);
               m.setTextColor(Color.parseColor("#AF9B60"));
               m.setOnClickListener(new OnClickListener() {
			        @Override
			        public void onClick(View v) {
			            Intent intent = new Intent (getActivity().getApplicationContext(), IssueActivity.class);
		                Bundle b = new Bundle();
						b.putString(TAG_USERID, user_id);
						intent.putExtras(b);
			            startActivity(intent);
			             }
			    });
               
               Is.addView(tr1);
               tr1.addView(it);
               Is.addView(tr2);
               tr2.addView(accNo);
               Is.addView(tr3);
               tr3.addView(calno);
               Is.addView(tr4);
               tr4.addView(title);
               Is.addView(tr5);
               tr5.addView(idate);
               Is.addView(tr6);
               tr6.addView(ddate);
               Is.addView(tr7);
               tr7.addView(type);
               Is.addView(tr8);
               tr8.addView(m);
              
               
               TableLayout Ts=(TableLayout) getView().findViewById(R.id.ts);
               TableRow ts1=new TableRow(getActivity().getApplicationContext());
               ts1.setGravity(Gravity.CENTER_HORIZONTAL);
               ts1.setPadding(5, 5, 5,5);
               Ts.setBackgroundResource(R.drawable.back);
               TextView tt=new TextView (getActivity().getApplicationContext());
               tt.setText("Transaction Details");
               tt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
               tt.setTextColor(Color.parseColor("#966F33"));
               
               
               TableRow ts2=new TableRow(getActivity().getApplicationContext());
               ts2.setGravity(Gravity.CENTER_HORIZONTAL);
               ts2.setPadding(3, 3, 3,3);
               TableRow ts3=new TableRow(getActivity().getApplicationContext());
               ts3.setGravity(Gravity.CENTER_HORIZONTAL);
               ts3.setPadding(3, 3, 3,3);
               TableRow ts4=new TableRow(getActivity().getApplicationContext());
               ts4.setGravity(Gravity.CENTER_HORIZONTAL);
               ts4.setPadding(3, 3, 3,3);
               TableRow ts5=new TableRow(getActivity().getApplicationContext());
               ts5.setGravity(Gravity.CENTER_HORIZONTAL);
               ts5.setPadding(3, 3, 3,3);
               TableRow ts6=new TableRow(getActivity().getApplicationContext());
               ts6.setGravity(Gravity.CENTER_HORIZONTAL);
               ts6.setPadding(3, 3, 3,3);
               TableRow ts7=new TableRow(getActivity().getApplicationContext());
               ts7.setGravity(Gravity.CENTER_HORIZONTAL);
               tr7.setPadding(3, 3, 3,3);
               TableRow ts8=new TableRow(getActivity().getApplicationContext());
               ts8.setGravity(Gravity.CENTER_HORIZONTAL);
               ts8.setPadding(3, 3, 3,3);
               TableRow ts9=new TableRow(getActivity().getApplicationContext());
               ts9.setGravity(Gravity.CENTER_HORIZONTAL);
               ts9.setPadding(3, 3, 3,3);
               

               accNot= new TextView(getActivity().getApplicationContext());
               calnot=  new TextView(getActivity().getApplicationContext());
               titlet=  new TextView(getActivity().getApplicationContext());
               authort=  new TextView(getActivity().getApplicationContext());
               idatet=  new TextView(getActivity().getApplicationContext());
               ddatet=  new TextView(getActivity().getApplicationContext());
               typet=  new TextView(getActivity().getApplicationContext());  
               rdate=  new TextView(getActivity().getApplicationContext());  
               mt=new TextView(getActivity().getApplicationContext());
              
               accNot.setText("Acession No : 40");
               accNot.setPadding(3, 3, 3, 3);
               accNot.setTextColor(Color.parseColor("#AF9B60"));

               calnot.setText("Call No : 823.914 r797h");
               calnot.setPadding(3, 3, 3, 3);
               calnot.setTextColor(Color.parseColor("#AF9B60"));
               
               titlet.setText("Title: Harry Potter and the Prisoner of Azkaban");
               titlet.setPadding(3, 3, 3, 3);
               titlet.setTextColor(Color.parseColor("#AF9B60"));

               authort.setText("Author: J.K.Rowling");
               authort.setPadding(3, 3, 3, 3);
               authort.setTextColor(Color.parseColor("#AF9B60"));
               
               idatet.setText("Issued on: 2014-01-02");
               idatet.setPadding(3, 3, 3, 3);
               idatet.setTextColor(Color.parseColor("#AF9B60"));
               
               ddatet.setText("Due Date: 2014-01-17");
               ddatet.setPadding(3, 3, 3, 3);
               ddatet.setTextColor(Color.parseColor("#AF9B60"));
               
               typet.setText("Type: Circulation");
               typet.setPadding(3, 3, 3, 3);
               typet.setTextColor(Color.parseColor("#AF9B60")); 
                        
             rdate.setText("Return Date: 2014-02-07 16:00:55");
             rdate.setPadding(3, 3, 3, 3);
             rdate.setTextColor(Color.parseColor("#AF9B60"));
               
               
             mt.setText("More....");
             mt.setPadding(3, 3, 3, 3);
             mt.setTextColor(Color.parseColor("#AF9B60"));
             mt.setOnClickListener(new OnClickListener() {
			        @Override
			        public void onClick(View v) {
			            Intent intent = new Intent (getActivity().getApplicationContext(), TransActivity.class);
		                Bundle b = new Bundle();
						b.putString(TAG_USERID, user_id);
						intent.putExtras(b);
			            startActivity(intent);
			             }
			    });
               
               
               Ts.addView(ts1);
               ts1.addView(tt);
               Ts.addView(ts2);
               ts2.addView(accNot);
               Ts.addView(ts3);
               ts3.addView(calnot);
               Ts.addView(ts4);
               ts4.addView(titlet);
               Ts.addView(ts5);
               ts5.addView(idatet);
               Ts.addView(ts6);
               ts6.addView(ddatet);
               Ts.addView(ts7);
               ts7.addView(typet);
               Ts.addView(ts9);
               ts9.addView(rdate);
               Ts.addView(ts8);
               ts8.addView(mt);/*
				accNo.setText(iacc);
				calno.setText(icalno);
				title.setText(ititle);
				author.setText(iauthor);
				idate.setText(iissue);
				ddate.setText(idue);
				type.setText(itype);
				accNot.setText(tacc);
				calnot.setText(tcalno);
				titlet.setText(ttitle);
				authort.setText(tauthor);
				idatet.setText(tissue);
				ddatet.setText(tdue);
				typet.setText(ttype);
				rdate.setText(tret);
              
                       
                   
                           */
               
               new HPSQLActivity().execute();
                }
    	class HPSQLActivity extends AsyncTask<String, String, String>{
        	
            Boolean flag = false;
            
            
            protected String doInBackground(String... strings ){
        		List<NameValuePair> params = new ArrayList<NameValuePair>();
        		Log.d(TAG_USERID,user_id);
        		params.add(new BasicNameValuePair(TAG_USERID, user_id));
        		
                JSONObject json = jParser.makeHttpRequest(creat_home_page, "POST", params);
     
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
     
                try {
                    int success = json.getInt(TAG_SUC_TRANS);
     
                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        results = json.getJSONArray(TAG_ENT_TRANS);
                        
                        flag = true;
     
                        // looping through All Products
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject c = results.getJSONObject(i);
     
                            if(i==results.length()-1){
                            // Storing each json item in variable
                            tacc += c.getString(TAG_ANO);
                            tcalno += c.getString(TAG_CNO);
                            ttitle += c.getString(TAG_TITLE);
                            tauthor += c.getString(TAG_AUTHOR);
                            tissue += c.getString(TAG_IDATE);
                            tdue += c.getString(TAG_DDATE);
                            tret += c.getString(TAG_RDATE);
                            ttype += c.getString(TAG_ITYPE);
                            Log.d("Values",ttype);
                            
                            
                            	
                            }
                        }
                    }
     
                    // Checking for SUCCESS TAG
                    success = json.getInt(TAG_SUC_ISSUE);
                    
                    Log.d(TAG_SUC_ISSUE,"" + success);
     
                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        results = json.getJSONArray(TAG_ENT_ISSUE);
                        
                        flag = true;
     
                        Log.d("results.length","" + results.length());
                        // looping through All Products
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject c = results.getJSONObject(i);
     
                            Log.d("i",""+i);
                            if(i==results.length()-1){
                            // Storing each json item in variable
                            iacc += c.getString(TAG_ANO);
                            icalno += c.getString(TAG_CNO);
                            ititle += c.getString(TAG_TITLE);
                            iauthor += c.getString(TAG_AUTHOR);
                            iissue += c.getString(TAG_IDATE);
                            idue += c.getString(TAG_DDATE);
                            itype += c.getString(TAG_ITYPE);
                            
                            
                            	
                            }
                            
                        }
                    }                       
                        
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
     
        		return null;
            }
            
            protected void onPostExecute(String file_url) {
                // dismiss the dialog once done
        		//pDialog.dismiss();
				Log.d("FLAG","In postExecute");
				//pDialog.dismiss();
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if(iacc.equals("Accession No: ")){
							accNo.setText("No Accession Details");
							m.setText("");
						}
						else{
						accNo.setText(iacc);
						calno.setText(icalno);
						title.setText(ititle);
						author.setText(iauthor);
						idate.setText(iissue);
						ddate.setText(idue);
						type.setText(itype);
						}
						if(tacc.equals("Accession No: ")){
							accNot.setText("No Accession Details");
							mt.setText("");
						}
						else{
						accNot.setText(tacc);
						calnot.setText(tcalno);
						titlet.setText(ttitle);
						authort.setText(tauthor);
						idatet.setText(tissue);
						ddatet.setText(tdue);
						typet.setText(ttype);
						rdate.setText(tret);
						}
						
					}
				}); 
										
            }
            
    	}
    	
    

    		
    	
    	
    	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
    		View rootview = inflater.inflate(R.layout.fragment_section_hp, container,false);
    		setRetainInstance(true);
        	    		return rootview;
    	}
    	
    	
    }
    
    public static class MemberSectionFragment extends ListFragment{
    	JSONParser jParser = new JSONParser();
        ArrayList<HashMap<String, String>> members_List;
   	 
        
        JSONArray results = null;
        
        Boolean test = false;
		static String msearch_id;
    
    	
    	public void onStart(){
    		super.onStart();
    		

   		 	addKeyListener();
   		 
    		ListView listview = getListView();
    		listview.setTextFilterEnabled(true);
    		
    		listview.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
					String uid = ((TextView) view.findViewById(R.id.user_id)).getText().toString();
	 
	                // Starting new intent
	                Intent in = new Intent(getActivity().getApplicationContext(),
	                        SearchMemberActivity.class);
	                // sending pid to next activity
	                Bundle b = new Bundle();
					b.putString(TAG_USERID, uid);
					in.putExtras(b);
	                startActivity(in);
	 
	                // starting new activity and expecting some response back
	                //startActivityForResult(in, 100);
					
				}
			});
    		
    		
    	}
    	
    	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
    		View rootview = inflater.inflate(R.layout.fragment_section_member, container,false);
    
        	    		return rootview;
    	}
    	
    	
    	public void onActivityCreated(Bundle savedInstanceState){
    		super.onActivityCreated(savedInstanceState);
    		
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
     Log.d("Success before",success + "");
                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        results = json.getJSONArray(TAG_MEMBER);
     
                        for (int i = 0; i < results.length(); i++) {
   Log.d("loop i","" + i);
                            JSONObject c = results.getJSONObject(i);
     
                            // Storing each json item in variable
                            String uid = c.getString(TAG_USERID);
                            String mname = c.getString(TAG_NAME);
                            mname = mname + " (" + uid + ")";
     Log.d("member values",mname);
                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();
     Log.d("flag","1");
                            // adding each child node to HashMap key => value
                            map.put(TAG_USERID, uid);
                            map.put(TAG_NAME, mname);
   Log.d("map",map.toString());
                            // adding HashList to ArrayList
                            members_List.add(map);
    Log.d("End of loop i ",i+"");
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
				
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						setListAdapter(new SimpleAdapter(
	                            getActivity(), members_List,
	                            R.layout.list_item, new String[] { TAG_USERID,
	                                    TAG_NAME},
	                            new int[] { R.id.user_id, R.id.name }));
					}
				});
				
			}
    		
    	}
    	
    	 public void addKeyListener()
         {
              
             // get edittext component
             final EditText edittext = (EditText)getView(). findViewById(R.id.searchMem);
             
            
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
    	
    	
    	
    	
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

	

}




