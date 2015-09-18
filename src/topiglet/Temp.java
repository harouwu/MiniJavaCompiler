package topiglet;

public class Temp {
	static int num;
	
	int mynum = 0;
	Temp()
	{
		
		num++;
		mynum = num;
	}
	Temp(int number)
	{
		 mynum = number;
	}
	public String toString()
	{
		return "TEMP "+mynum;
	}
	public static int getNumber()
	{
		return num;
	}
}
