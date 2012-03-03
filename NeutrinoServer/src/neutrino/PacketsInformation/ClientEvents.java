/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.PacketsInformation;

/**
 *
 * @author Julián
 */
public class ClientEvents {
    // 2914, 417 no
    public static int ReadRelease = 4000;
    public static int MyData = 1610;
    public static int LoadCredits = 138;
    public static int LoadCatalog = 202;
    public static int LoadFriends = 3071;
    public static int AskToBeAFriend = 2697;
    public static int AcceptFriend = 2133; // (int countfriends) + (int userid)
    public static int RemoveFriend = 1375; // (int countfriends) + (int userid)
    public static int TalkOnChat = 137; // int (to user) + string(content)
    public static int FollowUser = 288;
    public static int ChangeMotto = 503; // string (newmotto)
    public static int SitOnRoom = 942;
    public static int LookTo = 3867;
    public static int LooTo2 = 2541;
    public static int KickUser = 704;
    public static int KickMe = 2880;
    public static int KickUserBYMODTOOLS = 1466;
    public static int BanUserBYMODTOOLS = 2561;
    public static int HelpTicket = 2418;
    public static int BanUserOfRoom = 1782;
    public static int IgnoreUser = 2317; // + utf(name)
    public static int WhispToUser = 13;
    public static int TrainPet = 1586;
    public static int LoadNewEvent = 1217;
    public static int CreateNewEvent = 903;
    public static int EndEvent = 1349;
    public static int LoadBadgesInventary = 2187;
    public static int UserInfo = 2541;
    public static int AllowDoorbell = 3889; // string (name) + bool (can)
    public static int LoadCatalogPage = 111;
    public static int LoadClub = 689;
    public static int LoadUserInfo = 876;
    public static int LoadRoomInfo = 592;
    public static int LoadRoomChatlog = 368; // int (junk) + int(roomid)
    public static int LoadUserChatlog = 3295; // int (userid)
    public static int SendAlertToUser = 846; // int(userid) + string(alert) + string(?)
    public static int PickTicket = 579;
    public static int LoadCFHChatlog = 2233;
    public static int CloseTicket = 282;
    public static int ReleaseTicket = 2998;
    public static int SendAlertToUser2 = 1490;
    public static int SendRoomAlert = 1807;
    public static int PerformRoomAction = 2232;
    public static int PurchaseCatalogItem = 76;
    public static int LookOnEvents = 2727;
    public static int SetHome = 1469;
    public static int CanTrade = 3315;
    public static int TradeObject = 547;
    public static int RemoveObjectFromTrading = 2508;
    public static int AcceptTrade = 2174;
    public static int ConfirmTrade = 3245;
    public static int ChangeTrade = 1372;
    public static int CancelTrade = 1707;
    public static int CancelTrade2 = 3789;
    public static int LoadNavigatorCategories = 2793;
    public static int LookOnMyRooms = 740;
    public static int LookOnAllRooms = 1099;
    public static int LookPublics = 2668;
    public static int LookOnSearchPage = 3509;
    public static int SearchForAPage = 3819;
    public static int LookProfile = 3974;
    public static int LookCats = 3736;
    public static int CanCreateRoom = 457;
    public static int CreateNewRoom = 1641;
    public static int EnterOnRoom = 1274;
    public static int SerializeHeightmap = 3130;
    public static int EndEnterRoom = 1161;
    public static int ChangeLooks = 408;
    public static int Talk = 1203;
    public static int Shout = 3247;
    public static int Walk = 3754;
    public static int Wave = 601;
    public static int Sign = 2554;
    public static int LoadInventary = 852;
    public static int LoadInventary2 = 1652;
    public static int LoadRoomData = 2090;
    public static int SaveRoomData = 362;
    public static int Dance = 2285;
    public static int UpdatePapers = 2326;
    public static int AddItemToMyRoom = 1435;
    public static int MoveOrRotateItem = 2091;
    public static int OnClickOnItem = 514;
    public static int MoveWallItem = 3531;
    public static int PickUpItem = 1759; // [0][0][0][2][0][0][0] / LEN: 8
    public static int LoadSnowStormState = 1124;
    public static int GeneralLeaderBoard = 558;
    public static int FriendLeaderBoard = 336;
    public static int LoadGeneralWeeklyLeaderBoard = 824;
    public static int MovePetToRoom = 553;
    public static int LoadPetData = 3250;
    public static int AddChairToPet = 2315;
    public static int RideAHorse = 529;
    public static int PickUpPet = 3328;
    public static int RemoveChairFromPet = 3405;
    public static int LoadFriendWeeklyLeaderBoard = 1165;
    public static int JoinToSnowStorm = 606;
    public static int UserLeaveGame = 1405;
    public static int Ping = 1880;
    public static int InitSnowStorm = 2994;
    public static int WalkToOnSnow = 1623;
    public static int GetPetRace = 1370;
    public static int IsValidPetName = 1617;
    public static int LoadPetInventary = 782;
    public static int LoadWallInventary = 545;
    public static int TalkOnSnow = 1694;
    public static int InitGuildCreate = 332;
    public static int PurchaseGroup = 340;
}
