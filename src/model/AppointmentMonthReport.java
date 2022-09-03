package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AppointmentMonthReport {

    private String month;
    private LocalDateTime formattedMonth;
    private int totalAppointments;

    /**
     * This class is used to create objects based on SQL queries that group appointments by month. These objects can
     * then be corrected loaded into a TableView.
     * @param month is the month/year combo by which the SQL query groups the appointments
     * @param totalAppointments is the total number of appointments the SQL returns via the COUNT query
     */
    public AppointmentMonthReport(String month, int totalAppointments) {
        this.month = month;
        this.totalAppointments = totalAppointments;
        this.formattedMonth = convertTime(this.month);
    }

    public String getMonth() {
        return month;
    }

    private LocalDateTime convertTime(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM");
            Date formattedDate = inputFormat.parse(inputDate);
            return formattedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LocalDateTime getFormattedMonth() {
        return formattedMonth;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }
}
