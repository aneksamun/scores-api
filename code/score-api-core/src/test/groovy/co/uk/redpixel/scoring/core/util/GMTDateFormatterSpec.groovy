package co.uk.redpixel.scoring.core.util

import spock.lang.Specification
import spock.lang.Unroll

import java.text.SimpleDateFormat

class GMTDateFormatterSpec extends Specification {

    @Unroll
    def 'test GMT date formatter for #source'() {
        given: 'test date'
        def formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        def date = formatter.parse(source)

        when: 'date is formatted to GMT format'
        def actual = GMTDateFormatter.format(date)

        then: 'has expected format'
        actual == expected

        where:
        source                | expected
        "13/06/2018 9:45:12"  | "Wed, 13 Jun 2018 08:45:12 GMT"
        "11/08/2013 10:00:00" | "Sun, 11 Aug 2013 09:00:00 GMT"
    }
}
