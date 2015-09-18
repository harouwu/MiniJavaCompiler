package ToMips;

public class MipsOutPut {
	static String output = "";
	static String Space = "";
	public static void add(String s)
	{
		//System.out.print(s);
		output +=Space+s;
	}
    public static String getMipsOutput()
	{
    	
		String MipsEnd = 
		 "         .text            \n" +
		 "         .globl _halloc  \n" +
		 "_halloc:                 \n" +
		 "         li $v0, 9        \n" +
		 "         syscall          \n" +
		 "         j $ra            \n" +
		 "                          \n" +
		 "         .text            \n" +
		 "         .globl _printint \n" +
		 "_printint:                \n" +
		 "         li $v0, 1        \n" +
		 "         syscall          \n" +
		 "         la $a0, newl     \n" +
		 "         li $v0, 4        \n" +
		 "         syscall          \n" +
		 "         j $ra            \n" +
		 "                          \n" +
		 "         .data            \n" +
		 "         .align   0       \n" +
		 "newl:    .asciiz \"\\n\"  \n" +
		 "         .data            \n" +
		 "         .align   0       \n" +
		 "str_er:  .asciiz \" ERROR: abnormal termination\\n\" "+
		 "                          \n" +
		 "         .text            \n" +
		 "         .globl _error    \n" +
		 "_error:                   \n" +
		 "         li $v0, 4        \n" +
		 "         la $a0, str_er   \n" +
		 "         syscall          \n" +
		 "         li $v0, 10       \n" +
		 "         syscall          \n";
       
    	return output+MipsEnd;
	}
    

}
