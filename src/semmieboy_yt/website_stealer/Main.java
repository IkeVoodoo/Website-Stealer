package semmieboy_yt.website_stealer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import semmieboy_yt.website_stealer.webserver.Webserver;

import java.io.File;

public class Main extends Application {
    static TextArea output = new TextArea();

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage stage) {
        stage.setTitle("Website Stealer");
        stage.setMinHeight(400);
        stage.setMinWidth(400);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(25, 25, 25, 25));
        vBox.setAlignment(Pos.CENTER);

        TextField websiteInput = new TextField();
        websiteInput.setPromptText("Enter website to clone");
        websiteInput.setMaxWidth(300);

        Label title = new Label("Website stealer");
        title.setFont(Font.font(20));

        TextField portInput = new TextField();
        portInput.setPromptText("Enter port number");
        portInput.setMaxWidth(300);

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        directoryChooser.setTitle("Choose run directory (cloned files will be stored here)");

        Button startWebserver = new Button("Start Webserver");
        startWebserver.setOnAction(event -> {
            String website = websiteInput.getText();
            String port = portInput.getText();

            //Needs more validation
            if (website.isEmpty()) {
                log("Invalid site");
            } else if (!port.matches("[0-9]*") | port.isEmpty() | port.startsWith("0")) {
                log("Invalid port");
            } else {
                startWebserver.setDisable(true);
                if (website.charAt(website.length()-1) != '/') website += "/";
                new Webserver(website, Integer.parseInt(port), directoryChooser.showDialog(stage).getPath());
            }
        });

        vBox.getChildren().addAll(title, websiteInput, portInput, startWebserver);

        output.setEditable(false);
        splitPane.getItems().addAll(vBox, output);

        Scene scene = new Scene(splitPane, 400, 400);
        stage.setScene(scene);

        stage.show();
    }

    public static void log(String text) {
        output.appendText(text+System.lineSeparator());
    }
}
