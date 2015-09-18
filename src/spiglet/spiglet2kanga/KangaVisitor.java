package spiglet.spiglet2kanga;//"OverFlow"
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import spiglet.syntaxtree.*;
import spiglet.visitor.GJVoidDepthFirst;

import ProcTable.*;



public class KangaVisitor extends GJVoidDepthFirst<ARGU>{

	public void visit(Goal n,ARGU argu)
	{
		Proc currProc = ProcTable.SearchProc("MAIN");
		FuncBegin("MAIN",(ARGU)currProc, 0, currProc.GetMaxStack(), currProc.GetMaxPara());
        n.f1.accept(this,(ARGU)currProc);
        FuncEnd((ARGU)currProc, 0);
		n.f3.accept(this,(ARGU)currProc);
	}
	/**
	 * Grammar production:
	 * f0 -> ( ( Label() )? Stmt() )*
	 */
	public void visit(StmtList n, ARGU argu)
	{
		if ( n.f0.present() )
	    for ( Enumeration<Node> e = n.f0.elements(); e.hasMoreElements(); )
	     e.nextElement().accept(this, argu);
	    return;
	}
	
	/**
	 * Grammar production:
	 * f0 -> Label()
	 * f1 -> "["
	 * f2 -> IntegerLiteral()
	 * f3 -> "]"
	 * f4 -> StmtExp()
	 */
	public void visit(Procedure n, ARGU argu)
	{
		Proc currProc = ProcTable.SearchProc(n.f0.f0.toString());
        FuncBegin(n.f0.f0.toString(),(ARGU)currProc,currProc.GetParaNum(), currProc.GetMaxStack(), currProc.GetMaxPara());
		n.f4.accept(this, (ARGU)currProc);
		FuncEnd((ARGU)currProc, currProc.GetParaNum());
	}
	
	public void visit(NoOpStmt n, ARGU argu)
	{
		OutPut.con(" NOOP\n");
	}

	public void visit(ErrorStmt n, ARGU argu)
	{
		OutPut.con(" ERROR\n");
	}
	/**
	 * Grammar production:
	 * f0 -> "CJUMP"
	 * f1 -> Temp()
	 * f2 -> Label()
	 */
	public void visit(CJumpStmt n, ARGU argu)
	{
		String t1 = ReadTemp(n.f1, argu, 0);
		OutPut.con(" CJUMP " + t1 + " ");
		n.f2.accept(this, argu);	
		OutPut.con("\n");
	}
	/**
	 * Grammar production:
	 * f0 -> "JUMP"
	 * f1 -> Label()
	 */	
	public void visit(JumpStmt n, ARGU argu)
	{
		OutPut.con(" JUMP ");
		n.f1.accept(this, argu);
		OutPut.con("\n");
	}
	/**
	 * Grammar production:
	 * f0 -> "HSTORE"
	 * f1 -> Temp()
	 * f2 -> IntegerLiteral()
	 * f3 -> Temp()
	 */
	public void visit(HStoreStmt n,ARGU argu)
	{
		String t1 = ReadTemp(n.f1, argu, 0);
		String t2 = ReadTemp(n.f3, argu, 1);
        OutPut.con(" HSTORE " + t1 + " ");
		n.f2.accept(this, argu);
		OutPut.con(" " + t2 + "\n");
	}
	/**
	 * Grammar production:
	 * f0 -> "HLOAD"
	 * f1 -> Temp()
	 * f2 -> Temp()
	 * f3 -> IntegerLiteral()
	 */
	public void visit(HLoadStmt n,ARGU argu)
	{
		Proc currProc = (Proc)argu;
		String t = currProc.GetReg(Integer.parseInt(n.f1.f1.f0.tokenImage));
        if(t==null)  
        {
    		OutPut.con(" NOOP \n");
        	return;
        }            
        String t1 = WriteTemp(n.f1,argu, 0);
		String t2 = ReadTemp(n.f2,argu, 0);
		OutPut.con(" HLOAD " + t1 + " " + t2 + " ");
		n.f3.accept(this, argu);
		OutPut.con("\n");
		SpillToStack(n.f1,argu,0);
	}
	/**
	 * Grammar production:
	 * f0 -> "MOVE"
	 * f1 -> Temp()
	 * f2 -> Exp()	
	 *				 Exp() Grammar production:
	 						* f0 -> Call()
	 						*       | HAllocate()
	 						*       | BinOp()
	 						*       | SimpleExp()
 */
	public void visit(MoveStmt n,ARGU argu)
	{
		Proc currProc = (Proc)argu;
		String t = currProc.GetReg(Integer.parseInt(n.f1.f1.f0.tokenImage));;
		if(t==null)                //************************************************
		{
			OutPut.con(" NOOP  \n" );
			return;
		}                        //*************************************************
		int i = n.f2.f0.which;
		
		if(i == 0)//是call语句
		{
			n.f2.accept(this, argu);
			OutPut.con("\n");
			String t1 = WriteTemp(n.f1,argu, 0);
			OutPut.con(" MOVE " + t1 + " v0\n" );
			SpillToStack(n.f1,argu,0);
			return;
		}
		else if(i == 1)//是HALLOCATE
		{
            String t1 = SimpleExpCode(((HAllocate)n.f2.f0.choice).f1, argu,0);//s1 返回的是个数值
			String t2 = WriteTemp(n.f1, argu, 0);
			OutPut.con(" MOVE " + t2 + " HALLOCATE " + t1 + "\n");
			SpillToStack(n.f1,argu,0);
			return;			
		}
		else if(i == 2)//是binop
		{
			String t1 = ReadTemp(((BinOp)n.f2.f0.choice).f1, argu, 0);//b.f1是个寄存器
			String t2 = SimpleExpCode(((BinOp)n.f2.f0.choice).f2, argu,1);//s1 返回的是个数值
			String t3 = WriteTemp(n.f1, argu, 0);
			OutPut.con(" MOVE " + t3 + " ");
			((BinOp)n.f2.f0.choice).f0.accept(this, argu);//b.f0是个运算符
			OutPut.con(" " + t1 + " " + t2 + "\n");
			SpillToStack(n.f1,argu,0);
	    }
		else if(i == 3)//是简单表达式
		{
            String t1 = WriteTemp(n.f1,argu, 0);
			String t2 = SimpleExpCode(((SimpleExp)n.f2.f0.choice), argu,0);//s1 返回的是个数值
			OutPut.con(" MOVE " + t1 + " " + t2 + "\n");
			SpillToStack(n.f1,argu,0);
		}
		return;		
	}

	/**
	 * Grammar production:
	 * f0 -> "PRINT"
	 * f1 -> SimpleExp()
	 */
	public void visit(PrintStmt n,ARGU argu)
	{
		String s1 = SimpleExpCode(n.f1,argu,0);
		OutPut.con(" PRINT " + s1 + "\n");
	}
	
	/**
	 * Grammar production:
	 * f0 -> "BEGIN"
	 * f1 -> StmtList()
	 * f2 -> "RETURN"
	 * f3 -> SimpleExp()
	 * f4 -> "END"
	 */
	public void visit(StmtExp n, ARGU argu)
	{
		n.f1.accept(this, argu);
		String t1 = SimpleExpCode(n.f3, argu,0);//处理返回值
		OutPut.con(" MOVE " + "v0 " + t1 + "\n");
	}
	
	/**
	 * Grammar production:
	 * f0 -> "CALL"
	 * f1 -> SimpleExp()
	 * f2 -> "("
	 * f3 -> ( Temp() )*
	 * f4 -> ")"
	 */
	/*public void visit(Call n, ARGU argu)////////////////////
	{
		int size = n.f3.size();
		int i;
		Proc currProc = (Proc)argu;
		Iterator<Node> Itr = n.f3.nodes.iterator();
		    if(currProc.ParaNum>4)//在call语句之后要恢复a0-a3，如果这个函数的参数数目大于4,那么（参数数目-4+使用的寄存器数目）->（参数数目+使用的寄存器数目+ParaStack）为存放a0->a3的栈单元
			 for(int k=0;k<currProc.ParaStack.size();k++)
			  OutPut.con(" ASTORE  "  + " SPILLEDARG "+(currProc.ParaNum-4+currProc.TakenRegs().size()+k)+" "+ currProc.ParaStack.get(k)+ "\n");
			else //如果这个函数的参数数目小于等于4,那么（使用的寄存器数目）->（使用的寄存器数目+ParaStack）为存放a0->a3的栈单元
			 for(int k=0;k<currProc.ParaStack.size();k++)
			  OutPut.con(" ASTORE  "  + " SPILLEDARG "+(currProc.TakenRegs().size()+k)+" "+ currProc.ParaStack.get(k)+ "\n");
		    
		    for(i=0;i<4&&i<size;i++)
		    {//将参数放在a0-a3里
		    	String t1 = ReadTemp((Temp)Itr.next(), argu, 0);
		    	if (t1.charAt(0)=='a')
		    	{
		    		String s =""+t1.charAt(1);
                    if(currProc.ParaNum>4)
			    	 OutPut.con("ALOAD v0 SPILLEDARG "+(currProc.ParaNum-4+currProc.TakenRegs().size()+ Integer.parseInt(s))+"\n");
                    else 
                     OutPut.con("ALOAD v0 SPILLEDARG "+(currProc.TakenRegs().size()+ Integer.parseInt(s))+"\n");
		    		OutPut.con("MOVE "+"a" +i+" v0\n");
		    	}
		    	else
		    	{
			    	OutPut.con(" MOVE " + "a" + i + " "+t1+"\n");
		    	}
		    }
		int j=1;
		for(;i<size;i++,j++)
		{//如果参数多于4个，都将其放到栈里
			String t1 = ReadTemp((Temp)Itr.next(),argu, 0);
			OutPut.con(" PASSARG " + j + " " + t1 +  "\n");
		}
		String t1 = SimpleExpCode(n.f1, argu,0);
		OutPut.con(" CALL " + t1 + "\n");
		if(currProc.ParaNum>4)//在call语句之后要恢复a0-a3，如果这个函数的参数数目大于4,那么（参数数目-4+使用的寄存器数目）->（参数数目+使用的寄存器数目+ParaStack）为存放a0->a3的栈单元
		 for(int k=0;k<currProc.ParaStack.size();k++)
		  OutPut.con(" ALOAD " + currProc.ParaStack.get(k) + " SPILLEDARG "+(currProc.ParaNum-4+currProc.TakenRegs().size()+k)+ "\n");
		else //如果这个函数的参数数目小于等于4,那么（使用的寄存器数目）->（使用的寄存器数目+ParaStack）为存放a0->a3的栈单元
		 for(int k=0;k<currProc.ParaStack.size();k++)
		  OutPut.con(" ALOAD " + currProc.ParaStack.get(k) + " SPILLEDARG "+(currProc.TakenRegs().size()+k)+ "\n");
		}*/
	public void visit(Call n, ARGU argu)////////////////////
	{
		int size = n.f3.size();
		int i;
		Proc currProc = (Proc)argu;
		Iterator<Node> Itr = n.f3.nodes.iterator();
		    if(currProc.ParaNum>4)//在call语句之后要恢复a0-a3，如果这个函数的参数数目大于4,那么（参数数目-4+使用的寄存器数目）->（参数数目+使用的寄存器数目+ParaStack）为存放a0->a3的栈单元
			 for(int k=0;k<currProc.ParaStack.size();k++)
			  OutPut.con(" ASTORE  "  + " SPILLEDARG "+(currProc.ParaNum-4+currProc.TakenRegs().size()+k)+" "+ currProc.ParaStack.get(k)+ "\n");
			else //如果这个函数的参数数目小于等于4,那么（使用的寄存器数目）->（使用的寄存器数目+ParaStack）为存放a0->a3的栈单元
			 for(int k=0;k<currProc.ParaStack.size();k++)
			  OutPut.con(" ASTORE  "  + " SPILLEDARG "+(currProc.TakenRegs().size()+k)+" "+ currProc.ParaStack.get(k)+ "\n");
		    
		    for(i=0;i<4&&i<size;i++)
		    {//将参数放在a0-a3里
		    	String t1 = ReadTemp((Temp)Itr.next(), argu, 0);
		    	if (t1.charAt(0)=='a')
		    	{
		    		String s =""+t1.charAt(1);
                    if(currProc.ParaNum>4)
			    	 OutPut.con("ALOAD v0 SPILLEDARG "+(currProc.ParaNum-4+currProc.TakenRegs().size()+ Integer.parseInt(s))+"\n");
                    else 
                     OutPut.con("ALOAD v0 SPILLEDARG "+(currProc.TakenRegs().size()+ Integer.parseInt(s))+"\n");
		    		OutPut.con("MOVE "+"a" +i+" v0\n");
		    	}
		    	else
		    	{
			    	OutPut.con(" MOVE " + "a" + i + " "+t1+"\n");
		    	}
		    }
		int j=1;
		for(;i<size;i++,j++)
		{//如果参数多于4个，都将其放到栈里
	    	String t1 = ReadTemp((Temp)Itr.next(), argu, 0);
	    	if (t1.charAt(0)=='a')
	    	{
	    		String s =""+t1.charAt(1);
                OutPut.con("ALOAD v0 SPILLEDARG "+(currProc.ParaNum-4+currProc.TakenRegs().size()+ Integer.parseInt(s))+"\n");
    			OutPut.con(" PASSARG " + j + " v0 \n");
	    	}
	    	else
	    	{
				OutPut.con(" PASSARG " + j + " " + t1 +  "\n");
	    	}
		//	String t1 = ReadTemp((Temp)Itr.next(),argu, 0);
		//	OutPut.con(" PASSARG " + j + " " + t1 +  "\n");
		}
		String t1 = SimpleExpCode(n.f1, argu,0);
		OutPut.con(" CALL " + t1 + "\n");
		if(currProc.ParaNum>4)//在call语句之后要恢复a0-a3，如果这个函数的参数数目大于4,那么（参数数目-4+使用的寄存器数目）->（参数数目+使用的寄存器数目+ParaStack）为存放a0->a3的栈单元
		 for(int k=0;k<currProc.ParaStack.size();k++)
		  OutPut.con(" ALOAD " + currProc.ParaStack.get(k) + " SPILLEDARG "+(currProc.ParaNum-4+currProc.TakenRegs().size()+k)+ "\n");
		else //如果这个函数的参数数目小于等于4,那么（使用的寄存器数目）->（使用的寄存器数目+ParaStack）为存放a0->a3的栈单元
		 for(int k=0;k<currProc.ParaStack.size();k++)
		  OutPut.con(" ALOAD " + currProc.ParaStack.get(k) + " SPILLEDARG "+(currProc.TakenRegs().size()+k)+ "\n");
		}
	
	
	/*public void visit(Call n, ARGU argu)
	{
		int size = n.f3.size();
		int i;
		Proc currProc = (Proc)argu;
		Iterator<Node> Itr = n.f3.nodes.iterator();
		for(i=0;i<4&&i<size;i++)
		{//将参数放在a0-a3里
            String t1 = ReadTemp((Temp)Itr.next(), argu, 0);
			OutPut.con(" MOVE " + "a" + i + " " + t1 + "\n");
		}
		int j=1;
		for(;i<size;i++,j++)
		{//如果参数多于4个，都将其放到栈里
			String t1 = ReadTemp((Temp)Itr.next(),argu, 0);
			OutPut.con(" PASSARG " + j + " " + t1 +  "\n");
		}
		String t1 = SimpleExpCode(n.f1, argu,0);
		OutPut.con(" CALL " + t1 + "\n");
		if(currProc.ParaNum>4)//在call语句之后要恢复a0-a3，如果这个函数的参数数目大于4,那么（参数数目-4+使用的寄存器数目）->（参数数目+使用的寄存器数目+ParaStack）为存放a0->a3的栈单元
		 for(int k=0;k<currProc.ParaStack.size();k++)
		  OutPut.con(" ALOAD " + currProc.ParaStack.get(k) + " SPILLEDARG "+(currProc.ParaNum-4+currProc.TakenRegs().size()+k)+ "\n");
		else //如果这个函数的参数数目小于等于4,那么（使用的寄存器数目）->（使用的寄存器数目+ParaStack）为存放a0->a3的栈单元
		 for(int k=0;k<currProc.ParaStack.size();k++)
		  OutPut.con(" ALOAD " + currProc.ParaStack.get(k) + " SPILLEDARG "+(currProc.TakenRegs().size()+k)+ "\n");
		}
	*/
	public void visit(Operator n,  ARGU argu)
	{
		switch(n.f0.which)
		{
		case 0: OutPut.con(" LT ");return;
		case 1: OutPut.con(" PLUS ");return;
		case 2: OutPut.con(" MINUS "); return;
		case 3: OutPut.con(" TIMES "); return;
		}
	}
	
	public void visit(IntegerLiteral n,  ARGU argu)
	{
		String s = n.f0.toString();
		OutPut.con(" " + s + " ");
	}
	

	
	public void visit(Label n,  ARGU argu)
	{
		String s = n.f0.toString();
		if (argu==null)
		{
			OutPut.con(" "+s+" ");
		}
		else
		{
			Proc currProc=(Proc)argu;
			OutPut.con(" " +currProc.GetLab( s )+ " ");
		}
	}

	public String SimpleExpCode(SimpleExp n,ARGU argu,int vRegister)///////////////////////////
	{
		Proc currProc=(Proc)argu;
		switch(n.f0.which)
		{
			case 0:	return ReadTemp((Temp)n.f0.choice,argu, vRegister);
			case 1: return ((IntegerLiteral)n.f0.choice).f0.toString();
			case 2:return currProc.GetLab(((Label)n.f0.choice).f0.toString());
		}
		return null;
	}
	public void FuncBegin(String funcName,ARGU argu,int ParaNum, int MaxStack, int MaxPara)
	{//函数开始，保存所有的寄存器
		Proc currProc = (Proc)argu;
		Vector<String> Registers= currProc.TakenRegs();
		OutPut.con(funcName + " [ " + ParaNum + " ] [ " + MaxStack + " ] [ " +MaxPara+ " ]\n");
		for(int i=4;i<ParaNum;i++)
	      currProc.StackRecorder.add(i);//放父函数传进来的参数
		if(ParaNum-4>0)//如果参数数目大于4，因为大于4的参数都要放到栈里面，所以存放寄存器的栈要接着存放参数的栈单元即为f1-3的位置往下增长，这里f1代表参数数目
		{
		 int i,j;
		 for(i=ParaNum-4,j=0;i<ParaNum-4+Registers.size();i++,j++)
		 {
		   OutPut.con(" ASTORE " + " SPILLEDARG " + i + " " + Registers.get(j) + "\n");
		   currProc.StackRecorder.add(-1);
		 }//存放所有使用的寄存器但除了a0-a3，因为不是temp溢出，所以不用存放temp号，这里用-1代替temp号
		 for(j=0;j<currProc.ParaStack.size();j++,i++)
		 {
		   OutPut.con(" ASTORE " + " SPILLEDARG " + i + " " + currProc.ParaStack.get(j) + "\n");
		   currProc.StackRecorder.add(-1);
		 }//存放a0->a3，因为不是temp溢出，所以不用存放temp号，这里用-1代替temp号
		}
		else
		{
			int i,j; 
		   for(i=0,j=0;i<Registers.size();i++,j++)
		  {
		    OutPut.con(" ASTORE " + " SPILLEDARG " + i + " " + Registers.get(j) + "\n");	
		    currProc.StackRecorder.add(-1);
		  }//如果参数数目小于等于4，因为参数都不放到栈里面，所以存放寄存器的栈从0开始增长即可
		  for(j=0;j<currProc.ParaStack.size();j++,i++)
		  {
			OutPut.con(" ASTORE " + " SPILLEDARG " + i + " " + currProc.ParaStack.get(j) + "\n");
		    currProc.StackRecorder.add(-1);
		  }//存放a0->a3，因为不是temp溢出，所以不用存放temp号，这里用-1代替temp号
		}
	}
	public void FuncEnd(ARGU argu,int ParaNum)
	{//函数结束，恢复所有的寄存器
		Proc currProc = (Proc)argu;
		Vector<String> Registers= currProc.TakenRegs();
		if(ParaNum-4>0)
		//如果参数数目大于4，因为大于4的参数在栈里面，所以恢复寄存器的时候要从f1-3开始往下恢复，f1代表参数数目
		  for(int i=ParaNum-4,j=0;i<ParaNum-4+Registers.size();i++,j++)
		   OutPut.con(" ALOAD " +  Registers.get(j) + " SPILLEDARG " + i + "\n");
		else//如果参数数目小于等于4，因为没有参数在栈里面，所以恢复寄存器的时候从0开始往下恢复即可
		  for(int i=0,j=0;i<Registers.size();i++,j++)
			OutPut.con(" ALOAD " +  Registers.get(j) + " SPILLEDARG " + i + "\n");	
		  OutPut.con("END\n");
	}
	
		public String ReadTemp(Temp n,ARGU argu, int vRegister)
	   {
         Proc currProc = (Proc)argu;
		 String t1 = currProc.GetReg(Integer.parseInt(n.f1.f0.tokenImage));
		 if(t1.equals("OverFlow"))
		 {
		  	OutPut.con(" ALOAD " + RegPlan.RegList[vRegister+4] + " SPILLEDARG " +currProc.StackRecorder.indexOf(Integer.parseInt(n.f1.f0.tokenImage))+ "\n");
			return RegPlan.RegList[vRegister+4];
		 }
		 else//如果当前temp 要占用寄存器的话
            return t1;
	   }
	   	public String WriteTemp(Temp n, ARGU argu,int vRegister)
	   {
         Proc currProc = (Proc)argu;
		 String t1 = currProc.GetReg(Integer.parseInt(n.f1.f0.tokenImage));
         if(t1.equals("OverFlow"))
		   return RegPlan.RegList[vRegister+4];
		 else//如果当前temp 占用寄存器的话
            return t1;
		
       }
       	public void SpillToStack(Temp n, ARGU argu,int vRegister)
	    {//如果转换之前HLOAD的第一个寄存器是注定要溢出的，那么用v0或v1先存放HLOAD进来的值，然后把该值溢出到栈里
	     Proc currProc = (Proc)argu;
		 String t1 = currProc.GetReg(Integer.parseInt(n.f1.f0.tokenImage));
		 if(t1.equals("OverFlow"))
		 {
		   if( !currProc.StackRecorder.contains(Integer.parseInt(n.f1.f0.tokenImage)))
		   {
		    OutPut.con(" ASTORE " + " SPILLEDARG " + currProc.StackRecorder.size()+ " " + RegPlan.RegList[vRegister+4] + "\n");
		    currProc.StackRecorder.add(Integer.parseInt(n.f1.f0.tokenImage));
		   }
		   else  
			OutPut.con(" ASTORE " + " SPILLEDARG " + currProc.StackRecorder.indexOf(Integer.parseInt(n.f1.f0.tokenImage))+ " " + RegPlan.RegList[vRegister+4] + "\n");
		 }
		 return ;
	    }
 }













