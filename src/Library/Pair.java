package Library;

public class Pair {

	  private int left;
	  private int right;

	  public Pair(int left, int right) {
	    this.left = left;
	    this.right = right;
	  }

	  public int getLeft() { return left; }
	  public int getRight() { return right; }

	  public void setLeft(int left) { this.left = left; }
	  public void setRight(int right) { this.right = right; }	  
	  
	  
	  @Override
	  public boolean equals(Object o) {
	    if (!(o instanceof Pair)) return false;
	    Pair pairo = (Pair) o;
	    return (this.left == pairo.getLeft()) &&
	           (this.right == pairo.getRight());
	  }
	  
	  @Override
	  public String toString(){
		  return "(" + this.left + "," + this.right + ")";
	  }
	}