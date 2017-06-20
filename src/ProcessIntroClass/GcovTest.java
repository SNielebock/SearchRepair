package ProcessIntroClass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import Library.Utility;
import antlr.preprocess.JavaLexer;
import antlr.preprocess.JavaParser;
import antlr.preprocess.JavaParser.CompilationUnitContext;
import antlr.preprocess.MyJavaListener;

public class GcovTest {
	
	//test case folder
	private String folder;
	
	//the buggy source file in that folder
	private String fileName;
	
	private Map<String, String> positives;
	private Map<String, String> negatives;
	private Map<Integer, Integer> positiveExecutions;
	private Map<Integer, Integer> negativeExecutions;
	private Map<Integer, Double> suspiciousness;
	private boolean wb;

	public GcovTest(String folder, String fileName, boolean wb) {
		super();
		this.folder = folder;
//		System.out.println("GCov Test Folder: " + folder);
		this.fileName = fileName;
//		System.out.println("GCov Test FileName: " + fileName);
		this.wb = wb;
		this.positiveExecutions = new HashMap<Integer, Integer>();
		this.negativeExecutions = new HashMap<Integer, Integer>();
		this.positives = new HashMap<String, String>();
		this.negatives = new HashMap<String, String>();
		this.suspiciousness = new HashMap<Integer, Double>();
		initInputs();
		initExecutions();
		System.out.println("Finished suspiciousness calculation");
	}

	private void initExecutions() {
		if(!compile()) return;
		
		initPositiveExecutions();
		if(this.positiveExecutions.isEmpty()){
			return;
		}
		initNegativeExecutions();
		if(this.negativeExecutions.isEmpty()){
			return;
		}
		calculatesuspiciousness();
//		for(int num : this.suspiciousness.keySet()){
//			System.out.println("suspiciousness: " + num + " " + this.suspiciousness.get(num));
//		}
		recordSuspiciousness();
	}

	private void recordSuspiciousness() {
		String filePath = this.folder + "/suspicious";
		try{
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
			for(int num = 1;  num <= this.suspiciousness.keySet().size(); num++){
				bw.write(Integer.toString(num));
				bw.write(" ");
				bw.write(Double.toString(this.suspiciousness.get(num)));
				bw.write("\n");
			}
			bw.flush();
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private void calculatesuspiciousness() {
		int totalFail = this.negatives.size();
		for(int num : this.negativeExecutions.keySet()){
			
			if(num == 10){
				num = 10;
			}
			int failed = this.negativeExecutions.get(num);
			int success = this.positiveExecutions.get(num);
			int denom = totalFail * (failed + success);
			
			//one way
//			if(denom == 0) this.suspiciousness.put(num, 0.0);
//			else{
//				this.suspiciousness.put(num, failed * 1.0 / denom);
//			}
			
			//the other way
			double left = failed * 1.0 / totalFail;
			double right = failed * 1.0 / (failed + success);
//			System.out.println("CalculateSuspiciousness num: " + num + " failed: " + failed + " success: " + success + " totalFail: " + totalFail);
			if(denom == 0) this.suspiciousness.put(num, 0.0);
			else{
				this.suspiciousness.put(num, Math.sqrt(left * right));
			}
			
		}
	}

	private void initNegativeExecutions() {
		//All different for IntroClassJava due to f.e. median_2c155667_000
		String functionName = this.fileName.substring(0, this.fileName.lastIndexOf('.'));
		for(String input : this.negatives.keySet()){
			createPropertiesWithInput(input);
//	 		String jcovCommand = "ant -f ./jcov_searchRepair.xml";
	 		String coberturaCommand = "ant -f ./cobertura_searchRepair.xml";
			String coberturaString = Utility.runCProgram(coberturaCommand);
			System.out.println("COBERTURA STRING: " + coberturaString);
			
			try{
//				System.out.println("HTML FILE: " + this.folder + "/reports/cobertura-html/introclassJava." + functionName + ".html");
				String packages = Utility.getANTLRListener(this.folder + "/" + this.fileName).getPackageName();
				if(!packages.isEmpty()){
					packages += ".";
				}
				BufferedReader br = new BufferedReader(new FileReader(this.folder + "/reports/cobertura-html/" + packages + functionName + ".html"));
				
				//String HTMLRegex = "(.*?<td class=\"numLineCover\">&nbsp;)(\\d+)(<a name=\"src_\\d+\"></a>)?(</td>.*)";
				String HTMLCoveredRegex = "(.*?<tr>  <td class=\"numLineCover\">&nbsp;)(?<line>\\d+)(</td>  <td class=\"nbHits(Un)?[Cc]overed\">)(<a .*?>)?(&nbsp;)(?<executions>\\d+)(</a>)?(</td>.*)";
				Pattern pattern = Pattern.compile(HTMLCoveredRegex);

				String HTMLAllLinesRegex = "(.*?<tr>  <td class=\"numLine)(Cover|Uncover)?(\">&nbsp;)(?<line>\\d+)(</td>.*)";
				Pattern HTMLAllLinesPattern = Pattern.compile(HTMLAllLinesRegex);

			    String line;
			    int lastLine = 0;
			    while ((line = br.readLine()) != null) {
			    	if(line.matches(HTMLCoveredRegex)){
						Matcher matcher = pattern.matcher(line);
						while (matcher.find()) {
//						    System.out.println("Covered Line: " + matcher.group(2));
						    int lineNumber = Integer.parseInt(matcher.group("line"));
						    int executions = Integer.parseInt(matcher.group("executions"));
						    if(executions > 0){
								if(!this.negativeExecutions.containsKey(lineNumber)){
									//TODO: number of executions irrelevant due to Tarantula formel.(?)
									//this.negativeExecutions.put(lineNumber, executions);
									this.negativeExecutions.put(lineNumber, 1);
								}
								else{
									//TODO: number of executions irrelevant due to Tarantula formel.(?)
									//this.negativeExecutions.put(lineNumber, executions + this.negativeExecutions.get(lineNumber));
									this.negativeExecutions.put(lineNumber, 1 + this.negativeExecutions.get(lineNumber));
								}
						    }
						}
			    	}
			    	if(line.matches(HTMLAllLinesRegex)){
			    		Matcher matcher = HTMLAllLinesPattern.matcher(line);
			    		while (matcher.find()) {
			    			lastLine = Integer.parseInt(matcher.group("line"));
			    		}
			    	}
			    }
			    for(int i = 1; i <= lastLine; i++){
			    	if(!this.negativeExecutions.containsKey(i)){
						this.negativeExecutions.put(i, 0);
					}
			    }
			    br.close();
			} catch(FileNotFoundException e){
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
			
			
			
			
			
			
			//getRelevantLineNumbers();
			//remove gcda file
			/*String cleanCommand = "rm " + functionName + ".gcda";
			Utility.runCProgram(cleanCommand);
			System.out.println("GCOV INPUT:" + input);
			
			//unused
			String s = runWithUserInput("./a.out", input);
			//System.out.println(s);
			String gcovCommand = "gcov " + "./" + fileName;
			Utility.runCProgram(gcovCommand);
			String gcovFile = this.fileName + ".gcov";
			GcovFileParse parser = new GcovFileParse(gcovFile);
			for(int lineNumber : parser.getExecutions().keySet()){
				if(!this.negativeExecutions.containsKey(lineNumber)){
					this.negativeExecutions.put(lineNumber, parser.getExecutions().get(lineNumber));
				}
				else{
					this.negativeExecutions.put(lineNumber, parser.getExecutions().get(lineNumber) + this.negativeExecutions.get(lineNumber));
				}
			}*/
		}
		
	}
	
	
	private void initPositiveExecutions() {
		String functionName = this.fileName.substring(0, this.fileName.lastIndexOf('.'));
		for(String input : this.positives.keySet()){
			createPropertiesWithInput(input);
//	 		String jcovCommand = "ant -f ./jcov_searchRepair.xml";
	 		String coberturaCommand = "ant -f ./cobertura_searchRepair.xml";
			String coberturaString = Utility.runCProgram(coberturaCommand);
			System.out.println("COBERTURA STRING: " + coberturaString);
			
			try{
//				System.out.println("HTML FILE: " + this.folder + "/reports/cobertura-html/introclassJava." + functionName + ".html");
				String packages = Utility.getANTLRListener(this.folder + "/" + this.fileName).getPackageName();
				if(!packages.isEmpty()){
					packages += ".";
				}
				BufferedReader br = new BufferedReader(new FileReader(this.folder + "/reports/cobertura-html/" + packages + functionName + ".html"));
				
				//String HTMLRegex = "(.*?<td class=\"numLineCover\">&nbsp;)(\\d+)(<a name=\"src_\\d+\"></a>)?(</td>.*)";
				String HTMLCoveredRegex = "(.*?<tr>  <td class=\"numLineCover\">&nbsp;)(?<line>\\d+)(</td>  <td class=\"nbHits(Un)?[Cc]overed\">)(<a .*?>)?(&nbsp;)(?<executions>\\d+)(</a>)?(</td>.*)";
				Pattern HTMLCoveredPattern = Pattern.compile(HTMLCoveredRegex);
				
				String HTMLAllLinesRegex = "(.*?<tr>  <td class=\"numLine)(Cover|Uncover)?(\">&nbsp;)(?<line>\\d+)(</td>.*)";
				Pattern HTMLAllLinesPattern = Pattern.compile(HTMLAllLinesRegex);

			    String line;
			    int lastLine = 0;
			    while ((line = br.readLine()) != null) {
			    	if(line.matches(HTMLCoveredRegex)){
						Matcher matcher = HTMLCoveredPattern.matcher(line);
						while (matcher.find()) {
//						    System.out.println("Covered Line: " + matcher.group("line") + " executions: " + matcher.group("executions"));
						    int lineNumber = Integer.parseInt(matcher.group("line"));
						    int executions = Integer.parseInt(matcher.group("executions"));
						    if(executions > 0){
								if(!this.positiveExecutions.containsKey(lineNumber)){
									//TODO: number of executions irrelevant due to Tarantula formel.(?)
									//this.positiveExecutions.put(lineNumber, executions);
									this.positiveExecutions.put(lineNumber, 1);
								}
								else{
									//TODO: number of executions irrelevant due to Tarantula formel.(?)
									//this.positiveExecutions.put(lineNumber, executions + this.positiveExecutions.get(lineNumber));
									this.positiveExecutions.put(lineNumber, 1 + this.positiveExecutions.get(lineNumber));
								}
							}
						}
			    	}
			    	
			    	if(line.matches(HTMLAllLinesRegex)){
			    		Matcher matcher = HTMLAllLinesPattern.matcher(line);
			    		while (matcher.find()) {
			    			lastLine = Integer.parseInt(matcher.group("line"));
			    		}
			    	}
			    }
			    for(int i = 1; i <= lastLine; i++){
			    	if(!this.positiveExecutions.containsKey(i)){
						this.positiveExecutions.put(i, 0);
					}
			    }
			    br.close();
			} catch(FileNotFoundException e){
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
			
			
			
			//getRelevantLineNumbers();
			/*String cleanCommand = "rm " + functionName + ".gcda";
			Utility.runCProgram(cleanCommand);
			System.out.println("GCOV INPUT:" + input);
			String s = runWithUserInput("./a.out", input);
			//System.out.println(s);
			String gcovCommand = "gcov " + "./" + fileName;
			Utility.runCProgram(gcovCommand);
			String gcovFile = this.fileName + ".gcov";
			GcovFileParse parser = new GcovFileParse(gcovFile);
			for(int lineNumber : parser.getExecutions().keySet()){
				if(!this.positiveExecutions.containsKey(lineNumber)){
					this.positiveExecutions.put(lineNumber, parser.getExecutions().get(lineNumber));
				}
				else{
					this.positiveExecutions.put(lineNumber, parser.getExecutions().get(lineNumber) + this.positiveExecutions.get(lineNumber));
				}
			}*/
		}
		
	}
	

	private boolean compile() {
		File classes = new File(this.folder + "/classes");
		if(!classes.exists()){
			classes.mkdir();
		}
		
		String command = "javac -g -d " + folder + "/classes/ " + folder + "/" + fileName;
		String s = Utility.runCProgram(command);
		
		if(s.equals("failed")) {
			System.out.println("COMPILE FAILED!");
			return false;
		}
		return true;
	}
	

	private String runWithUserInput(String command, String input) {
		String out = "";
		String ls_str;
		StringBuffer sb = new StringBuffer();
		try {
			Process ls_proc = Runtime.getRuntime().exec(command);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ls_proc.getOutputStream()));
			writer.write(input);
			writer.flush();

			BufferedReader ls_in = new BufferedReader(new InputStreamReader(
					ls_proc.getInputStream()));
			BufferedReader ls_err = new BufferedReader(new InputStreamReader(
					ls_proc.getErrorStream()));

			long now = System.currentTimeMillis();
			long timeoutInMillis = 100L * 10; // timeout in seconds
			long finish = now + timeoutInMillis;

			try {
				while (Utility.isAlive(ls_proc)
						&& (System.currentTimeMillis() < finish)) {
					Thread.sleep(10);
				}
				if (Utility.isAlive(ls_proc)) {
					ls_proc.destroy();
					sb.append("");
				}
				while ((ls_str = ls_in.readLine()) != null) {
					sb.append(ls_str);
					// System.out.println(ls_str);
				}
				while((ls_str = ls_err.readLine()) != null){
					//System.out.println(ls_str);
					sb.append(ls_str);
				}

			} catch (IOException e) {
				out = "";
				// System.exit(0);
			} catch (Exception e) {
				out = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			out= "";
		}
		out = sb.toString();
		return out;
	}

	private void initInputs() {
		//obtainPositives
		initPositives();
		initNegatives();
//		for(String s : positives.keySet()){
//			System.out.println("input: " + s + " output: " + positives.get(s));
//		}
//		
//		for(String s : negatives.keySet()){
//			System.out.println("input: " + s + " output: " + negatives.get(s));
//		}
	}

	private void initNegatives() {
		
		if(wb){
			String dir = this.folder + "/whitebox/negative";
			initNegatives(dir);
		}else{
			String blackdir = this.folder + "/blackbox/negative";
			initNegatives(blackdir);
		}
	}

	private void initNegatives(String dir) {
		File directory = new File(dir);
		if(!directory.exists() || !directory.isDirectory()) return;
		//System.out.println(dir);
		for(File file : directory.listFiles()){
			String name = file.getAbsolutePath();
			if(name.endsWith(".in")){				
				String input = Utility.getStringFromFile(name);
				String outputFile = name.substring(0, name.length() - 3) + ".out";
				String output = Utility.getStringFromFile(outputFile);
				this.negatives.put(input, output);
			}
		}
	}

	private void initPositives() {
		//fetch from whitebox
		if(wb){
			String dir = this.folder + "/whitebox/positive";
			initPositives(dir);
		}
		else{
			String blackdir = this.folder + "/blackbox/positive";
			initPositives(blackdir);
		}
	}

	private void initPositives(String dir) {
		File directory = new File(dir);
		if(!directory.exists() || !directory.isDirectory()) return;
		for(File file : directory.listFiles()){
			String name = file.getAbsolutePath();
			if(name.endsWith(".in")){				
				String input = Utility.getStringFromFile(name);
				String outputFile = name.substring(0, name.length() - 3) + ".out";
				String output = Utility.getStringFromFile(outputFile);
				this.positives.put(input, output);
			}
		}
	}
	
	private void createPropertiesWithInput(String input) {
//      String defaultProperties = "default.build.properties";
//      String thisFileProperties = "jcov_searchRepair.build.properties";
		String defaultProperties = "coberturaDefault.build.properties";
		String thisFileProperties = "cobertura_searchRepair.build.properties";

      BufferedReader br = null;
      BufferedWriter bw = null;
      try {
         br = new BufferedReader(new FileReader(defaultProperties));
         bw = new BufferedWriter(new FileWriter(thisFileProperties));
         
         bw.write("root = " + folder + "\n");
         String functionName = this.fileName.substring(0, this.fileName.lastIndexOf('.'));
         
		String packages = Utility.getANTLRListener(this.folder + "/" + this.fileName).getPackageName();
		if(!packages.isEmpty()){
			packages += ".";
		}
			
         bw.write("fileName = " + packages + functionName + "\n");
         
         String inputNoLineBreak = input.replace(System.lineSeparator(), " ");
         bw.write("args = " + inputNoLineBreak + "\n");
         
         String line;
         while ((line = br.readLine()) != null) {
            bw.write(line+"\n");
         }
      } catch (Exception e) {
         return;
      } finally {
         try {
            if(br != null)
               br.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
         try {
            if(bw != null)
               bw.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
	

	public static void groupExperiment(String root, boolean wb){
		try{
			File dir = new File(root);
			for(String typeName : dir.list()){
				//TODO: Comment in?
//				if(typeName.equals("smallest")){
//					generate(root + "/smallest", "smallest.c", wb);
//				}
//				else if(typeName.equals("median")){
//					generate(root + "/median", "median.c", wb);
//				}
//				else if(typeName.equals("grade")){
//					generate(root + "/grade", "grade.c");
//				}
//				else if(typeName.equals("checksum")){
//					generate(root + "/checksum", "checksum.c");
//				}
//				else if(typeName.equals("digits")){
//					generate(root + "/digits", "digits.c");
//				}
//				if(typeName.equals("syllables")){
//					generate(root + "/syllables", "syllables.c");
//				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	private static void generate(String root, String fileName, boolean wb) {
		try{
			File dir = new File(root);
			for(File file : dir.listFiles()){
				if(file.isDirectory()){
					String path = file.getAbsolutePath();
					GcovTest test = new GcovTest(path, fileName, wb);
					//test.
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

	public static void main(String[] args){
//		GcovTest test = new GcovTest("./bughunt/myTest/0", "myTest.java", false);
		GcovTest test = new GcovTest("./bughunt/median/40", "median_2c155667_000.java", false);
		//test.createPropertiesWithInput("2 3 4");
		//groupExperiment("./bughunt");
	}

}
