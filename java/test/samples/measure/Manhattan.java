package samples.measure;

import java.lang.*;

import org.junit.Test;
public class Manhattan
{
	@Test
    public void main ()
    {
    int x0=20;
    int y0=30;
    int x1=15;
    int y1=-34;
    //computing Manhattan Distance
    int x=Math.abs(x0-x1);
    int y=Math.abs(y0-y1);
    int z=(x+y);
    //included angle between vectors
    double b=Math.atan(y/z);
    //to find the shortest distance between the coordinates
    double shortest=Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
    //Displaying Manhattan Distance, Shortest Distance, and Coordinates
    System.out.println("("+x0+", "+y0+"), ("+x1+", "+y1+")");
    System.out.println("Manhattan distance is:"+z);
    System.out.println("Shortest distance is:"+shortest);
    System.out.println("Angle:"+b);

    }
}