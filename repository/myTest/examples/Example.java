package examples;

public class Example {

    public static void main (String[] args) {
        Example ex = new Example();
        ex.foo(4, 5);
    }

	public int foo(int x, int y){
		int i = 25;



		if (x > y){
			if(x > 5) {
				return x+y;
			}else{
				i = 5;
			}
		}else{
			if(y < 10){
				i = (i + x) - y;
			}else{
				i = x*y;
			}
		}
		return i;
	}

}
