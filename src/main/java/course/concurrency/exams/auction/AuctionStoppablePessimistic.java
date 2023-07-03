package course.concurrency.exams.auction;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private Notifier notifier;

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
        this.latestBid = new Bid(0L, 0L, 0L);
        this.freeLock = true;
    }

    private volatile Bid latestBid;
    private volatile boolean freeLock;

    public boolean propose(Bid bid) {
        if (freeLock && bid.getPrice() > latestBid.getPrice()) {
            synchronized (this) {
                if (freeLock && bid.getPrice() > latestBid.getPrice()) {
                    notifier.sendOutdatedMessage(latestBid);
                    latestBid = bid;
                    return true;
                }
            }
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }

    public Bid stopAuction() {
        synchronized (this) {
            freeLock = false;
            return latestBid;
        }
    }
}
