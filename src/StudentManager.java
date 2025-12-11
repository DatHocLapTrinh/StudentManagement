import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class StudentManager {
    private static LinkedList<Student> studentList = new LinkedList<>();
    private static IdTree idTracker = new IdTree();
    private static Stack<Student> deletedStack = new Stack<>();
    private static Queue<String> actionLog = new LinkedList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeData();
        while (true) {
            System.out.println("\n=============================================");
            System.out.println("|     🎓  STUDENT MANAGEMENT SYSTEM  🎓    |");
            System.out.println("=============================================");
            System.out.println("|  1. Add New Student                       |");
            System.out.println("|  2. Edit Student Information              |");
            System.out.println("|  3. Delete Student                        |");
            System.out.println("|  4. Search Student                        |");
            System.out.println("|  5. Sort by Marks                         |");
            System.out.println("|  6. Show All Students                     |");
            System.out.println("|  7. Undo Delete                           |");
            System.out.println("|  8. Show Activity Log                     |");
            System.out.println("|  9. Exit System                           |");
            System.out.println("=============================================");
            System.out.print(">> Please enter your choice: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("\n[!] Error: Invalid input format.");
                continue;
            }

            switch (choice) {
                case 1: add(); break;
                case 2: edit(); break;
                case 3: delete(); break;
                case 4: search(); break;
                case 5: sort(); break;
                case 6: showAll(); break;
                case 7: undoDelete(); break;
                case 8: showLog(); break;
                case 9:
                    System.out.println("Exiting... Goodbye!");
                    System.exit(0);
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void add() {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        if (idTracker.contains(id)) {
            System.out.println("[!] Error: ID '" + id + "' already exists!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Class: ");
        String sClass = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        double marks = -1;
        while (true) {
            try {
                System.out.print("Enter Marks (0-10): ");
                marks = Double.parseDouble(scanner.nextLine());
                if (marks >= 0 && marks <= 10) break;
            } catch (Exception e) {}
            System.out.println("Invalid marks.");
        }

        Student s = new Student(id, name, marks, sClass, address);
        studentList.add(s);
        idTracker.insert(s);
        System.out.println(">> Success: Student added.");
        logAction("Added ID: " + id);
    }

    private static void edit() {
        System.out.print("Enter ID to edit: ");
        String id = scanner.nextLine();
        for (Student s : studentList) {
            if (s.getId().equals(id)) {
                System.out.println("Editing: " + s.getName());
                System.out.print("New Name (Enter to skip): ");
                String val = scanner.nextLine();
                if (!val.isEmpty()) s.setName(val);
                System.out.print("New Class (Enter to skip): ");
                val = scanner.nextLine();
                if (!val.isEmpty()) s.setSClass(val);
                System.out.print("New Address (Enter to skip): ");
                val = scanner.nextLine();
                if (!val.isEmpty()) s.setAddress(val);
                System.out.print("New Marks (Enter to skip): ");
                val = scanner.nextLine();
                if (!val.isEmpty()) {
                    try {
                        double m = Double.parseDouble(val);
                        s.setMarks(m);
                    } catch (Exception e) {
                        System.out.println("Invalid marks. Skipped.");
                    }
                }
                System.out.println(">> Updated successfully.");
                logAction("Edited ID: " + id);
                return;
            }
        }
        System.out.println("[!] Student not found.");
    }

    private static void delete() {
        System.out.print("Enter ID to delete: ");
        String id = scanner.nextLine();
        Student sToDelete = null;
        for (Student s : studentList) {
            if (s.getId().equals(id)) {
                sToDelete = s;
                break;
            }
        }

        if (sToDelete != null) {
            studentList.remove(sToDelete);
            idTracker.delete(id);
            deletedStack.push(sToDelete);
            System.out.println(">> Student deleted.");
            logAction("Deleted ID: " + id);
        } else {
            System.out.println("[!] Student not found.");
        }
    }

    private static void undoDelete() {
        if (deletedStack.isEmpty()) {
            System.out.println("[!] Nothing to undo.");
            return;
        }
        Student s = deletedStack.pop();
        studentList.add(s);
        idTracker.insert(s);
        System.out.println(">> Restored student: " + s.getName());
        logAction("Undid delete: " + s.getId());
    }

    private static void search() {
        System.out.println("\n🔍 ADVANCED SEARCH OPTIONS 🔍");
        System.out.println("------------------------------");
        System.out.printf("| %-3s | %-20s |%n", "NO.", "CRITERIA");
        System.out.println("------------------------------");
        System.out.printf("| %-3s | %-20s |%n", "1", "Find by ID");
        System.out.printf("| %-3s | %-20s |%n", "2", "Find by Marks");
        System.out.printf("| %-3s | %-20s |%n", "3", "Find by Name");
        System.out.printf("| %-3s | %-20s |%n", "4", "Find by Class");
        System.out.printf("| %-3s | %-20s |%n", "5", "Find by Address");
        System.out.println("------------------------------");
        System.out.print(">> Select search criteria: ");
        try {
            int type = Integer.parseInt(scanner.nextLine());
            switch (type) {
                case 1: searchById(); break;
                case 2: searchByMarks(); break;
                case 3: searchByName(); break;
                case 4: searchByClass(); break;
                case 5: searchByAddress(); break;
                default: System.out.println("Invalid selection.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private static void searchById() {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        Student s = idTracker.search(id);
        if (s != null) {
            System.out.println("Found: " + s);
        } else {
            System.out.println("Not found.");
        }
    }

    private static void searchByMarks() {
        System.out.print("Enter Marks to search: ");
        double target;
        try {
            target = Double.parseDouble(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return;
        }
        Student[] arr = studentList.toArray(new Student[0]);
        quickSort(arr, 0, arr.length - 1);
        int left = 0, right = arr.length - 1;
        boolean found = false;
        System.out.println("--- Search Result ---");
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid].getMarks() == target) {
                System.out.println(arr[mid]);
                found = true;
                int temp = mid - 1;
                while (temp >= 0 && arr[temp].getMarks() == target) {
                    System.out.println(arr[temp]);
                    temp--;
                }
                temp = mid + 1;
                while (temp < arr.length && arr[temp].getMarks() == target) {
                    System.out.println(arr[temp]);
                    temp++;
                }
                break;
            }
            if (arr[mid].getMarks() < target) right = mid - 1;
            else left = mid + 1;
        }
        if (!found) System.out.println("No student found with marks: " + target);
    }

    private static void searchByName() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().toLowerCase();
        boolean found = false;
        for (Student s : studentList) {
            if (s.getName().toLowerCase().contains(name)) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) System.out.println("Not found.");
    }

    private static void searchByClass() {
        System.out.print("Enter Class: ");
        String sClass = scanner.nextLine();
        boolean found = false;
        for (Student s : studentList) {
            if (s.getSClass().equalsIgnoreCase(sClass)) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) System.out.println("No students in this class.");
    }

    private static void searchByAddress() {
        System.out.print("Enter Address keyword: ");
        String keyword = scanner.nextLine().toLowerCase();
        boolean found = false;
        for (Student s : studentList) {
            if (s.getAddress().toLowerCase().contains(keyword)) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) System.out.println("Not found.");
    }

    private static void sort() {
        if (studentList.isEmpty()) {
            System.out.println("List is empty.");
            return;
        }
        Student[] arr = studentList.toArray(new Student[0]);
        quickSort(arr, 0, arr.length - 1);

        studentList.clear();
        for (Student s : arr) studentList.add(s);

        System.out.println(">> Sorted by Marks (Descending) using Quick Sort.");
        showAll();
    }

    private static void quickSort(Student[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(Student[] arr, int low, int high) {
        double pivot = arr[high].getMarks();
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j].getMarks() > pivot) {
                i++;
                Student temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        Student temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    private static void logAction(String action) {
        if (actionLog.size() >= 5) actionLog.poll();
        actionLog.offer(action);
    }

    private static void showLog() {
        System.out.println("\n--- RECENT ACTIVITY LOG (Queue) ---");
        if (actionLog.isEmpty()) System.out.println("[Empty]");
        for (String log : actionLog) System.out.println(" -> " + log);
    }

    private static void showAll() {
        if (studentList.isEmpty()) {
            System.out.println("List is empty.");
            return;
        }
        String border = "+------------+---------------------------+--------+--------------+------------+----------------------+";
        System.out.println(border);
        System.out.printf("| %-10s | %-25s | %-6s | %-12s | %-10s | %-20s |%n",
                "ID", "FULL NAME", "MARKS", "RANK", "CLASS", "ADDRESS");
        System.out.println(border);
        for (Student s : studentList) {
            System.out.println(s);
        }
        System.out.println(border);
    }

    private static void initializeData() {
        Student[] data = {
                new Student("BH001", "Nguyen Van An", 9.5, "SE08101", "Ha Noi"),
                new Student("BH002", "Tran Thi Bich", 8.0, "SE08102", "Da Nang"),
                new Student("BH003", "Le Van Cuong", 6.5, "SE08101", "HCM City"),
                new Student("BH004", "Pham Thi Dung", 4.5, "SE08103", "Can Tho"),
                new Student("BH005", "Hoang Van Em", 5.5, "SE08102", "Hue"),
                new Student("BH006", "Vo Thi Gam", 7.8, "SE08101", "Ha Noi"),
                new Student("BH007", "Dang Van Hung", 10.0, "SE08103", "Hai Phong"),
                new Student("BH008", "Bui Thi Kieu", 3.0, "SE08102", "Nghe An"),
                new Student("BH009", "Ngo Van Long", 6.0, "SE08101", "Nam Dinh"),
                new Student("BH010", "Vu Thi Mai", 7.0, "SE08103", "Thai Binh")
        };
        for (Student s : data) {
            studentList.add(s);
            idTracker.insert(s);
        }
        System.out.println(">> [SYSTEM] 10 Sample Students Loaded.");
    }
}
