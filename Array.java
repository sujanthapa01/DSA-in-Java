

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


    static public void bubbleSort() {

        int arr[] = {7, 6, 2, 9, 1, 0, 5};

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

        for(int o = 0; o < arr.length; o++){
            System.out.print(arr[o]);
        }
    }


    public static void main() {

        bubbleSort();
    }
}