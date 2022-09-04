package kr.ac.hufs.ice.comProNLab.s201902822;

import stanford.karel.OopKarelProgram;


/**
 * BeeperCollect
 * Using own algorithm
 * @author JunYoung Lee
 */
@SuppressWarnings("serial")
public class BeeperCollect extends OopKarelProgram {
	/* Run & Main -----------------------------------------------------------*/
	public void run() {
		CollectKarel karel = (CollectKarel) getKarel();
		
		try {
			karel.searchAlgorithm();
			karel.showBeepersNumber();
		} catch (Exception e) {
			System.out.println("An exception occured.");
			karel.showBeepersNumber();
		}
	}
	
	public static void main(String[] args) {
		OopKarelProgram.main(args, new CollectKarel());
	}
		
}