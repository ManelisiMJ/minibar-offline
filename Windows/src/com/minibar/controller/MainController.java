package com.minibar.controller;

import com.minibar.model.Sentence;
import com.minibar.model.Translation;
import com.minibar.ui.ExpandableVBox;
import com.minibar.ui.ParseTreeGraph;
import com.minibar.ui.SentencePanel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.grammaticalframework.pgf.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static javafx.scene.input.KeyCode.ENTER;

/**
 * Main UI controller
 */
public class MainController {
    private final HashMap<String, String> filePaths = new HashMap<>();
    private final SentenceController sentenceController = new SentenceController(new Sentence());
    private final String historyFilePath = "./HistoryData.txt";
    private ArrayList<String> words;
    private String selectedCategory, lastWord = "";
    private HBox suggestionsHBox;
    private PGF grammar = null;
    private PredictionController predictionController;
    private VBox suggestWordVBox;
    private Stage primaryStage;
    @FXML
    private ScrollPane mainScrollPane;
    private TextField sentenceField;
    @FXML
    private ChoiceBox<String> startCatChoiceBox;
    @FXML
    private ChoiceBox<String> grammarChoiceBox;
    @FXML
    private VBox mainContainer;
    @FXML
    private VBox addBox;
    @FXML
    private VBox gfIconBox;
    @FXML
    private VBox randomBox;
    @FXML
    private VBox clearBox;
    @FXML
    private Label grammarLabel;
    @FXML
    private Label addLabel;
    @FXML
    private Label clearLabel;
    @FXML
    private Label randomLabel;
    @FXML
    private Label startCatLabel;
    @FXML
    private HBox menuHbox;
    @FXML
    private Label fromLabel;
    @FXML
    private ChoiceBox<String> fromChoiceBox;
    @FXML
    private Label toLabel;
    @FXML
    private ChoiceBox<String> toChoiceBox;

    /**
     * Sets the stage from the Main App and initializes the app
     *
     * @param primaryStage is the stage from the Main App
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        newSentencePanel(); //Add a panel
        makeResponsive();   //Makes controls responsive
        grammarChoiceBox.setOnAction(this::handleGrammarSelection);
        loadHistoricalData();
    }

    /**
     * Loads cached data of users previous words type for each grammar
     */
    public void loadHistoricalData() {
        try {
            FileInputStream fos = new FileInputStream(historyFilePath); //reads cached data file if present
            ObjectInputStream ois = new ObjectInputStream(fos); //take file data as input stream
            predictionController = new PredictionController();
            predictionController.setHistoricalData((Map<String, ArrayList<String>>) ois.readObject()); // set file historical data to previous session
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Cached data not found");
        }
    }

    public void handleGrammarSelection(ActionEvent event) {
        populateChoiceBoxes();
    }

    /**
     * Populates "from , to and startCat" choice boxes based on the grammar
     */
    public void populateChoiceBoxes() {
        try {
            String path = filePaths.get(grammarChoiceBox.getValue());
            grammar = PGF.readPGF(path);
            predictionController = new PredictionController(grammar);
            displayCategories();
            displayLanguages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loops through the available categories in the grammar and sets them on the dropdown
     */
    private void displayCategories() {
        startCatChoiceBox.getItems().clear();
        for (String category : grammar.getCategories()) {
            startCatChoiceBox.getItems().add(category);
        }
        selectedCategory = startCatChoiceBox.getItems().get(0);
        startCatChoiceBox.setValue(selectedCategory);
    }

    /**
     * Loops through the available languages in the grammar and sets them on the dropdown
     */
    private void displayLanguages() {
        fromChoiceBox.getItems().clear();   //Clear previous languages
        toChoiceBox.getItems().clear();
        toChoiceBox.getItems().add("All");  //Start with an "All" option
        for (Map.Entry<String, Concr> entry : grammar.getLanguages().entrySet()) {
            fromChoiceBox.getItems().add(entry.getKey());   //Add language to the dropdowns
            toChoiceBox.getItems().add(entry.getKey());
        }
        if (fromChoiceBox.getItems().size() > 0) {
            fromChoiceBox.setValue(fromChoiceBox.getItems().get(0));    //Default to the first option
            toChoiceBox.setValue(toChoiceBox.getItems().get(0));
        }
    }

    /**
     * Handles the button to upload a new grammar
     */
    public void handleGrammarUpload() {
        FileChooser fileChooser = new FileChooser();
        //Only recognize .pgf files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PGF files", "*.pgf");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(new Stage());    //Open dialog
        if (!(file == null) && file.exists()) {     //If file exists and is not null
            grammarChoiceBox.getItems().add(file.getName());    //Add new grammar to the dropdown
            filePaths.put(file.getName(), file.getPath());  //Store its path
            grammarChoiceBox.setValue(file.getName());
            populateChoiceBoxes();
        } else {
            System.out.println("file does not exist");
        }
    }

    /**
     * Creates the necessary UI elements needed to display the translations and calls displayTranslations
     */
    public void performTranslations() {
        if (!(grammar == null)) {
            suggestionsHBox = new HBox();
            suggestionsHBox.setSpacing(10);
            suggestionsHBox.setPadding(new Insets(10, 10, 10, 10));
            suggestionsHBox.setPrefHeight(45);
            suggestionsHBox.setPrefWidth(701);
            suggestWordVBox.getChildren().clear();

            displayTranslations(suggestWordVBox);
            suggestWordVBox.requestLayout();
        }
    }

    /**
     * Handles the random sentence button
     */
    public void handleRandom() {
        if (!(grammar == null)) {
            sentenceField.clear();  //Clear previous sentence
            sentenceController.randomSentence(grammar, fromChoiceBox.getValue(), selectedCategory); //Generate random sentence
            String randomSentence = sentenceController.getSentence().toString();    //Get random sentence
            sentenceField.appendText(randomSentence);   //Display random sentence
            performTranslations();  //Translate random sentence

        }
    }

    /**
     * Event handler for the clear button
     */
    public void handleClear() {
        sentenceField.clear();  //Clear sentence field
        suggestWordVBox.getChildren().clear();  //Remove translations/suggestions
        sentenceController.setSentence(new Sentence()); //Clear sentence
    }

    /**
     * Calls sentence controller to translate sentence and renders the translations on the ui
     *
     * @param clonedBox is the VBox to place translations on
     */
    private void displayTranslations(VBox clonedBox) {
        if (sentenceController.isComplete(grammar, fromChoiceBox.getValue(), selectedCategory)) {
            // store translated sentences
            predictionController.storeWords(sentenceController.getSentence().toString());
            Translation translation = new Translation(grammar);     //Translation object
            if (toChoiceBox.getValue().equals("All")) {     //If translating to all languages
                HashMap<String, String> translations = translation.translateToAllLanguages(sentenceController.getSentence(), fromChoiceBox.getValue(), selectedCategory);
                Label abstractLabel = new Label("Abstract" + ": " + translations.get("Abstract"));  //Show abstract expression
                abstractLabel.getStyleClass().add("translationLabel");
                clonedBox.getChildren().add(abstractLabel);     //Add label to the ui
                for (Map.Entry<String, String> entry : translations.entrySet()) {   //Loop through other translations
                    if (!entry.getKey().equals("Abstract")) {   //Ignore the abstract expression
                        Label translationLabel = new Label(entry.getKey() + ": " + entry.getValue());
                        translationLabel.getStyleClass().add("translationLabel");
                        Button graphBtn = new Button("");   //Button to show graph visualization
                        graphBtn.getStyleClass().add("graph-btn");
                        graphBtn.setOnAction(this::onGraphButtonClick);     //Event handler
                        HBox translationHBox = new HBox();
                        translationHBox.getChildren().addAll(graphBtn, translationLabel);   //Place label and button on HBox
                        clonedBox.getChildren().add(translationHBox);   //Render on ui
                    }
                }
            } else {        //If translating to only one sentence
                HashMap<String, String> translatedSentence = translation.translate(sentenceController.getSentence(), fromChoiceBox.getValue(), toChoiceBox.getValue(), selectedCategory);
                Label abstractLabel = new Label("Abstract" + ": " + translatedSentence.get("Abstract")); //Abstract expression
                abstractLabel.getStyleClass().add("translationLabel");
                Label toLabel = new Label(toChoiceBox.getValue() + ": " + translatedSentence.get(toChoiceBox.getValue()));  //Translation
                toLabel.getStyleClass().add("translationLabel");
                Button graphBtn = new Button("");   //Button for graph visualization
                graphBtn.getStyleClass().add("graph-btn");
                graphBtn.setOnAction(this::onGraphButtonClick); //Event handler
                HBox translationHBox = new HBox();
                translationHBox.getChildren().addAll(graphBtn, toLabel);
                clonedBox.getChildren().add(translationHBox);
            }
        }
    }

    /**
     * Event handler for the buttons to show graphical visualization of parse tree
     *
     * @param event is the onclick event
     */
    public void onGraphButtonClick(ActionEvent event) {
        Button graphButton = (Button) event.getSource();    //Get button
        HBox container = (HBox) graphButton.getParent();    //Get button's HBox parent
        Label translationLabel = (Label) container.getChildren().get(1);    //Get label
        String textContent = translationLabel.getText();    //Get label's content
        String language = textContent.substring(0, textContent.indexOf(":"));   //Get language on that label
        Concr targetLanguage = grammar.getLanguages().get(language);    //Concrete syntax of that language
        Concr sourceLanguage = grammar.getLanguages().get(fromChoiceBox.getValue());    //Concrete syntax of the source language
        try {
            Iterable<ExprProb> expressions = sourceLanguage.parse(selectedCategory, sentenceController.getSentence().toString());
            Expr sentenceExpression = expressions.iterator().next().getExpr();  //Get the expression
            String graph = targetLanguage.graphvizParseTree(sentenceExpression);    //Get the parse tree graph string
            ParseTreeGraph parseTreeVisualization = new ParseTreeGraph(primaryStage, graph);
            parseTreeVisualization.showGraph();     //Show graph visualization
        } catch (ParseError e) {
            e.printStackTrace();
        }
    }

    /**
     * Iterate through the suggested words and display them as clickable buttons
     */
    public void displaySuggestions() {
        int index = 0;
        int numberOfHboxes = words.size();
        if (numberOfHboxes % 9 == 0) {
            numberOfHboxes = words.size() / 9;
        } else {
            numberOfHboxes = words.size() / 9 + 1;
        }
        for (int j = 0; j < numberOfHboxes; j++) {
            suggestionsHBox = new HBox();
            suggestionsHBox.setSpacing(10);
            suggestionsHBox.setPadding(new Insets(10, 10, 10, 10));
            suggestionsHBox.setPrefHeight(45);
            suggestionsHBox.setPrefWidth(701);
            for (int i = 0; i < 9; i++) {
                if (index == words.size()) break;
                Button suggestionButton = new Button(words.get(index));     //Create the button
                suggestionButton.setId(words.get(index));           //Set the button's id
                String name = words.get(index);

                suggestionButton.setOnAction(event -> {     //Set suggestions button event listener
                    Button button = (Button) event.getSource();     //Get the button that was pressed
                    HBox suggestionsHBox = (HBox) button.getParent();   //Get the hbox it is on top of
                    VBox content = (VBox) suggestionsHBox.getParent();      //Get the VBox that the hbox is on
                    ExpandableVBox expandableVBox = (ExpandableVBox) content.getParent();   //Get the expandablevbox
                    VBox secondSuggestWordVBox = expandableVBox.getContentVBox();       //Get the content box
                    if (!secondSuggestWordVBox.equals(suggestWordVBox)) {   //If the content box is not the same as the current active content box
                        this.suggestWordVBox = secondSuggestWordVBox;       //Set the current active content box to the new one
                        VBox mainVBox = (VBox) expandableVBox.getParent();
                        HBox topPanel = (HBox) mainVBox.getChildren().get(0);
                        sentenceField = (TextField) topPanel.getChildren().get(0);      //Set the active text field to the correct one
                        sentenceController.setSentence(new Sentence(sentenceField.getText()));  //Update the sentence
                    }
                    handleSuggestionButtonClick(name);
                });

                index++;
                suggestionButton.getStyleClass().add("prediction-buttons"); //Set css class
                suggestionButton.setPrefWidth(65);
                suggestionButton.setPrefHeight(25);
                suggestionsHBox.getChildren().addAll(suggestionButton);     //Put the button on the hbox
            }
            suggestWordVBox.getChildren().add(suggestionsHBox);     //Display the suggestions on the VBox
        }
    }

    /**
     * Method to handle the click of the suggestions buttons
     *
     * @param word is the word on the button
     */
    public void handleSuggestionButtonClick(String word) {
        String sentence = sentenceController.getSentence().toString();
        if (!(sentence.endsWith(" "))) {        //User still typing, delete that word
            sentenceController.deleteWord();
        }
        sentenceController.appendWord(word + " ");      //Then add the word they clicked
        sentenceField.setText(sentenceController.getSentence().toString());
        suggestWordVBox.getChildren().clear();
        suggestWordVBox.requestFocus();

        makePredictions();  //Show predictions after clicking the button
    }

    /**
     * updates the sentence controller based on the currently selected textbox
     */
    public void updateSentenceController() {
        String sentenceOnScreen = sentenceField.getText();
        if (sentenceOnScreen.contains(" ")) {
            //set Sentence object to sentence on screen, don't include the last word : user is still typing
            sentenceController.setSentence(new Sentence(sentenceOnScreen.substring(0, sentenceOnScreen.lastIndexOf(" ") + 1)));
            if (sentenceOnScreen.lastIndexOf(" ") == sentenceOnScreen.length() - 1)
                lastWord = "";  //lastChar is a space, don't filter suggestions
            else
                lastWord = sentenceOnScreen.substring(sentenceOnScreen.lastIndexOf(' ') + 1); //filter suggestions based on what the user is typing
        } else { // no word added or user is still typing the first word
            sentenceController.setSentence(new Sentence());
            lastWord = sentenceOnScreen;
        }
    }

    /**
     * handles sentence field keyboard input
     *
     * @param event this is the keyboard input
     */
    public void handleKeyboard(KeyEvent event) {
        TextField translateBtn = (TextField) event.getSource();
        HBox topPanel = (HBox) translateBtn.getParent();
        VBox sentencePanel = (VBox) topPanel.getParent();
        setSuggestWordVBox(sentencePanel);
        this.sentenceField = (TextField) event.getSource();
        updateSentenceController();
        if (event.getCode() == ENTER) { //user wants to translate sentence
            if (!lastWord.equals("")) sentenceController.appendWord(lastWord);
            performTranslations();
        } else {
            makePredictions();
        }
    }

    /**
     * filters suggested words array when user is typing
     */
    private void filterSuggestions() {
        ArrayList<String> filteredWords = new ArrayList<>();
        if (lastWord.length() > 0) { //do not filter suggestion , user has typed nothing
            for (String eachWord : words) {
                if (eachWord.length() >= lastWord.length())
                    if (eachWord.substring(0, lastWord.length()).equals(lastWord)) filteredWords.add(eachWord);
            }
            words = filteredWords;
        }
    }

    /**
     * Gets predictions from the predictions controller
     */
    private void makePredictions() {
        if (!(grammar == null)) {
            suggestWordVBox.getChildren().clear();      //Clear the VBox
            selectedCategory = startCatChoiceBox.getValue();    //Get selected category
            updateSentenceController();    //Get the sentence on active text field
            //Get predicted words
            words = predictionController.predictNextWord(sentenceController.getSentence().toString(), fromChoiceBox.getValue(), selectedCategory);
            filterSuggestions();    //Filter suggested words by what user is typing
            displaySuggestions(); //Display the predictions
        }
    }

    /**
     * Makes the controls on the UI responsive by binding them to their parent nodes
     */
    private void makeResponsive() {
        mainScrollPane.prefHeightProperty().bind(primaryStage.getScene().heightProperty());
        mainContainer.prefWidthProperty().bind(primaryStage.getScene().widthProperty());
        menuHbox.prefWidthProperty().bind(mainContainer.widthProperty().multiply(1.0));
        grammarLabel.prefWidthProperty().bind(menuHbox.widthProperty().multiply(0.32)); // 30% width
        grammarChoiceBox.prefWidthProperty().bind(menuHbox.widthProperty().multiply(0.3));
        fromChoiceBox.prefWidthProperty().bind(menuHbox.widthProperty().multiply(0.3));
        fromLabel.prefWidthProperty().bind(menuHbox.widthProperty().multiply(0.3));
        addBox.prefWidthProperty().bind(menuHbox.widthProperty().multiply(0.3));
        addLabel.prefWidthProperty().bind(addBox.widthProperty().multiply(0.7));
        randomBox.prefWidthProperty().bind(menuHbox.widthProperty().multiply(0.3));
        gfIconBox.prefWidthProperty().bind(menuHbox.widthProperty().multiply(0.3));
        randomLabel.prefWidthProperty().bind(randomBox.widthProperty().multiply(0.7));
        clearBox.prefWidthProperty().bind(menuHbox.widthProperty().multiply(0.3));
        clearLabel.prefWidthProperty().bind(clearBox.widthProperty().multiply(0.7));
        startCatLabel.prefWidthProperty().bind(menuHbox.widthProperty().multiply(0.3));
        toChoiceBox.prefWidthProperty().bind(menuHbox.widthProperty().multiply(0.3));
        startCatChoiceBox.prefWidthProperty().bind(menuHbox.widthProperty().multiply(0.3));
        toLabel.prefWidthProperty().bind(menuHbox.widthProperty().multiply(0.3));
        toLabel.prefWidthProperty().bind(menuHbox.widthProperty().multiply(0.3));
    }

    /**
     * Creates and renders a new sentence panel on the UI
     */
    public void newSentencePanel() {
        SentencePanel panel = new SentencePanel(mainContainer);
        panel.render();
        setTextFieldEventListener(panel.getSentenceField());    //Set text field's event listener
        setTranslationBtnEventListener(panel.getTranslateBtn());   //Set translate button's event listener
        this.suggestWordVBox = panel.getTranslationsArea().getContentVBox();   //Set active VBox
        this.sentenceField = panel.getSentenceField();  //Set active text field
        this.sentenceController.setSentence(new Sentence(sentenceField.getText())); //Set a new sentence on the controller
    }

    /**
     * Sets the event listener for the translate buttons
     *
     * @param translateBtn is the target button
     */
    public void setTranslationBtnEventListener(Button translateBtn) {
        translateBtn.setOnAction(this::handleTranslateBtn);
    }

    /**
     * Event handler for the translate buttons
     *
     * @param actionEvent is the onclick event of the buttons
     */
    private void handleTranslateBtn(ActionEvent actionEvent) {
        Button translateBtn = (Button) actionEvent.getSource();     //Get the button clicked
        HBox topPanel = (HBox) translateBtn.getParent();    //Get the top panel
        sentenceField = (TextField) topPanel.getChildren().get(0);  //Get the sentence field associated with the button clicked
        sentenceController.setSentence(new Sentence(sentenceField.getText()));  //Set the current sentence to the one on the text field
        VBox sentencePanel = (VBox) topPanel.getParent();   //Get the VBox
        setSuggestWordVBox(sentencePanel);      //Activate VBox
        performTranslations();
    }


    /**
     * Sets the active VBox to show predictions and translations on
     *
     * @param sentencePanel is the VBox to activate
     */
    public void setSuggestWordVBox(VBox sentencePanel) {
        ExpandableVBox expandableVBox = (ExpandableVBox) sentencePanel.getChildren().get(1);
        this.suggestWordVBox = expandableVBox.getContentVBox();
    }

    /**
     * Sets the text field's onclick and mouse enter event handlers
     *
     * @param sentenceField is the target text field
     */
    public void setTextFieldEventListener(TextField sentenceField) {
        sentenceField.setOnKeyReleased(this::handleKeyboard);
        sentenceField.setOnMouseClicked(this::handleMouseEnter);
    }

    /**
     * Handles the mouseEnter event on a text field, when you press on the text field, show predictions
     *
     * @param mouseEvent is the event
     */
    public void handleMouseEnter(MouseEvent mouseEvent) {
        sentenceField = (TextField) mouseEvent.getSource();     //Get text field from the event
        HBox topPanel = (HBox) sentenceField.getParent();   //Get the top panel
        VBox sentencePanel = (VBox) topPanel.getParent();   //Get the sentencePanel, where the top panel is
        setSuggestWordVBox(sentencePanel);      //Set the VBox to show predictions on
        makePredictions();  //Show predictions
    }

    /**
     * Quits the application
     */
    public void quit() {
        try {
            FileOutputStream fos = new FileOutputStream(historyFilePath);
            ObjectOutputStream oss = new ObjectOutputStream(fos);
            oss.writeObject(predictionController.getHistoricalData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.exit();
    }
}
