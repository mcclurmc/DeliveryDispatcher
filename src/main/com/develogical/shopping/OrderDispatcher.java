package com.develogical.shopping;

import java.util.HashMap;
import java.util.Map;

public class OrderDispatcher implements VehicleListener {
    private final Warehouse warehouse;

    private Map<Recipient, Parcel> inventory = new HashMap<Recipient, Parcel>();
    private Map<Recipient, Parcel> outbox = new HashMap<Recipient, Parcel>();
    private DeliveryVehicle parkedVehicle;

    public OrderDispatcher(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void placeOrder(Recipient recipient, Order order, OrderTracker orderTracker) {
        if (warehouse.hasStockOf(order)) {
            orderTracker.orderConfirmed(order);
            Item item = warehouse.retrieve(order);
            if (parkedVehicle != null) {
                parkedVehicle.deliver(recipient, new Parcel(item));
            } else if (inventory.containsKey(recipient)) {
                inventory.get(recipient).addItem(item);
            } else {
                inventory.put(recipient, new Parcel(item));
            }

        } else {
            orderTracker.outOfStock(order);
        }
    }

    public void vehicleArrived(DeliveryVehicle vehicle) {
        this.parkedVehicle = vehicle;
        outbox.putAll(inventory);
        inventory.clear();
    }

    public void vehicleDeparted(DeliveryVehicle vehicle) {
        for (Map.Entry<Recipient, Parcel> delivery : outbox.entrySet()) {
            vehicle.deliver(delivery.getKey(), delivery.getValue());
        }
        this.parkedVehicle = null;
        outbox.clear();
    }
}
