/*

Nic Yantzi
COMP 346 Operating Systems
Assignment 2 - CPU Scheduling
Due: Sunday July 26th
Prof. Todd Eavis

*/
import java.util.*;
import java.lang.*;

public class Scheduler {

	private static Random rand = new Random();


	public static class BurstWait{
		
		private int burst;
		private int wait;


		public BurstWait(int burst, int wait){
			this.burst = burst;
			this.wait = wait;
		}

		public int getBurst(){
			return this.burst;
		}
		public int getWait(){
			return this.wait;
		}
	}

	public static class WaitQ_StartStop{
		private int process;
		private double stopTime;

		public WaitQ_StartStop(int process, double stopTime){
			this.process = process;
			this.stopTime = stopTime;
		}

		public int getProcess(){
			return this.process;
		}

		public double getStopTime(){
			return this.stopTime;
		}

	}
	public static class ExponentialAvgNode{
		private int process;
		private double expAverage;
		private double startTime;

		public ExponentialAvgNode(int process, double expAverage, double startTime){
			this.process = process;
			this.expAverage = expAverage;
			this.startTime = startTime;
		}
		public int getProcess(){
			return this.process;
		}
		public double getExpAverage(){
			return this.expAverage;
		}
		public double getStartTime(){
			return this.startTime;
		}
		public void setExpAverage(double input){
			this.expAverage = input;
		}
	}
	public static class FCFSNode{
		private int process;
		private double startTime;

		public FCFSNode(int process, double startTime){
			this.process = process;
			this.startTime = startTime;
		}
		public int getProcess(){
			return this.process;
		}
		public double getStartTime(){
			return this.startTime;
		}
	}


	public static void main (String args[]){

		//Scheduler process_count cycle_max
		//take in arguments from command line (process_count (2-20) & cycle_max (2-1000))

		if (args.length != 2){
			System.out.println("\nPlease try again. In order to successfully run program, the correct input at the command line should be: \n");
			System.out.println("Scheduler process_count (2-20) cycle_max (2-1000)");

		}
		else {


			//Create Trace

			int process_count = Integer.parseInt(args[0]);
			int cycle_max = Integer.parseInt(args[1]);


			ArrayList<LinkedList<BurstWait>> processArray = new ArrayList<LinkedList<BurstWait>>();
			ArrayList<LinkedList<BurstWait>> processArray2 = new ArrayList<LinkedList<BurstWait>>();


			//structure to store wait times for each process, initialize to the right number of linked lists each storing 0
			ArrayList<LinkedList<Double>> fcfsAverages = new ArrayList<LinkedList<Double>>();
			ArrayList<LinkedList<Double>> sjfAverages = new ArrayList<LinkedList<Double>>();

			for(int i =0; i< process_count; i++){
				LinkedList<Double> temp = new LinkedList<Double>();
				double zero = 0;
				temp.add(zero);
				fcfsAverages.add(temp);
			}

			for(int i =0; i< process_count; i++){
				LinkedList<Double> temp = new LinkedList<Double>();
				double zero = 0;
				temp.add(zero);
				sjfAverages.add(temp);
			}


			System.out.println("\n\nNic Yantzi - Assignment 2: CPU Scheduling\n\n");
			System.out.println("\n\nList of Processes and their respective burst/wait cycles: \n\n");
			//for every process
			for (int i = 0; i < process_count; i++){
				
				int cycleLength = rand.nextInt(cycle_max)+1;
				LinkedList<BurstWait> cycleList = new LinkedList<BurstWait>();
				LinkedList<BurstWait> cycleList2 = new LinkedList<BurstWait>();

				processArray.add(cycleList);
				processArray2.add(cycleList2);

				int burstRoot = rand.nextInt(10)+1;

				//for every cycle within a process
				
				for (int n = 0; n < cycleLength; n++){
					int wait = rand.nextInt(1000)+1;
					int burst = burstRoot*100 + (rand.nextInt(10)+1);
					BurstWait cycle = new BurstWait(burst, wait);
					cycleList.add(cycle);
					cycleList2.add(cycle);

					System.out.println("Process "+ i+"<"+burst+","+wait+">");
				}
			}
			System.out.println("\n\n");

			//test section get the 2nd elements of both lists and print as test to make sure identical

			// for (int i = 0; i < process_count; i++){
			// 	int burst = ((processArray.get(i)).getFirst()).getBurst();
			// 	int wait = ((processArray.get(i)).getFirst()).getWait();

			// 	int burst2 = ((processArray2.get(i)).getFirst()).getBurst();
			// 	int wait2 = ((processArray2.get(i)).getFirst()).getWait();

			// 	System.out.println("Comparing... <"+burst+","+wait+"> <"+burst2+","+wait2+">");
			// }

			//now have the two identical traces. 
			//FCFS

			//initialize wait(has nothing) and ready(has everything) queues. 
			LinkedList<FCFSNode> readyQ = new LinkedList<FCFSNode>();
			LinkedList<WaitQ_StartStop> waitQ = new LinkedList<WaitQ_StartStop>();


			double clock = 0.0;

			for (int index = 0; index < process_count; index++){
				FCFSNode temp = new FCFSNode(index, clock);
				readyQ.add(temp);
			}


			//

			System.out.println("\n*************************************");
			System.out.println("  First Come First Served Scheduler ");
			System.out.println("*************************************");


			while(readyQ.size() != 0 || waitQ.size() !=0){

				System.out.println("\nCurrent ReadyQ:");
				for(int n = 0; n < readyQ.size(); n++){
					int processRQ = (readyQ.get(n)).getProcess();
					int cycles = (processArray.get(processRQ)).size();
					System.out.println(" Process "+processRQ+", Cycle Count "+cycles);
				}
				System.out.println("\nCurrent Wait Q:");
				for(int q = 0; q < waitQ.size(); q++){
					int processWQP = (waitQ.get(q)).getProcess();
					int cycles = (processArray.get(processWQP)).size();
					System.out.println(" Process "+processWQP+", Cycle Count "+cycles);

				}

				if(readyQ.size() !=0){
					int currentProcess = (readyQ.getFirst()).getProcess();
					double startTime = (readyQ.pollFirst()).getStartTime();
					BurstWait currentCycle = (processArray.get(currentProcess)).pollFirst();
					int burst = currentCycle.getBurst();
					int wait = currentCycle.getWait();
					int remainingCycles = (processArray.get(currentProcess)).size();

					//add wait time to averages structure

					(fcfsAverages.get(currentProcess)).add(clock);

					double waitTime = clock - startTime;

					System.out.println("\n\nRunning Process "+currentProcess+"...");
					System.out.println("  wait time in readyQ (ms) : "+waitTime);
					System.out.println("  burst time : "+burst);
					System.out.println("  remaining cycles : "+remainingCycles);

					if(remainingCycles == 0){
						System.out.println(" process has terminated.");
						clock = clock + burst;
					}
					else{
						System.out.println(" process moved to waitQ for "+wait+"ms");
						double stopTime = clock + wait;
						clock = clock + burst;

						WaitQ_StartStop temp = new WaitQ_StartStop(currentProcess, stopTime);

						waitQ.add(temp);
					}				
				}

				//readyQ is empty, need to deal with processes in waitQ

				if ( (readyQ.size()==0) && (waitQ.size() > 0) ){
					double lowestStopTimeWQ = (waitQ.getFirst()).getStopTime();

					for(int p = 0; p<waitQ.size(); p++){
						double stopTimeWQCheck = (waitQ.get(p)).getStopTime();
						if(lowestStopTimeWQ > stopTimeWQCheck){
							lowestStopTimeWQ = stopTimeWQCheck;
						}
					}
					clock = clock + lowestStopTimeWQ;
				}

				System.out.println("\nChecking waitQ...");

				boolean flag = false;

				for (int i=0; i< waitQ.size(); i++){
					int processWQ = (waitQ.get(i)).getProcess();
					double stopTimeWQ = (waitQ.get(i)).getStopTime();

					//System.out.println("Clock Time: "+clock+" StopTime: "+stopTimeWQ);

					if(stopTimeWQ <= clock){
						double timeOver = stopTimeWQ - clock;
						System.out.println(" process " + processWQ+" wait time "+timeOver);
						System.out.println(" moving process "+processWQ+" to readyQ");
						FCFSNode temp = new FCFSNode(processWQ, clock);
						readyQ.add(temp);
						waitQ.remove(i);
						flag = true;
					}

				}

				if (flag == false){
					System.out.println(" no processes ready to be moved to readyQ.");
				}

				System.out.println("\n** Scheduling Iteration Complete");

			}
			System.out.println("\nCurrent ReadyQ:");
			System.out.println("\nCurrent Wait Q:");
			System.out.println("\n\nWe are done the FCFS Scheduling, all processes have terminated.\n\n");


			//////////////////////////////SJF

			clock = 0;

			LinkedList<ExponentialAvgNode> readyQ2 = new LinkedList<ExponentialAvgNode>();
			LinkedList<WaitQ_StartStop> waitQ2 = new LinkedList<WaitQ_StartStop>();
			ArrayList<ExponentialAvgNode> sjfExpAvg = new ArrayList<ExponentialAvgNode>();

			for (int index = 0; index < process_count; index++){
				ExponentialAvgNode temp = new ExponentialAvgNode(index, 500.0, clock);
				readyQ2.add(temp);
				sjfExpAvg.add(temp);
			}

			System.out.println("\n*************************************");
			System.out.println("  Shortest Job First Scheduler ");
			System.out.println("*************************************");


			

			while(readyQ2.size() != 0 || waitQ2.size() !=0){

				//System.out.println("Clock Time: " +clock);
				System.out.println("\nCurrent ReadyQ:");
				for(int n = 0; n < readyQ2.size(); n++){
					int processRQ = (readyQ2.get(n)).getProcess();
					int cycles = (processArray2.get(processRQ)).size();
					System.out.println(" Process "+processRQ+", Cycle Count "+cycles+", Estimated Burst: "+(readyQ2.get(n)).getExpAverage());
				}
				System.out.println("\nCurrent Wait Q:");
				for(int q = 0; q < waitQ2.size(); q++){
					int processWQP = (waitQ2.get(q)).getProcess();
					int cycles = (processArray2.get(processWQP)).size();
					System.out.println(" Process "+processWQP+", Cycle Count "+cycles);

				}
				// for(int i =0; i < sjfExpAvg.size(); i++){
				// 	System.out.println("Expected Burst for "+i+" "+(sjfExpAvg.get(i)).getExpAverage());
				// }

				if(readyQ2.size() !=0){

					int currentProcess=(readyQ2.get(0)).getProcess();
					int index=0;
					double expAvg=(readyQ2.get(0)).getExpAverage();
					double startTime = (readyQ2.get(0)).getStartTime();

					for(int i = 0; i < readyQ2.size(); i++){
						int tempProcess = (readyQ2.get(i)).getProcess();
						double tempExpAvg = (readyQ2.get(i)).getExpAverage();
						double tempStartTime = (readyQ2.get(i)).getStartTime();

						if(tempExpAvg < expAvg){
							expAvg = tempExpAvg;
							currentProcess = tempProcess;
							startTime = tempStartTime;
							index = i;
						}
					}
					for(int i = 0; i < readyQ2.size(); i++){
						int processID = (readyQ2.get(i)).getProcess();
						if(processID == currentProcess){
							readyQ2.remove(i);
						}
					}

					// System.out.println("\n\ncurrentProcess:"+currentProcess);
					// System.out.println("current index: "+index+"\n\n");

					BurstWait currentCycle = (processArray2.get(currentProcess)).pollFirst();
					int burst = currentCycle.getBurst();
					int wait = currentCycle.getWait();
					int remainingCycles = (processArray2.get(currentProcess)).size();


					//add wait time to averages structure and update exponential averaging array
					double oldAverage = (sjfExpAvg.get(currentProcess)).getExpAverage();
					double newAverage = oldAverage + (0.5 * (burst - oldAverage));

					// System.out.println("Old Average= "+oldAverage);
					// System.out.println("New Average= "+newAverage);


					(sjfExpAvg.get(currentProcess)).setExpAverage(newAverage);

					double waitTime = clock - startTime;

					(sjfAverages.get(currentProcess)).add(waitTime);

					System.out.println("\n\nRunning Process "+currentProcess+"...");
					System.out.println("  wait time in readyQ (ms) : "+ waitTime);
					System.out.println("  burst time : "+burst);
					System.out.println("  remaining cycles : "+remainingCycles);

					if(remainingCycles == 0){
						System.out.println("  process has terminated.");
						clock = clock + burst;
					}
					else{
						System.out.println("  process moved to waitQ for "+wait+"ms");
						double stopTime = clock + wait;
						clock = clock + burst;

						WaitQ_StartStop temp = new WaitQ_StartStop(currentProcess, stopTime);

						waitQ2.add(temp);
					}				
				}

				//readyQ is empty, need to deal with processes in waitQ

				if ( (readyQ2.size()==0) && (waitQ2.size() > 0) ){
					double lowestStopTimeWQ = (waitQ2.getFirst()).getStopTime();
					//System.out.println("lowestStopTimeWQ = "+lowestStopTimeWQ+ " Clock Time: "+ clock);

					for(int p = 0; p<waitQ2.size(); p++){
						double stopTimeWQCheck = (waitQ2.get(p)).getStopTime();
						if(lowestStopTimeWQ > stopTimeWQCheck){
							lowestStopTimeWQ = stopTimeWQCheck;
						}
					}
					clock = clock + lowestStopTimeWQ;
				}

				System.out.println("\nChecking waitQ...");

				boolean flag = false;

				for (int i=0; i< waitQ2.size(); i++){

					int processWQ = (waitQ2.get(i)).getProcess();
					int index = 0;
					for (int n = 0; n < sjfExpAvg.size(); n++){
						if((sjfExpAvg.get(n)).getProcess() == processWQ){
							index = n;
						}
					}
					double expAvgAddBack = (sjfExpAvg.get(index)).getExpAverage();
					ExponentialAvgNode addBack = new ExponentialAvgNode(processWQ, expAvgAddBack, clock);
					double stopTimeWQ = (waitQ2.get(i)).getStopTime();

					//System.out.println("Clock Time: "+clock+" StopTime: "+stopTimeWQ);

					if(stopTimeWQ <= clock){
						double timeOver = stopTimeWQ - clock;
						System.out.println("  process " + processWQ+" wait time "+timeOver);
						System.out.println("  moving process "+processWQ+" to readyQ");
						readyQ2.add(addBack);
						waitQ2.remove(i);
						flag = true;
					}

				}

				if (flag == false){
					System.out.println(" no processes ready to be moved to readyQ.");
				}

				System.out.println("\n** Scheduling Iteration Complete");

			}

			System.out.println("\nCurrent ReadyQ:");
			System.out.println("\nCurrent Wait Q:");
			System.out.println("\n\nWe are done the SJF Scheduling, all processes have terminated.\n\n");


/////////////////////////


			System.out.println("\n***************************************");
			System.out.println("Results: Average Wait Time per Process");
			System.out.println("***************************************\n");

			for(int i=0; i < process_count; i++){

				double processAverageFCFS;
				double processAverageSJF;
				double totalCycles = (double)(fcfsAverages.get(i)).size();
				double totalCycles2 = (double)(sjfAverages.get(i)).size();
				
				double fcfsSum = 0;
				double sjfSum = 0;

				for(int n =0; n< (fcfsAverages.get(i)).size(); n++){
					double temp = (fcfsAverages.get(i)).get(n);

					fcfsSum = fcfsSum + temp;
				}

				for(int n =0; n< (sjfAverages.get(i)).size(); n++){
					double temp = (sjfAverages.get(i)).get(n);
					sjfSum = sjfSum + temp;
				}

				processAverageFCFS = fcfsSum/totalCycles;
				processAverageSJF = sjfSum/totalCycles2;

				System.out.println("Process "+i);
				System.out.println(" FCFS: "+((int)processAverageFCFS));
				System.out.println(" SJF:  "+((int)processAverageSJF));

			}
			System.out.println("\n");
		}
	}
}