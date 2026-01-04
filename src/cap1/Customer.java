import java.util.Enumeration;
import java.util.Vector;

public class Customer {
    private String _name;
    private final Vector<Rental> _rentals = new Vector<>();

    public Customer (String name) {
        _name = name;
    }

    public void addRental(Rental rental) {
        _rentals.addElement(rental);
    }

    public String statement(){
        double totalAmount = 0;
        int frequentRenterPoints = 0;

        StringBuilder result = new StringBuilder();

        for (Rental each : _rentals) {
            double thisAmount = 0.0;

            thisAmount = each.getCharge();

            frequentRenterPoints += each.getFrequentRenterPoints();

            result.append("\t")
                    .append(each.getMovie().getTitle())
                    .append("\t")
                    .append(thisAmount)
                    .append("\n");

            totalAmount += thisAmount;
        }
        result.append("Amount owed is ").append(totalAmount).append("\n");

        result.append("You earned ").append(frequentRenterPoints).append(" frequent renter points");

        return result.toString();
    }
}
