package com.company;


import java.util.ArrayList;
import java.util.HashMap;


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

        this.isEnd();
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

    public int[] getPiecesPerCol() {
        return piecesPerCol;
    }

    public boolean getEnd(){
        return end;
    }

    public Piece getTurn(){
        return turn;
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
                        //System.out.println("Right: " + row + "," + col);
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
                        //System.out.println("Up: " + row + "," + col);
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
                        //System.out.println("Left Up: " + row + "," + col);
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
                        //System.out.println("Right Up: " + row + "," + col);
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

    public int[] getBoardFeatures(Piece player) {
        /*
        Opp Match 1 with empty spots (Active/Inactive) --> For each piece find 3 N's
        Opp Match 2 with empty spots (Active/Inactive) --> For each piece find 2 N's and an additional piece
        Opp Match 3 with empty spots (Active/Inactive) --> Group of 5 [!(P or N) (3 P's and 1 N)]
        Opp Match 3 with 2 empty spots (Active/Inactive) --> Group of 5 [N P P P N]
        self Match 1 with empty spots (Active/Inactive)
        self Match 2 with empty spots (Active/Inactive)
        self Match 3 with empty spot (Active/Inactive)
        self Match 3 with 2 empty spots (Active/Inactive)
        self Game Won [P P P P]
         */

        /*
        "L/R" = 42
        "U/D" = 21
        "Diagonal1" = 18
        "Diagonal2" = 18
        Total = 99
        */

        Piece opponent;

        if (player == Piece.O) {
            opponent = Piece.X;
        } else {
            opponent = Piece.O;
        }

        int[] boardFeatures = new int[17];

        HashMap<String, ArrayList<String>> featureStrings = getFeatureStrings();
        for (String s : featureStrings.get("Active")) {
            if (s.length() == 5) {
                if (count(s, opponent) == 3) {
                    boardFeatures[6] = boardFeatures[6] + 1;
                } else {
                    boardFeatures[14] = boardFeatures[14] + 1;
                }
            } else {
                if (s.contains(opponent.name())) {
                    int num = count(s, opponent);
                    switch (num) {
                        case 1:
                            boardFeatures[0] = boardFeatures[0] + 1;
                            break;

                        case 2:
                            boardFeatures[2] = boardFeatures[2] + 1;
                            break;

                        case 3:
                            boardFeatures[4] = boardFeatures[4] + 1;
                            break;
                    }
                } else {
                    int num = count(s, player);
                    switch (num) {
                        case 1:
                            boardFeatures[8] = boardFeatures[8] + 1;
                            break;

                        case 2:
                            boardFeatures[10] = boardFeatures[10] + 1;
                            break;

                        case 3:
                            boardFeatures[12] = boardFeatures[12] + 1;
                            break;

                        case 4:
                            boardFeatures[16] = 1;
                            break;
                    }
                }
            }
        }

        for (String s : featureStrings.get("Inactive")) {
            if (s.length() == 5) {
                if (count(s, opponent) == 3) {
                    boardFeatures[7] = boardFeatures[7] + 1;
                } else {
                    boardFeatures[15] = boardFeatures[15] + 1;
                }
            } else {
                if (s.contains(opponent.name())) {
                    int num = count(s, opponent);
                    switch (num) {
                        case 1:
                            boardFeatures[1] = boardFeatures[1] + 1;
                            break;

                        case 2:
                            boardFeatures[3] = boardFeatures[3] + 1;
                            break;

                        case 3:
                            boardFeatures[5] = boardFeatures[5] + 1;
                            break;
                    }
                } else {
                    int num = count(s, player);
                    switch (num) {
                        case 1:
                            boardFeatures[9] = boardFeatures[9] + 1;
                            break;

                        case 2:
                            boardFeatures[11] = boardFeatures[11] + 1;
                            break;

                        case 3:
                            boardFeatures[13] = boardFeatures[13] + 1;
                            break;

                        case 4:
                            boardFeatures[16] = 1;
                            break;
                    }
                }
            }
        }

        return boardFeatures;
    }

    private HashMap<String, ArrayList<String>> getFeatureStrings() {
        HashMap<String, ArrayList<String>> featureStrings = new HashMap<>();
        ArrayList<String> activeStrings = new ArrayList<>();
        ArrayList<String> inactiveStrings = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                boolean col_pos_4 = (col + 3 < cols);
                boolean col_pos_5 = (col + 4 < cols);
                boolean col_neg_4 = (col - 3 >= 0);
                boolean col_neg_5 = (col - 4 >= 0);
                boolean row_pos_4 = (row + 3 < rows);
                boolean row_pos_5 = (row + 4 < rows);

                //Right 4
                boolean isActive = false;
                StringBuilder sb = new StringBuilder();
                if (col_pos_4) {
                    for (int i = 0; i < 4; i++) {
                        if (piecesPerCol[col + i] == row) {
                            isActive = true;
                        }
                        sb.append(state[row][col + i]);
                    }
                    if (isActive) {
                        activeStrings.add(sb.toString());
                    } else {
                        inactiveStrings.add(sb.toString());
                    }
                }

                //Right 5
                if (col_pos_5) {
                    if (piecesPerCol[col + 4] == row) {
                        isActive = true;
                    }
                    sb.append(state[row][col + 4]);
                    if (isActive) {
                        activeStrings.add(sb.toString());
                    } else {
                        inactiveStrings.add(sb.toString());
                    }
                }

                //Up 4
                isActive = false;
                sb = new StringBuilder();
                if (row_pos_4) {
                    for (int i = 0; i < 4; i++) {
                        if (piecesPerCol[col] == row + i) {
                            isActive = true;
                        }
                        sb.append(state[row + i][col]);
                    }
                    if (isActive) {
                        activeStrings.add(sb.toString());
                    } else {
                        inactiveStrings.add(sb.toString());
                    }
                }

                //Up 5
                if (row_pos_5) {
                    if (piecesPerCol[col] == row + 4) {
                        isActive = true;
                    }
                    sb.append(state[row + 4][col]);
                    if (isActive) {
                        activeStrings.add(sb.toString());
                    } else {
                        inactiveStrings.add(sb.toString());
                    }
                }

                //Right-Up 4
                isActive = false;
                sb = new StringBuilder();
                if (col_pos_4 && row_pos_4) {
                    for (int i = 0; i < 4; i++) {
                        if (piecesPerCol[col + i] == row + i) {
                            isActive = true;
                        }
                        sb.append(state[row + i][col + i]);
                    }
                    if (isActive) {
                        activeStrings.add(sb.toString());
                    } else {
                        inactiveStrings.add(sb.toString());
                    }
                }

                //Right-Up 5
                if (col_pos_5 && row_pos_5) {
                    if (piecesPerCol[col + 4] == row + 4) {
                        isActive = true;
                    }
                    sb.append(state[row + 4][col + 4]);
                    if (isActive) {
                        activeStrings.add(sb.toString());
                    } else {
                        inactiveStrings.add(sb.toString());
                    }
                }

                //Left-Up 4
                isActive = false;
                sb = new StringBuilder();
                if (col_neg_4 && row_pos_4) {
                    for (int i = 0; i < 4; i++) {
                        if (piecesPerCol[col - i] == row - i) {
                            isActive = true;
                        }
                        sb.append(state[row + i][col - i]);
                    }
                    if (isActive) {
                        activeStrings.add(sb.toString());
                    } else {
                        inactiveStrings.add(sb.toString());
                    }
                }

                //Left-Up 5
                if (col_neg_5 && row_pos_5) {
                    if (piecesPerCol[col - 4] == row + 4) {
                        isActive = true;
                    }
                    sb.append(state[row + 4][col - 4]);
                    if (isActive) {
                        activeStrings.add(sb.toString());
                    } else {
                        inactiveStrings.add(sb.toString());
                    }
                }

            }
        }

        ArrayList<String> inactiveFeatureStrings = collectFeatureStrings(inactiveStrings);
        ArrayList<String> activeFeatureStrings = collectFeatureStrings(activeStrings);

        featureStrings.put("Inactive", inactiveFeatureStrings);
        featureStrings.put("Active", activeFeatureStrings);

        return featureStrings;
    }

    private ArrayList<String> collectFeatureStrings(ArrayList<String> allStrings) {
        ArrayList<String> featureStrings = new ArrayList<>();
        for (String s : allStrings) {
            if (s.length() == 4) {
                if (s.contains(Piece.O.name()) ^ s.contains(Piece.X.name())) {
                    featureStrings.add(s);
                }
            }
            if (s.length() == 5) {
                int o_inner_count = 0;
                int x_inner_count = 0;
                int o_count = 0;
                int x_count = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == 'O') {
                        o_count++;
                        if (i > 0 && i < s.length() - 1) {
                            o_inner_count++;
                        }
                    }
                    if (s.charAt(i) == 'X') {
                        if (i > 0 && i < s.length() - 1) {
                            x_inner_count++;
                        }
                        x_count++;
                    }
                }

                /* Must have a match 3, Inner count must not contain both O and X, Outer character could prevent, Must have O or X in string */
                if ((x_inner_count == 3 && o_count == 0) || (o_inner_count == 3 && x_count == 0)) {
                    featureStrings.add(s);
                }
            }
        }
        return featureStrings;
        //return new ArrayList<>();
    }

    private int count(String s, Piece p) {
        int num = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == p.name().charAt(0)) {
                num++;
            }
        }
        //System.out.println("String: " + s + ", Character: " + p.name().charAt(0) + ", Count: " + num);
        return num;
    }
}
