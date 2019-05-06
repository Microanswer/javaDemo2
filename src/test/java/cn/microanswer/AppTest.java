package cn.microanswer;

import org.junit.Test;

import java.io.File;

/**
 * Unit test for simple App.
 */
public class AppTest {


	@Test
	public void testOne() throws Exception {
		Test54 spider1727Com = new Test54(new File("/sdcard/testdown"));

		Object[][] meinvtupians = spider1727Com.getImgList(1, "meinvtupian");

		for (Object[] meinvtupian : meinvtupians) {
			spider1727Com.downloadImg(meinvtupian);
		}
	}
}
