package com.matus.lyrics;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;

import com.matus.lyrics.proceed.ImageGetter;
import com.matus.lyrics.proceed.ImageSequenceToGif;



public class LyricsController {
	
	static ImageGetter im = new ImageGetter();
	RestCall restCall = new RestCall();
	
	public static List<String> convertStringToList(String inputString) {
        String[] words = inputString.split("\\s+");
        return Arrays.asList(words);
    }
	
	static String fixLyrics(String justLyrics) {
		 StringBuilder result = new StringBuilder();

	        for (int i = 0; i < justLyrics.length(); i++) {
	            char currentChar = justLyrics.charAt(i);

	            if (i > 0) {
	                char previousChar = justLyrics.charAt(i - 1);
	                if (Character.isLowerCase(previousChar) && Character.isUpperCase(currentChar)) {
	                    result.append(" ");
	                }
	            }
	            result.append(currentChar);
	        }
	        return result.toString();
	    }
	
	@SuppressWarnings("static-access")
	static List<String> eachWordToList(String lyrics) throws IOException, JSONException {
		String[] words = lyrics.split(" ");
		System.out.println("Downloading images...");
		 for (String word : words) 		 
		 {		
			 try {
				 im.execute(word);	
			 }
			 catch (Exception e) 
			 {
				 System.out.println("Error when fetching image.");
				 continue;
			 }			 		 
	     }
		 System.out.println("Images donwloaded.");
		 try {
			 System.out.println("About to start creating gifs");
			 ImageSequenceToGif seq = new ImageSequenceToGif();
			 seq.sequence();
			 System.out.println("Gif created successfully");
		 }
		 catch (Exception e) {
			 System.out.println("Something went wrong...");
	            e.printStackTrace();
	        }
		 
		return null;		
	}
}
