package es.uniovi.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Punto implements Serializable {

	private static final long serialVersionUID = 7463609167239189416L;

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
