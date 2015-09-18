//过程表类，类似于符号表，包含了所有过程的信息

package ProcTable;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

 public class ProcTable {
	public static Vector<Proc> procs = new Vector<Proc>();  //过程表
	public static int LabCounter = 0;   //标签计数器
	
	public static Proc SearchProc(String temp)   //按名称寻找过程
	{
		int i=0;
		for (i=0;i<procs.size();i++)
		{
			if (temp == procs.get(i).Name)
			{
				return procs.get(i);
			}
		}
		return null;
	}
	
	public static void AddProc(Proc p)
	{
		procs.add(p);
	}
	
	public static void SetBlock()
	{
		int i=0;
		for (i=0; i<procs.size();i++)
		{
			procs.get(i).CreateBlock();
		}
	}
	
	public static void SetGraph()
	{
		int i=0;
		for (i=0; i<procs.size();i++)
		{
			procs.get(i).CreateGraph();
		}
	}
	
	public static void SetIntroLive()
	{
		int i=0;
		for (i=0;i<procs.size();i++)
		{
			procs.get(i).SetIntroLive();
			procs.get(i).GetRegPlan();
		}
		
	}
	public static void Output()
	{
		int i=0;
		String Temp=null;
		for (i=0; i<procs.size(); i++)
		{
			int j=0;
			System.out.println("****************************************");
			System.out.println("PROC "+ procs.get(i).Name + " , has "+procs.get(i).ParaNum + " Paras, and maxpara is " 
								+ procs.get(i).MaxPara +"! It has "+ procs.get(i).GetMaxStack()+ " Stack!");
			System.out.print("Labels are: ");

			
            int size = procs.get(i).labels.size();
            Set keysSet = procs.get(i).labels.keySet();
	        Iterator it = keysSet.iterator();
	        while( it.hasNext()) 
	        {
	            Temp = (String) it.next();
	            System.out.print(procs.get(i).labels.get(Temp).Pos);
	            System.out.print(":  "+Temp);
	            System.out.print("->"+procs.get(i).labels.get(Temp).Name+"    ");
	        }
	        
	        System.out.print("\n");
	        System.out.print("Flags: ");
	        for (j=0;j<procs.get(i).flags.size();j++)
	        {
	        	System.out.print(procs.get(i).flags.get(j)+"   ");
	        }
	        System.out.print("\n");
	        
	        for (j=0;j<procs.get(i).blocks.size();j++)
	        {
	        	int m=0;
	        	System.out.println("Block "+j +" From "+procs.get(i).blocks.get(j).Start+ " To "+procs.get(i).blocks.get(j).End
	        			+", it goes to Block "+procs.get(i).blocks.get(j).Next+", and it jumps to Block "+procs.get(i).blocks.get(j).JumpDes);
	        	for (m=0;m<procs.get(i).blocks.get(j).intros.size();m++)
	        	{
	        		int k=0;
	        		if (procs.get(i).blocks.get(j).intros.get(m).Des!=null)
	        		{
	        			System.out.print("Des:" +procs.get(i).blocks.get(j).intros.get(m).Des+"      " );
	        		}
	        	
	        		if (procs.get(i).blocks.get(j).intros.get(m).Src!=null && !procs.get(i).blocks.get(j).intros.get(m).Src.isEmpty())
	        		{
	        			System.out.print("Src:");
	        			for (k=0;k<procs.get(i).blocks.get(j).intros.get(m).Src.size();k++)
	        			{
	        				System.out.print((String)procs.get(i).blocks.get(j).intros.get(m).Src.get(k)+",");
	        			}
	        		}
	        		System.out.print("**************");
	        		for (k=0;k<procs.get(i).blocks.get(j).intros.get(m).Live.size();k++)
	        		{
	        			System.out.print(procs.get(i).blocks.get(j).intros.get(m).Live.get(k)+",");
	        		}
	        		System.out.print("\n");
	        	}
	        }
	        
	        for (j=0;j<procs.get(i).TakenRegs().size();j++)
	        {
	        	System.out.print(procs.get(i).TakenRegs().get(j)+" ");
	        }
	        System.out.println();
	        for (j=0;j<procs.get(i).ParaStack.size();j++)
	        {
	        	System.out.print(procs.get(i).ParaStack.get(j)+" ");
	        }
			System.out.print("\n");
			procs.get(i).regplan.Output();
			procs.get(i).cyclegraph.OutPut();
		}
	}
}
