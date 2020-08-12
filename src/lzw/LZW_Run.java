package lzw;

import java.io.IOException;

/**
 * Main class to run LZW functions on CLI
 *
 */
public class LZW_Run {

	public static void main(String[] args) 
	{
		int i = 15;
		int j = 255;
		int twelve_bit = ((i & 15) << 8) | j;
		System.out.println("Ans: " + twelve_bit);
		
		int x = 4;
		int y = 129;
		int z = 244;
		char twelve_bit2 = (char) ((x << 4) | ((y & 240) >>> 4));
		int twelve_bit3 = ((y & 15) << 8) | z;
		System.out.println("Ans2: " + twelve_bit2);
		System.out.println("Ans3: " + twelve_bit3);
		
		// TODO Command line program to run compress using user provided input/output files
		try 
		{
			System.out.println("OUTPUT: " + LZW.decompress("input_data/compressedfile2.z"));
		} 
		catch (OutOfMemoryError | SecurityException | IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(LZW.decompress(LZW.compress("Hello my name is Basssssssil regi")));
		
	}

}
