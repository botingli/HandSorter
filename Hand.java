import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;

/**
 * Represents a Hand of poker, with combination and values information included.
 * Implements @Comarable interface. 
 * The main purpose for this class is to represent a Hand and conveniently compare with other Hand objects. 
 * 
 * @author Boting
 *
 */
public class Hand implements Comparable<Hand>{

	// The list of values to compare when need to break tie between to same combination of hands.
	// The sequence of criticalValues is based on hand combination and poker rules.
	// The size of critical value list should always be same between two combinations.
	private List<Integer> criticalValues;
	// The type of the combination of Hand. 
	private Combination combination;
	
	// Use enum and map to represent the combination and corresponding rank for better readability.
	// Royal flush is considered as a special kind of Straight Flush.
	enum Combination { 
		HIGH_CARD, PAIR, TWO_PAIRS, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH; 
    }
	public static final HashMap<Combination, Integer> combinationRankMap = new HashMap<Combination, Integer>();
	static {
	 	combinationRankMap.put(Combination.HIGH_CARD, 1);
		combinationRankMap.put(Combination.PAIR, 2);
		combinationRankMap.put(Combination.TWO_PAIRS, 3);
		combinationRankMap.put(Combination.THREE_OF_A_KIND, 4);
		combinationRankMap.put(Combination.STRAIGHT, 5);
		combinationRankMap.put(Combination.FLUSH, 6);
		combinationRankMap.put(Combination.FULL_HOUSE, 7);
		combinationRankMap.put(Combination.FOUR_OF_A_KIND, 8);
		combinationRankMap.put(Combination.STRAIGHT_FLUSH, 9);
	}
	
	//Map to convert Card;s character notation to numeric value
	//T = 10, J = 11, Q = 12, K = 13, A = 14
	public static final HashMap<Character, Integer> CharacterValueMap = new HashMap<Character, Integer>();
	static {
		CharacterValueMap.put('T', 10);
		CharacterValueMap.put('J', 11);
		CharacterValueMap.put('Q', 12);
		CharacterValueMap.put('K', 13);
		CharacterValueMap.put('A', 14);
	}
		
	Hand(Combination c, List<Integer> v){
		combination = c;
		criticalValues = v;
	}
	
	/*
	 *  Compare two hands first on the rank of Combinations. If the combinations are same then compare
	 *  the card values based on Texas poker rules. 
	 *  return 0 for a tie. 
	 */
	@Override
	public int compareTo(Hand otherHand){
		int rankDiff = combinationRankMap.get(this.combination) - combinationRankMap.get(otherHand.combination);	
		if(rankDiff == 0){
			for(int i = 0; i < this.criticalValues.size(); i ++){
				int valueDiff = this.criticalValues.get(i) - otherHand.criticalValues.get(i);
				if(valueDiff != 0){
					return valueDiff;
				}
				// Continue to the next critical value until there is a difference. 
			}
		}
		return rankDiff;
	}

	/*
	 *	Takes and analyzes an array of Strings representing the 5 cards of a hand. Then
	 *	creates and returns the Hand object based on the hand's combination and card values. 
	 */
	public static Hand evaluatePattern (String[] hand){
		if(hand.length != 5){
			throw new RuntimeException("Input must be a String array of size 5");
		}
		boolean isFlush = true, isStraight = true;
		HashMap <Integer, Integer> countMap = new HashMap<>();	// Map to keep the count of occurrence for each value 
		Integer [] values = new Integer[5];		// Array of sorted card values of the hand
		for(int i = 0; i < hand.length; i++){
			char c = hand[i].charAt(0);
			values[i] = (Character.isDigit(c)) ? c - '0' 
					: CharacterValueMap.get(c);
			countMap.put(values[i], countMap.getOrDefault(values[i], 0) + 1);
		}
		//Determine if the hand is flush or straight		
		Arrays.sort(values);
		for(int i = 1; i < hand.length; i++){
			isStraight &= values[i] - values[i-1] == 1;
			isFlush &= hand[i].charAt(1) == hand[i-1].charAt(1);
		}
		
		// For each (value, count) entry, first sort the entries by the count, then by the value.
		PriorityQueue<Entry<Integer, Integer>> maxHeap = new PriorityQueue<>( (a, b) -> {
            if(a.getValue() != b.getValue()) 
                return b.getValue() - a.getValue();
            return b.getKey() - a.getKey();
		});
		maxHeap.addAll(countMap.entrySet());
		
		//Create Hand object based on the information of values, counts and suites analyzed above. 
		LinkedList<Integer> criticalValues = new LinkedList<Integer>();
		if(isStraight && isFlush){		//Straight Flush
			criticalValues.addLast(values[4]);	//Only the highest value card is needed for tie breaking
			return new Hand(Combination.STRAIGHT_FLUSH, criticalValues);
		}
		else {
			if(isFlush){		//Flush
				criticalValues.addAll(Arrays.asList(values));	
				Collections.reverse(criticalValues);	//All 5 card values from large to small
				return new Hand(Combination.FLUSH, criticalValues);
			}
			if(isStraight){		//Straight
				criticalValues.addLast(values[4]);	//Only the highest value card is needed for tie breaking
				return new Hand(Combination.STRAIGHT, criticalValues);
			}
		}
		
		Entry<Integer, Integer> highestCount, secoundHighestCount;
		highestCount = maxHeap.poll();
		secoundHighestCount = maxHeap.poll();
		if (highestCount.getValue() == 4){		//Four of a Kind
			criticalValues.add(highestCount.getKey());
			criticalValues.add(secoundHighestCount.getKey());
			return new Hand(Combination.FOUR_OF_A_KIND, criticalValues);
		}
		else if (highestCount.getValue() == 3){
			if (secoundHighestCount.getValue() == 2){	//Full House
				criticalValues.add(highestCount.getKey());
				criticalValues.add(secoundHighestCount.getKey());
				return new Hand(Combination.FULL_HOUSE, criticalValues);
			}
			else{	//Three of a kind
				criticalValues.add(highestCount.getKey());
				criticalValues.add(secoundHighestCount.getKey());
				criticalValues.add(maxHeap.poll().getKey());
				return new Hand(Combination.THREE_OF_A_KIND, criticalValues);
			}
		}
		else if(highestCount.getValue() == 2){
			if (secoundHighestCount.getValue() == 2){	// Two Pairs
				criticalValues.add(highestCount.getKey());
				criticalValues.add(secoundHighestCount.getKey());
				criticalValues.add(maxHeap.poll().getKey());
				return new Hand(Combination.TWO_PAIRS, criticalValues);
			}
			else{	// One Pair
				criticalValues.add(highestCount.getKey());
				criticalValues.add(secoundHighestCount.getKey());
				criticalValues.add(maxHeap.poll().getKey());
				criticalValues.add(maxHeap.poll().getKey());
				return new Hand(Combination.PAIR, criticalValues);
			}
		}
		else{	//High card
			criticalValues.addAll(Arrays.asList(values));	
			Collections.reverse(criticalValues);	//All 5 card values from large to small
			return new Hand(Combination.HIGH_CARD, criticalValues);
		}
	}
	
	public String toString(){
		return combination.name() + ", " + criticalValues.toString();
	}

}
