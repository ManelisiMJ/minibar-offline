package com.minibar.model;

import org.grammaticalframework.pgf.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for using a specified grammar to perform translations
 */
public class Translation {
    private final PGF grammar;  //Grammar used to perform translations

    /**
     * Creates a Translation object with the grammar being used
     *
     * @param grammar is the grammar used to perform the translations
     */
    public Translation(PGF grammar) {
        this.grammar = grammar;
    }

    /**
     * Translates a sentence to the specified language
     *
     * @param sentence is the sentence to translate
     * @param from     is the language the sentence was typed in
     * @param to       is the language we are translating to
     * @return the translated sentence
     */
    public HashMap<String, String> translate(Sentence sentence, String from, String to, String category) {
        HashMap<String, String> translation = new HashMap<>();
        Concr sourceLanguage = grammar.getLanguages().get(from);
        try {
            //Get the sentence as an expression
            Iterable<ExprProb> expressions = sourceLanguage.parse(category, sentence.toString());
            Expr sentenceExpression = expressions.iterator().next().getExpr();
            Concr targetLanguage = grammar.getLanguages().get(to);      //Language we are translating to
            translation.put("Abstract", sentenceExpression.toString());
            translation.put(to, targetLanguage.linearize(sentenceExpression)); //Linearize the expression
        } catch (ParseError e) {    //Sentence could not be parsed
            return null;
        }
        return translation;
    }

    /**
     * Translates a specified sentence to all the languages in the grammar
     *
     * @param sentence is the sentence to be translated
     * @param from     is the language the sentence was typed in
     * @return a map of translations to all available languages
     */
    public HashMap<String, String> translateToAllLanguages(Sentence sentence, String from, String category) {
        //Stores the translations in a Hash Map; key = language name, value = linearized expr (translation)
        HashMap<String, String> translations = new HashMap<>();
        Map<String, Concr> languages = grammar.getLanguages();      //Get languages
        Concr sourceLanguage = grammar.getLanguages().get(from);
        try {
            Iterable<ExprProb> expressions = sourceLanguage.parse(category, sentence.toString());
            Expr sentenceExpression = expressions.iterator().next().getExpr();
            translations.put("Abstract", sentenceExpression.toString());
            //Loop through all languages and translate to each one
            for (Map.Entry<String, Concr> concrete : languages.entrySet()) {
                Concr targetLanguage = concrete.getValue();
                translations.put(concrete.getKey(), targetLanguage.linearize(sentenceExpression));
            }
        } catch (ParseError e) {    //Sentence could not be parsed
            return null;
        }
        return translations;
    }

    /**
     * @return the name of the grammar being used for translations
     */
    @Override
    public String toString() {
        return "Translation{" + "grammar=" + grammar.getAbstractName() + '}';
    }
}
