import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deliveries {
    Map<String, List<Delivery>> byArea ;
    
    Deliveries(){
        byArea = new HashMap<>();
    }
    
    public void addDelivery(Delivery delivery) {
        byArea.computeIfAbsent(delivery.getArea(), k -> new ArrayList<>()).add(delivery);
    }

    public List<Delivery> getDeliveriesByArea(String area) {
        return byArea.getOrDefault(area, new ArrayList<>());
    }

    public Map<String, List<Delivery>> getAllDeliveries() {
        return byArea;
    }

    public void sortDeliveriesByArea(){
        for (List<Delivery> list : byArea.values()) {
            list.sort(
                Comparator.comparingInt(Delivery::getPriority)
                    .thenComparingDouble(Delivery::getWeight)
                    .thenComparingInt(Delivery::getId)
            );
        }
    }

}
