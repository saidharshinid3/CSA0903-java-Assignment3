
class ScannerThread extends Thread {

    private int stationNumber;
    private int scripts;

    ScannerThread(int stationNumber, int scripts) {
        this.stationNumber = stationNumber;
        this.scripts = scripts;
    }

    @Override
    public void run() {
        try {
            System.out.println(
                "Station " + stationNumber + " started"
            );

            Thread.sleep(1000);

            System.out.println(
                "Station " + stationNumber +
                " scanned " + scripts + " scripts"
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Station interrupted");
        }
    }
}

class ScannerRunnable implements Runnable {

    private int stationNumber;
    private int scripts;

    ScannerRunnable(int stationNumber, int scripts) {
        this.stationNumber = stationNumber;
        this.scripts = scripts;
    }

    @Override
    public void run() {
        try {
            System.out.println(
                "Runnable Station " + stationNumber + " started"
            );

            Thread.sleep(1000);

            System.out.println(
                "Runnable Station " + stationNumber +
                " scanned " + scripts + " scripts"
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class ScannerStations {

    public static void main(String[] args)
            throws InterruptedException {

        Thread[] stations = new Thread[4];

        for (int i = 0; i < 4; i++) {
            stations[i] = new ScannerThread(i + 1, 25);
            stations[i].start();
        }

        for (Thread station : stations) {
            station.join();
        }

        System.out.println("All Thread Stations Completed");

        Thread[] runnableStations = new Thread[4];

        for (int i = 0; i < 4; i++) {
            runnableStations[i] = new Thread(
                new ScannerRunnable(i + 1, 25)
            );

            runnableStations[i].start();
        }

        for (Thread station : runnableStations) {
            station.join();
        }

        System.out.println("All Runnable Stations Completed");
    }
}
