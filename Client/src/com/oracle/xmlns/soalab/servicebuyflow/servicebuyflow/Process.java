
package com.oracle.xmlns.soalab.servicebuyflow.servicebuyflow;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceId" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="customerAccountNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="creditAccountNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="holderFirstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="holderLastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="expireDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="provider" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
         "serviceId", "customerAccountNumber", "creditAccountNumber", "holderFirstName", "holderLastName", "expireDate",
         "provider"
    })
@XmlRootElement(name = "process")
public class Process {

    @XmlElement(required = true)
    protected Integer serviceId;
    @XmlElement(required = true)
    protected Integer customerAccountNumber;
    @XmlElement(required = true)
    protected Long creditAccountNumber;
    @XmlElement(required = true)
    protected String holderFirstName;
    @XmlElement(required = true)
    protected String holderLastName;
    @XmlElement(required = true)
    protected String expireDate;
    @XmlElement(required = true)
    protected String provider;

    /**
     * Gets the value of the serviceId property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public Integer getServiceId() {
        return serviceId;
    }

    /**
     * Sets the value of the serviceId property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setServiceId(Integer value) {
        this.serviceId = value;
    }

    /**
     * Gets the value of the customerAccountNumber property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public Integer getCustomerAccountNumber() {
        return customerAccountNumber;
    }

    /**
     * Sets the value of the customerAccountNumber property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setCustomerAccountNumber(Integer value) {
        this.customerAccountNumber = value;
    }

    /**
     * Gets the value of the creditAccountNumber property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public Long getCreditAccountNumber() {
        return creditAccountNumber;
    }

    /**
     * Sets the value of the creditAccountNumber property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setCreditAccountNumber(Long value) {
        this.creditAccountNumber = value;
    }

    /**
     * Gets the value of the holderFirstName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getHolderFirstName() {
        return holderFirstName;
    }

    /**
     * Sets the value of the holderFirstName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setHolderFirstName(String value) {
        this.holderFirstName = value;
    }

    /**
     * Gets the value of the holderLastName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getHolderLastName() {
        return holderLastName;
    }

    /**
     * Sets the value of the holderLastName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setHolderLastName(String value) {
        this.holderLastName = value;
    }

    /**
     * Gets the value of the expireDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getExpireDate() {
        return expireDate;
    }

    /**
     * Sets the value of the expireDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setExpireDate(String value) {
        this.expireDate = value;
    }

    /**
     * Gets the value of the provider property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Sets the value of the provider property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setProvider(String value) {
        this.provider = value;
    }

}
