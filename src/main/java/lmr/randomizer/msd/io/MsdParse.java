package lmr.randomizer.msd.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import lmr.randomizer.msd.object.*;
import lmr.randomizer.msd.object.tilemap.TileMap;

public abstract class MsdParse {
    @FunctionalInterface
    private interface SimpleParser<T> {
        T parse(ByteBuffer buffer);
    }

    public static Stage parse(Path filePath) throws IOException {
        return parse(filePath, true);
    }

    public static Stage parse(Path filePath, boolean autoDetectStageType) throws IOException {
        var fileBytes = Files.readAllBytes(filePath);
        var buffer = ByteBuffer.wrap(fileBytes).order(ByteOrder.BIG_ENDIAN);

        var stage = asStage(buffer);
        stage.setFilePath(filePath);
        if (autoDetectStageType) stage.type = detectStageTypeFromFilePath(filePath);

        return stage;
    }

    // extract, derive, build, construct, create, get, parse, interpret
    public static Stage asStage(ByteBuffer buffer) {
        var animatedTiles = asListDynamic(buffer, MsdParse::asAnimatedTile);
        var gfxFileId = getByte(buffer);
        var scenesCount = getShort(buffer);
        var scenes = asList(buffer, scenesCount, MsdParse::asScene);

        var stage = new Stage(animatedTiles, scenes);
        stage.gfxFileId = gfxFileId;

        return stage;
    }

    public static AnimatedTile asAnimatedTile(ByteBuffer buffer) {
        var header = getShort(buffer);

        if (header == 0) return null;

        var headerBits = new BitProcessor(header);
        var frameCount = headerBits.get(MsdFormat.ANIM_TILE_FRAMES_COUNT_BIT_LENGTH);
        var animateInBoss = headerBits.get(MsdFormat.ANIM_TILE_ANIMATE_IN_BOSS_BIT_LENGTH) == 1;

        var frames = asList(buffer, frameCount, MsdParse::asAnimatedTileFrame);

        return new AnimatedTile(frames, animateInBoss);
    }

    public static AnimatedTileFrame asAnimatedTileFrame(ByteBuffer buffer) {
        var bits = new BitProcessor(getShort(buffer));
        var gfxIndex = (short) bits.get(MsdFormat.ANIM_TILE_FRAME_GFX_IDX_BIT_LENGTH);
        var wait = (byte) bits.get(MsdFormat.ANIM_TILE_FRAME_WAIT_BIT_LENGTH);

        return new AnimatedTileFrame(gfxIndex, wait);
    }

    public static Scene asScene(ByteBuffer buffer) {
        boolean useBossGfx = getByte(buffer) == 1;
        var layerGroupsCount = getByte(buffer);
        var primeLayerGroupIdx = getByte(buffer);
        var collisionWidth = getShort(buffer);
        var collisionHeight = getShort(buffer);
        var collision = asCollisionMask(buffer, collisionWidth, collisionHeight);
        var layerGroups = asList(buffer, layerGroupsCount, MsdParse::asLayerGroup);

        var scene = new Scene(collision, layerGroups);
        scene.useBossGfx = useBossGfx;
        scene.primeLayerGroupIdx = primeLayerGroupIdx;

        return scene;
    }

    public static Collision asCollisionMask(ByteBuffer buffer, short width, short height) {
        var data = new ArrayList<List<Byte>>(width);

        for (int x = 0; x < width; x++)
            data.add(new ArrayList<>(height));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data.get(x).add(getByte(buffer));
            }
        }

        return new Collision(data);
    }

    public static LayerGroup asLayerGroup(ByteBuffer buffer) {
        var width = getShort(buffer);
        var height = getShort(buffer);
        var layersCount = getByte(buffer);
        var layers = asList(layersCount, () -> asLayer(buffer, width, height));

        var group = new LayerGroup(layers);
        group.fallbackWidth = width;
        group.fallbackHeight = height;

        return group;
    }

    public static Layer asLayer(ByteBuffer buffer, short width, short height) {
        var data = new ArrayList<List<GfxTile>>(width);

        for (int x = 0; x < width; x++)
            data.add(new ArrayList<>(height));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data.get(x).add(asGfxTile(buffer));
            }
        }

        return new Layer(data);
    }

    private static Map<Integer, GfxTileBlendMode> TILE_BLEND_MODES_BY_VALUE = Map.of(
        0, GfxTileBlendMode.EMPTY,
        1, GfxTileBlendMode.NORMAL,
        2, GfxTileBlendMode.ADD,
        3, GfxTileBlendMode.MULTIPLY
    );

    public static GfxTile asGfxTile(ByteBuffer buffer) {
        var bits = new BitProcessor(getShort(buffer));
        var gfxIdx = (short) bits.get(MsdFormat.TILE_GFX_IDX_BIT_LENGTH);
        var blendModeVal = bits.get(MsdFormat.TILE_BLEND_MODE_BIT_LENGTH);
        var blendMode = TILE_BLEND_MODES_BY_VALUE.get(blendModeVal);
        var flipHorizontal = bits.get(MsdFormat.TILE_FLIP_HORIZONTAL_BIT_LENGTH) == 1;

        var rotations = (byte) 0;
        rotations += bits.get(MsdFormat.TILE_ROTATE_90_BIT_LENGTH) > 0 ? 1 : 0;
        rotations += bits.get(MsdFormat.TILE_ROTATE_180_BIT_LENGTH) > 0 ? 2 : 0;

        return new GfxTile(gfxIdx, blendMode, flipHorizontal, rotations);
    }

    // If you need to parse a section of your buffer as an array of known size, use this.
    public static <T> List<T> asList(ByteBuffer buffer, int count, SimpleParser<T> parser) {
        Supplier<T> cb = () -> parser.parse(buffer);
        return asList(count, cb);
    }

    public static <T> List<T> asList(int count, Supplier<T> supplier) {
        var list = new ArrayList<T>(count);

        for (int i = 0; i < count; i++)
            list.add(supplier.get());

        return list;
    }

    public static <T> List<T> asList(int count, Function<Integer, T> supplier) {
        var list = new ArrayList<T>(count);

        for (int i = 0; i < count; i++)
            list.add(supplier.apply(i));

        return list;
    }

    public static <T> List<T> asListDynamic(ByteBuffer buffer, SimpleParser<T> parser) {
        Supplier<T> cb = () -> parser.parse(buffer);
        return asListDynamic(cb);
    }

    // Similar to asList(), but appends to the list until callback returns null.
    public static <T> List<T> asListDynamic(Supplier<T> supplier) {
        var list = new ArrayList<T>();

        while (true) {
            var element = supplier.get();

            if (element == null) break;
            else list.add(element);
        }

        return list;
    }

    private static byte getByte(ByteBuffer buffer) {
        return buffer.get();
    }

    private static short getShort(ByteBuffer buffer) {
        return buffer.getShort();
    }

    private static final Map<String, StageType> STAGE_TYPES_BY_IDENTIFIER = Map.of(
        "map", StageType.ZONE,
        "shop", StageType.SHOP,
        "boss", StageType.BOSS,
        "ending", StageType.ENDING,
        "opening", StageType.OPENING,
        "title", StageType.TITLE
    );

    public static StageType detectStageTypeFromFilePath(Path filePath) {
        var fileName = filePath.getFileName().toString();

        return STAGE_TYPES_BY_IDENTIFIER.entrySet().stream()
            .filter(e -> fileName.contains(e.getKey()))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(StageType.UNKNOWN);
    }
}
