package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionOptimistic implements Auction {

    private final Notifier notifier;
    private final AtomicReference<Bid> latestBid;

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
        this.latestBid = new AtomicReference<>(new Bid(0L, 0L, 0L));
    }

    public boolean propose(Bid bid) {
        Bid currentLatestBid;
        do {
            currentLatestBid = latestBid.get();
            if (currentLatestBid.getPrice() >= bid.getPrice()) {
                return false;
            }
        } while(!latestBid.compareAndSet(currentLatestBid, bid));

        notifier.sendOutdatedMessage(currentLatestBid);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }
}
