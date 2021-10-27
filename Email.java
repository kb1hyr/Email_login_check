import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
This program gets a list of usernames from a copy of /etc/passwd and then
checks against a copy of imap log for logins.  
<br>
This is a way to test if a given user has used their email during the period
covered by the log.   


@author Brett Markham
@version 1
*/
public class Email {

    static String etcPasswd = ""; //insert full path to the local copy of the /etc/passwd file here
    static String imapFile = ""; //insert full path to the local copy of the imap.log file here

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int totalnames = 0;
		int totallogins = 0;
		int totalmatches = 0;
		
		ArrayList<String> userNames = getNames(etcPasswd);
		
		
		for (String name: userNames) {
			System.out.println(name);
			totalnames++;
		}
		
		ArrayList<String> logins = getLogins(imapFile);
		
		for (String login: logins) {
			System.out.println(login);
			totallogins++;
		}
		
		System.out.println("Done logins, starting search");
		
		ArrayList<String> usedNames = new ArrayList<String>();
		
		for (String searchString: userNames) {
			System.out.println("Searching for " + searchString);
			if (logins.contains(searchString)) {
					usedNames.add(searchString);
					totalmatches++;
					System.out.println("Found match for " + searchString);
					continue;
				}
			
		}
		
		System.out.println(totalnames);
		System.out.println(totallogins);
		System.out.println(totalmatches);
		

	}
	
	
	/**
	Gets all of the usernames from a copy of an /etc/passwd file
	
	@param filename Full path to the file
	@return An array list of the names
	*/
	public static ArrayList<String> getNames(String filename) {
		FileInputStream stream = null;
        try {
            stream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String strLine;
        String name;
        ArrayList<String> lines = new ArrayList<String>();
        try {
            while ((strLine = reader.readLine()) != null) {
            	if (strLine.contains(":")) {
            		int spot = strLine.indexOf(":");
            		name = strLine.substring(0, spot);
         			lines.add(name);
                    name ="";
            	}                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
	
	/**
	Searches an imap.log file for all instances of a successful login and returns the
	usernames for those logins
	
	@param filename Full path to the imap.log file
	@return An array list of usernames with successful logins
	*/ 
	public static ArrayList<String> getLogins(String filename) {
		FileInputStream stream = null;
        try {
            stream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String strLine;
        ArrayList<String> lines = new ArrayList<String>();
        try {
            while ((strLine = reader.readLine()) != null) {
            	if (strLine.contains("LOGIN") && !strLine.contains("FAILED")) {
            		int theStart = strLine.indexOf("user") + 5;
            		int theEnd = strLine.indexOf(",", theStart);
            		String login = strLine.substring(theStart, theEnd);
            		lines.add(login);
            	}                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
