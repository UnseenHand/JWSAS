package com.vuvp.wsas.exceptions;

import javax.naming.ConfigurationException;

public class JMeterConfigurationException extends ConfigurationException {
    public JMeterConfigurationException() {
        super("The configurations for a JMeter API are not set correctly or have been unspecified.");
    }

    public JMeterConfigurationException(String message) {
        super(message);
    }
}