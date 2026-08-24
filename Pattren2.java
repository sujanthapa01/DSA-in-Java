class Pattren {

    public static void main(){

        int n = 4;
        int m = 5;

        for(int i =1; i <= n; i++){

            for(int j = 1; j <= m; j++){

//                System.out.println("i: " + i + "||" + " j: " + j);

                if(i == 1 || j == 1 || i == n || j == m){

//                    System.out.println("- inner loop -");
//                    System.out.println("i: " + i + "||"+ "j: " + j);
//                    System.out.println("n: " + n + "||" +"m: " + m);


                    System.out.print("*");
//

                }else{
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

    }
}