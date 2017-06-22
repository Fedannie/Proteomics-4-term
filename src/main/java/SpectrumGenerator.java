import msalign_parser.MSTask;

import java.util.HashMap;
import java.util.List;

public class SpectrumGenerator {
    public static final HashMap <Character, Double> amino_masses = new HashMap<>();
    public static final Character[] amino_acids = {'A', 'R', 'N', 'D', 'C', 'E', 'Q', 'G', 'H', 'I',
            'L', 'K', 'M', 'F', 'P', 'S', 'T', 'W', 'Y', 'V'};
    private final MSTask scan;
    private final String tag;
    private final List<Double> peaks;

    static {
        amino_masses.put('A', 71.03711);
        amino_masses.put('R', 156.10111);
        amino_masses.put('N', 114.04293);
        amino_masses.put('D', 115.02694);
        amino_masses.put('C', 103.00919);
        amino_masses.put('E', 129.04259);
        amino_masses.put('Q', 128.05858);
        amino_masses.put('G', 57.02146);
        amino_masses.put('H', 137.05891);
        amino_masses.put('I', 113.08406);
        amino_masses.put('L', 113.08406);
        amino_masses.put('K', 128.09496);
        amino_masses.put('M', 131.04049);
        amino_masses.put('F', 147.06841);
        amino_masses.put('P', 97.05276);
        amino_masses.put('S', 87.03203);
        amino_masses.put('T', 101.04768);
        amino_masses.put('W', 186.07931);
        amino_masses.put('Y', 163.06333);
        amino_masses.put('V', 99.06841);
    }


    public SpectrumGenerator(MSTask scan, String tag, List<Double> peaks) {
        this.scan = scan;
        this.tag = tag;
        this.peaks = peaks;
    }


    public void generate(int pos) throws IndexOutOfBoundsException{
        if (pos < 0 || pos >= (peaks.size() / 2)) {
            throw new IndexOutOfBoundsException();
        }
        System.out.println(tag);
        for (Character i : amino_acids){
            if (scan.has_peak(peaks.get(pos) - amino_masses.get(i))){
                System.out.println("found: " + i.toString() + " result tag: " + i.toString() + tag);
            } else {
                System.out.println("not found: " + i.toString());
            }
            for (Character j : amino_acids){
                if (scan.has_peak(peaks.get(pos) - amino_masses.get(i) - amino_masses.get(j))){
                    System.out.println(i.toString() + ' ' + j.toString() + " result tag: " + j.toString() + i.toString() + tag);
                }
            }
        }
        System.out.println();
        pos = tag.length() - pos - 1;
        for (Character i : amino_acids){
            if (scan.has_peak(peaks.get(pos) + amino_masses.get(i))){
                System.out.println("found: " + i.toString() + " result tag: " + tag + i.toString());
            } else {
                System.out.println("not found: " + i.toString());
            }
            for (Character j : amino_acids){
                if (scan.has_peak(peaks.get(pos) + amino_masses.get(i) + amino_masses.get(j))){
                    System.out.println(i.toString() + ' ' + j.toString() + " result tag: " + tag + i.toString() + j.toString());
                }
            }
        }
        System.out.println();
        System.out.println();
    }
}
