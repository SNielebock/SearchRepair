package Repository;

import java.util.List;
import java.util.Map;

import lookups.TypeTable;
import Database.DataHandler;
import Database.EntryObject;

public class EntryTranslator {
	private Method method;
	private EntryObject entryObject;
	
	
	
	public EntryTranslator(Method method) {
		super();
		this.method = method;
		this.entryObject = new EntryObject();
		this.entryObject.setSource(method.getSource());
		
		initEntryObject();
		
		
	}
	
	
	
	
	private void initEntryObject() {
		translateWithSSA();
		
	}




	private void translateWithSSA() {
		// deal with two situations, one is path with return statement, one is path without return statement
		for(String path : method.getPath()){
			System.out.println("PATH: " + path);
			PathTranslator pathTranslator = new PathTranslator(path, method.getPathToInput().get(path));
			List<String> constraints = pathTranslator.getSsa();
			StringBuilder constraintString = new StringBuilder();
			String declare = "(declare-fun _output_  () " + TypeTable.getInstance().getType(method.getReturnType()) + " )\n";
			if (!declare.contains("Void"))	{
				constraintString.append(declare);
			}
			for(String line : constraints){
				constraintString.append(line);
				constraintString.append("\n");
			}
			
			
			System.out.println("ConstraintString: " + constraintString.toString());
			this.entryObject.getPathConstraint().put(path, constraintString.toString());
			
			Map<String, String> mapping = pathTranslator.getVariableMap();
			StringBuilder mappingString = new StringBuilder();
			for(String var : mapping.keySet()){
				String one = DataHandler.concat(var, mapping.get(var));
				mappingString.append(one);
			}
			
			System.out.println("Mapping: " + mapping.toString());
			this.entryObject.getPathVariableMap().put(path, mapping.toString());
			
			
			Map<String, String> tracks = pathTranslator.getVariableTrack();
			StringBuilder trackString = new StringBuilder();
			for(String var : tracks.keySet()){
				String one = DataHandler.concat(var, tracks.get(var));
				trackString.append(one);
			}
			
			System.out.println("TrackString: " + trackString.toString());
			this.entryObject.getPathVariableTrack().put(path, trackString.toString());
			
			
			Map<String, String> types = pathTranslator.getVariableType();
			StringBuilder typeString = new StringBuilder();
			for(String var : types.keySet()){
				String one = DataHandler.concat(var, types.get(var));
				typeString.append(one);
			}			
			
			System.out.println("TypeString: " + typeString.toString());
			this.entryObject.getPathVariablesTypes().put(path, typeString.toString());
			
			
			Map<String, String> formals = pathTranslator.getFormalVariables();
			StringBuilder formalString = new StringBuilder();
			for(String var : formals.keySet()){
				String one = DataHandler.concat(var, formals.get(var));
				formalString.append(one);
			}			
			
			System.out.println("FormalString: " + formalString.toString());
			this.entryObject.getPathFormalVariables().put(path, formalString.toString());
			
		}

		
		
	}




	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public EntryObject getEntryObject() {
		return entryObject;
	}
	public void setEntryObject(EntryObject entryObject) {
		this.entryObject = entryObject;
	}
	
	
	
	
	
	
}
