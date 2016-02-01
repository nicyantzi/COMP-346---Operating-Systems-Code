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


README.txt	

	If running from the terminal. Compile the java files within the package (javac *.java). Then run the prorgam from the directory that contains the package itself by typing this in the terminal: 

		java RobinHoods.RobinHoods 10 1000 0 1

	Known Issues: 

		1. My program always runs as if skewed was turned on. Don't have an option for non-skewed. 

		2. My threads create, and at low iterations most of the time the coins will balance out. At higher iterations for some reason I am losing or gaining coins. I have synchornized all of my methods that access the data structure so I am confused as to why this is happening. 
			I know this means that somehwere methods arent working in a synchronized manner. 
		3. Game's run with high numbers of iterations sometimes develop runtime errors where they say NoSuchElementException when I try and remove the head of the linked list, in my code however I have a if statement to check this which works at lower iteration numbers so not sure why it doesnt at higher amounts. 

		A problem with synchronization. 

			For example in a game with just 2 robin's and 2 iterations it worked successfully for 19 times, and on the 20th try I got this result: 

			Nics-MacBook-Air:A1 nic$ java RobinHoods.RobinHoods 2 2 0 0




					** The Robin Fest **
					---------------------

					Total Competitors = 2
					Total Challenge Iterations = 2
					Skew The Odds: No
					Donations Expected: No
					Total Coins Available = 2 * 2 = 4

					Start the Contest!!! 

					START: Robin-0 was in 0 battles, donated 0 coins and has a balance of 2 coins.
					START: Robin-1 was in 0 battles, donated 0 coins and has a balance of 2 coins.
					Donations Flag =0
					Donations Flag =0
					List is empty.
					List is empty.
					1 Attacks 0 with 2 troops for 2 coins.
					0 Attacks 1 with 1 troops for 1 coins.
					List has challenges.
					List has challenges.
					Your (1) battle count: 0
					1 wins 1 coins from0
					Your (0) battle count: 0
					0 lose 2 coins from1
					END:Robin-0 was in 1 battles, donated 0 coins and has a balance of 0 coins.
					END:Robin-1 was in 2 battles, donated 0 coins and has a balance of 5 coins.


					** Competition Summary **
					----------------------------

					Coins Donated = 0.

					Winners List...TBA

					Total Coins in circulation =  Donations (0) + Total Robin Balances (5) = totalCoins (5).
					Total Elapsed time for competition:...TBA


					For some reason the last battle's result doesnt update Robin-O's coin balance, it does with Robin-1 and they are statements that are one right after the other. Also the battle count for robin-1 is correct, but robbin-0 only says it was in 1 battle. This is also confusing to me as these 4 statements to update the Robin's "battlecount" and "coinbalance" are one after the other. 


