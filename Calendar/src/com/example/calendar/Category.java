/*
 *
 *
 **/
package com.example.calendar;

import java.io.Serializable;

//import static java.lang.System.*;
//import java.awt.Color;
//import android.graphics.Color; //COLORS ARE REFERENCES TO RES FOLDER, NOT DYNAMICALLY ASSIGNABLE, java.awt.Color off limits

public class Category implements Serializable{

	/**The name of this Category 
	 **/
	String name; //CategoryID?
	/**The color of this Category (in the form 0xAARRGGBB).
	 * The default value is 0xFFABCDEF 
	 **/
	int color = 0xFFABCDEF;	//this default color is a cool-light blue
		//colors are defined in hexadecimal as AARRGGBB (A is alpha, all values are 0-255 -> 00-FF)

	/**Default no-argument constructor. Sets the name of the category to null.
	 * */
	public Category(){
		name = null;	//redundant?
	}
	/**One-argument constructor. Sets the name of the category according the param.
	 * @param name the name for this category object.
	 * */
	public Category(String name){
		this.name = name;
	}
	/**Complete constructor. Sets the name and color of the category according to the params.
	 * @param name the name for this category object
	 * @param color the color in the form 0xAARRGGBB for this category
	 * */
	public Category(String name, int color){
		this.name = name;
		this.color = color;
	}

	/**Simple toString() method that returns string containing ONLY the name of the category.
	 * @return a String object holding this category's name
	 * */
	public String toString(){
	//	return "Category Name:\t" + name +
		//	"\nCategory Color:\t" + color;	//debugging
		return name;	//makes the use in spinner easier
	}
	
	/**Sets the name of the category according to param.
	 * @param name the new name for the category
	 * */
	public void setName(String name){
		this.name = name;
	}
	/**Sets the color of the category according to param.
	 * @param color the new color for the category in form 0xAARRGGBB
	 * */
	public void setColor(int color){
		this.color = color;
	}

	/**Gets the name of the category.
	 * @return String object containing name of this category
	 * */
	public String getName(){
		return name;
	}
	/**Gets the color of the category.
	 * @return integer representing color value of this category
	 * */
	public int getColor(){
		return color;
	}
	
	/**Simple .equals method for comparing two category references. If the names of both 
	 * categories are the same, they are heuristically considered equal.
	 * @param otherCategory a Category object for comparison against "this"
	 * @return boolean value (true if equal, false if not equal).
	 * */
	public boolean equals(Category otherCategory){
		if(this.name.equals(otherCategory.getName())){
			return true;
		}
		return false;
	}
}//end Category class