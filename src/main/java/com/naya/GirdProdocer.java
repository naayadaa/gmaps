package com.naya;

import com.naya.coords.LatLng;
import com.naya.coords.UTMcoord;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by naayadaa on 15.08.16.
 */
public class GirdProdocer {
    public static final double RAD_2_DEG = (180.0 / Math.PI);
    public static final double DEG_2_RAD = (1.0 / RAD_2_DEG);

    public static final PointLatLngAlt startPointLatLngAlt = new PointLatLngAlt(new LatLng(0., 0.), 0.);

    static void addToMap(LineLatLng pos){
        List<LatLng> latLngList = new ArrayList<>();
        latLngList.add(pos.getP1().toLatLng());
        latLngList.add(pos.getP2().toLatLng());

        //add to Map
    }

    static void addToMap(UTMcoord pos, String tag)
    {
        if (tag.equals("M"));

        /*polygons.Markers.Add(new GMapMarkerWP(pos.ToLLA(), tag));
        map.ZoomAndCenterMarkers("polygons");
        map.Invalidate();*/
    }

    //polar to rectangular
    static double newposX(double x, double bearing, double distance)
    {
        double degN = 90 - bearing;
        if (degN < 0)
            degN += 360;
        return x + distance * Math.cos(degN * DEG_2_RAD);
    }

    static double newposY(double y, double bearing, double distance)
    {
        double degN = 90 - bearing;
        if (degN < 0)
            degN += 360;
        return y + distance * Math.sin(degN * DEG_2_RAD);
    }



    public static List<PointLatLngAlt> createGird(List<PointLatLngAlt> polygon, double altitude, double distance,
                                                  double spacing, double angle, double overshoot1, double overshoot2,
                                                  LatLng startLatLng, boolean shutter, double minLaneSeparation, double leadin){
        if (spacing < 4 && spacing != 0)
            spacing = 4;
        if (distance < 0.1)
            distance = 0.1;
        if (polygon.size() == 0)
            return new ArrayList<>();

        // Make a non round number in case of corner cases
        if (minLaneSeparation != 0)
            minLaneSeparation += 0.5;
        // Lane Separation in meters
        double minLaneSeparationINMeters = minLaneSeparation * distance;

        List<PointLatLngAlt> ans = new ArrayList<>();

        // utm zone distance calcs will be done in
        int utmZone = polygon.get(0).getLatLng().toUTM().getUtmZone();
        String utmLatZone = polygon.get(0).getLatLng().toUTM().getUtmLatZone();

        // utm position list
        List<UTMcoord> utmCoords =
                polygon.stream().map(point -> point.getLatLng().toUTM()).collect(Collectors.toList());
        if(utmCoords.get(0) != utmCoords.get(utmCoords.size())){
            utmCoords.add(utmCoords.get(0));   //make a full loop
        }

        // get mins/maxs of coverage area
        Rect area = getPolyMinMax(utmCoords);

        // get initial grid

        // used to determine the size of the outer grid area
        double diagdist = area.diagDistance();

        // somewhere to store out generated lines
        List<LineLatLng> grid = new ArrayList<>();
        // number of lines we need
        int lines = 0;

        // get start point middle
        double x = area.getMidWidth();
        double y = area.getMidHeight();

        addToMap(new UTMcoord(utmZone, utmLatZone, x, y), "Base");

        // get left extent
        double xb1 = x;
        double yb1 = y;
        // to the left

        xb1 = newposX(newposX(xb1, angle - 90, diagdist / 2 + distance), angle + 180, diagdist / 2 + distance);

        // backwards
        yb1 = newposX(newposX(yb1, angle - 90, diagdist / 2 + distance), angle + 180, diagdist / 2 + distance);

        UTMcoord left = new UTMcoord(utmZone, utmLatZone, xb1, yb1);
        addToMap(left, "left");

        // get right extent
        double xb2 = x;
        double yb2 = y;
        // to the right
        xb2 = newposX(newposX(xb2, angle + 90, diagdist / 2 + distance), angle + 180, diagdist / 2 + distance);
        // backwards
        yb1 = newposX(newposX(yb1, angle + 90, diagdist / 2 + distance), angle + 180, diagdist / 2 + distance);

        UTMcoord right = new UTMcoord(utmZone, utmLatZone, xb2, yb2);
        addToMap(right, "right");

        // set start point to left hand side
        x = xb1;
        y = yb1;

        // draw the outergrid, this is a grid that cover the entire area of the rectangle plus more.
        while (lines < ((diagdist + distance * 2) / distance))
        {
            // copy the start point to generate the end point
            double nx = newposX(x, angle, diagdist + distance*2);
            double ny = newposY(y, angle, diagdist + distance*2);

            LineLatLng line = new LineLatLng(
                    new UTMcoord(utmZone, utmLatZone, x, y),
                    new UTMcoord(utmZone, utmLatZone,x, y),
                    new UTMcoord(utmZone,utmLatZone,x, y));
            grid.add(line);

            // addtomap(line);
            newposX(x, angle + 90, distance);
            newposY(y, angle + 90, distance);
            lines++;
        }

        // find intersections with our polygon

        // store lines that dont have any intersections
        List<LineLatLng> remove = new ArrayList<>();

        int gridno = grid.size();

        for (int a = 0; a < gridno; a++)
        {
            double closestdistance = Double.MAX_VALUE;
            double farestdistance = Double.MIN_VALUE;

            UTMcoord closestpoint = UTMcoord.getZero();
            UTMcoord farestpoint = UTMcoord.getZero();

            // somewhere to store our intersections
            List<UTMcoord> matchs = new ArrayList<>();

            int b = -1;
            int crosses = 0;
            UTMcoord newUTMcoord ;
            for (UTMcoord utmCoord : utmCoords) {
                b++;
                if(b == 0){
                    continue;
                }
                newUTMcoord = findLineIntersection(utmCoords.get(b-1),
                        utmCoords.get(b), grid.get(a).getP1(), grid.get(a).getP2());
                if(!newUTMcoord.isZero()) {
                    crosses++;
                    matchs.add(newUTMcoord);
                    if (closestdistance > grid.get(a).getP1().getDistance(newUTMcoord)) {
                        closestpoint = newUTMcoord;
                        closestdistance = grid.get(a).getP1().getDistance(newUTMcoord);
                    }
                    if (farestdistance < grid.get(a).getP1().getDistance(newUTMcoord)) {
                        farestpoint = newUTMcoord;
                        farestdistance = grid.get(a).getP1().getDistance(newUTMcoord);
                    }
                }
            }
            if (crosses == 0){        //outside our polygon
                if(!pointInPolygon(grid.get(a).getP1(), utmCoords) &&
                        !pointInPolygon(grid.get(a).getP2(), utmCoords)){
                    remove.add(grid.get(a));
                }
            }
            else if (crosses == 1) {  //bad - shodnt happen

            }
            else if (crosses == 2) {  //siple start and finish
                LineLatLng line = new LineLatLng(closestpoint, farestpoint, grid.get(a).getBasepnt());
            }
            else { // multiple intersection
                LineLatLng line = grid.get(a);
                remove.add(line);

                while (matchs.size() > 1){
                    UTMcoord closestpoint1 = findClosestPoint(closestpoint, matchs);
                    matchs.remove(closestpoint1);
                    closestpoint = findClosestPoint(closestpoint1, matchs);
                    matchs.remove(closestpoint);
                    LineLatLng newLine = new LineLatLng(closestpoint1, closestpoint, line.getBasepnt());

                    grid.add(newLine);
                }
            }
        }

        // cleanup and keep only lines that pass though our polygon
        remove.forEach(grid::remove);

        //debug
        remove.forEach(GirdProdocer::addToMap);

        if(grid.size() == 0)
            return ans;

        UTMcoord startUTMcoord = startLatLng.toUTM();

       /* switch (startPosition){
            default:
            case HOME:
                startUTMcoord = UTMcoord.getZero(); //get home from geolacation!!!
                break;
            case BOTTOM_LEFT:
                startUTMcoord = new UTMcoord(utmZone, utmLatZone, area.getLeft(), area.getBottom());
                break;
            case BOTTOM_RIGHT:
                startUTMcoord = new UTMcoord(utmZone, utmLatZone, area.getRight(), area.getTop());
                break;
            case POINT:
                startUTMcoord = startPointLatLngAlt.getLatLng().toUTM();
                break;
        }*/

        // find closest line point to startpos
        LineLatLng closest = findClosestLine(startUTMcoord, grid, 0
                /*Lane separation does not apply to starting point*/, angle);

        UTMcoord lastUTMcoord;
        // get the closes point from the line we picked
        if(closest.getP1().getDistance(startUTMcoord) <
                closest.getP2().getDistance(startUTMcoord)){
            lastUTMcoord = closest.getP1();
        }
        else {
            lastUTMcoord = closest.getP2();
        }

        // S =  start
        // E = end
        // ME = middle end
        // SM = start middle

        while (grid.size() > 0){
            // for each line, check which end of the line is the next closest
            if(closest.getP1().getDistance(lastUTMcoord) <
                    closest.getP2().getDistance(lastUTMcoord)){
                UTMcoord newStart = new UTMcoord(utmZone, utmLatZone,
                        newposX(closest.getP1().getEasting(), angle, -leadin),
                        newposY(closest.getP1().getNorthing(), angle, -leadin));
                addToMap(newStart, "S");
                ans.add(new PointLatLngAlt(newStart));

                addToMap(closest.getP1(), "SM");
                ans.add(new PointLatLngAlt(closest.getP1()));

                if(spacing > 0){
                    int k = (int)(spacing -
                            (closest.getBasepnt().getDistance(closest.getP1()) % spacing));
                    for (int d = k; d < closest.getP1().getDistance(closest.getP2()); d += (int)spacing){
                        double ax = newposX(closest.getP1().getEasting(), angle, d);
                        double ay = newposY(closest.getP1().getNorthing(), angle, d);

                        UTMcoord utmCoord1 = new UTMcoord(utmZone, utmLatZone, ax, ay);
                        addToMap(utmCoord1, "M");
                        ans.add(new PointLatLngAlt(utmCoord1));
                    }
                }

                addToMap(closest.getP2(), "ME");
                ans.add(new PointLatLngAlt(closest.getP2()));

                double ax = newposX(closest.getP2().getEasting(), angle, overshoot1);
                double ay = newposY(closest.getP2().getNorthing(), angle, overshoot1);

                UTMcoord newEnd = new UTMcoord(utmZone, utmLatZone, ax, ay);

                addToMap(newEnd, "E");
                ans.add(new PointLatLngAlt(newEnd));

                lastUTMcoord = closest.getP2();

                grid.remove(closest);
                if(grid.size() == 0){
                    break;
                }

                closest = findClosestLine(newEnd, grid, minLaneSeparationINMeters, angle);
            }
            else {
                UTMcoord newStart = new UTMcoord(utmZone, utmLatZone,
                        newposX(closest.getP2().getEasting(), angle, -leadin),
                        newposY(closest.getP2().getNorthing(), angle, -leadin));
                addToMap(newStart, "S");
                ans.add(new PointLatLngAlt(newStart));

                addToMap(closest.getP2(), "SM");
                ans.add(new PointLatLngAlt(closest.getP2()));

                if(spacing > 0){
                    int k = (int)(spacing -
                            (closest.getBasepnt().getDistance(closest.getP2()) % spacing));
                    for (int d = k; d < closest.getP2().getDistance(closest.getP2()); d += (int)spacing){
                        double ax = newposX(closest.getP2().getEasting(), angle, -d);
                        double ay = newposY(closest.getP2().getNorthing(), angle, -d);

                        UTMcoord utmCoord2 = new UTMcoord(utmZone, utmLatZone, ax, ay);
                        addToMap(utmCoord2, "M");
                        ans.add(new PointLatLngAlt(utmCoord2));
                    }
                }

                addToMap(closest.getP1(), "ME");
                ans.add(new PointLatLngAlt(closest.getP1()));

                double ax = newposX(closest.getP1().getEasting(), angle, - overshoot2);
                double ay = newposY(closest.getP1().getNorthing(), angle, - overshoot2);

                UTMcoord newEnd = new UTMcoord(utmZone, utmLatZone, ax, ay);

                addToMap(newEnd, "E");
                ans.add(new PointLatLngAlt(newEnd));

                lastUTMcoord = closest.getP1();

                grid.remove(closest);
                if(grid.size() == 0){
                    break;
                }
                closest = findClosestLine(newEnd, grid, minLaneSeparationINMeters, angle);

            }
        }

        //set the altitude on all points
        for (PointLatLngAlt point : ans) {
            point.setAltitude(altitude);
        }

        return ans;

    }

    static Rect getPolyMinMax(List<UTMcoord> utmCoords){
        if(utmCoords.size() == 0){
            return new Rect(0, 0, 0 ,0);
        }
        double minx, miny, maxx, maxy;
        minx = maxx = utmCoords.get(0).getEasting();
        miny = maxy = utmCoords.get(0).getNorthing();

        for (UTMcoord utmCoord : utmCoords) {
            minx = Math.min(minx, utmCoord.getEasting());
            maxx = Math.max(maxx, utmCoord.getEasting());

            miny = Math.min(miny, utmCoord.getNorthing());
            maxy = Math.max(maxy, utmCoord.getNorthing());
        }

        return new Rect(minx, maxy, maxx - minx, miny - maxy);
    }

    public static UTMcoord findLineIntersection(UTMcoord start1, UTMcoord end1,
                                                UTMcoord start2, UTMcoord end2){
        double denom = ((end1.getEasting() - start1.getEasting()) * (end2.getNorthing() - start2.getNorthing())) -
                ((end1.getNorthing() - start1.getNorthing()) * (end2.getEasting() - start2.getEasting()));
        //  AB & CD are parallel
        if (denom == 0)
            return UTMcoord.getZero();
        double numer = ((start1.getNorthing() - start2.getNorthing()) * (end2.getEasting() - start2.getEasting()))
                - ((start1.getEasting() - start2.getEasting()) * (end2.getNorthing() - start2.getNorthing()));
        double r = numer / denom;
        double numer2 = ((start1.getNorthing() - start2.getNorthing()) * (end1.getEasting() - start1.getEasting()))
                - ((start1.getEasting() - start2.getEasting()) * (end1.getNorthing() - start1.getNorthing()));
        double s = numer2 / denom;
        if ((r < 0 || r > 1) || (s < 0 || s > 1))
            return UTMcoord.getZero();
        // Find intersection point
        double easting = start1.getEasting() + (r * (end1.getEasting() - start1.getEasting()));
        double northing = start1.getNorthing() + (r * (end1.getNorthing() - start1.getNorthing()));

        return new UTMcoord(start1.getUtmZone(), start1.getUtmLatZone(), easting, northing);

    }

    static UTMcoord findClosestPoint(UTMcoord start, List<UTMcoord> list)
    {
        UTMcoord answer = UTMcoord.getZero();
        double currentbest = Double.MAX_VALUE;

        for (UTMcoord pnt : list) {
            double dist1 = start.getDistance(pnt);

            if (dist1 < currentbest)
            {
                answer = pnt;
                currentbest = dist1;
            }
        }

        return answer;
    }

    // polar to rectangular
    static UTMcoord newpos(UTMcoord input, double bearing, double distance)
    {
        double degN = 90 - bearing;
        if (degN < 0)
            degN += 360;
        double x = input.getEasting() + distance * Math.cos(degN * DEG_2_RAD);
        double y = input.getNorthing() + distance * Math.sin(degN * DEG_2_RAD);

        return new UTMcoord(input.getUtmZone(), input.getUtmLatZone(), x, y);
    }

    // Add an angle while normalizing output in the range 0...360
    static double addAngle(double angle, double degrees)
    {
        angle += degrees;

        angle = angle % 360;

        while (angle < 0)
        {
            angle += 360;
        }
        return angle;
    }

    static LineLatLng findClosestLine(UTMcoord start, List<LineLatLng> list, double minDistance, double angle)
    {
        // By now, just add 5.000 km to our lines so they are long enough to allow intersection
        double METERS_TO_EXTEND = 5000000;

        double perperndicularOrientation = addAngle(angle, 90);

        // Calculation of a perpendicular line to the grid lines containing the "start" point
        UTMcoord start_perpendicular_line = newpos(start, perperndicularOrientation, -METERS_TO_EXTEND);
        UTMcoord stop_perpendicular_line = newpos(start, perperndicularOrientation, METERS_TO_EXTEND);

        // Store one intersection point per grid line
        Map<UTMcoord, LineLatLng> intersectedPoints = new LinkedHashMap<>();
        // lets order distances from every intersected point per line with the "start" point
       Map<Double, UTMcoord> ordered_min_to_max = new LinkedHashMap<>();

        for(LineLatLng line : list){
            // Extend line at both ends so it intersecs for sure with our perpendicular line
            UTMcoord extended_line_start = newpos(line.getP1(), angle, -METERS_TO_EXTEND);
            UTMcoord extended_line_stop = newpos(line.getP2(), angle, METERS_TO_EXTEND);
            // Calculate intersection point
            UTMcoord p = findLineIntersection(extended_line_start, extended_line_stop, start_perpendicular_line, stop_perpendicular_line);

            // Store it
            intersectedPoints.put(p, line);

            // Calculate distances between interesected point and "start" (i.e. line and start)
            double distance_p = start.getDistance(p);
            if (!ordered_min_to_max.containsKey(distance_p))
                ordered_min_to_max.put(distance_p, p);
        }

        // Acquire keys and sort them.
        List<Double> ordered_keys = new ArrayList<>(ordered_min_to_max.keySet());
        Collections.sort(ordered_keys);

        // Lets select a line that is the closest to "start" point but "mindistance" away at least.
        // If we have only one line, return that line whatever the minDistance says
        double key = Double.MAX_VALUE;
        int i = 0;
        while (key == Double.MAX_VALUE && i < ordered_keys.size())
        {
            if (ordered_keys.get(i) >= minDistance)
                key = ordered_keys.get(i);
            i++;
        }

        // If no line is selected (because all of them are closer than minDistance, then get the farest one
        if (key == Double.MAX_VALUE)
            key = ordered_keys.get(ordered_keys.size()-1);

        // return line
        return intersectedPoints.get(ordered_min_to_max.get(key));
    }

    static boolean pointInPolygon(UTMcoord p, List<UTMcoord> poly)
    {
        UTMcoord p1, p2;
        boolean inside = false;

        if (poly.size() < 3)
        {
            return false;
        }
        UTMcoord oldPoint = (poly.get(poly.size() - 1));

        for (UTMcoord newPoint : poly) {

            if (newPoint.getNorthing() > oldPoint.getNorthing()) {
                p1 = oldPoint;
                p2 = newPoint;
            } else {
                p1 = newPoint;
                p2 = oldPoint;
            }

            if ((newPoint.getNorthing() < p.getNorthing()) == (p.getNorthing() <= oldPoint.getNorthing())
                    && (p.getEasting() - p1.getEasting()) * (p2.getNorthing() - p1.getNorthing())
                    < (p2.getEasting() - p1.getEasting()) * (p.getNorthing() - p1.getNorthing())) {
                inside = true;
            }
            oldPoint = newPoint;
        }
        return inside;
    }
}
