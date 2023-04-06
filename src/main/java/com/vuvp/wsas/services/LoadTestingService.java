package com.vuvp.wsas.services;


import com.vuvp.wsas.exceptions.JMeterConfigurationException;
import com.vuvp.wsas.exceptions.JMeterHomeNotSetException;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

//This service should be the gate (entrance point) or the main
// service to the interaction between JMeter API and my project
public class LoadTestingService {

    private static final String JMETER_HOME_VAR = "JMETER_HOME";
    private static final String JMETER_PROPS_PATH_VAR = "JMETER_PROPS_PATH";

    public void runLoadTest() throws JMeterConfigurationException {
        // Try to access the JMETER_HOME environment variable
        var jmeterHome = System.getenv(JMETER_HOME_VAR);
        if (jmeterHome == null) {
            throw new JMeterHomeNotSetException(
                    """
                        If you see this error message, then it seems that the environment variable
                        'JMETER_HOME' is not set inside your system, or something prevents the program
                        from accessing it. All the instructions on how to do it are placed inside the
                        'README.md' file of the project.
                    """
            );
        }

        // Try to access the JMETER_PROPS_PATH environment variable
        var jmeterPropsPath = System.getenv(JMETER_PROPS_PATH_VAR);
        if (jmeterPropsPath == null) {
            jmeterPropsPath = jmeterHome + "/bin/jmeter.properties";
        }

        // Initialize JMeter engine and JMeterUtils
        var jmeter = new StandardJMeterEngine();
        JMeterUtils.setJMeterHome(jmeterHome);
        JMeterUtils.loadJMeterProperties(jmeterPropsPath);

        // Create a test plan
        var testPlan = new TestPlan();
        testPlan.setName("My Test Plan");

        // Add samplers, controllers, listeners, etc. to the test plan

        // Set the test plan for the JMeter engine
        var testPlanTree = new HashTree(testPlan);
        jmeter.configure(testPlanTree);

        // Run the test plan
        jmeter.run();
    }
}
