import javax.swing.*;

public class DisplayManager{
    private JFrame frame;
    private ChessBoardPanel boardPanel;
    private String[] pieces;
    private Board chessBoard;
    private int lastClickedColumn, lastClickedRow;
    private char lastMove;

    public DisplayManager(Board chessBoard) {
        lastClickedRow = lastClickedColumn = -1;
        lastMove = 'B';
        pieces = new String[64];
        this.chessBoard = chessBoard;
        for (int i = 0; i < 64; i++) {
            pieces[i] = chessBoard.getPieceAt(i/8 + 1,i%8 + 1);
        }
        boardPanel = new ChessBoardPanel(pieces,this);

        frame = new JFrame("Chess Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(boardPanel);
        frame.pack();                 // frame size = panel's preferred size (640×640)
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void update(){
        for (int i = 0; i < 64; i++) {
            pieces[i] = chessBoard.getPieceAt(i/8 + 1,i%8 + 1);
        }
        boardPanel.updatePieces(pieces);
    }

    public void clickedSquare(int column, int row){
        if( chessBoard.getPieceAt(lastClickedColumn,lastClickedRow).charAt(0) != chessBoard.getPieceAt(column,row).charAt(0)
            && !chessBoard.getPieceAt(lastClickedColumn,lastClickedRow).equals(" ") &&
            chessBoard.getPieceAt(lastClickedColumn,lastClickedRow).charAt(0) != lastMove) {

            boolean result = chessBoard.movePiece(lastClickedColumn, lastClickedRow, column, row);
            if(lastMove == 'B' && result)
                lastMove = 'W';
            else if(result)
                lastMove = 'B';
        }

        lastClickedColumn = column;
        lastClickedRow = row;
        update();
    }

}
