package tospiglet;

public class SpigletTemp {
	static int num;
	int NowNum = 0;
	SpigletTemp()
	{
		
		num++;
		NowNum = num;
	}
	SpigletTemp(int number)
	{
		NowNum = number;
	}
	public String toString()
	{
		return "TEMP "+NowNum;
	}
	public static int getNumber()
	{
		return num;
	}
}
