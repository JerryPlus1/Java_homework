
import java.util.*;

public class GridGame {
    private static final int ROWS = 3;
    private static final int COLS = 3;

    private static int playerRow = ROWS - 1;
    private static int playerCol = COLS - 1;
    private static int monster1Row = 0;
    private static int monster1Col = COLS - 1;
    private static int monster2Row = ROWS - 1;
    private static int monster2Col = 0;
    private static int monster3Row = 0;
    private static int monster3Col = 0;

    private static int playerHealth = 100;
    private static int monster1Health = 100;
    private static int monster2Health = 100;
    private static int monster3Health = 100;

    private static char[][] map = new char[ROWS][COLS];

    public static void main(String[] args) {
        System.out.println("" +
                "███████╗████████╗ █████╗ ██████╗ ████████╗     ██████╗  █████╗ ███╗   ███╗███████╗\n" +
                "██╔════╝╚══██╔══╝██╔══██╗██╔══██╗╚══██╔══╝    ██╔════╝ ██╔══██╗████╗ ████║██╔════╝\n" +
                "███████╗   ██║   ███████║██████╔╝   ██║       ██║  ███╗███████║██╔████╔██║█████╗  \n" +
                "╚════██║   ██║   ██╔══██║██╔══██╗   ██║       ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝  \n" +
                "███████║   ██║   ██║  ██║██║  ██║   ██║       ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗\n" +
                "╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝        ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝\n" +
                "This is a brave world, please pick up your weapons and fight the enemy!" +
                "                                                                                  ");
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        initializeMap();
        printMap();

        // 游戏循环
        for (int round = 1; ; round++) {
            System.out.println("Round " + round);
            System.out.print("Enter direction (up, down, left, right): ");
            String direction = scanner.nextLine().toLowerCase();

            movePlayer(direction);
            
            map[monster1Row][monster1Col] = '•';
            int[] newMonster1Pos = moveMonster(random, monster1Row, monster1Col);
            monster1Row = newMonster1Pos[0];
            monster1Col = newMonster1Pos[1];
            map[monster1Row][monster1Col] = '%';

            map[monster2Row][monster2Col] = '•';
            int[] newMonster2Pos = moveMonster(random, monster2Row, monster2Col);
            monster2Row = newMonster2Pos[0];
            monster2Col = newMonster2Pos[1];
            map[monster2Row][monster2Col] = '%';

            map[monster3Row][monster3Col] = '•';
            int[] newMonster3Pos = moveMonster(random, monster3Row, monster3Col);
            monster3Row = newMonster3Pos[0];
            monster3Col = newMonster3Pos[1];
            map[monster3Row][monster3Col] = '%';

            printHealthStatus(); // The method printHealthStatus() is undefined for the type GridGame
            printMap();
            checkCollisions();
            checkMonsterDeaths();
            attackSystem();
            if (playerHealth <= 0) {
                System.out.println("YOU LOSE!");
                break;
            } else if (monster1Health <= 0 && monster2Health <= 0 && monster3Health <= 0) {
                System.out.println("YOU WIN!");
                break;
            }
        }
    }

    private static void initializeMap() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                map[i][j] = '•';
            }
        }
        map[playerRow][playerCol] = '@';
        map[monster1Row][monster1Col] = '%';
        map[monster2Row][monster2Col] = '%';
        map[monster3Row][monster3Col] = '%';
    }

    private static void printMap() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void movePlayer(String direction) {
        int newPlayerRow = playerRow;
        int newPlayerCol = playerCol;
    
        switch (direction) {
            case "up":
                if (newPlayerRow > 0) {
                    newPlayerRow--;
                } else {
                    System.out.println("You can't go up. You lose a move.");
                    return;
                }
                break;
            case "down":
                if (newPlayerRow < ROWS - 1) {
                    newPlayerRow++;
                } else {
                    System.out.println("You can't go down. You lose a move.");
                    return;
                }
                break;
            case "left":
                if (newPlayerCol > 0) {
                    newPlayerCol--;
                } else {
                    System.out.println("You can't go left. You lose a move.");
                    return;
                }
                break;
            case "right":
                if (newPlayerCol < COLS - 1) {
                    newPlayerCol++;
                } else {
                    System.out.println("You can't go right. You lose a move.");
                    return;
                }
                break;
            default:
                System.out.println("Use only keywords up, down, left, right.");
                return;
        }
    
        // Rest of the method...
    }

    private static boolean attackMonster(int row, int col) {
        Random random = new Random();
        boolean successfulAttack = random.nextBoolean(); // 50% chance of success
        if (successfulAttack) {
            if (row == monster1Row && col == monster1Col && monster1Health > 0) {
                monster1Health -= 50;
            } else if (row == monster2Row && col == monster2Col && monster2Health > 0) {
                monster2Health -= 50;
            } else if (row == monster3Row && col == monster3Col && monster3Health > 0) {
                monster3Health -= 50;
            }
        }
        return successfulAttack;
    }

    private static void printHealthStatus(){

        System.out.println("Your Hp:"+playerHealth);
        System.out.println("monster1 HP:"+monster1Health);
        System.out.println("monster2 HP:"+monster2Health);
        System.out.println("monster3 HP:"+monster3Health);

    }

    private static int[] moveMonster(Random random, int monsterRow, int monsterCol) {
        // 随机选择一个方向
        int direction = random.nextInt(4);

        // 根据选择的方向更新怪物的位置
        switch (direction) {
            case 0: // 上
                if (monsterRow > 0) {
                    monsterRow--;
                }
                break;
            case 1: // 下
                if (monsterRow < ROWS - 1) {
                    monsterRow++;
                }
                break;
            case 2: // 左
                if (monsterCol > 0) {
                    monsterCol--;
                }
                break;
            case 3: // 右
                if (monsterCol < COLS - 1) {
                    monsterCol++;
                }
                break;
        }

        return new int[] {monsterRow, monsterCol};
    }

    private static void attackSystem(){
        if (playerCol == monster1Col && playerRow == monster1Row){
            attackMonster( playerRow,playerCol);
        }
        if (playerCol == monster2Col && playerRow == monster2Row){
            attackMonster( playerRow,playerCol);
        }
        if (playerCol == monster3Col && playerRow == monster3Row){
            attackMonster( playerRow,playerCol);
        }
    }
    private static void checkMonsterDeaths() {
        checkMonsterDeath(monster1Row, monster1Col, monster1Health);
        checkMonsterDeath(monster2Row, monster2Col, monster2Health);
        checkMonsterDeath(monster3Row, monster3Col, monster3Health);
    }

    private static void checkMonsterDeath(int row, int col, int health) {
        if (health <= 0) {
            map[row][col] = '⨉';
        }
    }

    private static void checkCollisions(){
        if (playerRow == monster1Row && playerCol == monster1Col && monster1Health > 0) {
            playerHealth -= 50;
        }
        if (playerRow == monster2Row && playerCol == monster2Col && monster2Health > 0) {
            playerHealth -= 50;
        }
        if (playerRow == monster3Row && playerCol == monster3Col && monster3Health > 0) {
            playerHealth -= 50;
        }
    }
}
