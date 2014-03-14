package com.mechinn.android.ouralliance.processor;

import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.Team;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Created by mechinn on 3/9/14.
 */
public class FmtSeason extends CellProcessorAdaptor {

    public FmtSeason() {
        super();
    }

    public FmtSeason(CellProcessor next) {
        // this constructor allows other processors to be chained after ParseDay
        super(next);
    }

    public Object execute(Object value, CsvContext context) {

        validateInputNotNull(value, context);

        if(!(value instanceof Season)) {
            throw new SuperCsvCellProcessorException(Season.class, value, context, this);
        }

        Season season = (Season) value;

        return next.execute(season.getYear(), context);
    }
}
