package com.mechinn.android.ouralliance.processor;

import android.util.Log;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.util.CsvContext;
import se.emilsjolander.sprinkles.Query;

/**
 * Created by mechinn on 3/9/14.
 */
public class ParseSeason extends CellProcessorAdaptor {

    public ParseSeason() {
        super();
    }

    public ParseSeason(CellProcessor next) {
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

        Season season = Query.one(Season.class, "select * from "+Season.TAG+" where "+Season.YEAR+"=? LIMIT 1", number).get();

        if(null==season) {
            throw new SuperCsvConstraintViolationException("Invalid season: "+number,context,this);
        }

        return next.execute(season,context);
    }
}
