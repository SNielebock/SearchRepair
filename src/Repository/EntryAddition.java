
package Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import Database.EntryHandler;
import Database.EntryObject;
import Library.Utility;

public class EntryAddition {
	
	private static int count = 0;
	private static int save = 0;
	public static void addOneFile(String filePath, String table){
		File file = new File(filePath);
		if(!file.exists()){
			System.out.println("Return in EntryAddition.addOneFile");
			return;
		}
		List<Method> methods = parse(filePath);
		for(Method method : methods){
			count++;

			EntryObject object;
			try{
				object = covertMethodToEntry(method);
			}catch(Exception e){
				System.out.println(e);
				continue;
			}
			EntryHandler.save(object, table);
//			try {
//				System.setOut(new PrintStream("log"));
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			System.out.println(method.getName());
//			save++;
			System.out.println(method.getSource());

			for(String path : object.getPathConstraint().keySet())
			{
				System.out.println("constraint:\n" + object.getPathConstraint().get(path));
				System.out.println("variable:\n" + object.getPathFormalVariables().get(path));
				System.out.println("track:\n" + object.getPathVariableTrack().get(path));
				System.out.println("type:\n" + object.getPathVariablesTypes().get(path));
				System.out.println("path:\n" + path);
			}
		}
		System.out.println("count: " + count + "save: " + save);
	}
	

	
	private static EntryObject covertMethodToEntry(Method method) {
		EntryTranslator translator = new EntryTranslator(method);
		return translator.getEntryObject();
		
	}



	public static void addOneFolder(String dirPath, String table){
		File dir = new File(dirPath);
		if(!dir.exists()) return;
		deleteJPFandClass(dirPath);
		for(File file : dir.listFiles()){
			if(file.isDirectory()){
				addOneFolder(file.getAbsolutePath(), table);
			}
			else{
				addOneFile(file.getAbsolutePath(), table);
			}
			
		}
	}
	

	
	private static List<Method> parse(String fileName){
		//assume only one method
		List<Method> methods = new ArrayList<Method>();
		if(fileName.endsWith(".java")){
			try {
				String fileNameWOPath = fileName.substring(fileName.lastIndexOf("/") + 1);
				String fileNameWOExt = fileName.substring(0, fileName.lastIndexOf("."));
				String pureFileName = fileNameWOPath.substring(0, fileNameWOPath.lastIndexOf("."));
				
				//if(!fileName.equals("./repository/scrape/test41.c")) return methods;
	//			String com = "./executors/pathgen " + fileName;
	
				createJPFfile(fileNameWOExt);
				Runtime.getRuntime().exec("javac -g " + fileName);
				File f = new File(fileNameWOExt + ".jpf");
				if(!f.exists()) { 
					System.out.println("JPF was not created!(check EntryAddition.createJPFfile for details)");
				    return methods;
				}
				String com = "java -jar /home/matthias/git/jpf-core/build/RunJPF.jar +shell.port=4242 " + fileNameWOExt + ".jpf";
	
	 			Process p = Runtime.getRuntime().exec(com);
				BufferedReader ls_in = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
	//			BufferedReader ls_error = new BufferedReader(new InputStreamReader(
	//					p.getErrorStream()));
	//			System.out.println(ls_error.readLine());
				String s = null;
				StringBuilder path = new StringBuilder();
				StringBuilder input = new StringBuilder();
				Method method = new Method();
				boolean startParsing = false;
				boolean correct = true;
				System.out.println("FILE: " + fileName);
				while((s = ls_in.readLine()) != null)
				{
					System.out.println("S:" + s);
					s = s.trim();
					if(s.startsWith("Processing:")){
						if(method.getName() != null){
							methods.add(method);
							method = new Method();						
						}
						method.setName(s.substring(12));
					}
					else if(s.startsWith("LOCAL")){					
						path.append(s.substring(6, s.length() - 1));
						path.append(";");
						path.append("\n");
					}
					else if(s.startsWith("GLOBAL")){
						continue;
	//					path.append(s.substring(7, s.length() - 1));
	//					path.append("\n");
					}
					else if(s.startsWith("FORMAL")){
						input.append(s.substring(7, s.length() - 1));
						input.append("\n");
						path.append(s.substring(7, s.length() - 1));
						path.append(";");
						path.append("\n");
					}
					else if(s.startsWith("STMT(return")){
						path.append(s.substring(5, s.length() - 1));
						path.append("\n");
						
						
						method.getPath().add(path.toString());
						System.out.println("METHOD.GETPATH: " + path.toString());
						method.getPathToInput().put(path.toString(), input.toString());	
						System.out.println("METHOD.GETPATHTOINPUT: " + input.toString());
	//					System.out.println("PATHGENPATH: " + method.getPathToInput());
	//					System.out.println(input.toString());
						path = new StringBuilder();
						input = new StringBuilder();
						//startParsing = false;
					}
					else if(s.equals("Paths:")){
						startParsing = true;
						continue;
					}
					else if( s.startsWith("path_enumeration") || s.startsWith("postprocess")){
						continue;
					}
					else if(s.startsWith("STMT(")){
						if(s.endsWith(";)")){
							path.append(s.substring(5, s.length() - 1));
							path.append("\n");
						}
						else{
							path.append(s.substring(5, s.length()));
							path.append("\n");
						}
					}
					else if(s.endsWith(";)")){
						path.append(s.substring(0, s.length() - 1));
						path.append("\n");
					}
					else if(s.startsWith("ASSUME(")){
						path.append(s.substring(7, s.length() - 1));
						path.append(";");
						path.append("\n");
					}
					else if(s.startsWith("Number of")){
						continue;
					}
					else {
						if(startParsing){
							path.append(s);
							path.append("\n");
						}
						if(s.contains("error")){
							correct = false;
							break;
						}
					}
				}
				if(!correct){
					methods.clear();
					return methods;
				}
				if(method.getName() != null)
				{
					methods.add(method);
				}
				
				ls_in.close();
			} catch (IOException e) {
				
				e.printStackTrace();
				return methods;
			}
			if(methods.isEmpty()) return methods;
			//Get  whole file text
			String fileString = Utility.getStringFromFile(fileName);
			List<String> sources = new ArrayList<String>();
			List<String> types = new ArrayList<String>();
			int start = -1;
			int end = -1;
			Stack<Character> stack = new Stack<Character>();
			Stack<Integer> typeStack = new Stack<Integer>();
			Stack<Integer> index = new Stack<Integer>();
			//gets first line for getType method and gets method body for sources.add
			for(int i = 0; i < fileString.length(); i++)
			{
				char c = fileString.charAt(i);
				if(c == '{'){
					if(stack.isEmpty()){
						int g = typeStack.isEmpty() ? 0 : typeStack.pop() + 1;
						String declare = fileString.substring(g, i);
						types.add(getType(declare));
					}
					index.push(i);
					stack.add(c);
					if(!typeStack.isEmpty())typeStack.pop();
					
				}
				else if(c == '}'){
					stack.pop();
					typeStack.push(i);
					int temp = index.pop();
					if(stack.isEmpty()){
						start = temp;
						end = i;
						String body = fileString.substring(start+1, end);
						sources.add(body);
					}				
				}
			}
			
			for(int i = 0; i < methods.size(); i++){
				methods.get(i).setSource(sources.get(i));
				methods.get(i).setReturnType(types.get(i));
	//			System.out.println("TYPES: " + types);
			}
			return methods;
		}else{//no *.java-file
			return methods;
		}
		
	}



	private static void createJPFfile(String fileNameWOExt) {
		List<String> lines = Arrays.asList( "target=Example",
											"classpath=/home/matthias/git/SearchRepair/repository/myTest",
											"symbolic.method=Example.foo(sym#sym)",
											"listener = gov.nasa.jpf.symbc.SymbolicListener",
											"vm.storage.class=nil",
											"search.multiple_errors=true");
		Path file = Paths.get(fileNameWOExt + ".jpf");
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}



	private static void deleteJPFandClass(String dirPath) {
		try {
			File dir = new File(dirPath);
			if(!dir.exists()) return;
			for(File file : dir.listFiles()){
				String fileAbsolutePath = file.getAbsolutePath();
				if(file.isDirectory()){
					deleteJPFandClass(fileAbsolutePath);
				}
				else{
					if(fileAbsolutePath.endsWith(".jpf")){
						Path JPFpath = Paths.get(fileAbsolutePath);
						Boolean jpfTest = Files.deleteIfExists(JPFpath);
						System.out.println("Deleted JPF:" + jpfTest);
					}else if(fileAbsolutePath.endsWith(".class")){
						Path CLASSpath = Paths.get(fileAbsolutePath);
						Boolean classTest = Files.deleteIfExists(CLASSpath);
						System.out.println("Deleted Class: " + classTest);
					}else if(!fileAbsolutePath.endsWith(".java")){
						System.out.println("This File will be ignored: " + fileAbsolutePath);
					}
				}	
			}
		} catch (IOException x) {
		    // File permission problems are caught here.
		    System.err.println(x);
		}
		
	}



	private static String getType(String declare) {
		declare = declare.trim();
		if(declare.startsWith("int")) return "int";
		else if(declare.startsWith("double")) return "double";
		else if(declare.startsWith("char")) return "char";
		else if(declare.startsWith("float")) return "float";
		else if(declare.startsWith("char*")) return "char*";
		else return "void";
	}




	public static void main(String[] args) throws FileNotFoundException{
		String filePath = "./repository/future";
		EntryAddition.addOneFolder(filePath, "future");;
		//EntryAddition.addOneFile(filePath);
	}
}
