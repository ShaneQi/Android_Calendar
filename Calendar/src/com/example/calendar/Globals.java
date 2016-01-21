package com.example.calendar;

import java.util.Date;

import android.app.Application;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

public class Globals extends Application{
	
	private Date selectedDate;
	private Event selectedEvent;
	private boolean appIsStarting;

	/**Getter method for the app's currently selected date.
	 * @return Date the selected date
	 * **/
	protected Date getSelectedDate(){
		return selectedDate;
	}
	
	/**Setter method for the app's selected date.
	 * @param newDate the newly selected date
	 * **/
	protected void setSelectedDate(Date newDate){
		selectedDate = newDate;
	}

	/**Getter method for the app's currently selected event.
	 * @return Event the selected event
	 * **/
	protected Event getSelectedEvent(){
		return selectedEvent;
	}	
	
	/**Setter method for the app's selected event.
	 * @param newEvent the newly selected event
	 * **/
	protected void setSelectedEvent(Event newEvent){
		selectedEvent = newEvent;
	}
	
	/**Getter method for the application's isStarting status (true/false)
	 * @return boolean value true if app is starting, false if otherwise
	 * **/
	protected boolean getAppIsStarting(){
		return appIsStarting;
	}
	
	/**Setter method for the application's isStarting status (true/false)
	 * @param isStarting the new value for the appIsStarting global variable
	 * **/
	protected void setAppIsStarting(boolean isStarting){
		appIsStarting = isStarting;
	}
	
	/**Used for formating event buttons across the whole application to maintain
	 * a uniform look and feel.
	 * @param eventButton the event button being formatted
	 * @param event the event this button corresponds to
	 * **/
	protected void setupEventButtonFormat(Button eventButton, Event event){
		eventButton.setMinHeight(0);
		eventButton.setPadding(15, 5, 20, 5); //this changes padding for things contained inside the button (space from the edges) L T R B
		eventButton.setTextSize(10);
		eventButton.setText(event.getName());
		eventButton.setEllipsize(android.text.TextUtils.TruncateAt.END);	//cuts the text off when it doesn't fit and adds ellipses
		eventButton.setSingleLine();// restricts text to a single line
		eventButton.setGravity(Gravity.LEFT); //text inside button will be left aligned
		eventButton.setBackgroundColor(event.getCategory().getColor());
    	
		//eventButton.setId(Integer.parseInt(string));	//need to find this out for later deleting? nah, cause the displayEvents method is called fresh each update
	}
	
}//end Globals class
