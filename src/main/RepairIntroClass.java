package main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import Experiment.CheckSumInstance;
import Experiment.DigitsInstance;
import Experiment.ExperimentType;
import Experiment.GradeInstance;
import Experiment.MedianInstance;
import Experiment.ProgramInstance;
import Experiment.SyllablesInstance;
import Experiment.WhiteOrBlack;

public class RepairIntroClass {

	private static int getActualRepository(String program, int size, int value) {
		switch(program) {
		case "median" :
			if(value < 49 || value > 100) return -1;
			if(value < size / 2) return 3;
			else return 4;
		default:
			if(value < size / 2) return 3;
			else return 4;
		}
	}


	public static void rerun(WhiteOrBlack wb, ExperimentType repositoryType) {
		for(String program : Configuration.programs) {
			File file = new File(Configuration.outputPath + File.separator + program);
			int size = file.listFiles().length;
			int actualRepository =  -1; 
			switch(repositoryType) {
			case FUTURE: actualRepository = 2;
			break;
			case INTROCLASS : actualRepository = 1;
			break;
			case LINUX : actualRepository = 0;  
			break; 
			}
			ProgramInstance instance = null;
			for(File root : file.listFiles ()) {
				Path folder = Paths.get(Configuration.outputPath + program + File.separator + root.getName());
				Path fileName = Paths.get(folder.toString() + File.separator + program + ".c"); 	
				if(repositoryType == ExperimentType.FUTURE) {
					actualRepository = getActualRepository(program, size, Integer.parseInt(root.getName()));
				}
				if(actualRepository < 0) continue;
				switch(program) {
				case "checksum" : 
					instance = new CheckSumInstance(program, folder, fileName, actualRepository, wb);
					break;
				case "grade" :
					instance = new GradeInstance(program, folder,fileName, actualRepository, wb);
					break;
				case "median" : 
				case "smallest" :
					instance =  new MedianInstance(program, folder, fileName, actualRepository, wb);
					break;
				case "syllables" :
					String name = root.getName();
					if(name.length() >= 2 && name.charAt(0) <=1 && name.charAt(0) <= 1)continue;
					instance = new SyllablesInstance(program, folder, fileName, actualRepository, wb);
					break;
				case "digits":
					instance = new DigitsInstance(program, folder, fileName, actualRepository, wb);
					break;
				default: continue;
				}
				instance.search();
				instance.recordResult(wb);
			}
		}
	}
}

