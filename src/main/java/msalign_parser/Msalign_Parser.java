package msalign_parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Msalign_Parser {
    private static final String BEGIN_IONS = "BEGIN IONS";
    private static final String END_IONS = "END IONS";
    private List<MSTask> data = new ArrayList<>();

    public Msalign_Parser(String filename) throws Exception{
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));
            String line;
            while (!(line = reader.readLine()).equals("--")) {
                if (line.equals(BEGIN_IONS)){
                    List<String> task = new ArrayList<>();
                    while (!(line = reader.readLine()).equals(END_IONS)){
                        task.add(line);
                    }
                    parseTask(task);
                }
            }
    }

    private void parseTask(List<String> task){
        int id = Integer.parseInt(task.get(0).substring(3));
        int scans = Integer.parseInt(task.get(1).substring(6));
        String activation = task.get(2).substring(11);
        double prec_mz = Double.parseDouble(task.get(3).substring(13));
        int prec_charge = Integer.parseInt(task.get(4).substring(17));
        double prec_mass = Double.parseDouble(task.get(5).substring(15));
        MSTask new_task = new MSTask(id, scans, activation, prec_mz, prec_charge, prec_mass);
        for (int i = 6; i < task.size(); i++){
            String spectrum = task.get(i).replace('\t', ' ');
            Scanner sc = new Scanner(spectrum).useLocale(Locale.US);
            double a = sc.nextDouble();
            double b = sc.nextDouble();
            int c = sc.nextInt();
            new_task.addPeak(a, b, c);
        }
        data.add(new_task);
        data.sort((o1, o2) -> Integer.compare(o1.getSCANS(), o2.getSCANS()));
    }

    public MSTask getMSTaskByScans(int scans){
        int res = Arrays.binarySearch(data.toArray(), new MSTask(scans), (o1, o2) -> {
            MSTask task1 = (MSTask) o1;
            MSTask task2 = (MSTask) o2;
            return (Integer.compare(task1.getSCANS(), task2.getSCANS()));
        });
        return res < 0 ? null : data.get(res);
    }
}
