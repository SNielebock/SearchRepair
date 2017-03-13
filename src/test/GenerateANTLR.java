package test;

import Library.Utility;

public class GenerateANTLR {

	public static void main(String[] args) {
		//generate new ANTLR Java files, because they don't seem to work sometimes, if not generated new (and I don't know why).
		String command1 = "java -jar ./lib/antlr-4.4-complete.jar -package antlr.preprocess ./src/antlr/preprocess/Java.g4";
		String output = Utility.runCProgram(command1);		
		System.out.println("ANTLR output: " + output);
//			for(IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()){
//			    project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
//			}
	}

}
