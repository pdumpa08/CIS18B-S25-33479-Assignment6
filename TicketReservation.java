// =============================
// Ticket Class
// =============================
class Ticket {
    private final int id;
    private final String event;

    public Ticket(int id, String event) {
        this.id = id;
        this.event = event;
    }

    public int getId() { return id; }
    public String getEvent() { return event; }
}

// =============================
// Ticket Pool Class
// =============================
class TicketPool {
    private int availableTickets;

    public TicketPool(int totalTickets) {
        this.availableTickets = totalTickets;
    }

    public synchronized Ticket reserveTicket(String customerName) {
        while (availableTickets <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        availableTickets--;
        System.out.println(customerName + " reserved a ticket. Tickets left: " + availableTickets);
        return new Ticket(availableTickets, "Concert");
    }
}

// =============================
// Customer Class
// =============================
class Customer implements Runnable {
    private final String customerName;
    private TicketPool ticketPool;

    public Customer (String customerName, TicketPool ticketPool) {
        this.customerName = customerName;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        ticketPool.reserveTicket(customerName);
    }
}

// =============================
// Main/Test Class
// =============================
public class TicketReservation {
    public static void main(String[] args) {
        // create ticketpool
        TicketPool ticketPool = new TicketPool(10);

        // create customers
        Customer jeff = new Customer("Jeff", ticketPool);
        Customer owen = new Customer("Owen", ticketPool);

        // run threads
        Thread thread1 = Thread.startVirtualThread(jeff);
        Thread thread2 = Thread.startVirtualThread(owen);
        
        // Wait for all virtual threads to finish
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}