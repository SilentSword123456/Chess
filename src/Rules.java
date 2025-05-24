public class Rules {

    public static boolean isPieceAllowedToMoveAt(Board board, int currentColumn, int currentRow, int targetColumn, int targetRow){
        String piece = board.getPieceAt(currentColumn,currentRow);
        boolean isCalledFromCheckMethod = false;
        if(targetColumn<0){
            isCalledFromCheckMethod = true;
            targetColumn *= -1;
        }
        boolean isCalledFromKingMethod = false;
        if(targetRow<0){
            isCalledFromKingMethod = true;
            targetRow *= -1;
        }

        if (!isCalledFromKingMethod && board.isKingUnderCheck(String.valueOf(piece.charAt(0))) &&
                board.getPieceAt(currentColumn,currentRow).length() > 1 &&
                board.getPieceAt(currentColumn,currentRow).charAt(1) != 'K')
            return false;
        if(piece.equals("WK") || piece.equals("BK")) {
            return canKingMoveAt(currentColumn, currentRow, targetColumn, targetRow, isCalledFromCheckMethod, board, piece);
        }

        if(piece.equals("WP")){
            if((targetColumn == currentColumn - 1 || targetColumn == currentColumn + 1) &&
                    targetRow == currentRow + 1 && !board.getPieceAt(targetColumn, targetRow).equals(" ")){
                //Add en' passant rule
                return true;
            }
            if(targetColumn == currentColumn && (targetRow == currentRow + 1 || targetRow == 4) && board.getPieceAt(targetColumn, targetRow).equals(" "))
                return true;
        }

        if(piece.equals("BP")){
            if((targetColumn == currentColumn - 1 || targetColumn == currentColumn + 1) &&
                    targetRow == currentRow - 1 && !board.getPieceAt(targetColumn, targetRow).equals(" ")){
                //Add en' passant rule
                return true;
            }
            if(targetColumn == currentColumn && (targetRow == currentRow - 1 || targetRow == 5) && board.getPieceAt(targetColumn, targetRow).equals(" "))
                return true;
        }

        if(piece.equals("WH") || piece.equals("BH")){
            for (int i = 0; i < 8; i++)
                if (currentColumn + board.knightAllowedJumps[i][0] == targetColumn &&
                        currentRow + board.knightAllowedJumps[i][1] == targetRow)
                    return true;
        }
        if(piece.equals("WB") || piece.equals("BB") || piece.equals("WQ") || piece.equals("BQ")){
            int columnModifier = 1,rowModifier = 1,nextColumn,nextRow;

            if(targetColumn < currentColumn)
                columnModifier = - 1;
            if(targetRow < currentRow)
                rowModifier = -1;


            nextColumn = currentColumn + columnModifier;
            nextRow = currentRow + rowModifier;
            while (board.getPieceAt(nextColumn, nextRow).equals(" ") &&
                    nextColumn >= 1 && nextColumn <= 8 && nextRow >= 1 && nextRow <= 8 &&
                    nextColumn != targetColumn && nextRow != targetRow){
                nextColumn += columnModifier;
                nextRow += rowModifier;
            }

            if(nextColumn >= 1 && nextColumn <= 8 && nextRow >= 1 && nextRow <= 8 && nextColumn == targetColumn && nextRow == targetRow)
                return true;
        }

        if(piece.equals("WR") || piece.equals("BR") || piece.equals("WQ") || piece.equals("BQ")){
            if ( !(currentColumn == targetColumn || currentRow == targetRow))
                return false;

            int columnModifier = 1,rowModifier = 0,nextColumn,nextRow;
            if(currentColumn==targetColumn) {
                columnModifier = 0;
                rowModifier = 1;
                if(targetRow < currentRow)
                    rowModifier = -1;

            }
            else if(currentColumn > targetColumn)
                columnModifier = -1;

            nextColumn = currentColumn + columnModifier;
            nextRow = currentRow + rowModifier;
            while (board.getPieceAt(nextColumn, nextRow).equals(" ") &&
                    nextColumn >= 1 && nextColumn <= 8 && nextRow >= 1 && nextRow <= 8 &&
                    (nextColumn != targetColumn || columnModifier == 0) &&
                    (nextRow != targetRow || rowModifier == 0)){
                nextColumn += columnModifier;
                nextRow += rowModifier;
            }

            if(nextColumn >= 1 && nextColumn <= 8 && nextRow >= 1 && nextRow <= 8 && nextColumn == targetColumn && nextRow == targetRow)
                return true;
        }

        return false;
    }

    private static boolean canKingMoveAt(int currentColumn,int currentRow,int targetColumn,int targetRow,
                                         boolean isCalledFromCheckMethod,Board board, String piece){
        for (int i = 0; i < 8; i++) {
            if (targetColumn == currentColumn + board.kingAllowedMoves[i][0] && targetRow == currentRow + board.kingAllowedMoves[i][1]){

                if(isCalledFromCheckMethod)
                    return true;
                String tempPiece = board.getPieceAt(targetColumn,targetRow);
                board.removePiece(targetColumn,targetRow);
                board.removePiece(currentColumn,currentRow);
                board.placePiece(piece,targetColumn,targetRow);

                boolean a = board.isSquareUnderCheck(piece.charAt(0),targetColumn,targetRow);
                board.removePiece(targetColumn,targetRow);
                board.placePiece(tempPiece,targetColumn,targetRow);
                board.placePiece(piece,currentColumn,currentRow);
                return !a;
            }
        }
        return false;
    }
}
