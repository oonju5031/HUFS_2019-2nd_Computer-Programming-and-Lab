package kr.ac.hufs.ice.comProNLab.s201902822;

import java.util.ArrayList;
import java.util.Arrays;
import stanford.karel.SuperKarel;

public class CollectKarel extends SuperKarel {
	/* Global Variables -----------------------------------------------------*/
	Integer[][] now = new Integer[2][2];  // ���� ��ǥ
	private int beepersNumber = 0;
	private int debug = 1;
	
	ArrayList<Integer[][]> visited = new ArrayList<Integer[][]>();
	ArrayList<Integer[][]> queue = new ArrayList<Integer[][]>();
	
	/* Methods --------------------------------------------------------------*/
	/**
	 * ������ ��Ģ�� ���� ���� Ž���ϸ� ������ ������ �����ϴ� ���� �˰����̴�.
	 */
	public void searchAlgorithm() {
		now = new Integer[][] {{0, 0}, {0, 0}};  // ���� ��ǥ��
		
		recordVisited();
		recordQueue();
		moveKarel();
		deleteDuplicate();
		recordVisited();
		debugProgram(); // Debug
		while (queue.size() != 0) {
			recordQueue();
			moveKarel();
			deleteDuplicate();
			recordVisited();
			debugProgram(); // Debug
		}
	}
	
	/**
	 * Karel�� ������ �ٶ󺸰� �Ѵ�.
	 */
	private void faceEast() {
		if (facingNorth())
			turnRight();
		if (facingSouth())
			turnLeft();
		if (facingWest())
			turnAround();
	}
	
	/**
	 * ���� ��ǥ�� ������ ��ǥ�� queue�� �̹� �ִ��� �˻��ϰ�, ���ٸ� ��, ��, ��, �� ������ queue�� ����Ѵ�.
	 * {{a, b}, {x, y}}���� a, b�� �ش� ��ġ�� ��ǥ, x, y�� ���� ��ǥ�̴�.
	 * �� x, y�� a, b�� �� �ܰ� ���� ��ġ�� �ش��Ѵ�.
	 * ���ο� �ܰ��� queue ���Ұ� ���� �ܰ� queue�� ���ҿ� �����ؾ� �ϹǷ�
	 * ������ ��, ��, ��, �� ������ queue�� ù ��° ��ġ�� �����ϴ� ����� ä���Ѵ�.
	 */
	private void recordQueue() {
		if ((frontIsClear()) && (noDuplicate(now[0][0] + 1, now[0][1]))) {  // ���� ���
			queue.add(0, new Integer[][] {{now[0][0] + 1, now[0][1]}, {now[0][0], now[0][1]}});
		}
		turnLeft();
		if ((frontIsClear()) && (noDuplicate(now[0][0], now[0][1] + 1))) {  // ���� ���
			queue.add(0, new Integer[][] {{now[0][0], now[0][1] + 1}, {now[0][0], now[0][1]}});
		}
		turnAround();
		if ((frontIsClear()) && (noDuplicate(now[0][0], now[0][1] - 1))) {  // ���� ���
			queue.add(0, new Integer[][] {{now[0][0], now[0][1] - 1}, {now[0][0], now[0][1]}});
		}
		turnRight();
		if ((frontIsClear()) && (noDuplicate(now[0][0] - 1, now[0][1]))) {  // ���� ���
			queue.add(0, new Integer[][] {{now[0][0] - 1, now[0][1]}, {now[0][0], now[0][1]}});
		}
		faceEast();
	}
	
	/**
	 * ���� ��ǥ�� ������ ��ǥ�� visited�� �̹� �ִ��� �˻��ϰ�, ���ٸ� visited�� ����Ѵ�.
	 * {{a, b}, {x, y}}���� a, b�� ������ ��ǥ, x, y�� ���� ��ġ�� ��ǥ�� �ش��Ѵ�.
	 */
	private void recordVisited() {
		if (noDuplicate(now[0][0], now[0][1])) {
			visited.add(new Integer[][] {{now[0][0], now[0][1]}, {now[1][0], now[1][1]}});
		}
		faceEast();
	}
	
	/**
	 * �Էµ� x, y���� visited�� ������ true�� ��ȯ�ϰ�, ������ false�� ��ȯ�Ѵ�.
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean noDuplicate(int x, int y) {
		Integer[] location = new Integer[2];
		location[0] = x;
		location[1] = y;
		int duplicate = 0;
		
		for (int i = 0; i < visited.size(); i++) {
			if (Arrays.deepEquals(visited.get(i)[0], location)) {
				duplicate += 1;
			}
		}
		
		if (duplicate == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * ���� �湮�� ��ġ�� �ߺ��Ǵ� ��ǥ�� queue�� �ִٸ� �ش� ���Ҹ� �����Ѵ�.
	 * {{a, b}, {x, y}}���� {a, b}�� �����ϸ� �ߺ��Ǵ� ��ǥ�� �����Ѵ�.
	 * ��ǥ�� ������������ �����ϸ� ArrayList�� �迭 �������� ������ ����Ƿ�,
	 * ������������ �����ϵ��� �����.
	 * @param x
	 * @param y
	 */
	private void deleteDuplicate() {
		for (int i = queue.size() - 1; i >= 0; i--) {
			if (Arrays.deepEquals(now[0], queue.get(i)[0])) {
				queue.remove(i);
			}
		}
	}
	
	/**
	 * �̵� �� now�� ���� ���� ���� ���� �Է��Ѵ�.
	 */
	private void recordPresentToPast() {
		System.arraycopy(now[0], 0, now[1], 0, 2);
	}
	
	/**
	 * �� ��ġ�� �� ��ġ�� ������ �� queue�� ù ���ҷ� �̵��Ѵ�.
	 * �̵��� �����ϸ� �ش� ���Ҹ� queue���� �����Ѵ�.
	 * �̵��� �����ϸ� ������ �ִ� ��ġ,
	 * �� {{a, b}, {x, y}}���� {x, y}�� �̵��Ѵ�.
	 */
	private void moveKarel() {
		if ((now[0][0] - queue.get(0)[0][0] == 1) && (now[0][1] - queue.get(0)[0][1] == 0) && backIsClear()) {  // ���� �̵�
			recordPresentToPast();
			turnAround();
			move();
			now[0][0] -= 1;
			collectBeeper();
			queue.remove(0);
		} else if ((now[0][0] - queue.get(0)[0][0] == 0) && (now[0][1] - queue.get(0)[0][1] == 1) && rightIsClear()) {  // ���� �̵�
			recordPresentToPast();
			turnRight();
			move();
			now[0][1] -= 1;
			collectBeeper();
			queue.remove(0);
		} else if ((now[0][0] - queue.get(0)[0][0] == 0) && (now[0][1] - queue.get(0)[0][1] == -1) && leftIsClear()) { // ���� �̵�
			recordPresentToPast();
			turnLeft();
			move();
			now[0][1] += 1;
			collectBeeper();
			queue.remove(0);
		} else if ((now[0][0] - queue.get(0)[0][0] == -1) && (now[0][1] - queue.get(0)[0][1] == 0) && frontIsClear()) {  // ���� �̵�
			recordPresentToPast();
			move();
			now[0][0] += 1;
			collectBeeper();
			queue.remove(0);
		} else {  // ���� ��ǥ�� ��ȯ
			Integer[][] prev = findPrevLocation();
			if ((now[0][0] - prev[0][0] == 1) && (now[0][1] - prev[0][1] == 0) && backIsClear()) {  // west
				turnAround();
				move();
				now = prev;
			} else if ((now[0][0] - prev[0][0] == 0) && (now[0][1] - prev[0][1] == 1) && rightIsClear()) {  // south
				turnRight();
				move();
				now = prev;
			} else if ((now[0][0] - prev[0][0] == 0) && (now[0][1] - prev[0][1] == -1) && leftIsClear()) {  // north
				turnLeft();
				move();
				now = prev;
			} else if ((now[0][0] - prev[0][0] == -1) && (now[0][1] - prev[0][1] == 0) && frontIsClear()) {  // east
				move();
				now = prev;
			}
		}
		faceEast();
	}
	
	/**
	 * visited���� ���� ��ǥ���� ���Ѵ�.
	 */
	private Integer[][] findPrevLocation() {
		Integer[][] prev = new Integer[2][2];
		for (int i = 0; i < visited.size(); i++) {
			if (Arrays.deepEquals(visited.get(i)[0], now[1])) {
				System.arraycopy(visited.get(i)[0], 0, prev[0], 0, 2);
				System.arraycopy(visited.get(i)[1], 0, prev[1], 0, 2);
			}
		}
		return prev;
	}
	
	/**
	 * ���� ��ġ�� Beeper�� ������, beepersNumber�� ũ�⸦ 1 ������Ų��.
	 */
	private void collectBeeper() {
		if (beepersPresent()) {
			pickBeeper();
			beepersNumber += 1;
		}
	}
	
	/**
	 * ������� ���� Beeper�� ������ ����Ѵ�.
	 */
	public void showBeepersNumber() {
		System.out.println("Number of Beepers: " + beepersNumber);
	}
	
	/** 
	 * Karel�� �Ĺ濡 ���� ������ true, ������ false�� �����Ѵ�.
	 * frontIsClear(), leftIsClear(), RightIsClear()�� ���������� �̿��ϱ� ���� ������ �޼ҵ��̴�.
	 * @return boolean
	 */
	public boolean backIsClear() {
		turnAround();
		if (frontIsClear()) {
			turnAround();
			return true;
		} else {
			turnAround();
			return false;
		}
	}
	
	
	private void debugProgram() {
		System.out.println("Debug #" + debug);
		debug++;
		debug_Now();
		debug_Visited();
		debug_Queue();
		debug_PrevView();
		showBeepersNumber();
		System.out.println();
	}
	
	private void debug_Now() {
		System.out.print("Now: ");
		System.out.println(Arrays.deepToString(now));
	}
	private void debug_Visited() {
		System.out.print("Visited: ");
		for (int i = 0; i < visited.size(); i++) {	
			System.out.print("[[" + visited.get(i)[0][0] + ", " + visited.get(i)[0][1] + "], [" + visited.get(i)[1][0] + ", " + visited.get(i)[1][1] + "]], ");
		}
		System.out.println();
	}
	private void debug_Queue() {
		System.out.print("Queue: ");
		for (int i = 0; i < queue.size(); i++) {
			System.out.print("[[" + queue.get(i)[0][0] + ", " + queue.get(i)[0][1] + "], [" + queue.get(i)[1][0] + ", " + queue.get(i)[1][1] + "]], ");
		}
		System.out.println();
	}
	private void debug_PrevView() {
		System.out.print("Previous Location: ");
		System.out.println(Arrays.deepToString(findPrevLocation()));
	}
	
}