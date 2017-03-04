package antlr.preprocess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;

import Library.Pair;
import antlr.preprocess.JavaParser.*;

public class MyJavaListener extends JavaBaseListener {
	private Map<Pair, MethodDeclarationContext> methodRanges = new HashMap<Pair, MethodDeclarationContext>(); 
	private List<Pair> statementRanges = new ArrayList<Pair>(); 
	private List<Pair> allStatementRanges = new ArrayList<Pair>();
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
	
	public MethodDeclarationContext getSpecificMethodContext(int lineNumber){
		for(Pair range: methodRanges.keySet()){
			if((range.getLeft() <= lineNumber) && (range.getRight() >= lineNumber)){
				return methodRanges.get(range);
			}
		}
		return null;		
	}
}