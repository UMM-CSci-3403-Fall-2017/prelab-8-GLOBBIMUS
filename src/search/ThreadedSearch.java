package search;

import java.util.ArrayList;

public class ThreadedSearch<T> implements Runnable {

	private T target;
	private ArrayList<T> list;
	private int begin;
	private int end;
	private Answer answer;

	public ThreadedSearch() {
	}

	private ThreadedSearch(T target, ArrayList<T> list, int begin, int end, Answer answer) {
		this.target=target;
		this.list=list;
		this.begin=begin;
		this.end=end;
		this.answer=answer;
	}

	/**
	 * Searches `list` in parallel using `numThreads` threads.
	 *
	 * You can assume that the list size is divisible by `numThreads`
	 */



	public boolean parSearch(int numThreads, T target, ArrayList<T> list) throws InterruptedException {
		Answer answer = new Answer(); //creating the instance of answer
		Thread[] threads  = new Thread[numThreads]; //The array of threads
		int sectionSize = list.size()/numThreads; //Calculates the size of each section for parallel search
		int[][] sectionPositions  = new int[2][numThreads]; //Double array which contains the begin and end indexes

		//This loop will find all start and end indexes for searching
		for(int i = 0, y = 0; i < numThreads; i++){ 
			sectionPositions[0][i] = y;
			y = y + sectionSize -1; //Increment by section size
			sectionPositions[1][i] = y;
			y++;
		}

		// This loop for creating the threads
		for(int i = 0; i < numThreads; i++){ 
			threads[i] = new Thread(new ThreadedSearch(target, list, sectionPositions[0][i], sectionPositions[1][i], answer));
			threads[i].start();
		}

		//Lets wait until everyone is done
		for (int i=0; i < numThreads; i++) { 
			threads[i].join();
		}

		//now we can get the answer
		return answer.getAnswer(); 
	}

	public void run() {
		System.out.println(begin);
		System.out.println(end);
		//Simple linear search for each thread 
		for(int i = begin; i <= end; i++){
			if(list.get(i).equals(target)) {
				answer.setAnswer(true); //Once we find the target we change the answer to be true.
			}
		}
	}

	private class Answer {
		private boolean answer = false;
		public boolean getAnswer() {
			return answer;
		}

		// This has to be synchronized to ensure that no two threads modify
		// this at the same time, possibly causing race conditions.
		public synchronized void setAnswer(boolean newAnswer) {
			answer = newAnswer;
		}
	}

}
