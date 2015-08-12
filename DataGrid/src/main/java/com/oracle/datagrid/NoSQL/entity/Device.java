/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.oracle.datagrid.NoSQL.entity;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class Device extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Device\",\"namespace\":\"com.oracle.datagrid.NoSQL.entity\",\"fields\":[{\"name\":\"deviceId\",\"type\":\"int\"},{\"name\":\"deviceType\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"deviceSerialNumber\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"deviceStatus\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"deviceMake\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"deviceModel\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"macAddress\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"ipAddress\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public int deviceId;
  @Deprecated public java.lang.String deviceType;
  @Deprecated public java.lang.String deviceSerialNumber;
  @Deprecated public java.lang.String deviceStatus;
  @Deprecated public java.lang.String deviceMake;
  @Deprecated public java.lang.String deviceModel;
  @Deprecated public java.lang.String macAddress;
  @Deprecated public java.lang.String ipAddress;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public Device() {}

  /**
   * All-args constructor.
   */
  public Device(java.lang.Integer deviceId, java.lang.String deviceType, java.lang.String deviceSerialNumber, java.lang.String deviceStatus, java.lang.String deviceMake, java.lang.String deviceModel, java.lang.String macAddress, java.lang.String ipAddress) {
    this.deviceId = deviceId;
    this.deviceType = deviceType;
    this.deviceSerialNumber = deviceSerialNumber;
    this.deviceStatus = deviceStatus;
    this.deviceMake = deviceMake;
    this.deviceModel = deviceModel;
    this.macAddress = macAddress;
    this.ipAddress = ipAddress;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return deviceId;
    case 1: return deviceType;
    case 2: return deviceSerialNumber;
    case 3: return deviceStatus;
    case 4: return deviceMake;
    case 5: return deviceModel;
    case 6: return macAddress;
    case 7: return ipAddress;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: deviceId = (java.lang.Integer)value$; break;
    case 1: deviceType = (java.lang.String)value$; break;
    case 2: deviceSerialNumber = (java.lang.String)value$; break;
    case 3: deviceStatus = (java.lang.String)value$; break;
    case 4: deviceMake = (java.lang.String)value$; break;
    case 5: deviceModel = (java.lang.String)value$; break;
    case 6: macAddress = (java.lang.String)value$; break;
    case 7: ipAddress = (java.lang.String)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'deviceId' field.
   */
  public java.lang.Integer getDeviceId() {
    return deviceId;
  }

  /**
   * Sets the value of the 'deviceId' field.
   * @param value the value to set.
   */
  public void setDeviceId(java.lang.Integer value) {
    this.deviceId = value;
  }

  /**
   * Gets the value of the 'deviceType' field.
   */
  public java.lang.String getDeviceType() {
    return deviceType;
  }

  /**
   * Sets the value of the 'deviceType' field.
   * @param value the value to set.
   */
  public void setDeviceType(java.lang.String value) {
    this.deviceType = value;
  }

  /**
   * Gets the value of the 'deviceSerialNumber' field.
   */
  public java.lang.String getDeviceSerialNumber() {
    return deviceSerialNumber;
  }

  /**
   * Sets the value of the 'deviceSerialNumber' field.
   * @param value the value to set.
   */
  public void setDeviceSerialNumber(java.lang.String value) {
    this.deviceSerialNumber = value;
  }

  /**
   * Gets the value of the 'deviceStatus' field.
   */
  public java.lang.String getDeviceStatus() {
    return deviceStatus;
  }

  /**
   * Sets the value of the 'deviceStatus' field.
   * @param value the value to set.
   */
  public void setDeviceStatus(java.lang.String value) {
    this.deviceStatus = value;
  }

  /**
   * Gets the value of the 'deviceMake' field.
   */
  public java.lang.String getDeviceMake() {
    return deviceMake;
  }

  /**
   * Sets the value of the 'deviceMake' field.
   * @param value the value to set.
   */
  public void setDeviceMake(java.lang.String value) {
    this.deviceMake = value;
  }

  /**
   * Gets the value of the 'deviceModel' field.
   */
  public java.lang.String getDeviceModel() {
    return deviceModel;
  }

  /**
   * Sets the value of the 'deviceModel' field.
   * @param value the value to set.
   */
  public void setDeviceModel(java.lang.String value) {
    this.deviceModel = value;
  }

  /**
   * Gets the value of the 'macAddress' field.
   */
  public java.lang.String getMacAddress() {
    return macAddress;
  }

  /**
   * Sets the value of the 'macAddress' field.
   * @param value the value to set.
   */
  public void setMacAddress(java.lang.String value) {
    this.macAddress = value;
  }

  /**
   * Gets the value of the 'ipAddress' field.
   */
  public java.lang.String getIpAddress() {
    return ipAddress;
  }

  /**
   * Sets the value of the 'ipAddress' field.
   * @param value the value to set.
   */
  public void setIpAddress(java.lang.String value) {
    this.ipAddress = value;
  }

  /** Creates a new Device RecordBuilder */
  public static com.oracle.datagrid.NoSQL.entity.Device.Builder newBuilder() {
    return new com.oracle.datagrid.NoSQL.entity.Device.Builder();
  }
  
  /** Creates a new Device RecordBuilder by copying an existing Builder */
  public static com.oracle.datagrid.NoSQL.entity.Device.Builder newBuilder(com.oracle.datagrid.NoSQL.entity.Device.Builder other) {
    return new com.oracle.datagrid.NoSQL.entity.Device.Builder(other);
  }
  
  /** Creates a new Device RecordBuilder by copying an existing Device instance */
  public static com.oracle.datagrid.NoSQL.entity.Device.Builder newBuilder(com.oracle.datagrid.NoSQL.entity.Device other) {
    return new com.oracle.datagrid.NoSQL.entity.Device.Builder(other);
  }
  
  /**
   * RecordBuilder for Device instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Device>
    implements org.apache.avro.data.RecordBuilder<Device> {

    private int deviceId;
    private java.lang.String deviceType;
    private java.lang.String deviceSerialNumber;
    private java.lang.String deviceStatus;
    private java.lang.String deviceMake;
    private java.lang.String deviceModel;
    private java.lang.String macAddress;
    private java.lang.String ipAddress;

    /** Creates a new Builder */
    private Builder() {
      super(com.oracle.datagrid.NoSQL.entity.Device.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.oracle.datagrid.NoSQL.entity.Device.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.deviceId)) {
        this.deviceId = data().deepCopy(fields()[0].schema(), other.deviceId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.deviceType)) {
        this.deviceType = data().deepCopy(fields()[1].schema(), other.deviceType);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.deviceSerialNumber)) {
        this.deviceSerialNumber = data().deepCopy(fields()[2].schema(), other.deviceSerialNumber);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.deviceStatus)) {
        this.deviceStatus = data().deepCopy(fields()[3].schema(), other.deviceStatus);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.deviceMake)) {
        this.deviceMake = data().deepCopy(fields()[4].schema(), other.deviceMake);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.deviceModel)) {
        this.deviceModel = data().deepCopy(fields()[5].schema(), other.deviceModel);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.macAddress)) {
        this.macAddress = data().deepCopy(fields()[6].schema(), other.macAddress);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.ipAddress)) {
        this.ipAddress = data().deepCopy(fields()[7].schema(), other.ipAddress);
        fieldSetFlags()[7] = true;
      }
    }
    
    /** Creates a Builder by copying an existing Device instance */
    private Builder(com.oracle.datagrid.NoSQL.entity.Device other) {
            super(com.oracle.datagrid.NoSQL.entity.Device.SCHEMA$);
      if (isValidValue(fields()[0], other.deviceId)) {
        this.deviceId = data().deepCopy(fields()[0].schema(), other.deviceId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.deviceType)) {
        this.deviceType = data().deepCopy(fields()[1].schema(), other.deviceType);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.deviceSerialNumber)) {
        this.deviceSerialNumber = data().deepCopy(fields()[2].schema(), other.deviceSerialNumber);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.deviceStatus)) {
        this.deviceStatus = data().deepCopy(fields()[3].schema(), other.deviceStatus);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.deviceMake)) {
        this.deviceMake = data().deepCopy(fields()[4].schema(), other.deviceMake);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.deviceModel)) {
        this.deviceModel = data().deepCopy(fields()[5].schema(), other.deviceModel);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.macAddress)) {
        this.macAddress = data().deepCopy(fields()[6].schema(), other.macAddress);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.ipAddress)) {
        this.ipAddress = data().deepCopy(fields()[7].schema(), other.ipAddress);
        fieldSetFlags()[7] = true;
      }
    }

    /** Gets the value of the 'deviceId' field */
    public java.lang.Integer getDeviceId() {
      return deviceId;
    }
    
    /** Sets the value of the 'deviceId' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder setDeviceId(int value) {
      validate(fields()[0], value);
      this.deviceId = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'deviceId' field has been set */
    public boolean hasDeviceId() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'deviceId' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder clearDeviceId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'deviceType' field */
    public java.lang.String getDeviceType() {
      return deviceType;
    }
    
    /** Sets the value of the 'deviceType' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder setDeviceType(java.lang.String value) {
      validate(fields()[1], value);
      this.deviceType = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'deviceType' field has been set */
    public boolean hasDeviceType() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'deviceType' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder clearDeviceType() {
      deviceType = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'deviceSerialNumber' field */
    public java.lang.String getDeviceSerialNumber() {
      return deviceSerialNumber;
    }
    
    /** Sets the value of the 'deviceSerialNumber' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder setDeviceSerialNumber(java.lang.String value) {
      validate(fields()[2], value);
      this.deviceSerialNumber = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'deviceSerialNumber' field has been set */
    public boolean hasDeviceSerialNumber() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'deviceSerialNumber' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder clearDeviceSerialNumber() {
      deviceSerialNumber = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'deviceStatus' field */
    public java.lang.String getDeviceStatus() {
      return deviceStatus;
    }
    
    /** Sets the value of the 'deviceStatus' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder setDeviceStatus(java.lang.String value) {
      validate(fields()[3], value);
      this.deviceStatus = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'deviceStatus' field has been set */
    public boolean hasDeviceStatus() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'deviceStatus' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder clearDeviceStatus() {
      deviceStatus = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'deviceMake' field */
    public java.lang.String getDeviceMake() {
      return deviceMake;
    }
    
    /** Sets the value of the 'deviceMake' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder setDeviceMake(java.lang.String value) {
      validate(fields()[4], value);
      this.deviceMake = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'deviceMake' field has been set */
    public boolean hasDeviceMake() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'deviceMake' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder clearDeviceMake() {
      deviceMake = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /** Gets the value of the 'deviceModel' field */
    public java.lang.String getDeviceModel() {
      return deviceModel;
    }
    
    /** Sets the value of the 'deviceModel' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder setDeviceModel(java.lang.String value) {
      validate(fields()[5], value);
      this.deviceModel = value;
      fieldSetFlags()[5] = true;
      return this; 
    }
    
    /** Checks whether the 'deviceModel' field has been set */
    public boolean hasDeviceModel() {
      return fieldSetFlags()[5];
    }
    
    /** Clears the value of the 'deviceModel' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder clearDeviceModel() {
      deviceModel = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /** Gets the value of the 'macAddress' field */
    public java.lang.String getMacAddress() {
      return macAddress;
    }
    
    /** Sets the value of the 'macAddress' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder setMacAddress(java.lang.String value) {
      validate(fields()[6], value);
      this.macAddress = value;
      fieldSetFlags()[6] = true;
      return this; 
    }
    
    /** Checks whether the 'macAddress' field has been set */
    public boolean hasMacAddress() {
      return fieldSetFlags()[6];
    }
    
    /** Clears the value of the 'macAddress' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder clearMacAddress() {
      macAddress = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /** Gets the value of the 'ipAddress' field */
    public java.lang.String getIpAddress() {
      return ipAddress;
    }
    
    /** Sets the value of the 'ipAddress' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder setIpAddress(java.lang.String value) {
      validate(fields()[7], value);
      this.ipAddress = value;
      fieldSetFlags()[7] = true;
      return this; 
    }
    
    /** Checks whether the 'ipAddress' field has been set */
    public boolean hasIpAddress() {
      return fieldSetFlags()[7];
    }
    
    /** Clears the value of the 'ipAddress' field */
    public com.oracle.datagrid.NoSQL.entity.Device.Builder clearIpAddress() {
      ipAddress = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    @Override
    public Device build() {
      try {
        Device record = new Device();
        record.deviceId = fieldSetFlags()[0] ? this.deviceId : (java.lang.Integer) defaultValue(fields()[0]);
        record.deviceType = fieldSetFlags()[1] ? this.deviceType : (java.lang.String) defaultValue(fields()[1]);
        record.deviceSerialNumber = fieldSetFlags()[2] ? this.deviceSerialNumber : (java.lang.String) defaultValue(fields()[2]);
        record.deviceStatus = fieldSetFlags()[3] ? this.deviceStatus : (java.lang.String) defaultValue(fields()[3]);
        record.deviceMake = fieldSetFlags()[4] ? this.deviceMake : (java.lang.String) defaultValue(fields()[4]);
        record.deviceModel = fieldSetFlags()[5] ? this.deviceModel : (java.lang.String) defaultValue(fields()[5]);
        record.macAddress = fieldSetFlags()[6] ? this.macAddress : (java.lang.String) defaultValue(fields()[6]);
        record.ipAddress = fieldSetFlags()[7] ? this.ipAddress : (java.lang.String) defaultValue(fields()[7]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
