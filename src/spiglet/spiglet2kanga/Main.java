package spiglet.spiglet2kanga;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import ProcTable.ARGU;
import ProcTable.BlockCreator;
import ProcTable.IntroCollectorVisitor;
import ProcTable.LabelAndCall_Saver;
import ProcTable.ProcTable;
import ProcTable.SetProcTableVisitor;
import piglet.PigletParser;
import spiglet.ParseException;
import spiglet.SpigletParser;
import spiglet.TokenMgrError;
import spiglet.syntaxtree.Node;
import spiglet.visitor.GJDepthFirst;
import tospiglet.GetMaxTempVisitor;
import tospiglet.SpigletOutput;
import tospiglet.SpigletVisitor;
public class Main { 
 
    public static void main(String[] args) {
    	try {
    		BufferedReader br= new BufferedReader(new InputStreamReader(System.in)); 
    		String temp = "";
    		String s = "";
    		while((temp = br.readLine()) != null)
    		{
    		  s = s + temp + "\n";
    		}

    		//new piglet.PigletParser(new ByteArrayInputStream(s.getBytes()));
    		//piglet.syntaxtree.Node pigletroot = PigletParser.Goal();
    		new SpigletParser( (new ByteArrayInputStream(s.getBytes()) ) ) ;
    		spiglet.syntaxtree.Node root = SpigletParser.Goal();
    		ARGU Pointer = new ARGU();
    		
    	    root.accept(new SetProcTableVisitor(), Pointer);
    	    root.accept(new LabelAndCall_Saver(), Pointer);
    	    root.accept(new BlockCreator(), Pointer);
    	    ProcTable.SetBlock();
    	    root.accept(new IntroCollectorVisitor(),Pointer);
    	    ProcTable.SetGraph();
    	    ProcTable.SetIntroLive();
    		//new spiglet.SpigletParser(System.in);
    		
    	    // ProcTable.Output();
    		root.accept(new KangaVisitor(),null);
    		//System.out.println("haha");
    	 //   System.out.println(spiglet.spiglet2kanga.OutPut.getoutput());
    		FileWriter fw = null;
    		fw = new FileWriter("save.txt");
    	    fw.write(spiglet.spiglet2kanga.OutPut.getoutput());
    		fw.close();
    	   //ProcTable.Output();
    			
    		
    	}
    	catch(TokenMgrError e){
    		//Handle Lexical Errors
    		e.printStackTrace();
    	}

    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
}