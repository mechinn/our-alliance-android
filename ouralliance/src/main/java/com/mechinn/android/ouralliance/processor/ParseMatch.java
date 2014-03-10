package com.mechinn.android.ouralliance.processor;

import com.mechinn.android.ouralliance.data.Match;
import com.mechinn.android.ouralliance.data.Team;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;
import se.emilsjolander.sprinkles.Query;

/**
 * Created by mechinn on 3/9/14.
 */
public class ParseMatch extends CellProcessorAdaptor {

    public ParseMatch() {
        super();
    }

    public ParseMatch(CellProcessor next) {
        // this constructor allows other processors to be chained after ParseDay
        super(next);
    }

    public Object execute(Object value, CsvContext context) {

        validateInputNotNull(value, context);

        if(!(value instanceof String)) {
            throw new SuperCsvCellProcessorException(String.class, value, context, this);
        }
        String numString = (String)value;

        int number = Integer.parseInt(numString);

        Match match = Query.one(Match.class, "select * from ? where ?=? LIMIT 1", Match.TAG, Match.NUMBER, number).get();

        if(null==match) {
            match = new Match(number);
        }

        return next.execute(match,context);
    }
}
