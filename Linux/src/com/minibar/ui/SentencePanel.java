package com.minibar.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Represents the panel where a user may type a sentence and get translations
 */
public class SentencePanel {
    private final VBox mainContainer;   //Container where the panels will be placed
    private VBox sentencePanel;
    private ExpandableVBox translationsArea;
    private TextField sentenceField;     //Field to type sentence
    private Button translate;    //Button to translate sentence

    public SentencePanel(VBox mainContainer) {
        this.mainContainer = mainContainer;
    }

    public ExpandableVBox getTranslationsArea() {
        return translationsArea;
    }

    public TextField getSentenceField() {
        return sentenceField;
    }

    public Button getTranslateBtn() {
        return translate;
    }

    /**
     * Creates a new sentence panel and places on the mainContainer
     */
    public void render() {
        //Sentence Panel VBox
        sentencePanel = new VBox();
        sentencePanel.setPrefHeight(240);
        sentencePanel.getStyleClass().add("sentencePanel");

        //Top panel for the text field and submit button
        HBox topPanel = new HBox();
        sentenceField = new TextField();
        sentenceField.setPrefWidth(743);
        sentenceField.setPrefHeight(45);
        translate = new Button("Translate");
        translate.setPrefWidth(418);
        translate.setPrefHeight(45);
        translate.getStyleClass().add("translateBtn");
        topPanel.getChildren().add(sentenceField);
        topPanel.getChildren().add(translate);

        //VBox for the translations area
        translationsArea = new ExpandableVBox();
        translationsArea.setPrefHeight(120);
        translationsArea.getStyleClass().add("suggestWordVBox");

        //Delete button
        Button delete = new Button("Delete");
        delete.getStyleClass().add("delete-button");

        //Add everything to the final sentence panel
        sentencePanel.getChildren().add(topPanel);
        sentencePanel.getChildren().add(translationsArea);
        sentencePanel.getChildren().add(delete);

        //Make controls responsive
        sentencePanel.prefWidthProperty().bind(mainContainer.widthProperty().multiply(1.0));
        sentenceField.prefWidthProperty().bind(topPanel.widthProperty().multiply(0.7));
        translate.prefWidthProperty().bind(topPanel.widthProperty().multiply(0.3));
        translationsArea.prefWidthProperty().bind(sentencePanel.widthProperty());
        delete.prefWidthProperty().bind(sentencePanel.widthProperty());

        //Add everything onto the mainContainer
        mainContainer.getChildren().add(sentencePanel);

        //adds delete event handler
        delete.setOnAction(this::handleDelete);
    }

    /**
     * Deletes the panel from the mainContainer
     *
     * @param event is the onclick event
     */
    public void handleDelete(ActionEvent event) {
        mainContainer.getChildren().remove(sentencePanel);
    }
}
