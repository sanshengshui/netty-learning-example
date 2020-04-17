package cn.mushuwei.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author james mu
 * @date 2020/4/16 19:53
 */
public class ClubbedTroll implements Troll {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClubbedTroll.class);

    private Troll decorated;

    public ClubbedTroll(Troll decorated) {
        this.decorated = decorated;
    }

    @Override
    public void attack() {
        decorated.attack();
        LOGGER.info("The troll swings at you with a club!");
    }

    @Override
    public int getAttackPower() {
        return decorated.getAttackPower() + 10;
    }

    @Override
    public void fleeBattle() {
        decorated.fleeBattle();
    }
}
