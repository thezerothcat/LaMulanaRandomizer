package lmr.randomizer.msd.validate;

import lmr.randomizer.msd.object.LayerGroup;
import lmr.randomizer.msd.object.Scene;
import lmr.randomizer.msd.object.Stage;

public abstract class LayerGroupException extends SceneValidationException {
    public final LayerGroup layerGroup;

    public LayerGroupException(String s, Stage stage, Scene scene, LayerGroup layerGroup) {
        super(s, stage, scene);
        this.layerGroup = layerGroup;
    }
}
