package ProcessIntroClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import Library.Pair;
import antlr.preprocess.JavaLexer;
import antlr.preprocess.JavaParser;
import antlr.preprocess.JavaParser.CompilationUnitContext;
import antlr.preprocess.MyJavaListener;


public class BugLineSearcher {
	private String folder;
	private String fileName;
	private List<String> linesContent;
	private Map<Integer, Double> suspiciousness;
	private int[] buggy;
	private boolean addBracket;
	private boolean hasPrintf;
	private boolean isWholeBlock;
	//private 

	public BugLineSearcher(String folder, String fileName) {
		super();
		this.folder = folder;
		this.fileName = fileName;
		this.linesContent = new ArrayList<String>();
		this.suspiciousness = new HashMap<Integer, Double>();
		this.buggy = new int[2];
		this.addBracket = false;
		this.hasPrintf = false;
		this.isWholeBlock = false;
		init();
	}
	
	

	public boolean isAddBracket() {
		return addBracket;
	}



	public void setAddBracket(boolean addBracket) {
		this.addBracket = addBracket;
	}



	private void init() {
		initSuspicious();
		initContent();
		
		if(this.linesContent.size() != this.suspiciousness.keySet().size()){
			System.out.println("BugLineSearcher init return!");
			return;
		}
		calculateBuggyANTLR();
//		calculateBuggy();
		System.out.println(buggy[0]);
		System.out.println(buggy[1]);
	}

	private void calculateBuggyANTLR() {
	    // Get our lexer
	    File file = new File(this.folder + "/" + this.fileName);

	    FileInputStream fis = null;
	    
	    // Open the input file stream
	    try {
			fis = new FileInputStream(file);

		    // Create a CharStream that reads from standard input
		    ANTLRInputStream input = new ANTLRInputStream(fis);
		    JavaLexer lexer = new JavaLexer(input);
	
		 
		    // Get a list of matched tokens
		    CommonTokenStream tokens = new CommonTokenStream(lexer);

		    // Pass the tokens to the parser
		    JavaParser parser = new JavaParser(tokens);
	
		 
		    // Specify our entry point
		    CompilationUnitContext context = parser.compilationUnit();
		 
		    // Walk it and attach our listener
		    ParseTreeWalker walker = new ParseTreeWalker();	
		    MyJavaListener listener = new MyJavaListener();
		    walker.walk(listener, context);
		    
		    //get Buggy lines
		    int lineNumber = getBigSupicious();
		    System.out.println("LineNumber: " + lineNumber);
		    Pair temp = listener.getMaxRange(lineNumber);
		    this.buggy[0] = temp.getLeft();
		    this.buggy[1] = temp.getRight();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    

	}



//	private void calculateBuggy() {
//		int lineNumber = getBigSupicious();
//		System.out.println("Big Suspicious: " + lineNumber);
//		initBuggyRange(lineNumber);
//		
//	}
	
	

	public int[] getBuggy() {
		return buggy;
	}


//	private void initBuggyRange(int lineNumber) {
//		String content = this.linesContent.get(lineNumber - 1);
//		if(isStatement(content)){
//			System.out.println("intiBuggyRange - isStatement? true");
//			if(checkISAllBlock(lineNumber)){
//				System.out.println("intiBuggyRange - checkISAllBlock? true");
//				this.buggy[0] = getLowerForStatement(lineNumber);
//				this.buggy[1] = getUpperForStatement(lineNumber);
//			}
//			else{
//				System.out.println("intiBuggyRange - checkISAllBlock? false");
//				this.buggy[0] = lineNumber;
//				this.buggy[1] = lineNumber;
//				checkBrackState(lineNumber);
//			}	
//		}
//		else{
//			System.out.println("intiBuggyRange - isStatement? false");
//			this.buggy[0] = getLower(lineNumber);
//			this.buggy[1] = getUpper(lineNumber);
//		}
//		checkPrintf(this.buggy[0], this.buggy[1]);
//	}

//	private int getUpperForStatement(int lineNumber) {
//		int temp = lineNumber;
//		lineNumber++;
//		String pre = this.linesContent.get(temp - 1).trim();
//		while(lineNumber <= this.linesContent.size()){
//			String s = this.linesContent.get(lineNumber-1).trim();
//			if(s.isEmpty()) {
//				lineNumber++;
//				continue;
//			}
//			if(s.startsWith("if")){
//				if(pre.endsWith("else")){
//					return getUpper(lineNumber);
//				}
//				else break;
//			}
//			else if(s.startsWith("else")){
//				return getUpper(lineNumber);
//			}
//			else {
//				lineNumber++;
//			}
//			pre = s;
//		}
//		return temp+1;
//		
//	}



//	private int getLowerForStatement(int lineNumber) {
//		return this.getLower(lineNumber);
//	}



//	private boolean checkISAllBlock(int lineNumber) {
//		int temp = lineNumber;
//		//lineNumber is bigger actual index
//		lineNumber--;
//		boolean begin = false;
//		if(this.linesContent.get(lineNumber).startsWith("{") || this.linesContent.get(lineNumber).startsWith("if") || this.linesContent.get(lineNumber).startsWith("else")) 
//			begin = true;
//		else{
//			lineNumber--;
//			while(lineNumber > 0 && this.linesContent.get(lineNumber).isEmpty()) lineNumber--;
//			if(this.linesContent.get(lineNumber).endsWith("{") || this.linesContent.get(lineNumber).startsWith("if") || this.linesContent.get(lineNumber).startsWith("else")) begin = true;
//		}
//		
//		return begin;
//	}



//	private void checkPrintf(int i, int j) {
//		for(int k = i; k <= j; k++){
//			if(this.linesContent.get(k-1).contains("printf(\"")){
//				this.hasPrintf = true;
//				return;
//			}
//		}
//		
//	}



//	private void checkBrackState(int lineNumber) {
//		while(lineNumber > 0){
//			String s = this.linesContent.get(lineNumber - 1).trim();
//			if(s.endsWith("{") || s.endsWith(";")){
//				this.addBracket = false;
//				break;
//			}
//			else if(s.startsWith("else") || s.startsWith("if")){
//				this.addBracket = true;
//				break;
//			}
//			lineNumber--;
//		}
//		
//	}

//	private int getUpper(int lineNumber) {
//		Stack<Character> stack = new Stack<Character>();
//		boolean finished = false;
//		while(lineNumber <= this.linesContent.size()){
//			String s = this.linesContent.get(lineNumber - 1).trim();
//			if(s.isEmpty()){
//				lineNumber++;
//				continue;
//			}
//			if(s.startsWith("else")){
//				System.out.println("BugLineSearcher GetUpper: StartsWithElse");
//				finished = false;
//			}
//			if(s.startsWith("if")){
//				System.out.println("BugLineSearcher GetUpper: StartsWithIf");
//				int lower = lineNumber - 1;
//				while(lower > 0){
//					if(!this.linesContent.get(lower - 1).trim().isEmpty()) break;
//					lower--;
//				}
//				if(lower == 0) return lineNumber - 1;
//				String top = this.linesContent.get(lower - 1).trim();
//				if(top.endsWith("else")){
//					finished = false;
//				}
//			}
//			if(stack.isEmpty() && !s.startsWith("else")) {
//				if(finished) return lineNumber - 1;
//				//gap++;
//			}
//			for(int i = 0; i < s.length(); i++){
//				if(s.charAt(i) == '{') stack.push('{');
//				else if(s.charAt(i) == '}'){
//					if(stack.isEmpty()) return lineNumber;
//					stack.pop();
//				}
//			}
//			if(stack.isEmpty() && (s.endsWith(";") || s.endsWith("}"))) finished = true;
//			lineNumber++;
//		}
//		return lineNumber - 1;
//	}

//	private int getLower(int lineNumber) {
//		//line
//		while(lineNumber > 0){
//			String s = this.linesContent.get(lineNumber - 1).trim();
//			int lower = lineNumber - 1;
//			while(lower > 0){
//				if(!this.linesContent.get(lower - 1).trim().isEmpty()) break;
//				lower--;
//			}
//			if(lower == 0) return 1;
//			String top = this.linesContent.get(lower - 1).trim();
//			if(s.startsWith("if") && !top.endsWith("else")) return lineNumber;
//			lineNumber = lower;
//		}
//		return lineNumber;
//	}

//	private boolean isStatement(String content) {
//		content = content.trim();
//		int index = content.indexOf('}');
//		if(index != -1) return false;
//		if(!content.startsWith("if") && !content.startsWith("else") && content.endsWith(";")) return true;
//		else return false;
//	}

	private int getBigSupicious() {
		int lineNumber = 1;
		double s = -1;
		
		for(int i = 0; i < this.suspiciousness.keySet().size(); i++){
			if(this.suspiciousness.get(i+1) > s) {
				lineNumber = i+1;
				s = this.suspiciousness.get(i+1);
			}
		}
		return lineNumber;
	}

	private void initContent() {
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.folder + "/" + this.fileName)));
			String s = null;
			while((s = br.readLine()) != null){
				this.linesContent.add(s.trim());
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private void initSuspicious() {
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.folder + "/suspicious")));
			String s = null;
			while((s = br.readLine()) != null){
				String[] info = s.split(" ");
				this.suspiciousness.put(Integer.parseInt(info[0]), Double.parseDouble(info[1]));
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		BugLineSearcher bug = new BugLineSearcher("./bughunt/median/40", "median_2c155667_000.java");
	}



	public boolean getHasPrintf() {
		return this.hasPrintf;
	}
	
	

}
