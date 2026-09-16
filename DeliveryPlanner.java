
import java.util.List;

public class DeliveryPlanner {
    double capacity;
    DeliveryTrips deliveryTrips = new DeliveryTrips();

    public DeliveryPlanner(double capacity) {
        this.capacity = capacity;
    }

    public void planDeliveries(Deliveries deliveries) {
        
        int tripNumber = 1;
        for (String area : deliveries.getAllDeliveries().keySet()) {
            List<Delivery> areaDeliveries = deliveries.getDeliveriesByArea(area);
            Trip currentTrip = new Trip(capacity, tripNumber++);
            for (Delivery delivery : areaDeliveries) {
                if (!currentTrip.addDeliveryToTrip(delivery)) {
                    deliveryTrips.addTrip(currentTrip);
                    currentTrip = new Trip(capacity, tripNumber++);
                    if (!currentTrip.addDeliveryToTrip(delivery)) {
                        System.out.println("Delivery " + delivery.getId() + " exceeds the trip capacity and cannot be added.");
                    }
                }
            }
            if (!currentTrip.getDeliveries().isEmpty()) {
                deliveryTrips.addTrip(currentTrip);
            }
        }
    } 
    public DeliveryTrips getDeliveryTrips() {
        deliveryTrips.sortTripsByMostUrgentFirstDelivery();
        return deliveryTrips;
    }
}