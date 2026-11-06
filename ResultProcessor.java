
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

class ProcessingResult {
    String fileName;
    boolean success;
    String message;

    ProcessingResult(String fileName, boolean success,
                     String message) {
        this.fileName = fileName;
        this.success = success;
        this.message = message;
    }
}

class ResultTask implements Callable<ProcessingResult> {

    private final Path file;

    ResultTask(Path file) {
        this.file = file;
    }

    @Override
    public ProcessingResult call() {
        try {
            if (!Files.exists(file)) {
                throw new FileNotFoundException(
                    "File is missing"
                );
            }

            String content = Files.readString(file);

            if (content.trim().isEmpty() ||
                    !content.trim().matches("\\d+(\\.\\d+)?")) {
                throw new IOException("Corrupt score file");
            }

            double score = Double.parseDouble(content.trim());

            if (score < 0 || score > 100) {
                throw new IOException("Invalid score");
            }

            String result = score >= 40 ? "PASS" : "FAIL";

            return new ProcessingResult(
                file.getFileName().toString(),
                true,
                result
            );

        } catch (Exception e) {
            return new ProcessingResult(
                file.getFileName().toString(),
                false,
                e.getMessage()
            );
        }
    }
}

public class ResultProcessor {

    static void createSampleFiles(Path directory)
            throws IOException {

        Files.createDirectories(directory);

        for (int i = 1; i <= 10000; i++) {
            Path file = directory.resolve("student" + i + ".txt");

            if (i % 100 == 0) {
                continue;
            }

            if (i % 50 == 0) {
                Files.writeString(file, "CORRUPT DATA");
            } else {
                int score = 30 + (i % 71);
                Files.writeString(file, String.valueOf(score));
            }
        }
    }

    static void processFiles(Path directory, int poolSize)
            throws Exception {

        ExecutorService executor =
            Executors.newFixedThreadPool(poolSize);

        ConcurrentLinkedQueue<ProcessingResult> results =
            new ConcurrentLinkedQueue<>();

        List<Future<ProcessingResult>> futures =
            new ArrayList<>();

        long start = System.nanoTime();

        try {
            for (int i = 1; i <= 10000; i++) {
                Path file = directory.resolve(
                    "student" + i + ".txt"
                );

                futures.add(
                    executor.submit(new ResultTask(file))
                );
            }

            for (Future<ProcessingResult> future : futures) {
                try {
                    results.add(future.get());
                } catch (ExecutionException e) {
                    results.add(
                        new ProcessingResult(
                            "Unknown",
                            false,
                            e.getCause().toString()
                        )
                    );
                }
            }

        } finally {
            executor.shutdown();
            executor.awaitTermination(
                1, TimeUnit.HOURS
            );
        }

        long end = System.nanoTime();

        int successCount = 0;
        int failureCount = 0;

        for (ProcessingResult result : results) {
            if (result.success) {
                successCount++;
            } else {
                failureCount++;
            }
        }

        System.out.println("\nThread Pool Size: " + poolSize);
        System.out.println("Successful Files: " + successCount);
        System.out.println("Failed Files: " + failureCount);
        System.out.println("Total Files: " + results.size());

        System.out.println(
            "Execution Time: " +
            ((end - start) / 1_000_000.0) + " ms"
        );
    }

    public static void main(String[] args)
            throws Exception {

        Path directory = Paths.get("score_files");

        createSampleFiles(directory);

        int[] poolSizes = {1, 2, 4, 8};

        for (int poolSize : poolSizes) {
            processFiles(directory, poolSize);
        }
    }
}
