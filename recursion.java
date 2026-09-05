
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

    public static void fabonachi(int value1, int value2, int n){

        if(n == 0){
            return;
        }

        int value3 = value1 + value2;
        System.out.println(value3);
        fabonachi(value2,value3,n-1);

    }

    public static void main(String[] args) {
        System.out.println("Start small. Ship something.");

        // printNum(0);
        // addSum(1, 100,0);
//        int result = factorial(200);
//        System.out.println(result);

        int n = 20;
        System.out.println(0);
        System.out.println(1);
        fabonachi(0,1,n-2);
    }
}