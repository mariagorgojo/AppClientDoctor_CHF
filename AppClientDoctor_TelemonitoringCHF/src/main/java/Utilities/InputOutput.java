/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputOutput {
    
    private static BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    
    // Read console inputs 
    		public static int readInteger() {
	        int num = 0;
	        boolean ok = false;
	        do {
	            try {
	            	num = Integer.parseInt(r.readLine());
	                if (num < 0) {
	                    ok = false;
	                    System.out.print("You didn't type a valid number!");
	                } else {
	                    ok = true;
	                }
	            } catch (IOException e) {
	                e.getMessage();
	            } catch (NumberFormatException nfe) {
	            	System.out.print("You didn't type a valid number!");
	            }
	        } while (!ok);

	        return num;
	    }

		
		public static String readString() {
	        String text = null;
	        boolean ok = false;
	        do {
	            try {
	            	text = r.readLine();
	                if (!text.isEmpty()) {
	                    ok = true;
	                } else {
	                    System.out.println("Empty string, please try again:");
	                }
	            } catch (IOException e) {

	            }
	        } while (!ok);

	        return text;
	    }
    
    
}
