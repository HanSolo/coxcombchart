/*
 * Copyright (c) 2017 by Gerrit Grunwald
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

package eu.hansolo.fx.coxcombchart;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;


/**
 * User: hansolo
 * Date: 26.12.17
 * Time: 13:03
 */
public class Demo extends Application {
    private CoxcombChart chart;

    @Override public void init() {
        Item       item1 = new Item(27, "", Color.web("#96AA3B"));
        Item       item2 = new Item(24, "", Color.web("#29A783"));
        Item       item3 = new Item(16, "", Color.web("#098AA9"));
        Item       item4 = new Item(15, "", Color.web("#62386F"));
        Item       item5 = new Item(13, "", Color.web("#89447B"));
        Item       item6 = new Item(5, "", Color.web("#EF5780"));
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);

        chart = new CoxcombChart(items);
    }

    @Override public void start(Stage stage) {
        StackPane pane = new StackPane(chart);
        pane.setPadding(new Insets(10));

        Scene scene = new Scene(pane);

        stage.setTitle("Coxcomb Chart");
        stage.setScene(scene);
        stage.show();
    }

    @Override public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}