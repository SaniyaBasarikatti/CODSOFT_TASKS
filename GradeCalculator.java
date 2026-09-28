
import java.util.Scanner;

public class GradeCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==========================================");
        System.out.println("         STUDENT GRADE CALCULATOR         ");
        System.out.println("==========================================");

        int numberOfSubjects = 0;

        // Prompt for valid number of subjects
        while (numberOfSubjects <= 0) {
            System.out.print("Enter the number of subjects: ");
            if (scanner.hasNextInt()) {
                numberOfSubjects = scanner.nextInt();
                if (numberOfSubjects <= 0) {
                    System.out.println("The number of subjects must be greater than 0.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();
            }
        }

        double totalMarks = 0;

        // Take marks input for each subject
        for (int i = 1; i <= numberOfSubjects; i++) {
            double marks = -1;
            while (marks < 0 || marks > 100) {
                System.out.printf("Enter marks obtained in Subject %d (out of 100): ", i);
                if (scanner.hasNextDouble()) {
                    marks = scanner.nextDouble();
                    if (marks < 0 || marks > 100) {
                        System.out.println("Invalid score. Marks must be between 0 and 100.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a numeric score.");
                    scanner.next();
                }
            }
            totalMarks += marks;
        }

        // Calculate average percentage
        double averagePercentage = totalMarks / numberOfSubjects;

        // Assign letter grade based on average percentage
        String grade;
        if (averagePercentage >= 90) {
            grade = "A+";
        } else if (averagePercentage >= 80) {
            grade = "A";
        } else if (averagePercentage >= 70) {
            grade = "B";
        } else if (averagePercentage >= 60) {
            grade = "C";
        } else if (averagePercentage >= 50) {
            grade = "D";
        } else {
            grade = "F";
        }

        // Display results
        System.out.println("\n==========================================");
        System.out.println("             STUDENT RESULTS              ");
        System.out.println("==========================================");
        System.out.printf("Total Marks Obtained : %.2f / %.2f%n", totalMarks, (double) (numberOfSubjects * 100));
        System.out.printf("Average Percentage   : %.2f%%%n", averagePercentage);
        System.out.printf("Final Grade          : %s%n", grade);
        System.out.println("==========================================");

        scanner.close();
    }
}