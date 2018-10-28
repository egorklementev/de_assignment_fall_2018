import javafx.scene.chart.XYChart;

public class NumericalMethod {

    private XYChart.Series series; // Data (points)
    private int point_number;
    private double step_size;
    private double prev_y_value; // Y-value of the last computed step

    NumericalMethod(
            int point_number,
            double step_size,
            double prev_y_value
    ) {
       this.point_number = point_number;
       this.step_size = step_size;
       this.prev_y_value = prev_y_value;
       series = new XYChart.Series();
    }

    public double function(double x) {
        return 0;
    }

    /** Given derivative of the exact function */
    double derivative(double x, double y) {
        return (Math.pow(Math.E, -Math.sin(x))) - y * Math.cos(x);
    }

    XYChart.Series getSeries() {
        return series;
    }

    int getPointNumber() {
        return point_number;
    }

    double getStepSize() {
        return step_size;
    }

    double getPrevYValue() {
        return prev_y_value;
    }

    void setPrevYValue(double prev_y_value) {
        this.prev_y_value = prev_y_value;
    }

    void setStepSize(double step_size) {
        this.step_size = step_size;
    }

    void setPointNumber(int point_number) {
        this.point_number = point_number;
    }
}
