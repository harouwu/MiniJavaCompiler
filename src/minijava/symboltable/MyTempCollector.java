package minijava.symboltable;

import java.util.Vector;

public class MyTempCollector extends MType{
	public Vector<MType> Saver;
	
	public MyTempCollector ()
	{
		Saver = new Vector();
	}
	public MyTempCollector (Vector v)
	{
		Saver= (Vector) v.clone();
	}
	
	public boolean AddOne(MType o)
	{
		try{
			o.GetName();
		}
		catch(Exception e)
		{
			//System.err.println("Error!");
			return false;
		}
		for (int i=0;i<Saver.size();i++)
		{
			if (o.GetName()==Saver.elementAt(i).GetName())
			{
				MyCollector.Errs.addElement("line "+o.DefPos+": "+o.GetName()+" Double defined");
				return false;
			}
		}
		Saver.addElement(o);
		return true;
	}
	
	public boolean AddOneAt(MType o,int i)
	{
		try{
			o.GetName();
		}
		catch(Exception e)
		{
			//System.err.println("Error!");
			return false;
		}
		for (int j=0;j<Saver.size();j++)
		{
			if (o.GetName()==Saver.elementAt(j).GetName())
			{
				MyCollector.Errs.addElement("line "+Saver.elementAt(j).DefPos+": "+o.GetName()+" Double defined");
				return false;
			}
		}
		Saver.insertElementAt(o,i);
		return true;
	}
}
