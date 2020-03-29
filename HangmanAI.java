import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Iterator;

public class HangmanAI {

	private Map<Integer, List<String>> wordList = new HashMap<Integer, List<String>>();
	private Set<Character> correct = new HashSet<Character>();
	private Set<Character> incorrect = new HashSet<Character>();
	private Map<Integer,List<String>> prepList = new HashMap<Integer,List<String>>();
	// create a list of words from SCOWL (http://wordlist.sourceforge.net/)
	HangmanAI() {
		try {
				File f = new File("words.txt");
				File f1 = new File("prep.txt");
				BufferedReader reader = new BufferedReader(new FileReader(f));
				for (String str; (str = reader.readLine()) != null;) {
					str = str.trim();
					if (!wordList.containsKey(str.length())) {
						wordList.put(str.length(), new ArrayList<String>());
					}
					wordList.get(str.length()).add(str);
				}

				BufferedReader reader1 = new BufferedReader(new FileReader(f1));
				for (String str; (str = reader.readLine()) != null;) {
					str = str.trim();
					if (!prepList.containsKey(str.length())) {
						prepList.put(str.length(), new ArrayList<String>());
					}
					prepList.get(str.length()).add(str);
				}
			
			} catch (IOException e) {
				System.err.println(e);
			}

	}

	// make a guess based on the state
	public char makeGuess(String state) {
		List<String> state_list = new ArrayList<String>(Arrays.asList(state
				.split("[^A-Z_']+")));
		StringBuilder excluding = new StringBuilder();

		for (Iterator<Character> ex = incorrect.iterator(); ex.hasNext();) {
			excluding.append(ex.next());
		}
		List<String> possibleWords = new ArrayList<String>();

		int count = Integer.MAX_VALUE;
		int length = Integer.MAX_VALUE;
		String guessing = null;
		int underscore_counter = 0;
		double maxScore = 0.0;
		double score = 0;

		//guess the smallest word with the least unknown letters 
		for (String word : state_list) { 

			for (int i = 0; i < word.length(); i++) {
				if ('_' == word.charAt(i)) {
					underscore_counter++;
				}
			}
			score = (double)(1.0 * (word.length() - underscore_counter) / word.length());
			
			if(score > maxScore && score != 1.0)
			{
				guessing = word;
				count = underscore_counter;
				maxScore = score;	
			}
			else if ((underscore_counter < count && underscore_counter != 0 )) {
				guessing = word;
				count = underscore_counter;
			}
			else if(underscore_counter == count && word.length() < guessing.length() && underscore_counter != 0)
			{
				guessing = word;
				count = underscore_counter;
			}
			underscore_counter = 0;
		}

		//if the length is 1, then most likely letters are 'a' and 'i'
		if(guessing.length() == 1){
			return (!correct.contains('a') && !incorrect.contains('a')) ? 'a' : 'I';
		}

		//guessing the one with least number of '_'
		String word = guessing.toLowerCase();
		Pattern regex = Pattern.compile(word.replace(
				"_",
				(excluding.length() > 0) ? String.format("[a-z&&[^%s]]",
						excluding) : "[a-z]"));
		if (wordList.containsKey(word.length())) {
			for (String guess : wordList.get(word.length())) {
				Matcher match = regex.matcher(guess);
				if (match.find()) {
					possibleWords.add(guess);
				}
			}
		}

		// separate list for prepositions as these have a higher score chance
		if (prepList.containsKey(word.length())) {
			for (String guess : prepList.get(word.length())) {
				Matcher match = regex.matcher(guess);
				if (match.find()) {
					possibleWords.add(guess); // get a list of words that match the state
				}
			}
		}

		//count the frequency of each letter within the possible words
		Map<Character, Integer> frequency = new HashMap<Character, Integer>();
		for (String possible : possibleWords) {
			Set<String> letters = new HashSet<String>();
			for (char letter : possible.toCharArray()) {
				if (!letters.contains(letter)) {
					if (!frequency.containsKey(letter)) {
						frequency.put(letter, 1);
					} else {
						frequency.put(letter, frequency.get(letter) + 1);
					}
				}
			}
		}

		//find the letter with highest frequency
		char guessLetter = 'a';
		int freq = 0;
		boolean no_letter = true;
		for (char c = 'a'; c <= 'z'; c++) {
			if (!correct.contains(c) && !incorrect.contains(c)) {
				if (frequency.get(c) != null && frequency.get(c) > freq) {
					guessLetter = c;
					freq = frequency.get(c);
					no_letter = false;
				}
			}
		}

		//when there's no letter left possible to guess
		if (no_letter) {
			for (char c = 'a'; c <= 'z'; ++c) {
				if (!(correct.contains(c) || incorrect.contains(c))) {
					return c;
				}
			}
		}
		return guessLetter;
	}

	// put the guessed letters into appropriate group
	public void update(char guess, boolean success) {
		if (success) {
			correct.add(guess);
		} else {
			incorrect.add(guess);
		}
	}
}