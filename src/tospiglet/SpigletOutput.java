package tospiglet;

public class SpigletOutput {
	static String output = "";
	static String Space = "";
	public static void add(String s)
	{
		//System.out.print(s);
		output +=Space+s;
	}
    public static String getSpigletOutput()
	{
		return output;
	}

}
