package lmr.randomizer.msd.validate;

import lmr.randomizer.msd.object.Stage;

public abstract class MsdValidation {
    public static void validate(Stage stage) throws MsdValidationException {
        for (var scene : stage.scenes) {
            var primeGroup = scene.primeGroup();
            if (primeGroup == null)
                throw new PrimeGroupMissingException(stage, scene);

            var coll = scene.collision;

            if (coll.width() != 2 * primeGroup.width() || coll.height() != 2 * primeGroup.height())
                throw new CollisionDimensionsMismatchException(stage, scene);

            for (var group : scene.layerGroups) {
                if (group.layers.isEmpty()) continue;

                var groupWidth = group.layers.get(0).width();
                var groupHeight = group.layers.get(0).height();

                // Skip first layer
                for (int i = 1; i < group.layers.size(); i++) {
                    var layer = group.layers.get(i);
                    if (layer.width() != groupWidth || layer.height() != groupHeight)
                        throw new LayerDimensionsMismatchException(stage, scene, group);
                }
            }
        }
    }
}
