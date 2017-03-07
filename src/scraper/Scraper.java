package scraper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import scraper.ScrapeParser.AssignStatContext;
import scraper.ScrapeParser.Assign_expressionContext;
import scraper.ScrapeParser.AtomContext;
import scraper.ScrapeParser.DeclarationStatContext;
import scraper.ScrapeParser.ElseifblockContext;
import scraper.ScrapeParser.If_statContext;
import scraper.ScrapeParser.ProgContext;
import scraper.ScrapeParser.ReturnStatContext;
import scraper.ScrapeParser.StatContext;
import Library.Pair;
import Library.Utility;
import Library.Variable;
import antlr.preprocess.MyJavaListener;


public class Scraper {
	private String folder;
	private String projecName;
	private static final String scrapRoot = "./repository/scraper";
//	private String returnType;
	
	
	public Scraper(String folder) {
		super();
		this.folder = folder;
		int temp = this.folder.substring(0, this.folder.lastIndexOf("/")).lastIndexOf("/");
		this.projecName = this.folder.substring(temp + 1);
//		this.returnType = "void";
		File dir = new File(scrapRoot + "/" + projecName);
		if(!dir.exists() || !dir.isDirectory()) {
			dir.mkdir();
		}
		scrape(new File(folder));
	}
	
	public void scrape(){
		scrape(new File(this.folder));
	}

	public  void scrape(File dir){
		HashMap<String, Pair> snippetRangeMap = new HashMap<String, Pair>();
		HashMap<String, Set<String>> snippetVariablesMap = new HashMap<String, Set<String>>();
		for(File file : dir.listFiles()){
			getSupport(file, snippetRangeMap, snippetVariablesMap);
		}
	}
	
	public void scrape(String filePath){
		HashMap<String, Pair> snippetRangeMap = new HashMap<String, Pair>();
		HashMap<String, Set<String>> snippetVariablesMap = new HashMap<String, Set<String>>();
//		for(File file : dir.listFiles()){
			getSupport(new File(filePath), snippetRangeMap, snippetVariablesMap);
//		}
	}
	
	private void getSupport(File file, HashMap<String, Pair> snippetRangeMap, HashMap<String, Set<String>> snippetVariablesMap) {
		if(file.isDirectory()){
			for(File g : file.listFiles()){
				getSupport(g, snippetRangeMap, snippetVariablesMap);
			}
		}
		else{
			parse(file, snippetRangeMap, snippetVariablesMap);
		}
		
	}

	private void parse(File file, HashMap<String, Pair> snippetRangeMap, HashMap<String, Set<String>> snippetVariablesMap) {
		
		if(!file.getAbsolutePath().endsWith(".java")) return;
		String fileString = Utility.getStringFromFile1(file.getAbsolutePath());
		List<String> array = new ArrayList<String>();
		
		int start = -1;
		int end = -1;
		Stack<Character> stack = new Stack<Character>();
		Stack<Integer> index = new Stack<Integer>();
		//TODO: probably not needed
//		for(int i = 0; i < fileString.length(); i++)
//		{
//			char c = fileString.charAt(i);
//			if(c == '{' ){
//				stack.add(c);
//				index.push(i);
//			}
//			else if(c == '}'){
//				if(stack.isEmpty()) {
//					index.clear();
//					continue;
//				}
//				stack.pop();
//				int temp = index.pop();
//				if(stack.isEmpty()){
//					start = temp;
//					end = i;
//					start = fileString.substring(0, start - 1).lastIndexOf('}');
//					//System.out.println(fileString.substring(start + 1, end + 1));
//					array.add (fileString.substring(start + 1, end + 1));
//				}				
//			}
//		}
		
		MyJavaListener listener = Utility.getANTLRListener(file.getAbsolutePath());
		List<Pair> biggestRanges = listener.getBiggestRanges();
		if(biggestRanges.size() == 0){
			return;
		}
		Collections.sort(biggestRanges);
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath())));
			String s = null;
			
			int lineNumber = 0;
			int listIndex = 0;
			String snippetString = "";
			while(lineNumber < biggestRanges.get(biggestRanges.size() - 1).getRight()){
				s = reader.readLine();
				lineNumber++;
				if(lineNumber >= biggestRanges.get(listIndex).getLeft() && lineNumber <= biggestRanges.get(listIndex).getRight()){
					snippetString += s;
					if(lineNumber == biggestRanges.get(listIndex).getRight()){
						Set<String> variableSet = listener.getVariabelsUsedInStatementRange(biggestRanges.get(listIndex));
						snippetRangeMap.put(snippetString, biggestRanges.get(listIndex));
						snippetVariablesMap.put(snippetString, variableSet);
						System.out.println("SNIPPETSTRING: " + snippetString + " Range: " + biggestRanges.get(listIndex));
						
						snippetString = "";
						listIndex++;
					}else{
						snippetString += System.getProperty("line.separator");
					}
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
//		for(String target : array){
//			try{
				//System.out.println(target);
				//target = Utility.getStringFromFile(this.casePrefix + ".c");
				
				
				
//				InputStream stream = new ByteArrayInputStream(target.getBytes());
//				ANTLRInputStream input = new ANTLRInputStream(stream);
//				ScrapeLexer lexer = new ScrapeLexer(input);
//				CommonTokenStream tokens = new CommonTokenStream(lexer);
//				ScrapeParser parser = new ScrapeParser(tokens);
//				ProgContext prog = parser.prog();
//				List<StatContext> stats = prog.stat();
//				for(StatContext con : stats){
//					ParseTree child = con.getChild(0);
//					if(child instanceof DeclarationStatContext){
//						list.add(getTreeString(child) + ";");
//					}
//					else if(child instanceof AssignStatContext){
//						AssignStatContext assign = (AssignStatContext) child;
//						list.add(getTreeString(child) + ";");
//					}
//					else if(child instanceof If_statContext) {
//						If_statContext ifstat = (If_statContext) child;
//						list.add(getTreeString(ifstat));
//					}
//					else if(child instanceof ReturnStatContext){
//						ReturnStatContext returnStat = (ReturnStatContext) child;
//						list.add(getTreeString(child) + ";");
//					}
//					else{
//						//do nothing
//					}
//				}
				//if(!g) continue;
				//for()
				//System.out.println(parser.prog().function().block().getText());
				//list.add(target);

//				}
//				
//			}catch(Exception e){
//				continue;
//			}
//		}
		//
		
		List<Variable> fieldDeclarations = listener.getFieldDeclarations();
		
		int i = 0;
		for(String snippet : snippetRangeMap.keySet()){
			System.out.println("SNIPPET BEFORE GENERATE: " + snippet);
			if(i > 1000){
				return;
			}
			String returnType = listener.getReturnType(snippetRangeMap.get(snippet));
			List<Variable> variableList = listener.getVariableListfromRange(snippetRangeMap.get(snippet));
			generate(scrapRoot + "/" + this.projecName + "/test" + i++ + ".java", snippet, variableList, snippetVariablesMap.get(snippet), fieldDeclarations, returnType);
		}
		
	}
	


	public   String getTreeString(ParseTree root){
		StringBuilder sb = new StringBuilder();
		if(root.getChildCount() == 0) return root.getText();
		for(int i = 0; i < root.getChildCount(); i++){
			ParseTree child = root.getChild(i);
			//System.out.println(child.getText());
			sb.append(getTreeString(child) + " ");
		}
		return sb.toString();
	}

	
	public String getTreeString(If_statContext ifstat){
		StringBuilder sb = new StringBuilder();
		sb.append(getTreeString(ifstat.ifpart()));
		if(ifstat.elseifpart() != null){
			sb.append(getTreeString(ifstat.elseifpart().elseifblock()));
		}
		
		if(ifstat.elsepart() != null){
			sb.append(getTreeString(ifstat.elsepart()));
		}
		return sb.toString();
	}
	
	private Object getTreeString(List<ElseifblockContext> elseifblock) {
		StringBuilder sb = new StringBuilder();
		for(ElseifblockContext block : elseifblock){
			sb.append(getTreeString(block));
		}
		return sb.toString();
	}

	public void generate(String file, String snippet, List<Variable> declaratedVariables, Set<String> variableIDs, List<Variable> fieldDeclarations, String returnType){
		System.out.println("In generate");
		Map<String, String> variables = new HashMap<String, String>();
		for(String id: variableIDs){
			boolean found = false;
			for(Variable declVar: declaratedVariables){
				if(declVar.getID().equals(id)){
					variables.put(id, declVar.getType());
					found = true;
				}
			}
			
			if(!found){
				for(Variable fieldVar: fieldDeclarations){
					if(fieldVar.getID().equals(id)){
						found = true;
					}
				}
				if(!found){
					System.out.println("No type found! Default type \"int\" used! - Scraper.generate");
					variables.put(id,  "int");
				}
			}
		}
		
		System.out.println("after variables");
		//if(!s.trim().startsWith("publish")) return;
//		if(variables.keySet().isEmpty()) return;
		
		//TODO: generate main method to make the snippets executable
		
		String param = generateHead(variables);
		String methodName = "test";
		String functionType = getFunctionType(snippet, returnType);
		String function = "public " + functionType + " " + methodName + param + "{\n" + snippet + "}";
		try {
			System.out.println("Filepath: " + file);
			File temp = new File(file);
			if(temp.exists()) temp.delete();
			temp.createNewFile();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp)));
			bw.write(function);
			bw.flush();
			bw.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	private  String getFunctionType(String s, String returnType) {
		return s.contains("return") ? returnType : "void";
	}

	private  String generateHead(Map<String, String> variables) {
		String s = "(";
		if(variables.isEmpty()){
			s += ")";
		}else{
			for(String v : variables.keySet()){
				s = s  +  variables.get(v) + " "+ v + ", ";
			}
			s = s.substring(0, s.length() - 2) + ")";
		}
		return s;
	}

//	private  Map<String, String> getUndeclaredVariable(String s) {
//		Map<String, String> list = new HashMap<String, String>();
//		Map<String, String> declared = new HashMap<String, String>();
//		try{
//			InputStream stream = new ByteArrayInputStream(s.getBytes());
//			ANTLRInputStream input = new ANTLRInputStream(stream);
//			ScrapeLexer lexer = new ScrapeLexer(input);
//			CommonTokenStream tokens = new CommonTokenStream(lexer);
//			ScrapeParser parser = new ScrapeParser(tokens);
//			ProgContext prog = parser.prog();
//			List<StatContext> stats = prog.stat();
//			for(StatContext con : stats){
//				ParseTree child = con.getChild(0);
//				if(child instanceof DeclarationStatContext){
//					DeclarationStatContext decStat = (DeclarationStatContext) child;
//					declared.put(decStat.ID().getText(), decStat.type().getText());
//					//list.add(getTreeString(child) + ";");
//				}
//				else if(child instanceof AssignStatContext){
//					AssignStatContext assign = (AssignStatContext) child;
//					//list.add(getTreeString(child) + ";");
//					if(assign.type() == null){
//						String id = assign.ID().getText();
//						String type = speculateType(assign.assign_expression(), declared);
//						list.put(id, type);
//						update(assign.assign_expression(), list, declared, type);
//					}
//					else{
//						declared.put(assign.ID().getText(), assign.type().getText());
//						update(assign.assign_expression(), list, declared, assign.type().getText());
//					}
//					
//				}
//				else if(child instanceof If_statContext) {
//					
//					If_statContext ifstat = (If_statContext) child;
//					update(ifstat, list, declared, "int");
//					//list.add(getTreeString(child));
//				}
//				else if(child instanceof ReturnStatContext){
//					//update
////					this.returnType = "int";
//					ReturnStatContext returnStat = (ReturnStatContext) child;
//					updateOnReturn(returnStat.arith_expression(), list, declared, "int");
//					//list.add(getTreeString(child) + ";");
//				}
//				else{
//					return list;
//				}
//			}
//			
//			
//		}catch(Exception e){
//			e.printStackTrace();
//			return list;
//		}
//		return list;
//	}
//
//	private String speculateType(
//			Assign_expressionContext assign_expression, Map<String, String> declared) {
//		for(int i = 0; i < assign_expression.getChildCount(); i++){
//			ParseTree tree = assign_expression.getChild(i);
//			if(tree instanceof AtomContext){
//				AtomContext atom = (AtomContext) tree;
//				String id = atom.ID().getText();
//				if(declared.containsKey(id)) return declared.get(id);
//			}
//			else{
//				String temp = speculateType(tree, declared);
//				if(temp != null) return temp;
//			}
//		}
//		return "int";
//	}
//	// no if stat for sure, not statement for sure, as a complement method for speculateType(assign_expression)
//	private  String speculateType(ParseTree tree,
//			Map<String, String> declared) {
//		for(int i = 0; i < tree.getChildCount(); i++){
//			ParseTree child = tree.getChild(i);
//			if(child instanceof AtomContext){
//				AtomContext atom = (AtomContext) child;
//				if(atom.ID() == null) continue;
//				String id = atom.ID().getText();
//				if(declared.containsKey(id)) return declared.get(id);
//			}
//			else{
//				String temp = speculateType(child, declared);
//				if(temp != null) return temp;
//			}
//		}
//		return null;
//	}
//
//	private   void update(If_statContext ifstat, Map<String, String> list, Map<String, String> declared, String backType) {
//		for(int i = 0; i < ifstat.getChildCount(); i++){
//			ParseTree tree = ifstat.getChild(i);
//			if(tree instanceof AtomContext){
//				AtomContext atom = (AtomContext) tree;
//				if(atom.ID() == null) continue;
//				String id = atom.ID().getText();
//				if(!declared.containsKey(id)) list.put(id, backType);
//			}
//			else{
//				update(tree, list, declared, backType);
//			}
//		}
//		
//	}
//
//	private  void updateOnReturn(ParseTree parseTree,
//			Map<String, String> list, Map<String, String> declared, String backType) {
//		for(int i = 0; i < parseTree.getChildCount(); i++){
//			ParseTree tree = parseTree.getChild(i);
//			if(tree instanceof AtomContext){
//				AtomContext atom = (AtomContext) tree;
//				if(atom.ID() == null) continue;
//				String id = atom.ID().getText();
//				if(!declared.containsKey(id)) list.put(id, backType);
////				else this.returnType = declared.get(id);
//			}
//			else{
//				updateOnReturn(tree, list, declared, backType);
//			}
//		}
//		//return "in"
//		
//	}
//
//	//no statement for sure and no if block
//	private   void update(Assign_expressionContext assign_expression,
//			Map<String, String> list, Map<String, String> declared, String typeBack) {
//		for(int i = 0; i < assign_expression.getChildCount(); i++){
//			ParseTree tree = assign_expression.getChild(i);
//			if(tree instanceof AtomContext){
//				AtomContext atom = (AtomContext) tree;
//				if(atom.ID() == null) continue;
//				String id = atom.ID().getText();
//				if(!declared.containsKey(id)) list.put(id, typeBack);
//			}
//			else{
//				update(tree, list, declared, typeBack);
//			}
//		}
//		
//	}
//
//	//no statement and no if in this tree
//	private   void update(ParseTree tree, Map<String, String> list, Map<String, String> declared, String typeBack) {
//		for(int i = 0; i < tree.getChildCount(); i++){
//			ParseTree child = tree.getChild(i);
//			if(child instanceof AtomContext){
//				AtomContext atom = (AtomContext) child;
//				if(atom.ID() == null) continue;
//				String id = atom.ID().getText();
//				if(!declared.containsKey(id)) list.put(id, typeBack);
//			}
//			else if(child instanceof AssignStatContext){
//				AssignStatContext assign = (AssignStatContext) child;
//				//list.add(getTreeString(child) + ";");
//				if(assign.type() == null){
//					String type = speculateType(assign.assign_expression(), declared);
//					if(!declared.containsKey(assign.ID().getText())){
//					
//						list.put(assign.ID().getText(),type);
//					}
//					update(assign.assign_expression(), list, declared, type);
//				}
//				else{
//					declared.put(assign.ID().getText(), assign.type().getText());
//					update(assign.assign_expression(), list, declared, typeBack);
//				}
//			}
//			else if (child instanceof DeclarationStatContext){
//				DeclarationStatContext decl = (DeclarationStatContext) child;
//				declared.put(decl.ID().getText(), decl.type().getText());
//			}
//			else{
//				update(child, list, declared, typeBack);
//			}
//		}
//		
//	}

	public static void main(String[] args){
//		Scraper sc = new Scraper("./block");
		Scraper sc = new Scraper("./repository/myTest");

//		List<String> list = new Scraper("./bughunt/syllables/33").scrape("./bughunt/syllables/33/syllables.c");
//		for(String s : list){
//			System.out.println("------------");
//			System.out.println(s);
//		}
//		System.out.println(sc.scrape());
	}
}
