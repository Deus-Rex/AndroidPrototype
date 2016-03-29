package test;

import android.test.InstrumentationTestCase;

import org.joda.time.LocalDate;

/**
 * Created by ryan on 29/03/16.
 */
public class testUtils extends InstrumentationTestCase {
    public testUtils() {

    }

    public void testConvertDate() {
        String dateFormat = "yyyy-MM-dd";

        String[] testDates = {
                "2016-03-29",
                "2016-03-30",
                "2016-03-28",
                "2016-04-29"
        };

        LocalDate[] resultDates = {
                new LocalDate().now(),
                new LocalDate().now().plusDays(1),
                new LocalDate().now().minusDays(1),
                new LocalDate().now().plusMonths(1),
        };

        for (int i = 0; i<testDates.length; i++) {
            assertEquals(resultDates[i].toString(), testDates[i].toString());
        }
    }
}
