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
        int minIndex = 0;

        for (int i = 0; i < len; i++) {

            for (int j = i + 1; j < len; i++) {
                if (arr[j] > arr[minIndex]) {
                    minIndex = j;
                }
            }

            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;

        }
        return arr;

    }

    static public void main() {
        int[] arr = {-1, 0, 2, 4, 6, 7, 8};
        int[] arr2 = {2,3,6,5,3,1,0,5,4,3,25,7};

        int[] sortedArray = sort(arr2);
        System.out.println(sortedArray);
        int high = sortedArray.length - 1;
        int low = 0;
        int result = binarySearch(sortedArray, 7, low, high); // binary search only apply on sorted array
        System.out.print(result);



    }

}