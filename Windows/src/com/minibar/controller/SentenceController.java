package com.minibar.controller;

import com.minibar.model.Sentence;
import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.PGF;
import org.grammaticalframework.pgf.ParseError;
import org.grammaticalframework.pgf.TokenProb;

import java.util.ArrayList;
import java.util.Random;

/**
 * This Controller handles operations relating to Sentence objects
 */
public class SentenceController {
    private Sentence sentence;

    /**
     * Creates a SentenceController object to control the Sentence object provided
     *
     * @param sentence Sentence object stores typed words
     */
    public SentenceController(Sentence sentence) {
        this.sentence = sentence;
    }

    public Sentence getSentence() {
        return sentence;
    }

    /**
     * Replaces the sentence in the controller
     *
     * @param sentence is the new sentence
     */
    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    /**
     * Appends a word to the end of the sentence
     *
     * @param word is the word to be appended
     */
    public void appendWord(String word) {
        sentence.addWord(word);
    }

    /**
     * Deletes the last word from the sentence
     */
    public void deleteWord() {
        sentence.deleteWord();
    }

    /**
     * Checks if the sentence is complete
     *
     * @param grammar  is the grammar being used
     * @param language is the language of the sentence
     * @param category is the category being used
     * @return true if a sentence is complete otherwise false
     */
    public boolean isComplete(PGF grammar, String language, String category) {
        Concr sourceLanguage = grammar.getLanguages().get(language);
        try {
            sourceLanguage.parse(category, sentence.toString());
            return true;
        } catch (ParseError e) {    //Sentence could not be parsed : invalid/incomplete
            return false;
        }
    }

    /**
     * Generate a random sentence
     *
     * @param grammar  is the grammar being used
     * @param language is the language the random sentence is generated in
     * @param category is the category being used
     */
    public void randomSentence(PGF grammar, String language, String category) {
        Random random = new Random();
        sentence = new Sentence(); //clear sentence
        String text, randomWord;
        int randomIndex, suggestionsLen;
        do {
            ArrayList<String> suggestions = new ArrayList<>();
            Concr sourceLanguage = grammar.getLanguages().get(language);
            text = sentence.toString();
            try {
                Iterable<TokenProb> tokenProbs = sourceLanguage.complete(category, text, "");
                for (TokenProb token : tokenProbs)
                    suggestions.add(token.getToken());   //Add legal word
                suggestionsLen = suggestions.size();
                if (suggestionsLen == 0) break;//no suggestions break;
                randomIndex = random.nextInt(suggestionsLen);
                randomWord = suggestions.get(randomIndex);
                if (sentence.toString().length() == 0) sentence.addWord(randomWord);
                else sentence.addWord(" " + randomWord);
            } catch (ParseError e) {
                break;
            }
        } while (true);
    }
}

