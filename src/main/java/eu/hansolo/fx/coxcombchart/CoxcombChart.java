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

import javafx.beans.DefaultProperty;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


/**
 * User: hansolo
 * Date: 26.12.17
 * Time: 12:11
 */
@DefaultProperty("children")
public class CoxcombChart extends Region {
    private static final double           PREFERRED_WIDTH  = 250;
    private static final double           PREFERRED_HEIGHT = 250;
    private static final double           MINIMUM_WIDTH    = 50;
    private static final double           MINIMUM_HEIGHT   = 50;
    private static final double           MAXIMUM_WIDTH    = 1024;
    private static final double           MAXIMUM_HEIGHT   = 1024;
    private              double           size;
    private              double           width;
    private              double           height;
    private              Canvas           canvas;
    private              GraphicsContext  ctx;
    private              Pane             pane;
    private              LinkedList<Item> items;


    // ******************** Constructors **************************************
    public CoxcombChart() {
        this(new ArrayList<>());
    }
    public CoxcombChart(final Item... ITEMS) {
        this(Arrays.asList(ITEMS));
    }
    public CoxcombChart(final List<Item> ITEMS) {
        getStylesheets().add(CoxcombChart.class.getResource("coxcomb-chart.css").toExternalForm());
        width  = PREFERRED_WIDTH;
        height = PREFERRED_HEIGHT;
        size   = PREFERRED_WIDTH;
        items  = new LinkedList<>(ITEMS);
        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 || Double.compare(getWidth(), 0.0) <= 0 ||
            Double.compare(getHeight(), 0.0) <= 0) {
            if (getPrefWidth() > 0 && getPrefHeight() > 0) {
                setPrefSize(getPrefWidth(), getPrefHeight());
            } else {
                setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        getStyleClass().add("coxcomb-chart");

        canvas = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT);
        ctx    = canvas.getGraphicsContext2D();

        ctx.setLineCap(StrokeLineCap.BUTT);
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setTextAlign(TextAlignment.CENTER);

        pane = new Pane(canvas);

        getChildren().setAll(pane);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
    }


    // ******************** Methods *******************************************
    @Override public void layoutChildren() {
        super.layoutChildren();
    }

    @Override protected double computeMinWidth(final double HEIGHT) { return MINIMUM_WIDTH; }
    @Override protected double computeMinHeight(final double WIDTH) { return MINIMUM_HEIGHT; }
    @Override protected double computePrefWidth(final double HEIGHT) { return super.computePrefWidth(HEIGHT); }
    @Override protected double computePrefHeight(final double WIDTH) { return super.computePrefHeight(WIDTH); }
    @Override protected double computeMaxWidth(final double HEIGHT) { return MAXIMUM_WIDTH; }
    @Override protected double computeMaxHeight(final double WIDTH) { return MAXIMUM_HEIGHT; }

    @Override public ObservableList<Node> getChildren() { return super.getChildren(); }

    public List<Item> getItems() { return items; }
    public void setItems(final Item... ITEMS) {
        setItems(Arrays.asList(ITEMS));
    }
    public void setItems(final List<Item> ITEMS) {
        items.clear();
        ITEMS.forEach(item -> items.add(item));
        redraw();
    }
    public void addItem(final Item ITEM) {
        if (!items.contains(ITEM)) {
            items.add(ITEM);
            redraw();
        }
    }
    public void addItems(final Item... ITEMS) {
        addItems(Arrays.asList(ITEMS));
    }
    public void addItems(final List<Item> ITEMS) {
        ITEMS.forEach(item -> addItem(item));
    }
    public void removeItem(final Item ITEM) {
        if (items.contains(ITEM)) {
            items.remove(ITEM);
            redraw();
        }
    }
    public void removeItems(final Item... ITEMS) {
        removeItems(Arrays.asList(ITEMS));
    }
    public void removeItems(final List<Item> ITEMS) {
        ITEMS.forEach(item -> removeItem(item));
    }

    public void sortItemsAscending() {
        Collections.sort(items, Comparator.comparingDouble(Item::getValue));
        redraw();
    }
    public void sortItemsDescending() {
        Collections.sort(items, Comparator.comparingDouble(Item::getValue).reversed());
        redraw();
    }

    public double sumOfAllItems() { return items.stream().mapToDouble(Item::getValue).sum(); }


    private void drawChart() {
        int          noOfItems   = items.size();
        double       center      = size * 0.5;
        double       barWidth    = size * 0.04;
        double       sum         = sumOfAllItems();
        double       stepSize    = 360.0 / sum;
        double       angle       = 0;
        double       startAngle  = 90;
        double       xy          = size * 0.32;
        double       minWH       = size * 0.36;
        double       maxWH       = size * 0.64;
        double       wh          = minWH;
        double       whStep      = (maxWH - minWH) / noOfItems;
        Color        textColor   = Color.WHITE;
        GaussianBlur blur        = new GaussianBlur();
        blur.setRadius(3);
        double       x1, y1;
        double       x2, y2;
        double       tx, ty;
        double       endAngle;
        double       radius;

        ctx.clearRect(0, 0, size, size);
        ctx.setFill(textColor);
        ctx.setFont(Font.font(size * 0.035));
        for (int i = 0 ; i < noOfItems ; i++) {
            Item   item  = items.get(i);
            double value = item.getValue();

            startAngle += angle;
            xy         -= (whStep / 2.0);
            wh         += whStep;
            barWidth   += whStep;

            angle    = value * stepSize;
            endAngle = startAngle + angle;
            radius   = wh * 0.5;

            // Segment
            ctx.save();
            ctx.setLineWidth(barWidth);
            ctx.setStroke(item.getColor());
            ctx.strokeArc(xy, xy, wh, wh, startAngle, angle, ArcType.OPEN);
            if (i != (noOfItems-1) && angle > 5) {
                x1 = center + radius * Math.cos(Math.toRadians(endAngle));
                y1 = center - radius * Math.sin(Math.toRadians(endAngle));
                x2 = x1 + 20 * Math.cos(Math.toRadians(endAngle - 90));
                y2 = y1 - 20 * Math.sin(Math.toRadians(endAngle - 90));
                ctx.setEffect(blur);
                ctx.setStroke(new LinearGradient(x1, y1, x2, y2, false, CycleMethod.NO_CYCLE,
                                                 new Stop(0.00, Color.rgb(0, 0, 0, 0.55)),
                                                 new Stop(0.01, Color.rgb(0, 0, 0, 0.25)),
                                                 new Stop(0.25, Color.rgb(0, 0, 0, 0.08)),
                                                 new Stop(0.75, Color.rgb(0, 0, 0, 0.01)),
                                                 new Stop(1.00, Color.rgb(0, 0, 0, 0))));
                ctx.strokeArc(xy, xy, wh, wh, endAngle, -5, ArcType.OPEN);
                if (i == 0) {
                    x1 = center + radius * Math.cos(Math.toRadians(startAngle));
                    y1 = center - radius * Math.sin(Math.toRadians(startAngle));
                    x2 = x1 - 20 * Math.cos(Math.toRadians(startAngle - 90));
                    y2 = y1 + 20 * Math.sin(Math.toRadians(startAngle - 90));
                    ctx.setStroke(new LinearGradient(x1, y1, x2, y2, false, CycleMethod.NO_CYCLE,
                                                     new Stop(0.00, Color.rgb(0, 0, 0, 0.55)),
                                                     new Stop(0.01, Color.rgb(0, 0, 0, 0.25)),
                                                     new Stop(0.25, Color.rgb(0, 0, 0, 0.08)),
                                                     new Stop(0.75, Color.rgb(0, 0, 0, 0.01)),
                                                     new Stop(1.00, Color.rgb(0, 0, 0, 0))));
                    ctx.strokeArc(xy, xy, wh, wh, startAngle, 5, ArcType.OPEN);
                }
            }
            ctx.restore();

            // Percentage
            if (angle > 8) {
                tx = center + radius * Math.cos(Math.toRadians(endAngle - angle * 0.5));
                ty = center - radius * Math.sin(Math.toRadians(endAngle - angle * 0.5));
                ctx.setFill(textColor);
                ctx.fillText(String.format(Locale.US, "%.0f%%", (value / sum * 100.0)), tx, ty, barWidth);
            }
        }
    }


    // ******************** Resizing ******************************************
    private void resize() {
        width = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();
        size = width < height ? width : height;

        if (width > 0 && height > 0) {
            pane.setMaxSize(size, size);
            pane.setPrefSize(size, size);
            pane.relocate((getWidth() - size) * 0.5, (getHeight() - size) * 0.5);

            canvas.setWidth(size);
            canvas.setHeight(size);

            redraw();
        }
    }

    private void redraw() {
        drawChart();
    }
}
