
import java.util.*;

// Represents an individual course
class Course {
    private final String courseCode;
    private final String title;
    private final String description;
    private final int capacity;
    private final String schedule;
    private final List<String> enrolledStudentIds;

    public Course(String courseCode, String title, String description, int capacity, String schedule) {
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.schedule = schedule;
        this.enrolledStudentIds = new ArrayList<>();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getSchedule() {
        return schedule;
    }

    public int getAvailableSlots() {
        return capacity - enrolledStudentIds.size();
    }

    public boolean enrollStudent(String studentId) {
        if (getAvailableSlots() > 0 && !enrolledStudentIds.contains(studentId)) {
            enrolledStudentIds.add(studentId);
            return true;
        }
        return false;
    }

    public boolean dropStudent(String studentId) {
        return enrolledStudentIds.remove(studentId);
    }
}

// Represents an individual student
class Student {
    private final String studentId;
    private final String name;
    private final List<String> registeredCourseCodes;

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.registeredCourseCodes = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public List<String> getRegisteredCourseCodes() {
        return registeredCourseCodes;
    }

    public void registerCourse(String courseCode) {
        if (!registeredCourseCodes.contains(courseCode)) {
            registeredCourseCodes.add(courseCode);
        }
    }

    public void dropCourse(String courseCode) {
        registeredCourseCodes.remove(courseCode);
    }
}

// Main registration application
public class CourseRegistrationSystem {
    private static final Map<String, Course> courseDatabase = new HashMap<>();
    private static final Map<String, Student> studentDatabase = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        seedInitialData();

        System.out.println("==================================================");
        System.out.println("        STUDENT COURSE REGISTRATION SYSTEM        ");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            System.out.println("\nSelect an action:");
            System.out.println("1. List All Available Courses");
            System.out.println("2. Register for a Course");
            System.out.println("3. Drop a Course");
            System.out.println("4. View Registered Courses");
            System.out.println("5. Exit");
            System.out.print("Enter choice (1-5): ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid selection. Please enter a digit from 1 to 5.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume trailing newline

            switch (choice) {
                case 1:
                    listCourses();
                    break;
                case 2:
                    handleCourseRegistration();
                    break;
                case 3:
                    handleCourseDrop();
                    break;
                case 4:
                    viewStudentSchedule();
                    break;
                case 5:
                    System.out.println("Exiting system. Have a productive semester!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select between 1 and 5.");
            }
        }
        scanner.close();
    }

    private static void listCourses() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("               AVAILABLE COURSES                  ");
        System.out.println("--------------------------------------------------");
        for (Course course : courseDatabase.values()) {
            System.out.printf("Code: %s | Title: %s%n", course.getCourseCode(), course.getTitle());
            System.out.printf("Schedule: %s | Open Slots: %d/%d%n", course.getSchedule(), course.getAvailableSlots(), course.getCapacity());
            System.out.printf("Description: %s%n%n", course.getDescription());
        }
    }

    private static void handleCourseRegistration() {
        Student student = promptForStudent();
        if (student == null) return;

        System.out.print("Enter the Course Code to register (e.g., CS101): ");
        String code = scanner.nextLine().trim().toUpperCase();

        Course course = courseDatabase.get(code);
        if (course == null) {
            System.out.println("Registration Failed: Course code not found.");
            return;
        }

        if (student.getRegisteredCourseCodes().contains(code)) {
            System.out.println("Registration Failed: You are already registered for this course.");
            return;
        }

        if (course.getAvailableSlots() <= 0) {
            System.out.println("Registration Failed: This course is currently full.");
            return;
        }

        course.enrollStudent(student.getStudentId());
        student.registerCourse(code);
        System.out.printf("Success: %s successfully registered for %s - %s!%n",
                student.getName(), course.getCourseCode(), course.getTitle());
    }

    private static void handleCourseDrop() {
        Student student = promptForStudent();
        if (student == null) return;

        if (student.getRegisteredCourseCodes().isEmpty()) {
            System.out.println("You are not currently registered in any courses.");
            return;
        }

        System.out.println("Currently enrolled courses: " + student.getRegisteredCourseCodes());
        System.out.print("Enter Course Code to drop: ");
        String code = scanner.nextLine().trim().toUpperCase();

        if (!student.getRegisteredCourseCodes().contains(code)) {
            System.out.println("Drop Failed: You are not enrolled in course " + code);
            return;
        }

        Course course = courseDatabase.get(code);
        if (course != null) {
            course.dropStudent(student.getStudentId());
        }
        student.dropCourse(code);
        System.out.printf("Success: Dropped %s successfully.%n", code);
    }

    private static void viewStudentSchedule() {
        Student student = promptForStudent();
        if (student == null) return;

        List<String> registered = student.getRegisteredCourseCodes();
        System.out.println("\nRegistered Courses for " + student.getName() + " (" + student.getStudentId() + "):");
        if (registered.isEmpty()) {
            System.out.println("No courses registered.");
            return;
        }

        for (String code : registered) {
            Course c = courseDatabase.get(code);
            if (c != null) {
                System.out.printf("- [%s] %s (Schedule: %s)%n", c.getCourseCode(), c.getTitle(), c.getSchedule());
            }
        }
    }

    private static Student promptForStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();

        Student student = studentDatabase.get(id);
        if (student == null) {
            System.out.println("Student not found. Available demo IDs: S101, S102, S103");
            return null;
        }
        return student;
    }

    private static void seedInitialData() {
        // Sample Courses
        courseDatabase.put("CS101", new Course("CS101", "Introduction to Java", "Fundamentals of OOP and basic Java syntax.", 3, "Mon/Wed 10:00 AM - 11:30 AM"));
        courseDatabase.put("CS102", new Course("CS102", "Data Structures", "Arrays, linked lists, trees, and hash tables.", 2, "Tue/Thu 02:00 PM - 03:30 PM"));
        courseDatabase.put("MATH201", new Course("MATH201", "Discrete Mathematics", "Logic, set theory, and combinatorics.", 30, "Mon/Wed/Fri 09:00 AM - 10:00 AM"));

        // Sample Students
        studentDatabase.put("S101", new Student("S101", "Alice Smith"));
        studentDatabase.put("S102", new Student("S102", "Bob Jones"));
        studentDatabase.put("S103", new Student("S103", "Charlie Brown"));
    }
}
