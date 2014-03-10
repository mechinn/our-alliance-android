package com.mechinn.android.ouralliance.processor;

import com.mechinn.android.ouralliance.data.Match;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Created by mechinn on 3/9/14.
 */
public class FmtMatch extends CellProcessorAdaptor {

    public FmtMatch() {
        super();
    }

    public FmtMatch(CellProcessor next) {
        // this constructor allows other processors to be chained after ParseDay
        super(next);
    }

    public Object execute(Object value, CsvContext context) {

        validateInputNotNull(value, context);

        if(!(value instanceof Match)) {
            throw new SuperCsvCellProcessorException(Match.class, value, context, this);
        }

        Match match = (Match) value;

        return next.execute(match.getDisplayNum(), context);
    }
}
