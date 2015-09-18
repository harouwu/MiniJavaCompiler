package minijava.symboltable;

public class MyVar extends MType {
	public String Type;
	public boolean isArray;
	public int size;
	public String Val;
	public int number;
	public MyVar()
	{
		DefPos=0;
		Type="Un Known";
		Name=null;
		isArray=false;
		size=0;
		Val=null;
		number=20;
	}
	
	public MyVar(String n,String t,int num)
	{
		DefPos=0;
		Type=t;
		Name=n;
		isArray=false;
		size=0;
		Val=null;
		number=num;
	}
	
	public MyVar CopyVar()
	{
		MyVar _ret = new MyVar();
		_ret.Name = Name;
		_ret.Type = Type;
		return _ret;
	}
	
	public String GetName()
	{
		return Name;
	}
	public int GetNumber()
	{
		return number;
	}

}
