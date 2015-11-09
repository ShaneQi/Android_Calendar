package com.example.calendar;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CategoryManagerActivity extends Activity implements OnItemSelectedListener {

	private Spinner categorySpinner;
	
	private EditText	nameEditText,
						redEditText,
						greenEditText,
						blueEditText;
	
	private Category selectedCategory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_manager);
		
		attachViewsById();
		setupCategorySpinner();	//when category changes, update the rgb
		setupEditTexts();
	}
	
	private void attachViewsById(){
		categorySpinner = (Spinner)findViewById(R.id.spinnerCategories);

		nameEditText = (EditText)findViewById(R.id.editTextCategoryName);
		redEditText = (EditText)findViewById(R.id.editTextRed);
		greenEditText = (EditText)findViewById(R.id.editTextGreen);
		blueEditText = (EditText)findViewById(R.id.editTextBlue);
	}
	
	private void setupCategorySpinner(){
		ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item); //, arrayOfData
		adapter.addAll(EventManager.getCategories());
		categorySpinner.setAdapter(adapter);

		selectedCategory = EventManager.getCategories().get(0);	//must have a selectedCategory at all times
		
		categorySpinner.setOnItemSelectedListener(this); // our activity implements OnItemSelectedListener
		
		System.out.println("I set up category spinner");
	}
	
	private void setupEditTexts(){
		nameEditText.setText(selectedCategory.getName());
		
		redEditText.setText("" + 	Color.red(selectedCategory.getColor()));	//the "" + is to convert the arg into a String (if it's an int, it reads as a resource id address)
		greenEditText.setText("" + 	Color.green(selectedCategory.getColor()));	
		blueEditText.setText("" + 	Color.blue(selectedCategory.getColor()));
	}
	
	public void saveChangeOnClick(View v){
		String newName = nameEditText.getText().toString();
		if(newName == null || newName.isEmpty()){
			Toast.makeText(getApplicationContext(), "Invalid name; Changes not saved", Toast.LENGTH_LONG).show();
			return;
		}
		if(!currentColorIsValid()){
			return;
		}
		selectedCategory.setName( nameEditText.getText().toString());
		//make sure to conform to 0-255 range and 0xFF...... 
		//opaqueness handled by int Color.rgb(r,g,b) and apparently also conforms using int%255 as well
		int red = Integer.parseInt(redEditText.getText().toString() );
		int blue = Integer.parseInt(blueEditText.getText().toString() );
		int green = Integer.parseInt(greenEditText.getText().toString() );
 		selectedCategory.setColor(	Color.rgb(red, green, blue)	);
 		//refresh
 		setupCategorySpinner();
 		setupEditTexts();
	}
	
	public void addCategoryOnClick(View v){

		String newName = nameEditText.getText().toString();
		if(newName == null || newName.isEmpty()){
			Toast.makeText(getApplicationContext(), "Invalid name; New category not created", Toast.LENGTH_LONG).show();
			return;
		}
		for(Category category: EventManager.getCategories()){
			if(newName.compareTo(category.getName()) == 0){	//doesn't account for possible whitespaces before after the name making it "different"
				Toast.makeText(getApplicationContext(), "Another category already exists with that name; New category not saved", Toast.LENGTH_LONG).show();
				return;
			}
		}
		if(!currentColorIsValid()){
			return;
		}
		int red = Integer.parseInt(redEditText.getText().toString() );
		int blue = Integer.parseInt(blueEditText.getText().toString() );
		int green = Integer.parseInt(greenEditText.getText().toString() );
		
		Category newCategory = new Category(newName, Color.rgb(red, green, blue) );
		EventManager.addCategory(newCategory);
		selectedCategory = newCategory;
		//refresh
		setupCategorySpinner();
		setupEditTexts();
	}
	
	public void deleteCategoryOnClick(View v){
		//if no events are in category, 
			//if category is not default category (or EventManager.getCategories.size()!=1, then delete it
		//update the categorySpinner and the EditTexts
		ArrayList<Event> eventsList = EventManager.getEvents();
		for(Event e: eventsList){
			if(e.getCategory().equals(selectedCategory) ){	//IF there are any events in the selectedCategory,
				//SEND TOAST SAYING EVENTS STILL IN CATEGORY, THEN ABORT DELETION METHOD
				Toast.makeText(getApplicationContext(), "Please remove all events from this category first!", Toast.LENGTH_LONG).show();
				return;
			}
		}
		if(EventManager.getCategories().size() <= 1){ //is 1
			Toast.makeText(getApplicationContext(), "There must always be a category; Category not deleted.", Toast.LENGTH_LONG).show();
			return;
		}
		EventManager.removeCategory(selectedCategory);
		setupCategorySpinner();
		setupEditTexts(); //because selectedCategory must change after the preceding one is deleted
	}

	private boolean currentColorIsValid(){
		String redStr = redEditText.getText().toString();
		String greenStr = greenEditText.getText().toString();
		String blueStr = blueEditText.getText().toString();
		if(redStr == null || redStr.isEmpty() ||
					greenStr == null || greenStr.isEmpty() ||
					blueStr == null || blueStr.isEmpty()){
			Toast.makeText(getApplicationContext(), "Invalid color value(s); Please enter values from 0-255", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		selectedCategory = (Category)categorySpinner.getSelectedItem();		//Update the selected category
		setupEditTexts();											//then change the rgb edittexts, so they correspond to the newly selected category.
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

}//end CategoryManagerActivity
