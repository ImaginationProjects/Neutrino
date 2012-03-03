package neutrino.ItemManager;

import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo; // some extradata shits
import neutrino.RoomManager.Room;
import neutrino.ItemManager.RoomItem;

public class RoomItemComposer {
	// basic roomitems composer
	public void Compose(RoomItem Item, ServerMessage Message) throws Exception
	{
		Room RoomData = Room.Rooms.get(Item.RoomId);
		Message.writeInt(Item.Id);
		Message.writeInt(Item.GetBaseItem().SpriteId);
		Message.writeInt(Item.X);
		Message.writeInt(Item.Y);
		Message.writeInt(Item.Rot);
		Message.writeUTF("0.0");
		Message.writeInt(0);
		Message.writeInt(0);
		Message.writeUTF(Item.ExtraData);
		Message.writeInt(-1);
		Message.writeBoolean((Item.GetBaseItem().Interactor.equals("default")) ? true : false);
		Message.writeInt(RoomData.OwnerId);
	}
	
	public void ComposeExtradataUpdate(RoomItem Item, ServerMessage Message) throws Exception
	{
		// soon coded, maybe like older releases? (itemid + extradata?)
	}
	
	public void ComposeWall(RoomItem Item, ServerMessage Message) throws Exception
	{
		// must analize this
		Room RoomData = Room.Rooms.get(Item.RoomId);
		Message.writeInt(Item.Id);
		Message.writeInt(Item.GetBaseItem().SpriteId);
	}
}
