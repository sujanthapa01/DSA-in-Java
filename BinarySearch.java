class Main {

    static public int binarySearch(int[] arr, int target, int low, int high) {
        int mid = low + (high - low) / 2;
        if (low > high) {
            return -1;
        }
        if (arr[mid] == target) {
            return mid;
        }
        if (arr[mid] < target) {
            return binarySearch(arr, target, mid + 1, high);
        }
        return binarySearch(arr, target, low, mid - 1);
    }


    static public int[] sort(int[] arr) {

        int len = arr.length - 1;


        for (int i = 0; i < len; i++) {
            int minIndex = i;
            for (int j = i + 1; j < len; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;

        }
        return arr;

    }


    static public void print(int[] arr){
System.out.print("[ ");
        for(int i = 0; i < arr.length - 1; i++ ){
            System.out.print(arr[i] + " ");
        }
        System.out.println("]");
    }

    static public void main() {
//        int[] arr = {-1, 0, 2, 4, 6, 7, 8};
        int[] arr2 = {2,3,6,5,3,1,0,5,4,3,25,7};
        print(arr2);
        int[] sortedArray = sort(arr2);
        print(arr2);
        int high = sortedArray.length;
//        System.out.println("high " + high);

        int low = 0;
        int result = binarySearch(sortedArray, 6, low, high); // binary search only apply on sorted array
        System.out.print("element found in Index : "+ result);



    }

}