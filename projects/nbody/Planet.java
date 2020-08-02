public class Planet {
    private static final double gf = 6.67e-11;
    double xxPos, yyPos, xxVel, yyVel, mass;
    String imgFileName;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) { //Planet Constructor
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) { //Planet Constructor by Copy
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) { // Calculating Distance
        return Math.sqrt((this.xxPos - p.xxPos) * (this.xxPos - p.xxPos) + (this.yyPos - p.yyPos) * (this.yyPos - p.yyPos));
    }

    public double calcForceExertedBy(Planet p) { // Calculating Force Exerted
        return gf * this.mass * p.mass / (this.calcDistance(p) * this.calcDistance(p));
    }

    public double calcForceExertedByX(Planet p) { // Calculating Force Exerted by X
        return this.calcForceExertedBy(p) / this.calcDistance(p) * (p.xxPos - this.xxPos);
    }

    public double calcForceExertedByY(Planet p) { // Calculating Force Exerted by Y
        return this.calcForceExertedBy(p) / this.calcDistance(p) * (p.yyPos - this.yyPos);
    }

    public double calcNetForceExertedByX(Planet[] pa) {  // Calculating Net Force Exerted by X
        double sum = 0;
        for (Planet p : pa) {
            if (this.equals(p)) {
                continue;
            }
            sum += this.calcForceExertedByX(p);
        }
        return sum;
    }

    public double calcNetForceExertedByY(Planet[] pa) { // Calculating Net Force Exerted by Y
        double sum = 0;
        for (Planet p : pa) {
            if (this.equals(p)) {
                continue;
            }
            sum += this.calcForceExertedByY(p);
        }
        return sum;
    }

    public void update(double dt, double fX, double fY) { // Updating Postion and Velocity
        this.xxVel += (fX / mass) * dt;
        this.yyVel += (fY / mass) * dt;
        this.xxPos += (this.xxVel) * dt;
        this.yyPos += (this.yyVel) * dt;
    }

    public void draw() {
        String image = "images/" + imgFileName;
        StdDraw.picture(xxPos, yyPos, image);
    }


}
