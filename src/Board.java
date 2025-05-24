public class Board {
    private long whitePawns,whiteHorses,whiteBishops,whiteRooks,whiteQueens,whiteKing;
    private long blackPawns,blackHorses,blackBishops,blackRooks,blackQueens,blackKing;
    private int whitePoints;
    private int blackPoints;
    private DisplayManager window;
    public int[][] knightAllowedJumps = {
            {-1,-2},{-1,2},
            {1,-2} ,{1,2},
            {-2,-1},{-2,1},
            {2,-1} ,{2,1}
    },
    kingAllowedMoves = {
            {-1,1}, {0,1} ,{1,1},
            {-1,0}, {1,0} ,
            {-1,-1},{0,-1},{1,-1}
    };
    public Board(){
        window = new DisplayManager(this);
        whitePoints = 0;
        blackPoints = 0;
        whitePawns = 0L;
        blackPawns = 0L;
        for(char i=0;i<8;i++){
            placePiece("WP", i, 2);
            placePiece("BP", i, 7);
        }

        //White
        whiteHorses = 0L;
        placePiece("WH",2,1);
        placePiece("WH",7,1);
        
        whiteBishops = 0L;
        placePiece("WB",3,1);
        placePiece("WB",6,1);
        
        whiteRooks = 0L;
        placePiece("WR",1,1);
        placePiece("WR",8,1);
        
        whiteQueens = 0L;
        placePiece("WQ",4,1);
        
        whiteKing = 0L;
        placePiece("WK",5,1);

        // Black
        blackHorses = 0L;
        placePiece("BH",2,8);
        placePiece("BH",7,8);

        blackBishops = 0L;
        placePiece("BB",3,8);
        placePiece("BB",6,8);

        blackRooks = 0L;
        placePiece("BR",1,8);
        placePiece("BR",8,8);

        blackQueens = 0L;
        placePiece("BQ",4,8);

        blackKing = 0L;
        placePiece("BK",5,8);
    }

    public void displayBoard(){
        for(int i = 8;i >= 1; i--) {
            for (int j = 1; j <= 8  ; j++){
                char piece;
                switch(getPieceAt(j,i)){
                    case "WP": piece = '♙';
                                break;
                    case "WH": piece = '♘';
                                break;
                    case "WB": piece = '♗';
                                break;
                    case "WR": piece = '♖';
                                break;
                    case "WQ": piece = '♕';
                                break;
                    case "WK": piece = '♔';
                                break;
                    case "BP": piece = '♟';
                                break;
                    case "BH": piece = '♞';
                                break;
                    case "BB": piece = '♝';
                                break;
                    case "BR": piece = '♜';
                                break;
                    case "BQ": piece = '♛';
                                break;
                    case "BK": piece = '♚';
                                break;
                    default:   piece = ' ';
                }

                System.out.print(piece + " ");
            }
            System.out.println();
        }
    }
    
    public boolean movePiece(int currentColumn, int currentRow, int targetColumn, int targetRow){
        String piece = getPieceAt(currentColumn,currentRow);
        if (targetRow < 1 || targetRow > 8 || targetColumn < 1 || targetColumn > 8 ||
            currentRow < 1 || currentRow > 8 || currentColumn < 1 || currentColumn > 8 ||
            getPieceAt(targetColumn,targetRow).charAt(0) == getPieceAt(currentColumn, currentRow).charAt(0) ||
            !Rules.isPieceAllowedToMoveAt(this,currentColumn, currentRow, targetColumn, targetRow)){
            //Log error
            return false;
        }

        if(piece.equals("WP") || piece.equals("BP") && targetRow == 1 || targetRow == 8) {
            removePiece(currentColumn, currentRow);
            if(piece.equals("WP"))
                whitePoints += getPiecePoints(getPieceAt(targetColumn, targetRow));
            else
                blackPoints += getPiecePoints(getPieceAt(targetColumn, targetRow));
            removePiece(targetColumn, targetRow);
            placePiece(piece.charAt(0) + "Q", targetColumn, targetRow);
        }

        removePiece(currentColumn, currentRow);
        //Removed the piece from currentColumn, currentRow
        whitePoints += getPiecePoints(getPieceAt(targetColumn, targetRow));
        removePiece(targetColumn,targetRow);
        placePiece(piece,targetColumn,targetRow);
        //Placed the piece to targetColumn, targetRow

        displayBoard();
        return true;
    }

    protected boolean isSquareUnderCheck(Character color, int column, int row){
        for (int i = 0; i < 64; i++) {
            if (getPieceAt(i/8 + 1,i%8 + 1).charAt(0) != color &&
                Rules.isPieceAllowedToMoveAt(this,i/8 + 1,i%8 + 1,-column,row))
                 return true;
        }
        return false;
    }

    public void clearBoard(){
        whitePawns=whiteHorses=whiteBishops=whiteRooks=whiteQueens=whiteKing=
                blackPawns=blackHorses=blackBishops=blackRooks=blackQueens=blackKing=0L;
        whitePoints=blackPoints=0;
    }
    private int getPiecePoints(String piece){
        return switch (piece) {
            case " " -> 0;
            case "WP", "BP" -> 1;
            case "WH", "WB", "BH", "BB" -> 3;
            case "WR", "BR" -> 5;
            case "WQ", "BQ" -> 9;
            default -> 0;
        };
    }
    protected void removePiece(int column, int row){
        switch(getPieceAt(column, row)){
            case "WP": whitePawns = whitePawns&(~(1L <<coordsShiftBits(column, row)));
                break;
            case "WH": whiteHorses = whiteHorses&(~(1L <<coordsShiftBits(column, row)));
                break;
            case "WB": whiteBishops = whiteBishops&(~(1L <<coordsShiftBits(column, row)));
                break;
            case "WR": whiteRooks = whiteRooks&(~(1L <<coordsShiftBits(column, row)));
                break;
            case "WQ": whiteQueens = whiteQueens&(~(1L <<coordsShiftBits(column, row)));
                break;
            case "WK": whiteKing = whiteKing&(~(1L <<coordsShiftBits(column, row)));
                break;
            case "BP": blackPawns = blackPawns&(~(1L <<coordsShiftBits(column, row)));
                break;
            case "BH": blackHorses = blackHorses&(~(1L <<coordsShiftBits(column, row)));
                break;
            case "BB": blackBishops = blackBishops&(~(1L <<coordsShiftBits(column, row)));
                break;
            case "BR": blackRooks = blackRooks&(~(1L <<coordsShiftBits(column, row)));
                break;
            case "BQ": blackQueens = blackQueens&(~(1L <<coordsShiftBits(column, row)));
                break;
            case "BK": blackKing = blackKing&(~(1L <<coordsShiftBits(column, row)));
                break;
            default:
        }
        window.update();
    }
    protected void placePiece(String piece,int column, int row){
        switch(piece){
            case "WP": whitePawns = whitePawns|(1L <<coordsShiftBits(column, row));
                break;
            case "WH": whiteHorses = whiteHorses|(1L <<coordsShiftBits(column, row));
                break;
            case "WB": whiteBishops = whiteBishops|(1L <<coordsShiftBits(column, row));
                break;
            case "WR": whiteRooks = whiteRooks|(1L <<coordsShiftBits(column, row));
                break;
            case "WQ": whiteQueens = whiteQueens|(1L <<coordsShiftBits(column, row));
                break;
            case "WK": whiteKing = whiteKing|(1L <<coordsShiftBits(column, row));
                break;
            case "BP": blackPawns = blackPawns|(1L <<coordsShiftBits(column, row));
                break;
            case "BH": blackHorses = blackHorses|(1L <<coordsShiftBits(column, row));
                break;
            case "BB": blackBishops = blackBishops|(1L <<coordsShiftBits(column, row));
                break;
            case "BR": blackRooks = blackRooks|(1L <<coordsShiftBits(column, row));
                break;
            case "BQ": blackQueens = blackQueens|(1L <<coordsShiftBits(column, row));
                break;
            case "BK": blackKing = blackKing|(1L <<coordsShiftBits(column, row));
                break;
            default:
        }
        window.update();
    }
    protected String getPieceAt(int column, int row){
        
        if((whitePawns & (1L <<coordsShiftBits(column,row))) != 0)
            return "WP";
        else if((whiteHorses & (1L <<coordsShiftBits(column,row))) != 0)
            return "WH";
        else if((whiteBishops & (1L <<coordsShiftBits(column,row))) != 0)
            return "WB";
        else if((whiteRooks & (1L <<coordsShiftBits(column,row))) != 0)
            return "WR";
        else if((whiteQueens & (1L <<coordsShiftBits(column,row))) != 0)
            return "WQ";
        else if((whiteKing & (1L <<coordsShiftBits(column,row))) != 0)
            return "WK";
        else if((blackPawns & (1L <<coordsShiftBits(column,row))) != 0)
            return "BP";
        else if((blackHorses & (1L <<coordsShiftBits(column,row))) != 0)
            return "BH";
        else if((blackBishops & (1L <<coordsShiftBits(column,row))) != 0)
            return "BB";
        else if((blackRooks & (1L <<coordsShiftBits(column,row))) != 0)
            return "BR";
        else if((blackQueens & (1L <<coordsShiftBits(column,row))) != 0)
            return "BQ";
        else if((blackKing & (1L <<coordsShiftBits(column,row))) != 0)
            return "BK";

        return " ";
    }
    protected boolean isKingUnderCheck(String color){
        for (int i = 0; i < 64; i++)
            if(getPieceAt(i/8+1,i%8+1).length() > 1 && getPieceAt(i/8+1,i%8+1).equals(color+"K")){
                for (int j = 0; j < 64; j++)
                    if (!getPieceAt(j/8+1,j%8+1).equals(" ") &&
                        getPieceAt(j/8+1,j%8+1).charAt(0) != color.charAt(0) &&
                        Rules.isPieceAllowedToMoveAt(this,j/8+1,j%8+1,i/8+1,-(i%8+1)))
                        return true;
                i=64;
            }
        return false;
    }
    private char coordsShiftBits(int column, int row){
        return (char) ((column - 1) * 8 + row -1);
    }


}
