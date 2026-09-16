import java.util.*;

class BackTracking {

    static public void subsets(int[] nums, int index, List<Integer> current) {

        if (index == nums.length ) {
            System.out.println(current);
            return;
        }

        current.add(nums[index]);
        subsets(nums, index + 1, current);

        current.remove(current.size() - 1);
        subsets(nums, index + 1, current);
    }


    static public void subsequence(String words, int index, String sets){

        if(index == words.length()){
            System.out.println(sets);
            return;
        }

        sets += words.charAt(index);
        subsequence(words, index + 1, sets);

        sets = sets.substring(0, sets.length() - 1);
        subsequence(words, index + 1, sets);
    }

//
//    static public void subsequence(String words, int index, List<String> sets){
//
//        if(index == words.length()){
//            System.out.println(sets);
//            return;
//        }
//        sets.add(String.valueOf(words.charAt(index)));
//        subsequence(words, index + 1, sets);
//
//        sets.remove(sets.size() - 1);
//        subsequence(words, index + 1, sets);
//    }



    static public void main() {

//        String[] words = {"A","B","C","R"};
//        subsets(nums, 0, new ArrayList<>());

        String words = "sujanThapa";

        subsequence(words, 0 , "");
//
//        String str = "ASS";
//
//        System.out.println(str.length());
//        System.out.println(str.charAt(2));

    }

}