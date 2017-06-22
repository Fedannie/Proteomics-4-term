package msalign_parser;

import java.util.*;

public class MSTask {
    private final int ID;
    private final int SCANS;
    private final String ACTIVATION;
    private final double PRECURSOR_MZ;
    private final int PRECURSOR_CHARGE;
    private final double PRECURSOR_MASS;

    public Object[] getPeaks() {
        return (peaks.stream().map(Peak::getPeak1).sorted().toArray());
    }

    private final List<Peak> peaks;
    private boolean sorted = false;

    public MSTask(int id, int scans, String activation, double precursor_mz, int precursor_charge, double precursor_mass) {
        ID = id;
        SCANS = scans;
        ACTIVATION = activation;
        PRECURSOR_MZ = precursor_mz;
        PRECURSOR_CHARGE = precursor_charge;
        PRECURSOR_MASS = precursor_mass;
        peaks = new ArrayList<>();
    }

    public MSTask(int scans){
        ID = 0;
        SCANS = scans;
        ACTIVATION = "";
        PRECURSOR_MZ = 0;
        PRECURSOR_CHARGE = 0;
        PRECURSOR_MASS = 0;
        peaks = new ArrayList<>();
    }

    public boolean addPeak(double peak1, double peak2, int charge){
        return peaks.add(new Peak(peak1, peak2, charge));
    }

    public int getID() {
        return ID;
    }

    public int getSCANS() {
        return SCANS;
    }

    public String getACTIVATION() {
        return ACTIVATION;
    }

    public double getPRECURSOR_MZ() {
        return PRECURSOR_MZ;
    }

    public double getPRECURSOR_CHARGE() {
        return PRECURSOR_CHARGE;
    }

    public double getPRECURSOR_MASS() {
        return PRECURSOR_MASS;
    }

    public boolean has_peak(double new_peak, boolean reversed){
        if (!sorted) {
            peaks.sort((o1, o2) -> Double.compare(o1.getPeak1(), o2.getPeak1()));
            sorted = true;
        }
        if (reversed) {
            return (Collections.binarySearch(peaks, new Peak(PRECURSOR_MASS - new_peak)) > 0);
        } else {
            return (Collections.binarySearch(peaks, new Peak(new_peak)) > 0);
        }
    }

    public boolean has_peak(double new_peak){
        if (!sorted) {
            peaks.sort((o1, o2) -> Double.compare(o1.getPeak1(), o2.getPeak1()));
            sorted = true;
        }
        return ((Collections.binarySearch(peaks, new Peak(PRECURSOR_MASS - new_peak)) > 0) || (Collections.binarySearch(peaks, new Peak(new_peak)) > 0));
    }
}
