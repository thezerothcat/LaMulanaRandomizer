package lmr.randomizer.msd.io;

import lmr.randomizer.msd.object.*;
import lmr.randomizer.msd.validate.MsdValidation;
import lmr.randomizer.msd.validate.MsdValidationException;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class MsdSerialize {
    @FunctionalInterface
    private interface Serializer<T>  {
        void serialize(DataOutputStream stream, T data) throws IOException;
    }

    public static void serialize(Stage stage) throws IOException, MsdValidationException {
        serialize(stage, stage.filePath());
    }

    public static void serialize(Stage stage, Path filePath) throws IOException, MsdValidationException {
        MsdValidation.validate(stage);

        // TODO: if there's an IOException, make sure nothing actually gets persisted to disk.
        try (var stream = new DataOutputStream(new FileOutputStream(filePath.toString()))) {
            serializeStage(stream, stage);
        }
    }

    public static void serializeStage(DataOutputStream stream, Stage stage) throws IOException {
        serializeList(stream, stage.animatedTiles, MsdSerialize::serializeAnimatedTile);
        stream.writeShort(0); // end of animated tile section

        stream.writeByte(stage.gfxFileId);

        stream.writeShort(stage.scenes.size());
        serializeList(stream, stage.scenes, MsdSerialize::serializeScene);
    }

    public static void serializeAnimatedTile(DataOutputStream stream, AnimatedTile tile) throws IOException {
        var header = new BitProcessor(0);
        header.put(tile.frames.size(), MsdFormat.ANIM_TILE_FRAMES_COUNT_BIT_LENGTH);
        header.put(tile.animateInBoss ? 1 : 0, MsdFormat.ANIM_TILE_ANIMATE_IN_BOSS_BIT_LENGTH);

        stream.writeShort(header.value);
        serializeList(stream, tile.frames, MsdSerialize::serializeAnimatedTileFrame);
    }

    public static void serializeAnimatedTileFrame(DataOutputStream stream, AnimatedTileFrame frame) throws IOException {
        var bits = new BitProcessor(0);
        bits.put(frame.gfxIndex, MsdFormat.ANIM_TILE_FRAME_GFX_IDX_BIT_LENGTH);
        bits.put(frame.wait, MsdFormat.ANIM_TILE_FRAME_WAIT_BIT_LENGTH);

        stream.writeShort(bits.value);
    }

    public static void serializeScene(DataOutputStream stream, Scene scene) throws IOException {
        stream.writeByte(scene.useBossGfx ? 1 : 0);
        stream.writeByte(scene.layerGroups.size());
        stream.writeByte(scene.primeLayerGroupIdx);
        stream.writeShort(scene.collision.width());
        stream.writeShort(scene.collision.height());

        serializeCollision(stream, scene.collision);
        serializeList(stream, scene.layerGroups, MsdSerialize::serializeLayerGroup);
    }

    public static void serializeCollision(DataOutputStream stream, Collision collision) throws IOException {
        var data = collision.getData();

        for (int y = 0; y < collision.height(); y++) {
            for (int x = 0; x < collision.width(); x++) {
                stream.writeByte(collision.getTile(x, y));
            }
        }
    }

    public static void serializeLayerGroup(DataOutputStream stream, LayerGroup group) throws IOException {
        stream.writeShort(group.width());
        stream.writeShort(group.height());
        stream.writeByte(group.layers.size());

        serializeList(stream, group.layers, MsdSerialize::serializeLayer);
    }

    public static void serializeLayer(DataOutputStream stream, Layer layer) throws IOException {
        var data = layer.getData();

        for (int y = 0; y < layer.height(); y++) {
            for (int x = 0; x < layer.width(); x++) {
                serializeGfxTile(stream, layer.getTile(x, y));
            }
        }
    }

    public static void serializeGfxTile(DataOutputStream stream, GfxTile gfxTile) throws IOException {
        var flipHorizVal = gfxTile.flipHorizontal ? 1 : 0;
        var rotations = gfxTile.rotations % 4;
        var rotate90Val = rotations % 2 == 1 ? 1 : 0;
        var rotate180Val = rotations > 1 ? 1 : 0;

        var bits = new BitProcessor(0);
        bits.put(gfxTile.gfxIdx, MsdFormat.TILE_GFX_IDX_BIT_LENGTH);
        bits.put(gfxTile.blendMode.value, MsdFormat.TILE_BLEND_MODE_BIT_LENGTH);
        bits.put(flipHorizVal, MsdFormat.TILE_FLIP_HORIZONTAL_BIT_LENGTH);
        bits.put(rotate90Val, MsdFormat.TILE_ROTATE_90_BIT_LENGTH);
        bits.put(rotate180Val, MsdFormat.TILE_ROTATE_180_BIT_LENGTH);

        stream.writeShort(bits.value);
    }

    public static <T> void serializeList(DataOutputStream stream, List<T> list, Serializer<T> serializer) throws IOException {
        for (var t : list)
            serializer.serialize(stream, t);
    }
}
