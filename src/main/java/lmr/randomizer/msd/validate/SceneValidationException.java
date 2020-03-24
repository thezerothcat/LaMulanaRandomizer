package lmr.randomizer.msd.validate;

import lmr.randomizer.msd.object.Scene;
import lmr.randomizer.msd.object.Stage;

public abstract class SceneValidationException extends MsdValidationException {
    public final Scene scene;

    public SceneValidationException(String s, Stage stage, Scene scene) {
        super(s, stage);
        this.scene = scene;
    }
}
