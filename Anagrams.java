/*
  File: Anagrams.java
  Author: Liam Staudinger
  Course: CSC 210, Fall 2024
  Purpose: This program processes a file containing a list of valid words and finds all possible anagrams 
  for a given word. It generates combinations of characters, checks them against the valid words, and 
  outputs all valid anagrams.
  
  To run the program, provide command line arguments indicating a file with a list of valid words, a phrase (without spaces) to find anagrams of, 
  and a limit on the number of words in the found anagrams (0 indicates no limit).
  For instance:
  java Anagrams words1.txt barbarabush 0
 */

package com.gradescope.anagrams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

public class Anagrams {
	public static HashSet<String> getWordList(String wordList) throws FileNotFoundException {
		/*
		    Reads a file and builds a set of valid words
		    
		    Parameters: 
		    - String wordList: The name of the file containing the list of valid words.
		    
		    Returns: 
		    - HashSet<String> validWords: A set of valid words.
		    
		    Throws:
		    - FileNotFoundException
		 */
		HashSet<String> validWords = new HashSet<String>();
		File wordFile = new File(wordList);
		Scanner fileReader = new Scanner(wordFile);
		while (fileReader.hasNextLine()) {
			String word = fileReader.nextLine();
			validWords.add(word);
		}
		fileReader.close();
		return validWords;
	}
	
	public static ArrayList<Character> getChars(String word) {
		/*
	        Converts a word into a list of characters
	        
	        Parameters: 
	        - String word: The word to be converted.
	        
	        Returns: 
	        - ArrayList<Character> allChars: A list of characters from the word.
       */
		ArrayList<Character> allChars = new ArrayList<Character>();
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			allChars.add(c);
		}
		return allChars;
	}
	
	public static void getCombinations(ArrayList<Character> allChars, String word, 
										HashSet<String> validWords, HashSet<String> solutions) {
		/*
		    Recursively generates all combinations of characters and checks if they are valid words
		    using exhaustive search
		    
		    Parameters: 
		    - ArrayList<Character> allChars: A list of characters to generate combinations from.
		    - String word: The current combination of characters.
		    - HashSet<String> validWords: A set of valid words.
		    - HashSet<String> solutions: A set to store valid combinations.
	   */
		
		// Base Case: if the word we have created is in the list of valid words, add it to solutions
		if (validWords.contains(word)) {
			solutions.add(word);
		}
		// Loop through each character, add it to a string, and recursively call without that character
		ArrayList<Character> remainingChars = new ArrayList<Character>(allChars);
		for (int i = 0; i < allChars.size(); i++) {
			char c = allChars.get(i);
			remainingChars.remove((Character) c);
			String newWord = word + c;
			getCombinations(remainingChars, newWord, validWords, solutions);
			remainingChars.add(c);
		}
	}
	
	public static String removeChars (String word, String toRemove) {
		/*
		    Removes characters from a word
		    
		    Parameters: 
		    - String word: The original word.
		    - String toRemove: The characters to remove from the word.
		    
		    Returns: 
		    - String newWord: The word after removing specified characters.
	   */
		String newWord = "";
		ArrayList<Character> charArray = new ArrayList<Character>();
		// Create an ArrayList of characters in word
		for (char c : word.toCharArray()) {
			charArray.add(c);
		}
		// Remove the characters in toRemove from ArrayList
		for (char c : toRemove.toCharArray()) {
			int i = charArray.indexOf(c);
			if (i != -1) {
				charArray.remove(i);
			}
		}
		// Add characters from modified ArrayList into new string
		for (char c : charArray) {
			newWord += c;
		}
		return newWord;
	}
	
	public static void getAnagrams(int length, ArrayList<String> orderedSolution, String word, ArrayList<String> result, 
									int maxAnas, int count, ArrayList<ArrayList<String>> allResults) {	
		 /*
		    Recursively finds all anagrams of a word using backtracking
		    
		    Parameters: 
		    - int length: The length of the original word.
		    - ArrayList<String> orderedSolution: A list of words that can be generated from word, sorted in order.
		    - String word: The current word being processed.
		    - ArrayList<String> result: A list to store the current anagram.
		    - int maxAnas: The maximum number of anagrams to find.
		    - int count: The current count of characters processed.
		    - ArrayList<ArrayList<String>> allResults: A list to store all found anagrams.
	   */
		
		// Base case: We've reached the maximum number of anagrams or we have no more characters to work with
		if (maxAnas == 0 || word.length() == 0) {
			// Base case: If the number of characters in the anagram matches the number of characters in word, 
			// an anagram has been found
			if (count == length) {
				ArrayList<String> copy = new ArrayList<String>(result);
				allResults.add(copy); 
			}
		} else {
			// Loop through each word that can be generated from word
			for (String solution : orderedSolution) {
				String newWord = removeChars(word, solution);
				// If characters have been properly removed from word, recurse with solution removed from orderedSolution
				// and the characters in solution removed from word
				if (word.length() - newWord.length() == solution.length()) {
					ArrayList<String> copySolution = new ArrayList<String>(orderedSolution);
					result.add(solution);	
					copySolution.remove(solution);
					getAnagrams(length, copySolution, newWord, result, maxAnas - 1, count + solution.length(), allResults);
					result.remove(result.size() - 1);
				}
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		  /*
	        Main function to run the program.
	        
	        Parameters: 
	        - String[] args: Command line arguments.
	        
	        Returns:
	        - None
	        
	        Throws:
	        - FileNotFoundException
       */
	    String wordList = args[0];
	    String word = args[1];
	    int maxAnas = Integer.valueOf(args[2]);
	    if (maxAnas == 0) maxAnas = -1;  // set to -1 for no limit
	
	    System.out.println("Phrase to scramble: " + word);
	    
	    HashSet<String> validWords = getWordList(wordList);
	    HashSet<String> solutions = new HashSet<String>();
	    ArrayList<Character> allChars = getChars(word);
	    
	    getCombinations(allChars, "", validWords, solutions);
	    ArrayList<String> orderedSolution = new ArrayList<String>(solutions);
	    Collections.sort(orderedSolution);
	    
	    System.out.println("\nAll words found in " + word + ":");
	    System.out.println(orderedSolution);
	    
	    ArrayList<String> result = new ArrayList<String>();
	    System.out.println("\nAnagrams for " + word + ":");
	    ArrayList<ArrayList<String>> allResults = new ArrayList<ArrayList<String>>();
	    getAnagrams(word.length(), orderedSolution, word, result, maxAnas, 0, allResults);
	    for (int i = 0; i < allResults.size(); i++) System.out.println(allResults.get(i));
    }
}
