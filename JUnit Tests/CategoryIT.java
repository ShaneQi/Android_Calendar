/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.calendar;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author E4D User
 */
public class CategoryIT {
   
    public CategoryIT() {
    }
   
    @BeforeClass
    
    public static void  initilize() {

        Category instance = new Category();
        
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of toString method, of class Category.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Category instance1 = new Category("Emergency issue");
        String expResult = "Emergency issue";
        String result = instance1.toString();
        assertEquals(expResult, result);
        Category instance2 = new Category("University Courses");
        expResult = "University Courses";
        result = instance2.toString();
        assertEquals(expResult, result);
        
        
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of setName method, of class Category.
     */
    @Test
    public void testSetName() {
        System.out.println("Test SetName");
        Category instance = new Category();
        String expResult = "Go to School";
        instance.setName("Go to School");
        String result = instance.toString();
        assertEquals(expResult, result);
        expResult = "Sam's Birthday";
        instance.setName("Sam's Birthday");
        result = instance.toString();
        assertEquals(expResult, result);
        expResult = "Go shopping";
        instance.setName("Go shopping");
        result = instance.toString();
        assertEquals(expResult, result);
        
        
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setColor method, of class Category.
     */
    @Test
    public void testSetColor() {
        System.out.println("setColor");
        int color = 0;
        Category instance = new Category();
        instance.setColor(color);
        instance.setColor(0xFFABCDE0);
        int expResult =0xFFABCDE0;
        int result =instance.color;
        assertEquals(expResult, result);

        
    }

    /**
     * Test of getName method, of class Category.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Category instance = new Category();
        instance.setName("name is wired");
        String expResult = "name is wired";
        String result = instance.getName();
        assertEquals(expResult, result);
       
        
    }

    /**
     * Test of getColor method, of class Category.
     */
    @Test
    public void testGetColor() {
        System.out.println("getColor");
        Category instance = new Category();
        instance.setColor(0xFFABCDE0);
        int expResult = 0xFFABCDE0;
        int result = instance.getColor();
        assertEquals(expResult, result);

    }

    /**
     * Test of equals method, of class Category.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Category instance = new Category("Cat1");
        Category otherCategory = new Category("Cat2");
        
        boolean expResult = false;
        
        boolean result = instance.equals(otherCategory);
        assertEquals(expResult, result);
  
    }
    
}
