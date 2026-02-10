package com.bmw.maintenance.commons.serialization;

/**
 * Represents a schema with versioning support for backward compatibility.
 * <p>
 * Implementations should provide a version number that can be used to handle
 * schema evolution and migration when deserializing persisted data.
 * </p>
 */
public interface VersionedSchema {

    /**
     * Returns the version of this schema.
     *
     * @return the schema version number
     */
    int schemaVersion();
}
