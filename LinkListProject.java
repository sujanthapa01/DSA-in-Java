import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
class LinkListProject {

    Node head = null;

    class Node {
        Map<Integer, String> records = new HashMap<>();
        Node next = null;

        Node(int rollno, String name){
            records.put(rollno,name);
        }
    }


    void addFirst(int rollno, String name){
        Node newNode = new Node(rollno,name);
        newNode.next = head;
        head = newNode;

        return;
    }

    void print(){

        HashSet<Node> visited = new HashSet<>();

        Node current = head;

        while(current != null){

            if(visited.contains(current)){
                System.out.println("cycle is created: "+ current.records.keySet() + "...");
                return;
            }

            for(Map.Entry<Integer,String> entry : current.records.entrySet()){
                System.out.print(entry.getKey() + ":" + entry.getValue() + " -> ");
            }

            visited.add(current);

            if(current.next == null){
                System.out.print("null");
                return;
            }

            current = current.next;

        }
    }

//    selection sort for sorting data

    void sort(){
        Node current = head;

        for(Node i = head; i != null; i =i.next){

            Node min = i;

            for(Node j = i.next; j != null;j = j.next){

                int jRollno = j.records.keySet().iterator().next();
                int minRollno = i.records.keySet().iterator().next();

                if(jRollno < minRollno){
                    min = j;
                }

            }

            Map<Integer,String> temp = i.records;
            i.records = min.records;
            min.records = temp;

        }


    }

    void createCycle(int position){
        System.out.println();
        System.out.println("detecting cycle");

        if( head == null){
            return;
        }

        Node target = head;

        for(int i = 0; i < position; i++){
            if(target == null){
                return;
            }
            target = target.next;
        }

        Node current = head;
        while(current.next != null){
            current = current.next;
        }

        if(target == null){
            return;
        }

        current.next = target;
    }


    boolean hasCycle(){

        Node slow = head;
        Node fast = head;

        while(fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;

            if(slow == fast){
                return true;
            }
        }
        return false;
    }
    private class PairNode {

        Node mid = null;
        Node high = null;

        PairNode(Node slow, Node fast){
            mid = slow;
            high = fast;
        }

        Node getMid(){
            return mid;
        }

        Node getHigh(){
            return high;
        }

   }
   private int checkMidOfList(){

        Node slow = head;
        Node fast = head;

        while( fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }

       PariNode nodePairs = new PairNode(slow,fast);

        return nodePairs;

    }
    Node search(int rollno, int target, Node low, Node high ){

        PairNode pairs = checkMidOfList();

        Node mid = pairs.getMid();

        if(head == null){
            return;
        }

        if(mid.records.keySet() > high.records.keySet()){
            return;
        }

        if(mid.records.keySet() == target){
            return mid;
        }

        if(mid.records.keySet() < target){
           return search(rollno, target, mid,high);
        }

        return search(rollno,target,low, mid - 1, high);

    }




    void main(){

        LinkListProject ll1 = new LinkListProject();

        ll1.addFirst(4658, "Sujan Thapa");
        ll1.addFirst(4661, "Sujal Thapa");
        ll1.addFirst(4659, "Rishika Gurung");
        ll1.addFirst(4660, "Abhijay Joli");
        ll1.print();
//        ll1.sort();
        System.out.println();
        ll1.print();
        ll1.createCycle(1);
        boolean hasCycle = ll1.hasCycle();
        System.out.println(hasCycle);
        ll1.print();
        Node result = ll1.search(4658)
    }


}