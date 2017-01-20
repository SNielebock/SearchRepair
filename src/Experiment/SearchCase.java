package Experiment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import search.PrototypeSearch;
import search.ResultObject;
import search.ResultObject.ResultState;
import Library.Utility;
import antlr.preprocess.FunctionLexer;
import antlr.preprocess.FunctionParser;
import antlr.preprocess.JavaLexer;
import antlr.preprocess.JavaParser;
import antlr.preprocess.MyJavaListener;
import antlr.preprocess.FunctionParser.AssignStatContext;
//import antlr.preprocess.FunctionParser.BlockContext;
import antlr.preprocess.FunctionParser.DeclarationStatContext;
//import antlr.preprocess.FunctionParser.FormalParameterContext;
import antlr.preprocess.FunctionParser.FunctionContext;
import antlr.preprocess.FunctionParser.If_statContext;
import antlr.preprocess.FunctionParser.StatContext;
import antlr.preprocess.JavaParser.*;



/**
 * This class is used to store  information of one test case. The information includes the file name of test case, positive and negative i/o pairs, results
 * For every test case, it includes two files. One is "prefix_TS", which contains i/o pairs. The other one is "prefix.c", which is source file with buggy lines.
 * @author keyalin
 *
 */
public class SearchCase {
	
	public static final String MARKINPUT = "_yalin_mark(\"input\");";
	public static final String MARKOUTPUT = "_yalin_mark(\"output\");";
	public String outputType = "";
	
	private String casePrefix;
	private Map<String, String> positives;
	private Map<String, String> negatives;
	private int[] buggy;
	private CaseInfo info;
	private Map<String, String> verifications;
	private  String inputfile;
	private  String outputfile;
	private  String folder;
	private String functionName;
	private String tempOutput;
	private int repo;
	
	
	
	


	public SearchCase(String casePrefix, int repo) {
		this.casePrefix = casePrefix;
		this.positives = new HashMap<String, String>();
		this.negatives = new HashMap<String, String>();
		this.info = new CaseInfo();
		this.buggy = new int[2];
		this.verifications = new HashMap<String, String>();
		this.folder = this.casePrefix.substring(0, this.casePrefix.lastIndexOf("/"));
		this.functionName = this.casePrefix.substring(this.casePrefix.lastIndexOf("/") + 1);
		this.inputfile = this.folder + "/1.in";
		this.outputfile = this.folder + "/1.out";
		this.tempOutput = this.folder + "/test.out";
		this.repo = repo;
	}

	public void init() {
		EndToEndProcess();
		search();
	}
	
	private void EndToEndProcess(){
		String IOFileName = this.casePrefix + "_TS";
		parse(IOFileName);		
	}
	
	public void search(){
		boolean pass = fillSearchCase();
		if(!pass) return;
		searchOverRepository();
		printResult();
		ruleOutFalsePositive();
		printSearchingResult();
		
		if(isEmpty(info.getResult())) {
			this.info.getResult().setState(ResultState.FAILED);
			return;
		}
		else{
			if(!info.getResult().getPositive().isEmpty())
			{
				this.info.getResult().setState(ResultState.SUCCESS);
			}
			else{
				this.info.getResult().setState(ResultState.PARTIAL);
			}
		}
	}
	
	

	private boolean isEmpty(ResultObject result) {
		return info.getResult().getPartial().isEmpty() && info.getResult().getPositive().isEmpty();
	}

	private void printSearchingResult() {
		System.out.println("True fix:\n");
		for(String source : info.getResult().getPositive()){
			System.out.println(source);
		}
		
		System.out.println("not a fix:\n");
		for(String source : info.getResult().getFalsePositve()){
			System.out.println(source);
		}
		
		System.out.println("partial fix:\n");
		for(String source : info.getResult().getPartial().keySet()){
			System.out.println(source);
			System.out.println("success: " + info.getResult().getPartial().get(source));
		}
		
	}

	private void ruleOutFalsePositive() {
		for(String source : info.getResult().getSearchMapping().keySet()){
			for(Map<String, String> map : info.getResult().getSearchMapping().get(source)){
				try{
					//TODO: Commented out
//					System.out.println("IN HERE! (RULEOUTFALSEPOSITIVE): " + map.toString());
					String input = Restore.getMappingString(source, map);
					String outputFile = generateOutputFile(input);
					if(testAllResults(source, outputFile)){
						info.getResult().getMappingSource().put(source, input);
						int extraPass = this.passTestSuite(source, outputFile, this.verifications);
						this.info.getResult().getExtraPass().put(source, extraPass);
						break;
					}
					else continue;
				}catch(Exception e){
					System.out.println(e);
					continue;
				}
			}
			
		}
		
	}


		


	private boolean testAllResults(String source, String outputFile) {
		//TODO: Commented out
//		System.out.println("IN HERE! (TESTALLRESULTS)");
		boolean pass = passAllPositive(source, outputFile);
		if(!pass){
//			System.out.println("NOT PASS -> RETURNED FALSE");
			return false;
		}
		int count = passNegatives(source, outputFile);
		if(count == this.getNegatives().size()) {
			info.getResult().getPositive().add(source);
//			System.out.println("COUNT = PASSNEGATIVES -> RETURNED TRUE");
			return true;
		}
		else if(count == 0){
			info.getResult().getFalsePositve().add(source);
//			System.out.println("COUNT = 0 -> RETURNED FALSE");
			return false;
		}
		else {
			info.getResult().getPartial().put(source, count * 1.0 / this.getNegatives().size());
//			System.out.println("ELSE -> RETURNED TRUE");
			return true;
		}
	}



	private int passNegatives(String source, String outputFile) {
		return this.passTestSuite(source, outputFile, this.negatives);
	}

	
	//TODO: GCC...
	private int passTestSuite(String source, String outputFile, Map<String, String> suite){
		File file = new File( this.casePrefix);
		if(file.exists()) file.delete();
		String command1 = "gcc " + outputFile + " -o " + this.casePrefix;
		Utility.runCProgram(command1);
		if(!new File(this.casePrefix).exists()){
			return 0;
		}
		int count = 0;
		for(String input : suite.keySet()){
			String output = suite.get(input);
			
			String command2 = "./" + this.casePrefix;
			
			String s2 = Utility.runCProgramWithInput(command2, input);
			
			if(s2.isEmpty() ){
				continue;
			}
			
			if(checkPassForOneCase(s2, output, input)) count++;
		}
		return count;
	}
	
	


	private boolean checkPassForOneCase(String s2, String output, String input) {
		Utility.writeTOFile(this.tempOutput, s2);
		Utility.writeTOFile(this.outputfile, output);
		Utility.writeTOFile(this.inputfile, input);
		String s = Utility.runCProgramWithPythonCommand(this.functionName, this.tempOutput, this.inputfile, this.outputfile);
		if(s.trim().endsWith("Test passed.")) return true;
		else return false;
		
	}

	//TODO: GCC...
	private boolean passAllPositive(String source, String outputFile) {
		File file = new File( this.casePrefix);
		if(file.exists()) file.delete();
		String command1 = "gcc " + outputFile + " -o " + this.casePrefix;
		Utility.runCProgram(command1);
		if(!new File(this.casePrefix).exists()){
			return false;
		}
		for(String input : this.positives.keySet()){
			String output = this.positives.get(input);
			
			String command2 = "./" + this.casePrefix;
			
			String s2 = Utility.runCProgramWithInput(command2, input);
			//TODO: Commented out
//			System.out.println("S2 OUTPUT: " + s2);
			
			if(s2.isEmpty() ){
				return false;
			}
			if(!checkPassForOneCase(s2, output, input)) return false;
	
		}
		return true;
	}

	private String generateOutputFile(String input) {
		String outputfile = this.casePrefix + "new.c";
		try{
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputfile)));
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.casePrefix + ".c")));	
			String s = null;
			
			
			for(int i = 1; i < buggy[0]; i++){
				s = reader.readLine();
				writer.write(s);
				writer.write("\n");
				writer.flush();
			}
			
			writer.write(input);
			
			for(int i = buggy[0]; i <= buggy[1]; i++){
				s = reader.readLine();
				
			}
			
			while((s = reader.readLine()) != null){
				writer.write(s);
				writer.write("\n");
				writer.flush();
			}
			reader.close();
			writer.close();
		}catch(Exception e){
			return "";
		}
		return outputfile;
	}

	private void printResult() {
		System.out.println(this.casePrefix);
		int i = 0;
		for(String source : info.getResult().getSearchMapping().keySet())
		{
			
			i++;
			System.out.println("result" + i + "\n--------------------");
			System.out.println(source);

		}
		
	}



	private void searchOverRepository() {
		try {
			PrototypeSearch.search(info, repo);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	private boolean fillSearchCase() {
		System.out.println("---"+Arrays.toString(this.buggy));
		try{
			//TODO: Here testfile!
			System.out.println("THISCASEPREFIX: " + this.casePrefix + ".java");
			if(insertStateStatements(this.casePrefix + ".java")){
				System.out.println("SearchCase.fillSearchCase insertStateStatements true");
				obtainPositiveStates();
				return true;
			}
			else{
				System.out.println("SearchCase.fillSearchCase insertStateStatements false");
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return true;
		}
		
	}

	//TODO: Full of C crap
	private void obtainPositiveStates() {
		String sourceFile = this.casePrefix + "state.c";
		for(String input : this.positives.keySet()){
			File file = new File( this.casePrefix);
			if(file.exists()) file.delete();
			String command1 = "gcc " + sourceFile + " -o " + this.casePrefix;
			String command2 = "./" + this.casePrefix;
			String s1 = Utility.runCProgram(command1);
			if(s1.equals("failed")) continue;
			String s2 = Utility.runCProgramWithInput(command2, input);
			//TODO: Commented out
//			System.out.println("S2!: " + s2);
			if(s2.trim().isEmpty()) return;
			String[] entries = s2.split("_nextloop_");
			for(String entry : entries){
				int inputStart = entry.indexOf("inputStart:");
				int inputEnd = entry.indexOf("inputEnd");
				int outputStart = entry.indexOf("outputStart:");
				//int outputEnd = s2.indexOf("outputEnd");
				if(inputStart == - 1) continue;
				if(outputStart == - 1) continue;
				
				List<String> inputList = new ArrayList<String>();
				List<String> outputList = new ArrayList<String>();
				
	
				String[] elems = entry.substring(inputStart + 11, inputEnd).split("_VBC_");
				for(String e : elems){
					if(e.equals("")) continue;
					inputList.add(e);				
				}
				for(String o : entry.substring(outputStart + 12).split("_VBC_")){
					if(o.equals("")) continue;
					outputList.add(o);
				}
				//TODO: Commented out
//				System.out.println("info.getpositives: " + info.getPositives().toString());
				info.getPositives().put(inputList, outputList);
//				System.out.println("inputList: " + inputList.toString());
//				System.out.println("outputList: " + outputList.toString());
//				System.out.println("info.getpositives: " + info.getPositives().toString());
			}
		}

		
	}

	/*
	 * 
	 * firstly, erase all of include statements and insert Mark, make a copy in prefix.mark
	 * get the target function using FuncitionExtractor, the entire function String
	 * Using state to obtain input and output variables and its types
	 * make a copy prefix_copy.c of original source file, and insert input and put statements
	 * @return
	 */
	private boolean insertStateStatements(String original) {
		System.out.println("Original: " + original);
		String markFile = insertMark(original);
		
		System.out.println("MARKFILE: " + markFile);
		String target = getFunction(markFile);
//old:	String[] states = getStatesStatement(target);
		String[] states = getStatesStatement(original);
		if(states == null) return false;
		writeStatesStatement(states);
		System.out.println("returned true(SearchCase.insertStateStatements)");
		return true;

		
	}

	private String writeStatesStatement(String[] states) {
		String fileName = this.casePrefix + "state.c";
		System.out.println("FILENAME writeStatesStatement: " + fileName);
		try{
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.casePrefix + ".c")));
			String s = null;
			
			
			for(int i = 1; i < buggy[0]; i++){
				s = reader.readLine();
				writer.write(s);
				writer.write("\n");
				writer.flush();
			}
			
			writer.write(states[0]);
			writer.write("\n");
			writer.flush();
			
			for(int i = buggy[0]; i <= buggy[1]; i++){
				s = reader.readLine();
				writer.write(s);
				writer.write("\n");
				writer.flush();
			}
			writer.write(states[1]);
			writer.write("\n");
			writer.flush();
			
			while((s = reader.readLine()) != null){
				writer.write(s);
				writer.write("\n");
				writer.flush();
			}
			reader.close();
			writer.close();
		}catch(Exception e){
			return "";
		}
		return fileName;
	}

	private String[] getStatesStatement(String target) {
		String[] states = null;
		
		Map<String, String> variables = new HashMap<String, String>();
		try{
//			InputStream stream = new ByteArrayInputStream(target.getBytes());
//			ANTLRInputStream input = new ANTLRInputStream(stream);
//			FunctionLexer lexer = new FunctionLexer(input);
//			CommonTokenStream tokens = new CommonTokenStream(lexer);
//			FunctionParser parser = new FunctionParser(tokens);
			System.out.println("GETSTATESSTATEMENT TARGET: " + target);

		    File file = new File(target);
		    FileInputStream fis = new FileInputStream(file);

		    ANTLRInputStream input = new ANTLRInputStream(fis);
//			InputStream stream = new ByteArrayInputStream(target.getBytes());
//		    ANTLRInputStream input = new ANTLRInputStream(stream);
		    JavaLexer lexer = new JavaLexer(input);
		    CommonTokenStream tokens = new CommonTokenStream(lexer);
		    JavaParser parser = new JavaParser(tokens);
		    CompilationUnitContext context = parser.compilationUnit();
		    ParseTreeWalker walker = new ParseTreeWalker();	
		    MyJavaListener listener = new MyJavaListener();
		    walker.walk(listener, context);
		    MethodDeclarationContext method = listener.getSpecificMethodContext(this.buggy[0]);
			System.out.println("Target: " + target);
			
			if(!method.isEmpty()){
				System.out.println("Method not empty! " + method.getText());
			}

//old:		getStatesVariables(parser.prog().function(), variables);
			getStatesVariables(method, variables);
			//stream.close();
			System.out.println("Test1");
		}catch(Exception e){
			System.out.println("Test5");
			e.printStackTrace();
			return null;
		}
		if(variables.isEmpty()){
			System.out.println("Test4");
			return null;
		}
		System.out.println("Test2");
		states = configureStatStatment(variables);
		System.out.println("Test3");
		return states;
	}

	private String[] configureStatStatment(Map<String, String> variables) {
		String[] states = new String[2];
		String inputbegin = "printf(\"inputStart:";
		String inputend = "";
		String outputbegin = "printf(\"outputStart:";
		String outputend = "";
		
		for(String id : variables.keySet()){
			String type = variables.get(id);
			if(type.equals("int")){
				String begin = id + ":%d:int_VBC_";
				String end = id + ", ";
				inputbegin += begin;
				inputend += end;
				outputbegin += begin;
				outputend += end;
			}
			else if(type.equals("char")){
				String begin = id + ":%d:char_VBC_";
				String end = id + ", ";
				inputbegin += begin;
				inputend += end;
				outputbegin += begin;
				outputend += end;
			}
			else if(type.equals("float") || type.equals("double")){
				String begin = id + ":%f:float_VBC_";
				String end = id + ", ";
				inputbegin += begin;
				inputend += end;
				outputbegin += begin;
				outputend += end;
			}
			else if(type.equals("char*")){
				String begin = id + ":%s:char*_VBC_";
				String end = id + ", ";
				inputbegin += begin;
				inputend += end;
				outputbegin += begin;
				outputend += end;
			}
		}
		System.out.println(inputend);
		states[0] = inputbegin.subSequence(0, inputbegin.length()) + "inputEnd\", " + inputend.substring(0, inputend.length() - 2) + ");";
		states[1] = outputbegin.subSequence(0, outputbegin.length()) + "_nextloop_\", " + outputend.substring(0, outputend.length() - 2) + ");";
		return states;
	}

	//changed parameters! originally: FunctionContext function, Map<String, String> variables
	private void getStatesVariables(MethodDeclarationContext method,
			Map<String, String> variables) {
		// formals
//old:	List<FormalParameterContext> fpc = function.parameters().formalParameter();
		List<FormalParameterContext> fpc = null;
		if(!method.formalParameters().getText().equals("()")){
			fpc = method.formalParameters().formalParameterList().formalParameter();
		}
		if(method.getText().startsWith("void")){
			this.outputType = "void";
		}else{
			this.outputType = method.typeType().getText().trim();
		}
		if(!(fpc==null)){
			for(FormalParameterContext fp : fpc){
				String type = fp.typeType().getText();
				String id = fp.variableDeclaratorId().getText();
				System.out.println("GetStatesVariables Type: " + type + " ID: " + id);
				variables.put(id, type);
			}
		}
		
		Map<String, String> local = getBlockVariable(method.methodBody().block());
		for(String s : local.keySet()){
			variables.put(s, local.get(s));
		}
		
	}

	private boolean find = false;
	
	//TODO: I don't understand this method. If I am correct, then local == variables and that means that this for loop is just unnecessary
	private void add(If_statContext ifstat, Map<String, String> variables) {
//		for(BlockContext block : ifstat.block()){
//			Map<String, String> local = getBlockVariable(block);
//			if(find){
//				for(String s : local.keySet()){
//					variables.put(s, local.get(s));
//				}
//				break;
//			}
//			else continue;
//		}
//		
	}



	private Map<String, String> getBlockVariable(BlockContext block) {
		Map<String, String> variables = new HashMap<String, String>();
		if(find) return variables;
		for(BlockStatementContext blockStateCon : block.blockStatement()){
			//System.out.println(statCon.getText());
			if(blockStateCon.start.getLine() >= this.buggy[0]){
				find = true;
				break;
			}
			ParseTree child = blockStateCon.getChild(0);
			if(child instanceof LocalVariableDeclarationStatementContext){
				LocalVariableDeclarationStatementContext decl = (LocalVariableDeclarationStatementContext) child;
				add(decl, variables);
			}
			else if(child instanceof StatementExpressionContext){
				StatementExpressionContext assign = (StatementExpressionContext) child;
				add(assign, variables);
			}
//			else if(child instanceof If_statContext) {
//				If_statContext ifstat = (If_statContext) child;
//				add(ifstat, variables);
//			}
		}
		return variables;
	}

	//TODO: Fix assigns!
	private void add(StatementExpressionContext assign, Map<String, String> variables) {
//		if(assign.type() == null) return;
//		String type = assign.type().getText();
//		String id  = assign.ID().getText();
//		variables.put(id, type);
		
	}

	private void add(LocalVariableDeclarationStatementContext decl, Map<String, String> variables) {
		String type = decl.localVariableDeclaration().typeType().getText();
		//TODO: How to handle Arrays?
		if(decl.getText().contains("[") || decl.getText().contains("*")){
			type = type + '*';
		}
		for(int i = 0; i < decl.localVariableDeclaration().variableDeclarators().variableDeclarator().size(); i++)
		{
			variables.put(decl.localVariableDeclaration().variableDeclarators().variableDeclarator().get(i).variableDeclaratorId().getText(), type);
		}
	}

	/**
	 * extract the target function, whose name is the same as casePrefix
	 * TODO: Picks only the last class in file! And there is no comparison with the casePrefix String
	 * @param markFile
	 * @return
	 */
	private String getFunction(String markFile) {
		String output = "";
		try{
			String fileString = Utility.getStringFromFile(markFile);
			//System.out.println(fileString);
			int start = -1;
			int end = -1;
			Stack<Character> stack = new Stack<Character>();
			Stack<Integer> index = new Stack<Integer>();
			for(int i = 0; i < fileString.length(); i++)
			{
				char c = fileString.charAt(i);
				if(c == '{' ){
					stack.add(c);
					index.push(i);
				}
				else if(c == '}'){
					stack.pop();
					int temp = index.pop();
					if(stack.isEmpty()){
						start = temp;
						end = i;
					}				
				}
			}
			
			start = fileString.substring(0, start - 1).lastIndexOf('}');
			output = fileString.substring(start + 1, end + 1);
			System.out.println("SearchCase.getFunction: " + output);
			
		}catch(Exception e){
			System.out.println(e);
			return "";
		}
		return output;
	}

	/**
	 * insert mark, and erase include statement, write the content into this.casePrefix.mark
	 * @param original
	 * @return
	 */
	private String insertMark(String original) {
		String output = this.casePrefix + ".mark";
		try{
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
//			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.casePrefix + ".c")));
			//TODO: just testing
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.casePrefix + ".java")));
			String s = null;
			
			for(int i = 0; i < buggy.length; i++){
				System.out.println("Buggy " + i + ": " + buggy[i]);
			}
			
			for(int i = 1; i < buggy[0]; i++){
				s = reader.readLine();
				if(s.trim().startsWith("#")) continue;
				//if(!find && !s.contains(function)) continue;
				//find = true;
				writer.write(s);
				writer.write("\n");
				writer.flush();
			}
			
			writer.write(SearchCase.MARKINPUT);
			writer.write("\n");
			writer.flush();
			
			for(int i = buggy[0]; i <= buggy[1]; i++){
				s = reader.readLine();
				writer.write(s);
				writer.write("\n");
				writer.flush();
			}
			writer.write(SearchCase.MARKOUTPUT);
			writer.write("\n");
			writer.flush();
			
			while((s = reader.readLine()) != null){
				writer.write(s);
				writer.write("\n");
				writer.flush();
			}
			reader.close();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		return output;
	}

	
	/**
	 * get input/output pairs, and buggy lines info
	 * @param caseFile
	 */
	private void parse(String caseFile) {
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(caseFile)));
			String line = null;
			boolean neg = false;
			while((line = br.readLine()) != null){
				line = line.trim();
				if(line.startsWith("positive:")){
					neg = false;
				}
				else if(line.startsWith("negative:")){
					neg = true;
				}
				else if(line.startsWith("buggy lines:")){
					String[] lines = line.substring(12).split("-");
					buggy[0] = Integer.valueOf(lines[0]);
					buggy[1] = Integer.valueOf(lines[1]);
				}
				else if(line.startsWith("input:")){
					int index = line.indexOf("output:");
					String input = line.substring(6, index);
					String output = line.substring(index + 7);
					if(neg){
						this.negatives.put(input.trim(), output.trim());
					}
					else{
						this.positives.put(input.trim(), output.trim());
					}
				}
				else{
					continue;
				}
			}
			br.close();
		}catch(Exception e){
			return;
		}
		
	}

	public String getCasePrefix() {
		return casePrefix;
	}

	public void setCasePrefix(String casePrefix) {
		this.casePrefix = casePrefix;
	}

	public Map<String, String> getPositives() {
		return positives;
	}

	public void setPositives(Map<String, String> positives) {
		this.positives = positives;
	}

	public Map<String, String> getNegatives() {
		return negatives;
	}

	public void setNegatives(Map<String, String> negatives) {
		this.negatives = negatives;
	}

	

	public CaseInfo getInfo() {
		return info;
	}

	public void setInfo(CaseInfo info) {
		this.info = info;
	}

	public int[] getBuggy() {
		return buggy;
	}

	public void setBuggy(int[] buggy) {
		this.buggy = buggy;
	}
	
	
	
	
	
	public Map<String, String> getVerifications() {
		return verifications;
	}

	public void setVerifications(Map<String, String> verifications) {
		this.verifications = verifications;
	}

	public static void main(String[] args){
		SearchCase case1 = new SearchCase("TestCases/examples/test1", 2);
		//case1.print();
	}

	public void searchJustOnMap() {
		try{
			info.setResult(new ResultObject());
			PrototypeSearch.searchOnlyMatchType(info, repo);
			this.printResult();
			this.ruleOutFalsePositive();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
