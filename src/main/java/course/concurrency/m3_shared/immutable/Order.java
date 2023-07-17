package course.concurrency.m3_shared.immutable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static course.concurrency.m3_shared.immutable.Order.Status.NEW;

public class Order {

    public enum Status { NEW, IN_PROGRESS, DELIVERED }

    private static final AtomicLong nextId = new AtomicLong();

    private final Long id;
    private final List<Item> items;
    private final PaymentInfo paymentInfo;
    private final boolean isPacked;
    private final Status status;

    public Order(List<Item> items) {
        this(nextId.incrementAndGet(), items, null, false, Status.NEW);
    }

    public Long getId() {
        return id;
    }

    private Order(Long id, List<Item> items, PaymentInfo paymentInfo, boolean isPacked, Status status) {
        this.id = id;
        this.items = Collections.unmodifiableList(items);
        this.paymentInfo = paymentInfo;
        this.isPacked = isPacked;
        this.status = status;
    }

    public Order withStatus(Status status) {
        return new Order(this.id, this.items, this.paymentInfo, this.isPacked, status);
    }

    public Order withInfo(PaymentInfo paymentInfo) {
        return new Order(this.id, this.items, paymentInfo, this.isPacked, this.status);
    }

    public Order packed() {
        return new Order(this.id, this.items, this.paymentInfo, true, this.status);

    }

    public boolean checkStatus() {
        return paymentInfo != null && isPacked;
    }

    public Status getStatus() {
        return status;
    }
}
