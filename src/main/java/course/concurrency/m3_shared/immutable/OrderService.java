package course.concurrency.m3_shared.immutable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderService {

    private final Map<Long, Order> currentOrders = new ConcurrentHashMap<>();
    public synchronized long createOrder(List<Item> items) {
        Order order = new Order(items);
        currentOrders.put(order.getId(), order);
        return order.getId();
    }

    public synchronized void updatePaymentInfo(long orderId, PaymentInfo paymentInfo) {
        Order paid = currentOrders.computeIfPresent(orderId, (key, o) -> o.withInfo(paymentInfo));
        if (paid != null && paid.checkStatus()) {
            deliver(paid);
        }
    }

    public void setPacked(long orderId) {
        Order packed = currentOrders.computeIfPresent(orderId, (key, o) -> o.packed());
        if (packed != null && packed.checkStatus()) {
            deliver(packed);
        }
    }

    private void deliver(Order order) {
        /* ... */
        currentOrders.computeIfPresent(order.getId(), (key, o) -> o.withStatus(Order.Status.DELIVERED));
    }

    public boolean isDelivered(long orderId) {
        return currentOrders.get(orderId).getStatus().equals(Order.Status.DELIVERED);
    }
}
