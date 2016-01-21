/**Event objects are for storing the information of an event that the user saves for 
 * future reference. 
 * Pre-Condition for all constructors:	startTime is actually before the endTime!
 * 
 **/

package com.example.calendar;

import java.io.Serializable;
import java.util.Date;	//deprecated! TimeStamp, Calendar, GregorianCalendar, Time
import java.util.GregorianCalendar;

public class Event implements Serializable{

	private	String name,
			location,
			description;

	private GregorianCalendar 	startTime,
								endTime;

	private Category category;

	/**Default constructor sets all attributes (except category) to null.
	 * Application needs to completely avoid referencing made to default objects; The null attributes assure a crash if reference is made.
	 **/
	public Event(){	
		name = null;	// "" ?
		location = null;
		description = null;
		startTime = null;
		endTime = null;
		//category = ?? define default category elsewhere
	}
	/**Incomplete constructor sets all required attributes according to parameters except for location and description.
	 * @param newName name for the event
	 * @param startTime the starting time of the event
	 * @param endTime the ending time of the event
	 * @param cat the category that this event is in
	 **/
	public Event(String newName, GregorianCalendar startTime, GregorianCalendar endTime, Category cat){
		name = newName;
		this.startTime = startTime;
		this.endTime = endTime;
		category = cat;
	}
	/**Incomplete constructor sets all attributes according to parameters except for description.
	 **@param newName name for the event
	 * @param startTime the starting time of the event
	 * @param endTime the ending time of the event
	 * @param cat the category that this event is in
	 * @param newLocation the user-provided location at which this event occurs
	 */
	public Event(String newName, GregorianCalendar startTime, GregorianCalendar endTime, Category cat, String newLocation){
		name = newName;
		this.startTime = startTime;
		this.endTime = endTime;
		category = cat;
		location = newLocation;
	}
	/**Complete constructor sets all attributes according to parameters.
	 **@param newName name for the event
	 * @param startTime the starting time of the event
	 * @param endTime the ending time of the event
	 * @param cat the category that this event is in
	 * @param newLocation the user-provided location at which this event occurs
	 * @param newDescription the user-provided description for this event
	 **/
	public Event(String newName, GregorianCalendar startTime, GregorianCalendar endTime, Category cat, String newLocation, String newDescription){
		name = newName;
		this.startTime = startTime;
		this.endTime = endTime;
		category = cat;
		location = newLocation;
		description = newDescription;
	}
	
	/**This toString() method is used purely for testing purposes.
	 * It returns all of the attributes for its corresponding Event object in String format.
	 * @return a String object containing all of the Event's data.
	 **/
	public String toString(){
		return 	"Name: " + name +
				"\nStart:\t" + startTime.getTime() +
				"\nEnd:  \t" + endTime.getTime() +
				"\n" + category +
				"\nLocation: \t\t" + location +
				"\nDescription: \t" + description;
	}
	
	/**Set the object's name according the given parameter.
	 * Only updates attribute if the param is not null or an empty string.
	 * @param newName the new name for the event
	 **/
	public void setName(String newName){
		if(newName!=null && !newName.isEmpty())//prevent null pointer exception (order matters)
			name = newName;						//insert exception handling for all
	}
	/**Set the object's location according the given parameter.
	 * Only updates attribute if the param is not null or an empty string.
	 * @param newLocation the new location string for the event
	 **/
	public void setLocation(String newLocation){
		if(newLocation!=null && !newLocation.isEmpty()) //prevent null pointer
		location = newLocation;
	}
	/**Set the object's description according the given parameter.
	 * Only updates attribute if the param is not null or an empty string.
	 * @param newDescription the new description string for the event
	 **/
	public void setDescription(String newDescription){
		if(newDescription!=null && !newDescription.isEmpty())
		description = newDescription;
	}
	/**Set the object's startTime according the given parameters.
	 * The values are allowed to overflow (for example, sending a month value of 15) because the GregorianCalendar class allows it.
	 * @param year the year in which the event now starts
	 * @param month the month in which the event now starts
	 * @param day the day of the month in which the event now starts
	 * @param hour the military time hour of the day in which the event now starts
	 * @param minute the minute of the hour in which the event now starts
	 **/
	public void setStartTime(int year, int month, int day, int hour, int minute){	//perhaps change this to setStartTime(Date newStartTime)
		//startTime = new GregorianCalendar(year-1970, month, day, hour, minute);	//the year minus 1970 (the start year from Date API)
		startTime = new GregorianCalendar(year, month, day, hour, minute);	//verify this in all similar methods
	}
	/**Set the object's endTime according the given parameters.
	 * The values are allowed to overflow (for example, sending a month value of 15) because the GregorianCalendar class allows it.
	 * @param year the year in which the event now ends
	 * @param month the month in which the event now ends
	 * @param day the day of the month in which the event now ends
	 * @param hour the military time hour of the day in which the event now ends
	 * @param minute the minute of the hour in which the event now ends
	 **/
	public void setEndTime(int year, int month, int day, int hour, int minute){
		//endTime = new GregorianCalendar(year-1970, month, day, hour, minute);
		endTime = new GregorianCalendar(year, month, day, hour, minute);
	}

	/**Set the object's category according the given parameter (a pointer to a category).
	 * @param cat the new category that event now belongs to
	 **/
	public void setCategory(Category cat){
		category = cat;
	}
	
	/**Return the object's name.
	 * *Intentionally does not check for null or empty string.
	 * @return a String object containing the name of the event
	 **/
	public String getName(){
	//	if(name!=null && !name.isEmpty())
			return name;
	//	return "NO SET NAME";// ERROR";	//can change to exception
	}
	/**Return the object's location.
	 * *If the current location attribute is set to null or empty string, then the returned value will be the string: "NO SET LOCATION".
	 * @return a String object containing the location of the event
	 **/
	public String getLocation(){
		if(location!=null && !location.isEmpty())
			return location;
		return "NO SET LOCATION";// ERROR";	//can change to exception
	}
	/**Return the object's description.
	 * *If the current description attribute is set to null or empty string, then the returned value will be the string: "NO SET DESCRIPTION".
	 * @return a String object containing the description of the event
	 **/
	public String getDescription(){
		if(description!=null && !description.isEmpty())
			return description;
		return "NO SET DESCRIPTION";// ERROR";	//can change to exception
	}
	/**Return the object's starting time.
	 ** @return a Date object corresponding to the starting time of the event
	 **/
	public Date getStartTime(){
		return startTime.getTime();
	}
	/**Return the object's ending time.
	 ** @return a Date object corresponding to the ending time of the event
	 **/
	public Date getEndTime(){
		return endTime.getTime();	//Calendar.getTime() returns a Date object
	}
	/**Return the category that the event is in.
	 ** @return a Category object reference corresponding to the event's category
	 **/
	public Category getCategory(){
		return category;
	}

	/**Return a boolean value - true if the event and the otherEvent have conflicting times, false if they do not.
	 * If the events overlap at any point at all, they are in conflict. 
	 * *If the events both start at the exact same time or end at the exact same time, then they are in conflict. 
	 * @param otherEvent an Event object reference for comparison
	 * @return a boolean value representing the time conflict status between the two event objects
	 * */
	public boolean inConflict(Event otherEvent){
		Date start1 = startTime.getTime();
		Date start2 = otherEvent.getStartTime();
		Date end1 = endTime.getTime();
		Date end2 = otherEvent.getEndTime();
		if(start1.after(start2) && start1.before(end2)){
			System.out.println("This event starts between the beginning and end of otherEvent");
			return true;
		}
		if(end1.after(start2) && end1.before(end2)){
			System.out.println("This event ends between the beginning and end of otherEvent");
			return true;
		}
		if(start2.after(start1) && start2.before(end1)){	//necessary checks because an event can begin before another event, then end after the other event ends
			System.out.println("Other event starts in span of this event");
			return true;
		}
		if(end2.after(start1) && end2.before(end1)){
			System.out.println("Other event ends in span of this event");
			return true;
		}
		if(start1.equals(start2) || end1.equals(end2)){	
			System.out.println("the events start or end at the exact same times (not a big deal if one ends while another starts?)");
			return true;
		}
		return false;
	}
	
	/**A simple .equals method for comparing two event objects.
	 * If the objects share starting times and ending times, then they are heuristically considered equal. 
	 * @param otherEvent an Event object reference for comparison 
	 * @return a boolean value representing the equivalence of the data from two event object references
	 * */
	public boolean equals(Event otherEvent){
		if(!startTime.getTime().equals(otherEvent.getStartTime())){
			return false;
		}
		if(!endTime.getTime().equals(otherEvent.getEndTime())){
			return false;
		}//no need to check non-Time fields <- can't have multiple events at same time anyways
		return true;
	}

}//end Event class