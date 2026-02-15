package com.bmw.maintenance.domain;

import com.bmw.maintenance.domain.enums.TirePosition;
import com.bmw.maintenance.domain.enums.TireServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TireServiceDetails implements MaintenanceDetails {

    private TireServiceType tireServiceType;

    private TirePosition tirePosition;
}


