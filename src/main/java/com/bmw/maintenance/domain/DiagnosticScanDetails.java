package com.bmw.maintenance.domain;

import com.bmw.maintenance.domain.enums.ScannerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticScanDetails implements MaintenanceDetails{

    private List<String> errorCodes;
    private ScannerType scannerType;
}
