package test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import Database.DataBaseManager;
import Experiment.Analyzer;
import Experiment.GroupTest;
import Library.Utility;
import ProcessIntroClass.GenerateStandardTestCases;
import Repository.EntryAddition;

/**
 * SearchRepair is an automatic repair program, which uses semantic search to find
 * patches for faulty C-code. This version is the attempt to convert it, so that it
 * can repair Java-code.
 * 
 * @author Matthias Eiserloh(Java version)
 * @author originally created by Yuriy Brun, Claire Le Goues and Kathryn T. Stolee
 */
public class Main {
	
	/**
	 * Standard main method; generates the test cases and runs the repair progress.
	 * @param args - unused
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		//repository type: 0 linux, 1 introclass, 2 future, 5 myTest
		int repositoryType = 5;
		
		//introclass path
//		String introclassPath = "/home/matthias/git/IntroClass-master";
		String introclassPath = "/home/matthias/git/IntroClassJava-master/dataset";
		
		
		//get data directly 0 or re run to get data:1
		int operation = 1;
		
		//run wb test or run bb test, wb : wb = true, bb: wb = false; 
		boolean wb = true;

		if(operation == 0){
			Analyzer.getExistingData();
		}
		else{
			//rerun
			deleteFolders();
			GenerateStandardTestCases test = new GenerateStandardTestCases(introclassPath, "./bughunt");
			test.generate();
			rerun(wb, repositoryType);
			Analyzer.getCSVData();
		}
		long endTime = System.currentTimeMillis();
		long millis = endTime - startTime;
		System.out.println(String.format("%d min, %d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(millis),
			    TimeUnit.MILLISECONDS.toSeconds(millis) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
			));
		
	}

	private static void deleteFolders() {
		File file = new File(".");
		File[] directories = file.listFiles();
		for(File directory: directories){
			if(directory.isDirectory()){
				if(directory.getName().equals("bughunt") || directory.getName().equals("introclassJava")){
					deleteDirectory(directory);
				}
			}
		}
	    System.out.println("deleted bughunt- and introclassJAva-Folders!");
	}
	
	private static void deleteDirectory(File directory) {
	    if(directory.exists()){
	        File[] files = directory.listFiles();
	        if(null!=files){
	            for(int i=0; i<files.length; i++) {
	                if(files[i].isDirectory()) {
	                    deleteDirectory(files[i]);
	                }
	                else {
	                    files[i].delete();
	                }
	            }
	        }
	    }
	    directory.delete();
	}

	private static void rerun(boolean wb, int repositoryType) {
		DataBaseManager.connect();
		if(!DataBaseManager.isConnected()){
			System.out.println("Database not connected!");
			return;
		}
		DataBaseManager.rebuildTables();
		initRepository();
		GroupTest.rerun(wb, repositoryType);
		Analyzer.getCSVData();
	}

	private static void initRepository() {
//		EntryAddition.addOneFolder("./repository/future", DataBaseManager.TABLEFUTURE1);
//		EntryAddition.addOneFolder("./repository/future2", DataBaseManager.TABLEFUTURE2);
//		EntryAddition.addOneFolder("./repository/introclass", DataBaseManager.TABLEALL);
//		EntryAddition.addOneFolder("./repository/linux", DataBaseManager.TABLELINUX);
		EntryAddition.addOneFolder("./repository/myTest", DataBaseManager.TABLEMYTEST);
	}

}
