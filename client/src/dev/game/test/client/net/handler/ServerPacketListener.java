package dev.game.test.client.net.handler;

public class ServerPacketListener {


//    @PacketEvent
//    public void on(PacketEntitySpawn packet) {
//        IWorld world = game.getClientManager().getCurrentWorld();
//
//        IPlayer player = world.getPlayer(packet.getId());
//        IClientManager clientManager = game.getClientManager();
//
//        if (packet.getId().equals(clientManager.getPlayer().getId())) {
//            player = clientManager.getPlayer();
//        } else if (player == null) {
//            player = new Player(packet.getId(), "");
//        }
//
//        if (!player.isSpawned()) {
//            world.spawnEntity(player, packet.getPosition());
//        }
//    }
//
//    @PacketEvent
//    public void on(PacketPlayerInfo packet) {
//        IWorld world = game.getClientManager().getCurrentWorld();
//        IPlayer player = world.getPlayer(packet.getId());
//
//        if (player == null) {
//            return;
//        }
//
//        player.setName(packet.getName());
//    }
//
//
//    @PacketEvent
//    public void on(PacketEntityPosition packet) {
//        IWorld world = game.getClientManager().getCurrentWorld();
//        IEntity entity = world.getEntity(packet.getId());
//
//        if (entity != null) {
//            /*if(!entity.equals(connectionHandler.clientManager.getPlayer())) {
//                entity.beginInterpolation(packet.getPosition(), Constants.INTERPOLATION_TIME);
//            } else {
//                entity.beginInterpolation(packet.getPosition(), Constants.INTERPOLATION_TIME * 4);
//            }*/
//        }
//    }
//
//    @PacketEvent
//    public void on(PacketEntityMovement packet) {
//        IWorld world = game.getClientManager().getCurrentWorld();
//        IPlayer player = world.getPlayer(packet.getId());
//
//        if (player != null) {
//            //player.move(packet.getMovement());
//        }
//    }
}
