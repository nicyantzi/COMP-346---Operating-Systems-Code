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



// challenge node - for details of a challenge, i.e. troops, coins, and challenger ID. 

public class ChallengeNode {
	private int challengerID;
	private int troops;
	private int wager;

	public ChallengeNode(int challengerID, int troops, int wager){
		this.challengerID = challengerID;
		this.troops = troops;
		this.wager = wager;
	}

	public int getChallengerID(){
		return this.challengerID;
	}
	public int getTroops(){
		return this.troops;
	}
	public int getWager(){
		return this.wager;
	}
}