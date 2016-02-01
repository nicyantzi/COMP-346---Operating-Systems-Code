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


//account node - each robin's ID, coin balance and (troops are random between 1 and thread ID!!!)

public class ProfileNode {
	private int robinID;
	private int coinBalance;
	private int donations;
	private int battleCount;

	public ProfileNode(int robinID, int coinBalance, int donations, int battleCount){
		this.robinID = robinID;
		this.coinBalance = coinBalance;
		this.donations = donations;
		this.battleCount = battleCount;
	}
	public int getRobinID(){
		return this.robinID;
	}
	public int getCoinBalance(){
		return this.coinBalance;
	}
	public int getDonations(){
		return this.donations;
	}
	public int getBattleCount(){
		return this.battleCount;
	}

	public void setCoinBalance(int coinBalance){
		this.coinBalance = coinBalance;
	}
	public void setDonations(int donations){
		this.donations = donations;
	}
	public void setBattleCount(int battleCount){
		this.battleCount = battleCount;
	}
}
