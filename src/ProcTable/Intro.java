package ProcTable;
//ЦёБо


import java.util.Vector;

public class Intro {
	
	String Des;
	Vector Src;
	int Pos;
	Vector<String> Live;
	
	public Intro()
	{
		Live = new Vector<String>();
		Pos = 0;
		Des = null;
		Src = new Vector();
	}
	
	public void Merge(Vector<String> pass)
	{
		int i=0,j=0;
		if (pass.contains(Des))
		{
			pass.remove(Des);
		}
		if (Src!=null)
		{
			for (j=0;j<Src.size();j++)
			{
				if (!pass.contains((String)Src.get(j)))
				{
					pass.add((String)Src.get(j));
				}
			}
		}
		for (i=0;i<pass.size();i++)
		{
			if (!Live.contains(pass.get(i)))
			{
				Live.add(pass.get(i));
			}
		}
	
	}
	
	public Intro(int pos,String d, Vector v)
	{
		Live = new Vector<String>();
		Pos = pos;
		Des = d;
		Src = v;
	}
}
