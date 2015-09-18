//visitor。找到所有过程


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
import ProcTable.ARGU;


public class SetProcTableVisitor<R,ARGU> extends GJDepthFirst<R,ARGU>{
	   public R visit(NodeList n, ARGU argu) {
		      R _ret=null;
		      int _count=0;
		      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
		         e.nextElement().accept(this,argu);
		         _count++;
		      }
		      return _ret;
		   }

		   public R visit(NodeListOptional n, ARGU argu) {
		      if ( n.present() ) {
		         R _ret=null;
		         int _count=0;
		         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
		            e.nextElement().accept(this,argu);
		            _count++;
		         }
		         return _ret;
		      }
		      else
		         return null;
		   }

		   public R visit(NodeOptional n, ARGU argu) {
		      if ( n.present() )
		         return n.node.accept(this,argu);
		      else
		         return null;
		   }

		   public R visit(NodeSequence n, ARGU argu) {
		      R _ret=null;
		      int _count=0;
		      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
		         e.nextElement().accept(this,argu);
		         _count++;
		      }
		      return _ret;
		   }

		   public R visit(NodeToken n, ARGU argu) { return null; }

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
		   public R visit(Goal n, ARGU argu) {
			   
			  
		      R _ret=null;
		      String procname = "MAIN";
		      Proc newproc = new Proc(procname);
		      n.f2.accept(this, argu);
		      
		      ProcTable.AddProc(newproc);
		      
		      n.f3.accept(this, argu);
		      n.f4.accept(this, argu);
		      return _ret;
		   }

		   /**
		    * f0 -> Label()
		    * f1 -> "["
		    * f2 -> IntegerLiteral()
		    * f3 -> "]"
		    * f4 -> StmtExp()
		    */
		   public R visit(Procedure n, ARGU argu) {
		      R _ret= null;
		      
		      
		      String procname = n.f0.f0.toString();
		      Proc newproc = new Proc(procname);
		      n.f1.accept(this, argu);
		      newproc.ParaNum = Integer.parseInt( n.f2.f0.toString() );
		      n.f3.accept(this, argu);
		      
		      ProcTable.AddProc(newproc);
		      
		      return _ret;
		   }


}
