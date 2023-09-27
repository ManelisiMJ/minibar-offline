# Makefile to compile the application
BIN = bin/
SRC = src/
GF = org/grammaticalframework/pgf/
PACK_SRC = src/com/minibar/

GF_TARGET = $(BIN)$(GF)*.class

$(BIN)$(GF)%.class: $(GF)%.java
	@javac -d $(BIN) $(GF)*.java $<
	@echo Grammatical Framework successfully compiled

compile: $(GF_TARGET)
	@javac -d $(BIN) $(PACK_SRC)model/*java
	@javac -cp $(BIN) -d $(BIN) $(PACK_SRC)controller/PredictionController.java $(PACK_SRC)controller/SentenceController.java
	@javac -cp "bin/;lib/gs-ui-javafx-2.0.jar;lib/gs-core-2.0.jar;lib/gs-algo-2.0.jar" -d $(BIN) --module-path "lib/javafx-sdk-20.0.2/lib" --add-modules javafx.controls,javafx.fxml $(PACK_SRC)ui/ParseTreeGraph.java
	@javac -cp $(BIN) -d $(BIN) --module-path "lib/javafx-sdk-20.0.2/lib" --add-modules javafx.controls,javafx.fxml $(PACK_SRC)ui/ExpandableVBox.java $(PACK_SRC)ui/SentencePanel.java
	@javac -cp "bin/;lib/gs-ui-javafx-2.0.jar;lib/gs-core-2.0.jar;lib/gs-algo-2.0.jar" -d $(BIN) --module-path "lib/javafx-sdk-20.0.2/lib" --add-modules javafx.controls,javafx.fxml $(PACK_SRC)controller/MainController.java
	@javac -cp $(BIN) -d $(BIN) --module-path "lib/javafx-sdk-20.0.2/lib" --add-modules javafx.controls,javafx.fxml $(PACK_SRC)ui/App.java
	@echo =====================================Application successfully compiled=======================================
	@echo =============================================================================================================

build: compile
	@jar cmf MANIFEST.MF Minibar-Offline.jar -C $(BIN) .
	
run: build
	@java --module-path "lib/javafx-sdk-20.0.2/lib" --add-modules javafx.controls,javafx.fxml -jar "Minibar-Offline.jar"