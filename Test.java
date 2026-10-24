
import javax.swing.*;
import java.awt.*;

class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}

class LinkList {
    Node head;

    void add(int value) {
        Node newNode = new Node(value);

        if (head == null) {
            head = newNode;
            return;
        }

        Node current = head;

        while (current.next != null) {
            current = current.next;
        }

        current.next = newNode;
    }
}

public class Main extends JFrame {

    LinkList list = new LinkList();
    JTextField inputField = new JTextField(10);
    JPanel listPanel = new JPanel();

    Main() {
        setTitle("Linked List Visualizer");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JPanel controls = new JPanel();
        JButton addButton = new JButton("Add Node");

        controls.add(new JLabel("Enter value:"));
        controls.add(inputField);
        controls.add(addButton);

        listPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 30));
        listPanel.setBackground(Color.WHITE);

        add(controls, BorderLayout.NORTH);
        add(new JScrollPane(listPanel), BorderLayout.CENTER);

        addButton.addActionListener(e -> addNode());

        inputField.addActionListener(e -> addNode());

        setVisible(true);
    }

    void addNode() {
        try {
            int value = Integer.parseInt(inputField.getText());

            list.add(value);
            renderList();

            inputField.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid integer.");
        }
    }

    void renderList() {
        listPanel.removeAll();

        Node current = list.head;

        listPanel.add(new JLabel("HEAD →"));

        while (current != null) {
            JPanel node = new JPanel(new GridLayout(1, 1));
            node.setPreferredSize(new Dimension(65, 65));
            node.setBackground(new Color(219, 234, 254));
            node.setBorder(
                    BorderFactory.createLineBorder(Color.BLUE, 2)
            );

            JLabel value = new JLabel(
                    String.valueOf(current.data),
                    SwingConstants.CENTER
            );

            value.setFont(new Font("Arial", Font.BOLD, 20));
            node.add(value);

            listPanel.add(node);
            listPanel.add(new JLabel("→"));

            current = current.next;
        }

        listPanel.add(new JLabel("null"));

        listPanel.revalidate();
        listPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
