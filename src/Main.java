import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    private ZoomManager zm;

    private static final double X = 9.3d;
    private ArrayList<NumericalMethod> n_methods;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart");
        n_methods = new ArrayList<>();
        interface_initialization(stage);
    }

    /** Initialization of the interface objects */
    private void interface_initialization(Stage stage){
        VBox root = new VBox(); // Root layout
        root.setSpacing(10);
        root.setFocusTraversable(true);
        root.setPadding(new Insets(30,30, 30,30));

        // Axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X-axis");
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        yAxis.setLabel("Y-axis");
        yAxis.setAutoRanging(true);
        yAxis.setForceZeroInRange(false);

        // Chart instance
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Functions");
        lineChart.setMinSize(800, 600);

        // Elements below the chart
        VBox elementBox = new VBox();
        elementBox.setSpacing(10);

        HBox line1 = new HBox();
        HBox line2 = new HBox();
        HBox line3 = new HBox();
        HBox line4 = new HBox();
        line1.setSpacing(10);
        line2.setSpacing(10);
        line3.setSpacing(10);
        line4.setSpacing(10);

        // region FUNCTIONS INTERFACE
        Label stepSizeLabel = new Label("Size of the step:");
        TextField stepSizeField = new TextField(".1");
        stepSizeField.setMinWidth(120);
        // endregion
        // region FUNCTION DEF
        ExactFunction exactFunc = new ExactFunction(50, X / 50d, 1d);
        n_methods.add(exactFunc);
        EulerFunction euFunc = new EulerFunction(50, X / 50d, 1d);
        n_methods.add(euFunc);
        EulerImpFunction euImpFunc = new EulerImpFunction(50, X / 50d, 1d);
        n_methods.add(euImpFunc);
        RungeKuttaFunction rkFunc = new RungeKuttaFunction(50, X / 50d, 1d);
        n_methods.add(rkFunc);
        // endregion

        // Some fields and labels
        Label x0Label = new Label("New x0:");
        TextField x0Field = new TextField("0");
        x0Field.setMaxWidth(60);
        Label y0Label = new Label("New y0:");
        TextField y0Field = new TextField("1");
        y0Field.setMaxWidth(60);
        Label errorLabel = new Label("Global trunc. errors (Euler, Euler imp., Runge-Kutta):\n");
        CheckBox cb = new CheckBox("Show truncation error graph");
        cb.setSelected(false);

        // Apply button
        Button ivpBtn = new Button();
        ivpBtn.setText("Apply");
        ivpBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            // region DATA UPDATE
            if (!cb.isSelected()) {
                /* Reset the errors */
                double errEuler = 0d;
                double errEulerImp = 0d;
                double errRungeKutta = 0d;
                /* Set up new x0 and y0 */
                double x0 = Double.parseDouble(x0Field.getText());
                double y0 = Double.parseDouble(y0Field.getText());
                /* Update constant in the exact solution */
                 exactFunc.updateConst(x0, y0);
                 int i = 0;
                 for (NumericalMethod n_method : n_methods) {
                     n_method.setStepSize(Double.parseDouble(stepSizeField.getText()));
                     n_method.setPrevYValue(y0);
                     n_method.getSeries().getData().clear();
                     /* If it is exact function */
                     if (i == 0) {
                         n_method.setPointNumber((int) (X / n_method.getStepSize())); // Re-calculate number of points
                         for (int n = 0; n < n_method.getPointNumber(); ++n) {
                             double next_x = n_method.getStepSize() * (double) (n);
                             double next_y = n_method.function(next_x);
                             XYChart.Data point = new XYChart.Data(next_x, next_y);
                             n_method.getSeries().getData().add(point);
                         }
                     }
                     /* Otherwise */
                     else {
                         n_method.setPointNumber((int) ((X - x0) / (n_method.getStepSize()))); // Re-calculate number of points
                         n_method.getSeries().getData().add(new XYChart.Data(x0, y0)); // Initial point is already given
                         for (int n = 1; n < n_method.getPointNumber(); ++n) {
                             double next_x = n_method.getStepSize() * (double) (n) + x0;
                             double next_y = n_method.function(next_x);
                             n_method.setPrevYValue(next_y);
                             XYChart.Data point = new XYChart.Data(next_x, next_y);
                             n_method.getSeries().getData().add(point);

                             /* Errors computation */
                             switch (i) {
                                 case 1: {
                                     errEuler += exactFunc.function(next_x) - next_y;
                                     break;
                                 }
                                 case 2: {
                                     errEulerImp += exactFunc.function(next_x) - next_y;
                                     break;
                                 }
                                 case 3: {
                                     errRungeKutta += exactFunc.function(next_x) - next_y;
                                     break;
                                 }
                             }
                         }
                     }
                     i++;
             }
             // Label's text update
             errorLabel.setText("Global trunc. errors (Euler, Euler imp., Runge-Kutta) " +
                     ":\n" + errEuler + "\n" + errEulerImp + "\n" + errRungeKutta);
             // endregion
             } else {
                 int i = 0;
                 for (NumericalMethod n_method : n_methods) {
                     n_method.getSeries().getData().clear();
                 }
                 exactFunc.updateConst(0, 1);
                 for (NumericalMethod n_method : n_methods) {
                     if (i > 0) {
                         for (int point_num = 10; point_num < 1000; ++point_num) {
                             double errEuler = 0d;
                             double errEulerImp = 0d;
                             double errRungeKutta = 0d;
                             n_method.setStepSize(X / point_num);
                             n_method.setPrevYValue(1d);
                             for (int n = 1; n < point_num; ++n) {
                                 double next_x = n_method.getStepSize() * (double) (n);
                                 double next_y = n_method.function(next_x);
                                 n_method.setPrevYValue(next_y);

                                 /* Errors computation */
                                 switch (i) {
                                     case 1: {
                                         errEuler += exactFunc.function(next_x) - next_y;
                                         break;
                                     }
                                     case 2: {
                                         errEulerImp += exactFunc.function(next_x) - next_y;
                                         break;
                                     }
                                     case 3: {
                                         errRungeKutta += exactFunc.function(next_x) - next_y;
                                         break;
                                     }
                                 }
                             }
                             switch (i) {
                                 case 1: {
                                     XYChart.Data point = new XYChart.Data(point_num, errEuler);
                                     n_method.getSeries().getData().add(point);
                                     break;
                                 }
                                 case 2: {
                                     XYChart.Data point = new XYChart.Data(point_num, errEulerImp);
                                     n_method.getSeries().getData().add(point);
                                     break;
                                 }
                                 case 3: {
                                     XYChart.Data point = new XYChart.Data(point_num, errRungeKutta);
                                     n_method.getSeries().getData().add(point);
                                     break;
                                 }
                             }
                         }
                     }
                     i++;
                 }
             }
            zm.restoreData();
        });

        // region ADDITION OF THE OBJECT TO THE SCREEN

        root.getChildren().add(lineChart);

        Rectangle empty1 = new Rectangle(100, 0);
        Rectangle empty2 = new Rectangle(50, 0);

        line1.getChildren().add(x0Label);
        line1.getChildren().add(y0Label);
        line1.getChildren().add(empty1);
        line1.getChildren().add(errorLabel);
        line1.getChildren().add(empty2);
        line1.getChildren().add(cb);

        line2.getChildren().add(x0Field);
        line2.getChildren().add(y0Field);
        line2.getChildren().add(ivpBtn);

        line3.getChildren().add(stepSizeLabel);

        line4.getChildren().add(stepSizeField);

        elementBox.getChildren().add(line1);
        elementBox.getChildren().add(line2);
        elementBox.getChildren().add(line3);
        elementBox.getChildren().add(line4);

        root.getChildren().add(elementBox);
        // endregion

        ArrayList<XYChart.Series> series = new ArrayList<>();
        for (NumericalMethod n_method : n_methods) {
            series.add(n_method.getSeries());
        }
        zm = new ZoomManager(root, lineChart, series);

        Scene scene = new Scene(root,1400,900);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}