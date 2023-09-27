package com.minibar.tests;

import com.minibar.model.Prediction;
import com.minibar.model.Sentence;
import org.grammaticalframework.pgf.PGF;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PredictionTest {

    /**
     * Tests the valid words that can be typed after typing "these"
     */
    @Test
    void testTokens() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Prediction prediction = new Prediction(grammar);
            ArrayList<String> actual = prediction.predictNextWord("these", "FoodsEng", grammar.getStartCat());
            String[] exp = {"pizzas", "cheeses", "wines", "fish", "Italian", "boring", "warm", "fresh", "delicious", "very", "expensive"};
            ArrayList<String> expected = new ArrayList<>(Arrays.asList(exp));
            assertEquals(expected, actual);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Tests the valid words that can be typed, without having typed anything
     */
    @Test
    void testTokens2() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Prediction prediction = new Prediction(grammar);
            ArrayList<String> actual = prediction.predictNextWord("", "FoodsEng", grammar.getStartCat());
            String[] exp = {"that", "these", "this", "those"};
            ArrayList<String> expected = new ArrayList<>(Arrays.asList(exp));
            assertEquals(expected, actual);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Tests the valid words that can be typed, without having typed anything in Dutch
     */
    @Test
    void testTokens3() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Prediction prediction = new Prediction(grammar);
            ArrayList<String> actual = prediction.predictNextWord("", "FoodsDut", grammar.getStartCat());
            assertEquals("[die, deze]", actual.toString());
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Tests the valid words that can be typed after typing "thes"
     */
    @Test
    void testInvalidToken() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Prediction prediction = new Prediction(grammar);
            //The word "thes" raises a ParseError, method returns empty ArrayList
            ArrayList<String> actual = prediction.predictNextWord("thes", "FoodsEng", grammar.getStartCat());
            ArrayList<String> expected = new ArrayList<>(); //Empty ArrayList
            assertEquals(expected, actual);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Tests to see if previous word "those" will be the first word in the list of valid words
     */
    @Test
    void testPredictNextWord() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            ArrayList<String> previousWords = new ArrayList<>();
            previousWords.add("those");
            Prediction prediction = new Prediction(grammar, previousWords);
            ArrayList<String> actual = prediction.predictNextWord("", "FoodsEng", grammar.getStartCat());
            //those gets stored at the front of the array in most recently used order
            String[] exp = {"those", "that", "these", "this"};
            ArrayList<String> expected = new ArrayList<>(Arrays.asList(exp));
            assertEquals(expected, actual);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Tests that the list of previous words will be suggested first
     */
    @Test
    void testPredictNextWord2() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Prediction prediction = new Prediction(grammar);
            prediction.storeWords("boring expensive warm");
            ArrayList<String> actual = prediction.predictNextWord("these", "FoodsEng", grammar.getStartCat());
            //Warm, expensive and boring get stored at the front of the array in most recently used order
            String[] exp = {"warm", "expensive", "boring", "pizzas", "cheeses", "wines", "fish", "Italian", "fresh", "delicious", "very"};
            ArrayList<String> expected = new ArrayList<>(Arrays.asList(exp));
            assertEquals(expected, actual);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Tests that the list of previous words will be suggested first
     */
    @Test
    void testPredictNextWord3() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Prediction prediction = new Prediction(grammar);
            prediction.storeWords("boring expensive warm");
            ArrayList<String> actual = prediction.predictNextWord("these boring cheeses are warm", "FoodsEng", grammar.getStartCat());
            assertEquals("[]", actual.toString());  //Sentence is complete, no predictions
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Tests that words are stored in most recently used order
     */
    @Test
    void testStoreWords() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Prediction prediction = new Prediction(grammar, new ArrayList<>());
            prediction.storeWords(new Sentence("world Hello"));
            String expected = "[Hello, world]";  //Most recently used word first
            String actual = prediction.getPreviousWords().toString();
            assertEquals(expected, actual);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Tests toString() method
     */
    @Test
    void testToString() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Prediction prediction = new Prediction(grammar);
            String expected = "Prediction{previousWords=[], grammar=Foods}";
            String actual = prediction.toString();
            assertEquals(expected, actual);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }
}