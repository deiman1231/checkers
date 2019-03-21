import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Board extends JPanel
{
    //Initializing variables
    public Square[][] squares;
    public Square selectedSquare;
    public List<Square> yellowSquares = new ArrayList<Square>();
    public boolean whoMoves = true;
    public boolean attackAgain = false;
    private ImageIcon whiteSquare = new ImageIcon("empty.png");
    private ImageIcon blackSquare = new ImageIcon("empty2.png");
    /**
     * Constructor of board class. Creates an instance of a board
     */
    public Board()
    {
        setLayout(new GridLayout(8, 8));
        squares = new Square[8][8];
        for (int y = 0; y < 8; y++){
            for (int x = 0; x < 8; x++){
                Square square = new Square(this, x, y);
                if ((x + y) % 2 == 0)//setting black and white squares into places
                    square.setIcon(blackSquare);
                else if ((x + y) % 2 == 1){
                    square.setIcon(whiteSquare);
                    if (y < 3)//setting checkers into places
                        square.setPiece(Square.Piece.Red);
                    else if (y > 4)
                        square.setPiece(Square.Piece.White);
                }
                squares[y][x] = square;
                add(squares[y][x]);
            }
        }
    }
    /**
     * method for checking the existance of white checker attacks
     */
    public boolean existsAttackWhite()
    {
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(y > 1 && x < 6 && (squares[y][x].piece == Square.Piece.White || squares[y][x].piece == Square.Piece.WhiteKing) && (squares[y-1][x+1].piece == Square.Piece.Red || squares[y-1][x+1].piece == Square.Piece.RedKing) && squares[y-2][x+2].piece == Square.Piece.None)
                    return true;
                if(y > 1 && x > 1 && (squares[y][x].piece == Square.Piece.White || squares[y][x].piece == Square.Piece.WhiteKing) && (squares[y-1][x-1].piece == Square.Piece.Red || squares[y-1][x-1].piece == Square.Piece.RedKing) && squares[y-2][x-2].piece == Square.Piece.None)
                    return true;
                if(y < 6 && x > 1 && squares[y][x].piece == Square.Piece.WhiteKing && (squares[y+1][x-1].piece == Square.Piece.Red || squares[y+1][x-1].piece == Square.Piece.RedKing) && squares[y+2][x-2].piece == Square.Piece.None)
                    return true;
                if(y < 6 && x < 6 && squares[y][x].piece == Square.Piece.WhiteKing && (squares[y+1][x+1].piece == Square.Piece.Red || squares[y+1][x+1].piece == Square.Piece.RedKing) && squares[y+2][x+2].piece == Square.Piece.None)
                    return true;
                
            }
        }
        return false;
    }
    /**
     * method for checking the existance of red checker attacks
     */
    public boolean existsAttackRed()
    {
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(y < 6 && x > 1 && (squares[y][x].piece == Square.Piece.Red || squares[y][x].piece == Square.Piece.RedKing) && (squares[y+1][x-1].piece == Square.Piece.White || squares[y+1][x-1].piece == Square.Piece.WhiteKing) && squares[y+2][x-2].piece == Square.Piece.None)
                    return true;
                if(y < 6 && x < 6 && (squares[y][x].piece == Square.Piece.Red || squares[y][x].piece == Square.Piece.RedKing) && (squares[y+1][x+1].piece == Square.Piece.White || squares[y+1][x+1].piece == Square.Piece.WhiteKing) && squares[y+2][x+2].piece == Square.Piece.None)
                    return true;
                if(y > 1 && x < 6 && squares[y][x].piece == Square.Piece.RedKing && (squares[y-1][x+1].piece == Square.Piece.White || squares[y-1][x+1].piece == Square.Piece.WhiteKing) && squares[y-2][x+2].piece == Square.Piece.None)
                    return true;
                if(y > 1 && x > 1 && squares[y][x].piece == Square.Piece.RedKing && (squares[y-1][x-1].piece == Square.Piece.White || squares[y-1][x-1].piece == Square.Piece.WhiteKing) && squares[y-2][x-2].piece == Square.Piece.None)
                    return true;
            }
        }
        return false;
    }
    /**
     * method for detecting if someone can become a king
     */
    public void detectionOfKings()
    {
        for(int x = 0; x < 8; x++)
        {
            if(squares[0][x].piece == Square.Piece.White)
            {
                squares[0][x].setPiece(Square.Piece.WhiteKing);
            }
            if(squares[7][x].piece == Square.Piece.Red)
            {
                squares[7][x].setPiece(Square.Piece.RedKing);
            }
        }
    }
}
