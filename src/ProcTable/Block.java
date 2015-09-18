//øÈ¿‡

package ProcTable;

import java.util.Vector;

public class Block {
	
	Vector<Intro> intros;
	int Start;
	int End;
	int Next;
	int JumpDes;
	boolean isLast;
	
	public Block(int s,int e,boolean islast)
	{
		Next = -1;
		JumpDes = -1;
		Start = s;
		End = e;
		isLast = islast;
		intros = new Vector<Intro>();
	}
	
	public void AddIntro(Intro i)
	{
		intros.add(i);
	}

}
