package com.minibar.tests;

import com.minibar.model.Sentence;
import com.minibar.model.Translation;
import org.grammaticalframework.pgf.PGF;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Translation class
 * Uses grammars/Foods.pgf to test
 */
public final class TranslationTest {

    /**
     * Tests a translation from English to English
     */
    @Test
    void translateEngToEng() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Translation translation = new Translation(grammar);
            HashMap<String, String> expected = translation.translate(new Sentence("those boring cheeses are warm"), "FoodsEng", "FoodsEng", grammar.getStartCat());
            String actual = "those boring cheeses are warm";
            assertEquals(expected.get("FoodsEng"), actual);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Tests a translation from English to Hebrew
     */
    @Test
    void translateEngToHeb() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Translation translation = new Translation(grammar);
            HashMap<String, String> expected = translation.translate(new Sentence("those boring cheeses are warm"), "FoodsEng", "FoodsHeb", grammar.getStartCat());
            String actual = "הגבינות המשעממות ההן חמות";
            assertEquals(expected.get("FoodsHeb"), actual);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Failing test from English to Hebrew with a ParseError
     */
    @Test
    void failingTranslateEngToHeb() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Translation translation = new Translation(grammar);
            //Last word "war" will cause a parse error, returning null
            HashMap<String, String> expected = translation.translate(new Sentence("those boring cheeses are war"), "FoodsEng", "FoodsHeb", grammar.getStartCat());
            assertNull(expected);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Tests a translation from Hebrew to English
     */
    @Test
    void translateHebToEng() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Translation translation = new Translation(grammar);
            HashMap<String, String> expected = translation.translate(new Sentence("הגבינות המשעממות ההן חמות"), "FoodsHeb", "FoodsEng", grammar.getStartCat());
            String actual = "those boring cheeses are warm";
            assertEquals(expected.get("FoodsEng"), actual);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Tests a translation from English to the other languages
     */
    @Test
    void translateToAllLanguagesFromEng() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Translation translation = new Translation(grammar);
            HashMap<String, String> expected = new HashMap<>();
            expected.put("FoodsAmh", "[Those] ትኩስ ነው::");
            expected.put("FoodsBul", "онези еднообразни сирена са горещи");
            expected.put("FoodsMlt", "dawk il- ġobniet tad-dwejjaq sħan");
            expected.put("FoodsDut", "die saaie kazen zijn warm");
            expected.put("FoodsSwe", "de där tråkiga ostarna är varma");
            expected.put("FoodsFin", "nuo tylsät juustot ovat lämpimiä");
            expected.put("FoodsIce", "þessir leiðinlegu ostar eru heitir");
            expected.put("FoodsMon", "тэдгээр амтгүй бяслагнууд бол халуун");
            expected.put("FoodsHeb", "הגבינות המשעממות ההן חמות");
            expected.put("FoodsPor", "esses queijos chatos são quentes");
            expected.put("FoodsGer", "jene langweiligen Käse sind warm");
            expected.put("FoodsRon", "acele brânzeturi plictisitoare sunt calde");
            expected.put("FoodsTur", "şu sıkıcı peynirler ılıktır");
            expected.put("FoodsTsn", "dikase tseo tse di bosula di bothitho");
            expected.put("FoodsHin", "वे अरुचिकर पनीर गरम हैं");
            expected.put("FoodsCze", "tamty nudné sýry jsou teplé");
            expected.put("FoodsFre", "ces fromages ennuyeux sont chauds");
            expected.put("FoodsNep", "ती नमिठा चिजहरु तातो छन्");
            expected.put("FoodsCat", "aquells formatges aburrits són calents");
            expected.put("FoodsIta", "quei formaggi noiosi sono caldi");
            expected.put("FoodsEpo", "tiuj enuigaj fromaĝoj estas varmaj");
            expected.put("FoodsSpa", "esos quesos aburridos son calientes");
            expected.put("FoodsLav", "tie garlaicīgie sieri ir silti");
            expected.put("FoodsGle", "tá na cáiseanna leamha sin te");
            expected.put("FoodsEng", "those boring cheeses are warm");
            expected.put("FoodsPes", "آن پنیرهاى ملال آور گرم هستند");
            expected.put("FoodsUrd", "وہ فضول پنیریں گرم ہیں");
            expected.put("FoodsJpn", "その つまらない チーズは あたたかい");
            expected.put("FoodsAfr", "daardie vervelige kase is warm");
            expected.put("FoodsTha", "เนยแข็ง น่าเบิ่อ ก้อน นั้น อุ่น");
            HashMap<String, String> actual = translation.translateToAllLanguages(new Sentence("those boring cheeses are warm"), "FoodsEng", grammar.getStartCat());
            actual.remove("Abstract");
            assertEquals(expected, actual);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Failing test from English to the other languages with a ParseError
     */
    @Test
    void failingTranslateToAllLanguagesFromEng() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Translation translation = new Translation(grammar);
            //Warm will cause a ParseError, returning null
            HashMap<String, String> actual = translation.translateToAllLanguages(new Sentence("those boring cheeses are war"), "FoodsEng", grammar.getStartCat());
            assertNull(actual);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    /**
     * Tests the toString() method
     */
    @Test
    void testToString() {
        try {
            PGF grammar = PGF.readPGF("grammars/Foods.pgf");
            Translation translation = new Translation(grammar);
            String expected = translation.toString();
            String actual = "Translation{grammar=Foods}";
            assertEquals(expected, actual);
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }
}