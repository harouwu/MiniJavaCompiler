package ToMips;

import java.io.ByteArrayInputStream;

import kanga.KangaParser;

public class Main {
	public static void main(String[] args) {
		
	try{
	   //new kanga.KangaParser(new ByteArrayInputStream(tospiglet.SpigletOutput.getSpigletOutput().getBytes()));
	 new kanga.KangaParser(System.in);
	 kanga.syntaxtree.Node kangaroot = KangaParser.Goal();
	 kangaroot.accept(new MipsVisitor(),null);
	System.out.println(ToMips.MipsOutPut.getMipsOutput());  
	}
	
	catch(Exception e){
		e.printStackTrace();
		}	
	}

}
