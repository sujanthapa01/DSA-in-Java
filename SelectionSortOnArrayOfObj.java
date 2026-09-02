import java.util.Scanner;

class Student {
    int roll_no;
    String student_name;

//   void User(int roll_no, String student_name) {
//        this.roll_no = roll_no;
//        this.student_name = student_name;
//    }

    public void createStudent(int total_student) {

        for (int i = 1; i <= total_student; i++) {

            Scanner scanner = new Scanner(System.in);


            System.out.print("==== Add Student " + i + " ====");

            System.out.println();
            System.out.println("Enter student rollno: ");
            int no = scanner.nextInt();

            System.out.println("Enter student name: ");
            String name = scanner.next();
            System.out.println();
            System.out.println();
            System.out.println();

            this.roll_no = no;
            this.student_name = name;

        }
    }

}

class SelectionSortOnArrayOfObj {

    public void main() {
        System.out.println("hello world!");

        Student student = new Student();
        student.createStudent(10);


    }
}


