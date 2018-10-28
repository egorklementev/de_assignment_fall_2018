public class EulerImpFunction extends NumericalMethod {

    EulerImpFunction(int point_number, double step_size, double prev_y_value) {
        super(point_number, step_size, prev_y_value);
        getSeries().setName("Euler's imp. app. function");
    }

    @Override
    public double function(double x) {
        return getPrevYValue() + (getStepSize() / 2d)*(derivative(x - getStepSize(), getPrevYValue()) +
                derivative(x, getPrevYValue() + getStepSize()*derivative(x - getStepSize(), getPrevYValue())));
    }

}
