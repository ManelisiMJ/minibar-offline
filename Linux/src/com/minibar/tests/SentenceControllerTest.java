package com.minibar.tests;

import org.grammaticalframework.pgf.*;
import com.minibar.controller.SentenceController;
import com.minibar.model.Sentence;
import org.junit.jupiter.api.Test;


import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class SentenceControllerTest {
    /**
     * Tests the getter for the Sentence object
     */
    @Test
    void testGetSentence() {
        Sentence sentence = new Sentence("those boring cheeses are warm");
        SentenceController controller = new SentenceController(sentence);
        String expected = "those boring cheeses are warm";
        String actual = controller.getSentence().toString();
        assertEquals(expected, actual);
    }

    /**
     * Tests that words are appended correctly
     */
    @Test
    void testAppendWord() {
        Sentence sentence = new Sentence("those boring cheeses are warm");
        SentenceController controller = new SentenceController(sentence);
        controller.appendWord("and");
        String expected = "those boring cheeses are warm and";
        String actual = controller.getSentence().toString();
        assertEquals(expected, actual);
    }

    /**
     * Tests that multiple words are appended correctly
     */
    @Test
    void testAppendWord2() {
        Sentence sentence = new Sentence("those boring cheeses are warm");
        SentenceController controller = new SentenceController(sentence);
        controller.appendWord("and sweet");
        controller.appendWord("a");
        controller.appendWord("lot");
        String expected = "those boring cheeses are warm and sweet a lot";
        String actual = controller.getSentence().toString();
        assertEquals(expected, actual);
    }

    /**
     * Tests that multiple words are appended correctly
     */
    @Test
    void testAppendWord3() {
        Sentence sentence = new Sentence();
        SentenceController controller = new SentenceController(sentence);
        controller.appendWord("Hello");
        String actual = controller.getSentence().toString();
        assertEquals("Hello", actual);
    }

    /**
     * Tests the deletion of a word
     */
    @Test
    void testDeleteWord() {
        Sentence sentence = new Sentence("those boring cheeses are warm");
        SentenceController controller = new SentenceController(sentence);
        controller.deleteWord();
        String expected = "those boring cheeses are";
        String actual = controller.getSentence().toString();
        assertEquals(expected, actual);
    }

    /**
     * Tests the deletion of a word when the sentence has only one word
     */
    @Test
    void testDeleteWord2() {
        Sentence sentence = new Sentence("those");
        SentenceController controller = new SentenceController(sentence);
        controller.deleteWord();
        String expected = "";
        String actual = controller.getSentence().toString();
        assertEquals(expected, actual);
    }

    /**
     * Tests the deletion of a word from an empty sentence
     */
    @Test
    void testDeleteWord3() {
        Sentence sentence = new Sentence("");
        SentenceController controller = new SentenceController(sentence);
        controller.deleteWord();
        String expected = "";
        String actual = controller.getSentence().toString();
        assertEquals(expected, actual);
    }

    /**
     * Test case to verify if sentence isComplete
     */
    @Test
    void testIsComplete1() {
        try {
            PGF gr = PGF.readPGF("grammars/Foods.pgf");
            Sentence sentence = new Sentence("that");
            SentenceController controller = new SentenceController(sentence);
            String category = gr.getStartCat();
            boolean actual = controller.isComplete(gr, "FoodsEng", category);
            assertFalse(actual);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test case to verify if sentence isComplete
     */
    @Test
    void testIsComplete2() {
        try {
            PGF gr = PGF.readPGF("grammars/Foods.pgf");
            Sentence sentence = new Sentence("");
            SentenceController controller = new SentenceController(sentence);
            String category = gr.getStartCat();
            boolean actual = controller.isComplete(gr, "FoodsEng", category);
            assertFalse(actual);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test case to verify if sentence isComplete
     */
    @Test
    void testIsComplete3() {
        try {
            PGF gr = PGF.readPGF("grammars/Foods.pgf");
            Sentence sentence = new Sentence("this boring cheese is expensie"); //invalid sentence
            SentenceController controller = new SentenceController(sentence);
            String category = gr.getStartCat();
            boolean actual = controller.isComplete(gr, "FoodsEng", category);
            assertFalse(actual);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test case to verify if sentence isComplete
     */
    @Test
    void testIsComplete4() {
        try {
            PGF gr = PGF.readPGF("grammars/Foods.pgf");
            Sentence sentence = new Sentence("these boring cheeses are delicious");
            SentenceController controller = new SentenceController(sentence);
            String category = gr.getStartCat();
            boolean actual = controller.isComplete(gr, "FoodsEng", category);
            assertTrue(actual);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test case to verify if sentence isComplete
     */
    @Test
    void testIsComplete5() {
        try {
            PGF gr = PGF.readPGF("grammars/Foods.pgf");
            Sentence sentence = new Sentence("these boring cheeses are delicious these");
            SentenceController controller = new SentenceController(sentence);
            String category = gr.getStartCat();
            boolean actual = controller.isComplete(gr, "FoodsEng", category);
            assertFalse(actual);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test case to verify generating random sentence
     */
    @Test
    void testRandomGenerator1() {
        try {
            PGF gr = PGF.readPGF("grammars/Foods.pgf");
            SentenceController controller = new SentenceController(new Sentence());
            String category = gr.getStartCat();
            controller.randomSentence(gr, "FoodsEng", category);
            String randomSentence = controller.getSentence().toString();
            System.out.println(randomSentence);
            assertNotEquals("", randomSentence);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test case to verify generating random sentence when user has typed in words
     */
    @Test
    void testRandomGenerator2() {
        try {
            PGF gr = PGF.readPGF("grammars/Foods.pgf");
            Sentence sentence = new Sentence("this is");
            String category = gr.getStartCat();
            SentenceController controller = new SentenceController(sentence);
            controller.randomSentence(gr, "FoodsEng", category);
            boolean actual = controller.isComplete(gr, "FoodsEng", category);
            assertTrue(actual);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}