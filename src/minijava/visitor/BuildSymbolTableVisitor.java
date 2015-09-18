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


public class BuildSymbolTableVisitor extends GJDepthFirst<MType,MType>  {
	//
	   // Auto class visitors--probably don't need to be overridden.
	   //
	public static int number=21;//函数中的临时变量从TEMP 20开始
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
	    	  		if (set.AddOne(temp)==true)
	    	  		{
	    	  			_count++;
	    	  		}
	    	  		else
	    	  		{	    	  			
	    	  			
	    	  		}
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
	      MType _ret=null;
	      MyMeth Print = new MyMeth();
	      n.f0.accept(this, argu);
	      
	     String class_name=n.f1.accept(this, argu).GetName();
	     Print.Belong = class_name;
	     Print.Return.Type ="void";
	     
	      MyClass myclass = new MyClass(class_name);
	      myclass.DefPos=n.f0.beginLine;
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
	      n.f14.accept(this, argu);
	      n.f15.accept(this, argu);
	      n.f16.accept(this, argu);
	      
	      myclass.Mlist.addElement(Print);
	      
	      MyCollector.mainclass = myclass;
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
	      MyClass myclass = new MyClass(class_name);
	      myclass.DefPos=n.f1.f0.beginLine;

	      
	      n.f2.accept(this, argu);
	      
//*********************************����**************	      
	      MType Temp = n.f3.accept(this,argu);
	      MyTempCollector test = (MyTempCollector)Temp;
	      if (test!=null)
	      {
	    //	  System.out.println("There are "+test.Saver.size()+" Vars");
	    	  myclass.Vlist =(Vector)test.Saver.clone();	  
	      }
	      else
	      {
	    	//  System.out.println("There is no Var");
	      }
//************************************************

//*****************�����************************
	      Temp = n.f4.accept(this,myclass);
	      test = (MyTempCollector)Temp;
	      if (test!=null)
	      {
	    //	  System.out.println("There are "+test.Saver.size()+" Methods");
	    	  myclass.Mlist =(Vector)test.Saver.clone();
	      }
	      else
	      {
	    //	  System.out.println("There is no Meth");
	      }
//****************************************************
	      n.f5.accept(this,argu);
		  MyCollector.addClass(myclass);
	//	  System.out.println("********************");
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
		 //********************************

		 //*********************************  
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      
	      String class_name="";
	      String father="";
	      
	      class_name = (n.f1.accept(this, argu)).GetName();
	      MyClass myclass = new MyClass(class_name);
	      myclass.DefPos=n.f1.f0.beginLine;
		//  System.out.println("Class "+MyCollector.classes.size()+ " is: "+myclass.Name);
	      
	      n.f2.accept(this, argu);
	     father= (n.f3.accept(this, argu)).GetName();
	     myclass.Father=father;
	     
	      n.f4.accept(this, argu);
	      
	    //*********************************����**************	      
	      MType Temp = n.f5.accept(this,argu);
	      MyTempCollector test = (MyTempCollector)Temp;
	      if (test!=null)
	      {
	    //	  System.out.println("There are "+test.Saver.size()+" Vars");
	    	  myclass.Vlist =(Vector)test.Saver.clone();	  
	      }
	      else
	      {
	    //	  System.out.println("There is no Var");
	      }
	      //***********************************************
	      
	    //*****************�����************************
	      Temp = n.f6.accept(this,myclass);
	      test = (MyTempCollector)Temp;
	      if (test!=null)
	      {
	    //	  System.out.println("There are "+test.Saver.size()+" Methods");
	    	  myclass.Mlist =(Vector)test.Saver.clone();	
	      }
	      else
	      {
	    //	  System.out.println("There is no Meth");
	      }
//****************************************************
	      n.f7.accept(this, argu);
	      
	      MyCollector.addClass(myclass);
		//  System.out.println("******************");
	      return _ret;
	   }

	   /**
	    * f0 -> Type()
	    * f1 -> Identifier()
	    * f2 -> ";"
	    */

	   
	   public MType visit(VarDeclaration n, MType argu) {
		   
		   String Type="";
		   String Name="";
	       Type = (n.f0.accept(this, argu)).GetName();
		   Name = (n.f1.accept(this, argu)).GetName();
		//   System.out.println("Define a Var: "+Type +" "+Name);
		   n.f2.accept(this, argu);
	      
		   MType _ret =new MyVar(Name,Type,number++);
		   _ret.DefPos=n.f1.f0.beginLine;
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
	      MyMeth mymeth = new MyMeth();	
	      int i=0;
	      int j=0;
	      mymeth.Belong = ((MyClass)argu).Name;
	      
	      n.f0.accept(this, argu);
	      
	      MyVar retval = new MyVar();
	      s=((MyType)n.f1.accept(this, argu)).GetName();
	      mymeth.DefPos=n.f2.f0.beginLine;
	      retval.Type = s;
	      retval.DefPos = n.f0.beginLine;
	      
	      s = (n.f2.accept(this, argu)).GetName();
	      
	      n.f3.accept(this, argu);
//*************************�����****************
	      MType Temp = n.f4.accept(this,argu);
	      MyTempCollector test = (MyTempCollector)Temp;
	      if (test!=null)
	      {
	    //	  System.out.println("There are "+test.Saver.size()+" Paras");
	    	  mymeth.Paralist =(Vector)test.Saver.clone();	  
	      }
	      else
	      {
	    //	  System.out.println("There is no Var");
	      }
//*********************************************	      
	      n.f5.accept(this, argu);
	      n.f6.accept(this, argu);
	      
//*****************************����**************************
	      Temp = n.f7.accept(this,argu);
	      test = (MyTempCollector)Temp;
	      if (test!=null)
	      {
	    //	  System.out.println("There are "+test.Saver.size()+" Vars");
	    	  mymeth.Vlist =(Vector)test.Saver.clone();	  
	      }
	      else
	      {
	   // 	  System.out.println("There is no Var");
	      }
//**********************************************	      
	      n.f8.accept(this, mymeth);
	      n.f9.accept(this, argu);
	      n.f10.accept(this, argu);
	      n.f11.accept(this, argu);
	      n.f12.accept(this, argu);
	            
	      mymeth.Name=s;
	      mymeth.Return = retval;
	     // System.err.println(retval.Type);
	      _ret =mymeth;
	      

	      for (i=0;i<mymeth.Paralist.size();i++)
	      {
	    	  for (j=0;j<mymeth.Vlist.size();j++)
	    	  {
	    		  if (mymeth.Paralist.get(i).Name == mymeth.Vlist.get(j).Name)
	    		  {
	    			  MyCollector.Errs.addElement("Line "+mymeth.Vlist.get(j).DefPos +": The Var "+mymeth.Vlist.get(j).Name+" has the same Name with one Para");
	    		  }
	    	  }
	      }
	  //    System.out.println("A new method, it is "+((MyMeth)_ret).Name +" and it's return type is " +((MyMeth)_ret).Return.Type);
	//	  System.out.println("and it has "+mymeth.Vlist.size()+" Vars");

	      return _ret;
	   }

	   /**
	    * f0 -> FormalParameter()
	    * f1 -> ( FormalParameterRest() )*
	    */
	   public MType visit(FormalParameterList n, MType argu) {
	      MType _ret=null;
	      MyVar temp=null;
	      temp = (MyVar) n.f0.accept(this, argu);
	      temp.DefPos =n.f0.f1.f0.beginLine;
	   //   System.out.println("The first Para is "+ temp.Name);
//*******************�����**********************    
	      MType Temp = n.f1.accept(this,argu);
	      MyTempCollector test = (MyTempCollector)Temp;
	      if (test!=null)
	      {
	    	//  System.out.println("There are "+( test.Saver.size()+1) +" Paras");  
	    	  test.AddOneAt(temp,0);
	      }
	      else
	      {
	    	//  System.out.println("There is 1 Paras");
	    	  test = new MyTempCollector();
	    	  test.AddOne(temp);
	      }
	  //    for (int i=0;i<test.Saver.size();i++)
	   //   {
	   // 	  System.out.println( ((MyVar)test.Saver.elementAt(i)).Name);
	   //   }
//*********************************
	      
	     // Vector
	     // n.f1.accept(this, argu);
	      _ret=test;
	      return _ret;
	   }

	   /**
	    * f0 -> Type()
	    * f1 -> Identifier()
	    */
	   public MType visit(FormalParameter n, MType argu) {
		   String Type;
		   String Name;
	       Type = (n.f0.accept(this, argu)).GetName();
		   Name = (n.f1.accept(this, argu)).GetName();
	//	   System.out.println("Define a Para: "+Type +" "+Name);
	      MType _ret=new MyVar(Name,Type,number++);
	      _ret.DefPos= n.f1.f0.beginLine;
	      return _ret;
	   }

	   /**
	    * f0 -> ","
	    * f1 -> FormalParameter()
	    */
	   public MType visit(FormalParameterRest n, MType argu) {
	      MType _ret=null;
	      
	      n.f0.accept(this, argu);
	      _ret =n.f1.accept(this, argu);
	  //    System.out.println("The Para is:"+((MyVar)_ret).Name);
	      return _ret;
	   }

	   /**
	    * f0 -> ArrayType()
	    *       | BooleanType()
	    *       | IntegerType()
	    *       | Identifier()
	    */
	   public MType visit(Type n, MType argu) {
		  
		  String Type=(n.f0.accept(this,argu)).GetName();
	      MType _ret=new MyType(Type);
	      //n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "int"
	    * f1 -> "["
	    * f2 -> "]"
	    */
	   public MType visit(ArrayType n, MType argu) {
	      MType _ret=new MyType("Array");
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "boolean"
	    */
	   public MType visit(BooleanType n, MType argu) {
	      MType _ret=new MyType("boolean");
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "int"
	    */
	   public MType visit(IntegerType n, MType argu) {
	      MType _ret=new MyType("int");
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

	   /** 
	    * f0 -> Identifier()
	    * f1 -> "="
	    * f2 -> Expression()
	    * f3 -> ";"
	    */
	   public MType visit(AssignmentStatement n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      return _ret;
	   }

	   /** 
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
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      n.f5.accept(this, argu);
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
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
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
	      
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);	      
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
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
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
		  MType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /** 
	    * f0 -> PrimaryExpression()
	    * f1 -> "&&"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(AndExpression n, MType argu) {
		   MType _ret=null;
		      n.f0.accept(this, argu);	      
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);     
		      return _ret;
	   }

	   /** 
	    * f0 -> PrimaryExpression()
	    * f1 -> "<"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(CompareExpression n, MType argu) {
		   MType _ret=null;
		      n.f0.accept(this, argu);	      
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);     
		      return _ret;
	   }

	   /** 
	    * f0 -> PrimaryExpression()
	    * f1 -> "+"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(PlusExpression n, MType argu) {
		   MType _ret=null;
		      n.f0.accept(this, argu);	      
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);     
		      return _ret;
	   }

	   /** 
	    * f0 -> PrimaryExpression()
	    * f1 -> "-"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(MinusExpression n, MType argu) {
		   MType _ret=null;
		      n.f0.accept(this, argu);	      
		      n.f1.accept(this, argu);
		      n.f2.accept(this, argu);     
		      return _ret;
	   }

	   /**  
	    * f0 -> PrimaryExpression()
	    * f1 -> "*"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(TimesExpression n, MType argu) {
		   MType _ret=null;
	      n.f0.accept(this, argu);	      
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);     
	      return _ret;
	   }

	   /**  
	    * f0 -> PrimaryExpression()
	    * f1 -> "["
	    * f2 -> PrimaryExpression()
	    * f3 -> "]"
	    */
	   public MType visit(ArrayLookup n, MType argu) {
		   MType _ret=null;
	      
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);	      
	      n.f3.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> "length"
	    */
	   public MType visit(ArrayLength n, MType argu) {
		   MType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      return _ret;
	   }

	   /** 
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> Identifier()
	    * f3 -> "("
	    * f4 -> ( ExpressionList() )?
	    * f5 -> ")"
	    */
	   public MType visit(MessageSend n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      n.f5.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> Expression()
	    * f1 -> ( ExpressionRest() )*
	    */
	   public MType visit(ExpressionList n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> ","
	    * f1 -> Expression()
	    */
	   public MType visit(ExpressionRest n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      return _ret;
	   }

	   /**
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
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> <INTEGER_LITERAL>
	    */
	   public MType visit(IntegerLiteral n, MType argu) {
		  MType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "true"
	    */
	   public MType visit(TrueLiteral n, MType argu) {
		  MType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "false"
	    */
	   public MType visit(FalseLiteral n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> <IDENTIFIER>
	    */
	   public MType visit(Identifier n, MType argu) {
		 //����һ��MTypeֵ
		   MType _ret = new MIdentifier(n.f0.toString());
		   ((MIdentifier)_ret).Type = "ID";
	      return _ret;
	   }

	   /** 
	    * f0 -> "this"
	    */
	   public MType visit(ThisExpression n, MType argu) {
		  MType _ret=null;
	      n.f0.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "new"
	    * f1 -> "int"
	    * f2 -> "["
	    * f3 -> Expression()
	    * f4 -> "]"
	    */
	   public MType visit(ArrayAllocationExpression n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      n.f4.accept(this, argu);
	      return _ret;
	   }

	   /**
	    * f0 -> "new"
	    * f1 -> Identifier()
	    * f2 -> "("
	    * f3 -> ")"
	    */
	   public MType visit(AllocationExpression n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      n.f2.accept(this, argu);
	      n.f3.accept(this, argu);
	      return _ret;
	   }

	   /** 
	    * f0 -> "!"
	    * f1 -> Expression()
	    */
	   public MType visit(NotExpression n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);
	      n.f1.accept(this, argu);
	      return _ret;
	   }

	   /**  
	    * f0 -> "("
	    * f1 -> Expression()
	    * f2 -> ")"
	    */
	   public MType visit(BracketExpression n, MType argu) {
	      MType _ret=null;
	      n.f0.accept(this, argu);	      
	      n.f1.accept(this, argu);	    
	      n.f2.accept(this, argu);
	      return _ret;
	   }
	

}
