//收集所有指令的活跃变量信息


package ProcTable;

import java.util.Enumeration;
import java.util.Vector;

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

public class IntroCollectorVisitor <Object ,ARGU> extends GJDepthFirst<Object ,ARGU>{
	
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
			  Object _ret = null;
			  Vector  newvector = new Vector();
		      if ( n.present() ) {

		         int _count=0;
		         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
		           newvector.add( e.nextElement().accept(this,argu));
		            _count++;
		         }
		         _ret = (Object)newvector;
		         return _ret;
		      }
		      else
		         return _ret;
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
			  tempproc.IntroCounter=0;
		      n.f0.accept(this, argu);
		      n.f1.accept(this, (ARGU)tempproc);
		      n.f2.accept(this, argu);
		      n.f3.accept(this, argu);
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
		    * f0 -> Label()
		    * f1 -> "["
		    * f2 -> IntegerLiteral()
		    * f3 -> "]"
		    * f4 -> StmtExp()
		    */
		   public Object visit(Procedure n, ARGU argu) {
			   Object _ret=null;
		      Proc tempproc = ProcTable.SearchProc(n.f0.f0.toString());
		      tempproc.IntroCounter = 0;
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);
		      n.f3.accept(this, argu);
		      n.f4.accept(this, (ARGU)tempproc);

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
		    * f0 -> "JUMP"
		    * f1 -> Label()
		    */
		   public Object visit(JumpStmt n, ARGU argu) {
			   Object _ret=null;
		      n.f0.accept(this, argu);
		      MyLabel templabel = ((Proc)argu).labels.get(n.f1.f0.toString());
		      int i=0,j=0;
		      for (i = 0 ;i<((Proc)argu).blocks.size();i++)
		      {
		    	  if (templabel.Pos == ((Proc)argu).blocks.get(i).Start)
		    	  {
		    		  for (j=0;j<((Proc)argu).blocks.size();j++)
		    		  {
		    			  if (((Proc)argu).blocks.get(j).End == ((Proc)argu).IntroCounter)
		    			  {
		    				  ((Proc)argu).blocks.get(j).JumpDes = i;
		    				  ((Proc)argu).blocks.get(j).Next = i;
		    			  }
		    		  }
		    	  }
		      }
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
		      if (n.f3.accept(this, argu)!=null)
		      {
		    	  Vector newvector = new Vector();
		    	  newvector.add(((Vector)n.f3.accept(this,argu)).get(0));
				  ((Proc)argu).AddIntro(((Proc)argu).IntroCounter,null,newvector);
		      }
		      ((Proc)argu).IntroCounter++;
		      n.f4.accept(this, argu);
		      return _ret;
		   }
		   
		   
		   /** 已处理
		    * f0 -> "CALL"
		    * f1 -> SimpleExp()
		    * f2 -> "("
		    * f3 -> ( Temp() )*
		    * f4 -> ")"
		    */
		   public Object visit(Call n, ARGU argu) {
			   Object _ret = null;
		      n.f0.accept(this, argu);
	    	  Vector newvector = new Vector();
		      if(n.f1.accept(this, argu)!=null)
		      {
		    	  newvector.add(((Vector)n.f1.accept(this,argu)).get(0));
				//   ((Proc)argu).AddIntro(null,newvector);
		      }
		      Vector temp = new Vector();
		      n.f2.accept(this, argu);
		      temp = (Vector)n.f3.accept(this,argu);
		      if(temp!=null)
		      {
		    	  int i=0;
		    	  for (i=0;i<temp.size();i++)
		    	  {
		    		 if (!newvector.contains(((Vector)temp.get(i)).get(0)))
		    			{
		    			 newvector.add(((Vector)temp.get(i)).get(0));
		    			}
		    	  }
		      }
	    	  _ret = (Object)newvector;
		    //  System.err.println(Temp);
		      n.f4.accept(this, argu);
		      return _ret;
		   }


		   /** 已处理
		    * f0 -> "CJUMP"
		    * f1 -> Temp()
		    * f2 -> Label()
		    */
		   public Object visit(CJumpStmt n, ARGU argu) {
			   Object _ret=null;
			  MyLabel templabel = ((Proc)argu).labels.get(n.f2.f0.toString());
			  int i=0,j=0;
			      for (i = 0 ;i<((Proc)argu).blocks.size();i++)
			      {
			    	  if (templabel.Pos == ((Proc)argu).blocks.get(i).Start)
			    	  {
			    		  for (j=0;j<((Proc)argu).blocks.size();j++)
			    		  {
			    			  if (((Proc)argu).blocks.get(j).End == ((Proc)argu).IntroCounter)
			    			  {
			    				  ((Proc)argu).blocks.get(j).JumpDes = i;
			    			  }
			    		  }
			    	  }
			      }
			   Vector newvector = new Vector();
		      newvector.add(((Vector)n.f1.accept(this,argu)).get(0));
			   ((Proc)argu).AddIntro(((Proc)argu).IntroCounter,null,newvector);
		      return _ret;
		   }


		   /**已处理
		    * f0 -> "HSTORE"
		    * f1 -> Temp()
		    * f2 -> IntegerLiteral()
		    * f3 -> Temp()
		    */
		   public Object visit(HStoreStmt n, ARGU argu) {
			   Object _ret=null;
			   Vector newvector = new Vector();
			   newvector.add(((Vector)n.f1.accept(this,argu)).get(0));
			   if(!newvector.contains(((Vector)n.f3.accept(this,argu)).get(0)))
			   {
				   newvector.add(((Vector)n.f3.accept(this,argu)).get(0));
			   }
			   ((Proc)argu).AddIntro(((Proc)argu).IntroCounter,null,newvector);
		      return _ret;
		   }

		   /**已处理
		    * f0 -> "HLOAD"
		    * f1 -> Temp()
		    * f2 -> Temp()
		    * f3 -> IntegerLiteral()
		    */
		   public Object visit(HLoadStmt n, ARGU argu) {
			   Object _ret=null;
			   String s1=(String)((Vector)n.f1.accept(this,argu)).get(0);
			   Vector newvector = new Vector();
			   newvector.add(((Vector)n.f2.accept(this,argu)).get(0));
			   ((Proc)argu).AddIntro(((Proc)argu).IntroCounter,s1,newvector);
		      return _ret;
		   }

		   /** 已处理
		    * f0 -> "MOVE"
		    * f1 -> Temp()
		    * f2 -> Exp()
		    */
		   public Object visit(MoveStmt n, ARGU argu) {
			   Object _ret=null;
			  String s=(String)((Vector)n.f1.accept(this,argu)).get(0);
			  ((Proc)argu).AddIntro(((Proc)argu).IntroCounter,s,(Vector)n.f2.accept(this,argu));

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
		      _ret = n.f0.accept(this, argu);
		      return _ret;
		   }

		   /**已处理
		    * f0 -> "PRINT"
		    * f1 -> SimpleExp()
		    */
		   public Object visit(PrintStmt n, ARGU argu) {
			   Object _ret=null;
		      if(n.f1.accept(this, argu)!=null)
		      {
				   Vector newvector = new Vector();
		    	  newvector.add(((Vector)n.f1.accept(this,argu)).get(0));
		    	  ((Proc)argu).AddIntro(((Proc)argu).IntroCounter,null, newvector);
		      }
		      return _ret;
		   }



		   /**已处理
		    * f0 -> "HALLOCATE"
		    * f1 -> SimpleExp()
		    */
		   public Object visit(HAllocate n, ARGU argu) {
			   Object _ret=null;
		      if (n.f1.accept(this, argu)!=null)
		    	{
				   Vector newvector = new Vector();
		    	  	newvector.add(((Vector)n.f1.accept(this,argu)).get(0));
				    _ret = (Object)newvector;
		    	}
		      return _ret;
		   }

		   /**已处理
		    * f0 -> Operator()
		    * f1 -> Temp()
		    * f2 -> SimpleExp()
		    */
		   public Object visit(BinOp n, ARGU argu) {
			   Object _ret=null;
			   Vector newvector = new Vector();
			   
		      newvector.add(((Vector)n.f1.accept(this,argu)).get(0));
		      
		      if (n.f2.accept(this, argu)!=null)
		      {
		    	  if(!newvector.contains(((Vector)n.f2.accept(this,argu)).get(0)))
		    	  {
		    		   newvector.add(((Vector)n.f2.accept(this,argu)).get(0));
		    	  }
		      }
		      _ret = (Object)newvector;
		      return _ret;
		   }

		   /**已处理
		    * f0 -> Temp()
		    *       | IntegerLiteral()
		    *       | Label()
		    */
		   public Object visit(SimpleExp n, ARGU argu) {
			   Object _ret=null;
			   
		      _ret = n.f0.accept(this, argu);

		      return _ret;
		   }

		   /**已处理
		    * f0 -> "TEMP"
		    * f1 -> IntegerLiteral()
		    */
		   public Object visit(Temp n, ARGU argu) {
			   Object _ret=null;
			   Vector newvector = new Vector();
		      newvector.add( n.f1.f0.toString());
		      _ret = (Object)newvector;
		      return _ret;
		   }
}
