package topiglet;
import java.util.Enumeration;
import topiglet.Lable;
import topiglet.Put;
import topiglet.Temp;
import minijava.symboltable.MyClass;
import minijava.symboltable.MyCollector;
import minijava.symboltable.MyMeth;
import minijava.symboltable.MyVar;
import minijava.syntaxtree.*;
import minijava.visitor.GJDepthFirst;
public class pigletVisitor extends GJDepthFirst<String,Object> {
	public pigletVisitor(int _num){
		Temp.num = _num;
		Lable.num = 0;
	}


	/**
	 * f0 -> MainClass()
	 * f1 -> ( StringDeclaration() )*
	 * f2 -> <EOF>
	 */
	boolean IsParalist=false;
	int ParalistNum=0;
	public String visit(Goal n, Object argu) {
		Put.con("MAIN\n");
	    n.f0.accept(this, argu);
	    Put.con("\nEND\n");
	    n.f1.accept(this, argu);
	    return null;
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
	public String visit(MainClass n, Object argu) {
		 n.f14.accept(this, MyCollector.mainclass.Mlist.get(0));
		return null;
	}
	
	/**
	 * f0 -> "System.out.println"
	 * f1 -> "("
	 * f2 -> Expression()
	 * f3 -> ")"
	 * f4 -> ";"
	 */
	public String visit(PrintStatement n, Object argu) {
		Put.Tab=Put.Tab+" ";
	    Put.con(Put.Tab + "PRINT ");
	    n.f2.accept(this, argu);
	    Put.con("\n");
	    return null;
	}

	/**
	 * f0 -> "class"
	 * f1 -> Identifier()
	 * f2 -> "{"
	 * f3 -> ( VarDeclaration() )*
	 * f4 -> ( MethodDeclaration() )*
	 * f5 -> "}"
	 */
	
	public String visit(ClassDeclaration n, Object argu) {
		String currClassName = n.f1.f0.toString();
		MyClass currClass = MyCollector.SearchClass(currClassName);  
        n.f4.accept(this, currClass);
	    return null;
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
	public String visit(ClassExtendsDeclaration n, Object argu) {
        String currClassName= n.f1.f0.toString();
		MyClass currClass= MyCollector.SearchClass(currClassName);  
        n.f6.accept(this, currClass);
	    return null;		
	}
	/**
	 * f0 -> String()
	 * f1 -> Identifier()
	 * f2 -> ";"
	 */
	public String visit(VarDeclaration n, Object argu) {
	    return null;
	}	
	/**f0 -> "public"
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
	public String visit(MethodDeclaration n, Object argu) {
        String currClassName=((MyClass)argu).Name;
		String currMethodName = n.f2.f0.toString();
		MyMeth currMethod = MyCollector.SearchMeth(currClassName,currMethodName);	
		int i = currMethod.Paralist.size();
		if(i>=1)
		 Put.con("\n"+currClassName + "_" + currMethodName + " [ " + 2+ " ]" + "\n");
		else Put.con("\n"+currClassName + "_" + currMethodName + " [ " + 1+ " ]" + "\n");
	    Put.Tab += " ";
	    Put.con("\n" + Put.Tab + "BEGIN\n");
	    Put.Tab += " ";
		n.f8.accept(this, currMethod);
		Temp t = new Temp();
		Put.con(Put.Tab + "MOVE " + t + " ");
		n.f10.accept(this, currMethod);
		Put.con("\n" + Put.Tab + "RETURN " + t);
		Put.con("\n" + Put.Tab + "END\n");
		return null;
	 }
	/**
	 * f0 -> Identifier()
	 * f1 -> "="
	 * f2 -> Expression()
	 * f3 -> ";"
	 */
	public String visit(AssignmentStatement n, Object argu) {
		String identifierName = n.f0.f0.toString();
		MyMeth currMethod = (MyMeth)argu;
		String currClassName= currMethod.Belong;
		MyClass currClass = MyCollector.SearchClass(currClassName); 
		boolean flag=false;
		for (int i=0;i<currMethod.Vlist.size();i++)
	      if (currMethod.Vlist.get(i).Name ==identifierName)
		  {
			 int num = currMethod.Vlist.get(i).GetNumber();
			 Put.con(Put.Tab + "MOVE " + new Temp(num) + " ");
			 flag=true;
			 break;
		  }
        for (int i=0;i<currMethod.Paralist.size();i++)
  	      if (currMethod.Paralist.get(i).Name ==identifierName)
  		  {
  			 Put.con(Put.Tab + "HSTORE PLUS " + new Temp(1) + " TIMES 4 "+i+" 0 "); 
  			 flag=true;
  			 break;
  		  }
	     if(!flag)
		 {
             int j;
 			for(j=0;j<currClass.Vlist.size();j++)
               if(currClass.Vlist.get(j).Name==identifierName )
   			    break; 
	    	 Put.con(Put.Tab + "HSTORE " + new Temp(0) +" "+(j+1)*4+ " ");
		 }
		n.f2.accept(this, argu);
	    Put.con("\n");
	    return null;
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
	public String visit(ArrayAssignmentStatement n, Object argu) {
		String identifierName= n.f0.f0.toString();
		MyMeth currMethod = (MyMeth)argu;
		String currClassName = currMethod.Belong;
		MyClass currClass= MyCollector.SearchClass(currClassName);  
		Temp t = new Temp();
		Put.con(Put.Tab + "MOVE "+ t);
	    boolean flag1=false;
	    boolean flag2=false;
		for (int i=0;i<currMethod.Vlist.size();i++)
	       if (currMethod.Vlist.get(i).Name == identifierName)
			{
				int num = currMethod.Vlist.get(i).GetNumber();
				Put.con(" " + new Temp(num)+"\n");
				flag1=true;
				break;
			}
		for(int i=0;i<currMethod.Paralist.size();i++)
		   if(currMethod.Paralist.get(i).Name==identifierName )
		   {
			   Temp t3=new Temp();
			   Put.Tab += " ";
			   Put.con("\n");
			   Put.con( Put.Tab + " BEGIN\n");
			   Put.Tab += " ";
			   Put.con(Put.Tab + "HLOAD " + t3 + " " +"PLUS "+ new Temp(1) + " " +"TIMES 4 "+i+ " 0\n");
			   if(Put.Tab.length()>1)
				Put.Tab = Put.Tab.substring(1);
		       Put.con(Put.Tab + "RETURN " + t3 + "\n");
			   Put.con(Put.Tab + "END\n");
			   Put.con("\n");
			   flag2=true;
			   break;
			 }
		if(!flag1&&!flag2)
		{
             int j;
			for(j=0;j<currClass.Vlist.size();j++)
              if(currClass.Vlist.get(j).Name==identifierName )
  			    break;
			Temp t2 = new Temp();
			Put.Tab += " ";
			Put.con("\n");
			Put.con( Put.Tab + "BEGIN\n");
			Put.Tab += " ";
			Put.con(Put.Tab + "HLOAD " + t2 + " " + new Temp(0) + " " +(j+1)*4+ "\n");
			if(Put.Tab.length()>1)
			Put.Tab = Put.Tab.substring(1);
            Put.con(Put.Tab + "RETURN " + t2 + "\n");
			Put.con(Put.Tab + "END\n");
			Put.con("\n");
		}
		Lable l = new Lable( "NOERROR" );
		Put.con(Put.Tab + "CJUMP LT " + t + " 1 " + l + "\n");
		Put.con(Put.Tab + "ERROR\n");
		Put.con(Put.Tab + l + " NOOP\n");
		Temp t3 = new Temp();
		Put.con(Put.Tab + "MOVE " + t3 + " ");
		n.f2.accept(this, argu);
		Put.con("\n");
		Temp t4 = new Temp();
		Put.con(Put.Tab + "HLOAD " + t4 + " " + t + " 0\n");
		Lable l2 = new Lable("BEERROR");
		Put.con(Put.Tab + "CJUMP LT " + t3 + " " + t4 + " " + l2 + "\n");
		Lable l3 = new Lable("NOERROR");
		Put.con(Put.Tab + "JUMP " + l3 + "\n");
		Put.con(Put.Tab + l2 + " NOOP\n");
		Put.con(Put.Tab + "ERROR\n");//缁堟
		Put.con(Put.Tab + l3 + " NOOP\n");//l3澶�
		Put.con(Put.Tab + "MOVE " + t + " PLUS " + t + " TIMES " + "4" + " PLUS " + "1 " + t3 + "\n");//plus 1 +t3琛ㄧず鍚庣Щ涓�綅锛屽洜涓虹涓�綅瀛樻斁鐨勬槸鏁扮粍涓厓绱犵殑涓暟锛�*(1+t3)琛ㄧず鍦板潃锛屽皢鏈�柊鐨勫湴鍧�祴缁檛
	    Put.con(Put.Tab + "HSTORE " + t + " 0");//t+0,杩欓噷0琛ㄧず鍋忕Щ閲忥紝
		n.f5.accept(this, argu);//鎺ュ彈瑕佽祴鐨勫�
		Put.con("\n");
	    return null;
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
	public String visit(IfStatement n, Object argu) {
		Temp t = new Temp();
		Put.con(Put.Tab + "MOVE " + t + " ");
		n.f2.accept(this, argu);
		Lable l = new Lable("IF");
		Put.con("\n");
		Put.con(Put.Tab + "CJUMP " + t + " " + l + "\n");//濡傛灉t涓簍rue,鍒欐墽琛屼笅涓�潯鎸囦护锛屽惁鍒欒烦杞埌L澶勬墽琛�
		n.f4.accept(this, argu);//濡傛灉鐪熸墽琛�
		Lable l2 = new Lable("IF");
		Put.con("\n");
		Put.con(Put.Tab + "JUMP " + l2+"\n");//鐪熺殑璇彞鎵ц瀹屽悗瑕佽烦鍒版湯灏�
		Put.con(Put.Tab + l + " NOOP\n");//鑻ユ槸鍋囧垯鎵ц
	    n.f6.accept(this, argu);//鑻ユ槸鍋囧垯鎵ц
		Put.con("\n");
		Put.con(Put.Tab + l2 + " NOOP\n");
	    return null;
	}
	/**
	 * f0 -> "while"
	 * f1 -> "("
	 * f2 -> Expression()
	 * f3 -> ")"
	 * f4 -> Statement()
	 */
	public String visit(WhileStatement n, Object argu) {
		Lable l = new Lable("WHILE");
		Put.con(Put.Tab + l + " NOOP\n");
		Temp t = new Temp();
		Put.con(Put.Tab + "MOVE " + t + " ");
		n.f2.accept(this, argu);
		Lable l2 = new Lable("WHILE");
		Put.con("\n");
		Put.con(Put.Tab + "CJUMP " + t + " " + l2);
		Put.con("\n");
		if(Put.Tab.length()>1)
		Put.Tab=Put.Tab.substring(1);
		n.f4.accept(this, argu);
		Put.con("\n");
		Put.con(Put.Tab + "JUMP " + l + "\n");
		Put.con(Put.Tab + l2 + " NOOP\n");
	    return null;
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
	public String visit(Expression n, Object argu) {
	     return n.f0.accept(this, argu);
	}	
	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "&&"
	 * f2 -> PrimaryExpression()
	 */
	public String visit(AndExpression n, Object argu) {
		
		Put.con("\n" + Put.Tab + "BEGIN\n");
		Put.con(Put.Tab + "CJUMP");//鑻ョ涓�釜鍊间负鍋囧垯璺冲埌l澶�
		n.f0.accept(this, argu);//鎺ユ敹绗竴涓�
		Lable l = new Lable("AND");
		Put.con(Put.Tab + l + "\n");//鑻ョ涓�釜鍊间负鍋囧垯璺冲埌l澶�
		Put.con(Put.Tab + "CJUMP");//鑻ョ浜屼釜鍊间负鍋囧垯璺冲埌l澶�
		n.f2.accept(this, argu);//鎺ユ敹绗簩涓�
		Put.con(Put.Tab + l + "\n");//鑻ョ浜屼釜鍊间负鍋囧垯璺冲埌l澶�
		Temp t = new Temp();
		Put.con(Put.Tab + "MOVE " + t + " 1\n");
		Lable l2 = new Lable("AND");
		Put.con(Put.Tab + "JUMP " + l2 + "\n");
		Put.con(Put.Tab + l + " NOOP\n");
		Put.con(Put.Tab + "MOVE " + t + " 0\n");
		Put.con(Put.Tab + l2 + " NOOP\n");
		if(Put.Tab.length()>1)
		Put.Tab=Put.Tab.substring(1);
		Put.con(Put.Tab + "RETURN " + t +"\n");
		Put.con(Put.Tab + "END");
		Put.Tab+=" ";
	    return "boolean";
	}
	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "<"
	 * f2 -> PrimaryExpression()
	 */

	public String visit(CompareExpression n, Object argu) {
		Put.con(" LT ");
	    n.f0.accept(this, argu);
	    n.f2.accept(this, argu);
	    return "boolean";
	}
	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "+"
	 * f2 -> PrimaryExpression()
	 */
	public String visit(PlusExpression n, Object argu) {
		Put.con(" PLUS ");
	    n.f0.accept(this, argu);
	    n.f2.accept(this, argu);
	    return "int";
	}
	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "-"
	 * f2 -> PrimaryExpression()
	 */
	public String visit(MinusExpression n, Object argu) {
		Put.con(" MINUS ");
	    n.f0.accept(this, argu);
	    n.f2.accept(this, argu);
	    return "int";
	}
	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "*"
	 * f2 -> PrimaryExpression()
	 */
	public String visit(TimesExpression n, Object argu) {
		Put.con(" TIMES ");
	    n.f0.accept(this, argu);
	    n.f2.accept(this, argu);
	    return "int";
	}	
	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "["
	 * f2 -> PrimaryExpression()
	 * f3 -> "]"
	 */
	 public String visit(ArrayLookup n, Object argu)
	 {
 		 Put.con("\n" + Put.Tab + "BEGIN\n");
		 Temp t = new Temp();
	     Put.con(Put.Tab + "MOVE " + t );
	     n.f0.accept(this, argu);
         Lable l = new Lable("NOERROR");
         Put.con("\n");
		 Put.con(Put.Tab + "CJUMP LT " + t + " 1 " + l + "\n");//姣旇緝璇ユ暟缁勫厓绱犱釜鏁板拰1鐨勫ぇ灏忥紝t鏀剧殑鏄暟缁�
		 Put.con(Put.Tab + "ERROR\n");
		 Put.con(Put.Tab + l + " NOOP\n");
		 Temp t3 = new Temp();
		 Put.con(Put.Tab + "MOVE " + t3 + " ");//t3鏀剧殑鏄暟缁勫厓绱犵殑涓嬫爣
		 n.f2.accept(this, argu);
		 Put.con("\n");
		 Temp t4 = new Temp();
		 Put.con(Put.Tab + "HLOAD " + t4 + " " + t + " 0\n");//t4瀛樻斁鐨勬槸鍏冪礌涓暟
		 Lable l2 = new Lable("BEERROR");
		 Put.con(Put.Tab + "CJUMP LT " + t3 + " " + t4 + " " + l2 + "\n");//鑻3 >t4鍒欓敊璇�骞惰烦鍒發2澶�
		 Lable l3 = new Lable("NOERROR");//t3<t4鐨勬儏鍐典笅鎵ц
		 Put.con(Put.Tab + "JUMP " + l3 + "\n");//t3<t4璺冲埌l3澶�
		 Put.con(Put.Tab + l2 + " NOOP\n");//t3>t4
		 Put.con(Put.Tab + "ERROR\n");//t3>t4缁堟鎵ц
		 Put.con(Put.Tab + l3 + " NOOP\n");//t3<t4鎯呭喌涓嬫墽琛�
		 Put.con(Put.Tab + "MOVE " + t + " PLUS " + t + " TIMES " + "4" + " PLUS " + "1 " + t3 + "\n");
		 Put.con(Put.Tab + "HLOAD " + t + " " + t + " 0\n");
		 if(Put.Tab.length()>1)
		 Put.Tab=Put.Tab.substring(1);
		 Put.con(Put.Tab + "RETURN " + t + "\n");
		 Put.con(Put.Tab + "END");
		 return null;
	} 
	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> "length"
	    */
	   public String visit(ArrayLength n, Object argu) 
	   {
            Put.con("\n"+ Put.Tab + "BEGIN\n");
			Temp t = new Temp();
		    Put.con(Put.Tab + "MOVE " + t );//t瀛樻斁鏁扮粍鍏冪礌涓暟
		    n.f0.accept(this, argu);
		    Lable l = new Lable("NOERROR");
            Put.con("\n");
			Put.con(Put.Tab + "CJUMP LT " + t + " 1 " + l + "\n");//姣旇緝鏁扮粍鍏冪礌鐨勪釜鏁板拰1鐨勫ぇ灏忥紝濡傛灉澶т簬1璺冲埌l澶勬墽琛�
			Put.con(Put.Tab + "ERROR\n");//鏁扮粍鍏冪礌涓暟灏忎簬1
			Put.con(Put.Tab + l + " NOOP\n");
			Put.con(Put.Tab + "HLOAD " + t +" "+t +" 0\n");//灏嗘暟缁勫厓绱犱釜鏁版斁鍦╰閲�
			if(Put.Tab.length()>1)
			Put.Tab=Put.Tab.substring(1);
			Put.con(Put.Tab + "RETURN " + t+"\n");//杩斿洖鏁扮粍鍏冪礌涓暟
			Put.con(Put.Tab + "END\n");
			return "int";
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
	   public String visit(PrimaryExpression n, Object argu) 
	   {
            return  n.f0.accept(this, argu);
	   }
	   /**
	     * f0 -> "this"
	    */
	   public String visit(ThisExpression n, Object argu) 
	   {
		   Put.con(" TEMP 0\n");
		   return  ((MyMeth)argu).Belong;
	   }
	   
       /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> Identifier()
	    * f3 -> "("
	    * f4 -> ( ExpressionList() )?
	    * f5 -> ")"
	    */
	   public String visit(MessageSend n, Object argu)
	   {

        Put.con("\n"+Put.Tab+"CALL");
	    Put.Tab+=" ";
	    Put.con("\n" + Put.Tab + "BEGIN\n");
		Temp t = new Temp();
		Put.con(Put.Tab + "MOVE " + t);	    
		String currClassName= n.f0.accept(this,argu);
        int index = currClassName.indexOf(".");
		if(index<0){}
		else
		{currClassName = currClassName.substring(0,index );}
        MyClass currClass=null;
		for (int i=0;i<MyCollector.classes.size();i++)
	     if(MyCollector.classes.get(i).Name.equals(currClassName))
		 {
			currClass =MyCollector.classes.get(i); //找到该函数所在的类 
			break;
		 }	    
	    Put.con("\n");
        Temp t2 = new Temp();
        Put.Tab+=" ";
	    Put.con(Put.Tab + "HLOAD " + t2 +" " +  t +" 0\n");
	    String methodName = n.f2.f0.toString();//函数名
	    Temp t3 = new Temp();
	    MyMeth currMethod=null;
	    for(int j=0;j<currClass.Mlist.size();j++)
	     if(currClass.Mlist.get(j).Name==methodName) 
	     {
	    	Put.con(Put.Tab + "HLOAD " + t3 +" " + t2 +" " +j*4+ "\n");//找到该函数在该类中的偏移地址
	    	currMethod =currClass.Mlist.get(j);//该函数
	    	break;
	     }
		Put.Tab=Put.Tab.substring(1);
        Put.con(Put.Tab + "RETURN " + t3 + "\n");
	    Put.con(Put.Tab + "END( "+ t+"\n" );//里面放各种参数，t即为函数的二次地址
	    Put.con(Put.Tab+"BEGIN\n");
	    Temp t4 = new Temp();
	    Put.con("MOVE "+t4+" HALLOCATE TIMES 4 "+currMethod.Paralist.size());
	    Put.con("\n");
	    IsParalist=true;
	    ParalistNum=0;
	   	ExpressionList e1 = (ExpressionList)n.f4.node;
	   	if(e1!=null)
	   	{
	   		Put.con(Put.Tab+"HSTORE PLUS "+t4+" TIMES 4 "+ParalistNum+" 0  ");
	   		e1.f0.accept(this,argu);
	   		Put.con("\n");
	   		ParalistNum++;
	       Enumeration<Node> e = ((ExpressionList)n.f4.node).f1.elements();
		   while(e.hasMoreElements()) 
		   {
			   Put.con(Put.Tab+"HSTORE PLUS "+t4+" TIMES 4 "+ParalistNum+" 0  ");
			   e.nextElement().accept(this,argu);
			   Put.con("\n");
		      ParalistNum++;
		   }
	   	}
	   	IsParalist=false;
	   	ParalistNum=0;
	   	Put.con("RETURN "+t4+"\n");
		 Put.con("END)\n");
	     return currMethod.Return.Type;
	  }	
	  /* public String visit(MessageSend n, Object argu)
	   {

        Put.con("\n"+Put.Tab+"CALL");
	    Put.Tab+=" ";
	    Put.con("\n" + Put.Tab + "BEGIN\n");
		Temp t = new Temp();
		Put.con(Put.Tab + "MOVE " + t);	    
		String currClassName= n.f0.accept(this,argu);
        int index = currClassName.indexOf(".");
		if(index<0){}
		else
		{currClassName = currClassName.substring(0,index );}
        MyClass currClass=null;
		for (int i=0;i<MyCollector.classes.size();i++)
	     if(MyCollector.classes.get(i).Name.equals(currClassName))
		 {
			currClass =MyCollector.classes.get(i); //鎵惧埌璇ュ嚱鏁版墍鍦ㄧ殑绫�
			break;
		 }	    
	    Put.con("\n");
        Temp t2 = new Temp();
        Put.Tab+=" ";
	    Put.con(Put.Tab + "HLOAD " + t2 +" " +  t +" 0\n");
	    String methodName = n.f2.f0.toString();//鍑芥暟鍚�
	    Temp t3 = new Temp();
	    MyMeth currMethod=null;
	    for(int j=0;j<currClass.Mlist.size();j++)
	     if(currClass.Mlist.get(j).Name==methodName) 
	     {
	    	Put.con(Put.Tab + "HLOAD " + t3 +" " + t2 +" " +j*4+ "\n");//鎵惧埌璇ュ嚱鏁板湪璇ョ被涓殑鍋忕Щ鍦板潃
	    	currMethod =currClass.Mlist.get(j);//璇ュ嚱鏁�
	    	break;
	     }
		Put.Tab=Put.Tab.substring(1);
        Put.con(Put.Tab + "RETURN " + t3 + "\n");
	    Put.con(Put.Tab + "END( "+ t+"\n" );//閲岄潰鏀惧悇绉嶅弬鏁帮紝t鍗充负鍑芥暟鐨勪簩娆″湴鍧�
	    Put.con(Put.Tab+"BEGIN\n");
	    Put.con("MOVE TEMP 20 HALLOCATE TIMES 4 "+currMethod.Paralist.size());
	    Put.con("\n");
	    IsParalist=true;
	    ParalistNum=0;
	   	ExpressionList e1 = (ExpressionList)n.f4.node;
	   	if(e1!=null)
	   	{
	   		Put.con(Put.Tab+"HSTORE PLUS TEMP 20 TIMES 4 "+ParalistNum+" 0  ");
	   		e1.f0.accept(this,argu);
	   		Put.con("\n");
	   		ParalistNum++;
	       Enumeration<Node> e = ((ExpressionList)n.f4.node).f1.elements();
		   while(e.hasMoreElements()) 
		   {
			   Put.con(Put.Tab+"HSTORE PLUS TEMP 20 TIMES 4 "+ParalistNum+" 0  ");
			   e.nextElement().accept(this,argu);
			   Put.con("\n");
		      ParalistNum++;
		   }
	   	}
	   	IsParalist=false;
	   	ParalistNum=0;
	   	Put.con("RETURN TEMP 20\n");
		 Put.con("END)\n");
	     return currMethod.Return.Type;
	  }	*/
	   /**
	    * f0 -> Expression()
	    * f1 -> ( ExpressionRest() )*
	    */
	   public String visit(ExpressionList n, Object argu)
	   {
	       n.f0.accept(this, argu);
	      Put.con(" ");
	      n.f1.accept(this, argu);
	      return null;
	   }

	   /**
	    * f0 -> ","
	    * f1 -> Expression()
	    */
	   public String visit(ExpressionRest n, Object argu) {
	      String cName = n.f1.accept(this, argu);
	      Put.con(" ");
	      return cName;
	   }
	   /**
	    * f0 -> <INTEGER_LITERAL>
	    */
	   public String visit(IntegerLiteral n, Object argu)
	   {
          Put.con(" " + n.f0.toString() + " ");
	      return "int";
	   }
	   /**
	    * f0 -> "true"
	    */
	   public String visit(TrueLiteral n, Object argu)
	   {
		   Put.con(" 1 ");
		   return "boolean";
	   }
	   /**
	    * f0 -> "false"
	    */
	   public String visit(FalseLiteral n, Object argu)
	   {
		   Put.con(" 0 ");
		   return "boolean";
	   }	
	   /**
	    * f0 -> "new"
	    * f1 -> "int"
	    * f2 -> "["
	    * f3 -> Expression()
	    * f4 -> "]"
	    */
	   public String visit(ArrayAllocationExpression n, Object argu)//鐢虫槑鏂版暟缁�
	   {
		   Temp t = new Temp();
		   Put.con("\n" + Put.Tab + "BEGIN\n");
		   Put.con(Put.Tab + "MOVE " + t );//t瀛樻斁鏁扮粍
		   n.f3.accept(this,argu);
		   Put.con("\n");
		   Temp t1 = new Temp();
		   Put.con(Put.Tab + "MOVE " + t1 +" HALLOCATE TIMES 4 PLUS " + t + " 1\n");//缁欐暟缁勫垎閰嶇┖闂�
		   Put.con(Put.Tab + "HSTORE " + t1 + " 0 " + t + "\n");//t1 0閲屽瓨鏀炬暟缁勫厓绱犵殑涓暟
		   Temp t2 = new Temp();
		   Put.con(Put.Tab + "MOVE " + t2 + " 0\n");//t2瀛樻斁0锛岀敤鏉ヨ〃绀鸿鏁扮殑鍒濆�
		   Lable l = new Lable("NOERROR");
		   Lable l1 = new Lable("NOERROR");
		   Put.con(Put.Tab + l + " NOOP\n");
		   Put.con(Put.Tab + "CJUMP LT "+ t2 + " " + t + " " + l1 + "\n");//姣旇緝t2鍜宼澶у皬锛屽惊鐜鏁拌灏忎簬鏁扮粍鍏冪礌涓暟锛岃繖閲屽惊鐜槸涓轰簡缁欐暟缁勫叏閮ㄥ垵濮嬪寲涓�
		   Temp t3 = new Temp();
		   Put.con(Put.Tab + "MOVE " +  t3 + " PLUS " + t1 + " TIMES 4 PLUS 1 " + t2 + "\n");//t3瀛樻斁鐨勬槸瑕佽祴鍊煎厓绱犵殑鍦板潃
		   Put.con(Put.Tab + "HSTORE " + t3 + " 0 0\n");//璧嬪�涓�
		   Put.con(Put.Tab + "MOVE " + t2 + " PLUS " + t2 + " 1\n");//璁℃暟鍣ㄥ姞涓�
		   Put.con(Put.Tab + "JUMP " + l + "\n");//璺冲埌l澶�
           Put.con(Put.Tab + l1 + " NOOP\n");//缁撴潫浜�
           if(Put.Tab.length()>1)
		   Put.Tab=Put.Tab.substring(1);
		   Put.con(Put.Tab + "RETURN " + t1 + "\n");//杩斿洖鏁扮粍
		   Put.con(Put.Tab + "END\n");
		   return "null";
	   }	
	   /**
	    * f0 -> "new"
	    * f1 -> Identifier()
	    * f2 -> "("
	    * f3 -> ")"
	    */
	   public String visit(AllocationExpression n,Object argu)
	   {
		   Temp t = new Temp();
		   Temp t2 = new Temp();
		   String currClassName = n.f1.f0.toString();//褰撳墠绫诲悕
		   MyClass currClass = MyCollector.SearchClass(currClassName); //褰撳墠绫�
		   Put.con("\n" + Put.Tab + "BEGIN\n");
		   int methodNum = currClass.Mlist.size();
		   int variableNum =currClass.Vlist.size();
		   Put.con(Put.Tab + "MOVE " + t + " HALLOCATE " + methodNum * 4 + "\n");//缁欐瘡涓嚱鏁板垎閰嶅洓涓瓧鑺傜殑鎸囬拡
		   Put.con(Put.Tab + "MOVE " + t2 + " HALLOCATE "+ (variableNum+1)* 4+"\n");//缁檛2鍒嗛厤(variableNum+1)* 4涓瓧鑺傦紝鍓嶅洓涓瓧鑺傜敤鏉ユ寚鍚憈鎵�寚鐨勭┖闂达紝鍗虫墍鏈夊嚱鏁扮殑绌洪棿锛屽悗闈㈢殑绌洪棿瀛樻斁璇ョ被鍙婂叾鐖剁被鐨勫彉閲�
	       for(int j=0;j<currClass.Mlist.size();j++)
		   {
			  String tempMethodName=currClass.Mlist.get(j).Name;
			  Put.con(Put.Tab + "HSTORE " + t + " " + j*4 + " " +currClass.Mlist.get(j).Belong+"_" + tempMethodName);
			  Put.con("\n");
		    }//灏嗙被鍚峗鍑芥暟鍚嶅瓨鏀惧湪涓存椂瀵勫瓨鍣╰閲�j*4琛ㄧず鍋忕Щ閲�
           
		    for(int i = 1;i <= variableNum;i++)
		    {
			   Put.con(Put.Tab + "HSTORE " + t2 + " " + i*4 +" " + 0 + "\n");//鍙橀噺鍏ㄩ儴鍒濆鍖栦负0
		    }
            Put.con(Put.Tab + "HSTORE " + t2 + " 0 " + t + "\n");//灏嗗嚱鏁板姞杞藉埌杩欓噷
            if(Put.Tab.length()>1)
            Put.Tab=Put.Tab.substring(1);
		    Put.con(Put.Tab + "RETURN " + t2 + "\n");
		    Put.con(Put.Tab + "END\n");
		    return n.f1.f0.toString();
	   }
	   /**
	    * f0 -> "!"
	    * f1 -> Expression()
	    */
	   public String visit(NotExpression n, Object argu)
	   {
		   Put.con("\n" + Put.Tab + "BEGIN\n");
		   Temp t = new Temp();
		   Put.con(Put.Tab + "CJUMP ");
		   n.f1.accept(this,argu);
		   Lable l = new Lable("NOERROR");
		   Lable l1 = new Lable("NOERROR");
		   Put.con(l+"\n");
		   Put.con(Put.Tab + "MOVE " + t + " 0 \n");
		   Put.con(Put.Tab + "JUMP " + l1.toString() + "\n");
		   Put.con(Put.Tab + l + " NOOP\n");
		   Put.con(Put.Tab + "MOVE " + t + " 1 \n");
		   Put.con(Put.Tab + l1 + " NOOP\n");
		   if(Put.Tab.length()>1)
		   Put.Tab=Put.Tab.substring(1);
		   Put.con(Put.Tab + "RETURN " + t+"\n");
		   Put.con(Put.Tab + "END\n");
		   return null;
	   }
	   /**
	    * f0 -> "("
	    * f1 -> Expression()
	    * f2 -> ")"
	    */
	   public String visit(BracketExpression n, Object argu) 
	   {
	      return n.f1.accept(this,argu);
	   }	   
	   /**
	    * f0 -> <IDENTIFIER>
	    */
	   public String visit(Identifier n, Object argu)
	   {
		   MyMeth currMethod = (MyMeth)argu;
		   String currClassName = currMethod.Belong;
		   String identifierName = n.f0.toString();
		   String typeName="";
		   boolean flag=false;
		   boolean tflag=false;
		   for(int i=0;i<currMethod.Paralist.size();i++)
		     if(currMethod.Paralist.get(i).Name==identifierName )
			 {    
			      typeName=currMethod.Paralist.get(i).Type;
				  flag=true;
				  Temp tt=new Temp(currMethod.Paralist.get(i).GetNumber());
				  Put.con("\n");
				  Put.con(Put.Tab+"BEGIN\n");
				  Put.con(Put.Tab+" HLOAD "+tt+" PLUS " + new Temp(1)+" TIMES 4 "+i+" 0\n");
				  if(Put.Tab.length()>1)
					  Put.Tab=Put.Tab.substring(1);
				  Put.con(Put.Tab+"RETURN  "+tt+"\n");
				  Put.con(Put.Tab+"END\n");
				  break;
			 }
	       if(!flag)
		   {
	    	   for(int i=0;i<currMethod.Vlist.size();i++)
			   if(currMethod.Vlist.get(i).Name==identifierName )
			   {   
				    typeName =currMethod.Vlist.get(i).Type;
				    MyVar myvar=currMethod.Vlist.get(i);
					Put.con(" " + new Temp(myvar.GetNumber()));
					tflag=true;
					break;
			   }
		   }
          if((!flag)&&(!tflag))
		   {
              MyClass currClass = MyCollector.SearchClass(currClassName);  
			  int j;
              for(j=0;j<currClass.Vlist.size();j++)
                if(currClass.Vlist.get(j).Name==identifierName )
			     {
                	typeName =currClass.Vlist.get(j).Type;
                	break;
                 }
              Temp t = new Temp();
              Put.con("\n");
              Put.con(Put.Tab + "BEGIN\n");
	          Put.Tab+=" ";
	          Put.con(Put.Tab + "HLOAD " +" "+ t + " " + new Temp(0) + " "  +(j+1)*4+ "\n");//j*4琛ㄧず鍋忕Щ閲�
	          if(Put.Tab.length()>1)
	          Put.Tab=Put.Tab.substring(1);
              Put.con(Put.Tab + "RETURN " + t + "\n");
		      Put.con(Put.Tab + "END\n");
		   }
          return typeName+"."+identifierName;
		 
	   }
     	
}
