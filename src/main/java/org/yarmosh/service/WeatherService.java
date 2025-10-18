package org.yarmosh.service;
import org.yarmosh.dao.DAOException;
import org.yarmosh.dao.DaoCitizenType;
import org.yarmosh.dao.DaoRegion;
import org.yarmosh.dao.DaoWeather;
import org.yarmosh.db.JDBCConnectionException;
import org.yarmosh.model.CitizenType;
import org.yarmosh.model.Region;
import org.yarmosh.model.Weather;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeatherService {
    private final DaoWeather daoWeather;
    private final DaoRegion daoRegion;
    private final DaoCitizenType daoCitizenType;


    public WeatherService() {
        daoWeather = new DaoWeather();
        daoRegion = new DaoRegion();
        daoCitizenType = new DaoCitizenType();
    }

    private int getRegionIdByName(String name) throws JDBCConnectionException, DAOException {
        List<Region> regions = daoRegion.getAll();

        for (Region region : regions) {
            if (region.getName().equalsIgnoreCase(name)) {
                return region.getId();
            }
        }

        throw new DAOException("Region with name '" + name + "' not found in database.");
    }

    public List<Weather> getWeatherForRegion(String regionName) throws JDBCConnectionException {
        int regionId = getRegionIdByName(regionName);

        List<Weather> weathers = daoWeather.getAll();
        List<Weather> result = new ArrayList<>();
        for (Weather weather : weathers) {
            if (weather.getRegion() == regionId) {
                result.add(weather);
            }
        }
        return result;
    }

    public List<String> getRegionSnowyDates(String regionName, int temperature) throws JDBCConnectionException {
        int regionId = getRegionIdByName(regionName);

        List<Weather> weathers = daoWeather.getAll();
        List<String> result = new ArrayList<>();

        for (Weather weather : weathers) {
            if (weather.getRegion() == regionId &&
                    weather.getTemperature() < temperature &&
                    weather.getPrecipitation().equals("снег")) {
                result.add(weather.getDate());
            }
        }
        return result;
    }

    public List<Weather> getWeatherByLanguage(String language) throws JDBCConnectionException {
        List<Region> regions = daoRegion.getAll();
        List<Weather> weathers = daoWeather.getAll();

        List<String> lastWeek = getCurrWeekDays();
        List<Integer> regionIds = new ArrayList<>();
        for (Region region : regions) {
            int citizenTypeId = region.getCitizenType();
            CitizenType citizenType = daoCitizenType.read(citizenTypeId);
            if (citizenType.getLanguage().equalsIgnoreCase(language)) {
                regionIds.add(region.getId());
            }
        }
        List<Weather> result = new ArrayList<>();
        for (Weather weather : weathers) {
            if (lastWeek.contains(weather.getDate()) && regionIds.contains(weather.getRegion())) {
                result.add(weather);
                System.out.println(weather.getPrecipitation());
            }
        }
        return result;
    }
    public static List<String> getCurrWeekDays() {
        List<String> days = new ArrayList<>();
        LocalDate today = LocalDate.now();

        LocalDate monday = today.with(DayOfWeek.MONDAY);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < 7; i++) {
            LocalDate day = monday.plusDays(i);
            days.add(day.format(formatter));
        }

        return days;
    }
    public void updateWeatherForRegion(String regionName, String date, int temperature, String precipitation) throws JDBCConnectionException {
        int regionId = getRegionIdByName(regionName);
        Region region = daoRegion.read(regionId);
        daoWeather.create(new Weather(1, region, date, temperature, precipitation));
        System.out.println("Погода добавлена успешно!");
    }
    public void createRegion(String regionName, int regionSquare, String citizenType) throws JDBCConnectionException {
        List<CitizenType> citizenTypes = daoCitizenType.getAll();

        CitizenType typeForRegion;
        for (CitizenType type : citizenTypes) {
            if (Objects.equals(type.getName(), citizenType)) {
                typeForRegion = daoCitizenType.read(type.getId());
                daoRegion.create(new Region(1, regionName, regionSquare, typeForRegion));
                System.out.println("Регион добавлен успешно!");
            }
        }
    }
}
