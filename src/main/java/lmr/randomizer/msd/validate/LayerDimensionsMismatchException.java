package lmr.randomizer.msd.validate;

import lmr.randomizer.msd.object.LayerGroup;
import lmr.randomizer.msd.object.Scene;
import lmr.randomizer.msd.object.Stage;

public class LayerDimensionsMismatchException extends LayerGroupException {
    public LayerDimensionsMismatchException(Stage stage, Scene scene, LayerGroup layerGroup) {
        super("Layers in LayerGroup have inconsistent dimensions.", stage, scene, layerGroup);
    }
}
