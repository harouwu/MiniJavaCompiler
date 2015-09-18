//核心类，记录过程信息

package ProcTable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Proc extends ARGU{

	public int IntroCounter;
	public String Name; 
	public int ParaNum; 
	public int MaxPara; 
	public RegPlan regplan;
	Vector<Block> blocks; 
	Graph cyclegraph; 
	Vector<Integer> flags;
	public Map<String,MyLabel> labels;
	public Vector<Integer> StackRecorder;
	public Vector<String> ParaStack;
	
	
	public Proc(String name)
	{
		ParaStack = new Vector<String>();
		StackRecorder = new Vector<Integer>();
		flags = new Vector<Integer>();
		IntroCounter = 0;
		blocks = new Vector<Block>();
		Name = name;
		ParaNum = 0;
		MaxPara = -1;
		regplan = new RegPlan();
	    labels = new HashMap<String,MyLabel>();   
	}
	
	public Vector<String> TakenRegs()
	{
		return regplan.Taken;
	}
	
	public String GetReg(int Num)  
	{
		return regplan.GetReg(Num);
	}
	
	public int GetMaxStack()
	{
		 return ParaNum + regplan.Taken.size() + regplan.MaxStack+4;
	}

	
	public int GetMaxPara()
	{
		return MaxPara;
	}
	
	public int GetParaNum()
	{
		return ParaNum;
	}
	
	
	public String GetLab(String s)
	{
		
		if(labels.get(s)==null)
		{
			return s;
		}
		else
		{
			return labels.get(s).Name;
		}
	}
	
	public void AddIntro(int Pos,String d, Vector v)
	{
		Intro temp = new Intro(Pos,d,v);
		for (int i=0;i<blocks.size();i++)
		{
			if (Pos >= blocks.get(i).Start && Pos <= blocks.get(i).End)
			{
				blocks.get(i).AddIntro(temp);
			}
		}
	}
	
	public void GetRegPlan()
	{
		int i=0;
		int j=0;
		int k=0;
		int l=0;
		for (i=0;i< 4;i++)
		{
			ParaStack.add("a"+i);
		}
		for (i=0;i<blocks.size();i++)
		{
			for (j=0;j<blocks.get(i).intros.size();j++)
			{
				for (k=0;k<blocks.get(i).intros.get(j).Live.size();k++)
				{
					regplan.AddTEMP(blocks.get(i).intros.get(j).Live.get(k));
				}
			}
		}
		regplan.Setgraph();
		for (i=0;i<blocks.size();i++)
		{
			for (j=0;j<blocks.get(i).intros.size();j++)
			{
				for (k=0;k<blocks.get(i).intros.get(j).Live.size()-1;k++)
				{
					for (l=k;l<blocks.get(i).intros.get(j).Live.size();l++)
					{
						regplan.AddEdge(blocks.get(i).intros.get(j).Live.get(k),blocks.get(i).intros.get(j).Live.get(l));
					}
				}
			}
		}
		
		regplan.Color(ParaNum);
	}
	
	public void SetIntroLive()
	{
		int i=0,j=0,k=0;
		Vector<String> pass = new Vector<String>();
		for (i=blocks.size()-1;i>=0;i--)
		{
			for (j=blocks.get(i).intros.size()-1;j>=0;j--)
			{
				blocks.get(i).intros.get(j).Merge(pass);
			}
		}
		
		for (i=0;i<cyclegraph.cyclerecorder.size();i++)	//锟斤拷一锟姐，锟斤拷i锟斤拷循锟斤拷
		{
			pass = (Vector)blocks.get(cyclegraph.cyclerecorder.get(i).get(0)).intros.get(0).Live.clone(); //锟斤拷锟狡碉拷一锟斤拷锟斤拷锟节碉拷锟�
			for (j=cyclegraph.cyclerecorder.get(i).size()-1;j>=0;j--)  //锟节讹拷锟姐，锟斤拷j锟斤拷block
			{
				for (k=blocks.get(cyclegraph.cyclerecorder.get(i).get(j)).intros.size()-1;k>=0;k--) //锟斤拷锟斤拷悖匡拷锟絙lock锟斤拷k锟斤拷指锟斤拷
				{
					blocks.get(cyclegraph.cyclerecorder.get(i).get(j)).intros.get(k).Merge(pass);
				}
			}
		}
		
		
	}
	
	public void CreateBlock()
	{
		Collections.sort(flags);
		int i=0;
		for (i=0;i<flags.size()-2;i++)
		{
			Block tempblock = new Block(flags.get(i),flags.get(i+1)-1,false);
			tempblock.Next = i+1;
			blocks.add( tempblock);
		}
			Block tempblock = new Block(flags.get(i),flags.get(i+1)-1,true);
			blocks.add( tempblock);
	}
	
	public void CreateGraph()
	{
		cyclegraph= new Graph(blocks.size());
		int i=0;
		for (i=0;i<blocks.size();i++)
		{
			cyclegraph.AddEdge(i, blocks.get(i).Next);
			cyclegraph.AddEdge(i, blocks.get(i).JumpDes);
		}
		cyclegraph.GetCycle();
	}
	
	
	
	public void SaveLab(int pos,String label)  
	{
		if(!labels.containsKey(label) && ProcTable.SearchProc(label)==null)
		{
			ProcTable.LabCounter++;
			labels.put(label,new MyLabel(pos, "Lab"+ProcTable.LabCounter));
		}
	}
	

	
}
