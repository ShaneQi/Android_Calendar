package com.example.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EventEditActivity extends Activity {

	private Globals global;
	private Event displayedEvent;
	private EditText 	nameField,
						locationField,
						descriptionField,
						weeklyRepetitionField;
	private TextView	TVeventStartDate, 
						TVeventEndDate;
	private DatePicker	startDateField, 
						endDateField;		//DateFields are for accessing Year Month DayOfMonth on DatePickers
	private TimePicker  startTimeField,		//TimeFields are for accessing Hour Minute on TimePickers
						endTimeField;
	private Spinner categorySpinner;
	
	private SimpleDateFormat shortForm = new SimpleDateFormat("d E MMM y\nhh:mm a");  //format date as Day# Weekday(abbreviated) Month(abbrev.) YYYY
																					//then \n Hour:Minute AM/PM
	/**Setes the content view accordingly and fetches values for
	 * variables global and and displayedEvent. Also calls in-class methods
	 * for setting up the dynamic UI elements.
	 * @param savedInstanceState used for super class constructor call
	 * **/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_edit);
		
		global = (Globals)getApplicationContext();
		displayedEvent = global.getSelectedEvent();
		
		setupTextViews();
		setupCategorySpinner();
		setupPickers();
	}
	
	/**Sets up every TextView in the activity using info from the displayed event.
	 * **/
	private void setupTextViews(){
		nameField = (EditText)findViewById(R.id.editTextEventName);
		nameField.setText(displayedEvent.getName());
		
		locationField = (EditText)findViewById(R.id.editTextEventLocation);
		locationField.setText(displayedEvent.getLocation());
		
		descriptionField = (EditText)findViewById(R.id.editTextEventDescription);
		descriptionField.setText(displayedEvent.getDescription());
		
		TVeventStartDate = (TextView)findViewById(R.id.textViewEventStartDate);			//could omit the TextViews
        TVeventStartDate.setText(shortForm.format(displayedEvent.getStartTime()));     //unfortunate collision of date-time terms (cause by date time pickers being separate API)   
		
        TVeventEndDate = (TextView)findViewById(R.id.textViewEventEndDate);
        TVeventEndDate.setText(shortForm.format(displayedEvent.getEndTime()));
        
        weeklyRepetitionField = (EditText)findViewById(R.id.editTextWeeklyRepetition);
	}
	
	/**Sets up the Spinner UI element used for selecting categories. 
	 * **/
	private void setupCategorySpinner(){
		categorySpinner = (Spinner)findViewById(R.id.spinnerEventCategory);
		ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item); //, arrayOfData
		adapter.addAll(EventManager.getCategories());
		categorySpinner.setAdapter(adapter);
		categorySpinner.setSelection(adapter.getPosition(displayedEvent.getCategory())); 
	}
	
	/**Sets up time and date pickers for the start and end times for the event.
	 * Initializes them with information from the displayed event.
	 * **/
	private void setupPickers(){
		//Initialize Pickers to the event's start date and time
		Date startTime = displayedEvent.getStartTime();
				
		startDateField = (DatePicker)findViewById(R.id.datePickerStartDate);
		startDateField.updateDate(startTime.getYear() + 1900, startTime.getMonth(), startTime.getDate());
				
		startTimeField = (TimePicker)findViewById(R.id.timePickerStartTime);
		startTimeField.setHour(startTime.getHours());
		startTimeField.setMinute(startTime.getMinutes());
				
		//Initialize other Pickers to event's end date and time	
		Date endTime = displayedEvent.getEndTime();
				
		endDateField = (DatePicker)findViewById(R.id.datePickerEndDate);
		endDateField.updateDate(endTime.getYear() + 1900, endTime.getMonth(), endTime.getDate());
			
		endTimeField = (TimePicker)findViewById(R.id.timePickerEndTime);
		endTimeField.setHour(endTime.getHours());
		endTimeField.setMinute(endTime.getMinutes());		
	}
	
	/**If the current input information is valid, it is all used to update the displayed event's
	 * data. If the weekly option is selected, the new events are created here and added to the EventManager's
	 * eventList. This method also calls finish() to return to the previous activity.
	 * @param view the clickable view that triggers this method
	 * **/
	public void saveChanges(View view){
		//Implement safety checks (if there is a conflict, send a long toast
			
		if(!currentInfoIsValid()){
			return;	//abort save, don't exit the event edit
		}
		
		displayedEvent.setName( nameField.getText().toString());
		displayedEvent.setLocation( locationField.getText().toString());
		displayedEvent.setDescription( descriptionField.getText().toString());

		System.out.println(displayedEvent);
		displayedEvent.setStartTime(startDateField.getYear(), startDateField.getMonth(), startDateField.getDayOfMonth(), 
																	startTimeField.getHour(), startTimeField.getMinute());
		displayedEvent.setEndTime(endDateField.getYear(), endDateField.getMonth(), endDateField.getDayOfMonth(),
																	endTimeField.getHour(), endTimeField.getMinute());
		displayedEvent.setCategory((Category)categorySpinner.getSelectedItem());
		
		System.out.println(displayedEvent);
		
		for(int repetition = 1; repetition <= Integer.parseInt(weeklyRepetitionField.getText().toString()); repetition++){
			GregorianCalendar newStart = new GregorianCalendar(startDateField.getYear(), startDateField.getMonth(), startDateField.getDayOfMonth() + 7*repetition,	//7 days ahead (1 week) 
															startTimeField.getHour(), startTimeField.getMinute());
			GregorianCalendar newEnd = new GregorianCalendar(endDateField.getYear(), endDateField.getMonth(), endDateField.getDayOfMonth() + 7*repetition,
															endTimeField.getHour(), endTimeField.getMinute() );
			Event newEvent = new Event(displayedEvent.getName(), newStart, newEnd, displayedEvent.getCategory());
			EventManager.addEvent(newEvent);
			System.out.println("added repetition for week " + repetition + " after original");
		}
		
		//MIGHT NEED TO DELETE AND READD EVENT FROM EVENTMANAGER TO RETAIN CHRONOLOGICAL ORDER (or call new sort method in EM) if it even matters
		finish();
	}

	/**OnClick method for the Delete Event button. Removes the displayed event from the EventManager's list.
	 * @param view the view that triggers this method
	 **/
	public void deleteEvent(View view){
		//if(event.isWeekly())
		//removeAllInstancesOfEvent(displayedEvent);
		EventManager.removeEvent(displayedEvent);
		finish();
	}
	
	/**Checks all currently input information from UI elements for veracity. If they cause any issue at all 
	 * (null pointers, weekly event collisions, etc.), then the information is not valid.
	 * @return boolean value: True if the information is valid, false otherwise.
	 * **/
	private boolean currentInfoIsValid(){
		//check for null/empty string name field
		//make sure event starts before it ends LOL can be done by setting min/max for the pickers
		String newName = nameField.getText().toString();
		if(newName == null || newName.isEmpty()){
			Toast.makeText(getApplicationContext(), "Please enter a valid name; Changes not saved.", Toast.LENGTH_LONG).show();
			return false;
		}
		
		Date newStartDate = new Date(startDateField.getYear(), startDateField.getMonth(), startDateField.getDayOfMonth(), 
																	startTimeField.getHour(), startTimeField.getMinute());
		Date newEndDate = new Date(endDateField.getYear(), endDateField.getMonth(), endDateField.getDayOfMonth(),
																	endTimeField.getHour(), endTimeField.getMinute());
		if(newStartDate.after(newEndDate)){
			Toast.makeText(getApplicationContext(), "Event ends before it begins; Changes not saved.", Toast.LENGTH_LONG).show();
			return false;
		}
		
		String numberOfWeeksToRepeat = weeklyRepetitionField.getText().toString();
		if(numberOfWeeksToRepeat == null || numberOfWeeksToRepeat.isEmpty()){
			Toast.makeText(getApplicationContext(), "Please enter a valid number of weeks to repeat; Changes not saved.", Toast.LENGTH_LONG).show();
			return false;
		}
		int numberOfWeeks = Integer.parseInt(numberOfWeeksToRepeat);
		if(numberOfWeeks >= 521){	//if more than 10 years worth of repetition
			Toast.makeText(getApplicationContext(), "Please enter a lower number of weeks to repeat; Changes not saved.", Toast.LENGTH_LONG).show();
			return false;
		}

		System.out.println("I GOT TO THE WEEKLY C HECK");
		//COULD REFACTOR THIS CHECK TO A NEW METHOD THAT RETURNS ARRAYLIST OF EVENTS TO ADD (SIZE==0 IF INFO INVALID)
		//ArrayList<Event> eventsToAdd = new ArrayList<Event>();
		Category tempCat = (Category)categorySpinner.getSelectedItem();
		Event tempEvent;
		ArrayList<Event> eventList = EventManager.getEvents();
		tempEvent = new Event(newName, 
								new GregorianCalendar(startDateField.getYear(), startDateField.getMonth(), startDateField.getDayOfMonth(), 
														startTimeField.getHour(), startTimeField.getMinute()), 
								new GregorianCalendar(endDateField.getYear(), endDateField.getMonth(), endDateField.getDayOfMonth(),
														endTimeField.getHour(), endTimeField.getMinute() ), 
														tempCat);
		eventList.remove(displayedEvent);	//no need to compare with the pre-edited version of itself
											//need to reinsert this after the check though!
	
		for(int repetition = 0; repetition <= numberOfWeeks; repetition++){
			tempEvent.setStartTime(startDateField.getYear(), startDateField.getMonth(), startDateField.getDayOfMonth() + 7*repetition,	//7 days ahead (1 week) 
									startTimeField.getHour(), startTimeField.getMinute()); 
			tempEvent.setEndTime(endDateField.getYear(), endDateField.getMonth(), endDateField.getDayOfMonth() + 7*repetition,
									endTimeField.getHour(), endTimeField.getMinute());
	
			for(Event event: eventList){
				if(tempEvent.inConflict(event)){
					System.out.println(tempEvent + " \n\n\n" + event);
					Toast.makeText(getApplicationContext(), "Event in conflict upon weekly repetition " + repetition + "; Changes not saved.", Toast.LENGTH_LONG).show();
					eventList.add(displayedEvent);
					return false;
				}
			}
		}
		eventList.add(displayedEvent);
		return true;
	}
	
}//end class EventEditActivity