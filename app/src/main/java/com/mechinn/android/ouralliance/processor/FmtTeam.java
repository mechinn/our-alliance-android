package com.mechinn.android.ouralliance.processor;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;
import se.emilsjolander.sprinkles.Query;

/**
 * Created by mechinn on 3/9/14.
 */
public class FmtTeam extends CellProcessorAdaptor {

    public FmtTeam() {
        super();
    }

    public FmtTeam(CellProcessor next) {
        // this constructor allows other processors to be chained after ParseDay
        super(next);
    }

    public Object execute(Object value, CsvContext context) {

        validateInputNotNull(value, context);

        if(!(value instanceof Team)) {
            throw new SuperCsvCellProcessorException(Team.class, value, context, this);
        }

        Team team = (Team) value;

        return next.execute(team.getTeamNumber(), context);
    }
}
