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

    static void main() {

        LinkListPractice ll1 = new LinkListPractice();
        ll1.addFirst(10);
        ll1.addFirst(20);
        ll1.addFirst(30);
//        ll1.print();
        ll1.recursivePrint(ll1.head);
        ll1.deleteValue(ll1.head,20);
        ll1.recursivePrint(ll1.head);
        ll1.deleteValue(ll1.head, 10);
        ll1.recursivePrint(ll1.head);
    }

    //    Node of the Linked List


}