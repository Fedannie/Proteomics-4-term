package msalign_parser;

import static java.lang.Math.abs;

public class Peak implements Comparable<Peak>{
    private static final double EPS = 0.0001;
    private final double peak1;
    private final double peak2;
    private final int charge;

    public Peak(double peak1, double peak2, int charge) {
        this.peak1 = peak1;
        this.peak2 = peak2;
        this.charge = charge;
    }

    public Peak(double peak1) {
        this.peak1 = peak1;
        this.peak2 = 0;
        this.charge = 0;
    }

    public double getPeak1() {
        return peak1;
    }

    public double getPeak2() {
        return peak2;
    }

    public int getCharge() {
        return charge;
    }

    @Override
    public int compareTo(Peak o) {
        double delta = peak1 - o.getPeak1();
        if (abs(delta) > EPS){
            return delta > 0 ? 1 : -1;
        }
        return 0;
    }
}
