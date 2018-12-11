# NetBinaryFormatter
Create java formatter from C# BinaryFormatter

This project has two methods. 
-Format / java object to c# object /
-Parse / c3 object bytes to java object/

# Usage
-Format
  byte[] formattedBytes = new BinaryFormatter().format(bytes);
  OR
  byte[] formattedBytes = new BinaryFormatter(bytes).format();
-Parse
  new BinaryFormatter().parse(bytes);

Convert types:
byte, boolean, char, short, int, long, float, double, string and array
