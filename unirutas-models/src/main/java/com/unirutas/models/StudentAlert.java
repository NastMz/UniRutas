package com.unirutas.models;

import com.flexcore.annotations.Column;
import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

@Table(name = "StudentAlert")
public class StudentAlert {
    @Column(name = "is_read")
    private boolean isRead;
    @PrimaryKey(name = "student_code")
    private String studentCode;
    @PrimaryKey(name = "alert_id")
    private String alertId;

    public StudentAlert(boolean isRead, String studentCode, String alertId) {
        this.isRead = isRead;
        this.studentCode = studentCode;
        this.alertId = alertId;
    }

    public void markRead() {
        isRead = true;
    }

    public void markNotRead() {
        isRead = false;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public String getAlertId() {
        return alertId;
    }

}
