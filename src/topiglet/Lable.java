package topiglet;

public class Lable {
	static int num;
	String currS;
	int mynum;
	Lable( String s){
		num ++;
		mynum = num;
		currS = s;
	}
	public String toString(){
		return currS + mynum;
	}
}