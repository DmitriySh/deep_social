package ru.shishmakov;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {

    /**
     * Logger used by test.
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        logTestStart();
    }

    private void logTestStart() {
        logger.info("Running test \"{}\"", testName.getMethodName());
    }

}
