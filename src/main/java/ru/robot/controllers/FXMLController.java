package ru.robot.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.robot.utils.UI;

public class FXMLController implements Initializable {


    public Label lSetFrame;
    public Button button;
    public TextArea textArea;
    public TextField m1_11;
    public TextField m1_12;
    public TextField m1_13;
    public TextField m1_21;
    public TextField m1_22;
    public TextField m1_23;
    public TextField m1_31;
    public TextField m1_32;
    public TextField m1_33;

    public TextField m2_11;
    public TextField m2_12;
    public TextField m2_13;
    public TextField m2_21;
    public TextField m2_22;
    public TextField m2_23;
    public TextField m2_31;
    public TextField m2_32;
    public TextField m2_33;
    private final ArrayList<TextField> m1arrayList = new ArrayList<>();
    private final ArrayList<TextField> m2arrayList = new ArrayList<>();



    @Override
    public void initialize(URL url, ResourceBundle rb) {

        m1arrayList.add(m1_11);
        m1arrayList.add(m1_12);
        m1arrayList.add(m1_13);
        m1arrayList.add(m1_21);
        m1arrayList.add(m1_22);
        m1arrayList.add(m1_23);
        m1arrayList.add(m2_31);
        m1arrayList.add(m1_32);
        m1arrayList.add(m1_33);

        m2arrayList.add(m1_11);
        m2arrayList.add(m1_12);
        m2arrayList.add(m1_13);
        m2arrayList.add(m1_21);
        m2arrayList.add(m1_22);
        m2arrayList.add(m1_23);
        m2arrayList.add(m2_31);
        m2arrayList.add(m1_32);
        m2arrayList.add(m1_33);

//        setFormater(m1arrayList);
//        setFormater(m2arrayList);
    }



    @FXML
    private void buttonClicked() {
        System.out.println("Button clicked!");
//        DoubleMatrix2D m1 = collectDataFromTextFieldMatrix(m1arrayList);
//        DoubleMatrix2D m2 = collectDataFromM2();
//        textArea.setText(m1.toString());

    }





}
