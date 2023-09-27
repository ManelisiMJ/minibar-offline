package com.minibar.controller;

import com.minibar.model.Prediction;
import com.minibar.model.Sentence;
import org.grammaticalframework.pgf.PGF;
import java.util.ArrayList;
import java.util.Map;

/**
 * This controller handles everything related to predictions
 */
public class PredictionController {
    private final Prediction prediction;

    /**
     * Creates a controller with a default Prediction object
     */
    public PredictionController() {
        this.prediction = new Prediction();
    }
    /**
     * Creates a controller with the Prediction object specified
     *
     * @param prediction is the Prediction object used
     */
    public PredictionController(Prediction prediction) {
        this.prediction = prediction;
    }

    /**
     * Initializes a controller with a new Prediction object created from the grammar specified
     *
     * @param grammar is the grammar used in the Prediction object
     */
    public PredictionController(PGF grammar) {
        this.prediction = new Prediction(grammar);
    }

    /**
     * Creates a controller with a new Prediction object created from the grammar and previous words
     *
     * @param grammar      is the grammar used to create the Prediction object
     * @param previousData is the array of previous words stored in the Prediction object
     */
    public PredictionController(PGF grammar, ArrayList<String> previousData) {
        this.prediction = new Prediction(grammar, previousData);
    }

    /**
     * Provides access to the previous data in the prediction
     * @return the ArrayList of previous words
     */
    public ArrayList<String> getPreviousWords(){
        return prediction.getPreviousWords();
    }

    /**
     * Adds a new string sentence to the previous words
     *
     * @param sentence is the sentence to be added
     */
    public void storeWords(String sentence) {
        prediction.storeWords(sentence);
    }

    /**
     * Adds a new Sentence object to the previous words
     *
     * @param sentence is the Sentence object being added
     */
    public void storeWords(Sentence sentence) {
        prediction.storeWords(sentence);
    }

    /**
     * Gets the history data of the previous words
     */
    public Map<String, ArrayList<String>> getHistoricalData() {
        return prediction.getHistoricalData();
    }

    /**
     * Sets the historical data of the previous words
     *
     * @param data Map of previous words user used for each grammar
     */
    public void setHistoricalData(Map<String, ArrayList<String>> data) {
       prediction.setHistoricalData(data);
    }
    /**
     * Returns an array of valid words, with the leading words being the suggested words
     *
     * @param text     is the text used to get the valid words from then on
     * @param language is the language that text was typed in
     * @param startCat is the start category
     * @return a list of valid words, with the suggested words at the front
     */
    public ArrayList<String> predictNextWord(String text, String language, String startCat) {
        return prediction.predictNextWord(text, language, startCat);
    }
}
