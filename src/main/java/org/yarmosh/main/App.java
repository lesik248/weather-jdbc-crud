package org.yarmosh.main;

import org.yarmosh.db.JDBCConnectionException;
import org.yarmosh.service.WeatherService;

import java.util.Scanner;

public class App{

    public static void main(String[] args) {

        WeatherService weatherService = new WeatherService();
        try (Scanner myObj = new Scanner(System.in)) {
            String choice;
            System.out.println("Lab3 Weather CLI");
            System.out.println("\"1\" - Вывести сведения о погоде в заданном регионе.");
            System.out.println("\"2\" - Вывести даты, когда в заданном регионе шел снег и температура была ниже заданной отрицательной.");
            System.out.println("\"3\" - Вывести информацию о погоде за прошедшую неделю в регионах, жители которых общаются на заданном языке.");
            System.out.println("\"4\" - Обновить информацию о погоде для заданного региона.");
            System.out.println("\"5\" - Добавить новый регион.");
            System.out.println("\"0\" - Выйти из программы.");
            while (true) {
                System.out.println("Введите команду: ");
                choice = myObj.nextLine();
                switch (choice) {
                    case "1": {
                        System.out.println("Введите название региона: ");
                        String regionName = myObj.nextLine();
                        weatherService.getWeatherForRegion(regionName).forEach(System.out::println);
                    }
                    case "2": {
                        System.out.println("Введите название региона: ");
                        String regionName = myObj.nextLine();
                        weatherService.getWeatherForRegion(regionName);
                    }
                    case "3": {
                        System.out.println("Введите язык: ");
                        String language = myObj.nextLine();
                        weatherService.getWeatherByLanguage(language);
                    }
                    case "4": {
                        System.out.println("Введите название региона: ");
                        String region = myObj.nextLine();
                        System.out.println("Введите дату в формате ГГ-ММ-ДД: ");
                        String date = myObj.nextLine();
                        System.out.println("Введите температуру (в C): ");
                        int temperature = Integer.parseInt(myObj.nextLine());
                        System.out.println("Введите тип осадков: ");
                        String precipitation = myObj.nextLine();
                        weatherService.updateWeatherForRegion(region, date, temperature, precipitation);
                    }
                    case "5": {
                        System.out.println("Введите название региона: ");
                        String region = myObj.nextLine();
                        System.out.println("Введите площадь региона (в км2): ");
                        int square = Integer.parseInt(myObj.nextLine());
                        System.out.println("Введите тип жителей: ");
                        String citizenType = myObj.nextLine();
                        weatherService.createRegion(region, square, citizenType);
                    }
                    case "0": {
                        break;
                    }
                }

            }
        } catch (JDBCConnectionException e) {
            throw new RuntimeException(e);

        }
    }
}