public class PeakFound{
    private final double time_ideal;
    private final double mass_ideal;
    private final double time_got;
    private final double mass_got;
    private final int scans_num;

    public PeakFound(double time_ideal, double mass_ideal, double time_got, double mass_got, int scans_num) {
        this.time_ideal = time_ideal;
        this.mass_ideal = mass_ideal;
        this.time_got = time_got;
        this.mass_got= mass_got;
        this.scans_num = scans_num;
    }

    public double getTime_ideal() {
        return time_ideal;
    }

    public double getMass_ideal() {
        return mass_ideal;
    }

    public double getTime_got() {
        return time_got;
    }

    public double getMass_got() {
        return mass_got;
    }

    public int getScans_num() {
        return scans_num;
    }
}