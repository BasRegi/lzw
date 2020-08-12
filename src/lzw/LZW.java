package lzw;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LZW compression and decompression library to convert .z files into .txt files and vice versa
 *
 */
public class LZW 
{
	
	private static final boolean debug = false;
	private static final boolean detailed_debug = false;
	
	/**
	 * Function to apply LZW decompression on a specified file and then write out to a specified location
	 * @param inputFilePath File path of input file
	 * @param outputFilePath File path of output file - File is created if it doesn't exist
	 * @throws IOException When file could not be read from
	 * @throws OutOfMemoryError When file is too large to allocate enough memory for it
	 * @throws SecurityException When the user does not have the correct access rights to read file
	 */
	public static void decompress(String inputFilePath, String outputFilePath) throws IOException, OutOfMemoryError, SecurityException
	{
		String text = decompress(inputFilePath);
		Path path = Paths.get(outputFilePath);
		if(!Files.exists(path)){
		    Files.createFile(path);
		}
		BufferedWriter writer = Files.newBufferedWriter(path);
		writer.write(text);
		writer.flush();
		writer.close();
		
	}
	
	/**
	 * Function to apply LZW decompression on a specified file using its path
	 * @param inputFilePath File path of the input file
	 * @return String representing the text after decompression
	 * @throws IOException When file could not be read from
	 * @throws OutOfMemoryError When file is too large to allocate enough memory for it
	 * @throws SecurityException When the user does not have the correct access rights to read file
	 */
	public static String decompress(String inputFilePath) throws IOException, OutOfMemoryError, SecurityException
	{
		String text = "";
		
		if(debug) System.out.println("Reading from input file: " + inputFilePath);
		
		//Read bytes from file
		byte[] input = Files.readAllBytes(Paths.get(inputFilePath));
		
		//Decode input and append to return string
		text += decompress(input);
		
		return text;
	}
	
	/**
	 * Function to apply LZW decompression using a byte array read in from file
	 * @param input byte array containing contents of compressed file
	 * @return String representing text after decompression
	 */
	public static String decompress(byte[] input)
	{
		String text = "";
		
		if(debug) System.out.println("Getting codes from input...");
		//Convert byte array into list of integer codes
		List<Integer> codes = getCodes(input);
		
		//Decode list and append to return string
		text += decompress(codes);
		
		return text;
	}
	
	/**
	 * Function to apply LZW decompression using a list of Integer codes
	 * @param codes List of Integer codes
	 * @return String representing text after decompression
	 */
	public static String decompress(List<Integer> codes)
	{
		//Initialise dictionary
		if(debug) System.out.println("Initialising dictionary...");
		Map<Integer, String> dict = buildDictD();
		
		String text = decompress_helper(codes, dict);
		return text;
	}
	
	/**
	 * Function to apply LZW decompression algorithm on a list of codes using a given dictionary
	 * @param codes List of Integer codes read in from file
	 * @param dict Dictionary mapping Intger codes to String patterns
	 * @return String representing the text after decompression
	 */
	private static String decompress_helper(List<Integer> codes, Map<Integer, String> dict)
	{
		if(debug) System.out.println("Decoding codes...");
		String text = dict.get(codes.remove(0));
		StringBuilder result = new StringBuilder(text);
		int dictSize = dict.size();
		
		for (int code : codes) {
			if(dict.size() > 4095)
			{
				dict = buildDictD();
				dictSize = dict.size();
			}
            String entry;
            if (dict.containsKey(code))
            {
                entry = dict.get(code);
            }
            else if(code == dictSize)
            {
                entry = text + text.charAt(0);
            }
            else
            {
            	throw new IllegalArgumentException("\nBad Code: " + code);
            }

            result.append(entry);

            dict.put(dictSize++, text + entry.charAt(0));

            text = entry;
        }
		
		if(debug) System.out.println("Decode Sucessful!");
        return result.toString();
	}
	
	/**
	 * Function to convert a list of bytes into a list of 12-bit int codes
	 * @param input List of bytes read in from file
	 * @return List of Integer codes - the decimal values of each 12 bit byte in the input
	 */
	public static List<Integer> getCodes(byte[] input)
	{
		List<Integer> codes = new ArrayList<Integer>();
		int offset = 1;
		
		//Empty file
		if(input.length < 2)
		{
			return codes;
		}
	
		//Check if last byte has been padded
		if(input.length % 3 != 0)
		{
			offset = 3;
		}
		
		//Decode bytes and add to list of codes
		for(int i = 0; i < input.length - offset; i += 3)
		{
			int value = get12BitValue(input[i], input[i+1], true);
			int value2 = get12BitValue(input[i+1], input[i+2], false);
			codes.add(value);
			codes.add(value2);
		}
		
		//Add final byte decoded to list
		if(offset == 3) codes.add(get12BitValue(input[input.length - 2], input[input.length - 1], false));
		
		if(detailed_debug) System.out.println("Codes: " + codes);
		return codes;
	}
	
	/**
	 * Function takes two bytes and returns the 12-bit int value. A boolean is used to determine which bits to use from each byte
	 * If firsthalf is true then use byte one and first half of byte two
	 * else use second half of byte one and byte two
	 * @param one First byte
	 * @param two Second byte
	 * @param firsthalf Boolean to determine which bytes to use
	 * @return 12-bit int value
	 */
	public static int get12BitValue(byte one, byte two, boolean firsthalf)
	{
		int twelve_bit;
		
		//Convert bytes to binary strings
		String bin_one = Integer.toBinaryString(one);
		String bin_two = Integer.toBinaryString(two);
		
		//Pad/remove bits till correct size
		while(bin_one.length() < 8)
		{
			bin_one = "0" + bin_one;
		}
		if(bin_one.length() > 8)
		{
			bin_one = bin_one.substring(bin_one.length() - 8);
		}
		
		while(bin_two.length() < 8)
		{
			bin_two = "0" + bin_two;
		}
		if(bin_two.length() > 8)
		{
			bin_two = bin_two.substring(bin_two.length() - 8);
		}
		
		//Select bits to use
		if(firsthalf)
		{
			//All of first bit and second half of second bit
			twelve_bit = Integer.parseInt(bin_one + bin_two.substring(0, 4), 2);
		}
		else
		{
			//Second half of first bit and all of second bit
			twelve_bit = Integer.parseInt(bin_one.substring(4) + bin_two, 2);
		}
		if(detailed_debug) System.out.println("(" + one + "," + two + ") -> " + twelve_bit);
		
		//if(twelve_bit < 0) twelve_bit = ~twelve_bit | 1;
		return twelve_bit;
	}
	
	/**
	 * Function to compress given string into list of codes
	 * FOR TESTING PURPOSES
	 * @param uncompressed String to be compressed
	 * @return List of Integers representing the codes to be written to compressed file
	 */
	public static List<Integer> compress(String uncompressed) 
	{
        //Initialise the dictionary
        Map<String,Integer> dict = buildDictC();
 
        String text = "";
        List<Integer> result = new ArrayList<Integer>();
        
        for (char c : uncompressed.toCharArray()) 
        {
        	if(dict.size() > 4095)
        	{
        		dict = buildDictC();
        	}
        	
            String entry = text + c;
            if (dict.containsKey(entry))
            {
                text = entry;
            }
            else 
            {
                result.add(dict.get(text));
                // Add entry to the dictionary.
                dict.put(entry, dict.size());
                text = "" + c;
            }
        }
 
        //Output the code for text.
        if (!text.equals(""))
        {
            result.add(dict.get(text));
        }
        return result;
    }
	
	/**
	 * Function to initialise dictionary with single characters for decompression
	 * @return Map<Integer, String> key value pairs
	 */
	private static Map<Integer, String> buildDictD()
	{
		Map<Integer, String> dict = new HashMap<Integer, String>();
		for(int i = 0; i < 256; i++)
		{
			dict.put(i, "" + (char)i);
		}
		return dict;
	}
	
	/**
	 * Function to initialise dictionary with single characters for compression
	 * @return Map<String, Integer> key value pairs
	 */
	private static Map<String, Integer> buildDictC()
	{
		Map<String,Integer> dict = new HashMap<String,Integer>();
        for (int i = 0; i < 256; i++)
        {
            dict.put("" + (char)i, i);
        }
        return dict;
	}

}
