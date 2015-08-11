package Experiment;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import search.ResultObject.ResultState;

public class CheckSumInstance extends ProgramInstance {

	public CheckSumInstance(String program, Path folder, Path fileName, int repo, WhiteOrBlack wb) {
		super(program, folder, fileName, repo, wb);
		this.transformAndInitRunDir(false, "");
		this.initTests();
	}

	@Override
	public void search() {
		if(this.getTrainingTests().getPositives().size() == 0) {
			this.getInfo().getResult().setState(ResultState.NOPOSITIVE);
			return;
		}
		if(this.getTrainingTests().getNegatives().size() == 0){
			this.getInfo().getResult().setState(ResultState.CORRECT);
			return;
		}
		
		List<int[]> buggylines = getMultipleBuggyLines();

		for(int[] range : buggylines){

			boolean pass = constructProfile(range);
			if (!pass)
				continue;
			searchOverRepository(); // diff b/w Program and RegionInstance??

			ruleOutFalsePositive(range);

			if (isEmpty(info.getResult())) {
				this.info.getResult().setState(ResultState.FAILED);
				return;
			} else {
				if (!info.getResult().getPositive().isEmpty()) {
					this.info.getResult().setState(ResultState.SUCCESS);
				} else {
					this.info.getResult().setState(ResultState.PARTIAL);
				}
			}
			if(info.getResult().getState() == ResultState.SUCCESS){
				break;
			}
		}
	}
	
	protected List<int[]> getMultipleBuggyLines(){
		List<int[]> list = new ArrayList<int[]>();
		initSuspicious();
		double average = getAverage();
		int index = 12;
		while(index < this.getSuspiciousness().keySet().size()){
			if(this.getSuspiciousness().get(index) >= average){
				list.add(new int[]{index,index});
				int right = index + 1;
				while(right <= this.getSuspiciousness().keySet().size() && right - index < 6) {
					if(this.getSuspiciousness().get(right) >= average) {
						list.add(new int[] {index, right});
					}
					right++;
				}
			}
			index++;
		}
				
		return list;
	}

//	public static void main(String[] args){
//		CheckSumSearchCase instan = new CheckSumSearchCase("./bughunt/checksum/6", "checksum.c", 5);
//		instan.transformAndInitRunDir(false, "");
//		instan.initInputAndOutput();
//		instan.search(true);
//		instan.recordResult(true);
//		instan.search(false);
//		instan.recordResult(false);
//	}

	

}
