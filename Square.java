import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Square extends JButton implements ActionListener
{
    //Initializing variables
    public enum Piece
    {
        None, 
        Red, 
        White,
        RedKing,
        WhiteKing
    };
    private ImageIcon redChecker = new ImageIcon("red.png");
    private ImageIcon whiteChecker = new ImageIcon("white.png");
    private ImageIcon whiteSquare = new ImageIcon("empty.png");
    private ImageIcon yellowSquare = new ImageIcon("selected.png");
    private ImageIcon redKing = new ImageIcon("red-king.png");
    private ImageIcon whiteKing = new ImageIcon("white-king.png");
    private int x;
    private int y;
    private Board board;
    public Piece piece;
    
    /**Constructor. Creates an instance of a Square
     * @param y y coordinate of a square
     * @param x x coordinate of a square
     * @param board uses board class
    */ 
    public Square(Board board, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.board = board;
        piece = Piece.None;
        addActionListener(this);
    }
    public void actionPerformed(ActionEvent e)
    {
        if(board.whoMoves)
        {
            //Selecting a square
            if(board.selectedSquare == null && (piece == Piece.White || piece == Piece.WhiteKing) && !board.existsAttackWhite())
            {
                yellowColor();//coloring available moves method
                if (board.yellowSquares.size() == 0)
                    return;
                board.selectedSquare = this;
            }
            //selecting a square if there exists an attack (attacking is mandatory)
            else if(canAttack(this) && (piece == Piece.White || piece == Piece.WhiteKing) && board.selectedSquare == null)
            {
                yellowColor();
                if(board.yellowSquares.size() == 0)
                    return;
                board.selectedSquare = this;
            }
            //unselecting our square we had selected(when we want to change our mind to move somewhere else)
            else if(board.selectedSquare == this && !board.attackAgain)
            {
                removeYellow();//removing yellow color
                board.selectedSquare = null;
            }
            //moving a selected square
            else if(board.selectedSquare != null && board.selectedSquare.canMoveTo(this))
            {
                removeYellow();
                board.selectedSquare.moveTo(this);
                //checks if the checker attacked other checker and removes it then
                if (Math.abs(board.selectedSquare.x - x) == 2 && Math.abs(board.selectedSquare.y - y) == 2)
                {
                    int xRemove = (board.selectedSquare.x + x) / 2;
                    int yRemove = (board.selectedSquare.y + y) / 2;
                    board.squares[yRemove][xRemove].setPiece(Piece.None);
                    if(canAttack(this))//checks for multiple attacks
                    {
                        board.selectedSquare = this;
                        yellowColor();
                        board.attackAgain = true;
                    }
                    else{
                        board.attackAgain = false;
                    }
                }
                //when move is finished sets back again selectedsquare to null
                if(!board.attackAgain)
                {
                    board.selectedSquare = null;
                    board.whoMoves = false;
                }
                board.detectionOfKings();//checks if there appeared any kings
            }
        }
        //the same events just for another checker
        else if(!board.whoMoves)
        {
            if(board.selectedSquare == null && (piece == Piece.Red || piece == Piece.RedKing) && !board.existsAttackRed())
            {
            yellowColor();
            if (board.yellowSquares.size() == 0)
                return;
            board.selectedSquare = this;
            }
            else if(canAttack(this) && (piece == Piece.Red || piece == Piece.RedKing) && board.selectedSquare == null)
            {
            yellowColor();
            if(board.yellowSquares.size() == 0)
                return;
            board.selectedSquare = this;
            }
            else if(board.selectedSquare == this && !board.attackAgain)
            {
            removeYellow();
            board.selectedSquare = null;
            }
            else if(board.selectedSquare != null && board.selectedSquare.canMoveTo(this))
            {
            removeYellow();
            board.selectedSquare.moveTo(this);
            if (Math.abs(board.selectedSquare.x - x) == 2 && Math.abs(board.selectedSquare.y - y) == 2)
            {
                int xRemove = (board.selectedSquare.x + x) / 2;
                int yRemove = (board.selectedSquare.y + y) / 2;
                board.squares[yRemove][xRemove].setPiece(Piece.None);
                if(canAttack(this))
                {
                    board.selectedSquare = this;
                    yellowColor();
                    board.attackAgain = true;
                }
                else{
                    board.attackAgain = false;
                }
            }
            if(!board.attackAgain)
            {
                board.selectedSquare = null;
                board.whoMoves = true;
            }
            board.detectionOfKings();
            }
        }
    }
    /**
     * method moves a checker to another square
     * @param destinantion paramater is another square where it takes the checker
     */
    public void moveTo(Square destination)
    {
        destination.setPiece(piece);
        setPiece(Piece.None);//takes out the piece where it was before
    }
    /**
     * Method that returns true or false if it can move to this square
     * @param destinantion Square that you want to move
     */
    public boolean canMoveTo(Square destination)
    {
        if (destination.piece != Piece.None)
            return false;
        return board.yellowSquares.contains(destination);
    }
    /**
     * Method that sets pieces.
     * @param piece choose a piece that you want to set
     */
    public void setPiece(Piece piece)
    {
        this.piece = piece;
        switch (piece)
        {
            case Red:
                setIcon(redChecker);
                break;
            case White:
                setIcon(whiteChecker);
                break;
            case RedKing:
                setIcon(redKing);
                break;
            case WhiteKing:
                setIcon(whiteKing);
                break;
            default:
                setIcon(whiteSquare);
                break;
        }
    }
    /**
     * colors available moves with yellow colors
     */
    public void yellowColor()
    {
        if(this.piece == Piece.White || this.piece == Piece.WhiteKing)
        {
            if(board.existsAttackWhite())//color moves if there are attacks
            {
                if(y > 1 && x < 6 && (board.squares[y-1][x+1].piece == Piece.Red || board.squares[y-1][x+1].piece == Piece.RedKing) && board.squares[y-2][x+2].piece == Piece.None)
                {
                    board.squares[y-2][x+2].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y-2][x+2]);
                }
                if(y > 1 && x > 1 && (board.squares[y-1][x-1].piece == Piece.Red || board.squares[y-1][x-1].piece == Piece.RedKing) && board.squares[y-2][x-2].piece == Piece.None)
                {
                    board.squares[y-2][x-2].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y-2][x-2]);
                }
                if(y < 6 && x > 1 && this.piece == Piece.WhiteKing && (board.squares[y+1][x-1].piece == Square.Piece.Red || board.squares[y+1][x-1].piece == Square.Piece.RedKing) && board.squares[y+2][x-2].piece == Square.Piece.None)
                {
                    board.squares[y+2][x-2].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y+2][x-2]);
                }
                if(y < 6 && x < 6 && this.piece == Piece.WhiteKing && (board.squares[y+1][x+1].piece == Square.Piece.Red || board.squares[y+1][x+1].piece == Square.Piece.RedKing) && board.squares[y+2][x+2].piece == Square.Piece.None)
                {
                    board.squares[y+2][x-2].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y+2][x-2]);
                }
            }
            else {//color moves if there arent any attacks
                if(y > 0 && x < 7 && board.squares[y-1][x+1].piece == Piece.None)
                {
                    board.squares[y-1][x+1].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y-1][x+1]);
                }
                if(y > 0 && x > 0 && board.squares[y-1][x-1].piece == Piece.None)
                {
                    board.squares[y-1][x-1].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y-1][x-1]);
                }
                if(y < 7 && x > 0 && this.piece == Piece.WhiteKing && board.squares[y+1][x-1].piece == Piece.None)
                {
                    board.squares[y+1][x-1].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y+1][x-1]);
                }
                if(y < 7 && x < 7 && this.piece == Piece.WhiteKing && board.squares[y+1][x+1].piece == Piece.None)
                {
                    board.squares[y+1][x+1].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y+1][x+1]);
                }
            }
        }
        //everything the same from commentation above just for red pieces
        else if(this.piece == Piece.Red || this.piece == Piece.RedKing)
        {
            if(board.existsAttackRed())
            {
                if(y < 6 && x > 1 && (board.squares[y+1][x-1].piece == Piece.White || board.squares[y+1][x-1].piece == Piece.WhiteKing) && board.squares[y+2][x-2].piece == Piece.None)
                {
                    board.squares[y+2][x-2].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y+2][x-2]);
                }
                if(y < 6 && x < 6 && (board.squares[y+1][x+1].piece == Piece.White || board.squares[y+1][x+1].piece == Piece.WhiteKing) && board.squares[y+2][x+2].piece == Piece.None)
                {
                    board.squares[y+2][x+2].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y+2][x+2]);
                }
                if(y > 1 && x < 6 && this.piece == Piece.RedKing && (board.squares[y-1][x+1].piece == Piece.White || board.squares[y-1][x+1].piece == Piece.WhiteKing) && board.squares[y-2][x+2].piece == Piece.None)
                {
                    board.squares[y-2][x+2].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y-2][x+2]);
                }
                if(y > 1 && x > 1 && this.piece == Piece.RedKing && (board.squares[y-1][x-1].piece == Piece.White || board.squares[y-1][x-1].piece == Piece.WhiteKing) && board.squares[y-2][x-2].piece == Piece.None)
                {
                    board.squares[y-2][x-2].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y-2][x-2]);
                }
            }
            else {
                if(y < 7 && x > 0 && board.squares[y+1][x-1].piece == Piece.None)
                {
                    board.squares[y+1][x-1].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y+1][x-1]);
                }
                if(y < 7 && x < 7 && board.squares[y+1][x+1].piece == Piece.None)
                {
                    board.squares[y+1][x+1].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y+1][x+1]);
                }
                if(y > 0 && x < 7 && this.piece == Piece.RedKing && board.squares[y-1][x+1].piece == Piece.None)
                {
                    board.squares[y-1][x+1].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y-1][x+1]);
                }
                if(y > 0 && x > 0 && this.piece == Piece.RedKing && board.squares[y-1][x-1].piece == Piece.None)
                {
                    board.squares[y-1][x-1].setIcon(yellowSquare);
                    board.yellowSquares.add(board.squares[y-1][x-1]);
                }
            }
        }
    }
    /**
     * removes yellow colors when the move has finished
     */
    public void removeYellow()
    {
        for (int i = 0; i < board.yellowSquares.size(); i++)
            board.yellowSquares.get(i).setIcon(whiteSquare);
        board.yellowSquares.clear();
    }
    /**
     * checks if you can attack with this square (returns true of false)
     * @param movableSquare takes a square you want to attack with.
     */
    public boolean canAttack(Square movableSquare)
    {
        if(y > 1 && x < 6 && (movableSquare.piece == Square.Piece.White || movableSquare.piece == Square.Piece.WhiteKing) && 
        (board.squares[y-1][x+1].piece == Square.Piece.Red || board.squares[y-1][x+1].piece == Square.Piece.RedKing) && board.squares[y-2][x+2].piece == Square.Piece.None)
            return true;
        if(y > 1 && x > 1 && (movableSquare.piece == Square.Piece.White || movableSquare.piece == Square.Piece.WhiteKing) && 
        (board.squares[y-1][x-1].piece == Square.Piece.Red || board.squares[y-1][x-1].piece == Square.Piece.RedKing) && board.squares[y-2][x-2].piece == Square.Piece.None)
            return true;
        if(y < 6 && x > 1 && (movableSquare.piece == Square.Piece.Red || movableSquare.piece == Square.Piece.RedKing) && 
        (board.squares[y+1][x-1].piece == Square.Piece.White || board.squares[y+1][x-1].piece == Square.Piece.WhiteKing) && board.squares[y+2][x-2].piece == Square.Piece.None)
            return true;
        if(y < 6 && x < 6 && (movableSquare.piece == Square.Piece.Red || movableSquare.piece == Square.Piece.RedKing) && 
        (board.squares[y+1][x+1].piece == Square.Piece.White || board.squares[y+1][x+1].piece == Square.Piece.WhiteKing) && board.squares[y+2][x+2].piece == Square.Piece.None)
            return true;
        if(y < 6 && x > 1 && movableSquare.piece == Square.Piece.WhiteKing && (board.squares[y+1][x-1].piece == Square.Piece.Red || board.squares[y+1][x-1].piece == Square.Piece.RedKing) && board.squares[y+2][x-2].piece == Square.Piece.None)
            return true;
        if(y < 6 && x < 6 && movableSquare.piece == Square.Piece.WhiteKing && (board.squares[y+1][x+1].piece == Square.Piece.Red || board.squares[y+1][x+1].piece == Square.Piece.RedKing) && board.squares[y+2][x+2].piece == Square.Piece.None)
            return true;
        if(y > 1 && x < 6 && movableSquare.piece == Square.Piece.RedKing && (board.squares[y-1][x+1].piece == Square.Piece.White || board.squares[y-1][x+1].piece == Square.Piece.WhiteKing) && board.squares[y-2][x+2].piece == Square.Piece.None)
            return true;
        if(y > 1 && x > 1 && movableSquare.piece == Square.Piece.RedKing && (board.squares[y-1][x-1].piece == Square.Piece.White || board.squares[y-1][x-1].piece == Square.Piece.WhiteKing) && board.squares[y-2][x-2].piece == Square.Piece.None)
            return true;
        return false;
    }
}