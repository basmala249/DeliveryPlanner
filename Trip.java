import java.util.ArrayList;
import java.util.List;

public class Trip {
    double remainingCapacity;
    int tripNumber;

    List<Delivery> deliveries = new ArrayList<>();

    public Trip(double remainingCapacity, int tripNumber) {
        this.remainingCapacity = remainingCapacity;
        this.tripNumber = tripNumber;
    }
 


    public double getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(double remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    public int getTripNumber() {
        return tripNumber;
    }

    public void setTripNumber(int tripNumber) {
        this.tripNumber = tripNumber;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public boolean addDeliveryToTrip(Delivery delivery) {
        if (remainingCapacity >= delivery.getWeight()) {
            deliveries.add(delivery);
            remainingCapacity -= delivery.getWeight();
            return true;
        }
        return false;
    }

    public double getCurrentWeight() {
        double totalWeight = 0;
        for (Delivery delivery : deliveries) {
            totalWeight += delivery.getWeight();
        }
        return totalWeight;
    }

    public int getDeliveriesCount() {
        return deliveries.size();
    }
    
    
}
