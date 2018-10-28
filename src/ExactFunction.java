public class ExactFunction extends NumericalMethod {

    private double CONST = 0;

    ExactFunction(int point_number, double step_size, double prev_y_value) {
        super(point_number, step_size, prev_y_value);
        getSeries().setName("Exact function");
    }

    /** Method recalculates value of the constant according to the initial values */
    void updateConst(double x0, double y0) {
        CONST = (Math.pow(Math.E, Math.sin(x0))) * y0 - x0;
    }

    /** Exact function */
    @Override
    public double function(double x) {
        return (x + CONST)*(Math.pow(Math.E, -Math.sin(x)));
    }

}
