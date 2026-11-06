
public class SeatBooking {

    private final boolean[] booked;
    int bookingsMade = 0;

    SeatBooking(int seats) {
        booked = new boolean[seats];
    }

    // Unsynchronized version
    void bookWithoutSync(int seat) {
        if (!booked[seat]) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            booked[seat] = true;
            bookingsMade++;
        }
    }

    // Synchronized method version
    synchronized void book(int seat) {
        if (!booked[seat]) {
            booked[seat] = true;
            bookingsMade++;
        }
    }

    // Synchronized block version
    void bookWithBlock(int seat) {
        synchronized (booked) {
            if (!booked[seat]) {
                booked[seat] = true;
                bookingsMade++;
            }
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        SeatBooking screen = new SeatBooking(10);

        Thread[] users = new Thread[100];

        for (int i = 0; i < users.length; i++) {
            final int seat = i % 10;

            users[i] = new Thread(() -> screen.book(seat));
            users[i].start();
        }

        for (Thread user : users) {
            user.join();
        }

        System.out.println("Seats sold: " +
            screen.bookingsMade);
    }
}
