import java.util.Scanner;
public class App {
    public static void main(String[] args) throws Exception {
        GameState game = new GameState();
        boolean appRuning = true;
        do{
            int op=0;
            System.out.print("\n___________________\n\n");
            System.out.println("1. Player Vs Player\n2. Player Vs Computer\n0. Exit");
            op = game.scanner.nextInt();
            switch (op){
                case 1:
                    game.startGame(false);
                    break;
                case 2:
                    game.startGame(true);
                    break;
                case 0:
                    appRuning = false;
                    break;
            }
        } while (appRuning);
    }
}
class BoardState{
    public Tile[][] board = new Tile[3][3];
    public void printBoard(){
        System.out.print("\n___________________\n\n");
        System.out.print("    | 1 | 2 | 3 | X\n");
        System.out.print(" ---+---+---+---+");
        System.out.println();
        for(int i = 0; i < 3; i++){
            System.out.print("  " + (i + 1) + " | ");
            for(int j = 0; j < 3; j++){
                
                if (board[i][j] == Tile.EMPTY) System.out.print(" ");
                else System.out.print(board[i][j]);
                System.out.print(" | ");
            }
            System.out.println();
            System.out.print(" ---+---+---+---+");
            System.out.println();
        }
        System.out.println("  Y");
        System.out.print("\n___________________\n\n");
    }
    public void setTile(int x, int y, Tile tile){
        board[x][y] = tile;
        printBoard();
    }
    public void clearBoard(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                board[i][j] = Tile.EMPTY;
            }
        }
        printBoard();
    }
}
enum Tile{
    EMPTY,
    X,
    O
}
class GameState{
    BoardState board = new BoardState();
    Scanner scanner = new Scanner(System.in);
    char player = 'X';
    int turns = 0;
    public void startGame(boolean comp){
        board.clearBoard();
        boolean isGame = true;
        
        while (isGame){
            if (comp){
                int playerTile[] = chooseTile();
                System.out.println("Player played at (" + (playerTile[0] + 1) + ", " + (playerTile[1] + 1) + ")");
                turns++;
                if (turns >= 9){
                    System.out.println("Draw!");
                    isGame = false;
                    break;
                }
                char winner = checkWin();
                if (winner != 'N'){
                    if (winner == 'O' && comp) System.out.println("Computer wins!");
                    else System.out.println("Player " + winner + " wins!");
                    isGame = false;
                }
                int compTile[] = computerTurn(playerTile);
                System.out.println("Computer played at (" + (compTile[0] + 1) + ", " + (compTile[1] + 1) + ")");
                turns++;
                if (turns >= 9){
                    System.out.println("Draw!");
                    isGame = false;
                }
                board.setTile(compTile[0], compTile[1], Tile.O);
                winner = checkWin();
                if (winner != 'N'){
                    if (winner == 'O' && comp) System.out.println("Computer wins!");
                    else System.out.println("Player " + winner + " wins!");
                    isGame = false;
                }
            }
            else{
                chooseTile();
                turns++;
                char winner = checkWin();
                if (winner != 'N'){
                    if (winner == 'O' && comp) System.out.println("Computer wins!");
                    else System.out.println("Player " + winner + " wins!");
                    isGame = false;
                }
                if (player == 'X') player = 'O';
                else if (player == 'O') player = 'X';
            }
            
            

        }
    }
    int[] chooseTile(){
        int x=0;
        boolean validTileX;
        int y=0;
        boolean validTileY;
        do{
            validTileX = true;
            System.out.println("Player " + player + " turn\nEnter y coordinates: ");
            x = scanner.nextInt()-1;
            if (x < 0 || x > 2){
                System.out.println("Invalid input\n\n");
                validTileX = false;
            }
        } while(!validTileX);
        do{
            validTileY = true;
            System.out.println("Player " + player + " turn\nEnter x coordinates: ");
            y = scanner.nextInt()-1;
            if (y < 0 || y > 2){
                System.out.println("Invalid input\n\n");
                validTileY = false;
            }
        } while(!validTileY);
        if (board.board[x][y] != Tile.EMPTY){
            System.out.println("Tile taken, try again");
            return chooseTile();
        }
        if (player == 'X'){
            board.setTile(x, y, Tile.X);
        } 
        else{
            board.setTile(x, y, Tile.O);
        } 
        return new int[]{x, y};
    }
    int[] computerTurn(int[] lastTurn){ // TODO corners and sides, then forks
        // 1.win
        for(int i = 0; i < 3; i++){
            // check horizontal
            if (board.board[i][0] == board.board[i][1] && board.board[i][2] == Tile.EMPTY && board.board[i][0] == Tile.O) return new int[]{i, 2};
            if (board.board[0][i] == board.board[1][i] && board.board[2][i] == Tile.EMPTY && board.board[0][i] == Tile.O) return new int[]{2, i};
            if (board.board[i][0] == board.board[i][2] && board.board[i][1] == Tile.EMPTY && board.board[i][0] == Tile.O) return new int[]{i, 1};

            // check vertical
            if (board.board[0][i] == board.board[2][i] && board.board[1][i] == Tile.EMPTY && board.board[0][i] == Tile.O) return new int[]{2, i};
            if (board.board[i][1] == board.board[i][2] && board.board[i][0] == Tile.EMPTY && board.board[i][1] == Tile.O) return new int[]{i, 0};
            if (board.board[1][i] == board.board[2][i] && board.board[0][i] == Tile.EMPTY && board.board[1][i] == Tile.O) return new int[]{0, i};
        }
        // check diagonal 1
        if (board.board[0][0] == board.board[1][1] && board.board[2][2] == Tile.EMPTY && board.board[0][0] == Tile.O) return new int[]{2, 2};
        if (board.board[0][0] == board.board[2][2] && board.board[1][1] == Tile.EMPTY && board.board[0][0] == Tile.O) return new int[]{1, 1};
        if (board.board[1][1] == board.board[2][2] && board.board[0][0] == Tile.EMPTY && board.board[1][1] == Tile.O) return new int[]{0, 0};

        // check diagonal 2
        if (board.board[0][2] == board.board[1][1] && board.board[2][0] == Tile.EMPTY && board.board[0][2] == Tile.O) return new int[]{2, 0};
        if (board.board[0][2] == board.board[2][0] && board.board[1][1] == Tile.EMPTY && board.board[0][2] == Tile.O) return new int[]{1, 1};
        if (board.board[2][0] == board.board[1][1] && board.board[0][2] == Tile.EMPTY && board.board[2][0] == Tile.O) return new int[]{0, 2};
       // 2.block
        for(int i = 0; i < 3; i++){
            // check horizontal
            if (board.board[i][0] == board.board[i][1] && board.board[i][2] == Tile.EMPTY && board.board[i][0] == Tile.X) return new int[]{i, 2};
            if ((board.board[0][i] == board.board[1][i]) && (board.board[2][i] == Tile.EMPTY) && (board.board[0][i] == Tile.X)) return new int[]{2, i};
            if (board.board[i][0] == board.board[i][2] && board.board[i][1] == Tile.EMPTY && board.board[i][0] == Tile.X) return new int[]{i, 1};

            // check vertical
            if (board.board[0][i] == board.board[2][i] && board.board[1][i] == Tile.EMPTY && board.board[0][i] == Tile.X) return new int[]{1, i};
            if (board.board[i][1] == board.board[i][2] && board.board[i][0] == Tile.EMPTY && board.board[i][1] == Tile.X) return new int[]{i, 0};
            if (board.board[1][i] == board.board[2][i] && board.board[0][i] == Tile.EMPTY && board.board[1][i] == Tile.X) return new int[]{0, i};
    }
        // check diagonal 1
        if (board.board[0][0] == board.board[1][1] && board.board[2][2] == Tile.EMPTY && board.board[0][0] == Tile.X) return new int[]{2, 2};
        if (board.board[0][0] == board.board[2][2] && board.board[1][1] == Tile.EMPTY && board.board[0][0] == Tile.X) return new int[]{1, 1};
        if (board.board[1][1] == board.board[2][2] && board.board[0][0] == Tile.EMPTY && board.board[1][1] == Tile.X) return new int[]{0, 0};

        // check diagonal 2
        if (board.board[0][2] == board.board[1][1] && board.board[2][0] == Tile.EMPTY && board.board[0][2] == Tile.X) return new int[]{2, 0};
        if (board.board[0][2] == board.board[2][0] && board.board[1][1] == Tile.EMPTY && board.board[0][2] == Tile.X) return new int[]{1, 1};
        if (board.board[2][0] == board.board[1][1] && board.board[0][2] == Tile.EMPTY && board.board[2][0] == Tile.X) return new int[]{0, 2};
        // 3.fork (Create an opportunity where you can win in two ways) 
        // 4. Block opponent fork
        // 5. Center
        if (board.board[1][1] == Tile.EMPTY) return new int[]{1, 1};
        // 6. Opposite corner
        if (lastTurn[0] == 0 && lastTurn[1] == 0 && board.board[2][2] == Tile.EMPTY) return new int[]{2, 2};
        else if (lastTurn[0] == 0 && lastTurn[1] == 2 && board.board[2][0] == Tile.EMPTY) return new int[]{0, 2};
        else if (lastTurn[0] == 2 && lastTurn[1] == 0 && board.board[0][2] == Tile.EMPTY) return new int[]{2, 0};
        else if (lastTurn[0] == 2 && lastTurn[1] == 2 && board.board[0][0] == Tile.EMPTY) return new int[]{0, 0};
        // 7. Empty corner
        if (board.board[0][0] == Tile.EMPTY) return new int[]{0, 0};
        else if (board.board[0][2] == Tile.EMPTY) return new int[]{0, 2};
        else if (board.board[2][0] == Tile.EMPTY) return new int[]{2, 0};
        else if (board.board[2][2] == Tile.EMPTY) return new int[]{2, 2};
        // 8. Empty side
        if (board.board[0][1] == Tile.EMPTY) return new int[]{0, 1};
        else if (board.board[1][0] == Tile.EMPTY) return new int[]{1, 0};
        else if (board.board[1][2] == Tile.EMPTY) return new int[]{1, 2};
        return new int[]{2, 1};
    }
    char checkWin(){
        for(int i = 0; i < 3; i++){
            if (board.board[i][0] == board.board[i][1] && board.board[i][0] == board.board[i][2] && board.board[i][0] != Tile.EMPTY) return board.board[i][0].name().charAt(0);
            if (board.board[0][i] == board.board[1][i] && board.board[0][i] == board.board[2][i] && board.board[0][i] != Tile.EMPTY) return board.board[0][i].name().charAt(0);
        }
        if (board.board[0][0] == board.board[1][1] && board.board[0][0] == board.board[2][2] && board.board[0][0] != Tile.EMPTY) return board.board[0][0].name().charAt(0);
        if (board.board[0][2] == board.board[1][1] && board.board[0][2] == board.board[2][0] && board.board[0][2] != Tile.EMPTY) return board.board[0][2].name().charAt(0);

        return 'N';
    }
}
