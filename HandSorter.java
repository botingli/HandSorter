import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *  Main class for the java program that takes  a stream of input representing two player's Hands and determine who wins.
 * 	Each line of input should look like "9C 9D 8D 7C 3C 2S KD TH 9H 8H", where "9C 9D 8D 7C 3C" represents player 1's hand 
 *  and "2S KD TH 9H 8H" represents player 2's hand.
 *  
 *  After the EOF is received from STDIN, it will output the total count of wins for each player and number of ties through STDOUT.
 *  
 * @author Boting
 */
public class HandSorter {
	
	public static List<String[][]> processInput(){ 
		List<String[][]> result = new LinkedList<>();
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			String[] temp = line.split(" ");
			if(temp.length != 10){
				sc.close();
				throw new RuntimeException("Input file format is incorrect. incorrect line: " + line);
			}
			result.add(new String[][]{ Arrays.copyOfRange(temp, 0, 5),  Arrays.copyOfRange(temp, 5, 10)});
		} 
		sc.close();
		return result;
	 } 
	
	public static void outputResult(List<String[][]> games){
		int p1win = 0, p2win = 0, tie = 0;
		for(String[][] game : games){
			int compareResult = Hand.evaluatePattern(game[0]).compareTo(Hand.evaluatePattern(game[1]));
			if(compareResult == 0){
				// Tie Condition, count win for neither player.  
				tie++;
			}
			if(compareResult > 0){
				p1win ++;
			}
			else{
				p2win ++;
			}
		}
		System.out.printf("player1 win: %s \nplayer2 win: %s \ntie: %s\n", p1win, p2win, tie);
	}
	

	public static void main(String[] args) {
		List<String[][]> games = processInput();
		outputResult(games);
	}
}
