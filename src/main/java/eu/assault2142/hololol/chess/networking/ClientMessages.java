package eu.assault2142.hololol.chess.networking;

/**
 * An Enum with all possible Messages to a Client
 *
 * @author hololol2
 */
public enum ClientMessages {
    Hello("hello"),
    UsernameWrong("loginerror:username"),
    PasswordWrong("loginerror:password"),
    LoggedIn("loggedin"),
    Message("msg:{0}"),
    Name("name:{0}"),
    Friends("friends:{0}"),
    AcceptUsernameChange("change:username:accept:{0}"),
    DeclineUsernameChange("change:username:decline"),
    AcceptPasswordChange("change:password:accept"),
    DeclinePasswordChange("change:password:decline"),
    Request("request:{0}"),
    Newgame("newgame:{0}"),
    Gamestart("gamestart:{0}"),
    Move("move:{0}:{1}:{2}"),
    Check("check"),
    Checkmate("checkmate"),
    Stalemate("stalemate"),
    Moves("moves:{0}"),
    Resignation("resignation:{0}"),
    Draw("draw"),
    DrawOffer("draw:offer"),
    Promotion("promotion:{0}:{1}:{2}"),
    Promote("promote:{1}");

    private final String value;

    private ClientMessages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
