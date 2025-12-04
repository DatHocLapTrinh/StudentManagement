public class Student {
    private String id;
    private String name;
    private double marks;
    private String sClass;
    private String address;

    public Student(String id, String name, double marks, String sClass, String address) {
        this.id = id;
        this.name = name;
        this.marks = marks;
        this.sClass = sClass;
        this.address = address;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getMarks() { return marks; }
    public String getSClass() { return sClass; }
    public String getAddress() { return address; }

    public void setName(String name) { this.name = name; }
    public void setSClass(String sClass) { this.sClass = sClass; }
    public void setAddress(String address) { this.address = address; }

    public void setMarks(double marks) {
        if (marks >= 0 && marks <= 10) {
            this.marks = marks;
        } else {
            System.out.println("Error: Marks must be between 0 and 10.");
        }
    }

    public String getRank() {
        if (marks >= 9.0) return "Excellent";
        if (marks >= 7.5) return "Very Good";
        if (marks >= 6.5) return "Good";
        if (marks >= 5.0) return "Medium";
        return "Fail";
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-25s | %-6.2f | %-12s | %-10s | %-20s |",
                id, name, marks, getRank(), sClass, address);
    }
}