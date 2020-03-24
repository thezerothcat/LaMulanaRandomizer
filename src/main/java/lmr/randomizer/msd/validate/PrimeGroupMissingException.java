package lmr.randomizer.msd.validate;

import lmr.randomizer.msd.object.Scene;
import lmr.randomizer.msd.object.Stage;

public class PrimeGroupMissingException extends SceneValidationException {
    public PrimeGroupMissingException(Stage stage, Scene scene) {
        super("Scene has no prime LayerGroup.", stage, scene);
    }
}
