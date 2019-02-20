package com.company;


import java.util.Arrays;


enum Piece{
    N, X, O;

    @Override
    public String toString() {
        return name();
    }
}

public class Board {
    private final int rows = 6;
    private final int cols = 7;

    private Piece[][] state;
    private Piece turn;
    private int[] piecesPerCol;
    private boolean end;
    private Piece winner;

    public Board(){
        this.piecesPerCol = new int[cols];

        this.state = new Piece[rows][cols]; /* 6 rows, 7 columns */
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                this.state[row][col] = Piece.N;
            }
        }

        this.turn = Piece.O;
        this.end = false;
        this.winner = Piece.N;
    }

    public Board(String str_state){
        String[] arr_state = str_state.split(","); // Create array of characters
        assert arr_state.length == 42: "str_state not 42 characters"; // Length of array must be 42

        int turn_count = 0; // Used to calculate whose turn it is
        int arr_index = 0; // Used to keep track of arr_state index

        this.piecesPerCol = new int[cols];
        this.state = new Piece[rows][cols]; /* 6 rows, 7 columns */

        /* Set board state */
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                Piece piece = Piece.valueOf(arr_state[arr_index]);
                this.state[row][col] = piece;

                /* Increase piecesPerCol if this is not empty spot */
                if(piece != Piece.N){
                    this.piecesPerCol[col] = row + 1;
                }

                /* Calculate turn */
                if(piece == Piece.O){
                    turn_count++;
                }
                if(piece == Piece.X){
                    turn_count--;
                }

                /* Move to next input index*/
                arr_index++;
            }
        }

        /* Set turn value */
        assert (turn_count == 0 || turn_count == 1): "Turn count was not 0 or 1";

        if(turn_count == 0){
            this.turn = Piece.O;
        }
        else{
            this.turn = Piece.X;
        }

        /* Verify board state */
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                Piece current_piece = this.state[row][col];
                assert current_piece == Piece.N || (row == 0 || this.state[row - 1][col] != Piece.N) : "String state failed verification";
            }
        }

        if(this.isEnd()){
            System.out.println(this.toString());
            throw new IllegalArgumentException("Game already complete");
        }
    }

    @Override
    public String toString() { /* For viewing */
        StringBuilder sb = new StringBuilder();
        for(int row = rows-1; row >= 0; row--){
            sb.append("[ ");
            for(int col = 0; col < cols; col++){
                sb.append(state[row][col]);
                sb.append(" ");
            }
            sb.append("]\n");
        }
        sb.append("Current turn: ");
        sb.append(turn.toString());
        sb.append("\n");
        return sb.toString();
    }

    public String getState(){ /* For saving state */
        StringBuilder sb = new StringBuilder();
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                sb.append(state[row][col]);
                sb.append(",");
            }
        }
        sb.deleteCharAt(sb.lastIndexOf(","));

        assert sb.length() == 83: "GetState - return length is not 83";

        return sb.toString();
    }

    public String getPiecesPerCol(){ /* For viewing */
        return Arrays.toString(piecesPerCol);
    }

    public boolean getEnd(){
        return end;
    }

    public Piece getWinner(){
        return winner;
    }

    private boolean canPlace(int col){
        return (!end && col >= 0 && col < cols && piecesPerCol[col] < rows);
    }

    public boolean place(int col){
        boolean ret = canPlace(col);
        if(ret){
            insert(col);
            isEnd_Turn(col);
        }

        return ret;
    }

    private void insert(int col){
        Piece nextTurn;
        Piece thisTurn;
        if(turn == Piece.O){
            thisTurn = Piece.O;
            nextTurn = Piece.X;
        }
        else{
            thisTurn = Piece.X;
            nextTurn = Piece.O;
        }

        state[piecesPerCol[col]][col] = thisTurn;
        piecesPerCol[col]++;
        turn = nextTurn;
    }

    /* Checks if game ended and calculates winner using entire board */
    private boolean isEnd(){
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                if(state[row][col] == Piece.N){
                    break;
                }
                boolean checkPass;

                //Right
                if(col + 3 < cols){
                    checkPass = true;
                    for(int i = col; i <= col + 3; i++){
                        if(state[row][i] != state[row][col]){
                            checkPass = false;
                            break;
                        }
                    }
                    if(checkPass){
                        winner = state[row][col];
                        end = true;
                        System.out.println("Right: " + row + "," + col);
                        return true;
                    }
                }

                //Up
                if(row + 3 < rows){
                    checkPass = true;
                    for(int i = row; i <= row + 3; i++){
                        if(state[i][col] != state[row][col]){
                            checkPass = false;
                            break;
                        }
                    }
                    if(checkPass){
                        winner = state[row][col];
                        end = true;
                        System.out.println("Up: " + row + "," + col);
                        return true;
                    }
                }

                //Left Up
                if(row + 3 < rows && col - 3 >= 0){
                    checkPass = true;
                    for(int i=1; i < 4; i++){
                        if(state[row+i][col-i] != state[row][col]){
                            checkPass = false;
                            break;
                        }
                    }
                    if(checkPass){
                        winner = state[row][col];
                        end = true;
                        System.out.println("Left Up: " + row + "," + col);
                        return true;
                    }
                }
                //Right Up
                if(row + 3 < rows && col + 3 < cols){
                    checkPass = true;
                    for(int i=1; i < 4; i++){
                        if(state[row+i][col+i] != state[row][col]){
                            checkPass = false;
                            break;
                        }
                    }
                    if(checkPass){
                        winner = state[row][col];
                        end = true;
                        System.out.println("Right Up: " + row + "," + col);
                        return true;
                    }
                }
            }
        }

        winner = Piece.N;
        return false;
    }

    /* Checks if game ended and calculates winner using last turn */
    private void isEnd_Turn(int col){
        /* Game is always over if there are no more moves to make */
        end = true;
        for(int i: piecesPerCol){
            if(i < 6){
                end = false;
                break;
            }
        }


        int row = piecesPerCol[col] - 1;
        boolean checkPass;


        // Count pieces to Left and Right
        int lr_total = 1;

        //Right
        for(int i = col + 1; i < cols; i++){
            if(state[row][i] != state[row][col]){
                break;
            }
            else{
                lr_total++;
            }
        }

        //Left
        for(int i = col - 1; i >= 0; i--){
            if(state[row][i] != state[row][col]){
                break;
            }
            else{
                lr_total++;
            }
        }

        if(lr_total >= 4){
            winner = state[row][col];
            end = true;
            return;
        }


        //Down
        int d_total = 1;
        if(row - 3 >= 0) {
            for (int i = row - 1; i >= 0; i--) {
                if (state[i][col] != state[row][col]) {
                    break;
                } else {
                    d_total++;
                }
            }
        }
        if(d_total >= 4){
            winner = state[row][col];
            end = true;
            return;
        }

        //Top Left to Bottom Right diagonal
        int tlbr_total = 1;

        //To Top Left
        for(int i = 1; i < 4; i++){
            if(row + i < rows && col - i >= 0){
                if(state[row+i][col-i] != state[row][col]) {
                    break;
                }
                else {
                    tlbr_total++;
                }
            }
            else{
                break;
            }
        }


        //To Bottom Right
        for(int i = 1; i < 4; i++){
            if(row - i >= 0 && col + i < cols){
                if(state[row-i][col+i] != state[row][col]) {
                    break;
                }
                else {
                    tlbr_total++;
                }
            }
            else{
                break;
            }
        }

        if(tlbr_total >= 4){
            winner = state[row][col];
            end = true;
            return;
        }

        //Top Right to Bottom Left Diagonal
        int trbl_total = 1;

        //To Top Right
        for(int i = 1; i < 4; i++){
            if(row + i < rows && col + i < cols){
                if(state[row+i][col+i] != state[row][col]) {
                    break;
                }
                else {
                    trbl_total++;
                }
            }
            else{
                break;
            }
        }


        //To Bottom Left
        for(int i = 1; i < 4; i++){
            if(row - i >= 0 && col - i >= 0){
                if(state[row-i][col-i] != state[row][col]) {
                    break;
                }
                else {
                    trbl_total++;
                }
            }
            else{
                break;
            }
        }

        if(trbl_total >= 4){
            winner = state[row][col];
            end = true;
            return;
        }

    }
}
