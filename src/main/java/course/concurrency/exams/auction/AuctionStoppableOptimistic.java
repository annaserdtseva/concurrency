package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private final Notifier notifier;
    private final AtomicMarkableReference<Bid> latestBid;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
        this.latestBid = new AtomicMarkableReference<>(new Bid(0L, 0L, 0L), true);
    }

    public boolean propose(Bid bid) {
        Bid currentLatestBid;
        do {
            if (!latestBid.isMarked()) {
                return false;
            }
            currentLatestBid = latestBid.getReference();
            if (currentLatestBid.getPrice() >= bid.getPrice()) {
                return false;
            }
        } while (!latestBid.compareAndSet(currentLatestBid, bid, true, true));

        notifier.sendOutdatedMessage(currentLatestBid);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.getReference();
    }

    public Bid stopAuction() {
        Bid currentLatestBid = latestBid.getReference();
        if (!latestBid.isMarked()) {
            return currentLatestBid;
        }
        latestBid.set(currentLatestBid, false);
        return currentLatestBid;
    }
}
