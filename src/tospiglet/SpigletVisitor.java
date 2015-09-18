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

public class SpigletVisitor extends GJDepthFirst<String,Object>{
	public SpigletVisitor(int num)
	{
		SpigletTemp.num = num;
	}
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
	      SpigletOutput.add("MAIN\n");
	      SpigletOutput.Space+=" ";
	      n.f1.accept(this, argu);
	      if(SpigletOutput.Space.length()>1)
	    	  SpigletOutput.Space=SpigletOutput.Space.substring(1); 	      
	      SpigletOutput.add("END\n");
          n.f3.accept(this, argu);
	      SpigletOutput.add("\n");
	      return null;
	   }
	   /**
	    * f0 -> ( ( Label() )? Stmt() )*
	    */
	   public String  visit(StmtList n, Object argu) {//����MAIN��������
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
	   public String visit(Procedure n, Object argu)//���ܺ�������
	   {
		  n.f0.accept(this,argu);
		  SpigletOutput.add(" [ " + n.f2.f0.toString() + " ]\n");//��������������f[2]
		  SpigletOutput.add("BEGIN\n");
		  SpigletOutput.Space+=" ";
	      String returnTemp = n.f4.accept(this,argu);//returnTemp��ʾ��ŷ���ֵ��TEMP 
	      if(SpigletOutput.Space.length()>1)
	    	  SpigletOutput.Space= SpigletOutput.Space.substring(1);
	      SpigletTemp temp = new  SpigletTemp();
	      SpigletOutput.add(SpigletOutput.Space+"MOVE " + temp + " " + returnTemp + "\n");
	      SpigletOutput.add(SpigletOutput.Space+"RETURN " + temp+"\n");//return  TEMP 
	      SpigletOutput.add(SpigletOutput.Space+"END\n");
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
		  SpigletOutput.add(SpigletOutput.Space+"NOOP\n");
	      return null;
	   }	
	   /**
	    * f0 -> "ERROR"
	    */
	   public String visit(ErrorStmt n, Object argu)
	   {
		   SpigletOutput.add(SpigletOutput.Space+"ERROR\n");
	     return null;
	   }	   
	   /**
	    * f0 -> "CJUMP"
	    * f1 -> Exp()
	    * f2 -> Label()
	    */
	   public String  visit(CJumpStmt n, Object argu) 
	   {
		  String returnTemp = n.f1.accept(this,argu);//����CJump��Ҫ��TEMP 
		  SpigletTemp temp = new  SpigletTemp();
		  SpigletOutput.add(SpigletOutput.Space+"MOVE " + temp + " " + returnTemp + "\n");
		  SpigletOutput.add(SpigletOutput.Space+"CJUMP " + temp + " " );//CJump TEMP
	      n.f2.accept(this,argu);
	      SpigletOutput.add(SpigletOutput.Space+"\n");
	      return null;
	      
	   }	
	   /**
	    * f0 -> "JUMP"
	    * f1 -> Label()
	    */
	   public String visit(JumpStmt n, Object argu)
	   {
		  SpigletOutput.add(SpigletOutput.Space+"JUMP ");//JUMP TEMP
	      n.f1.accept(this,argu);
	      SpigletOutput.add("\n");
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
		   String returnTemp1 = n.f1.accept(this,argu);//��returnTemp1���ܲ���TEMP������һ�����ʽ������PLUS ��������Ҫ�����MOVE
		   if(expression[n.f1.f0.which] != "Temp")//HSTORE EXP ��EXP���ܲ���TEMP ����PLUS��
		   {
		     SpigletTemp temp = new SpigletTemp();
		     SpigletOutput.add(SpigletOutput.Space+"MOVE " + temp + " "+ returnTemp1+"\n");
		     returnTemp1 = temp.toString();//��ʾMOVE �ĵ�һ��TEMP ,��Ϊ����MOVE EXP��EXP��ΪTEMP,�����Ƚ�EXP�Ƶ���TEMP��
		   }
		   SpigletTemp temp = new SpigletTemp();//��ʾMOVE�ĵڶ���TEMP 
	       if(expression[n.f3.f0.which] == "Label")//��Label��ʾ�����ı��,�������ı�ŵ�ַ�Ƶ��ڶ���TEMP��
	       { 
	    	   SpigletOutput.add(SpigletOutput.Space+"MOVE " + temp + " ");
		       SpigletOutput.add(n.f3.accept(this,argu)+"\n");
		   }
	       else//���Ǻ�����ţ�����TEMP �������� ���߱��ʽ
	       {
			   String returnTemp2 = n.f3.accept(this,argu);//���յڶ���EXP���ص�TEMP 
			   SpigletOutput.add(SpigletOutput.Space+"MOVE " + temp + " "+returnTemp2+"\n");
		   }
	       SpigletOutput.add(SpigletOutput.Space+"HSTORE " + returnTemp1 + " " + n.f2.f0.toString() + " " + temp + "\n");//ִ��HSTORE
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
		   String returnTemp = n.f2.accept(this,argu);
		   if(expression[n.f2.f0.which] != "Temp")
		   {
		     SpigletTemp temp = new SpigletTemp();
		     SpigletOutput.add(SpigletOutput.Space+"MOVE " + temp + " "+ returnTemp+"\n");
		     returnTemp = temp.toString();
		   }
		   SpigletOutput.add(SpigletOutput.Space+"HLOAD " + n.f1.accept(this,argu) + " " + returnTemp +  " " + n.f3.f0.toString() + "\n");
	       return null;
	   }
	   /**
	    * f0 -> "MOVE"
	    * f1 -> Temp()
	    * f2 -> Exp()
	    */
	   public String  visit(MoveStmt n, Object argu)
	   {
		   String returnTemp = n.f2.accept(this,argu);//����Exp���ص�TEMP Ҳ�����Ǽ򵥱��ʽ
		   SpigletOutput.add(SpigletOutput.Space+"MOVE " + n.f1.accept(this,argu) + " " + returnTemp + "\n");//MOVEָ��ֻ�仯�˵ڶ���
		   return null;
	   }
	   /**
	    * f0 -> "PRINT"
	    * f1 -> Exp()
	    * 
	    */
	   public String visit(PrintStmt n, Object argu)
	   {
		   String returnTemp = n.f1.accept(this,argu);
		   if(expression[n.f1.f0.which] != "Temp"&&expression[n.f1.f0.which] != "IntegerLiteral"
			   &&expression[n.f1.f0.which] != "Label")
		   {
		     SpigletTemp temp = new SpigletTemp();
		     SpigletOutput.add(SpigletOutput.Space+"MOVE " + temp + " "+ returnTemp+"\n");
		     returnTemp = temp.toString();
		   }
		   SpigletOutput.add(SpigletOutput.Space+"PRINT " + returnTemp+ "\n");
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
	      return  n.f0.accept(this, argu);
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
	      n.f1.accept(this,argu);
	      String returnTemp = n.f3.accept(this,argu);
	      return returnTemp;//����RETURN��TEMP
	   }	
	   /**
	    * f0 -> "CALL"
	    * f1 -> Exp()
	    * f2 -> "("
	    * f3 -> ( Exp() )*
	    * f4 -> ")"
	    */
	   public String visit(Call n, Object argu)//�е�С����
	   {
	     String returnTemp1 = n.f1.accept(this,argu);
	     String returnTemp2 = n.f3.accept(this,argu);
	     SpigletTemp temp = new SpigletTemp();
	     SpigletOutput.add(SpigletOutput.Space+"MOVE " + temp + " " + "CALL " + returnTemp1  + "(" + returnTemp2+")\n");
	     return temp.toString();//����TEMP ����PRINT �����������ú����Ĳ���
	   }   
	   
	   public String visit(NodeListOptional n, Object argu)//�����������
	   {
		   int i = 0;
		   Vector<Node> nodeArray = n.nodes;
		   String returnParalist = "";
		   String temps = "";
		   for(i = 0;i < nodeArray.size();i++)
		   {
			   temps = nodeArray.get(i).accept(this,argu);
			   if(temps!=null && !temps.startsWith("TEMP "))
			   {
				   SpigletTemp temp = new SpigletTemp();
				   SpigletOutput.add(SpigletOutput.Space+"MOVE " + temp + " " + temps+"\n");
				   temps = temp.toString();
			   }
			   if(i>0)
				   returnParalist +=" ";
			   returnParalist+= temps;
		   }
		   return returnParalist;
	   }
	   /**
	    * f0 -> "HALLOCATE"
	    * f1 -> Exp()
	    */
	   public String visit(HAllocate n, Object argu)
	   {
	      String returnTemp = n.f1.accept(this,argu);
	      if(expression[n.f1.f0.which] == "BinOp")
	      {
	    	  SpigletTemp temp  = new  SpigletTemp();
	    	  SpigletOutput.add(SpigletOutput.Space+"MOVE " + temp + " "+returnTemp+" ");
	    	 returnTemp=temp.toString();
	       }
	       return "HALLOCATE " +returnTemp + " ";
         }
	   /**
	    * f0 -> Operator()
	    * f1 -> Exp()
	    * f2 -> Exp()
	    */
	   public String visit(BinOp n, Object argu)
	   {
	      String rtTemp1 = n.f1.accept(this,argu);
	      if(expression[n.f1.f0.which] != "Temp")
	      {
	    	  SpigletTemp temp = new SpigletTemp();
	    	  SpigletOutput.add(SpigletOutput.Space+"MOVE " + temp + " "+ rtTemp1+"\n");
	    	  rtTemp1 = temp.toString();
	      }
	      String rtTemp2 = n.f2.accept(this,argu);
		   if(expression[n.f2.f0.which] != "Temp"&&expression[n.f2.f0.which] != "IntegerLiteral"
			   &&expression[n.f2.f0.which] != "Label")
	      {
	    	  SpigletTemp temp = new SpigletTemp();
	    	  SpigletOutput.add(SpigletOutput.Space+"MOVE " + temp + " "+ rtTemp2+"\n");
	    	  rtTemp2 = temp.toString();
	      }
	      return (n.f0.accept(this,argu) + " " + rtTemp1 + " " + rtTemp2+"\n");
       }
	   /**
	    * f0 -> "LT"
	    *       | "PLUS"
	    *       | "MINUS"
	    *       | "TIMES"
	    */
	   public String visit(Operator n, Object argu)
	   {
	     return n.f0.choice.toString();
	   }

	   /**
	    * f0 -> "TEMP"
	    * f1 -> IntegerLiteral()
	    */
	   public String visit(Temp n, Object argu)
	   {
	        return  "TEMP " + n.f1.f0.toString();
	   }

	   /**
	    * f0 -> <INTEGER_LITERAL>
	    */
	   public String visit(IntegerLiteral n, Object argu)
	   {
	      return n.f0.toString();
	   }

	   /**
	    * f0 -> <IDENTIFIER>
	    */
	   public String visit(Label n, Object argu) 
	   {
		   SpigletOutput.add(n.f0.toString()+ " ");
	      return "";
	   }
	   
	
}
