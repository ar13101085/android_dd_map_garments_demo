package com.dds.arif.drfm_b.Model;

import java.util.List;

class Northeast
{
    public  double lat;
    public double lng;
}

class Southwest
{
    public double lat;
    public  double lng;
}

class Viewport
{
    public Northeast northeast;
    public Southwest southwest;
}


public class Nearest
{
    public List<Object> html_attributions;
    public String next_page_token;
    public List<Result> results;
    public String status;
}
