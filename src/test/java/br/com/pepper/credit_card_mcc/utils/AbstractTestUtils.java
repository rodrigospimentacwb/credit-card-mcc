package br.com.pepper.credit_card_mcc.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

@ExtendWith(MockitoExtension.class)
public class AbstractTestUtils extends TestSupport{

    public ListAppender<ILoggingEvent> listAppender;

    protected void setupLogger(Class<?> loggerClass) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(loggerClass);

        listAppender = new ListAppender<>();
        listAppender.setContext(loggerContext);
        listAppender.start();

        logger.addAppender(listAppender);
    }

    protected void clearLogger(Class<?> loggerClass) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(loggerClass);
        logger.detachAndStopAllAppenders();
    }

    protected void assertLogContains(String expectedMessage) {
        List<ILoggingEvent> logs = listAppender.list;
        assertTrue(logs.stream().anyMatch(e -> e.getFormattedMessage().contains(expectedMessage)),
                "Expected log message not found: " + expectedMessage);
    }
}
