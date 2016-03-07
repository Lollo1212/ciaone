package eu.assault2142.hololol.chess.client.networking;

/**
 * An Enum with all possible Messages to the Server
 *
 * @author hololol2
 */
public enum ServerMessages {
    Message("msg:{0}:{1}"),
    FriendsAdd("friends:add:{0}"),
    FriendsRemove("friends:remove:{0}"),
    FriendsAccept("friends:accept:{0}"),
    FriendsReject("friends:decline:{0}"),
    Logout("logout"),
    ChangeUsername("change:username:{0}"),
    ChangePassword("change:password:{0}"),
    AcceptChallenge("game:accept:{0}"),
    DeclineChallenge("game:decline:{0}"),
    Challenge("newgame:friend:{0}"),
    PlayRandom("newgame:random"),
    DoMove("move:{0}:{1}:{2}"),
    Resignation("resignation"),
    OfferDraw("draw"),
    Promotion("promotion:{0}:{1}:{2}");

    private final String value;

    private ServerMessages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
