import org.systemsbiology.jrap.MSXMLParser;
import org.systemsbiology.jrap.Scan;

import java.util.ArrayList;

class Parser {
    private static final double EPS_TIME = 30;
    private static final double EPS_MASS = 0.09;

    private final MSXMLParser parser;
    private int cnt;
    private int peaks;
    private final double[] times;
    private final double[] masses;
    private ArrayList<ArrayList<PeakFound>> convenient;

    Parser(String databaseName, final double[] times, final double[] masses, int peaks){
        parser = new MSXMLParser(databaseName);
        cnt = parser.getScanCount();
        this.times = times;
        this.masses = masses;
        this.peaks = peaks;

        convenient =  new ArrayList<>();
        for (int i = 0; i < peaks; i++) {
            convenient.add(new ArrayList<>());
        }
    }

    public ArrayList<ArrayList<PeakFound>> computeAndPrint(){
        filter();
        return print();
    }

    private void filter() {
        for (int i = 0; i < peaks; i++) {
            for (int j = 1; j < cnt + 1; j++) {
                if (check(parser.rap(j), masses[i]) &&
                        low_difference_time(parser.rap(j).getDoubleRetentionTime(), times[i])) {
                    convenient.get(i).add(new PeakFound(
                            times[i],
                            masses[i],
                            parser.rap(j).getDoubleRetentionTime(),
                            count_mass(parser.rap(j)),
                            parser.rap(j).getNum()));
                }
            }
        }
    }

    private boolean low_difference_time(double a, double b) {
        return (Math.abs(a - b) < EPS_TIME);
    }

    private boolean low_difference_mass(double a, double b) {
        return (Math.abs(a - b) < EPS_MASS);
    }

    private double count_mass(Scan scan) {
        return ((scan.getPrecursorMz() - 1) * (scan.getPrecursorCharge()));
    }

    private boolean check(Scan scan, double mass) {
        return (scan.getMsLevel() == 2) && (low_difference_mass(count_mass(scan), mass));
    }

    private ArrayList<ArrayList<PeakFound>> print(){
        for (int i = 0; i < convenient.size(); i++){
            if (convenient.get(i).size() > 0) {
                System.out.println(i + " : ");
                for (PeakFound j : convenient.get(i)) {
                    System.out.println("\t* \t time: " + j.getTime_ideal() + " \t mass: " + j.getMass_ideal());
                    System.out.println("\t \t time: " + j.getTime_got() +
                            " \t mass: " + j.getMass_got() + " \t scan_num: " + j.getScans_num());
                }
                System.out.println();
            }
        }
        return convenient;
    }
}
