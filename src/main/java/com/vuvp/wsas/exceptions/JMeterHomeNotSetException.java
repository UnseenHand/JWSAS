package com.vuvp.wsas.exceptions;

public class JMeterHomeNotSetException extends JMeterConfigurationException {
    public JMeterHomeNotSetException() {
        super("The 'JMETER_HOME' environment variable is not set.");
    }

    public JMeterHomeNotSetException(String message) {
        super(message);
    }
}
