# HandSorter
A java program that takes string representation of two player's Hands and determine who wins.
This program takes a stream of input, each line should look like "9C 9D 8D 7C 3C 2S KD TH 9H 8H", where "9C 9D 8D 7C 3C" 
represents player 1's hand and "2S KD TH 9H 8H" represents player 2's hand. 
After the EOF is received from STDIN, it will output the total count of wins for each plyaer and number of ties through STDOUT.

## Instruction to run demo
Run the following command in unix terminal:
$ javac HandSorter.java
$ cat poker-hands.txt | java HandSorter

