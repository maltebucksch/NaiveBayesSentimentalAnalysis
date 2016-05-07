package io;

import rating_model.Review;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class XMLOutput {

	public static void output(ArrayList<Review> reviews , String path, String tag){
		
		File file = new File(path);
		String content;
		byte [] buff=new byte[]{};  
		
		try{
			FileOutputStream fout=new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			String begin = "<"+tag+">\n";
			String end = "</"+tag+">";
			buff = begin.getBytes();
	        fout.write(buff, 0, buff.length);  
			
	        for(Review review : reviews) {
				content = review.toXML();
				
		        buff=content.getBytes();  
		        fout.write(buff, 0, buff.length);  
			}
			
			buff = end.getBytes();
	        fout.write(buff, 0, buff.length);  
	        
			fout.flush();
	    	fout.close();
	    	System.out.println("Finished Writing!");
		}
	    catch (IOException e) {  
	        e.printStackTrace();  
	    }
		return;
	}
    
}
