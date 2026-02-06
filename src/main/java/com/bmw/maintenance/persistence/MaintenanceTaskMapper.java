package com.bmw.maintenance.persistence;

import com.bmw.maintenance.domain.MaintenanceTask;

public interface MaintenanceTaskMapper {
    MaintenanceTask toDomain(MaintenanceTaskEntity entity);
    MaintenanceTaskEntity toEntity(MaintenanceTask task);
}
