package com.example.laboras.control;

import com.example.laboras.Start;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ReturnHandler {

    public static void returnMethod(FXMLLoader fxmlLoader, Stage stage) throws IOException {
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
