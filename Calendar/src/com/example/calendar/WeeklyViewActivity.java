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


public class WeeklyViewActivity extends AppCompatActivity { //ActionBarActivity //CompatActivity

	private RelativeLayout weeklyLayout;
	private Globals global;
	private ArrayList<Button> eventButtonList = new ArrayList<Button>();
	private Date 	selectedDate,
					startOfWeek, 
					endOfWeek;
	private Calendar dummyCalendar = Calendar.getInstance();	//used for extracting dayOfWeek(int) from Dates

	/**Sets the content view accordingly. Assigns appropriate values to the
	 * weeklyLayout and global variables for future use.
	 * @param savedInstanceState used for super class constructor call
	 * **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_view);
        
        weeklyLayout = (RelativeLayout) findViewById(R.id.weeklyLayout);
        global = (Globals)getApplicationContext();

    }

    /**Calls setDisplayedWeek() and displayEvents() to setup the activity upon resuming.
	 * **/
    @Override
    public void onResume(){	//onCreate -> onStart -> onResume
    	super.onResume();

        setDisplayedWeek();
    	displayEvents();
    }
  
    /**Removes all event buttons from the UI and clears the eventButtonList so that there will be no duplication
     * of buttons upon this activity being brought back to the foreground.
	 * **/
    @Override
    protected void onPause(){
    	super.onPause();
    	if(!eventButtonList.isEmpty()){
    		for(Button eventButton: eventButtonList){
    			weeklyLayout.removeView(eventButton);	//remove the buttons from the layout to make way for new buttons 
    		}											//corresponding to the *possibly changed* events (since the activity may not have been destroyed)
    		eventButtonList.clear();	//this empties the list so that there are not duplicate buttons upon onResume()
    	}
    }

    /**Updates the UI's week TextView to display the correct dates. It follows a format using SimpleDateFormat 
     * from java public libraries. 
	 * **/
    private void setDisplayedWeek(){
    	TextView currentWeekTV = (TextView)findViewById(R.id.theWeek);
        
        selectedDate = global.getSelectedDate();
    	
    	//REFACTOR THIS PIECE (EXTRACT METHOD + REFACtor all the gets)
    	dummyCalendar.setTime(selectedDate);
    	int offSet = dummyCalendar.get(Calendar.DAY_OF_WEEK);
    	startOfWeek = new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate()-offSet+1);
    	offSet = 7-offSet;
    	endOfWeek = new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate()+offSet); //early morning of Saturday
    	
    	String theWeekText = "";	
        SimpleDateFormat shortForm = new SimpleDateFormat("MM/d/yy");	//format date as Month # / DayOfMonth # / YY (year)
        theWeekText += shortForm.format(startOfWeek);	//startOfWeek
        theWeekText += " - "; 
        theWeekText += shortForm.format(endOfWeek);	//endOfWeek
        System.out.println("SETDISPLAYEDWeek");
        currentWeekTV.setText(theWeekText);
        endOfWeek.setHours(24);
        System.out.println(endOfWeek + "\n CHECK THIS");
    }    
 
    /**Sets up and displays all of the event buttons for the displayed week. Fetches all the 
     * required events by using EventManager.getEvents(Date,Date) method and sets up the layout parameters
     * for each event button using the global preferences (setupEventButtonFormat method) to retain consistent
     * look and feel. Also calls methods inside this class to do the rest of the setup for the individual buttons.
	 * **/
    private void displayEvents(){
    	
    //	Date startOfSelectedDate = new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate());
    //	Date endOfSelectedDate = new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate()+1);	//should be midnight (24 hours after start)
    	
    	ArrayList<Event> eventsToDisplay = EventManager.getEvents(startOfWeek, endOfWeek);	//Fetches list of all events starting in selectedDate
    	for(Event e: eventsToDisplay){

    		Button eventButton = new Button(this);

    		RelativeLayout.LayoutParams eventButtonParams = getEventButtonParams(e);	
    		global.setupEventButtonFormat(eventButton, e);
    		eventButton.setLayoutParams(eventButtonParams);
    		setupEventButtonListener(eventButton, e);
    		
    		weeklyLayout.addView(eventButton);	//PUT THE BUTTON IN THE DAILYVIEW LAYOUT
    		eventButtonList.add(eventButton);	//store the button, so it may be deleted during refreshes
    	}
    }

    /**Creates the appropriate layout parameters needed for the event's button to line up 
     * relatively to the hour markers on the left side of the screen and the day markers on the top.
     * @param event the event for which this set of button layout parameters are being found for
     * @return RelativeLayout.LayoutParams the layout parameters required for the event's button to line up with the axes of weekly UI
	 * **/
    private RelativeLayout.LayoutParams getEventButtonParams(Event event){

    	RelativeLayout.LayoutParams eventButtonParams = new RelativeLayout.LayoutParams(
																			RelativeLayout.LayoutParams.MATCH_PARENT,
																			RelativeLayout.LayoutParams.MATCH_PARENT);
    	Date eventStart = event.getStartTime();
    	Date eventEnd = event.getEndTime();
    	dummyCalendar.setTime(eventStart);
    	int eventStartDayOfWeek = dummyCalendar.get(Calendar.DAY_OF_WEEK);
    	//int eventEndDayOfWeek = //IMPLEMENT LATER POSSIBLY FOR DISPLAYING EVENTS THAT SPAN MULTIPLE DAYS (care for ending past this week)
    	int eventStartHour = eventStart.getHours();
    	int eventEndHour = eventEnd.getHours();
    	
    	//RelativeLayout.LayoutParams eventButtonParams = getParamsForAlignLeftRight(event);
    	eventButtonParams.addRule(RelativeLayout.RIGHT_OF, R.id.hourW1);	//eventButtons will always be to the right of the hour column
    	
    	switch(eventStartHour){
    		case 0: //eventButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);			break; //can't put in top for weekly, the day titles 
    																								//are in the way
    		case 1: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW1); 	break;
    		case 2: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW2); 	break;
    		case 3: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW3); 	break;
    		case 4: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW4); 	break;
    		case 5: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW5);	break;
    		case 6: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW6); 	break;
    		case 7: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW7); 	break;
    		case 8: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW8); 	break;
    		case 9: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW9); 	break;
    		case 10: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW10); 	break;
    		case 11: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW11); 	break;
    		case 12: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW12); 	break;
    		case 13: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW13); 	break;
    		case 14: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW14); 	break;
    		case 15: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW15); 	break;
    		case 16: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW16); 	break;
    		case 17: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW17); 	break;
    		case 18: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW18); 	break;
    		case 19: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW19); 	break;
    		case 20: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW20); 	break;
    		case 21: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW21); 	break;
    		case 22: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW22); 	break;
    		case 23: eventButtonParams.addRule(RelativeLayout.ALIGN_TOP, R.id.hourW23); 	break;
    	}
    	
    	switch(eventStartDayOfWeek){
			case 1: eventButtonParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.dayW1);
					eventButtonParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.dayW1);break;
			case 2: eventButtonParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.dayW2); 
					eventButtonParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.dayW2);break;
			case 3: eventButtonParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.dayW3); 
					eventButtonParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.dayW3);break;
			case 4: eventButtonParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.dayW4); 
					eventButtonParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.dayW4);break;
			case 5: eventButtonParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.dayW5); 
					eventButtonParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.dayW5);break;
			case 6: eventButtonParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.dayW6); 
					eventButtonParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.dayW6);break;
			case 7: eventButtonParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.dayW7); break;
		}

    	if(eventStart.getDate() < eventEnd.getDate() || 
    				eventStart.getMonth() < eventEnd.getMonth()  ||
    				eventStart.getYear() < eventEnd.getYear() 	){	//IF the event ends after the day it starts on, expand its button to the bottom of layout
    		eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW23);
        	return eventButtonParams;
    	}
    	
    	switch(eventEndHour){
			case 0: //same as hour 1 because we need the event snippet to be tall enough
			case 1: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW1); break;
			case 2: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW2); break;
			case 3: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW3); break;
			case 4: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW4); break;
			case 5: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW5); break;
			case 6: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW6); break;
			case 7: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW7); break;
			case 8: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW8); break;
			case 9: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW9); break;
			case 10: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW10); break;
			case 11: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW11); break;
			case 12: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW12); break;
			case 13: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW13); break;
			case 14: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW14); break;
			case 15: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW15); break;
			case 16: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW16); break;
			case 17: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW17); break;
			case 18: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW18); break;
			case 19: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW19); break;
			case 20: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW20); break;
			case 21: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW21); break;
			case 22: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW22); break;
			case 23: eventButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.hourW23); break;
    	}
    	return eventButtonParams;
    }
    
    /**Makes sure that the event's button will lead to a corresponding editEvent page when clicked.
     * @param eventButton the event's button
     * @param event the event that this button listener and button is for
	 * **/     
    private void setupEventButtonListener(Button eventButton, final Event event){	//eclipse says the parameter has to be final, I don't know why
    	eventButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				global.setSelectedEvent(event);	//save selected event so the EventEditActivity can display corresponding data
				startActivity(new Intent(WeeklyViewActivity.this, EventEditActivity.class));
			}
		});
    }    

    /**Refreshes the UI to reflect any changes and performs necessary clean up. Removes all eventButtons from the UI and then 
     * deletes them from the eventButtonList to make way for future use.
	 * **/
    private void refreshDisplay(){
  //  	if(!eventButtonList.isEmpty()){		//is this check pointless?
    		for(Button eventButton: eventButtonList){
    			weeklyLayout.removeView(eventButton);	//remove the buttons from the layout to make way for new buttons 
    		}											//corresponding to the *possibly changed* events (since the activity may not have been destroyed)
    		eventButtonList.clear();	//Empty the list to retain consistency (only containing events for the selected date)
    //  }
    	setDisplayedWeek();	//because the selected week has changed
    	displayEvents();	//to show the newly-relevant events and their corresponding buttons
    }

    /**Sets the global preferences to reflect that the selected date is now today.
     * Also calls refshDisplay() method.
     * @param view the view that triggers this method on click
	 * **/
    public void todayButtonWeeklyOnClick(View view){	//named with "Weekly" because there may be todayButtons for weekly/monthly
    	Date today = Calendar.getInstance().getTime();	
    	//Implement? -> IF the view is already displaying this week, then take no action
    	global.setSelectedDate(today);
    	selectedDate = today;
    	refreshDisplay();		//Show this week's events
    	
    }
    
    /**Sets the global preferences to reflect that the date seven days before the previously selected date is now the selected date.
     * Calls refreshDisplay() method.
     * @param view the view that triggers this method upon being clicked
	 * **/
    public void previousWeekButtonOnClick(View view){
    	//System.out.println("in prevDayonclick: " + selectedDate.getYear() + " " + selectedDate.getMonth() + " " + (selectedDate.getDate() - 1));
    	Date previousWeek = new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate() - 7);
    	global.setSelectedDate(previousWeek);	//update for whole app
    	selectedDate = previousWeek;				//update just for daily view
    	refreshDisplay();	//Replace the eventButtons with new ones for the date now being viewed
    }

    /**Sets the global preferences to reflect that the date seven days ahead of the previously selected date is now the selected date.
     * Calls refreshDisplay() method.
     * @param view the view that triggers this method upon being clicked
	 * **/
    public void nextWeekButtonOnClick(View view){
     	Date nextWeek = new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate() + 7);
     	global.setSelectedDate(nextWeek);
     	selectedDate = nextWeek;
     	refreshDisplay();	//Replace the eventButtons with new ones for the date now being viewed
    }

    /**Sets up a dummy Event object and uses it to open a new EventEditActivity.
     * @param view the clickable view that triggers this method
	 * **/
    public void addEventButtonWeeklyOnClick(View view){	//also named with"Daily" because of other similar buttons
    	GregorianCalendar startCal = new GregorianCalendar();
    	startCal.setTime(selectedDate);
    	Event dummyEvent = new Event("No set name", startCal, startCal, EventManager.getCategories().get(0));	//requires a category inside the list,
    																											//so we must assure user cannot delete default category!
    	EventManager.addEvent(dummyEvent);
    	global.setSelectedEvent(dummyEvent);
    	startActivity(new Intent(WeeklyViewActivity.this, EventEditActivity.class));	//if it is null, and the user hits baack arrow instead of del/save, it's still there
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
        	case R.id.menu_option_daily_view:
        		startActivity(new Intent(this, DailyViewActivity.class));
        		finish();
        		break;
        	//no case for the current view (weekly_view) because we should do nothing when that button is hit
        }
        return false;
    }
   
}//end WeeklyViewActivity class