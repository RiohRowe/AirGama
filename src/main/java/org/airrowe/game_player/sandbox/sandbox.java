package org.airrowe.game_player.sandbox;

public class sandbox {
	public static void printArray(int[]intArr) {
		System.out.print("["+intArr[0]);
		for(int i = 1; i<intArr.length; ++i) {
			System.out.print(","+intArr[i]);
		}
		System.out.println("]");
	}
	public static void updateIntArray(int[] intArr) {
		for(int i = 0; i<intArr.length; ++i) {
			intArr[i]=i+1;
		}
		printArray(intArr);
	}
	public static void main(String[] args) {
		int[] nums = new int[10];
		for(int i = 0; i<10; ++i) {
			nums[i]=0;
		}
		updateIntArray(nums);
		printArray(nums);
	}
}
