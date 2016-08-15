package com.naya;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by naayadaa on 15.08.16.
 */
public class UTMcoord {

    private int utmZone;
    private String utmLatZone;
    private double easting;
    private double northing;

    public UTMcoord(int utmZone, String utmLatZone, double easting, double northing) {
        this.utmZone = utmZone;
        this.utmLatZone = utmLatZone;
        this.easting = easting;
        this.northing = northing;
    }

    private static class Digraphs
    {
        private Map digraph1 = new Hashtable();

        private Map digraph2 = new Hashtable();

        private String[] digraph1Array = { "A", "B", "C", "D", "E", "F", "G", "H",
                "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
                "Y", "Z" };

        private String[] digraph2Array = { "V", "A", "B", "C", "D", "E", "F", "G",
                "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V" };

        public Digraphs()
        {
            digraph1.put(new Integer(1), "A");
            digraph1.put(new Integer(2), "B");
            digraph1.put(new Integer(3), "C");
            digraph1.put(new Integer(4), "D");
            digraph1.put(new Integer(5), "E");
            digraph1.put(new Integer(6), "F");
            digraph1.put(new Integer(7), "G");
            digraph1.put(new Integer(8), "H");
            digraph1.put(new Integer(9), "J");
            digraph1.put(new Integer(10), "K");
            digraph1.put(new Integer(11), "L");
            digraph1.put(new Integer(12), "M");
            digraph1.put(new Integer(13), "N");
            digraph1.put(new Integer(14), "P");
            digraph1.put(new Integer(15), "Q");
            digraph1.put(new Integer(16), "R");
            digraph1.put(new Integer(17), "S");
            digraph1.put(new Integer(18), "T");
            digraph1.put(new Integer(19), "U");
            digraph1.put(new Integer(20), "V");
            digraph1.put(new Integer(21), "W");
            digraph1.put(new Integer(22), "X");
            digraph1.put(new Integer(23), "Y");
            digraph1.put(new Integer(24), "Z");

        }

        public int getDigraph1Index(String letter)
        {
            for (int i = 0; i < digraph1Array.length; i++)
            {
                if (digraph1Array[i].equals(letter))
                {
                    return i + 1;
                }
            }

            return -1;
        }


        public String getDigraph1(int longZone, double easting)
        {
            int a1 = longZone;
            double a2 = 8 * ((a1 - 1) % 3) + 1;

            double a3 = easting;
            double a4 = a2 + ((int) (a3 / 100000)) - 1;
            return (String) digraph1.get(new Integer((int) Math.floor(a4)));
        }

    }

    private static class LatZones
    {
        private char[] letters = { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
                'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Z' };

        private int[] degrees = { -90, -84, -72, -64, -56, -48, -40, -32, -24, -16,
                -8, 0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 84 };

        private char[] negLetters = { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
                'L', 'M' };

        private int[] negDegrees = { -90, -84, -72, -64, -56, -48, -40, -32, -24,
                -16, -8 };

        private char[] posLetters = { 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Z' };

        private int[] posDegrees = { 0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 84 };

        private int arrayLength = 22;

        public LatZones()
        {
        }

        public int getLatZoneDegree(String letter)
        {
            char ltr = letter.charAt(0);
            for (int i = 0; i < arrayLength; i++)
            {
                if (letters[i] == ltr)
                {
                    return degrees[i];
                }
            }
            return -100;
        }

        public String getLatZone(double latitude)
        {
            int latIndex = -2;
            int lat = (int) latitude;

            if (lat >= 0)
            {
                int len = posLetters.length;
                for (int i = 0; i < len; i++)
                {
                    if (lat == posDegrees[i])
                    {
                        latIndex = i;
                        break;
                    }

                    if (lat > posDegrees[i])
                    {
                        continue;
                    }
                    else
                    {
                        latIndex = i - 1;
                        break;
                    }
                }
            }
            else
            {
                int len = negLetters.length;
                for (int i = 0; i < len; i++)
                {
                    if (lat == negDegrees[i])
                    {
                        latIndex = i;
                        break;
                    }

                    if (lat < negDegrees[i])
                    {
                        latIndex = i - 1;
                        break;
                    }

                }

            }

            if (latIndex == -1)
            {
                latIndex = 0;
            }
            if (lat >= 0)
            {
                if (latIndex == -2)
                {
                    latIndex = posLetters.length - 1;
                }
                return String.valueOf(posLetters[latIndex]);
            }
            else
            {
                if (latIndex == -2)
                {
                    latIndex = negLetters.length - 1;
                }
                return String.valueOf(negLetters[latIndex]);

            }
        }

    }


}
