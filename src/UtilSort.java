import java.io.*;
import java.util.*;
import java.nio.file.*;

public class UtilSort {
    public static void main(String[] args) {
        List<String> stringData = new ArrayList<>();
        List<Double> floatData = new ArrayList<>();
        List<Integer> intData = new ArrayList<>();

        String outputPath = ".";
        String filePrefix = "";
        boolean showSummary = false;
        boolean calculateStats = false;

        for (int i = 0; i < args.length; i++) {
            if ("-o".equals(args[i])) {
                outputPath = args[i + 1];
                i++;
            } else if ("-p".equals(args[i])) {
                filePrefix = args[i + 1];
                i++;
            } else if ("-s".equals(args[i])) {
                showSummary = true;
            } else if ("-f".equals(args[i])) {
                calculateStats = true;
            } else {
                try (Scanner scanner = new Scanner(new File(args[i]))) {
                    while (scanner.hasNext()) {
                        if (scanner.hasNextInt()) {
                            intData.add(scanner.nextInt());
                        } else if (scanner.hasNextDouble()) {
                            floatData.add(scanner.nextDouble());
                        } else {
                            String data = scanner.next();
                            try {
                                Double.parseDouble(data);
                                floatData.add(Double.parseDouble(data));
                            } catch (NumberFormatException e) {
                                stringData.add(data);
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
            }
        }

        try {
            writeToFile(outputPath, filePrefix + "String.txt", stringData);
            writeToFile(outputPath, filePrefix + "Float.txt", floatData);
            writeToFile(outputPath, filePrefix + "Int.txt", intData);

            if (showSummary) {
                System.out.println("======КРАТКАЯ СТАТИСТИКА ДЛЯ СТРОК И ЧИСЕЛ======");
                System.out.println("String: " + stringData.size());
                System.out.println("Float: " + floatData.size());
                System.out.println("Int: " + intData.size());
            }

            if (calculateStats) {
                if (!stringData.isEmpty()) {
                    displayStringStats(stringData);
                }
                if (!floatData.isEmpty()) {
                    displayStats(floatData, "Float");
                }
                if (!intData.isEmpty()) {
                    displayStats(intData, "Int");
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

    private static void writeToFile(String outputPath, String filename, List<?> data) throws IOException {
        if (!data.isEmpty()) {
            Path filePath = Paths.get(outputPath, filename);
            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                for (Object item : data) {
                    writer.write(item.toString());
                    writer.newLine();
                }
            }
        }
    }

    private static void displayStringStats(List<String> strings) {
        if (strings.isEmpty()) {
            System.out.println("Строки отсутствуют.");
            return;
        }

        int shortest = Integer.MAX_VALUE;
        int longest = 0;
        for (String s : strings) {
            int length = s.length();
            if (length < shortest) {
                shortest = length;
            }
            if (length > longest) {
                longest = length;
            }
        }
        System.out.println("======ПОЛНАЯ СТАТИСТИКА ДЛЯ СТРОК======");
        System.out.println("Самая короткая строка: " + shortest);
        System.out.println("Самая длинная строка: " + longest);
        System.out.println("======ПОЛНАЯ СТАТИСТИКА ДЛЯ ЧИСЕЛ======");
    }

    private static void displayStats(List<? extends Number> data, String dataType) {
        if (data.isEmpty()) {
            System.out.println(dataType + " отсутствуют.");
            return;
        }
        double sum = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (Number d : data) {
            double val = d.doubleValue();
            sum += val;
            if (val < min) {
                min = val;
            }
            if (val > max) {
                max = val;
            }
        }
        double average = sum / data.size();
        System.out.println("Минимальное значение " + dataType + ": " + min);
        System.out.println("Максимальное значение " + dataType + ": " + max);
        System.out.println("Сумма " + dataType + ": " + sum);
        System.out.println("Среднее значение " + dataType + ": " + average);
    }
}