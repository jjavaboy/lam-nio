package lam.study.sample;

import lam.study.StudyGson;
import lam.study.sample.dto.CarDto;
import lam.study.sample.mapper.DataMapper;
import lam.study.sample.model.Car;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description: DataSampleTest
 * @author: linanmiao
 * @date: 2018/9/27 23:46
 * @version: 1.0
 */
public class DataSampleTest {

    public static void main(String[] args) throws InterruptedException {
        Car car = new Car();
        car.setCreateTime(new Date());

        CarDto carDto = DataMapper.INSTANCE.dateToString(car);
        System.out.println("carDto:" + StudyGson.GSON.toJson(carDto));

        Date date0 = new Date();
        TimeUnit.SECONDS.sleep(3L);
        Date date1 = new Date();
        List<Date> dates = new ArrayList<Date>();
        dates.add(date0);
        dates.add(date1);
        List<String> dateStrings = DataMapper.INSTANCE.dateListToStringList(dates);
        System.out.println("dateStrings:" + StudyGson.GSON.toJson(dateStrings));
    }
}
