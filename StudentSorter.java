import java.util.Scanner;

class Student {
    int roll_no;
    String student_name;

      Student(int roll_no, String student_name) {
        this.roll_no = roll_no;
        this.student_name = student_name;
    }

}


class StudentSorter {

    public Student[] createStudent(int total_student) {

        Student[] students = new Student[total_student];

        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < total_student; i++) {

            System.out.println("==== Add Student " + i + " ====");

            System.out.print("Enter student rollno: ");
            int no = scanner.nextInt();

            scanner.nextLine();

            System.out.print("Enter student name: ");
            String name = scanner.nextLine();
            System.out.println();
            System.out.println();

            students[i] = new Student(no,name);
            System.out.println();
        }

        return students;
    }


      public Student[] sort(Student[] arr){
        int n = arr.length;

        for(int i = 0 ; i < n ; i ++){
            int minIndex = i;

            for( int j = i + 1; j < n; j++){
                if(arr[j].roll_no < arr[minIndex].roll_no){
                    minIndex = j;
                }
            }

            Student temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }

        return arr;

    }


    static public void printStudent(Student[]arr){

        for (int i = 0; i < arr.length; i++) {
            System.out.println("=== Student "+ i + 1 + " ===");
            System.out.println(arr[i].roll_no);
            System.out.println(arr[i].student_name);
        }
    }



    public void main() {
        System.out.println("hello world!");

        StudentSorter class1 = new StudentSorter();
       Student[] arr =  class1.createStudent(5);
       class1.printStudent(arr);



       System.out.println();
       System.out.println("Sorted array");
       Student[] sortedArr = class1.sort(arr);
       class1.printStudent(sortedArr);

    }
}


