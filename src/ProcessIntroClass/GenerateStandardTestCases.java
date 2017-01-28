package ProcessIntroClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Library.Utility;

/**
 * This Class copies the faulty C-functions from IntroClass to bughunt and runs the tests to find out,
 * which ones are positive and which ones are negative.
 *
 */
public class GenerateStandardTestCases {
	private static int count = 0;
	
	private String introPath;
	private String outputFolderPath;
	public GenerateStandardTestCases(String introPath, String outputFolderPath) {
		this.introPath = introPath;
		this.outputFolderPath = outputFolderPath;
		new File(outputFolderPath).mkdir();
		this.list = new ArrayList<String>();
	}
	private List<String> list;
	
	public void printFailed(){
		System.out.println(list);
	}
	
	/**
	 * this method calls the {@link GenerateStandardTestCases#generate(String, String)} method with parameters
	 * pointing to files that shall be repaired.
	 * 
	 * tip: comment out unnecessary folders, to safe some time.
	 */
	public void generate(){
		try{
			File dir = new File(introPath);
			System.out.println(dir.getAbsolutePath());
			for(String typeName : dir.list()){
				if(typeName.equals("smallest")){
//					generate(introPath + "/smallest", outputFolderPath + "/smallest");
				}
				if(typeName.equals("median")){
					generate(introPath + "/median", outputFolderPath + "/median");
				}
				if(typeName.equals("grade")){
//					generate(introPath + "/grade", outputFolderPath + "/grade");
				}
				if(typeName.equals("checksum")){
//					generate(introPath + "/checksum", outputFolderPath + "/checksum");
				}
				if(typeName.equals("digits")){
//					generate(introPath + "/digits", outputFolderPath + "/digits");
				}
				if(typeName.equals("syllables")){
//					generate(introPath + "/syllables", outputFolderPath + "/syllables");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * this method finds all folders that are two levels deeper, than the input folder and calls {@link GenerateStandardTestCases#init(File, File, String)}
	 * on each of those.
	 * 
	 * example: /home/matthias/git/IntroClass-master/median/[...]/exampleFolder1
	 * 
	 * @param inputFolder 	folder that contains the collection of the "to-be-repaired"-files
	 * @param outputFolder 	will be used in {@link GenerateStandardTestCases#init(File, File, String)} as second parameter
	 * 					   	with enumerated sub folders added to the specified output folder
	 */
	private void generate(String inputFolder, String outputFolder) {
		String functionName = inputFolder.substring(inputFolder.lastIndexOf("/") + 1);
		File outputFolderFile = new File(outputFolder);
		outputFolderFile.mkdir();
		try{
			int depth = 0;
			File file = new File(inputFolder);
			List<File> queue = new ArrayList<File>();
			queue.add(file);
			
			//get all folders that are two levels deeper than the inputfolder
			while(!queue.isEmpty() && depth != 2){
				List<File> list = new ArrayList<File>();
				for(int i = 0; i < queue.size();i++){
					File temp = queue.get(i);
					if(temp.getName().equals("tests")) continue;
					if(temp.isDirectory()){
						for(File f : temp.listFiles()){
							if(f.isDirectory()) list.add(f);
						}
					}
				}
				queue = list;
				depth++;
			}
			if(depth != 2) return;
			
			int count = 0;
			//call init on each folder in queue
			for(File temp : queue){
				File caseFolder = new File(outputFolderFile.getAbsolutePath() + "/" + count++);
				//TODO: only for testing!
				if(count == 40){
					init(temp, caseFolder, functionName);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * <p>calls the following functions:</p>
	 * <p>{@link Utility#copy(String, String)}</p>
	 * <p>{@link Utility#writeTOFile(String, String)}</p>
	 * <p>{@link GenerateStandardTestCases#generateWhiteAndBlack(String, String, String)}</p>
	 * <p>{@link GenerateStandardTestCases#getOtherTechInfo(String, String)}</p>
	 * 
	 * @param temp 			current folder
	 * @param caseFolder 	output folder
	 * @param functionName 	current Function
	 */
	private void init(File temp, File caseFolder, String functionName) {
		String inputFolder = temp.getAbsolutePath() + "/src/main/java/introclassJava";
		String outputFolder = caseFolder.getAbsolutePath();
		
//		inputFolder = "/home/matthias/git/SearchRepair/TestCases/MedianJavaTest";
		new File(outputFolder).mkdir();
		System.out.println(inputFolder + "\n" + outputFolder);
	
		File inputDirectory = new File(inputFolder);
		for(File tmpFile: inputDirectory.listFiles()){
			if(tmpFile.getName().endsWith(".java")){
				System.out.println("TEMPFILE GET NAME: " + tmpFile.getName());
				
				Utility.copy(inputFolder + "/" + tmpFile.getName(), outputFolder + "/" + tmpFile.getName());
				Utility.writeTOFile(outputFolder + "/original", inputFolder);
				generateWhiteAndBlack(outputFolder, inputFolder, tmpFile.getName());
				
				
//				Utility.copy(inputFolder + "/" + functionName + ".java", outputFolder + "/" + functionName + ".java");
//				Utility.writeTOFile(outputFolder + "/original", inputFolder);
//				generateWhiteAndBlack(outputFolder, inputFolder, functionName + ".java");
			}
		}
		
		
		getOtherTechInfo(inputFolder, outputFolder);
	}
	
	/**
	 * writes those files to outputFolder/repair that indicate, whether the other automatic repair programs
	 * (GenProg, TrpAutoRepair, AE) could repair this case or not.
	 * 
	 * @param inputFolder 	path to the log-files
	 * @param outputFolder 	./repair is the output locations
	 * 
	 * @see Utility#writeTOFile(String, String)
	 */
	private void getOtherTechInfo(String inputFolder, String outputFolder) {
		new File(outputFolder + "/repair").mkdir();
		File dir = new File(inputFolder);
		boolean findGP = false;
		for(File file : dir.listFiles()){
			String name = file.getName();
			if(name.contains("gp") && name.contains("bb")) {
				findGP = true;
			}
			
		
			if(name.endsWith("log") && name.startsWith("gp")){
				String fileString = Utility.getStringFromFile(file.getAbsolutePath());
				if(fileString.contains("Repair Found") || fileString.contains("repair found")){
					if(name.contains("wb"))
					{
						Utility.writeTOFile(outputFolder + "/repair/gp-wb", "success");
					}
					else if(name.contains("bb")){
						Utility.writeTOFile(outputFolder + "/repair/gp-bb", "success");
					}
				}
			}
			else if(name.endsWith("log") && name.startsWith("ae")){
				String fileString = Utility.getStringFromFile(file.getAbsolutePath());
				if(fileString.contains("Repair Found") || fileString.contains("repair found")){
					if(name.contains("wb"))
					{
						Utility.writeTOFile(outputFolder + "/repair/ae-wb", "success");
					}
					else if(name.contains("bb")){
						Utility.writeTOFile(outputFolder + "/repair/ae-bb", "success");
					}
				}
			}
			else if(name.endsWith("log") && name.startsWith("trp")){
				String fileString = Utility.getStringFromFile(file.getAbsolutePath());
				if(fileString.contains("Repair Found") || fileString.contains("repair found")){
					if(name.contains("-wb-"))
					{
						Utility.writeTOFile(outputFolder + "/repair/tsp-wb", "success");
					}
					else if(name.contains("-bb-")){
						Utility.writeTOFile(outputFolder + "/repair/tsp-bb", "success");
					}
				}
			}
		}
		if(findGP)Utility.writeTOFile(outputFolder+ "/repair/bbdefect", "bbdefect");
		
	}

	/**
	 * <p>create outputfolder/(whitebox|balckbox)[/(positive|negative)] folders;</p>
	 * <p>calls {@link Utility#runCProgram(String)}</p>
	 * <p>calls {@link GenerateStandardTestCases#initWhiteBox(String, String, String, String)}</p>
	 * <p>calls {@link GenerateStandardTestCases#initBlackBox(String, String, String, String)}</p>
	 * 
	 * @param outputFolder 	self-explanatory
	 * @param inputFolder 	self-explanatory
	 * @param fileName 		self-explanatory
	 */
	private void generateWhiteAndBlack(String outputFolder, String inputFolder, String fileName) {
		//TODO: works for now, but is not very beautiful
		String whiteboxPath = inputFolder + "/../../../../../../tests/whitebox";
		String blackboxPath = inputFolder + "/../../../../../../tests/blackbox";
		
		new File(outputFolder + "/whitebox").mkdir();
		new File(outputFolder + "/whitebox/positive").mkdir();
		new File(outputFolder + "/whitebox/negative").mkdir();
		new File(outputFolder + "/blackbox").mkdir();
		new File(outputFolder + "/blackbox/positive").mkdir();
		new File(outputFolder + "/blackbox/negative").mkdir();
		try{
			//TODO: introclassJava...
			String testingExe =  "introclassJava." + fileName.substring(0, fileName.lastIndexOf("."));
			System.out.println("TestingExeString: " + testingExe);
			
			
			//TODO: doesn't work with current testingExe String
			if(new File(testingExe).exists()) new File(testingExe).delete();
			
//			String s = Utility.runCProgram("gcc -o " + testingExe + " " + inputFolder + '/'+fileName);
			System.out.println("FILENAME: " + inputFolder + '/' + fileName);
			String s = Utility.runCProgram("javac -d . " + inputFolder + '/' + fileName);
			System.out.println("Output Javac Compile Process: " + s);

			if(s.equals("failed")) {
				list.add(s);
				return;
			}
			initWhiteBox(whiteboxPath, inputFolder, outputFolder, testingExe);
			initBlackBox(blackboxPath, inputFolder, outputFolder, testingExe);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * <p>Take a path to a file that ends with ".in" and is in blackboxPath. </p>
	 * <p>call {@link Utility#getStringFromFile1(String)} with that path.</p>
	 * <p>Get the fitting ".out"-file.</p>
	 * <p>call {@link Utility#runCProgramWithInput(String, String)}</p>
	 * <p>write with {@link Utility#writeTOFile(String, String)} resulting String to "./tempFolder/test.out"</p>
	 * <p>call {@link Utility#runCProgramWithPythonCommand(String, String, String, String)}</p>
	 * <p>if result String is "Test passed." put ".in"- and ".out"-files with {@link Utility#copy(String, String)} in "/blackbox/positive" else in "blackbox/negative".</p>
	 * 
	 * 
	 * @param blackboxPath 	self-explanatory
	 * @param inputFolder	self-explanatory
	 * @param outputFolder	self-explanatory
	 * @param testingExe	Executable file, created by {@link Utility#runCProgram(String)} in {@link GenerateStandardTestCases#generateWhiteAndBlack(String, String, String)}.
	 */
	private void initBlackBox(String blackboxPath, String inputFolder,
			String outputFolder, String testingExe) {
		for(File file : new File(blackboxPath).listFiles()){
			String path = file.getAbsolutePath();
			if(path.endsWith(".in")){
				String input = Utility.getStringFromFile1(path);
				System.out.println("Utility.initBlackBox Input: " + input);
				String outPath = path.substring(0, path.length() - 3) + ".out";
				String runOutput = Utility.runCProgramWithInput("java " + testingExe, input);
				String tempOuputFile = "./tempFolder/test.out";
				Utility.writeTOFile(tempOuputFile, runOutput);
				
//				String s = Utility.runCProgramWithPythonCommand(testingExe, tempOuputFile, path, outPath).trim();
				
				//TestingExe = introclassJava.median_cd2d9b5b_010
				String className = testingExe.substring(testingExe.lastIndexOf(".") + 1);
				String s = "";
				if(className.startsWith("median")){
					s = Utility.runCProgramWithPythonCommand("median", tempOuputFile, path, outPath).trim();
				}else if(className.startsWith("checksum")){
					s = Utility.runCProgramWithPythonCommand("checksum", tempOuputFile, path, outPath).trim();
				}else if(className.startsWith("digits")){
					s = Utility.runCProgramWithPythonCommand("digits", tempOuputFile, path, outPath).trim();
				}else if(className.startsWith("grade")){
					s = Utility.runCProgramWithPythonCommand("grade", tempOuputFile, path, outPath).trim();
				}else if(className.startsWith("smallest")){
					s = Utility.runCProgramWithPythonCommand("smallest", tempOuputFile, path, outPath).trim();
				}else if(className.startsWith("syllables")){
					s = Utility.runCProgramWithPythonCommand("syllables", tempOuputFile, path, outPath).trim();
				}
				System.out.println("Utility.initBlackBox Python output: " + s);
				if(s.equals("Test passed.")){
					String index = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
					Utility.copy(path, outputFolder + "/blackbox/positive/" + index + ".in");
					Utility.copy(outPath, outputFolder + "/blackbox/positive/" + index + ".out");
				}
				else{
					String index = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
					Utility.copy(path, outputFolder + "/blackbox/negative/" + index + ".in");
					Utility.copy(outPath, outputFolder + "/blackbox/negative/" + index + ".out");
				}
			}
		}		
	}
	

	/**
	 * Same as {@link GenerateStandardTestCases#initBlackBox(String, String, String, String)}, with the difference
	 * that this method's output folder the argument outputFolder + "/whitebox/(positive|negative)" is.
	 * 
	 * @param whiteboxPath 	self-explanatory
	 * @param inputFolder 	self-explanatory
	 * @param outputFolder 	self-explanatory
	 * @param testingExe 	Executable file, created by {@link Utility#runCProgram(String)} in {@link GenerateStandardTestCases#generateWhiteAndBlack(String, String, String)}.
	 */
	private void initWhiteBox(String whiteboxPath, String inputFolder,
			String outputFolder, String testingExe) {
		for(File file : new File(whiteboxPath).listFiles()){
			String path = file.getAbsolutePath();
			if(path.endsWith(".in")){
				String input = Utility.getStringFromFile1(path);
				String outPath = path.substring(0, path.length() - 3) + ".out";
				String runOutput = Utility.runCProgramWithInput("java " + testingExe, input);
				String tempOuputFile = "./tempFolder/test.out";
				Utility.writeTOFile(tempOuputFile, runOutput);
				
//				String s = Utility.runCProgramWithPythonCommand(testingExe, tempOuputFile, path, outPath).trim();
				
				String className = testingExe.substring(testingExe.lastIndexOf(".") + 1);
				System.out.println("CLASSNAME: " + className);
				String s = "";
				if(className.startsWith("median")){
					s = Utility.runCProgramWithPythonCommand("median", tempOuputFile, path, outPath).trim();
				}else if(className.startsWith("checksum")){
					s = Utility.runCProgramWithPythonCommand("checksum", tempOuputFile, path, outPath).trim();
				}else if(className.startsWith("digits")){
					s = Utility.runCProgramWithPythonCommand("digits", tempOuputFile, path, outPath).trim();
				}else if(className.startsWith("grade")){
					s = Utility.runCProgramWithPythonCommand("grade", tempOuputFile, path, outPath).trim();
				}else if(className.startsWith("smallest")){
					s = Utility.runCProgramWithPythonCommand("smallest", tempOuputFile, path, outPath).trim();
				}else if(className.startsWith("syllables")){
					s = Utility.runCProgramWithPythonCommand("syllables", tempOuputFile, path, outPath).trim();
				}
				
				System.out.println("Utility.initWhiteBox Python output: " + s);
				if(s.equals("Test passed.")){
					String index = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
					Utility.copy(path, outputFolder + "/whitebox/positive/" + index + ".in");
					Utility.copy(outPath, outputFolder + "/whitebox/positive/" + index + ".out");
				}
				else{
					String index = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
					Utility.copy(path, outputFolder + "/whitebox/negative/" + index + ".in");
					Utility.copy(outPath, outputFolder + "/whitebox/negative/" + index + ".out");
				}
			}
		}
		
	}
	



	/**
	 * only for testing purposes
	 * 
	 * @param args useless
	 */
	public static void main(String[] args){
		GenerateStandardTestCases test = new GenerateStandardTestCases("/home/matthias/git/IntroClass-master", "./bughunt");
		test.generate();
		test.printFailed();
	}

}
