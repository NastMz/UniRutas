package com.unirutas.models;

import com.unirutas.core.annotations.Column;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;

@Table(name="Service")
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
        // TODO: Lógica para marcar la alerta como leída en la base de datos
    }

    public void markNotRead() {
        // TODO: Lógica para marcar la alerta como no leída en la base de datos
    }
}
