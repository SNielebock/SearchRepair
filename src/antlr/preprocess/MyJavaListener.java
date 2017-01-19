package antlr.preprocess;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;

import Library.Pair;

public class MyJavaListener extends JavaBaseListener {
	private Pair maxRange = new Pair(0,0);
	private List<Pair> ranges = new ArrayList<Pair>(); 

	@Override 
	public void enterMethodDeclaration(@NotNull JavaParser.MethodDeclarationContext ctx) {
		System.out.println("SYSO_NICE! " + ctx.getText());
	}
	
	@Override 
	public void enterStatement(@NotNull JavaParser.StatementContext ctx) {
		System.out.println("Statement: " + ctx.getText());
		int start = ctx.start.getLine();
		int stop = ctx.stop.getLine();		

		ranges.add(new Pair(start, stop));
	}
	
	@Override 
	public void enterEveryRule(@NotNull ParserRuleContext ctx) { 
//		System.out.println("EveryRule: " + ctx.getText());
	}
	
	public Pair getMaxRange(int bigSuspicious){
		for(Pair range: ranges){
			System.out.println("Range: " + range);
			if((range.getLeft() <= bigSuspicious) && (range.getRight() >= bigSuspicious)){
				if((range.getRight() - range.getLeft()) > (maxRange.getRight() - maxRange.getLeft())){
					maxRange = range;
				}
			}
		}
		System.out.println("MaxRange: " + maxRange.toString());
		return maxRange;
	}
}