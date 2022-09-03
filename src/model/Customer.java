package model;

import utilities.CustomerQuery;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Customer {

    private int customerId;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int divisionId;
    private String country;
    private String divisionName;
    private LocalDateTime joinDate;

    public Customer(int customerId, String name, String address, String postalCode, String phone, Timestamp createDate,
                    String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int divisionId) throws SQLException {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
        this.country = CustomerQuery.getCountryFromDivisionId(divisionId);
        this.divisionName = CustomerQuery.getDivisionName(divisionId);
        this.joinDate = getJoinDate(createDate);
    }

    public LocalDateTime getJoinDate(Timestamp createDate) {
        return this.createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Override
    public String toString() {
        return name;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public String getCountry() {
        return country;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }
}
