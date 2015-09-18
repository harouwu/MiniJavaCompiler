package tospiglet;

import java.util.Vector;
import piglet.syntaxtree.BinOp;
import piglet.syntaxtree.CJumpStmt;
import piglet.syntaxtree.Call;
import piglet.syntaxtree.ErrorStmt;
import piglet.syntaxtree.Exp;
import piglet.syntaxtree.Goal;
import piglet.syntaxtree.HAllocate;
import piglet.syntaxtree.HLoadStmt;
import piglet.syntaxtree.HStoreStmt;
import piglet.syntaxtree.IntegerLiteral;
import piglet.syntaxtree.JumpStmt;
import piglet.syntaxtree.Label;
import piglet.syntaxtree.MoveStmt;
import piglet.syntaxtree.NoOpStmt;
import piglet.syntaxtree.Node;
import piglet.syntaxtree.NodeListOptional;
import piglet.syntaxtree.Operator;
import piglet.syntaxtree.PrintStmt;
import piglet.syntaxtree.Procedure;
import piglet.syntaxtree.Stmt;
import piglet.syntaxtree.StmtExp;
import piglet.syntaxtree.StmtList;
import piglet.syntaxtree.Temp;
import piglet.visitor.GJDepthFirst;

import tospiglet.SpigletOutput;
import tospiglet.SpigletTemp;

public class GetMaxTempVisitor extends GJDepthFirst<String,Object>{
	
	public static int MaxNumber=0;

	String []expression = {"StmtExp","Call","HAllocate","BinOp","Temp","IntegerLiteral","Label"};
	/**
	    * f0 -> "MAIN"
	    * f1 -> StmtList()
	    * f2 -> "END"
	    * f3 -> ( Procedure() )*
	    * f4 -> <EOF>
	    */
	   public String visit(Goal n, Object argu)
	   {
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);
		      n.f3.accept(this, argu);
		      n.f4.accept(this, argu);
	      return null;
	   }
	   /**
	    * f0 -> ( ( Label() )? Stmt() )*
	    */
	   public String  visit(StmtList n, Object argu) {
		   n.f0.accept(this,argu);
	      return null;
	   }
	   /**
	    * f0 -> Label()
	    * f1 -> "["
	    * f2 -> IntegerLiteral()
	    * f3 -> "]"
	    * f4 -> StmtExp()
	    */
	   public String visit(Procedure n, Object argu)//接受函数主体
	   {
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);
		      n.f3.accept(this, argu);
		      n.f4.accept(this, argu);
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
		      n.f0.accept(this, argu);
	      return null;
	   }	
	   /**
	    * f0 -> "ERROR"
	    */
	   public String visit(ErrorStmt n, Object argu)
	   {
		      n.f0.accept(this, argu);
	     return null;
	   }	   
	   /**
	    * f0 -> "CJUMP"
	    * f1 -> Exp()
	    * f2 -> Label()
	    */
	   public String  visit(CJumpStmt n, Object argu) 
	   {
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);

	      return null;
	      
	   }	
	   /**
	    * f0 -> "JUMP"
	    * f1 -> Label()
	    */
	   public String visit(JumpStmt n, Object argu)
	   {
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
	      return null;
	   }	   
	   /**
	    * f0 -> "HSTORE"
	    * f1 -> Exp()
	    * f2 -> IntegerLiteral()
	    * f3 -> Exp()
	    */
	   public String visit(HStoreStmt n, Object argu)
	   {
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);
		      n.f3.accept(this, argu);
	      return null;
	   }
	   /**
	    * f0 -> "HLOAD"
	    * f1 -> Temp()
	    * f2 -> Exp()
	    * f3 -> IntegerLiteral()
	    */
	   public String visit(HLoadStmt n, Object argu)
	   {
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);
		      n.f3.accept(this, argu);
		   return null;
	   }
	   /**
	    * f0 -> "MOVE"
	    * f1 -> Temp()
	    * f2 -> Exp()
	    */
	   public String  visit(MoveStmt n, Object argu)
	   {
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);
		      return null;
	   }
	   /**
	    * f0 -> "PRINT"
	    * f1 -> Exp()
	    * 
	    */
	   public String visit(PrintStmt n, Object argu)
	   {
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);

		   return null;
	   }	

	   /**
	    * f0 -> StmtExp()
	    *       | Call()
	    *       | HAllocate()
	    *       | BinOp()
	    *       | Temp()
	    *       | IntegerLiteral()
	    *       | Label()
	    */
	   public String visit(Exp n, Object argu) 
	   {
		      n.f0.accept(this, argu);

	      return  null;
	   }

	   /**
	    * f0 -> "BEGIN"
	    * f1 -> StmtList()
	    * f2 -> "RETURN"
	    * f3 -> Exp()
	    * f4 -> "END"
	    */
	   public String visit(StmtExp n, Object argu)
	   {
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);
		      n.f3.accept(this, argu);
		      n.f4.accept(this, argu);
		      return null;
	   }	
	   /**
	    * f0 -> "CALL"
	    * f1 -> Exp()
	    * f2 -> "("
	    * f3 -> ( Exp() )*
	    * f4 -> ")"
	    */
	   public String visit(Call n, Object argu)//有点小问题
	   {
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);
		      n.f3.accept(this, argu);
		      n.f4.accept(this, argu);
		      return null;
	   }   
	   
	
	   /**
	    * f0 -> "HALLOCATE"
	    * f1 -> Exp()
	    */
	   public String visit(HAllocate n, Object argu)
	   {
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
		      return null;
         }
	   /**
	    * f0 -> Operator()
	    * f1 -> Exp()
	    * f2 -> Exp()
	    */
	   public String visit(BinOp n, Object argu)
	   {
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);
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
		      n.f0.accept(this, argu);
		      return null;
	   }

	   /**
	    * f0 -> "TEMP"
	    * f1 -> IntegerLiteral()
	    */
	   public String visit(Temp n, Object argu)
	   {
		      n.f0.accept(this, argu);
		    	  if (Integer.parseInt(n.f1.f0.toString())>MaxNumber)
		    	  {
		    		  MaxNumber = Integer.parseInt(n.f1.f0.toString());
		    	  }

		      return null;
	   }

	   /**
	    * f0 -> <INTEGER_LITERAL>
	    */
	   public String visit(IntegerLiteral n, Object argu)
	   {
		      n.f0.accept(this, argu);
		      return null;
	   }

	   /**
	    * f0 -> <IDENTIFIER>
	    */
	   public String visit(Label n, Object argu) 
	   {
		      n.f0.accept(this, argu);
		      return null;
	   }
	   
	
}
