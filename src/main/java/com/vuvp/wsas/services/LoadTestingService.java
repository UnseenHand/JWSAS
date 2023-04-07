package com.vuvp.wsas.services;


import com.vuvp.wsas.exceptions.JMeterConfigurationException;
import com.vuvp.wsas.exceptions.JMeterHomeNotSetException;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.springframework.stereotype.Service;

import java.util.Locale;

//This service should be the gate (entrance point) or the main
// service to the interaction between JMeter API and my project
@Service
public class LoadTestingService {

    private static final String JMETER_HOME_VAR = "JMETER_HOME";
    private static final String JMETER_PROPS_PATH_VAR = "JMETER_PROPS_PATH";

    public void runLoadTest() throws JMeterConfigurationException {
        //An important message, to access the environment variable you need to run your IDE with administrator rights
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

        // Set up JMeter environment
        JMeterUtils.setJMeterHome(jmeterHome);
        JMeterUtils.loadJMeterProperties(jmeterPropsPath);
        JMeterUtils.setLocale(Locale.ENGLISH);
        JMeterUtils.initLocale();

        //Create JMeter Engine
        var jmeter = new StandardJMeterEngine();

        // Create Test Plan
        var testPlan = new TestPlan("Test Plan 1");

        // Create Thread Group
        var threadGroup = new ThreadGroup();

        // Set Thread Group properties
        threadGroup.setName("My Thread Group");
        threadGroup.setNumThreads(10);
        threadGroup.setRampUp(5);

        // Create a LoopController with the desired loop count
        var loopController = new LoopController();
        loopController.setLoops(5);

        // Set the LoopController as the SamplerController for the ThreadGroup
        threadGroup.setSamplerController(loopController);

        // Create HTTP Sampler
        var httpSampler = new HTTPSampler();
        httpSampler.setProtocol("HTTPS");
        httpSampler.setDomain("localhost");
        httpSampler.setPort(7294);
        httpSampler.setPath("/Home");
        httpSampler.setMethod("GET");

        // Add HTTP Sampler to Thread Group
        threadGroup.addTestElement(httpSampler);

        // Add Thread Group to Test Plan
        testPlan.addThreadGroup(threadGroup);

        // Provide XML configurations
        SampleSaveConfiguration xmlConfig = new SampleSaveConfiguration();
        xmlConfig.setAsXml(true);

        // Create Result Collector
        var resultCollector = new ResultCollector(new Summariser());
        resultCollector.setFilename("results.xml");
        resultCollector.setSaveConfig(xmlConfig);

        // Adding the nodes to the tree
        var hashTree = new HashTree(testPlan);
        var threadsHashTree = hashTree.add(testPlan, threadGroup);
        threadsHashTree.add(threadGroup, httpSampler);
        threadsHashTree.add(threadGroup, resultCollector);

        // Run test plan
        jmeter.configure(hashTree);
        jmeter.run();

        // Clean up JMeter environment
        jmeter.exit();
    }
}