//锟斤拷锟斤拷锟洁，锟侥达拷锟斤拷锟斤拷浞斤拷锟斤拷锟酵既旧�

package ProcTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

public class RegPlan extends ARGU{
	
	public static String[] RegList = {"a0","a1","a2","a3","v0","v1","s0","s1","s2","s3","s4","s5","s6","s7","t0"
			,"t1","t2","t3","t4","t5","t6","t7","t8","t9"};
	int[][] Matrix;   //锟斤拷锟斤拷图
	Vector <String> temps;
	Vector <String> Taken;
	Map<String,String> Alloc;
	int MaxStack;
	int MaxReg;
	
	public RegPlan()
	{ 
		Taken = new Vector<String>();
		MaxReg =0;
		MaxStack = 0;
		temps = new Vector<String>();
		Alloc = new HashMap<String,String>();
	}
	
	public void AddTEMP(String s)
	{
		if (!temps.contains(s))
		{
			temps.add(s);
		}
	}
	
	public void Setgraph()
	{
		Matrix = new int[temps.size()][temps.size()];
	}
	
	public void AddEdge(String s1,String s2)
	{
		Matrix[temps.indexOf(s1)][temps.indexOf(s2)] =1;
		Matrix[temps.indexOf(s2)][temps.indexOf(s1)] =1;
	}
	
	public void Color(int ParaNum)           //----------------------锟斤拷锟侥猴拷锟斤拷图染色
	{
		Stack<Integer> tempstack = new Stack<Integer>();
		Map<Integer,Integer> saver= new HashMap<Integer,Integer>();
		int i=0;
		int j=0;
		for (i=0;i<temps.size();i++)
		{
			if (Integer.parseInt(temps.get(i))<20)
			{
				for (j=0;j<temps.size();j++)
				{
					Matrix[j][i] =0;
					Matrix[i][j] =0;
				}
			}
		}
		int Coloring =0;
		Vector<Integer> colored = new Vector<Integer>();
		for (i=0;i<temps.size();i++)
		{
			tempstack.push(i);
		}
		
		boolean[] Used = new boolean[temps.size()];
		
		while(!tempstack.isEmpty())
		{
			for (i=0;i<temps.size();i++)
			{
				Used[i]=false;
			}
			Coloring = tempstack.pop();

			for (i=0;i<colored.size();i++)
			{
				if (Matrix[colored.get(i)][Coloring]==1)
				{
					if (saver.get(colored.get(i))!=-1)
					{
						Used[saver.get(colored.get(i))]=true;
					}
				}
			}
			for (i=0;i<temps.size();i++)
			{
				if (!Used[i])
				{
					saver.put(Coloring, i);
					break;
				}
			}
			colored.add(Coloring);
		}
		
		for (i=0;i<temps.size();i++)
		{
			if (Integer.parseInt(temps.get(i))<20)
			{
			    if (Integer.parseInt(temps.get(i))>ParaNum-1&&Integer.parseInt(temps.get(i))>3)
			    {
			    	MaxStack++;
			    }
				Alloc.put(temps.get(i), "OverFlow");
				continue;
			}
			
			if (saver.get(i)<17)
			{
				Alloc.put(temps.get(i),RegList[saver.get(i)+6]);
				if (MaxReg < saver.get(i))
				{
					MaxReg = saver.get(i);
				}
			}
			else
			{
				Alloc.put(temps.get(i),"OverFlow");
				MaxStack++;
			}
		}
		
		
		for (i=0;i<=MaxReg;i++)
		{
			Taken.add(RegList[i+6]);
		}
		
		for (i=0;i<4;i++)
		{
			Alloc.put(i+"", "a"+i);
		}
		for (i=4;i<20;i++)
		{
			Alloc.put(i+"","OverFlow");
		}
		
	/*	
		regraph();
		for(i=0;i<temps.size();i++)
		{
			if (Alloc.get(temps.get(i))=="OverFlow")
			{
				tempstack.push(i);
			}
		}
		
		Map<Integer,Integer> newsaver= new HashMap<Integer,Integer>();
		colored = new Vector();
		while(!tempstack.isEmpty())
		{
			boolean[] Used ={false,false,false,false,false,false,false,false,false,false,false,false,
					         false,false,false,false,false,false,false,false,false,false,false,false};
			Coloring = tempstack.pop();

			for (i=0;i<colored.size();i++)
			{
				if (Matrix[colored.get(i)][Coloring]==1)
				{
					if (newsaver.get(colored.get(i))!=-1)
					{
						Used[newsaver.get(colored.get(i))]=true;
					}
				}
			}
			for (i=0;i<24;i++)
			{
				if (!Used[i])
				{
					if (MaxStack < i+1)
					{ 
						MaxStack = i+1;
					}
					newsaver.put(Coloring, i);
					break;
				}
			}
			if (i==24)
			{
				System.err.println("StackOverFlow!");
				newsaver.put(Coloring, -1);
			}
			colored.add(Coloring);
		}
		
		for (i=0;i<temps.size();i++)
		{
			if (Alloc.get(temps.get(i))=="OverFlow")
			{
				Alloc.remove(temps.get(i));
				Alloc.put(temps.get(i),"Stack"+newsaver.get(i));
			}
		}
	*/	
		//System.out.println();
		
	}
	/*
	public void regraph()
	{
		int i=0;
		int j=0;
		for (i=0;i<temps.size();i++)
		{
			if (Alloc.get(temps.get(i)) != "OverFlow")
			{
				for (j=0;j<temps.size();j++)
				{
					Matrix[i][j]=0;
					Matrix[j][i]=0;
				}
			}
		}
	}
	*/
	public void Output()
	{
		int i=0;
		int j=0;
		for (i=0;i<temps.size();i++)
		{
			System.out.print(temps.get(i)+"  ");
		}
		System.out.println();
	/*	for (i=0;i<temps.size();i++)
		{
			for (j=0;j<temps.size();j++)
			{
				System.out.print(Matrix[i][j]+" ");
			}
			System.out.print("\n");
		}
		*/
		for (i=0;i<temps.size();i++)
		{
			System.out.print(temps.get(i)+"->"+Alloc.get(temps.get(i))+"  ");
		}
		System.out.println();
		for (i=0;i<Taken.size();i++)
		{
			System.out.print(Taken.get(i)+" ");
		}
		System.out.println();
		
		 
	}
	
	public String GetReg(int Num)
	{
		return Alloc.get(Integer.toString(Num));
	}

}
