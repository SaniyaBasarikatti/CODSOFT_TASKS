import java.util.Random;
import java.util.Scanner;

public class NumberGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        final int MIN_RANGE = 1;
        final int MAX_RANGE = 100;
        final int MAX_ATTEMPTS = 7;

        int totalRounds = 0;
        int roundsWon = 0;
        int totalAttemptsUsed = 0;

        System.out.println("==========================================");
        System.out.println("        WELCOME TO THE NUMBER GAME        ");
        System.out.println("==========================================");

        boolean playAgain = true;

        while (playAgain) {
            totalRounds++;
            int targetNumber = random.nextInt(MAX_RANGE - MIN_RANGE + 1) + MIN_RANGE;
            int attemptsLeft = MAX_ATTEMPTS;
            boolean hasGuessedCorrectly = false;

            System.out.println("\n--- Round " + totalRounds + " ---");
            System.out.printf("Guess a number between %d and %d.%n", MIN_RANGE, MAX_RANGE);
            System.out.printf("You have %d attempts.%n", MAX_ATTEMPTS);

            while (attemptsLeft > 0) {
                System.out.printf("\nAttempts remaining: %d%nEnter your guess: ", attemptsLeft);

                // Handle non-integer input
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    scanner.next(); // Clear invalid token
                    continue;
                }

                int userGuess = scanner.nextInt();
                attemptsLeft--;
                totalAttemptsUsed++;

                if (userGuess == targetNumber) {
                    int attemptsTaken = MAX_ATTEMPTS - attemptsLeft;
                    System.out.printf("Correct! You guessed the number in %d attempt(s)!%n", attemptsTaken);
                    roundsWon++;
                    hasGuessedCorrectly = true;
                    break;
                } else if (userGuess > targetNumber) {
                    System.out.println("Too high! Try a lower number.");
                } else {
                    System.out.println("Too low! Try a higher number.");
                }
            }

            if (!hasGuessedCorrectly) {
                System.out.printf("%nOut of attempts! The correct number was %d.%n", targetNumber);
            }

            // Prompt for replay
            System.out.print("\nWould you like to play another round? (yes/no): ");
            String response = scanner.next().trim().toLowerCase();
            playAgain = response.equals("yes") || response.equals("y");
        }

        // Final score summary
        System.out.println("\n==========================================");
        System.out.println("                GAME OVER                 ");
        System.out.println("==========================================");
        System.out.printf("Total Rounds Played : %d%n", totalRounds);
        System.out.printf("Rounds Won          : %d%n", roundsWon);
        System.out.printf("Total Guesses Made  : %d%n", totalAttemptsUsed);
        
        double winRate = totalRounds > 0 ? ((double) roundsWon / totalRounds) * 100 : 0.0;
        System.out.printf("Win Rate            : %.1f%%%n", winRate);
        System.out.println("Thanks for playing!");

        scanner.close();
    }
}