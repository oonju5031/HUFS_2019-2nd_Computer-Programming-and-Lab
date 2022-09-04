package kr.ac.hufs.ice.comProNLab.s201902822;

import java.util.ArrayList;
import java.util.Arrays;
import stanford.karel.SuperKarel;

public class CollectKarel extends SuperKarel {
	/* Global Variables -----------------------------------------------------*/
	Integer[][] now = new Integer[2][2];  // 현재 좌표
	private int beepersNumber = 0;
	private int debug = 1;
	
	ArrayList<Integer[][]> visited = new ArrayList<Integer[][]>();
	ArrayList<Integer[][]> queue = new ArrayList<Integer[][]>();
	
	/* Methods --------------------------------------------------------------*/
	/**
	 * 정해진 규칙에 따라 맵을 탐사하며 비퍼의 개수를 저장하는 고유 알고리즘이다.
	 */
	public void searchAlgorithm() {
		now = new Integer[][] {{0, 0}, {0, 0}};  // 최초 좌표값
		
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
	 * Karel이 동쪽을 바라보게 한다.
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
	 * 현재 좌표와 인접한 좌표가 queue에 이미 있는지 검사하고, 없다면 서, 남, 북, 동 순으로 queue에 기록한다.
	 * {{a, b}, {x, y}}에서 a, b는 해당 위치의 좌표, x, y는 현재 좌표이다.
	 * 즉 x, y는 a, b의 한 단계 이전 위치에 해당한다.
	 * 새로운 단계의 queue 원소가 이전 단계 queue의 원소에 선행해야 하므로
	 * 역순인 동, 북, 남, 서 순으로 queue의 첫 번째 위치에 삽입하는 방식을 채택한다.
	 */
	private void recordQueue() {
		if ((frontIsClear()) && (noDuplicate(now[0][0] + 1, now[0][1]))) {  // 동향 기록
			queue.add(0, new Integer[][] {{now[0][0] + 1, now[0][1]}, {now[0][0], now[0][1]}});
		}
		turnLeft();
		if ((frontIsClear()) && (noDuplicate(now[0][0], now[0][1] + 1))) {  // 북향 기록
			queue.add(0, new Integer[][] {{now[0][0], now[0][1] + 1}, {now[0][0], now[0][1]}});
		}
		turnAround();
		if ((frontIsClear()) && (noDuplicate(now[0][0], now[0][1] - 1))) {  // 남향 기록
			queue.add(0, new Integer[][] {{now[0][0], now[0][1] - 1}, {now[0][0], now[0][1]}});
		}
		turnRight();
		if ((frontIsClear()) && (noDuplicate(now[0][0] - 1, now[0][1]))) {  // 서향 기록
			queue.add(0, new Integer[][] {{now[0][0] - 1, now[0][1]}, {now[0][0], now[0][1]}});
		}
		faceEast();
	}
	
	/**
	 * 현재 좌표와 동일한 좌표가 visited에 이미 있는지 검사하고, 없다면 visited에 기록한다.
	 * {{a, b}, {x, y}}에서 a, b는 현재의 좌표, x, y는 이전 위치의 좌표에 해당한다.
	 */
	private void recordVisited() {
		if (noDuplicate(now[0][0], now[0][1])) {
			visited.add(new Integer[][] {{now[0][0], now[0][1]}, {now[1][0], now[1][1]}});
		}
		faceEast();
	}
	
	/**
	 * 입력된 x, y값이 visited에 없으면 true를 반환하고, 있으면 false를 반환한다.
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
	 * 현재 방문한 위치와 중복되는 좌표가 queue에 있다면 해당 원소를 삭제한다.
	 * {{a, b}, {x, y}}에서 {a, b}가 동일하면 중복되는 좌표로 간주한다.
	 * 좌표를 오름차순으로 삭제하면 ArrayList의 배열 순서에서 오류가 생기므로,
	 * 내림차순으로 삭제하도록 만든다.
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
	 * 이동 전 now의 현재 값을 이전 값에 입력한다.
	 */
	private void recordPresentToPast() {
		System.arraycopy(now[0], 0, now[1], 0, 2);
	}
	
	/**
	 * 현 위치를 전 위치에 저장한 후 queue의 첫 원소로 이동한다.
	 * 이동에 성공하면 해당 원소를 queue에서 삭제한다.
	 * 이동에 실패하면 이전에 있던 위치,
	 * 즉 {{a, b}, {x, y}}에서 {x, y}로 이동한다.
	 */
	private void moveKarel() {
		if ((now[0][0] - queue.get(0)[0][0] == 1) && (now[0][1] - queue.get(0)[0][1] == 0) && backIsClear()) {  // 서향 이동
			recordPresentToPast();
			turnAround();
			move();
			now[0][0] -= 1;
			collectBeeper();
			queue.remove(0);
		} else if ((now[0][0] - queue.get(0)[0][0] == 0) && (now[0][1] - queue.get(0)[0][1] == 1) && rightIsClear()) {  // 남향 이동
			recordPresentToPast();
			turnRight();
			move();
			now[0][1] -= 1;
			collectBeeper();
			queue.remove(0);
		} else if ((now[0][0] - queue.get(0)[0][0] == 0) && (now[0][1] - queue.get(0)[0][1] == -1) && leftIsClear()) { // 북향 이동
			recordPresentToPast();
			turnLeft();
			move();
			now[0][1] += 1;
			collectBeeper();
			queue.remove(0);
		} else if ((now[0][0] - queue.get(0)[0][0] == -1) && (now[0][1] - queue.get(0)[0][1] == 0) && frontIsClear()) {  // 동향 이동
			recordPresentToPast();
			move();
			now[0][0] += 1;
			collectBeeper();
			queue.remove(0);
		} else {  // 이전 좌표로 귀환
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
	 * visited에서 이전 좌표값을 구한다.
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
	 * 현재 위치에 Beeper가 있으면, beepersNumber의 크기를 1 증가시킨다.
	 */
	private void collectBeeper() {
		if (beepersPresent()) {
			pickBeeper();
			beepersNumber += 1;
		}
	}
	
	/**
	 * 현재까지 모은 Beeper의 개수를 출력한다.
	 */
	public void showBeepersNumber() {
		System.out.println("Number of Beepers: " + beepersNumber);
	}
	
	/** 
	 * Karel의 후방에 벽이 없으면 true, 있으면 false를 리턴한다.
	 * frontIsClear(), leftIsClear(), RightIsClear()와 병렬적으로 이용하기 위해 정의한 메소드이다.
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