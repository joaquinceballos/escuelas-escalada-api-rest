package es.uniovi.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Punto {

	private double x;

	private double y;

	public Punto() {
		super();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
