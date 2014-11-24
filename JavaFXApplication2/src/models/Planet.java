package models;

import javafx.scene.paint.Color;

public class Planet {
	double NMod1, NMod2, iMod1, iMod2, wMod1, wMod2, aMod1, aMod2, eMod1, eMod2, MMod1, MMod2;
	double N, i, w, a, e, M, E, x, y, r, v, xEclip, yEclip, zEclip;
	int day = 0;
	int daysInYear = 300;
	String name;
        Color firstColor;
        Color secondColor;
        double radius;

	/**
	 * 
	 * @param name Planet name
	 * @param NMod1 Longtitude of ascending node
	 * @param NMod2 Longtitude of ascending node 
	 * @param iMod1 Inclination
	 * @param iMod2 Inclination
	 * @param wMod1 Argument of perihelion
	 * @param wMod2 Argument of perihelion
	 * @param aMod1 Semi-major axis
	 * @param aMod2 Semi-major axis
	 * @param eMod1 Eccentricity
	 * @param eMod2 Eccentricity
	 * @param MMod1 Mean anomaly
	 * @param MMod2 Mean anomaly
	 */
	public Planet(String name, double NMod1, double NMod2, double iMod1, double iMod2, double wMod1, double wMod2, 
			double aMod1, double aMod2, double eMod1, double eMod2, double MMod1, double MMod2){
		this.NMod1 = NMod1;
		this.NMod2 = NMod2;
		this.iMod1 = iMod1;
		this.iMod2 = iMod2;
		this.wMod1 = wMod1;
		this.wMod2 = wMod2;
		this.aMod1 = aMod1;
		this.aMod2 = aMod2;
		this.eMod1 = eMod1;
		this.eMod2 = eMod2;
		this.MMod1 = MMod1;
		this.MMod2 = MMod2;
		calcVariables();		
	}
	
	/**
	 * 
	 * @param name Planet name
         * @param firstColor firstColor of the planet
         * @param secondColor secondColor of the planet
         * @param radius double radius of sphere
	 * @param daysInYear days it takes to fulfill an orbit. Only used when drawing the planet orbit
	 * @param NMod1 Longtitude of ascending node
	 * @param NMod2 Longtitude of ascending node 
	 * @param iMod1 Inclination
	 * @param iMod2 Inclination
	 * @param wMod1 Argument of perihelion
	 * @param wMod2 Argument of perihelion
	 * @param aMod1 Semi-major axis
	 * @param aMod2 Semi-major axis
	 * @param eMod1 Eccentricity
	 * @param eMod2 Eccentricity
	 * @param MMod1 Mean anomaly
	 * @param MMod2 Mean anomaly
	 */
	public Planet(String name, Color firstColor, Color secondColor, double radius, int daysInYear, double NMod1, double NMod2, double iMod1, double iMod2, double wMod1, double wMod2, 
			double aMod1, double aMod2, double eMod1, double eMod2, double MMod1, double MMod2){
		this.NMod1 = NMod1;
		this.NMod2 = NMod2;
		this.iMod1 = iMod1;
		this.iMod2 = iMod2;
		this.wMod1 = wMod1;
		this.wMod2 = wMod2;
		this.aMod1 = aMod1;
		this.aMod2 = aMod2;
		this.eMod1 = eMod1;
		this.eMod2 = eMod2;
		this.MMod1 = MMod1;
		this.MMod2 = MMod2;
                this.firstColor = firstColor;
                this.secondColor = secondColor;
                this.radius = radius;
		this.daysInYear = daysInYear;
		this.name = name;
		calcVariables();		
	}
	
	private void calcVariables(){
		setN(rev(NMod1 + NMod2 * day));
		setSmallI(rev(iMod1 + iMod2 * day));
		setSmallW(rev(wMod1 + wMod2 * day));
		setSmallA(aMod1 + aMod2);
		setSmallE(eMod1 + eMod2 * day);
		setM(rev(MMod1 + MMod2 * day));
		
		double radians = Math.toRadians(M);

		findEDiff(M, true);

		radians = Math.toRadians(E);
		setSmallX(a * (Math.cos(radians) - e));
		
		setSmallY(a * Math.sqrt(1 - e * e) * Math.sin(radians));
		
		setSmallR(Math.sqrt(x*x + y*y));
		
//		setSmallV(Math.atan2(y, x));
		setSmallV(Math.toDegrees(Math.atan2(y, x)));
		
		double radianN = Math.toRadians(N);
		double radianVW = Math.toRadians(v+w);
		double radianI = Math.toRadians(i);
		
	
		setxEclip(r * ( Math.cos(radianN) * Math.cos(radianVW) - Math.sin(radianN) * Math.sin(radianVW) * Math.cos(radianI)));

		setyEclip(r * ( Math.sin(radianN) * Math.cos(radianVW) + Math.cos(radianN) * Math.sin(radianVW) * Math.cos(radianI) ));
		
		setzEclip(r * Math.sin(radianVW) * Math.sin(radianI));
		//	    xeclip = r * ( cos(N) * cos(v+w) - sin(N) * sin(v+w) * cos(i) )
//	    yeclip = r * ( sin(N) * cos(v+w) + cos(N) * sin(v+w) * cos(i) )
//	    zeclip = r * sin(v+w) * sin(i)

		
	}
	
	private void findEDiff(double varM, boolean firstRun){
		double radians = Math.toRadians(varM);
		double varE;
		if(firstRun){
			varE = varM + (180/Math.PI) * e * Math.sin(radians) * (1 + e * Math.cos(radians));
		}
		else{
			varE = varM - (varM - (180/Math.PI) * e * Math.sin(radians) - M) / (1 + e * Math.cos(radians));
		}

		if(firstRun || Math.abs(varE - varM) > 0.005){
			findEDiff(varE, false);
		}
		else {
			setE(varE);
		}
	}
	
    private double rev( double x )
    {
        return  x - Math.floor(x/360.0)*360.0;
    }
	
	public int getDay(){
		return this.day;
	}
	
	public void setDay(int day){
		this.day = day;
		calcVariables();
	}
	
	public double getN() {
		return N;
	}

	private void setN(double n) {
		this.N = n;
	}


	public double getSmallI() {
		return i;
	}


	private void setSmallI(double i) {
		this.i = i;
	}


	public double getSmallW() {
		return w;
	}


	private void setSmallW(double w) {
		this.w = w;
	}


	public double getSmallA() {
		return a;
	}


	private void setSmallA(double a) {
		this.a = a;
	}


	public double getSmallE() {
		return e;
	}


	private void setSmallE(double e) {
		this.e = e;
	}


	public double getM() {
		return M;
	}


	private void setM(double m) {
		this.M = m;
	}


	public double getE() {
		return E;
	}


	private void setE(double E) {
		this.E = E;
	}


	public double getSmallX() {
		return x;
	}


	private void setSmallX(double x) {
		this.x = x;
	}


	public double getSmallY() {
		return y;
	}


	private void setSmallY(double y) {
		this.y = y;
	}


	public double getSmallR() {
		return r;
	}


	private void setSmallR(double r) {
		this.r = r;
	}


	public double getSmallV() {
		return v;
	}


	private void setSmallV(double v) {
		this.v = v;
	}
	
	public double getxEclip() {
		return xEclip;
	}

	private void setxEclip(double xEclip) {
		this.xEclip = xEclip;
	}
	
	public double getxEclipAdjusted(int constant) {
		return (xEclip) + constant;
	}

	public double getyEclip() {
		return yEclip;
	}

	private void setyEclip(double yEclip) {
		this.yEclip = yEclip;
	}
	
	public double getyEclipAdjusted(int constant) {
		return (yEclip) + constant;
	}

	public double getzEclip() {
		return zEclip;
	}

	private void setzEclip(double zEclip) {
		this.zEclip = zEclip;
	}
	
	public double getzEclipAdjusted(int constant) {
		return (zEclip) + constant;
	}
	

	public int getDaysInYear() {
		return daysInYear;
	}

	private void setDaysInYear(int daysInYear) {
		this.daysInYear = daysInYear;
	}
        
        public Color getFirstColor(){
            return firstColor;
        }
        
        public void setFirstColor(Color firstColor){
            this.firstColor = firstColor;
        }
        
        public Color getSecondColor(){
            return secondColor;
        }
        
        public void setSecondColor(Color secondColor){
            this.secondColor = secondColor;
        }
	
	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}
        
        public double getRadius() {
            return radius;
        }

        public void setRadius(double radius) {
            this.radius = radius;
        }       

}

