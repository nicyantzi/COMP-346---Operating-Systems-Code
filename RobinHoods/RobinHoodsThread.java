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


public class RobinHoodsThread extends Thread{

	private String name;
	private static ArrayList<LinkedList<ChallengeNode>> robinListChallenges;
	private static ArrayList<ProfileNode> robinListProfiles;
	private static int threads; 
	private static int iterationCount;
	private static int donationsFlag;
	private static int totalDonations;

	RobinHoodsThread(String name, ArrayList<LinkedList<ChallengeNode>> robinListChallenges, ArrayList<ProfileNode> robinListProfiles, int threads, int iterationCount, int donationsFlag, int totalDonations){
		this.name = name;
		this.robinListChallenges = robinListChallenges;
		this.robinListProfiles = robinListProfiles;
		this.threads = threads;
		this.iterationCount = iterationCount;
		this.donationsFlag = donationsFlag;
		this.totalDonations = totalDonations;
	}

	private static Random rand = new Random();

	public synchronized int currentThreadID(){
		String currentThreadName = name;
		currentThreadName = currentThreadName.replace("Robin-","");
		int currentThreadIndex = Integer.parseInt(currentThreadName);
		return currentThreadIndex;
	}

	public synchronized void addToDonations(int amount){
		//System.out.println("HERE. In the addToDonations mehtod."); 
		totalDonations = totalDonations + amount;
		//System.out.println("Total Donations: " +totalDonations);
	}

	public synchronized int challengeCheck (){
		int index = currentThreadID();
		//System.out.println("Current Thread Index = " + index);
	 	LinkedList<ChallengeNode> list = robinListChallenges.get(index);

		
	 	//check to see if list is empty, if it is then no challenges present. 
	 	if (list.isEmpty()){
	 		//System.out.println("List is empty.");
	 		return 1;
	 	}
	 	else {
	 		//System.out.println("List has challenges.");
			ChallengeNode current = list.removeFirst();
			//ChallengeNode current = list.getFirst();

			//view challenge and respond. 
			int challengerID = current.getChallengerID();
			int challengerTroops = current.getTroops();
			int challengerWager = current.getWager(); 

			int youTroops = rand.nextInt(index+1)+1;


			//System.out.println("Current Challenge = "+challengerID+ " challenges you for "+ challengerWager+" coins with "+challengerTroops+" troops.");
	 		
	 		//respond to this challenge

	 		ProfileNode youProfile = robinListProfiles.get(index);
	 		ProfileNode challengerProfile = robinListProfiles.get(challengerID);
	 		

	 		//Profile Details For You
	 		int youID = youProfile.getRobinID();
	 		int youCoinBalance = youProfile.getCoinBalance();
	 		int youDonations = youProfile.getDonations();
	 		int youBattleCount = youProfile.getBattleCount();

	 		//System.out.println("Your (" + youID+ ") battle count: " +youBattleCount);

	 		//Profile Details for Challenger
	 		int chalID = challengerProfile.getRobinID();
	 		int chalCoinBalance = challengerProfile.getCoinBalance();
	 		int chalDonations = challengerProfile.getDonations();
	 		int chalBattleCount = challengerProfile.getBattleCount();

	 		//checks for if robin still has money, and money is greater than or equal to wager
	 		if (chalCoinBalance !=0 && chalCoinBalance >= challengerWager){
	 			if((youCoinBalance !=0) && (youCoinBalance >= challengerWager)){

	 				//troop BATTLE! check to see who has more troops
	 				
	 				//Case 1: You win, you get the challengers Wager from him/her. 
	 				if (youTroops > challengerTroops){
	 					youCoinBalance = youCoinBalance + challengerWager;
	 					chalCoinBalance = chalCoinBalance - challengerWager;
	 					youBattleCount = youBattleCount+1;
	 					chalBattleCount = chalBattleCount+1;
	 					



	 					//System.out.println(youID + " wins "+challengerWager+" coins from "+ chalID);




	 					//update the values in both profiles. 
	 					(robinListProfiles.get(index)).setCoinBalance(youCoinBalance);
	 					(robinListProfiles.get(challengerID)).setCoinBalance(chalCoinBalance);
	 					(robinListProfiles.get(index)).setBattleCount(youBattleCount);
	 					(robinListProfiles.get(challengerID)).setBattleCount(chalBattleCount);

	 					//1 donations on, 0 donations off
	 					if(donationsFlag==1){
	 						//donate 10% of wealth
	 						if(youBattleCount %10 ==0){
	 							//System.out.println("Donations are going to work.......................");
	 							int donationAmount = (int)(youCoinBalance*(10.0/100.0f));
	 							//System.out.println("donation amount = "+donationAmount);

	 							youDonations = youDonations + donationAmount;
	 							youCoinBalance = youCoinBalance - donationAmount;

	 							(robinListProfiles.get(index)).setDonations(youDonations);
	 							(robinListProfiles.get(index)).setCoinBalance(youCoinBalance);

	 						}
	 						if(chalBattleCount %10 ==0){
	 							int donationAmount = (int)(chalCoinBalance*(10.0/100.0f));
	 							//System.out.println("donation amount = "+donationAmount);

	 							chalDonations = chalDonations + donationAmount;
	 							chalCoinBalance = chalCoinBalance - donationAmount;


	 							(robinListProfiles.get(challengerID)).setDonations(chalDonations);
	 							(robinListProfiles.get(challengerID)).setCoinBalance(chalCoinBalance);
	 						}
	 					}
	 				}
	 				//Case 2: You lose, challenger gets the wager from you. 
	 				if (youTroops < challengerTroops){
	 					youCoinBalance = youCoinBalance - challengerWager;
	 					chalCoinBalance = chalCoinBalance + challengerWager;
	 					youBattleCount = youBattleCount+1;
	 					chalBattleCount = chalBattleCount+1;
	 					


	 					//System.out.println(youID + " lose "+challengerWager+" coins from "+ chalID);


	 					//update the values in both profiles. 
	 					(robinListProfiles.get(index)).setCoinBalance(youCoinBalance);
	 					(robinListProfiles.get(challengerID)).setCoinBalance(chalCoinBalance);
	 					(robinListProfiles.get(index)).setBattleCount(youBattleCount);
	 					(robinListProfiles.get(challengerID)).setBattleCount(chalBattleCount);


	 					//1 donations on, 0 donations off
	 					if(donationsFlag==1){
	 						//donate 10% of wealth
	 						if(youBattleCount %10 ==0){
	 							int donationAmount = (int)(youCoinBalance*(10.0/100.0f));
	 							//System.out.println("donation amount = "+donationAmount);
	 							
	 							youDonations = youDonations + donationAmount;
	 							youCoinBalance = youCoinBalance - donationAmount;

	 							(robinListProfiles.get(index)).setDonations(youDonations);
	 							(robinListProfiles.get(index)).setCoinBalance(youCoinBalance);


	 						}
	 						if(chalBattleCount %10 ==0){
	 							int donationAmount = (int)(chalCoinBalance*(10.0/100.0f));
	 							//System.out.println("donation amount = "+donationAmount);
	 							chalDonations = chalDonations + donationAmount;
	 							chalCoinBalance = chalCoinBalance - donationAmount;


	 							(robinListProfiles.get(challengerID)).setDonations(chalDonations);
	 							(robinListProfiles.get(challengerID)).setCoinBalance(chalCoinBalance);
	 						}
	 					}
	 				}
	 				//Case 3: Tie: End battle, no change of coins.
	 				if (youTroops == challengerTroops){

	 					//System.out.println("You tie");

	 					youBattleCount = youBattleCount+1;
	 					chalBattleCount = chalBattleCount+1;

						(robinListProfiles.get(index)).setBattleCount(youBattleCount);
	 					(robinListProfiles.get(challengerID)).setBattleCount(chalBattleCount);

	 					//1 donations on, 0 donations off
	 					if(donationsFlag==1){
	 						//donate 10% of wealth
	 						if(youBattleCount %10 ==0){
	 							int donationAmount = (int)(youCoinBalance*(10.0/100.0f));
	 							youDonations = youDonations + donationAmount;
	 							youCoinBalance = youCoinBalance - donationAmount;

	 							(robinListProfiles.get(index)).setDonations(youDonations);
	 							(robinListProfiles.get(index)).setCoinBalance(youCoinBalance);

	 						}
	 						if(chalBattleCount %10 ==0){
	 							int donationAmount = (int)(chalCoinBalance*(10.0/100.0f));
	 							chalDonations = chalDonations + donationAmount;
	 							chalCoinBalance = chalCoinBalance - donationAmount;


	 							(robinListProfiles.get(challengerID)).setDonations(chalDonations);
	 							(robinListProfiles.get(challengerID)).setCoinBalance(chalCoinBalance);
	 						}
	 					}
	 				}
	 			}
	 		}
			return 0;
	 	}
	}

	public synchronized void makeChallenge (){
		int attacker = currentThreadID();
		int possibleDefenders = threads;
		int defender = attacker;

		//check to see if attacker is bankrupt, if he is, end. if not continue. 

		int attackerCoinBalance = (robinListProfiles.get(attacker)).getCoinBalance();

		if (attackerCoinBalance > 0){ 

			while (defender == attacker){
				defender = rand.nextInt(possibleDefenders);
			}

			int coins = rand.nextInt(10)+1;

			while(coins > attackerCoinBalance){
				coins = rand.nextInt(attackerCoinBalance+1)+1;
			}

			int troops = rand.nextInt(attacker+1)+1;

			//System.out.println(attacker+" Attacks " + defender + " with "+troops+" troops for "+coins+" coins.");

			ChallengeNode challenge = new ChallengeNode(attacker, troops, coins);

			//check to see if challenge node is being created properly. 
			// int coins2 = challenge.getWager();
			// int troops2 = challenge.getTroops();
			// int attacker2 = challenge.getChallengerID();
			// System.out.println(attacker2+" Attacks " + defender + " with "+troops2+" troops for "+coins2+" coins.");

			//have new challenge node now must add to challenge list data structure at correct place. 
			(robinListChallenges.get(defender)).add(challenge);
		}
		else return;
	}

	public void run (){

		//code for thread to run
		//System.out.println("Running  " + name);
		//GAME ACTUALLY STARTS HERE... 

		//System.out.println("Donations Flag =" + donationsFlag);
		//Check if there are challenges against you. (This needs to be synchronized).

		//Make a challenge (This needs to be synchronized).
		for (int i =0; i<iterationCount; i++){

			//challengeCheck returns 1 if list is Empty and ready to proceed to making a challenge
			//returns 0 if there are still challenges in list. 

			//while (challengeCheck() == 0){
			if (challengeCheck() == 1){
				makeChallenge();
			}
		}
	}
}