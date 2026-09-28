

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

// Represents a single question, its options, and the correct answer index
class Question {
    private final String questionText;
    private final String[] options;
    private final int correctOptionIndex; // 1-based index (1-4)

    public Question(String questionText, String[] options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}

// Stores the outcome for an individual question
class QuizRecord {
    private final Question question;
    private final int userChoice; // 0 indicates timed out / unanswered
    private final boolean isCorrect;

    public QuizRecord(Question question, int userChoice, boolean isCorrect) {
        this.question = question;
        this.userChoice = userChoice;
        this.isCorrect = isCorrect;
    }

    public Question getQuestion() {
        return question;
    }

    public int getUserChoice() {
        return userChoice;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}

public class QuizApplication {
    private static final int SECONDS_PER_QUESTION = 10;

    public static void main(String[] args) {
        List<Question> questions = loadQuestions();
        List<QuizRecord> records = new ArrayList<>();
        int score = 0;

        // Executor to handle timed input
        ExecutorService executor = Executors.newSingleThreadExecutor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("==================================================");
        System.out.println("         WELCOME TO THE TIMED JAVA QUIZ           ");
        System.out.println("==================================================");
        System.out.printf("Rules: You have %d seconds per question. Good luck!%n%n", SECONDS_PER_QUESTION);

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            System.out.println("--------------------------------------------------");
            System.out.printf("Question %d of %d:%n", (i + 1), questions.size());
            System.out.println(q.getQuestionText());

            String[] options = q.getOptions();
            for (int j = 0; j < options.length; j++) {
                System.out.printf("  [%d] %s%n", (j + 1), options[j]);
            }

            System.out.printf("%nEnter choice (1-%d) within %d seconds: ", options.length, SECONDS_PER_QUESTION);

            // Submit input task to executor with a timeout limit
            Future<String> inputFuture = executor.submit(() -> reader.readLine());
            int userChoice = 0;
            boolean timedOut = false;

            try {
                String input = inputFuture.get(SECONDS_PER_QUESTION, TimeUnit.SECONDS);
                if (input != null && input.trim().matches("\\d+")) {
                    userChoice = Integer.parseInt(input.trim());
                }
            } catch (TimeoutException e) {
                timedOut = true;
                inputFuture.cancel(true);
                System.out.println("\n\n[TIME'S UP!] You ran out of time for this question.");
            } catch (Exception e) {
                System.out.println("\nAn error occurred while reading input.");
            }

            boolean isCorrect = (!timedOut && userChoice == q.getCorrectOptionIndex());
            if (!timedOut) {
                if (isCorrect) {
                    System.out.println(">> Correct Answer!");
                    score++;
                } else {
                    System.out.printf(">> Incorrect! The correct answer was [%d].%n", q.getCorrectOptionIndex());
                }
            }

            records.add(new QuizRecord(q, userChoice, isCorrect));
            System.out.println();
        }

        executor.shutdownNow();

        // Display final summary and score screen
        displayResults(questions.size(), score, records);
    }

    private static void displayResults(int totalQuestions, int score, List<QuizRecord> records) {
        System.out.println("==================================================");
        System.out.println("                   FINAL RESULT                   ");
        System.out.println("==================================================");
        System.out.printf("Final Score : %d / %d (%.1f%%)%n%n", score, totalQuestions, ((double) score / totalQuestions) * 100);

        System.out.println("--- Answer Summary ---");
        for (int i = 0; i < records.size(); i++) {
            QuizRecord record = records.get(i);
            Question q = record.getQuestion();
            String status = record.isCorrect() ? "CORRECT" : (record.getUserChoice() == 0 ? "TIMED OUT" : "INCORRECT");

            System.out.printf("Q%d: %s%n", (i + 1), q.getQuestionText());
            System.out.printf("     Your answer: %s | Correct answer: [%d] %s | Result: %s%n%n",
                    record.getUserChoice() == 0 ? "None" : "[" + record.getUserChoice() + "]",
                    q.getCorrectOptionIndex(),
                    q.getOptions()[q.getCorrectOptionIndex() - 1],
                    status);
        }
        System.out.println("==================================================");
    }

    private static List<Question> loadQuestions() {
        List<Question> list = new ArrayList<>();
        list.add(new Question(
                "Which component is responsible for converting Java bytecode into machine code?",
                new String[]{"JDK", "JRE", "JVM", "JIT Compiler"},
                3
        ));
        list.add(new Question(
                "Which keyword is used to prevent method overriding in Java?",
                new String[]{"static", "final", "abstract", "protected"},
                2
        ));
        list.add(new Question(
                "What is the default value of a boolean variable in Java?",
                new String[]{"true", "false", "null", "0"},
                2
        ));
        list.add(new Question(
                "Which collection class allows unique elements only?",
                new String[]{"ArrayList", "LinkedList", "HashSet", "Vector"},
                3
        ));
        return list;
    }
}
