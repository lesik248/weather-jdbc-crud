package org.yarmosh.main;

import org.yarmosh.service.WeatherService;
import org.yarmosh.service.WeatherServiceException;

import java.util.Scanner;

public class App{

    public static void main(String[] args) {

        WeatherService weatherService = new WeatherService();
        Scanner myObj = new Scanner(System.in);
        String choice;
        System.out.println("Lab3 Weather CLI");
        while (true) {
            System.out.println("\"1\" - Вывести сведения о погоде в заданном регионе.");
            System.out.println("\"2\" - Вывести даты, когда в заданном регионе шел снег и температура была ниже заданной отрицательной.");
            System.out.println("\"3\" - Вывести информацию о погоде за прошедшую неделю в регионах, жители которых общаются на заданном языке.");
            System.out.println("\"4\" - Обновить информацию о погоде для заданного региона.");
            System.out.println("\"5\" - Добавить новый регион.");
            System.out.println("\"0\" - Выйти из программы.");
            System.out.println("Введите команду: ");
            choice = myObj.nextLine();
            try {
                switch (choice) {
                    case "1": {
                        System.out.println("Введите название региона: ");
                        String regionName = myObj.nextLine();
                        weatherService.getWeatherForRegion(regionName).forEach(System.out::println);
                        break;
                    }
                    case "2": {
                        System.out.println("Введите название региона: ");
                        String regionName = myObj.nextLine();
                        int temperature = 1;
                        while (temperature >= 0) {
                            System.out.println("Введите температуру (<0): ");
                            temperature = Integer.parseInt(myObj.nextLine());
                        }
                        weatherService.getRegionSnowyDates(regionName, temperature).forEach(System.out::println);
                        break;
                    }
                    case "3": {
                        System.out.println("Введите язык: ");
                        String language = myObj.nextLine();
                        language = language.toLowerCase();
                        weatherService.getWeatherByLanguage(language).forEach(System.out::println);
                        break;
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
                        break;
                    }
                    case "5": {
                        System.out.println("Введите название региона: ");
                        String region = myObj.nextLine();
                        System.out.println("Введите площадь региона (в км2): ");
                        int square = Integer.parseInt(myObj.nextLine());
                        System.out.println("Введите тип жителей: ");
                        String citizenType = myObj.nextLine();
                        weatherService.createRegion(region, square, citizenType);
                        break;
                    }
                    case "0": {
                        break;
                    }
                }
            } catch (WeatherServiceException e) {
                System.out.println("⚠️ Ошибка: " + e.getMessage());
            }
        }
    }
}