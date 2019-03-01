/*
Thomas Mease
Matrix Multiplecation
Grissom
CS-371

*/

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyMatrixMultiply implements Runnable{

	final int[][] a;
	final int[][] b;
	final int[][] matrix;
	final int i;
	final int j;


	public static void main(String [] args){
		int aDim1 = Integer.parseInt(args[0]);
		int aDim2 = Integer.parseInt(args[1]);
		int bDim1 = Integer.parseInt(args[2]);
		int bDim2 = Integer.parseInt(args[3]);
		
		if(aDim2==bDim1){
			int[][]a = generateRandomMatrix(aDim1,aDim2);
			int[][]b = generateRandomMatrix(bDim1,bDim2);
		
			long start = System.currentTimeMillis();
			int[][]product = multiply(a,b);
			long end = System.currentTimeMillis();
			System.err.println("Multiplying with one thread took " + ((float)(end - start) / 1000F) + " seconds.");
			
			start = System.currentTimeMillis();
			int[][] threadedProduct = multiplyThreaded(a,b);
			end = System.currentTimeMillis();
			System.err.println("Multiplying with threads took " + ((float)(end - start) / 1000F) + " seconds.");
			
			int[][]check = MatrixMultiply.multiply(a,b);
			int[][]threadedCheck = MatrixMultiply.multiplyThreaded(a,b);
			
			System.err.println("The single threaded answer match Grissom's: "+matrixCheck(product,check));
			System.err.println("The multi-threaded answer match Grissom's: "+matrixCheck(product,threadedCheck));
			
		}else{
			System.err.println("Matrix dimensions not compatible for multiplication.");
			System.exit(1);
		}
	}
	
	public MyMatrixMultiply(final int[][] a, final int[][] b, final int[][] matrix, final int i,final int j){
		this.a=a;
		this.b=b;
		this.matrix=matrix;
		this.i=i;
		this.j=j;
	}
	
	public static int[][] multiply(int[][] a, int[][] b){
		int [][] product = new int [a.length][b[0].length];
		
		for(int i =0; i<a.length; i++){
			for(int j=0; j<b[0].length; j++){
				int sum = 0;
				for(int k =0; k<a[0].length; k++){
					sum=sum+a[i][k]*b[k][j];
				}
				product[i][j]=sum;
			}
		}
		return product;
	}
	
	public static int[][] multiplyThreaded(int [][] a, int [][] b){
		int[][] product = new int[a.length][b[0].length];
		
		//ExecutorService executor = Executors.newWorkStealingPool(4);
		//ExecutorService executor = Executors.newCachedThreadPool();
		ExecutorService executor = Executors.newFixedThreadPool(4);
		
		ArrayList<Runnable> threads = new ArrayList<>();
		
		for(int i =0; i<a.length; i++){
			for(int j=0; j<b[0].length; j++){
				Runnable thread = new MyMatrixMultiply(a,b,product,i,j);
				threads.add(thread);
			}
		}
		
		for(int i=0;i<threads.size();i++){
			executor.execute(threads.get(i));
		}
		
		executor.shutdown();
		try { 
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch(InterruptedException e) {
			System.err.println(e.getMessage());
		}
		
		return product;
	}
	
	public static int dotProd(int[][] a,int[][] b, int i, int j){
		int sum = 0;
		for(int k =0;k<a[0].length;k++){
			sum+=a[i][k]*b[k][j];
		}
		
		return sum;
	}
	
	public static final int[][] generateRandomMatrix(int rows, int cols) {
		int[][] matrix = new int[rows][cols];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
			matrix[i][j] = (int)(Math.random() * 100F);
			}
		}
		return matrix;
    }
	
	public static void printMatrix(int[][] matrix) {
		for(int i = 0; i < matrix.length; i++) {
			System.out.println(Arrays.toString(matrix[i]));
		}
		System.out.println();
	}
	
	public static boolean matrixCheck(int[][] a, int[][] b){
		boolean isGood = false;
			for(int i=0;i<a.length;i++){
				for(int j=0;j<a[0].length;j++){
					if(a[i][j]!=b[i][j]){
						isGood = false;
						return isGood;
					}else{
						isGood=true;
					}
					
				}
			}
		return isGood;
	}
	
	public void run(){
		this.matrix[i][j]=dotProd(this.a,this.b,this.i,this.j);
	}
}