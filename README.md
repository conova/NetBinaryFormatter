# NetBinaryFormatter
Create java formatter from C# BinaryFormatter

This project has two methods. <br> 
-Format / java object to c# object /<br> 
-Parse / c3 object bytes to java object/<br> 

# Usage
-Format<br> 
  byte[] formattedBytes = new BinaryFormatter().format(bytes); <br> 
  OR<br> 
  Object formattedBytes = new BinaryFormatter(bytes).format();<br> 
-Parse<br> 
  new BinaryFormatter().parse(bytes);<br> 

# Convert types:
byte, boolean, char, short, int, long, float, double, string and array
