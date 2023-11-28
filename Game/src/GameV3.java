import java.util.Random;
import java.util.Scanner;

class Game {
    private char[][] map;
    private char playerSymbol;
    private char monsterSymbol;
    private char deadSymbol;
    private int playerX;
    private int playerY;
    private int[] monsterX;
    private int[] monsterY;
    private int playerHealth;
    private int[] monsterHealth;
    private Random rand;
    private int round;


Game() {
    map = new char[3][3];
    playerSymbol = '*';
    monsterSymbol = '%';
    deadSymbol = 'x';
    playerX = 2;
    playerY = 2;
    monsterX = new int[] {0, 2, 0};
    monsterY = new int[] {0, 0, 2};
    playerHealth = 100;
    monsterHealth = new int[] {100, 100, 100};
    rand = new Random();
    round = 1;

    resetMap();
}

private void resetMap() {
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            map[i][j] = '.';
        }
    }
    map[playerX][playerY] = playerSymbol;
    for (int i = 0; i < monsterX.length; i++) {
        if (monsterHealth[i] > 0) {
            map[monsterX[i]][monsterY[i]] = monsterSymbol;
        } else if (monsterHealth[i] <= 0) {
            map[monsterX[i]][monsterY[i]] = deadSymbol;
        }
    }
}

public void start() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("" +
                "███████╗████████╗ █████╗ ██████╗ ████████╗     ██████╗  █████╗ ███╗   ███╗███████╗\n" +
                "██╔════╝╚══██╔══╝██╔══██╗██╔══██╗╚══██╔══╝    ██╔════╝ ██╔══██╗████╗ ████║██╔════╝\n" +
                "███████╗   ██║   ███████║██████╔╝   ██║       ██║  ███╗███████║██╔████╔██║█████╗  \n" +
                "╚════██║   ██║   ██╔══██║██╔══██╗   ██║       ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝  \n" +
                "███████║   ██║   ██║  ██║██║  ██║   ██║       ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗\n" +
                "╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝        ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝\n" +
                "This is a brave world, please pick up your weapons and fight the enemy!" +
                "                                                                                  ");
    while (!isGameOver()) {
        printMapAndStatus();
        playerMove(scanner);
        for (int i = 0; i < monsterX.length; i++) {
            monsterMove(i);
        }
        round++;
    }
    scanner.close();
}

private void playerMove(Scanner scanner) {
    System.out.print("Enter move (up, down, left, right): ");
    String move = scanner.nextLine().trim();
    int newX = playerX, newY = playerY;
    
    switch (move) {
        case "up":    newX--; break;
        case "down":  newX++; break;
        case "left":  newY--; break;
        case "right": newY++; break;
        default: System.out.println("Invalid move. Try again."); return;
    }

    if (isValidMove(newX, newY)) {
        movePlayer(newX, newY);
    } else {
        System.out.println("Can't move there! You lose a move!");
    }
}

private boolean isValidMove(int x, int y) {
    return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
}

private void movePlayer(int newX, int newY) {
    if (map[newX][newY] == monsterSymbol) {
        for (int i = 0; i < monsterX.length; i++) {
            if (monsterX[i] == newX && monsterY[i] == newY && monsterHealth[i] > 0) {
                attack(true, i);
                return; 
            }
        }
    }
    playerX = newX;
    playerY = newY;
    resetMap();
}

private void monsterMove(int monsterIndex) {
    if (monsterHealth[monsterIndex] <= 0) {
        return;
    }

    int newX = monsterX[monsterIndex], newY = monsterY[monsterIndex];
    int move = rand.nextInt(4);

    switch (move) {
        case 0: newX--; break;
        case 1: newX++; break;
        case 2: newY--; break;
        case 3: newY++; break;
    }

    if (isValidMove(newX, newY) && !(newX == playerX && newY == playerY)) {
        monsterX[monsterIndex] = newX;
        monsterY[monsterIndex] = newY;
    }
    resetMap();
}

private void attack(boolean isPlayerAttacking, int monsterIndex) {
    if (rand.nextBoolean()) { // Defense successful
        System.out.println(isPlayerAttacking ? "Player missed!" : "Monster missed!");
    } else { // Attack successful
            if (isPlayerAttacking) {
                monsterHealth[monsterIndex] -= 50;
                System.out.println("Player hits Monster" + (monsterIndex + 1));
                if (monsterHealth[monsterIndex] <= 0) {
                    System.out.println("Monster" + (monsterIndex + 1) + " is dead.");
                }
            } else {
                playerHealth -= 20;
                System.out.println("Monster hits Player");
                if (playerHealth <= 0) {
                    System.out.println("Player is dead.");
                }
            }
        }
        checkDead();
    }

    private void checkDead() {
        if (playerHealth <= 0) {
            map[playerX][playerY] = deadSymbol;
            playerX = -1; 
            playerY = -1;
        }
        for (int i = 0; i < monsterHealth.length; i++) {
            if (monsterHealth[i] <= 0 && map[monsterX[i]][monsterY[i]] != deadSymbol) {
                map[monsterX[i]][monsterY[i]] = deadSymbol;
                monsterX[i] = -1; 
                monsterY[i] = -1;
            }
        }
    }

    private void printMapAndStatus() {
        System.out.println("Round: " + round);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Player Health: " + playerHealth);
        for (int i = 0; i < monsterHealth.length; i++) {
            System.out.println("Monster" + (i + 1) + " Health: " + monsterHealth[i]);
        }
        System.out.println();
    }

    private boolean isGameOver() {
        if (playerHealth <= 0) {
            System.out.println("____    ____  ______    __    __      __        ______        _______. _______ \r\n" + //
                    "\\   \\  /   / /  __  \\  |  |  |  |    |  |      /  __  \\      /       ||   ____|\r\n" + //
                    " \\   \\/   / |  |  |  | |  |  |  |    |  |     |  |  |  |    |   (----`|  |__   \r\n" + //
                    "  \\_    _/  |  |  |  | |  |  |  |    |  |     |  |  |  |     \\   \\    |   __|  \r\n" + //
                    "    |  |    |  `--'  | |  `--'  |    |  `----.|  `--'  | .----)   |   |  |____ \r\n" + //
                    "    |__|     \\______/   \\______/     |_______| \\______/  |_______/    |_______|");
            return true;
        }
        boolean allMonstersDead = true;
        for (int health : monsterHealth) {
            if (health > 0) {
                allMonstersDead = false;
                break;
            }
        }
        if (allMonstersDead) {
            System.out.println("____    ____  ______    __    __     ____    __    ____  __  .__   __.  __   __   __  \r\n" + //
                    "\\   \\  /   / /  __  \\  |  |  |  |    \\   \\  /  \\  /   / |  | |  \\ |  | |  | |  | |  | \r\n" + //
                    " \\   \\/   / |  |  |  | |  |  |  |     \\   \\/    \\/   /  |  | |   \\|  | |  | |  | |  | \r\n" + //
                    "  \\_    _/  |  |  |  | |  |  |  |      \\            /   |  | |  . `  | |  | |  | |  | \r\n" + //
                    "    |  |    |  `--'  | |  `--'  |       \\    /\\    /    |  | |  |\\   | |__| |__| |__| \r\n" + //
                    "    |__|     \\______/   \\______/         \\__/  \\__/     |__| |__| \\__| (__) (__) (__) ");
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        GameV2 gameV2 = new GameV2();
        gameV2.start();
    }
}