import java.util.HashSet;

class LinkListPractice {


    public Node head = null;

    class Node {
        int val;
        Node next;

        Node(int data) {
            this.val = data;
            this.next = null;
        }
    }


    Node addFirst(int val) {
        Node newNode = new Node(val);
        newNode.next = head;
        head = newNode;
        return head;
    }

    //   Print LinkList using Loops 0(n)
    void print() {

        Node current = head;

        while (current != null) {
            System.out.print(current.val + " -> ");
            if (current.next == null) {
                System.out.print("null");
            }
            current = current.next;
        }
    }

    //    Print LinkList using recursion
    void recursivePrint(Node head) {
        if (head == null) {
            return;
        }

        System.out.print(head.val + " -> ");

        if (head.next == null) {
            System.out.println("null");
        }
        recursivePrint(head.next);

    }

    void deleteValue(Node head, int value) {
        if (head == null || head.next == null ) {
            System.out.println("no value found!");
            return;
        }

        if(head.next.val == value){
            head.next = head.next.next;
            return;
        }
        deleteValue(head.next, value);

    }


    Node createCycle(int position){
       if(head == null){
           return head;
       }

       Node target = head;

       for(int i =0; i< position; i++){
           if(target == null){
               return target;
           }

           System.out.println("position: " + position + "|  target: " + target.val);
           target = target.next;
       }

       if(target == null){
           return target;
       }

       Node current = head;

       while(current.next != null){
           current = current.next;
       }

       current.next = target;
       return current;
    }



     void printCycle(){

         Node current = head;

        HashSet<Node> visited = new HashSet<>();


        while(current != null){

            if(visited.contains(current)){
                System.out.print("cycle connects to " + current.val);
            return;
            }

            visited.add(current);
            System.out.print(current.val + " -> ");
            current = current.next;
        }

        System.out.print("null");

    }



       void main() {

        LinkListPractice ll1 = new LinkListPractice();
        ll1.addFirst(10);
        ll1.addFirst(20);
        ll1.addFirst(30);
        ll1.addFirst(40);
        ll1.print();
        System.out.println();
//        ll1.recursivePrint(ll1.head);
//        ll1.deleteValue(ll1.head,20);
//        ll1.recursivePrint(ll1.head);
//        ll1.deleteValue(ll1.head, 10);
//        ll1.recursivePrint(ll1.head);


//        linklist cycle
       Node cycleNode =  ll1.createCycle(1);
//        System.out.println(cycleNode.next.val);
        ll1.printCycle();



    }

    //    Node of the Linked List


}