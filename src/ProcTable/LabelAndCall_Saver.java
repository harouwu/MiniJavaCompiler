//记录label和过程调用


package ProcTable;

import java.util.Enumeration;

import spiglet.syntaxtree.BinOp;
import spiglet.syntaxtree.CJumpStmt;
import spiglet.syntaxtree.Call;
import spiglet.syntaxtree.ErrorStmt;
import spiglet.syntaxtree.Exp;
import spiglet.syntaxtree.Goal;
import spiglet.syntaxtree.HAllocate;
import spiglet.syntaxtree.HLoadStmt;
import spiglet.syntaxtree.HStoreStmt;
import spiglet.syntaxtree.IntegerLiteral;
import spiglet.syntaxtree.JumpStmt;
import spiglet.syntaxtree.Label;
import spiglet.syntaxtree.MoveStmt;
import spiglet.syntaxtree.NoOpStmt;
import spiglet.syntaxtree.Node;
import spiglet.syntaxtree.NodeList;
import spiglet.syntaxtree.NodeListOptional;
import spiglet.syntaxtree.NodeOptional;
import spiglet.syntaxtree.NodeSequence;
import spiglet.syntaxtree.NodeToken;
import spiglet.syntaxtree.Operator;
import spiglet.syntaxtree.PrintStmt;
import spiglet.syntaxtree.Procedure;
import spiglet.syntaxtree.SimpleExp;
import spiglet.syntaxtree.Stmt;
import spiglet.syntaxtree.StmtExp;
import spiglet.syntaxtree.StmtList;
import spiglet.syntaxtree.Temp;
import spiglet.visitor.GJDepthFirst;

public class LabelAndCall_Saver <Object ,ARGU> extends GJDepthFirst<Object ,ARGU>{
	   public Object visit(NodeList n, ARGU argu) {
		   Object _ret=null;
		      int _count=0;
		      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
		         e.nextElement().accept(this,argu);
		         _count++;
		      }
		      return _ret;
		   }

		   public Object visit(NodeListOptional n, ARGU argu) {
			   String _ret =  "0";
		      if ( n.present() ) {

		         int _count=0;
		         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
		            e.nextElement().accept(this,argu);
		            _count++;
		         }
		         String temp = _count+"";
		         return (Object)temp;
		      }
		      else
		         return (Object)_ret;
		   }

		   public Object visit(NodeOptional n, ARGU argu) {
		      if ( n.present() )
		         return n.node.accept(this,argu);
		      else
		         return null;
		   }

		   public Object visit(NodeSequence n, ARGU argu) {
			   Object _ret=null;
		      int _count=0;
		      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
		         e.nextElement().accept(this,argu);
		         _count++;
		      }
		      return _ret;
		   }

		   public Object visit(NodeToken n, ARGU argu) { return null; }

		   //
		   // User-generated visitor methods below
		   //

		   /**
		    * f0 -> "MAIN"
		    * f1 -> StmtList()
		    * f2 -> "END"
		    * f3 -> ( Procedure() )*
		    * f4 -> <EOF>
		    */
		   public Object visit(Goal n, ARGU argu) {
			   
			  
			   Object _ret=null;
		      Proc tempproc = ProcTable.SearchProc("MAIN");
		      tempproc.MaxPara = tempproc.ParaNum;
		      n.f1.accept(this,(ARGU)tempproc);
		      n.f3.accept(this,argu);

		      return _ret;
		   }

		   /**
		    * f0 -> Label()
		    * f1 -> "["
		    * f2 -> StringLiteral()
		    * f3 -> "]"
		    * f4 -> StmtExp()
		    */
		   public Object visit(Procedure n, ARGU argu) {
			   Object _ret= null;
		      Proc tempproc = ProcTable.SearchProc(n.f0.f0.toString());
		      tempproc.MaxPara = tempproc.ParaNum;
		    //  System.err.println(n.f0.f0.toString());

		      n.f4.accept(this,(ARGU)tempproc);
		      
		      return _ret;
		   }
		   
		   
		   /**
		    * f0 -> <IDENTIFIER>
		    */
		   public Object visit(Label n, ARGU argu) {
			   Object _ret=null;
		      String temp =(String) (n.f0.toString());
		      if (ProcTable.SearchProc(temp)==null)
		      {
		    	  ((Proc)argu).SaveLab(((Proc)argu).IntroCounter,temp);
		      }
		      n.f0.accept(this, argu);
		      return _ret;
		   }
		   
		   
		   /**
		    * f0 -> "CALL"
		    * f1 -> SimpleExp()
		    * f2 -> "("
		    * f3 -> ( Temp() )*
		    * f4 -> ")"
		    */
		   public Object visit(Call n, ARGU argu) {
			   Object _ret = null;
			  String Temp = null;
		      n.f0.accept(this, argu);
		      n.f2.accept(this, argu);
		      Temp = (String)n.f3.accept(this, argu);
		      if (Integer.parseInt(Temp)> ((Proc)argu).MaxPara)
		      {
		    	  ((Proc)argu).MaxPara = Integer.parseInt(Temp);
		      }
		    //  System.err.println(Temp);
		      n.f4.accept(this, argu);
		      return _ret;
		   }
		   /**
		    * f0 -> ( ( Label() )? Stmt() )*
		    */
		   public Object visit(StmtList n, ARGU argu) {
			   Object _ret=null;
		      n.f0.accept(this, argu);
		      return _ret;
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
		   public Object visit(Stmt n, ARGU argu) {
			   Object _ret=null;
		      n.f0.accept(this, argu);
			   ((Proc)argu).IntroCounter++;
		      return _ret;
		   }


		   /**
		    * f0 -> "CJUMP"
		    * f1 -> Temp()
		    * f2 -> Label()
		    */
		   public Object visit(CJumpStmt n, ARGU argu) {
			   Object _ret=null;
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
		      return _ret;
		   }

		   /**
		    * f0 -> "JUMP"
		    * f1 -> Label()
		    */
		   public Object visit(JumpStmt n, ARGU argu) {
			   Object _ret=null;
		      n.f0.accept(this, argu);
		      return _ret;
		   }

		   /**
		    * f0 -> "MOVE"
		    * f1 -> Temp()
		    * f2 -> Exp()
		    */
		   public Object visit(MoveStmt n, ARGU argu) {
			   Object _ret=null;
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
		      n.f2.accept(this,argu);
		      return _ret;
		   }

		   /**
		    * f0 -> "PRINT"
		    * f1 -> SimpleExp()
		    */
		   public Object visit(PrintStmt n, ARGU argu) {
			   Object _ret=null;
		      n.f0.accept(this, argu);
		      return _ret;
		   }

		   /**
		    * f0 -> Call()
		    *       | HAllocate()
		    *       | BinOp()
		    *       | SimpleExp()
		    */
		   public Object visit(Exp n, ARGU argu) {
			   Object _ret=null;
			   n.f0.accept(this,argu);
		      return _ret;
		   }

		   /**
		    * f0 -> "BEGIN"
		    * f1 -> StmtList()
		    * f2 -> "RETURN"
		    * f3 -> SimpleExp()
		    * f4 -> "END"
		    */
		   public Object visit(StmtExp n, ARGU argu) {
			   Object _ret=null;
		      n.f0.accept(this, argu);
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);
		      n.f3.accept(this, argu);		
		      ((Proc)argu).IntroCounter++;
		      n.f4.accept(this, argu);
		      return _ret;
		   }

		   
}
