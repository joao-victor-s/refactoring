import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private static String norm(String s) {
        return s.replace("\r\n", "\n");
    }

    private static void assertContains(String statement, String expected) {
        assertTrue(statement.contains(expected),
                () -> "Esperava encontrar:\n" + expected + "\n\nStatement foi:\n" + statement);
    }

    @Test
    void statementWithNoRentals() {
        Customer customer = new Customer("João");

        String statement = norm(customer.statement());

        assertContains(statement, "Amount owed is 0");
        assertContains(statement, "You earned 0 frequent renter points");
    }

    @Test
    void statementWithOneRegularMovieOneDay() {
        Customer customer = new Customer("João");
        customer.addRental(new Rental(new Movie("Matrix", Movie.REGULAR), 1));

        String statement = norm(customer.statement());

        assertContains(statement, "\tMatrix\t2.0\n");

        assertContains(statement, "Amount owed is 2.0\n");
        assertContains(statement, "You earned 1 frequent renter points");
    }

    @Test
    void statementWithRegularMovieFourDays() {
        Customer customer = new Customer("João");
        customer.addRental(new Rental(new Movie("Titanic", Movie.REGULAR), 4));

        String statement = norm(customer.statement());

        // REGULAR 4 days: 2 + (4-2)*1.5 = 5.0
        assertContains(statement, "\tTitanic\t5.0\n");
        assertContains(statement, "Amount owed is 5.0\n");
        assertContains(statement, "You earned 1 frequent renter points");
    }

    @Test
    void statementWithNewReleaseTwoDaysGetsBonusPoint() {
        Customer customer = new Customer("João");
        customer.addRental(new Rental(new Movie("Avatar 2", Movie.NEW_RELEASE), 2));

        String statement = norm(customer.statement());

        // NEW_RELEASE 2 days: 2*3 = 6.0, pontos = 2
        assertContains(statement, "\tAvatar 2\t6.0\n");
        assertContains(statement, "Amount owed is 6.0\n");
        assertContains(statement, "You earned 2 frequent renter points");
    }

    @Test
    void statementWithChildrensFourDays() {
        Customer customer = new Customer("João");
        customer.addRental(new Rental(new Movie("Toy Story", Movie.CHILDRENDS), 4));

        String statement = norm(customer.statement());

        // CHILDRENDS 4 days: 1.5 + (4-3)*1.5 = 3.0
        assertContains(statement, "\tToy Story\t3.0\n");
        assertContains(statement, "Amount owed is 3.0\n");
        assertContains(statement, "You earned 1 frequent renter points");
    }

    @Test
    void statementWithMultipleRentals() {
        Customer customer = new Customer("João");

        customer.addRental(new Rental(new Movie("Matrix", Movie.REGULAR), 3));      // 3.5, pts 1
        customer.addRental(new Rental(new Movie("Avatar 2", Movie.NEW_RELEASE), 2)); // 6.0, pts 2
        customer.addRental(new Rental(new Movie("Toy Story", Movie.CHILDRENDS), 4)); // 3.0, pts 1

        String statement = norm(customer.statement());

        assertContains(statement, "\tMatrix\t3.5\n");
        assertContains(statement, "\tAvatar 2\t6.0\n");
        assertContains(statement, "\tToy Story\t3.0\n");

        // total: 3.5 + 6.0 + 3.0 = 12.5
        assertContains(statement, "Amount owed is 12.5\n");

        // pontos: 1 + 2 + 1 = 4
        assertContains(statement, "You earned 4 frequent renter points");
    }
}
