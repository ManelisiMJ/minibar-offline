package com.minibar.tests;

import com.minibar.model.Sentence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class contains test cases for the Sentence class.
 * Verifies the behavior of the Sentence class methods.
 */
public class SentenceTest {
    /**
     * Test case to verify adding a word to an empty sentence.
     */
    @Test
    void testAddWord() {
        Sentence sentence = new Sentence();
        sentence.addWord("I");
        assertEquals("I", sentence.toString());
    }

    /**
     * Test case to verify adding multiple words to the sentence.
     */
    @Test
    void testAddMultipleWords() {
        Sentence sentence = new Sentence();
        sentence.addWord("I ");
        sentence.addWord("love ");
        sentence.addWord("Grammatical ");
        sentence.addWord("Framework");
        assertEquals("I love Grammatical Framework", sentence.toString());
    }

    /**
     * Test case to verify deleting a word from an empty sentence.
     */
    @Test
    void testDeleteEmptySentence() {
        Sentence sentence = new Sentence();
        sentence.deleteWord();
        assertEquals("", sentence.toString());
    }

    /**
     * Test case to verify deleting a word from sentence.
     */
    @Test
    void testDeleteWord() {
        Sentence sentence = new Sentence("My name is");
        sentence.deleteWord();
        assertEquals("My name", sentence.toString());
    }

    /**
     * Tests the deletion if the sentence only has one word
     */
    @Test
    void testDeleteOneWord() {
        Sentence sentence = new Sentence("My");
        sentence.deleteWord();
        assertEquals("", sentence.toString());
    }

    /**
     * Test case to verify comparing equal objects.
     */
    @Test
    void testEquals() {
        Sentence sentence1 = new Sentence("I");
        Sentence sentence2 = new Sentence("I");
        Boolean result = sentence1.equals(sentence2);
        assertEquals(true, result);
    }

    /**
     * Test case to verify comparing equal objects with different cases.
     */
    @Test
    void testEqualsDiffCases() {
        Sentence sentence1 = new Sentence("I am");
        Sentence sentence2 = new Sentence("i AM");
        Boolean result = sentence1.equals(sentence2);
        assertEquals(true, result);
    }

    /**
     * Test case to verify comparing unequal Sentence objects.
     */
    @Test
    void testNotEquals() {
        Sentence sentence1 = new Sentence("I");
        Sentence sentence2 = new Sentence("l");
        Boolean result = sentence1.equals(sentence2);
        assertEquals(false, result);
    }
}

