package com.mechinn.android.ouralliance.processor;

import com.mechinn.android.ouralliance.data.Team;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;
import se.emilsjolander.sprinkles.Query;

/**
 * Created by mechinn on 3/9/14.
 */
public class ParseTeam extends CellProcessorAdaptor {

    public ParseTeam() {
        super();
    }

    public ParseTeam(CellProcessor next) {
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

        Team team = Query.one(Team.class, "select * from "+Team.TAG+" where "+Team.NUMBER+"=? LIMIT 1", number).get();

        if(null==team) {
            team = new Team(number);
            team.save();
        }

        return next.execute(team,context);
    }
}
