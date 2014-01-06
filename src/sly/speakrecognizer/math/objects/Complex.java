package sly.speakrecognizer.math.objects;

public class Complex {
	public static final Complex ZERO = new Complex(0.0,0.0);
	public static final Complex ONE = new Complex(1.0,0.0);
	public static final Complex ONEi = new Complex(0.0,1.0);
	
	private final double re;
	private final double im;
	
	public Complex(){
		this(0.0,0.0);
	}
	public Complex(double r){
		this(r,0.0);
	}
	public Complex(Complex c){
		this(c.re,c.im);
	}
	public Complex(double r, double i){
		re = r;
		im = i;
	}
	public double getReal(){
		return re;
	}
	public double getImagine(){
		return im;
	}
	public double getMod(){
		return Math.hypot(re, im);
	}
	public double getArg(){
		return Math.atan2(im, re);
	}
    public Complex plus(Complex b) {
        return new Complex(re + b.re, im + b.im);
    }    
    public Complex plus(double b) {
        return new Complex(re + b, im);
    }
    public Complex minus(Complex b) {
        return new Complex(re - b.re, im - b.im);
    }
    public Complex minus(double b) {
        return new Complex(re - b, im);
    }
    public Complex multiply(Complex b) {
        return new Complex(re * b.re - im * b.im,re * b.im + im * b.re);
    }
    public Complex multiply(double b) {
        return new Complex(re * b, im * b);
    }
    public Complex divide(Complex b) {
        return multiply(b.reciprocal());
    }
    public Complex divide(double b) {
        return new Complex(re/b,im/b);
    }
    public Complex conjugate() {
        return new Complex(re, -im);
    }
    public Complex reciprocal() {
        double scale = re*re + im*im;
        if(scale == 0.0){
            return new Complex(re < 0.0 ? Double.NEGATIVE_INFINITY:Double.POSITIVE_INFINITY , 
            		im < 0.0 ? Double.POSITIVE_INFINITY:Double.NEGATIVE_INFINITY);
        }
        return new Complex(re / scale, -im / scale);
    }    
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }
    public Complex tan() {
        return sin().divide(cos());
    }
    public static Complex fromPolar(double magnitude, double angle) {
        return new Complex(magnitude * Math.cos(angle),magnitude * Math.sin(angle));
    }
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(im);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(re);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	public boolean equals(Object obj) {
		Complex other = (Complex) obj;
		return Double.doubleToLongBits(im) == Double.doubleToLongBits(other.im) && 
				Double.doubleToLongBits(re) == Double.doubleToLongBits(other.re);
	}
    public String toString() {
        if (im == 0) return Double.toString(re);
        if (re == 0) return Double.toString(im) + "i";
        if (im <  0) return Double.toString(re) + " - " + Double.toString(-im) + "i";
        return Double.toString(re) + " + " + Double.toString(im) + "i";
    }
}
