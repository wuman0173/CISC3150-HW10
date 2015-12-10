/*
Benjamin Wu
CISC 3150 - HW10
Create Basic Shell with:
1. Ending & to execute program + return, (non-blocking call) 
2. Not ending with &, wait for end of program to return to shell, and 
3. redirection of > for program output.

Comments: For the &, I had a method where it just removes the last char of the string and returned the substring.
Screwed myself force shutdown cause i forgot to put user input in my while loop while testing.
The > redirection operator is done without tokens, used Strings and Split it with '\\>' , so no spaces
USE "hello.o>output.txt"  NO SPACES!... didn't implement with spaces. So, I kinda cheated.

Got help in figuring out how to redirect the program into the file with: 
http://www.avajava.com/tutorials/lessons/how-do-i-redirect-standard-output-to-a-file.html
http://stackoverflow.com/questions/16452964/how-do-i-execute-dos-commands-in-java/16453653#16453653

Still having a small problem after redirecting the exe to an output file, I need another User Input to stop the Java Program
Also, I should have a label or something so it can go in a big loop if I wanted to redirect something, then run something else afterwards.
This just goes in order (need labels?), if it ends with &, if it doesnt, and if its redirect. So I can't be doing... "notepad, notepad&" or "helloworld.exe>output.txt, notepad" orders
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
	while (my_string.endsWith("&") && !my_string.contains(">"))
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
	while (!my_string.endsWith("&") && !my_string.contains(">"))
		{
		try
			{
			ProcessBuilder pb = new ProcessBuilder(my_string);
			Process p = pb.start();
			p.waitFor();
			System.out.print("myShell>");
			my_string = (in.next()); 	/*****need to add this! be careful! after the process runs and waits for it.********
							******need to add a label here? to go back up? because if it goes down here*********
							*after doing notepad&, notepad, then going back to notepad&, it would end program.*/
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
while (my_string.contains(">") && !my_string.contains("&"))  //REDIRECTION CODE IS HERE!
	{
	if (my_string.contains(">") && !my_string.contains("&"))
		{
		String parts[] = my_string.split("\\>");
		//System.out.println(parts[0] + " " + parts[1]); //test
		String program = parts[0];
		String redirectedTO = parts[1]; //easier reading
		try
			{
			File my_file = new File(redirectedTO);
			FileOutputStream fos = new FileOutputStream(my_file); 	//used that split, changed the name, then used as the output name.
			PrintStream printer = new PrintStream(fos); 		//uses the printStream to print stuff out
			System.setOut(printer); 				//setOut is to redirect to a file...?
			ProcessBuilder pb = new ProcessBuilder(program); 	//this to run the program
			pb.redirectError(); 					//I need this! for redirection!
			Process p = pb.start();
			InputStream is = p.getInputStream(); 
			int value = -1;						//I copied some code here for reading the input
			while ((value = is.read()) != -1) 
				{
           			 System.out.print((char) value);
       				 }
       			int exitCode = p.waitFor();				//I need these 2 lines for some strange reason
			my_string = (in.next()); 				//If I dont have this line, It wont print out to txt file
			//System.out.print("myShell>"); //Since I used pb.redirectError(), this goes in file too.
			}
		catch (IOException ex)
			{
			System.out.println("OIException occured yeah.");
			}
		catch (InterruptedException ex)
			{
			System.out.println("Interruption Caught");
			}
		}
	else 
		{
		System.out.println("Something went HORRIBLY WRONG.");
		System.exit(0);
		}
	}//EndofWhileLoop
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
