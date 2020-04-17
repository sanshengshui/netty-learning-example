package cn.mushuwei.decorator;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;


import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author james mu
 * @date 2020/4/17 09:13
 */
public class SimpleTrollTest {

    private InMemoryAppender appender;

    @BeforeEach
    public void setUp() {
        appender = new InMemoryAppender(SimpleTroll.class);
    }

    @AfterEach
    public void tearDown() {
        appender.stop();
    }

    @Test
    public void testTrollActions() {
        final Troll troll = new SimpleTroll();
        assertEquals(10, troll.getAttackPower());

        troll.attack();
        assertEquals("The troll tries to grab you!", appender.getLastMessage());

        troll.fleeBattle();
        assertEquals("The troll shrieks in horror and runs away!", appender.getLastMessage());

        assertEquals(2, appender.getLogSize());
    }

    private class InMemoryAppender extends AppenderBase<ILoggingEvent> {

        private List<ILoggingEvent> log = new LinkedList<>();

        public InMemoryAppender(Class clazz) {
            ((Logger) LoggerFactory.getLogger(clazz)).addAppender(this);
            start();
        }

        @Override
        protected void append(ILoggingEvent iLoggingEvent) {
            log.add(iLoggingEvent);
        }

        public String getLastMessage() {
            return log.get(log.size() - 1).getMessage();
        }

        public int getLogSize() {
            return log.size();
        }
    }
}
