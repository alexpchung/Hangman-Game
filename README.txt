This is a Java program that plays the Hangman game.

Running the Program:
1) The program reads a dictionary file=words.txt in the local directory
2) The program takes an optional command-line argument as the number of random words to score.
  	The default number of random words is 1000.
  	
Example 1: java -jar Hangman.jar
Example 2: java -jar Hangman.jar 15

Example Output:
1) Secret word=HONEYCOMB; Game So Far=HONEYCOMB; score=5; status=GAME_WON
2) Secret word=REARMICE; Game So Far=REARMICE; score=4; status=GAME_WON
3) Secret word=THRALDOM; Game So Far=THRALDOM; score=6; status=GAME_WON
4) Secret word=SHAVER; Game So Far=SHAVER; score=9; status=GAME_WON
5) Secret word=SPEARHEADS; Game So Far=SPEARHEADS; score=2; status=GAME_WON
6) Secret word=MAXIS; Game So Far=-A--S; score=25; status=GAME_LOST
7) Secret word=METASTASIS; Game So Far=METASTASIS; score=4; status=GAME_WON
8) Secret word=REMODEL; Game So Far=REMODEL; score=5; status=GAME_WON
9) Secret word=RETINOBLASTOMATA; Game So Far=RETINOBLASTOMATA; score=2; status=GAME_WON
10) Secret word=DIPLOPIC; Game So Far=DIPLOPIC; score=7; status=GAME_WON
11) Secret word=SEMIPARASITE; Game So Far=SEMIPARASITE; score=3; status=GAME_WON
12) Secret word=SHEERER; Game So Far=SHEERER; score=4; status=GAME_WON
13) Secret word=HUMBLING; Game So Far=--MBLI--; score=25; status=GAME_LOST
14) Secret word=STRUGGLER; Game So Far=STRUGGLER; score=5; status=GAME_WON
15) Secret word=HITCHHIKE; Game So Far=HITCHHIKE; score=5; status=GAME_WON
------------------------
Games Won: 13, Games Lost: 2
Total Score: 111
Execution Time (sec): 2.21



