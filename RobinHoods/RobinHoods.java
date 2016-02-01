/*************************************************
**************************************************
**												**
**		NIC YANTZI								**
**		COMP 346 Operating Systems				**
**		Summer 2015 - Assignment 1				**
**		Prof. Todd Eavis						**
**												**
**************************************************
*************************************************/
package RobinHoods;


import java.util.*;
import java.lang.*;


public class RobinHoods {
	//data structure to hold all of the Robin-X's list of Challenges. 
	private static ArrayList<LinkedList<ChallengeNode>> robinListChallenges = new ArrayList<LinkedList<ChallengeNode>>();

	//list of robin's... 
	private static ArrayList<ProfileNode> robinListProfiles = new ArrayList<ProfileNode>();

	private static int totalDonations = 0;

	public static int threads = 0;

	private static int donationsFlag = 0;

	
	public static void addToRobinListChallenges(LinkedList<ChallengeNode> challengeList){
		robinListChallenges.add(challengeList);
	}
	public static void addToRobinListProfiles(ProfileNode profile){
		robinListProfiles.add(profile);
	}

	public static void main(String args[]){

		//error checking for correct number of inputs at command line
		long startTime = System.currentTimeMillis();

		if (args.length != 4){
			System.out.println("\nPlease try again. In order to successfully run program, the correct input at the command line should be: \n");
			System.out.println("RobinHoods thread_count iteration_count skew_odds donations \n");
			System.exit(0);
		}

		//Read in Command Line Arguments for Parameters

		int threadCount = Integer.parseInt(args[0]);
		int iterationCount = Integer.parseInt(args[1]);
		int skewOdds = Integer.parseInt(args[2]);
		int donations = Integer.parseInt(args[3]);


		threads = threadCount;

		//Skew Odds and Donations Yes/No for print. 
		String skewOddsWord = "";
		String donationsWord = "";
		
		//Word for skewOdds
		if (skewOdds == 0){
			skewOddsWord = "No";
		}
		else if (skewOdds == 1){
			skewOddsWord = "Yes";
		}
		else{
			System.out.println("\nPlease try again. Your input to skew the odds must be 1 (Yes) or 0 (No).");
			System.exit(0);
		}

		//word for donations
		if (donations == 0){
			donationsWord = "No";
			donationsFlag = 0;

		}
		else if (donations == 1){
			donationsWord = "Yes";
			donationsFlag = 1;
		}
		else{
			System.out.println("\nPlease try again. Your input for donations must be 1 (Yes) or 0 (No).\n");
			System.exit(0);
		}
		
		int totalStartCoins = threadCount*iterationCount;
		//Start Print Dialog:

		String startDialog =
			"\n\n** The Robin Fest **\n"
			+"---------------------\n"
			+"\nTotal Competitors = " + threadCount+"\n"
			+"Total Challenge Iterations = " + iterationCount+ "\n"
			+"Skew The Odds: "+ skewOddsWord +"\n"
			+"Donations Expected: "+ donationsWord + "\n"
			+"Total Coins Available = " + threadCount + " * " + iterationCount + " = " + totalStartCoins +"\n\n" 
			+"Start the Contest!!! \n\n";

		System.out.print(startDialog);
		
		//Create the Correct Number of Threads

		//based on thread count input, create the appropriate number of threads/Robin-X's. 
		RobinHoodsThread[] threadArray = new RobinHoodsThread[threadCount];


		for (int i = 0; i < threadCount; i++){
			
			//instantiate thread
			RobinHoodsThread x = new RobinHoodsThread ("Robin-" +i, robinListChallenges, robinListProfiles, threads, iterationCount, donationsFlag, totalDonations);

			threadArray[i] = x;

			//instantiate challenge list for robin and add to arraylist of challenges. 
			LinkedList<ChallengeNode> individualListChallenges = new LinkedList<ChallengeNode>();
			addToRobinListChallenges(individualListChallenges);

			//instantiate profile and add to the arraylist of profiles

			ProfileNode tempProfile = new ProfileNode(i, iterationCount, 0, 0);
			addToRobinListProfiles(tempProfile);
		}
		for (int i =0; i <threadCount; i++){
			int id = (robinListProfiles.get(i)).getRobinID();
			int coinBalance = (robinListProfiles.get(i)).getCoinBalance();
			int individualDonations = (robinListProfiles.get(i)).getDonations();
			int battleCount = (robinListProfiles.get(i)).getBattleCount();
			//totalDonations = totalDonations + individualDonations;
			//finalCoinBalance = finalCoinBalance + coinBalance;
		
		System.out.println("START: Robin-"+id+" was in "+battleCount+" battles, donated "+individualDonations+" coins and has a balance of "+coinBalance+" coins.");
		}

		System.out.println("\n\n");


		for (int i = 0; i < threadCount; i++){
			//start threads
			threadArray[i].start();
		}

		try{
			for (int i = 0; i < threadCount; i++){
			//join threads
			threadArray[i].join();
			}
		} catch (InterruptedException e) {

		}
		int[] winners = new int[threadCount];


		int finalCoinBalance = 0;
		for (int i =0; i <threadCount; i++){
			int id = (robinListProfiles.get(i)).getRobinID();
			int coinBalance = (robinListProfiles.get(i)).getCoinBalance();
			int individualDonations = (robinListProfiles.get(i)).getDonations();
			int battleCount = (robinListProfiles.get(i)).getBattleCount();
			totalDonations = totalDonations + individualDonations;
			finalCoinBalance = finalCoinBalance + coinBalance;





			System.out.println("END:Robin-"+id+" was in "+battleCount+" battles, donated "+individualDonations+" coins and has a balance of "+coinBalance+" coins.");
		}

		int totalCoins = totalDonations + finalCoinBalance;
		long stopTime = System.currentTimeMillis();

		long totalTime = stopTime - startTime;
		long totalTimeSecond = totalTime/1000;




		String finishDialog =
			"\n\n** Competition Summary **\n"
			+"----------------------------\n"
			+"\nCoins Donated = "+ totalDonations+".\n"
			+"\nWinners List: Above ^\n"
			+"\nTotal Coins in circulation =  Donations ("+totalDonations+") + Total Robin Balances ("+finalCoinBalance+") = totalCoins ("+totalCoins+").\n"
			+"Total Elapsed time for competition: " + totalTimeSecond+ " seconds.\n\n\n";
		System.out.println(finishDialog);



	}
}