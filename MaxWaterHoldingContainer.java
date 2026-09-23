class MaxWaterHoldingContainer {

    static public void calculate(int[] arr){

        int max = 0;
        int min = 0;

        for(int i = 0; i < arr.length; i++){

            if(arr[i] > arr[max]){
                max = i;
                System.out.println("condition max: " + arr[max]);
            }

            if(arr[min] < arr[i] && arr[i] < arr[max]){
                min = i;
                System.out.println("condition min: " + arr[min]);
            }

//            if (arr[i] > arr[min] && arr[max] < arr[min] ){
//                min = arr[min];
//                System.out.println("condition 2");
//            }

//            if(arr[max] == arr[i] && i != max){
//                min = i;
//            }

        }
        System.out.println("min: " + min );
        System.out.println("max: " +max);

    }

    static public void calculate(int arr[], int i, int min, int max){



        if(i > arr.length -1 ){
            System.out.println("min: " + min);
            System.out.println("max: " + max);
            return;
        }

        if(arr[i] > arr[max]){
            max = i;
            System.out.println("condition max: " + arr[max]);
            calculate(arr, i + 1, min , max);
            return;
        }

        if(arr[min] < arr[i] && arr[i] < arr[max]){
            min = i;
            System.out.println("condition min: " + arr[min]);
        }

//        if(arr[i] == arr[max]){
//            System.out.println("min: " + arr[i]);
//            System.out.println("max: " + arr[max]);
//          min = i;
//        }

        calculate(arr, i + 1, min , max);
    }




    static public void main(){

        int[] arr = {1,4,3,7,2,6,9,3,2,10,56,33};


       calculate(arr);
       calculate(arr,0,0,0);


    }


}