import java.io.RandomAccessFile;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class IOComparison {

    public static void main(String[] args) throws IOException {
        int numWrites = 1000000;
        String fileName = "sequential_io.txt";
        String randomFileName = "random_io.txt";

        // Seqential IO
        long startTime = System.nanoTime();
        try (FileWriter writer = new FileWriter(fileName)) {
            for (int i = 0; i < numWrites; i++) {
                writer.write(i + "\n");
            }
        }
        long seqentialTime = System.nanoTime() - startTime;
        System.out.println("Sequentail I/O Time: " + TimeUnit.NANOSECONDS.toMillis(seqentialTime) + " ms");

        // Random IO
        startTime = System.nanoTime();
        try (RandomAccessFile randomWriter = new RandomAccessFile(randomFileName, "rw")) {
            for (int i = 0; i < numWrites; i++) {
                randomWriter.seek((long) (Math.random() * numWrites) * 10); // random offset
                randomWriter.writeBytes(i + "\n");
            }
        }
        long randomTime = System.nanoTime() - startTime;
        System.out.println("Random I/O Time: " + TimeUnit.NANOSECONDS.toMillis(randomTime) + " ms");

        // Print the comparison between the two times
        System.out.println("Sequential I/O was " + (randomTime/seqentialTime) + " times faster");
    }

}