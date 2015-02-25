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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class BookPageActivity extends Activity {

	String coauthor="",publisher="HarperCollins",call_no="823.91422 R8956m",isbn="0-09-957851-4", review = "This is a very good book",rec1="Kritika",rec2="Nistha",author="Shel Silverstein",title="The Giving Tree" ;
	int no_pages=90;
	RatingBar ratingBar;
	TextView bDescription,Review_1,recommonders,btitle,bauthor;
	ImageView bookImage;
	Button recommond,seemore,writeReview,viewCopies;
	float i;
	
	String sbDescription,sReview_1,sRecommonders,sbtitle,sbauthor;
	int Rating;
	
    private static final String TAG_SUCCESS1 = "succ_book";
    private static final String TAG_SUCCESS2 = "succ_rating";
    private static final String TAG_SUCCESS3 = "succ_recomm";
    private static final String TAG_SUCCESS4 = "succ_rnr";
    private static final String TAG_KEY = "key";
    private static final String TAG_OPT = "column";
    private static final String TAG_TITLE = "title";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_COAUTHOR = "co_author";
    private static final String TAG_CNO = "call_no";
    private static final String TAG_PGS = "total_pages";
    private static final String TAG_PUB = "publisher";
    private static final String TAG_ISBN = "isbn";
    private static final String TAG_ENTRY1 = "book";
    private static final String TAG_ENTRY3 = "recomm";
    private static final String TAG_ENTRY4 = "rnr";
    private static final String TAG_REVIEWS = "review";
    private static final String TAG_RATING = "rating";
    private static final String TAG_COMMENTS = "comments";
    private static final String TAG_USERID = "user_id";
    

	String key,str1,opt;
	JSONParser jParser = new JSONParser();
	 
    ArrayList<HashMap<String, String>> booksData;
 
    // url to get all products list
    private static String book_det = "http://10.0.2.2/con_test/book_details.php";
    JSONArray results = null;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_page);
/*
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	*/	
		Bundle b = getIntent().getExtras();
		if(b!=null){
			isbn = b.getString(TAG_ISBN);
			Log.d("isbn",isbn);
		}
		ratingBar = (RatingBar)findViewById(R.id.ratingBar);
		recommond = (Button)findViewById(R.id.recommond);
		recommonders = (TextView)findViewById(R.id.recommonders);
		writeReview = (Button)findViewById(R.id.writeReview);
		btitle = (TextView)findViewById(R.id.book_name);
		bauthor = (TextView)findViewById(R.id.author_name);
		bookImage = (ImageView)findViewById(R.id.books);
		seemore = (Button)findViewById(R.id.seemore);
		viewCopies = (Button)findViewById(R.id.viewCopies);
		
		
		
		//bookDisHeading = (Button)findViewById(R.id.bookDisHeading);
		
		bDescription = (TextView)findViewById(R.id.bDescription);
		Review_1 = (TextView)findViewById(R.id.Review_1);
		
		new BookPageBGActivity().execute();
		//bDiscription.setLines(5);
		
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				/*Toast.makeText(MainActivity.this,
						String.valueOf(ratingBar.getRating()),
							Toast.LENGTH_SHORT).show();*/
			   i = ratingBar.getRating();	
			   System.out.print(i);
			}
		});
		
		viewCopies.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
        		Intent intent = new Intent(getApplicationContext(),BookCopies.class);
            	Bundle b = new Bundle();
            	b.putString("isbn", isbn);
            	intent.putExtras(b);
			    startActivity(intent);
				
				
			}
		});
			}
	
	class BookPageBGActivity extends AsyncTask<String,String,String>{
		Boolean flag = false;
		protected String doInBackground(String... strings){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair(TAG_ISBN,isbn));
			
            JSONObject json = jParser.makeHttpRequest(book_det, "GET", params);

            Log.d("All Products: ", json.toString());
            
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS1);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    results = json.getJSONArray(TAG_ENTRY1);
                    
                    flag = true;
 
                    // looping through All Products
                        JSONObject c = results.getJSONObject(0);
 
                        // Storing each json item in variable
                        sbtitle = c.getString(TAG_TITLE);
                        sbauthor = c.getString(TAG_AUTHOR);
                        String co_author = c.getString(TAG_COAUTHOR);
                        String pub = c.getString(TAG_PUB);
                        String cno = c.getString(TAG_CNO);
                        String pgs = c.getString(TAG_PGS);
                        
                        sbDescription = " Co-Author      :  " + co_author +"\n Publisher       :  " + pub + "\n Call No.          :  "+ cno + "\n ISBN NO.       :  " + isbn + "\n No Of Pages :  " + pgs;
 
                        // creating new HashMap
                        /*
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_TITLE, title);
                        map.put(TAG_AUTHOR, author);
                        map.put(TAG_COAUTHOR, co_author);
                        map.put(TAG_PUB, pub);
                        map.put(TAG_CNO, cno);
                        map.put(TAG_PGS, pgs);
                        */
 
                        // adding HashList to ArrayList
                        //booksData.add(map);
                        
                } else {
                	flag = false;
                }

                success = json.getInt(TAG_SUCCESS2);
                if(success==1){
                Rating = json.getInt(TAG_RATING);
                //HashMap<String, String> map = new HashMap<String, String>();
                //map.put(TAG_RATING,""+rating);
                //booksData.add(map);
                }
                
                
                success = json.getInt(TAG_SUCCESS3);
                if(success==1){

                
                results = json.getJSONArray(TAG_ENTRY3);
                sRecommonders = "";
                for (int i = 0; i < results.length(); i++) {
                    JSONObject c = results.getJSONObject(i);

                    // Storing each json item in variable
                    String recommendation = c.getString(TAG_USERID);
                    sRecommonders = sRecommonders + " " + recommendation +"\n";

                    // creating new HashMap
                    /*
                    if(i==results.length()-1){
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_USERID, recommendation);
                    
                    // adding HashList to ArrayList
                    booksData.add(map);
                    */
                    }
                    
                }
                

                success = json.getInt(TAG_SUCCESS4);
                if(success==1){
                	results = json.getJSONArray(TAG_ENTRY4);
                	sReview_1 = "";
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(i);

                        // Storing each json item in variable
                        String uid = c.getString(TAG_USERID);
                        String rev = c.getString(TAG_REVIEWS);
                        
                        sReview_1 = sReview_1 + " " + uid + " says:\n \t" + rev + "\n\n";
                    	
                    }
                	
                }
                

                
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
 
            

			return null;
		}
		
		protected void onPostExecute(String file_url){
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {	
					bDescription.setText(sbDescription);
					Review_1.setText(sReview_1);
					recommonders.setText(sRecommonders);
					btitle.setText(sbtitle);
					bauthor.setText(sbauthor);
					ratingBar.setRating(Rating);
					String bookimage = getBookPhoto(isbn);
					int did = getResources().getIdentifier("com.example.tabviews:drawable/"+bookimage, null, null);
					bookImage.setImageResource(did);
					Log.d("Image String",bookimage);
					
				}
			});
			
		}
	}
		 
	
	 
	public String getBookPhoto(String s)
	  {
		String a = "i";
	    s = s.replaceAll("-", "");
	    s = s.replaceAll("=", "");
	    s=a+s;
	    return(s);
	  }

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
			
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_book_page, container,
					false);
			return rootView;
		}
	}

}

