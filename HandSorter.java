import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;


public class HandSorter {
	
	public static List<String[][]> processFile (String fileName) throws IOException
	  { 
		List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8); 
		LinkedList<String[][]> output = new LinkedList<String[][]>();
		for(String line : lines){
			String[] temp = line.split(" ");
			if(temp.length != 10){
				throw new RuntimeException("Input file format is incorrect. incorrect line: " + line);
			}
			output.add(new String[][]{ Arrays.copyOfRange(temp, 0, 5),  Arrays.copyOfRange(temp, 5, 10)});
		}
	    return output; 
	  } 
	

	public static void main(String[] args) {
		List<String[][]> result = new LinkedList<>();
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			String[] temp = line.split(" ");
			if(temp.length != 10){
				throw new RuntimeException("Input file format is incorrect. incorrect line: " + line);
			}
			result.add(new String[][]{ Arrays.copyOfRange(temp, 0, 5),  Arrays.copyOfRange(temp, 5, 10)});
		}
		
		int p1win = 0, p2win = 0, tie = 0;
		for(String[][] game : result){
			Hand h1 = Hand.evaluatePattern(game[0]);
			Hand h2 = Hand.evaluatePattern(game[1]);
			
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
}
