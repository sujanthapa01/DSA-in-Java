import java.util.HashMap;
import java.util.Map;
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
        Node current = head;

        while(current != null){

            for(Map.Entry<Integer,String> entry : current.records.entrySet()){
                System.out.print(entry.getKey() + ":" + entry.getValue() + " -> ");
            }
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


    void main(){

        LinkListProject ll1 = new LinkListProject();

        ll1.addFirst(4658, "Sujan Thapa");
        ll1.addFirst(4661, "Sujal Thapa");
        ll1.addFirst(4659, "Rishika Gurung");
        ll1.addFirst(4660, "Abhijay Joli");
        ll1.print();
        ll1.sort();
        System.out.println();
        ll1.print();

    }


}