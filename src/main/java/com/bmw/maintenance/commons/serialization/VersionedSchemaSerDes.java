package com.bmw.maintenance.commons.serialization;

/**
 * Generic interface for serializing and deserializing versioned schema objects.
 *
 * @param <S> the type of the serialized representation (e.g., String, byte[], etc.)
 */
public interface VersionedSchemaSerDes<S>{

    /**
     * Serializes a versioned schema object to a string.
     *
     * @param schema the versioned schema object to serialize
     * @return the serialized string
     */
    S serialize(VersionedSchema schema);

    /**
     * Deserializes a string into a versioned schema object\.
     *
     * @param data the string to deserialize
     * @return the deserialized versioned schema object
     */
    VersionedSchema deserialize(S data);

}
