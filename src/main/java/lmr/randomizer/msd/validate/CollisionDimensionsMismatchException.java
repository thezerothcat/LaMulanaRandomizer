package lmr.randomizer.msd.validate;

import lmr.randomizer.msd.object.Scene;
import lmr.randomizer.msd.object.Stage;

public class CollisionDimensionsMismatchException extends SceneValidationException {
    public CollisionDimensionsMismatchException(Stage stage, Scene scene) {
        super("Collision dimensions must be twice the dimensions of the prime LayerGroup.", stage, scene);
    }
}
