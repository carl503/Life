package ch.zhaw.pm2.life.model.lifeform;

import ch.zhaw.pm2.life.exception.LifeFormException;

/**
 * This functional interface should validate a life form before the action is performed.
 * @author meletlea
 */
@FunctionalInterface
public interface LifeFormActionCheck {

    /**
     * Make some checks with the life form.
     * @throws LifeFormException if the check didn't pass.
     */
    void check() throws LifeFormException;

}
