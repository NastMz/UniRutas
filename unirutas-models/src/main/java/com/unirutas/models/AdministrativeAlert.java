package com.unirutas.models;

import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

@Table(name="AdministrativeAlert")
public class AdministrativeAlert {
    @PrimaryKey(name = "administrative_code")
    private String administrativeCode;
    @PrimaryKey(name = "alert_id")
    private String alertId;

    public AdministrativeAlert(String administrativeCode, String alertId) {
        this.administrativeCode = administrativeCode;
        this.alertId = alertId;
    }

    public void assignAdministrative() {
        // TODO: Lógica para agregar la relación en la base de datos
    }

    public void removeAdministrative() {
        // TODO: Lógica para eliminar la relación en la base de datos
    }
}
