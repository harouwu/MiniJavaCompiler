package minijava.visitor;

import minijava.*;
import java.util.Enumeration;
import java.util.Vector;
import minijava.symboltable.MIdentifier;
import minijava.symboltable.MType;
import minijava.symboltable.MyClass;
import minijava.symboltable.MyCollector;
import minijava.symboltable.MyMeth;
import minijava.symboltable.MyTempCollector;
import minijava.symboltable.MyType;
import minijava.symboltable.MyVar;
import minijava.syntaxtree.AllocationExpression;
import minijava.syntaxtree.AndExpression;
import minijava.syntaxtree.ArrayAllocationExpression;
import minijava.syntaxtree.ArrayAssignmentStatement;
import minijava.syntaxtree.ArrayLength;
import minijava.syntaxtree.ArrayLookup;
import minijava.syntaxtree.ArrayType;
import minijava.syntaxtree.AssignmentStatement;
import minijava.syntaxtree.Block;
import minijava.syntaxtree.BooleanType;
import minijava.syntaxtree.BracketExpression;
import minijava.syntaxtree.ClassDeclaration;
import minijava.syntaxtree.ClassExtendsDeclaration;
import minijava.syntaxtree.CompareExpression;
import minijava.syntaxtree.Expression;
import minijava.syntaxtree.ExpressionList;
import minijava.syntaxtree.ExpressionRest;
import minijava.syntaxtree.FalseLiteral;
import minijava.syntaxtree.FormalParameter;
import minijava.syntaxtree.FormalParameterList;
import minijava.syntaxtree.FormalParameterRest;
import minijava.syntaxtree.Goal;
import minijava.syntaxtree.Identifier;
import minijava.syntaxtree.IfStatement;
import minijava.syntaxtree.IntegerLiteral;
import minijava.syntaxtree.IntegerType;
import minijava.syntaxtree.MainClass;
import minijava.syntaxtree.MessageSend;
import minijava.syntaxtree.MethodDeclaration;
import minijava.syntaxtree.MinusExpression;
import minijava.syntaxtree.Node;
import minijava.syntaxtree.NodeList;
import minijava.syntaxtree.NodeListOptional;
import minijava.syntaxtree.NodeOptional;
import minijava.syntaxtree.NodeSequence;
import minijava.syntaxtree.NodeToken;
import minijava.syntaxtree.NotExpression;
import minijava.syntaxtree.PlusExpression;
import minijava.syntaxtree.PrimaryExpression;
import minijava.syntaxtree.PrintStatement;
import minijava.syntaxtree.Statement;
import minijava.syntaxtree.ThisExpression;
import minijava.syntaxtree.TimesExpression;
import minijava.syntaxtree.TrueLiteral;
import minijava.syntaxtree.Type;
import minijava.syntaxtree.TypeDeclaration;
import minijava.syntaxtree.VarDeclaration;
import minijava.syntaxtree.WhileStatement;


public class TypeCheckVisitor extends GJDepthFirst<MType,MType>  {
	//
	   // Auto class visitors--probably don't need to be overridden.
	   //
	   public MType visit(NodeList n, MType argu) {
	      MType _ret=null;
	      int _count=0;
	      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
	         e.nextElement().accept(this,argu);
	         _count++;
	      }
	      return _ret;
	   }

	   public MType visit(NodeListOptional n, MType argu) {
		   MyTempCollector set=new MyTempCollector();
	      if ( n.present() ) {
	    	  //........................
	    	  //	System.out.println("It's in a list!");
	    	  	MType _ret=null;
	    	  	MType temp=null;
	    	    //Vector Saver = new Vector();
	    	  	int _count=0;
	    	  	for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
	    	  		temp=e.nextElement().accept(this,argu);   
	    //**********************************************
	    	  		set.Saver.addElement(temp);
	    	  		_count++;
	   //**************************************************
	    	  	}
	    	  	if (set.Saver.size()!=0)
	    	  	{
	    	  	//	 set= new MyTempCollector(Saver);
	    	  	//	 System.out.println(set.Saver.size());
	    	  		 _ret= set;	    	  		 
	    	  	}
	    	  	return _ret;
	      }
	      else
	         return null;
	   }

	   public MType visit(NodeOptional n, MType argu) {
	      if ( n.present() )
	         return n.node.accept(this,argu);
	      else
	         return null;
	   }

	   public MType visit(NodeSequence n, MType argu) {
	      MType _ret=null;
	      int _count=0;
	      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
	         e.nextElement().accept(this,argu);
	         _count++;
	      }
	      return _ret;
	   }

	   public MType visit(NodeToken n, MType argu) { return null; }

	   //
	   // User-generated visitor methods below
	   //

	   /**
	    * f0 -> MainClass()
	    * f1 -> ( TypeDeclaration() )*
	    * f2 -> <EOF>
	    */
	   public MType visit(Goal n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "class"
	    * f1 -> Identifier()
	    * f2 -> "{"
	    * f3 -> "public"
	    * f4 -> "static"
	    * f5 -> "void"
	    * f6 -> "main"
	    * f7 -> "("
	    * f8 -> "String"
	    * f9 -> "["
	    * f10 -> "]"
	    * f11 -> Identifier()
	    * f12 -> ")"
	    * f13 -> "{"
	    * f14 -> PrintStatement()
	    * f15 -> "}"
	    * f16 -> "}"
	    */
	   public MType visit(MainClass n, MType argu) {
		  
		   MType _ret = null;
	      n.f0.accept(this, argu);      
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      n.f5.accept(this, argu);
	      n.f6.accept(this, argu);
	      n.f7.accept(this, argu);
	      n.f8.accept(this, argu);
	      n.f9.accept(this, argu);
	      n.f10.accept(this, argu);
	      n.f11.accept(this, argu);
	      n.f12.accept(this, argu);
	      n.f13.accept(this, argu);
	      n.f14.accept(this, MyCollector.mainclass.Mlist.get(0));
	      n.f15.accept(this, argu);
	      n.f16.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> ClassDeclaration()
	    *       | ClassExtendsDeclaration()
	    */
	   public MType visit(TypeDeclaration n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "class"
	    * f1 -> Identifier()
	    * f2 -> "{"
	    * f3 -> ( VarDeclaration() )*
	    * f4 -> ( MethodDeclaration() )*
	    * f5 -> "}"
	    */
	   public MType visit(ClassDeclaration n, MType argu) {
	      MType _ret=null;
	      String class_name ;
	      
	      n.f0.accept(this, argu);     
	      class_name = (n.f1.accept(this, argu)).GetName();
	      MyClass myclass = MyCollector.SearchClass(class_name);    
	      
	      n.f2.accept(this, argu);
	      n.f3.accept(this,argu);
	      n.f4.accept(this,myclass);
	      n.f5.accept(this,argu);

	      return _ret;
	   }

	   
	   /**
	    * f0 -> "class"
	    * f1 -> Identifier()
	    * f2 -> "extends"
	    * f3 -> Identifier()
	    * f4 -> "{"
	    * f5 -> ( VarDeclaration() )*
	    * f6 -> ( MethodDeclaration() )*
	    * f7 -> "}"
	    */
	   public MType visit(ClassExtendsDeclaration n, MType argu) {
		 
	      MType _ret=null;
	      n.f0.accept(this, argu);     
	      
	      String class_name="";	      
	      class_name = (n.f1.accept(this, argu)).GetName();
	      MyClass myclass = MyCollector.SearchClass(class_name);
	      
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);	     
	      n.f4.accept(this, argu);
	      n.f5.accept(this,argu);
	      n.f6.accept(this,myclass);
	      n.f7.accept(this, argu);
	      
		//  System.out.println("******************");
	      return _ret;
	   }

	   /**
	    * f0 -> Type()
	    * f1 -> Identifier()
	    * f2 -> ";"
	    */

	   
	   public MType visit(VarDeclaration n, MType argu) {
		   
	       n.f0.accept(this, argu);
		   n.f1.accept(this, argu);
		   n.f2.accept(this, argu);
		   MType _ret =null;
		   return _ret;
	   }

	   /**
	    * f0 -> "public"
	    * f1 -> Type()
	    * f2 -> Identifier()
	    * f3 -> "("
	    * f4 -> ( FormalParameterList() )?
	    * f5 -> ")"
	    * f6 -> "{"
	    * f7 -> ( VarDeclaration() )*
	    * f8 -> ( Statement() )*
	    * f9 -> "return"
	    * f10 -> Expression()
	    * f11 -> ";"
	    * f12 -> "}"
	    */
	   public MType visit(MethodDeclaration n, MType argu) {
	      MType _ret=null;
	      String s=null;
	      MyVar checkreturn =null;
	      
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);	  
	      
	      s = (n.f2.accept(this, argu)).GetName();
	      MyMeth mymeth = MyCollector.SearchMeth(argu.Name,s);	
	    //  System.err.println(mymeth.Return.Type);
	      
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu); 
	      n.f5.accept(this, argu);
	      n.f6.accept(this, argu);
	      n.f7.accept(this,argu);
	      n.f8.accept(this, mymeth);
	      n.f9.accept(this, argu);
	     checkreturn = (MyVar)n.f10.accept(this, mymeth);
	     if (MyCollector.Assiable(mymeth.Return.Type , checkreturn.Type)==false)
	     {
	    	// System.err.println(checkreturn.Type);
	    	// System.err.println(mymeth.Return.Type);
	    	 MyCollector.Errs.addElement("Line "+n.f9.beginLine+": Return type not match!");
	     }
	      n.f11.accept(this, argu);
	      n.f12.accept(this, argu);
	      
	      return _ret;
	   }

	   /**
	    * f0 -> FormalParameter()
	    * f1 -> ( FormalParameterRest() )*
	    */
	   public MType visit(FormalParameterList n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);  
	      n.f1.accept(this,argu);

	      return _ret;
	   }

	   /**
	    * f0 -> Type()
	    * f1 -> Identifier()
	    */
	   public MType visit(FormalParameter n, MType argu) {
	       n.f0.accept(this, argu);
		   n.f1.accept(this, argu);

	      MType _ret=null;

	      return _ret;
	   }

	   /**
	    * f0 -> ","
	    * f1 -> FormalParameter()
	    */
	   public MType visit(FormalParameterRest n, MType argu) {
	      MType _ret=null;
	      
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);

	      return _ret;
	   }

	   /**
	    * f0 -> ArrayType()
	    *       | BooleanType()
	    *       | IntegerType()
	    *       | Identifier()
	    */
	   public MType visit(Type n, MType argu) {

	      MType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "int"
	    * f1 -> "["
	    * f2 -> "]"
	    */
	   public MType visit(ArrayType n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "boolean"
	    */
	   public MType visit(BooleanType n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "int"
	    */
	   public MType visit(IntegerType n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**  
	    * f0 -> Block()
	    *       | AssignmentStatement()
	    *       | ArrayAssignmentStatement()
	    *       | IfStatement()
	    *       | WhileStatement()
	    *       | PrintStatement()
	    */
	   public MType visit(Statement n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**  
	    * f0 -> "{"
	    * f1 -> ( Statement() )*
	    * f2 -> "}"
	    */
	   public MType visit(Block n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /** 	 	未做完
	    * f0 -> Identifier()
	    * f1 -> "="
	    * f2 -> Expression()
	    * f3 -> ";"
	    */
	   public MType visit(AssignmentStatement n, MType argu) {
	      MType _ret=null;
	      MType checkleft =new MyVar();
	      MType checkright = new MyVar();
	      
	      checkleft =n.f0.accept(this, argu);
	      checkleft = MyCollector.SearchVar((MyMeth)argu, checkleft.Name);
	      if (checkleft == null)
	      {
	    	  MyCollector.Errs.addElement("Line "+n.f0.f0.beginLine+": "+ (n.f0.accept(this,argu)).Name +" Not defined!");
	    	  return _ret;
	      }
	      
	      n.f1.accept(this, argu);
	      checkright =n.f2.accept(this, argu);
	      if (MyCollector.Assiable ( ((MyVar)checkleft).Type ,((MyVar)checkright).Type )==false)
	      {
	    	  MyCollector.Errs.addElement("Line "+n.f1.beginLine+": Type not match in a AssignmentStatement");
	      }
	      n.f3.accept(this, argu);
	      return _ret;
	   }

	   /**    未做完
	    * f0 -> Identifier()
	    * f1 -> "["
	    * f2 -> Expression()
	    * f3 -> "]"
	    * f4 -> "="
	    * f5 -> Expression()
	    * f6 -> ";"
	    */
	   public MType visit(ArrayAssignmentStatement n, MType argu) {
	      MType _ret=null;
	      MType check = new MyVar();
	      
	      check =n.f0.accept(this, argu);
	      check = MyCollector.SearchVar((MyMeth)argu, check.Name);
	      
	      if (check==null)
	      {
	    	  MyCollector.Errs.addElement("Line "+ n.f4.beginLine +": Not defiend!");
	    	  return _ret;
	      }
	      if (((MyVar)check).Type != "Array")
	      {
	    	  MyCollector.Errs.addElement("Line "+ n.f4.beginLine +": It is not a Array!");
	      }
	      
	      n.f1.accept(this, argu);
	      
	      check =n.f2.accept(this, argu);
	      if (((MyVar)check).Type != "int")
	      {
	    	  MyCollector.Errs.addElement("Line "+ n.f4.beginLine +": The Index is not a int!");
	      }
	      
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      check =n.f5.accept(this, argu);
	      if (((MyVar)check).Type != "int")
	      {
	    	  MyCollector.Errs.addElement("Line "+ n.f4.beginLine +": The Value is not a int!");
	      }
	      
	      n.f6.accept(this, argu);
	      return _ret;
	   }

	   /**  
	    * f0 -> "if"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> Statement()
	    * f5 -> "else"
	    * f6 -> Statement()
	    */
	   public MType visit(IfStatement n, MType argu) {
	      MType _ret=null;
	      MType check = new MyVar();
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      
	      check =n.f2.accept(this, argu);
	      if (((MyVar)check).Type != "boolean")
	      {
	    	  MyCollector.Errs.addElement("Line "+n.f0.beginLine+" Not a Boolean in If()!");
	      }
	      
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      n.f5.accept(this, argu);
	      n.f6.accept(this, argu);
	      return _ret;
	   }

	   /**  
	    * f0 -> "while"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> Statement()
	    */
	   public MType visit(WhileStatement n, MType argu) {
	      MType _ret=null;
	      MType check = new MyVar();
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      
	      check =n.f2.accept(this, argu);
	      if (((MyVar)check).Type != "boolean")
	      {
	    	  MyCollector.Errs.addElement("Line "+n.f0.beginLine+ ": Not a Boolean in while()");
	      }
	      
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      return _ret;
	   }

	   /**  
	    * f0 -> "System.out.println"
	    * f1 -> "("
	    * f2 -> Expression()
	    * f3 -> ")"
	    * f4 -> ";"
	    */
	   public MType visit(PrintStatement n, MType argu) {
	      MType _ret=null;
	      MType check = new MyVar();
	       
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      check = n.f2.accept(this, argu);
	      if (((MyVar)check).Type != "int")
	      {
	    	  MyCollector.Errs.addElement("Line "+n.f0.beginLine+": need to output a Int!");
	      }
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> AndExpression()
	    *       | CompareExpression()
	    *       | PlusExpression()
	    *       | MinusExpression()
	    *       | TimesExpression()
	    *       | ArrayLookup()
	    *       | ArrayLength()
	    *       | MessageSend()
	    *       | PrimaryExpression()
	    */
	   public MType visit(Expression n, MType argu) {
		  MType _ret=new MyVar();
	      _ret = n.f0.accept(this, argu);
	      return _ret;
	   }

	   /** 
	    * f0 -> PrimaryExpression()
	    * f1 -> "&&"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(AndExpression n, MType argu) {
		   MType _ret=new MyVar();
		   ((MyVar)_ret).Type = "boolean";
		   _ret.DefPos=n.f1.beginLine;
	       MType check = new MyVar();
		   
	      check = n.f0.accept(this, argu);
	      if (((MyVar)check).Type !="boolean")
	      {
	    	  MyCollector.Errs.addElement("Line "+_ret.DefPos+": First Number not Boolean");
	      }
	      n.f1.accept(this, argu);
	      check = n.f2.accept(this, argu);
	      if (((MyVar)check).Type != "boolean")
	      {
	    	  MyCollector.Errs.addElement("Line "+_ret.DefPos+": Second Number not Boolean");
	      }
	      
	      return _ret;
	   }

	   /** 
	    * f0 -> PrimaryExpression()
	    * f1 -> "<"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(CompareExpression n, MType argu) {
		   MType _ret=new MyVar();
		   ((MyVar)_ret).Type = "boolean";
		   _ret.DefPos=n.f1.beginLine;
	       MType check = new MyVar();
		   
	      check = n.f0.accept(this, argu);
	      if (((MyVar)check).Type !="int")
	      {
	    	  MyCollector.Errs.addElement("Line "+_ret.DefPos+": First Number not Int");
	      }
	      n.f1.accept(this, argu);
	      check = n.f2.accept(this, argu);
	      if (((MyVar)check).Type != "int")
	      {
	    	  MyCollector.Errs.addElement("Line "+_ret.DefPos+": Second Number not Int");
	      }
	      
	      return _ret;
	   }

	   /** 
	    * f0 -> PrimaryExpression()
	    * f1 -> "+"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(PlusExpression n, MType argu) {
		   MType _ret=new MyVar();
		   ((MyVar)_ret).Type = "int";
		   _ret.DefPos=n.f1.beginLine;
	       MType check = new MyVar();
		   
	      check = n.f0.accept(this, argu);
	      if (((MyVar)check).Type !="int")
	      {
	    	  MyCollector.Errs.addElement("Line "+_ret.DefPos+": First Number not Int");
	      }
	      n.f1.accept(this, argu);
	      check = n.f2.accept(this, argu);
	      if (((MyVar)check).Type != "int")
	      {
	    	  MyCollector.Errs.addElement("Line "+_ret.DefPos+": Second Number not Int");
	      }
	      
	      return _ret;
	   }

	   /** 
	    * f0 -> PrimaryExpression()
	    * f1 -> "-"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(MinusExpression n, MType argu) {
		   MType _ret=new MyVar();
		   ((MyVar)_ret).Type = "int";
		   _ret.DefPos=n.f1.beginLine;
	       MType check = new MyVar();
		   
	      check = n.f0.accept(this, argu);
	      if (((MyVar)check).Type !="int")
	      {
	    	  MyCollector.Errs.addElement("Line "+_ret.DefPos+": First Number not Int");
	      }
	      n.f1.accept(this, argu);
	      check = n.f2.accept(this, argu);
	      if (((MyVar)check).Type != "int")
	      {
	    	  MyCollector.Errs.addElement("Line "+_ret.DefPos+": Second Number not Int");
	      }
	      
	      return _ret;
	   }

	   /**  
	    * f0 -> PrimaryExpression()
	    * f1 -> "*"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(TimesExpression n, MType argu) {
		   MType _ret=new MyVar();
		   ((MyVar)_ret).Type = "int";
		   _ret.DefPos=n.f1.beginLine;
	       MType check = new MyVar();
		   
	      check = n.f0.accept(this, argu);
	      if (((MyVar)check).Type !="int")
	      {
	    	  MyCollector.Errs.addElement("Line "+_ret.DefPos+": First Number not Int");
	      }
	      n.f1.accept(this, argu);
	      check = n.f2.accept(this, argu);
	      if (((MyVar)check).Type != "int")
	      {
	    	  MyCollector.Errs.addElement("Line "+_ret.DefPos+": Second Number not Int");
	      }
	      
	      return _ret;
	   }

	   /**  已处理
	    * f0 -> PrimaryExpression()
	    * f1 -> "["
	    * f2 -> PrimaryExpression()
	    * f3 -> "]"
	    */
	   public MType visit(ArrayLookup n, MType argu) {
		   MType _ret=new MyVar();
		   _ret.DefPos =n.f1.beginLine;
		   ((MyVar)_ret).Type = "int";
	       MType check = new MyVar();
	       
		   
	      check = n.f0.accept(this, argu);
	      if (((MyVar)check).Type !="Array")
	      {
	    	  MyCollector.Errs.addElement("Line "+_ret.DefPos+": Name is not a Array");
	      }
	      n.f1.accept(this, argu);
	      check = n.f2.accept(this, argu);
	      if (((MyVar)check).Type != "int")
	      {
	    	  MyCollector.Errs.addElement("Line "+_ret.DefPos+": Index is not a Int");
	      }
	      _ret.DefPos = n.f1.beginLine;
	      n.f3.accept(this, argu);
	      return _ret;
	   }

	   /** 已处理
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> "length"
	    */
	   public MType visit(ArrayLength n, MType argu) {
		   MType _ret=new MyVar();
		   ((MyVar)_ret).Type = "int";
		   _ret.DefPos = n.f1.beginLine;
		   MyVar check =new MyVar();
		   
	      check=(MyVar)n.f0.accept(this, argu);
	      if (check.Type != "Array")
	      {
	    	  MyCollector.Errs.addElement("Line "+_ret.DefPos+": Not a Array!");
	      }
	      
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }
//****************************************************************************
	   /** 这是调用函数
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> Identifier()
	    * f3 -> "("
	    * f4 -> ( ExpressionList() )?
	    * f5 -> ")"
	    */
	   public MType visit(MessageSend n, MType argu) {
	      MType _ret= new MyVar();
	      _ret.DefPos = n.f1.beginLine;
	      ((MyVar)_ret).Type = "Un Known";
	      
	     MyClass tempclass = MyCollector.SearchClass(((MyVar)n.f0.accept(this, argu)).Type);
	     if (tempclass == null)
	     {
	    	 MyCollector.Errs.addElement("Line "+n.f1.beginLine+": Unknown class or it is something like 'Int'");
	    	 return _ret;
	     }
	     
	     MyMeth tempmeth =new MyMeth();
	     
	      n.f1.accept(this, argu);
	      String meth_name=n.f2.accept(this, argu).Name;
	      
	      for (int i=0; i<tempclass.Mlist.size();i++)
	      {
	    	  if (meth_name == tempclass.Mlist.get(i).Name)
	    	  {
	    		  tempmeth = tempclass.Mlist.get(i);
	    	  }
	      }
	      if (tempmeth.Name == null)
	      {
	    	  MyCollector.Errs.addElement("Line " +n.f1.beginLine+": method not found");
	    	  return _ret;
	      }
	      
	      _ret = tempmeth.Return;
	      n.f3.accept(this, argu);
	      MyTempCollector ParaSaver= (MyTempCollector)n.f4.accept(this, argu);
	      if (ParaSaver!=null)
	      {
	    //	  System.out.println("There are "+test.Saver.size()+" Methods");
	    	  if (ParaSaver.Saver.size()!=tempmeth.Paralist.size())
	    	  {
	    		  MyCollector.Errs.addElement("Line "+n.f1.beginLine+": Para Number not match!");
	    		  return _ret;
	    	  }
	    	  for (int i=0;i<tempmeth.Paralist.size();i++)
	    	  {
	    		  if (MyCollector.Assiable( tempmeth.Paralist.get(i).Type,((MyVar)ParaSaver.Saver.get(i)).Type )==false)
	    		  {
		    		  MyCollector.Errs.addElement("Line "+n.f1.beginLine+": Para Type not match!");
		    		  return _ret;
	    		  }
	    	  }
	      }
	      else
	      {
	    	  if (tempmeth.Paralist.size()!=0)
	    	  {
	    		  MyCollector.Errs.addElement("Line "+n.f1.beginLine+": Para Number not match!");
	    		  return _ret;	    		  
	    	  }
	      }
	      n.f5.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> Expression()
	    * f1 -> ( ExpressionRest() )*
	    */
	   public MType visit(ExpressionList n, MType argu) {
		      MType _ret=null;
		      MyVar temp=null;
		      temp = (MyVar) n.f0.accept(this, argu);
	//*******************参数表**********************    
		      MType Temp = n.f1.accept(this,argu);
		      MyTempCollector test = (MyTempCollector)Temp;
		      if (test!=null)
		      {
	//	    	  System.err.println("There are "+( test.Saver.size()+1) +" Paras");  
		    	  test.Saver.insertElementAt(temp,0);
		      }
		      else
		      {
		    	//  System.err.println("There is 1 Paras");
		    	  test = new MyTempCollector();
		    	  test.Saver.addElement(temp);
		      }

		      _ret=test;

	      return _ret;
	   }

	   /**
	    * f0 -> ","
	    * f1 -> Expression()
	    */
	   public MType visit(ExpressionRest n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	     _ret = n.f1.accept(this, argu);
	      return _ret;
	   }

	   /**  已处理
	    * f0 -> IntegerLiteral()
	    *       | TrueLiteral()
	    *       | FalseLiteral()
	    *       | Identifier()
	    *       | ThisExpression()
	    *       | ArrayAllocationExpression()
	    *       | AllocationExpression()
	    *       | NotExpression()
	    *       | BracketExpression()
	    */
	   public MType visit(PrimaryExpression n, MType argu) {
	     MType _ret= new MyVar();
	     MyVar temp= new MyVar();
	     
	     _ret = n.f0.accept(this, argu);
	     
	     if ( ((MyVar)_ret).Type=="Un Known")
	     {
	    	 MyCollector.Errs.addElement("Line "+_ret.DefPos+": Unknown type, maybe not created successfully……");
	     }
	     else
	     {
	    	 if ( ((MyVar)_ret).Type=="ID")
	    	 {
	    		 temp= MyCollector.SearchVar((MyMeth)argu,((MyVar)_ret).Name);
	    		 if (temp!=null)
	    		 {
	    			 _ret =temp;
	    		 }
	    		 else
	    		 {
	    			 ((MyVar)_ret).Type = "Un Known";
	    			 MyCollector.Errs.addElement("Line "+ _ret.DefPos+": "+_ret.Name+" Not defined!");
	    		 }
	    	 }
	     }
	      return _ret;
	   }

	   /** 已处理
	    * f0 -> <INTEGER_LITERAL>
	    */
	   public MType visit(IntegerLiteral n, MType argu) {
		  MType _ret=new MyVar();
		  ((MyVar)_ret).Type = "int";
		  _ret.DefPos=n.f0.beginLine;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /** 已处理
	    * f0 -> "true"
	    */
	   public MType visit(TrueLiteral n, MType argu) {
	      MType _ret=new MyVar();
		  _ret.DefPos=n.f0.beginLine;
	      ((MyVar)_ret).Type = "boolean";
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /** 已处理
	    * f0 -> "false"
	    */
	   public MType visit(FalseLiteral n, MType argu) {
	      MType _ret=new MyVar();
	      _ret.DefPos =n.f0.beginLine;
	      ((MyVar)_ret).Type = "boolean";
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /** 已处理
	    * f0 -> <IDENTIFIER>
	    */
	   public MType visit(Identifier n, MType argu) {
		 //返回一个MType值
		   MType _ret = new MIdentifier(n.f0.toString());
		   _ret.DefPos=n.f0.beginLine;
		   ((MIdentifier)_ret).Type = "ID";
		  // System.err.println(_ret.Name);
	      return _ret;
	   }

	   /** 未处理值
	    * f0 -> "this"
	    */
	   public MType visit(ThisExpression n, MType argu) {
		   
		  MType _ret=new MyVar();
		  _ret.DefPos=n.f0.beginLine;
		  ((MyVar)_ret).Type = ((MyMeth)argu).Belong;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /** 未处理值
	    * f0 -> "new"
	    * f1 -> "int"
	    * f2 -> "["
	    * f3 -> Expression()
	    * f4 -> "]"
	    */
	   public MType visit(ArrayAllocationExpression n, MType argu) {
	      MType _ret=new MyVar();
	      MyVar temp=new MyVar();
	      ((MyVar)_ret).Type = "Array";
	      _ret.DefPos = n.f0.beginLine;
	    		  
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      temp = (MyVar)n.f3.accept(this, argu);
	      if (temp.Type != "int")
	      {
	    	  MyCollector.Errs.addElement("Line "+n.f0.beginLine+ ": the Length is not a int!");
	      }
	      n.f4.accept(this, argu);
	      return _ret;
	   }

	   /**  未处理值
	    * f0 -> "new"
	    * f1 -> Identifier()
	    * f2 -> "("
	    * f3 -> ")"
	    */
	   public MType visit(AllocationExpression n, MType argu) {
	      MType _ret=new MyVar();
	      _ret.DefPos=n.f0.beginLine;
	      String tempname=null;
	      n.f0.accept(this, argu);
	      tempname = ((MyVar)n.f1.accept(this, argu)).Name;
	      if(MyCollector.SearchClass(tempname)==null)
	      {
	    	  ((MyVar)_ret).Type="Un Known";
	    	  MyCollector.Errs.addElement("Line "+n.f1.f0.beginLine+": "+"Bad new stament! No this class!");
	      }
	      else
	      {
	    	  ((MyVar)_ret).Type=tempname;
	      }
	      
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      return _ret;
	   }

	   /** 未处理值
	    * f0 -> "!"
	    * f1 -> Expression()
	    */
	   public MType visit(NotExpression n, MType argu) {
	      MType _ret=new MyVar();
	      _ret.DefPos =n.f0.beginLine;
	      
	     n.f0.accept(this, argu);
	     
	      MyVar check=(MyVar)n.f1.accept(this, argu);
	      if (check.Type != "boolean")
	      {
	    	  MyCollector.Errs.addElement("Line "+n.f0.beginLine+ ": Not a boolean!");
	      }
	      ((MyVar)_ret).Type ="boolean";
	      return _ret;
	   }

	   /**  传递，已做  
	    * f0 -> "("
	    * f1 -> Expression()
	    * f2 -> ")"
	    */
	   public MType visit(BracketExpression n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);	       
	      
	      _ret=n.f1.accept(this, argu);	    
	      
	      n.f2.accept(this, argu);
	      return _ret;
	   }
	

}
