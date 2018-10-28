public class EulerFunction extends NumericalMethod {

    EulerFunction(int point_number, double step_size, double prev_y_value) {
        super(point_number, step_size, prev_y_value);
        getSeries().setName("Euler's app. function");
    }

    @Override
    public double function(double x) {
        return getPrevYValue() + getStepSize()*derivative(x - getStepSize(), getPrevYValue());
    }

}
