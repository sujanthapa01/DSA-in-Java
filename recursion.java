
class Main {
    public static void printNum(int n){

        System.out.println(n);

        if(n == 5){
            return ;
        }

        printNum(n + 1);
    }


    public static void addSum(int i , int n, int finalsum){


        if(i == n){
            finalsum += i ;
            System.out.println(finalsum);
            return;
        }

        finalsum += i;

        addSum(i + 1,n, finalsum);



    }

    public static int factorial(int n ){
        if(n == 1){
            return 1;
        }

        int fac =  factorial(n - 1);
        int result = n * fac ;
        return result;
    }

    public static void fibonacci(int value1, int value2, int n){

        if(n == 0){
            return;
        }

        int value3 = value1 + value2;
        System.out.println(value3);
        fibonacci(value2,value3,n-1);

    }

    public static void printHelloWorld(int i, int n){

        if(i == n){
            return;
        }

        System.out.println("hello world");
        printHelloWorld(i+1,n);
    }

    static public void printEven(int i , int n){

        if(i > n){
            return ;
        }
        if(i%2 == 0){
            System.out.println( i + " is even.");
        }else {
            System.out.println(i + " is odd.");
        }

        printEven(i + 1, n);
    }


    static public void find_max_element(int i, int max, int[] arr){
        int size = arr.length;

         if( i > size - 1){
             System.out.println("max element is "+ max);
             return;
    }

        if( max < arr[i]){
            max = arr[i];
//            System.out.println("find max loop runned!");
        }

        find_max_element(i + 1,max, arr);
    }

    static public void count_repeated_numbers(int i, int target, int count, int[] arr) {
        if (i > arr.length - 1) {
            System.out.println("target: "+ target + "  ||  count: "+ count);
            return;
        }
            if (target == arr[i]) {
                count = count + 1;
            }
            count_repeated_numbers(i+1 , target, count, arr);
        }


        public static void main (String[]args) {
            System.out.println("Start small. Ship something.");

            // printNum(0);
            // addSum(1, 100,0);
//        int result = factorial(200);
//        System.out.println(result);

//        int n = 20;
//        System.out.println(0);
//        System.out.println(1);
//        fabonachi(0,1,n-2);


//        printHelloWorld(0,10);
//        printEven(0,20);
//        find_max_element(0,0,new int[] {1,2,4,5,122});
//
          count_repeated_numbers(0, 1, 0, new int[]{3, 4, 5, 2, 1,2,3,1,2,3,2,2,3,2,3,2,3,1,1,3,2,2,3,43,23,4,2,2,23,2,6, 3, 2, 5, 2});

}
}