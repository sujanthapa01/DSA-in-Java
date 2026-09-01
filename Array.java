class Array {


    static public void missingNum() {

        int arr[] = {1, 2, 4, 5, 6};

        int count = 1;
        for (int i = 0; i < arr.length; i++) {

            if (arr[i] == count) {
                count++;
            } else {
                System.out.println(count);
                return;
            }
        }
    }


    static public int[] bubbleSort(int arr[]) {

//        int arr[] = {-7, 6, 23, 9, 1, 0, 5};

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[i] < arr[j]) {
                    int temp;
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }

        return arr;

    }

    static public void print(int arr[]) {
        for (int o = 0; o < arr.length; o++) {
            System.out.print(arr[o] + " ");
        }
    }


    static public void find(int arr[], int target) {
//      int arr[] = {3,9,0,1,7,14,2};
        int newArr[] = bubbleSort(arr); //increases time complexity
        int mid = newArr.length / 2;

        boolean found = false;

//     find target in first half
        for (int i = 0; i < mid; i++) {
            if (target == newArr[i]) {
                found = true;
                System.out.print("target found in first half");
                return;
            }
        }

//     find target in second half
        for (int i = mid; i < newArr.length; i++) {
            if (target == newArr[i]) {
                found = true;
                System.out.print("terget found in second half");
                return;
            }
        }


        print(newArr);


    }

    public static void main() {

//        bubbleSort();
        find(new int[]{3,9,0,1,7,14,2},14);
    }
}