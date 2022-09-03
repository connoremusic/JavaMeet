package model;

public class CustomerLocationReport {

    private String customerLocation;
    private int totalCustomers;

    /**
     * This class is used to create objects based on SQL queries that group Customers by location. These objects can
     * then be corrected loaded into a TableView.
     * @param customerLocation is the country or subdivision by which the SQL query groups the customers
     * @param totalCustomers is the total number of customers the SQL returns via the COUNT query
     */
    public CustomerLocationReport(String customerLocation, int totalCustomers) {
        this.customerLocation = customerLocation;
        this.totalCustomers = totalCustomers;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }
}
