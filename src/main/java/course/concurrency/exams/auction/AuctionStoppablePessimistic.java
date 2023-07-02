package course.concurrency.exams.auction;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private final Notifier notifier;
    private final Object lock;
    private volatile Bid latestBid;
    private volatile boolean running;

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
        this.latestBid = new Bid(0L, 0L, 0L);
        this.lock = new Object();
        this.running = true;
    }

    public boolean propose(Bid bid) {
        if (running && bid.getPrice() > latestBid.getPrice()) {
            synchronized (lock) {
                if (running && bid.getPrice() > latestBid.getPrice()) {
                    latestBid = bid;
                    notifier.sendOutdatedMessage(latestBid);
                }
            }
            return true;
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }

    public Bid stopAuction() {
        synchronized (lock) {
            this.running = false;
            return latestBid;
        }
    }
}
