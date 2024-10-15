package ru.robot.utils;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class UI {

    public static void setFormater(ArrayList<TextField> list) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        Consumer<TextField> consumer = t -> {
            t.setTextFormatter(textFormatter);
        };


        list.forEach(consumer);
    }
}
