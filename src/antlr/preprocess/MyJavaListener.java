package antlr.preprocess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import Library.Pair;
import Library.Variable;
import antlr.preprocess.JavaParser.*;

public class MyJavaListener extends JavaBaseListener {
	private Map<Pair, MethodDeclarationContext> methodRanges = new HashMap<Pair, MethodDeclarationContext>(); 
	private Map<Pair, List<Variable>> variableMap = new HashMap<Pair, List<Variable>>(); 
	private Map<Pair, BlockStatementContext> allStatementRanges = new HashMap<Pair, BlockStatementContext>();
	private Map<Pair, String> returnType = new HashMap<Pair, String>();
	
	//no VariableDeclarations needed here;
	private List<Pair> statementRanges = new ArrayList<Pair>(); 
	private List<Variable> fieldDeclarations = new ArrayList<Variable>();
	private String packageName = "";

	@Override 
	public void enterMethodDeclaration(@NotNull MethodDeclarationContext ctx) {
		Pair tempRange = new Pair(ctx.start.getLine(), ctx.stop.getLine());
		methodRanges.put(tempRange,ctx);
//		System.out.println("METHODRETURNTYPE: " + ctx.getChild(0).getText());
		returnType.put(tempRange, ctx.getChild(0).getText());
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
		
		allStatementRanges.put(new Pair(start, stop), ctx);
	}

	public Set<String> getVariabelsUsedInStatementRange(Pair range){
		if(!(this.allStatementRanges.containsKey(range))){
			Set<String> emptyList = new HashSet<String>();
			return emptyList;
		}
		BlockStatementContext ctx = this.allStatementRanges.get(range);
		Set<String> variables = new HashSet<String>();
		for(int i = 0; i < ctx.getChildCount(); i++){
			getVariableList(ctx.getChild(i), variables);
		}
		
//		for(String variable: variables){
//			System.out.println("VARIABLE! " + variable);
//		}
		
		return variables;
	}
	
	private Set<String> getVariableList(ParseTree tree, Set<String> variables) {
		if(tree instanceof TerminalNode){
			TerminalNode terminal = (TerminalNode)tree;
			if(terminal.getSymbol().getType() == JavaLexer.Identifier && terminal.getParent() instanceof PrimaryContext){
				if(!variables.contains(terminal.getText())){
					variables.add(terminal.getText());				}
			}
		}else{
			for(int i = 0; i < tree.getChildCount(); i++){
				getVariableList(tree.getChild(i), variables);
			}
		}
		return variables;
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
		
//		for(Variable tempVar: fieldDeclarations){
//			System.out.println("MapEntry: " + tempVar.getID() + " - " + tempVar.getType());
//		}
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
		
//		for(Pair keyRange: variableMap.keySet()){
//			System.out.println("Mehtodrange: " + keyRange.toString());
//			
//			for(Variable var: variableMap.get(keyRange)){
//				System.out.println("Variable: " + var.toString());
//			}
//		}
		
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
		
//		for(Pair keyRange: variableMap.keySet()){
//			System.out.println("Mehtodrange: " + keyRange.toString());
//			
//			for(Variable var: variableMap.get(keyRange)){
//				System.out.println("Variable: " + var.toString());
//			}
//		}
	}
	
	public String getPackageName(){
		return packageName;
	}
	
	public Pair getBigSuspiciousRange(int bigSuspicious){
		Pair bigSuspiciousRange = new Pair(0,0);
		for(Pair range: statementRanges){
//			System.out.println("statementranges: " + range.getLeft() + " " + range.getRight());
			if((range.getLeft() <= bigSuspicious) && (range.getRight() >= bigSuspicious)){
				if((range.getRight() - range.getLeft()) > (bigSuspiciousRange.getRight() - bigSuspiciousRange.getLeft())){
					bigSuspiciousRange = range;
				}
			}
		}
//		System.out.println("BigSuspiciousRange: " + bigSuspiciousRange.toString());
		return bigSuspiciousRange;
	}
	
	public List<Pair> getBiggestRanges(){
		List<Pair> ranges = new ArrayList<Pair>(this.allStatementRanges.keySet());
		Collections.sort(ranges);
		Pair tempStatement = ranges.get(0);
		List<Pair> biggestRanges = new ArrayList<Pair>();
		for(int i = 1; i < ranges.size(); i++){
			//The list is sorted, so we just need to look at the upper border
			if((tempStatement.getRight() < ranges.get(i).getRight())){
				//Not sure if this is necessary, but for safety, we check if two statements started at 
				//the same line and then the bigger range would be later in the list after the sort.
				if(tempStatement.getLeft() == ranges.get(i).getLeft()){
					tempStatement = ranges.get(i);
					continue;
				}
				biggestRanges.add(tempStatement);
				tempStatement = ranges.get(i);
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
	
	public List<Variable> getVariableListfromRange(Pair range){
		for(Pair methodRange: variableMap.keySet()){
			if(range.getLeft() >= methodRange.getLeft() && range.getRight() <= methodRange.getRight()){
				return variableMap.get(methodRange);
			}
		}
		List<Variable> emptyList = new ArrayList<Variable>();
		return emptyList;
	}
	
	public MethodDeclarationContext getSpecificMethodContext(int lineNumber){
		for(Pair range: methodRanges.keySet()){
			if((range.getLeft() <= lineNumber) && (range.getRight() >= lineNumber)){
				return methodRanges.get(range);
			}
		}
		return null;		
	}
	
	public String getReturnType(Pair range){
		for(Pair methodRange: returnType.keySet()){
			if((methodRange.getLeft() <= range.getLeft()) && (methodRange.getRight() >= range.getRight())){
				return returnType.get(methodRange);
			}
		}
		
		return "";
	}
}