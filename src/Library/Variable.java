package Library;

public class Variable {
	private String id = "";
	private String type = "";
	
	public Variable(String id, String type){
		this.id = id;
		this.type = type;
	}
	
	public String getID(){
		return this.id;
	}
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getType(){
		return this.type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	  @Override
	  public String toString(){
		  return "ID: " + this.id + " Type: " + this.type;
	  }
}
