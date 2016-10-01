/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.game;

import eu.assault2142.hololol.chess.game.chessmen.Bishop;
import eu.assault2142.hololol.chess.game.chessmen.Chessman;
import eu.assault2142.hololol.chess.game.chessmen.King;
import eu.assault2142.hololol.chess.game.chessmen.Knight;
import eu.assault2142.hololol.chess.game.chessmen.Pawn;
import eu.assault2142.hololol.chess.game.chessmen.Queen;
import eu.assault2142.hololol.chess.game.chessmen.Rook;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hololol2
 */
public class TestGameState extends GameState {

    public TestGameState(String pieces, boolean color, String castling, String enpassant) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        super((Game) null);
        initSquares();
        String[] ranks = pieces.split("/");
        int posY = 0;
        int posX = 0;
        boolean pieceblack;
        int piablack = 0, piawhite = 0;
        for (String rank : ranks) {
            for (char c : rank.toCharArray()) {
                if (Character.isDigit(c)) {
                    posX += c;
                } else {
                    pieceblack = true;
                    if (Character.isUpperCase(c)) {
                        pieceblack = false;
                    }
                    switch (Character.toUpperCase(c)) {
                        case 'P':
                            Constructor constructor = Pawn.class.getDeclaredConstructor(boolean.class, int.class, int.class, Game.class);
                            constructor.setAccessible(true);
                            Pawn pawn = (Pawn) constructor.newInstance(pieceblack, posX, posY, null);
                            getSquare(posX, posY).occupier = pawn;
                            Field field = Chessman.class.getDeclaredField("positioninarray");
                            field.setAccessible(true);
                            field.set(pawn, pieceblack ? piablack : piawhite);
                            piawhite = pieceblack ? piawhite : piawhite + 1;
                            piablack = pieceblack ? piablack + 1 : piablack;
                            break;
                        case 'N':
                            constructor = Knight.class.getDeclaredConstructor(boolean.class, int.class, int.class, Game.class);
                            constructor.setAccessible(true);
                            Knight knight = (Knight) constructor.newInstance(pieceblack, posX, posY, null);
                            getSquare(posX, posY).occupier = knight;
                            field = Chessman.class.getDeclaredField("positioninarray");
                            field.setAccessible(true);
                            field.set(knight, pieceblack ? piablack : piawhite);
                            piawhite = pieceblack ? piawhite : piawhite + 1;
                            piablack = pieceblack ? piablack + 1 : piablack;
                            break;
                        case 'B':
                            constructor = Bishop.class.getDeclaredConstructor(boolean.class, int.class, int.class, Game.class);
                            constructor.setAccessible(true);
                            Bishop bishop = (Bishop) constructor.newInstance(pieceblack, posX, posY, null);
                            getSquare(posX, posY).occupier = bishop;
                            field = Chessman.class.getDeclaredField("positioninarray");
                            field.setAccessible(true);
                            field.set(bishop, pieceblack ? piablack : piawhite);
                            piawhite = pieceblack ? piawhite : piawhite + 1;
                            piablack = pieceblack ? piablack + 1 : piablack;
                            break;
                        case 'R':
                            constructor = Rook.class.getDeclaredConstructor(boolean.class, int.class, int.class, Game.class);
                            constructor.setAccessible(true);
                            Rook rook = (Rook) constructor.newInstance(pieceblack, posX, posY, null);
                            getSquare(posX, posY).occupier = rook;
                            field = Chessman.class.getDeclaredField("positioninarray");
                            field.setAccessible(true);
                            field.set(rook, pieceblack ? piablack : piawhite);
                            piawhite = pieceblack ? piawhite : piawhite + 1;
                            piablack = pieceblack ? piablack + 1 : piablack;
                            break;
                        case 'Q':
                            constructor = Queen.class.getDeclaredConstructor(boolean.class, int.class, int.class, Game.class);
                            constructor.setAccessible(true);
                            Queen queen = (Queen) constructor.newInstance(pieceblack, posX, posY, null);
                            getSquare(posX, posY).occupier = queen;
                            field = Chessman.class.getDeclaredField("positioninarray");
                            field.setAccessible(true);
                            field.set(queen, pieceblack ? piablack : piawhite);
                            piawhite = pieceblack ? piawhite : piawhite + 1;
                            piablack = pieceblack ? piablack + 1 : piablack;
                            break;
                        case 'K':
                            constructor = King.class.getDeclaredConstructor(boolean.class, int.class, int.class, Game.class);
                            constructor.setAccessible(true);
                            King king = (King) constructor.newInstance(pieceblack, posX, posY, null);
                            getSquare(posX, posY).occupier = king;
                            field = Chessman.class.getDeclaredField("positioninarray");
                            field.setAccessible(true);
                            field.set(king, pieceblack ? piablack : piawhite);
                            piawhite = pieceblack ? piawhite : piawhite + 1;
                            piablack = pieceblack ? piablack + 1 : piablack;
                            break;
                    }
                }
            }
            posX = 0;
            posY++;
        }
        if (!enpassant.equals("-")) {
            if (getTurn() == color) {
                nextTurn(null);
            }
            char file = enpassant.charAt(0);
            char rank = enpassant.charAt(1);

            if (rank == '3') {
                nextTurn(getSquare(Character.getNumericValue(rank) - 10, 3).occupier);
            } else {
                nextTurn(getSquare(Character.getNumericValue(rank) - 10, 4).occupier);
            }
        } else if (getTurn() != color) {
            nextTurn(null);
        }
    }

    public static void main(String[] args) {
        try {
            new TestGameState("1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5", true, "-", "-");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(TestGameState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(TestGameState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TestGameState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TestGameState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(TestGameState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(TestGameState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
