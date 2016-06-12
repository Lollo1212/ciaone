package eu.assault2142.hololol.chess.networking;

import java.text.MessageFormat;

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
    ChallengeDeclined("challenge:declined:{0}"),
    Move("move:{0}:{1}:{2}"),
    Capture("capture:{0}:{1}:{2}"),
    Check("check"),
    Checkmate("checkmate"),
    Stalemate("stalemate"),
    Resignation("resignation:{0}"),
    Draw("draw:end"),
    DrawOffer("draw:offer"),
    Promotion("promotion:{0}:{1}:{2}"),
    Promote("promote:{0}"),
    AlreadyOnline("loginerror:alreadyonline"),
    Turn("turn"),
    Castling("castling:{0}:{1}:{2}");

    private final String value;
    private final MessageFormat format;

    private ClientMessages(String value) {
        this.value = value;
        format = new MessageFormat(value);
    }

    public String getValue() {
        return value;
    }

    public MessageFormat getFormat() {
        return format;
    }
}
