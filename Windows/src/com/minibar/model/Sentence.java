package com.minibar.model;

/**
 * The Sentence class represents a sentence being typed by a user, that will be translated.
 */
public class Sentence {
    private final StringBuilder sentence;

    /**
     * Constructs a Sentence object with an empty StringBuilder.
     */
    public Sentence() {
        sentence = new StringBuilder();
    }

    /**
     * Constructs a Sentence object with an initial sentence.
     *
     * @param sentence The initial sentence to set.
     */
    public Sentence(String sentence) {
        this.sentence = new StringBuilder(sentence);
    }

    /**
     * Adds a word to the sentence.
     *
     * @param word The word to add to the sentence.
     */
    public void addWord(String word) {
        //add space if sentence has words
        if (sentence.length() > 0) {
            sentence.append(word);
        } else {
            sentence.append(word);
        }
    }

    /**
     * Deletes the last word in the sentence.
     */
    public void deleteWord() {
        if (sentence.indexOf(" ") == -1) { //No space found, sentence either has 1 word or is empty
            if (sentence.length() > 0) sentence.setLength(0);//Sentence not empty, but has one word, so clear
        } else {   //Sentence has spaces, i.e. more than one word
            int startIndex = sentence.lastIndexOf(" "); //Start deleting from the space
            sentence.delete(startIndex, sentence.length());
        }

    }

    /**
     * Converts the sentence to a string representation.
     *
     * @return A string representation of the sentence.
     */
    @Override
    public String toString() {
        return sentence.toString();
    }

    /**
     * Compares this Sentence object to another Sentence object.
     *
     * @param other The object to compare to this Sentence.
     * @return True if the objects are equal, false if not.
     */
    public boolean equals(Sentence other) {
        String otherSentence = other.toString();
        return this.toString().equalsIgnoreCase(otherSentence);
    }
}
