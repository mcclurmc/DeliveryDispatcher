package com.develogical.shopping;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(value=JMock.class)
public class OrderDispatcherTest {
    Mockery context = new Mockery();
    private final Warehouse warehouse = context.mock(Warehouse.class);

    private final OrderDispatcher orderDispatcher = new OrderDispatcher(warehouse);

    @Test
    public void itemIsInStock_ThenConfirmOrder() {
        // Objects we need
        final Order order = new Order("My Order");
        final OrderTracker orderTracker = context.mock(OrderTracker.class);
        Recipient recipient = new Recipient("","","");

        context.checking(new Expectations() {{
            oneOf(warehouse).hasStockOf(order);
            will(returnValue(true));
            oneOf(orderTracker).orderConfirmed(order);
            ignoring(warehouse);
        }});

        orderDispatcher.placeOrder(recipient, order, orderTracker);
    }
}
