import msalign_parser.MSTask;
import msalign_parser.Msalign_Parser;

import java.io.*;
import java.util.*;

public class Main {
    public static final String GREEN_NAT = "res\\GM_native.mzXML";
    public static final String GREEN_RED = "res\\GM_red.mzXML";
    public static final String BLACK_NAT = "res\\BM_native.mzXML";
    public static final String BLACK_RED = "res\\BM_red.mzXML";
    public static final String GREEN_NAT_MSALIGN = "res\\GM_native_msdeconv.msalign";
    public static final String GREEN_RED_MSALIGN = "res\\GM_red_msdeconv.msalign";
    public static final String BLACK_NAT_MSALIGN = "res\\BM_native_msdeconv.msalign";
    public static final String BLACK_RED_MSALIGN = "res\\BM_red_msdeconv.msalign";
    private static ArrayList<ArrayList<PeakFound>> resGN;
    private static ArrayList<ArrayList<PeakFound>> resGR;
    private static ArrayList<ArrayList<PeakFound>> resBN;
    private static ArrayList<ArrayList<PeakFound>> resBR;
    public static double[] times_nat_green;
    public static double[] masses_nat_green;
    public static double[] times_red_green;
    public static double[] masses_red_green;
    public static double[] times_nat_black;
    public static double[] masses_nat_black;
    public static double[] times_red_black;
    public static double[] masses_red_black;
    private static Msalign_Parser parser_gr;
    private static Msalign_Parser parser_br;

    private static double[] getArray(String s){
        return Arrays.stream(s.substring(1, s.length()-1).split(","))
                .map(String::trim).mapToDouble(Double::parseDouble).toArray();
    }

    public static void main(String[] args) {
        try {
            //calculate_peaks_from_mzxml();
            //read_peaks_from_file();
            parser_gr = new Msalign_Parser(GREEN_RED_MSALIGN);
            parser_br = new Msalign_Parser(BLACK_RED_MSALIGN);
            handle_twister_results("res\\twister_gr", parser_gr);
            //handle_twister_results("res\\twister_br", parser_br);

            //console_peak_search();


        } catch (Exception e){
            System.out.println("Exception caught: " + e.getMessage());
        }
    }

    private static void console_peak_search() {
        Scanner sc = new Scanner(System.in).useLocale(Locale.US);
        while (true){
            //System.out.println("Enter scan (GR): ");
            //int scans = sc.nextInt();
            //if (scans == -1){
            //    break;
            //}
            MSTask scan = parser_gr.getMSTaskByScans(1284);
            //MSTask scan = parser_gr.getMSTaskByScans(1273);
            //if (scan == null){
            //    System.out.println("AAAAAA");
            //} else {
                System.out.println("Enter peak (GR): ");
                double peak = sc.nextDouble();
                if (!scan.has_peak(peak)) {
                    System.out.println("- b");
                } else {
                    System.out.println("+ b");
                }
                if (!scan.has_peak(peak, false)) {
                    System.out.println("- nr");
                } else {
                    System.out.println("+ nr");
                }
                if (!scan.has_peak(peak, true)) {
                    System.out.println("- r");
                } else {
                    System.out.println("+ r");
                }
        //    }
        }
    }

    private static void handle_twister_results(String twister_file_name, Msalign_Parser parser) throws Exception{
        File input = new File(twister_file_name);
        BufferedReader in = new BufferedReader(new FileReader(input.getAbsoluteFile()));
        FileWriter writer = new FileWriter(twister_file_name + "_res", false);
        Scanner cin = new Scanner(in).useLocale(Locale.US);

        while (cin.hasNextInt()) {
            int scans = cin.nextInt();
            writer.write(Integer.toString(scans));
            writer.append('\n');

            MSTask scan = parser.getMSTaskByScans(scans);
            String tag = cin.next();
            writer.write(tag + '\n');
 //           boolean flag_not_reversed = true;
 //           List<Integer> failed_not_reversed = new ArrayList<>();
 //           boolean flag_reversed = true;
 //           List<Integer> failed_reversed = new ArrayList<>();
 //           boolean flag_both = true;

            if (scans == 1284){
                double mass = 307.073;
                double EPS = 0.01;
                Object[] scan_peaks = scan.getPeaks();
                List<Double> all_peaks = new ArrayList<>();
                for (Object scan_peak : scan_peaks) {
                    all_peaks.add((double) scan_peak);
                    all_peaks.add(scan.getPRECURSOR_MASS() - (double) scan_peak);
                }
                all_peaks.sort(Double::compareTo);
                for (int i = 0; i < all_peaks.size(); i++){
                    double d = all_peaks.get(i);
                    int j = i - 1;
                    while (j >= 0 && (d - all_peaks.get(j) < mass + EPS)){
                        if ((d - all_peaks.get(j)) > mass - EPS) {
                            System.out.println(Double.toString(d) + " " + Double.toString(all_peaks.get(j)));
                        }
                        j--;
                    }

                    //System.out.println(Double.toString((Double) d));
                }
                System.out.println();
                /*for (Object d : scan.getPeaks()){
                    System.out.println(Double.toString(scan.getPRECURSOR_MASS() - (Double) d));
                }*/
            }
            /*List<Double> peaks = new ArrayList<>();
            for (int i = 0; i <= tag.length(); i++){
                double peak_mass = cin.nextDouble();
                peaks.add(peak_mass);
                if (scan != null && !scan.has_peak(peak_mass, false)){
                    failed_not_reversed.add(i);
                    flag_not_reversed = false;
                }
                if (scan != null && !scan.has_peak(peak_mass, true)){
                    failed_reversed.add(i);
                    flag_reversed = false;
                }
                if (scan != null && !scan.has_peak(peak_mass)){
                    flag_both = false;
                }
                writer.write(Double.toString(peak_mass));
                writer.append(' ');
            }
            writer.append('\n');
            if (scan == null) {
                writer.append('!');
            } else {
                if (!flag_both){
                    writer.write("- b");
                } else {
                    writer.write("+ b");
                }
                writer.append('\n');
                if (!flag_not_reversed){
                    writer.write("- nr " + Arrays.toString(failed_not_reversed.toArray()));
                } else {
                    writer.append("+ nr");
                }
                writer.append('\n');
                if (!flag_reversed){
                    writer.write("- r " + Arrays.toString(failed_reversed.toArray()));
                } else {
                    writer.append("+ r");
                }
            }
            writer.append('\n');
            writer.append('\n');
            if (scan != null) {
                SpectrumGenerator sg = new SpectrumGenerator(scan, tag, peaks);
                System.out.print(Integer.toString(scans) + " ");
                sg.generate(0);
            }*/
        }

        writer.flush();
    }

    private static void input_main_peaks() throws Exception {
            File input = new File("res\\input_main_peaks");
            BufferedReader in = new BufferedReader(new FileReader(input.getAbsoluteFile()));
            /*
                Peaks 6 and 8, green mamba, table 2.
            */
            times_nat_green = getArray(in.readLine());
            masses_nat_green = getArray(in.readLine());
            times_red_green = getArray(in.readLine());
            masses_red_green = getArray(in.readLine());
            /*
                Peaks 5, 8 and 12, black mamba, table 3.
            */
            times_nat_black = getArray(in.readLine());
            masses_nat_black = getArray(in.readLine());
            times_red_black = getArray(in.readLine());
            masses_red_black = getArray(in.readLine());
            in.close();
    }

    private static void calculate_peaks_from_mzxml() throws Exception{
        System.out.println("GREEN NATIVE:");
        Parser parserNatGreen = new Parser(GREEN_NAT, times_nat_green, masses_nat_green, times_nat_green.length);
        resGN = parserNatGreen.computeAndPrint();

        System.out.println("GREEN REDUCED:");
        Parser parserRedGreen = new Parser(GREEN_RED, times_red_green, masses_red_green, times_red_green.length);
        resGR = parserRedGreen.computeAndPrint();

        System.out.println("BLACK NATIVE:");
        Parser parserNatBlack = new Parser(BLACK_NAT, times_nat_black, masses_nat_black, times_nat_black.length);
        resBN = parserNatBlack.computeAndPrint();

        System.out.println("BLACK REDUCED:");
        Parser parserRedBlack = new Parser(BLACK_RED, times_red_black, masses_red_black, times_red_black.length);
        resBR = parserRedBlack.computeAndPrint();
    }

    private static void read_peaks_from_file() throws Exception{
        File input = new File("res\\input_coincidence");
        BufferedReader in = new BufferedReader(new FileReader(input.getAbsoluteFile()));
        Scanner sc = new Scanner(in).useLocale(Locale.US);

        resGN = fill(times_nat_green, masses_nat_green, sc);
        resGR = fill(times_red_green, masses_red_green, sc);
        resBN = fill(times_nat_black, masses_nat_black, sc);
        resBR = fill(times_red_black, masses_red_black, sc);
    }

    private static ArrayList<ArrayList<PeakFound>> fill(double[] times_ideal, double[] masses_ideal, Scanner sc){
        ArrayList<ArrayList<PeakFound>> list = new ArrayList<>();
        int peak_num;
        sc.next();
        while (sc.hasNextInt()) {
            peak_num = sc.nextInt();
            int cnt = sc.nextInt();
            for (int i = 0; i < cnt; i++){
                double ideal_time = times_ideal[peak_num];
                double ideal_mass = masses_ideal[peak_num];
                double time_got = sc.nextDouble();
                double mass_got = sc.nextDouble();
                int scans = sc.nextInt();
                while (list.size() <= peak_num) {
                    list.add(new ArrayList<>());
                }
                list.get(peak_num).add(new PeakFound(ideal_time, ideal_mass, time_got, mass_got, scans));
            }
        }
        sc.next();
        return list;
    }
}



/*private static final double times_nat_green[] = {1740, 1740, 1740, 1740, 1818, 1818, 1818, 1818};
    private static final double masses_nat_green[] = {7044.43, 7087.43, 7061.39, 6761.00, 6787.01, 6577.08, 3741.83, 3944.94};
    private static final double times_red_green[] = {2805, 2847.27, 3052.615, 2280, 2548.235, 2910, 2072.31, 2072.31};
    private static final double masses_red_green[] = {7050.47, 7095.47, 7069.35, 6769.01, 6795.01, 6587.11, 3743.84, 3946.92};

private static final double times_nat_black[] = {1560, 1560, 1560, 1560, 1560, 1560, 1560, 1560, 1560, 1560, 1560,
        1800, 1800, 1800, 1800, 1800, 1800, 1800, 1800, 1800,
        2280, 2280, 2280, 2280, 2280, 2280, 2280, 2280};
    private static final double masses_nat_black[] = {8030.68, 8010.69, 8070.77, 7001.45, 7170.52, 7000.44, 7434.44, 6760.99, 8052.68, 7992.65, 6667.19,
            7040.23, 6394.94, 7129.49, 6597.28, 7540.38, 7418.33, 6351.96, 6381.95, 8023.69,
            7982.73, 6941.47, 6848.24, 6813.35, 6552.30, 8532.05, 8501.04, 7073.26};
    private static final double times_red_black[] = {5490, 3420, 4680, 2256, 2730, 3420, 2970, 2430, 4800, 4146, 2430,
            2730, 2970, 2580, 0, 5040, 4800, 2820, 2820, 4590,
            5040, 3540, 0, 0, 0, 3240, 3240, 0};
    private static final double masses_red_black[] = {8040.73, 8020.74, 8080.77, 7011.31, 7176.56, 7006.46, 7442.49, 6769.04, 8062.75, 8002.75, 6675.25,
            7048.28, 6400.97, 7135.54, 6603.31, 7548.42, 7426.47, 6360.06, 6390.01, 8033.78,
            7992.78, 6947.46, 6854.37, 6819.39, 6558.29, 8543.11, 8512.09, 7082.34};
*/