public class NBody {

    public static Planet[] readPlanets(String s) {
        In in = new In(s); // In class object

        int num = in.readInt(); // input number of planets
        double radius = in.readDouble(); // input radius
        Planet[] pa = new Planet[num]; // Declaration of Planet Array

        for (int i = 0; i < num; i++) {
            pa[i] = new Planet(0, 0, 0, 0, 0, "");   //Initializing the array of Planets by constructor (avoiding NULL exceptions)
        }

        for (int i = 0; i < num; i++) { //Inputing the variables
            pa[i].xxPos = in.readDouble();
            pa[i].yyPos = in.readDouble();
            pa[i].xxVel = in.readDouble();
            pa[i].yyVel = in.readDouble();
            pa[i].mass = in.readDouble();
            pa[i].imgFileName = in.readString();
        }

        return pa;  //returning planet array
    }

    public static double readRadius(String s) { // Universe Radius
        In in = new In(s);  //In class object
        int num = in.readInt(); // input number of planets
        double radius = in.readDouble(); // input radius
        return radius;
    }

    public static void main(String[] args) {
        double T;
        double dt;
        String filename;
        double radius;
        Planet[] planets;

        T = Double.parseDouble(args[0]);
        dt = Double.parseDouble(args[1]);
        filename = args[2];

        planets = NBody.readPlanets(filename);
        radius = NBody.readRadius(filename);

        StdDraw.enableDoubleBuffering();

        for (int i = 0; i < T; i += dt) {
            double[] xForces = new double[planets.length];
            double[] yForces = new double[planets.length];
            for (int j = 0; j < planets.length; j++) {
                xForces[j] = planets[j].calcNetForceExertedByX(planets);
                yForces[j] = planets[j].calcNetForceExertedByY(planets);
            }
            for (int j = 0; j < planets.length; j++) {
                planets[j].update(dt, xForces[j], yForces[j]);
            }
            StdDraw.setScale(-radius, radius);
            StdDraw.picture(0, 0, "images/starfield.jpg");
            for (Planet p : planets) {
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i += 1) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }

    }

}
