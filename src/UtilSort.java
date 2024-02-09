import java.io.*;
import java.util.*;
import java.nio.file.*;

public class UtilSort {
    public static void main(String[] args) {
        List<String> StringData = new ArrayList<>();
        List<Double> FloatData = new ArrayList<>();
        List<Integer> IntData = new ArrayList<>();

        String OutputPath = "";
        String FilePrefix = "";
        boolean BriefStat = false;
        boolean FullStat = false;

        for (int i = 0; i < args.length; i++) {
            if ("-o".equals(args[i])) {
                OutputPath = args[i + 1];
                i++;
            } else if ("-p".equals(args[i])) {
                FilePrefix = args[i + 1];
                i++;
            } else if ("-s".equals(args[i])) {
                BriefStat = true;
            } else if ("-f".equals(args[i])) {
                FullStat = true;
            } else {
                try (Scanner scanner = new Scanner(new File(args[i]))) {
                    while (scanner.hasNext()) {
                        if (scanner.hasNextInt()) {
                            IntData.add(scanner.nextInt());
                        } else if (scanner.hasNextDouble()) {
                            FloatData.add(scanner.nextDouble());
                        } else {
                            String data = scanner.next();
                            try {
                                Double.parseDouble(data);
                                FloatData.add(Double.parseDouble(data));
                            } catch (NumberFormatException e) {
                                StringData.add(data);
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
            }
        }

        try {
            writeToFile(OutputPath, FilePrefix + "String.txt", StringData);
            writeToFile(OutputPath, FilePrefix + "Float.txt", FloatData);
            writeToFile(OutputPath, FilePrefix + "Int.txt", IntData);

            if (BriefStat) {
                System.out.println("======КРАТКАЯ СТАТИСТИКА ДЛЯ СТРОК И ЧИСЕЛ======");
                System.out.println("String: " + StringData.size());
                System.out.println("Float: " + FloatData.size());
                System.out.println("Int: " + IntData.size());
            }

            if (FullStat) {
                if (!StringData.isEmpty()) {
                    displayStringStats(StringData);
                }
                if (!FloatData.isEmpty()) {
                    DisplayStats(FloatData, "Float");
                }
                if (!IntData.isEmpty()) {
                    DisplayStats(IntData, "Int");
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

    private static void writeToFile(String OutputPath, String FileName, List<?> data) throws IOException {
        if (!data.isEmpty()) {
            Path FilePath = Paths.get(OutputPath, FileName);
            try (BufferedWriter writer = Files.newBufferedWriter(FilePath)) {
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

    private static void DisplayStats(List<? extends Number> data, String dataType) {
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