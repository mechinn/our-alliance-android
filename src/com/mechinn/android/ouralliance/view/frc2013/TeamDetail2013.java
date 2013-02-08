package com.mechinn.android.ouralliance.view.frc2013;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Switch;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;
import com.mechinn.android.ouralliance.data.source.frc2013.TeamScouting2013DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;
import com.mechinn.android.ouralliance.view.TeamDetailFragment;

public class TeamDetail2013 extends TeamDetailFragment<TeamScouting2013, TeamScouting2013DataSource> {
	private static final String TAG = "TeamDetail2013";
	private RadioGroup maxClimb;
	private RadioGroup shooterTypes;
	private RadioGroup continuousShooting;
	private CheckBox lowGoal;
	private CheckBox midGoal;
	private CheckBox highGoal;
	private CheckBox pyramidGoal;
	private CheckBox slot;
	private CheckBox ground;
	private RatingBar reloadSpeed;
	private Switch safeShooter;
	private Switch colorFrisbee;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		View seasonView = inflater.inflate(R.layout.fragment_team_detail_2013, getSeason(), false);
		maxClimb = (RadioGroup) seasonView.findViewById(R.id.team2013climbLevels);
		shooterTypes = (RadioGroup) seasonView.findViewById(R.id.team2013shooterTypes);
		continuousShooting = (RadioGroup) seasonView.findViewById(R.id.team2013continuousShooting);
		lowGoal = (CheckBox) seasonView.findViewById(R.id.team2013lowGoal);
		midGoal = (CheckBox) seasonView.findViewById(R.id.team2013midGoal);
		highGoal = (CheckBox) seasonView.findViewById(R.id.team2013highGoal);
		pyramidGoal = (CheckBox) seasonView.findViewById(R.id.team2013pyramidGoal);
		slot = (CheckBox) seasonView.findViewById(R.id.team2013slot);
		ground = (CheckBox) seasonView.findViewById(R.id.team2013ground);
		reloadSpeed = (RatingBar) seasonView.findViewById(R.id.team2013reloadSpeed);
		safeShooter = (Switch) seasonView.findViewById(R.id.team2013safeShooter);
		colorFrisbee = (Switch) seasonView.findViewById(R.id.team2013scoreColor);
		getSeason().addView(seasonView);
		return rootView;
	}
	
	@Override
	public void setView() {
		super.setView();
		maxClimb.check(getScouting().getMaxClimb());
		shooterTypes.check(getScouting().getShooterType());
		continuousShooting.check(getScouting().getContinuousShooting());
		lowGoal.setChecked(getScouting().isLowGoal());
		midGoal.setChecked(getScouting().isMidGoal());
		highGoal.setChecked(getScouting().isHighGoal());
		pyramidGoal.setChecked(getScouting().isPyramidGoal());
		slot.setChecked(getScouting().isSlot());
		ground.setChecked(getScouting().isGround());
		reloadSpeed.setRating(getScouting().getReloadSpeed());
		safeShooter.setChecked(getScouting().isSafeShooter());
		colorFrisbee.setChecked(getScouting().isColorFrisbee());
	}
	
	@Override
	public void updateScouting() {
		super.updateScouting();
		getScouting().setMaxClimb(maxClimb.getCheckedRadioButtonId());
		getScouting().setShooterType(shooterTypes.getCheckedRadioButtonId());
		getScouting().setContinuousShooting(continuousShooting.getCheckedRadioButtonId());
		getScouting().setLowGoal(lowGoal.isChecked());
		getScouting().setMidGoal(midGoal.isChecked());
		getScouting().setHighGoal(highGoal.isChecked());
		getScouting().setPyramidGoal(pyramidGoal.isChecked());
		getScouting().setSlot(slot.isChecked());
		getScouting().setGround(ground.isChecked());
		getScouting().setReloadSpeed(reloadSpeed.getRating());
		getScouting().setSafeShooter(safeShooter.isChecked());
		getScouting().setColorFrisbee(colorFrisbee.isChecked());
	}
	
	@Override
	public TeamScouting2013 setScoutingFromCursor(Cursor cursor) throws OurAllianceException {
		return TeamScouting2013DataSource.getSingle(cursor);
	}

	@Override
	public TeamScouting2013DataSource createDataSouce() {
		return new TeamScouting2013DataSource(this.getActivity());
	}
}
