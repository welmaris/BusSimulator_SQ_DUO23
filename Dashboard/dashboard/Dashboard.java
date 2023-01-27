package Dashboard.dashboard;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import DatabaseLogger.mockDatabaseLogger.ArrivaLogger;
import InfobordSysteem.infoborden.Infobord;

import Simulator.bussimulator.Runner;

public class Dashboard extends Application {

    private void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    private void startBord(String halte, String richting) {
        Infobord infobord = new Infobord(halte, richting);
        Platform.runLater(() -> infobord.start(new Stage()));
    }

    private void startAlles() {
        thread(new Runner(), false);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane pane = createGridPane();

        TextField halteInput = addTextField("Halte:", "A-Z", pane, 0);
        TextField richtingInput = addTextField("Richting:", "1 of -1", pane, 1);

        addButtons(pane, e -> startBord(halteInput.getText(), richtingInput.getText()));

        showStage(primaryStage, pane, "BusSimulatie control-Center");
    }

    private void showStage(Stage primaryStage, GridPane pane, String title) {
        Scene scene = new Scene(pane);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TextField addTextField(String label, String placeholder, GridPane pane, int index) {
        pane.add(new Label(label), 0, index);
        TextField textField = new TextField(placeholder);
        pane.add(textField, 1, index);
        return textField;
    }

    private void addButtons(GridPane pane, EventHandler<ActionEvent> startButtonEventHandler) {
        Button startBordButton = createDashboardButton("Start Bord", startButtonEventHandler);
        pane.add(startBordButton, 1, 5);

        Button startButton = createDashboardButton("Start Alles", e -> startAlles());
        pane.add(startButton, 2, 5);

        Button loggerButton = createDashboardButton("Start Logger", e -> thread(new ArrivaLogger(), false));
        pane.add(loggerButton, 3, 5);
    }

    private GridPane createGridPane() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);
        pane.setMinWidth(400);
        pane.setMinHeight(150);

        return pane;
    }

    private Button createDashboardButton(String label, EventHandler<ActionEvent> eventHandler) {
        Button button = new Button(label);
        button.setOnAction(eventHandler);
        GridPane.setHalignment(button, HPos.LEFT);
        return button;
    }

} 