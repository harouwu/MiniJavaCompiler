//¿ØÖÆÁ÷Í¼

package ProcTable;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

public class Graph {
	
	int[][] Matrix;
	int Size;
	Vector<Vector<Integer>> cyclerecorder;
	ArrayList<Integer> trace;
	
	public Graph(int size)
	{
		trace=new ArrayList<Integer>();
		cyclerecorder = new Vector<Vector<Integer>>();
		Size = size;
		Matrix = new int[size][size];
		int i=0,j=0;
		for (i=0;i<size;i++)
		{
			for (j=0;j<size;j++)
			{
				Matrix[i][j]=0;
			}
		}
	}
	
	public void GetCycle()
	{
		int i =0;
		int j=0;
    	Stack<Integer> tempstack;
		for (i=0;i<Size;i++)
		{
			tempstack = new Stack<Integer>();
			tempstack.push(i);
			FindCycle(i,tempstack);
			tempstack.pop();
			for (j=0;j<Size;j++)
			{
				Matrix[j][i]=0;
				Matrix[i][j]=0;
			}
		}
	}
	
    public void FindCycle(int Start,Stack<Integer> tempstack)
    {

    	int i=0;
    	int j=0;
    	Vector<Integer> newvector;
    	for (i=0;i<Size;i++)
    	{
    		if (Matrix[tempstack.get(tempstack.size()-1)][i]==1)
    		{
    			//System.out.println("Start is :"+Start);
    			if (i==Start)
    			{
    			//	System.err.println("Find!");
    				newvector = new Vector<Integer>();
    				for (j=0;j<tempstack.size();j++)
    				{
    					newvector.add(tempstack.get(j));
    				}
    				cyclerecorder.add(newvector);
    			}
        		else
        		{
        			if (!tempstack.contains(i))
        			{
        				tempstack.push(i);
        			//	System.out.print("Now the Stack is: ");
           			//	for (j=0;j<tempstack.size();j++)
        				//{
        			//		System.out.print(tempstack.get(j));
        			//	}
           			//	System.out.println();
        				FindCycle(Start,tempstack);
        				tempstack.pop();
        			}
        		}
    		}
    	}
    }

	
	public void AddEdge(int s, int e)
	{
		if (s!=-1 && e!=-1)
		{
			Matrix[s][e]=1;
		}
	}
	
	public void OutPut()
	{
		int i=0,j=0;
		System.out.println("Cycle :");
		for (i=0;i<cyclerecorder.size();i++)
		{
			for (j=0;j<cyclerecorder.get(i).size();j++)
			{
				System.out.print(cyclerecorder.get(i).get(j)+" ");
			}
			System.out.print("\n");
		}
	}
	

}
