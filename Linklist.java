class Linklist {

    Node head;

    class Node {
        int value;
        Node next;

        Node(int x) {
            value = x;
            next = null;
        }
    }

    public Node add_forward(int data) {

        Node newNode = new Node(data);

        newNode.next = head;

        head = newNode;

        return head;
    }

    public boolean delFirst(){
        if(head == null) return false;
        head = head.next;
        return true;
    }

    public boolean delValue(int target){

        if(head == null){
            return false;
        }

        if(head.value == target){
            head = head.next;
            return true;
        }

        Node current = head;

        while(current.next != null && current.next.value != target){
            current = current.next;
        }

        if(current.next == null){
            return false;
        }


        current.next = current.next.next;
        return true;
    }

    public void print(){
        Node current = head;

        while(current!=null){
            System.out.print(current.value);
            if(current.next != null){
                System.out.print(" -> ");
            }else if(current.next == null){
                System.out.println( " -> null");
            }
            current = current.next;
        }
    }


    public static void main() {
        System.out.println("this is a linklist class!");

        Linklist ll1 = new Linklist();

        ll1.add_forward(10);
        ll1.add_forward(20);
        ll1.add_forward(30);

//        Display Linklist
        ll1.print();

        ll1.delFirst();
        ll1.delValue(10);

        ll1.print();

    }
}