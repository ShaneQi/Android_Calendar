/**Tests the Category and EventManager classes
 */
package com.example.calendar;

import static java.lang.System.*;
import java.util.List;
import java.util.Date;
import java.util.GregorianCalendar;
//import java.awt.Color;

public class CategoryDriver{
	public static void main(String [] args){

		//TESTING THE CATEGORY CLASS

		Category c1 = new Category();
/*		out.println(c1 + "\n");
		out.println(c1.getName() + "   " + c1.getColor());

		c1 = new Category("Bald man");
		out.println(c1);
		c1.setColor(new Color(0xBEEF));
		out.println(c1);
		c1.setName("Hairy man");
		out.println(c1);
		out.println(c1.getName() + "   " + c1.getColor());
*/
		c1 = new Category("Sasquatch");//, Color.CYAN);
//		out.println(c1);

		//TESTING THE EVENTMANAGER CLASS

		//em.addEvent(new Event());
		//em.addEvent(new Event());//without proper handling, these two will cause numerous problems in addEvent method

		EventManager.addCategory(new Category("Holiday"));
		EventManager.addEvent(new Event("Birthday1", new GregorianCalendar(1995, 6, 27, 7, 30), new GregorianCalendar(1995, 6, 27, 8, 42), c1));
		EventManager.addEvent(new Event("Birthday2", new GregorianCalendar(1995, 6, 27, 7, 30), new GregorianCalendar(1995, 6, 27, 8, 42), c1));	//perfect dupes shouldn't get inserted?
		EventManager.addEvent(new Event("Birthday3", new GregorianCalendar(1995, 6, 27, 8, 45), new GregorianCalendar(1995, 6, 27, 8, 42), c1));
		EventManager.addEvent(new Event("Birthday4", new GregorianCalendar(1995, 6, 27, 5, 23), new GregorianCalendar(1995, 6, 27, 8, 42), c1));

		List<Event> events= EventManager.getEvents();
	//	for(Event e : events){
	//		out.println("At index " + events.indexOf(e) + "\n" + e);
	//	}

		out.println(new Date(1994-1900, 11, 1 , 0, 1));

/*		events = EventManager.getEvents(new Date(1994-1900, 0, 1 , 0, 0), new Date(1998-1900, 0 ,1 ,0 ,0));
		out.println(events);
		for(Event e: events){
			out.println(e);
		}
*/
/*		events = EventManager.getEvents(new Date(1995-1900, 6, 27 , 7, 30), new Date(1995-1900, 6 ,27 ,8 ,46));
		for(Event e: events){
			out.println(e.getName());
		}
*/

		events = EventManager.getEvents(new Date(1995-1900, 6, 27 , 3, 30), new Date(1995-1900, 6 ,27 ,7 ,31));
		for(Event e: events){
			out.println(e.getName());
		}

	}//end main
}//end CategoryDriver class