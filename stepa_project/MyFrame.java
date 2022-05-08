package stepa.project;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;
import java.util.List;


public class MyFrame extends JFrame implements ActionListener {

    public final JButton button_ADD;
    public final JButton button_RESET;
    public final JButton button_SOLVE;

    public final JButton button_ADD_RANDOM;
    public final JButton button_SAVE_TO_FILE;
    public final JButton button_READ_FILE;


    public final JTextField textFIELD;
    public final JTextArea textFIELD2;
    public final JTextComponent textArea;

    public final JTextField countOfRandomCircles;


    Solution solve = new Solution(new ArrayList<>());

    List<Point> allPoints = new ArrayList<>();


    public String s1, s2;
    public StringBuilder num1 = new StringBuilder();
    public StringBuilder num2 = new StringBuilder();
    public double a, b;
    public int index = 0;


    public MyFrame(String title) {
        super(title);

        textArea = new JTextArea("          Условие задачи :" + "\n    Среди всего множества окружностей " + "\n    найти пару окружностей," +
                " такую," + "\n    что длина их общей хорды будет максимальна");
        textArea.setBounds(1000, 10, 350, 70);

        button_SOLVE = new JButton("РЕШИТЬ");
        button_SOLVE.setBounds(1000, 250, 175, 50);
        button_SOLVE.addActionListener(this);

        button_RESET = new JButton("СБРОС");
        button_RESET.setBounds(1180, 250, 175, 50);
        button_RESET.addActionListener(this);

        button_ADD = new JButton("ДОБАВИТЬ");
        button_ADD.setBounds(1000, 180, 350, 50);
        button_ADD.addActionListener(this);

        button_ADD_RANDOM = new JButton("СГЕНЕРИРОВАТЬ РАНДОМНО");
        button_ADD_RANDOM.setBounds(1070, 700, 280, 50);
        button_ADD_RANDOM.addActionListener(this);

        button_SAVE_TO_FILE = new JButton("СОХРАНИТЬ В ФАЙЛ");
        button_SAVE_TO_FILE.setBounds(1000, 620, 150, 50);
        button_SAVE_TO_FILE.addActionListener(this);

        button_READ_FILE = new JButton("ДОБАВИТЬ ИЗ ФАЙЛА");
        button_READ_FILE.setBounds(1200, 620, 150, 50);
        button_READ_FILE.addActionListener(this);

        countOfRandomCircles = new JTextField();
        countOfRandomCircles.setBounds(1000, 700, 50, 50);

        textFIELD = new HintTextField("Сюда вводить 4  координаты");
        textFIELD.setBounds(1000, 100, 350, 50);

        textFIELD2 = new JTextArea("Последняя веденная точка :");
        textFIELD2.setBounds(1000, 400, 350, 200);


        add(countOfRandomCircles);
        add(textFIELD);
        add(textFIELD2);
        add(textArea);

        add(button_ADD);
        add(button_SOLVE);
        add(button_RESET);
        add(button_ADD_RANDOM);
        add(button_SAVE_TO_FILE);
        add(button_READ_FILE);

        setLayout(null);

        setSize(1400, 900);
        getContentPane().setBackground(new Color(26, 67, 103));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addMouseListener(new MyMouseListener());
        setVisible(true);
    }


    public void drawCircle(Graphics g, Circle newCircle) {
        g.drawOval((int) (newCircle.point.x - newCircle.radius), (int) (newCircle.point.y - newCircle.radius), (int) newCircle.radius * 2, (int) newCircle.radius * 2);
    }

    @Override
    public void paint(Graphics g1) {
        super.paint(g1);

        Graphics2D g = (Graphics2D) g1;
        BasicStroke pen1 = new BasicStroke(5);
        g.setStroke(pen1);
        g.setColor(Color.WHITE);

        for (Point allPoint : allPoints) {
            g.drawOval((int) allPoint.x - 3, (int) allPoint.y - 3, 5, 5);
            g.fillOval((int) allPoint.x - 3, (int) allPoint.y - 3, 5, 5);
        }

        for (int i = 0; i < solve.circles.size(); i++) {
            Circle newCircle = solve.circles.get(i);
            g.setColor(Color.BLACK);
            drawCircle(g, newCircle);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (allPoints.size() > 0) {
                textFIELD2.setText("Последняя введенная точка : \n    " + allPoints.get(allPoints.size() - 1));
            }
        } catch (IndexOutOfBoundsException e1) {
            e1.printStackTrace();
        }
        if (e.getSource() == button_ADD) {
            try {
                String data = textFIELD.getText();
                data += " ";

                StringBuilder newDATA = new StringBuilder();

                for (int i = 0; i < data.length(); i++) {
                    if (data.charAt(i) != '(' && data.charAt(i) != ')') {
                        newDATA.append(data.charAt(i));
                    }
                }

                List<Double> coordinates = new ArrayList<>(4);
                StringBuilder coord = new StringBuilder();

                for (int i = 0; i < newDATA.length(); i++) {
                    if (!Character.isWhitespace(newDATA.charAt(i))) {
                        coord.append(newDATA.charAt(i));
                    } else {
                        if (coord.length() != 0) {
                            coordinates.add(Double.parseDouble(coord.toString()));
                            coord = new StringBuilder();
                        }
                    }
                }

                Point centre = new Point(coordinates.get(0), coordinates.get(1));
                Point pointOnCircle = new Point(coordinates.get(2), coordinates.get(3));

                System.out.println(coordinates);

                allPoints.add(centre);
                allPoints.add(pointOnCircle);

                solve.circles.add(new Circle(centre, pointOnCircle));
                coordinates.clear();

                repaint();
            } catch (NumberFormatException err) {
                System.out.println("Вы неправильно ввели координаты :" + textFIELD.getText());
            }
        }

        if (e.getSource() == button_RESET) {
            getContentPane().repaint();
            allPoints.clear();
            solve.circles.clear();
        }

        if (e.getSource() == button_SOLVE) {
            showSolution(getGraphics());
            textFIELD2.setText("        Задача решена!\n"
                    + " Нужная пара окружностей :\n"
                    + "    Первая окружность :\n   " + solve.c1 + "\n" + "\n"
                    + "    Вторая окружность :\n        " + solve.c2);
        }

        if (e.getSource() == button_ADD_RANDOM) {
            int count = Integer.parseInt(countOfRandomCircles.getText());
            if (count < 0) {
                throw new NullPointerException();
            }

            solve.addRandomCircles(solve.circles, count, allPoints);

            repaint();
        }

        if (e.getSource() == button_SAVE_TO_FILE) {
            try {
                File output = new File("out.txt");
                BufferedWriter out = new BufferedWriter(new FileWriter(output));

                File outputPoints = new File("outPoints.txt");
                BufferedWriter outP = new BufferedWriter(new FileWriter(outputPoints));

                List<Circle> circles = solve.circles;
                for (int i = 0; i < circles.size(); i++) {
                    Circle circle = circles.get(i);
                    out.write(i + 1 + " окружность:");
                    out.write(circle + "\n");
                    out.write("\n");
                }

                List<Point> points = allPoints;
                for (Point p : points) {
                    outP.write(p.x + " " + p.y + "\n");
                }

                outP.close();
                out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (e.getSource() == button_READ_FILE) {
            try {
                Scanner in = new Scanner(new File("in.txt"));
                while (in.hasNextLine()) {
                    s1 = in.nextLine();
                    s2 = in.nextLine();


                    if (Objects.equals(s1, "")
                            || Objects.equals(s2, "")) {
                        break;
                    } else {
                        allPoints.add(getPoint(s1));
                        allPoints.add(getPoint(s2));


                        solve.circles.add(new Circle(getPoint(s1), getPoint(s2)));
                        repaint();
                    }
                }
            } catch (NoSuchElementException | FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }


    public Point getPoint(String line) {
        num1 = new StringBuilder();
        num2 = new StringBuilder();


        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) != ' ') {
                num1.append(line.charAt(i));
            } else {
                index = i;
                break;
            }
        }

        for (int j = index; j < line.length(); j++) {
            num2.append(line.charAt(j));
        }

        a = Double.parseDouble(num1.toString());
        b = Double.parseDouble(num2.toString());

        return new Point(a, b);
    }

    private void showSolution(Graphics g1) {
        super.paint(g1);

        Graphics2D g = (Graphics2D) g1;
        BasicStroke pen1 = new BasicStroke(5);
        g.setStroke(pen1);
        g.setColor(Color.WHITE);

        for (Point allPoint : allPoints) {
            g.drawOval((int) allPoint.x - 3, (int) allPoint.y - 3, 5, 5);
            g.fillOval((int) allPoint.x - 3, (int) allPoint.y - 3, 5, 5);
        }


        for (int i = 0; i < solve.circles.size(); i++) {
            Circle newCircle = solve.circles.get(i);
            g.setColor(Color.BLACK);
            drawCircle(g, newCircle);
        }


        List<Line> lines = solve.findLines(solve.circles);


//        for (Line value : lines) {
//            g.drawLine((int) value.p1.x, (int) value.p1.y, (int) value.p2.x, (int) value.p2.y);
//        }

        Line line = solve.findMaxLength(lines);

        g.setColor(new Color(27, 255, 0));


        drawCircle(g, line.c1);
        drawCircle(g, line.c2);
        g.setColor(new Color(197, 91, 204, 255));
        g.drawLine((int) line.p1.x, (int) line.p1.y, (int) line.p2.x, (int) line.p2.y);
    }

    private class MyMouseListener implements MouseListener {

        Point rememberedPoint;

        @Override
        public void mouseClicked(MouseEvent e) {
            Point newPoint = new Point(e.getX(), e.getY());
            allPoints.add(newPoint);
            if (rememberedPoint != null) {
                Circle circle = new Circle(rememberedPoint, newPoint);
                solve.circles.add(circle);
                rememberedPoint = null;
                repaint();
            } else {
                rememberedPoint = newPoint;
            }

            textFIELD2.setText("Последняя введенная точка : \n    " + allPoints.get(allPoints.size() - 1));
            try {
                if (solve.circles.size() > 0) {
                    textFIELD2.append("\nПоследняя введенная окружность : \n" + solve.circles.get(solve.circles.size() - 1));
                }
            } catch (IndexOutOfBoundsException e1) {
                e1.printStackTrace();
            }

            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public static void main(String[] args) {
        new MyFrame("project");
    }
}


