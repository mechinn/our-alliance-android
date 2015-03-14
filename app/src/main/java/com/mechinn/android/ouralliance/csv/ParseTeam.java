package com.mechinn.android.ouralliance.csv;

import com.activeandroid.query.Select;
import com.mechinn.android.ouralliance.data.Team;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Created by mechinn on 3/11/15.
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

        Team team = new Select().from(Team.class).where(Team.TEAM_NUMBER+"=?",number).executeSingle();

        if(null==team) {
            team = new Team();
            team.setTeamNumber(number);
            team.save();
        }

        return next.execute(team,context);
    }
}

