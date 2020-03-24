package lmr.randomizer.msd.validate;

import lmr.randomizer.msd.object.Stage;

public abstract class MsdValidationException extends Exception {
    public final Stage stage;

    public MsdValidationException(String s, Stage stage) {
        super(s);
        this.stage = stage;
    }
}
