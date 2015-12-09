/*
Benjamin Wu
CISC 3150 - HW10
Create Basic Shell with:
1. Ending & to execute program + return, (non-blocking call) 
2. Not ending with &, wait for end of program to return to shell, and 
3. redirection of > for program output.

Comments: For the &, I had a method where it just removes the last char of the string and returned the substring.
Screwed myself force shutdown cause i forgot to put user input in my while loop while testing.

*/

import java.io.*;
import java.util.*;

public class Shell
{
public static String my_string = "";

public static void main (String[] args)
	{
	System.out.print("myShell>");
	Scanner in = new Scanner(System.in);
	my_string = (in.next());
	while (my_string.endsWith("&"))
		{
		try
			{
			String my_string4Process = removeAMPER(my_string);
			ProcessBuilder pb = new ProcessBuilder(my_string4Process);
			pb.start();
			System.out.print("myShell>");
			my_string = (in.next()); //I fucked myself over when I didn't have this in here.
						 //Infinite loop opening notepad. with the input outside the loop. had to force shutdown manually. 
						 //this is because in the while loop, it never waits to ask for another input lol.
			}
		catch (IOException ex)
			{
			System.out.println("IOException occured");
			}

		}
	while (!my_string.endsWith("&"))
		{
		try
			{
			ProcessBuilder pb = new ProcessBuilder(my_string);
			Process p = pb.start();
			p.waitFor();
			System.out.print("myShell>");
			my_string = (in.next()); //need to add this! be careful! after the process runs and waits for it.
			}
		catch (IOException ex)
			{
			System.out.println("IOException occured");
			}
		catch (InterruptedException ex)
			{
			System.out.println("InterruptedException caught");
			}
		}
	}//EndofMain
/***Method to remove the Ampersand sign so it wouldn't have the IOException in the ProcessBuilder***/
	public static String removeAMPER(String str)
		{
		if (str.length() > 0 && str.charAt(str.length()-1)=='&')
			{
			str = str.substring(0, str.length()-1);
			return str;
			}
		else
			{
			return str;
			}
		}
}//EndOfClass
