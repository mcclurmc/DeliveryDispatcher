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
    private final Order order = new Order("My Order");
    private final OrderTracker orderTracker = context.mock(OrderTracker.class);
    private final Recipient recipient = new Recipient("","","");

    @Test
    public void itemIsInStock_ThenConfirmOrder() {
        context.checking(new Expectations() {{
            oneOf(warehouse).hasStockOf(order);
            will(returnValue(true));
            oneOf(orderTracker).orderConfirmed(order);
            ignoring(warehouse);
        }});

        orderDispatcher.placeOrder(recipient, order, orderTracker);
    }

    @Test
    public void itemIsNotInStock_ThenMarked_OutOfStock() {
        context.checking(new Expectations() {{
            oneOf(warehouse).hasStockOf(order);
            will(returnValue(false));
            oneOf(orderTracker).outOfStock(order);
            ignoring(warehouse);
        }});

        orderDispatcher.placeOrder(recipient, order, orderTracker);
    }
}
