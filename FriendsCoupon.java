// Christen Reinbeck
// Assignment 2
// CS 445


import java.util.*;
import java.io.*;

public class FriendsCoupon {
	
	
	public static void main(String[] args) throws IOException{
	// taking arguments from command line
		if(args[0]==null || args[1]==null){
			System.exit(0);
		}
		String filename = args[0];
		String strCoupons = args[1];
		
		if(!(filename.equals("-t"))){
			int numCoupons = Integer.parseInt(strCoupons);
		// reading file to determine size of n-dimensional array
	
			BufferedReader input;
			ArrayList<String> tempRows = new ArrayList<String>();
			int rows = 0;
			int cols = 0;
			int [][] data = new int[rows][rows];
			
			try {
				input = new BufferedReader(new FileReader(filename));
				
				while(input.ready()){
					tempRows.add(input.readLine());
				}
				rows = tempRows.size();
		
				char[] tempCols = tempRows.get(0).toCharArray();	
				for(int i=0; i<tempCols.length; i++){
					if(tempCols[i] != ' '){
						cols++;
					}
				}
				input.close();
				
		// checks if a square - if not exits and error message
				if(rows != cols){
					System.out.println("Error: not a square table.");
					System.exit(0);
				}
				char[] loopCols;
				for(int x=0; x<rows; x++){
					loopCols = tempRows.get(x).toCharArray();
					if(loopCols.length != tempCols.length){
						System.out.println("Error: not a square table.");
						System.exit(0);
					}
				}
		
	
		// initialize multi dim array
				data = new int[rows][rows];
				
		// reading into multi dim array
				Scanner arrayIn = new Scanner(new File(filename));
				for(int i=0; i<rows; i++){
					for(int j=0; j<rows; j++){
						if(arrayIn.hasNextInt()){
							data[i][j] = arrayIn.nextInt();
						}
					}
				}
				
		// checking for symmetry data(i,j) = data(j,i)
				for(int m=0; m<rows; m++){
					for(int n=0; n<rows; n++){
						if(data[m][n] != data[n][m]){
							System.out.println("Error: not a symmetric table.");
							System.exit(0);
						}
					}
				}
	
		// checking that all data(i,i) = 0
				for(int k=0; k<rows; k++){
					if(data[k][k] != 0){
						System.out.println("Error: not a valid table.");
						System.exit(0);
					}
				}
				
				// System.out.println(data[0][4]);
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	// END OF FILE / DATA CHECK
			
			
	// EXECUTES RECURSIION METHODS		
			// create array that is as long as your data
			int[] friends = new int[rows];
			friends = zeroFill(friends);
			int[] solution = solveOnce(friends, data, numCoupons);
			
			// checks if solution returns null - if null then it is impossible to solve
			if(solution == null){
				System.out.println("No assignment possible.");
				System.exit(0);
			}
			
			printSolution(solution);
			System.exit(0);
// RUNNING TESTS		
		}else{
			
			
			int[][] testingArr = testDataArray();
			testIsFullSolution(testingArr);
			testReject(testingArr);
			testExtend();
			testNext();
			
			System.exit(0);
			
		}
		
		
	}
	
	
	
	// initally fills the friend coupon array with zeros for easy error checking
	public static int[] zeroFill(int[] partial){
		for(int i=0; i<partial.length; i++){
			partial[i] = 0;
		}
		return partial;
	}
	
	// converts numbered coupons to capital letters via ascii
	public static char[] numToLetter(int[] partial){
		char[] couponLetters = new char[partial.length];
		int convert;
		for(int i=0; i<partial.length; i++){
			convert = partial[i] + 64;
			couponLetters[i] = (char)convert;
		}
		return couponLetters;
	}
	
	// print solution
	public static void printSolution(int[] solution){
		char[] letters = numToLetter(solution);
		for(int i=0; i<letters.length; i++){
			System.out.print(letters[i]);
			if(i==(letters.length - 1)){
				System.out.print("\n");
				break;
			}
			System.out.print(",");
			
		}
	}
	
	// chooses rejection circumstances
	public static boolean compareData(int i, int j, int[][] data, int[] friends){
		int startingCoupon = friends[i];
		int compareCoupon = friends[j];
		int dataValue = data[i][j];
		if(data[i][j] == 1 && startingCoupon == compareCoupon){
			return true;
			// not a valid partial
		}
		// false is good and means this is a valid partial
		return false;
	}
	
	public static boolean isFullSolution(int[] partial, int[][] data){
		for(int a=0; a<partial.length; a++){
			// returns false if any of the coupons are 0
			if(partial[a] == 0){
				return false;
			}
		}
		if(reject(partial, data)){
			return false;
		}
		// only return true if a good solution
		return true;
	}
	
	public static boolean reject(int[] partial, int[][] data){
		for(int i=0; i<partial.length; i++){
			for(int j=0; j<i; j++){
				if(partial[i] == 0 || partial[j] == 0){
					// cant have conflict bc not filled with an answer
					
				}else if((i != j) && compareData(i, j, data, partial)){
					return true;
				}
			}
		}
		// only returns false if it can get through the above logic
		return false;
	}
	
	//adds the one additional choice starting at the beginning
	public static int[] extend(int[] partial){
		int[] temp = new int[partial.length];
		for(int i=0; i<partial.length;i++){
			if(partial[i] != 0){
				temp[i] = partial[i];
			}else{
				temp[i] = 1;
				return temp;
			}
		}
		return null;
	}
	
	// changes the current coupon to the next possible via +1
	public static int[] next(int[] partial, int numCoupons){
		int[] temp = new int[partial.length];
		int i = 0;
		while(i < partial.length){
			if( i == (partial.length - 1) || partial[i+1]==0){
				if(partial[i] == numCoupons){
					// means we are at the end of the types of coupons and cannot try a higher one
					return null;
				}else{
					// any other case we can increment up the coupon
					temp[i] = partial[i] +1;
					break;
				}
			}else{
				//copy this bc it isnt the one we are concerned with
				temp[i] = partial[i];
			}
			i++;
		}
		return temp;
	}
	
	// THE MAIN METHOD TO RUN IT
	public static int[] solveOnce(int[] partial, int[][] data, int numCoupons){
		if(reject(partial, data)){
			return null;
		}
		if(isFullSolution(partial, data)){
			return partial;
		}
		int[] attempt = extend(partial);
		while(attempt != null){
			int[] solution = solveOnce(attempt, data, numCoupons);
			if(solution != null){
				return solution;
			}
			attempt = next(attempt, numCoupons);
		}
		return null;
	}
	
// test methods
	static void testIsFullSolutionUnit(int[] test, int[][] testData){
		if(isFullSolution(test, testData)){
			System.err.println("Full solution:\t" + Arrays.toString(test));
		}else{
			System.err.println("Not full solution:\t" + Arrays.toString(test));
		}
	}
	
	public static void testIsFullSolution(int[][] testData){
		System.err.println("Testing isFullSolution()");
		
		// full solutions
		testIsFullSolutionUnit((new int[] {1,1,2,2,1,3,2,1,1}), testData);
		testIsFullSolutionUnit((new int[] {1,1,2,2,3,4,2,1,1}), testData);
		
		//not full solutions
		testIsFullSolutionUnit((new int[] {0,0,0,0,0,0,0,0,0}), testData);
		testIsFullSolutionUnit((new int[] {1,1,2,3,4,4,1,3,2}), testData);
		testIsFullSolutionUnit((new int[] {0,2,0,2,0,0,0,0,0}), testData);
		testIsFullSolutionUnit((new int[] {1,1,2,2,1,3,2,1,0}), testData);

	}
	static void testRejectUnit(int[] test, int[][] testData){
		if(reject(test, testData)){
			System.err.println("Rejected:\t" + Arrays.toString(test));
		}else{
			System.err.println("Not rejected:\t" + Arrays.toString(test));
		}
	}
	public static void testReject(int[][] testData){
		System.err.println("Testing reject()");
		
		// Should be rejected
		testRejectUnit((new int[] {1,1,2,2,1,3,2,2,2}), testData);
		testRejectUnit((new int[] {1,1,2,2,0,3,2,2,2}), testData);

		
		//should not be rejected
		testRejectUnit((new int[] {0,0,0,0,0,0,0,0,0}), testData);
		testRejectUnit((new int[] {1,1,2,2,1,0,0,0,0}), testData);
		testRejectUnit((new int[] {1,1,2,2,1,3,2,1,1}), testData);

		
	}
	static void testExtendUnit(int[] test){
		System.err.println("Extended " + Arrays.toString(test) + " to " + Arrays.toString(extend(test)));
	}
	public static void testExtend(){
		System.err.println("Testing extend()");

        // Cannot be extended:
		testExtendUnit(new int[] {1,2,3,1,2,3,1,2,3});
		testExtendUnit(new int[] {1,2,3,4,4,3,1,1,2});
		
		// can be extended:
        testExtendUnit(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0});
        testExtendUnit(new int[] {1, 2, 3, 3, 0, 0, 0, 0, 0});
        testExtendUnit(new int[] {1, 2, 3, 3, 2, 1, 2, 2, 0});

	}
	static void testNextUnit(int[] test, int testNumCoup){
		System.err.println("Nexted " + Arrays.toString(test) + " to " + Arrays.toString(next(test, testNumCoup)));
	}
	public static void testNext(){
		System.err.println("Testing next()");
		
		// cannot be next
		testNextUnit((new int[] {1,1,2,2,4,0,0,0,0}), 4);
		testNextUnit((new int[] {1,1,2,2,1,1,1,2,3}), 3);
		
		//can be next
		testNextUnit((new int[] {1,0,0,0,0,0,0,0,0}), 3);
		testNextUnit((new int[] {1,1,2,2,0,0,0,0,0}), 4);
	}
// create test data multi array
	public static int[][] testDataArray(){
		int num = 9;
	
		int[][] data = new int[num][num];
		for(int i=0; i<num; i++){
			for(int j=0; j<num; j++){
				if(i==j){
					data[i][j] = 0;
				}else if(j == i+2){
					data[i][j] = 1;
					data[j][i] = 1;
				}else if(j == i+5){
					data[i][j] = 1;
					data[j][i] = 1;
				}else{
					if(data[i][j]!=1)
					data[i][j] = 0;
				}
				//System.out.print(data[i][j]);
			}
			//System.out.println();
		}
		
		return data;
	}
	
}


