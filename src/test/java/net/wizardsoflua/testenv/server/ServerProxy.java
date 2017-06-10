package net.wizardsoflua.testenv.server;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.wizardsoflua.testenv.CommonProxy;
import net.wizardsoflua.testenv.WolTestEnvironment;
import net.wizardsoflua.testenv.net.ConfigMessage;

@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy {

  @Override
  public void onInit(FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(WolTestEnvironment.instance.getEventRecorder());
  }

  @SubscribeEvent
  public void onEvent(PlayerLoggedInEvent evt) {
    System.out.println("PlayerLoggedInEvent " + evt);
    System.out.println("PlayerLoggedInEvent");
    if (WolTestEnvironment.instance.getTestPlayer() == null) {
      EntityPlayerMP player = (EntityPlayerMP) evt.player;
      WolTestEnvironment.instance.setTestPlayer(player);
      makeOperator(player);
      ConfigMessage message = new ConfigMessage();
      message.wolVersionOnServer = WolTestEnvironment.VERSION;
      WolTestEnvironment.instance.getPacketPipeline().sendTo(message, player);
    }
  }

  @SubscribeEvent
  public void onEvent(PlayerLoggedOutEvent evt) {
    System.out.println("PlayerLoggedOutEvent evt");
    EntityPlayerMP testPlayer = WolTestEnvironment.instance.getTestPlayer();
    if (testPlayer != null && testPlayer == evt.player) {
      WolTestEnvironment.instance.setTestPlayer(null);
    }
  }

  private void makeOperator(EntityPlayerMP player) {
    MinecraftServer server = WolTestEnvironment.instance.getServer();
    GameProfile gameprofile =
        server.getPlayerProfileCache().getGameProfileForUsername(player.getName());
    server.getPlayerList().addOp(gameprofile);
  }

}