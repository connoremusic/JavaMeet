package model;

import utilities.ContactQuery;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String meetingType;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int customerId;
    private int userId;
    private int contactId;
    private String contactName;
    private LocalDateTime startDateFormatted;
    private LocalDateTime endDateFormatted;
    private String startTimeFormatted;

    public Appointment(int appointmentId, String title, String description, String location, String meetingType, Timestamp startTime,
                       Timestamp endTime, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy,
                       int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.meetingType = meetingType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = getContactName();
        this.startDateFormatted = formattedStartDate();
        this.endDateFormatted = formattedEndDate();
        this.startTimeFormatted = formattedStartTime();
    }

    public LocalDateTime formattedStartDate() {
        return startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public LocalDateTime formattedEndDate() {
        return endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public String getContactName() {
        try {
            return ContactQuery.getNameFromId(this.contactId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String formattedStartTime() {
        return startTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getMeetingType() {
        return meetingType;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
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

    public int getCustomerId() {
        return customerId;
    }

    public int getUserId() {
        return userId;
    }

    public int getContactId() {
        return contactId;
    }

    public LocalDateTime getStartDateFormatted() {
        return startDateFormatted;
    }

    public LocalDateTime getEndDateFormatted() {
        return endDateFormatted;
    }

    public String getStartTimeFormatted() {
        return startTimeFormatted;
    }
}
