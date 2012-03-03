package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.ItemManager.RoomItem;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Room;
import org.jboss.netty.channel.Channel;

public class ClickOnFurniMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo User = Client.GetSession();
        int RoomId = User.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        int ItemId = Client.inreader.readInt();
        int ExtraInformation = Client.inreader.readInt();
        Server.WriteLine(ItemId + ";" + ExtraInformation);
        RoomItem Item = RoomItem.Items.get(ItemId);
        if (Item.GetBaseItem().Interactor == "drinks")
        {
            if (Item.ExtraData == "")
                Item.ExtraData = "0";
            if (Item.ExtraData == "0")
                Item.ExtraData = "1";
            else if (Item.ExtraData == "1")
                Item.ExtraData = "0";
            Item.UpdateState();

            if (Item.GetBaseItem().Name.contains("icecream"))
            {
                //User.CarryItem(4);
            }

        } else if (Item.GetBaseItem().Interactor.equals("musicplayer"))
        {
           /* if (R.OwnerId == User.Id || User.Rank >= 8)
                (new JukeBox(User)).ShowMyDisk();*/
        }
        else
        {
            if (Item.GetBaseItem().Interactor.equals("default"))
            {
                /*if (User.QuestType.Contains("SWITCH_ITEM_STATE"))
                    User.UpdateQuest(1, 12);*/
            	
            	// Banzai
                if (Item.GetBaseItem().Name.contains("bb_"))
                {
                	// Counter
                    	if(!R.HavePowers(User.Id))
                    		return;
                        
                    	if (Item.GetBaseItem().Name.contains("counter"))
                        {
                            if (Item.ExtraData == "")
                                Item.ExtraData = "0";


                            if (ExtraInformation == 1)
                            {
                                /*if (R.BattleOnGame && !R.BattlePause)
                                {
                                    // Pause Game
                                    R.BattlePause = true;
                                }
                                else if (R.BattleOnGame && R.BattlePause)
                                {
                                    // Restart Game
                                    if (R.CurrentBanzai == null)
                                        return;

                                    R.BattlePause = false;
                                    R.CurrentBanzai.ContinueGame();
                                }
                                else if (!R.BattleOnGame)
                                {
                                    // Start Game
                                    BattleBanzai Batt = new BattleBanzai(Item, R.FloorItems, User, R, this);
                                    R.CurrentBanzai = Batt;
                                    Batt.StartGame();
                                }
                                /*if (StaticGame.CounterSystem.ContainsKey(Item.Id))
                                {
                                    // PAUSE GAME
                                    Thread Battle = StaticGame.CounterSystem[Item.Id];
                                    Battle.Abort();
                                    Battle = null;
                                    StaticGame.CounterSystem.Remove(Item.Id);
                                    R.BattlePause = true;
                                }
                                else
                                {
                                    if (R.BattleOnGame && R.BattlePause)
                                    {
                                        BattleBanzai Batt = new BattleBanzai(Item, R.FloorItems, User, R, this);
                                        Thread Battle = new Thread(new ThreadStart(Batt.StartCounter));
                                        StaticGame.CounterSystem.Add(Item.Id, Battle);
                                        Battle.Start();
                                        Thread.Sleep(1);
                                        R.BattlePause = false;
                                    }
                                    else
                                    {
                                        // PLAY GAME!
                                        R.BattleOnGame = true;
                                        int TimeSeconds = int.Parse(Item.ExtraData);
                                        if (TimeSeconds > 0)
                                        {
                                            BattleBanzai Batt = new BattleBanzai(Item, R.FloorItems, User, R, this);
                                            Batt.StartGame();
                                            Thread Battle = new Thread(new ThreadStart(Batt.StartCounter));
                                            StaticGame.CounterSystem.Add(Item.Id, Battle);
                                            Battle.Start();
                                            Thread.Sleep(1);
                                        }
                                    }
                                }
                                 */
                            }
                            else if (ExtraInformation == 2)
                            {
                                // Times
                                if (Item.ExtraData == "")
                                    Item.ExtraData = "0";

                                int Extra = Integer.parseInt(Item.ExtraData);
                                if (Extra == 0)
                                    Extra = 30;
                                else if (Extra == 30)
                                    Extra = 60;
                                else if (Extra == 60)
                                    Extra = 120;
                                else if (Extra == 120)
                                    Extra = 180;
                                else if (Extra == 180)
                                    Extra = 300;
                                else if (Extra == 300)
                                    Extra = 600;
                                else if (Extra == 600)
                                    Extra = 0;
                                else
                                    Extra = 0;

                                Item.ExtraData = ((Integer)Extra).toString();
                                Item.UpdateState();
                            }
                        }
                }
                // Freeze
                else if (Item.GetBaseItem().Name.contains("es_"))
                {
                        // Counter
                        if (Item.GetBaseItem().Name.contains("counter"))
                        {
                        	if(!R.HavePowers(User.Id))
                        		return;
                            if (Item.ExtraData == "")
                                Item.ExtraData = "0";

                            if (ExtraInformation == 1)
                            {
                                /*if (StaticGame.CounterSystem.ContainsKey(Item.Id))
                                {
                                    // PAUSE GAME
                                    Thread Freeze = StaticGame.CounterSystem[Item.Id];
                                    Freeze.Abort();
                                    Freeze = null;
                                    StaticGame.CounterSystem.Remove(Item.Id);
                                    R.FreezePause = true;
                                }
                                else
                                {
                                    if (R.FreezeOnGame && R.FreezePause)
                                    {
                                        Freeze Freez = new Freeze(Item, R.FloorItems, User, R, this);
                                        Thread Freeze = new Thread(new ThreadStart(Freez.StartCounter));
                                        StaticGame.CounterSystem.Add(Item.Id, Freeze);
                                        Freeze.Start();
                                        Thread.Sleep(1);
                                        R.FreezePause = false;
                                    }
                                    else
                                    {
                                        // PLAY GAME!
                                        R.FreezeOnGame = true;
                                        int TimeSeconds = int.Parse(Item.ExtraData);
                                        if (TimeSeconds > 0)
                                        {
                                            Freeze Freez = new Freeze(Item, R.FloorItems, User, R, this);
                                            Freez.StartGame();
                                            Thread Freeze = new Thread(new ThreadStart(Freez.StartCounter));
                                            StaticGame.CounterSystem.Add(Item.Id, Freeze);
                                            Freeze.Start();
                                            Thread.Sleep(1);
                                        }
                                    }
                                }*/
                            }
                            else if (ExtraInformation == 2)
                            {
                                // Times
                                int Extra = Integer.parseInt(Item.ExtraData);
                                if (Extra == 0)
                                    Extra = 30;
                                else if (Extra == 30)
                                    Extra = 60;
                                else if (Extra == 60)
                                    Extra = 120;
                                else if (Extra == 120)
                                    Extra = 180;
                                else if (Extra == 180)
                                    Extra = 300;
                                else if (Extra == 300)
                                    Extra = 600;
                                else if (Extra == 600)
                                    Extra = 0;
                                else
                                    Extra = 0;

                                Item.ExtraData = ((Integer)Extra).toString();
                                Item.UpdateState();
                            }
                        }
                    /*
                    if (R.FreezeOnGame)
                    {
                        if (Item.ExtraData == "")
                            Item.ExtraData = "0";

                        #region Tiles
                        if (Item.GetBaseItem().Name.Contains("tile") || Item.GetBaseItem().Name.Contains("box") && Item.ExtraData != "0")
                        {
                            if (Item.GetBaseItem().Name == "es_box")
                            {
                                bool IsTile = false;
                                foreach (RoomItem zItem in GetItemsForXY(Item.X, Item.Y))
                                {
                                    if (zItem.GetBaseItem().Name == "es_tile")
                                    {
                                        IsTile = true;
                                        Item = zItem;
                                    }
                                }

                                if (!IsTile)
                                    return;
                            }

                            if (User.CurrentEffect != 40 && User.CurrentEffect != 41 && User.CurrentEffect != 42 && User.CurrentEffect != 43 && User.CurrentEffect != 49 && User.CurrentEffect != 50 && User.CurrentEffect != 51 && User.CurrentEffect != 52)
                                return;

                            if (User.frzBalls > 0)
                            {
                                User.frzBalls--;
                                TileToFreeze = Item;
                                if (StaticGame.FreezeSystem.ContainsKey(Item.Id))
                                {
                                    Thread SubFreeze = StaticGame.FreezeSystem[Item.Id];
                                    StaticGame.FreezeSystem.Remove(Item.Id);
                                    SubFreeze.Abort();
                                }
                                Thread sSubFreeze2 = new Thread(new ThreadStart(FreezeTiles));
                                sSubFreeze2.Start();
                                StaticGame.FreezeSystem.Add(Item.Id, sSubFreeze2);
                            }
                        }
                        #endregion
                    }
                }
                #endregion
                */
            }
                //#region Wired Furniture
                else if (Item.GetBaseItem().Name.startsWith("wf_"))
                {
                    if (R.OwnerId == User.Id || User.RankLevel >= 7)
                    {
                        // Wired
                    	/*
                        if (Item.GetBaseItem().Name.Contains("trg_enter_room"))
                        {
                            // JJHHH[XNjKZHS{2}HHSAH{2}{1}
                            ServerMessage WiredFurni = new ServerMessage("JJ");
                            WiredFurni.AddInt(0);
                            WiredFurni.AddInt(5);
                            WiredFurni.AddInt(0);
                            WiredFurni.AddInt(Item.GetBaseItem().SpriteId);
                            WiredFurni.AddInt(Item.Id);
                            WiredFurni.AddStringWithBreak(Item.WiredString); // User Maybe?
                            WiredFurni.AddInt(0);
                            WiredFurni.AddInt(0);
                            WiredFurni.AddInt(7);
                            WiredFurni.AddInt(0);
                            WiredFurni.AddStringWithBreak("");
                            User.SendMessage(WiredFurni);
                        }
                        else if (Item.GetBaseItem().Name.Contains("wf_act_show_message"))
                        {
                            ServerMessage WiredFurni = new ServerMessage("JK");
                            WiredFurni.AddBoolean(false); // boolean false?
                            WiredFurni.AddInt(5); // Max furnis selected (WTF?)
                            WiredFurni.AddInt(0); // Total furnis selected
                            // here WiredFurni(ItemId2) if you have one item selected, it repeats and blablabla
                            WiredFurni.AddInt(Item.GetBaseItem().SpriteId); // Item SpriteID
                            WiredFurni.AddInt(Item.Id); // ItemID
                            WiredFurni.AddStringWithBreak(Item.WiredString); // Say Message
                            WiredFurni.AddInt(0); // Blah
                            WiredFurni.AddInt(0); // Blah
                            WiredFurni.AddInt(7); // More
                            WiredFurni.AddInt(0); // and mooore!
                            WiredFurni.AddStringWithBreak(""); // char2 :D!
                            User.SendMessage(WiredFurni);
                        }
                        else if (Item.GetBaseItem().Name.Contains("wf_act_teleport_to"))
                        {
                            ServerMessage WiredFurni = new ServerMessage("JK");
                            WiredFurni.AddBoolean(false); // boolean false?
                            WiredFurni.AddInt(5); // Max Furnis Selected
                            WiredFurni.AddInt(Item.WiredItems.Count); // Total furnis selected
                            foreach (int i in Item.WiredItems)
                            {
                                WiredFurni.AddInt(i);
                            }
                            // here WiredFurni(ItemId2) if you have one item selected, it repeats and blablabla
                            WiredFurni.AddInt(Item.GetBaseItem().SpriteId); // Item SpriteID
                            WiredFurni.AddInt(Item.Id); // ItemID
                            WiredFurni.AddStringWithBreak("HHPBHH"); // some blahblahblah
                            WiredFurni.AddStringWithBreak("HHPBHH"); // some blah
                            User.SendMessage(WiredFurni);
                        }
                        else if (Item.GetBaseItem().Name.Contains("wf_trg_walks_on_furni"))
                        {
                            ServerMessage WiredFurni = new ServerMessage("JJ");
                            WiredFurni.AddBoolean(false); // boolean false?
                            WiredFurni.AddInt(5); // Max Furnis Selected
                            WiredFurni.AddInt(Item.WiredItems.Count); // Total furnis selected
                            foreach (int i in Item.WiredItems)
                            {
                                WiredFurni.AddInt(i);
                            }
                            // here WiredFurni(ItemId2) if you have one item selected, it repeats and blablabla
                            WiredFurni.AddInt(Item.GetBaseItem().SpriteId); // Item SpriteID
                            WiredFurni.AddInt(Item.Id); // ItemID
                            WiredFurni.AddStringWithBreak("HHIH");
                            WiredFurni.AddStringWithBreak("HHIH");
                            User.SendMessage(WiredFurni);
                        }
                        else if (Item.GetBaseItem().Name.Contains("wf_trg_walks_off_furni"))
                        {
                            ServerMessage WiredFurni = new ServerMessage("JJ");
                            WiredFurni.AddBoolean(false); // boolean false?
                            WiredFurni.AddInt(5); // Max Furnis Selected
                            WiredFurni.AddInt(Item.WiredItems.Count); // Total furnis selected
                            foreach (int i in Item.WiredItems)
                            {
                                WiredFurni.AddInt(i);
                            }
                            // here WiredFurni(ItemId2) if you have one item selected, it repeats and blablabla
                            WiredFurni.AddInt(Item.GetBaseItem().SpriteId); // Item SpriteID
                            WiredFurni.AddInt(Item.Id); // ItemID
                            WiredFurni.AddStringWithBreak("HHIH");
                            WiredFurni.AddStringWithBreak("HHIH");
                            User.SendMessage(WiredFurni);
                        }
                        else if (Item.GetBaseItem().Name.ToLower() == "wf_trg_game_ends")
                        {
                            ServerMessage WiredFurni = new ServerMessage("JJ");
                            WiredFurni.AddBoolean(false); // boolean false?
                            WiredFurni.AddInt(5); // Max Furnis Selected
                            WiredFurni.AddInt(0);
                            WiredFurni.AddInt(Item.GetBaseItem().SpriteId); // Item SpriteID
                            WiredFurni.AddInt(Item.Id); // ItemID
                            WiredFurni.AddStringWithBreak("HHPBH");
                            WiredFurni.AddStringWithBreak("HHPBH");
                            User.SendMessage(WiredFurni);
                        }
                        else if (Item.GetBaseItem().Name.ToLower() == "wf_trg_game_starts")
                        {
                            ServerMessage WiredFurni = new ServerMessage("JJ");
                            WiredFurni.AddBoolean(false); // boolean false?
                            WiredFurni.AddInt(5); // Max Furnis Selected
                            WiredFurni.AddInt(0);
                            WiredFurni.AddInt(Item.GetBaseItem().SpriteId); // Item SpriteID
                            WiredFurni.AddInt(Item.Id); // ItemID
                            WiredFurni.AddStringWithBreak("HHPBH");
                            WiredFurni.AddStringWithBreak("HHPBH");
                            User.SendMessage(WiredFurni);
                        }
                        else if (Item.GetBaseItem().Name == "wf_act_toggle_state")
                        {
                            ServerMessage WiredFurni = new ServerMessage("JK");
                            WiredFurni.AddBoolean(false); // boolean false?
                            WiredFurni.AddInt(5); // Max Furnis Selected
                            WiredFurni.AddInt(Item.WiredItems.Count); // Total furnis selected
                            foreach (int i in Item.WiredItems)
                            {
                                WiredFurni.AddInt(i);
                            }
                            // here WiredFurni(ItemId2) if you have one item selected, it repeats and blablabla
                            WiredFurni.AddInt(Item.GetBaseItem().SpriteId); // Item SpriteID
                            WiredFurni.AddInt(Item.Id); // ItemID
                            WiredFurni.AddString("HHH");
                            if (Item.WiredString != "")
                                WiredFurni.AddInt((int.Parse(Item.WiredString) / 500));
                            else
                                WiredFurni.AddInt(0);
                            WiredFurni.AddStringWithBreak("H"); // HHH [Wait] HH
                            WiredFurni.AddString("HHH");
                            if (Item.WiredString != "")
                                WiredFurni.AddInt((int.Parse(Item.WiredString) / 500));
                            else
                                WiredFurni.AddInt(0);
                            WiredFurni.AddStringWithBreak("H"); // HHH [Wait] HH
                            User.SendMessage(WiredFurni);
                        }
                        else if (Item.GetBaseItem().Name == "wf_trg_state_changed")
                        {
                            ServerMessage WiredFurni = new ServerMessage("JJ");
                            WiredFurni.AddBoolean(false); // boolean false?
                            WiredFurni.AddInt(5); // Max Furnis Selected
                            WiredFurni.AddInt(Item.WiredItems.Count); // Total furnis selected
                            foreach (int i in Item.WiredItems)
                            {
                                WiredFurni.AddInt(i);
                            }
                            // here WiredFurni(ItemId2) if you have one item selected, it repeats and blablabla
                            WiredFurni.AddInt(Item.GetBaseItem().SpriteId); // Item SpriteID
                            WiredFurni.AddInt(Item.Id); // ItemID
                            WiredFurni.AddStringWithBreak("HHPAH");
                            WiredFurni.AddStringWithBreak("HHPAH");
                            User.SendMessage(WiredFurni);
                        }
                        else if (Item.GetBaseItem().Name.ToLower() == "wf_trg_says_something")
                        {
                            ServerMessage WiredFurni = new ServerMessage("JJ");
                            WiredFurni.AddBoolean(false); // boolean false?
                            WiredFurni.AddInt(5); // Max Furnis Selected
                            WiredFurni.AddInt(0); // No Items
                            WiredFurni.AddInt(Item.GetBaseItem().SpriteId); // Item SpriteID
                            WiredFurni.AddInt(Item.Id); // ItemID
                            WiredFurni.AddStringWithBreak(Item.WiredString);
                            User.SendMessage(WiredFurni);
                        }
                        else if (Item.GetBaseItem().Name == "wf_trg_periodically")
                        {
                            ServerMessage WiredFurni = new ServerMessage("JJ");
                            WiredFurni.AddBoolean(false); // boolean false?
                            WiredFurni.AddInt(5); // Max Furnis Selected
                            WiredFurni.AddInt(0); // No Items
                            WiredFurni.AddInt(Item.GetBaseItem().SpriteId); // Item SpriteID
                            WiredFurni.AddInt(Item.Id); // ItemID
                            WiredFurni.AddInt(1);
                            if (Item.WiredString != "")
                                WiredFurni.AddInt((int.Parse(Item.WiredString) / 500));
                            else
                                WiredFurni.AddInt(0);
                            WiredFurni.AddStringWithBreak("HRAH");
                            WiredFurni.AddInt(1);
                            if (Item.WiredString != "")
                                WiredFurni.AddInt((int.Parse(Item.WiredString) / 500));
                            else
                                WiredFurni.AddInt(0);
                            WiredFurni.AddStringWithBreak("HRAH");
                            User.SendMessage(WiredFurni);
                        }
                        else if (Item.GetBaseItem().Name.Contains("wf_act_move_rotate"))
                        {
                            ServerMessage WiredFurni = new ServerMessage("JK");
                            WiredFurni.AddBoolean(false); // boolean false?
                            WiredFurni.AddInt(5); // Max Furnis Selected
                            WiredFurni.AddInt(Item.WiredItems.Count); // Total furnis selected
                            foreach (int i in Item.WiredItems)
                            {
                                WiredFurni.AddInt(i);
                            }
                            // here WiredFurni(ItemId2) if you have one item selected, it repeats and blablabla
                            WiredFurni.AddInt(Item.GetBaseItem().SpriteId); // Item SpriteID
                            WiredFurni.AddInt(Item.Id); // ItemID
                            WiredFurni.AddInt(2); // ??
                            if (Item.WiredString.Contains("|"))
                            {
                                string[] Separe = Item.WiredString.Split('|');
                                WiredFurni.AddInt(int.Parse(Separe[0])); // Move
                                WiredFurni.AddInt(int.Parse(Separe[1])); // Rotate
                                WiredFurni.AddInt(0);
                                WiredFurni.AddInt(4);
                                WiredFurni.AddInt((int.Parse(Separe[2]) / 500)); // Miliseconds
                                WiredFurni.AddStringWithBreak("H");
                                WiredFurni.AddInt(2); // ??
                                WiredFurni.AddInt(int.Parse(Separe[0])); // Move
                                WiredFurni.AddInt(int.Parse(Separe[1])); // Rotate
                                WiredFurni.AddInt(0);
                                WiredFurni.AddInt(4);
                                WiredFurni.AddInt((int.Parse(Separe[2]) / 500)); // Miliseconds
                                WiredFurni.AddStringWithBreak("H");
                            }
                            else
                            {
                                WiredFurni.AddStringWithBreak("HHHPAHH");
                                WiredFurni.AddStringWithBreak("JHHHPAHH");
                            }
                            User.SendMessage(WiredFurni);
                        }
                        else if (Item.GetBaseItem().Name == "wf_floor_switch1")
                        {
                            Coord Actual = new Coord(User.X, User.Y);
                            if (Item.SquareBehind != Actual && Item.SquareDer != Actual && Item.SquareIzq != Actual && Item.SquareDer != Actual)
                                return;

                            if (Item.ExtraData == "")
                                Item.ExtraData = "0";
                            if (Item.ExtraData == "0")
                                Item.ExtraData = "1";
                            else if (Item.ExtraData == "1")
                                Item.ExtraData = "0";
                            Item.UpdateState(User);
                        */}
                        //#endregion
                   
                }
                //#endregion
                else if(R.HavePowers(cUser.Id))
                {
                    if (Item.ExtraData.equals(""))
                        Item.ExtraData = "0";
                    if (Item.ExtraData.equals("0"))
                        Item.ExtraData = "1";
                    else if (Item.ExtraData.equals("1"))
                        Item.ExtraData = "0";
                    Item.UpdateState();
                }
            }
            else if (Item.GetBaseItem().Interactor.equals("gate") && R.HavePowers(cUser.Id))
            {
                String ExtraData = "1";
                if (Item.ExtraData.equals("1"))
                    ExtraData = "0";
                else if (Item.ExtraData.equals(""))
                	ExtraData = "0";

                Item.IsUpdated = true;
                Item.ExtraData = ExtraData;

                Item.UpdateState();
            }
        }
	}
}
