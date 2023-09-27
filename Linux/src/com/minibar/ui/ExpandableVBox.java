package com.minibar.ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * VBox container with additional capabilities to collapse/expand
 */
public class ExpandableVBox extends VBox {
    private final Button expandButton;  //Button to control collapsing/expanding
    private final VBox content;        //VBox to show translations and predictions
    private List<Node> children;        //Temporarily stores the nodes on top of content
    private boolean expanded = true;

    public ExpandableVBox() {
        expandButton = new Button("Collapse");
        expandButton.getStyleClass().add("expand-button");
        expandButton.setOnAction(event -> toggleContent());
        content = new VBox();
        getChildren().addAll(content, expandButton);   //Add VBox and expand/collapse button
    }

    /**
     * Expands or Collapses the VBox based on the number of nodes/children on the VBox
     */
    private void toggleContent() {
        //Only do anything when the vBox has children and those children are translation labels, not prediction labels
        if (!(content.getChildren().isEmpty()) && content.getChildren().get(0) instanceof Label) {
            if (expanded) {
                if (content.getChildren().size() > 5) {     //If more than 5 children, collapse
                    duplicateChildren();    //Temporarily store children
                    expanded = false;
                    showOnlyFirst5Children();   //Remove other children, leaving only 5
                    expandButton.setText("Expand");
                }
            } else {
                if (content.getChildren().size() > 5) {    //Already expanded i.e. has a lot of children
                    expanded = true;                    //Set expanded back to true
                    expandButton.setText("Collapse");
                } else if (children.size() > 5) {      //If previous children were more than 5, expand
                    expanded = true;
                    expandButton.setText("Collapse");
                    showAllChildren();      //Show all children nodes
                }
            }
        }
    }

    public VBox getContentVBox() {
        return content;
    }

    /**
     * Creates a copy of the children on content at that time
     */
    private void duplicateChildren() {
        children = new ArrayList<>();
        children.addAll(content.getChildren());
    }

    /**
     * Restores all the children on content, thereby expanding its size
     */
    private void showAllChildren() {
        content.getChildren().clear();
        for (Node child : children) {
            content.getChildren().add(child);
        }
    }

    /**
     * Removes all the children, except 5, from content, thereby collapsing it
     */
    private void showOnlyFirst5Children() {
        content.getChildren().clear();
        for (int i = 0; i < 5; i++) {
            content.getChildren().add(children.get(i));
        }
    }
}
