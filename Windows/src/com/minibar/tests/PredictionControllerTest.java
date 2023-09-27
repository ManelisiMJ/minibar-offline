package com.minibar.tests;

import com.minibar.controller.PredictionController;
import com.minibar.model.Prediction;
import com.minibar.model.Sentence;
import org.grammaticalframework.pgf.PGF;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PredictionControllerTest {

    /**
     * Tests if the controller is able to correctly store words in the prediction
     */
    @Test
    void testStoreSentence() throws FileNotFoundException {
        PGF grammar = PGF.readPGF("grammars/Foods.pgf");
        //Create predictionController with the specified grammar
        PredictionController predictionController = new PredictionController(grammar, new ArrayList<>());
        predictionController.storeWords(new Sentence("I love grammatical framework"));
        assertEquals("[framework, grammatical, love, I]", predictionController.getPreviousWords().toString());
    }

    /**
     * Tests if the controller is able to correctly store words in the prediction
     */
    @Test
    void testStoreSentence2() throws FileNotFoundException {
        PGF grammar = PGF.readPGF("grammars/Foods.pgf");
        //Create predictionController with the specified grammar
        PredictionController predictionController = new PredictionController(new Prediction(grammar));
        predictionController.storeWords(new Sentence("World Hello"));
        assertEquals("[Hello, World]", predictionController.getPreviousWords().toString());
    }

    /**
     * Tests if the controller can use the prediction object to provide predictions
     */
    @Test
    void testPredictNextWord() throws FileNotFoundException {
        PGF grammar = PGF.readPGF("grammars/Foods.pgf");
        PredictionController predictionController = new PredictionController(new Prediction(grammar));
        predictionController.storeWords("boring expensive warm");
        ArrayList<String> actual = predictionController.predictNextWord("these", "FoodsEng", grammar.getStartCat());
        //Warm, expensive and boring get stored at the front of the array in most recently used order
        String expected = "[warm, expensive, boring, pizzas, cheeses, wines, fish, Italian, fresh, delicious, very]";
        assertEquals(expected, actual.toString());
    }

    /**
     * Tests if the controller can use the prediction object to provide predictions
     */
    @Test
    void testPredictNextWord2() throws FileNotFoundException {
        PGF grammar = PGF.readPGF("grammars/Foods.pgf");
        ArrayList<String> previousWords = new ArrayList<>();
        previousWords.add("those");
        Prediction prediction = new Prediction(grammar, previousWords);
        PredictionController predictionController = new PredictionController(prediction);
        ArrayList<String> actual = predictionController.predictNextWord("", "FoodsEng", grammar.getStartCat());
        //those gets stored at the front of the array in most recently used order
        String expected = "[those, that, these, this]";
        assertEquals(expected, actual.toString());
    }
}