package Experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import search.ResultObject.ResultState;

/**
 * This class calls the different searchCases for the specific tests.
 * 
 * @see MedianSearchCase
 * @see GradeSearchCase
 * @see SyllableSearchCase
 * @see CheckSumSearchCase
 * @see DigitSearchCase
 * 
 *
 */
public class GroupTest {

	/**
	 * for testing purpose only
	 * 
	 * @param args unused
	 */
	public static void main(String[] args) {
		//medianTest(false, 2);
		//smallestTest(false, 2);
		//gradeTest(false, 2);
		//checkSumTest(false, 2);
		syllablesTest(false, 2);
	}

	/**
	 * <p>calls the different function tests:</p>
	 * <p>{@link GroupTest#medianTest(boolean, int)}</p>
	 * <p>{@link GroupTest#smallestTest(boolean, int)}</p>
	 * <p>{@link GroupTest#gradeTest(boolean, int)}</p>
	 * <p>{@link GroupTest#checkSumTest(boolean, int)}</p>
	 * <p>{@link GroupTest#syllablesTest(boolean, int)}</p>
	 * <p> tip: running all of those tests at the same time will take very long; comment unnecessary ones out.</p>
	 * 
	 * @param wb 				wb = whitebox and determines whether the program will run a whitebox test(true) or a blackbox one(false).
	 * @param repositoryType	which repository the program should use to repair the faulty functions.
	 * 							repository type: 0 linux, 1 introclass, 2 future
	 */
	public static void rerun(boolean wb, int repositoryType) {
		medianTest(wb, repositoryType);
		//smallestTest(wb, repositoryType);
		// gradeTest(wb, repositoryType);
		// checkSumTest(wb, repositoryType);
		// syllablesTest(wb, repositoryType);
	}

	/**
	 * This function tries to repair each checksum.c file in bughunt/checksum/ one by one.
	 * 
	 * @see CheckSumSearchCase
	 * @see ESearchCase
	 *  
	 * @param wb 	true = whitebox test; false = blackbox test
	 * @param type	repository type: 0 linux, 1 introclass, 2 future
	 */
	public static void checkSumTest(boolean wb, int type) {
		List<String> list = new ArrayList<String>();
		File file = new File("./bughunt/checkSum");
		int size = file.listFiles().length;
		int actualRepository = 0;
		for (File root : file.listFiles()) {
			try {
				String folder = "./bughunt/checkSum/" + root.getName();
				String fileName = "checkSum.c";
				if (type == 2) {
					//get numbor of folder to decide which future Repository to take
					int value = Integer.parseInt(root.getName());
					if(value < size / 2) actualRepository = 3;
					else actualRepository = 4;
				}else if(type == 1){
					actualRepository = 1;
				}
				CheckSumSearchCase searcher = new CheckSumSearchCase(folder, fileName, actualRepository);
				searcher.transformAndInitRunDir(false, "");
				searcher.initInputAndOutput();
				searcher.search(wb);
				searcher.recordResult(wb);
				if (searcher.getInfo().getResult().getState() == ResultState.SUCCESS) {
					list.add(folder);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		for (String s : list) {
			System.out.println(s);
		}
	}

	/**
	 * This function tries to repair each median.c file in bughunt/median/ one by one.
	 * 
	 * @see MedianSearchCase
	 * @see ESearchCase
	 *  
	 * @param wb 	true = whitebox test; false = blackbox test
	 * @param type	repository type: 0 linux, 1 introclass, 2 future
	 */
	public static void medianTest(boolean wb, int type) {
		List<String> list = new ArrayList<String>();
		File file = new File("./bughunt/median");
		int size = file.listFiles().length;
		int actualRepository = 0;
		for (File root : file.listFiles()) {
			try {
				// if(!root.getName().equals("225"))continue;
				String folderPath = "./bughunt/median/" + root.getName();
				File folder = new File(folderPath);
				String javaFileName = "";
				for(File thisFile: folder.listFiles()){
					if(thisFile.getName().endsWith(".java")){
						javaFileName = thisFile.getName();
						break;
					}
				}
				if (type == 2) {
					int value = Integer.parseInt(root.getName());
					if(value < 40 || value > 40) continue;
//					if(value < 10 || value >= 20) continue;
					if(value < size / 2) {
						actualRepository = 3;
					}
					else actualRepository = 4;
				}else{
					int value = Integer.parseInt(root.getName());
					if(value < 40 || value > 40) continue;
					actualRepository = type;

				}
				MedianSearchCase searcher = new MedianSearchCase(folderPath, javaFileName, actualRepository);
				//TODO: for now no transformation. later maybe switch back to "true"?
				searcher.transformAndInitRunDir(false, "");
				searcher.initInputAndOutput();
				searcher.search(wb);
				searcher.recordResult(wb);
				if (searcher.getInfo().getResult().getState() == ResultState.SUCCESS) {
					list.add(folderPath);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

	}

	/**
	 * This function tries to repair each smallest.c file in bughunt/smallest/ one by one.
	 * 
	 * @see MedianSearchCase
	 * @see ESearchCase
	 *  
	 * @param wb 	true = whitebox test; false = blackbox test
	 * @param type	repository type: 0 linux, 1 introclass, 2 future
	 */
	public static void smallestTest(boolean wb, int type) {
		List<String> list = new ArrayList<String>();
		File file = new File("./bughunt/smallest");
		int size = file.listFiles().length;
		int actualRepository = 0;
		for (File root : file.listFiles()) {
			try {
				String folder = "./bughunt/smallest/" + root.getName();
				String fileName = "smallest.c";
				if (type == 2) {
//					String name = root.getName();
//					if(name.charAt(0) == '0') continue;
//					if(name.charAt(0) == '1'){
//						if(name.length() == 1) continue;
//						if((name.length() == 2 || name.length() == 3) && name.charAt(1) < '4') continue;
//						if(name.length() == 3 && name.charAt(1) == '4' && name.charAt(2) < '6') continue;
//					}
					int value = Integer.parseInt(root.getName());
					if(value < size / 2) actualRepository = 3;
					else actualRepository = 4;
				}else if(type == 1){
					actualRepository = 1;
				}
				System.out.println(folder);
				MedianSearchCase searcher = new MedianSearchCase(folder, fileName, actualRepository);
				searcher.transformAndInitRunDir(true, "");
				searcher.initInputAndOutput();
				searcher.search(wb);
				searcher.recordResult(wb);
				if (searcher.getInfo().getResult().getState() == ResultState.SUCCESS) {
					list.add(folder);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

	}

	/**
	 * This function tries to repair each grade.c file in bughunt/grade/ one by one.
	 * 
	 * @see GradeSearchCase
	 * @see ESearchCase
	 *  
	 * @param wb 	true = whitebox test; false = blackbox test
	 * @param type	repository type: 0 linux, 1 introclass, 2 future
	 */
	public static void gradeTest(boolean wb, int type) {
		List<String> list = new ArrayList<String>();
		File file = new File("./bughunt/grade");
		int size = file.listFiles().length;
		int actualRepository = 0;
		for (File root : file.listFiles()) {
			try {
				String folder = "./bughunt/grade/" + root.getName();
				String fileName = "grade.c";
				if (type == 2) {
					int value = Integer.parseInt(root.getName());
					if(value < size / 2) actualRepository = 3;
					else actualRepository = 4;
					//if(value != 120) continue;
				}else if(type == 1){
					actualRepository = 1;
				}
				System.out.println(folder);
				GradeSearchCase instan = new GradeSearchCase(folder, fileName, actualRepository);
				instan.transformAndInitRunDir(true, "--type grade");
				instan.initInputAndOutput();
				instan.search(wb);
				instan.recordResult(wb);
				if (instan.getInfo().getResult().getState() == ResultState.SUCCESS) {
					list.add(folder);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * This function tries to repair each syllables.c file in bughunt/syllables/ one by one.
	 * 
	 * @see SyllableSearchCase
	 * @see ESearchCase
	 *  
	 * @param wb 	true = whitebox test; false = blackbox test
	 * @param type	repository type: 0 linux, 1 introclass, 2 future
	 */
	public static void syllablesTest(boolean wb, int type) {
		List<String> list = new ArrayList<String>();
		File file = new File("./bughunt/syllables");
		int size = file.listFiles().length;
		int actualRepository = 0;
		for (File root : file.listFiles()) {
			try {
				// if(root.getName().charAt(0) < '5') continue;
				String folder = "./bughunt/syllables/" + root.getName();
				String fileName = "syllables.c";
				String name = root.getName();
				if(name.length() >= 2 && name.charAt(0) <=1 && name.charAt(0) <= 1)continue;
				if (type == 2) {
					int value = Integer.parseInt(root.getName());
					if(value < size / 2) actualRepository = 3;
					else actualRepository = 4;
				}else if(type == 1){
					actualRepository = 1;
				}
				SyllableSearchCase searcher = new SyllableSearchCase(folder, fileName, actualRepository);
				searcher.transformAndInitRunDir(false, "");
				searcher.initInputAndOutput();
				searcher.search(wb);
				searcher.recordResult(wb);
				if (searcher.getInfo().getResult().getState() == ResultState.SUCCESS) {
					list.add(folder);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

}
