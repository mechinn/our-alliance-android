#!/usr/bin/python2.7

# Copyright (C) 2010 The Android Open Source Project
# 
# Licensed under the Apache License, Version 2.0 (the "License"); you may not
# use this file except in compliance with the License. You may obtain a copy of
# the License at
# 
# http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
# License for the specific language governing permissions and limitations under
# the License.

from google.appengine.ext import db

class TeamScouting(db.Model):
    updated = db.DateTimeProperty(auto_now_add=True)
    status = db.TextProperty()
    
    team = db.IntegerProperty(required=True)
    orientation = db.StringProperty()
    numWheels = db.IntegerProperty()
    deadwheel = db.BooleanProperty()
    wheel1Type = db.StringProperty()
    wheel1Diameter = db.IntegerProperty()
    wheel2Type = db.StringProperty()
    wheel2Diameter = db.IntegerProperty()
    deadWheelType = db.StringProperty()
    turret = db.BooleanProperty()
    tracking = db.BooleanProperty()
    fenderShooter = db.BooleanProperty()
    keyShooter = db.BooleanProperty()
    barrierShooter = db.BooleanProperty()
    climb = db.BooleanProperty()
    notes = db.StringProperty()
    autonomous = db.BooleanProperty()
    CT = db.BooleanProperty()
    avgHoops = db.FloatProperty()
    avgBalance = db.FloatProperty()
    avgBroke = db.FloatProperty()

    @classmethod
    def get_team_info(cls, team):
        if team not in (None, 0):
            query = cls.gql('WHERE team = :1', team)
            return query.get()
        return None

    @classmethod
    def get_team_last_updated(cls, team):
        if team not in (None, 0):
            query = cls.gql('WHERE team = :1', team)
            return query.get().updated
        return None

    @classmethod
    def get_team_id(cls, team):
        if team not in (None, 0):
            query = cls.gql('WHERE team = :1', team)
            return query.get().key().id()
        return None

    @classmethod
    def get_team_status(cls, team):
        if team not in (None, 0):
            query = cls.gql('WHERE team = :1', team)
            return query.get().status
        return None
    
class MatchList(db.Model):
    updated = db.DateTimeProperty(auto_now_add=True)
    status = db.TextProperty()
    
    competition = db.StringProperty(required=True)
    matchNum = db.IntegerProperty()
    matchTime = db.DateTimeProperty()
    red1 = db.IntegerProperty()
    red2 = db.IntegerProperty()
    red3 = db.IntegerProperty()
    blue1 = db.IntegerProperty()
    blue2 = db.IntegerProperty()
    blue3 = db.IntegerProperty()

    @classmethod
    def get_team_info(cls, competition, matchNum):
        if competition not in (None, '') and matchNum not in (None, 0):
            query = cls.gql('WHERE competition = :1 AND matchNum = :2', competition, matchNum)
            return query.get()
        return None

    @classmethod
    def get_team_last_updated(cls, team):
        if team not in (None, ''):
            query = cls.gql('WHERE team = :1', team)
            return query.get().updated
        return None

    @classmethod
    def get_team_id(cls, competition, matchNum):
        if competition not in (None, '') and matchNum not in (None, 0):
            query = cls.gql('WHERE competition = :1 AND matchNum = :2', competition, matchNum)
            return query.get().key().id()
        return None

    @classmethod
    def get_team_status(cls, competition, matchNum):
        if competition not in (None, '') and matchNum not in (None, 0):
            query = cls.gql('WHERE competition = :1 AND matchNum = :2', competition, matchNum)
            return query.get().status
        return None
    
class MatchScouting(db.Model):
    updated = db.DateTimeProperty(auto_now_add=True)
    status = db.TextProperty()
    
    competition = db.StringProperty(required=True)
    matchNum = db.IntegerProperty()
    team = db.IntegerProperty()
    slot = db.StringProperty()
    broke = db.BooleanProperty()
    autonomous = db.BooleanProperty()
    balanced = db.BooleanProperty()
    shooter = db.IntegerProperty()
    top = db.IntegerProperty()
    mid = db.IntegerProperty()
    bot = db.IntegerProperty()
    notes = db.StringProperty()

    @classmethod
    def get_team_info(cls, team, competition, matchNum):
        if team not in (None, 0) and competition not in (None, '') and matchNum not in (None, 0):
            query = cls.gql('WHERE team = :1 AND competition = :2 AND matchNum = :3', team, competition, matchNum)
            return query.get()
        return None

    @classmethod
    def get_team_last_updated(cls, team, competition, matchNum):
        if team not in (None, 0) and competition not in (None, '') and matchNum not in (None, 0):
            query = cls.gql('WHERE team = :1 AND competition = :2 AND matchNum = :3', team, competition, matchNum)
            return query.get().updated
        return None

    @classmethod
    def get_team_id(cls, team, competition, matchNum):
        if team not in (None, 0) and competition not in (None, '') and matchNum not in (None, 0):
            query = cls.gql('WHERE team = :1 AND competition = :2 AND matchNum = :3', team, competition, matchNum)
            return query.get().key().id()
        return None

    @classmethod
    def get_team_status(cls, team, competition, matchNum):
        if team not in (None, 0) and competition not in (None, '') and matchNum not in (None, 0):
            query = cls.gql('WHERE team = :1 AND competition = :2 AND matchNum = :3', team, competition, matchNum)
            return query.get().status
        return None
    
'''
unused so far
'''
class TeamRankings(db.Model):

    updated = db.DateTimeProperty(auto_now_add=True)
    status = db.TextProperty()
    
    competition = db.StringProperty(required=True)
    rank = db.IntegerProperty()
    team = db.IntegerProperty()
    qs = db.FloatProperty()
    hybrid = db.FloatProperty()
    bridge = db.FloatProperty()
    teleop = db.FloatProperty()
    coop = db.IntegerProperty()
    record = db.StringProperty()
    dq = db.IntegerProperty()
    played = db.IntegerProperty()

    @classmethod
    def get_ranking_info(cls, team, competition):
        if team not in (None, 0) and competition not in (None, ''):
            query = cls.gql('WHERE team = :1 AND competition = :2', team, competition)
            return query.get()
        return None

    @classmethod
    def get_ranking_last_updated(cls, team, competition):
        if team not in (None, 0) and competition not in (None, ''):
            query = cls.gql('WHERE team = :1 AND competition = :2', team, competition)
            return query.get().updated
        return None

    @classmethod
    def get_ranking_id(cls, team, competition):
        if team not in (None, 0) and competition not in (None, ''):
            query = cls.gql('WHERE team = :1 AND competition = :2', team, competition)
            return query.get().key().id()
        return None

    @classmethod
    def get_ranking_status(cls, team, competition):
        if team not in (None, 0) and competition not in (None, ''):
            query = cls.gql('WHERE team = :1 AND competition = :2', team, competition)
            return query.get().status
        return None
