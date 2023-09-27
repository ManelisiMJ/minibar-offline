package com.minibar.model;

import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.PGF;
import org.grammaticalframework.pgf.ParseError;
import org.grammaticalframework.pgf.TokenProb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A Prediction object interacts with a specified grammar, retrieves valid words and also makes predictions
 * based on previously translated sentences.
 */
public class Prediction {
    private static Map<String, ArrayList<String>> historicalData = new HashMap<>();
    private PGF grammar;    //Grammar being used
    private ArrayList<String> previousWords;

    /**
     * Creates a default Prediction Object
     */
    public Prediction() {
    }

    /**
     * Creates a Prediction object with a grammar and existing previousData to make predictions
     *
     * @param grammar       is the grammar being used
     * @param previousWords is the array of previously typed words
     */

    public Prediction(PGF grammar, ArrayList<String> previousWords) {
        this.grammar = grammar;
        if (historicalData.containsKey(grammar.getAbstractName())) {
            historicalData.replace(grammar.getAbstractName(), previousWords);
        } else historicalData.put(grammar.getAbstractName(), previousWords);
        this.previousWords = previousWords;
    }

    /**
     * Creates a Prediction object with the grammar specified
     *
     * @param grammar is the grammar being used
     */
    public Prediction(PGF grammar) {
        this.grammar = grammar;
        if (historicalData.containsKey(grammar.getAbstractName())) {        //If previous words for this grammar already exists
            previousWords = historicalData.get(grammar.getAbstractName());
        } else {  //New grammar with new previous words
            this.previousWords = new ArrayList<>();   //Initialize to empty ArrayList
            historicalData.put(grammar.getAbstractName(), previousWords);
        }
    }

    public ArrayList<String> getPreviousWords() {
        return previousWords;
    }

    /**
     * Stores words in previousWords array
     *
     * @param words is a string of words to store
     */
    public void storeWords(String words) {
        String[] splitWords = words.split(" ");
        for (String splitWord : splitWords) {
            previousWords.remove(splitWord);    //Remove from previous index
            previousWords.add(0, splitWord);    //Insert word in index 0
        }
    }

    /**
     * Stores a complete sentence in previousWords
     *
     * @param sentence is the Sentence object to store
     */
    public void storeWords(Sentence sentence) {
        storeWords(sentence.toString());
    }

    /**
     * Re-orders the legal words, putting the applicable previous words in front
     *
     * @param legalWords is the list of all words that are available in the grammar
     * @return the legal words reordered with previous words at the front
     */
    private ArrayList<String> reOrderLegalWords(ArrayList<String> legalWords) {
        for (int i = previousWords.size() - 1; i >= 0; i--) {
            if (legalWords.contains(previousWords.get(i))) {
                legalWords.remove(previousWords.get(i));   //Remove from previous index
                legalWords.add(0, previousWords.get(i));    //Place at the front (Most-Recently-Used)
            }
        }
        return legalWords;
    }

    /**
     * Suggests the next word from a list of the valid words
     *
     * @param text     typed in by the user
     * @param language the user is translating from
     * @param category selected by the user
     * @return a list of valid words, with most recently used words at the front
     */
    public ArrayList<String> predictNextWord(String text, String language, String category) {
        ArrayList<String> legalWords = new ArrayList<>();
        Concr sourceLanguage = grammar.getLanguages().get(language);
        try {
            Iterable<TokenProb> tokenProbs = sourceLanguage.complete(category, text, "");
            for (TokenProb token : tokenProbs)
                if (!legalWords.contains(token.getToken())) legalWords.add(token.getToken());   //Add legal word
        } catch (ParseError e) {
            return legalWords;  //Return the empty legal words
        }
        if (legalWords.size() == 0)     //No legal words found, sentence is complete
            return legalWords;
        return reOrderLegalWords(legalWords);   //Re-order, putting previous words at the front
    }

    /**
     * @return historical data
     */
    public Map<String, ArrayList<String>> getHistoricalData() {
        return historicalData;
    }

    public void setHistoricalData(Map<String, ArrayList<String>> data) {
        historicalData = data;
    }

    /**
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "Prediction{" + "previousWords=" + previousWords + ", grammar=" + grammar.getAbstractName() + '}';
    }
}
