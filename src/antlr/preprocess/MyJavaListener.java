package antlr.preprocess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;

import Library.Pair;
import Library.Variable;
import antlr.preprocess.JavaParser.*;

public class MyJavaListener extends JavaBaseListener {
	private Map<Pair, MethodDeclarationContext> methodRanges = new HashMap<Pair, MethodDeclarationContext>(); 
	private Map<Pair, List<Variable>> variableMap = new HashMap<Pair, List<Variable>>(); 
	//no VariableDeclarations needed here;
	private List<Pair> statementRanges = new ArrayList<Pair>(); 
	private List<Pair> allStatementRanges = new ArrayList<Pair>();
	private List<Variable> fieldDeclarations = new ArrayList<Variable>();
	private String packageName = "";

	@Override 
	public void enterMethodDeclaration(@NotNull MethodDeclarationContext ctx) {
		Pair tempRange = new Pair(ctx.start.getLine(), ctx.stop.getLine());
		methodRanges.put(tempRange,ctx);
	}
	
	@Override 
	public void enterStatement(@NotNull StatementContext ctx) {
		int start = ctx.start.getLine();
		int stop = ctx.stop.getLine();		

		statementRanges.add(new Pair(start, stop));
	}
	
	@Override
	public void enterBlockStatement(@NotNull BlockStatementContext ctx) {
		int start = ctx.start.getLine();
		int stop = ctx.stop.getLine();		

		allStatementRanges.add(new Pair(start, stop));
	}
	
	@Override 
	public void enterEveryRule(@NotNull ParserRuleContext ctx) { 
//		System.out.println("EveryRule: " + ctx.getText());
	}
	
	@Override
	public void enterPackageDeclaration(@NotNull PackageDeclarationContext ctx) { 
		packageName = ctx.qualifiedName().getText();
	}
	
	@Override
	public void enterFieldDeclaration(@NotNull FieldDeclarationContext ctx) {
		String type = "";
		String id = "";
		for(ParseTree context: ctx.children){
			if(context instanceof TypeTypeContext){
				type =  context.getText();
			}else if(context instanceof VariableDeclaratorsContext){
				for(int i = 0; i < context.getChildCount(); i++){
					if(context.getChild(i) instanceof VariableDeclaratorContext){
						for(int j = 0; j < context.getChild(i).getChildCount(); j++){
							if(context.getChild(i).getChild(j) instanceof VariableDeclaratorIdContext){
								id = context.getChild(i).getChild(j).getText();
								break;
							}
						}
						break;
					}
				}
				fieldDeclarations.add(new Variable(id, type));
				type = "";
				id = "";
			}
		}
		
		for(Variable tempVar: fieldDeclarations){
			System.out.println("MapEntry: " + tempVar.getID() + " - " + tempVar.getType());
		}
	}
	
	@Override 
	public void enterFormalParameterList(@NotNull FormalParameterListContext ctx) {
		Pair tempMethodRange = new Pair(0,0);
		for(Pair range: methodRanges.keySet()){
			if(ctx.getStart().getLine() >= range.getLeft() && ctx.getStop().getLine() <= range.getRight()){
				tempMethodRange = range;
				break;
			}
		}
		
		List<Variable> tempVariableList = new ArrayList<Variable>();
		
		String type = "";
		String id = "";
		for(ParseTree context: ctx.children){
			if(context instanceof FormalParameterContext || context instanceof LastFormalParameterContext){
				for(int i = 0; i < context.getChildCount(); i++){
					if(context.getChild(i) instanceof TypeTypeContext){
						type = context.getChild(i).getText();
					}else if(context.getChild(i) instanceof VariableDeclaratorIdContext){
						id = context.getChild(i).getText();
					}
				}
				if(!(type.equals("") || id.equals(""))){
					tempVariableList.add(new Variable(id, type));
				}else{
					System.out.println("Something went wrong! No type or id found - MyJavaListener.enterFormalParameterList");
				}
				type = "";
				id = "";
			}
		}
		
		if(variableMap.containsKey(tempMethodRange)){
			tempVariableList.addAll(variableMap.get(tempMethodRange));
			variableMap.put(tempMethodRange, tempVariableList);
		}else{
			variableMap.put(tempMethodRange, tempVariableList);
		}
		
		for(Pair keyRange: variableMap.keySet()){
			System.out.println("Mehtodrange: " + keyRange.toString());
			
			for(Variable var: variableMap.get(keyRange)){
				System.out.println("Variable: " + var.toString());
			}
		}
		
	}

	@Override
	public void enterLocalVariableDeclaration(@NotNull LocalVariableDeclarationContext ctx) {
		Pair tempMethodRange = new Pair(0,0);
		for(Pair range: methodRanges.keySet()){
			if(ctx.getStart().getLine() >= range.getLeft() && ctx.getStop().getLine() <= range.getRight()){
				tempMethodRange = range;
				break;
			}
		}
		
		List<Variable> tempVariableList = new ArrayList<Variable>();
		
		String type = "";
		String id = "";
		for(ParseTree context: ctx.children){
			if(context instanceof TypeTypeContext){
				type =  context.getText();
			}else if(context instanceof VariableDeclaratorsContext){
				for(int i = 0; i < context.getChildCount(); i++){
					if(context.getChild(i) instanceof VariableDeclaratorContext){
						for(int j = 0; j < context.getChild(i).getChildCount(); j++){
							if(context.getChild(i).getChild(j) instanceof VariableDeclaratorIdContext){
								id = context.getChild(i).getChild(j).getText();
								break;
							}
						}
						break;
					}
				}
				if(!(type.equals("") || id.equals(""))){
					tempVariableList.add(new Variable(id, type));
				}else{
					System.out.println("Something went wrong! No type or id found - MyJavaListener.enterLocalVariableDeclaration");
				}
				type = "";
				id = "";
			}
		}
		
		if(variableMap.containsKey(tempMethodRange)){
			tempVariableList.addAll(variableMap.get(tempMethodRange));
			variableMap.put(tempMethodRange, tempVariableList);
		}else{
			variableMap.put(tempMethodRange, tempVariableList);
		}
		
		for(Pair keyRange: variableMap.keySet()){
			System.out.println("Mehtodrange: " + keyRange.toString());
			
			for(Variable var: variableMap.get(keyRange)){
				System.out.println("Variable: " + var.toString());
			}
		}
	}
	
	public String getPackageName(){
		return packageName;
	}
	
	public Pair getBigSuspiciousRange(int bigSuspicious){
		Pair bigSuspiciousRange = new Pair(0,0);
		for(Pair range: statementRanges){
			if((range.getLeft() <= bigSuspicious) && (range.getRight() >= bigSuspicious)){
				if((range.getRight() - range.getLeft()) > (bigSuspiciousRange.getRight() - bigSuspiciousRange.getLeft())){
					bigSuspiciousRange = range;
				}
			}
		}
		System.out.println("BigSuspiciousRange: " + bigSuspiciousRange.toString());
		return bigSuspiciousRange;
	}
	
	public List<Pair> getBiggestRanges(){
		Collections.sort(this.allStatementRanges);
		Pair tempStatement = this.allStatementRanges.get(0);
		List<Pair> biggestRanges = new ArrayList<Pair>();
		for(int i = 1; i < this.allStatementRanges.size(); i++){
			//The list is sorted, so we just need to look at the upper border
			if((tempStatement.getRight() < this.allStatementRanges.get(i).getRight())){
				biggestRanges.add(tempStatement);
				tempStatement = this.allStatementRanges.get(i);
			}
			
			if(i == this.allStatementRanges.size() - 1){
				biggestRanges.add(tempStatement);
			}
		}
		return biggestRanges;
	}
	
	public List<Variable> getFieldDeclarations(){
		return fieldDeclarations;
	}
	
	public Map<Pair, List<Variable>> getVariableMap(){
		return variableMap;
	}
	
	public MethodDeclarationContext getSpecificMethodContext(int lineNumber){
		for(Pair range: methodRanges.keySet()){
			if((range.getLeft() <= lineNumber) && (range.getRight() >= lineNumber)){
				return methodRanges.get(range);
			}
		}
		return null;		
	}
}