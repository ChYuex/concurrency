package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private Notifier notifier;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
        this.latestBid = new AtomicMarkableReference<>(new Bid(0L, 0L, 0L), false);
    }

    private AtomicMarkableReference<Bid> latestBid;

    public boolean propose(Bid bid) {
        do {
            if (latestBid.isMarked()) {
                return false;
            }
            if (latestBid.getReference() != null && bid.getPrice() <= latestBid.getReference().getPrice()) {
                return false;
            }
        } while (!latestBid.compareAndSet(latestBid.getReference(), bid, false, false));

        notifier.sendOutdatedMessage(latestBid.getReference());

        return true;
    }

    public Bid getLatestBid() {
        return latestBid.getReference();
    }

    public Bid stopAuction() {
        if (latestBid.isMarked()) {
            return latestBid.getReference();
        }
        Bid latest = latestBid.getReference();
        latestBid.set(latest, true);
        return latest;
    }
}
