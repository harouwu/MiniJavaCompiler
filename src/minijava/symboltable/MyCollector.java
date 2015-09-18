package minijava.symboltable;

import java.util.Vector;

public class MyCollector {
	public static MyClass mainclass=new MyClass("Temp");
	public static Vector<MyClass> classes = new Vector<MyClass>();
	public static Vector<String> Errs= new Vector<String>();
	
	public static void Extends()
	{	
		for (int k=0;k<classes.size();k++)
		{
			classes.setElementAt( classes.elementAt(k).Ex(classes.elementAt(k).Name),k);
		}
	}
	

	public static void CheckVar()
	{
		int i=0;
		int j=0;
		int k=0;
		for (i=0;i<classes.size();i++)
		{
			for (j=0;j<classes.get(i).Vlist.size();j++)
			{
				if (SearchType(classes.get(i).Vlist.get(j))==false)
				{
					Errs.addElement("Line "+classes.get(i).Vlist.get(j).DefPos+": Type "+classes.get(i).Vlist.get(j).Type+" not defined!");
				}
			}
			
			for (j=0;j<classes.get(i).Mlist.size();j++)
			{
				if (SearchType(classes.get(i).Mlist.get(j).Return)==false)
				{	
					Errs.addElement("Line "+classes.get(i).Mlist.get(j).Return.DefPos+": Type "+classes.get(i).Mlist.get(j).Return.Type+" not defined!");
				}
				
				for (k=0;k<classes.get(i).Mlist.get(j).Vlist.size();k++)
				{
					if (SearchType(classes.get(i).Mlist.get(j).Vlist.get(k))==false)
					{
						Errs.addElement("Line "+classes.get(i).Mlist.get(j).Vlist.get(k).DefPos+": Type "+classes.get(i).Mlist.get(j).Vlist.get(k).Type+" not defined!");
					}
				}
				
				for (k=0;k<classes.get(i).Mlist.get(j).Paralist.size();k++)
				{
					if (SearchType(classes.get(i).Mlist.get(j).Paralist.get(k))==false)
					{
						Errs.addElement("Line "+classes.get(i).Mlist.get(j).Paralist.get(k).DefPos+": Type "+classes.get(i).Mlist.get(j).Paralist.get(k).Type+" not defined!");
					}
				}
				
			}
		}
	}
	
	public static boolean Assiable(String D,String S)
	{
		if (D==S)
		{
			return true;
		}
		MyClass temp=SearchClass(S);
		if (temp==null)
		{
			return false;
		}
		while (temp.Father != null)
		{
			if (temp.Father == D)
			{
				return true;
			}
			temp = SearchClass(temp.Father);
		}
		return false;
	}
	
	public static boolean SearchType(MyVar v)
	{
		if (v.Type =="int" || v.Type =="boolean" || v.Type =="Array"|| v.Type =="void")
		{
			return true;
		}
		if (v.Type == mainclass.Name)
		{
			return true;
		}
		for (int i=0;i<classes.size();i++)
		{
			if (classes.get(i).Name == v.Type)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean addClass(MyClass c)
	{
		if (c.Name==mainclass.Name)
		{
			Errs.addElement("Line "+c.DefPos+" : Class "+c.Name + " has the same name with the mainclass");
			return false;
		}
		for (int i=0;i<classes.size();i++)
		{
			if (c.Name == classes.get(i).Name)
			{
				Errs.addElement("line "+c.DefPos+": Class "+c.Name+" Double defined");
				return false;
			}
		}
		classes.addElement(c);
		return true;
	}
	
	public static MyClass SearchClass(String name)
	{
		
		MyClass _ret=null;
		if (name == mainclass.Name)
		{
			_ret = mainclass;
			return _ret;
		}
		int i=0;
		for (i=0;i<classes.size();i++)
		{
			if (classes.get(i).Name == name)
			{
				_ret = classes.get(i);
			}
		}
		return _ret;
	}
	
	public static MyMeth SearchMeth(String class_name, String meth_name)
	{
		MyMeth _ret = null;
		MyClass mclass = SearchClass(class_name);
		int i=0;
		for (i=0;i<mclass.Mlist.size();i++)
		{
			if (mclass.Mlist.get(i).Name == meth_name)
			{
				_ret = mclass.Mlist.get(i);
			}
		}
		return _ret;
	}
	
	public static MyVar SearchVar(MyMeth meth,String var_name)
	{
		MyVar _ret =null;
		int i=0;
		for (i=0;i<meth.Vlist.size();i++)
		{
			if (meth.Vlist.get(i).Name == var_name)
			{
				_ret = meth.Vlist.get(i);
				return _ret;
			}
		}
		for (i=0;i<meth.Paralist.size();i++)
		{
			if (meth.Paralist.get(i).Name == var_name)
			{
				_ret = meth.Paralist.get(i);
				return _ret;
			}
		}
		MyClass tempclass =SearchClass(meth.Belong);
		if (tempclass ==null)
		{
			return _ret;
		}
		for (i=0;i<tempclass.Vlist.size();i++)
		{
			if (tempclass.Vlist.get(i).Name == var_name)
			{
				_ret = tempclass.Vlist.get(i);
				return _ret;
			}
		}
		return _ret;
	}
	
	public static void Output()
	{
		if (Errs.size()!=0)
		{
			System.out.println("The Symboltable is not established successfully! The errors are:");
			for (int i=0;i<Errs.size();i++)
			{
				System.err.println(Errs.get(i));
			}
		return;
		}
		System.out.println("The Symboltable is established successfully!");
		System.out.println("The Main Class is "+mainclass.Name);
		int i=0;
		int j=0;
		int k=0;
		for (i=0;i<classes.size();i++)
		{
			System.out.println("Class "+i+" : "+classes.get(i).Name);
			if (classes.get(i).Father!=null)
			{
				System.out.println("Extends "+ classes.get(i).Father +"!");
			}
			System.out.println("-It has "+ classes.get(i).Vlist.size() +" Vars:");
			System.out.print("---");
			for (j=0; j<classes.get(i).Vlist.size();j++)
			{
				System.out.print(classes.get(i).Vlist.get(j).Type +" "+ classes.get(i).Vlist.get(j).Name+" , ");
			}
			System.out.println("\n-It has "+ classes.get(i).Mlist.size() +" Methods:");
			for (j=0;j<classes.get(i).Mlist.size();j++)
			{
				System.out.println("---In "+classes.get(i).Mlist.get(j).Belong+", "+classes.get(i).Mlist.get(j).Name + ", It returns a "+classes.get(i).Mlist.get(j).Return.Type);
				System.out.println("-----It has "+ classes.get(i).Mlist.get(j).Paralist.size()+" Paras:");
				System.out.print("---------");
				for (k=0; k< classes.get(i).Mlist.get(j).Paralist.size();k++)
				{
					System.out.print(classes.get(i).Mlist.get(j).Paralist.get(k).Type+" "+ classes.get(i).Mlist.get(j).Paralist.get(k).Name+" , ");
				}
				System.out.println("\n-----It has "+ classes.get(i).Mlist.get(j).Vlist.size()+" Vars:");
				System.out.print("---------");
				for (k=0; k< classes.get(i).Mlist.get(j).Vlist.size();k++)
				{
					System.out.print(classes.get(i).Mlist.get(j).Vlist.get(k).Type+" "+classes.get(i).Mlist.get(j).Vlist.get(k).Name+" ,  ");
				}
				System.out.print("\n");
			}
			System.out.println("*************************************");
		}
	}
	public static Vector GetVars(String classname)
	{
		Vector _ret = new Vector();
		MyVar  tempvar;
		MyClass tempclass = SearchClass(classname);
		for (int i=0; i < tempclass.Vlist.size(); i++)
		{
			tempvar = new MyVar();
			tempvar.Type = tempclass.Vlist.get(i).Type;
			tempvar.Name = tempclass.Vlist.get(i).Name;
			_ret.addElement(tempvar);
		}
	//	for (int i=0;i < _ret.size();i++)
	//	{
	//		System.err.println( ((MyVar)_ret.get(i)).Name +" "+ ((MyVar)_ret.get(i)).Type);
	//	}
		return _ret;
	}
	public static Vector GetMethVars(MyMeth m)
	{	
		Vector _ret = new Vector();
		MyVar  tempvar;
		MyMeth tempmeth = SearchMeth(m.Belong,m.Name);
		for (int i=0; i < tempmeth.Vlist.size(); i++)
		{
			tempvar = new MyVar();
			tempvar.Type = tempmeth.Vlist.get(i).Type;
			tempvar.Name = tempmeth.Vlist.get(i).Name;
			_ret.addElement(tempvar);
		}
		for (int i=0; i < tempmeth.Paralist.size(); i++)
		{
			tempvar = new MyVar();
			tempvar.Type = tempmeth.Paralist.get(i).Type;
			tempvar.Name = tempmeth.Paralist.get(i).Name;
			_ret.addElement(tempvar);
		}
			
	return _ret;	
	}	


}
