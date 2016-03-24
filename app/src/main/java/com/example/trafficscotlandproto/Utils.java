package com.example.trafficscotlandproto;

import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ryan on 15/03/16.
 */
public class Utils {
    // Convert String to LocalDate
    public static LocalDate ConvertDate(String dateInput) {
        LocalDate dateOutput = null;

        try {
            // Parse string using DateFormat to a Date datatype
            DateFormat fmt = DateFormat.getDateInstance(DateFormat.FULL, Locale.UK);
            Date tmpDate = fmt.parse(dateInput);

            // Convert the Date to a LocalDate
            dateOutput = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(tmpDate));
        } catch (ParseException e) {
            e.printStackTrace();
            dateOutput = new LocalDate(); // TODO: Test this to check if the timestamps from the RSS could break this

            DateFormat fmt = DateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.UK);
            Date tmpDate = fmt.parse(dateInput);

            // Convert the Date to a LocalDate
            dateOutput = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(tmpDate));

        } finally {
            return dateOutput;
        }
    }

    public static String brToNewLine(String input) {
        String newLine = "\n"; // Set newline character
        return input
                .replaceAll("(<br\\s*\\/?>)+", newLine) // Regex to replace any type of <br/> with newline
                .replaceAll("[\n]+", newLine)           // Regex to replace repeated newlines with one
                .trim();                                // Remove whitespace before and after string
    }
}
