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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.scene.paint.Color;


public class Item {
    private double                _value;
    private DoubleProperty        value;
    private String                _name;
    private StringProperty        name;
    private Color                 _color;
    private ObjectProperty<Color> color;


    // ******************** Constructors **********************************
    public Item() {
        this(0, "", Color.RED);
    }
    public Item(final double VALUE, final Color COLOR) {
        this(VALUE, "", COLOR);
    }
    public Item(final double VALUE, final String NAME) {
        this(VALUE, NAME, Color.RED);
    }
    public Item(final double VALUE, final String NAME, final Color COLOR) {
        _value = VALUE;
        _name  = NAME;
        _color = COLOR;
    }


    // ******************** Methods ***************************************
    public double getValue() { return null == value ? _value : value.get(); }
    public void setValue(final double VALUE) {
        if (null == value) {
            _value = VALUE;
        } else {
            value.set(VALUE);
        }
    }
    public DoubleProperty valueProperty() {
        if (null == value) {
            value = new DoublePropertyBase(_value) {
                @Override protected void invalidated() { }
                @Override public Object getBean() { return Item.this; }
                @Override public String getName() { return "value"; }
            };
        }
        return value;
    }

    public String getName() { return null == name ? _name : name.get(); }
    public void setName(final String NAME) {
        if (null == name) {
            _name = NAME;
        } else {
            name.set(NAME);
        }
    }
    public StringProperty nameProperty() {
        if (null == name) {
            name = new StringPropertyBase(_name) {
                @Override protected void invalidated() { }
                @Override public Object getBean() { return Item.this; }
                @Override public String getName() { return "name"; }
            };
            _name = null;
        }
        return name;
    }

    public Color getColor() { return null == color ? _color : color.get(); }
    public void setColor(final Color COLOR) {
        if (null == color) {
            _color = COLOR;
        } else {
            color.set(COLOR);
        }
    }
    public ObjectProperty<Color> colorProperty() {
        if (null == color) {
            color = new ObjectPropertyBase<Color>(_color) {
                @Override protected void invalidated() {  }
                @Override public Object getBean() { return Item.this; }
                @Override public String getName() { return "color"; }
            };
            _color = null;
        }
        return color;
    }


    @Override public String toString() {
        return new StringBuilder().append("{\n")
                                  .append("  \"name\":\"").append(getName()).append("\",\n")
                                  .append("  \"value\":").append(getValue()).append(",\n")
                                  .append("  \"color\":\"").append(getColor().toString().replace("0x", "#")).append("\"\n")
                                  .append("}")
                                  .toString();
    }
}
