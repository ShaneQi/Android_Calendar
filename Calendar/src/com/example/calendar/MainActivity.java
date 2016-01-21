/**
 * The methods setupTutorialButtons(), setupNavigationButton(), and setupTestFixture() are not
 * utilized in the final working version of this calendar application.
 * */


package com.example.calendar;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;	//for starting new activities
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;		//tut
import android.widget.Button;	//part of tut
import android.widget.TextView;	//tut

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class MainActivity extends ActionBarActivity {

	private int counter;	//vars part of tutorial
	Button add, sub;		
	TextView display;
	
	private Globals global;
	
	/**Sets up the activity for immediate use; sets content view, but this activity is intentionally never displayed.
	 * Sets the app's selected date to the current date and time.
	 * @param savedInstanceState used for overridden method call
	 * **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // res/layout/activity_main.xml
        
        global = (Globals)getApplicationContext();
        global.setSelectedDate(Calendar.getInstance().getTime());	//set selectedDate to today's date
        global.setAppIsStarting(true);	//the app is starting right now, this is the only time onCreate is called for this activity
        
        //setupTutorialButtons();
        //setupTestFixture();	//comment out once the IO is working
        //setupNavigationButton();
    }

    /**Handles all of the File I/O for saving categories and events.
	 * **/
    @Override
    protected void onResume(){
    	super.onResume();
    	
    	String 	categoryFileName = "categoryFile",
    			eventFileName = "eventFile";
    	
    	boolean appIsStarting = global.getAppIsStarting();
    	
    	if(appIsStarting){
    		System.out.println("APP IS STARTING NOW");
    		//read from files
    		try{	//Read in categories
    			FileInputStream readCategoryStream = openFileInput(categoryFileName);
    			ObjectInputStream catInStream = new ObjectInputStream(readCategoryStream);
    			System.out.println("Opened teh input streams successfully");
    			
    			//READ IN CATEGORIES FROM FILE
    			ArrayList<Category> catList; 
    			catList = (ArrayList<Category>) catInStream.readObject();
    			boolean listNeedsUpdate = false;
    			int i = 0;
    			ArrayList<Category> currentCatList = EventManager.getCategories();
    			for(Category c: catList){
    				if( i >= currentCatList.size()){
    					listNeedsUpdate = true;
    					System.out.println("File's catList is longer than the currentCatList");
    					break;
    				}
    				System.out.println("index i:" + c + " " + currentCatList.get(i));
    				if(!c.equals(currentCatList.get(i))){
    					listNeedsUpdate = true;
    					System.out.println("Unlike categories found at index" + i + ", needs update");
    					break;
    				}
    				i++;
    			}
    			//The list needs updating, this check stops the app from doubling its list whenever reopening from running in the background
    			if(listNeedsUpdate){
    				for(Category ca : catList){
        				EventManager.addCategory(ca);
        			}
    			}
    			
    			System.out.println("Closing input streams");
    			catInStream.close();
    			readCategoryStream.close();
    			
    		}catch(Exception e){
    			//File not found, this means app is running first time -> must make category file and setup default category
    			//This also means an empty event file must be created as well!
    			try{
    				System.out.println("CREATING THE NEW FILE FOR CATEGORIES");
    				FileOutputStream createCatFile = openFileOutput(categoryFileName, Context.MODE_PRIVATE);//this creates the catFile
    				ObjectOutputStream catOutStream = new ObjectOutputStream(createCatFile);
    				System.out.println("CREATING THE NEW FILE FOR EVENTS");
    				FileOutputStream createEventFile = openFileOutput(eventFileName, Context.MODE_PRIVATE);//this creates the eventFile
    				ObjectOutputStream eventOutStream = new ObjectOutputStream(createEventFile);
    				
    				Category defaultCategory = new Category("Default Category");
    				EventManager.addCategory(defaultCategory);
    				catOutStream.writeObject(EventManager.getCategories());	//write the whole arraylists for future extraction
    				eventOutStream.writeObject(EventManager.getEvents());	//empty eventList at this time
    				
    				catOutStream.close();
    				createCatFile.close();
    			}catch(Exception outputException){
    				outputException.printStackTrace();
    			}
    		}
    		
    		try{	//This block runs even on the first opening, but it does not matter b/c the eventFile is already created above
    			FileInputStream readEventStream = openFileInput(eventFileName);
    			ObjectInputStream eventInStream = new ObjectInputStream(readEventStream);
    			
    			ArrayList<Event> eventList;
    			eventList = (ArrayList<Event>) eventInStream.readObject();
    			boolean eventListNeedsUpdate = false;
    			int i = 0;
    			ArrayList<Event> currentEventList = EventManager.getEvents();
    			for(Event e: eventList){
    				if( i >= currentEventList.size()){
    					eventListNeedsUpdate = true;
    					System.out.println("File's eventList is longer than the currentEventList");
    					break;
    				}
    				System.out.println("index i:" + e.getName() + " " + currentEventList.get(i).getName());
    				if(!e.equals(currentEventList.get(i))){
    					eventListNeedsUpdate = true;
    					System.out.println("Unlike events found at index" + i + ", needs update");
    					break;
    				}
    				i++;
    			}
    			//The event list needs updating, this check stops the app from doubling its list whenever reopening from running in the background
    			if(eventListNeedsUpdate){
    				for(Event ev : eventList){
        				EventManager.addEvent(ev);
        			}
    			}
    			
    			//System.out.println(EventManager.getEvents());
    		}catch(Exception e){
    			System.out.println("NO EVENTFILE FOUND");
    		}
    		
    		global.setAppIsStarting(false); //set this flag to false, the app is no longer starting up
    		//go to monthly view
    		startActivity(new Intent(MainActivity.this, MonthlyViewActivity.class));	
    	}else{
    		//write to files (app is closing now)
    		try{
				FileOutputStream writeToCatFile = openFileOutput(categoryFileName, Context.MODE_PRIVATE);
				ObjectOutputStream catOutStream = new ObjectOutputStream(writeToCatFile);
				FileOutputStream writeToEventFile = openFileOutput(eventFileName, Context.MODE_PRIVATE);
				ObjectOutputStream eventOutStream = new ObjectOutputStream(writeToEventFile);		
				System.out.println("Opened final output streams");
				
				catOutStream.writeObject(EventManager.getCategories());
				System.out.println("Saved categories");
				eventOutStream.writeObject(EventManager.getEvents());
				System.out.println("Saved events");
				
				System.out.println("Closing final output streams");
				eventOutStream.close();
				writeToEventFile.close();
				catOutStream.close();
				writeToCatFile.close();
				
    		}catch(Exception writingException){
    			writingException.printStackTrace();
    		}
    		
    		
    		System.out.println("APP IS CLOSING NOW");
    		finish();
    	}
    } 
    
    /**UNUSED METHOD - Not used in final application version.
	 * **/
    private void setupTutorialButtons(){
        counter = 0;
        add = (Button) findViewById(R.id.buttonAdd);	//Button is subclass of TextView
        sub = (Button) findViewById(R.id.buttonSub);	//Typecasting View to Button
        display = (TextView) findViewById(R.id.tvDisplay);
        add.setOnClickListener(new View.OnClickListener(){
       		public void onClick(View v){
       			counter++;
       			display.setText("Your total is " + counter);
        	}
        });
        sub.setOnClickListener(new View.OnClickListener(){
       		public void onClick(View v){
       			counter--;
       			display.setText("Your total is " + counter);
        	}
        });
    }
    
    /**UNUSED METHOD - Not used in final application version.
	 * **/
    private void setupNavigationButton(){
    	Button toDailyView = (Button) findViewById(R.id.buttonToDailyView);
    	toDailyView.setOnClickListener(new View.OnClickListener(){
    		@Override
    		public void onClick(View v){
    			startActivity(new Intent(MainActivity.this, DailyViewActivity.class));
    			//Learned from https://www.youtube.com/watch?v=hB3AqKy8QME
    		}
    	});
    }
    
    /**UNUSED METHOD - Not used in final application version.
	 * **/
    private void setupTestFixture(){
    	Category defaultCat = new Category("Default Category");
    	Category cat1 = new Category("Category juan", 0x12345679);
    	EventManager.addCategory(defaultCat);
    	EventManager.addCategory(cat1);	//need to make it to where events aren't added without adding the category to EM
    	EventManager.addCategory(new Category("ROASTIN BRUH", 0x55555555));
    	EventManager.addEvent(new Event("Hey man, I start work at 1:10 AM?", new GregorianCalendar(2015, 10, 6, 1, 10), new GregorianCalendar(2015, 10, 6, 2, 10), defaultCat));
    	EventManager.addEvent(new Event("Ayy waddup breh?", new GregorianCalendar(2015, 10, 7, 2, 10), new GregorianCalendar(2015, 10, 7, 2, 10), defaultCat));
    	EventManager.addEvent(new Event("third event :(", new GregorianCalendar(2015, 10, 14, 1, 10), new GregorianCalendar(2015, 10, 15, 12, 10), cat1));
    	EventManager.addEvent(new Event("what you wa\nnt", new GregorianCalendar(2015, 10, 6, 5, 10), new GregorianCalendar(2015, 10, 6, 7, 10), cat1));
    //	defaultCat.setColor(0x99420690);
    }

    /**UNUSED METHOD - Not used in final application version.
	 * **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.global_menu, menu);
        return true;
    }

    /**UNUSED METHOD - Not used in final application version.
	 * **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
        	case R.id.menu_option_category_manager:
        		startActivity(new Intent(this, CategoryManagerActivity.class));
        		break;
        }
        return false;
        //return super.onOptionsItemSelected(item);
    }
}
