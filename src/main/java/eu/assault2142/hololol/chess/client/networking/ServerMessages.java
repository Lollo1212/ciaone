package eu.assault2142.hololol.chess.client.networking;

/**
 * An Enum with all possible Messages to the Server
 *
 * @author hololol2
 */
public enum ServerMessages {
    Message("msg:{0}:{1}"),
    AddFriend("friends:add:{0}"),
    RemoveFriend("friends:remove:{0}"),
    AcceptFriend("friends:accept:{0}"),
    DeclineFriend("friends:decline:{0}"),
    Logout("logout"),
    ChangePassword("change:password:{0}"),
    ChangeUsername("change:username:{0}"),
    AcceptGame("game:accept:{0}"),
    DeclineGame("game:decline:{0}"),
    RandomGame("newgame:random"),
    FriendGame("newgame:friend:{0}"),
    Click("click:{0}:{1}"),
    Promotion("promotion:{0},{1},{2}"),
    Resignation("resignation"),
    Draw("draw");

    private final String value;

    private ServerMessages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
