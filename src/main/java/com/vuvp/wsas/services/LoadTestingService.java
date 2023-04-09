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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//This service should be the gate (entrance point) or the main
// service to the interaction between JMeter API and my project
@Service
public class LoadTestingService {

    private static final String JMETER_HOME_VAR = "JMETER_HOME";
    private static final String JMETER_PROPS_PATH_VAR = "JMETER_PROPS_PATH";
    private static final String XML_LOAD_TEST_RESULTS_DIRECTORY_VAR = "XML_LOAD_TEST_RESULTS_DIRECTORY";

    public String runLoadTest(
            String url,
            String requestMethod,
            int users,
            int timeToLoadAllUsers,
            int loopCount) throws JMeterConfigurationException, URISyntaxException {
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
        //JMeterUtils.initLocale();

        //Create JMeter Engine
        var jmeter = new StandardJMeterEngine();

        // Create Test Plan
        var testPlan = new TestPlan("Test Plan 1");

        // Create Thread Group
        var threadGroup = new ThreadGroup();

        // Set Thread Group properties
        threadGroup.setName("My Thread Group");
        threadGroup.setNumThreads(users);
        threadGroup.setRampUp(timeToLoadAllUsers);

        // Create a LoopController with the desired loop count
        var loopController = new LoopController();
        loopController.setLoops(loopCount);

        // Set the LoopController as the SamplerController for the ThreadGroup
        threadGroup.setSamplerController(loopController);

        // Create an URI variable to get all URL components
        URI uri = new URI(url);

        // Create HTTP Sampler
        var httpSampler = new HTTPSampler();
        httpSampler.setProtocol(uri.getScheme());
        httpSampler.setDomain(uri.getHost());
        httpSampler.setPort(uri.getPort());
        httpSampler.setPath(uri.getPath());
        httpSampler.setMethod(requestMethod);

        // Provide XML configurations
        SampleSaveConfiguration xmlConfig = new SampleSaveConfiguration();
        xmlConfig.setAsXml(true);

        // Set directory for XML load test result files

        // Create a unique filename using a timestamp suffix
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String filename = "result-" + timestamp + ".xml";

        // Create Result Collector
        var resultCollector = new ResultCollector(new Summariser());
        var fullDirectoryName = System.getenv(XML_LOAD_TEST_RESULTS_DIRECTORY_VAR) + File.separator + filename;
        resultCollector.setFilename(fullDirectoryName);
        resultCollector.setSaveConfig(xmlConfig);

//        // Add HTTP Sampler to Thread Group
//        threadGroup.addTestElement(httpSampler);
//
//        // Add Listeners to Thread Group
//        threadGroup.addTestElement(resultCollector);
//
//        // Add Thread Group to Test Plan
//        testPlan.addThreadGroup(threadGroup);

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

        // Return the results directory name
        return fullDirectoryName;
    }
}