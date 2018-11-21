package edu.muict.NetworkBench;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Random;

import junit.framework.TestCase;

public class TestAppSkeleton extends TestCase {
	protected int value1, value2;

	// assigning the values
	protected void setUp() {
		value1 = 3;
		value2 = 3;
	}

	// test run server and client theads
	public void testAdd() {
		double result = value1 + value2;
		assertTrue(result == 6);
	}

	// test generating random data
	public void testGenerateData() {

		String rs = Long.toHexString(Double.doubleToLongBits(Math.random()));
		System.out.println("Generate Data:" + rs);

		Random r = new Random();
		byte[] b = new byte[20000 * 1024 * 1024];
		r.nextBytes(b);
		System.out.println("Random Data: " + (b.length / 1024) + " KB:" + b.toString());
	}

	public void testWriteCSV() {
		PrintWriter pw;
		try {
			pw = new PrintWriter(new File("test.csv"));
			StringBuilder sb = new StringBuilder();
			sb.append("id");
			sb.append(',');
			sb.append("Name");
			sb.append('\n');

			sb.append("1");
			sb.append(',');
			sb.append("Prashant Ghimire");
			sb.append('\n');

			pw.write(sb.toString());
			pw.close();
			System.out.println("done!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
