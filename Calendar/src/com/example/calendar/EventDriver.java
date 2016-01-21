package com.example.calendar;

import static java.lang.System.*;
//import java.util.Date;	//MONTHS FROM BASE ZERO  month 0 is january! YEARS FROM 1900
import java.util.Calendar;
import java.util.GregorianCalendar;
//import java.awt.Color;

public class EventDriver{
	public static void main(String [] args){
	/*	Event event1 = new Event();
		out.println(event1.getName());
		out.println(event1.getStartTime());
		out.println(event1.getEndTime());
		out.println(event1.getCategory());
		out.println(event1);
*/	//Set up default times or deny no arg constructor

		//Test Fixture
		Category defaultCat = new Category("Default");
/*
		Event event2 = new Event("Birthday", new GregorianCalendar(1995, 6, 27, 7, 30), new GregorianCalendar(1995, 6, 27, 8, 42), defaultCat);
		out.println(event2);

		Event event3 = new Event("BirthdayBro", new GregorianCalendar(1995, 6, 27, 7, 30), new GregorianCalendar(1995, 6, 27, 8, 42), defaultCat, "Mesquite, TX");
		out.println(event3);

		Event event4 = new Event("BirthdayTIME", new GregorianCalendar(1995, 6, 27, 7, 30), new GregorianCalendar(1995, 6, 27, 8, 42), defaultCat, "Masskqweet, Tejas", "Best time to be alive");
		out.println(event4);

/*

	//	defaultCat.setColor(new Color(114, 220, 42));

/*		Event event5 = new Event("Birthday5", new GregorianCalendar(1995, 6, 27, 7, 30), new GregorianCalendar(1995, 6, 27, 8, 42), defaultCat);
		out.println(event5);

		Event event6 = new Event("BirthdayBro6", new GregorianCalendar(1995, 6, 27, 7, 30), new GregorianCalendar(1995, 6, 27, 8, 42), defaultCat, "Mesquite, TX");
		out.println(event6);

		Event event7 = new Event("Birthday7", new GregorianCalendar(1995, 6, 27, 7, 30), new GregorianCalendar(1995, 6, 27, 8, 42), defaultCat, "Masskqweet, Tejas", "Best time to be alive");
		out.println(event7);
*/

		//Setting a time past that of the boundary for an hour, a day, a month
		Event td1 = new Event("TestGregorianCalendar1", new GregorianCalendar(1995, 5, 27, 7, 61), new GregorianCalendar(1995, 13, 27, 8, 42), defaultCat);  //month 13 is month 1(feb) of the next year
		out.println(td1);											///minute 61 is minute 1 of the next hour

		Event td2 = new Event("TestGregorianCalendar2", new GregorianCalendar(1995, 4, 58, 23, 59), new GregorianCalendar(1995, 6, 27, 34, 42), defaultCat);	//hour 34 is same as 10 am the next day
		out.println(td2);									//day 58 flows over to next month

	}
}
