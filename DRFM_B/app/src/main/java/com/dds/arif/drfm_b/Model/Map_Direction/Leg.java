package com.dds.arif.drfm_b.Model.Map_Direction;

import java.util.List;

/**
 * Created by Arif on 7/12/2017.
 */
public class Leg
{
    public Distance distance;
    public Duration duration;
    public String end_address;
    public EndLocation end_location;
    public String start_address;
    public StartLocation start_location;
    public List<Step> steps;
    public List<Object> traffic_speed_entry;
    public List<Object> via_waypoint;
}
