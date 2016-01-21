package com.example.calendar;

import java.text.SimpleDateFormat;
import java.util.*;	//Date, Calendar, & GregorianCalendar

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;	
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;	
import android.widget.TextView;	
import android.widget.RelativeLayout;


public class DailyViewActivity extends AppCompatActivity { //ActionBarActivity //CompatActivity

	private RelativeLayout dailyLayout;
	private Globals global;
	private ArrayList<Button> eventButtonList = new ArrayList<Button>();
	private Date selectedDate;
	
	/**Sets the content view accordingly. Assigns appropriate values to the
	 * dailyLayout and global variables for future use.
	 * @param savedInstanceState used for super class constructor call
	 * **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_view);	//really should be R.layout.activity_daily_view according to eclipse
        
        dailyLayout = (RelativeLayout) findViewById(R.id.ScrollingLayout);
        global = (Globals)getApplicationContext();
    }
    
    /**Sets up and displays all of the event buttons for the displayed date. Fetches all the 
     * required events by using EventManager.getEvents(Date,Date) method and sets up the layout parameters
     * for each event button using the global preferences (setupEventButtonFormat method) to retain consistent
     * look and feel. Also calls methods inside this class to do the rest of the setup for the individual buttons.
	 * **/
    private void displayEvents(){
    	//possibly utilize clipping? clip_vertical in RelativeLayout api
    	
    	Date startOfSelectedDate = new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate());
    	Date endOfSelectedDate = new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate()+1);	//should be midnight (24 hours after start)
    	
    	ArrayList<Event> eventsToDisplay = EventManager.getEvents(startOfSelectedDate, endOfSelectedDate);	//Fetches list of all events starting in selectedDate
    	for(Event e: eventsToDisplay){

    		Button eventButton = new Button(this);

    		RelativeLayout.LayoutParams eventButtonParams = getEventButtonParams(e);	
    		global.setupEventButtonFormat(eventButton, e);
    		eventButton.setLayoutParams(eventButtonParams);
    		setupEventButtonListener(eventButton, e);
    		
    		dailyLayout.addView(eventButton);	//PUT THE BUTTON IN THE DAILYVIEW LAYOUT
    		eventButtonList.add(eventButton);	//store the button, so it may be deleted during refreshes
    	}
    }

    /**Creates the appropriate layout parameters needed for the event's button to line up 
     * relatively to the hour markers on the left side of the screen.
     * @param event the event for which this set of button layout parameters are being found for
     * @return RelativeLayout.LayoutParams the layout parameters required for the event's button to line up with the vertical axis of daily UI
	 * **/
    private RelativeLayout.LayoutParams getEventButtonParams(Event event){
    	RelativeLayout.LayoutParams eventButtonParams = new RelativeLayout.LayoutParams(
																			RelativeLayout.LayoutParams.MATCH_PARENT,
																			RelativeLayout.LayoutParams.MATCH_PARENT);
    	Date eventStart = event.getStartTime();
    	Date eventEnd = event.getEndTime();
    	int eventStartHour = eventStart.getHours();
    	int eventEndHour = eventEnd.getHours();
    	
    	eventButtonParams.addRule(RelativeLayout.RIGHT_OF, R.id.hour1);	//eventButtons will always be to the right of the hour column
    	
    	switch(eventStartHour){
    		case 0: eventButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);			break;
    		case 1: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour1); 	break;
    		case 2: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour2); 	break;
    		case 3: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour3); 	break;
    		case 4: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour4); 	break;
    		case 5: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour5);	break;
    		case 6: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour6); 	break;
    		case 7: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour7); 	break;
    		case 8: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour8); 	break;
    		case 9: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour9); 	break;
    		case 10: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour10); 	break;
    		case 11: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour11); 	break;
    		case 12: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour12); 	break;
    		case 13: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour13); 	break;
    		case 14: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour14); 	break;
    		case 15: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour15); 	break;
    		case 16: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour16); 	break;
    		case 17: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour17); 	break;
    		case 18: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour18); 	break;
    		case 19: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour19); 	break;
    		case 20: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour20); 	break;
    		case 21: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour21); 	break;
    		case 22: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour22); 	break;
    		case 23: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hour23); 	break;
    	}

    	if(selectedDate.getDate() < eventEnd.getDate() || 
    				selectedDate.getMonth() < eventEnd.getMonth()  ||
    				selectedDate.getYear() < eventEnd.getYear() 	){	//IF the event ends after displayed date, expand its button to the bottom of layout
    		eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour23);
        	return eventButtonParams;
    	}
    	
    	switch(eventEndHour){
			case 0: //same as hour 1 because we need the event snippet to be tall enough
			case 1: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour1); break;
			case 2: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour2); break;
			case 3: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour3); break;
			case 4: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour4); break;
			case 5: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour5); break;
			case 6: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour6); break;
			case 7: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour7); break;
			case 8: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour8); break;
			case 9: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour9); break;
			case 10: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour10); break;
			case 11: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour11); break;
			case 12: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour12); break;
			case 13: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour13); break;
			case 14: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour14); break;
			case 15: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour15); break;
			case 16: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour16); break;
			case 17: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour17); break;
			case 18: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour18); break;
			case 19: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour19); break;
			case 20: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour20); break;
			case 21: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour21); break;
			case 22: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour22); break;
			case 23: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hour23); break;
    	}
    	return eventButtonParams;
    }
    //can use android:onClick="methodName" instead of OnClickListeners, the method must take only a View
    //use android:minHeight="0dp" to override inherited min values in Buttons?
    //call finish(); somewhere? maybe not. Probably only when going to weekly or monthly view

    /**Makes sure that the event's button will lead to a corresponding editEvent page
     * when clicked.
     * @param eventButton the event's button
     * @param event the event that this button listener and button is for
	 * **/
    private void setupEventButtonListener(Button eventButton, final Event event){	//eclipse says the parameter has to be final, I don't know why
    	eventButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				global.setSelectedEvent(event);	//save selected event so the EventEditActivity can display corresponding data
				startActivity(new Intent(DailyViewActivity.this, EventEditActivity.class));
			}
		});
    }
    
    /**Updates the UI's date TextView to display the correct date. It follows a format using SimpleDateFormat 
     * from java public libraries. 
	 * **/
    private void setDisplayedDate(){
    	//can make this an app-wide method (accepting a TextView as argument)
    	TextView currentDateTV = (TextView)findViewById(R.id.theDate);

    	String theDateText = "";
    	selectedDate = global.getSelectedDate();
    	
        SimpleDateFormat shortForm = new SimpleDateFormat("d E MMM y");	//format date as Day# Weekday(abbreviated) Month(abbrev.) YYYY
        theDateText += shortForm.format(selectedDate);
        currentDateTV.setText(theDateText);
    }

    /**Sets the global preferences to reflect that the selected date is now today.
     * Also calls refshDisplay() method.
     * @param view the view that triggers this method on click
	 * **/
    public void todayButtonDailyOnClick(View view){	//named with "Daily" because there may be todayButtons for weekly/monthly
    	Date today = Calendar.getInstance().getTime();	//Causes the default time to be current time if the today button was the last navigation button pressed
    														//not just because we are viewing today
    	if(selectedDate != today){	//IF the view is already displaying today, then take no action
    		global.setSelectedDate(today);
    		selectedDate = today;
    		refreshDisplay();		//Show today's events
    	}
    }

    /**Sets the global preferences to reflect that the previous day is now the selected date.
     * Calls refreshDisplay() method.
     * @param view the view that triggers this method upon being clicked
	 * **/
    public void previousDayButtonOnClick(View view){
    	//System.out.println("in prevDayonclick: " + selectedDate.getYear() + " " + selectedDate.getMonth() + " " + (selectedDate.getDate() - 1));
    	Date previousDay = new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate() - 1);
    	global.setSelectedDate(previousDay);	//update for whole app
    	selectedDate = previousDay;				//update just for daily view
    	refreshDisplay();	//Replace the eventButtons with new ones for the date now being viewed
    }

    /**Sets the global preferences to reflect that the next day is now the selected date.
	 * Calls refreshDisplay() method.
	 * **/
    public void nextDayButtonOnClick(View view){
     	Date nextDay = new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate() + 1);
     	global.setSelectedDate(nextDay);
     	selectedDate = nextDay;
     	refreshDisplay();	//Replace the eventButtons with new ones for the date now being viewed
    }
    
    /**Sets up a dummy Event object and uses it to open a new EventEditActivity.
     * @param view the clickable view that triggers this method
	 * **/
    public void addEventButtonDailyOnClick(View view){	//also named with"Daily" because of other similar buttons
    	GregorianCalendar startCal = new GregorianCalendar();
    	startCal.setTime(selectedDate);
    	Event dummyEvent = new Event("No set name", startCal, startCal, EventManager.getCategories().get(0));	//requires a category inside the list,
    																											//so we must assure user cannot delete default category!
    	EventManager.addEvent(dummyEvent);
    	global.setSelectedEvent(dummyEvent);
    	startActivity(new Intent(DailyViewActivity.this, EventEditActivity.class));	//if it is null, and the user hits baack arrow instead of del/save, it's still there
    }
    
    /**Refreshes the UI to reflect any changes and performs necessary clean up. Removes all eventButtons from the UI and then 
     * deletes them from the eventButtonList to make way for future use.
	 * **/
    private void refreshDisplay(){
    	if(!eventButtonList.isEmpty()){		//is this check pointless?
    		for(Button eventButton: eventButtonList){
    			dailyLayout.removeView(eventButton);	//remove the buttons from the layout to make way for new buttons 
    		}											//corresponding to the *possibly changed* events (since the activity may not have been destroyed)
    		eventButtonList.clear();	//Empty the list to retain consistency (only containing events for the selected date)
    	}
    	setDisplayedDate();	//because the selected date has changed
    	displayEvents();	//to show the newly-relevant events and their corresponding buttons
    }
    
    /**Removes all event buttons from the UI and clears the eventButtonList so that there will be no duplication
     * of buttons upon this activity being brought back to the foreground.
	 * **/
    @Override
    protected void onPause(){
    	super.onPause();
    	if(!eventButtonList.isEmpty()){
    		for(Button eventButton: eventButtonList){
    			dailyLayout.removeView(eventButton);	//remove the buttons from the layout to make way for new buttons 
    		}											//corresponding to the *possibly changed* events (since the activity may not have been destroyed)
    		eventButtonList.clear();	//this empties the list so that there are not duplicate buttons upon onResume()
    	}
    }
   
    /**Calls setDisplayedDate() and displayEvents() to setup the activity upon resuming.
	 * **/
    @Override
    public void onResume(){	//onCreate -> onStart -> onResume
    	super.onResume(); //Do we need onWindowFocusChanged to be sure activity is visible?

        setDisplayedDate();
    	displayEvents();
    	//System.out.println("THIS IS THE eventButtonList length: " + eventButtonList.size()); //DEBUGGING
    }
    
    /**Required method for the option menu in the UI. 
     * @param menu the menu that goes into the UI element.
     * @return boolean always returns true
	 * **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.global_menu, menu);
        return true;
    }

    /**Opens the corresponding activity when the option is selected
     * from the options menu.
     * @param item the selected item from the options menu
     * @return boolean value always false (not used)
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
        	case R.id.menu_option_monthly_view:
        		startActivity(new Intent(this, MonthlyViewActivity.class));
        		finish();
        		break;
        	case R.id.menu_option_weekly_view:
        		startActivity(new Intent(this, WeeklyViewActivity.class));
        		finish();
        		break;
        	//no case for the current view (daily_view) because we should do nothing when that button is hit
        }
        return false;
    }
   
}//end DailyViewActivity class