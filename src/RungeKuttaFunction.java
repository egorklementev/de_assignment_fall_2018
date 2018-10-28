public class RungeKuttaFunction extends NumericalMethod {

    RungeKuttaFunction(int point_number, double step_size, double prev_y_value) {
        super(point_number, step_size, prev_y_value);
        getSeries().setName("Runge-Kutta app. function");
    }

    /** Runge-Kutta app. function */
    @Override
    public double function(double x) {
        double k1 = derivative(x - getStepSize(), getPrevYValue());
        double k2 = derivative(x - (getStepSize() / 2d), getPrevYValue() + k1 * (getStepSize() / 2d));
        double k3 = derivative(x - (getStepSize() / 2d), getPrevYValue() + k2 * (getStepSize() / 2d));
        double k4 = derivative(x, getPrevYValue() + getStepSize() * k3);
        return getPrevYValue() + (getStepSize() / 6d)*(k1 + 2*k2 + 2*k3 + k4);
    }

}
