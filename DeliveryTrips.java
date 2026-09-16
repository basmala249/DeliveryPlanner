
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DeliveryTrips {
    List<Trip> trips = new ArrayList<>();

    public List<Trip> getTrips() {
        return trips;
    }
    public void addTrip(Trip trip) {
        trips.add(trip);
    }

    public void sortTripsByMostUrgentFirstDelivery() {
        trips.sort(Comparator.comparingInt(trip ->
        trip.getDeliveries().isEmpty()
        ? Integer.MAX_VALUE
        : trip.getDeliveries().get(0).getPriority()
        ));
    }

}
