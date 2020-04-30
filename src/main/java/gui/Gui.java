package gui;

import consoleUI.ConsoleUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

@SuppressWarnings("SpellCheckingInspection")
public class Gui extends Application implements Initializable {
    @FXML
    public ListView<String> log;
    @FXML
    public Label cbet;
    @FXML
    public Label pot;
    @FXML
    public Label player;
    @FXML
    public Label pbet;
    @FXML
    public Label cards;
    @FXML
    public Button bet;
    @FXML
    public Button fold;
    @FXML
    public Button check;
    @FXML
    public Button call;
    @FXML
    public Button raise;
    @FXML
    public TextField sum;

    private Runnable delayedActions = null;

    public static void main(String[] args) {
        launch(args);
    }

    public static void setLabelText(Label label, String format, String... args) {
        //noinspection ConfusingArgumentToVarargsMethod
        Platform.runLater(() -> label.setText(String.format(format, args)));
    }

    public static void enableButton(Button button) {
        Platform.runLater(() -> button.setDisable(false));
    }

    public void clearUI() {
        bet.setDisable(true);
        fold.setDisable(true);
        check.setDisable(true);
        call.setDisable(true);
        raise.setDisable(true);
        sum.setText("0");
    }

    public Map<String, Runnable> myMap = new HashMap<>() {{
        put("bet", () -> enableButton(bet));
        put("fold", () -> enableButton(fold));
        put("check", () -> enableButton(check));
        put("call", () -> enableButton(call));
        put("raise", () -> enableButton(raise));
    }};

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Poker");
        primaryStage.setWidth(800);
        primaryStage.setHeight(450);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/mainScene.fxml");
        loader.setLocation(xmlUrl);
        AnchorPane root = loader.load();
        final double[] xOffset = {0};
        final double[] yOffset = {0};
        root.setOnMousePressed(event -> {
            xOffset[0] = primaryStage.getX() - event.getScreenX();
            yOffset[0] = primaryStage.getY() - event.getScreenY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset[0]);
            primaryStage.setY(event.getScreenY() + yOffset[0]);
        });

        primaryStage.setScene(new Scene(root, 800, 450));
        primaryStage.show();
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        bet.setOnAction((e) -> {
            try {
                IOUtils.writeString("bet");
                delayedActions = () -> {
                    try {
                        IOUtils.writeString(sum.getText());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                };
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        fold.setOnAction((e) -> {
            try {
                IOUtils.writeString("fold");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        check.setOnAction((e) -> {
            try {
                IOUtils.writeString("check");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        call.setOnAction((e) -> {
            try {
                IOUtils.writeString("call");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        raise.setOnAction((e) -> {
            try {
                IOUtils.writeString("raise");
                delayedActions = () -> {
                    try {
                        IOUtils.writeString(sum.getText());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                };
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        try {
            IOUtils.redirectIO(x -> Platform.runLater(() -> {
                log.getItems().add(x);
                log.scrollTo(Math.max(log.getItems().size() - 1, 0));
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        var t1 = new Thread(() -> {
            var consoleUI = new ConsoleUI();
            consoleUI.startGame();
        });
        t1.setDaemon(true);
        var t2 = new Thread(() -> {
            while (true) {
                String sb = null;
                try {
                    sb = IOUtils.readString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert sb != null;
                Pattern.
                        compile("List of actions: \\n((\".+?\".*\\n)+)").
                        matcher(sb).
                        results().
                        forEach(x -> clearUI());
                Pattern.
                        compile("List of actions: \\n((\".+?\".*\\n)+)").
                        matcher(sb).
                        results().
                        flatMap(
                                x -> Pattern.
                                        compile("\"(.+)\"").
                                        matcher(x.group(1)).
                                        results().
                                        map(y -> y.group(1))
                        ).
                        map(x -> myMap.get(x)).
                        filter(Objects::nonNull).
                        forEach(Runnable::run);
                Pattern.
                        compile("Карты на столе:\\n(.+)").
                        matcher(sb).
                        results().
                        map(
                                x -> x.group(1)
                        ).
                        forEach(x -> setLabelText(cards, "CARDS: %s", x));
                Pattern.
                        compile("Current bet = (\\d+)\\$ ").
                        matcher(sb).
                        results().
                        map(
                                x -> x.group(1)
                        ).
                        forEach(x -> setLabelText(cbet, "Current bet = %s$", x));
                Pattern.
                        compile("Overall pot = (\\d+)\\$ ").
                        matcher(sb).
                        results().
                        map(
                                x -> x.group(1)
                        ).
                        forEach(x -> setLabelText(pot, "Overall pot = %s$", x));
                Pattern.
                        compile("Current bet .+? = (\\d+)\\$").
                        matcher(sb).
                        results().
                        map(
                                x -> x.group(1)
                        ).
                        forEach(x -> setLabelText(pbet, "Player`s bet: %s$", x));
                Pattern.
                        compile("Player (.+)").
                        matcher(sb).
                        results().
                        map(
                                x -> x.group(1)
                        ).
                        forEach(x -> setLabelText(player, "Current player: %s", x));
                if (delayedActions != null) {
                    delayedActions.run();
                    delayedActions = null;
                }
            }
        });
        t2.setDaemon(true);

        t1.start();
        t2.start();
    }
}
