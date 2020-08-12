package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import lzw.LZW;

/**
 * Class to run unit tests on lZW class
 * @author basilregi
 *
 */
public class LZW_Test 
{
	/**
	 * Test to check whether a string compressed into a list of codes can be successfully decompressed to produce the same string
	 */
	@Test
	public void compressDecompressTest()
	{
		String before = "Howdly doodly, I am going to test the decompress method using this reeaaaaaalllyyyy wuite largeeeeeee "
				+ "stringgg \n where there \n\n\n\n are lotssss of reeeeaaapppeeeaaated letters tooooo hopefully makkkee sure it worksss";
		
		String after = LZW.decompress(LZW.compress(before));
		
		assertEquals(before, after);
	}
	
	@Test
	public void get12BitValueTests()
	{
		int x = 255;
		int y = 1;
		int z = 144;
		
		int first = LZW.get12BitValue((byte)x, (byte)x, true);
		assertEquals(4095, first);
		
		int second = LZW.get12BitValue((byte)x, (byte)x, false);
		assertEquals(4095, second);
		
		int third = LZW.get12BitValue((byte)y, (byte)y, true);
		assertEquals(16, third);
		
		int fourth = LZW.get12BitValue((byte)y, (byte)y, false);
		assertEquals(257, fourth);
	}
}
