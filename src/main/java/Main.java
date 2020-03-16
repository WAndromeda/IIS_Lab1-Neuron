import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Main {
    public static String mainPath = "./src/main/resources";
    public static long sumOfIters = 0;
    private static LinkedList<Point> redDots, blueDots;
    private static boolean when = false;
    private static Perceptron p;
    private static double[] idealRedTop = new double[] { 1.0d, 0, 1.0d };
    private static double[] idealRedBot = new double[] { 0, 0, 1.0d };
    private static double[] idealBlue = new double[] { 0, 1.0d, 0 };
    private static ArrayList<ArrayList<Double>> redSetTop;
    private static ArrayList<ArrayList<Double>> redSetBot;
    private static ArrayList<ArrayList<Double>> blueSet;

    public static void main(String[] args) throws IOException {
        redSetTop = new ArrayList<>();
        redSetBot = new ArrayList<>();
        blueSet = new ArrayList<>();
        redDots = new LinkedList<>();
        blueDots = new LinkedList<>();
        p = new Perceptron(3 ,17 ,3);

        Random r = new Random();
        BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));
        int elem;
        init();
        while (true) {
            menu();
            String com = bR.readLine();
            switch (Integer.parseInt(com)) {
                case 1:
                    when = true;
                    int iter = 50000;
                    sumOfIters += iter;
                    for (int i = 0; i < iter; i++){
                        int choice = r.nextInt(3);
                        switch (choice){
                            case 0:
                                elem = r.nextInt(redSetTop.size());
                                p.backPropagation(redSetTop.get(elem), idealRedTop);
                                break;
                            case 1:
                                elem = r.nextInt(blueSet.size());
                                p.backPropagation(blueSet.get(elem), idealBlue);
                                break;
                            case 2:
                                elem = r.nextInt(redSetBot.size());
                                p.backPropagation(redSetBot.get(elem), idealRedBot);
                                break;
                        }
                    }
                    break;
                case 2:
                    System.out.println("Введите координаты X и Y в одну строку");
                    String coordinates = bR.readLine();
                    Point point = new Point(coordinates.split("[\\s]")[0], coordinates.split("[\\s]")[1]);
                    System.out.println(String.format("ЦВЕТ ТОЧКИ: %s", getColor(point)));
                    break;
                case 3:
                    p.resetWeights();
                    break;
                case 4:
                    p.printWeights(when);
                    break;
                default:
                    System.out.println("Нет такой команды, повторите ввод\n");
                case 0:
                    System.exit(0);
            }
        }
    }

    private static void menu(){
        System.out.println("ИИС | Лабораторная работа №1 | Николаев Н.С. | ИКБО-13-17");
        System.out.println("1. Натренировать нейронную сеть");
        System.out.println("2. Ввеси координаты точки и получить её цвет");
        System.out.println("3. Сбросить вес нейронов");
        System.out.println("4. Вывести вес в файлы");
        System.out.println("0. Выйти");
        System.out.print("> ");
    }

    private static void init() throws IOException {
        ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(new File(mainPath + "/red_set_top.txt").toPath(), StandardCharsets.UTF_8);
        String[] nums;


        for (int i = 0; i < lines.size(); i++) {
            nums = lines.get(i).split("[\\s]");
            ArrayList<Double> t = new ArrayList<>();
            t.add(1.0d); t.add(Double.parseDouble(nums[0])); t.add(Double.parseDouble(nums[1]));
            redSetTop.add(t);
        }

        lines = (ArrayList<String>) Files.readAllLines(new File(mainPath + "/red_set_bot.txt").toPath(), StandardCharsets.UTF_8);

        for (int i = 0; i < lines.size(); i++) {
            nums = lines.get(i).split("[\\s]");
            ArrayList<Double> t = new ArrayList<>();
            t.add(1.0d); t.add(Double.parseDouble(nums[0])); t.add(Double.parseDouble(nums[1]));
           redSetBot.add(t);
        }

        lines = (ArrayList<String>) Files.readAllLines(new File(mainPath + "/blue_set.txt").toPath(), StandardCharsets.UTF_8);

        for (int i = 0; i < lines.size(); i++){
            nums = lines.get(i).split("[\\s]");
            ArrayList<Double> t = new ArrayList<>();
            t.add(1.0d); t.add(Double.parseDouble(nums[0])); t.add(Double.parseDouble(nums[1]));
            blueSet.add(t);
        }

        for (ArrayList<Double> tmp : redSetBot)
            redDots.addLast(new Point(tmp.get(1), tmp.get(2)));

        for (ArrayList<Double> tmp : redSetTop)
            redDots.addLast(new Point(tmp.get(1), tmp.get(2)));

        for (ArrayList<Double> tmp : blueSet)
            blueDots.addLast(new Point(tmp.get(1), tmp.get(2)));
    }

    private static String getColor(Point point){
        String dotColor = "";
        ArrayList<Double> tmpData = new ArrayList<>();
        tmpData.add(1.0d); tmpData.add(point.x); tmpData.add(point.y);
        double[] answer = p.countResult(tmpData);

        if (answer[1] > answer[0] && answer[1] > answer[2]) {
            blueDots.addLast(new Point(point.x, point.y));
            dotColor = "BLUE";
        }
        else {
            redDots.addLast(new Point(point.x, point.y));
            dotColor = "RED";
        }
        return dotColor;
    }
}
