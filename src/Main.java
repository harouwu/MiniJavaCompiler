import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Vector;

import kanga.KangaParser;

import ProcTable.ARGU;
import ProcTable.BlockCreator;
import ProcTable.IntroCollectorVisitor;
import ProcTable.LabelAndCall_Saver;
import ProcTable.ProcTable;
import ProcTable.SetProcTableVisitor;
import ToMips.MipsVisitor;

import piglet.PigletParser;

import spiglet.SpigletParser;
import spiglet.spiglet2kanga.KangaVisitor;
import tospiglet.GetMaxTempVisitor;
import tospiglet.SpigletOutput;
import tospiglet.SpigletVisitor;
import topiglet.Put;
import topiglet.pigletVisitor;

import minijava.MiniJavaParser;
import minijava.ParseException;
import minijava.TokenMgrError;
import minijava.symboltable.MType;
import minijava.symboltable.MyCollector;
import minijava.symboltable.MyVar;
import minijava.symboltable.MIdentifier;
import minijava.symboltable.MyClass;
import minijava.symboltable.MyMeth;
import minijava.symboltable.MyTempCollector;
import minijava.symboltable.MyType;
import minijava.symboltable.MyVar;
import minijava.syntaxtree.Node;
import minijava.visitor.BuildSymbolTableVisitor;
import minijava.visitor.TypeCheckVisitor;


public class Main {

	public static void main(String[] args) {
			
	try{
		
        /**************************  类型检查  *************************************/
		//System.out.println("Begin to COMPILE");
		Node root = new MiniJavaParser(System.in).Goal();
		//Node root = new MiniJavaParser(new ByteArrayInputStream(s.getBytes())).Goal();
		MType pointer = new MType();
		root.accept(new BuildSymbolTableVisitor(),pointer);
		MyCollector.Extends();
		MyCollector.CheckVar();
		//root.accept(new TypeCheckVisitor(),pointer);
		//MyCollector.Output();
		

		
		/***************************** To Piglet ***********************************/
		pigletVisitor pg = new  pigletVisitor(BuildSymbolTableVisitor.number);
	    root.accept(pg,pointer);
	   // System.out.println("***************************** To Piglet ***********************************\n");
	   // System.out.println(topiglet.Put.getoutput());


	    
	    /***************************** To Spiglet   ********************************/
  	    int pigletTempNum = topiglet.Temp.getNumber();
	    new piglet.PigletParser(new ByteArrayInputStream(topiglet.Put.getoutput().getBytes()));
		//new piglet.PigletParser(new ByteArrayInputStream(s.getBytes()));
		piglet.syntaxtree.Node pigletroot = PigletParser.Goal();
		//GetMaxTempVisitor GetNum = new GetMaxTempVisitor();
		//pigletroot.accept(GetNum,null);
		//int pigletTempNum = GetNum.MaxNumber;
	    pigletroot.accept(new SpigletVisitor(pigletTempNum), null);	

	    
	    
	    //System.out.println("***************************** To Piglet ***********************************\n");
	   //System.out.println(tospiglet.SpigletOutput.getSpigletOutput());
	    
	    
	    
	    /***************************** To Kanga   **********************************/
	    new SpigletParser( (new ByteArrayInputStream(tospiglet.SpigletOutput.getSpigletOutput().getBytes()) ) ) ;
    	spiglet.syntaxtree.Node spigletroot = SpigletParser.Goal();
    	ARGU Pointer = new ARGU();
    	spigletroot.accept(new SetProcTableVisitor(), Pointer);
    	spigletroot.accept(new LabelAndCall_Saver(), Pointer);
    	spigletroot.accept(new BlockCreator(), Pointer);
    	ProcTable.SetBlock();
    	spigletroot.accept(new IntroCollectorVisitor(),Pointer);
    	ProcTable.SetGraph();
    	ProcTable.SetIntroLive();
        spigletroot.accept(new KangaVisitor(),null); 
	    System.out.println("***************************** To Kanga ***********************************\n");
	    System.out.println(spiglet.spiglet2kanga.OutPut.getoutput());  
	    
	    
	    
        /******************************** To   Mips  ********************************/
	    
     	new kanga.KangaParser(new ByteArrayInputStream(spiglet.spiglet2kanga.OutPut.getoutput().getBytes()));
       	kanga.syntaxtree.Node kangaroot = KangaParser.Goal();
       	kangaroot.accept(new MipsVisitor(),null);
        System.out.println("***************************** To Mips ***********************************\n");
		System.out.println(ToMips.MipsOutPut.getMipsOutput());         	
	    
	    
		
		
	}
	catch(TokenMgrError e){
		e.printStackTrace();
	}
	//catch (ParseException e){
	//	e.printStackTrace();
	//}
	catch(Exception e){
		e.printStackTrace();
		}	
	}
}