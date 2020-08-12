package lzw;

import java.io.IOException;
import java.util.Scanner;

/**
 * Main class to run LZW functions on CLI
 *
 */
public class LZW_Run {

	public static void main(String[] args) 
	{
		if(args.length < 2)
		{
			printUsage();
			System.out.println("EXITING...");
			System.exit(0);	
		}
		
		switch(args[0])
		{
			case "-d":
				if(args.length > 3)
				{
					printUsage();
					break;
				}
				if(args.length == 3)
				{
					try 
					{
						LZW.decompress(args[1], args[2]);
					} 
					catch (OutOfMemoryError | SecurityException | IOException e) 
					{
						System.out.println("EXCEPTION");
						e.printStackTrace();
						break;
					}
					
				}
				else
				{
					try 
					{
						System.out.println("OUTPUT: " + LZW.decompress(args[1]));
					} 
					catch (OutOfMemoryError | SecurityException | IOException e) 
					{
						System.out.println("EXCEPTION");
						e.printStackTrace();
						break;
					}
				}
				break;
			case "-c":
				Scanner in = new Scanner(System.in);
				System.out.print("Input text to compress and press Enter: ");
				String text = in.nextLine();
				in.close();
				System.out.println("OUTPUT: " + LZW.compress(text));
				break;
			default:
				printUsage();
				break;
		}
		
		System.out.println("EXITING...");
		
	}
	
	private static void printUsage()
	{
		System.out.println("Invalid arguments");
		System.out.println("USAGE:");
		System.out.println("\t- java LZW_Run -c");
		System.out.println("\t- java LZW_Run -d <input-file> <output-file>");
	}

}
