package com.minibar.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class creates a visualization of a given parse-tree
 */
public class ParseTreeGraph {
    private final Stage primaryStage;     //Stage to show visualization in
    private final String graphString;    //String representing the graph
    private final Map<Integer, ArrayList<String>> nodes = new HashMap<>();
    private final ArrayList<String> edges = new ArrayList<>();

    public ParseTreeGraph(Stage primaryStage, String graphString) {
        this.primaryStage = primaryStage;
        this.graphString = graphString;
    }

    /**
     * Takes in the string representation of the graph and parses it to create a graph data structure
     */
    private void extractGraph() {
        // Parse the input graph and create nodes and edges
        String[] lines = graphString.split("\n");
        int level = 0;
        ArrayList<String> levelNodes = new ArrayList<>();

        for (String line : lines) {
            if (line.contains("subgraph")) {     //New level of nodes
                level = level + 1;
                levelNodes = new ArrayList<>();
            } else if (line.contains("label=")) {   //Get node
                line = line.trim();
                String nodeName = line.substring(0, line.indexOf("["));
                String label = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                if (label.contains(":")) {
                    label = label.substring(label.indexOf(":") + 2);
                }
                levelNodes.add(nodeName + "-" + label);
            } else {
                if (!levelNodes.isEmpty()) nodes.put(level, levelNodes);   //Add nodes to their level
                //Edge lines start with only 2 spaces and not four
                if (line.contains("--") && line.startsWith("  ") && !line.startsWith("    ")) {
                    edges.add(line.trim());
                }
            }
        }
    }

    /**
     * Creates a graph object as well as a graphical representation
     */
    public void showGraph() {
        nodes.clear();
        edges.clear();
        extractGraph();
        // Create a GraphStream graph
        org.graphstream.graph.Graph graph = new SingleGraph("ParseTree");
        int level = 0;

        //Loop through the nodes
        for (Map.Entry<Integer, ArrayList<String>> entry : nodes.entrySet()) {
            ArrayList<String> nodes = entry.getValue();
            int x = nodes.size() - level;       //x co-ordinate
            for (String node : nodes) {
                String nodeName = node.substring(0, node.indexOf("-"));
                Node newNode = graph.addNode(nodeName);     //Add to graph
                //Style the node
                newNode.setAttribute("ui.label", node.substring(node.indexOf("-") + 1));
                newNode.setAttribute("ui.style", "text-style: bolder;");
                newNode.setAttribute("ui.style", "text-size: 20;");
                newNode.setAttribute("x", x);   //x co-ordinate
                newNode.setAttribute("y", level * -1);  //y co-ordinate based on level
                x += 1;
            }
            level++;
        }

        //Loop through the edges and add those to the graph
        for (String edge : edges) {
            String[] nodes = edge.split(" -- ");
            String node1 = nodes[0];
            String node2 = nodes[1];
            graph.addEdge(edge, node1, node2);
        }
        String styleSheet = """
                node {
                fill-color: white;
                }""";
        graph.setAttribute("ui.stylesheet", styleSheet);
        // Create a JavaFX viewer
        FxViewer viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        //viewer.enableAutoLayout();

        // Get the JavaFX view
        FxDefaultView view = (FxDefaultView) viewer.addDefaultView(false);
        Scene secondScene = new Scene(view, 800, 600);

        // Create a new stage for the second UI
        Stage secondStage = new Stage();
        secondStage.setTitle("Parse Tree Visualization");
        secondStage.setScene(secondScene);

        // Set the owner (primary stage) to make it a modal dialog
        secondStage.initOwner(primaryStage);

        // Show the new stage
        secondStage.show();
    }
}
