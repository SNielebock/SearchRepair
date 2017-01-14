package antlr.preprocess;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;

public class MyJavaListener extends JavaBaseListener{

	@Override 
	public void enterClassDeclaration(@NotNull JavaParser.ClassDeclarationContext ctx) { 
		System.out.println("Class: " + ctx.Identifier().getText());
	}
	
	@Override 
	public void enterMethodDeclaration(@NotNull JavaParser.MethodDeclarationContext ctx) { 
		System.out.println("Method: " + ctx.getText());
	}
	
	@Override 
	public void enterCompilationUnit(@NotNull JavaParser.CompilationUnitContext ctx) { 
		System.out.println("CompilationUnit: " + ctx.getText());
	}
	
	@Override
	public void enterEveryRule(@NotNull ParserRuleContext ctx) { 
		System.out.println("Every rule: " + ctx.getText());
	}
}
