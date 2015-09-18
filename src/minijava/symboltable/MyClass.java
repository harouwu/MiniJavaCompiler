package minijava.symboltable;

import java.util.Vector;

public class MyClass extends MType {
	public Vector<MyVar> Vlist;
	public Vector<MyMeth> Mlist;
	public String Father;
	
	public MyClass (String n)
	{
		DefPos=0;
		Name=n;
		Vlist = new Vector<MyVar>();
		Mlist = new Vector<MyMeth>();
		Father = null;
	}
	public boolean AddVar( MyVar v)
	{
		Vlist.addElement(v);
		return true;
	}
	public boolean AddMeth( MyMeth m)
	{		
		Mlist.addElement(m);
		return true;
	}
	
	public MyClass CopyClass (MyClass c)
	{
		MyClass _ret = new MyClass(c.Name);
		_ret.DefPos= c.DefPos;
		_ret.Father =c.Father;
		MyMeth temp=new MyMeth();
		int i=0;
		for (i=0;i<c.Vlist.size();i++)
		{
			_ret.Vlist.addElement(c.Vlist.get(i).CopyVar());
		}
		for(i=0;i<c.Mlist.size();i++)
		{
			temp = temp.Copymeth(c.Mlist.get(i));
			_ret.Mlist.addElement(temp);
		}
		return _ret;
	}
	
	public boolean JudgeCircle()
	{
		MyClass father = null;
		Vector<String> Saver=new Vector<String>();
		String Temp=Father;
		int i=0;

		//System.out.println("Name=="+Name);
		Saver.addElement(Name);
		Saver.addElement(Temp);
		while(Temp!=null)
		{
			father = MyCollector.SearchClass(Temp);
			if (father==null)
			{
				MyCollector.Errs.addElement("Line "+this.DefPos+": There is no class named "+Temp+" to Extends for "+Name);
				return false;
			}
			Temp=father.Father;
			if(Saver.contains(Temp)==true)
			{
				return true;
			}
			Saver.addElement(Temp);
			
			//System.out.println("Temp=="+Temp);
		}
		return false;
	}
	
	public MyClass Ex  (String Name)
	{
		if (Father==null)
		{
			return this;
		}
		
		if (JudgeCircle()==true)
		{
			MyCollector.Errs.add("Line "+this.DefPos+": Extend Fail! Circle");
			return this;
		}
		
		MyClass father = MyCollector.SearchClass(Father);
		int i=0;
		int j=0;
		int k=0;
		if (father==null)
		{
			return this;
		}
		father = father.Ex(father.Father);
		
		Vector<MyVar> temp = new Vector<MyVar>();
		for (i=0;i<father.Vlist.size();i++)
		{
			temp.addElement(father.Vlist.get(i).CopyVar());
		}
		
		for (i=0;i<Vlist.size();i++)
		{
			for (j=0;j<father.Vlist.size();j++)
			{
				if (father.Vlist.get(j).Name == Vlist.get(i).Name)
				{
					temp.setElementAt(Vlist.get(i).CopyVar(),j);
					break;
				}
			}
			if (j==father.Vlist.size())
			{
				temp.addElement(Vlist.get(i));
			}
		}
		
		Vlist = temp;
		
		Vector<MyMeth> methtemp = new Vector<MyMeth>();
		for (i=0;i<father.Mlist.size();i++)
		{
			methtemp.addElement(father.Mlist.get(i));
		//	System.err.println(Name);
		}
		
		for (i=0;i<Mlist.size();i++)
		{
			for (j=0;j<father.Mlist.size();j++)
			{
				if (Mlist.get(i).Name == father.Mlist.get(j).Name)
				{
					if (Mlist.get(i).Return.Type != father.Mlist.get(j).Return.Type)
					{
						MyCollector.Errs.addElement("Line "+Mlist.get(i).DefPos+": "+Mlist.get(i).Name+" Not Return the same Type");
						break;
					}
					if (Mlist.get(i).Paralist.size() != father.Mlist.get(j).Paralist.size())
					{
						MyCollector.Errs.addElement("Line "+Mlist.get(i).DefPos+": "+Mlist.get(i).Name+" Para Number not match");
						break;
					}
					for (k=0;k<Mlist.get(i).Paralist.size();k++)
					{
						if (Mlist.get(i).Paralist.get(k).Type != father.Mlist.get(j).Paralist.get(k).Type)
						{
							MyCollector.Errs.addElement("Line "+Mlist.get(i).DefPos+": "+Mlist.get(i).Name+" Paras not match");	
							break;
						}
					}
					methtemp.setElementAt(Mlist.get(i),j);
				//	System.err.println(Name);
					break;
				}
			}
			if (j==father.Mlist.size())
			{
				methtemp.addElement(Mlist.get(i));
			//	System.err.println(Name);
			}
		}
		Mlist = methtemp;
		return this;
	}

}
