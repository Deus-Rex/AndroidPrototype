package test;

import android.test.InstrumentationTestCase;

import com.example.trafficscotlandproto.TrafficItem;

/**
 * Created by ryan on 29/03/16.
 */
public class testTrafficItem extends InstrumentationTestCase {

    TrafficItem test;

    public testTrafficItem() {
        test = new TrafficItem();
    }

    public void testTitle() {
        String[] testTitles = {
                "M77 Motorway Glasgow",
                "M8 Motorway Glasgow",
                "Peat Road",
                "A86 Newtonmore - Drumgask",
                "1",
                "Super long road name that goes on forever"
        };

        for (String title : testTitles) {
            test.setTitle(title);
            assertEquals(test.getTitle(), title);
        }
    }

    String[] testDescriptions = {
            "Start Date: Friday, 01 April 2016 - 00:00<br />End Date: Friday, 22 April 2016 - 00:00<br />Retaining Wall Strengthening- Temporary Traffic Signals in operation.",
            "Start Date: Sunday, 10 April 2016 - 00:00<br />End Date: Sunday, 10 April 2016 - 00:00<br />3 way traffic signals in use For BT works",
            "Start Date: Friday, 01 April 2016 - 00:00<br />End Date: Friday, 01 April 2016 - 00:00<br />BT Maintenance - Temporary Traffic Signals",
            "Start Date: Monday, 04 April 2016 - 00:00<br />End Date: Friday, 08 April 2016 - 00:00<br />Access to Boxes for Cabling - Temp Traffic Signals"
    };

    public void testDescription() {
        ;

        String[] testResultsDesc = {
                "Retaining Wall Strengthening- Temporary Traffic Signals in operation.",
                "3 way traffic signals in use For BT works",
                "BT Maintenance - Temporary Traffic Signals",
                "Access to Boxes for Cabling - Temp Traffic Signals",
        };

        for (int i = 0; i < testDescriptions.length; i++) {
            test.setDescription(testDescriptions[i]);
            assertEquals(test.getDescription(), testResultsDesc[i]);
        }
    }

    public void testStartDate() {
        String[] testResultsStartDate = {
                "2016-04-01",
                "2016-04-10",
                "2016-04-01",
                "2016-04-04"
        };

        for (int i = 0; i < testDescriptions.length; i++) {
            test.setDescription(testDescriptions[i]);
            assertEquals(test.getStartDateString(), testResultsStartDate[i]);
        }
    }

    public void testEndDate() {
        String[] testResultsEndDate = {
                "2016-04-22",
                "2016-04-10",
                "2016-04-01",
                "2016-04-08"
        };

        for (int i = 0; i < testDescriptions.length; i++) {
            test.setDescription(testDescriptions[i]);
            assertEquals(test.getEndDateString(), testResultsEndDate[i]);
        }
    }
}
