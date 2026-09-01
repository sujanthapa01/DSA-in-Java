class Sorting {

    static public int[] selectionSort(int arr[]) {

        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            // swap the minIndex with the arr[i]
            int temp = arr[minIndex];

            arr[minIndex] = arr[i];
            arr[i] = temp;
        }

        return arr;
    }

    static public void print(int arr[]) {

        for (int i = 0; i < arr.length; i++) {
            System.out.print(" " + arr[i]);
        }

    }


    static public void main() {

       int arr[] = selectionSort(new int[]{-15, 42, 0, -8, 23, -42, 10, 0, -1, 5});
       print(arr);

    }


}