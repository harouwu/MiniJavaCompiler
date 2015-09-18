package minijava.symboltable;

import java.util.Vector;

public class MyMeth extends MType {
	public String Belong;
	public Vector<MyVar> Paralist;
	public MyVar Return;
	public Vector<MyVar> Vlist;
	
	public MyMeth()
	{
		Belong =null;
		DefPos=0;
		Name=null;
		Paralist = new Vector<MyVar>();
		Vlist = new Vector<MyVar> (); 
		Return = new MyVar();
	}
	
	public boolean AddVar(MyVar v)
	{
		Vlist.addElement(v);
		return true;
	}
	
	public boolean AddPara(MyVar v)
	{
		Paralist.addElement(v);
		return true;
	}
	
	public MyMeth Copymeth(MyMeth m)
	{
		MyMeth _ret = new MyMeth();
		int i=0;
		
		_ret.DefPos=m.DefPos;
		_ret.Name=m.Name;
		_ret.Return = m.Return;
		
		for (i=0;i<m.Vlist.size();i++)
		{
			_ret.Vlist.addElement(m.Vlist.get(i));
		}
		
		for (i=0;i<m.Paralist.size();i++)
		{
			_ret.Paralist.addElement(m.Paralist.get(i));
		}
		return _ret;
	}

	
}
