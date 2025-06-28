package WayofTime.alchemicalWizardry.common;

import java.util.Arrays;
import java.util.BitSet;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.ColourAndCoords;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import WayofTime.alchemicalWizardry.common.tileEntity.TEOrientable;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPedestal;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPlinth;
import WayofTime.alchemicalWizardry.common.tileEntity.TEReagentConduit;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public enum NewPacketHandler {

    INSTANCE;

    private final EnumMap<Side, FMLEmbeddedChannel> channels;

    NewPacketHandler() {
        this.channels = NetworkRegistry.INSTANCE.newChannel("BloodMagic", new TEAltarCodec());
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            addClientHandler();
        }
        if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
            System.out.println("Server sided~");
            addServerHandler();
        }
    }

    @SideOnly(Side.CLIENT)
    private void addClientHandler() {
        FMLEmbeddedChannel clientChannel = this.channels.get(Side.CLIENT);

        String tileAltarCodec = clientChannel.findChannelHandlerNameForType(TEAltarCodec.class);
        clientChannel.pipeline().addAfter(tileAltarCodec, "TEAltarHandler", new TEAltarMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TEOrientableHandler", new TEOrientableMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TEPedestalHandler", new TEPedestalMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TEPlinthHandler", new TEPlinthMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TESocketHandler", new TESocketMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TETeleposerHandler", new TETeleposerMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TEWritingTableHandler", new TEWritingTableMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "ParticleHandler", new ParticleMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "VelocityHandler", new VelocityMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TEMasterStoneHandler", new TEMasterStoneMessageHandler());
        clientChannel.pipeline()
                .addAfter(tileAltarCodec, "TEReagentConduitHandler", new TEReagentConduitMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "CurrentLPMessageHandler", new CurrentLPMessageHandler());
        clientChannel.pipeline()
                .addAfter(tileAltarCodec, "CurrentReagentBarMessageHandler", new CurrentReagentBarMessageHandler());
        clientChannel.pipeline()
                .addAfter(tileAltarCodec, "CurrentAddedHPMessageHandler", new CurrentAddedHPMessageHandler());
        clientChannel.pipeline()
                .addAfter(tileAltarCodec, "GaiaBiomeChangeHandler", new GaiaBiomeChangeMessageHandler());
    }

    @SideOnly(Side.SERVER)
    private void addServerHandler() {
        FMLEmbeddedChannel serverChannel = this.channels.get(Side.SERVER);

        String messageCodec = serverChannel.findChannelHandlerNameForType(TEAltarCodec.class);
        serverChannel.pipeline().addAfter(messageCodec, "KeyboardMessageHandler", new KeyboardMessageHandler());
    }

    private static class TEAltarMessageHandler extends SimpleChannelInboundHandler<TEAltarMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEAltarMessage msg) {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
            if (te instanceof TEAltar altar) {
                altar.handlePacketData(msg.items, msg.fluids, msg.capacity);
            }
        }
    }

    private static class TEOrientableMessageHandler extends SimpleChannelInboundHandler<TEOrientableMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEOrientableMessage msg) {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
            if (te instanceof TEOrientable teOrientable) {
                teOrientable.setInputDirection(ForgeDirection.getOrientation(msg.input));
                teOrientable.setOutputDirection(ForgeDirection.getOrientation(msg.output));
            }
        }
    }

    private static class TEPedestalMessageHandler extends SimpleChannelInboundHandler<TEPedestalMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEPedestalMessage msg) {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
            if (te instanceof TEPedestal pedestal) {
                pedestal.handlePacketData(msg.items);
            }
        }
    }

    private static class TEPlinthMessageHandler extends SimpleChannelInboundHandler<TEPlinthMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEPlinthMessage msg) {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
            if (te instanceof TEPlinth Plinth) {
                Plinth.handlePacketData(msg.items);
            }
        }
    }

    private static class TESocketMessageHandler extends SimpleChannelInboundHandler<TESocketMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TESocketMessage msg) {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
            if (te instanceof TESocket Socket) {
                Socket.handlePacketData(msg.items);
            }
        }
    }

    private static class TETeleposerMessageHandler extends SimpleChannelInboundHandler<TETeleposerMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TETeleposerMessage msg) {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
            if (te instanceof TETeleposer Teleposer) {
                Teleposer.handlePacketData(msg.items);
            }
        }
    }

    private static class TEWritingTableMessageHandler extends SimpleChannelInboundHandler<TEWritingTableMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEWritingTableMessage msg) {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
            if (te instanceof TEWritingTable WritingTable) {
                WritingTable.handlePacketData(msg.items);
            }
        }
    }

    private static class ParticleMessageHandler extends SimpleChannelInboundHandler<ParticleMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ParticleMessage msg) {
            World world = AlchemicalWizardry.proxy.getClientWorld();

            world.spawnParticle(msg.particle, msg.xCoord, msg.yCoord, msg.zCoord, msg.xVel, msg.yVel, msg.zVel);
        }
    }

    private static class VelocityMessageHandler extends SimpleChannelInboundHandler<VelocityMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, VelocityMessage msg) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;

            if (player != null) {
                player.motionX = msg.xVel;
                player.motionY = msg.yVel;
                player.motionZ = msg.zVel;
            }
        }
    }

    private static class TEMasterStoneMessageHandler extends SimpleChannelInboundHandler<TEMasterStoneMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEMasterStoneMessage msg) {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
            if (te instanceof TEMasterStone masterStone) {
                masterStone.setCurrentRitual(msg.ritual);
                masterStone.isRunning = msg.isRunning;
            }
        }
    }

    private static class TEReagentConduitMessageHandler extends SimpleChannelInboundHandler<TEReagentConduitMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEReagentConduitMessage msg) {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
            if (te instanceof TEReagentConduit reagentConduit) {
                reagentConduit.destinationList = msg.destinationList;
            }
        }
    }

    private static class CurrentLPMessageHandler extends SimpleChannelInboundHandler<CurrentLPMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, CurrentLPMessage msg) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;

            APISpellHelper.setPlayerLPTag(player, msg.currentLP);
            APISpellHelper.setPlayerMaxLPTag(player, msg.maxLP);
        }
    }

    private static class CurrentReagentBarMessageHandler extends SimpleChannelInboundHandler<CurrentReagentBarMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, CurrentReagentBarMessage msg) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;

            APISpellHelper.setPlayerReagentType(player, msg.reagent);
            APISpellHelper.setPlayerCurrentReagentAmount(player, msg.currentAR);
            APISpellHelper.setPlayerMaxReagentAmount(player, msg.maxAR);
        }
    }

    private static class CurrentAddedHPMessageHandler extends SimpleChannelInboundHandler<CurrentAddedHPMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, CurrentAddedHPMessage msg) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;

            APISpellHelper.setCurrentAdditionalHP(player, msg.currentHP);
            APISpellHelper.setCurrentAdditionalMaxHP(player, msg.maxHP);
        }
    }

    private static class KeyboardMessageHandler extends SimpleChannelInboundHandler<KeyboardMessage> {

        public KeyboardMessageHandler() {
            System.out.println("I am being created");
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, KeyboardMessage msg) {
            System.out.println("Hmmm");
        }
    }

    private static class GaiaBiomeChangeMessageHandler extends SimpleChannelInboundHandler<GaiaBiomeChangeMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, GaiaBiomeChangeMessage msg) {
            Chunk chunk = AlchemicalWizardry.proxy.getClientWorld().getChunkFromChunkCoords(msg.chunkX, msg.chunkZ);
            if (chunk != null) {
                byte[] biomeArray = chunk.getBiomeArray();
                for (int i = 0; i < 16 * 16; ++i) {
                    if (msg.mask.get(i)) {
                        biomeArray[i] = msg.biome;
                    }
                }
                chunk.setBiomeArray(biomeArray);
            }
        }
    }

    public static class BMMessage {

        int index;
    }

    public static class TEAltarMessage extends BMMessage {

        int x;
        int y;
        int z;

        int[] items;
        int[] fluids;
        int capacity;
    }

    public static class TEOrientableMessage extends BMMessage {

        int x;
        int y;
        int z;

        int input;
        int output;
    }

    public static class TEPedestalMessage extends BMMessage {

        int x;
        int y;
        int z;

        int[] items;
    }

    public static class TEPlinthMessage extends BMMessage {

        int x;
        int y;
        int z;

        int[] items;
    }

    public static class TESocketMessage extends BMMessage {

        int x;
        int y;
        int z;

        int[] items;
    }

    public static class TETeleposerMessage extends BMMessage {

        int x;
        int y;
        int z;

        int[] items;
    }

    public static class TEWritingTableMessage extends BMMessage {

        int x;
        int y;
        int z;

        int[] items;
    }

    public static class ParticleMessage extends BMMessage {

        String particle;

        double xCoord;
        double yCoord;
        double zCoord;

        double xVel;
        double yVel;
        double zVel;
    }

    public static class VelocityMessage extends BMMessage {

        double xVel;
        double yVel;
        double zVel;
    }

    public static class TEMasterStoneMessage extends BMMessage {

        int x;
        int y;
        int z;

        String ritual;
        boolean isRunning;
    }

    public static class TEReagentConduitMessage extends BMMessage {

        int x;
        int y;
        int z;

        List<ColourAndCoords> destinationList;
    }

    public static class CurrentLPMessage extends BMMessage {

        int currentLP;
        int maxLP;
    }

    public static class CurrentReagentBarMessage extends BMMessage {

        String reagent;
        float currentAR;
        float maxAR;
    }

    public static class CurrentAddedHPMessage extends BMMessage {

        float currentHP;
        float maxHP;
    }

    public static class KeyboardMessage extends BMMessage {

        byte keyPressed;
    }

    public static class GaiaBiomeChangeMessage extends BMMessage {

        int chunkX;
        int chunkZ;
        byte biome;
        BitSet mask;
        // One bit per coordinate in a chunk, 16*16 bits = 32 bytes
        public static final int maskByteCount = 32;
    }

    private static class TEAltarCodec extends FMLIndexedMessageToMessageCodec<BMMessage> {

        public TEAltarCodec() {
            addDiscriminator(0, TEAltarMessage.class);
            addDiscriminator(1, TEOrientableMessage.class);
            addDiscriminator(2, TEPedestalMessage.class);
            addDiscriminator(3, TEPlinthMessage.class);
            addDiscriminator(4, TESocketMessage.class);
            addDiscriminator(5, TETeleposerMessage.class);
            addDiscriminator(6, TEWritingTableMessage.class);
            addDiscriminator(7, ParticleMessage.class);
            addDiscriminator(8, VelocityMessage.class);
            addDiscriminator(9, TEMasterStoneMessage.class);
            addDiscriminator(10, TEReagentConduitMessage.class);
            addDiscriminator(11, CurrentLPMessage.class);
            addDiscriminator(12, CurrentReagentBarMessage.class);
            addDiscriminator(13, CurrentAddedHPMessage.class);
            addDiscriminator(14, KeyboardMessage.class);
            addDiscriminator(15, GaiaBiomeChangeMessage.class);
        }

        @Override
        public void encodeInto(ChannelHandlerContext ctx, BMMessage msg, ByteBuf target) {
            target.writeInt(msg.index);

            switch (msg.index) {
                case 0 -> {
                    TEAltarMessage m = (TEAltarMessage) msg;
                    target.writeInt(m.x);
                    target.writeInt(m.y);
                    target.writeInt(m.z);

                    target.writeBoolean(m.items != null);
                    if (m.items != null) {
                        int[] items = m.items;
                        Arrays.stream(items).forEach(target::writeInt);
                    }

                    target.writeBoolean(m.fluids != null);
                    if (m.fluids != null) {
                        int[] fluids = m.fluids;
                        Arrays.stream(fluids).forEach(target::writeInt);
                    }

                    target.writeInt(m.capacity);
                }
                case 1 -> {
                    TEOrientableMessage m = (TEOrientableMessage) msg;
                    target.writeInt(m.x);
                    target.writeInt(m.y);
                    target.writeInt(m.z);

                    target.writeInt(m.input);
                    target.writeInt(m.output);
                }
                case 2 -> {
                    TEPedestalMessage m = (TEPedestalMessage) msg;
                    target.writeInt(m.x);
                    target.writeInt(m.y);
                    target.writeInt(m.z);

                    target.writeBoolean(m.items != null);
                    if (m.items != null) {
                        int[] items = m.items;
                        Arrays.stream(items).forEach(target::writeInt);
                    }
                }
                case 3 -> {
                    TEPlinthMessage m = (TEPlinthMessage) msg;
                    target.writeInt(m.x);
                    target.writeInt(m.y);
                    target.writeInt(m.z);

                    target.writeBoolean(m.items != null);
                    if (m.items != null) {
                        int[] items = m.items;
                        Arrays.stream(items).forEach(target::writeInt);
                    }
                }
                case 4 -> {
                    TESocketMessage m = (TESocketMessage) msg;
                    target.writeInt(m.x);
                    target.writeInt(m.y);
                    target.writeInt(m.z);

                    target.writeBoolean(m.items != null);
                    if (m.items != null) {
                        int[] items = m.items;
                        Arrays.stream(items).forEach(target::writeInt);
                    }
                }
                case 5 -> {
                    TETeleposerMessage m = (TETeleposerMessage) msg;
                    target.writeInt(m.x);
                    target.writeInt(m.y);
                    target.writeInt(m.z);

                    target.writeBoolean(m.items != null);
                    if (m.items != null) {
                        int[] items = m.items;
                        Arrays.stream(items).forEach(target::writeInt);
                    }
                }
                case 6 -> {
                    TEWritingTableMessage m = (TEWritingTableMessage) msg;
                    target.writeInt(m.x);
                    target.writeInt(m.y);
                    target.writeInt(m.z);

                    target.writeBoolean(m.items != null);
                    if (m.items != null) {
                        int[] items = m.items;
                        Arrays.stream(items).forEach(target::writeInt);
                    }
                }
                case 7 -> {
                    ParticleMessage m = (ParticleMessage) msg;
                    String str = m.particle;
                    target.writeInt(str.length());
                    IntStream.range(0, str.length()).map(str::charAt).forEach(target::writeChar);

                    target.writeDouble(m.xCoord);
                    target.writeDouble(m.yCoord);
                    target.writeDouble(m.zCoord);

                    target.writeDouble(m.xVel);
                    target.writeDouble(m.yVel);
                    target.writeDouble(m.zVel);
                }
                case 8 -> {
                    VelocityMessage m = (VelocityMessage) msg;
                    target.writeDouble(m.xVel);
                    target.writeDouble(m.yVel);
                    target.writeDouble(m.zVel);
                }
                case 9 -> {
                    TEMasterStoneMessage m = (TEMasterStoneMessage) msg;
                    target.writeInt(m.x);
                    target.writeInt(m.y);
                    target.writeInt(m.z);

                    String ritual = m.ritual;
                    target.writeInt(ritual.length());
                    IntStream.range(0, ritual.length()).map(ritual::charAt).forEach(target::writeChar);

                    target.writeBoolean(m.isRunning);
                }
                case 10 -> {
                    TEReagentConduitMessage m = (TEReagentConduitMessage) msg;
                    target.writeInt(m.x);
                    target.writeInt(m.y);
                    target.writeInt(m.z);

                    List<ColourAndCoords> list = m.destinationList;
                    target.writeInt(list.size());

                    list.forEach(colourSet -> {
                        target.writeInt(colourSet.colourRed);
                        target.writeInt(colourSet.colourGreen);
                        target.writeInt(colourSet.colourBlue);
                        target.writeInt(colourSet.colourIntensity);
                        target.writeInt(colourSet.xCoord);
                        target.writeInt(colourSet.yCoord);
                        target.writeInt(colourSet.zCoord);
                    });
                }
                case 11 -> {
                    CurrentLPMessage m = (CurrentLPMessage) msg;
                    target.writeInt(m.currentLP);
                    target.writeInt(m.maxLP);
                }
                case 12 -> {
                    CurrentReagentBarMessage m = (CurrentReagentBarMessage) msg;
                    char[] charSet = m.reagent.toCharArray();
                    target.writeInt(charSet.length);
                    for (char cha : charSet) {
                        target.writeChar(cha);
                    }
                    target.writeFloat(m.currentAR);
                    target.writeFloat(m.maxAR);
                }
                case 13 -> {
                    CurrentAddedHPMessage m = (CurrentAddedHPMessage) msg;
                    target.writeFloat(m.currentHP);
                    target.writeFloat(m.maxHP);
                }
                case 14 -> target.writeByte(((KeyboardMessage) msg).keyPressed);
                case 15 -> {
                    GaiaBiomeChangeMessage m = (GaiaBiomeChangeMessage) msg;
                    target.writeInt(m.chunkX);
                    target.writeInt(m.chunkZ);
                    target.writeByte(m.biome);
                    byte[] arr = Arrays.copyOf(m.mask.toByteArray(), GaiaBiomeChangeMessage.maskByteCount);
                    target.writeBytes(arr);
                }
            }
        }

        @Override
        public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, BMMessage msg) {
            int index = dat.readInt();

            switch (index) {
                case 0 -> {
                    TEAltarMessage m = (TEAltarMessage) msg;
                    m.x = dat.readInt();
                    m.y = dat.readInt();
                    m.z = dat.readInt();
                    boolean hasStacks = dat.readBoolean();

                    m.items = new int[TEAltar.sizeInv * 3];
                    if (hasStacks) {
                        m.items = new int[TEAltar.sizeInv * 3];
                        IntStream.range(0, m.items.length).forEach(i -> m.items[i] = dat.readInt());
                    }

                    boolean hasFluids = dat.readBoolean();
                    m.fluids = new int[6];
                    if (hasFluids) IntStream.range(0, m.fluids.length).forEach(i -> m.fluids[i] = dat.readInt());

                    m.capacity = dat.readInt();
                }
                case 1 -> {
                    TEOrientableMessage m = (TEOrientableMessage) msg;
                    m.x = dat.readInt();
                    m.y = dat.readInt();
                    m.z = dat.readInt();

                    m.input = dat.readInt();
                    m.output = dat.readInt();
                }
                case 2 -> {
                    TEPedestalMessage m = (TEPedestalMessage) msg;
                    m.x = dat.readInt();
                    m.y = dat.readInt();
                    m.z = dat.readInt();

                    boolean hasStacks = dat.readBoolean();

                    m.items = new int[TEPedestal.sizeInv * 3];
                    if (hasStacks) {
                        m.items = new int[TEPedestal.sizeInv * 3];
                        IntStream.range(0, m.items.length).forEach(i -> m.items[i] = dat.readInt());
                    }
                }
                case 3 -> {
                    TEPlinthMessage m = (TEPlinthMessage) msg;
                    m.x = dat.readInt();
                    m.y = dat.readInt();
                    m.z = dat.readInt();

                    boolean hasStacks = dat.readBoolean();

                    m.items = new int[TEPlinth.sizeInv * 3];
                    if (hasStacks) {
                        m.items = new int[TEPlinth.sizeInv * 3];
                        IntStream.range(0, m.items.length).forEach(i -> m.items[i] = dat.readInt());
                    }
                }
                case 4 -> {
                    TESocketMessage m = (TESocketMessage) msg;
                    m.x = dat.readInt();
                    m.y = dat.readInt();
                    m.z = dat.readInt();

                    boolean hasStacks = dat.readBoolean();

                    m.items = new int[TESocket.sizeInv * 3];
                    if (hasStacks) {
                        m.items = new int[TESocket.sizeInv * 3];
                        IntStream.range(0, m.items.length).forEach(i -> m.items[i] = dat.readInt());
                    }
                }
                case 5 -> {
                    TETeleposerMessage m = (TETeleposerMessage) msg;
                    m.x = dat.readInt();
                    m.y = dat.readInt();
                    m.z = dat.readInt();

                    boolean hasStacks = dat.readBoolean();

                    m.items = new int[TETeleposer.sizeInv * 3];
                    if (hasStacks) {
                        m.items = new int[TETeleposer.sizeInv * 3];
                        IntStream.range(0, m.items.length).forEach(i -> m.items[i] = dat.readInt());
                    }
                }
                case 6 -> {
                    TEWritingTableMessage m = (TEWritingTableMessage) msg;
                    m.x = dat.readInt();
                    m.y = dat.readInt();
                    m.z = dat.readInt();

                    boolean hasStacks = dat.readBoolean();

                    m.items = new int[TEWritingTable.sizeInv * 3];
                    if (hasStacks) {
                        m.items = new int[TEWritingTable.sizeInv * 3];
                        IntStream.range(0, m.items.length).forEach(i -> m.items[i] = dat.readInt());
                    }
                }
                case 7 -> {
                    String str = IntStream.range(0, dat.readInt()).mapToObj(i -> String.valueOf(dat.readChar()))
                            .collect(Collectors.joining());

                    ParticleMessage m = (ParticleMessage) msg;
                    m.particle = str;

                    m.xCoord = dat.readDouble();
                    m.yCoord = dat.readDouble();
                    m.zCoord = dat.readDouble();

                    m.xVel = dat.readDouble();
                    m.yVel = dat.readDouble();
                    m.zVel = dat.readDouble();
                }
                case 8 -> {
                    VelocityMessage m = (VelocityMessage) msg;
                    m.xVel = dat.readDouble();
                    m.yVel = dat.readDouble();
                    m.zVel = dat.readDouble();
                }
                case 9 -> {
                    TEMasterStoneMessage m = (TEMasterStoneMessage) msg;
                    m.x = dat.readInt();
                    m.y = dat.readInt();
                    m.z = dat.readInt();

                    m.ritual = IntStream.range(0, dat.readInt()).mapToObj(i -> String.valueOf(dat.readChar()))
                            .collect(Collectors.joining());
                    m.isRunning = dat.readBoolean();
                }
                case 10 -> {
                    TEReagentConduitMessage m = (TEReagentConduitMessage) msg;
                    m.x = dat.readInt();
                    m.y = dat.readInt();
                    m.z = dat.readInt();

                    int listSize = dat.readInt();

                    m.destinationList = IntStream.range(0, listSize)
                            .mapToObj(
                                    i -> new ColourAndCoords(
                                            dat.readInt(),
                                            dat.readInt(),
                                            dat.readInt(),
                                            dat.readInt(),
                                            dat.readInt(),
                                            dat.readInt(),
                                            dat.readInt()))
                            .collect(Collectors.toCollection(LinkedList::new));
                }
                case 11 -> {
                    CurrentLPMessage m = (CurrentLPMessage) msg;
                    m.currentLP = dat.readInt();
                    m.maxLP = dat.readInt();
                }
                case 12 -> {
                    CurrentReagentBarMessage m = (CurrentReagentBarMessage) msg;
                    m.reagent = IntStream.range(0, dat.readInt()).mapToObj(i -> String.valueOf(dat.readChar()))
                            .collect(Collectors.joining());
                    m.currentAR = dat.readFloat();
                    m.maxAR = dat.readFloat();
                }
                case 13 -> {
                    CurrentAddedHPMessage m = (CurrentAddedHPMessage) msg;
                    m.currentHP = dat.readFloat();
                    m.maxHP = dat.readFloat();
                }
                case 14 -> ((KeyboardMessage) msg).keyPressed = dat.readByte();
                case 15 -> {
                    GaiaBiomeChangeMessage m = (GaiaBiomeChangeMessage) msg;
                    m.chunkX = dat.readInt();
                    m.chunkZ = dat.readInt();
                    m.biome = dat.readByte();

                    byte[] buffer = new byte[GaiaBiomeChangeMessage.maskByteCount];
                    dat.readBytes(buffer);
                    m.mask = BitSet.valueOf(buffer);
                }
            }
        }
    }

    // Packets to be obtained
    public static Packet getPacket(TEAltar tileAltar) {
        TEAltarMessage msg = new TEAltarMessage();
        msg.index = 0;
        msg.x = tileAltar.xCoord;
        msg.y = tileAltar.yCoord;
        msg.z = tileAltar.zCoord;
        msg.items = tileAltar.buildIntDataList();
        msg.fluids = tileAltar.buildFluidList();
        msg.capacity = tileAltar.getCapacity();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEOrientable tileOrientable) {
        TEOrientableMessage msg = new TEOrientableMessage();
        msg.index = 1;
        msg.x = tileOrientable.xCoord;
        msg.y = tileOrientable.yCoord;
        msg.z = tileOrientable.zCoord;
        msg.input = TEOrientable.getIntForForgeDirection(tileOrientable.getInputDirection());
        msg.output = TEOrientable.getIntForForgeDirection(tileOrientable.getOutputDirection());

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEPedestal tilePedestal) {
        TEPedestalMessage msg = new TEPedestalMessage();
        msg.index = 2;
        msg.x = tilePedestal.xCoord;
        msg.y = tilePedestal.yCoord;
        msg.z = tilePedestal.zCoord;
        msg.items = tilePedestal.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEPlinth tilePlinth) {
        TEPlinthMessage msg = new TEPlinthMessage();
        msg.index = 3;
        msg.x = tilePlinth.xCoord;
        msg.y = tilePlinth.yCoord;
        msg.z = tilePlinth.zCoord;
        msg.items = tilePlinth.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TESocket tileSocket) {
        TESocketMessage msg = new TESocketMessage();
        msg.index = 4;
        msg.x = tileSocket.xCoord;
        msg.y = tileSocket.yCoord;
        msg.z = tileSocket.zCoord;
        msg.items = tileSocket.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TETeleposer tileTeleposer) {
        TETeleposerMessage msg = new TETeleposerMessage();
        msg.index = 5;
        msg.x = tileTeleposer.xCoord;
        msg.y = tileTeleposer.yCoord;
        msg.z = tileTeleposer.zCoord;
        msg.items = tileTeleposer.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEWritingTable tileWritingTable) {
        TEWritingTableMessage msg = new TEWritingTableMessage();
        msg.index = 6;
        msg.x = tileWritingTable.xCoord;
        msg.y = tileWritingTable.yCoord;
        msg.z = tileWritingTable.zCoord;
        msg.items = tileWritingTable.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getParticlePacket(String str, double xCoord, double yCoord, double zCoord, double xVel,
            double yVel, double zVel) {
        ParticleMessage msg = new ParticleMessage();
        msg.index = 7;
        msg.particle = str;
        msg.xCoord = xCoord;
        msg.yCoord = yCoord;
        msg.zCoord = zCoord;
        msg.xVel = xVel;
        msg.yVel = yVel;
        msg.zVel = zVel;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getVelSettingPacket(double xVel, double yVel, double zVel) {
        VelocityMessage msg = new VelocityMessage();
        msg.index = 8;
        msg.xVel = xVel;
        msg.yVel = yVel;
        msg.zVel = zVel;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEMasterStone tile) {
        TEMasterStoneMessage msg = new TEMasterStoneMessage();
        msg.index = 9;
        msg.x = tile.xCoord;
        msg.y = tile.yCoord;
        msg.z = tile.zCoord;

        msg.ritual = tile.getCurrentRitual();
        msg.isRunning = tile.isRunning;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEReagentConduit tile) {
        TEReagentConduitMessage msg = new TEReagentConduitMessage();
        msg.index = 10;
        msg.x = tile.xCoord;
        msg.y = tile.yCoord;
        msg.z = tile.zCoord;

        msg.destinationList = tile.destinationList;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getLPPacket(int curLP, int maxLP) {
        CurrentLPMessage msg = new CurrentLPMessage();
        msg.index = 11;
        msg.currentLP = curLP;
        msg.maxLP = maxLP;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getReagentBarPacket(Reagent reagent, float curAR, float maxAR) {
        CurrentReagentBarMessage msg = new CurrentReagentBarMessage();
        msg.index = 12;
        msg.reagent = ReagentRegistry.getKeyForReagent(reagent);
        msg.currentAR = curAR;
        msg.maxAR = maxAR;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getAddedHPPacket(float health, float maxHP) {
        CurrentAddedHPMessage msg = new CurrentAddedHPMessage();
        msg.index = 13;
        msg.currentHP = health;
        msg.maxHP = maxHP;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getKeyboardPressPacket(byte bt) {
        KeyboardMessage msg = new KeyboardMessage();
        msg.index = 14;
        msg.keyPressed = bt;

        return INSTANCE.channels.get(Side.CLIENT).generatePacketFrom(msg);
    }

    public static Packet getGaiaBiomeChangePacket(int x, int z, byte biome, BitSet mask) {
        GaiaBiomeChangeMessage msg = new GaiaBiomeChangeMessage();
        msg.index = 15;
        msg.chunkX = x;
        msg.chunkZ = z;
        msg.biome = biome;
        msg.mask = mask;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public void sendTo(Packet message, EntityPlayerMP player) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.PLAYER);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToAll(Packet message) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.ALL);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToAllAround(Packet message, NetworkRegistry.TargetPoint point) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToDimension(Packet message, Integer dimensionId) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToServer(Packet message) {
        this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.channels.get(Side.CLIENT).writeAndFlush(message)
                .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}
