package com.example.calendar;

import java.text.SimpleDateFormat;
import java.util.*;	//Date, Calendar, & GregorianCalendar

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;	
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;	
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;	
import android.widget.RelativeLayout;


public class MonthlyViewActivity extends AppCompatActivity { //ActionBarActivity //CompatActivity

	private TableLayout monthlyLayout;
	private Globals global;
	private Date 	selectedDate,
					startOfMonth,
					endOfMonth;
	Calendar dummyCalendar = Calendar.getInstance();
	
	/**Sets the content view accordingly. Assigns appropriate values to the
	 * monthlyLayout and global variables for future use.
	 * @param savedInstanceState used for super class constructor call
	 * **/	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_view);
        
        //setupTestFixture();	//This initializes some categories and events for quick display during demo, do not come back to monthly view, or else duplicate categories
        
        monthlyLayout = (TableLayout) findViewById(R.id.tableLayoutMonthly);
        global = (Globals)getApplicationContext();

        global.setSelectedDate(Calendar.getInstance().getTime());	//set selectedDate to today's date
    }
    
    /**Calls setDisplayedMonth() and displayEvents() to setup the activity upon resuming.
	 * **/
    @Override
    public void onResume(){	//onCreate -> onStart -> onResume
    	super.onResume();

        setDisplayedMonth();
    	displayEvents();
    }
    
    /**UNUSED - empty method**/
    @Override
    protected void onPause(){
    	super.onPause();
    }

    /**Updates the UI's month TextView to display the correct month. It follows a format using SimpleDateFormat 
     * from java public libraries. 
	 * **/
    private void setDisplayedMonth(){
    	TextView currentMonthTV = (TextView)findViewById(R.id.theMonth);

        selectedDate = global.getSelectedDate();
    	
    	startOfMonth = new Date(selectedDate.getYear(), selectedDate.getMonth(), 1);
    	endOfMonth = new Date(selectedDate.getYear(), selectedDate.getMonth()+1, 1); //exactly midnight on last day of month
    	
    	String theMonthText = "";	
        SimpleDateFormat shortForm = new SimpleDateFormat("MMMM yyyy");	//format date as Full month name YYYY (year)
        theMonthText += shortForm.format(selectedDate);
        currentMonthTV.setText(theMonthText);
    }

    /**Sets up and displays all of the event buttons for the displayed date. Fetches all the 
     * required events by using EventManager.getEvents(Date,Date) method.
     * Also calls methods inside this class to do the rest of the setup for the individual buttons.
     * Also colors in the cells for the monthly view's grid.
	 * **/
    private void displayEvents(){
    	//Initialize dummyDates for traversing the cells in monthly view
    	dummyCalendar.setTime(startOfMonth);
    	int dayInWeek = dummyCalendar.get(Calendar.DAY_OF_WEEK) - 1;
    	Date dummyDate = new Date(startOfMonth.getYear(), startOfMonth.getMonth(), startOfMonth.getDate() - dayInWeek);	// 0:00 AM for our dummy date
    	Date endOfDummyDate = new Date(dummyDate.getYear(), dummyDate.getMonth(), dummyDate.getDate() + 1);	//midnight of dummy date
    	
    	ArrayList<Event> eventsToDisplay = new ArrayList<Event>();
    	TableRow tableRow;
    	Button dateButton;
    	String textForDateButton = "";
    	
    	for(int row = 1; row < monthlyLayout.getChildCount(); row++){
    		tableRow = (TableRow)monthlyLayout.getChildAt(row);
    		
    		for(int col = 0; col < tableRow.getChildCount(); col++){
    			dateButton = (Button)tableRow.getChildAt(col);
        	    dateButton.setTextSize(10);
        	    
        	    //dateButton.setSingleLine();
        		dateButton.setMaxLines(3);	//only a temporary solution for keeping text from expanding button height and making some rows taller than others
        									//could try setting the text to scroll horizontally +  marquee (so that they are one line) since the column width is set and stone
        	    
        	    dateButton.setEllipsize(android.text.TextUtils.TruncateAt.END);
        		dateButton.setPadding(1, 0, 1, 1);
        		dateButton.setGravity(Gravity.LEFT);
        		dateButton.setBackgroundColor(getColor(R.color.weekdays));	//can change weekday/end color setting to static (in the XML file)
        		if(col==0 || col==6){
        			dateButton.setBackgroundColor(getColor(R.color.weekends)); //Specified color for weekends (can place in preferences later)
        		}
        		if(dummyDate.getMonth()==11 && dummyDate.getDate()==25){  
        			dateButton.setBackgroundColor(getColor(R.color.holidays));	//if the day is Christmas
        		}	
        			//EVENTBUTTONLISTENER NEEDS SOLUTION TO MISLEADING POINTER, possibly an arraylist (emptied and repopulated on each refresh of this activity)
        	    //setupEventButtonListener(dateButton, dummyDate);	//so that when the date is hit, it takes user to the corresponding dailyView
        
        	    eventsToDisplay = EventManager.getEvents(dummyDate, endOfDummyDate);	//Fetches list of all events starting in dummyDate
        		textForDateButton += dummyDate.getDate();
      
        		for(Event event : eventsToDisplay){
        			textForDateButton += ("\n" + event.getName());
        		}
      
        	    dateButton.setText(textForDateButton);
        	    textForDateButton = "";	//reset the string for next go around
        	    dummyDate.setDate(dummyDate.getDate() + 1);
        	    endOfDummyDate.setDate(endOfDummyDate.getDate() + 1);
    		}	
    	}
    }

    /**Refreshes the UI to reflect any changes.
	 * **/
    private void refreshDisplay(){
    	
    //		for(Button eventButton: eventButtonList){
    			//weeklyLayout.removeView(eventButton);	//remove the buttons from the layout to make way for new buttons 
    //		}											//corresponding to the *possibly changed* events (since the activity may not have been destroyed)
    //		eventButtonList.clear();	//Empty the list to retain consistency (only containing events for the selected date)
   
    	setDisplayedMonth();	//because the selected week has changed
    	displayEvents();	//to show the newly-relevant events and their corresponding buttons
    }

    /**UNUSED - not used in final version**/
    /*Do not use this listener yet, it is broken. Needs a solution to misleading pointer. Currently, it will lead user to daily view displaying the first day
     * that comes right after the last day displayed in this monthly view. This is a result of the dummyDate being left at that date and the listener still 
     * pointing to it.
     * 
     * Possible solution: create array list and use it to hold new Date objects copied from dummyEvent and then send that new Date into the listener method
     * instead. Upon refresh, clear that list first. Not sure if this will work.
     * */
    private void setupEventButtonListener(Button dateButton, final Date theClickedDate){	//eclipse says the parameter has to be final, I don't know why
    	dateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println(theClickedDate);
				Date selectedDate = new Date(theClickedDate.getYear(), theClickedDate.getMonth(), theClickedDate.getDate());	//does not stop misleading pointers
				global.setSelectedDate(selectedDate);	//set selectedDate to the day that this button is for
				startActivity(new Intent(MonthlyViewActivity.this, DailyViewActivity.class));	//hit that date, go
				finish();
			}
		});
    }
    
    /**Sets the global preferences to reflect that the selected date is now today.
     * Also calls refshDisplay() method.
     * @param view the view that triggers this method on click
	 * **/
    public void todayButtonMonthlyOnClick(View view){	//named with "Monthly" because there are todayButtons for weekly/monthly
    	Date today = Calendar.getInstance().getTime();	
    	//Implement? -> IF the view is already displaying this month, then take no action
    	global.setSelectedDate(today);
    	selectedDate = today;
    	refreshDisplay();		//Show this week's events
    	
    }
    
    /**Sets the global preferences to reflect that the date one month before the previously selected date is now the selected date.
     * Calls refreshDisplay() method.
     * @param view the view that triggers this method upon being clicked
	 * **/
    public void previousMonthButtonOnClick(View view){
    	Date previousMonth = new Date(selectedDate.getYear(), selectedDate.getMonth()-1, 15);	//cannot use getDate because if selectedDate was day 31 it would cause
    																							//the displayedMonth to be the same (via overflow in 30-day month)
    	global.setSelectedDate(previousMonth);	//update for whole app
    	selectedDate = previousMonth;				//update just for daily view
    	refreshDisplay();	//Replace the eventButtons with new ones for the date now being viewed
    }

    /**Sets the global preferences to reflect that the date a month ahead of the previously selected date is now the selected date.
     * Calls refreshDisplay() method.
     * @param view the view that triggers this method upon being clicked
	 * **/
    public void nextMonthButtonOnClick(View view){
     	Date nextMonth = new Date(selectedDate.getYear(), selectedDate.getMonth()+1, 15);
     	global.setSelectedDate(nextMonth);
     	selectedDate = nextMonth;
     	refreshDisplay();	//Replace the eventButtons with new ones for the date now being viewed
    }
   
    /**Sets up a dummy Event object and uses it to open a new EventEditActivity.
     * @param view the clickable view that triggers this method
	 * **/
    public void addEventButtonMonthlyOnClick(View view){	//also named with"Weekly" because of other similar buttons
    	GregorianCalendar startCal = new GregorianCalendar();
    	startCal.setTime(selectedDate);
    	Event dummyEvent = new Event("No set name", startCal, startCal, EventManager.getCategories().get(0));	//guaranteed to fetch a category
    	EventManager.addEvent(dummyEvent);
    	global.setSelectedEvent(dummyEvent);
    	startActivity(new Intent(MonthlyViewActivity.this, EventEditActivity.class));	//if it is null, and the user hits back arrow instead of delete/save, it's still there
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
    	switch(item.getItemId()){
        	case R.id.menu_option_category_manager:
        		startActivity(new Intent(this, CategoryManagerActivity.class));
        		break;
        	case R.id.menu_option_weekly_view:
        		startActivity(new Intent(this, WeeklyViewActivity.class));
        		finish();
        		break;
        	case R.id.menu_option_daily_view:
        		startActivity(new Intent(this, DailyViewActivity.class));
        		finish();
        		break;
        	//no case for the current view (monthly_view) because we should do nothing when that button is hit
        }
        return false;
    }
   
    /**UNUSED - not used in final version**/
    private void setupTestFixture(){
    	
    	Category defaultCat = new Category("Default Category");
    	Category cat1 = new Category("Category juan", 0x12345679);
    	EventManager.addCategory(defaultCat);
    	EventManager.addCategory(cat1);
    	EventManager.addCategory(new Category("ROASTIN BRUH", 0x55555555));
    	EventManager.addEvent(new Event("Hey man, I start work at 1:10 AM?", new GregorianCalendar(2015, 10, 6, 1, 10), new GregorianCalendar(2015, 10, 6, 2, 10), defaultCat));
    	EventManager.addEvent(new Event("Ayy waddup breh?", new GregorianCalendar(2015, 10, 7, 2, 10), new GregorianCalendar(2015, 10, 7, 2, 10), defaultCat));
    	EventManager.addEvent(new Event("third event :(", new GregorianCalendar(2015, 10, 14, 1, 10), new GregorianCalendar(2015, 10, 15, 12, 10), cat1));
    	EventManager.addEvent(new Event("what you wa\nnt", new GregorianCalendar(2015, 10, 6, 5, 10), new GregorianCalendar(2015, 10, 6, 7, 10), cat1));
    //	defaultCat.setColor(0x99420690);
    }
    
}