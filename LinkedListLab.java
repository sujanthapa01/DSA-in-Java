import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Linked List Lab - a Swing visualizer for singly, doubly and circular linked lists.
 *
 * Compile: javac LinkedListLab.java
 * Run:     java LinkedListLab
 */
public class LinkedListLab extends JFrame {

    // ======================================================================
    // Model
    // ======================================================================

    enum ListType {
        SINGLY("Singly Linked List"),
        DOUBLY("Doubly Linked List"),
        CIRCULAR("Circular Linked List");

        final String label;

        ListType(String label) { this.label = label; }

        @Override public String toString() { return label; }
    }

    static final class Node {
        final String value;
        Node next, prev;

        Node(String value) { this.value = value; }
    }

    /** A real pointer-based list. {@link #relink()} enforces the invariants of the current type. */
    static final class ListModel {
        ListType type = ListType.SINGLY;
        Node head;
        int size;

        Node nodeAt(int index) {
            Node n = head;
            while (index-- > 0) n = n.next;
            return n;
        }

        Node tail() { return size == 0 ? null : nodeAt(size - 1); }

        List<Node> nodes() {
            List<Node> out = new ArrayList<>(size);
            Node n = head;
            for (int i = 0; i < size; i++) {
                out.add(n);
                n = n.next;
            }
            return out;
        }

        int indexOf(String value) {
            Node n = head;
            for (int i = 0; i < size; i++, n = n.next) {
                if (n.value.equals(value)) return i;
            }
            return -1;
        }

        Node insertAt(int index, String value) {
            Node node = new Node(value);
            if (index == 0) {
                node.next = head;
                head = node;
            } else {
                Node before = nodeAt(index - 1);
                node.next = before.next;
                before.next = node;
            }
            size++;
            relink();
            return node;
        }

        void deleteAt(int index) {
            if (index == 0) {
                head = size == 1 ? null : head.next;
            } else {
                Node before = nodeAt(index - 1);
                before.next = before.next.next;
            }
            size--;
            relink();
        }

        void rotateLeft(int k) {
            if (size < 2) return;
            k %= size;
            if (k == 0) return;
            Node oldTail = nodeAt(size - 1);
            Node newTail = nodeAt(k - 1);
            Node newHead = newTail.next;
            oldTail.next = head;
            head = newHead;
            newTail.next = null;
            relink();
        }

        void rotateRight(int k) {
            if (size < 2) return;
            k %= size;
            if (k != 0) rotateLeft(size - k);
        }

        void setType(ListType newType) {
            type = newType;
            relink();
        }

        void clear() {
            head = null;
            size = 0;
        }

        /** Rebuilds prev pointers and the tail's next pointer according to the list type. */
        private void relink() {
            if (size == 0) return;
            Node cur = head, before = null;
            for (int i = 0; i < size; i++) {
                Node following = cur.next;
                if (i == size - 1) cur.next = type == ListType.CIRCULAR ? head : null;
                cur.prev = type == ListType.DOUBLY ? before : null;
                before = cur;
                cur = following;
            }
        }
    }

    // ======================================================================
    // Rendering
    // ======================================================================

    private static final int NODE_H = 44, DATA_W = 60, PTR_W = 26, GAP = 64, MARGIN = 70;
    private static final int NODE_RAD = 26, STEP_MS = 450;

    private static final Color BG = new Color(0xFAFBFD);
    private static final Color NODE_FILL = new Color(0xE3EDFC);
    private static final Color NODE_BORDER = new Color(0x2F4B7C);
    private static final Color TEXT = new Color(0x1B2A41);
    private static final Color MUTED = new Color(0x78849A);
    private static final Color NEXT_C = new Color(0x263238);
    private static final Color PREV_C = new Color(0x8E24AA);
    private static final Color LOOP_C = new Color(0xE65100);
    private static final Color HEAD_C = new Color(0x2E7D32);
    private static final Color TAIL_C = new Color(0xAD1457);
    private static final Color VISIT = new Color(0xFFE082);
    private static final Color FOUND = new Color(0xA5D6A7);
    private static final Color REMOVE = new Color(0xEF9A9A);
    private static final Color FRESH = new Color(0x90CAF9);
    private static final Color ERROR = new Color(0xB71C1C);

    private static final Stroke LINK = new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final Stroke LOOP = new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final Stroke BORDER = new BasicStroke(2f);
    private static final Font TAG_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 11);
    private static final Font SMALL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);

    static final class Canvas extends JPanel implements Scrollable {
        private final ListModel model;
        final Map<Node, Color> marks = new HashMap<>();
        boolean circle;

        Canvas(ListModel model) {
            this.model = model;
            setBackground(BG);
        }

        private int nodeW() {
            return model.type == ListType.DOUBLY ? DATA_W + 2 * PTR_W : DATA_W + PTR_W;
        }

        private static double circleRadius(int n) {
            return Math.max(110, n * (2 * NODE_RAD + 36) / (2 * Math.PI));
        }

        @Override public Dimension getPreferredSize() {
            int n = model.size;
            if (circle) {
                int d = (int) Math.max(420, 2 * (circleRadius(n) + NODE_RAD + 70));
                return new Dimension(d, d);
            }
            return new Dimension(Math.max(500, 2 * MARGIN + n * (nodeW() + GAP) - GAP), 300);
        }

        // --- Scrollable: stretch to the viewport when content is smaller, scroll otherwise ---
        @Override public Dimension getPreferredScrollableViewportSize() { return new Dimension(900, 400); }
        @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 24; }
        @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) {
            return o == SwingConstants.HORIZONTAL ? r.width : r.height;
        }
        @Override public boolean getScrollableTracksViewportWidth() {
            return getParent() instanceof JViewport && getParent().getWidth() > getPreferredSize().width;
        }
        @Override public boolean getScrollableTracksViewportHeight() {
            return getParent() instanceof JViewport && getParent().getHeight() > getPreferredSize().height;
        }

        @Override protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            drawLegend(g);
            if (model.size == 0) {
                g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
                g.setColor(MUTED);
                drawText(g, "Empty list - insert a node to begin", getWidth() / 2.0, getHeight() / 2.0, 0.5);
            } else if (circle) {
                paintCircle(g);
            } else {
                paintLinear(g);
            }
            g.dispose();
        }

        private void drawLegend(Graphics2D g) {
            double x = 16, y = 20;
            g.setStroke(LINK);
            g.setFont(SMALL_FONT);
            g.setColor(NEXT_C);
            arrow(g, x, y, x + 28, y);
            drawText(g, "next", x + 36, y, 0);
            x += 90;
            if (model.type == ListType.DOUBLY) {
                g.setColor(PREV_C);
                arrow(g, x, y, x + 28, y);
                drawText(g, "prev", x + 36, y, 0);
                x += 90;
            }
            if (model.type == ListType.CIRCULAR) {
                g.setColor(LOOP_C);
                arrow(g, x, y, x + 28, y);
                drawText(g, "tail.next = head", x + 36, y, 0);
            }
        }

        // ---------------------------------------------------------------- linear view

        private void paintLinear(Graphics2D g) {
            List<Node> nodes = model.nodes();
            int n = nodes.size();
            boolean dbl = model.type == ListType.DOUBLY, circ = model.type == ListType.CIRCULAR;
            int w = nodeW(), pitch = w + GAP;
            int x0 = Math.max(MARGIN, (getWidth() - (n * pitch - GAP)) / 2);
            int top = Math.max(80, (getHeight() - NODE_H) / 2 - 10);
            double cy = top + NODE_H / 2.0;
            double nextY = dbl ? cy - 9 : cy, prevY = cy + 9;

            for (int i = 0; i < n; i++) {
                int x = x0 + i * pitch;
                drawLinearNode(g, nodes.get(i), x, top, w, dbl);

                g.setFont(SMALL_FONT);
                g.setColor(MUTED);
                drawText(g, String.valueOf(i), x + w / 2.0, top + NODE_H + 16, 0.5);

                if (i < n - 1) {
                    g.setStroke(LINK);
                    g.setColor(NEXT_C);
                    arrow(g, x + w - PTR_W / 2.0, nextY, x + pitch, nextY);
                    if (dbl) {
                        g.setColor(PREV_C);
                        arrow(g, x + pitch + PTR_W / 2.0, prevY, x + w, prevY);
                    }
                }
            }

            // head / tail tags
            int lastX = x0 + (n - 1) * pitch;
            if (n == 1) tag(g, "HEAD / TAIL", x0 + w / 2.0, top, HEAD_C);
            else {
                tag(g, "HEAD", x0 + w / 2.0, top, HEAD_C);
                tag(g, "TAIL", lastX + w / 2.0, top, TAIL_C);
            }

            double tailDotX = lastX + w - PTR_W / 2.0;
            g.setStroke(LINK);
            if (circ) {
                // loop: tail.next -> head
                double headX = x0 + DATA_W / 2.0, loopY = top + NODE_H + 40;
                Path2D p = new Path2D.Double();
                p.moveTo(tailDotX, cy);
                p.lineTo(tailDotX, loopY);
                p.lineTo(headX, loopY);
                p.lineTo(headX, top + NODE_H + 2);
                g.setStroke(LOOP);
                g.setColor(LOOP_C);
                g.draw(p);
                arrowHead(g, headX, loopY, headX, top + NODE_H);
                g.setFont(SMALL_FONT);
                drawText(g, "tail.next = head", (tailDotX + headX) / 2.0, loopY + 14, 0.5);
            } else {
                g.setColor(NEXT_C);
                arrow(g, tailDotX, nextY, lastX + w + 26, nextY);
                g.setFont(SMALL_FONT);
                g.setColor(MUTED);
                drawText(g, "null", lastX + w + 30, nextY, 0);
                if (dbl) {
                    g.setColor(PREV_C);
                    arrow(g, x0 + PTR_W / 2.0, prevY, x0 - 26, prevY);
                    g.setColor(MUTED);
                    drawText(g, "null", x0 - 30, prevY, 1);
                }
            }

            // pointer dots on top of everything
            for (int i = 0; i < n; i++) {
                int x = x0 + i * pitch;
                g.setColor(NEXT_C);
                dot(g, x + w - PTR_W / 2.0, nextY);
                if (dbl) {
                    g.setColor(PREV_C);
                    dot(g, x + PTR_W / 2.0, prevY);
                }
            }
        }

        private void drawLinearNode(Graphics2D g, Node node, int x, int y, int w, boolean dbl) {
            Color fill = marks.getOrDefault(node, NODE_FILL);
            Shape box = new RoundRectangle2D.Double(x, y, w, NODE_H, 12, 12);
            g.setColor(fill);
            g.fill(box);

            Shape oldClip = g.getClip();
            g.clip(box);
            g.setColor(shade(fill, 0.86));
            if (dbl) {
                g.fillRect(x, y, PTR_W, NODE_H);
                g.fillRect(x + w - PTR_W, y, PTR_W, NODE_H);
            } else {
                g.fillRect(x + DATA_W, y, PTR_W, NODE_H);
            }
            g.setClip(oldClip);

            g.setStroke(BORDER);
            g.setColor(NODE_BORDER);
            g.draw(box);
            int d1 = dbl ? x + PTR_W : x + DATA_W;
            g.drawLine(d1, y, d1, y + NODE_H);
            if (dbl) g.drawLine(x + w - PTR_W, y, x + w - PTR_W, y + NODE_H);

            g.setColor(TEXT);
            drawFit(g, node.value, (dbl ? x + PTR_W : x) + DATA_W / 2.0, y + NODE_H / 2.0, DATA_W - 10, 15);
        }

        private void tag(Graphics2D g, String text, double cx, int top, Color c) {
            g.setColor(c);
            g.setFont(TAG_FONT);
            drawText(g, text, cx, top - 26, 0.5);
            g.setStroke(LINK);
            arrow(g, cx, top - 17, cx, top - 3);
        }

        // ---------------------------------------------------------------- circle view

        private void paintCircle(Graphics2D g) {
            List<Node> nodes = model.nodes();
            int n = nodes.size();
            boolean dbl = model.type == ListType.DOUBLY, circ = model.type == ListType.CIRCULAR;
            double R = n == 1 ? 0 : circleRadius(n);
            double cx = getWidth() / 2.0, cy = getHeight() / 2.0;

            Point2D.Double[] pos = new Point2D.Double[n];
            for (int i = 0; i < n; i++) {
                double a = -Math.PI / 2 + 2 * Math.PI * i / n;
                pos[i] = new Point2D.Double(cx + R * Math.cos(a), cy + R * Math.sin(a));
            }

            g.setStroke(LINK);
            int links = circ ? n : n - 1;
            for (int i = 0; i < links; i++) {
                Point2D a = pos[i], b = pos[(i + 1) % n];
                g.setColor(circ ? (i == n - 1 ? LOOP_C : NEXT_C) : NEXT_C);
                if (n == 1) {
                    g.setColor(LOOP_C);
                    selfLoop(g, a);
                    break;
                }
                circleArrow(g, a, b, dbl ? 14 : (n == 2 && circ ? 30 : 0));
                if (dbl) {
                    g.setColor(PREV_C);
                    circleArrow(g, b, a, 14);
                }
            }

            // null stub on the tail for non-circular lists
            if (!circ) {
                Point2D.Double t = pos[n - 1];
                double dx = n == 1 ? 1 : t.x - cx, dy = n == 1 ? 0 : t.y - cy;
                double len = Math.hypot(dx, dy);
                dx /= len;
                dy /= len;
                g.setColor(NEXT_C);
                arrow(g, t.x + dx * NODE_RAD, t.y + dy * NODE_RAD, t.x + dx * (NODE_RAD + 26), t.y + dy * (NODE_RAD + 26));
                g.setColor(MUTED);
                g.setFont(SMALL_FONT);
                drawText(g, "null", t.x + dx * (NODE_RAD + 44), t.y + dy * (NODE_RAD + 44), 0.5);
            }

            for (int i = 0; i < n; i++) {
                Node node = nodes.get(i);
                Point2D.Double p = pos[i];
                Shape circleShape = new Ellipse2D.Double(p.x - NODE_RAD, p.y - NODE_RAD, 2 * NODE_RAD, 2 * NODE_RAD);
                g.setColor(marks.getOrDefault(node, NODE_FILL));
                g.fill(circleShape);
                g.setStroke(BORDER);
                g.setColor(NODE_BORDER);
                g.draw(circleShape);
                g.setColor(TEXT);
                drawFit(g, node.value, p.x, p.y + 4, 2 * NODE_RAD - 14, 14);
                g.setColor(MUTED);
                g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
                drawText(g, String.valueOf(i), p.x, p.y - 12, 0.5);
            }

            // tags
            g.setFont(TAG_FONT);
            g.setColor(HEAD_C);
            drawText(g, n == 1 ? "HEAD / TAIL" : "HEAD", pos[0].x, pos[0].y - NODE_RAD - 12, 0.5);
            if (n > 1) {
                Point2D.Double t = pos[n - 1];
                double dx = cx - t.x, dy = cy - t.y, len = Math.hypot(dx, dy);
                g.setColor(TAIL_C);
                drawText(g, "TAIL", t.x + dx / len * (NODE_RAD + 16), t.y + dy / len * (NODE_RAD + 16), 0.5);
            }
        }

        /** Straight (bend = 0) or curved arrow between the boundaries of two circular nodes. */
        private void circleArrow(Graphics2D g, Point2D a, Point2D b, double bend) {
            double dx = b.getX() - a.getX(), dy = b.getY() - a.getY();
            double len = Math.hypot(dx, dy);
            if (len == 0) return;
            double ux = dx / len, uy = dy / len;
            double cx = (a.getX() + b.getX()) / 2 - uy * bend, cy = (a.getY() + b.getY()) / 2 + ux * bend;

            double sx = cx - a.getX(), sy = cy - a.getY(), sl = Math.hypot(sx, sy);
            double ex = cx - b.getX(), ey = cy - b.getY(), el = Math.hypot(ex, ey);
            double x1 = a.getX() + sx / sl * NODE_RAD, y1 = a.getY() + sy / sl * NODE_RAD;
            double x2 = b.getX() + ex / el * (NODE_RAD + 2), y2 = b.getY() + ey / el * (NODE_RAD + 2);

            g.draw(new QuadCurve2D.Double(x1, y1, cx, cy, x2, y2));
            arrowHead(g, cx, cy, x2, y2);
        }

        private void selfLoop(Graphics2D g, Point2D p) {
            double a1 = Math.toRadians(-45), a2 = Math.toRadians(45);
            double x1 = p.getX() + NODE_RAD * Math.cos(a1), y1 = p.getY() + NODE_RAD * Math.sin(a1);
            double x2 = p.getX() + (NODE_RAD + 2) * Math.cos(a2), y2 = p.getY() + (NODE_RAD + 2) * Math.sin(a2);
            double c1x = p.getX() + NODE_RAD + 70, c1y = p.getY() - 70;
            double c2x = p.getX() + NODE_RAD + 70, c2y = p.getY() + 70;
            g.draw(new CubicCurve2D.Double(x1, y1, c1x, c1y, c2x, c2y, x2, y2));
            arrowHead(g, c2x, c2y, x2, y2);
        }
    }

    // ---------------------------------------------------------------- drawing helpers

    private static void arrow(Graphics2D g, double x1, double y1, double x2, double y2) {
        g.draw(new Line2D.Double(x1, y1, x2, y2));
        arrowHead(g, x1, y1, x2, y2);
    }

    private static void arrowHead(Graphics2D g, double fromX, double fromY, double toX, double toY) {
        double angle = Math.atan2(toY - fromY, toX - fromX), len = 10, spread = Math.PI / 7;
        Path2D head = new Path2D.Double();
        head.moveTo(toX, toY);
        head.lineTo(toX - len * Math.cos(angle - spread), toY - len * Math.sin(angle - spread));
        head.lineTo(toX - len * Math.cos(angle + spread), toY - len * Math.sin(angle + spread));
        head.closePath();
        g.fill(head);
    }

    private static void dot(Graphics2D g, double x, double y) {
        g.fill(new Ellipse2D.Double(x - 3.5, y - 3.5, 7, 7));
    }

    /** anchor: 0 = left aligned at x, 0.5 = centered on x, 1 = right aligned at x. */
    private static void drawText(Graphics2D g, String s, double x, double cy, double anchor) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, (float) (x - anchor * fm.stringWidth(s)), (float) (cy + (fm.getAscent() - fm.getDescent()) / 2.0));
    }

    private static void drawFit(Graphics2D g, String s, double cx, double cy, int maxW, int size) {
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, size);
        while (size > 8 && g.getFontMetrics(f).stringWidth(s) > maxW) f = f.deriveFont((float) --size);
        g.setFont(f);
        drawText(g, s, cx, cy, 0.5);
    }

    private static Color shade(Color c, double factor) {
        return new Color((int) (c.getRed() * factor), (int) (c.getGreen() * factor), (int) (c.getBlue() * factor));
    }

    // ======================================================================
    // Frame / controller
    // ======================================================================

    private final ListModel model = new ListModel();
    private final Canvas canvas = new Canvas(model);
    private final List<JComponent> controls = new ArrayList<>();
    private final JComboBox<ListType> typeBox = new JComboBox<>(ListType.values());
    private final JTextField valueField = new JTextField(8);
    private final JSpinner indexSpin = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
    private final JSpinner stepsSpin = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private final JToggleButton circleBtn = new JToggleButton("Create Circle");
    private final JLabel statusLabel = new JLabel(" ");
    private final JLabel infoLabel = new JLabel(" ");
    private Timer anim;

    public LinkedListLab() {
        super("Linked List Lab");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        controls.add(typeBox);
        controls.add(valueField);
        controls.add(indexSpin);
        controls.add(stepsSpin);
        controls.add(circleBtn);

        JPanel row1 = row(label("List type"), typeBox, label("Value"), valueField, label("Index"), indexSpin,
                label("Steps"), stepsSpin, circleBtn,
                button("Sample", this::sample), button("Clear", this::clear));
        JPanel row2 = row(
                button("Insert at Head", this::insertHead),
                button("Insert at Tail", this::insertTail),
                button("Insert at Index", this::insertAtIndex),
                button("Search", this::search),
                button("Delete", this::delete),
                button("Rotate Left", () -> rotate(true)),
                button("Rotate Right", () -> rotate(false)));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(6, 8, 2, 8));
        top.add(row1);
        top.add(row2);

        JPanel bottom = new JPanel(new BorderLayout(12, 0));
        bottom.setBorder(new MatteBorder(1, 0, 0, 0, new Color(0xD5DBE5)));
        statusLabel.setBorder(new EmptyBorder(6, 12, 6, 0));
        infoLabel.setBorder(new EmptyBorder(6, 0, 6, 12));
        infoLabel.setForeground(MUTED);
        bottom.add(statusLabel, BorderLayout.CENTER);
        bottom.add(infoLabel, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(canvas), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        valueField.addActionListener(e -> insertTail());
        typeBox.addActionListener(e -> {
            ListType t = (ListType) typeBox.getSelectedItem();
            model.setType(t);
            canvas.marks.clear();
            status("Switched to " + t + (t == ListType.CIRCULAR ? " - tail.next now points to head" : ""), false);
            redraw();
        });
        circleBtn.addActionListener(e -> {
            canvas.circle = circleBtn.isSelected();
            circleBtn.setText(canvas.circle ? "Linear View" : "Create Circle");
            redraw();
        });

        sample();
        setSize(1120, 640);
        setLocationRelativeTo(null);
    }

    // ---------------------------------------------------------------- UI helpers

    private JPanel row(Component... parts) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        for (Component c : parts) p.add(c);
        return p;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(MUTED);
        return l;
    }

    private JButton button(String text, Runnable action) {
        JButton b = new JButton(text);
        b.addActionListener(e -> action.run());
        controls.add(b);
        return b;
    }

    private void busy(boolean busy) {
        for (JComponent c : controls) c.setEnabled(!busy);
    }

    private void status(String msg, boolean error) {
        statusLabel.setForeground(error ? ERROR : TEXT);
        statusLabel.setText(msg);
    }

    private void redraw() {
        canvas.revalidate();
        canvas.repaint();
        Node tail = model.tail();
        infoLabel.setText(String.format("%s  |  size %d  |  head %s  |  tail %s", model.type, model.size,
                model.head == null ? "-" : model.head.value, tail == null ? "-" : tail.value));
    }

    /** Reads and validates the value field; returns null (and shows an error) if invalid. */
    private String readValue() {
        String v = valueField.getText().trim();
        if (v.isEmpty()) {
            status("Enter a value first", true);
            return null;
        }
        if (v.length() > 8) {
            status("Value is limited to 8 characters", true);
            return null;
        }
        return v;
    }

    /** Animates a walk from the head to lastIdx, then runs onDone. The last node is painted endColor. */
    private void traverse(int lastIdx, Color endColor, Runnable onDone) {
        canvas.marks.clear();
        if (lastIdx < 0) {
            onDone.run();
            return;
        }
        List<Node> nodes = model.nodes();
        int[] i = {0};
        busy(true);
        anim = new Timer(STEP_MS, null);
        anim.setInitialDelay(0);
        anim.addActionListener(e -> {
            if (i[0] > lastIdx) {
                anim.stop();
                busy(false);
                onDone.run();
                canvas.repaint();
                return;
            }
            canvas.marks.clear();
            canvas.marks.put(nodes.get(i[0]), i[0] == lastIdx ? endColor : VISIT);
            status("Traversing... at index " + i[0] + " (value " + nodes.get(i[0]).value + ")", false);
            i[0]++;
            canvas.repaint();
        });
        anim.start();
    }

    // ---------------------------------------------------------------- operations

    private void insertHead() {
        String v = readValue();
        if (v != null) commitInsert(0, v);
    }

    private void insertTail() {
        String v = readValue();
        if (v != null) commitInsert(model.size, v);
    }

    private void insertAtIndex() {
        String v = readValue();
        if (v == null) return;
        int idx = (Integer) indexSpin.getValue();
        if (idx > model.size) {
            status("Index out of range - valid range is 0.." + model.size, true);
            return;
        }
        traverse(idx - 1, VISIT, () -> commitInsert(idx, v));
    }

    private void commitInsert(int idx, String v) {
        canvas.marks.clear();
        Node node = model.insertAt(idx, v);
        canvas.marks.put(node, FRESH);
        status("Inserted \"" + v + "\" at index " + idx, false);
        valueField.selectAll();
        valueField.requestFocusInWindow();
        redraw();
    }

    private void search() {
        String v = readValue();
        if (v == null) return;
        if (model.size == 0) {
            status("List is empty", true);
            return;
        }
        int idx = model.indexOf(v);
        if (idx >= 0) {
            traverse(idx, FOUND, () ->
                    status("Found \"" + v + "\" at index " + idx + " after " + (idx + 1) + " comparison(s)", false));
        } else {
            reportMiss(v, "not found");
        }
    }

    private void delete() {
        String v = readValue();
        if (v == null) return;
        if (model.size == 0) {
            status("List is empty - nothing to delete", true);
            return;
        }
        int idx = model.indexOf(v);
        if (idx < 0) {
            reportMiss(v, "not found - nothing deleted");
            return;
        }
        traverse(idx, REMOVE, () -> {
            canvas.marks.clear();
            model.deleteAt(idx);
            status("Deleted \"" + v + "\" from index " + idx, false);
            redraw();
        });
    }

    private void reportMiss(String v, String suffix) {
        traverse(model.size - 1, VISIT, () -> {
            canvas.marks.clear();
            status("\"" + v + "\" " + suffix + " (" + model.size + " nodes checked)", true);
            redraw();
        });
    }

    private void rotate(boolean left) {
        if (model.size < 2) {
            status("Rotate needs at least 2 nodes", true);
            return;
        }
        int k = (Integer) stepsSpin.getValue();
        canvas.marks.clear();
        if (k % model.size == 0) {
            status("Rotating by a multiple of the list size changes nothing", false);
            redraw();
            return;
        }
        if (left) model.rotateLeft(k);
        else model.rotateRight(k);
        canvas.marks.put(model.head, FRESH);
        status("Rotated " + (left ? "left" : "right") + " by " + k + " - new head: " + model.head.value, false);
        redraw();
    }

    private void sample() {
        Random rnd = new Random();
        model.clear();
        canvas.marks.clear();
        for (int i = 0; i < 5; i++) model.insertAt(model.size, String.valueOf(1 + rnd.nextInt(99)));
        status("Loaded 5 random nodes", false);
        redraw();
    }

    private void clear() {
        model.clear();
        canvas.marks.clear();
        status("List cleared", false);
        redraw();
    }

    // ======================================================================

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // fall back to the default look and feel
        }
        SwingUtilities.invokeLater(() -> new LinkedListLab().setVisible(true));
    }
}
