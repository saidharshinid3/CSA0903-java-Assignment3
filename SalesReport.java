
import java.io.*;
import java.util.*;

public class SalesReport {

    static void generateManualReport(
            String[] files, String outputFile) {

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(
                new FileWriter(outputFile)
            );

            for (String file : files) {
                BufferedReader reader =
                    new BufferedReader(new FileReader(file));

                String line;

                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }

                reader.close();
            }

        } catch (IOException e) {
            System.out.println(
                "Manual report error: " + e.getMessage()
            );

        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println(
                        "Writer closing error: " + e.getMessage()
                    );
                }
            }
        }
    }

    static void generateSafeReport(
            String[] files, String outputFile) {

        try (BufferedWriter writer =
                 new BufferedWriter(new FileWriter(outputFile))) {

            for (String file : files) {
                System.out.println("Reading file: " + file);

                try (BufferedReader reader =
                         new BufferedReader(new FileReader(file))) {

                    String line;

                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }

            System.out.println("Safe report generated");

        } catch (IOException e) {
            System.out.println(
                "Safe report error: " + e.getMessage()
            );
        }
    }

    public static void main(String[] args) {

        try {
            try (PrintWriter writer =
                     new PrintWriter("store1.txt")) {
                writer.println("Store 1 Sales: 10000");
            }

            try (PrintWriter writer =
                     new PrintWriter("store2.txt")) {
                writer.println("Store 2 Sales: 15000");
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        String[] files = {
            "store1.txt",
            "missing-store.txt",
            "store2.txt"
        };

        generateManualReport(files, "manual-report.txt");

        generateSafeReport(files, "safe-report.txt");
    }
}
