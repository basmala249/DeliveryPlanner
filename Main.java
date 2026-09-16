import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException{
        
         //file read
        if (args.length < 1) {
            System.out.println("Usage: java DeliveryPlanner <file> [strategy] [capacity]");
            return;
        }
        String filePath = args[0];
        String strategy = args.length > 1 ? args[1] : "fill-gaps"; 
        double capacity = args.length > 2 ? Double.parseDouble(args[2]) : 10.0; 
        
        try (FileWriter fw = new FileWriter("Refused_Deliveries.txt", false)) { // false = overwrite
            // just opening it in overwrite mode clears any old content
        } catch (IOException e) {
            System.out.println("Error initializing refused-deliveries file: " + e.getMessage());
        }
        Deliveries deliveries = readDeliveries(filePath, capacity);
        deliveries.sortDeliveriesByArea();

        DeliveryPlanner planner = new DeliveryPlanner(strategy, capacity);
        planner.planDeliveries(deliveries); 
        DeliveryTrips trips = planner.getDeliveryTrips();
        for (Trip trip : trips.getTrips()) {
            System.out.println("Trip Number: " + trip.getTripNumber() + ", Remaining Capacity: " + trip.getRemainingCapacity());
            for (Delivery delivery : trip.getDeliveries()) {
                System.out.println("  Delivery ID: " + delivery.getId() + ", Area: " + delivery.getArea() + ", Priority: " + delivery.getPriority() + ", Weight: " + delivery.getWeight());
            }
        }
        
        exportTripsDetailsText(trips.getTrips());
        exportTripsReportText(trips.getTrips());
    
        
    }
   
    public static Deliveries readDeliveries(String filePath , double capacity) throws IOException {
        Deliveries deliveries = new Deliveries();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); 
            Integer DeliveryNumber = 0;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue; 
                String[] parts = line.split(",");
                DeliveryNumber++;


                if(parts[0].isBlank()) {
                    writeDeliveries(DeliveryNumber.toString(), "Missing ID");
                    continue;
                }
                int id = Integer.parseInt(parts[0].trim());


                if(parts[1].isBlank()) {
                    writeDeliveries(DeliveryNumber.toString(), "Missing Area");
                    continue;
                }
                String area = parts[1].trim().toLowerCase(); // normalize while reading
                

                if(parts[2].isBlank()) {
                    writeDeliveries(DeliveryNumber.toString(), "Missing Priority");
                    continue;
                }
                int priority = Integer.parseInt(parts[2].trim());


                if(parts[3].isBlank()) {
                    writeDeliveries(DeliveryNumber.toString(), "Missing Weight");
                    continue;
                }
                if(Double.parseDouble(parts[3].trim()) <= 0) {
                    writeDeliveries(DeliveryNumber.toString(), "Weight cannot be zero or negative");
                    continue;
                }
                if(Double.parseDouble(parts[3].trim()) > capacity) {
                    writeDeliveries(DeliveryNumber.toString(), "Weight exceeds capacity");
                    continue;
                }
                double weight = Double.parseDouble(parts[3].trim());
                
                deliveries.addDelivery(new Delivery(id, area, priority, weight));
            }
      }
      return deliveries;
   }

   public static void writeDeliveries(String DeliveryNumber , String reason) throws IOException {
        try (FileWriter fw = new FileWriter("Refused_Deliveries.txt", true); // true = append mode
            PrintWriter pw = new PrintWriter(fw)) {
            pw.println("Delivery Number: " + DeliveryNumber + ", Reason: " + reason);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    public static void exportTripsDetailsText(List<Trip> trips) {
        String filePath = "trips_details.txt";
        StringBuilder sb = new StringBuilder();
        int lineLength = 65;

        for (Trip trip : trips) {
            double maxCapacity = trip.getCurrentWeight() + trip.getRemainingCapacity();
            String title = "=== TRIP #" + trip.getTripNumber() + " ===";
            String load = "Load: " + trip.getCurrentWeight() + " / " + maxCapacity + " kg";

            sb.append(centerText(title, lineLength)).append("\n");
            sb.append(centerText(load, lineLength)).append("\n\n");

            sb.append(String.format("%-15s %-12s %-15s %-15s%n", "Delivery ID", "Priority", "Weight (kg)", "Area"));
            sb.append("-".repeat(lineLength)).append("\n");

            for (Delivery d : trip.getDeliveries()) {
                sb.append(String.format("%-15s %-12d %-15.2f %-15s%n",
                        d.getId(), d.getPriority(), d.getWeight(), d.getArea()));
            }

            sb.append("=".repeat(lineLength)).append("\n\n");
        }

        writeToFile(filePath, sb.toString());
    }

    public static void exportTripsReportText(List<Trip> trips) {
        String filePath = "trips_report.txt";
        StringBuilder sb = new StringBuilder();
        int lineLength = 75;

        sb.append(centerText("===========================================", lineLength)).append("\n");
        sb.append(centerText("TRIPS DISPATCH SUMMARY REPORT", lineLength)).append("\n");
        sb.append(centerText("===========================================", lineLength)).append("\n\n");

        sb.append(String.format("%-10s %-18s %-18s %-15s %-12s%n",
                "Trip #", "Deliveries Count", "Used / Max (kg)", "Utilization", "Remaining"));
        sb.append("-".repeat(lineLength)).append("\n");

        int totalDeliveries = 0;
        double totalWeight = 0;

        for (Trip trip : trips) {
            int count = trip.getDeliveriesCount();
            double weight = trip.getCurrentWeight();
            double remaining = trip.getRemainingCapacity();
            double capacity = weight + remaining;
            double utilization = capacity > 0 ? (weight / capacity) * 100 : 0;

            totalDeliveries += count;
            totalWeight += weight;

            sb.append(String.format("%-10d %-18d %-18s %-14.1f%% %-12.2f kg%n",
                    trip.getTripNumber(),
                    count,
                    (int) weight + " / " + (int) capacity + " kg",
                    utilization,
                    remaining));
        }

        sb.append("=".repeat(lineLength)).append("\n");
        sb.append(String.format("Total Trips: %d | Total Deliveries: %d | Total Weight: %.2f kg%n",
                trips.size(), totalDeliveries, totalWeight));

        writeToFile(filePath, sb.toString());
    }

    private static String centerText(String text, int width) {
        if (text.length() >= width) return text;
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text;
    }

    private static void writeToFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            System.out.println("File created successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}
