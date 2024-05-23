/*
 * ChecksumFX
 *
 * Copyright 2024 Sean Leichtle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.checksumfx;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ChecksumFXUI extends Application {

    File selectedFile;
    FileChooser fileChooser;
    ChecksumFXHash checksumFXHash;

    public ChecksumFXUI() {
        fileChooser = new FileChooser();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ChecksumFX");
        primaryStage.setResizable(false);

        // Define elements
        Button searchButton = new Button("Search");
        TextField selectField = new TextField();

        ToggleGroup group = new ToggleGroup();
        Label algorithmLabel = new Label("Select algorithm:");
        RadioButton md5 = new RadioButton("MD5");
        md5.setToggleGroup(group);
        RadioButton sha1 = new RadioButton("SHA-1");
        sha1.setToggleGroup(group);
        RadioButton sha256 = new RadioButton("SHA-256");
        sha256.setToggleGroup(group);
        RadioButton sha512 = new RadioButton("SHA-512");
        sha512.setToggleGroup(group);
        RadioButton sha3 = new RadioButton("SHA3-256");
        sha3.setToggleGroup(group);
        Region buttonPadding = new Region();
        // Right justify applyButton
        HBox.setHgrow(buttonPadding, Priority.ALWAYS);
        Button applyButton = new Button("Apply");

        TextField artifactChecksum = new TextField();
        TextField publishedChecksum = new TextField();

        Button compareButton = new Button("Compare");

        Label result = new Label();

        // Modify elements
        selectField.setPrefSize(860, 30);
        selectField.setPromptText("Use search to find file or enter it manually here.");
        artifactChecksum.setPrefSize(935, 30);
        artifactChecksum.setPromptText("Artifact checksum will appear here.");
        publishedChecksum.setPrefSize(935, 30);
        publishedChecksum.setPromptText("Enter published checksum here and hit compare.");

        // Assemble elements
        GridPane grid = new GridPane();

        HBox selectPane = new HBox(searchButton, selectField/*, selectButton*/);
        selectPane.setSpacing(20);

        HBox buttonPane = new HBox(algorithmLabel, /*blake2b, */md5, sha1, sha256, sha512, sha3, buttonPadding, applyButton);
        buttonPane.setSpacing(40);

        grid.add(selectPane, 0, 0);
        grid.add(buttonPane, 0, 1);
        grid.add(artifactChecksum, 0, 2);
        grid.add(publishedChecksum, 0, 3);
        grid.add(compareButton, 0, 4);
        grid.add(result, 0, 5);

        // Modify layout
        grid.setPrefSize(985, 200);
        grid.setVgap(15);
        grid.setHgap(15);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Platform.runLater(result::requestFocus);

        // Action logic
        searchButton.setOnAction((event) -> {
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            if(selectedFile != null) {
                selectField.setText(selectedFile.getAbsolutePath());
            }
        });

        applyButton.setOnAction((event) -> {
            // Cast group to RadioButton to make the text of selected Button accessible
            RadioButton selectedToggle = (RadioButton) group.getSelectedToggle();
            checksumFXHash = new ChecksumFXHash(selectedFile, selectedToggle.getText());
            artifactChecksum.setText(checksumFXHash.getHashValue());
        });

        compareButton.setOnAction((event) -> result.setText(artifactChecksum.getText().equals(publishedChecksum.getText()) ?
                "Checksums match!" :
                "NOT A MATCH!"));

        Scene scene = new Scene(grid);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
