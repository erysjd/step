// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import com.google.sps.Event;
import com.google.sps.Events;
import com.google.sps.TimeRange;
import com.google.sps.MeetingRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

// import Events.java;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> result = new ArrayList<>(); // the return value: just a list of time ranges.
    Collection<String> attendees = request.getAttendees();
    Iterator<Event> itr = events.iterator();

    //noOptionsForTooLongOfARequest()
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()){
        return result;
    }
    // noConflicts(), optionsForNoAttendees()
    if(events.isEmpty()){
        TimeRange range = TimeRange.WHOLE_DAY;
        result.add(range);
    }
    //eventSplitsRestriction()
    else if(events.size() == 1){
        Event event = itr.next();
        Set<String> eventAttendees = new HashSet<>(event.getAttendees());

        if(attendees.containsAll(eventAttendees) == false){
            result.add(TimeRange.WHOLE_DAY);
        }
        else{
            result.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, event.getWhen().start(),false));
            result.add(TimeRange.fromStartEnd(event.getWhen().end(), TimeRange.END_OF_DAY,true));
        }
    }
    else{
        Event event1 = itr.next();
        Set<String> attendees1 = new HashSet<>(event1.getAttendees());
        Event event2 = itr.next();
        Set<String> attendees2 = new HashSet<>(event2.getAttendees());

        if((attendees.containsAll(attendees1) && attendees.containsAll(attendees2)) == false){
            result.add(TimeRange.WHOLE_DAY);
        }

        //nestedEvents(), doubleBookedPeople(), 
        if(event1.getWhen().contains(event2.getWhen())){
            result.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, event1.getWhen().start(),false));
            result.add(TimeRange.fromStartEnd(event1.getWhen().end(), TimeRange.END_OF_DAY,true));

        }
        else if(event2.getWhen().contains(event1.getWhen())){
            result.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, event2.getWhen().start(),false));
            result.add(TimeRange.fromStartEnd(event2.getWhen().end(), TimeRange.END_OF_DAY,true));

        }
        //overlappingEvents()
        else if(event1.getWhen().overlaps(event2.getWhen())){
            result.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, event1.getWhen().start(),false));
            result.add(TimeRange.fromStartEnd(event2.getWhen().end(), TimeRange.END_OF_DAY,true));
        }
        else if(event2.getWhen().overlaps(event1.getWhen())){
            result.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, event2.getWhen().start(),false));
            result.add(TimeRange.fromStartEnd(event1.getWhen().end(), TimeRange.END_OF_DAY,true));
        }
        // justEnoughRoom(), notEnoughRoom()
        else if(event1.getWhen().start() == TimeRange.START_OF_DAY && event2.getWhen().end() == TimeRange.END_OF_DAY + 1){
            TimeRange range = TimeRange.fromStartEnd(event1.getWhen().end(), event2.getWhen().start(),false);
            if(range.duration() >= request.getDuration()){
                result.add(range);
            }
        }
        else if(event1.getWhen().start() > TimeRange.START_OF_DAY && event2.getWhen().start() > TimeRange.START_OF_DAY){
            if(event1.getWhen().end() < TimeRange.END_OF_DAY && event2.getWhen().end() < TimeRange.END_OF_DAY){
                if(event1.getWhen().end() < event2.getWhen().start()){
                    TimeRange before = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, event1.getWhen().start(),false);
                    TimeRange middle = TimeRange.fromStartEnd(event1.getWhen().end(), event2.getWhen().start(),false);
                    TimeRange after = TimeRange.fromStartEnd(event2.getWhen().end(), TimeRange.END_OF_DAY,true);

                    if (before.duration() >= request.getDuration()){
                        result.add(before);
                    }
                    if (middle.duration() >= request.getDuration()){
                        result.add(middle);
                    }
                    if (after.duration() >= request.getDuration()){
                        result.add(after);
                    }
                }
            }
        }
    }

    return result;
  }
}


