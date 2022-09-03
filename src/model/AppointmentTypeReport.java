package model;

public class AppointmentTypeReport {

    private String appointmentType;
    private int totalAppointments;

    /**
     * This class is used to create objects based on SQL queries that group appointments by type. These objects can
     * then be corrected loaded into a TableView.
     * @param appointmentType is the type by which the SQL query groups the appointments
     * @param totalAppointments is the total number of appointments the SQL returns via the COUNT query
     */
    public AppointmentTypeReport(String appointmentType, int totalAppointments) {
        this.appointmentType = appointmentType;
        this.totalAppointments = totalAppointments;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }
}
