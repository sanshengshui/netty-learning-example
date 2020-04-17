package cn.mushuwei.decorator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author james mu
 * @date 2020/4/17 09:26
 */
public class ClubbedTrollTest {

    @Test
    public void testClubbedTroll() {
        final SimpleTroll simpleTroll = spy(new SimpleTroll());

        final ClubbedTroll clubbed = new ClubbedTroll(simpleTroll);
        assertEquals(20, clubbed.getAttackPower());
        verify(simpleTroll, times(1)).getAttackPower();

        clubbed.attack();
        verify(simpleTroll, times(1)).attack();

        clubbed.fleeBattle();
        verify(simpleTroll, times(1)).fleeBattle();
        verifyNoMoreInteractions(simpleTroll);
    }
}
