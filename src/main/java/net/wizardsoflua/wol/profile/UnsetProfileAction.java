package net.wizardsoflua.wol.profile;

import java.util.Collections;
import java.util.Deque;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.wizardsoflua.WizardsOfLua;
import net.wizardsoflua.WolAnnouncementMessage;
import net.wizardsoflua.wol.menu.CommandAction;
import net.wizardsoflua.wol.menu.MenuEntry;

public class UnsetProfileAction extends MenuEntry implements CommandAction {

  private final WizardsOfLua wol;

  public UnsetProfileAction() {
    wol = WizardsOfLua.instance;
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,
      Deque<String> argList, BlockPos targetPos) {
    return Collections.emptyList();
  }

  @Override
  public void execute(ICommandSender sender, Deque<String> argList) throws CommandException {
    Entity entity = sender.getCommandSenderEntity();
    if (entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entity;
      wol.getProfiles().setProfile(player, null);
      sender.sendMessage(new WolAnnouncementMessage("unset profile"));
    } else {
      // TODO I18n
      throw new CommandException("Only players can execute this command!");
    }
  }

}
