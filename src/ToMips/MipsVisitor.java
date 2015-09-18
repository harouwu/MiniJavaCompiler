package ToMips;
import kanga.syntaxtree.ALoadStmt;
import kanga.syntaxtree.AStoreStmt;
import kanga.syntaxtree.BinOp;
import kanga.syntaxtree.CJumpStmt;
import kanga.syntaxtree.CallStmt;
import kanga.syntaxtree.ErrorStmt;
import kanga.syntaxtree.Exp;
import kanga.syntaxtree.Goal;
import kanga.syntaxtree.HAllocate;
import kanga.syntaxtree.HLoadStmt;
import kanga.syntaxtree.HStoreStmt;
import kanga.syntaxtree.IntegerLiteral;
import kanga.syntaxtree.JumpStmt;
import kanga.syntaxtree.Label;
import kanga.syntaxtree.MoveStmt;
import kanga.syntaxtree.NoOpStmt;
import kanga.syntaxtree.Operator;
import kanga.syntaxtree.PassArgStmt;
import kanga.syntaxtree.PrintStmt;
import kanga.syntaxtree.Procedure;
import kanga.syntaxtree.Reg;
import kanga.syntaxtree.SimpleExp;
import kanga.syntaxtree.SpilledArg;
import kanga.syntaxtree.Stmt;
import kanga.syntaxtree.StmtList;
import kanga.visitor.GJDepthFirst;
import spiglet.visitor.GJVoidDepthFirst;
import tospiglet.SpigletOutput;
import ProcTable.ARGU;

public class MipsVisitor extends  GJDepthFirst<String,Object> {
	
	//public MipsVisitor()
	//{};
	/**
	 * f0 -> "MAIN"
	 * f1 -> "["
	 * f2 -> IntegerLiteral()
	 * f3 -> "]"
	 * f4 -> "["
	 * f5 -> IntegerLiteral()
	 * f6 -> "]"
	 * f7 -> "["
	 * f8 -> IntegerLiteral()
	 * f9 -> "]"
	 * f10 -> StmtList()
	 * f11 -> "END"
	 * f12 -> ( Procedure() )*
	 * f13 -> <EOF>
	 */
	public String visit(Goal n, Object argu)
	{
		MipsOutPut.Space+=" ";
		MipsOutPut.add(MipsOutPut.Space+".text \n" );
		MipsOutPut.add(MipsOutPut.Space+".globl main \n" );
		MipsOutPut.add("main: \n" );
		MipsOutPut.add(MipsOutPut.Space+"sw $fp, -8($sp) \n");
		MipsOutPut.add(MipsOutPut.Space+"move $fp, $sp \n");
		int stackLength = (Integer.parseInt(n.f5.f0.tokenImage)+2)*4;
        MipsOutPut.add(MipsOutPut.Space+"subu  $sp, $sp, " +stackLength +"\n");
		MipsOutPut.add(MipsOutPut.Space+"sw $ra, -4($fp) \n");
		n.f10.accept(this,argu);
		MipsOutPut.add(MipsOutPut.Space+"lw $ra, -4($fp) \n");
		MipsOutPut.add(MipsOutPut.Space+"lw $fp,  -8($fp) \n");
		MipsOutPut.add(MipsOutPut.Space+"addu $sp, $sp, " +stackLength +"\n");
		MipsOutPut.add(MipsOutPut.Space+"j $ra \n");
		n.f12.accept(this,argu);
		return null;
	}  
	/**
	 * f0 -> ( ( Label() )? Stmt() )*
	 */
	public String visit(StmtList n, Object argu)
	{
	      n.f0.accept(this, argu);
	      return null;
	}

	/**
	 * f0 -> Label()
	 * f1 -> "["
	 * f2 -> IntegerLiteral()
	 * f3 -> "]"
	 * f4 -> "["
	 * f5 -> IntegerLiteral()
	 * f6 -> "]"
	 * f7 -> "["
	 * f8 -> IntegerLiteral()
	 * f9 -> "]"
	 * f10 -> StmtList()
	 * f11 -> "END"
	 */
	public String visit(Procedure n, Object argu) 
	{
		
		MipsOutPut.add(MipsOutPut.Space+".text \n");
		MipsOutPut.add(n.f0.f0.tokenImage+": \n");
	    MipsOutPut.add(MipsOutPut.Space+"sw $fp, -8($sp) \n");
		MipsOutPut.add(MipsOutPut.Space+"move $fp, $sp \n");
        int stackLength = (Integer.parseInt(n.f5.f0.tokenImage)+2)*4;
        MipsOutPut.add(MipsOutPut.Space+"subu  $sp, $sp, " +stackLength +"\n");
		MipsOutPut.add(MipsOutPut.Space+"sw $ra, -4($fp) \n");
		n.f10.accept(this,argu);
		MipsOutPut.add(MipsOutPut.Space+"lw $ra, -4($fp) \n");
		MipsOutPut.add(MipsOutPut.Space+"lw $fp,  -8($fp) \n");
		MipsOutPut.add(MipsOutPut.Space+"addu $sp, $sp, " +stackLength +"\n");
		MipsOutPut.add(MipsOutPut.Space+"j $ra \n");
		return null;
	}
	/**
	 * f0 -> NoOpStmt()
	 *       | ErrorStmt()
	 *       | CJumpStmt()
	 *       | JumpStmt()
	 *       | HStoreStmt()
	 *       | HLoadStmt()
	 *       | MoveStmt()
	 *       | PrintStmt()
	 *       | ALoadStmt()
	 *       | AStoreStmt()
	 *       | PassArgStmt()
	 *       | CallStmt()
	 */
	public String visit(Stmt n, Object argu) 
	{
	      n.f0.accept(this, argu);
	      return null;
	}
	/**
	 * f0 -> "NOOP"
	 */
	public String visit(NoOpStmt n, Object argu)
	{
		MipsOutPut.add(MipsOutPut.Space+"nop \n" );
	    return null;
	}
	/**
     * f0 -> "ERROR"
	 */
	public String visit(ErrorStmt n, Object argu) 
	{
		MipsOutPut.add(MipsOutPut.Space+"jal _error\n");
	      return null;
	}
	   /**
	    * f0 -> "CJUMP"
	    * f1 -> Reg()
	    * f2 -> Label()
	    */
	public String visit(CJumpStmt n, Object argu)
	{
		int RegNum = n.f1.f0.which;
		MipsOutPut.add(MipsOutPut.Space+"beqz " + Regs.RegList[RegNum] + ", " + n.f2.f0.toString() + "\n");
	    return null;
	}
	   /**
	    * f0 -> "JUMP"
	    * f1 -> Label()
	    */
	   public String visit(JumpStmt n, Object argu)
	   {
		  MipsOutPut.add(MipsOutPut.Space+"j "  + n.f1.f0.toString()+"\n");
	      return null;
	   }
	   /**
	    * f0 -> "HSTORE"
	    * f1 -> Reg()
	    * f2 -> IntegerLiteral()
	    * f3 -> Reg()
	    */
	   public String visit(HStoreStmt n, Object argu)
	   {
           int RegRsNum = n.f3.f0.which;
		   int RegRtNum = n.f1.f0.which;
		   MipsOutPut.add(MipsOutPut.Space+"sw " + Regs.RegList[RegRsNum ] +", "+ n.f2.f0.toString() + "(" + Regs.RegList[RegRtNum] +") \n");
		   return null;
	   }
	   /**
	    * f0 -> "HLOAD"
	    * f1 -> Reg()
	    * f2 -> Reg()
	    * f3 -> IntegerLiteral()
	    */
	   public String visit(HLoadStmt n, Object argu)
	   {
           int RegRsNum = n.f2.f0.which;
		   int RegRtNum = n.f1.f0.which;		   
		   MipsOutPut.add(MipsOutPut.Space+"lw "+Regs.RegList[RegRtNum] +", " + n.f3.f0.toString() + "(" + Regs.RegList[RegRsNum] +") \n");
	   	   return null;
	   }
	   /**
	    * f0 -> "MOVE"
	    * f1 -> Reg()
	    * f2 -> Exp()
	    * 				/** Exp()
	    				* Grammar production:
						 * f0 -> HAllocate()
						 *       | BinOp()
						 *       | SimpleExp()
						 *       
					   */
	   public String visit(MoveStmt n, Object argu) 
	   {	     
	     int RegRtNum = n.f1.f0.which; 
		 if(n.f2.f0.which == 0)
		 {
			   n.f2.f0.accept(this,argu);
			   MipsOutPut.add(MipsOutPut.Space+"jal " + "_halloc \n");
			   MipsOutPut.add(MipsOutPut.Space+"move "+ Regs.RegList[RegRtNum] + " $v0 \n");
			   MipsOutPut.add(MipsOutPut.Space+"move $a0 $t9 \n");//因为涉及到mips调用函数halloc，这个函数不是从kanga翻译出来的，这里用a0传参数，但要先用t9保存a0,调用完成之后用t9恢复a0
		 }
		 if(n.f2.f0.which == 1)
		 {
			  BinOp p = ((BinOp)n.f2.f0.choice);
			  int RegRsNum = p.f1.f0.which;
			  String val2 = p.f2.accept(this,argu);
			  String val1 = p.accept(this,argu);
			  MipsOutPut.add(MipsOutPut.Space+val1 +" " +Regs.RegList[RegRtNum] +", "+ Regs.RegList[RegRsNum] + ", "  + val2 + "\n");
		 }
		 else if(n.f2.f0.which == 2)
		 {
			 SimpleExp Exp = ((SimpleExp)n.f2.f0.choice);
			 String exp = Exp.accept(this,argu);
             switch(Exp.f0.which)
		     {//如果move的第二个操作数是标号或者整数，需要先li或者la到$t9里，再move到第一个操作数里
		       case 0:MipsOutPut.add(MipsOutPut.Space+"move " + Regs.RegList[RegRtNum] + " "+ exp + "\n");break;
		       case 1:MipsOutPut.add(MipsOutPut.Space+"li $t9 "  + exp + "\n");
		              MipsOutPut.add(MipsOutPut.Space+"move " + Regs.RegList[RegRtNum] + " $t9 \n");break;
		       case 2:MipsOutPut.add(MipsOutPut.Space+"la $t9 "+exp + "\n");
		              MipsOutPut.add(MipsOutPut.Space+"move " +Regs.RegList[RegRtNum] +" $t9 \n");break;
		       default:break;
		     }
		 }
	     return null;
	   }
	   /**
	    * f0 -> "PRINT"
	    * f1 -> SimpleExp()
	    * 					/** SimpleExp
					    * Grammar production:
					    * f0 -> Reg()
					    *       | IntegerLiteral()
					    *       | Label()
					    */
	   public String visit(PrintStmt n, Object argu)
	   {//因为涉及到mips调用函数printint，这个函数不是从kanga翻译出来的，这里用a0传参数，但要先用t9保存a0,调用完成之后用t9恢复a0
	      String exp =  n.f1.accept(this,argu);
	      MipsOutPut.add(MipsOutPut.Space+"move $t9 $a0 \n");
          switch(n.f1.f0.which)
	      {
	       case 0:MipsOutPut.add(MipsOutPut.Space+"move $a0 "+ exp + "\n");break;
	       case 1:MipsOutPut.add(MipsOutPut.Space+"li $a0 " + exp + "\n");break;
	       case 2:MipsOutPut.add(MipsOutPut.Space+"la $a0 "+ exp +"\n");break;
	       default:break;
	      }
	      MipsOutPut.add(MipsOutPut.Space+"jal _printint \n" );
	      MipsOutPut.add(MipsOutPut.Space+"move $a0 $t9 \n");
	      return null;
	   }
	   /**
	    * f0 -> "ALOAD"
	    * f1 -> Reg()
	    * f2 -> SpilledArg()
	    */
	   public String visit(ALoadStmt n, Object argu)
	   {
	      int stackPos = 4*Integer.parseInt(n.f2.f1.f0.tokenImage);
	      int RegNum = n.f1.f0.which;
	        MipsOutPut.add(MipsOutPut.Space+"lw "+ Regs.RegList[RegNum] +", "+ stackPos + "( $sp ) \n");
	      return null;
	   }
	   /**
	    * f0 -> "ASTORE"
	    * f1 -> SpilledArg()
	    * f2 -> Reg()
	    */
	   public String visit(AStoreStmt n, Object argu)
	   {
		   int stackPos = 4*Integer.parseInt(n.f1.f1.f0.tokenImage);
           int RegNum = n.f2.f0.which;
           MipsOutPut.add(MipsOutPut.Space+"sw " + Regs.RegList[RegNum] +", " + stackPos + "($sp) \n");
           return null;
	   }
	   /**
	    * f0 -> "PASSARG"
	    * f1 -> IntegerLiteral()
	    * f2 -> Reg()
	    */
	   public String visit(PassArgStmt n, Object argu)
	   {
		   int stackPos = 4*( Integer.parseInt(n.f1.f0.tokenImage)-1);
		   int RegNum = n.f2.f0.which;
		   MipsOutPut.add(MipsOutPut.Space+"sw "+ Regs.RegList[RegNum] + ", " + stackPos+ "( $sp ) \n" );
	       return null;
	   }	   
	   /**
	    * f0 -> "CALL"
	    * f1 -> SimpleExp()
	    */
	   public String visit(CallStmt n, Object argu) 
	   {
		  String val = n.f1.accept(this,argu);
          switch(n.f1.f0.which)
	      {
	       case 0: MipsOutPut.add(MipsOutPut.Space+"jalr " + val + "\n");break;
	       case 1: MipsOutPut.add(MipsOutPut.Space+"li  $a0 " + val+ "\n"+MipsOutPut.Space+"jalr $a0 \n");break;
	       case 2: MipsOutPut.add(MipsOutPut.Space+"jal " + val +"\n");break;
	      }
	      return null;
	   }
	   /**
	    * f0 -> HAllocate()
	    *       | BinOp()
	    *       | SimpleExp()
	    */
	   public String visit(Exp n, int argu)
	   {
		  return null;
		   
	   }
	   /**
	    * f0 -> "HALLOCATE"
	    * f1 -> SimpleExp()
	    */
	   public String visit(HAllocate n, Object argu)
	   {//因为涉及到mips调用函数halloc，这个函数不是从kanga翻译出来的，这里用a0传参数，但要先用t9保存a0,调用完成之后用t9恢复a0
		  String Num = n.f1.accept(this,argu);
		  MipsOutPut.add(MipsOutPut.Space+"move $t9 $a0 \n");
          switch(n.f1.f0.which)
		  {
		   case 0:MipsOutPut.add(MipsOutPut.Space+"move $a0 " + Num +"\n");break;
		   case 1:MipsOutPut.add(MipsOutPut.Space+"li $a0 " + Num +"\n");break;
		   case 2:MipsOutPut.add(MipsOutPut.Space+"la $a0 " + Num +"\n");break;
		   default:break;
		  }
		  return null;
	   }
	   /**
	    * f0 -> Operator()
	    * f1 -> Reg()
	    * f2 -> SimpleExp()
	    */
	   public String visit(BinOp n, Object argu)
	   {
          switch(n.f0.f0.which)
	      {
	       case 0:return "slt";
	       case 1:return "add";
	       case 2:return "sub";
	       case 3:return "mul";
	       default:break;
	      }
          return null;
	   }
	   /**
	    * f0 -> "LT"
	    *       | "PLUS"
	    *       | "MINUS"
	    *       | "TIMES"
	    */
	   public String visit(Operator n, Object argu)
	   {
	      return null;
	   }
	   /**
	    * f0 -> "SPILLEDARG"
	    * f1 -> IntegerLiteral()
	    */
	   public String visit(SpilledArg n, Object argu)
	   {
		   return null;
	   }
	   /**
	    * f0 -> Reg()
	    *       | IntegerLiteral()
	    *       | Label()
	    */
	   public String visit(SimpleExp n, Object argu) 
	   {
		   if(n.f0.which != 2)
			   return n.f0.accept(this,argu);
		   else
			   return ((Label)n.f0.choice).f0.tokenImage;
	   }
	   /**
	   f0->"a0","a1","a2","a3","v0","v1","s0","s1","s2","s3","s4","s5","s6","s7","t0"
		,"t1","t2","t3","t4","t5","t6","t7","t8","t9";
	    */
	   public String visit(Reg n, Object argu)
	   {
	     return "$" + n.f0.choice.toString();
	   }
	   /**
	    * f0 -> <INTEGER_LITERAL>
	    */
	   public String visit(IntegerLiteral n, Object argu) 
	   {
	      return n.f0.tokenImage;
	   }

	   /**
	    * f0 -> <IDENTIFIER>
	    */
	   public String visit(Label n, Object argu)
	   {
		  MipsOutPut.add(MipsOutPut.Space+n.f0.tokenImage + ":");
	      return null;
	   }	   
}
