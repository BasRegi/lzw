# LZW Compression

## How to use
### LZW Library
The LZW class has been designed so that it can be used as library class as part of another application if you choose to use it. The functions available as part of this class are:
* `decompress(String inputFilePath, String outputFilePath)` - This function will open the .z file at _inputFilePath_, read the contents, decompress it and write the contents to _outputFilePath_ as a .txt file.
* `decompress(String inputFilePath)` - This function will do the same as above but will return a _String_ of the text contained in the .z file rather than writing out.
* `decompress(byte[] input)` - This function does the same as above but works on a _byte array_ rather than an file path - Used once contents have been read in
* `decompress(List<Integer> codes)` - This function does the same as above but works on a list of integer codes that have been converted from a .z file
* `compress(String uncompressed)` - This function is used to compress a _String_ using the *LZW* algorithm and return a list of integer codes that can be written to a .z file. Implemented as part of this project to carry out some testing.

In addition to these I have also made the following functions public, should anyone wish to use it, but they are used as helper functions as part of the *LZW* decompression:
* `getCodes(byte[] input)` - This function converts a byte array into a list of integer codes using 12-bit ints
* `get12BitValue(byte one, byte two, boolean firsthalf)` - This function combines two bytes to return a 12-bit integer value. _Boolean firsthalf_ determines which bits to drop from which byte. If _firsthalf_ is true then use byte one and first half of byte two else use second half of byte one and byte two.

### LZW_Run
Alongside the *LZW* library, I have also implemented a basic CLI for using the LZW functions directly via Command Line. This can be used in the following ways:
* `>> java LZW_Run -c"` - Which will then ask for a string input to compress
* `>> java LZW_Run -d <input-file>` - Which will print out the contents of the input file in the command line
* `>> java LZW_Run -d <input-file> <output-file>` - Which will write out the human readable text from the input file to the output file

## Notes
* I have implemented some very basic unit tests to ensure that various aspects of the implementation work. Due to time constraints, I was unable to implement any further tests. But given more time I would like to rigorously test the implementation with more edge cases.
* The current implementation only deals with files up to approx 2GB as this is the max number of bytes that can be read into an array. I thought this would be sufficient to deal with majority of .txt files. However to expand on this, and make it more usable for larger files I would read in the file chunk by chunk and decode one chunk at a time. Should be a straightforward change to make if required.
* Java is the language I am most confident in using, which is why I chose it for this project. However, it is not very straightforward to do bitwise operations in Java, especially for unsigned integers as this isn't supported in Java, which is why I chose to use a Binary string representation based approach. However given more time I would like to find out how to do this directly using bitwise operations in Java!

#### _If you have any questions or would like to discuss any part of this implementation further, please feel free to contact me via email at 08regib@googlemail.com_ 
