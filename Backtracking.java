import java.util.*;

class BackTracking {

    static public void backtrack(int[] nums, int index, List<Integer> current) {

        if (index == nums.length ) {
            System.out.println(current);
            return;
        }

        current.add(nums[index]);
        backtrack(nums, index + 1, current);

        current.remove(current.size() - 1);
        backtrack(nums, index + 1, current);
    }

    static public void main() {

        int[] nums = {1, 2,3,2,4,2};
        backtrack(nums, 0, new ArrayList<>());
    }

}