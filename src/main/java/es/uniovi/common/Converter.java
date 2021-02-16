package es.uniovi.common;

/**
 * @author SatyaSnehith
 * @author https://gist.github.com/SatyaSnehith
 */
public class Converter {

	private Converter() {
	}

	static long kilo = 1000;
	static long mega = kilo * kilo;
	static long giga = mega * kilo;

	public static String getSize(long size) {
		String s = "";
		double kb = (double) size / kilo;
		double mb = kb / kilo;
		double gb = mb / kilo;
		if (size < kilo) {
			s = size + " Bytes";
		} else if (size < mega) {
			s = String.format("%.2f", kb) + " KiB";
		} else if (size < giga) {
			s = String.format("%.2f", mb) + " MiB";
		} else {
			s = String.format("%.2f", gb) + " GiB";
		}
		return s;
	}
}